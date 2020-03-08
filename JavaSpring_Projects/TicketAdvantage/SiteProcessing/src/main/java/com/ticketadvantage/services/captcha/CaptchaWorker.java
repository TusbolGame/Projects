package com.ticketadvantage.services.captcha;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.httpclient.HttpClientWrapper;

/**
 * 
 * @author jmiller
 *
 */
public class CaptchaWorker {
	private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(CaptchaWorker.class);
	private HttpClientWrapper httpClient;
	private static final String[][] CAPTCHA_FILES = new String [][] { 
		{ "Captcha0Upper.gif", "0" },
		{ "Captcha1Upper.gif", "1" },
		{ "Captcha2Lower.gif", "2" },
		{ "Captcha2Upper.gif", "2" },
		{ "Captcha3Lower.gif", "3" },
		{ "Captcha4Lower.gif", "4" },
		{ "Captcha5Lower.gif", "5" },
		{ "Captcha6Upper.gif", "6" },
		{ "Captcha7Lower.gif", "7" },
		{ "Captcha8Upper.gif", "8" },
		{ "Captcha9Lower.gif", "9" }
	};

	/**
	 * 
	 * @param client
	 */
	public CaptchaWorker(HttpClientWrapper client) {
		super();
		this.httpClient = client;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HttpClientWrapper httpClient = new HttpClientWrapper();
			httpClient.setupHttpClient("None");
			httpClient.setHost("ttdsportsbook.net");
			CaptchaWorker captchaWorker = new CaptchaWorker(httpClient);
			final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
			// headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
			headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
			headerValuePairs.add(new BasicNameValuePair("Referer", "https://ttdsportsbook.net"));
			headerValuePairs.add(new BasicNameValuePair("Host", "ttdsportsbook.net"));

			httpClient.getSitePage("https://ttdsportsbook.net", null, headerValuePairs);
			httpClient.getSitePage("https://ttdsportsbook.net/Qubic/getcaptcha.asp?" + Math.random(), null, headerValuePairs);
			for (int y = 1; y <= 5; y++) {
				byte[] imagBytes = captchaWorker.getImage("https://ttdsportsbook.net/Qubic/captcha.asp?captchaID=" + y, "https://ttdsportsbook.net/Qubic/getcaptcha.asp?0.47142963478928435");
				for (int x = 0; x < CaptchaWorker.CAPTCHA_FILES.length; x++) {
					String imageName = CAPTCHA_FILES[x][0];
					InputStream in = CaptchaWorker.class.getClassLoader().getResourceAsStream(imageName);
					final ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int n = 0;
					while (-1!=(n=in.read(buf)))
					{
					   out.write(buf, 0, n);
					}
					out.close();
					in.close();
					byte[] captchaFile = out.toByteArray();

					boolean isSame = compareImage(imagBytes, captchaFile);
					if (isSame) {
						System.out.println("Files are the same");
					} else {
						System.out.println("Files are NOT the same");
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param headerValuePairs
	 * @param verifyUrl
	 * @param captchaUrl
	 * @param referer
	 * @return
	 * @throws BatchException
	 */
	public String getCaptcha(List<NameValuePair> headerValuePairs, String verifyUrl,
			String captchaUrl, String referer, String number) throws BatchException {
		LOGGER.info("Entering getCaptcha()");
		LOGGER.debug("verifyUrl: " + verifyUrl);
		LOGGER.debug("captchaUrl: " + captchaUrl);
		LOGGER.debug("referer: " + referer);
		LOGGER.debug("number: " + number);
		String imgText = null;

		try {
			byte[] imagBytes = getImage(captchaUrl, referer);
			imgText = getTextFromImage(imagBytes);
		} catch (Exception e) {
			LOGGER.error(e);
		} 

		LOGGER.info("Exiting getCaptcha()");
		return imgText;
	}

	/**
	 * 
	 * @param headerValuePairs
	 * @param verifyUrl
	 * @param captchaUrl
	 * @param referer
	 * @return
	 * @throws BatchException
	 */
	public String getCaptchaWithStoredImages(List<NameValuePair> headerValuePairs, String verifyUrl,
			String captchaUrl, String referer, String number) throws BatchException {
		LOGGER.info("Entering getCaptcha()");
		LOGGER.debug("verifyUrl: " + verifyUrl);
		LOGGER.debug("captchaUrl: " + captchaUrl);
		LOGGER.debug("referer: " + referer);
		String imgText = null;

		try {
			// Get the image
			byte[] imagBytes = getImage(captchaUrl, referer);
			for (int x = 0; x < CaptchaWorker.CAPTCHA_FILES.length; x++) {
				String imageName = CAPTCHA_FILES[x][0];
				InputStream in = CaptchaWorker.class.getClassLoader().getResourceAsStream(imageName);
				if (in != null) {
					final ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int n = 0;
					while (-1 != (n=in.read(buf)))
					{
					   out.write(buf, 0, n);
					}
					
					final byte[] captchaFile = out.toByteArray();
					out.close();
					LOGGER.debug("imagBytes: " + imagBytes);
					LOGGER.debug("captchaFile: " + captchaFile);
					boolean isSame = compareImage(imagBytes, captchaFile);
					if (isSame) {
						imgText = CAPTCHA_FILES[x][1];
					}
				} else {
					System.exit(0);
				}

				if (in != null) {
					in.close();
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting getCaptcha()");
		return imgText;
	}

	/**
	 * 
	 * @param xhtmlData
	 * @param postUrl
	 * @param postName
	 * @param headerValuePairs
	 * @param postValuePairs
	 * @param verifyUrl
	 * @param captchaUrl
	 * @return
	 * @throws BatchException
	 */
	public String processCaptcha(String xhtmlData, String postUrl, String postName,
			List<NameValuePair> headerValuePairs, List<NameValuePair> postValuePairs, String verifyUrl,
			String captchaUrl, String referer) throws BatchException {
		LOGGER.info("Entering processCaptcha()");
		LOGGER.debug("xhtmlData: " + xhtmlData);
		LOGGER.debug("postUrl: " + postUrl);
		LOGGER.debug("postName: " + postName);
		LOGGER.debug("headerValuePairs: " + headerValuePairs);
		LOGGER.debug("postValuePairs: " + postValuePairs);
		LOGGER.debug("verifyUrl: " + verifyUrl);
		LOGGER.debug("captchaUrl: " + captchaUrl);
		String xhtml = null;

		// First get the verify captcha info
		List<NameValuePair> retValue = httpClient.getSitePage(verifyUrl, null, headerValuePairs);
		LOGGER.debug("retValue: " + retValue);

		try {
			byte[] imagBytes = getImage(captchaUrl, referer);
			String imgText = getTextFromImage(imagBytes);

			// Do we have at least 6 characters?
			if (imgText != null && imgText.length() != 6) {
				// try again
				imagBytes = getImage(captchaUrl, referer);
				imgText = getTextFromImage(imagBytes);
				if (imgText != null && imgText.length() != 6) {
					// throw an exception
					throw new BatchException(BatchErrorCodes.CAPTCHA_PROCESSING_EXCEPTION,
							BatchErrorMessage.CAPTCHA_PROCESSING_EXCEPTION, "The captcha text is: " + imgText);
				}
			}

			headerValuePairs = new ArrayList<NameValuePair>(1);
			headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
			headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
			headerValuePairs.add(new BasicNameValuePair("Referer", referer));
			postValuePairs.add(new BasicNameValuePair(postName, imgText));
			retValue = httpClient.postSitePage(postUrl, null, postValuePairs, headerValuePairs);
			xhtml = httpClient.getCookiesAndXhtml(retValue);
			final String redirect = httpClient.getRedirectLocation(retValue);
			if (redirect != null && redirect.length() > 0) {
				xhtml = redirect;
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}

		LOGGER.info("Entering processCaptcha()");
		return xhtml;
	}

	/**
	 * 
	 * @param url
	 * @param fileName
	 * @param referer
	 * @throws BatchException
	 */
	public byte[] getImage(String url, String referer) throws BatchException {
		LOGGER.info("Entering getImage()");
		LOGGER.debug("url: " + url);
		LOGGER.debug("referer: " + referer);
		byte[] imagBytes = null;

		// Get the image
		final HttpGet get = new HttpGet(url);

		// Set the get header values
		get.addHeader("Accept", "*/*");
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Accept-Language", "en-US,en;q=0.5");
		get.addHeader("Connection", "keep-alive");
		String host = httpClient.getHost().replace("https://", "").replace("http://", "").replace("/", "");
		LOGGER.debug("getHost(): " + host);
		get.addHeader("Host", host);
		get.addHeader("Proxy-Authorization", "Basic dGlja2V0YWR2YW50YWdlOnBhc3QzeXdpbnRlcg==");
		get.addHeader("Referer", referer);
		get.addHeader("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0");

		try {
			final HttpResponse response = httpClient.getClient().execute(get);
			final HttpEntity entity = response.getEntity();
			final InputStream in = entity.getContent();
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1!=(n=in.read(buf)))
			{
			   out.write(buf, 0, n);
			}
			out.close();
			in.close();
			imagBytes = out.toByteArray();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Exiting getImage()");
		return imagBytes;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	protected String getTextFromImage(byte[] imagBytes) {
		LOGGER.info("Entering getTextFromImage()");
		String imgText = null;

		Map<String, String> map = APIMain.getImageInfo(imagBytes);
		if (map.get("status").equals("0")) {
			imgText = map.get("text");
		} else if (map.get("status").equals("-7")) {
			// Try it again since we had a connection issue
			map = APIMain.getImageInfo(imagBytes);
			if (map.get("status").equals("0")) {
				imgText = map.get("text");
			}
		}

		LOGGER.info("Exiting getTextFromImage()");
		return imgText;
	}

	/**
	 * 
	 * @param fileA
	 * @param fileB
	 * @return
	 */
	public static boolean compareImage(byte[] fileA, byte[] fileB) {        
	    try {
	        // compare data-buffer objects
			LOGGER.debug("fileA.length: " + fileA.length);
			LOGGER.debug("fileB.length: " + fileB.length);
	    		if (fileA.length == fileB.length) {
	    			for(int i=0; i < fileA.length; i++) {
	    				LOGGER.debug("fileA[i]: " + fileA[i]);
	    				LOGGER.debug("fileB[i]: " + fileB[i]);
	    				if (fileA[i] != fileB[i]) {
	                    return false;
	                }
	            }
	            return true;
	        }
	        else {
	            return false;
	        }
	    } 
	    catch (Exception e) { 
	        LOGGER.error("Failed to compare image files ...");
	        return  false;
	    }
	}
}
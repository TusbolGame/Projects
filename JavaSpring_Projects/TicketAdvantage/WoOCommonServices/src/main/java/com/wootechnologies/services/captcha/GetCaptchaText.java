package com.wootechnologies.services.captcha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

import com.wootechnologies.errorhandling.BatchException;

/**
 * 
 * @author jmiller
 *
 */
public class GetCaptchaText implements Callable <Map<String, String>> {
	private static final Logger LOGGER = Logger.getLogger(GetCaptchaText.class);
	private String verifyUrl;
	private String captchaUrl;
	private String referer;
	private String number;
	private CaptchaWorker captchaWorker;

	/**
	 * 
	 * @param verifyUrl
	 * @param captchaUrl
	 * @param referer
	 * @param number
	 * @param captchaWorker
	 */
	public GetCaptchaText(String verifyUrl, String captchaUrl, String referer, String number, CaptchaWorker captchaWorker) {
		this.verifyUrl = verifyUrl;
		this.captchaUrl = captchaUrl;
		this.referer = referer;
		this.number = number;
		this.captchaWorker = captchaWorker;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Map<String, String> call() {
		Map<String, String> map = new HashMap<String, String>();
		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>();

		try {
			LOGGER.debug("verifyUrl: " + verifyUrl);
			LOGGER.debug("captchaUrl: " + captchaUrl);
			LOGGER.debug("referer: " + referer);
			String imageText = captchaWorker.getCaptchaWithStoredImages(headerValuePairs, verifyUrl, captchaUrl, referer, number);
			LOGGER.debug("number: " + number);
			LOGGER.debug("imageText: " + imageText);
			map.put(number, imageText);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		return map;
	}
}
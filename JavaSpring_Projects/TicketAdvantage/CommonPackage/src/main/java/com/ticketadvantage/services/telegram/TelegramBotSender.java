package com.ticketadvantage.services.telegram;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * 
 * @author jmiller
 *
 */
public class TelegramBotSender {
	private static final Logger LOGGER = Logger.getLogger(TelegramBotSender.class);
	private static final String API_TOKEN = "909291353:AAGfS9867WutcaHYlHzF9fmBlPN_r5IXumg"; // apiToken of your bot created by botFather

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 3 ml Washington Capitals +130 +130
		TelegramBotSender.sendToTelegram("-367692212", "3 ml Washington Capitals +130 +130");
	}

	/**
	 * 
	 * @param chatId
	 * @param text
	 */
    public static void sendToTelegram(String chatId, String text) {
    	LOGGER.info("Entering sendToTelegram()");

    	if (chatId != null && chatId.length() > 0 && text != null && text.length() > 0) {
	        try {
	            text = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
	        } catch (UnsupportedEncodingException ex) {
	            throw new RuntimeException(ex.getCause());
	        }

    		if (chatId.contains(",")) {
    			final StringTokenizer st = new StringTokenizer(chatId, ",");

    			while (st.hasMoreTokens()) {
    				final String chatNum = st.nextToken().trim();
    				LOGGER.info("chatNum: " + chatNum);
    		        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    		        urlString = String.format(urlString, API_TOKEN, chatNum, text);

    		        try {
    		            final URL url = new URL(urlString);
    		            final URLConnection conn = url.openConnection();
    		            InputStream is = new BufferedInputStream(conn.getInputStream());
    		        } catch (IOException e) {
    		            e.printStackTrace();
    		        }
    			}
    		} else {
    			LOGGER.info("chatId: " + chatId);
		        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
		        urlString = String.format(urlString, API_TOKEN, chatId, text);
		
		        try {
		            final URL url = new URL(urlString);
		            final URLConnection conn = url.openConnection();
		            InputStream is = new BufferedInputStream(conn.getInputStream());
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
    		}
    	}

    	LOGGER.info("Exiting sendToTelegram()");
    }
}
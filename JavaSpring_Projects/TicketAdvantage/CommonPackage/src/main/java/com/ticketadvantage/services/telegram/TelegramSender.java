package com.ticketadvantage.services.telegram;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TelegramSender {
	// TODO: Replace the following with your instance ID, Premium Client ID and
	// Secret:
	private static final String INSTANCE_ID = "2";
	private static final String CLIENT_ID = "felipe.berdes@gmail.com";
	private static final String CLIENT_SECRET = "5a0ffd53848b49ecbd92a8d39739db3d";
	private static final String GATEWAY_URL = "http://api.whatsmate.net/v1/telegram/single/message/" + INSTANCE_ID;

	/**
	 * Sends out a Telegram message via WhatsMate Telegram Gateway.
	 */
	public static void sendMessage(String number, String message) throws Exception {
		// TODO: Should have used a 3rd party library to make a JSON string from an
		// object
		String jsonPayload = new StringBuilder().append("{").append("\"number\":\"").append(number).append("\",")
				.append("\"message\":\"").append(message).append("\"").append("}").toString();

		URL url = new URL(GATEWAY_URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
		conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
		conn.setRequestProperty("Content-Type", "application/json");

		OutputStream os = conn.getOutputStream();
		os.write(jsonPayload.getBytes());
		os.flush();
		os.close();

		int statusCode = conn.getResponseCode();
		BufferedReader br = new BufferedReader(new InputStreamReader((statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()));
		String output = null;

		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
	}
}
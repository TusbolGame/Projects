/*
 * 
 */
package com.wootechnologies.email;

import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.mail.Session;

import org.apache.log4j.Logger;

import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.smtp.SMTPTransport;

/**
 * Performs OAuth2 authentication.
 *
 * <p>
 * Before using this class, you must call {@code initialize} to install the
 * OAuth2 SASL provider.
 */
public class OAuth2Authenticator {
	private static final Logger LOGGER = Logger.getLogger(OAuth2Authenticator.class);
	private Session session = null;

	public final class OAuth2Provider extends Provider {
		private static final long serialVersionUID = 1L;

		public OAuth2Provider() {
			super("Google OAuth2 Provider", 1.0, "Provides the XOAUTH2 SASL Mechanism");
			put("SaslClientFactory.XOAUTH2", "com.ticketadvantage.services.email.OAuth2SaslClientFactory");
		}
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Installs the OAuth2 SASL provider. This must be called exactly once
	 * before calling other methods on this class.
	 */
	public void initialize() {
		LOGGER.info("Entering initialize()");

		Security.addProvider(new OAuth2Provider());
		
		LOGGER.info("Exiting initialize()");
	}

	/**
	 * Connects and authenticates to an IMAP server with OAuth2. You must have
	 * called {@code initialize}.
	 *
	 * @param host
	 *            Hostname of the imap server, for example {@code
	 *     imap.googlemail.com}.
	 * @param port
	 *            Port of the imap server, for example 993.
	 * @param userEmail
	 *            Email address of the user to authenticate, for example
	 *            {@code oauth@gmail.com}.
	 * @param oauthToken
	 *            The user's OAuth token.
	 * @param debug
	 *            Whether to enable debug logging on the IMAP connection.
	 *
	 * @return An authenticated IMAPStore that can be used for IMAP operations.
	 */
	public synchronized IMAPStore connectToImap(String host, int port, String userEmail, String oauthToken, boolean debug)
			throws Exception {
		LOGGER.info("Entering connectToImap()");
		LOGGER.debug("host: " + host);
		LOGGER.debug("port: " + port);
		LOGGER.debug("userEmail: " + userEmail);
		LOGGER.debug("oauthToken: " + oauthToken);
		IMAPSSLStore store = null;

		Properties PROPERTIES = System.getProperties();
		PROPERTIES.put("mail.imaps.sasl.enable", "true");
		PROPERTIES.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		PROPERTIES.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);
		session = Session.getInstance(PROPERTIES);
		session.setDebug(debug);

		store = new IMAPSSLStore(session, null);
		store.connect(host, port, userEmail, "");

		LOGGER.info("Exiting connectToImap()");
		return store;
	}

	/**
	 * Connects and authenticates to an SMTP server with OAuth2. You must have
	 * called {@code initialize}.
	 *
	 * @param host
	 *            Hostname of the smtp server, for example {@code
	 *     smtp.googlemail.com}.
	 * @param port
	 *            Port of the smtp server, for example 587.
	 * @param userEmail
	 *            Email address of the user to authenticate, for example
	 *            {@code oauth@gmail.com}.
	 * @param oauthToken
	 *            The user's OAuth token.
	 * @param debug
	 *            Whether to enable debug logging on the connection.
	 *
	 * @return An authenticated SMTPTransport that can be used for SMTP
	 *         operations.
	 */
	public synchronized SMTPTransport connectToSmtp(String host, int port, String userEmail, String oauthToken, boolean debug)
			throws Exception {
		LOGGER.info("Entering connectToSmtp()");
		LOGGER.debug("host: " + host);
		LOGGER.debug("port: " + port);
		LOGGER.debug("userEmail: " + userEmail);
		LOGGER.debug("oauthToken: " + oauthToken);

		// Setup mail server
		Properties PROPERTIES = System.getProperties();
		PROPERTIES.put("mail.smtp.starttls.enable", "true");
		PROPERTIES.put("mail.smtp.starttls.required", "true");
		PROPERTIES.put("mail.smtp.sasl.enable", "true");
		PROPERTIES.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		PROPERTIES.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);

		session = Session.getInstance(PROPERTIES);
		session.setDebug(debug);

		final SMTPTransport transport = new SMTPTransport(session, null);
		// If the password is non-null, SMTP tries to do AUTH LOGIN.
		transport.connect(host, port, userEmail, "");
		LOGGER.debug("past connect()");

		LOGGER.info("Exiting connectToSmtp()");
		return transport;
	}

	/**
	 * Authenticates to IMAP with parameters passed in on the commandline.
	 */
	public static void main(String args[]) throws Exception {
		final OAuth2Authenticator oAuth2Authenticator = new OAuth2Authenticator();
		oAuth2Authenticator.initialize();

//		final IMAPStore imapStore = oAuth2Authenticator.connectToImap("imap.gmail.com", 993, "shootfromanywhere@gmail.com", "ya29.Gl2SBS-2kyX36ddxYMqzSAY91YsXiOy338WvQdNdkQvcRkh6aCt3Jv4MF9XAaL6WeGkKn8N4pDKmm0PIARHxUtmUzVOa04v6KE8weE22Ae5keKh-5tIQe96sJOUKCgo", true);
		final IMAPStore imapStore = oAuth2Authenticator.connectToImap("imap.gmail.com", 993, "ticketadvantage@gmail.com", "ya29.Gl1WBqTekspUaa0qKM5lbVYGEZ4SV5XThzYoGCXZIyuFZR9tz8I_XnRewghb6oCMVWqEZDq9enlLDIYyIhmYumEys9fEBoUCLgwW6nT7wLY6FAREVKKnA9B6ZhWgHyI", true);
//		final SMTPTransport smtpTransport = oAuth2Authenticator.connectToSmtp("smtp.gmail.com", 587, "ticketadvantage@gmail.com", "ya29.GlzTBBwkYB-HcusRNlLjQIrbXq9u55E8pPbYhEMTFBPJPFFeNPjui7IvnUeTpF-dWsUYb11mg-E9eshOF-HMK1X17icia9vO3bgzxCkC2qE-Im_9-Dh5tIKV4A-CAQ", true);
	}
}

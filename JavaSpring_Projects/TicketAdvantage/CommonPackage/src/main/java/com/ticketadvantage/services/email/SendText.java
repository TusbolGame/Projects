package com.ticketadvantage.services.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.sun.mail.smtp.SMTPTransport;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.util.ServerInfo;

public class SendText {
	private static final Logger LOGGER = Logger.getLogger(SendText.class);
	// Get system properties
	private String HOST = "smtp.gmail.com";
	private Integer PORT = new Integer(587);
	private String EMAIL_ADDRESS = "ticketadvantage@gmail.com";
	private String OAUTH2_TOKEN = "ya29.GlsmBcIPgVmLkVEhA7jJ5QTqy1V5j89nQggT48ccUYlRS99w4GC5JdaM15fdySWGHJ9F5zh2IbMSRh-uMJfG3A2SBvjCCUNDIPTQONlu2YN8VCEVV2yqfuPtTSwQ";
	private AccessTokenObject accessTokenObject;

	public static void main(String[] args) {
		try {
			final SendText sendText = new SendText();
			// sendText.sendTextWithMessage("9132195234@vtext.com", "this is a test");
			final String url = "http://" + ServerInfo.getIp() + ":" + ServerInfo.getAppPort() + "/captchaprocessing/viewcaptcha.jsp?id=457";
			final TicketAdvantageGmailOath ticketAdvantageGmailOath = new TicketAdvantageGmailOath();
			sendText.setOAUTH2_TOKEN(ticketAdvantageGmailOath.getAccessToken());
			sendText.sendTextCaptureMessage("6467085712@txt.att.net", url);
		} catch (BatchException be) {
			be.printStackTrace();
		}
	}

	/**
	 * @return the hOST
	 */
	public String getHOST() {
		return HOST;
	}

	/**
	 * @param hOST the hOST to set
	 */
	public void setHOST(String hOST) {
		HOST = hOST;
	}

	/**
	 * @return the pORT
	 */
	public Integer getPORT() {
		return PORT;
	}

	/**
	 * @param pORT the pORT to set
	 */
	public void setPORT(Integer pORT) {
		PORT = pORT;
	}

	/**
	 * @return the eMAIL_ADDRESS
	 */
	public String getEMAIL_ADDRESS() {
		return EMAIL_ADDRESS;
	}

	/**
	 * @param eMAIL_ADDRESS the eMAIL_ADDRESS to set
	 */
	public void setEMAIL_ADDRESS(String eMAIL_ADDRESS) {
		EMAIL_ADDRESS = eMAIL_ADDRESS;
	}

	/**
	 * @return the oAUTH2_TOKEN
	 */
	public String getOAUTH2_TOKEN() {
		return OAUTH2_TOKEN;
	}

	/**
	 * @param oAUTH2_TOKEN the oAUTH2_TOKEN to set
	 */
	public void setOAUTH2_TOKEN(String oAUTH2_TOKEN) {
		OAUTH2_TOKEN = oAUTH2_TOKEN;
	}

	/**
	 * @return the accessTokenObject
	 */
	public AccessTokenObject getAccessTokenObject() {
		return accessTokenObject;
	}

	/**
	 * @param accessTokenObject the accessTokenObject to set
	 */
	public void setAccessTokenObject(AccessTokenObject accessTokenObject) {
		this.accessTokenObject = accessTokenObject;
	}

	/**
	 * 
	 * @param toAddress
	 * @throws BatchException
	 */
	public void sendText(String toAddress) throws BatchException {
		LOGGER.info("Entering sendText()");

		try {
			OAuth2Authenticator oAuth2Authenticator = new OAuth2Authenticator();
			oAuth2Authenticator.initialize();
			SMTPTransport smtpTransport = oAuth2Authenticator.connectToSmtp(HOST, PORT, EMAIL_ADDRESS, OAUTH2_TOKEN, false);

			final Message message = new MimeMessage(oAuth2Authenticator.getSession());
			message.setFrom(new InternetAddress("ticketadvantage@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress + ", 9132195234@vtext.com"));
			message.setSubject("Check");
			message.setText("Check");

			smtpTransport.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException me) {
			LOGGER.error(me.getMessage(), me);
			throw new BatchException(BatchErrorCodes.ERROR_SENDING_EMAIL, BatchErrorMessage.ERROR_SENDING_EMAIL, me.getMessage());
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			throw new BatchException(BatchErrorCodes.ERROR_SENDING_EMAIL, BatchErrorMessage.ERROR_SENDING_EMAIL, t.getMessage());
		}

		LOGGER.info("Exiting sendText()");
	}

	/**
	 * 
	 * @param toAddress
	 * @param messageBody
	 * @throws BatchException
	 */
	public void sendTextWithMessage(String toAddress, String messageBody) throws BatchException {
		LOGGER.info("Entering sendTextWithMessage()");

		try {
			OAuth2Authenticator oAuth2Authenticator = new OAuth2Authenticator();
			oAuth2Authenticator.initialize();
			SMTPTransport smtpTransport = oAuth2Authenticator.connectToSmtp(HOST, PORT, EMAIL_ADDRESS, OAUTH2_TOKEN, false);

			final Message message = new MimeMessage(oAuth2Authenticator.getSession());
			message.setFrom(new InternetAddress("ticketadvantage@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress + ", 9132195234@vtext.com"));
			message.setSubject(messageBody);
			message.setText(messageBody);

			// Send the message
			smtpTransport.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException me) {
			LOGGER.error(me.getMessage(), me);
			throw new BatchException(BatchErrorCodes.ERROR_SENDING_EMAIL, BatchErrorMessage.ERROR_SENDING_EMAIL, me.getMessage());
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			throw new BatchException(BatchErrorCodes.ERROR_SENDING_EMAIL, BatchErrorMessage.ERROR_SENDING_EMAIL, t.getMessage());
		}
		
		LOGGER.info("Exiting sendTextWithMessage()");
	}

	/**
	 * 
	 * @param textNumber
	 * @param messageBody
	 * @throws BatchException
	 */
	public void sendTextCaptureMessage(String textNumber, String messageBody) throws BatchException {
		LOGGER.info("Entering sendTextCaptureMessage()");

		try {
			OAuth2Authenticator oAuth2Authenticator = new OAuth2Authenticator();
			oAuth2Authenticator.initialize();
			SMTPTransport smtpTransport = oAuth2Authenticator.connectToSmtp(HOST, PORT, EMAIL_ADDRESS, OAUTH2_TOKEN, false);

			final Message message = new MimeMessage(oAuth2Authenticator.getSession());
			message.setFrom(new InternetAddress("ticketadvantage@gmail.com"));
			if (textNumber != null && textNumber.length() > 0) {
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(textNumber + ", 9132195234@vtext.com, 9134753465@vtext.com, 9137496961@txt.att.net"));
			} else {
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("9132195234@vtext.com, 9134753465@vtext.com, 9137496961@txt.att.net"));
			}
			message.setSubject(messageBody);
			message.setText(messageBody);

			// Send the message
			smtpTransport.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException me) {
			LOGGER.error(me.getMessage(), me);
			throw new BatchException(BatchErrorCodes.ERROR_SENDING_EMAIL, BatchErrorMessage.ERROR_SENDING_EMAIL, me.getMessage());
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			throw new BatchException(BatchErrorCodes.ERROR_SENDING_EMAIL, BatchErrorMessage.ERROR_SENDING_EMAIL, t.getMessage());
		}
		
		LOGGER.info("Exiting sendTextCaptureMessage()");
	}
}
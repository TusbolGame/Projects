package com.ticketadvantage.services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.model.EmailEvent;

public class RetrievePopEmail {
	private static final Logger LOGGER = Logger.getLogger(RetrievePopEmail.class);
	// Get system properties
	private final Properties PROPERTIES = System.getProperties();
	private Session emailSession = null;
	private Store store = null;
	private String host;
	private String port;
	private String starttls;
	private String username;
	private String password;
	private Folder emailFolder;

	/**
	 * 
	 * @param host
	 * @param port
	 * @param starttls
	 * @param username
	 * @param password
	 */
	public RetrievePopEmail(String host, String port, String starttls, String username, String password) {
		super();
		
		this.host = host;
		this.port = port;
		this.starttls = starttls;
		this.username = username;
		this.password = password;

		// Setup mail server
		PROPERTIES.put("mail.pop3.host", this.host);
		PROPERTIES.put("mail.pop3.port", this.port);
		PROPERTIES.put("mail.pop3.starttls.enable", this.starttls);

		try {
			// create the POP3 store object and connect with the pop server
			emailSession = Session.getDefaultInstance(PROPERTIES);
			store = emailSession.getStore("pop3s");
		} catch (NoSuchProviderException nspe) {
			LOGGER.error(nspe.getMessage(), nspe);
		}		
	}

	public static void main(String[] args) {
//		RetrieveEmail retrieveEmail = new RetrieveEmail();
//		retrieveEmail.connect();
//		List<EmailContainer> bodies = retrieveEmail.getEmail();
//		for (EmailContainer body : bodies) {
//			System.out.println("body: " + body);
//		}
	}

	/**
	 * 
	 */
	public void connect() {
		LOGGER.info("Entering connect()");

		try {
			store.connect(host, username, password);

			// create the folder object and open it
			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
		} catch (MessagingException me) {
			LOGGER.error(me.getMessage(), me);
		}

		LOGGER.info("Exiting connect()");
	}

	/**
	 * 
	 */
	public void close() {
		LOGGER.info("Entering close()");

		try {
			// close the store and folder objects
			emailFolder.close(false);
			store.close();
		} catch (MessagingException me) {
			LOGGER.error(me.getMessage(), me);
		}

		LOGGER.info("Exiting close()");
	}

	/**
	 * 
	 * @return
	 */
	public List<EmailEvent> getEmail() throws MessagingException, IOException {
		LOGGER.debug("Entering getEmail()");
		final List<EmailEvent> container = new ArrayList<EmailEvent>();

		// retrieve the messages from the folder in an array and print it
		final Message[] messages = emailFolder.getMessages();
		LOGGER.debug("messages.length---" + messages.length);

		if (messages != null && messages.length > 0) {
			LOGGER.debug("messages length: " + messages.length);
			EmailEvent emailContainer = null;

			// Loop through the messages
			for (Message message : messages) {
				LOGGER.debug("Message: " + message);

				try {
					emailContainer = new EmailEvent();
					emailContainer.setDatecreated(new Date());
					emailContainer.setDatemodified(new Date());
					emailContainer.setSubject(message.getSubject());
					final Date receivedDate = message.getReceivedDate();
					if (receivedDate != null) {
						emailContainer.setDatereceived(receivedDate);
					} else {
						emailContainer.setDatereceived(new Date());
					}
					emailContainer.setMessagenum(message.getMessageNumber());
					emailContainer.setDatesent(message.getSentDate());

					String fromaddresses = "";
					final Address[] froms = message.getFrom();
					for (int a = 0; (froms != null && a < froms.length); a++) {
						if (a == (froms.length - 1)) {
							fromaddresses = froms[a].toString();
						} else {
							fromaddresses = froms[a].toString() + ",";
						}
					}
					emailContainer.setFromemail(fromaddresses);

					final String contentType = message.getContentType();
					if (contentType.toLowerCase(Locale.ENGLISH).startsWith("text/plain")) {
						final String messageContent = (String) message.getContent();
						emailContainer.setBodytext(messageContent);
					} else if (contentType.toLowerCase(Locale.ENGLISH).startsWith("text/html")) {
						final String messageContent = (String) message.getContent();
						emailContainer.setBodyhtml(messageContent);
					} else if (contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/")) {
						final MimeMultipart multipart = (MimeMultipart) message.getContent();
						int mCount = multipart.getCount();
						for (int j = 0; j < mCount; j++) {
							final BodyPart bodyPart = multipart.getBodyPart(j);
							final String cType = bodyPart.getContentType();
							if (cType.toLowerCase(Locale.ENGLISH).startsWith("text/plain")) {
								emailContainer.setBodytext((String) bodyPart.getContent());
							} else if (cType.toLowerCase(Locale.ENGLISH).startsWith("text/html")) {
								emailContainer.setBodyhtml((String) bodyPart.getContent());
							}
						}
					}

					String toaddresses = "";
					final Address[] tos = message.getRecipients(Message.RecipientType.TO);
					for (int a = 0; (tos != null && a < tos.length); a++) {
						if (a == (tos.length - 1)) {
							toaddresses = tos[a].toString();
						} else {
							toaddresses = tos[a].toString() + ",";
						}
					}
					emailContainer.setToemail(toaddresses);

					String ccaddresses = "";
					final Address[] ccs = message.getRecipients(Message.RecipientType.CC);
					for (int a = 0; (ccs != null && a < ccs.length); a++) {
						if (a == (ccs.length - 1)) {
							ccaddresses = ccs[a].toString();
						} else {
							ccaddresses = ccs[a].toString() + ",";
						}
					}
					emailContainer.setCcemail(ccaddresses);

					String bccaddresses = "";
					final Address[] bccs = message.getRecipients(Message.RecipientType.BCC);
					for (int a = 0; (bccs != null && a < bccs.length); a++) {
						if (a == (bccs.length - 1)) {
							bccaddresses = bccs[a].toString();
						} else {
							bccaddresses = bccs[a].toString() + ",";
						}
					}
					emailContainer.setBccemail(bccaddresses);

					LOGGER.error("EmailContainer: " + emailContainer);
					container.add(emailContainer);
				} catch (MessagingException me) {
					LOGGER.error(me.getMessage(), me);
				} catch (IOException io) {
					LOGGER.error(io.getMessage(), io);
				}
			}
		}

		LOGGER.debug("Exiting getEmail()");
		return container;
	}

	/**
	 * 
	 * @param emailContainer
	 */
	public void deletMessage(EmailEvent emailContainer) {
		try {
			// create the folder object and open it
			final Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			final Message[] messages = emailFolder.getMessages();

			for (int i = 0, n = messages.length; i < n; i++) {
				emailContainer = new EmailEvent();
				Message message = messages[i];
				emailContainer.setFromemail(message.getFrom()[0].toString());
				emailContainer.setSubject(message.getSubject());
				emailContainer.setMessagenum(message.getMessageNumber());
				emailContainer.setToemail(message.getAllRecipients()[0].toString());

				// set the DELETE flag to true
				message.setFlag(Flags.Flag.DELETED, true);
			}

			// close the store and folder objects
			emailFolder.close(false);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
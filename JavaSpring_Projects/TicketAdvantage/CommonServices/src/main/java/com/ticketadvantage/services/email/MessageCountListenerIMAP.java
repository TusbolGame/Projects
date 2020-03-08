package com.ticketadvantage.services.email;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.batch.processevent.EmailBaseSiteBatch;
import com.ticketadvantage.services.model.EmailEvent;

/**
 * 
 * @author jmiller
 *
 */
public class MessageCountListenerIMAP implements MessageCountListener {
	private static final Logger LOGGER = Logger.getLogger(MessageCountListenerIMAP.class);
	private EmailBaseSiteBatch emailBaseSiteBatch;

	/**
	 * 
	 * @param emailBaseSiteBatch
	 */
	public MessageCountListenerIMAP(EmailBaseSiteBatch emailBaseSiteBatch) {
		super();
		this.emailBaseSiteBatch = emailBaseSiteBatch;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.mail.event.MessageCountListener#messagesAdded(javax.mail.event.MessageCountEvent)
	 */
	@Override
	public void messagesAdded(MessageCountEvent event) {
		LOGGER.info("Entering messagesAdded()");
		Message[] messages = event.getMessages();
		EmailEvent emailContainer = null;

		if (messages != null && messages.length > 0) {
			LOGGER.debug("messages length: " + messages.length);

			for (Message message : messages) {
				LOGGER.debug("Message: " + message);

				try {
					emailContainer = new EmailEvent();
					emailContainer.setDatecreated(new Date());
					emailContainer.setDatemodified(new Date());
					String subject = message.getSubject();
					LOGGER.debug("subject: " + subject);
					emailContainer.setSubject(subject);

					final Date receivedDate = message.getReceivedDate();
					if (receivedDate != null) {
						LOGGER.debug("Setting received date");
						emailContainer.setDatereceived(receivedDate);
					} else {
						LOGGER.debug("Setting received date");
						emailContainer.setDatereceived(new Date());
					}
					int messageNumber = message.getMessageNumber();
					LOGGER.debug("messageNumber: " + messageNumber);
					emailContainer.setMessagenum(messageNumber);
					
					Date sentDate = message.getSentDate();
					LOGGER.debug("sentDate: " + sentDate);
					emailContainer.setDatesent(sentDate);

					String fromaddresses = "";
					final Address[] froms = message.getFrom();
					for (int a = 0; (froms != null && a < froms.length); a++) {
						if (a == (froms.length - 1)) {
							fromaddresses = froms[a].toString();
						} else {
							fromaddresses = froms[a].toString() + ",";
						}
					}
					LOGGER.error("fromaddresses: " + fromaddresses);
					emailContainer.setFromemail(fromaddresses);

					final String contentType = message.getContentType();
					LOGGER.debug("contentType: " + contentType);

					if (contentType.toLowerCase(Locale.ENGLISH).startsWith("text/plain")) {
						final String messageContent = (String) message.getContent();
						LOGGER.debug("bodytext: " + messageContent);
						emailContainer.setBodytext(messageContent);
					} else if (contentType.toLowerCase(Locale.ENGLISH).startsWith("text/html")) {
						final String messageContent = (String) message.getContent();
						LOGGER.debug("bodyhtml: " + messageContent);
						emailContainer.setBodyhtml(messageContent);
					} else if (contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/")) {
						final MimeMultipart multipart = (MimeMultipart) message.getContent();
						int mCount = multipart.getCount();
						LOGGER.debug("multipart.getCount(): " + mCount);
						for (int j = 0; j < mCount; j++) {
							final BodyPart bodyPart = multipart.getBodyPart(j);
							String cType = bodyPart.getContentType();
							LOGGER.debug("cType: " + cType);
							if (cType.toLowerCase(Locale.ENGLISH).startsWith("text/plain")) {
								String bodyContent = (String) bodyPart.getContent();
								LOGGER.debug("bodytext: " + bodyContent);
								emailContainer.setBodytext(bodyContent);
								LOGGER.debug("EmailContainer: " + emailContainer);
							} else if (cType.toLowerCase(Locale.ENGLISH).startsWith("text/html")) {
								String bodyContent = (String) bodyPart.getContent();
								LOGGER.debug("bodyhtml: " + bodyContent);
								emailContainer.setBodyhtml(bodyContent);
								LOGGER.debug("EmailContainer: " + emailContainer);
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
					LOGGER.error("toaddresses: " + toaddresses);
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
					LOGGER.debug("ccaddresses: " + ccaddresses);
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
					LOGGER.error("bccaddresses: " + bccaddresses);
					emailContainer.setBccemail(bccaddresses);

					LOGGER.error("EmailContainer: " + emailContainer);
	
					// Process this email
					if (emailBaseSiteBatch != null) {
						emailContainer.setInet(emailBaseSiteBatch.getInet());
						emailBaseSiteBatch.processEmail(emailContainer);
					}
				} catch (MessagingException me) {
					LOGGER.error(me.getMessage(), me);
				} catch (IOException io) {
					LOGGER.error(io.getMessage(), io);
				}
			}
		}

		LOGGER.info("Exiting messagesAdded()");
	}

	/*
	 * (non-Javadoc)
	 * @see javax.mail.event.MessageCountListener#messagesRemoved(javax.mail.event.MessageCountEvent)
	 */
	@Override
	public void messagesRemoved(MessageCountEvent arg0) {
		// Not needed
	}
}
package com.ticketadvantage.services.email;

import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.batch.processevent.EmailBaseSiteBatch;

public class MessageChangedListenerIMAP implements MessageChangedListener {
	private static final Logger LOGGER = Logger.getLogger(MessageChangedListenerIMAP.class);
	private EmailBaseSiteBatch emailBaseSiteBatch;

	/**
	 * 
	 * @param emailBaseSiteBatch
	 */
	public MessageChangedListenerIMAP(EmailBaseSiteBatch emailBaseSiteBatch) {
		super();
		this.emailBaseSiteBatch = emailBaseSiteBatch;
	}

	@Override
	public void messageChanged(MessageChangedEvent arg0) {
		LOGGER.error("Message Changed!");
	}
}
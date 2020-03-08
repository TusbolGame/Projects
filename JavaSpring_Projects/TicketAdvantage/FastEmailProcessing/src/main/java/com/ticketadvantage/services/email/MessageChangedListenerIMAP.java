package com.ticketadvantage.services.email;

import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.batch.DrHEmailEventBatch;

public class MessageChangedListenerIMAP implements MessageChangedListener {
	private static final Logger LOGGER = Logger.getLogger(MessageChangedListenerIMAP.class);
	private DrHEmailEventBatch drHEmailEventBatch;

	/**
	 * 
	 * @param emailBaseSiteBatch
	 */
	public MessageChangedListenerIMAP(DrHEmailEventBatch drHEmailEventBatch) {
		super();
		this.drHEmailEventBatch = drHEmailEventBatch;
	}

	@Override
	public void messageChanged(MessageChangedEvent arg0) {
		LOGGER.error("Message Changed!");
	}
}
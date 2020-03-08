package com.ticketadvantage.services.dao.email;

import java.util.Set;

import com.ticketadvantage.services.dao.base.BaseParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * 
 * @author jmiller
 *
 */
public abstract class EmailParser extends BaseParser {

	/**
	 * 
	 */
	public EmailParser() {
		super();
	}

	/**
	 * 
	 * @param body
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public abstract Set<PendingEvent> parsePendingBets(String body, String accountName, String accountId) throws BatchException;
}
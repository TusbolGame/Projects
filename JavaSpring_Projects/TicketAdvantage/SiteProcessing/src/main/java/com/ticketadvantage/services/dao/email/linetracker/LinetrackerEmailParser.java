package com.ticketadvantage.services.dao.email.linetracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class LinetrackerEmailParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(LinetrackerEmailParser.class);
	// 10/8/2017 4:05 PM EDT
	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
		}
	};

	/**
	 * 
	 */
	public LinetrackerEmailParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String emailString = "717) Golden State Warriors - LA Clippers o222 -110  $800.00";
			// String emailString = "701) San Antonio Spurs +3½ -110  $800.00";
			// String emailString = "712) Houston Rockets - Philadelphia 76rs u109½ -110 1st Half $800.00";
			// String emailString = "957) Chicago Cubs ML +101  $800.00";

			LinetrackerEmailParser linetrackerEmailParser = new LinetrackerEmailParser();
			Set<PendingEvent> events = linetrackerEmailParser.parsePendingBets(emailString, "acct1", "acct");
			for (PendingEvent event : events) {
				System.out.println("Event: " + event);
			}
		} catch (Throwable be) {
			be.printStackTrace();
		}
	}

	/**
	 * 
	 * @param subject
	 * @param body
	 * @param accountName
	 * @param accountId
	 * @return
	 */
	public boolean isPendingBet(String subject, String body, String accountName, String accountId) {
		LOGGER.info("Entering isPendingBet()");
		LOGGER.debug("body: " + body);
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		boolean retValue = false;

//		CD36 Alert
//		717) Golden State Warriors - LA Clippers o222 -110  $800.00
		int index = subject.indexOf("Alert");
		if (index != -1) {
			subject = subject.substring(0, index).trim();
		}

		int paren = body.indexOf(")");
		int dollar = body.indexOf("$");
		if (paren != -1 && dollar != -1 && hasRotationId(body) && accountId.equals(subject)) {
			retValue = true;
		}

		LOGGER.info("Exiting isPendingBet()");
		return retValue;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	public boolean hasRotationId(String line) {
		LOGGER.info("Entering hasRotationId()");
		boolean retValue = false;
		final StringTokenizer st = new StringTokenizer(line, " ");

		if (st.hasMoreElements()) {
			while (st.hasMoreElements() && !retValue) {
				Object element = st.nextElement();
				retValue = isRotationId(element);
			}
		} else {
			retValue = isRotationId(line);
		}

		LOGGER.info("Exiting hasRotationId()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String body, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("body: " + body);
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		final Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

//		717) Golden State Warriors - LA Clippers o222 -110  $800.00
		Long time = System.currentTimeMillis();
		String ticketNum = Long.toString(time);
		if (ticketNum.length() > 20) {
			ticketNum = ticketNum.substring(0, 20);
		}

		final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		final PendingEvent pendingEvent = new PendingEvent();
		pendingEvent.setPosturl("");
		pendingEvent.setCustomerid(accountId);
		pendingEvent.setInet(accountName);
		pendingEvent.setAccountname(accountName);
		pendingEvent.setAccountid(accountId);
		pendingEvent.setTicketnum(ticketNum);
		pendingEvent.setDateaccepted(PENDING_DATE_FORMAT.get().format(now.getTime()));
		pendingEvent.setDatecreated(now.getTime());
		pendingEvent.setDatemodified(now.getTime());
//		pendingEvent.setEventdate(scheduled);
//		pendingEvent.setPitcher(pitcher);

		// Get the rotation id
		body = setRotationid(body, pendingEvent);
		
		// Get line, juice and team
		parseData(body, pendingEvent);

		// Add to list
		pendingEvents.add(pendingEvent);

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 */
	private void parseData(String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering parseData()");

		// 717) Golden State Warriors - LA Clippers o222 -110  $800.00
		// 712) Houston Rockets - Philadelphia 76rs u109½ -110 1st Half $800.00
		int lastIndex = line.lastIndexOf(" ");
		if (lastIndex != -1) {
			setRiskWin(line.substring(lastIndex + 1).trim(), pendingEvent);

			// Get the line without the $ amount
			line = line.substring(0, lastIndex).trim();
			LOGGER.debug("line: " + line);

			// Setup the line type
			line = setLineType(line, pendingEvent);
			LOGGER.debug("line: " + line);

			int bindex = line.lastIndexOf(" ");
			if (bindex != -1) {
				// Setup the juice
				line = setJuice(bindex, line, pendingEvent);
				LOGGER.debug("line: " + line);

				// Setup the event type and line information
				bindex = line.lastIndexOf(" ");
				if (bindex != -1) {
					line = setEventTypeAndLine(bindex, line, pendingEvent);
					LOGGER.debug("line: " + line);

					line = line.substring(0, bindex).trim();
					LOGGER.debug("line: " + line);
					pendingEvent.setTeam(line);
				}
			}
		}

		LOGGER.info("Exiting parseData()");
	}

	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 */
	private String setLineType(String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setLineType()");

		if (line != null && line.contains("1st")) {
			pendingEvent.setLinetype("first");
			int index = line.indexOf("1st");
			if (index != -1) {
				line = line.substring(0, index).trim();
			}
		} else if (line != null && line.contains("2nd")) {
			pendingEvent.setLinetype("second");
			int index = line.indexOf("2nd");
			if (index != -1) {
				line = line.substring(0, index).trim();
			}
		} else if (line != null && line.contains("3rd")) {
			pendingEvent.setLinetype("third");
			int index = line.indexOf("3rd");
			if (index != -1) {
				line = line.substring(0, index).trim();
			}
		} else {
			pendingEvent.setLinetype("game");
		}

		LOGGER.info("Exiting setLineType()");
		return line;
	}

	/**
	 * 
	 * @param bindex
	 * @param line
	 * @param pendingEvent
	 * @return
	 */
	private String setJuice(int bindex, String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setJuice()");

		final String juice = line.substring(bindex + 1).trim();
		LOGGER.debug("juice: " + juice);

		if (juice != null) {
			if (juice.startsWith("+")) {
				pendingEvent.setJuiceplusminus("+");
				pendingEvent.setJuice(super.reformatValues(juice.substring(1)));
			} else if (juice.startsWith("-")) {
				pendingEvent.setJuiceplusminus("-");
				pendingEvent.setJuice(super.reformatValues(juice.substring(1)));
			} else if (juice.toLowerCase().contains("ev")) {
				pendingEvent.setJuiceplusminus("+");
				pendingEvent.setJuice("100");
			} else if (juice.toLowerCase().contains("pk")) {
				pendingEvent.setJuiceplusminus("+");
				pendingEvent.setJuice("100");
			}
		}

		// Line
		line = line.substring(0,bindex).trim();

		LOGGER.info("Exiting setJuice()");
		return line;
	}

	/**
	 * 
	 * @param bindex
	 * @param line
	 * @param pendingEvent
	 */
	private String setEventTypeAndLine(int bindex, String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setEventTypeAndLine()");

		// 717) Golden State Warriors - LA Clippers o222 -110  $800.00
		// 701) San Antonio Spurs +3½ -110  $800.00
		// 712) Houston Rockets - Philadelphia 76rs u109½ -110 1st Half $800.00
		// 957) Chicago Cubs ML +101  $800.00
		String lineInfo = line.substring(bindex + 1).trim();

		// Setup the event type and line
		if (lineInfo != null) {
			lineInfo = lineInfo.trim().toLowerCase();
			if (lineInfo.contains("ml")) {
				pendingEvent.setEventtype("ml");
				pendingEvent.setLineplusminus(pendingEvent.getJuiceplusminus());
				pendingEvent.setLine(pendingEvent.getJuice());
			} else if (lineInfo.contains("pk")) {
				pendingEvent.setEventtype("spread");
				pendingEvent.setLineplusminus("+");
				pendingEvent.setLine("0");
			} else if (lineInfo.startsWith("+") || lineInfo.startsWith("-")) {
				pendingEvent.setEventtype("spread");
				pendingEvent.setLineplusminus(lineInfo.substring(0, 1));
				pendingEvent.setLine(super.reformatValues(lineInfo.substring(1)));
			} else if (lineInfo.startsWith("o") || lineInfo.startsWith("u")) {
				pendingEvent.setEventtype("total");
				pendingEvent.setLineplusminus(lineInfo.substring(0, 1));
				pendingEvent.setLine(super.reformatValues(lineInfo.substring(1)));
			}
		}

		LOGGER.info("Exiting setEventTypeAndLine()");
		return line;
	}

	/**
	 * 
	 * @param riskWin
	 * @param pendingEvent
	 */
	private void setRiskWin(String riskWin, PendingEvent pendingEvent) {
		LOGGER.info("Entering setRiskWin()");

		// $800
		pendingEvent.setRisk(riskWin.trim());
		pendingEvent.setWin(riskWin.trim());

		LOGGER.info("Exiting setRiskWin()");
	}

	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 * @return
	 */
	private String setRotationid(String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setRotationid()");

		// 717) Golden State Warriors - LA Clippers o222 -110  $800.00
		if (line != null) {
			int eindex = line.indexOf(")");
			if (eindex != -1) {
				String rotationid = line.substring(0, eindex);
				pendingEvent.setRotationid(rotationid);
				line = line.substring(eindex + 2);
			}
		}

		LOGGER.info("Exiting setRotationid()");
		return line;
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	private boolean isRotationId(Object element) {
		LOGGER.info("Entering isRotationId()");
		boolean retValue = false;

		try {
			if (element != null) {
				String str = (String)element;
				LOGGER.debug("Str: " + str);
				str = str.replace(")", "");
				if (str != null && 
					(str.length() == 3 || str.length() == 4) && 
					(!str.startsWith("-") && !str.startsWith("+")) && 
					(!str.contains("."))) {
					// Now try to convert into Integer
					Integer rotationId = Integer.parseInt(str);
					if (rotationId != null) {
						retValue = true;
					}
				}
			}
		} catch (Throwable t) {
			// do nothing and move to next element
		}

		LOGGER.debug("retValue: " + retValue);
		LOGGER.info("Exiting isRotationId()");
		return retValue;
	}
}
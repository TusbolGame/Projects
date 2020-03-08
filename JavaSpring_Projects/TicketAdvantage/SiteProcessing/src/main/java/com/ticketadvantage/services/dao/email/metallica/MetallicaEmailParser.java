package com.ticketadvantage.services.dao.email.metallica;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;
import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class MetallicaEmailParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(MetallicaEmailParser.class);
	// Nov, 17 03:30 PM
	private final static SimpleDateFormat GAME_DATE = new SimpleDateFormat("MMM, dd hh:mm a yyyy", Locale.US);

	/**
	 * 
	 */
	public MetallicaEmailParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String emailString = "\t\t\t\t\tCustomer Id:	WC110\r\n\t\t\t\t\t\r\nInet:	betwc\t\t\t\t\t\r\nTicket #:	192764543\t\t\t\t\t\r\nAccepted:	Sep, 28 03:47 PM\r\n\t\t\t\t\tRisk/Win:	$55.00 to win $50.00\r\n\t\t\t\t\tScheduled:	Sep, 29 09:00 PM\r\n\t\t\t\t\tSport:	WNBA\r\n\t\t\t\t\tDescription	#667 - Minnesota Lynx/Los Angeles Sparks o155½ (-110)";
			String emailString = "\t\t\t\t\tCustomer Id:	POP402\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tInet:	690sports\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tTicket #:	261912428\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tAccepted:	Mar, 01 12:07 AM\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tRisk/Win:	$330.00 to win $300.00\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tScheduled:	Mar, 11 06:00 PM\r\n\t\t\t\t\t\r\n" +
					"\t\t\t\t\tSport:	NCAA - March Madness\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tNotes:	COGAPI, Placed this bet\r\n\t\t\t\t\t\r\n" + 
					"\t\t\t\t\tDescription		#823 - Saint Louis +9½ (-105)";
			// String emailString = "\t\t\t\t\tCustomer Id:	WC110\r\n\t\t\t\t\t\r\nInet:	betwc\t\t\t\t\t\r\nTicket #:	192764543\t\t\t\t\t\r\nAccepted:	Sep, 28 03:47 PM\r\n\t\t\t\t\tRisk/Win:	$55.00 to win $50.00\r\n\t\t\t\t\tScheduled:	Sep, 29 09:00 PM\r\n\t\t\t\t\tSport:	WNBA\r\n\t\t\t\t\tPitcher:	Action\r\n\t\t\t\t\tDescription	#110 - Utah St pk (-105) 1st Half";

			final MetallicaEmailParser metallicaEmailParser = new MetallicaEmailParser();
			metallicaEmailParser.setTimezone("ET");
			final Set<PendingEvent> events = metallicaEmailParser.parsePendingBets(emailString, "acct1", "acct");
			boolean retValue = metallicaEmailParser.isPendingBet(emailString, "690sports", "POP402");
			LOGGER.error("retValue: " + retValue);

			for (PendingEvent event : events) {
				System.out.println("Event: " + event);
			}
		} catch (Throwable be) {
			be.printStackTrace();
		}
	}

	/**
	 * 
	 * @param body
	 * @param accountName
	 * @param accountId
	 * @return
	 */
	public boolean isPendingBet(String body, String accountName, String accountId) {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("body: " + body);
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		boolean retValue = false;

//		Customer Id:	WC553
//		Inet:	betwc
		String customerId = parseField(body, "Customer Id:", "Inet:").replace("\t", "").trim();
		String inet = parseField(body, "Inet:", "Ticket #:").replace("\t", "").trim();
		if (inet != null && customerId != null && accountName.equals(inet) && accountId.equals(customerId)) {
			retValue = true;
		}

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
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

//		Customer Id:	WC553
//		Inet:	betwc
//		Ticket #:	192752747
//		Accepted:	Sep, 28 01:15 PM
//		Risk/Win:	$1080.00 to win $1000.00
//		Scheduled:	Sep, 30 04:00 PM
//		Sport:	College Football
//		Pitcher:	Action
//		Description	#157 - Texas St/Wyoming u48½ (-108)

		String customerid = parseField(body, "Customer Id:", "Inet:").replace("\t", "").trim();
		String inet = parseField(body, "Inet:", "Ticket #:").replace("\t", "").trim();
		String ticketNum = parseField(body, "Ticket #:", "Accepted:").replace("\t", "").trim();
		String accepted = parseField(body, "Accepted:", "Risk/Win:").replace("\t", "").trim();
		String riskWin = parseField(body, "Risk/Win:", "Scheduled:").replace("\t", "").trim();
		String scheduled = parseField(body, "Scheduled:", "Sport:").replace("\t", "").trim();
		String sport = null;
		String pitcher = "";
		String description = null;
		if (body.contains("Pitcher")) {
			sport = parseField(body, "Sport:", "Pitcher").replace("\t", "").trim();
			pitcher = parseField(body, "Pitcher:", "Description").replace("\t", "").trim();
		} else {
			sport = parseField(body, "Sport:", "Description").replace("\t", "").trim();
		}
		description = parseField(body, "Description", null).replace("\t", "").trim();

		final PendingEvent pendingEvent = new PendingEvent();
		pendingEvent.setPosturl("");
		pendingEvent.setCustomerid(customerid);
		pendingEvent.setInet(inet);
		pendingEvent.setAccountname(accountName);
		pendingEvent.setAccountid(accountId);
		pendingEvent.setTicketnum(ticketNum);
		pendingEvent.setDateaccepted(accepted);
		pendingEvent.setDatecreated(new Date());
		pendingEvent.setDatemodified(new Date());
		pendingEvent.setPitcher(pitcher);

		try {
			final Calendar cal = Calendar.getInstance();
			String timezone = super.timezone;
			TimeZone zone = TimeZone.getTimeZone("America/New_York");
			GAME_DATE.setTimeZone(zone);

			if ("ET".equals(timezone)) {
				pendingEvent.setEventdate(scheduled + " ET");
				zone = TimeZone.getTimeZone("America/New_York");
				GAME_DATE.setTimeZone(zone);
			} else if ("CT".equals(timezone)) {
				pendingEvent.setEventdate(scheduled + " CT");
				zone = TimeZone.getTimeZone("America/Chicago");
				GAME_DATE.setTimeZone(zone);
			} else if ("MT".equals(timezone)) {
				pendingEvent.setEventdate(scheduled + " MT");
				zone = TimeZone.getTimeZone("America/Denver");
				GAME_DATE.setTimeZone(zone);
			} else if ("PT".equals(timezone)) {
				pendingEvent.setEventdate(scheduled + " PT");
				zone = TimeZone.getTimeZone("America/Los_Angeles");
				GAME_DATE.setTimeZone(zone);
			}

			scheduled = scheduled.replace("AM", "am");
			scheduled = scheduled.replace("PM", "pm");
			LOGGER.debug("scheduled: " + scheduled);
			final Date gamedate = GAME_DATE.parse(scheduled + " " + cal.get(Calendar.YEAR));
			pendingEvent.setGamedate(gamedate);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		// Set the game sport and game type
		setSport(sport, pendingEvent);

		// Set the risk and win
		setRiskWin(riskWin, pendingEvent);

		// Get the rotation id
		description = setRotationid(description, pendingEvent);
		
		// Get line, juice and team
		parseLineandJuice(description, pendingEvent);

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
	private void parseLineandJuice(String line, PendingEvent pendingEvent) {
		// #667 - Minnesota Lynx/Los Angeles Sparks o155½ (-110)
		// #963 - Houston Astros/Boston Red Sox o5 (-105) 1st 5 Innings
		// #280 - Kansas City Chiefs -4 (-105) 1st Half
		int lastIndex = line.lastIndexOf(")");
		if (lastIndex != -1) {
			// Setup the line type
			setLineType(lastIndex, line, pendingEvent);
			
			int bindex = line.lastIndexOf("(");
			if (bindex != -1) {
				// Setup the juice
				setJuice(bindex, lastIndex, line, pendingEvent);

				// Setup the event type and line information
				setEventTypeAndLine(bindex, line, pendingEvent);
			}
		}
	}

	/**
	 * 
	 * @param lastIndex
	 * @param line
	 * @param pendingEvent
	 */
	private void setLineType(int lastIndex, String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setLineType()");

		if (lastIndex == (line.length() - 1)) {
			// Means we are at the end
			pendingEvent.setLinetype("game");
		} else {
			final String tempString = line.substring(lastIndex + 1);
			if (tempString != null && tempString.contains("1st")) {
				pendingEvent.setLinetype("first");
			} else if (tempString != null && tempString.contains("2nd")) {
				pendingEvent.setLinetype("second");
			} else if (tempString != null && tempString.contains("3rd")) {
				pendingEvent.setLinetype("third");
			}
		}

		LOGGER.info("Exiting setLineType()");
	}

	/**
	 * 
	 * @param bindex
	 * @param lastIndex
	 * @param line
	 * @param pendingEvent
	 */
	private void setJuice(int bindex, int lastIndex, String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setJuice()");

		String juice = line.substring(bindex + 1, lastIndex);
		LOGGER.debug("juice: " + juice);

		if (juice != null) {
			juice = juice.replace("½", ".5").trim();
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

		LOGGER.info("Exiting setJuice()");
	}

	/**
	 * 
	 * @param bindex
	 * @param line
	 * @param pendingEvent
	 */
	private void setEventTypeAndLine(int bindex, String line, PendingEvent pendingEvent) {
		line = line.substring(0, bindex).trim();
		int lastIndex = line.lastIndexOf(" ");

		if (lastIndex != -1) {
			String lineInfo = line.substring(lastIndex + 1);
			if (lineInfo != null) {
				lineInfo = lineInfo.replace("½", ".5");
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

				line = line.substring(0, lastIndex).trim();
				pendingEvent.setTeam(line);
			}
		}
	}

	/**
	 * 
	 * @param riskWin
	 * @param pendingEvent
	 */
	private void setRiskWin(String riskWin, PendingEvent pendingEvent) {
		LOGGER.info("Entering setRiskWin()");

		// $125.00 to win $50.00
		int toWin = riskWin.indexOf("to win");
		if (toWin != -1) {
			pendingEvent.setRisk(riskWin.substring(0, toWin).trim());
			pendingEvent.setWin(riskWin.substring(toWin + 6).trim());
		}

		LOGGER.info("Exiting setRiskWin()");
	}

	/**
	 * 
	 * @param sport
	 * @param pendingEvent
	 */
	private void setSport(String sport, PendingEvent pendingEvent) {
		LOGGER.info("Entering setSport()");

		if (sport.contains("NFL")) {
			pendingEvent.setGamesport("Football");
			pendingEvent.setGametype("NFL");
		} else if (sport.contains("College Football")) {
			pendingEvent.setGamesport("Football");
			pendingEvent.setGametype("NCAA");
		} else if (sport.contains("WNBA")) {
			pendingEvent.setGamesport("Basketball");
			pendingEvent.setGametype("WNBA");
		} else if (sport.contains("NBA")) {
			pendingEvent.setGamesport("Basketball");
			pendingEvent.setGametype("NBA");
		} else if (sport.contains("College Basketball") || 
				sport.contains("NCAA Basketball") ||
				sport.contains("NCAA Basketball Extra") ||
				sport.contains("NCAA Basketball Added") ||
				sport.contains("NCAA - March Madness") ||
				sport.contains("CIT Tournament") ||
				sport.contains("NIT Tournament") || 
				sport.contains("CBI Tournament") || 
				sport.contains("Added Tournaments") ||
				sport.contains("Conference Tournaments") || 
				sport.contains("Extra Tournaments")) {
			pendingEvent.setGamesport("Basketball");
			pendingEvent.setGametype("NCAA");
		} else if (sport.contains("NHL")) {
			pendingEvent.setGamesport("Hockey");
			pendingEvent.setGametype("NHL");
		} else if (sport.contains("MLB") || sport.contains("MLB - Exhibition")) {
			pendingEvent.setGamesport("Baseball");
			pendingEvent.setGametype("MLB");
		} else if (sport.contains("Japan Baseball")) {
			pendingEvent.setGamesport("Baseball");
			pendingEvent.setGametype("International Baseball");
		} else if (sport.contains("Korean Baseball")) {
			pendingEvent.setGamesport("Baseball");
			pendingEvent.setGametype("International Baseball");
		}

		LOGGER.info("Exiting setSport()");
	}

	/**
	 * 
	 * @param body
	 * @param start
	 * @param end
	 * @return
	 */
	private String parseField(String body, String start, String end) {
		LOGGER.info("Entering parseField()");
		String retValue = null;

		if (start != null && end != null) {
			int bindex = body.indexOf(start);
			int eindex = body.indexOf(end);
			if (bindex != -1 && eindex != -1) {
				retValue = body.substring(bindex + start.length(), eindex);
			}
		} else if (start != null && end == null) {
			int bindex = body.indexOf(start);
			if (bindex != -1) {
				retValue = body.substring(bindex + start.length());
			}
		}

		LOGGER.info("Exiting parseField()");
		return retValue;
	}

	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 * @return
	 */
	private String setRotationid(String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setRotationid()");

		// #667 - Minnesota Lynx/Los Angeles Sparks o155½ (-110)
		if (line != null) {
			int bindex = line.indexOf("#");
			int eindex = line.indexOf(" - ");
			if (bindex != -1 && eindex != -1) {
				String rotationid = line.substring(bindex + 1, eindex);
				pendingEvent.setRotationid(rotationid);
				line = line.substring(eindex + 3);
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
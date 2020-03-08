package com.ticketadvantage.services.dao.email.krackwins;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class KrackWinsParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(KrackWinsParser.class);

	/**
	 * 
	 */
	public KrackWinsParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			String emailString = "105- Miami -6.5\r\n118- ECU +23\r\n129- Akron -3\r\n140- Kansas State -15.5";
			String emailString = "385 Ball State +10.5\r\n\r\n1h Ball St +7\r\n\r\nMore coming";

			// String emailString = "306 Boise under 51 \r\n\r\n356 NMST-19.5 \r\n\r\n................\r\n\r\nFIRST HALF\r\n\r\n306 Boise under 26 \r\n\r\n351 Toledo over 30\r\n\r\n342 VT -17";
			// String emailString = "361 ND -4";
			// String emailString = "122 UAB 2h. Pick or better ";
			// String emailString = "122 UAB 2h. Pick or better ";
			// String emailString = "Full games \r\n\r\n151 V Tech -24\r\n117 Uconn+10.5\r\n204 Utah -27\r\n\r\nFirst halves\r\n\r\n 204 Utah-16.5";
			// String emailString = "First half plays \r\n\r\n131 Oak St-7\r\n151 Vir Tech -13\r\n151 over 32.5 \r\n177 over 30.5\r\n\r\nFull game \r\n\r\n131 oak st-11.5";
			// String emailString = "WNBA 687 Phoenix +8.5";
			// String emailString = "140 under nebraska 56";
			// String emailString = "132 oak st under 64";
			// String emailString = "117 over Virginia 51";
			// String emailString = "351 OVER BAYLOR/Utsa  58.5\r\n\r\n369 OVER Nevada/Toledo 68\r\n\r\n377 OVER Auburn/Clemson 55.5 ";
			// String emailString = "These are FIRST HALF PLAYS\r\n\r\n1336 Colorado -22.5\r\n1336 OVER 32 Colorado (same game)\r\n1348 UNDER Navy 24.5";

			KrackWinsParser krackWinsParser = new KrackWinsParser();
			Set<PendingEvent> events = krackWinsParser.parsePendingBets(emailString, "acct1", "acct");
			for (PendingEvent event : events) {
				System.out.println("Event: " + event);
			}

/*
			final KrackWinsParser proCapSportsParser = new KrackWinsParser();
//			final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("#369 Toledo/Nevada over 65", "procap","1");
			final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("#363 TCU -2.5", "procap","1");
			
			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr != null && itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					System.out.println("PendingEvent: " + pendingEvent);
				}
			}
*/
		} catch (Throwable be) {
			be.printStackTrace();
		}
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
				String element = (String)st.nextElement();
				if (element.endsWith("-")) {
					element = element.replace("-", "");
				}
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
		final BufferedReader bufferedReader = new BufferedReader(new StringReader(body));
		final Stream<String> lines = bufferedReader.lines();

		lines.forEach(line -> {
			LOGGER.error("line: " + line);
			int linetype = -1;
			int previouslinetype = -1;

			// Check for a game type
			linetype = checkForGameType(line);
			if (linetype == -1 && previouslinetype != -1) {
				linetype = previouslinetype;
			}
			previouslinetype = linetype;
			LOGGER.debug("linetype: " + linetype);

			// Process the event if there is one
			if (hasRotationId(line)) {
				LOGGER.debug("line: " + line);
				final PendingEvent pendingEvent = processEvent(line, linetype);
				pendingEvent.setAccountname(accountName);
				pendingEvent.setAccountid(accountId);
				pendingEvent.setTicketnum("krackwins");

				pendingEvents.add(pendingEvent);
			}
		});

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param line
	 * @param linetype
	 * @return
	 */
	private PendingEvent processEvent(String line, int linetype) {
		LOGGER.info("Entering processEvent()");
		final PendingEvent pendingEvent = new PendingEvent();

		final Date now = new Date();
		pendingEvent.setDateaccepted(now.toString());
		pendingEvent.setDatecreated(now);
		pendingEvent.setDatemodified(now);
		pendingEvent.setJuiceplusminus("-");
		pendingEvent.setJuice("110");
		pendingEvent.setRisk("0");
		pendingEvent.setWin("0");
		pendingEvent.setDoposturl(false);
		pendingEvent.setPosturl("");
		pendingEvent.setPitcher("");

		// Check if total
		int overUnder = checkForOverUnder(line);
		LOGGER.debug("overUnder: " + overUnder);

		if (overUnder == -1) { // SPREAD
			boolean spread = checkForSpread(line);
			LOGGER.debug("Spread: " + spread);

			// It is a spread
			if (spread) {
				// First get the rotation ID
				setRotationid(line, pendingEvent);
				setSpread(line, pendingEvent);
			} else {
				// ML????
				LOGGER.error("Unknown Game Type");
			}
		} else { // TOTAL
			// First get the rotation ID
			setRotationid(line, pendingEvent);
			setTotal(line, pendingEvent);
		}

		// Setup the line type
		setLinetype(linetype, pendingEvent);

		LOGGER.info("Exiting processEvent()");
		return pendingEvent;
	}
	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 * @return
	 */
	private void setRotationid(String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setRotationid()");
		boolean found = false;

		// First try and get the rotation by breaking up the words
		final StringTokenizer st = new StringTokenizer(line, " ");
		while (st.hasMoreElements() && !found) {
			String word = (String)st.nextElement();
			if (word.endsWith("-")) {
				word = word.replace("-", "");
			}
			if (word != null && (word.length() == 3 || word.length() == 4)) {
				try {
					Integer.parseInt(word);
					pendingEvent.setRotationid(word);
					found = true;
				} catch (Throwable t) {
					LOGGER.debug(t.getMessage(), t);
				}
			}
		}

		LOGGER.info("Exiting setRotationid()");
	}

	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 * @return
	 */
	private void setTotal(String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setTotal()");

		final StringTokenizer st = new StringTokenizer(line, " ");
		while (st.hasMoreElements()) {
			final String word = (String)st.nextElement();
			if (word != null) {
				if (word.trim().equals("over")) {
					pendingEvent.setLineplusminus("o");
					pendingEvent.setEventtype("total");
				} else if (word.trim().equals("under")) {
					pendingEvent.setLineplusminus("u");
					pendingEvent.setEventtype("total");
				} else {
					try {
						Double value = Double.parseDouble(word.trim());
						pendingEvent.setLine(value.toString());
					} catch (Throwable t) {
						// Do nothing
					}
				}
			}
		}

		LOGGER.info("Exiting setTotal()");
	}

	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 * @return
	 */
	private void setSpread(String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setSpread()");

		// Spread indicator
		final StringTokenizer st = new StringTokenizer(line, " ");
		while (st.hasMoreElements()) {
			final String word = (String)st.nextElement();
			if (!isRotationId(word)) {
				if (word.contains("+" ) || 
					word.contains("-") ||  
					word.contains("plus" ) || 
					word.contains("minus" )) {
					int index = word.indexOf("-");
					if (index != -1) {
						pendingEvent.setLineplusminus("-");
						pendingEvent.setLine(word.substring(index + 1));
						pendingEvent.setEventtype("spread");
					} else {
						index = word.indexOf("+");
						if (index != -1) {
							pendingEvent.setLineplusminus("+");
							pendingEvent.setLine(word.substring(index + 1));
							pendingEvent.setEventtype("spread");
						} else {
							index = word.indexOf("minus");
							if (index != -1) {
								pendingEvent.setLineplusminus("-");
								pendingEvent.setLine(word.substring(index + 5));
								pendingEvent.setEventtype("spread");
							} else {
								index = word.indexOf("plus");
								if (index != -1) {
									pendingEvent.setLineplusminus("+");
									pendingEvent.setLine(word.substring(index + 4));
									pendingEvent.setEventtype("spread");
								}
							}
						}
					}
				} else if (word.contains("ev" ) ||
						word.contains("even" ) || 
						word.contains("pk" ) || 
						word.contains("pick")) {
					pendingEvent.setLineplusminus("+");
					pendingEvent.setLine("0");
					pendingEvent.setEventtype("spread");
				}
			}
		}

		LOGGER.info("Exiting setSpread()");
	}

	/**
	 * 
	 * @param linetype
	 * @param pendingEvent
	 */
	private void setLinetype(int linetype, PendingEvent pendingEvent) {
		LOGGER.info("Entering setLinetype()");

		final String rotationid = pendingEvent.getRotationid();
		if (rotationid != null && rotationid.length() == 4) {
			if (rotationid.startsWith("1")) {
				pendingEvent.setLinetype("first");
				pendingEvent.setRotationid(rotationid.substring(1));
			} else if (rotationid.startsWith("2")) {
				pendingEvent.setLinetype("second");
				pendingEvent.setRotationid(rotationid.substring(1));
			}
		} else {
			if (linetype == -1 || linetype == 0) {
				pendingEvent.setLinetype("game");
			} else if (linetype == 1) {
				pendingEvent.setLinetype("first");
			} else if (linetype == 2) {
				pendingEvent.setLinetype("second");
			}
		}

		LOGGER.info("Exiting setLinetype()");
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

	/**
	 * 
	 * @param line
	 * @return
	 */
	private int checkForGameType(String line) {
		LOGGER.info("Entering checkForGameType()");
		int gameType = -1;

		if (line.contains("full") || line.contains("game")) {
			gameType = 0;
		} else if (line.contains("first") || line.contains("1h")) {
			gameType = 1;
		} else if (line.contains("second") || line.contains("2h")) {
			gameType = 2;
		}

		LOGGER.info("Exiting checkForGameType()");
		return gameType;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	private int checkForOverUnder(String line) {
		LOGGER.info("Entering checkForOverUnder()");
		int overUnder = -1;

		if (line.contains("over")) {
			overUnder = 0;
		} else if (line.contains("under")) {
			overUnder = 1;
		} else {
			// check if there is u50, o50
			final StringTokenizer st = new StringTokenizer(line, " ");
			while (st.hasMoreElements()) {
				String element = (String)st.nextElement();
				if (element != null && element.length() > 0) {
					if (element.startsWith("o")) {
						try {
							element = element.substring(1);
							if (element != null) {
								Integer.parseInt(element);
								overUnder = 0;		
							}
						} catch (Throwable t) {
							LOGGER.debug(t.getMessage(), t);
						}
					} else if (element.startsWith("u")) {
						try {
							element = element.substring(1);
							if (element != null) {
								Integer.parseInt(element);
								overUnder = 1;		
							}
						} catch (Throwable t) {
							LOGGER.debug(t.getMessage(), t);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting checkForOverUnder()");
		return overUnder;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	private boolean checkForSpread(String line) {
		LOGGER.info("Entering checkForSpread()");
		boolean retValue = false;

		if (line.contains("+" ) || 
			line.contains("-") || 
			line.contains("ev" ) || 
			line.contains("plus" ) || 
			line.contains("minus" ) || 
			line.contains("even" ) || 
			line.contains("pk" ) || 
			line.contains("pick")) {
			retValue = true;
		}

		LOGGER.info("Exiting checkForSpread()");
		return retValue;
	}
}
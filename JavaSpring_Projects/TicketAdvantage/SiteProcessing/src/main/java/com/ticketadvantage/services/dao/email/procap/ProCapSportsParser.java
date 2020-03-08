package com.ticketadvantage.services.dao.email.procap;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.util.ParseBullshit;

public class ProCapSportsParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(ProCapSportsParser.class);

	/**
	 * 
	 */
	public ProCapSportsParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final ProCapSportsParser proCapSportsParser = new ProCapSportsParser();
			// final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("369 Toledo/Nevada over 65", "procap","1");
			// final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("Bee-Y-U/Mi$si$sippi $t ov3r Forty-6.5", "procap","1");
			final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("522 K4ns4s St/Kansas und3r 153", "procap","1");
			
			// (On3-8-S3ven) MT$U/F4U moar than 58.5
			// (0n3-E1ght-N1n3) M1SS ST/4UBURN 0v3r FIFTY-1
//			final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("#363 TCU -2.5", "procap","1");
//			final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("#204 Nevada/Fresno State under 62.5", "procap","1");
//			final Set<PendingEvent> pendingEvents = proCapSportsParser.parsePendingBets("140 under nebraska 56", "procap","1");

			if (pendingEvents != null && !pendingEvents.isEmpty()) {
				final Iterator<PendingEvent> itr = pendingEvents.iterator();
				while (itr != null && itr.hasNext()) {
					final PendingEvent pendingEvent = itr.next();
					System.out.println("PendingEvent: " + pendingEvent);
				}
			}
		} catch (BatchException be) {
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
		final BufferedReader bufferedReader = new BufferedReader(new StringReader(body));
		final Stream<String> lines = bufferedReader.lines();

		lines.forEach(line -> {
			LOGGER.error("line: " + line);
			int linetype = -1;
			int previouslinetype = -1;

			// Check for a game type
			line = line.replace("$", "s");
			linetype = checkForGameType(line);
			if (linetype == -1 && previouslinetype != -1) {
				linetype = previouslinetype;
			}
			previouslinetype = linetype;
			LOGGER.debug("linetype: " + linetype);

			// Process the event if there is one
			if (hasRotationId(line)) {
				LOGGER.debug("line: " + line);
				// final PendingEvent pendingEvent = processEvent(line, linetype);
				final PendingEvent pendingEvent = new PendingEvent();
				final ParseBullshit parseBullshit = new ParseBullshit();
				parseBullshit.parse(line, pendingEvent);

				pendingEvent.setInet(accountName);
				pendingEvent.setAccountname(accountName);
				pendingEvent.setCustomerid(accountId);
				pendingEvent.setAccountid(accountId);
				pendingEvents.add(pendingEvent);
			} else {
				LOGGER.debug("line: " + line);
				// final PendingEvent pendingEvent = processEvent(line, linetype);
				final PendingEvent pendingEvent = new PendingEvent();
				final ParseBullshit parseBullshit = new ParseBullshit();
				parseBullshit.parse(line, pendingEvent);

				pendingEvent.setInet(accountName);
				pendingEvent.setAccountname(accountName);
				pendingEvent.setCustomerid(accountId);
				pendingEvent.setAccountid(accountId);
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
			final String word = (String)st.nextElement();
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
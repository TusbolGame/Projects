package com.ticketadvantage.services.dao.email.themachine;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class TheMachineEmailParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(TheMachineEmailParser.class);

	/**
	 * 
	 */
	public TheMachineEmailParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String emailString = "NCAAF:\r\nUAB ML *10 units*\r\nNBA:\r\nPacers -12 *10 units*\r\nNCAAB:\r\nKansas State -11 *10 units*\r\nLeans:\r\nChattanooga +6";
			//final String emailString = "NFL:\r\n\r\nRaiders +3 *10 units*";
			final TheMachineEmailParser theMachineEmailParser = new TheMachineEmailParser();
			final Set<PendingEvent> events = theMachineEmailParser.parsePendingBets(emailString, "acct1", "acct");
			for (PendingEvent event : events) {
				LOGGER.error("Event: " + event);
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

		// Check for valid markers
		if (body != null) {
			body = body.toLowerCase();

			if (body.contains("units*") ||
				body.contains("lean:") ||
				body.contains("leans") ||
				body.contains("nfl:") ||
				body.contains("ncaaf:") ||
				body.contains("nba:") ||
				body.contains("ncaab:") ||
				body.contains("mlb") ||
				body.contains("nba all-star game:")) {
				retValue = true;				
			}
		}

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

		String[] tokens = body.split("\\r\\n");
		boolean startNcaaf = false;
		boolean startNcaab = false;
		boolean startNfl = false;
		boolean startNba = false;
		boolean startNhl = false;
		boolean startMlb = false;
		boolean startLean = false;

		// Loop through the lines
		for (String line: tokens) {
			line = line.toLowerCase().trim();
			LOGGER.debug("line: " + line);

			if (line.length() > 0) {
				if (line.contains("nfl:")) {
					startNcaaf = false;
					startNcaab = false;
					startNfl = true;
					startNba = false;
					startNhl = false;
					startMlb = false;
					startLean = false;
				} else if (line.contains("ncaaf:")) {
					startNcaaf = true;
					startNcaab = false;
					startNfl = false;
					startNba = false;
					startNhl = false;
					startMlb = false;
					startLean = false;
				} else if (line.contains("ncaab:")) {
					startNcaaf = false;
					startNcaab = true;
					startNfl = false;
					startNba = false;
					startMlb = false;
					startLean = false;
				} else if (line.contains("nba:")) {
					startNcaaf = false;
					startNcaab = false;
					startNfl = false;
					startNba = true;
					startNhl = false;
					startMlb = false;
					startLean = false;
				} else if (line.contains("nhl:")) {
					startNcaaf = false;
					startNcaab = false;
					startNfl = false;
					startNba = false;
					startNhl = true;
					startMlb = false;
					startLean = false;
				} else if (line.contains("mlb:")) {
					startNcaaf = false;
					startNcaab = false;
					startNfl = false;
					startNba = false;
					startNhl = false;
					startMlb = true;
					startLean = false;
				} else if (line.contains("lean:") || line.contains("leans:")) {
					// Do nothing
					startLean = true;
				} else if (startNfl) {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Football");
					pe.setGametype("NFL");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
	
					try {
						// Get the line
						parseLine(line, pe);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}

					// Check for a lean
					if (startLean) {
						pe.setIslean(true);
						pe.setNumunits(new Float(10));
					}
	
					// Add to list
					pendingEvents.add(pe);
				} else if (startNcaaf) {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Football");
					pe.setGametype("NCAAF");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
	
					try {
						// Get the line
						parseLine(line, pe);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}

					// Check for a lean
					if (startLean) {
						pe.setIslean(true);
						pe.setNumunits(new Float(10));
					}
	
					// Add to list
					pendingEvents.add(pe);
				} else if (startNba) {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Basketball");
					pe.setGametype("NBA");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
	
					try {
						// Get the line
						parseLine(line, pe);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}

					// Check for a lean
					if (startLean) {
						pe.setIslean(true);
						pe.setNumunits(new Float(10));
					}
	
					// Add to list
					pendingEvents.add(pe);
				} else if (startNcaab) {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Basketball");
					pe.setGametype("NCAAB");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
	
					try {
						// Get the line
						parseLine(line, pe);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}

					// Check for a lean
					if (startLean) {
						pe.setIslean(true);
						pe.setNumunits(new Float(10));
					}
	
					// Add to list
					pendingEvents.add(pe);
				} else if (startNhl) {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Hockey");
					pe.setGametype("NHL");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
	
					try {
						// Get the line
						parseLine(line, pe);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}
	
					// Check for a lean
					if (startLean) {
						pe.setIslean(true);
						pe.setNumunits(new Float(10));
					}
	
					// Add to list
					pendingEvents.add(pe);
				} else if (startMlb) {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Baseball");
					pe.setGametype("MLB");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
	
					try {
						// Get the line
						parseLine(line, pe);
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
					}
	
					// Check for a lean
					if (startLean) {
						pe.setIslean(true);
						pe.setNumunits(new Float(10));
					}
	
					// Add to list
					pendingEvents.add(pe);
				}
			}
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param line
	 * @param pe
	 */
	private void parseLine(String line, PendingEvent pe) {
		line = line.toLowerCase();		
		int index = parseMl(line, pe);

		if (index == -1) {
			index = parseFavoriteSpread(line, pe);

			if (index == -1) {
				index = parseDogSpread(line, pe);

				if (index == -1) {
					index = parseOverTotal(line, pe);

					if (index == -1) {
						index = parseUnderTotal(line, pe);
					}
				}				
			}
		}
	}

	/**
	 * 
	 * @param line
	 * @param pe
	 * @return
	 */
	private int parseMl(String line, PendingEvent pe) {
		int index = line.indexOf("ml");

		if (index != -1) {
			pe.setEventtype("ml");
			pe.setLinetype("game");
			pe.setTeam(line.substring(0, index).trim());
			pe.setLineplusminus("-");
			pe.setLine("500");
			pe.setJuiceplusminus("-");
			pe.setJuice("500");
			
			line = line.substring(index + 2);
			int uindex = line.indexOf("units*");
			if (uindex != -1) {
				line = line.substring(0, uindex).trim();
				line = line.replace("*", "").trim();
				pe.setNumunits(Float.parseFloat(line));
			} else {
				pe.setNumunits(Float.parseFloat("10"));
			}
		}
		
		return index;
	}

	/**
	 * 
	 * @param line
	 * @param pe
	 * @return
	 */
	private int parseFavoriteSpread(String line, PendingEvent pe) {
		int index = line.indexOf("-");

		if (index != -1) {
			pe.setEventtype("spread");
			pe.setLinetype("game");
			pe.setTeam(line.substring(0, index).trim());
			pe.setLineplusminus("-");

			line = line.substring(index + 1).trim();
			index = line.indexOf(" ");

			if (index != -1) {
				pe.setLine(line.substring(0, index).trim());
				line = line.substring(index + 1);

				int uindex = line.indexOf("units*");
				if (uindex != -1) {
					line = line.substring(0, uindex).trim();
					line = line.replace("*", "").trim();
					pe.setNumunits(Float.parseFloat(line));
				} else {
					pe.setNumunits(Float.parseFloat("10"));
				}
			} else {
				pe.setLine(line);
				pe.setNumunits(Float.parseFloat("10"));
				index = 123;
			}

			pe.setJuiceplusminus("-");
			pe.setJuice("150");
		}
		
		return index;
	}

	/**
	 * 
	 * @param line
	 * @param pe
	 * @return
	 */
	private int parseDogSpread(String line, PendingEvent pe) {
		int index = line.indexOf("+");

		if (index != -1) {
			pe.setEventtype("spread");
			pe.setLinetype("game");
			pe.setTeam(line.substring(0, index).trim());
			pe.setLineplusminus("+");

			line = line.substring(index + 1).trim();
			index = line.indexOf(" ");

			if (index != -1) {
				pe.setLine(line.substring(0, index).trim());
				line = line.substring(index + 1);

				int uindex = line.indexOf("units*");
				if (uindex != -1) {
					line = line.substring(0, uindex).trim();
					line = line.replace("*", "").trim();
					pe.setNumunits(Float.parseFloat(line));
				} else {
					pe.setNumunits(Float.parseFloat("10"));
				}
			} else {
				pe.setLine(line);
				pe.setNumunits(Float.parseFloat("10"));
				index = 123;
			}

			pe.setJuiceplusminus("-");
			pe.setJuice("150");
		}

		return index;
	}

	/**
	 * 
	 * @param line
	 * @param pe
	 * @return
	 */
	private int parseOverTotal(String line, PendingEvent pe) {
		int index = line.indexOf("over");

		if (index != -1) {
			pe.setEventtype("total");
			pe.setLinetype("game");
			pe.setTeam(line.substring(0, index).trim());
			pe.setLineplusminus("o");

			line = line.substring(index + 4).trim();
			index = line.indexOf(" ");

			if (index != -1) {
				pe.setLine(line.substring(0, index).trim());
				line = line.substring(index + 1);

				int uindex = line.indexOf("units*");
				if (uindex != -1) {
					line = line.substring(0, uindex).trim();
					line = line.replace("*", "").trim();
					pe.setNumunits(Float.parseFloat(line));
				} else {
					pe.setNumunits(Float.parseFloat("10"));
				}
			} else {
				pe.setLine(line);
				pe.setNumunits(Float.parseFloat("10"));
				index = 123;
			}

			pe.setJuiceplusminus("-");
			pe.setJuice("150");
		}

		return index;
	}

	/**
	 * 
	 * @param line
	 * @param pe
	 * @return
	 */
	private int parseUnderTotal(String line, PendingEvent pe) {
		int index = line.indexOf("under");

		if (index != -1) {
			pe.setEventtype("total");
			pe.setLinetype("game");
			pe.setTeam(line.substring(0, index).trim());
			pe.setLineplusminus("u");

			line = line.substring(index + 5).trim();
			index = line.indexOf(" ");

			if (index != -1) {
				pe.setLine(line.substring(0, index).trim());
				line = line.substring(index + 1);

				int uindex = line.indexOf("units*");
				if (uindex != -1) {
					line = line.substring(0, uindex).trim();
					line = line.replace("*", "").trim();
					pe.setNumunits(Float.parseFloat(line));
				} else {
					pe.setNumunits(Float.parseFloat("10"));
				}
			} else {
				pe.setLine(line);
				pe.setNumunits(Float.parseFloat("10"));
				index = 123;
			}

			pe.setJuiceplusminus("-");
			pe.setJuice("150");
		}

		return index;
	}
}
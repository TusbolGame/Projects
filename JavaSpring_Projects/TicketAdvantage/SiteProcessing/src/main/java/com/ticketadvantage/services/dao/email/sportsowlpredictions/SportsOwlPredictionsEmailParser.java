package com.ticketadvantage.services.dao.email.sportsowlpredictions;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class SportsOwlPredictionsEmailParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(SportsOwlPredictionsEmailParser.class);

	/**
	 * 
	 */
	public SportsOwlPredictionsEmailParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final String emailString = "Model 12/28\r\n" + 
					"FG:\r\n" + 
					"MIN -9.5 3.8u\r\n" + 
					"Most platforms love the lakers and our model does as well; however, I'm not sure how much ESPN, 538 and OddsShark are taking the Lebron and Rondo injury into account. The Clippers have only covered -5.5 twice in their last 13 games including 2 in their last 3.  Lakers have covered +5.5 11 of their last 15. The Lakers looked good without Lebron against the warriors and kings, so I am sending the Lakers game as a play today.  These are the units used by the model, as a reminder it is always better to be cautious than over aggressive. We never recommend changing your unit size, however with the amount of potential risk on this play you may want to lower the number of units or alter your unit size. Caution is never a bad thing.\r\n" + 
					"LAL +5.5 11.5u\r\n" + 
					"LAL ML +180 risking 4.8u\r\n" + 
					"\r\n" + 
					"On Wed, Dec 26, 2018 at 8:23 AM Sports Owl <sportsowlpredictions@gmail.com> wrote:";
			final SportsOwlPredictionsEmailParser theMachineEmailParser = new SportsOwlPredictionsEmailParser();
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

			if (body.contains("model")) {
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
		
		int index = body.indexOf("<sportsowlpredictions@gmail.com> wrote:");
		if (index != -1) {
			body = body.substring(0, index);
			index = body.lastIndexOf("On ");

			if (index != -1) {
				body = body.substring(0, index);
			}
		}

		String[] tokens = body.split("\\r\\n");
		boolean hasModel = false;
		boolean startFG = false;
		boolean start1H = false;
		boolean start1Q = false;

		// Loop through the lines
		for (String line: tokens) {
			line = line.toLowerCase();
			LOGGER.debug("line: " + line);

			if (line.contains("model") && !hasModel) {
				// Do nothing
				startFG = false;
				start1H = false;
				start1Q = false;
				hasModel = true;
			} else if (line.contains("fg:")) {
				startFG = true;
				start1H = false;
				start1Q = false;
			} else if (line.contains("1h:")) {
				startFG = false;
				start1H = true;
				start1Q = false;
			} else if (line.contains("1q:")) {
				startFG = false;
				start1H = false;
				start1Q = true;
			} else if (startFG) {
				try {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Basketball");
					pe.setGametype("NBA");
					pe.setLinetype("game");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);

					LOGGER.debug("line: " + line);
					// Get the line
					parseLine(line, pe);
	
					// Add to list
					pendingEvents.add(pe);
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			} else if (start1H) {
				try {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Basketball");
					pe.setGametype("NBA");
					pe.setLinetype("first");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
	
					// Get the line
					parseLine(line, pe);
	
					// Add to list
					pendingEvents.add(pe);
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			} else if (start1Q) {
				try {
					final PendingEvent pe = new PendingEvent();
					pe.setGamesport("Basketball");
					pe.setGametype("NBA");
					pe.setLinetype("quarter");
					pe.setPosturl("");
					pe.setInet(accountName);
					pe.setCustomerid(accountId);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
	
					// Get the line
					parseLine(line, pe);
	
					// Add to list
					pendingEvents.add(pe);
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
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
		line = line.replace("risk ", "");
		line = line.replace("risking ", "");
		line = line.replace("pick em", "+0");

		int index = parseMl(line, pe);

		if (index == -1) {
			LOGGER.debug("line: " + line);
			index = parseFavoriteSpread(line, pe);

			if (index == -1) {
				LOGGER.debug("line: " + line);
				index = parseDogSpread(line, pe);

				if (index == -1) {
					parseTotal(line, pe);
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
			// MEM ML +145 1.6u
			pe.setEventtype("ml");

			final StringTokenizer st = new StringTokenizer(line, " ");
			int counter = 0;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				LOGGER.debug("token: " + token);

				switch (counter++) {
					case 0:
						String team = token.trim();
						team = team.replace("gsw", "golden state");
						team = team.replace("nyk", "new york");
						team = team.replace("bkn", "brooklyn");
						team = team.replace("uta", "utah");
						team = team.replace("san", "san antonio");
						team = team.replace("phx", "phoenix");
						team = team.replace("lal", "lakers");
						team = team.replace("lac", "clippers");
						pe.setTeam(team);
						break;
					case 2:
						if (token.startsWith("+") || token.startsWith("-")) {
							pe.setLineplusminus(token.substring(0, 1));
							pe.setLine(token.substring(1));
							pe.setJuiceplusminus(token.substring(0, 1));
							pe.setJuice(token.substring(1));
						} else if (token.endsWith("u")) {
							pe.setLineplusminus("-");
							pe.setLine("500");
							pe.setJuiceplusminus("-");
							pe.setJuice("500");

							token = token.replace("u", "").trim();
							pe.setNumunits(Float.parseFloat(token));
						} else {
							token = token.trim();
							try {
								pe.setNumunits(Float.parseFloat(token));
							} catch (Throwable t) {
								LOGGER.warn(t.getMessage(), t);
							}
						}
						break;
					case 3:
						if (token.endsWith("u")) {
							token = token.replace("u", "").trim();
							pe.setNumunits(Float.parseFloat(token));
						}
					case 1:
					default:
						break;
				}
			}

			index = 999;
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

			final StringTokenizer st = new StringTokenizer(line, " ");
			int counter = 0;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				LOGGER.debug("token: " + token);

				switch (counter++) {
					case 0:
						String team = token.trim();
						team = team.replace("gsw", "golden state");
						team = team.replace("nyk", "new york");
						team = team.replace("bkn", "brooklyn");
						team = team.replace("uta", "utah");
						team = team.replace("san", "san antonio");
						team = team.replace("phx", "phoenix");
						team = team.replace("lal", "lakers");
						team = team.replace("lac", "clippers");
						pe.setTeam(team);
						break;
					case 1:
						if (token.startsWith("-")) {
							pe.setLineplusminus(token.substring(0, 1));
							pe.setLine(token.substring(1));
							pe.setJuiceplusminus("-");
							pe.setJuice("500");
						}
						break;
					case 2:
						if (token.endsWith("u")) {
							token = token.replace("u", "").trim();
							pe.setNumunits(Float.parseFloat(token));
						} else {
							token = token.trim();
							try {
								pe.setNumunits(Float.parseFloat(token));
							} catch (Throwable t) {
								LOGGER.warn(t.getMessage(), t);
							}
						}
					default:
						break;
				}
			}

			if (pe.getNumunits().floatValue() == 0) {
				pe.setNumunits(new Float(2));
			}

			index = 999;
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
		LOGGER.debug("index: " + index);

		if (index != -1) {
			pe.setEventtype("spread");

			final StringTokenizer st = new StringTokenizer(line, " ");
			int counter = 0;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();

				switch (counter++) {
					case 0:
						String team = token.trim();
						team = team.replace("gsw", "golden state");
						team = team.replace("nyk", "new york");
						team = team.replace("bkn", "brooklyn");
						team = team.replace("uta", "utah");
						team = team.replace("san", "san antonio");
						team = team.replace("phx", "phoenix");
						team = team.replace("lal", "lakers");
						team = team.replace("lac", "clippers");
						pe.setTeam(team);
						break;
					case 1:
						if (token.startsWith("+")) {
							pe.setLineplusminus(token.substring(0, 1));
							pe.setLine(token.substring(1));
							pe.setJuiceplusminus("-");
							pe.setJuice("500");
						}
						break;
					case 2:
						if (token.endsWith("u")) {
							token = token.replace("u", "").trim();
							pe.setNumunits(Float.parseFloat(token));
						} else {
							token = token.trim();
							try {
								pe.setNumunits(Float.parseFloat(token));
							} catch (Throwable t) {
								LOGGER.warn(t.getMessage(), t);
							}
						}
					default:
						break;
				}
			}

			if (pe.getNumunits().floatValue() == 0) {
				pe.setNumunits(new Float(2));
			}

			index = 999;
		}
		
		return index;
	}

	/**
	 * 
	 * @param line
	 * @param pe
	 * @return
	 */
	private int parseTotal(String line, PendingEvent pe) {
		pe.setEventtype("total");

		final StringTokenizer st = new StringTokenizer(line, " ");
		int counter = 0;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();

			switch (counter++) {
				case 0:
					String teamn = token.trim();
					int tindex = teamn.indexOf("/");

					if (tindex != -1) {
						String team = token.substring(0, tindex);
						team = team.replace("gsw", "golden state");
						team = team.replace("nyk", "new york");
						team = team.replace("bkn", "brooklyn");
						team = team.replace("uta", "utah");
						team = team.replace("san", "san antonio");
						team = team.replace("phx", "phoenix");
						team = team.replace("lal", "lakers");
						team = team.replace("lac", "clippers");
						pe.setTeam(team);
					} else {
						String team = token.trim();
						team = team.replace("gsw", "golden state");
						team = team.replace("nyk", "new york");
						team = team.replace("bkn", "brooklyn");
						team = team.replace("uta", "utah");
						team = team.replace("san", "san antonio");
						team = team.replace("phx", "phoenix");
						team = team.replace("lal", "lakers");
						team = team.replace("lac", "clippers");
						pe.setTeam(team);
					}
					break;
				case 1:
					if (token.startsWith("o")) {
						pe.setLineplusminus("o");
						pe.setLine(token.substring(1));
						pe.setJuiceplusminus("-");
						pe.setJuice("500");
					} else if (token.startsWith("u")) {
						pe.setLineplusminus("u");
						pe.setLine(token.substring(1));
						pe.setJuiceplusminus("-");
						pe.setJuice("500");
					}
					break;
				case 2:
					if (token.endsWith("u")) {
						token = token.replace("u", "").trim();
						pe.setNumunits(Float.parseFloat(token));
					} else {
						token = token.trim();
						try {
							pe.setNumunits(Float.parseFloat(token));
						} catch (Throwable t) {
							LOGGER.warn(t.getMessage(), t);
						}
					}
				default:
					break;
			}
		}

		if (pe.getNumunits().floatValue() == 0) {
			pe.setNumunits(new Float(2));
		}

		return 999;
	}
}
package com.ticketadvantage.services.dao.twitter.threemanweave;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class ThreeManWeaveTwitterParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(ThreeManWeaveTwitterParser.class);

	/**
	 * 
	 */
	public ThreeManWeaveTwitterParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//final String twitterString = "NCAAF:\r\nUAB ML *10 units*\r\nNBA:\r\nPacers -12 *10 units*\r\nNCAAB:\r\nKansas State -11 *10 units*\r\nLeans:\r\nChattanooga +6";
			final String twitterString = "The only way we know how to get out of this consensus slump is to keep chuckin'...\n" + 
					"\n" + 
					"Cmon Sean Woods, take care of business tonight - degenerate nation needs you now more than ever:\n" + 
					"\n" + 
					"Southern +6.5\n" + 
					"\n" + 
					"YTD: 149-110-7";
			//final String emailString = "NFL:\r\n\r\nRaiders +3 *10 units*";
			final ThreeManWeaveTwitterParser theMachineEmailParser = new ThreeManWeaveTwitterParser();
			final Set<PendingEvent> events = theMachineEmailParser.parsePendingBets(twitterString, "acct1", "acct");
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
			int index = body.indexOf(" -");
			
			if (index != -1) {
				body = body.substring(index + 2, index + 3);

				if (body.matches("[0-9]+")) {
					// your operations
					retValue = true;
				}			
			} else {
				index = body.indexOf(" +");
				
				if (index != -1) {
					body = body.substring(index + 2, index + 3);

					if (body.matches("[0-9]+")) {
						// your operations
						retValue = true;
					}			
				} else {
					index = body.indexOf(" pk");

					if (index != -1) {
						retValue = true;
					}
				}
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

		String[] tokens = body.split("\\n");

		// Loop through the lines
		for (String line : tokens) {
			line = line.toLowerCase().trim();
			LOGGER.debug("line: " + line);

			if (line.length() > 0) {
				line = line.toLowerCase();
				int index = line.indexOf(" -");
				
				if (index != -1) {
					String templine = line.substring(index + 2, index + 3);

					if (templine.matches("[0-9]+")) {
						final PendingEvent pe = new PendingEvent();
						pe.setGamesport("Basketball");
						pe.setGametype("NCAAB");
						pe.setLinetype("game");
						pe.setEventtype("spread");
						pe.setPosturl("");
						pe.setInet(accountName);
						pe.setCustomerid(accountId);
						pe.setAccountname(accountName);
						pe.setAccountid(accountId);

						String team = line.substring(0, index).trim();
						if (team.contains(")")) {
							int tindex = team.indexOf(")");

							if (tindex != -1) {
								team = team.substring(tindex + 1).trim();
								pe.setTeam(team);
							}
						} else {
							pe.setTeam(team);
						}

						String linevalue = line.substring(index + 2).trim();
						index = linevalue.indexOf(" ");
						if (index != -1) {
							linevalue = linevalue.substring(0, index).trim();
						}

						pe.setLineplusminus("-");
						pe.setLine(linevalue);
						pe.setJuiceplusminus("-");
						pe.setJuice("130");

						pendingEvents.add(pe);
					}			
				} else {
					index = line.indexOf(" +");
					
					if (index != -1) {
						String templine = line.substring(index + 2, index + 3);

						if (templine.matches("[0-9]+")) {
							final PendingEvent pe = new PendingEvent();
							pe.setGamesport("Basketball");
							pe.setGametype("NCAAB");
							pe.setLinetype("game");
							pe.setEventtype("spread");
							pe.setPosturl("");
							pe.setInet(accountName);
							pe.setCustomerid(accountId);
							pe.setAccountname(accountName);
							pe.setAccountid(accountId);

							String team = line.substring(0, index).trim();
							if (team.contains(")")) {
								int tindex = team.indexOf(")");

								if (tindex != -1) {
									team = team.substring(tindex + 1).trim();
									pe.setTeam(team);
								}
							} else {
								pe.setTeam(team);
							}

							String linevalue = line.substring(index + 2).trim();
							index = linevalue.indexOf(" ");
							if (index != -1) {
								linevalue = linevalue.substring(0, index).trim();
							}

							pe.setLineplusminus("+");
							pe.setLine(linevalue);
							pe.setJuiceplusminus("-");
							pe.setJuice("130");

							pendingEvents.add(pe);
						}			
					} else {
						index = line.indexOf(" pk");

						if (index != -1) {
							PendingEvent pe = new PendingEvent();
							pe.setGamesport("Basketball");
							pe.setGametype("NCAAB");
							pe.setLinetype("game");
							pe.setEventtype("spread");
							pe.setPosturl("");
							pe.setInet(accountName);
							pe.setCustomerid(accountId);
							pe.setAccountname(accountName);
							pe.setAccountid(accountId);
							pe.setLineplusminus("+");
							pe.setLine("0");
							pe.setJuiceplusminus("-");
							pe.setJuice("130");

							String team = line.substring(0, index).trim();
							if (team.contains(")")) {
								int tindex = team.indexOf(")");

								if (tindex != -1) {
									team = team.substring(tindex + 1).trim();
									pe.setTeam(team);
								}
							} else {
								pe.setTeam(team);
							}

							pendingEvents.add(pe);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}
}
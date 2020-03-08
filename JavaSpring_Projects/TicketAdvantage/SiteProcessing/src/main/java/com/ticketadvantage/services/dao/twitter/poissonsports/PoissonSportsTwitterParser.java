package com.ticketadvantage.services.dao.twitter.poissonsports;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class PoissonSportsTwitterParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(PoissonSportsTwitterParser.class);

	/**
	 * 
	 */
	public PoissonSportsTwitterParser() {
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
			final PoissonSportsTwitterParser theMachineEmailParser = new PoissonSportsTwitterParser();
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
			if (body.contains("CBB")) {
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

		String[] tokens = body.split("\\n");
		boolean sides = false;
		boolean totals = false;

		// Loop through the lines
		for (String line: tokens) {
			line = line.toLowerCase().trim();
			LOGGER.debug("line: " + line);

			if (line.length() > 0) {
				line = line.replace("<br>", "");

				if (line.contains("sides")) {
					sides = true;
				} else if (line.contains("totals")) {
					totals = true;
				} else {
					if (sides) {
						// 825 Missouri State 5.0 vs Drake
						int index = line.indexOf(" ");
						if (index != -1) {
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
							pe.setRotationid(line.substring(0, index));
							
							index = line.indexOf("vs");
							if (index != -1) {
								String part1 = line.substring(0, index).trim();
								int tindex = part1.lastIndexOf(" ");
								LOGGER.debug("part1: " + part1);

								// Missouri State 5.0 vs Drake
								if (tindex != -1) {
									String ln = part1.substring(tindex + 1).trim();
									
									if (ln.startsWith("-")) {
										pe.setLineplusminus("-");
										pe.setLine(ln.substring(1).trim());
									} else {
										pe.setLineplusminus("+");
										pe.setLine(ln);
									}
	
									pe.setJuiceplusminus("-");
									pe.setJuice("130");
								}
								part1 = part1.substring(0, tindex).trim();
								String part2 = line.substring(index + 2);
								pe.setTeam(part1.trim() + " vs " + part2.trim());
	
								pendingEvents.add(pe);
							}
						}
					} else if (totals) {
						// 853 Saint Joseph's (PA)/Saint Louis OVER 132.0
						int index = line.indexOf("over");
						
						if (index != -1) {
							final PendingEvent pe = new PendingEvent();
							pe.setGamesport("Basketball");
							pe.setGametype("NCAAB");
							pe.setLinetype("game");
							pe.setEventtype("total");
							pe.setPosturl("");
							pe.setInet(accountName);
							pe.setCustomerid(accountId);
							pe.setAccountname(accountName);
							pe.setAccountid(accountId);

							String part1 = line.substring(0, index).trim();
							String part2 = line.substring(index + 5).trim();
							LOGGER.debug("part1: " + part1);
							LOGGER.debug("part2: " + part2);

							int xindex = part1.indexOf(" ");
							if (xindex != -1) {
								pe.setRotationid(part1.substring(0, xindex));
								pe.setTeam(part1.substring(xindex).trim());
							}

							pe.setLineplusminus("o");
							pe.setLine(part2);
							pe.setJuiceplusminus("-");
							pe.setJuice("130");

							pendingEvents.add(pe);
						} else {
							index = line.indexOf("under");
							if (index != -1) {
								final PendingEvent pe = new PendingEvent();
								pe.setGamesport("Basketball");
								pe.setGametype("NCAAB");
								pe.setLinetype("game");
								pe.setEventtype("total");
								pe.setPosturl("");
								pe.setInet(accountName);
								pe.setCustomerid(accountId);
								pe.setAccountname(accountName);
								pe.setAccountid(accountId);

								String part1 = line.substring(0, index).trim();
								String part2 = line.substring(index + 5).trim();
								
								int xindex = part1.indexOf(" ");
								if (xindex != -1) {
									pe.setRotationid(part1.substring(0, xindex));
									pe.setTeam(part1.substring(xindex).trim());
								}

								pe.setLineplusminus("u");
								pe.setLine(part2);
								pe.setJuiceplusminus("-");
								pe.setJuice("130");

								pendingEvents.add(pe);
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}
}
package com.ticketadvantage.services.dao.twitter.jerryspicks;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class JerrysPicksTwitterParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(JerrysPicksTwitterParser.class);

	/**
	 * 
	 */
	public JerrysPicksTwitterParser() {
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
			final JerrysPicksTwitterParser theMachineEmailParser = new JerrysPicksTwitterParser();
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
			int index = body.indexOf("-");
			
			if (index != -1) {
				body = body.substring(index + 1, index + 2);

				if (body.matches("[0-9]+")) {
					// your operations
					retValue = true;
				}			
			} else {
				index = body.indexOf("+");
				
				if (index != -1) {
					body = body.substring(index + 1, index + 2);

					if (body.matches("[0-9]+")) {
						// your operations
						retValue = true;
					}			
				} else {
					index = body.indexOf("pk");

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
			line = line.trim();
			LOGGER.debug("line: " + line);

			if (line.length() > 0) {
				final PendingEvent pe = new PendingEvent();
				if (line.contains("2H")) {
					pe.setLinetype("second");
					line = line.replace("2H", "").trim();
				} else if (line.contains("1H")) {
					pe.setLinetype("first");
					line = line.replace("1H", "").trim();
				} else {
					pe.setLinetype("game");
				}
				pe.setPosturl("");
				pe.setInet(accountName);
				pe.setCustomerid(accountId);
				pe.setAccountname(accountName);
				pe.setAccountid(accountId);

				int index = line.indexOf("OVER");
				if (index != -1) {
					pe.setEventtype("total");

					// Victor Oladipo Points OVER 19.5 +100
					final String team = line.substring(0, index).trim();
					pe.setTeam(team.toLowerCase());
					line = line.substring(index + 4).trim();

					index = line.indexOf(" ");
					if (index != -1) {
						String ln = line.substring(0, index);
						line = line.substring(index + 1);
						pe.setLineplusminus("o");
						pe.setLine(ln);
						pe.setJuiceplusminus(line.substring(0, 1));
						pe.setJuice(line.substring(1).trim());
					}
					
					pendingEvents.add(pe);
				} else {
					index = line.indexOf("UNDER");
					if (index != -1) {
						pe.setEventtype("total");
						// Victor Oladipo Points UNDER 19.5 +100
						final String team = line.substring(0, index).trim();
						pe.setTeam(team.toLowerCase());
						line = line.substring(index + 5).trim();

						index = line.indexOf(" ");
						if (index != -1) {
							String ln = line.substring(0, index);
							line = line.substring(index + 1);
							pe.setLineplusminus("u");
							pe.setLine(ln);
							pe.setJuiceplusminus(line.substring(0, 1));
							pe.setJuice(line.substring(1).trim());
						}
						
						pendingEvents.add(pe);
					} else {
						index = line.indexOf("ML");
						if (index != -1) {
							pe.setEventtype("ml");
							// Atlanta Hawks ML -135
							final String team = line.substring(0, index).trim();
							pe.setTeam(team);
							line = line.substring(index + 2).trim();
							pe.setLineplusminus(line.substring(0, 1));
							pe.setLine(line.substring(1).trim());
							pe.setJuiceplusminus(line.substring(0, 1));
							pe.setJuice(line.substring(1).trim());
							pendingEvents.add(pe);
						} else {
							int numberOfMatches = 0;
							String templine = line;
							while (templine.contains("-")){
								templine = templine.replaceFirst("-", "#");
							    numberOfMatches++;
							}
							LOGGER.debug("numberOfMatches: " + numberOfMatches);
							
							if (numberOfMatches > 1) {
								index = line.indexOf("-");
								if (index != -1) {
									pe.setEventtype("spread");
									// New Orleans Saints -3 -109
									final String team = line.substring(0, index).trim();
									pe.setTeam(team.toLowerCase());
									line = line.substring(index).trim();
		
									int spindex = line.indexOf(" ");
									if (spindex != -1) {
										final String spread = line.substring(0, spindex).trim();
										pe.setLineplusminus(spread.substring(0, 1));
										pe.setLine(spread.substring(1).trim());
										final String juice = line.substring(spindex + 1).trim();
										pe.setJuiceplusminus(juice.substring(0, 1));
										pe.setJuice(juice.substring(1).trim());
									}
									
									pendingEvents.add(pe);
								}
							} else {
								numberOfMatches = 0;
								templine = line;
								while (templine.contains("+")){
									templine = templine.replaceFirst("\\+", "#");
								    numberOfMatches++;
								}

								if (numberOfMatches > 1) {
									index = line.indexOf("+");
									if (index != -1) {
										pe.setEventtype("spread");
										// New Orleans Saints -3 -109
										final String team = line.substring(0, index).trim();
										pe.setTeam(team.toLowerCase());
										line = line.substring(index).trim();

										int spindex = line.indexOf(" ");
										if (spindex != -1) {
											final String spread = line.substring(0, spindex).trim();
											pe.setLineplusminus(spread.substring(0, 1));
											pe.setLine(spread.substring(1).trim());
											final String juice = line.substring(spindex + 1).trim();
											pe.setJuiceplusminus(juice.substring(0, 1));
											pe.setJuice(juice.substring(1).trim());
										}
										
										pendingEvents.add(pe);
									}
								} else {
									int mindex = line.indexOf("-");
									int pindex = line.indexOf("+");
									
									if (mindex < pindex) {
										index = line.indexOf("-");
										if (index != -1) {
											pe.setEventtype("spread");
											// New Orleans Saints -3 -109
											final String team = line.substring(0, index).trim();
											pe.setTeam(team.toLowerCase());
											line = line.substring(index).trim();
				
											int spindex = line.indexOf(" ");
											if (spindex != -1) {
												final String spread = line.substring(0, spindex).trim();
												pe.setLineplusminus(spread.substring(0, 1));
												pe.setLine(spread.substring(1).trim());
												final String juice = line.substring(spindex + 1).trim();
												pe.setJuiceplusminus(juice.substring(0, 1));
												pe.setJuice(juice.substring(1).trim());
											}

											pendingEvents.add(pe);
										} 
									} else {
										index = line.indexOf("+");
										if (index != -1) {
											pe.setEventtype("spread");
											// New Orleans Saints -3 -109
											final String team = line.substring(0, index).trim();
											pe.setTeam(team.toLowerCase());
											line = line.substring(index).trim();

											int spindex = line.indexOf(" ");
											if (spindex != -1) {
												final String spread = line.substring(0, spindex).trim();
												pe.setLineplusminus(spread.substring(0, 1));
												pe.setLine(spread.substring(1).trim());
												final String juice = line.substring(spindex + 1).trim();
												pe.setJuiceplusminus(juice.substring(0, 1));
												pe.setJuice(juice.substring(1).trim());
											}
											
											pendingEvents.add(pe);
										}
									}
								}
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
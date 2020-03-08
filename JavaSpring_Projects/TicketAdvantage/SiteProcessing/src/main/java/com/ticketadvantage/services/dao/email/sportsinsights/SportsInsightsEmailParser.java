package com.ticketadvantage.services.dao.email.sportsinsights;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

public class SportsInsightsEmailParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(SportsInsightsEmailParser.class);

	/**
	 * 
	 */
	public SportsInsightsEmailParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String emailString = "\t\t\t\t\tCustomer Id:	WC110\r\n\t\t\t\t\t\r\nInet:	betwc\t\t\t\t\t\r\nTicket #:	192764543\t\t\t\t\t\r\nAccepted:	Sep, 28 03:47 PM\r\n\t\t\t\t\tRisk/Win:	$55.00 to win $50.00\r\n\t\t\t\t\tScheduled:	Sep, 29 09:00 PM\r\n\t\t\t\t\tSport:	WNBA\r\n\t\t\t\t\tDescription	#667 - Minnesota Lynx/Los Angeles Sparks o155½ (-110)";
			// String emailString = "\t\t\t\t\tCustomer Id:	WC110\r\n\t\t\t\t\t\r\nInet:	betwc\t\t\t\t\t\r\nTicket #:	192764543\t\t\t\t\t\r\nAccepted:	Sep, 28 03:47 PM\r\n\t\t\t\t\tRisk/Win:	$55.00 to win $50.00\r\n\t\t\t\t\tScheduled:	Sep, 29 09:00 PM\r\n\t\t\t\t\tSport:	WNBA\r\n\t\t\t\t\tDescription	#110 - Utah St pk (-105) 1st Half";
			// String emailString = "\t\t\t\t\tCustomer Id:	WC110\r\n\t\t\t\t\t\r\nInet:	betwc\t\t\t\t\t\r\nTicket #:	192764543\t\t\t\t\t\r\nAccepted:	Sep, 28 03:47 PM\r\n\t\t\t\t\tRisk/Win:	$55.00 to win $50.00\r\n\t\t\t\t\tScheduled:	Sep, 29 09:00 PM\r\n\t\t\t\t\tSport:	WNBA\r\n\t\t\t\t\tPitcher:	Action\r\n\t\t\t\t\tDescription	#110 - Utah St pk (-105) 1st Half";
			// String emailString = "New Betting Alert\r\n\r\nPlayer: WC1199\r\nAmount: 30\r\nPlaced: 1/4/2018 4:30:07 AM\r\nDetail:\r\nPARLAY (2 TEAMS) RR (10P-2T)\r\n[1760] TOTAL u79-110 (1H SETON HALL vrs 1H CREIGHTON)\r\n[1786] TOTAL u69½-110 (1H FRESNO STATE vrs 1H SAN DIEGO STATE)\r\n";
			String emailString = "MLB Alert O/U Game\n" + 
					"\n" + 
					"Date	Game	Details\n" + 
					"4/5/2018	969 Detroit Tigers vs 970 Chicago White Sox	Match on 970-Chicago White Sox Over, Wagershack (9+115) O/U Game\n" + 
					"\n" + 
					"\n" + 
					"Sports Insights, Inc\n" + 
					"100 Cummings Center Suite 226Q\n" + 
					"Beverly, MA 01915\n" + 
					"877-838-2853";
			
//			String estring = "Date	Game	Details\n4/5/2018	969 Detroit Tigers vs 970 Chicago White Sox	Match on 969-Detroit Tigers , Pinnacle (+122) Moneyline Game\nSports Insights, Inc";
			String estring = "Date	Game	Details\n4/5/2018	969 Detroit Tigers vs 970 Chicago White Sox	Match on 969-Detroit Tigers , Pinnacle (1.5-170) Spread Game\nSports Insights, Inc";

			final SportsInsightsEmailParser krackWinsParser = new SportsInsightsEmailParser();
			final Set<PendingEvent> events = krackWinsParser.parsePendingBets(estring, "acct1", "acct");
			for (PendingEvent pe : events) {
				LOGGER.debug("pe: " + pe);
			}
		} catch (Throwable be) {
			be.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String body, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.error("body: " + body);
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

//
//		Date	Game	Details
//		4/5/2018	969 Detroit Tigers vs 970 Chicago White Sox	Match on 970-Chicago White Sox Over, Wagershack (9+115) O/U Game
//
		int index = body.indexOf("Details");
		if (index != -1) {
			body = body.substring(index + 7);
			index = body.indexOf("Sports Insights");
			if (index != -1) {
				body = body.substring(0, index);
				if (body != null && body.length() > 0) {
					body = body.replace("\n", "");
					body = body.replace("\r", "");
					body = body.trim();
					index = body.indexOf("\t");
					if (index != -1) {
						final PendingEvent pendingEvent = new PendingEvent();
						String overUnder = null;

						// Get the date
						String theDate = body.substring(0, index);
						theDate = theDate.trim();
						LOGGER.debug("theDate: " + theDate);
						pendingEvent.setEventdate(theDate);

						body = body.substring(index + "\t".length());
						index = body.indexOf("\t");
						if (index != -1) {
							String rotationId = body.substring(0, index);
							rotationId = rotationId.trim();
							LOGGER.debug("rotationId: " + rotationId);

							body = body.substring(index + "\t".length());
							index = body.indexOf("Match on ");
							if (index != -1) {
								body = body.substring(index + 9);
								index = body.indexOf("-");
								if (index != -1) {
									String theRotationId = body.substring(0, index);
									LOGGER.debug("theRotationId: " + theRotationId);
									pendingEvent.setRotationid(theRotationId);

									body = body.substring(index + 1);
									index = body.indexOf(",");

									if (index != -1) {
										String beforebody = body.substring(0, index);
										int nindex = beforebody.lastIndexOf(" ");
										if (nindex != -1) {
											beforebody = beforebody.substring(nindex);
											if (beforebody != null && beforebody.length() > 0) {
												if (beforebody.contains("Over")) {
													LOGGER.debug("Over");
													overUnder = "o";
												} else if (beforebody.contains("Under")) {
													LOGGER.debug("Under");
													overUnder = "u";
												}
											}
										}
										body = body.substring(index + 1);
										body = body.trim();
									}
									// 1/17/2018	721 Tulsa vs 722 Temple	Match on 721-Tulsa Under, Wagershack (142.5+108) O/U Game
									// 4/5/2018	969 Detroit Tigers vs 970 Chicago White Sox	Match on 970-Chicago White Sox Over, Wagershack (9+115) O/U Game
									// 4/5/2018	969 Detroit Tigers vs 970 Chicago White Sox	Match on 969-Detroit Tigers , Pinnacle (+122) Moneyline Game
									// 4/5/2018	969 Detroit Tigers vs 970 Chicago White Sox	Match on 969-Detroit Tigers , Pinnacle (1.5-170) Spread Game

									final StringTokenizer st = new StringTokenizer(body," ");
									int size = st.countTokens();

									while (st.hasMoreTokens()) {
										switch (size) {
											case 0:
												String book = st.nextToken();
												pendingEvent.setPitcher(book);
												LOGGER.debug("book: " + book);
												break;
											case 1:
												String line = st.nextToken();
												pendingEvent.setLine(line);
												LOGGER.debug("line: " + line);
												break;
											case 2:
												String type = st.nextToken();
												LOGGER.debug("type: " + type);
												if (type != null && type.equals("Spread")) {
													pendingEvent.setEventtype("spread");
												} else if (type != null && type.equals("O/U")) {
													pendingEvent.setEventtype("total");
												} else if (type != null && type.equals("Moneyline")) {
													pendingEvent.setEventtype("ml");
												}
												break;
											case 3:
												final String game = st.nextToken();
												LOGGER.debug("game: " + game);
												if (game != null && game.equals("Game")) {
													pendingEvent.setLinetype("game");
												} else if (game != null && game.equals("1st")) {
													pendingEvent.setLinetype("first");
												}
												break;
										}
										size++;
									}
								}
							}
						}

						final String eventType = pendingEvent.getEventtype();
						if (eventType != null) {
							String tempLine = pendingEvent.getLine();
							tempLine = tempLine.replaceAll("\\(", "");
							tempLine = tempLine.replaceAll("\\)", "");
							if (eventType.equals("spread")) {
								if (tempLine.startsWith("-")) {
									pendingEvent.setLineplusminus("-");
									tempLine = tempLine.substring(1);
									int mindex = tempLine.indexOf("-");
									if (mindex != -1) {
										final String line = tempLine.substring(0, mindex);
										pendingEvent.setLine(line);
										pendingEvent.setJuiceplusminus("-");
										pendingEvent.setJuice(tempLine.substring(mindex + 1));
									} else {
										int pindex = tempLine.indexOf("+");
										if (pindex != -1) {
											final String line = tempLine.substring(0, pindex);
											pendingEvent.setLine(line);
											pendingEvent.setJuiceplusminus("+");
											pendingEvent.setJuice(tempLine.substring(pindex + 1));
										}
									}
								} else {
									pendingEvent.setLineplusminus("+");
									int mindex = tempLine.indexOf("-");
									if (mindex != -1) {
										final String line = tempLine.substring(0, mindex);
										pendingEvent.setLine(line);
										pendingEvent.setJuiceplusminus("-");
										pendingEvent.setJuice(tempLine.substring(mindex + 1));
									} else {
										int pindex = tempLine.indexOf("+");
										if (pindex != -1) {
											final String line = tempLine.substring(0, pindex);
											pendingEvent.setLine(line);
											pendingEvent.setJuiceplusminus("+");
											pendingEvent.setJuice(tempLine.substring(pindex + 1));
										}
									}
								}
							} else if (eventType.equals("total")) {
								pendingEvent.setLineplusminus(overUnder);
								int mindex = tempLine.indexOf("-");
								if (mindex != -1) {
									final String line = tempLine.substring(0, mindex);
									pendingEvent.setLine(line);
									pendingEvent.setJuiceplusminus("-");
									pendingEvent.setJuice(tempLine.substring(mindex + 1));
								} else {
									int pindex = tempLine.indexOf("+");
									if (pindex != -1) {
										final String line = tempLine.substring(0, pindex);
										pendingEvent.setLine(line);
										pendingEvent.setJuiceplusminus("+");
										pendingEvent.setJuice(tempLine.substring(pindex + 1));
									}
								}
							} else if (eventType.equals("ml")) {
								if (tempLine.startsWith("-")) {
									pendingEvent.setLineplusminus("-");
									pendingEvent.setJuiceplusminus("-");
									tempLine = tempLine.substring(1);
									pendingEvent.setLine(tempLine);
									pendingEvent.setJuice(tempLine);
								} else {
									pendingEvent.setLineplusminus("+");
									pendingEvent.setJuiceplusminus("+");
									tempLine = tempLine.substring(1);
									pendingEvent.setLine(tempLine);
									pendingEvent.setJuice(tempLine);									
								}
							}
						}

						pendingEvent.setTicketnum(Long.toString(System.currentTimeMillis()));
						pendingEvents.add(pendingEvent);
					}
				}
			}
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param customerid
	 * @param accountName
	 * @param accountId
	 * @param accepted
	 * @param amount
	 * @param detail
	 * @return
	 * @throws BatchException
	 */
	private PendingEvent addPendingEvent(String customerid, String accountName, String accountId, String accepted, String amount, String detail) throws BatchException {
		LOGGER.info("Entering addPendingEvent()");

		final PendingEvent pendingEvent = new PendingEvent();
		pendingEvent.setPosturl("");
		pendingEvent.setCustomerid(customerid);
		pendingEvent.setInet("BigManSports");
		pendingEvent.setAccountname(accountName);
		pendingEvent.setAccountid(accountId);
		pendingEvent.setTicketnum(String.valueOf(System.currentTimeMillis()));
		pendingEvent.setDateaccepted(accepted);
		pendingEvent.setDatecreated(new Date());
		pendingEvent.setDatemodified(new Date());
		pendingEvent.setRisk(amount);
		pendingEvent.setWin(amount);
		
		// Set the rotationID
		detail = setRotationid(detail, pendingEvent);
		LOGGER.debug("detail: " + detail);

		int bindex = detail.indexOf("TOTAL ");
		if (bindex != -1) {
			LOGGER.debug("TOTAL");
			getTotal(detail, bindex, pendingEvent);
		} else {
			LOGGER.debug("SPREAD/ML");
			getSpreadMoneyline(detail, pendingEvent);
		}

		LOGGER.info("Exiting addPendingEvent()");
		return pendingEvent;
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
	 * @throws BatchException
	 */
	private String setRotationid(String line, PendingEvent pendingEvent) throws BatchException {
		LOGGER.info("Entering setRotationid()");

		// [101] TENNESSEE TITANS +355
		if (line != null) {
			int bindex = line.indexOf("[");
			int eindex = line.indexOf("]");
			if (bindex != -1 && eindex != -1) {
				final String rotationId = line.substring(bindex + 1, eindex);
				LOGGER.debug("RotationID: " + rotationId);
				pendingEvent.setRotationid(rotationId);

				// Check what type it is
				if (rotationId.length() == 1 || rotationId.length() == 2 || rotationId.length() == 3) {
					pendingEvent.setLinetype("game");
				} else if (rotationId.startsWith("1") && rotationId.length() == 4) {
					pendingEvent.setLinetype("first");
				} else if (rotationId.startsWith("2") && rotationId.length() == 4) {
					pendingEvent.setLinetype("second");
				} else if (rotationId.startsWith("3") && rotationId.length() == 4) {
					// Determine if this is a Quarter, Period or something else
					if (line.contains("1Q")) {
						pendingEvent.setEventtype("unsupported");
						throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, line);
					} else {
						pendingEvent.setLinetype("third");
					}
				} else if (rotationId.length() >= 4) {
					pendingEvent.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, line);
				}

				line = line.substring(eindex + 2); // Skip to next thing
			}
		}

		LOGGER.info("Exiting setRotationid()");
		return line;
	}
	
	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getTotal(String html, int bindex, PendingEvent pe) {
		LOGGER.info("Entering getTotal()");
		html = html.substring(bindex + 6);
		LOGGER.debug("html: " + html);

		// Then we know we have a total
		pe.setEventtype("total");
		int eindex = html.indexOf(" ");
		if (eindex != -1) {
			String lines = html.substring(0, eindex).trim();
			LOGGER.debug("lines: " + lines);
			if (lines != null) {
				lines = lines.trim();
				// u9EV or o42½-110 or
				pe.setLineplusminus(lines.substring(0, 1));
				lines = lines.substring(1);
				int evindex = lines.indexOf("EV");
				int pkindex = lines.indexOf("PK");
	
				if (evindex != -1 || pkindex != -1) {
					if (evindex != -1) {
						LOGGER.debug("lines: " + lines);
						pe.setLine(reformatValues(lines.substring(0, evindex)));
						pe.setJuiceplusminus("+");
						pe.setJuice("100");
					} else {
						LOGGER.debug("lines: " + lines);
						pe.setLine(reformatValues(lines.substring(0, pkindex)));
						pe.setJuiceplusminus("+");
						pe.setJuice("100");
					}
				} else {
					int mindex = lines.indexOf("-");
					int pindex = lines.indexOf("+");
					if (mindex != -1 || pindex != -1) {
						if (mindex != -1) {
							pe.setLine(reformatValues(lines.substring(0, mindex)));
							pe.setJuiceplusminus("-");
							pe.setJuice(reformatValues(lines.substring(mindex + 1)));
						} else {
							pe.setLine(reformatValues(lines.substring(0, pindex)));
							pe.setJuiceplusminus("-");
							pe.setJuice(reformatValues(lines.substring(pindex + 1)));
						}
					} 
				}	
			}

			html = html.substring(eindex + 1);
			LOGGER.debug("html: " + html);
			String gameType = pe.getGametype();
			LOGGER.debug("gameType: " + gameType);
			if ("MLB".equals(gameType)) {
				if (html != null) {
					html = html.replace("\r\n", " ");
					html = html.replace("(", "");
					html = html.replace(")", "");
					pe.setTeam(html.trim());
					pe.setPitcher(html.trim());
				}
			} else {
				if (html != null) {
					html = html.replace("\r\n", " ");
					html = html.replace("(", "");
					html = html.replace(")", "");
					pe.setTeam(html.trim());
				}
			}
		}

		LOGGER.info("Exiting getTotal()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	protected void getSpreadMoneyline(String html, PendingEvent pe) {
		LOGGER.info("Entering getSpreadMoneyline()");
		LOGGER.debug("html: " + html);

		// Now determine spread or ML
		html = html.trim();
		int index = html.lastIndexOf(" ");
		if (index != -1) {
			String tempHtml = html.substring(index + 1);

			// Kansas State o62-120 (B+½)
			if (tempHtml != null && tempHtml.startsWith("(")) {
				tempHtml = html.substring(0, index);
				LOGGER.debug("tempHtml: " + tempHtml);
				index = tempHtml.lastIndexOf(" ");
				if (index != -1) {
					// Get the team while we are here
					pe.setTeam(tempHtml.substring(0, index));
					tempHtml = tempHtml.substring(index + 1);
					LOGGER.debug("tempHtml: " + tempHtml);

					// Now determine spread or ML
					determineSpreadMoney(tempHtml, pe);
				}
			} else {
				LOGGER.debug("tempHtml: " + tempHtml);
				if (tempHtml != null) {
					tempHtml = tempHtml.replace(" ", "").trim();

					// Get the team while we are here
					pe.setTeam(html.substring(0, index).trim());

					// Now determine spread or ML
					determineSpreadMoney(tempHtml.trim(), pe);
				}
			}
		}

		LOGGER.info("Exiting getSpreadMoneyline()");
	}
	
	/**
	 * 
	 * @param html
	 * @param pe
	 */
	protected void determineSpreadMoney(String html, PendingEvent pe) {
		LOGGER.info("Entering determineSpreadMoney()");
		LOGGER.debug("html: " + html);

		//
		// Spread Examples
		//
		// -1-120
		// +1+120
		// -1+120
		// +1-120
		// PK-120
		// PK+120
		// EV-120
		// EV+120
		// -1EV
		// +1EV
		// -1PK
		// +1PK
		// PKEV
		// EVPK
		// PKPK
		// EVEV

		//
		// Moneyline Examples
		//
		// +120
		// -233
		// EV
		// PK

		// Check for money first
		if ((html.startsWith("EV") || html.startsWith("PK")) && (html.length() == 2)) {
			// We have moneyline
			pe.setEventtype("ml");
			pe.setLineplusminus("+");
			pe.setLine("100");
			pe.setJuiceplusminus("+");
			pe.setJuice("100");
		} else if ((html.startsWith("+") || html.startsWith("-")) && (html.length() == 4) && (!html.endsWith("EV") && !html.endsWith("PK"))) {
			// We have moneyline
			pe.setEventtype("ml");
			pe.setLineplusminus(html.substring(0, 1));
			pe.setLine(super.reformatValues(html.substring(1)));
			pe.setJuiceplusminus(html.substring(0, 1));
			pe.setJuice(super.reformatValues(html.substring(1)));
		} else {
			// Spread
			pe.setEventtype("spread");
			if (html.startsWith("EV") || html.startsWith("PK")) {
				pe.setLineplusminus("+");
				pe.setLine("100");
				// Now get the Juice
				html = html.substring(2);
				if (html.startsWith("EV") || html.startsWith("PK")) {
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (html.startsWith("+") || html.startsWith("-")) {
					pe.setJuiceplusminus(html.substring(0, 1));
					pe.setJuice(super.reformatValues(html.substring(1)));
				}
			} else {
				pe.setLineplusminus(html.substring(0, 1));
				html = html.substring(1);

				int eindex = html.lastIndexOf("EV");
				int pindex = html.lastIndexOf("PK");
				int plindex = html.lastIndexOf("+");
				int mindex = html.lastIndexOf("-");
				LOGGER.debug("eindex: " + eindex);
				LOGGER.debug("pindex: " + pindex);
				LOGGER.debug("plindex: " + plindex);
				LOGGER.debug("mindex: " + mindex);

				// Now get the line value and juice
				if (eindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, eindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (pindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, pindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice("100");
				} else if (plindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, plindex)));
					pe.setJuiceplusminus("+");
					pe.setJuice(super.reformatValues(html.substring(plindex + 1)));
				} else if (mindex != -1) {
					pe.setLine(super.reformatValues(html.substring(0, mindex)));
					pe.setJuiceplusminus("-");
					pe.setJuice(super.reformatValues(html.substring(mindex + 1)));					
				}
			}
		}

		LOGGER.info("Exiting determineSpreadMoney()");
	}
}
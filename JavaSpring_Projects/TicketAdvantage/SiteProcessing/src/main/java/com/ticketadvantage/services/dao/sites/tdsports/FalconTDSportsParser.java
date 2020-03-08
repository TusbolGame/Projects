/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class FalconTDSportsParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(FalconTDSportsParser.class);
	// 3/18/2019 7:00:00 PM
	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a z");
		}
	};

	/**
	 * Constructor
	 */
	public FalconTDSportsParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final FalconTDSportsParser falconTDSportsParser = new FalconTDSportsParser();
			falconTDSportsParser.setTimezone("ET");

			final String json = "{\"list\":[{\"TicketNumber\":31971334,\"IdWager\":31971334,\"PlacedDate\":\"3/18/2019 9:01:27 PM\",\"HeaderDescription\":\"STRAIGHT BET\",\"HeaderDescriptionLang\":\"STRAIGHT BET\",\"RiskAmount\":240.0,\"WinAmount\":200.0,\"LoginName\":\"Internet\",\"GameDateTime\":\"3/19/2019 7:00:00 PM\",\"IdSport\":\"CBB\",\"DetailDescription\":\"[673] HOFSTRA +135\",\"DetailDescriptionLang\":\"[673] HOFSTRA +10-120\",\"PitcherChange\":false,\"ShortGame\":false,\"IdGame\":978975,\"RotationNumbers\":[674,673]}]}";
//			final String json = "{\"list\":[{\"TicketNumber\":31971334,\"IdWager\":31971334,\"PlacedDate\":\"3/18/2019 9:01:27 PM\",\"HeaderDescription\":\"STRAIGHT BET\",\"HeaderDescriptionLang\":\"STRAIGHT BET\",\"RiskAmount\":240.0,\"WinAmount\":200.0,\"LoginName\":\"Internet\",\"GameDateTime\":\"3/19/2019 7:00:00 PM\",\"IdSport\":\"CBB\",\"DetailDescription\":\"[673] HOFSTRA PKEV\",\"DetailDescriptionLang\":\"[673] HOFSTRA +10-120\",\"PitcherChange\":false,\"ShortGame\":false,\"IdGame\":978975,\"RotationNumbers\":[674,673]}]}";
//			final String json = "{\"list\":[{\"TicketNumber\":31971334,\"IdWager\":31971334,\"PlacedDate\":\"3/18/2019 9:01:27 PM\",\"HeaderDescription\":\"STRAIGHT BET\",\"HeaderDescriptionLang\":\"STRAIGHT BET\",\"RiskAmount\":240.0,\"WinAmount\":200.0,\"LoginName\":\"Internet\",\"GameDateTime\":\"3/19/2019 7:00:00 PM\",\"IdSport\":\"CBB\",\"DetailDescription\":\"[661] TOTAL o143½EV (QUINNIPIAC vrs NJIT)\",\"DetailDescriptionLang\":\"[661] TOTAL o143½-110 (QUINNIPIAC vrs NJIT)\",\"PitcherChange\":false,\"ShortGame\":false,\"IdGame\":978975,\"RotationNumbers\":[674,673]}]}";
			final Set<PendingEvent> pes = falconTDSportsParser.parsePendingBets(json, "accountName", "accountId");

			for (PendingEvent pe : pes) {
				LOGGER.error("PendingEvent: " + pe);
			}
		} catch (Throwable be) {
			LOGGER.error(be.getMessage(), be);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = super.parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden" };
		final Elements forms = doc.select("form");
		if (forms != null && forms.size() > 0) {
			for (Element form : forms) {
				// Get form action field
				map.put("action", form.attr("action"));
				getAllElementsByType(form, "input", types, map);
			}
		}

		// Check if we need to send upper case password
		if (!xhtml.contains("toLowerCase") && xhtml.contains("toUpperCase")) {
			map.put("toUpperCase", "true");
		}

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		final Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

//		{"list":
//			[
//		     	{
//				 "TicketNumber":31955246,
//		     	 "IdWager":31955246,
//		     	 "PlacedDate":"3/18/2019 1:08:34 PM",
//		     	 "HeaderDescription":"STRAIGHT BET",
//		     	 "HeaderDescriptionLang":"STRAIGHT BET",
//		     	 "RiskAmount":220.0,
//		     	 "WinAmount":200.0,
//		     	 "LoginName":"Internet",
//		     	 "GameDateTime":"3/18/2019 7:00:00 PM",
//		     	 "IdSport":"CBB",
//		     	 "DetailDescription":"[661] TOTAL o143½-110 \r(QUINNIPIAC vrs NJIT)",
//		     	 "DetailDescriptionLang":"[661] TOTAL o143½-110 \r(QUINNIPIAC vrs NJIT)",
//		     	 "PitcherChange":false,
//		     	 "ShortGame":false,
//		     	 "IdGame":979271,
//		     	 "RotationNumbers":[662,661]
//		     	}
//			]
//		}

		if (xhtml != null && xhtml.length() > 0) {
			// Get the document object
			final JSONObject obj = new JSONObject(xhtml);
	
			if (!obj.isNull("list")) {
				final JSONArray list = obj.getJSONArray("list");
				LOGGER.error("here1");
	
				if (list != null && list.length() > 0) {
					for (int x = 0; x < list.length(); x++) {
						final JSONObject bet = list.getJSONObject(x);
						final String betType = bet.getString("HeaderDescription");
						
						if (betType != null && betType.equals("STRAIGHT BET")) {
							final Integer ticketNumber = bet.getInt("TicketNumber");
							final String dateAccepted = bet.getString("PlacedDate");
							final Double riskAmount = bet.getDouble("RiskAmount");
							final Double winAmount = bet.getDouble("WinAmount");
							final String gameDate = bet.getString("GameDateTime");
							final String gameType = bet.getString("IdSport");
							final String description = bet.getString("DetailDescription");
		
							final PendingEvent pe = new PendingEvent();
							pe.setCustomerid(accountName);
							pe.setInet(accountName);
							pe.setAccountname(accountName);
							pe.setAccountid(accountId);
							pe.setTicketnum(ticketNumber.toString());
							pe.setDateaccepted(dateAccepted);
							pe.setRisk(riskAmount.toString());
							pe.setWin(winAmount.toString());
							pe.setDatecreated(new Date());
							pe.setDatemodified(new Date());
							pe.setPosturl("");
							pe.setPitcher("");
	
							// Setup gamesport and gametype
							setSport(gameType, pe);
	
							// Parse the event info
							parseEvent(description, pe);
	
							// Setup the date
							try {
								pe.setEventdate(gameDate + " " + determineTimeZone(super.timezone));
								final Date gDate = PENDING_DATE_FORMAT.get().parse(gameDate + " " + determineTimeZone(super.timezone));
								pe.setGamedate(gDate);
							} catch (Throwable t) {
								LOGGER.error("gamedate: " + gameDate + " " + determineTimeZone(super.timezone));
								LOGGER.error(t.getMessage(), t);
								pe.setGamedate(new Date());
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

	/**
	 * 
	 * @param description
	 * @param pendingEvent
	 */
	private void parseEvent(String description, PendingEvent pendingEvent) throws BatchException {
		// [673] HOFSTRA +10-120
		// [673] HOFSTRA PK-120
		// [673] HOFSTRA PKEV
		// [661] TOTAL o143½-110 (QUINNIPIAC vrs NJIT)
		// [661] TOTAL o143½-EV (QUINNIPIAC vrs NJIT)
		// [673] HOFSTRA -120
		// [673] HOFSTRA EV
		description = description.substring(1).trim();
		description = parseRotationId(description, pendingEvent);

		// Total?
		if (description.contains("TOTAL ")) {
			parseTotal(description, pendingEvent);
		} else {
			// Spread or ML?
			int index = description.indexOf(" ");
			
			if (index != -1) {
				pendingEvent.setTeam(description.substring(0, index));
				description = description.substring(index + 1);
				parseSpreadMoneyline(description, pendingEvent);
			}
		}
	}

	/**
	 * 
	 * @param description
	 * @param pendingEvent
	 */
	private void parseTotal(String description, PendingEvent pendingEvent) {
		int index = description.indexOf("TOTAL");

		if (index != -1) {
			pendingEvent.setEventtype("total");
			description = description.substring(index + 5).trim();

			if (description.startsWith("o")) {
				pendingEvent.setLineplusminus("o");
			} else if (description.startsWith("u")) {
				pendingEvent.setLineplusminus("u");
			}

			description = description.substring(1);
			index = description.indexOf(" ");

			if (index != -1) {
				final String team = description.substring(index + 1).trim();
				pendingEvent.setTeam(team);

				description = description.substring(0, index);
				index = description.indexOf("-");

				if (index != -1) {
					final String line = super.reformatValues(description.substring(0, index).trim());
					pendingEvent.setLine(line);

					pendingEvent.setJuiceplusminus(description.substring(index, index + 1));
					pendingEvent.setJuice(description.substring(index + 1));
				} else {
					index = description.indexOf("+");
					
					if (index != -1) {
						final String line = super.reformatValues(description.substring(0, index).trim());
						pendingEvent.setLine(line);

						pendingEvent.setJuiceplusminus(description.substring(index, index + 1));
						pendingEvent.setJuice(description.substring(index + 1));
					} else {
						index = description.indexOf("EV");

						if (index != -1) {
							final String line = super.reformatValues(description.substring(0, index).trim());
							pendingEvent.setLine(line);

							pendingEvent.setJuiceplusminus("+");
							pendingEvent.setJuice("100");
						}
					}
				}
			}
		}		
	}

	/**
	 * 
	 * @param description
	 * @param pendingEvent
	 */
	protected void parseSpreadMoneyline(String description, PendingEvent pendingEvent) {
		LOGGER.info("Entering parseSpreadMoneyline()");
		LOGGER.debug("description: " + description);

		// Check for money first
		if ((description.startsWith("EV") || description.startsWith("PK")) && (description.length() == 2)) {
			// We have moneyline
			pendingEvent.setEventtype("ml");
			pendingEvent.setLineplusminus("+");
			pendingEvent.setLine("100");
			pendingEvent.setJuiceplusminus("+");
			pendingEvent.setJuice("100");
		} else if ((description.startsWith("+") || description.startsWith("-")) && (description.length() == 4) && (!description.endsWith("EV") && !description.endsWith("PK"))) {
			// We have moneyline
			pendingEvent.setEventtype("ml");
			pendingEvent.setLineplusminus(description.substring(0, 1));
			pendingEvent.setLine(super.reformatValues(description.substring(1)));
			pendingEvent.setJuiceplusminus(description.substring(0, 1));
			pendingEvent.setJuice(super.reformatValues(description.substring(1)));
		} else {
			if (description.startsWith("EV") || description.startsWith("PK")) {
				// Spread
				pendingEvent.setEventtype("spread");
				pendingEvent.setLineplusminus("+");
				pendingEvent.setLine("100");
				// Now get the Juice
				description = description.substring(2);

				if (description.startsWith("EV") || description.startsWith("PK")) {
					pendingEvent.setJuiceplusminus("+");
					pendingEvent.setJuice("100");
				} else if (description.startsWith("+") || description.startsWith("-")) {
					pendingEvent.setJuiceplusminus(description.substring(0, 1));
					pendingEvent.setJuice(reformatValues(description.substring(1)));
				}
			} else if (description.startsWith("+") || description.startsWith("-")) {
				// Spread
				pendingEvent.setEventtype("spread");
				pendingEvent.setLineplusminus(description.substring(0, 1));
				description = description.substring(1);

				int eindex = description.lastIndexOf("EV");
				int pindex = description.lastIndexOf("PK");
				int plindex = description.lastIndexOf("+");
				int mindex = description.lastIndexOf("-");
				LOGGER.debug("eindex: " + eindex);
				LOGGER.debug("pindex: " + pindex);
				LOGGER.debug("plindex: " + plindex);
				LOGGER.debug("mindex: " + mindex);

				// Now get the line value and juice
				if (eindex != -1) {
					pendingEvent.setLine(reformatValues(description.substring(0, eindex)));
					pendingEvent.setJuiceplusminus("+");
					pendingEvent.setJuice("100");
				} else if (pindex != -1) {
					pendingEvent.setLine(reformatValues(description.substring(0, pindex)));
					pendingEvent.setJuiceplusminus("+");
					pendingEvent.setJuice("100");
				} else if (plindex != -1) {
					pendingEvent.setLine(reformatValues(description.substring(0, plindex)));
					pendingEvent.setJuiceplusminus("+");
					pendingEvent.setJuice(reformatValues(description.substring(plindex + 1)));
				} else if (mindex != -1) {
					pendingEvent.setLine(reformatValues(description.substring(0, mindex)));
					pendingEvent.setJuiceplusminus("-");
					pendingEvent.setJuice(super.reformatValues(description.substring(mindex + 1)));					
				}
			}
		}

		LOGGER.info("Exiting parseSpreadMoneyline()");
	}

	/**
	 * 
	 * @param description
	 * @param pendingEvent
	 * @return
	 */
	private String parseRotationId(String description, PendingEvent pendingEvent) throws BatchException {
		if (description != null) {
			int index = description.indexOf("]");

			if (index != -1) {
				final String rotationId  = description.substring(0, index);
				pendingEvent.setRotationid(rotationId);
				description = description.substring(index + 1).trim();
				
				// Check what type it is
				if (rotationId.length() == 1 || rotationId.length() == 2 || rotationId.length() == 3 || rotationId.length() == 6) {
					pendingEvent.setLinetype("game");
				} else if (rotationId.startsWith("1") && rotationId.length() == 4) {
					pendingEvent.setLinetype("first");
				} else if (rotationId.startsWith("2") && rotationId.length() == 4) {
					pendingEvent.setLinetype("second");
				} else if (rotationId.startsWith("3") && rotationId.length() == 4) {
					// Determine if this is a Quarter, Period or something else
					if (description.contains("1Q")) {
						pendingEvent.setEventtype("unsupported");
						throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, description);
					} else {
						pendingEvent.setLinetype("third");
					}
				} else if (rotationId.length() >= 4) {
					pendingEvent.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, description);
				}
			}
		}

		return description;
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
		} else if (sport.contains("CFB")) {
			pendingEvent.setGamesport("Football");
			pendingEvent.setGametype("NCAA");
		} else if (sport.contains("WNBA")) {
			pendingEvent.setGamesport("Basketball");
			pendingEvent.setGametype("WNBA");
		} else if (sport.contains("NBA")) {
			pendingEvent.setGamesport("Basketball");
			pendingEvent.setGametype("NBA");
		} else if (sport.contains("CBB")) {
			pendingEvent.setGamesport("Basketball");
			pendingEvent.setGametype("NCAA");
		} else if (sport.contains("NHL")) {
			pendingEvent.setGamesport("Hockey");
			pendingEvent.setGametype("NHL");
		} else if (sport.contains("MLB")) {
			pendingEvent.setGamesport("Baseball");
			pendingEvent.setGametype("MLB");
		}

		LOGGER.info("Exiting setSport()");
	}
}
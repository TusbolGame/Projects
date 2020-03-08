/**
 * 
 */
package com.ticketadvantage.services.dao.sites.metallica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class MetallicaMobileParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(MetallicaMobileParser.class);

	/**
	 * Constructor
	 */
	public MetallicaMobileParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LOGGER.debug(Boolean.toString(true));
/*
			String[] NCAAF_SECOND_SPORT = new String[] { "2nd Halves" };
			String[] NCAAF_SECOND_NAME = new String[] { "2nd Half", "College Football", "College Football Extra" };
			String json = "{\"Items\": { \"2nd Halves\": { \"items\": [ { \"Sport\": \"Football\", \"SportSubType\": \"College Football\", \"IsCombined\": false, \"GroupLabel\": \"College Football\", \"PeriodNumber\": 2, \"PeriodDescription\": \"2nd Half\", \"IdSportType\": 98, \"IdType\": 1, \"IdLeague\": 46, \"LeagueName\": \"NCAA Football\", \"Rank\": 0, \"SequenceNumber\": 1042, \"BetMakerOrder\": 0, \"BetMakerExpand\": true, \"EventId\": 0, \"Time\": \"\", \"WebColumn\": 1, \"WebOrder\": 2 } ], \"IdLeague\": 46, \"Description\": \"2nd Halves\" }}}";
			MetallicaMobileParser metallicaMobileParser = new MetallicaMobileParser();
			Map<String, String> map = metallicaMobileParser.parseMenu(json, NCAAF_SECOND_NAME, NCAAF_SECOND_SPORT);
			Set<String> keys = map.keySet();
			Iterator<String> itr = keys.iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				System.out.println("Key: " + key);
				System.out.println("Value: " + map.get(key));
			}
*/
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param xhtml
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		// TODO
		LOGGER.info("Exiting parsePendingBets()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		LOGGER.info("Exiting parseIndex()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		final Map<String, String> retValue = new HashMap<String, String>();

		final JSONObject obj = new JSONObject(xhtml);
		retValue.put("ErrorMessage", obj.getString("ErrorMessage"));
		retValue.put("AccessToken", obj.getString("AccessToken"));
		retValue.put("ExpirationEpoch", Integer.toString(obj.getInt("ExpirationEpoch")));
		retValue.put("ExpirationUTC", obj.getString("ExpirationUTC"));

		LOGGER.info("Exiting parseLogin()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseMenu(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		LOGGER.info("Entering parseMenu()");
		LOGGER.debug("type: " + java.util.Arrays.toString(type));
		LOGGER.debug("sport: " + java.util.Arrays.toString(sport));
		final Map<String, String> labels = new HashMap<String, String>();

		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONObject sportItems = obj.getJSONObject("Items");

		JSONObject item = null;
		try {
			item = sportItems.getJSONObject(sport[1]);
		} catch (Exception e) {
			throw new BatchException(BatchErrorCodes.SPORT_EVENT_NOT_FOUND, 
					BatchErrorMessage.SPORT_EVENT_NOT_FOUND,  
					"Sport event not found on site for " + sport[1], 
					e.getMessage());
		}

		if (item != null && item.length() > 0) {
			final JSONArray items = item.getJSONArray("items");
			for (int y = 0; y < items.length(); y++) {
				final JSONObject sItem = items.getJSONObject(y);
				final String periodDescription = sItem.getString("PeriodDescription");
				final Integer periodNumber = sItem.getInt("PeriodNumber");
				final String sportSubType = sItem.getString("SportSubType");
				final String sportType = sItem.getString("SportType");
				LOGGER.debug("periodDescription: " + periodDescription);
				LOGGER.debug("periodNumber: " + periodNumber);
				LOGGER.debug("sportSubType: " + sportSubType);
				LOGGER.debug("sportType: " + sportType);
	
				String sport1 = type[1];
	
				for (String menuitem : type) {
					LOGGER.error("menuitem: " + menuitem);

					if ((type[0].equals(periodDescription)) && 
						((periodNumber.intValue() != 2 && sport[0].equals(sportType)) || periodNumber.intValue() == 2) && 
						((periodNumber.intValue() != 0 && (sport1 == null || sport1.length() == 0)) || (sportSubType != null && sportSubType.equals(menuitem)))) {
						final StringBuffer sb = new StringBuffer();
						final JSONArray combinedItems = sItem.getJSONArray("CombinedItems");
						if (combinedItems != null && combinedItems.length() > 1) {
							for (int w = 0; w < combinedItems.length(); w++) {
								final JSONObject combinedItem = combinedItems.getJSONObject(w);
								LOGGER.debug("IdSportType: " + combinedItem.getInt("IdSportType"));
								LOGGER.debug("PeriodNumber: " + combinedItem.getInt("PeriodNumber"));
								sb.append("{\"IdSport\":").append(Integer.toString(combinedItem.getInt("IdSportType")));
								sb.append(",\"Period\":").append(Integer.toString(combinedItem.getInt("PeriodNumber")));
								sb.append("}");
								if (w != (combinedItems.length() - 1)) {
									sb.append(",");
								}
								labels.put("sport" + y, sb.toString());
							}
							sb.append("]");
						} else {
							for (int w = 0; w < combinedItems.length(); w++) {
								final JSONObject combinedItem = combinedItems.getJSONObject(w);
								LOGGER.debug("IdSportType: " + combinedItem.getInt("IdSportType"));
								LOGGER.debug("PeriodNumber: " + combinedItem.getInt("PeriodNumber"));
								sb.append("{\"IdSport\":").append(Integer.toString(combinedItem.getInt("IdSportType")));
								sb.append(",\"Period\":").append(Integer.toString(combinedItem.getInt("PeriodNumber")));
								sb.append("}");
								labels.put("sport" + y, sb.toString());
							}
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseMenu()");
		return labels;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseGames(java.lang.String, java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <SiteEventPackage> List<SiteEventPackage> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		LOGGER.info("Entering parseGame()");
		LOGGER.debug("type: " + type);
		final List<com.ticketadvantage.services.dao.sites.SiteEventPackage> events = new ArrayList<com.ticketadvantage.services.dao.sites.SiteEventPackage>();

//		xhtml = xhtml.trim();
//		xhtml = xhtml.substring(1);
//		int index = xhtml.lastIndexOf("]");
//		xhtml = xhtml.substring(0, index);
//		final JSONObject obj = new JSONObject(xhtml); 
		JSONArray ScheduleArray = new JSONArray(xhtml);
		for (int i = 0; i < ScheduleArray.length(); i++) {
			final JSONObject scheduleArrayItem = ScheduleArray.getJSONObject(i);
			JSONObject Schedule = scheduleArrayItem.getJSONObject("Schedule");

			// Limits
			int spreadMax = 1000;
			int mlMax = 1000;
			int totalMax = 1000;

			if (Schedule.has("Limits")) {
				try {
					final JSONObject Limits = Schedule.getJSONObject("Limits");
					spreadMax = Limits.getInt("Spread");
					mlMax = Limits.getInt("MoneyLine");
					totalMax = Limits.getInt("Total");
				} catch (JSONException je) {
					LOGGER.warn(je.getMessage(), je);
				}
			}

			JSONArray ScheduleList = Schedule.getJSONArray("ScheduleList");
			for (int x = 0; x < ScheduleList.length(); x++) {
				final JSONObject scheduleListItem = ScheduleList.getJSONObject(x);
				JSONArray Games = scheduleListItem.getJSONArray("Games");
				for (int y = 0; y < Games.length(); y++) {
					com.ticketadvantage.services.dao.sites.SiteEventPackage siteEventPackage = new com.ticketadvantage.services.dao.sites.SiteEventPackage();
					final JSONObject gameItem = Games.getJSONObject(y);
					JSONArray Teams = gameItem.getJSONArray("Teams");
					for (int z = 0; z < Teams.length(); z++) {
						// Team
						SiteTeamPackage team = new SiteTeamPackage();
						JSONObject teamItem = setTeam(z, Teams, gameItem, scheduleListItem, team);
						final JSONObject lineItem = teamItem.getJSONObject("Lines");
	
						// Spread
						getSpreadData(lineItem, team);
	
						// Money Lines
						getMoneyLineData(lineItem, team);
	
						// Totals
						getTotalData(z, lineItem, team);
	
						// Setup the SitePackage
						getSitePackage(z, siteEventPackage, team, spreadMax, mlMax, totalMax, events);
						LOGGER.debug("siteEventPackage: " + siteEventPackage);
					}
				}
			}
		}
		LOGGER.debug("inputFields: " + inputFields);
		LOGGER.debug("events: " + events);
		
		LOGGER.info("Exiting parseGame()");
		return (List<SiteEventPackage>)events;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseEventSelection(java.lang.String, com.ticketadvantage.services.dao.sites.SiteTeamPackage, java.lang.String)
	 */
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException {
		LOGGER.info("Entering parseEventSelection()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

      	if (xhtml.contains("The game has already started")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The game has already started", xhtml);	
		}

      	if (xhtml.contains("The status of the game has changed (such as circled)")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The status of the game has changed (such as circled)", xhtml);	
		}

      	if (xhtml.contains("The wager for this panel has already been processed or cancelled")) {
			throw new BatchException(BatchErrorCodes.WAGER_HAS_EXPIRED, BatchErrorMessage.WAGER_HAS_EXPIRED, "The wager for this panel has already been processed or cancelled", xhtml);	
		}

		final JSONObject obj = new JSONObject(xhtml);
		map.put("TicketNumber", Integer.toString(obj.getInt("TicketNumber")));
		map.put("WagerNumber", Integer.toString(obj.getInt("WagerNumber")));
		map.put("DelayUntilUTC", obj.getString("DelayUntilUTC"));
		map.put("DelayUntilSignature", obj.getString("DelayUntilSignature"));
		map.put("NeedConfirmation", Boolean.toString(obj.getBoolean("NeedConfirmation")));
		try {
			if (!obj.isNull("CaptchaImage")) {
				String captchaImage = obj.getString("CaptchaImage");
				LOGGER.error("captchaImage: " + captchaImage);
				map.put("CaptchaImage", captchaImage);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		JSONArray Bets = obj.getJSONArray("Bets");
		for (int b = 0; b < Bets.length(); b++) {
			final JSONObject betItem = Bets.getJSONObject(b);
			JSONObject Errors = betItem.getJSONObject("Errors");

			JSONArray Bet = Errors.getJSONArray("Bet");
			for (int bt = 0; bt < Bet.length(); bt++) {
				final JSONObject betError = Bet.getJSONObject(bt);
				final String description = betError.getString("Description");
				if (description != null && description.contains("Game is not available")) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, description, xhtml);
				} else if (description != null && description.contains("is greater than your amount available")) {
					throw new BatchException(BatchErrorCodes.ACCOUNT_BALANCE_LIMIT, BatchErrorMessage.ACCOUNT_BALANCE_LIMIT, description, xhtml);
				}
			}

			JSONArray Item = Errors.getJSONArray("Items");
			for (int i = 0; i < Item.length(); i++) {
				final JSONObject itemError = Item.getJSONObject(i);
				String description = itemError.getString("Description");
				if (description != null && description.contains("Game is not available")) {
					throw new BatchException(BatchErrorCodes.GAME_NOT_AVAILABLE, BatchErrorMessage.GAME_NOT_AVAILABLE, description, xhtml);
				} else if (description != null && description.contains("exceeds your account setting limit")) {
					throw new BatchException(BatchErrorCodes.MAX_WAGER_AMOUNT_REACHED, BatchErrorMessage.MAX_WAGER_AMOUNT_REACHED, description, xhtml);
				} else if (description != null && description.contains("Your maximum wager is")) {
					int bindex = description.indexOf("Your maximum wager is ");
					int eindex = description.indexOf(" until ");
					String amount = "500";
					if (bindex != -1 && eindex != -1) {
						amount = description.substring(bindex + "Your maximum wager is ".length(), eindex);
					}
					
					// Check for an amount of $1
					if (amount.equals("1")) {
						throw new BatchException(BatchErrorCodes.PLAY_CANNOT_BE_MADE_UNTIL_DAY_OF_GAME, BatchErrorMessage.PLAY_CANNOT_BE_MADE_UNTIL_DAY_OF_GAME, description, xhtml);
					} else {
						throw new BatchException(BatchErrorCodes.MAX_WAGER_UNTIL_TIME, BatchErrorMessage.MAX_WAGER_UNTIL_TIME, amount, xhtml);
					}
				} else {
					throw new BatchException(BatchErrorCodes.ADD_BET_EXCEPTION, BatchErrorMessage.ADD_BET_EXCEPTION, description, xhtml);
				}
			}

			JSONArray LineChanges = Errors.getJSONArray("LineChanges");
			for (int l = 0; l < LineChanges.length(); l++) {
				 // "BuyingOptions": [ 
				//		{ "Id": "5_184966698_9.5_-115_0_0_0", 
				//		  "Points": 9.5, 
				// 		  "Odds": -115, 
				//        "BuyPoints": 0 
				//		} 
				//	 ], "AutoAccepted": false, "PreviousId": "5_184966698_9.5_-110_0_0_0", "Id": "5_184966698_9.5_-115_0_0_0", "ErrorType": 1, "Description": ""
				final JSONObject lineErrors = LineChanges.getJSONObject(l);
				lineErrors.getString("Description");

				JSONArray BuyingOptions = lineErrors.getJSONArray("BuyingOptions");
				for (int bo = 0; bo < BuyingOptions.length(); bo++) {
					final JSONObject buyingOption = BuyingOptions.getJSONObject(bo);
//					String Id = buyingOption.getString("Id");
					final Double Points = buyingOption.getDouble("Points");
					final Double Odds = buyingOption.getDouble("Odds");

					// Through an exception
					throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "A line has changed to " + Points + " " + Odds, xhtml);
				}
			}
		}

		// Check for a wager limit and change it 
		if (xhtml.contains("exceeds your account setting limit of $")) {
			int index = xhtml.indexOf("exceeds your account setting limit of $");
			if (index != -1) {
				xhtml = xhtml.substring(index + "exceeds your account setting limit of $".length());
				index = xhtml.indexOf(". You have ");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						map.put("wageramount", wagerAmount);
					}
				}
			}
		}

		if (xhtml.contains("risk amount for your selected wagers of ")) {
			int index = xhtml.indexOf("is greater than your amount available of ");
			if (index != -1) {
				xhtml = xhtml.substring(index + "is greater than your amount available of ".length());
				index = xhtml.indexOf(".");
				if (index != -1) {
					String wagerAmount = xhtml.substring(0, index);
					if (wagerAmount != null) {
						wagerAmount = wagerAmount.replaceAll(",", "");
						wagerAmount = wagerAmount.replaceAll(" ", "");
						xhtml = xhtml.substring(index + 1);
						wagerAmount = wagerAmount + "." + xhtml.substring(0, 2);
						map.put("wageraccountexceeded", wagerAmount);
					}
				}
			}
		} 

		LOGGER.info("Exiting parseEventSelection()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseTicketNumber(java.lang.String)
	 */
	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		LOGGER.info("Entering parseTicketTransaction()");

		final JSONObject obj = new JSONObject(xhtml);
		final String ticketNumber = Integer.toString(obj.getInt("TicketNumber"));
		if (ticketNumber.equals("-1")) {
			JSONArray Bets = obj.getJSONArray("Bets");
			for (int b = 0; b < Bets.length(); b++) {
				final JSONObject betItem = Bets.getJSONObject(b);
				final JSONObject Errors = betItem.getJSONObject("Errors");

				final JSONArray Bet = Errors.getJSONArray("Bet");
				for (int bt = 0; bt < Bet.length(); bt++) {
					final JSONObject betError = Bet.getJSONObject(bt);
					// Description="Captcha Validation Fail. "
					final String description = betError.getString("Description");
					if (description != null && description.length() > 0 && description.contains("Captcha Validation Fail")) {
						throw new BatchException(BatchErrorCodes.RETRY_CAPTCHA, BatchErrorMessage.RETRY_CAPTCHA, description, xhtml);
					}
				}

				final JSONArray Item = Errors.getJSONArray("Items");
				for (int i = 0; i < Item.length(); i++) {
					final JSONObject itemError = Item.getJSONObject(i);
					itemError.getString("Description");
				}

			
				// "LineChanges":
				//  [
				//	 {"BuyingOptions":
				//     [
				//	     {"Id":"4_187508414_8.5_-115_0_0_0",
				//	      "Points":8.5,
				//	      "Odds":-115,
				//	      "BuyPoints":0.0
				//	     }
				//	   ],
				//	 "AutoAccepted":false,
				//	 "PreviousId":"4_187508414_8.5_-110_0_0_0",
				//	 "Id":"4_187508414_8.5_-115_0_0_0",
				//	 "ErrorType":1,
				//	 "Description":""
				//   }
				// ]
				final JSONArray LineChanges = Errors.getJSONArray("LineChanges");
				for (int l = 0; l < LineChanges.length(); l++) {
					 // "BuyingOptions": [ 
					//		{ "Id": "5_184966698_9.5_-115_0_0_0", 
					//		  "Points": 9.5, 
					// 		  "Odds": -115, 
					//        "BuyPoints": 0 
					//		} 
					//	 ], "AutoAccepted": false, "PreviousId": "5_184966698_9.5_-110_0_0_0", "Id": "5_184966698_9.5_-115_0_0_0", "ErrorType": 1, "Description": ""
					final JSONObject lineErrors = LineChanges.getJSONObject(l);
					lineErrors.getString("Description");

					final JSONArray BuyingOptions = lineErrors.getJSONArray("BuyingOptions");
					for (int bo = 0; bo < BuyingOptions.length(); bo++) {
						final JSONObject buyingOption = BuyingOptions.getJSONObject(bo);
//						final String Id = buyingOption.getString("Id");
						final Double Points = buyingOption.getDouble("Points");
						Double Odds = buyingOption.getDouble("Odds");

						// Through an exception
						throw new BatchException(BatchErrorCodes.LINE_CHANGED_ERROR, BatchErrorMessage.LINE_CHANGED_ERROR, "A line has changed to " + Points + " " + Odds, xhtml);
					}
				}
			}
		}

		LOGGER.info("Exiting parseTicketNumber()");
		return ticketNumber;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public Map<String, String> processLineChange(String xhtml) throws BatchException {
		LOGGER.info("Entering processLineChange()");
		final Map<String, String> map = new HashMap<String, String>();
						
//		<td align='left' style='border-top:transparent;padding:2px'>
//			<span class=thedate3 >10/25/2017 7:05 PM -  (EST)</span>
//			<br />
//			<span class=rotnumb >Denver Nuggets -2</span>
//			<span class=rotnumb > -110 for Game   </span>
//		</td>

		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray Bets = obj.getJSONArray("Bets");
		for (int b = 0; b < Bets.length(); b++) {
			final JSONObject betItem = Bets.getJSONObject(b);
			final JSONObject Errors = betItem.getJSONObject("Errors");
			final JSONArray LineChanges = Errors.getJSONArray("LineChanges");

			for (int l = 0; l < LineChanges.length(); l++) {
				 // "BuyingOptions": [ 
				//		{ "Id": "5_184966698_9.5_-115_0_0_0", 
				//		  "Points": 9.5, 
				// 		  "Odds": -115, 
				//        "BuyPoints": 0 
				//		} 
				//	 ], "AutoAccepted": false, "PreviousId": "5_184966698_9.5_-110_0_0_0", "Id": "5_184966698_9.5_-115_0_0_0", "ErrorType": 1, "Description": ""
				final JSONObject lineErrors = LineChanges.getJSONObject(l);
				final JSONArray BuyingOptions = lineErrors.getJSONArray("BuyingOptions");
				for (int bo = 0; bo < BuyingOptions.length(); bo++) {
					final JSONObject buyingOption = BuyingOptions.getJSONObject(bo);
					String Id = buyingOption.getString("Id");
					Double Points = buyingOption.getDouble("Points");
					Double Odds = buyingOption.getDouble("Odds");

					map.put("Id", Id);
					String points = Points.toString();
					if (points.startsWith("-")) {
						map.put("valueindicator", "-");
						points = points.replace("-", "");
					} else {
						// Instead of + just make it empty
						map.put("valueindicator", "");
					}
					map.put("value", points);

					final Map<String, String> juice = parseJuice(Odds.toString(), null, null);
					if (juice.get("juiceindicator") == null) {
						juice.put("juiceindicator", "");
					}

					map.put("juiceindicator", juice.get("juiceindicator"));
					map.put("juice", juice.get("juice"));
				}
			}
		}

		LOGGER.info("Entering processLineChange()");
		return map;
	}

	/**
	 * 
	 * @param json
	 * @return
	 * @throws BatchException
	 */
	public void parseConfirmation(String json) throws BatchException {
		LOGGER.info("Entering parseConfirmation()");

		final JSONObject obj = new JSONObject(json);
		final String StatusDescription = obj.getString("StatusDescription");
		final int Status = obj.getInt("Status");
		
		if (!"ACCEPTED".equals(StatusDescription)) {
			LOGGER.error("StatusDescription: " + StatusDescription);
			LOGGER.error("Status: " + Status);
			throw new BatchException(BatchErrorCodes.FAILED_TO_GET_TICKET_NUMBER, BatchErrorMessage.FAILED_TO_GET_TICKET_NUMBER, "Transaction was not accepted, status code is " + Status, json);
		}

		LOGGER.info("Exiting parseConfirmation()");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#getGameData(org.jsoup.select.Elements)
	 */
	@SuppressWarnings("unchecked")
	@Override	
	protected List<MetallicaEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		LOGGER.info("Exiting getGameData()");
		return null;
	}

	/**
	 * 
	 * @param z
	 * @param Teams
	 * @param gameItem
	 * @param scheduleListItem
	 * @param team
	 * @return
	 */
	private JSONObject setTeam(int z, JSONArray Teams, JSONObject gameItem, JSONObject scheduleListItem, SiteTeamPackage team) {
		LOGGER.info("Entering setTeam()");

		final JSONObject teamItem = Teams.getJSONObject(z);
		team.setId(teamItem.getInt("RotationNumber"));
		team.setEventid(Integer.toString(teamItem.getInt("RotationNumber")));
		team.setTeam(teamItem.getString("Name"));
		team.setTimeofevent(gameItem.getString("Time"));
		team.setDateofevent(scheduleListItem.getString("Date"));

		LOGGER.info("Exiting setTeam()");
		return teamItem;
	}

	/**
	 * 
	 * @param lineItem
	 * @param team
	 */
	private void getSpreadData(JSONObject lineItem, SiteTeamPackage team) {
		LOGGER.info("Entering getSpreadData()");

		// Spreads
		JSONArray Spreads = lineItem.getJSONArray("Spread");
		for (int s = 0; s < Spreads.length(); s++) {
			final JSONObject spreadItem = Spreads.getJSONObject(s);
			String id = spreadItem.getString("Id");
			double points = spreadItem.getDouble("Points");
			double odds = spreadItem.getDouble("Odds");
			LOGGER.debug("id: " + id);
			LOGGER.debug("points: " + points);
			LOGGER.debug("odds: " + odds);

			// line
			if (points >= 0) {
				team.addGameSpreadOptionIndicator(Integer.toString(s), "+");
			} else {
				team.addGameSpreadOptionIndicator(Integer.toString(s), "-");
			}
			team.addGameSpreadOptionNumber(Integer.toString(s), Double.toString(Math.abs(points)));

			// Juice
			if (odds >= 0) {
				team.addGameSpreadOptionJuiceIndicator(Integer.toString(s), "+");
			} else {
				team.addGameSpreadOptionJuiceIndicator(Integer.toString(s), "-");
			}
			team.addGameSpreadOptionJuiceNumber(Integer.toString(s), Double.toString(Math.abs(odds)));

			// Set the ID value
			team.addGameSpreadOptionValue(Integer.toString(s), id);
		}

		LOGGER.info("Exiting getSpreadData()");
	}

	/**
	 * 
	 * @param lineItem
	 * @param team
	 */
	private void getMoneyLineData(JSONObject lineItem, SiteTeamPackage team) {
		LOGGER.info("Entering getMoneyLineData()");

		final JSONArray MoneyLines = lineItem.getJSONArray("MoneyLine");
		for (int m = 0; m < MoneyLines.length(); m++) {
			final JSONObject moneyLineItem = MoneyLines.getJSONObject(m);
			String id = moneyLineItem.getString("Id");
			double odds = moneyLineItem.getDouble("Odds");
	
			// ID
			team.setGameMLInputId(id);
			team.setGameMLInputName(id);
	
			// Juice
			if (odds >= 0) {
				team.addGameMLOptionJuiceIndicator(Integer.toString(m), "+");
			} else {
				team.addGameMLOptionJuiceIndicator(Integer.toString(m), "-");
			}
			team.addGameMLOptionJuiceNumber(Integer.toString(m), Double.toString(Math.abs(odds)));
			team.addGameMLInputValue(Integer.toString(m), id);
		}
		
		LOGGER.info("Exiting getMoneyLineData()");
	}

	/**
	 * 
	 * @param z
	 * @param lineItem
	 * @param team
	 */
	private void getTotalData(int z, JSONObject lineItem, SiteTeamPackage team) {
		LOGGER.info("Entering getTotalData()");

		final JSONArray Totals = lineItem.getJSONArray("Total");
		for (int t = 0; t < Totals.length(); t++) {
			final JSONObject totalItem = Totals.getJSONObject(t);
			String id = totalItem.getString("Id");
			double points = totalItem.getDouble("Points");
			double odds = totalItem.getDouble("Odds");

			// line
			if (z == 0) {
				team.addGameTotalOptionIndicator(Integer.toString(t), "o");
			} else {
				team.addGameTotalOptionIndicator(Integer.toString(t), "u");
			}
			team.addGameTotalOptionNumber(Integer.toString(t), Double.toString(Math.abs(points)));
	
			// Juice
			if (odds >= 0) {
				team.addGameTotalOptionJuiceIndicator(Integer.toString(t), "+");
			} else {
				team.addGameTotalOptionJuiceIndicator(Integer.toString(t), "-");
			}
			team.addGameTotalOptionJuiceNumber(Integer.toString(t), Double.toString(Math.abs(odds)));
			
			team.addGameTotalOptionValue(Integer.toString(t), id);
		}
	
		LOGGER.info("Exiting getTotalData()");
	}

	/**
	 * 
	 * @param z
	 * @param siteEventPackage
	 * @param team
	 * @param spreadMax
	 * @param mlMax
	 * @param totalMax
	 * @param events
	 */
	private void getSitePackage(int z, com.ticketadvantage.services.dao.sites.SiteEventPackage siteEventPackage, SiteTeamPackage team, Integer spreadMax, Integer mlMax, Integer totalMax, List<com.ticketadvantage.services.dao.sites.SiteEventPackage> events) {
		LOGGER.info("Entering getSitePackage()");

		if (z == 0) {
			siteEventPackage.setSiteteamone(team);
			siteEventPackage.setId(team.getId());
			siteEventPackage.setDateofevent(team.getDateofevent());
			siteEventPackage.setTimeofevent(team.getTimeofevent());
			siteEventPackage.setSpreadMax(spreadMax);
			siteEventPackage.setMlMax(mlMax);
			siteEventPackage.setTotalMax(totalMax);
		} else {
			siteEventPackage.setSiteteamtwo(team);
			events.add(siteEventPackage);
		}

		LOGGER.info("Exiting getSitePackage()");
	}
}
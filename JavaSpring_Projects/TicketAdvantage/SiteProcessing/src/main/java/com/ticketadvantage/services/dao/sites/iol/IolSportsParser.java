/**
 * 
 */
package com.ticketadvantage.services.dao.sites.iol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class IolSportsParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(IolSportsParser.class);
	// 11/14/2017 10:00AM
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a Z");
	private String inetWagerNumber;
	private String inetSportSelection;

	/**
	 * Constructor
	 */
	public IolSportsParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "text", "password", "hidden" };
		final Elements forms = doc.select("form");
		for(int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		LOGGER.debug("xhtml: " + xhtml);
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		final Element inetWagerNumber = doc.getElementById("inetWagerNumber");
		final Element inetSportSelection = doc.getElementById("inetSportSelection");

		if (inetWagerNumber != null) {
			this.inetWagerNumber = inetWagerNumber.attr("value");
			map.put("inetWagerNumber", this.inetWagerNumber);
		}

		if (inetSportSelection != null) {
			this.inetSportSelection = inetSportSelection.attr("value");
			map.put("inetSportSelection", this.inetSportSelection);
		}

		LOGGER.info("Exiting parseLogin()");
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		final Elements trs = doc.select("#open-bets div table tbody tr");
		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				int x = 0;
				Elements tds = tr.select("td");
				final PendingEvent pe = new PendingEvent();
				for (Element td : tds) {
					LOGGER.debug("td: " + td);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);

					switch (x++) {
					case 0:
						break;
					case 1:
						this.getTicketNumberAndAcceptedDate(td, pe);
						break;
					case 2:
						// STRAIGHT BET
						if (td.html() != null && !td.html().contains("STRAIGHT BET")) {
							
						}
						break;
					case 3:
						getSportType(td, pe);
						break;
					case 4:
						getGameDate(td, pe); 
						break;
					case 5:
						getEventInfo(td, pe);
						break;
					case 6:
						getRisk(td, pe);
						break;
					case 7:
						getWin(td, pe);
						break;
					default:
						break;
					}
				}
				pe.setDoposturl(false);
				LOGGER.debug("PendingEvent: " + pe);
				pendingEvents.add(pe);
			}	
		}
		LOGGER.debug("PendingEvents: " + pendingEvents);

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getTicketNumberAndAcceptedDate(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumberAndAcceptedDate()");

		// <span class="ticket-number">#38388664</span>
		// <span class="rot-date">11/14/2017 10:00AM</span>
		final Elements spans = td.select("span");
		if (spans != null && spans.size() > 1) {
			pe.setTicketnum(super.reformatValues(spans.get(0).html().trim().replace("#", "")));
			final String dateAccepted = spans.get(1).html().trim() + " " + super.determineTimeZone(super.timezone);
			pe.setDateaccepted(dateAccepted);
		}

		LOGGER.info("Exiting getTicketNumberAndAcceptedDate()");
	}
	
	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getSportType(Element td, PendingEvent pe) {
		LOGGER.info("Entering getSportType()");

		// <td nowrap="nowrap">6/16/2017 0:26 AM</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			if ("NFL".equals(html)) {
				pe.setGamesport("Football");
				pe.setGametype(html);
			} else if ("CFB".equals(html)) {
				pe.setGamesport("Football");
				pe.setGametype("NCAA");				
			} else if ("NBA".equals(html)) {
				pe.setGamesport("Basketball");
				pe.setGametype(html);
			} else if ("CBB".equals(html)) {
				pe.setGamesport("Basketball");
				pe.setGametype("NCAA");
			} else if ("WNBA".equals(html)) {
				pe.setGamesport("Basketball");
				pe.setGametype(html);
			} else if ("NHL".equals(html)) {
				pe.setGamesport("Hockey");
				pe.setGametype(html);
			} else if ("MLB".equals(html)) {
				pe.setGamesport("Baseball");
				pe.setGametype(html);				
			} else if ("MU".equals(html)) {
				pe.setGamesport("Golf");
				pe.setGametype(html);
			}
		}

		LOGGER.info("Exiting getSportType()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getGameDate(Element td, PendingEvent pe) {
		LOGGER.info("Entering getGameDate()");

		// <td nowrap="nowrap">6/16/2017 0:26 AM</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			LOGGER.info("html: " + html);
			final String gameDate = html.trim() + " " + super.determineTimeZone(super.timezone);;
			try {
				pe.setEventdate(gameDate);
				pe.setGamedate(DATE_FORMAT.parse(gameDate));
			} catch (ParseException pex) {
				LOGGER.error(pex.getMessage(), pex);
			}
		}

		LOGGER.info("Exiting getGameDate()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getRisk(Element td, PendingEvent pe) {
		LOGGER.info("Entering getRisk()");
		LOGGER.debug("td: " + td);

		// <td align="RIGHT">550</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			html = super.reformatValues(html).trim();
			LOGGER.info("html: " + html);
			pe.setRisk(html);
		}

		LOGGER.info("Exiting getRisk()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getWin(Element td, PendingEvent pe) {
		LOGGER.info("Entering getWin()");
		LOGGER.debug("td: " + td);

		// <td align="RIGHT">  25.00</td>
		String html = td.html();
		if (html != null && html.length() > 0) {
			html = super.reformatValues(html).trim();
			LOGGER.info("html: " + html);
			pe.setWin(html);
		}

		LOGGER.info("Exiting getWin()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	private void getEventInfo(Element td, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getEventInfo()");

		// [702] MIAMI HEAT -1-110
		// [304] TOTAL u47-110  (CENTRAL MICHIGAN vrs KENT STATE)
		// [1975] TOTAL o4½-120 (1H CINCINNATI REDS vrs 1H CLEVELAND INDIANS) ( S ROMANO / T BAUER )
		// [702] MIAMI HEAT -1-110
		String html = td.html();
		int bindex = html.indexOf("[");
		int eindex = html.indexOf("]");
		if (bindex != -1 && eindex != -1) {
			String rotationId = html.substring(bindex + 1, eindex);
			pe.setRotationid(rotationId);
			
			// Check what type it is
			if (rotationId.length() == 1 || rotationId.length() == 2 || rotationId.length() == 3) {
				pe.setLinetype("game");
			} else if (rotationId.startsWith("1") && rotationId.length() == 4) {
				pe.setLinetype("first");
			} else if (rotationId.startsWith("2") && rotationId.length() == 4) {
				pe.setLinetype("second");
			} else if (rotationId.startsWith("3") && rotationId.length() == 4) {
				// Determine if this is a Quarter, Period or something else
				if (html.contains("1Q")) {
					pe.setEventtype("unsupported");
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				} else {
					pe.setLinetype("third");
				}
			} else if (rotationId.length() >= 4) {
				pe.setEventtype("unsupported");
				throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE, BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
			}
			
			html = html.substring(eindex + 2); // Skip to next thing
			LOGGER.debug("html: " + html);
			bindex = html.indexOf("TOTAL ");

			if (bindex != -1) {
				getTotal(html, bindex, pe);
			} else {
				getSpreadMoneyline(html, pe);
			}
		}

		LOGGER.info("Exiting getEventInfo()");
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

		// [917] TOTAL o8-110 (CHICAGO WHITE SOX vrs HOUSTON ASTROS) ( J SHIELDS      -R / C MORTON       -R )

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
				// [917] TOTAL o8-110 (CHICAGO WHITE SOX vrs HOUSTON ASTROS) ( J SHIELDS      -R / C MORTON       -R )
				if (html != null) {
					int tindex = html.indexOf(")");
					if (tindex != -1) {
						final String team = html.substring(0, tindex).replace("(", "").trim();
						pe.setTeam(team);

						html = html.substring(tindex + 1);
						html = html.replace("(", "").replace(")", "").trim();
						tindex = html.indexOf("/");
						if (tindex != -1) {
							pe.setPitcher(html.substring(0, tindex).trim());
							pe.setPitcher1(html.substring(0, tindex).trim());
							pe.setPitcher2(html.substring(tindex + 1).trim());
						}
					}
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
		int index = html.indexOf("(");

		if (index != -1) {
			String pitchers = html.substring(index + 1);
			pitchers = pitchers.replace("(", "").replace(")", "").trim();
			LOGGER.debug("pitchers: " + pitchers);

			int tindex = pitchers.indexOf("/");
			if (tindex != -1) {
				pe.setPitcher(pitchers.substring(0, tindex).trim());
				pe.setPitcher1(pitchers.substring(0, tindex).trim());
				pe.setPitcher2(pitchers.substring(tindex + 1).trim());
			} else {
				pe.setPitcher(pitchers);
				pe.setPitcher1(pitchers);
				pe.setPitcher2(pitchers);
			}

			String gameInfo = html.substring(0, index).trim();
			LOGGER.debug("gameInfo: " + gameInfo);
			index = gameInfo.lastIndexOf(" ");

			if (index != -1) {
				// Get the team while we are here
				pe.setTeam(gameInfo.substring(0, index));
				gameInfo = gameInfo.substring(index + 1);
				LOGGER.debug("gameInfo: " + gameInfo);

				// Now determine spread or ML
				determineSpreadMoney(gameInfo, pe);
			}
		} else {
			index = html.lastIndexOf(" ");
	
			if (index != -1) {
				String tempHtml = html.substring(index + 1);
	
				// [927] COLORADO ROCKIES +1½-125 ( K FREELAND     -L / J PAXTON       -L )
				if (tempHtml != null && tempHtml.startsWith("(")) {
					tempHtml = html.substring(0, index).trim();
					LOGGER.debug("tempHtml: " + tempHtml);
					int nindex = tempHtml.lastIndexOf(" ");
					if (nindex != -1) {
						// Get the team while we are here
						pe.setTeam(tempHtml.substring(0, nindex));
						tempHtml = tempHtml.substring(nindex + 1);
						LOGGER.debug("tempHtml: " + tempHtml);
	
						// Now determine spread or ML
						determineSpreadMoney(tempHtml, pe);
					}
				} else {
					LOGGER.debug("tempHtml: " + tempHtml);
					if (tempHtml != null) {
						int nindex = tempHtml.indexOf("(");
	
						if (nindex != -1) {
							String pitchers = tempHtml.substring(nindex + 1);
							
							String gameInfo = tempHtml.substring(0, nindex).trim();
							nindex = gameInfo.lastIndexOf(" ");
							
							if (index != -1) {
								// Get the team while we are here
								pe.setTeam(tempHtml.substring(0, nindex));
								tempHtml = tempHtml.substring(nindex + 1);
								LOGGER.debug("tempHtml: " + tempHtml);
	
								// Now determine spread or ML
								determineSpreadMoney(tempHtml, pe);
							}
						} else {
							tempHtml = tempHtml.replace(" ", "").trim();
		
							// Get the team while we are here
							pe.setTeam(html.substring(0, index).trim());
		
							// Now determine spread or ML
							determineSpreadMoney(tempHtml.trim(), pe);
						}
					}
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

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getRiskWin(Element td, PendingEvent pe) {
		LOGGER.info("Entering getRiskWin()");
		String html = td.html().trim();

		if (html != null) {			
			int index = html.indexOf("/");
			if (index != -1) {
				pe.setRisk(html.substring(0, index).trim());
				pe.setWin(html.substring(index + 1).trim());
			}
		}

		LOGGER.info("Exiting getRiskWin()");
	}

	/**
	 * 
	 * @param gamedate
	 * @param pe
	 */
	protected void setGameDate(String gamedate, PendingEvent pe) {
		LOGGER.info("Entering setGameDate()");
		LOGGER.info("gamedate: " + gamedate);

		try {
			pe.setEventdate(gamedate);
			try {
				pe.setGamedate(fixDate(gamedate + " " + determineTimeZone(super.timezone), PENDING_DATE_FORMAT.get()));
			} catch (ParseException pe1) {
				pe.setGamedate(fixDate(gamedate + " " + determineTimeZone(super.timezone), PENDING_DATE_FORMAT_2.get()));
			}
		} catch (ParseException pee) {
			LOGGER.error("gamedate: " + gamedate + " " + determineTimeZone(super.timezone));
			LOGGER.error(pee.getMessage(), pee);
			pe.setGamedate(new Date());
		} catch (NumberFormatException nfe) {
			LOGGER.error("gamedate: " + gamedate + " " + determineTimeZone(super.timezone));
			LOGGER.error(nfe.getMessage(), nfe);
			pe.setGamedate(new Date());
		}

		LOGGER.info("Exiting setGameDate()");
	}
}
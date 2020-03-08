/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsportsseven;

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
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class TDSportsSevenParser extends TDSportsParser {
	protected static final Logger LOGGER = Logger.getLogger(TDSportsSevenParser.class);
	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd hh:mm a z");
		}
	};

	/**
	 * Constructor
	 */
	public TDSportsSevenParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// FireOnSports
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[981] CHICAGO CUBS +1½-175<br>( K HENDRICKS -R / S STRASBURG -R )<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[2310] TOTAL u21½-110<br>(2H BOISE STATE vrs 2H BYU)<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>NHL<br></td><td align=\"center\">STRAIGHT BET<br>[72] ARIZONA COYOTES -183<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";

			// LvAction
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[981] CHICAGO CUBS +1½-175<br>( K HENDRICKS -R / S STRASBURG -R ) (NL - Divisional Playoffs - Game #1)<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[1309] TOTAL o23½-105<br>(1H BOISE STATE vrs 1H BYU)<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:139659800<br>Oct 07 12:16 AM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 07 12:01 AM </td><td><br>CFB<br></td><td align=\"center\">STRAIGHT BET<br>[981] CHICAGO CUBS +130<br>( ACTION ) (NL - Divisional Playoffs - Game #1)<br></td><td><br>550 / 500<br></td></tr></tbody></table></center>";

			// BetBigCity
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[348] PURDUE -3½-110 (ESPN-2)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[377] WASHINGTON STATE o60-110 (FOX)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";
//			final String xhtml = "<table id=\"CreateWagerTable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td width=\"20%\" align=\"center\">Ticket Number: 88014656<br>Oct 0703:30 PM</td><td>Internet / -1</td><td>Oct 0403:16 PM </td><td>CFB<br></td><td>STRAIGHT BET<br>[348] PURDUE +120 (ESPN-2)<br></td><td class=\"sum\">1100</td><td class=\"sum1\">1000<br></td></tr></tbody></table>";

			// GotoHCC
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[981] CUBS (CHICAGO) +1½-175<br>( K HENDRICKS-R / S STRASBURG -R ) (TV: TBS)<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[1309] 1H BOISE STATE o23½-105<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[981] CUBS (CHICAGO) +135<br>( ACTION ) (TV: TBS)<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
			
			// YoPig
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:225858515<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>CFB<br><br></td><td>STRAIGHT BET<br>[2310] 2H BYU +3-105<br></td><td><br>875 / 500<br></td><td><br>1050 / 1000<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:225858515<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>CFB<br><br></td><td>STRAIGHT BET<br>[310] TOTAL u47-110<br>(BOISE STATE vrs BYU)<br></td><td><br>1050 / 1000<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Ticket#:225858515<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>CFB<br><br></td><td>STRAIGHT BET<br>[395] LSU +135<br></td><td><br>1050 / 1000<br></td></tr></tbody></table></center>";

/*			
			final TDSportsParser tsp = new TDSportsParser();
			final Set<PendingEvent> pending = tsp.parsePendingBets(xhtml, "test1", "test2");
			final Iterator<PendingEvent> itrs = pending.iterator();
			while (itrs.hasNext()) {
				PendingEvent pe = itrs.next();
				System.out.println("PendingEvent: " + pe);
			}
*/
			try {
				//Date gDate = fixDate("Oct 10 07:05 PM" + " " + determineTimeZone("ET"), PENDING_DATE_FORMAT);
				Date gDate = fixDate("2018 Apr 21 12:10 PM " + determineTimeZone("PT"), DATE_FORMAT);
				System.out.println("GameDate: " + gDate);
			} catch (ParseException pee) {
				LOGGER.error(pee.getMessage(), pee);
			}
		} catch (Throwable t) {
			t.printStackTrace();
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
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		final Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();
		
		if (xhtml != null) {
			xhtml = xhtml.replace("Â", "");
		}

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		Elements trs = doc.select(".content_container table tbody tr");

		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				PendingEvent pe = null;
				int x = 0;
				final String className = tr.attr("class");
				if (className != null && className.contains("TrGame")) {
					try {
						final Elements tds = tr.select("td");
						pe = new PendingEvent();
						pe.setDatecreated(new Date());
						pe.setDatemodified(new Date());
						pe.setCustomerid(accountName);
						pe.setInet(accountName);
						pe.setAccountname(accountName);
						pe.setAccountid(accountId);
						for (Element td : tds) {
							LOGGER.debug("td: " + td);
							LOGGER.debug("tds.size(): " + tds.size());
							if (tds.size() == 7) {
								switch (x++) {
									case 0:
										getTicketNumber(td, pe);
										break;
									case 1:
										// do nothing
										getGameDate(td, pe);
										break;
									case 3:
										pe.setDateaccepted(td.html().trim());
										break;
									case 4:
										getEventSportAndType(td, pe);
										break;
									case 5:
									{
										final Elements divs = td.select("div");
										if (divs != null && divs.size() > 0) {
											final Element div = divs.get(0);
											getGameInfo(div, pe);
										}
										break;
									}
									case 6:
										String thtml = td.html();
										if (thtml != null) {
											int index = thtml.indexOf("/");
											if (index != -1) {
												final String risk = thtml.substring(0, index).trim();
												final String win = thtml.substring(index + 1).trim();

												pe.setRisk(risk);
												pe.setWin(win);
											}
										}
										break;
									case 2:
									default:
										break;
								}							
							}
						}
						pe.setDoposturl(false);
	
						// Quick check if WNBA
						if ("NBA".equals(pe.getGametype())) {
							String peTeam = pe.getTeam();
							if (peTeam != null) {
								for (String wnbaTeam : WNBA_TEAMS) {
									if (peTeam.contains(wnbaTeam)) {
										pe.setGametype("WNBA");
										break;
									}
								}
							}
						}
						pendingEvents.add(pe);
					} catch (BatchException be) {
						if (pe != null) {
							pe.setGamesport("");
							pe.setGametype("");
						}
					} catch (Throwable t) {
						LOGGER.error(t.getMessage(), t);
						if (pe != null) {
							pe.setGamesport("");
							pe.setGametype("");
						}
						// Get the email access token so we can update the users
						String accessToken = "";
						try {
							String clientid = "529498092660-bcn5kjtr7iqr1k7fasv28ld16hgdi8p0.apps.googleusercontent.com";
							String clientsecret = "o4VwTH0ykC3qjyeMlI7FdlaM";						
							String refreshtoken = "1/CA1PWWUpXS6Y4N0jEGtK6KnR5fCCSD4mvg2gO-ERKC4";
							String granttype = "refresh_token";
							final AccessTokenFromRefreshToken accessTokenFromRefreshToken = new AccessTokenFromRefreshToken(clientid, clientsecret, refreshtoken, granttype);
							accessToken = accessTokenFromRefreshToken.getAccessToken();
							final SendText sendText = new SendText();
							sendText.setOAUTH2_TOKEN(accessToken);
							sendText.sendTextWithMessage("9132195234@vtext.com", "Error in pendingSite " + accountName + " " + accountId + " " + tr.html());
						} catch (Throwable tt) {
							LOGGER.error(tt.getMessage(), tt);
						}
					}
				}
			}	
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		LOGGER.info("Entering parseIndex()");
		final Map<String, String> map = new HashMap<String, String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden", "text", "password", "submit" };
		final Elements forms = doc.select("form");
		if (forms != null && forms.size() > 0) {
			final Element form = forms.get(0);

			// Get form action field
			map.put("action", form.attr("action"));
			getAllElementsByType(form, "input", types, map);
		}

		// Check if we need to send upper case password
		if (!xhtml.contains("toLowerCase") && xhtml.contains("toUpperCase")) {
			map.put("toUpperCase", "true");
		}

		LOGGER.info("Exiting parseIndex()");
		return map;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		Map<String, String> map = new HashMap<String, String>();

		// Parse the xhtml
		final Document doc = parseXhtml(xhtml);

		// Get the form information
		final Elements forms = doc.select("form");
		for (int x = 0; (forms != null && x < forms.size()); x++) {
			final Element form = forms.get(x);
			if (form != null) {
				// Get form action field
				map.put("action", form.attr("action"));
				map = getAllElementsByNameByElement(form, "input", "value", map);
			}
		}

		LOGGER.info("Exiting parseLogin()");
		return map;
	}


	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getTicketNumber(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumber()");

		//
		// 31478859
		//
		String html = td.html();
		LOGGER.debug("html: " + html);
		if (html != null) {
			html = html.trim();
			pe.setTicketnum(html);
		}

		LOGGER.info("Exiting getTicketNumber()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getGameDate(Element td, PendingEvent pe) {
		LOGGER.info("Entering getGameDate()");

		final Elements divs = td.select("div");
		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String html = div.html();
			LOGGER.debug("html: " + html);
			if (html != null) {
				html = html.trim();
				setGameDate(html, pe);
			}
		}

		LOGGER.info("Exiting getGameDate()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getEventSportAndType(Element td, PendingEvent pe) {
		LOGGER.info("Entering getEventSportAndType()");

		// <br>
		// NFL
		// <br>

		final Elements divs = td.select("div");
		if (divs != null && divs.size() > 0) {
			final Element div = divs.get(0);
			String html = div.html().trim();
			LOGGER.debug("html: " + html);
			if (html != null) {
				html = html.trim();

				LOGGER.debug("html2: " + html);
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
		}

		LOGGER.info("Exiting getEventSportAndType()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getGameInfo(Element td, PendingEvent pe) throws BatchException {
		LOGGER.info("Entering getGameInfo()");
		LOGGER.debug("td: " + td);
		LOGGER.debug("PendingEvent: " + pe);

		String html = td.html().trim();
		LOGGER.debug("html: " + html);

		//
		// Spread
		//
		// [927] COLORADO ROCKIES +1½-125 ( K FREELAND     -L / J PAXTON       -L )

		// 
		// Total
		//
		// [917] TOTAL o8-110 (CHICAGO WHITE SOX vrs HOUSTON ASTROS) ( J SHIELDS      -R / C MORTON       -R )

		//
		// MoneyLine
		//
		// [923] BOSTON RED SOX -189 ( ACTION )

		// First get the rotation ID
		int bindex = html.indexOf("[");
		int eindex = html.indexOf("]");
		if (bindex != -1 && eindex != -1) {
			String rotationId = html.substring(bindex + 1, eindex);
			LOGGER.debug("rotationID: " + rotationId);
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
					throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE,
							BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
				} else {
					pe.setLinetype("third");
				}
			} else if (rotationId.length() >= 4) {
				pe.setEventtype("unsupported");
				throw new BatchException(BatchErrorCodes.UNSUPPORTED_SPORT_TYPE,
						BatchErrorMessage.UNSUPPORTED_SPORT_TYPE, html);
			}

			html = html.substring(eindex + 2); // Skip to next thing
			LOGGER.debug("html: " + html);

			bindex = html.indexOf("TOTAL ");
			if (bindex != -1) {
				LOGGER.debug("TOTAL");
				getTotal(html, bindex, pe);
			} else {
				LOGGER.debug("SPREAD/ML");
				getSpreadMoneyline(html, pe);
			}
		}

		LOGGER.info("Exiting getGameInfo()");
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
						int nindex = tempHtml.indexOf("(");
	
						if (nindex != -1) {
							String pitchers = tempHtml.substring(nindex + 1);
							
							String gameInfo = tempHtml.substring(0, nindex).trim();
							nindex = gameInfo.lastIndexOf(" ");
							
							if (nindex != -1) {
								// Get the team while we are here
								pe.setTeam(tempHtml.substring(0, index));
								tempHtml = tempHtml.substring(index + 1);
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
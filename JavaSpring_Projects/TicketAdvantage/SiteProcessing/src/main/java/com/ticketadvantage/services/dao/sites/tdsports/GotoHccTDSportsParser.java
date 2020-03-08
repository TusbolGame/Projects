/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class GotoHccTDSportsParser extends TDSportsParser {
	protected static final Logger LOGGER = Logger.getLogger(GotoHccTDSportsParser.class);
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm a z");
	protected static final String[] WNBA_TEAMS = {
		"DREAM",
		"SKY",
		"SUN",
		"FEVER",
		"LIBERTY",
		"MYSTICS",
		"WINGS",
		"SPARKS",
		"LYNX",
		"MERCURY",
		"STARS",
		"STORM",
	};

	/**
	 * Constructor
	 */
	public GotoHccTDSportsParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// GotoHCC
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[981] CUBS (CHICAGO) +1½-175<br>( K HENDRICKS-R / S STRASBURG -R ) (TV: TBS)<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
//			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[1309] 1H BOISE STATE o23½-105<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
			final String xhtml = "<center><table width=\"100%\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\"><tbody><tr class=\"TrGameOdd\"><td align=\"center\">Confirmation #:58181335<br>Oct 06 07:32 PM<br></td><td valign=\"center\">INTERNET / -1</td><td valign=\"center\">Oct 06 06:47 PM </td><td><br>MLB<br><br></td><td>STRAIGHT BET<br>[981] CUBS (CHICAGO) +135<br>( ACTION ) (TV: TBS)<br></td><td><br>875 / 500<br></td></tr></tbody></table></center>";
			
			final GotoHccTDSportsParser tsp = new GotoHccTDSportsParser();
			final Set<PendingEvent> pending = tsp.parsePendingBets(xhtml, "test1", "test2");
			final Iterator<PendingEvent> itrs = pending.iterator();
			while (itrs.hasNext()) {
				PendingEvent pe = itrs.next();
				System.out.println("PendingEvent: " + pe);
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
		Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();
		
		if (xhtml != null) {
			xhtml = xhtml.replace("Â", "");
		}

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		Elements trs = doc.select("center table tbody tr");
		if (trs != null && trs.size() > 0) {
			// Do nothing
		} else {
			trs = doc.select("#CreateWagerTable tr");
		}

		LOGGER.debug("trs: " + trs);
		if (trs != null) {
			for (Element tr : trs) {
				int x = 0;
				final String className = tr.attr("class");
				if (className != null && className.contains("TrGame")) {
					final Elements tds = tr.select("td");
					final PendingEvent pe = new PendingEvent();
					pe.setDatecreated(new Date());
					pe.setDatemodified(new Date());
					pe.setCustomerid(accountName);
					pe.setInet(accountName);
					pe.setAccountname(accountName);
					pe.setAccountid(accountId);
					for (Element td : tds) {
						LOGGER.debug("td: " + td);		
						if (tds.size() == 6) {
							switch (x++) {
								case 0:
									getTicketNumberAndDate(td, pe);
									break;
								case 1:
									// do nothing
									break;
								case 2:
									pe.setDateaccepted(td.html().trim());
									break;
								case 3:
									getEventSportAndType(td, pe);
									break;
								case 4:
									getGameInfo(td, pe);
									break;
								case 5:
									getRiskWin(td, pe);
									break;
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
									// pe.setGametype("WNBA");
									break;
								}
							}
						}
					}

					pendingEvents.add(pe);
				}
			}	
		}

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getTicketNumberAndDate(Element td, PendingEvent pe) {
		LOGGER.info("Entering getTicketNumberAndDate()");

		// Confirmation #:31478859
		// <br>
		// Aug 26 08:00 PM
		// <br>
		String html = td.html();
		LOGGER.debug("html: " + html);
		if (html != null) {
			int index = html.indexOf("Confirmation #:");
			int brindex = html.indexOf("<br>");
			if (index != -1 && brindex != -1) {
				pe.setTicketnum(html.substring(index + "Confirmation #:".length(), brindex).trim());
				html = html.substring(brindex + 4);
				html = html.replace("<br>", "").trim();
				setGameDate(html, pe);
			}
		}

		LOGGER.info("Exiting getTicketNumberAndDate()");
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
		String html = td.html().trim();
		LOGGER.debug("html: " + html);
		if (html != null) {
			html = html.replace("<br>", "");
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
			}
		}

		LOGGER.info("Exiting getEventSportAndType()");
	}

	/**
	 * 
	 * @param td
	 * @param pe
	 */
	protected void getGameInfo(Element td, PendingEvent pe) {
		LOGGER.info("Entering getGameInfo2()");
		LOGGER.debug("td: " + td);
		LOGGER.debug("PendingEvent: " + pe);
		String html = td.html().trim();
		LOGGER.debug("html: " + html);

		int index = html.indexOf("<br>");
		if (index != -1) {
			html = html.substring(index + 4);
			// First get the rotation ID
			int bindex = html.indexOf("[");
			int eindex = html.indexOf("]");
			if (bindex != -1 && eindex != -1) {
				String rotationId = html.substring(bindex + 1, eindex);
				LOGGER.debug("rotationID: " + rotationId);
				pe.setRotationid(rotationId);
				
				if (rotationId.startsWith("1") && rotationId.length() == 4) {
					pe.setLinetype("first");
				} else if (rotationId.startsWith("2") && rotationId.length() == 4) {
					pe.setLinetype("second");
				} else if (rotationId.startsWith("3") && rotationId.length() == 4) {
					pe.setLinetype("third");
				} else {
					pe.setLinetype("game");
				}

				html = html.substring(eindex + 2); // Skip to next thing
				LOGGER.debug("html: " + html);
				getSpreadTotalMoneyline(html, pe);
			}
		}

		LOGGER.info("Exiting getGameInfo2()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	protected void getSpreadTotalMoneyline(String html, PendingEvent pe) {
		LOGGER.info("Entering getSpreadTotalMoneyline()");
		LOGGER.debug("html: " + html);

		// Now determine spread or ML
		html = html.trim();
		
		int bindex = html.indexOf("<br>");
		if (bindex != -1) {
			html = html.substring(0, bindex);
			int index = html.lastIndexOf(" ");
			String tempHtml = html.substring(index + 1);
			LOGGER.debug("tempHtml: " + tempHtml);
			if (tempHtml != null) {
				tempHtml = tempHtml.trim();
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
						determineSpreadTotalMoney(tempHtml, pe);
					}
				} else {
					LOGGER.debug("tempHtml: " + tempHtml);
					if (tempHtml != null) {
						tempHtml = tempHtml.replace("<br>", "").trim();
	
						// Get the team while we are here
						pe.setTeam(html.substring(0, index).trim());
		
						// Now determine spread or ML
						determineSpreadTotalMoney(tempHtml.trim(), pe);
					}
				}
			}
		}

		LOGGER.info("Exiting getSpreadTotalMoneyline()");
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 */
	private void determineSpreadTotalMoney(String html, PendingEvent pe) {
		LOGGER.info("Entering determineSpreadTotalMoney()");
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
		} else if (html.startsWith("o") || html.startsWith("u")) {
			// We have moneyline
			pe.setEventtype("total");
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

		LOGGER.info("Exiting determineSpreadTotalMoney()");
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
			html = html.replace("<br>", "");
			
			int index = html.indexOf("/");
			if (index != -1) {
				pe.setRisk(html.substring(0, index).trim());
				pe.setWin(html.substring(index + 1).trim());
			}
		}

		LOGGER.info("Exiting getRiskWin()");
	}
}
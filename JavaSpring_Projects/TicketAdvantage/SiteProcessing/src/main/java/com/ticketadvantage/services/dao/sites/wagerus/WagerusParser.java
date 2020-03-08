/**
 * 
 */
package com.ticketadvantage.services.dao.sites.wagerus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.tdsports.TDSportsEventPackage;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsParser;
import com.ticketadvantage.services.dao.sites.tdsports.TDSportsTeamPackage;
import com.ticketadvantage.services.email.AccessTokenFromRefreshToken;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * @author jmiller
 *
 */
public class WagerusParser extends TDSportsParser {
	private static final Logger LOGGER = Logger.getLogger(WagerusParser.class);

	/**
	 * Constructor
	 */
	public WagerusParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final WagerusParser wagerusParser = new WagerusParser();
		} catch (Throwable be) {
			LOGGER.error(be.getMessage(), be);
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
		Elements trs = doc.select("#maincol center table tbody tr");
		if (trs != null && trs.size() > 0) {
			// Do nothing
			LOGGER.debug("trs: " + trs);
		}

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
									{
										Pattern p = Pattern.compile("<br>");
										Matcher m = p.matcher(td.html());
										int i = 0;
										while (m.find()) {
										    i++;
										}
										if (i == 3) {
											getGameInfo(td, pe);	
										} else {
											getGameInfo2(td, pe);
										}
										break;
									}
									case 5:
										getRiskWin(td, pe);
										break;
									default:
										break;
								}
							} else if (tds.size() == 7) {
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
									{
										Pattern p = Pattern.compile("<br>");
										Matcher m = p.matcher(td.html());
										int i = 0;
										while (m.find()) {
										    i++;
										}
										if (i == 3) {
											getGameInfo(td, pe);	
										} else {
											getGameInfo2(td, pe);
										}
										break;
									}
									case 5:
										String thtml = td.html();
										if (thtml != null) {
											thtml = thtml.replace("<br>", "").trim();
											pe.setRisk(thtml);
										}
										break;
									case 6:
										String whtml = td.html();
										if (whtml != null) {
											whtml = whtml.replace("<br>", "").trim();
											pe.setWin(whtml);
										}
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
}
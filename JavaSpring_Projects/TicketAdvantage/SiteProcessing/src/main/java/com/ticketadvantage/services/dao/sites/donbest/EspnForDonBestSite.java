/**
 * 
 */
package com.ticketadvantage.services.dao.sites.donbest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class EspnForDonBestSite extends SiteProcessor {
	private final static Logger LOGGER = Logger.getLogger(EspnForDonBestSite.class);
	private final EspnForDonBestParser efdbp = new EspnForDonBestParser();

	/**
	 * 
	 */
	public EspnForDonBestSite(String host, String username, String password) {
		super("EspnForDonBestSite", host, username, password, false, false);
		LOGGER.info("Entering EspnForDonBestSite()");
		LOGGER.info("Exiting EspnForDonBestSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			final EspnForDonBestSite efdbs = new EspnForDonBestSite("https://site.api.espn.com", "", "");
			final EspnForDonBestSite efdbs = new EspnForDonBestSite("https://www.espn.com", "", "");
			efdbs.httpClientWrapper.setupHttpClient("None");
			List<EspnForDonBestData> ncaafList = efdbs.getNcaafData();
//			List<EspnForDonBestData> ncaafList = efdbs.getNflData();

			if (ncaafList != null && !ncaafList.isEmpty()) {
				for (EspnForDonBestData efdbd : ncaafList) {
					LOGGER.error("EspnForDonBestData: " + efdbd);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<EspnForDonBestData> getNflData() {
		final List<EspnForDonBestData> footballGames = new ArrayList<EspnForDonBestData>();

		try {
			List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
			// headerValuePairs.add(new BasicNameValuePair("Host", "site.api.espn.com"));
			// headerValuePairs.add(new BasicNameValuePair("If-None-Match",
			// "W/\"86b7305088e0022196f58874f7f0f053c51a4ee2\""));

			headerValuePairs.add(new BasicNameValuePair("Host", "www.espn.com"));
			headerValuePairs
					.add(new BasicNameValuePair("If-None-Match", "W/\"32f03b4c14a63bc9393292ef814ecdec6b374f47\""));
			headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));

			for (int x = 1; x < 18; x++) {
				List<NameValuePair> retValue = super.getHttpClientWrapper().getSitePage(
						"https://www.espn.com/nfl/scoreboard/_/year/2018/seasontype/2/week/" + x, null,
						headerValuePairs);
				String html = httpClientWrapper.getCookiesAndXhtml(retValue);

				int findex = html.indexOf("window.espn.scoreboardData 	= ");
				LOGGER.debug("findex: " + findex);

				if (findex != -1) {
					html = html.substring(findex + "window.espn.scoreboardData 	= ".length());
					int eindex = html.indexOf("window.espn.scoreboardSettings = ");
					LOGGER.debug("eindex: " + eindex);

					if (eindex != -1) {
						String json = html.substring(0, eindex);
						LOGGER.debug("json: " + json);
						final List<EspnForDonBestData> efdbds = efdbp.parseFootballData(false, json);

						if (efdbds != null && !efdbds.isEmpty()) {
							for (EspnForDonBestData efdbd : efdbds) {
								footballGames.add(efdbd);
							}
						}
					}
				}
			}

			for (int x = 1; x < 6; x++) {
				if (x != 4) {
					List<NameValuePair> retValue = super.getHttpClientWrapper().getSitePage(
							"https://www.espn.com/nfl/scoreboard/_/year/2018/seasontype/3/week/" + x, null,
							headerValuePairs);
					String html = httpClientWrapper.getCookiesAndXhtml(retValue);
	
					int findex = html.indexOf("window.espn.scoreboardData 	= ");
					LOGGER.debug("findex: " + findex);
	
					if (findex != -1) {
						html = html.substring(findex + "window.espn.scoreboardData 	= ".length());
						int eindex = html.indexOf("window.espn.scoreboardSettings = ");
						LOGGER.debug("eindex: " + eindex);
	
						if (eindex != -1) {
							String json = html.substring(0, eindex);
							LOGGER.debug("json: " + json);
							final List<EspnForDonBestData> efdbds = efdbp.parseFootballData(false, json);
	
							if (efdbds != null && !efdbds.isEmpty()) {
								for (EspnForDonBestData efdbd : efdbds) {
									footballGames.add(efdbd);
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return footballGames;
	}

	/**
	 * 
	 * @return
	 */
	public List<EspnForDonBestData> getNcaafData() {
		final List<EspnForDonBestData> footballGames = new ArrayList<EspnForDonBestData>();

		try {
			List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(3);
//			headerValuePairs.add(new BasicNameValuePair("Host", "site.api.espn.com"));
//			headerValuePairs.add(new BasicNameValuePair("If-None-Match", "W/\"86b7305088e0022196f58874f7f0f053c51a4ee2\""));
			
			headerValuePairs.add(new BasicNameValuePair("Host", "www.espn.com"));
			headerValuePairs.add(new BasicNameValuePair("If-None-Match", "W/\"32f03b4c14a63bc9393292ef814ecdec6b374f47\""));
			headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));

			for (int x = 1; x < 16; x++) {
				List<NameValuePair> retValue = super.getHttpClientWrapper().getSitePage("https://www.espn.com/college-football/scoreboard/_/group/80/year/2018/seasontype/2/week/" + x, null, headerValuePairs);
				String html = httpClientWrapper.getCookiesAndXhtml(retValue);

				int findex = html.indexOf("window.espn.scoreboardData 	= ");
				LOGGER.debug("findex: " + findex);

				if (findex != -1) {
					html = html.substring(findex + "window.espn.scoreboardData 	= ".length());
					int eindex = html.indexOf("window.espn.scoreboardSettings = ");
					LOGGER.debug("eindex: " + eindex);

					if (eindex != -1) {
						String json = html.substring(0, eindex);
						LOGGER.debug("json: " + json);
						final List<EspnForDonBestData> efdbds = efdbp.parseFootballData(true, json);

						if (efdbds != null && !efdbds.isEmpty()) {
							for (EspnForDonBestData efdbd : efdbds) {
								footballGames.add(efdbd);
							}
						}
					}
				}
			}
			
			for (int x = 1; x < 16; x++) {
				List<NameValuePair> retValue = super.getHttpClientWrapper().getSitePage("https://www.espn.com/college-football/scoreboard/_/group/81/year/2018/seasontype/2/week/" + x, null, headerValuePairs);
				String html = httpClientWrapper.getCookiesAndXhtml(retValue);

				int findex = html.indexOf("window.espn.scoreboardData 	= ");
				LOGGER.debug("findex: " + findex);

				if (findex != -1) {
					html = html.substring(findex + "window.espn.scoreboardData 	= ".length());
					int eindex = html.indexOf("window.espn.scoreboardSettings = ");
					LOGGER.debug("eindex: " + eindex);

					if (eindex != -1) {
						String json = html.substring(0, eindex);
						LOGGER.debug("json: " + json);
						final List<EspnForDonBestData> efdbds = efdbp.parseFootballData(true, json);

						if (efdbds != null && !efdbds.isEmpty()) {
							for (EspnForDonBestData efdbd : efdbds) {
								footballGames.add(efdbd);
							}
						}
					}
				}
			}

			List<NameValuePair> retValue = super.getHttpClientWrapper().getSitePage("https://www.espn.com/college-football/scoreboard/_/group/80/year/2018/seasontype/3/week/1", null, headerValuePairs);
			String html = httpClientWrapper.getCookiesAndXhtml(retValue);

			int findex = html.indexOf("window.espn.scoreboardData 	= ");
			LOGGER.debug("findex: " + findex);

			if (findex != -1) {
				html = html.substring(findex + "window.espn.scoreboardData 	= ".length());
				int eindex = html.indexOf("window.espn.scoreboardSettings = ");
				LOGGER.debug("eindex: " + eindex);

				if (eindex != -1) {
					final String json = html.substring(0, eindex);
					LOGGER.debug("json: " + json);
					final List<EspnForDonBestData> efdbds = efdbp.parseFootballData(true, json);

					if (efdbds != null && !efdbds.isEmpty()) {
						for (EspnForDonBestData efdbd : efdbds) {
							footballGames.add(efdbd);
						}
					}
				}
			}

			retValue = super.getHttpClientWrapper().getSitePage("https://www.espn.com/college-football/scoreboard/_/group/81/year/2018/seasontype/3/week/1", null, headerValuePairs);
			html = httpClientWrapper.getCookiesAndXhtml(retValue);

			findex = html.indexOf("window.espn.scoreboardData 	= ");
			LOGGER.debug("findex: " + findex);

			if (findex != -1) {
				html = html.substring(findex + "window.espn.scoreboardData 	= ".length());
				int eindex = html.indexOf("window.espn.scoreboardSettings = ");
				LOGGER.debug("eindex: " + eindex);

				if (eindex != -1) {
					final String json = html.substring(0, eindex);
					LOGGER.debug("json: " + json);
					final List<EspnForDonBestData> efdbds = efdbp.parseFootballData(true, json);

					if (efdbds != null && !efdbds.isEmpty()) {
						for (EspnForDonBestData efdbd : efdbds) {
							footballGames.add(efdbd);
						}
					}
				}
			}

/*
			for (int x = 1; x < 2; x++) {
				final String xhtml = super.getJSONSite("https://site.api.espn.com/apis/site/v2/sports/football/college-football/scoreboard?lang=en&region=us&calendartype=blacklist&limit=300&group=80&dates=2018&seasontype=2&week=" + x, headerValuePairs);
				final List<EspnForDonBestData> efdbds = efdbp.parseFootballData(xhtml);

				if (efdbds != null && !efdbds.isEmpty()) {
					for (EspnForDonBestData efdbd : efdbds) {
						footballGames.add(efdbd);
					}
				}
			}
*/
/*
			final String xhtml = super.getJSONSite("https://bam.nr-data.net/events/1/11e6ffb0d9?a=317047613&sa=1&v=1123.df1c7f8&t=Unnamed%20Transaction&rst=98701&ref=https://www.espn.com/college-football/scoreboard/_/group/80/year/2018/seasontype/3/week/1", headerValuePairs);
			final List<EspnForDonBestData> efdbds = efdbp.parseFootballData(xhtml);

			if (efdbds != null && !efdbds.isEmpty()) {
				for (EspnForDonBestData efdbd : efdbds) {
					footballGames.add(efdbd);
				}
			}
*/
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return footballGames;
	}

	/**
	 * 
	 * @param proxy
	 */
	public void setProxy(String proxy) {
		LOGGER.info("Entering setProxy()");

		try {
			this.httpClientWrapper.setupHttpClient(proxy);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting setProxy()");
	}

	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage,
			SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String loginToSite(String username, String password) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}
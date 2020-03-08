/**
 * 
 */
package com.ticketadvantage.services.dao.sites.oddsmaker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
public class OddsmakerParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(OddsmakerParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyy-MM-dd hh:mm z");

	/**
	 * Constructor
	 */
	public OddsmakerParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LOGGER.debug(Boolean.toString(true));
			String json = "[{\"sports\":{\"id\":0,\"sportsID\":23,\"event_count\":221,\"sportsType\":\"Football\",\"sportsDisplayName\":\"Football\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/football.svg\",\"link\":\"\\/sportsbook\\/football\",\"section\":[{\"sectionID\":\"720\",\"sectionType\":\"NFL\",\"sectionDisplayName\":\"NFL\",\"hasActiveEvents\":\"true\",\"eventCount\":\"16\",\"leagueType\":\"NFL Football\",\"link\":\"\\/sportsbook\\/football\\/nfl\",\"event_count\":16},{\"sectionID\":\"718\",\"sectionType\":\"HF1-NFL\",\"sectionDisplayName\":\"NFL 1st Half\",\"hasActiveEvents\":\"true\",\"eventCount\":\"15\",\"leagueType\":\"NFL Football\",\"link\":\"\\/sportsbook\\/football\\/nfl-1st-half\",\"event_count\":15},{\"sectionID\":\"25343\",\"sectionType\":\"Rams vs Bears Props (NBC)\",\"sectionDisplayName\":\"Rams vs Bears Props (NBC)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"11\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/rams-vs-bears-props-nbc\",\"event_count\":11},{\"sectionID\":\"25344\",\"sectionType\":\"Vikings vs Seahawks Props (ESPN)\",\"sectionDisplayName\":\"Vikings vs Seahawks Props (ESPN)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"11\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/vikings-vs-seahawks-props-espn\",\"event_count\":11},{\"sectionID\":\"15354\",\"sectionType\":\"Colts vs Texans Props\",\"sectionDisplayName\":\"Colts vs Texans Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/colts-vs-texans-props\",\"event_count\":9},{\"sectionID\":\"15616\",\"sectionType\":\"Eagles vs Cowboys Props\",\"sectionDisplayName\":\"Eagles vs Cowboys Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"11\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/eagles-vs-cowboys-props\",\"event_count\":11},{\"sectionID\":\"15767\",\"sectionType\":\"Giants vs Redskins Props\",\"sectionDisplayName\":\"Giants vs Redskins Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/giants-vs-redskins-props\",\"event_count\":9},{\"sectionID\":\"15856\",\"sectionType\":\"Saints vs Buccaneers Props\",\"sectionDisplayName\":\"Saints vs Buccaneers Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/saints-vs-buccaneers-props\",\"event_count\":9},{\"sectionID\":\"15974\",\"sectionType\":\"Jets vs Bills Props\",\"sectionDisplayName\":\"Jets vs Bills Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/jets-vs-bills-props\",\"event_count\":9},{\"sectionID\":\"15976\",\"sectionType\":\"Patriots vs Dolphins Props\",\"sectionDisplayName\":\"Patriots vs Dolphins Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/patriots-vs-dolphins-props\",\"event_count\":9},{\"sectionID\":\"25345\",\"sectionType\":\"Ravens vs Chiefs Props\",\"sectionDisplayName\":\"Ravens vs Chiefs Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/ravens-vs-chiefs-props\",\"event_count\":9},{\"sectionID\":\"25346\",\"sectionType\":\"Panthers vs Browns Props\",\"sectionDisplayName\":\"Panthers vs Browns Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/panthers-vs-browns-props\",\"event_count\":9},{\"sectionID\":\"25348\",\"sectionType\":\"Broncos vs 49ers Props\",\"sectionDisplayName\":\"Broncos vs 49ers Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/broncos-vs-49ers-props\",\"event_count\":9},{\"sectionID\":\"25349\",\"sectionType\":\"Bengals vs Chargers Props\",\"sectionDisplayName\":\"Bengals vs Chargers Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/bengals-vs-chargers-props\",\"event_count\":9},{\"sectionID\":\"25350\",\"sectionType\":\"Lions vs Cardinals Props\",\"sectionDisplayName\":\"Lions vs Cardinals Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/lions-vs-cardinals-props\",\"event_count\":9},{\"sectionID\":\"25351\",\"sectionType\":\"Steelers vs Raiders Props\",\"sectionDisplayName\":\"Steelers vs Raiders Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/steelers-vs-raiders-props\",\"event_count\":9},{\"sectionID\":\"25352\",\"sectionType\":\"Falcons vs Packers Props\",\"sectionDisplayName\":\"Falcons vs Packers Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NFL Props\",\"link\":\"\\/sportsbook\\/football\\/falcons-vs-packers-props\",\"event_count\":9},{\"sectionID\":\"692\",\"sectionType\":\"NCAA\",\"sectionDisplayName\":\"NCAA\",\"hasActiveEvents\":\"true\",\"eventCount\":\"40\",\"leagueType\":\"NCAA Football\",\"link\":\"\\/sportsbook\\/football\\/ncaa\",\"event_count\":40},{\"sectionID\":\"12641\",\"sectionType\":\"NFL Futures\",\"sectionDisplayName\":\"NFL Futures\",\"hasActiveEvents\":\"true\",\"eventCount\":\"8\",\"leagueType\":\"NFL Futures\",\"link\":\"\\/sportsbook\\/football\\/nfl-futures\",\"event_count\":8},{\"sectionID\":\"12642\",\"sectionType\":\"NCAA Football Futures\",\"sectionDisplayName\":\"NCAA Football Futures\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"NCAA Football Futures\",\"link\":\"\\/sportsbook\\/football\\/ncaa-football-futures\",\"event_count\":1}]}},{\"sports\":{\"id\":1,\"sportsID\":12,\"event_count\":32,\"sportsType\":\"Basketball\",\"sportsDisplayName\":\"Basketball\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/basketball.svg\",\"link\":\"\\/sportsbook\\/basketball\",\"section\":[{\"sectionID\":\"369\",\"sectionType\":\"NBA\",\"sectionDisplayName\":\"NBA\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"NBA Basketball\",\"link\":\"\\/sportsbook\\/basketball\\/nba\",\"event_count\":4},{\"sectionID\":\"394\",\"sectionType\":\"NCAA Men's\",\"sectionDisplayName\":\"NCAA Men's\",\"hasActiveEvents\":\"true\",\"eventCount\":\"15\",\"leagueType\":\"NCAA Men's Basketball\",\"link\":\"\\/sportsbook\\/basketball\\/ncaa-mens\",\"event_count\":15},{\"sectionID\":\"25385\",\"sectionType\":\"NBA Christmas Day Props\",\"sectionDisplayName\":\"NBA Christmas Day Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"NBA Game Props\",\"link\":\"\\/sportsbook\\/basketball\\/nba-christmas-day-props\",\"event_count\":4},{\"sectionID\":\"23667\",\"sectionType\":\"NBA-Futures\",\"sectionDisplayName\":\"NBA-Futures\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"NBA-Futures\",\"link\":\"\\/sportsbook\\/basketball\\/nbafutures\",\"event_count\":9}]}},{\"sports\":{\"id\":2,\"sportsID\":30,\"event_count\":14,\"sportsType\":\"Hockey\",\"sportsDisplayName\":\"Hockey\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/hockey.svg\",\"link\":\"\\/sportsbook\\/hockey\",\"section\":[{\"sectionID\":\"1224\",\"sectionType\":\"NHL\",\"sectionDisplayName\":\"NHL\",\"hasActiveEvents\":\"true\",\"eventCount\":\"7\",\"leagueType\":\"NHL Hockey\",\"link\":\"\\/sportsbook\\/hockey\\/nhl\",\"event_count\":7},{\"sectionID\":\"1164\",\"sectionType\":\"NHL Futures\",\"sectionDisplayName\":\"NHL Futures\",\"hasActiveEvents\":\"true\",\"eventCount\":\"7\",\"leagueType\":\"Hockey\",\"link\":\"\\/sportsbook\\/hockey\\/nhl-futures\",\"event_count\":7}]}},{\"sports\":{\"id\":3,\"sportsID\":29,\"event_count\":3,\"sportsType\":\"Golf\",\"sportsDisplayName\":\"Golf\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/golf.svg\",\"link\":\"\\/sportsbook\\/golf\",\"section\":[{\"sectionID\":\"20363\",\"sectionType\":\"Masters\",\"sectionDisplayName\":\"Masters\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Golf\",\"link\":\"\\/sportsbook\\/golf\\/masters\",\"event_count\":1},{\"sectionID\":\"25377\",\"sectionType\":\"South African Open\",\"sectionDisplayName\":\"South African Open\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Golf\",\"link\":\"\\/sportsbook\\/golf\\/south-african-open\",\"event_count\":1},{\"sectionID\":\"22269\",\"sectionType\":\"Tiger Woods Props\",\"sectionDisplayName\":\"Tiger Woods Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Golf Props\",\"link\":\"\\/sportsbook\\/golf\\/tiger-woods-props\",\"event_count\":1}]}},{\"sports\":{\"id\":4,\"sportsID\":62,\"event_count\":2,\"sportsType\":\"Tennis\",\"sportsDisplayName\":\"Tennis\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/tennis.svg\",\"link\":\"\\/sportsbook\\/tennis\",\"section\":[{\"sectionID\":\"13991\",\"sectionType\":\"Australian Open Men\",\"sectionDisplayName\":\"Australian Open Men\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Tennis\",\"link\":\"\\/sportsbook\\/tennis\\/australian-open-men\",\"event_count\":1},{\"sectionID\":\"13992\",\"sectionType\":\"Australian Open Women\",\"sectionDisplayName\":\"Australian Open Women\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Tennis\",\"link\":\"\\/sportsbook\\/tennis\\/australian-open-women\",\"event_count\":1}]}},{\"sports\":{\"id\":5,\"sportsID\":37,\"event_count\":111,\"sportsType\":\"International Soccer\",\"sportsDisplayName\":\"International Soccer\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/international-soccer.svg\",\"link\":\"\\/sportsbook\\/international-soccer\",\"section\":[{\"sectionID\":\"24743\",\"sectionType\":\"Copa Libertadores Final - 2nd  Leg \",\"sectionDisplayName\":\"Copa Libertadores: Final (2nd Leg)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"3\",\"leagueType\":\"Copa Libertadores Final - 2nd  Leg \",\"link\":\"\\/sportsbook\\/international-soccer\\/copa-libertadores-final-2nd-leg\",\"event_count\":3},{\"sectionID\":\"22792\",\"sectionType\":\"Copa Sudamericana \",\"sectionDisplayName\":\"Copa Sudamericana\",\"hasActiveEvents\":\"true\",\"eventCount\":\"3\",\"leagueType\":\"Copa Sudamericana \",\"link\":\"\\/sportsbook\\/international-soccer\\/copa-sudamericana\",\"event_count\":3},{\"sectionID\":\"22805\",\"sectionType\":\"England Premier League \",\"sectionDisplayName\":\"English Premier League\",\"hasActiveEvents\":\"true\",\"eventCount\":\"6\",\"leagueType\":\"England Premier League \",\"link\":\"\\/sportsbook\\/international-soccer\\/english-premier-league\",\"event_count\":6},{\"sectionID\":\"22806\",\"sectionType\":\"Italy Serie A \",\"sectionDisplayName\":\"Italian Serie A\",\"hasActiveEvents\":\"true\",\"eventCount\":\"18\",\"leagueType\":\"Italy Serie A \",\"link\":\"\\/sportsbook\\/international-soccer\\/italian-serie-a\",\"event_count\":18},{\"sectionID\":\"22804\",\"sectionType\":\"Spain La Liga \",\"sectionDisplayName\":\"Spanish La Liga\",\"hasActiveEvents\":\"true\",\"eventCount\":\"15\",\"leagueType\":\"Spain La Liga \",\"link\":\"\\/sportsbook\\/international-soccer\\/spanish-la-liga\",\"event_count\":15},{\"sectionID\":\"22802\",\"sectionType\":\"German Bundesliga \",\"sectionDisplayName\":\"German Bundesliga\",\"hasActiveEvents\":\"true\",\"eventCount\":\"6\",\"leagueType\":\"German Bundesliga \",\"link\":\"\\/sportsbook\\/international-soccer\\/german-bundesliga\",\"event_count\":6},{\"sectionID\":\"22801\",\"sectionType\":\"France Ligue 1 \",\"sectionDisplayName\":\"France Ligue 1\",\"hasActiveEvents\":\"true\",\"eventCount\":\"6\",\"leagueType\":\"France Ligue 1 \",\"link\":\"\\/sportsbook\\/international-soccer\\/france-ligue-1\",\"event_count\":6},{\"sectionID\":\"22803\",\"sectionType\":\"Netherlands Eredivisie \",\"sectionDisplayName\":\"Dutch Eredivisie\",\"hasActiveEvents\":\"true\",\"eventCount\":\"12\",\"leagueType\":\"Netherlands Eredivisie \",\"link\":\"\\/sportsbook\\/international-soccer\\/dutch-eredivisie\",\"event_count\":12},{\"sectionID\":\"22800\",\"sectionType\":\"Belgium Jupiler League \",\"sectionDisplayName\":\"Belgian First Division A\",\"hasActiveEvents\":\"true\",\"eventCount\":\"9\",\"leagueType\":\"Belgium Jupiler League \",\"link\":\"\\/sportsbook\\/international-soccer\\/belgian-first-division-a\",\"event_count\":9},{\"sectionID\":\"25371\",\"sectionType\":\"Hannover 96 at Fsv Mainz 05 Props (FS2)\",\"sectionDisplayName\":\"Hannover 96 at Fsv Mainz 05 Props (FS2)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"Game Props\",\"link\":\"\\/sportsbook\\/international-soccer\\/hannover-96-at-fsv-mainz-05-props-fs2\",\"event_count\":4},{\"sectionID\":\"25369\",\"sectionType\":\"Real Madrid at Huesca Props (beIN)\",\"sectionDisplayName\":\"Real Madrid at Huesca Props (beIN)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"Game Props\",\"link\":\"\\/sportsbook\\/international-soccer\\/real-madrid-at-huesca-props-bein\",\"event_count\":4},{\"sectionID\":\"25368\",\"sectionType\":\"Wolverhampton Wanderers at Newcastle United Props (NBCSN)\",\"sectionDisplayName\":\"Wolverhampton Wanderers at Newcastle United Props (NBCSN)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"Game Props\",\"link\":\"\\/sportsbook\\/international-soccer\\/wolverhampton-wanderers-at-newcastle-united-props-nbcsn\",\"event_count\":4},{\"sectionID\":\"25372\",\"sectionType\":\"Vfb Stuttgart at Borussia Monchengladbach Props (FS2)\",\"sectionDisplayName\":\"Vfb Stuttgart at Borussia Monchengladbach Props (FS2)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"Game Props\",\"link\":\"\\/sportsbook\\/international-soccer\\/vfb-stuttgart-at-borussia-monchengladbach-props-fs2\",\"event_count\":4},{\"sectionID\":\"25370\",\"sectionType\":\"Real Valladolid at Real Sociedad Props (beIN)\",\"sectionDisplayName\":\"Real Valladolid at Real Sociedad Props (beIN)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"Game Props\",\"link\":\"\\/sportsbook\\/international-soccer\\/real-valladolid-at-real-sociedad-props-bein\",\"event_count\":4},{\"sectionID\":\"25374\",\"sectionType\":\"Watford at Everton Props (NBCSN)\",\"sectionDisplayName\":\"Watford at Everton Props (NBCSN)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"Game Props\",\"link\":\"\\/sportsbook\\/international-soccer\\/watford-at-everton-props-nbcsn\",\"event_count\":4},{\"sectionID\":\"25375\",\"sectionType\":\"Girona at Athletic Bilbao Props (beIN)\",\"sectionDisplayName\":\"Girona at Athletic Bilbao Props (beIN)\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"Game Props\",\"link\":\"\\/sportsbook\\/international-soccer\\/girona-at-athletic-bilbao-props-bein\",\"event_count\":4},{\"sectionID\":\"23576\",\"sectionType\":\"International Soccer Futures\",\"sectionDisplayName\":\"International Soccer Futures\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"International Soccer Futures\",\"link\":\"\\/sportsbook\\/international-soccer\\/international-soccer-futures\",\"event_count\":1},{\"sectionID\":\"23863\",\"sectionType\":\"UEFA Champions League Futures\",\"sectionDisplayName\":\"UEFA Champions League Futures\",\"hasActiveEvents\":\"true\",\"eventCount\":\"4\",\"leagueType\":\"International Soccer Futures\",\"link\":\"\\/sportsbook\\/international-soccer\\/uefa-champions-league-futures\",\"event_count\":4}]}},{\"sports\":{\"id\":6,\"sportsID\":40,\"event_count\":4,\"sportsType\":\"Mixed Martial Arts\",\"sportsDisplayName\":\"Mixed Martial Arts\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/mixed-martial-arts.svg\",\"link\":\"\\/sportsbook\\/mixed-martial-arts\",\"section\":[{\"sectionID\":\"24715\",\"sectionType\":\"UFC 232\",\"sectionDisplayName\":\"UFC 232\",\"hasActiveEvents\":\"true\",\"eventCount\":\"2\",\"leagueType\":\"Mixed Martial Arts\",\"link\":\"\\/sportsbook\\/mixed-martial-arts\\/ufc-232\",\"event_count\":2},{\"sectionID\":\"24842\",\"sectionType\":\"UFC 233\",\"sectionDisplayName\":\"UFC 233\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Mixed Martial Arts\",\"link\":\"\\/sportsbook\\/mixed-martial-arts\\/ufc-233\",\"event_count\":1},{\"sectionID\":\"24244\",\"sectionType\":\"Conor vs Khabib Props\",\"sectionDisplayName\":\"Conor vs Khabib Props\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Mixed Martial Arts\",\"link\":\"\\/sportsbook\\/mixed-martial-arts\\/conor-vs-khabib-props\",\"event_count\":1}]}},{\"sports\":{\"id\":7,\"sportsID\":10,\"event_count\":4,\"sportsType\":\"Baseball\",\"sportsDisplayName\":\"Baseball\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/baseball.svg\",\"link\":\"\\/sportsbook\\/baseball\",\"section\":[{\"sectionID\":\"12278\",\"sectionType\":\"Major League Baseball Futures\",\"sectionDisplayName\":\"MLB Futures\",\"hasActiveEvents\":\"true\",\"eventCount\":\"3\",\"leagueType\":\"Major League Baseball Futures\",\"link\":\"\\/sportsbook\\/baseball\\/mlb-futures\",\"event_count\":3},{\"sectionID\":\"12799\",\"sectionType\":\"MLB Player Futures\",\"sectionDisplayName\":\"MLB Player Futures\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Major League Baseball Futures\",\"link\":\"\\/sportsbook\\/baseball\\/mlb-player-futures\",\"event_count\":1}]}},{\"sports\":{\"id\":8,\"sportsID\":14,\"event_count\":5,\"sportsType\":\"Boxing\",\"sportsDisplayName\":\"Boxing\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/boxing.svg\",\"link\":\"\\/sportsbook\\/boxing\",\"section\":[{\"sectionID\":\"410\",\"sectionType\":\"Boxing\",\"sectionDisplayName\":\"Boxing\",\"hasActiveEvents\":\"true\",\"eventCount\":\"5\",\"leagueType\":\"Boxing\",\"link\":\"\\/sportsbook\\/boxing\\/boxing\",\"event_count\":5}]}},{\"sports\":{\"id\":9,\"sportsID\":77,\"event_count\":1,\"sportsType\":\"Rugby Union\",\"sportsDisplayName\":\"Rugby Union\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/rugby-union.svg\",\"link\":\"\\/sportsbook\\/rugby-union\",\"section\":[{\"sectionID\":\"23276\",\"sectionType\":\"2019 Six Nations Championship\",\"sectionDisplayName\":\"2019 Six Nations Championship\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Rugby Union\",\"link\":\"\\/sportsbook\\/rugby-union\\/2019-six-nations-championship\",\"event_count\":1}]}},{\"sports\":{\"id\":10,\"sportsID\":170,\"event_count\":1,\"sportsType\":\"Politics\",\"sportsDisplayName\":\"Politics\",\"hasActiveLeagues\":\"true\",\"icon\":\"\\/assets\\/images\\/sportsbook\\/politics.svg\",\"link\":\"\\/sportsbook\\/politics\",\"section\":[{\"sectionID\":\"24304\",\"sectionType\":\"2020 U.S Elections\",\"sectionDisplayName\":\"2020 U.S Elections\",\"hasActiveEvents\":\"true\",\"eventCount\":\"1\",\"leagueType\":\"Politics\",\"link\":\"\\/sportsbook\\/politics\\/2020-us-elections\",\"event_count\":1}]}}]";
			final OddsmakerParser oddsmakerParser = new OddsmakerParser();
			Map<String, String> mp = oddsmakerParser.parseMenu(json, null, null);

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
		final Map<String, String> retValue = new HashMap<String, String>();
		
		// Parse the html
		final Document doc = parseXhtml(xhtml);

		// Get hidden input fields
		final String[] types = new String[] { "hidden", "password", "text" };
		final Elements forms = doc.select("form");
		for (int i = 0; (forms != null && i < forms.size()); i++) {
			final Element form = forms.get(i);
			final String formname = form.attr("name");

			if (formname != null && formname.equals("TopForm")) {
				retValue.put("action", form.attr("action"));
				getAllElementsByType(form, "input", types, retValue);
			}
		}

		LOGGER.info("Exiting parseIndex()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseLogin(java.lang.String)
	 */
	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		LOGGER.info("Entering parseLogin()");
		final Map<String, String> retValue = new HashMap<String, String>();
		
		// {"userID":"1369816",
		//  "username":"hendu2",
		//  "currency":"USD",
		//  "freeMoneyBalance":"0.00",
		//  "gamingBalance":"4,873.94"}
		final JSONObject obj = new JSONObject(xhtml.trim());
		final String userID = obj.getString("userID");
		retValue.put("userID", userID);

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
		final Map<String, String> retValue = new HashMap<String, String>();

		xhtml = xhtml.trim();
		xhtml = "{\"sportstypes\":" + xhtml + "}";

		final JSONObject obj = new JSONObject(xhtml.trim());
		final JSONArray sportsteams = obj.getJSONArray("sportstypes");

		for (int x = 0; x < sportsteams.length(); x++) {
			final JSONObject sItem = sportsteams.getJSONObject(x);
			final JSONObject sportsItem = sItem.getJSONObject("sports");
			final String sportstype = sportsItem.getString("sportsType");
			final JSONArray sections = sportsItem.getJSONArray("section");

			for (int y = 0; y < sections.length(); y++) {
				final JSONObject section = sections.getJSONObject(y);
				final String sectiontype = section.getString("sectionType");

				for (String sportname : sport) {
					for (String typename : type) {
						if (sportname.equals(sportstype) && typename.equals(sectiontype)) {
							retValue.put((sportname + typename), section.getString("link"));
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseMenu()");
		return retValue;
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

		final Document doc = parseXhtml(xhtml);
		final Elements divs = doc.select(".gamelines-event");
		
		if (divs != null && divs.size() > 0) {
			for (Element div : divs) {
				final OddsmakerEventPackage oep = new OddsmakerEventPackage();
				
				// Set event date
				parseEventDate(oep, div);

				// Set event time
				parseEventTime(oep, div);

				// Set game date
				setupGameDate(oep);

				final Elements gameteams = div.select(".gamelines-team");
				if (gameteams != null && gameteams.size() > 0) {
					int x = 0;

					// Loop through the teams
					for (Element gameteam : gameteams) {
						OddsmakerTeamPackage team = new OddsmakerTeamPackage();
						team.setEventdatetime(oep.getEventdatetime());
						team.setDateofevent(oep.getDateofevent());

						// Get the team name
						parseTeamName(team, gameteam);

						// Parse the ML
						team = parseMl(team, gameteam);

						// Parse the Spread
						team = parseSpread(team, gameteam);

						// Parse the Total
						team = parseTotal(team, gameteam, x == 0 ? true : false );

						// Team one or two?
						if (x == 0) {
							oep.setSiteteamone(team);
							oep.setId(team.getId());
						} else {
							oep.setSiteteamtwo(team);
							events.add(oep);
						}

						x++;
					}
				}
			}
		}

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


//      	{
//      	 "userID":"1369816",
//      	 "wagers":{
//      		"wagerOrder":"1",
//      		"wagerType":"1",
//      		"wagerValidationStatus":"true",
//      		"wagerOverallOdds":"-110",
//     		"wagerOverallPayoutAmount":"9.99",
//      		"wageredLines":{
//      			"lineID":"1449995",
//      			"lineType":"9",
//      			"wageredLineValidationStatus":"true"}
//      	 }
//      	}

		final JSONObject obj = new JSONObject(xhtml);
		JSONObject wagers = obj.getJSONObject("wagers");
		map.put("wagerValidationStatus", wagers.getString("wagerValidationStatus"));

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
		String ticketNumber = "";
		
//		{
//		    "userID": "1369816",
//		    "wagerConfirmations": {
//		        "wagerOrder": "1",
//		        "globalBetID": "63799061"
//		    }
//		}

		final JSONObject obj = new JSONObject(xhtml);

		if (obj.has("wagerConfirmations")) {
			final JSONObject wagers = obj.getJSONObject("wagerConfirmations");

			if (wagers.has("globalBetID")) {
				ticketNumber = wagers.getString("globalBetID");
			} else {
				throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, 
						BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "Error retrieving ticket number", xhtml);
			}
		} else {
			throw new BatchException(BatchErrorCodes.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, 
					BatchErrorMessage.FAILED_TO_SETUP_TRANSACTION_CORRECTLY, "Error retrieving ticket number", xhtml);			
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
	protected List<OddsmakerEventPackage> getGameData(Elements elements) throws BatchException {
		LOGGER.info("Entering getGameData()");
		LOGGER.info("Exiting getGameData()");
		return null;
	}

	/**
	 * 
	 * @param oep
	 * @param div
	 */
	private void parseEventDate(OddsmakerEventPackage oep, Element div) {
		final Elements eventdates = div.select(".gamelines-details div div span");

		if (eventdates != null && eventdates.size() > 0) {
			final String edate = eventdates.get(0).html().trim();
			LOGGER.debug("edate: " + edate);
			oep.setEventdate(edate);
		}		
	}

	/**
	 * 
	 * @param oep
	 * @param div
	 */
	private void parseEventTime(OddsmakerEventPackage oep, Element div) {
		final Elements eventtimes = div.select(".gamelines-details div div");

		if (eventtimes != null && eventtimes.size() > 0) {
			String eventtime = eventtimes.get(0).html().trim();
			int index = eventtime.indexOf("</span>");

			if (index != -1) {
				eventtime = eventtime.substring(index + "</span>".length()).trim();
			}
			LOGGER.debug("eventtime: " + eventtime);

			oep.setEventtime(eventtime);
		}		
	}

	/**
	 * 
	 * @param oep
	 */
	private void setupGameDate(OddsmakerEventPackage oep) {
		try {
			// 2018-12-09 13:00 EST
			final Date gameDate = DATE_FORMAT.parse(oep.getEventdate() + " " + oep.getEventtime());
			oep.setEventdatetime(gameDate);
			oep.setDateofevent(oep.getEventdate() + " " + oep.getEventtime());
		} catch (ParseException pe) {
			LOGGER.warn(pe.getMessage(), pe);
		}
	}

	/**
	 * 
	 * @param team
	 * @param gameteam
	 */
	private void parseTeamName(OddsmakerTeamPackage team, Element gameteam) {
		final Elements teamtitles = gameteam.select(".team-title");

		if (teamtitles != null && teamtitles.size() > 0) {
			final String tthtml = teamtitles.get(0).html().trim();
			team.setTeam(tthtml);
		}		
	}

	/**
	 * 
	 * @param team
	 * @param gameteam
	 * @return
	 */
	private OddsmakerTeamPackage parseMl(OddsmakerTeamPackage team, Element gameteam) {
		final Elements btnmoneys = gameteam.select(".btn-money");

		if (btnmoneys != null && btnmoneys.size() > 0) {
			final Element btnmoney = btnmoneys.get(0);

			// {"extid":574933,
			//  "linetypenum":8,
			//  "linetype":"MoneyLineBet",
			//  "iscircled":"false",
			//  "pitcherID":0,
			//  "pitcherDisplayName":0,
			//  "enable":"true",
			//	"linedata":{
			//    "contestantDisplayName":"Baltimore Ravens",
			//    "rotationNumber":"105",
			//    "isHomeTeam":"false",
			//    "odds":"250"},
			//  "eventTitle":"Baltimore Ravens at Kansas City Chiefs",
			//  "sport":"Football",
			//  "freebet":"0",
			//  "freeBetAmount":0,
			//  "freeBetMinimumRealBalance":0,
			//  "section":"NFL",
			//  "league":"NFL Football"}
			String dataline = btnmoney.attr("data-line");

			if (dataline != null && dataline.length() > 0) {
				dataline = dataline.replace("&quot;", "\"");
				final JSONObject obj = new JSONObject(dataline.trim());
				final Integer lineID = obj.getInt("extid");
				final Integer lineType = obj.getInt("linetypenum");
				team.setMllineID(lineID);
				team.setMllineType(lineType);
				final JSONObject linedata = obj.getJSONObject("linedata");

				if (linedata.has("rotationNumber")) {
					team.setId(Integer.parseInt(linedata.getString("rotationNumber")));
				}
				String ml = linedata.getString("odds").trim();

				if (!ml.startsWith("-")) {
					ml = ("+" + ml);
				}

				team = (OddsmakerTeamPackage)parseMlData(reformatValues(ml), 0, team);
			}

			// ID
//			team.addGameMLInputValue(Integer.toString(m), id);
//			team.setGameMLInputId(id);
//			team.setGameMLInputName(id);
//			team.addGameMLOptionJuiceIndicator(Integer.toString(m), "+");
//			team.addGameMLOptionJuiceIndicator(Integer.toString(m), "-");
//			team.addGameMLOptionJuiceNumber(Integer.toString(m), Double.toString(Math.abs(odds)));
		}

		return team;
	}

	/**
	 * 
	 * @param team
	 * @param gameteam
	 * @return
	 */
	private OddsmakerTeamPackage parseSpread(OddsmakerTeamPackage team, Element gameteam) {
		final Elements btnspreads = gameteam.select(".btn-spread");

		if (btnspreads != null && btnspreads.size() > 0) {
			final Element btnspread = btnspreads.get(0);

			// {"extid":1449380,
			//  "linetypenum":9,
			//  "linetype":"PointSpreadBet",
			//  "iscircled":"false",
			//  "pitcherID":0,
			//  "pitcherDisplayName":0,
			//  "enable":"true",
			/// "linedata":{
			//    "contestantDisplayName":"Kansas City Chiefs",
			//    "rotationNumber":"106",
			//    "isHomeTeam":"true",
			//    "odds":"-110",
			//    "pointSpreadValue":"-6.5",
			//    "handicapPrice":[
			//      {"handicap":"-6.0","handicapOdds":"-120"},
			//      {"handicap":"-5.5","handicapOdds":"-130"},
			//      {"handicap":"-5.0","handicapOdds":"-145"},
			//      {"handicap":"-4.5","handicapOdds":"-160"},
			//      {"handicap":"-4.0","handicapOdds":"-175"},
			//      {"handicap":"-3.5","handicapOdds":"-190"}]
			//  },
			//  "eventTitle":"Baltimore Ravens at Kansas City Chiefs",
			//  "sport":"Football",
			//  "freebet":"0",
			//  "freeBetAmount":0,
			//  "freeBetMinimumRealBalance":0,
			//  "section":"NFL",
			//  "league":"NFL Football"
			//  }
			String dataline = btnspread.attr("data-line");

			if (dataline != null && dataline.length() > 0) {
				dataline = dataline.replace("&quot;", "\"");
				LOGGER.debug("dataline: " + dataline);

				final JSONObject obj = new JSONObject(dataline.trim());
				final Integer lineID = obj.getInt("extid");
				final Integer lineType = obj.getInt("linetypenum");
				team.setSpreadlineID(lineID);
				team.setSpreadlineType(lineType);

				final JSONObject linedata = obj.getJSONObject("linedata");
				if (linedata.has("rotationNumber")) {
					team.setId(Integer.parseInt(linedata.getString("rotationNumber")));
				}

				String spread = linedata.getString("pointSpreadValue").trim();
				String juice = linedata.getString("odds").replace("(", "").replace(")", "").trim();

				if (spread != null && !spread.startsWith("-")) {
					spread = "+" + spread;
				}
				if (juice != null && !juice.startsWith("-")) {
					juice = "+" + juice;
				}

				team = (OddsmakerTeamPackage)parseSpreadData(reformatValues(spread + " " + juice), 0, " ", null, team);

				if (obj.has("handicapPrice")) {
					final JSONArray sides = obj.getJSONArray("handicapPrice");
					for (int s = 0; s < sides.length(); s++) {
						final JSONObject side = sides.getJSONObject(s);
						String sprd = side.getString("handicap");
						String juce = side.getString("handicapOdds");
	
						if (!sprd.startsWith("-")) {
							sprd = "+" + sprd;
						}
						if (!juce.startsWith("-")) {
							juce = "+" + juce;
						}

						team = (OddsmakerTeamPackage)parseSpreadData(reformatValues(sprd + " " + juce), (s+1), " ", null, team);
					}
				}
			}
		}

		return team;
	}

	/**
	 * 
	 * @param team
	 * @param gameteam
	 * @param isOver
	 * @return
	 */
	private OddsmakerTeamPackage parseTotal(OddsmakerTeamPackage team, Element gameteam, boolean isOver) {
		final Elements btntotals = gameteam.select(".btn-total");

		if (btntotals != null && btntotals.size() > 0) {
			final Element btntotal = btntotals.get(0);

			// {"extid":1449380,
			//  "linetypenum":9,
			//  "linetype":"PointSpreadBet",
			//  "iscircled":"false",
			//  "pitcherID":0,
			//  "pitcherDisplayName":0,
			//  "enable":"true",
			/// "linedata":{
			//    "contestantDisplayName":"Kansas City Chiefs",
			//    "rotationNumber":"106",
			//    "isHomeTeam":"true",
			//    "odds":"-110",
			//    "pointSpreadValue":"-6.5",
			//    "handicapPrice":[
			//      {"handicap":"-6.0","handicapOdds":"-120"},
			//      {"handicap":"-5.5","handicapOdds":"-130"},
			//      {"handicap":"-5.0","handicapOdds":"-145"},
			//      {"handicap":"-4.5","handicapOdds":"-160"},
			//      {"handicap":"-4.0","handicapOdds":"-175"},
			//      {"handicap":"-3.5","handicapOdds":"-190"}]
			//  },
			//  "eventTitle":"Baltimore Ravens at Kansas City Chiefs",
			//  "sport":"Football",
			//  "freebet":"0",
			//  "freeBetAmount":0,
			//  "freeBetMinimumRealBalance":0,
			//  "section":"NFL",
			//  "league":"NFL Football"
			//  }
			String dataline = btntotal.attr("data-line");

			if (dataline != null && dataline.length() > 0) {
				dataline = dataline.replace("&quot;", "\"");
				final JSONObject obj = new JSONObject(dataline.trim());
				final Integer lineID = obj.getInt("extid");
				final Integer lineType = obj.getInt("linetypenum");
				team.setTotallineID(lineID);
				team.setTotallineType(lineType);

				final JSONObject linedata = obj.getJSONObject("linedata");
				if (linedata.has("rotationNumber")) {
					team.setId(Integer.parseInt(linedata.getString("rotationNumber")));
				}
				String total = null;

				if (isOver) {
					total = linedata.getString("overLineValue").trim();
				} else {
					total = linedata.getString("underLineValue").trim();
				}
				String juice = linedata.getString("odds").replace("(", "").replace(")", "").trim();
				if (!juice.startsWith("-")) {
					juice = "+" + juice;
				}

				if (isOver) {
					team = (OddsmakerTeamPackage)parseTotalData(reformatValues("o" + total + " " + juice), 0, " ", null, team);
				} else {
					team = (OddsmakerTeamPackage)parseTotalData(reformatValues("u" + total + " " + juice), 0, " ", null, team);
				}

				if (obj.has("handicapPrice")) {
					final JSONArray sides = obj.getJSONArray("handicapPrice");
					for (int s = 0; s < sides.length(); s++) {
						final JSONObject side = sides.getJSONObject(s);
						final String totl = side.getString("handicap");
						String juce = side.getString("handicapOdds");

						if (!juce.startsWith("-")) {
							juce = "+" + juce;
						}

						if (isOver) {
							team = (OddsmakerTeamPackage)parseSpreadData(reformatValues("o" + totl + " " + juce), (s+1), " ", null, team);
						} else {
							team = (OddsmakerTeamPackage)parseSpreadData(reformatValues("u" + totl + " " + juce), (s+1), " ", null, team);
						}
					}
				}
			}
		}

		return team;
	}
}
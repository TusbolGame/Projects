/**
 * 
 */
package com.wootechnologies.services.dao.sites.espn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.services.dao.sites.SiteParser;
import com.wootechnologies.services.dao.sites.SiteTeamPackage;


/**
 * @author jmiller
 *
 */
public class EspnParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(EspnParser.class);
	private static final Map<String, String> NFLMapping = new HashMap<String, String>();
	private static final Map<String, String> MLBMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAAFMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAABMapping = new HashMap<String, String>();

	static {
		NFLMapping.put("ATL", "Atlanta Falcons");
		NFLMapping.put("ARI", "Arizona Cardinals");
		NFLMapping.put("BAL", "Baltimore Ravens");
		NFLMapping.put("BUF", "Buffalo Bills");
		NFLMapping.put("CAR", "Carolina Panthers");
		NFLMapping.put("CHI", "Chicago Bears");
		NFLMapping.put("CIN", "Cincinnati Bengals");
		NFLMapping.put("CLE", "Cleveland Browns");
		NFLMapping.put("DAL", "Dallas Cowboys");
		NFLMapping.put("DEN", "Denver Broncos");
		NFLMapping.put("DET", "Detroit Lions");
		NFLMapping.put("GB", "Green Bay Packers");
		NFLMapping.put("HOU", "Houston Texans");
		NFLMapping.put("IND", "Indianapolis Colts");
		NFLMapping.put("JAX", "Jacksonville Jaguars");
		NFLMapping.put("KC", "Kansas City Chiefs");
		NFLMapping.put("LA", "Los Angeles Rams");
		NFLMapping.put("MIA", "Miami Dolphins");
		NFLMapping.put("MIN", "Minnesota Vikings");
		NFLMapping.put("NE", "New England Patriots");
		NFLMapping.put("NO", "New Orleans Saints");
		NFLMapping.put("NYG", "New York Giants");
		NFLMapping.put("NYJ", "New York Jets");
		NFLMapping.put("OAK", "Oakland Raiders");
		NFLMapping.put("PHI", "Philadelphia Eagles");
		NFLMapping.put("PIT", "Pittsburgh Steelers"); 
		NFLMapping.put("SD", "San Diego Chargers");
		NFLMapping.put("SF", "San Francisco 49ers");
		NFLMapping.put("SEA", "Seattle Seahawks");
		NFLMapping.put("TB", "Tampa Bay Buccaneers");
		NFLMapping.put("TEN", "Tennessee Titans");
		NFLMapping.put("WAS", "Washington Redskins");

		MLBMapping.put("ATL", "Atlanta Braves");
		MLBMapping.put("ARI", "Arizona Diamondbacks");
		MLBMapping.put("BAL", "Baltimore Orioles");
		MLBMapping.put("BOS", "Boston Red Sox");
		MLBMapping.put("CHC", "Chicago Cubs");
		MLBMapping.put("CHW", "Chicago White Sox");
		MLBMapping.put("CIN", "Cincinnati Reds");
		MLBMapping.put("CLE", "Cleveland Indians");
		MLBMapping.put("COL", "Colorado Rockies");
		MLBMapping.put("DET", "Detroit Tigers");
		MLBMapping.put("HOU", "Houston Astros");
		MLBMapping.put("JAX", "Jacksonville Jaguars");
		MLBMapping.put("KC", "Kansas City Royals");
		MLBMapping.put("LAA", "Los Angeles Angels");
		MLBMapping.put("LAD", "Los Angeles Dodgers");
		MLBMapping.put("MIA", "Miami Marlins");
		MLBMapping.put("MIL", "Milwaukee Brewers");
		MLBMapping.put("MIN", "Minnesota Twins");
		MLBMapping.put("NYM", "New York Mets");
		MLBMapping.put("NYY", "New York Yankees");
		MLBMapping.put("OAK", "Oakland Athletics");
		MLBMapping.put("PHI", "Philadelphia Phillies");
		MLBMapping.put("PIT", "Pittsburgh Pirates"); 
		MLBMapping.put("SD", "San Diego Padres");
		MLBMapping.put("SEA", "Seattle Mariners");
		MLBMapping.put("SF", "San Francisco Giants");
		MLBMapping.put("STL", "Saint Louis Cardinals");
		MLBMapping.put("TB", "Tampa Bay Rays");
		MLBMapping.put("TEN", "Tennessee Titans");
		MLBMapping.put("TEX", "Texas Rangers");
		MLBMapping.put("TOR", "Toronto Blue Jays");
		MLBMapping.put("WAS", "Washington Nationals");

		NCAAFMapping.put("Abilene Christian", "Abilene Christian");
		NCAAFMapping.put("Air Force", "Air Force");
		NCAAFMapping.put("Akron", "Akron");
		NCAAFMapping.put("Alabama A&amp;M", "Alabama A&M");
		NCAAFMapping.put("Alabama", "Alabama");
		NCAAFMapping.put("Alabama State", "Alabama St.");
		NCAAFMapping.put("UAB", "UAB");
		NCAAFMapping.put("Albany", "Albany");
		NCAAFMapping.put("Alcorn State", "Alcorn St.");
		NCAAFMapping.put("Appalachian State", "Appalachian St.");
		NCAAFMapping.put("Arizona State", "Arizona St.");
		NCAAFMapping.put("Arizona U", "Arizona");
		NCAAFMapping.put("Arkansas", "Arkansas");
		NCAAFMapping.put("Arkansas State", "Arkansas St.");
		NCAAFMapping.put("Arkansas Pine Bluff", "Arkansas-Pine Bluff");
		NCAAFMapping.put("Army", "Army");
		NCAAFMapping.put("Auburn", "Auburn");
		NCAAFMapping.put("Austin Peay", "Austin Peay St.");
		NCAAFMapping.put("Ball State", "Ball St.");
		NCAAFMapping.put("Baylor", "Baylor");
		NCAAFMapping.put("Bethune Cookman", "Bethune Cookman");
		NCAAFMapping.put("Boise State", "Boise St.");
		NCAAFMapping.put("Boston College", "Boston College");
		NCAAFMapping.put("Bowling Green", "Bowling Green");
		NCAAFMapping.put("BYU", "BYU");
		NCAAFMapping.put("Brown", "Brown");
		NCAAFMapping.put("Bryant", "Bryant University");
		NCAAFMapping.put("Bucknell", "Bucknell");
		NCAAFMapping.put("Buffalo U", "Buffalo");
		NCAAFMapping.put("Butler", "Butler");
		NCAAFMapping.put("Cal Poly", "Cal Poly-Slo");
		NCAAFMapping.put("California", "California");
		NCAAFMapping.put("SACRAMENTO STATE", "California State-Sacramento");
		NCAAFMapping.put("UC Davis", "California-Davis");
		NCAAFMapping.put("Campbell", "Campbell");
		NCAAFMapping.put("Central Arkansas", "Central Arkansas");
		NCAAFMapping.put("Central Conn.", "Central Connecticut St.");
		NCAAFMapping.put("Central Florida", "UCF");
		NCAAFMapping.put("Central Michigan", "Central Michigan");
		NCAAFMapping.put("Charleston Southern", "Charleston Southern");
		NCAAFMapping.put("Cincinnati U", "Cincinnati");
		NCAAFMapping.put("Citadel", "Citadel");
		NCAAFMapping.put("Clemson", "Clemson");
		NCAAFMapping.put("Coastal Carolina", "Coastal Carolina");
		NCAAFMapping.put("Colgate", "Colgate");
		NCAAFMapping.put("Colorado", "Colorado");
		NCAAFMapping.put("Colorado State", "Colorado St.");
		NCAAFMapping.put("Columbia", "Columbia");
		NCAAFMapping.put("UConn", "Connecticut");
		NCAAFMapping.put("Cornell", "Cornell");
		NCAAFMapping.put("Dartmouth", "Dartmouth");
		NCAAFMapping.put("Davidson", "Davidson College");
		NCAAFMapping.put("Dayton", "Dayton");
		NCAAFMapping.put("Delaware", "Delaware");
		NCAAFMapping.put("Delaware State", "Delaware St.");
		NCAAFMapping.put("Drake", "Drake");
		NCAAFMapping.put("Duke", "Duke");
		NCAAFMapping.put("Duquesne", "Duquesne");
		NCAAFMapping.put("East Carolina", "East Carolina");
		NCAAFMapping.put("East Tennessee State", "East Tennessee St.");
		NCAAFMapping.put("Eastern Illinois", "Eastern Illinois");
		NCAAFMapping.put("Eastern Kentucky", "Eastern Kentucky");
		NCAAFMapping.put("Eastern Michigan", "Eastern Michigan");
		NCAAFMapping.put("Eastern Washington", "Eastern Washington");
		NCAAFMapping.put("Elon", "Elon");
		NCAAFMapping.put("Florida A&amp;M", "Florida A&M");
		NCAAFMapping.put("Florida Atlantic", "Florida Atlantic");
		NCAAFMapping.put("Florida", "Florida");
		NCAAFMapping.put("Florida International", "FIU");
		NCAAFMapping.put("Florida State", "Florida St.");
		NCAAFMapping.put("Fordham", "Fordham");
		NCAAFMapping.put("Fresno State", "Fresno St.");
		NCAAFMapping.put("Furman", "Furman");
		NCAAFMapping.put("Gardner Webb", "Gardner Webb");
		NCAAFMapping.put("Georgetown", "Georgetown");
		NCAAFMapping.put("Georgia", "Georgia");
		NCAAFMapping.put("Georgia Southern", "Georgia Southern");
		NCAAFMapping.put("Georgia State", "Georgia St.");
		NCAAFMapping.put("Georgia Tech", "Georgia Tech");
		NCAAFMapping.put("Grambling", "Grambling");
		NCAAFMapping.put("Hampton", "Hampton");
		NCAAFMapping.put("Harvard", "Harvard");
		NCAAFMapping.put("Hawai'i", "Hawaii");
		NCAAFMapping.put("Holy Cross", "Holy Cross");
		NCAAFMapping.put("Houston Baptist", "Houston Baptist");
		NCAAFMapping.put("Houston U", "Houston");
		NCAAFMapping.put("Howard", "Howard");
		NCAAFMapping.put("Idaho State", "Idaho St.");
		NCAAFMapping.put("Idaho", "Idaho");
		NCAAFMapping.put("Illinois", "Illinois");
		NCAAFMapping.put("Illinois State", "Illinois St.");
		NCAAFMapping.put("Incarnate Word", "Incarnate Word");
		NCAAFMapping.put("Indiana", "Indiana");
		NCAAFMapping.put("Indiana State", "Indiana St.");
		NCAAFMapping.put("Iowa", "Iowa");
		NCAAFMapping.put("Iowa State", "Iowa St.");
		NCAAFMapping.put("Jackson State", "Jackson St.");
		NCAAFMapping.put("Jacksonville", "Jacksonville");
		NCAAFMapping.put("Jacksonville State", "Jacksonville St.");
		NCAAFMapping.put("James Madison", "James Madison");
		NCAAFMapping.put("Kansas", "Kansas");
		NCAAFMapping.put("Kansas State", "Kansas St.");
		NCAAFMapping.put("Kennesaw State", "Kennesaw St.");
		NCAAFMapping.put("Kent State", "Kent St.");
		NCAAFMapping.put("Kentucky", "Kentucky");
		NCAAFMapping.put("Lafayette", "Lafayette");
		NCAAFMapping.put("Lamar", "Lamar");
		NCAAFMapping.put("Lehigh", "Lehigh");
		NCAAFMapping.put("Liberty", "Liberty");
		NCAAFMapping.put("LSU", "LSU");
		NCAAFMapping.put("Louisiana Tech", "Louisiana Tech");
		NCAAFMapping.put("UL Lafayette", "Louisiana");
		NCAAFMapping.put("UL Monroe", "Louisiana Monroe");
		NCAAFMapping.put("Louisville", "Louisville");
		NCAAFMapping.put("Maine", "Maine");
		NCAAFMapping.put("Marist", "Marist");
		NCAAFMapping.put("Marshall", "Marshall");
		NCAAFMapping.put("Maryland", "Maryland");
		NCAAFMapping.put("UMass", "Massachusetts");
		NCAAFMapping.put("McNeese State", "McNeese St.");
		NCAAFMapping.put("Memphis", "Memphis");
		NCAAFMapping.put("Mercer", "Mercer");
		NCAAFMapping.put("Miami", "Miami FL");
		NCAAFMapping.put("Miami (OH)", "Miami OH");
		NCAAFMapping.put("Michigan State", "Michigan St.");
		NCAAFMapping.put("Michigan", "Michigan");
		NCAAFMapping.put("Middle Tennessee", "Middle Tennessee");
		NCAAFMapping.put("Minnesota U", "Minnesota");
		NCAAFMapping.put("Ole Miss", "Mississippi");
		NCAAFMapping.put("Mississippi State", "Mississippi St.");
		NCAAFMapping.put("MISSISSIPPI VALLEY STATE", "Mississippi Valley St.");
		NCAAFMapping.put("Missouri State", "Missouri St.");
		NCAAFMapping.put("Missouri", "Missouri");
		NCAAFMapping.put("Monmouth", "Monmouth-New Jersey");
		NCAAFMapping.put("Montana", "Montana");
		NCAAFMapping.put("Montana State", "Montana St.");
		NCAAFMapping.put("Morehead State", "Morehead St.");
		NCAAFMapping.put("Morgan State", "Morgan St.");
		NCAAFMapping.put("Murray State", "Murray St.");
		NCAAFMapping.put("Navy", "Navy");
		NCAAFMapping.put("Nebraska", "Nebraska");
		NCAAFMapping.put("Nevada", "Nevada");
		NCAAFMapping.put("New Hampshire", "New Hampshire");
		NCAAFMapping.put("New Mexico", "New Mexico");
		NCAAFMapping.put("New Mexico State", "New Mexico St.");
		NCAAFMapping.put("Nicholls State", "Nicholls St.");
		NCAAFMapping.put("Norfolk State", "Norfolk St.");
		NCAAFMapping.put("North Alabama", "North Alabama");
		NCAAFMapping.put("NORTH CAROLINA A&AMP;T", "North Carolina A&T");
		NCAAFMapping.put("NC Central", "North Carolina Central");
		NCAAFMapping.put("NC State", "North Carolina St.");
		NCAAFMapping.put("North Carolina", "North Carolina");
		NCAAFMapping.put("Charlotte", "Charlotte");
		NCAAFMapping.put("North Dakota", "North Dakota");
		NCAAFMapping.put("North Dakota State", "North Dakota St.");
		NCAAFMapping.put("North Texas", "North Texas");
		NCAAFMapping.put("Northern Arizona", "Northern Arizona");
		NCAAFMapping.put("Northern Colorado", "Northern Colorado");
		NCAAFMapping.put("No Illinois", "Northern Illinois");
		NCAAFMapping.put("Northern Iowa", "Northern Iowa");
		NCAAFMapping.put("Northwestern State", "Northwestern St.");
		NCAAFMapping.put("Northwestern", "Northwestern");
		NCAAFMapping.put("Notre Dame", "Notre Dame");
		NCAAFMapping.put("Ohio", "Ohio");
		NCAAFMapping.put("Ohio State", "Ohio St.");
		NCAAFMapping.put("Oklahoma", "Oklahoma");
		NCAAFMapping.put("Oklahoma State", "Oklahoma St.");
		NCAAFMapping.put("Old Dominion", "Old Dominion");
		NCAAFMapping.put("Oregon", "Oregon");
		NCAAFMapping.put("Oregon State", "Oregon St.");
		NCAAFMapping.put("Penn State", "Penn St.");
		NCAAFMapping.put("Pennsylvania", "Pennsylvania");
		NCAAFMapping.put("Pittsburgh U", "Pittsburgh");
		NCAAFMapping.put("Portland State", "Portland St.");
		NCAAFMapping.put("Prairie View A&amp;M", "Prairie View A&M");
		NCAAFMapping.put("Presbyterian", "Presbyterian");
		NCAAFMapping.put("Princeton", "Princeton");
		NCAAFMapping.put("Purdue", "Purdue");
		NCAAFMapping.put("Rhode Island", "Rhode Island");
		NCAAFMapping.put("Rice", "Rice");
		NCAAFMapping.put("Richmond", "Richmond");
		NCAAFMapping.put("Robert Morris", "Robert Morris");
		NCAAFMapping.put("Rutgers", "Rutgers");
		NCAAFMapping.put("Sacred Heart", "Sacred Heart");
		NCAAFMapping.put("St. Francis (PA)", "Saint Francis-Pennsylvania");
		NCAAFMapping.put("Sam Houston State", "Sam Houston St.");
		NCAAFMapping.put("Samford", "Samford");
		NCAAFMapping.put("San Diego State", "San Diego St.");
		NCAAFMapping.put("San Diego", "San Diego");
		NCAAFMapping.put("San Jose State", "San Jose St.");
		NCAAFMapping.put("Savannah State", "Savannah St.");
		NCAAFMapping.put("South Alabama", "South Alabama");
		NCAAFMapping.put("South Carolina", "South Carolina");
		NCAAFMapping.put("South Carolina State", "South Carolina St.");
		NCAAFMapping.put("South Dakota", "South Dakota");
		NCAAFMapping.put("South Dakota State", "South Dakota St.");
		NCAAFMapping.put("South Florida", "South Florida");
		NCAAFMapping.put("SOUTHEAST MISSOURI STATE", "Southeast Missouri St.");
		NCAAFMapping.put("SE Louisiana", "Southeastern Louisiana");
		NCAAFMapping.put("USC", "USC");
		NCAAFMapping.put("Southern Illinois", "Southern Illinois");
		NCAAFMapping.put("SMU", "SMU");
		NCAAFMapping.put("Southern Mississippi", "Southern Miss");
		NCAAFMapping.put("Southern", "Southern University A&M");
		NCAAFMapping.put("Southern Utah", "Southern Utah");
		NCAAFMapping.put("Stanford", "Stanford");
		NCAAFMapping.put("Stephen F. Austin", "Stephen F. Austin St.");
		NCAAFMapping.put("Stetson", "Stetson");
		NCAAFMapping.put("Stony Brook", "Stony Brook");
		NCAAFMapping.put("Syracuse", "Syracuse");
		NCAAFMapping.put("Temple", "Temple");
		NCAAFMapping.put("Tennessee State", "Tennessee St.");
		NCAAFMapping.put("Tennessee Tech", "Tennessee Tech");
		NCAAFMapping.put("Tennessee U", "Tennessee");
		NCAAFMapping.put("Chattanooga", "Tennessee-Chattanooga");
		NCAAFMapping.put("Tenn Martin", "Tennessee-Martin");
		NCAAFMapping.put("Texas A&amp;M", "Texas A&M");
		NCAAFMapping.put("TCU", "TCU");
		NCAAFMapping.put("Texas", "Texas");
		NCAAFMapping.put("Texas Southern", "Texas Southern");
		NCAAFMapping.put("Texas State", "Texas St.");
		NCAAFMapping.put("Texas Tech", "Texas Tech");
		NCAAFMapping.put("UTEP", "UTEP");
		NCAAFMapping.put("Tex San Antonio", "UTSA");
		NCAAFMapping.put("Toledo", "Toledo");
		NCAAFMapping.put("Towson", "Towson");
		NCAAFMapping.put("Troy", "Troy");
		NCAAFMapping.put("Tulane", "Tulane");
		NCAAFMapping.put("Tulsa", "Tulsa");
		NCAAFMapping.put("UCLA", "UCLA");
		NCAAFMapping.put("UNLV", "UNLV");
		NCAAFMapping.put("Utah State", "Utah St.");
		NCAAFMapping.put("Utah", "Utah");
		NCAAFMapping.put("Valparaiso", "Valparaiso");
		NCAAFMapping.put("Vanderbilt", "Vanderbilt");
		NCAAFMapping.put("Villanova", "Villanova");
		NCAAFMapping.put("Virginia", "Virginia");
		NCAAFMapping.put("VMI", "Virginia Military");
		NCAAFMapping.put("Virginia Tech", "Virginia Tech");
		NCAAFMapping.put("Wagner", "Wagner");
		NCAAFMapping.put("Wake Forest", "Wake Forest");
		NCAAFMapping.put("Washington U", "Washington");
		NCAAFMapping.put("Washington State", "Washington St.");
		NCAAFMapping.put("Weber State", "Weber St.");
		NCAAFMapping.put("West Virginia", "West Virginia");
		NCAAFMapping.put("Western Carolina", "Western Carolina");
		NCAAFMapping.put("Western Illinois", "Western Illinois");
		NCAAFMapping.put("Western Kentucky", "Western Kentucky");
		NCAAFMapping.put("Western Michigan", "Western Michigan");
		NCAAFMapping.put("William &amp; Mary", "William & Mary");
		NCAAFMapping.put("Wisconsin", "Wisconsin");
		NCAAFMapping.put("Wofford", "Wofford");
		NCAAFMapping.put("Wyoming", "Wyoming");
		NCAAFMapping.put("Yale", "Yale");
		NCAAFMapping.put("Youngstown State", "Youngstown St.");

		NCAABMapping.put("ALABAMA AM" , "Alabama AM");
		NCAABMapping.put("ALABAMA A&M" , "Alabama AM");
		NCAABMapping.put("ALABAMA A&AMP;M" , "Alabama AM");
		NCAABMapping.put("Alabama St","Alabama State");
		NCAABMapping.put("Alcorn State","Alcorn State");
		NCAABMapping.put("Appalachian St","Appalachian State");
		NCAABMapping.put("Arizona St","Arizona State");
		NCAABMapping.put("Arkansas St","Arkansas State");
		NCAABMapping.put("Arkansas State","Arkansas State");
		NCAABMapping.put("LITTLE ROCK" , "Arkansas-Little Rock");
		NCAABMapping.put("Boston Univ.","Boston University");
		NCAABMapping.put("Chicago St","Chicago State");
		NCAABMapping.put("Cleveland St","Cleveland State");
		NCAABMapping.put("Colorado St","Colorado State");
		NCAABMapping.put("Coppin St","Coppin State");
		NCAABMapping.put("Delaware St","Delaware State");
		NCAABMapping.put("EAST TENNESSEE ST","East Tennessee State");
		NCAABMapping.put("Florida International","Florida International");
		NCAABMapping.put("Florida Int'l","Florida International");
		NCAABMapping.put("Florida St","Florida State");
		NCAABMapping.put("Fresno St","Fresno State");
		NCAABMapping.put("Georgia St","Georgia State");
		NCAABMapping.put("Grambling State","Grambling");
		NCAABMapping.put("Grambling St","Grambling");
		NCAABMapping.put("Idaho St","Idaho State");
		NCAABMapping.put("Illinois St","Illinois State");
		NCAABMapping.put("Indiana St","Indiana State");
		NCAABMapping.put("Iowa St","Iowa State");
		NCAABMapping.put("Jackson St","Jackson State");
		NCAABMapping.put("Jacksonville St","Jacksonville State");
		NCAABMapping.put("Kansas St","Kansas State");
		NCAABMapping.put("Kennesaw St","Kennesaw State");
		NCAABMapping.put("Kent St","Kent State");
		NCAABMapping.put("Long Beach St","Long Beach State");
		NCAABMapping.put("Loyola-Chicago","LOYOLA (CHI)");
		NCAABMapping.put("Loyola Marymount" , "Loyola Marymount");
		NCAABMapping.put("McNeese St","McNeese State");
		NCAABMapping.put("Michigan St","Michigan State");
		NCAABMapping.put("Mississippi St","Mississippi State");
		NCAABMapping.put("Miss Val St","Mississippi Valley State");
		NCAABMapping.put("Missouri St","Missouri State");
		NCAABMapping.put("Montana St","Montana State");
		NCAABMapping.put("Morehead St","Morehead State");
		NCAABMapping.put("Morgan St","Morgan State");
		NCAABMapping.put("MOUNT ST. MARY","Mt. St. Mary's");
		NCAABMapping.put("Murray St","Murray State");
		NCAABMapping.put("Nicholls","Nicholls State");
		NCAABMapping.put("Nicholls St","Nicholls State");
		NCAABMapping.put("Norfolk St","Norfolk State");
		NCAABMapping.put("NORTH DAKOTA ST","NORTH DAKOTA STATE");
		NCAABMapping.put("NORTHWESTERN ST" , "Northwestern State");
		NCAABMapping.put("N Colorado" , "Northern Colorado");
		NCAABMapping.put("Ohio State","Ohio State");
		NCAABMapping.put("Oklahoma St","Oklahoma State");
		NCAABMapping.put("Oregon St","Oregon State");
		NCAABMapping.put("Penn State","Penn State");
		NCAABMapping.put("Portland St","Portland State");
		NCAABMapping.put("Purdue Fort Wayne","Fort Wayne");
		NCAABMapping.put("PRAIRIE VIEW A&AMP;M" , "Prairie View AM");
		NCAABMapping.put("SACRAMENTO ST","SACRAMENTO State");
		NCAABMapping.put("Sam Houston St","Sam Houston State");
		NCAABMapping.put("SAINT LOUIS","MISSOURI-ST. LOUIS");
		NCAABMapping.put("San Diego State","SAN DIEGO STATE");
		NCAABMapping.put("San Jose State","SAN JOSE STATE");
		NCAABMapping.put("Savannah St","Savannah State");
		NCAABMapping.put("SE MISSOURI ST","SE Missouri State");
		NCAABMapping.put("SIU-Edwardsville","SIU Edwardsville");
		NCAABMapping.put("SOUTHEAST MISSOURI STATE","SE Missouri State");
		NCAABMapping.put("South Carolina St","South Carolina State");
		NCAABMapping.put("South Dakota St","South Dakota State");
		NCAABMapping.put("ST. FRANCIS (BKN)","ST. FRANCIS BKN");
		NCAABMapping.put("TENNESSEE ST" , "Tennessee State");
		NCAABMapping.put("UT MARTIN" , "Tenn-Martin");
		NCAABMapping.put("TENNESSEE-MARTIN" , "Tenn-Martin");
		NCAABMapping.put("Washington St","Washington State");
		NCAABMapping.put("Wichita St","Wichita State");
		NCAABMapping.put("Youngstown St","Youngstown State");
	}

	/**
	 * Constructor
	 */
	public EspnParser() {
		super();
		LOGGER.info("Entering EspnParser()");
		LOGGER.info("Exiting EspnParser()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat sdfmt1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		try {
			String dateElement = "2/09";
			String timeElement = "10:00 pm";
			final Calendar now = Calendar.getInstance();
			dateElement += "/" + String.valueOf(now.get(Calendar.YEAR));
			String dtDate = dateElement + " " + timeElement;
			LOGGER.error("dtDate: " + dtDate);
			Date dDate = sdfmt1.parse(dtDate);
		} catch (ParseException pe) {
			LOGGER.error("Error parsing date", pe);
		}
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public List<String> parseNccafGamesForDay(String xhtml) {
		LOGGER.info("Entering parseNccafGamesForDay()");
		final List<String> games = new ArrayList<String>();

		try {
			String data = xhtml;
			String data2 = xhtml;
			LOGGER.debug("xhtml: " + xhtml);

			int index = data.indexOf("/college-football/game/_/gameId/");
			while (index != -1) {
				data = data.substring(index + "/college-football/game/_/gameId/".length());
				String num = null;
				index = data.indexOf("\"");
				if (index != -1) {
					num = data.substring(0, index);
					LOGGER.debug("num: " + num);
					games.add(num);
					data = data.substring(index + "\"".length());
				}

				index = data.indexOf("/college-football/game/_/gameId/");
			}

			index = data2.indexOf("/college-football/game?gameId=");
			while (index != -1) {
				data2 = data2.substring(index + "/college-football/game?gameId=".length());
				String num;
				index = data2.indexOf("\"");
				if (index != -1) {
					num = data2.substring(0, index);
					games.add(num);
					data2 = data2.substring(index + "\"".length());
				}

				index = data2.indexOf("/college-football/game?gameId=");
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting parseNccafGamesForDay()");
		return games;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public List<String> parseNccabGamesForDay(String xhtml) {
		LOGGER.info("Entering parseNccabGamesForDay()");
		final List<String> games = new ArrayList<String>();

		try {
			LOGGER.debug("xhtml: " + xhtml);
			String data = xhtml;
			int index = data.indexOf("/mens-college-basketball/game?gameId=");
			if (index != -1) {
				while (index != -1) {
					data = data.substring(index + "/mens-college-basketball/game?gameId=".length());
					LOGGER.debug("data: " + data);
					String num;
					index = data.indexOf("\",\"text");
					if (index != -1) {
						num = data.substring(0, index);
						String url = "/mens-college-basketball/matchup?gameId=" + num;
						LOGGER.debug("url: " + url);
						games.add(url);
						data = data.substring(index + "\",\"text".length());
					}

					index = data.indexOf("/mens-college-basketball/game?gameId=");
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting parseNccabGamesForDay()");
		return games;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public List<String> parseBasketballSchedulePlayByPlay(String xhtml) {
		LOGGER.info("Entering parseBasketballSchedulePlayByPlay()");
		List<String> urls = new ArrayList<String>();

		if (xhtml != null) {
			int indexof = xhtml.indexOf("\"Play-by-Play\",\"rel\":[\"pbp\",\"desktop\",\"event\"],\"language\":\"en-US\",\"href\":\"");
			while (indexof != -1) {
				xhtml = xhtml.substring(indexof + "\"Play-by-Play\",\"rel\":[\"pbp\",\"desktop\",\"event\"],\"language\":\"en-US\",\"href\":\"".length());
				int endindex = xhtml.indexOf("\",\"text\":\"Play-by-Play\"");
				if (endindex != -1) {
					String url = xhtml.substring(0, endindex);
					urls.add(url);
				}
				indexof = xhtml.indexOf("\"Play-by-Play\",\"rel\":[\"pbp\",\"desktop\",\"event\"],\"language\":\"en-US\",\"href\":\"");
			}
		}

		LOGGER.info("Exiting parseBasketballSchedulePlayByPlay()");
		return urls;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public List<String> parseFootballSchedulePlayByPlay(String xhtml) throws BatchException {
		LOGGER.info("Entering parseFootballSchedulePlayByPlay()");
		final List<String> urls = new ArrayList<String>();

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		final Element events = doc.getElementById("events");
		final Elements games = events.select("article div section a");
		for (Element game : games) {
			final String gamecast = game.html();
			if (gamecast != null && gamecast.equals("Gamecast")) {
				// /college-football/game?gameId=400935257
				// /college-football/playbyplay?gameId=400935257
				String url = game.attr("href");
				url = url.replaceAll("/game?", "/playbyplay?");
				urls.add(url);
			}
		}

		LOGGER.info("Exiting parseFootballSchedulePlayByPlay()");
		return urls;
	}

	/**
	 * 
	 * @param json
	 * @throws BatchException
	 */
	public void parsePlayByPlay(String json) throws BatchException {
		if (json != null) {
			final JSONObject obj = new JSONObject(json);
			
			if (obj != null) {
				final JSONObject content = obj.getJSONObject("content");
				
				if (content != null) {
					final String html = content.getString("html");
					
					if (html != null) {
						// parseWnbaPlayByPlay(html);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public List<EspnFootballPowerIndex> parseNcaafPowerIndexes(String xhtml) {
		LOGGER.info("Entering parseNcaafPowerIndexes()");
		final List<EspnFootballPowerIndex> powerIndexes = new ArrayList<EspnFootballPowerIndex>();

		try {
			final Document doc = parseXhtml(xhtml);
			
			final Elements trs = doc.select("#my-teams-table div div table tbody tr");
			if (trs != null && trs.size() > 0) {
				for (Element tr : trs) {
					final String classnames = tr.attr("class");

					if (classnames.contains("oddrow") || classnames.contains("evenrow")) {
						final EspnFootballPowerIndex efpi = new EspnFootballPowerIndex();
						final Elements tds = tr.select("td");

						int tdcounter = 0;
						for (Element td : tds) {
							switch (tdcounter++) {
								case 0:
									try {
										efpi.setRank(Integer.parseInt(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 1:
									final Elements as = td.select("a");
									for (Element a : as) {
										final String teamName = a.html().trim();
										efpi.setTeam(teamName.toUpperCase());
										final Iterator<String> itr = NCAABMapping.keySet().iterator();

										while (itr.hasNext()) {
											final String key = itr.next();
											if (key.toUpperCase().equals(teamName.toUpperCase())) {
												efpi.setTeam(NCAABMapping.get(key).toUpperCase());
											}
										}
									}
									break;
								case 3:
									try {
										final String winslosses = td.html().trim();
										int index = winslosses.indexOf("-");
										if (index != -1) {
											efpi.setWins(Float.parseFloat(winslosses.substring(0, index).trim()));
											efpi.setLosses(Float.parseFloat(winslosses.substring(index + 1).trim()));
										}
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 4:
									try {
										efpi.setWinout(Float.parseFloat(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 5:
									try {
										efpi.setConfwin(Float.parseFloat(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 6:
									try {
										efpi.setRemainsos(Integer.parseInt(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 7:
									try {
										efpi.setFpi(Float.parseFloat(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 2:
								default:
									break;
							}
						}
						
						// Add to the list
						powerIndexes.add(efpi);
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting parseNcaafPowerIndexes()");
		return powerIndexes;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public List<EspnFootballTeamEfficiencies> parseNcaafTeamEfficiencies(String xhtml) {
		LOGGER.info("Entering parseNcaafTeamEfficiencies()");
		final List<EspnFootballTeamEfficiencies> teamEfficiencies = new ArrayList<EspnFootballTeamEfficiencies>();

		try {
			final Document doc = parseXhtml(xhtml);
			final Elements trs = doc.select("#my-teams-table div div table tbody tr");

			if (trs != null && trs.size() > 0) {
				for (Element tr : trs) {
					final String classnames = tr.attr("class");

					if (classnames.contains("oddrow") || classnames.contains("evenrow")) {
						final EspnFootballTeamEfficiencies efte = new EspnFootballTeamEfficiencies();
						final Elements tds = tr.select("td");

						int tdcounter = 0;
						for (Element td : tds) {
							switch (tdcounter++) {
								case 0:
									try {
										efte.setRank(Integer.parseInt(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 1:
									final Elements as = td.select("a");
									for (Element a : as) {
										final String teamName = a.html().trim();
										efte.setTeam(teamName.toUpperCase());

										final Iterator<String> itr = NCAABMapping.keySet().iterator();
										while (itr.hasNext()) {
											final String key = itr.next();
											if (key.toUpperCase().equals(teamName.toUpperCase())) {
												efte.setTeam(NCAABMapping.get(key).toUpperCase());
											}
										}
									}
									break;
								case 2:
									try {
										efte.setOffense(Float.parseFloat(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 3:
									try {
										efte.setDefense(Float.parseFloat(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 4:
									try {
										efte.setSpecialteams(Float.parseFloat(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								case 5:
									try {
										efte.setOverall(Float.parseFloat(td.html().trim()));
									} catch (Throwable t) {
										LOGGER.warn(t.getMessage(), t);
									}
									break;
								default:
									break;
							}
						}

						// Add to the list
						teamEfficiencies.add(efte);
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting parseNcaafTeamEfficiencies()");
		return teamEfficiencies;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public EspnCollegeFootballGameData parseNcaafGameTeamData(String xhtml) throws BatchException {
		LOGGER.info("Entering parseNcaafGameTeamData()");
		LOGGER.debug("xhtml: " + xhtml);
		final EspnCollegeFootballGameData game = new EspnCollegeFootballGameData();

		int hindex = xhtml.indexOf("customNav\":\"");
		if (hindex != -1) {
			xhtml = xhtml.substring(hindex + "customNav\":\"".length());
			hindex = xhtml.indexOf("\",\"sport");
			if (hindex != -1) {
				xhtml = xhtml.substring(0, hindex);
				xhtml = xhtml.replaceAll("\\\\\"", "\"");
			}
		}

		final Document doc = parseXhtml(xhtml);

		// Conference game?
		if (xhtml.contains("Conf</span>")) {
			game.setIsconferencegame(true);
		}

		final Elements teamcontents = doc.select(".team__content");
		if (teamcontents != null && teamcontents.size() > 0) {
			int x = 0;
			for (Element teamcontent : teamcontents) {
				final Elements divs = teamcontent.select(".team-info-wrapper");
				if (divs != null && divs.size() > 0) {
					final Element div = divs.get(0);
					final Elements ranks = div.select(".rank");
					if (ranks != null && ranks.size() > 0) {
						final Element rank = ranks.get(0);
						if (x == 0) {
							game.setAwayranking(Integer.parseInt(rank.html().trim()));
						} else {
							game.setHomeranking(Integer.parseInt(rank.html().trim()));
						}
					}
					final Elements longnames = div.select(".long-name");
					if (longnames != null && longnames.size() > 0) {
						final Element longname = longnames.get(0);
						String lname = longname.html().trim().toUpperCase().trim();
						boolean isfbs = false;
						String collegename = null;
						Iterator<String> itr = NCAAFMapping.keySet().iterator();
						while (itr.hasNext()) {
							final String key = itr.next();
							if (key.toUpperCase().equals(lname)) {
								collegename = NCAAFMapping.get(key).toUpperCase().trim();
								isfbs = true;
							}
						}

						if (x == 0) {
							if (collegename == null) {
								game.setAwaycollegename(lname);
								game.setAwayteam(lname);
							} else {
								game.setAwaycollegename(collegename);
								game.setAwayteam(collegename);
							}
							game.setAwayisfbs(isfbs);
						} else {
							if (collegename == null) {
								game.setHomecollegename(lname);
								game.setHometeam(lname);
							} else {
								game.setHomecollegename(collegename);
								game.setHometeam(collegename);
							}
							game.setHomeisfbs(isfbs);
						}
					}
					final Elements shortnames = div.select(".short-name");
					if (shortnames != null && shortnames.size() > 0) {
						final Element shortname = shortnames.get(0);
						if (x == 0) {
							game.setAwaymascotname(shortname.html().trim());
						} else {
							game.setHomemascotname(shortname.html().trim());
						}
					}
					final Elements abbrevs = div.select(".abbrev");
					if (abbrevs != null && abbrevs.size() > 0) {
						final Element abbrev = abbrevs.get(0);
						if (x == 0) {
							game.setAwayshortname(abbrev.html().trim());
						} else {
							game.setHomeshortname(abbrev.html().trim());
						}
					}
				}
				final Elements records = teamcontent.select(".record");
				if (records != null && records.size() > 0) {
					final Element record = records.get(0);
					String teamrecord = record.html().trim();
					if (teamrecord.contains("inner-record")) {
						int p = teamrecord.indexOf("<span");
						if (p != -1) {
							teamrecord = teamrecord.substring(0, p).trim();
						}
					}
					int index = teamrecord.indexOf("-");
					if (x == 0) {
						game.setAwaywins(Integer.parseInt(teamrecord.substring(0, index)));
						game.setAwaylosses(Integer.parseInt(teamrecord.substring(index + 1)));
					} else {
						game.setHomewins(Integer.parseInt(teamrecord.substring(0, index)));
						game.setHomelosses(Integer.parseInt(teamrecord.substring(index + 1)));
					}
				}

				x++;
			}
		}

		final Element linescore = doc.getElementById("linescore");
		if (linescore != null) {
			final Elements trs = linescore.select("tbody tr");
			if (trs != null && trs.size() > 0) {
				int x = 0;
				for (Element tr : trs) {
					LOGGER.debug("tr: " + tr);
					final Elements tds = tr.select("td");
					if (tds != null && tds.size() == 6) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
												game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore());
											} else {
												game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
												game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore());
											}
										}
										break;
									case 5:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfinalscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefinalscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html() + xhtml, t);
						}
					} else if (tds != null && tds.size() == 7) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore());
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore());
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 8) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore());
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore());
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 9) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
//								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwayotthreescore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore());
										} else {
											game.setHomeotthreescore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore());
										}
										break;
									case 8:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 10) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
//								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore());
										} else {
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore());
										}
										break;
									case 8:
										if (x == 0) {
											game.setAwayotfourscore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore() + game.getAwayotfourscore());
										} else {
											game.setHomeotfourscore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore() + game.getHomeotfourscore());
										}
										break;
									case 9:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 11) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
//								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore());
										} else {
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore());
										}
										break;
									case 8:
										if (x == 0) {
											game.setAwayotfourscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotfourscore(Integer.parseInt(tdhtml));
										}
										break;
									case 9:
										if (x == 0) {
											game.setAwayotfivescore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore() + game.getAwayotfourscore() + game.getAwayotfivescore());
										} else {
											game.setHomeotfivescore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore() + game.getHomeotfourscore() + game.getHomeotfivescore());
										}
										break;
									case 10:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 12) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
//								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore());
										} else {
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore());
										}
										break;
									case 8:
										if (x == 0) {
											game.setAwayotfourscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotfourscore(Integer.parseInt(tdhtml));
										}
										break;
									case 9:
										if (x == 0) {
											game.setAwayotfivescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotfivescore(Integer.parseInt(tdhtml));
										}
										break;
									case 10:
										if (x == 0) {
											game.setAwayotsixscore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore() + game.getAwayotfourscore() + game.getAwayotfivescore() + game.getAwayotsixscore());
										} else {
											game.setHomeotsixscore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore() + game.getHomeotfourscore() + game.getHomeotfivescore() + game.getHomeotsixscore());
										}
										break;
									case 11:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					}
	
					x++;
				}
	
				// Determine the winner
				int awayscore = game.getAwayfinalscore();
				int homescore = game.getHomefinalscore();
				if (awayscore > homescore) {
					game.setAwaywin(true);
				} else {
					game.setHomewin(true);
				}
			}
		}

		return game;
	}

	/**
	 * 
	 * @param xhtml
	 * @param game
	 * @throws BatchException
	 */
	public void parseNcaafGameMatchupTeamData(String xhtml, EspnCollegeFootballGameData game) throws BatchException {
		LOGGER.info("Entering parseNcaafGameMatchupTeamData()");

		int hindex = xhtml.indexOf("\"html\":\"");
		LOGGER.debug("herehere0");
		if (hindex != -1) {
			xhtml = xhtml.substring(hindex + "\"html\":\"".length());
			hindex = xhtml.indexOf("\"},\"analytics");
			if (hindex != -1) {
				xhtml = xhtml.substring(0, hindex);
				xhtml = xhtml.replaceAll("\\\\\"", "\"");
				xhtml = xhtml.replaceAll("<\\\\/script>", "</script>");
				xhtml = "<html><body>" + xhtml + "</body></html>";
			}
		}

		final Document doc = parseXhtml(xhtml);

		// Conference game?
		if (xhtml.contains("Conf</span>")) {
			game.setIsconferencegame(true);
		}

		final Elements gamedetails = doc.select(".game-details");
		if (gamedetails != null && gamedetails.size() > 0) {
			final Element gamedetail = gamedetails.get(0);
//			LOGGER.debug("gamedetail: " + gamedetail);
			final Elements gamelocations = gamedetail.select(".game-location");
			if (gamelocations != null && gamelocations.size() > 0) {
				final Element gamelocation = gamelocations.get(0);
				game.setEventlocation(gamelocation.html().trim());
			}

			final Elements gamedatetime = gamedetail.select(".game-date-time span");
			if (gamedatetime != null && gamedatetime.size() > 0) {
				for (Element gdt : gamedatetime) {
					if (gdt.hasAttr("data-date")) {
						final String datadate = gdt.attr("data-date");
						// 2018-04-03T01:20Z
						final SimpleDateFormat ddate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
						ddate.setTimeZone(TimeZone.getTimeZone("UTC"));
						try {
							final Date gdate = ddate.parse(datadate);
							final Calendar cdate = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							cdate.setTime(gdate);

							int month = cdate.get(Calendar.MONTH) + 1;
							int day = cdate.get(Calendar.DAY_OF_MONTH);
							int year = cdate.get(Calendar.YEAR);
							int hour = cdate.get(Calendar.HOUR_OF_DAY);
							int minute = cdate.get(Calendar.MINUTE);
							int ampm = cdate.get(Calendar.AM_PM);

							game.setMonth(month);
							game.setDay(day);
							game.setYear(year);
							game.setHour(hour);
							game.setMinute(minute);
							if (ampm == 1) {
								game.setAmpm("pm");
							} else {
								game.setAmpm("am");
							}
							game.setTimezone("PT");
							game.setGamedate(gdate);
						} catch (ParseException pe) {
							LOGGER.error(pe.getMessage(), pe);
						}
					}
				}
			}

			final Elements gamenetworks = gamedetail.select(".game-network");
			if (gamenetworks != null && gamenetworks.size() > 0) {
				final Element gamenetwork = gamenetworks.get(0);
				game.setTv(gamenetwork.html().replace("Coverage: ", "").trim());
			}
		}

		final Elements locationdetails = doc.select(".location-details ul li");
		if (locationdetails != null && locationdetails.size() > 0) {
			final Element li = locationdetails.get(0);
			final Elements spans = li.select("span");
			if (spans != null && spans.size() > 0) {
				final Element span = spans.get(0);
				String zipcode = span.html();
				if (zipcode != null && zipcode.length() > 0) {
					LOGGER.debug("zipcode: " + zipcode);
					game.setZipcode(Integer.parseInt(zipcode.trim()));
				}
			}
			String lihtml = li.html();
			if (lihtml != null && lihtml.length() > 0) {
				int index = lihtml.indexOf(",");
				if (index != -1) {
					game.setCity(lihtml.substring(0, index));
					lihtml = lihtml.substring(index + 1).trim();
					index = lihtml.indexOf("<span>");
					if (index != -1) {
						game.setState(lihtml.substring(0, index));
					}
				}
			}
		}

		final Elements oddsdetails = doc.select(".odds-details");
		if (oddsdetails != null && oddsdetails.size() > 0) {
			final Element oddsdetail = oddsdetails.get(0);
			final Elements lis = oddsdetail.select("ul li");
			int z = 0;
			for (Element li : lis) {
				if (z == 0) {
					String odds = li.html();
					int index = odds.indexOf("Line: ");
					if (index != -1) {
						odds = odds.substring(index + 6);
						index = odds.indexOf(" ");
						if (index != -1) {
							game.setLinefavorite(odds.substring(0, index).trim());
							odds = odds.substring(index + 1).trim();
							game.setLine(Float.parseFloat(odds));
							if (odds.startsWith("-")) {
								game.setLineindicator("-");
								game.setLinevalue(Float.parseFloat(odds.substring(1).trim()));
							}
						}
					}
				} else {
					String odds = li.html();
					int index = odds.indexOf("Over/Under: ");
					if (index != -1) {
						odds = odds.substring(index + "Over/Under: ".length()).trim();
						odds = super.reformatValues(odds);
						game.setTotal(Float.parseFloat(odds));
					}
				}

				z++;
			}
			
			final Elements capacities = oddsdetail.select(".capacity");
			if (capacities != null && capacities.size() > 0) {
				final Element capacity = capacities.get(0);
				String attendance = capacity.html();
				int index = attendance.indexOf("Attendance: ");
				if (index != -1) {
					attendance = attendance.substring(index + "Attendance: ".length()).replace(",", "").trim();
					game.setAttendance(Integer.parseInt(attendance));
				}
			}
		}

		final Elements trs = doc.select("#teamstats-wrap div table tbody tr");
		int x = 0;
		if (trs != null && trs.size() > 0) {
			for (Element tr : trs) {
				final Elements tds = tr.select("td");
				int w = 0;
				switch (x++) {
					case 0:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayfirstdowns(Integer.parseInt(tdhtml));
									break;
								case 2:
									game.setHomefirstdowns(Integer.parseInt(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 1:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							int index = tdhtml.indexOf("-");
							switch (w++) {
								case 1:
									game.setAwaythirdefficiencymade(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setAwaythirdefficiencyattempts(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								case 2:
									game.setHomethirdefficiencymade(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setHomethirdefficiencyattempts(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								default:
									break;
							}
						}
						break;
					case 2:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							int index = tdhtml.indexOf("-");
							switch (w++) {
								case 1:
									game.setAwayfourthefficiencymade(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setAwayfourthefficiencyattempts(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								case 2:
									game.setHomefourthefficiencymade(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setHomefourthefficiencyattempts(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								default:
									break;
							}
						}
						break;
					case 3:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwaytotalyards(Integer.parseInt(tdhtml));
									break;
								case 2:
									game.setHometotalyards(Integer.parseInt(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 4:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwaypassingyards(Integer.parseInt(tdhtml));
									break;
								case 2:
									game.setHomepassingyards(Integer.parseInt(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 5:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							int index = tdhtml.indexOf("-");
							switch (w++) {
								case 1:
									game.setAwaypasscomp(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setAwaypassattempts(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								case 2:
									game.setHomepasscomp(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setHomepassattempts(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								default:
									break;
							}
						}
						break;
					case 6:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayyardsperpass(Float.parseFloat(tdhtml));
									break;
								case 2:
									game.setHomeyardsperpass(Float.parseFloat(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 7:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayinterceptions(Integer.parseInt(tdhtml));
									break;
								case 2:
									game.setHomeinterceptions(Integer.parseInt(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 8:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayrushingyards(Integer.parseInt(tdhtml));
									break;
								case 2:
									game.setHomerushingyards(Integer.parseInt(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 9:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayrushingattempts(Integer.parseInt(tdhtml));
									break;
								case 2:
									game.setHomerushingattempts(Integer.parseInt(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 10:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayyardsperrush(Float.parseFloat(tdhtml));
									break;
								case 2:
									game.setHomeyardsperrush(Float.parseFloat(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 11 :
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							int index = tdhtml.indexOf("-");
							switch (w++) {
								case 1:
									game.setAwaypenalties(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setAwaypenaltyyards(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								case 2:
									game.setHomepenalties(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setHomepenaltyyards(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								default:
									break;
							}
						}
						break;
					case 12 :
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayturnovers(Integer.parseInt(tdhtml));
									break;
								case 2:
									game.setHometurnovers(Integer.parseInt(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 13 :
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayfumbleslost(Integer.parseInt(tdhtml));
									break;
								case 2:
									game.setHomefumbleslost(Integer.parseInt(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 15 :
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							int index = tdhtml.indexOf(":");
							switch (w++) {
								case 1:
									game.setAwaypossessionminutes(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setAwaypossessionseconds(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								case 2:
									game.setHomepossessionminutes(Integer.parseInt(tdhtml.substring(0, index).trim()));
									game.setHomepossessionseconds(Integer.parseInt(tdhtml.substring(index + 1).trim()));
									break;
								default:
									break;
							}
						}
						break;
					default:
						break;
				}
			}
		} else {
//			LOGGER.debug("game: " + game);
			throw new BatchException();
		}

		LOGGER.info("Exiting parseNcaafGameMatchupTeamData()");
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public EspnCollegeFootballGameData parseNcaafTeamDataForBets(String xhtml) throws BatchException {
		LOGGER.info("Entering parseNcaafTeamDataForBets()");
		final EspnCollegeFootballGameData game = new EspnCollegeFootballGameData();

		final Document doc = parseXhtml(xhtml);

		// Conference game?
		if (xhtml.contains("Conf</span>")) {
			game.setIsconferencegame(true);
		}

		final Elements teamcontents = doc.select(".team__content");
		if (teamcontents != null && teamcontents.size() > 0) {
			int x = 0;
			for (Element teamcontent : teamcontents) {
				final Elements divs = teamcontent.select(".team-info-wrapper");
				if (divs != null && divs.size() > 0) {
					final Element div = divs.get(0);
					final Elements ranks = div.select(".rank");
					if (ranks != null && ranks.size() > 0) {
						final Element rank = ranks.get(0);
						if (x == 0) {
							game.setAwayranking(Integer.parseInt(rank.html().trim()));
						} else {
							game.setHomeranking(Integer.parseInt(rank.html().trim()));
						}
					}
					final Elements longnames = div.select(".long-name");
					if (longnames != null && longnames.size() > 0) {
						final Element longname = longnames.get(0);
						if (x == 0) {
							game.setAwaycollegename(longname.html().trim());
							game.setAwayteam(longname.html().trim());
						} else {
							game.setHomecollegename(longname.html().trim());
							game.setHometeam(longname.html().trim());
						}
					}
					final Elements shortnames = div.select(".short-name");
					if (shortnames != null && shortnames.size() > 0) {
						final Element shortname = shortnames.get(0);
						if (x == 0) {
							game.setAwaymascotname(shortname.html().trim());
						} else {
							game.setHomemascotname(shortname.html().trim());
						}
					}
					final Elements abbrevs = div.select(".abbrev");
					if (abbrevs != null && abbrevs.size() > 0) {
						final Element abbrev = abbrevs.get(0);
						if (x == 0) {
							game.setAwayshortname(abbrev.html().trim());
						} else {
							game.setHomeshortname(abbrev.html().trim());
						}
					}
				}
				final Elements records = teamcontent.select(".record");
				if (records != null && records.size() > 0) {
					final Element record = records.get(0);
					String teamrecord = record.html().trim();
					if (teamrecord.contains("inner-record")) {
						int p = teamrecord.indexOf("<span");
						if (p != -1) {
							teamrecord = teamrecord.substring(0, p).trim();
						}
					}
					int index = teamrecord.indexOf("-");
					if (x == 0) {
						game.setAwaywins(Integer.parseInt(teamrecord.substring(0, index)));
						game.setAwaylosses(Integer.parseInt(teamrecord.substring(index + 1)));
					} else {
						game.setHomewins(Integer.parseInt(teamrecord.substring(0, index)));
						game.setHomelosses(Integer.parseInt(teamrecord.substring(index + 1)));
					}
				}

				x++;
			}
		}

		final Elements gamedetails = doc.select(".game-details");
		if (gamedetails != null && gamedetails.size() > 0) {
			final Element gamedetail = gamedetails.get(0);
			LOGGER.debug("gamedetail: " + gamedetail);
			final Elements gamelocations = gamedetail.select(".game-location");
			if (gamelocations != null && gamelocations.size() > 0) {
				final Element gamelocation = gamelocations.get(0);
				game.setEventlocation(gamelocation.html().trim());
			}

			final Elements gamedatetime = gamedetail.select(".game-date-time span");
			if (gamedatetime != null && gamedatetime.size() > 0) {
				for (Element gdt : gamedatetime) {
					if (gdt.hasAttr("data-date")) {
						final String datadate = gdt.attr("data-date");
						// 2018-04-03T01:20Z
						final SimpleDateFormat ddate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
						ddate.setTimeZone(TimeZone.getTimeZone("UTC"));
						try {
							final Date gdate = ddate.parse(datadate);
							final Calendar cdate = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
							cdate.setTime(gdate);

							int month = cdate.get(Calendar.MONTH) + 1;
							int day = cdate.get(Calendar.DAY_OF_MONTH);
							int year = cdate.get(Calendar.YEAR);
							int hour = cdate.get(Calendar.HOUR_OF_DAY);
							int minute = cdate.get(Calendar.MINUTE);
							int ampm = cdate.get(Calendar.AM_PM);

							game.setMonth(month);
							game.setDay(day);
							game.setYear(year);
							game.setHour(hour);
							game.setMinute(minute);
							if (ampm == 1) {
								game.setAmpm("pm");
							} else {
								game.setAmpm("am");
							}
							game.setTimezone("PT");
							game.setGamedate(gdate);
						} catch (ParseException pe) {
							LOGGER.error(pe.getMessage(), pe);
						}
					}
				}
			}

			final Elements gamenetworks = gamedetail.select(".game-network");
			if (gamenetworks != null && gamenetworks.size() > 0) {
				final Element gamenetwork = gamenetworks.get(0);
				game.setTv(gamenetwork.html().replace("Coverage: ", "").trim());
			}
		}

		final Elements locationdetails = doc.select(".location-details ul li");
		if (locationdetails != null && locationdetails.size() > 0) {
			final Element li = locationdetails.get(0);
			final Elements spans = li.select("span");
			if (spans != null && spans.size() > 0) {
				final Element span = spans.get(0);
				String zipcode = span.html();
				if (zipcode != null && zipcode.length() > 0) {
					LOGGER.debug("zipcode: " + zipcode);
					game.setZipcode(Integer.parseInt(zipcode.trim()));
				}
			}
			String lihtml = li.html();
			if (lihtml != null && lihtml.length() > 0) {
				int index = lihtml.indexOf(",");
				if (index != -1) {
					game.setCity(lihtml.substring(0, index));
					lihtml = lihtml.substring(index + 1).trim();
					index = lihtml.indexOf("<span>");
					if (index != -1) {
						game.setState(lihtml.substring(0, index));
					}
				}
			}
		}

		final Elements oddsdetails = doc.select(".odds-details");
		if (oddsdetails != null && oddsdetails.size() > 0) {
			final Element oddsdetail = oddsdetails.get(0);
			final Elements lis = oddsdetail.select("ul li");
			int z = 0;
			for (Element li : lis) {
				if (z == 0) {
					LOGGER.debug("li: " + li);
					String odds = li.html();
					int index = odds.indexOf("Line: ");
					if (index != -1) {
						odds = odds.substring(index + 6);
						index = odds.indexOf(" ");
						if (index != -1) {
							game.setLinefavorite(odds.substring(0, index).trim());
							odds = odds.substring(index + 1).trim();
							game.setLine(Float.parseFloat(odds));
							if (odds.startsWith("-")) {
								game.setLineindicator("-");
								game.setLinevalue(Float.parseFloat(odds.substring(1).trim()));
							}
						}
					}
				} else {
					String odds = li.html();
					int index = odds.indexOf("Over/Under: ");
					if (index != -1) {
						odds = odds.substring(index + "Over/Under: ".length()).trim();
						odds = super.reformatValues(odds);
						game.setTotal(Float.parseFloat(odds));
					}
				}

				z++;
			}
			
			final Elements capacities = oddsdetail.select(".capacity");
			if (capacities != null && capacities.size() > 0) {
				final Element capacity = capacities.get(0);
				String attendance = capacity.html();
				int index = attendance.indexOf("Attendance: ");
				if (index != -1) {
					attendance = attendance.substring(index + "Attendance: ".length()).replace(",", "").trim();
					game.setAttendance(Integer.parseInt(attendance));
				}
			}
		} else {
			final Elements spans = doc.select(".game-status .line");
			
			if (spans != null && spans.size() > 0) {
				String odds = spans.get(0).html();
				int index = odds.indexOf(" ");
				if (index != -1) {
					game.setLinefavorite(odds.substring(0, index).trim());
					odds = odds.substring(index + 1).trim();
					game.setLine(Float.parseFloat(odds));
					if (odds.startsWith("-")) {
						game.setLineindicator("-");
						game.setLinevalue(Float.parseFloat(odds.substring(1).trim()));
					}
				}
			} else {
				LOGGER.debug("xhtml: " + xhtml);
				final Elements trs = doc.select(".pick-center-content .smallTable tbody tr");
				if (trs != null && trs.size() > 0) {
					int trcount = 0;
					for (Element tr : trs) {
						LOGGER.debug("tr: " + tr);
						switch (trcount++) {
							case 0:
								final Elements atds = tr.select(".awayteam");
								if (atds != null && atds.size() > 0) {
									Element td = atds.get(0);
									String ranking = td.html();
									if (ranking != null && ranking.length() > 0) {
										ranking = ranking.trim();
										if (ranking.equals("--")) {
											game.setAwayranking(0);
										} else {
											game.setAwayranking(Integer.parseInt(ranking));
										}
									}
								}
								final Elements htds = tr.select(".hometeam");
								if (htds != null && htds.size() > 0) {
									Element td = htds.get(0);
									String ranking = td.html();
									if (ranking != null && ranking.length() > 0) {
										ranking = ranking.trim();
										if (ranking.equals("--")) {
											game.setHomeranking(0);
										} else {
											game.setHomeranking(Integer.parseInt(ranking));
										}
									}
								}
								break;
							case 3:
								final Elements scores = tr.select(".score");
								if (scores != null && scores.size() > 1) {
									String ascore = scores.get(0).html();
									String hscore = scores.get(1).html();
									
									LOGGER.error("ascore: " + ascore);
									LOGGER.error("hscore: " + hscore);
	
									if (ascore != null && ascore.length() > 0 && 
										hscore != null && hscore.length() > 0) {
										if (!ascore.startsWith("--") && ascore.startsWith("-")) {
											ascore = ascore.trim();
											game.setLine(Float.valueOf(ascore));
											game.setLinefavorite(game.getAwayshortname());
											game.setLineindicator(ascore.substring(0, 1));
											game.setLinevalue(Float.valueOf(ascore.substring(0)));
										} else if (!hscore.startsWith("--") && hscore.startsWith("-")) {
											hscore = hscore.trim();
											game.setLine(Float.valueOf(hscore));
											game.setLinefavorite(game.getHomeshortname());
											game.setLineindicator(hscore.substring(0, 1));
											game.setLinevalue(Float.valueOf(hscore.substring(0)));										
										}
									}
								}
								break;
							case 1:
							case 2:
							default:
								break;
						}
					}
				}
			}
		}

		final Element linescore = doc.getElementById("linescore");
		if (linescore != null) {
			final Elements trs = linescore.select("tbody tr");
			if (trs != null && trs.size() > 0) {
				int x = 0;
				for (Element tr : trs) {
					LOGGER.debug("tr: " + tr);
					final Elements tds = tr.select("td");
					if (tds != null && tds.size() == 6) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
												game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore());
											} else {
												game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
												game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore());
											}
										}
										break;
									case 5:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfinalscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefinalscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html() + xhtml, t);
						}
					} else if (tds != null && tds.size() == 7) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore());
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore());
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 8) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore());
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore());
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 9) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwayotthreescore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore());
										} else {
											game.setHomeotthreescore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore());
										}
										break;
									case 8:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 10) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore());
										} else {
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore());
										}
										break;
									case 8:
										if (x == 0) {
											game.setAwayotfourscore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore() + game.getAwayotfourscore());
										} else {
											game.setHomeotfourscore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore() + game.getHomeotfourscore());
										}
										break;
									case 9:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 11) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore());
										} else {
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore());
										}
										break;
									case 8:
										if (x == 0) {
											game.setAwayotfourscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotfourscore(Integer.parseInt(tdhtml));
										}
										break;
									case 9:
										if (x == 0) {
											game.setAwayotfivescore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore() + game.getAwayotfourscore() + game.getAwayotfivescore());
										} else {
											game.setHomeotfivescore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore() + game.getHomeotfourscore() + game.getHomeotfivescore());
										}
										break;
									case 10:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					} else if (tds != null && tds.size() == 12) {
						try {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
									case 1:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwayfirstquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomefirstquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 2:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaysecondquarterscore(Integer.parseInt(tdhtml));
												game.setAwayfirsthalfscore(game.getAwayfirstquarterscore() + game.getAwaysecondquarterscore());
											} else {
												game.setHomesecondquarterscore(Integer.parseInt(tdhtml));
												game.setHomefirsthalfscore(game.getHomefirstquarterscore() + game.getHomesecondquarterscore());
											}
										}
									case 3:
										if (tdhtml != null && tdhtml.length() > 0) {
											if (x == 0) {
												game.setAwaythirdquarterscore(Integer.parseInt(tdhtml));
											} else {
												game.setHomethirdquarterscore(Integer.parseInt(tdhtml));
											}
										}
										break;
									case 4:
										if (x == 0) {
											game.setAwayfourthquarterscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefourthquarterscore(Integer.parseInt(tdhtml));
										}
										break;
									case 5:
										if (x == 0) {
											game.setAwayotonescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotonescore(Integer.parseInt(tdhtml));
										}
										break;
									case 6:
										if (x == 0) {
											game.setAwayottwoscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeottwoscore(Integer.parseInt(tdhtml));
										}
										break;
									case 7:
										if (x == 0) {
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore());
										} else {
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore());
										}
										break;
									case 8:
										if (x == 0) {
											game.setAwayotfourscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotfourscore(Integer.parseInt(tdhtml));
										}
										break;
									case 9:
										if (x == 0) {
											game.setAwayotfivescore(Integer.parseInt(tdhtml));
										} else {
											game.setHomeotfivescore(Integer.parseInt(tdhtml));
										}
										break;
									case 10:
										if (x == 0) {
											game.setAwayotsixscore(Integer.parseInt(tdhtml));
											game.setAwaysecondhalfscore(game.getAwaythirdquarterscore() + game.getAwayfourthquarterscore() + game.getAwayotonescore() + game.getAwayottwoscore() + game.getAwayotthreescore() + game.getAwayotfourscore() + game.getAwayotfivescore() + game.getAwayotsixscore());
										} else {
											game.setHomeotsixscore(Integer.parseInt(tdhtml));
											game.setHomesecondhalfscore(game.getHomethirdquarterscore() + game.getHomefourthquarterscore() + game.getHomeotonescore() + game.getHomeottwoscore() + game.getHomeotthreescore() + game.getHomeotfourscore() + game.getHomeotfivescore() + game.getHomeotsixscore());
										}
										break;
									case 11:
										if (x == 0) {
											game.setAwayfinalscore(Integer.parseInt(tdhtml));
										} else {
											game.setHomefinalscore(Integer.parseInt(tdhtml));
										}
										break;
									default:
										break;
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage() + tr.html(), t);
						}
					}
	
					x++;
				}
	
				// Determine the winner
				int awayscore = game.getAwayfinalscore();
				int homescore = game.getHomefinalscore();
				if (awayscore > homescore) {
					game.setAwaywin(true);
				} else {
					game.setHomewin(true);
				}
			}
		}

		LOGGER.info("Entering parseNcaafTeamDataForBets()");
		return game;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public EspnCollegeBasketballGameData parseNcaabTeamData(String xhtml) throws BatchException {
		LOGGER.info("Entering parseNcaabTeamData()");
		final EspnCollegeBasketballGameData game = new EspnCollegeBasketballGameData();

		LOGGER.debug("xhtml: " + xhtml);
		final Document doc = parseXhtml(xhtml);

		try {
			// Conference game?
			if (xhtml.contains("Conf</span>")) {
				game.setIsconferencegame(true);
			}

			final Elements teamcontents = doc.select(".team__content");
			if (teamcontents != null && teamcontents.size() > 0) {
				int x = 0;
				for (Element teamcontent : teamcontents) {
					final Elements divs = teamcontent.select(".team-info-wrapper");
					if (divs != null && divs.size() > 0) {
						final Element div = divs.get(0);
						final Elements ranks = div.select(".rank");
						if (ranks != null && ranks.size() > 0) {
							final Element rank = ranks.get(0);
							if (x == 0) {
								game.setAwayranking(Integer.parseInt(rank.html().trim()));
							} else {
								game.setHomeranking(Integer.parseInt(rank.html().trim()));
							}
						}
						final Elements longnames = div.select(".long-name");
						if (longnames != null && longnames.size() > 0) {
							final Element longname = longnames.get(0);
							if (x == 0) {
								final String teamName = longname.html().trim().toUpperCase();
								game.setAwaycollegename(teamName);
								game.setAwayteam(teamName);

								final Iterator<String> itr = NCAABMapping.keySet().iterator();
								while (itr.hasNext()) {
									final String key = itr.next();
									if (key.toUpperCase().equals(teamName.toUpperCase())) {
										game.setAwaycollegename(NCAABMapping.get(key).toUpperCase());
										game.setAwayteam(NCAABMapping.get(key).toUpperCase());
									}
								}
							} else {
								final String teamName = longname.html().trim().toUpperCase();
								game.setHomecollegename(teamName);
								game.setHometeam(teamName);

								final Iterator<String> itr = NCAABMapping.keySet().iterator();
								while (itr.hasNext()) {
									final String key = itr.next();
									if (key.toUpperCase().equals(teamName.toUpperCase())) {
										game.setHomecollegename(NCAABMapping.get(key).toUpperCase());
										game.setHometeam(NCAABMapping.get(key).toUpperCase());
									}
								}
							}
						}

						final Elements shortnames = div.select(".short-name");
						if (shortnames != null && shortnames.size() > 0) {
							final Element shortname = shortnames.get(0);
							if (x == 0) {
								game.setAwaymascotname(shortname.html().trim().toUpperCase());
							} else {
								game.setHomemascotname(shortname.html().trim().toUpperCase());
							}
						}

						final Elements abbrevs = div.select(".abbrev");
						if (abbrevs != null && abbrevs.size() > 0) {
							final Element abbrev = abbrevs.get(0);
							if (x == 0) {
								game.setAwayshortname(abbrev.html().trim().toUpperCase());
							} else {
								game.setHomeshortname(abbrev.html().trim().toUpperCase());
							}
						}
					}

					final Elements records = teamcontent.select(".record");
					if (records != null && records.size() > 0) {
						final Element record = records.get(0);
						String teamrecord = record.html().trim();
						if (teamrecord.contains("inner-record")) {
							int p = teamrecord.indexOf("<span");
							if (p != -1) {
								teamrecord = teamrecord.substring(0, p).trim();
							}
						}
						int index = teamrecord.indexOf("-");
						if (x == 0) {
							if (teamrecord.contains("-")) {
								game.setAwaywins(Integer.parseInt(teamrecord.substring(0, index)));
								game.setAwaylosses(Integer.parseInt(teamrecord.substring(index + 1)));
							}
						} else {
							if (teamrecord.contains("-")) {
								game.setHomewins(Integer.parseInt(teamrecord.substring(0, index)));
								game.setHomelosses(Integer.parseInt(teamrecord.substring(index + 1)));
							}
						}
					}

					x++;
				}
			}

			final Elements gamedetails = doc.select(".game-information .content");
			if (gamedetails != null && gamedetails.size() > 0) {
				final Element gamedetail = gamedetails.get(0);
				LOGGER.debug("gamedetail: " + gamedetail);
				final Elements gamelocations = gamedetail.select(".game-location");
				if (gamelocations != null && gamelocations.size() > 0) {
					final Element gamelocation = gamelocations.get(0);
					LOGGER.debug("gamelocation: " + gamelocation);
					game.setEventlocation(gamelocation.html().trim());
				}

				final Elements gamedatetime = gamedetail.select(".game-date-time span");
				if (gamedatetime != null && gamedatetime.size() > 0) {
					for (Element gdt : gamedatetime) {
						if (gdt.hasAttr("data-date")) {
							final String datadate = gdt.attr("data-date");
							// 2018-04-03T01:20Z
							final SimpleDateFormat ddate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
							ddate.setTimeZone(TimeZone.getTimeZone("UTC"));
							try {
								final Date gdate = ddate.parse(datadate);
								final Calendar cdate = Calendar
										.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
								cdate.setTime(gdate);

								int month = cdate.get(Calendar.MONTH) + 1;
								int day = cdate.get(Calendar.DAY_OF_MONTH);
								int year = cdate.get(Calendar.YEAR);
								int hour = cdate.get(Calendar.HOUR_OF_DAY);
								int minute = cdate.get(Calendar.MINUTE);
								int ampm = cdate.get(Calendar.AM_PM);

								game.setMonth(month);
								game.setDay(day);
								game.setYear(year);
								game.setHour(hour);
								game.setMinute(minute);
								if (ampm == 1) {
									game.setAmpm("pm");
								} else {
									game.setAmpm("am");
								}
								game.setTimezone("PT");
								game.setGamedate(gdate);
							} catch (ParseException pe) {
								LOGGER.error(pe.getMessage(), pe);
							}
						}
					}
				}

				final Elements gamenetworks = gamedetail.select(".game-network");
				if (gamenetworks != null && gamenetworks.size() > 0) {
					final Element gamenetwork = gamenetworks.get(0);
					game.setTv(gamenetwork.html().replace("Coverage: ", "").trim());
				}
			}

			final Elements locationdetails = doc.select(".location-details ul li");
			if (locationdetails != null && locationdetails.size() > 0) {
				final Element li = locationdetails.get(0);
				final Elements spans = li.select("span");
				if (spans != null && spans.size() > 0) {
					final Element span = spans.get(0);
					String zipcode = span.html();
					if (zipcode != null && zipcode.length() > 0) {
						game.setZipcode(Integer.parseInt(zipcode.trim()));
					}
				}
				String lihtml = li.html();
				if (lihtml != null && lihtml.length() > 0) {
					int index = lihtml.indexOf(",");
					if (index != -1) {
						game.setCity(lihtml.substring(0, index).trim());
						lihtml = lihtml.substring(index + 1).trim();
						index = lihtml.indexOf("<span>");
						if (index != -1) {
							game.setState(lihtml.substring(0, index).trim());
						}
					}
				}
			}

			final Elements oddsdetails = doc.select(".odds-details");
			if (oddsdetails != null && oddsdetails.size() > 0) {
				final Element oddsdetail = oddsdetails.get(0);
				final Elements lis = oddsdetail.select("ul li");
				int z = 0;
				for (Element li : lis) {
					if (z == 0) {
						String odds = li.html();
						int index = odds.indexOf("Line: ");
						if (index != -1) {
							odds = odds.substring(index + 6);
							index = odds.indexOf(" ");
							if (index != -1) {
								game.setLinefavorite(odds.substring(0, index).trim());
								odds = odds.substring(index + 1).trim();
								game.setLine(Float.parseFloat(odds));
								if (odds.startsWith("-")) {
									game.setLineindicator("-");
									game.setLinevalue(Float.parseFloat(odds.substring(1).trim()));
								}
							}
						}
					} else {
						String odds = li.html();
						int index = odds.indexOf("Over/Under: ");
						if (index != -1) {
							odds = odds.substring(index + "Over/Under: ".length()).trim();
							odds = super.reformatValues(odds);
							game.setTotal(Float.parseFloat(odds));
						}
					}

					z++;
				}
			}

			final Elements capacities = doc.select(".location-details .capacity");
			if (capacities != null && capacities.size() > 0) {
				final Element capacity = capacities.get(0);
				String attendance = capacity.html();
				int index = attendance.indexOf("Attendance: ");
				if (index != -1) {
					attendance = attendance.substring(index + "Attendance: ".length()).replace(",", "").trim();
					game.setAttendance(Integer.parseInt(attendance));
				}
			}

			final Elements refs = doc.select(".game-info-note__content");
			if (refs != null && refs.size() > 0) {
				final Element ref = refs.get(0);
				String refdata = ref.html();
				if (refdata != null && refdata.length() > 0) {
					int rindex = refdata.indexOf(",");
					if (rindex != -1) {
						String ref1 = refdata.substring(0, rindex).trim();
						game.setRef1(ref1);
						refdata = refdata.substring(rindex + 1);
						rindex = refdata.indexOf(",");
						if (rindex != -1) {
							String ref2 = refdata.substring(0, rindex).trim();
							String ref3 = refdata.substring(rindex + 1).trim();
							game.setRef2(ref2);
							game.setRef3(ref3);
						} else {
							String ref2 = refdata.trim();
							game.setRef2(ref2);
						}
					}
				}
			}

			final Element linescore = doc.getElementById("linescore");
			if (linescore != null) {
				final Elements trs = linescore.select("tbody tr");
				if (trs != null && trs.size() > 0) {
					int x = 0;
					for (Element tr : trs) {
						LOGGER.debug("tr: " + tr);
						final Elements tds = tr.select("td");
						if (tds != null && tds.size() == 4) {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
								case 1:
									if (x == 0) {
										game.setAwayfirsthalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefirsthalfscore(Integer.parseInt(tdhtml));
									}
									break;
								case 2:
									if (x == 0) {
										game.setAwaysecondhalfscore(Integer.parseInt(tdhtml));
										game.setAwaysecondhalfotscore(game.getAwaysecondhalfscore());
									} else {
										game.setHomesecondhalfscore(Integer.parseInt(tdhtml));
										game.setHomesecondhalfotscore(game.getHomesecondhalfscore());
									}
								case 3:
									if (x == 0) {
										game.setAwayfinalscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefinalscore(Integer.parseInt(tdhtml));
									}
									break;
								default:
									break;
								}
							}
						}
						if (tds != null && tds.size() == 5) {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
								case 1:
									if (x == 0) {
										game.setAwayfirsthalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefirsthalfscore(Integer.parseInt(tdhtml));
									}
									break;
								case 2:
									if (x == 0) {
										game.setAwaysecondhalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomesecondhalfscore(Integer.parseInt(tdhtml));
									}
								case 3:
									if (x == 0) {
										game.setAwayotonescore(Integer.parseInt(tdhtml));
										game.setAwaysecondhalfotscore(
												game.getAwaysecondhalfscore() + game.getAwayotonescore());
									} else {
										game.setHomeotonescore(Integer.parseInt(tdhtml));
										game.setHomesecondhalfotscore(
												game.getHomesecondhalfscore() + game.getHomeotonescore());
									}
								case 4:
									if (x == 0) {
										game.setAwayfinalscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefinalscore(Integer.parseInt(tdhtml));
									}
									break;
								default:
									break;
								}
							}
						}
						if (tds != null && tds.size() == 6) {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
								case 1:
									if (x == 0) {
										game.setAwayfirsthalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefirsthalfscore(Integer.parseInt(tdhtml));
									}
									break;
								case 2:
									if (x == 0) {
										game.setAwaysecondhalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomesecondhalfscore(Integer.parseInt(tdhtml));
									}
								case 3:
									if (x == 0) {
										game.setAwayotonescore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeotonescore(Integer.parseInt(tdhtml));
									}
								case 4:
									if (x == 0) {
										game.setAwayottwoscore(Integer.parseInt(tdhtml));
										game.setAwaysecondhalfotscore(game.getAwaysecondhalfscore()
												+ game.getAwayotonescore() + game.getAwayottwoscore());
									} else {
										game.setHomeottwoscore(Integer.parseInt(tdhtml));
										game.setHomesecondhalfotscore(game.getHomesecondhalfscore()
												+ game.getHomeotonescore() + game.getHomeottwoscore());
									}
								case 5:
									if (x == 0) {
										game.setAwayfinalscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefinalscore(Integer.parseInt(tdhtml));
									}
									break;
								default:
									break;
								}
							}
						}
						if (tds != null && tds.size() == 7) {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
								case 1:
									if (x == 0) {
										game.setAwayfirsthalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefirsthalfscore(Integer.parseInt(tdhtml));
									}
									break;
								case 2:
									if (x == 0) {
										game.setAwaysecondhalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomesecondhalfscore(Integer.parseInt(tdhtml));
									}
								case 3:
									if (x == 0) {
										game.setAwayotonescore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeotonescore(Integer.parseInt(tdhtml));
									}
								case 4:
									if (x == 0) {
										game.setAwayottwoscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeottwoscore(Integer.parseInt(tdhtml));
									}
								case 5:
									if (x == 0) {
										game.setAwayotthreescore(Integer.parseInt(tdhtml));
										game.setAwaysecondhalfotscore(
												game.getAwaysecondhalfscore() + game.getAwayotonescore()
														+ game.getAwayottwoscore() + game.getAwayotthreescore());
									} else {
										game.setHomeotthreescore(Integer.parseInt(tdhtml));
										game.setHomesecondhalfotscore(
												game.getHomesecondhalfscore() + game.getHomeotonescore()
														+ game.getHomeottwoscore() + game.getHomeotthreescore());
									}
								case 6:
									if (x == 0) {
										game.setAwayfinalscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefinalscore(Integer.parseInt(tdhtml));
									}
									break;
								default:
									break;
								}
							}
						}
						if (tds != null && tds.size() == 8) {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
								case 1:
									if (x == 0) {
										game.setAwayfirsthalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefirsthalfscore(Integer.parseInt(tdhtml));
									}
									break;
								case 2:
									if (x == 0) {
										game.setAwaysecondhalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomesecondhalfscore(Integer.parseInt(tdhtml));
									}
								case 3:
									if (x == 0) {
										game.setAwayotonescore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeotonescore(Integer.parseInt(tdhtml));
									}
								case 4:
									if (x == 0) {
										game.setAwayottwoscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeottwoscore(Integer.parseInt(tdhtml));
									}
								case 5:
									if (x == 0) {
										game.setAwayotthreescore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeotthreescore(Integer.parseInt(tdhtml));
									}
								case 6:
									if (x == 0) {
										game.setAwayotfourscore(Integer.parseInt(tdhtml));
										game.setAwaysecondhalfotscore(game.getAwaysecondhalfscore()
												+ game.getAwayotonescore() + game.getAwayottwoscore()
												+ game.getAwayotthreescore() + game.getAwayotfourscore());
									} else {
										game.setHomeotfourscore(Integer.parseInt(tdhtml));
										game.setHomesecondhalfotscore(game.getHomesecondhalfscore()
												+ game.getHomeotonescore() + game.getHomeottwoscore()
												+ game.getHomeotthreescore() + game.getHomeotfourscore());
									}
								case 7:
									if (x == 0) {
										game.setAwayfinalscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefinalscore(Integer.parseInt(tdhtml));
									}
									break;
								default:
									break;
								}
							}
						}
						if (tds != null && tds.size() == 9) {
							int tdcount = 0;
							for (Element td : tds) {
								final String tdhtml = td.html().trim();
								LOGGER.debug("tdhtml: " + tdhtml);
								switch (tdcount++) {
								case 1:
									if (x == 0) {
										game.setAwayfirsthalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefirsthalfscore(Integer.parseInt(tdhtml));
									}
									break;
								case 2:
									if (x == 0) {
										game.setAwaysecondhalfscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomesecondhalfscore(Integer.parseInt(tdhtml));
									}
								case 3:
									if (x == 0) {
										game.setAwayotonescore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeotonescore(Integer.parseInt(tdhtml));
									}
								case 4:
									if (x == 0) {
										game.setAwayottwoscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeottwoscore(Integer.parseInt(tdhtml));
									}
								case 5:
									if (x == 0) {
										game.setAwayotthreescore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeotthreescore(Integer.parseInt(tdhtml));
									}
								case 6:
									if (x == 0) {
										game.setAwayotfourscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomeotfourscore(Integer.parseInt(tdhtml));
									}
								case 7:
									if (x == 0) {
										game.setAwayotfivescore(Integer.parseInt(tdhtml));
										game.setAwaysecondhalfotscore(
												game.getAwaysecondhalfscore() + game.getAwayotonescore()
														+ game.getAwayottwoscore() + game.getAwayotthreescore()
														+ game.getAwayotfourscore() + game.getAwayotfivescore());
									} else {
										game.setHomeotfivescore(Integer.parseInt(tdhtml));
										game.setHomesecondhalfotscore(
												game.getHomesecondhalfscore() + game.getHomeotonescore()
														+ game.getHomeottwoscore() + game.getHomeotthreescore()
														+ game.getHomeotfourscore() + game.getHomeotfivescore());
									}
								case 8:
									if (x == 0) {
										game.setAwayfinalscore(Integer.parseInt(tdhtml));
									} else {
										game.setHomefinalscore(Integer.parseInt(tdhtml));
									}
									break;
								default:
									break;
								}
							}
						}

						x++;
					}
				}

				// Determine the winner
				int awayscore = game.getAwayfinalscore();
				int homescore = game.getHomefinalscore();
				if (awayscore > homescore) {
					game.setAwaywin(true);
				} else {
					game.setHomewin(true);
				}
			}

			final Elements ts = doc.select("#teamstats-wrap div table tbody tr");
			int x = 0;
			for (Element tr : ts) {
				final Elements tds = tr.select("td");
				int w = 0;
				switch (x++) {
					case 0:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							int index = tdhtml.indexOf("-");
							
							if (index != -1) {
								switch (w++) {
									case 1:
										game.setAwayfgmade(Integer.parseInt(tdhtml.substring(0, index).trim()));
										game.setAwayfgattempt(Integer.parseInt(tdhtml.substring(index + 1).trim()));
										break;
									case 2:
										game.setHomefgmade(Integer.parseInt(tdhtml.substring(0, index).trim()));
										game.setHomefgattempt(Integer.parseInt(tdhtml.substring(index + 1).trim()));
										break;
									default:
										break;
								}
							} else {
								
							}
						}
						break;
					case 1:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
								case 1:
									game.setAwayfgpercentage(Float.parseFloat(tdhtml));
									break;
								case 2:
									game.setHomefgpercentage(Float.parseFloat(tdhtml));
									break;
								default:
									break;
							}
						}
						break;
					case 2:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							int index = tdhtml.indexOf("-");
							switch (w++) {
							case 1:
								game.setAway3ptfgmade(Integer.parseInt(tdhtml.substring(0, index).trim()));
								game.setAway3ptfgattempt(Integer.parseInt(tdhtml.substring(index + 1).trim()));
								break;
							case 2:
								game.setHome3ptfgmade(Integer.parseInt(tdhtml.substring(0, index).trim()));
								game.setHome3ptfgattempt(Integer.parseInt(tdhtml.substring(index + 1).trim()));
								break;
							default:
								break;
							}
						}
						break;
					case 3:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAway3ptfgpercentage(Float.parseFloat(tdhtml));
								break;
							case 2:
								game.setHome3ptfgpercentage(Float.parseFloat(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 4:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							int index = tdhtml.indexOf("-");
							switch (w++) {
							case 1:
								game.setAwayftmade(Integer.parseInt(tdhtml.substring(0, index).trim()));
								game.setAwayftattempt(Integer.parseInt(tdhtml.substring(index + 1).trim()));
								break;
							case 2:
								game.setHomeftmade(Integer.parseInt(tdhtml.substring(0, index).trim()));
								game.setHomeftattempt(Integer.parseInt(tdhtml.substring(index + 1).trim()));
								break;
							default:
								break;
							}
						}
						break;
					case 5:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwayftpercentage(Float.parseFloat(tdhtml));
								break;
							case 2:
								game.setHomeftpercentage(Float.parseFloat(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 6:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwaytotalrebounds(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHometotalrebounds(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 7:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwayoffrebounds(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHomeoffrebounds(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 8:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwaydefrebounds(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHomedefrebounds(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 10:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwayassists(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHomeassists(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 11:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwaysteals(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHomesteals(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 12:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwayblocks(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHomeblocks(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 13:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwaytotalturnovers(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHometotalturnovers(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 14:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwaypersonalfouls(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHomepersonalfouls(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					case 15:
						for (Element td : tds) {
							String tdhtml = td.html().trim();
							switch (w++) {
							case 1:
								game.setAwaytechnicalfouls(Integer.parseInt(tdhtml));
								break;
							case 2:
								game.setHometechnicalfouls(Integer.parseInt(tdhtml));
								break;
							default:
								break;
							}
						}
						break;
					default:
						break;
				}
			}
		} catch (Throwable t) {
			LOGGER.error("xhtml: " + xhtml);
			LOGGER.error(t.getMessage(), t);
		}

		return game;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public EspnFootballGame parseNcaafGame(String xhtml) throws BatchException {
		LOGGER.info("Entering parseNcaafGame()");
		final EspnFootballGame game = new EspnFootballGame();
		
		final Document doc = parseXhtml(xhtml);
		final Elements gameInfo = doc.select(".game-information .content");
		if (gameInfo != null && gameInfo.size() > 0) {
			final Element gameinfo = gameInfo.get(0);
			final Elements spans = gameinfo.select(".game-date-time span span");

			int x = 0;
			String fulltime = null;
			String fulldate = null;
			for (Element span : spans) {
				if (x++ == 0) {
					// 4:30 PM PT
					fulltime = span.html();
					String gametime = span.html();
					int index = gametime.indexOf(":");
					if (index != -1) {
						String hour = gametime.substring(0, index).trim();
						game.setHour(Integer.parseInt(hour));
						gametime = gametime.substring(index + 1);
						index = gametime.indexOf(" ");
						if (index != -1) {
							String minute = gametime.substring(0, index);
							game.setMinute(Integer.parseInt(minute));
							gametime = gametime.substring(index + 1).trim();
							index = gametime.indexOf(" ");
							if (index != -1) {
								String ampm = gametime.substring(0, index).trim();
								String timezone = gametime.substring(index + 1).trim();
								game.setAmpm(ampm);
								game.setTimezone(timezone);
							}
						}
					}
				} else {
					// August 26, 2017
					fulldate = span.html();
					String gamedate = span.html();
					int index = gamedate.indexOf(" ");
					if (index != -1) {
						String month = gamedate.substring(0, index).trim();
						game.setMonth(Integer.parseInt(month));
						gamedate = gamedate.substring(index + 1);
						index = gamedate.indexOf(" ");
						if (index != -1) {
							String day = gamedate.substring(0, index).replace(",", "").trim();
							String year = gamedate.substring(index + 1);
							game.setDay(Integer.parseInt(day));
							game.setYear(Integer.parseInt(year));
						}
					}
				}
			}

			// Game date
			final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy hh:mm a z");
			try {
				final Date gamedate = DATE_FORMAT.parse(fulldate + " " + fulltime);
				game.setDate(gamedate);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}

			// TV
			final Elements tv = gameinfo.select(".game-network");
			if (tv != null && tv.size() > 0) {
				Element gamenetwork = tv.get(0);
				String tvhtml = gamenetwork.html().trim();
				game.setTv(tvhtml);
			}

			final Elements lis = gameinfo.select(".location-details ul li");
			if (lis != null && lis.size() > 0) {
				String lihtml = lis.get(0).html();
				int index = lihtml.indexOf(",");
				if (index != -1) {
					String city = lihtml.substring(0, index).trim();
					game.setCity(city);
					lihtml = lihtml.substring(index + 1);
					index = lihtml.indexOf("<span>");
					if (index != -1) {
						String state = lihtml.substring(0, index).trim();
						game.setState(state);
						lihtml = lihtml.substring(index + 1).replace("<span>", "").replace("</span>", "");
						String zip = lihtml.trim();
						if (zip != null && zip.length() > 0) {
							game.setZipcode(Integer.parseInt(zip));
						}
					}
				}
			}
			
			// odds-details
			final Elements odds = gameinfo.select(".odds-details li");
			int y = 0;
			for (Element odd : odds) {
				if (y++ == 0) {
					// Line: USF -21.0
					String line = odd.html().replace("Line: ", "").trim();
					int index = line.indexOf(" ");
					if (index != -1) {
						String lineteam = line.substring(0, index).trim();
						String lineinfo = line.substring(index + 1).trim();
						game.setLinefavorite(lineteam);
						game.setLine(Float.parseFloat(lineinfo));
					}
				} else {
					// Over/Under: 70
					String total = odd.html().replace("Over/Under: ", "").trim();
					game.setTotal(Float.parseFloat(total));
				}
			}

			// Attendance: 13,377
			final Elements gameinfos = gameinfo.select(".game-info-note");
			if (gameinfos != null && gameinfos.size() > 0) {
				game.setAttendance(Integer.parseInt(gameinfos.get(0).html().replace(",", "").trim()));
			}
		}

		LOGGER.info("Exiting parseNcaafGame()");
		return game;
	}

	/**
	 * 
	 * @param date
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<EspnFootballDrive> parseNcaafDrives(String date, String xhtml) throws BatchException {
		LOGGER.info("Entering parseNcaafDrives()");
		final List<EspnFootballDrive> ncaafdrives = new ArrayList<EspnFootballDrive>();
		String awayteam = "";
		String hometeam = "";

		final Document doc = parseXhtml(xhtml);
		final Elements drives = doc.select(".accordion-item");
		for (Element drive : drives) {
			EspnFootballDrive efd = new EspnFootballDrive();
			String currentTeam = null;
			Elements team = drive.select("div a div span img");
			if (team != null && team.size() > 0) {
				Element img = team.get(0);
				String src = img.attr("src");
				int index = src.indexOf("ncaa/500/");
				if (index != -1) {
					src = src.substring(index + "ncaa/500/".length());
					index = src.indexOf(".png");
					if (index != -1) {
						currentTeam = src.substring(0, index);
					}
				}
			}

			// What was the result of the drive?
			final Elements spandrives = drive.select(".drives");
			for (Element spandrive : spandrives) {
				final Elements headlines = spandrive.select(".headline");
				if (headlines != null && headlines.size() > 0) {
					final Element hl = headlines.get(0);
					String headline = hl.html();
					if (headline != null && headline.length() > 0) {
						if (headline.equals("Touchdown")) {
							efd.setActiontype(EspnFootballDrive.TOUCHDOWN);
							efd.setDidscore(true);
						} else if (headline.equals("Field Goal")) {
							efd.setActiontype(EspnFootballDrive.FIELD_GOAL);
							efd.setDidscore(true);
						} else if (headline.equals("Interception Touchdown")) {
							efd.setActiontype(EspnFootballDrive.INTERCEPTION_RETURN_TOUCHDOWN);
							efd.setDidscore(true);
						} else if (headline.equals("Fumble Return Touchdown")) {
							efd.setActiontype(EspnFootballDrive.FUMBLE_RETURN_TOUCHDOWN);
							efd.setDidscore(true);
						} else if (headline.equals("Punt")) {
							efd.setActiontype(EspnFootballDrive.PUNT);
						} else if (headline.equals("Downs")) {
							efd.setActiontype(EspnFootballDrive.DOWNS);
						} else if (headline.equals("Fumble")) {
							efd.setActiontype(EspnFootballDrive.FUMBLE);
						} else if (headline.equals("Interception")) {
							efd.setActiontype(EspnFootballDrive.INTERCEPTION);
						} else if (headline.equals("Missed FG")) {
							efd.setActiontype(EspnFootballDrive.MISSED_FIELD_GOAL);
						} else if (headline.equals("End of Half")) {
							efd.setActiontype(EspnFootballDrive.END_OF_HALF);
						} else if (headline.equals("End of Game")) {
							efd.setActiontype(EspnFootballDrive.END_OF_GAME);
						} else {
							LOGGER.error("Missing action type!");
						}
					} else {
						efd.setActiontype(EspnFootballDrive.KICK_OFF);
					}
				}

				// Get the drive details
				final Elements drivedetails = spandrive.select(".drive-details");
				if (drivedetails != null && drivedetails.size() > 0) {
					final Element dd = drivedetails.get(0);
					String drivedetail = dd.html();
					if (drivedetail != null && drivedetail.length() > 0) {
						int index = drivedetail.indexOf(" plays,");
						if (index != -1) {
							String plays = drivedetail.substring(0, index).trim();
							efd.setPlays(Integer.parseInt(plays));
							drivedetail = drivedetail.substring(index + " plays,".length());
						} else {
							index = drivedetail.indexOf(" play,");
							if (index != -1) {
								String plays = drivedetail.substring(0, index).trim();
								efd.setPlays(Integer.parseInt(plays));
								drivedetail = drivedetail.substring(index + " play,".length());
							}
						}
						
						index = drivedetail.indexOf(" yards,");
						if (index != -1) {
							String yards = drivedetail.substring(0, index).trim();
							efd.setYards(Integer.parseInt(yards));
							drivedetail = drivedetail.substring(index + " yards,".length());
						} else {
							index = drivedetail.indexOf(" yard,");
							if (index != -1) {
								String yards = drivedetail.substring(0, index).trim();
								efd.setYards(Integer.parseInt(yards));
								drivedetail = drivedetail.substring(index + " yard,".length());
							}
						}

						index = drivedetail.indexOf(":");
						if (index != -1) {
							String minutes = drivedetail.substring(0, index).trim();
							String seconds = drivedetail.substring(index + 1).trim();
							efd.setMinutes(Integer.parseInt(minutes));
							efd.setSeconds(Integer.parseInt(seconds));
						}
					}
				}
			}

			// Score after drive finished
			final Elements homes = drive.select(".home");
			if (homes != null && homes.size() > 0) {
				final Element home = homes.get(0);
				final Elements teamnames = home.select(".team-name");
				if (teamnames != null && teamnames.size() > 0) {
					final Element teamname = teamnames.get(0);
					hometeam = teamname.html();
					efd.setHometeam(hometeam);
				}
				final Elements teamscores = home.select(".team-score");
				if (teamscores != null && teamscores.size() > 0) {
					final Element teamscore = teamscores.get(0);
					String homescore = teamscore.html();
					efd.setHomescore(Integer.parseInt(homescore));
				}				
			}

			final Elements aways = drive.select(".away");
			if (aways != null && aways.size() > 0) {
				final Element away = aways.get(0);
				final Elements teamnames = away.select(".team-name");
				if (teamnames != null && teamnames.size() > 0) {
					final Element teamname = teamnames.get(0);
					awayteam = teamname.html();
				}
				final Elements teamscores = away.select(".team-score");
				if (teamscores != null && teamscores.size() > 0) {
					final Element teamscore = teamscores.get(0);
					String awayscore = teamscore.html();
					efd.setAwayscore(Integer.parseInt(awayscore));
				}				
			}

			final Elements lis = drive.select(".drive-list li");
			for (Element li : lis) {
				final EspnFootballPlayByPlay playByPlay = new EspnFootballPlayByPlay();
				final Elements h3s = li.select("h3");

				if (h3s != null && h3s.size() > 0) {
					final Element h3 = h3s.get(0);
					String h3data = h3.html().replace("1st", "1").replace("2nd", "2").replace("3rd", "3").replace("4th", "4");
					
					// 1st and 10 at OKST 26
					int index = h3data.indexOf(" and ");
					if (index != -1) {
						String down = h3data.substring(0, index);
						playByPlay.setDown(Integer.parseInt(down));
						h3data = h3data.substring(index + 5);
						index = h3data.indexOf(" at ");
						if (index != -1) {
							String distance = h3data.substring(0, index);
							boolean goal = false;
							if (distance.equals("Goal")) {
								goal = true;
							} else {
								playByPlay.setDistance(Integer.parseInt(distance));
							}
							h3data = h3data.substring(index + 4);
							index = h3data.indexOf(" ");
							if (index != -1) {
								String teamside = h3data.substring(0, index);
								playByPlay.setTeamside(teamside);
								h3data = h3data.substring(index + 1);
								String yardline = h3data;
								playByPlay.setYardline(Integer.parseInt(yardline));
								if (goal) {
									playByPlay.setDistance(Integer.parseInt(yardline));
								}
							}							
						}
					}
				}

				final Elements spans = li.select("p span");
				if (spans != null && spans.size() > 0) {
					final Element span = spans.get(0);
					String spandata = span.html();

					// (14:50 - 1st) Justice Hill run for no gain to the OKSt 26
					int bindex = spandata.indexOf("(");
					int eindex = spandata.indexOf(")");
					if (bindex != -1 && eindex != -1) {
						final String tdata = spandata.substring(bindex, eindex);
						int index = tdata.indexOf(" - ");
						if (index != -1) {
							final String time = tdata.substring(0 , index).trim();
							int tindex = time.indexOf(":");
							if (tindex != -1) {
								final String minute = time.substring(0, tindex).replace("(", "").trim();
								final String second = time.substring(tindex + 1).trim();

								playByPlay.setMinute(Integer.parseInt(minute));
								playByPlay.setSecond(Integer.parseInt(second));
							}

							String quarter = tdata.substring(index + 3).trim();
							quarter = quarter.replace("1st", "1");
							quarter = quarter.replace("2nd", "2");
							quarter = quarter.replace("3rd", "3");
							quarter = quarter.replace("4th", "4");

							playByPlay.setQuarter(Integer.parseInt(quarter));
						}

						// Justice Hill run for no gain to the OKSt 26
						spandata = spandata.substring(eindex + 1).trim();
						if (spandata.contains("kickoff for")) {
							// P.J. Rosowski kickoff for 65 yds for a touchback
							// Jet Toner kickoff for 63 yds , Nahshon Ellerbe return for 18 yds to the Rice 20
							playByPlay.setPlaytype(EspnFootballPlayByPlay.KICK_OFF);
							int pindex = spandata.indexOf("kickoff for");
							if (pindex != -1) {
								String name = spandata.substring(0, pindex).trim();
								playByPlay.setPlayername(name);
								spandata = spandata.substring(pindex + "kickoff for".length());
								pindex = spandata.indexOf("yds for a touchback");
								if (pindex != -1) {
									String kickoffyards = spandata.substring(0, pindex).trim();
									playByPlay.setPlayeryards(Integer.parseInt(kickoffyards));
								} else {
									spandata = spandata.replace("yds ,", "yds,");
									pindex = spandata.indexOf("yds, ");
									if (pindex != -1) {
										String kickoffyards = spandata.substring(0, pindex).trim();
										playByPlay.setPlayeryards(Integer.parseInt(kickoffyards));
										spandata = spandata.substring(pindex + "yds, ".length());
										spandata = spandata.replace("returns for", "return for");
										pindex = spandata.indexOf(" return for ");
										if (pindex != -1) {
											String returnname = spandata.substring(0, pindex);
											playByPlay.setReturnname(returnname);
											spandata = spandata.substring(pindex + " return for ".length());
											pindex = spandata.indexOf("yds to the");
											if (pindex != -1) {
												String returnyards = spandata.substring(0, pindex).trim();
												playByPlay.setReturnyards(Integer.parseInt(returnyards));
											}
										}
									}
								}
							}
						} else if (spandata.contains("Yd Field Goal") || spandata.contains("yd FG GOOD")) {
							// Jet Toner 34 yd FG GOOD
							playByPlay.setPlaytype(EspnFootballPlayByPlay.MADE_FG);
							int pindex = spandata.indexOf("yd FG GOOD");
							if (pindex != -1) {
								spandata = spandata.substring(0, pindex).trim();
								pindex = spandata.lastIndexOf(" ");
								if (pindex != -1) {
									String fgname = spandata.substring(0, pindex);
									String fgyards = spandata.substring(pindex + 1);
									playByPlay.setPlayername(fgname);
									playByPlay.setPlayeryards(Integer.parseInt(fgyards));
								}
							} else {
								// Matt Ammendola 24 Yd Field Goal
								pindex = spandata.indexOf("Yd Field Goal");
								if (pindex != -1) {
									spandata = spandata.substring(0, pindex).trim();
									pindex = spandata.lastIndexOf(" ");
									if (pindex != -1) {
										String fgname = spandata.substring(0, pindex);
										String fgyards = spandata.substring(pindex + 1);
										playByPlay.setPlayername(fgname);
										playByPlay.setPlayeryards(Integer.parseInt(fgyards));
									}
								}
							}
						} else if (spandata.contains("Yd pass from")) {
							// James Washington 77 Yd pass from Mason Rudolph (Matt Ammendola Kick)
							playByPlay.setPlaytype(EspnFootballPlayByPlay.TD_PASS);
							int pindex = spandata.indexOf("Yd pass from");
							if (pindex != -1) {
								String before = spandata.substring(0, pindex).trim();
								int sindex = before.lastIndexOf(" ");
								if (sindex != -1) {
									String rname = before.substring(0, sindex);
									String rtyards = before.substring(sindex + 1);
									playByPlay.setPlayername(rname);
									playByPlay.setPlayeryards(Integer.parseInt(rtyards));
									playByPlay.setPasseryards(Integer.parseInt(rtyards));
								}

								String after = spandata.substring(pindex + "Yd pass from".length()).trim();
								sindex = after.indexOf("(");
								if (sindex != -1) {
									String thrower = after.substring(0, sindex).trim();
									playByPlay.setPassername(thrower);
									String extrapoint = after.substring(sindex + 1).replace(")", "");
								}
							}
						} else if (spandata.contains("pass intercepted for a TD")) {
							playByPlay.setPlaytype(EspnFootballPlayByPlay.INTERCEPTION_TD);
						} else if (spandata.contains("run for")) {
							// Justice Hill run for 1 yd to the Tulsa 44
							// Justice Hill run for no gain to the OKSt 26
							// Justice Hill run for 7 yds to the OKSt 46
							// Justice Hill run for 13 yds to the Tulsa 39 for a 1ST down
							// James Washington run for a loss of 1 yard to the OKSt 48
							// Chad President run for 11 yds to the OKSt 12 for a 1ST down
							// Mason Rudolph run for a loss of 1 yard to the OKSt 50 Mason Rudolph fumbled, forced by Kolton Shindelar, recovered by Tulsa Petera Wilson Jr. , return for 26 yds to the OKSt 24 Petera Wilson Jr. fumbled, forced by Dillon Stoner, recovered by Tulsa
							playByPlay.setPlaytype(EspnFootballPlayByPlay.RUN);
							int pindex = spandata.indexOf("run for");
							if (pindex != -1) {
								String rname = spandata.substring(0, pindex);
								playByPlay.setPlayername(rname);
								spandata = spandata.substring(pindex + "run for".length()).trim();
								pindex = spandata.indexOf("to the");
								if (pindex != -1) {
									String runyards = spandata.substring(0, pindex);
									runyards = runyards.replace("a loss of ", "-");
									runyards = runyards.replace("yds", "").replace("yards", "").replace("yd", "").replace("yard", "");
									runyards = runyards.replace("no gain", "0").trim();
									playByPlay.setPlayeryards(Integer.parseInt(runyards));
									spandata = spandata.substring(pindex + "to the".length()).trim();

									if (spandata.contains("yard line")) {
										playByPlay.setTeamside("");
										pindex = spandata.indexOf(" ");
										if (pindex != -1) {
											String yardline = spandata.substring(0, pindex).trim();
											playByPlay.setYardline(Integer.parseInt(yardline));
											spandata = spandata.substring(pindex + 1);
											if (spandata.contains("for a 1ST down")) {
												playByPlay.setIsfirstdown(true);
											}
										}
									} else {
										pindex = spandata.indexOf(" ");
										if (pindex != -1) {
											// TODO
											String teamname = spandata.substring(0, pindex);
											playByPlay.setTeamside(teamname);
											String yardline = spandata.substring(pindex + 1);
											if (yardline.contains("for a 1ST down")) {
												playByPlay.setIsfirstdown(true);
												pindex = yardline.indexOf("for a 1ST down");
												if (pindex != -1) {
													yardline = yardline.substring(0, pindex);
													yardline = yardline.replace("yard line ", "").trim();
													playByPlay.setYardline(Integer.parseInt(yardline));
												}
											} else {
												yardline = yardline.replace("yard line ", "").trim();
												if (yardline.length() > 3) {
													// TODO
												} else {
													playByPlay.setYardline(Integer.parseInt(yardline));
												}
											}
										}
									}
								}
							}
						} else if (spandata.contains("pass complete to")) {
							// Mason Rudolph pass complete to James Washington for 5 yds to the Tulsa 45
							// Mason Rudolph pass complete to Dillon Stoner for 4 yds to the 50 yard line for a 1ST down
							// Mason Rudolph pass complete to Justice Hill for a loss of 4 yards to the OKSt 44
							// Mason Rudolph pass complete to Chris Lacy for 13 yds to the OKSt 39 for a 1ST down
							playByPlay.setPlaytype(EspnFootballPlayByPlay.COMPLETED_PASS);
							int pindex = spandata.indexOf("pass complete to");
							if (pindex != -1) {
								String rname = spandata.substring(0, pindex);
								playByPlay.setPassername(rname);
								spandata = spandata.substring(pindex + "pass complete to".length()).trim();
								int findex = spandata.indexOf(" for ");
								if (findex != -1) {
									String recname = spandata.substring(0, findex).trim();
									playByPlay.setPlayername(recname);
									spandata = spandata.substring(findex + " for ".length());
									pindex = spandata.indexOf("to the");
									if (pindex != -1) {
										String ryards = spandata.substring(0, pindex);
										ryards = ryards.replace("a loss of ", "-");
										ryards = ryards.replace("yds", "").replace("yards", "").replace("yd", "").replace("yard", "");
										ryards = ryards.replace("no gain", "0").trim();
										LOGGER.error("ryards: " + ryards);
										playByPlay.setPlayeryards(Integer.parseInt(ryards));
										playByPlay.setPasseryards(Integer.parseInt(ryards));
										spandata = spandata.substring(pindex + "to the".length()).trim();

										if (spandata.contains("yard line")) {
											playByPlay.setTeamside("");
											pindex = spandata.indexOf(" ");
											if (pindex != -1) {
												String yardline = spandata.substring(0, pindex).trim();
												playByPlay.setYardline(Integer.parseInt(yardline));
												spandata = spandata.substring(pindex + 1);
												if (spandata.contains("for a 1ST down")) {
													playByPlay.setIsfirstdown(true);
												}
											}
										} else {
											pindex = spandata.indexOf(" ");
											if (pindex != -1) {
												String teamname = spandata.substring(0, pindex);
												LOGGER.error("teamname: " + teamname);
												playByPlay.setTeamside(teamname);
												String yardline = spandata.substring(pindex + 1);
												if (yardline.contains("for a 1ST down")) {
													playByPlay.setIsfirstdown(true);
													pindex = yardline.indexOf("for a 1ST down");
													if (pindex != -1) {
														LOGGER.error("yardline: " + yardline);
														yardline = yardline.substring(0, pindex).replace("yard line ", "").trim();
														playByPlay.setYardline(Integer.parseInt(yardline));
													}
												} else {
													yardline = yardline.replace("yard line ", "");
													playByPlay.setYardline(Integer.parseInt(yardline));											
												}
											}
										}
									}
								}
							}
						} else if (spandata.contains("pass incomplete to") || spandata.contains("pass incomplete")) {
							// Luke Skipper pass incomplete to Keenen Johnson
							// Chad President pass incomplete
							playByPlay.setPlaytype(EspnFootballPlayByPlay.INCOMPLETED_PASS);
							int pindex = spandata.indexOf("pass incomplete to");
							if (pindex != -1) {
								String qbname = spandata.substring(0, pindex).trim();
								playByPlay.setPassername(qbname);
								spandata = spandata.substring(pindex + "pass incomplete to".length()).trim();
								playByPlay.setPlayername(spandata);
							} else {
								pindex = spandata.indexOf("pass incomplete");
								if (pindex != -1) {
									String qbname = spandata.substring(0, pindex).trim();
									playByPlay.setPassername(qbname);
									playByPlay.setPlayername("");
								}
							}
						} else if (spandata.contains("pass from")) {
							// James Washington 40 Yd pass from Mason Rudolph (Matt Ammendola Kick)
							playByPlay.setPlaytype(EspnFootballPlayByPlay.TD_PASS);
							int pindex = spandata.indexOf("pass from");
							if (pindex != -1) {
								String before = spandata.substring(0, pindex).replace("Yd", "").trim();
								int sindex = before.lastIndexOf(" ");
								if (sindex != -1) {
									String recname = before.substring(0, sindex);
									String recyards = before.substring(sindex + 1);
									playByPlay.setPlayername(recname);
									playByPlay.setPlayeryards(Integer.parseInt(recyards));
									playByPlay.setPasseryards(Integer.parseInt(recyards));
								}
								spandata = spandata.substring(pindex + "pass from".length()).trim();
								pindex = spandata.indexOf("(");
								if (pindex != -1) {
									String qbname = spandata.substring(0, pindex).trim();
									playByPlay.setPassername(qbname);
									String extrapoint = spandata.substring(sindex + 1).replace(")", "");
								}
							}
						} else if (spandata.contains("punt for")) {
							// Thomas Bennett punt for 37 yds, punt out-of-bounds at the OKSt 48
							// Thomas Bennett punt for 49 yds , Jalen McCleskey returns for 11 yds to the OKSt 26
							// Thomas Bennett punt for 46 yds, fair catch by Jalen McCleskey at the OKSt 23
							// Zach Sinor punt for 37 yds, fair catch by Nigel Carter at the Tulsa 11
							playByPlay.setPlaytype(EspnFootballPlayByPlay.PUNT);
							int pindex = spandata.indexOf("punt for");
							if (pindex != -1) {
								String name = spandata.substring(0, pindex).trim();
								playByPlay.setPlayername(name);
								spandata = spandata.substring(pindex + "punt for".length());
								pindex = spandata.indexOf("yds for a touchback");
								if (pindex != -1) {
									String kickoffyards = spandata.substring(0, pindex).trim();
									playByPlay.setPlayeryards(Integer.parseInt(kickoffyards));
								} else {
									spandata = spandata.replace("yds ,", "yds,");
									pindex = spandata.indexOf("yds, ");
									if (pindex != -1) {
										String kickoffyards = spandata.substring(0, pindex).trim();
										playByPlay.setPlayeryards(Integer.parseInt(kickoffyards));
										spandata = spandata.substring(pindex + "yds, ".length());
										spandata = spandata.replace("returns for", "return for");
										pindex = spandata.indexOf(" return for ");
										if (pindex != -1) {
											String returnname = spandata.substring(0, pindex);
											playByPlay.setReturnname(returnname);
											spandata = spandata.substring(pindex + " return for ".length());
											pindex = spandata.indexOf("yds to the");
											if (pindex != -1) {
												String returnyards = spandata.substring(0, pindex).trim();
												playByPlay.setReturnyards(Integer.parseInt(returnyards));
											}
										}
									}
								}
							}
						} else if (spandata.contains("Yd Run (")) {
							// Justice Hill 3 Yd Run (Matt Ammendola Kick)
							playByPlay.setPlaytype(EspnFootballPlayByPlay.TD_RUN);
							int pindex = spandata.indexOf("Yd Run (");
							if (pindex != -1) {
								String tempdata = spandata.substring(0, pindex).trim();
								int sindex = tempdata.lastIndexOf(" ");
								if (sindex != -1) {
									String rname = tempdata.substring(0, sindex);
									String yards = tempdata.substring(sindex + 1);
									playByPlay.setPlayername(rname);
									playByPlay.setPlayeryards(Integer.parseInt(yards));
								}

								String extrapoint = spandata.substring(pindex + "Yd Run (".length()).replace(")", "");
							}
						} else if (spandata.contains("Timeout")) {
							// Timeout OKLAHOMA ST, clock 01:28
							// Timeout TULSA, clock 01:22
							playByPlay.setPlaytype(EspnFootballPlayByPlay.TIMEOUT);
							spandata = spandata.replace("Timeout", "").trim();
							int pindex = spandata.indexOf(", ");
							if (pindex != -1) {
								String tname = spandata.substring(0, pindex);
								playByPlay.setTeamside(tname);
								spandata = spandata.substring(pindex + 2).replace("clock", "").trim();

								pindex = spandata.indexOf(":");
								if (pindex != -1) {
									String minute = spandata.substring(0, pindex);
									String second = spandata.substring(pindex + 1);
									playByPlay.setMinute(Integer.parseInt(minute));
									playByPlay.setSecond(Integer.parseInt(second));
								}
							}
						} else if (spandata.contains("Penalty,")) {
							// TULSA Penalty, Defensive Pass Interference (Reggie Robinson II) to the Tulsa 49 for a 1ST down
							// OKLAHOMA ST Penalty, Defensive Pass Interference (Madre Harper) to the Tulsa 26 for a 1ST down
							playByPlay.setPlaytype(EspnFootballPlayByPlay.PENALTY);
							int pindex = spandata.indexOf("Penalty,");
							if (pindex != -1) {
								String tname = spandata.substring(0, pindex);
								playByPlay.setTeamside(tname);
								spandata = spandata.substring(pindex + "Penalty,".length());
							}
						} else if (spandata.contains("Fumble Return")) {
							// Adam Higuera 0 Yd Fumble Return (Redford Jones Kick)
							playByPlay.setPlaytype(EspnFootballPlayByPlay.FUMBLE_TD);
							int pindex = spandata.indexOf("Fumble Return");
							if (pindex != -1) {
								String before = spandata.substring(0, pindex).replace("Yd", "").trim();
								int sindex = before.lastIndexOf(" ");
								if (sindex != -1) {
									String fname = before.substring(0, sindex);
									String yards = before.substring(sindex + 1);
									playByPlay.setPlayername(fname);
									playByPlay.setPlayeryards(Integer.parseInt(yards));
								}
								spandata = spandata.substring(pindex + "Fumble Return".length()).trim();
								String extrapoint = spandata.replace("(", "").replace(")", "");
							}
						} else if (spandata.contains("pass intercepted")) {
							// Sam Glaesmann pass intercepted Jovan Swann return for 2 yds to the Rice 45
							playByPlay.setPlaytype(EspnFootballPlayByPlay.INTERCEPTION_PASS);
							int pindex = spandata.indexOf("pass intercepted");
							if (pindex != -1) {
								String qbname = spandata.substring(0, pindex);
								playByPlay.setPassername(qbname);
								spandata = spandata.substring(pindex + "pass intercepted".length());
								pindex = spandata.indexOf("return for");
								if (pindex != -1) {
									String iname = spandata.substring(0, pindex);
									playByPlay.setPlayername(iname);
									spandata = spandata.substring(pindex + "return for".length()).trim();
									int yindex = spandata.indexOf("yds to the");
									if (yindex != -1) {
										String yards = spandata.substring(0, yindex);
										playByPlay.setPlayeryards(Integer.parseInt(yards));
									}
								}
							}
						} else {
							// Chad President sacked by Kenneth Edison-McGruder for a loss of 11 yards to the Tulsa 33 TULSA Penalty, Intentional Grounding (Chad President) to the Tulsa 33
							// Mason Rudolph run for a loss of 1 yard to the OKSt 50 Mason Rudolph fumbled, forced by Kolton Shindelar, recovered by Tulsa Petera Wilson Jr. , return for 26 yds to the OKSt 24 Petera Wilson Jr. fumbled, forced by Dillon Stoner, recovered by Tulsa
							// Shamari Brooks run for no gain to the Tulsa 20 Shamari Brooks fumbled, forced by Trey Carter, recovered by OKSt Jerel Morrow
						}
					}
				}

				efd.addPlayByPlays(playByPlay);
			}
		
			ncaafdrives.add(efd);
		}

		LOGGER.info("Entering parseNcaafDrives()");
		return ncaafdrives;
	}

	/**
	 * 
	 * @param gameid
	 * @param date
	 * @param html
	 * @return
	 * @throws BatchException
	 */
	public List<EspnBasketballPlayByPlay> parseWnbaPlayByPlay(Integer gameid, String date, String html) throws BatchException {
		LOGGER.info("Entering parseWnbaPlayByPlay()");
		final List<EspnBasketballPlayByPlay> playByPlays = new ArrayList<EspnBasketballPlayByPlay>();
		String awayteam = "";
		String hometeam = "";

		// Parse the html
		final Document doc = parseXhtml(html);
		final Elements awayspans = doc.select(".away div div div div a span");
		if (awayspans != null && awayspans.size() > 2) {
			final Element span0 = awayspans.get(0);
			final Element span1 = awayspans.get(1);
			awayteam = span0.html() + " " + span1.html();
		}
		final Elements homespans = doc.select(".home div div div div a span");
		if (homespans != null && homespans.size() > 2) {
			final Element span0 = homespans.get(0);
			final Element span1 = homespans.get(1);
			hometeam = span0.html() + " " + span1.html();
		}
		final Elements quarters = doc.select(".accordion-content");
		if (quarters != null && quarters.size() > 0) {
			int quarternum = 1;
			for (Element quarter : quarters) {
				final Elements plays = quarter.select("table tbody tr");

				if (plays != null && plays.size() > 0) {
					for (Element play : plays) {
						final EspnBasketballPlayByPlay playByPlay = new EspnBasketballPlayByPlay();
						final EspnBasketballPlayByPlay playByPlay2 = new EspnBasketballPlayByPlay();
						playByPlay.setGameid(gameid);
						playByPlay2.setGameid(gameid);
						playByPlay.setGamedate(date);
						playByPlay2.setGamedate(date);
						playByPlay.setAwayteam(awayteam);
						playByPlay2.setAwayteam(awayteam);
						playByPlay.setHometeam(hometeam);
						playByPlay2.setHometeam(hometeam);
						playByPlay.setQuarter(quarternum);
						playByPlay2.setQuarter(quarternum);

						final Elements tds = play.select("td");
	
						if (tds != null && tds.size() > 0) {
							for (int y = 0; y < tds.size(); y++) {
								final Element td = tds.get(y);

								switch (y) {
									case 0:
										final String time = td.html();
										LOGGER.debug("time: " + time);
										parseTime(playByPlay, time);
										parseTime(playByPlay2, time);
										break;
									case 2:
										String detail = td.html();
										LOGGER.debug("detail: " + detail);

										if (detail != null && detail.length() > 0) {
											checkPlayType(playByPlay, playByPlay2, detail);
										}
										break;
									case 3:
										final String score = td.html();
										LOGGER.debug("score: " + score);
										parseScore(playByPlay, score);
										parseScore(playByPlay2, score);
										break;
									case 1:
									case 4:
									default:
										break;
								}
							}
						}

						if (playByPlay.getActiontype() != null) {
							playByPlays.add(playByPlay);
						}

						if (playByPlay2.getActiontype() != null) {
							playByPlays.add(playByPlay2);
						}
					}

					quarternum++;
				}
			}
		}

		LOGGER.info("Exiting parseWnbaPlayByPlay()");
		return playByPlays;
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 * @return
	 */
	private void checkPlayType(EspnBasketballPlayByPlay playByPlay, EspnBasketballPlayByPlay playByPlay2, String detail) {
		LOGGER.info("Entering checkPlayType()");
		// 0 - gains possesion
		// 1 - block
		// 2 - layup
		// 3 - 3pt shot

		// 1 - bad pass
		// 2 - steal
		// 3 - misses driving layup
		// 4 - defensive rebound
		// 5 - offensive rebound
		// 6 - misses two point shot
		// 7 - make two point shot
		// 8 - assist
		// 9 - block
		// 10 - foul
		// 11 - enters game
		// 12 - free throw
		// 13 - lost ball
		if (detail != null) {
			if (detail.contains("gains possesion")) {
				gainsPossession(playByPlay, detail);
			} else if (detail.contains("block")) {
				block(playByPlay, playByPlay2, detail);
			} else if (detail.contains("defensive team rebound")) {
				defensiveTeamRebound(playByPlay, detail);
			} else if (detail.contains("defensive rebound")) {
				defensiveRebound(playByPlay, detail);
			} else if (detail.contains("makes")) {
				makes(playByPlay, playByPlay2, detail);
			} else if (detail.contains("misses")) {
				misses(playByPlay, detail);
			} else if (detail.contains("lost ball") || detail.contains("bad pass") || detail.contains("turnover") || detail.contains("traveling") || detail.contains("delay of game")) {
				turnover(playByPlay, playByPlay2, detail);
			} else if (detail.contains("shooting foul")) {
				shootingFoul(playByPlay, playByPlay2, detail);
			} else if (detail.contains("loose ball foul")) {
				foul(playByPlay, detail);
			} else if (detail.contains("offensive Charge") || detail.contains("offensive foul")) {
				offensiveFoul(playByPlay, detail);
			} else if (detail.contains("offensive rebound")) {
				offensiveRebound(playByPlay, detail);
			} else if (detail.contains("personal foul")) {
				foul(playByPlay, detail);
			} else if (detail.contains("offensive team rebound")) {
				offensiveTeamRebound(playByPlay, detail);
			} else {
				LOGGER.debug("Undefined detail: " + detail);
			}
		}

		LOGGER.info("Exiting checkPlayType()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param playByPlay2
	 * @param detail
	 */
	private void shootingFoul(EspnBasketballPlayByPlay playByPlay, EspnBasketballPlayByPlay playByPlay2, String detail) {
		LOGGER.info("Entering shootingFoul()");
		detail = detail.replace("(", "");
		detail = detail.replace(")", "");

		playByPlay.setActiontype(EspnBasketballPlayByPlay.SHOOTING_FOUL);
		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		int counter = 0;
		while (st1.hasMoreTokens()) {
			switch (counter) {
				case 0:
					parseFirstName(st1, playByPlay, detail);
					break;
				case 1:
					parseLastName(st1, playByPlay, detail);
					break;
				default:
					st1.nextToken();
					break;
			}
			counter++;
		}

		LOGGER.info("Exiting shootingFoul()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 */
	private void foul(EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering foul()");
		detail = detail.replace("(", "");
		detail = detail.replace(")", "");

		playByPlay.setActiontype(EspnBasketballPlayByPlay.FOUL);
		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		int counter = 0;
		while (st1.hasMoreTokens()) {
			switch (counter) {
				case 0:
					parseFirstName(st1, playByPlay, detail);
					break;
				case 1:
					parseLastName(st1, playByPlay, detail);
					break;
				default:
					st1.nextToken();
					break;
			}
			counter++;
		}

		LOGGER.info("Exiting foul()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 */
	private void offensiveFoul(EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering offensiveFoul()");
		detail = detail.replace("(", "");
		detail = detail.replace(")", "");

		playByPlay.setActiontype(EspnBasketballPlayByPlay.OFFENSIVE_FOUL);
		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		int counter = 0;
		while (st1.hasMoreTokens()) {
			switch (counter) {
				case 0:
					parseFirstName(st1, playByPlay, detail);
					break;
				case 1:
					parseLastName(st1, playByPlay, detail);
					break;
				default:
					st1.nextToken();
					break;
			}
			counter++;
		}

		LOGGER.info("Exiting offensiveFoul()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param playByPlay2
	 * @param detail
	 */
	private void turnover(EspnBasketballPlayByPlay playByPlay, EspnBasketballPlayByPlay playByPlay2, String detail) {
		LOGGER.info("Entering turnover()");
		detail = detail.replace("(", "");
		detail = detail.replace(")", "");

		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		int counter = 0;
		while (st1.hasMoreTokens()) {
			switch (counter) {
				case 0:
					parseFirstName(st1, playByPlay, detail);
					break;
				case 1:
					parseLastName(st1, playByPlay, detail);
					break;
				case 2:
					st1.nextToken();
					playByPlay.setActiontype(EspnBasketballPlayByPlay.TURNOVER);
					break;
				case 5:
					parseFirstName(st1, playByPlay2, detail);
					break;
				case 6:
					parseLastName(st1, playByPlay2, detail);
					break;
				case 7:
					st1.nextToken();
					playByPlay2.setActiontype(EspnBasketballPlayByPlay.STEAL);
					break;
				case 3:
				case 4:
				default:
					st1.nextToken();
					break;
			}
			counter++;
		}

		LOGGER.info("Exiting turnover()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param playByPlay2
	 * @param detail
	 */
	private void makes(EspnBasketballPlayByPlay playByPlay, EspnBasketballPlayByPlay playByPlay2, String detail) {
		LOGGER.info("Entering makes()");
		playByPlay.setDidscore(true);
		playByPlay.setScoringplay(true);

		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		int counter = 0;
		while (st1.hasMoreTokens()) {
			switch (counter) {
				case 0:
					parseFirstName(st1, playByPlay, detail);
					break;
				case 1:
					parseLastName(st1, playByPlay, detail);
					break;
				case 3:
					final String token = st1.nextToken();
					if (detail.contains("two point") || detail.contains("layup")) {
						playByPlay.setActiontype(EspnBasketballPlayByPlay.TWO_POINT);
					} else if (detail.contains("three point")) {
						playByPlay.setActiontype(EspnBasketballPlayByPlay.THREE_POINT);
					} else if (detail.contains("free throw")) {
						playByPlay.setActiontype(EspnBasketballPlayByPlay.FREE_THROW);
					}

					if (detail.contains("-foot")) {
						int index = token.indexOf("-foot");
						if (index != -1) {
							playByPlay.setShotdistance(Integer.parseInt(token.substring(0, index)));
						}
					}
					break;
				default:
					st1.nextToken();
					break;
			}

			counter++;
		}

		// Assists
		if (detail.contains("assist")) {
			int beginindex = detail.indexOf("(");
			if (beginindex != -1) {
				detail = detail.substring(beginindex + 1).replace(")", "").trim();
				final StringTokenizer st = new StringTokenizer(detail, " ");
				counter = 0;
				while (st.hasMoreTokens()) {
					switch (counter) {
						case 0:
							parseFirstName(st, playByPlay2, detail);
							break;
						case 1:
							parseLastName(st, playByPlay2, detail);
							playByPlay2.setActiontype(EspnBasketballPlayByPlay.ASSIST);
							playByPlay2.setDidscore(true);
							playByPlay2.setScoringplay(true);
							break;
						default:
							st.nextToken();
							break;
					}
				}
			}
		}

		LOGGER.info("Exiting makes()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 */
	private void misses(EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering misses()");
		playByPlay.setDidscore(false);
		playByPlay.setScoringplay(true);

		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		int counter = 0;
		while (st1.hasMoreTokens()) {
			switch (counter) {
				case 0:
					parseFirstName(st1, playByPlay, detail);
					break;
				case 1:
					parseLastName(st1, playByPlay, detail);
					break;
				case 3:
					if (detail.contains("two point") || detail.contains("layup")) {
						if (detail.contains("-foot")) {
							final String token = st1.nextToken();
							playByPlay.setActiontype(EspnBasketballPlayByPlay.TWO_POINT);
							int index = token.indexOf("-foot");
							if (index != -1) {
								playByPlay.setShotdistance(Integer.parseInt(token.substring(0, index)));
							}
						}
					} else if (detail.contains("three point")) {
						if (detail.contains("-foot")) {
							final String token = st1.nextToken();
							playByPlay.setActiontype(EspnBasketballPlayByPlay.THREE_POINT);
							int index = token.indexOf("-foot");
							if (index != -1) {
								playByPlay.setShotdistance(Integer.parseInt(token.substring(0, index)));
							}
						}
					} else if (detail.contains("jumper")) {
						if (detail.contains("-foot")) {
							final String token = st1.nextToken();
							playByPlay.setActiontype(EspnBasketballPlayByPlay.TWO_POINT);
							int index = token.indexOf("-foot");
							if (index != -1) {
								playByPlay.setShotdistance(Integer.parseInt(token.substring(0, index)));
							}
						}
					}

					break;
				default:
					st1.nextToken();
					break;
			}

			counter++;
		}

		LOGGER.info("Exiting misses()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 */
	private void defensiveTeamRebound(EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering defensiveTeamRebound()");

		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		if (st1.countTokens() == 4) {
			playByPlay.setActiontype(EspnBasketballPlayByPlay.TEAM_DEFENSIVE_REBOUND);
			int counter = 0;
			while (st1.hasMoreTokens()) {
				switch (counter) {
					case 0:
						parseFirstName(st1, playByPlay, detail);
						break;
					default:
						st1.nextToken();
						break;
				}
				counter++;
			}
		}

		LOGGER.info("Exiting defensiveTeamRebound()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 */
	private void offensiveTeamRebound(EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering offensiveTeamRebound()");

		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		if (st1.countTokens() == 4) {
			playByPlay.setActiontype(EspnBasketballPlayByPlay.TEAM_OFFENSIVE_REBOUND);
			int counter = 0;
			while (st1.hasMoreTokens()) {
				switch (counter) {
					case 0:
						parseFirstName(st1, playByPlay, detail);
						break;
					default:
						st1.nextToken();
						break;
				}
				counter++;
			}
		}

		LOGGER.info("Exiting offensiveTeamRebound()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 */
	private void defensiveRebound(EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering defensiveRebound()");

		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		if (st1.countTokens() == 4) {
			playByPlay.setActiontype(EspnBasketballPlayByPlay.DEFENSIVE_REBOUND);
			int counter = 0;
			while (st1.hasMoreTokens()) {
				switch (counter) {
					case 0:
						parseFirstName(st1, playByPlay, detail);
						break;
					case 1:
						parseLastName(st1, playByPlay, detail);
						break;
					default:
						st1.nextToken();
						break;
				}
				counter++;
			}
		}

		LOGGER.info("Exiting defensiveRebound()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 */
	private void offensiveRebound(EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering offensiveRebound()");

		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		if (st1.countTokens() == 4) {
			playByPlay.setActiontype(EspnBasketballPlayByPlay.OFFENSIVE_REBOUND);
			int counter = 0;
			while (st1.hasMoreTokens()) {
				switch (counter) {
					case 0:
						parseFirstName(st1, playByPlay, detail);
						break;
					case 1:
						parseLastName(st1, playByPlay, detail);
						break;
					default:
						st1.nextToken();
						break;
				}
				counter++;
			}
		}

		LOGGER.info("Exiting offensiveRebound()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param playByPlay2
	 * @param detail
	 */
	private void block(EspnBasketballPlayByPlay playByPlay, EspnBasketballPlayByPlay playByPlay2, String detail) {
		LOGGER.info("Entering block()");

		// Block
		playByPlay.setActiontype(EspnBasketballPlayByPlay.BLOCK);
		
		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		int counter = 0;
		while (st1.hasMoreTokens()) {
			switch (counter) {
				case 0:
					parseFirstName(st1, playByPlay, detail);
					break;
				case 1:
					parseLastName(st1, playByPlay, detail);
					break;
				case 2:
					st1.nextToken();
					if (detail.contains("layup")) {
						playByPlay2.setActiontype(EspnBasketballPlayByPlay.LAYUP);
					} else if (detail.contains("three point")) {
						playByPlay2.setActiontype(EspnBasketballPlayByPlay.THREE_POINT);
					}
					break;
				case 3:
					parseFirstName(st1, playByPlay2, detail);
					break;
				case 4:
					parseLastName(st1, playByPlay2, detail);
					break;
				default:
					st1.nextToken();
					break;
			}

			counter++;
		}

		LOGGER.info("Exiting block()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param detail
	 */
	private void gainsPossession(EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering gainsPossession()");

		final StringTokenizer st1 = new StringTokenizer(detail, " ");
		if (st1.countTokens() == 4) {
			playByPlay.setActiontype(EspnBasketballPlayByPlay.GAINS_POSSESION);
			int counter = 0;
			while (st1.hasMoreTokens()) {
				switch (counter) {
					case 0:
						parseFirstName(st1, playByPlay, detail);
						break;
					case 1:
						parseLastName(st1, playByPlay, detail);
						break;
					default:
						st1.nextToken();
						break;
				}
				counter++;
			}
		}

		LOGGER.info("Exiting gainsPossession()");
	}

	/**
	 * 
	 * @param st1
	 * @param playByPlay
	 * @param detail
	 */
	private void parseFirstName(StringTokenizer st1, EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering parseFirstName()");

		final String firstname = st1.nextToken().trim();
		LOGGER.debug("firstname: " + firstname);

		// Set the first name
		playByPlay.setFirstname(firstname);

		LOGGER.info("Exiting parseFirstName()");
	}

	/**
	 * 
	 * @param st1
	 * @param playByPlay
	 * @param detail
	 */
	private void parseLastName(StringTokenizer st1, EspnBasketballPlayByPlay playByPlay, String detail) {
		LOGGER.info("Entering parseLastName()");

		final String lastname = st1.nextToken().replace("'s", "").trim();
		LOGGER.debug("lastname: " + lastname);

		// Set the first name
		playByPlay.setLastname(lastname);

		LOGGER.info("Exiting parseLastName()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param xhtml
	 */
	private void parseTime(EspnBasketballPlayByPlay playByPlay, String xhtml) {
		LOGGER.info("Entering parseTime()");

		// 10:00
		int index = xhtml.indexOf(":");
		if (index != -1) {
			playByPlay.setMinute(Integer.parseInt(xhtml.substring(0, index).trim()));
			playByPlay.setSecond(Integer.parseInt(xhtml.substring(index + 1).trim()));
		}

		LOGGER.info("Exiting parseTime()");
	}

	/**
	 * 
	 * @param playByPlay
	 * @param xhtml
	 */
	private void parseScore(EspnBasketballPlayByPlay playByPlay, String xhtml) {
		LOGGER.info("Entering parseScore()");

		// 5 - 10
		int index = xhtml.indexOf(" - ");
		if (index != -1) {
			playByPlay.setAwayscore(Integer.parseInt(xhtml.substring(0, index).trim()));
			playByPlay.setHomescore(Integer.parseInt(xhtml.substring(index + 3).trim()));
		}

		LOGGER.info("Exiting parseScore()");
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<EspnBasketBallTeamData> parseBasketbBallTeams(String xhtml) throws BatchException {
		final List<EspnBasketBallTeamData> dataContainer = new ArrayList<EspnBasketBallTeamData>();
		final Document doc = parseXhtml(xhtml);

		final Elements lis = doc.select(".mod-content ul li");
		if (lis != null) {
			for (Element li : lis) {
				final EspnBasketBallTeamData espnBballTeamData = new EspnBasketBallTeamData();
				final Elements as = li.select("h5 a");
				if (as != null && as.size() > 0) {
					final Element a = as.get(0);
					espnBballTeamData.setName(a.html().trim());
				}

				final Elements spanas = li.select("span a");
				if (spanas != null && spanas.size() > 0) {
					final Element a = spanas.get(0);
					espnBballTeamData.setRosterUrl(a.attr("href"));
				}

				dataContainer.add(espnBballTeamData);
			}
		}

		return dataContainer;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<EspnBasketBallRosterData> parseBasketbBallTeamRoster(String xhtml) throws BatchException {
		final List<EspnBasketBallRosterData> dataContainer = new ArrayList<EspnBasketBallRosterData>();
		final Document doc = parseXhtml(xhtml);

		final Elements trs = doc.select(".mod-content table tbody tr");
		if (trs != null) {
			for (Element tr : trs) {
				String trclass = tr.attr("class");
				if (trclass != null && (trclass.contains("oddrow") || trclass.contains("evenrow"))) {
					final EspnBasketBallRosterData espnBasketBallRosterData = new EspnBasketBallRosterData();
					Elements tds = tr.select("td");
					if (tds != null && tds.size() > 0) {
						for (Element td : tds) {

						}
					}
				}
			}
		}

		return dataContainer;
	}

	/**
	 * 
	 * @param xhtml
	 */
	public List<EspnData> parseStrengthOfSchedule(String xhtml) throws BatchException {
		final List<EspnData> dataContainer = new ArrayList<EspnData>();
		final Document doc = parseXhtml(xhtml);

		final Elements trs = doc.select(".tr-table tbody tr");
		if (trs != null) {
			for (Element tr : trs) {
				final EspnData dc = new EspnData();
				final Elements tds = tr.select("td");
				int count = 0;
				for (Element td : tds) {
					switch (count) {
						case 0:
							dc.setField1(td.html().trim());
							break;
						case 1:
							Elements as = td.select("a");
							if (as != null && as.size() > 0) {
								String teamName = as.get(0).html().trim();
								dc.setField2(teamName.toUpperCase());

								Iterator<String> itr = NCAABMapping.keySet().iterator();
								while (itr.hasNext()) {
									String key = itr.next();
									if (key.toUpperCase().equals(teamName.toUpperCase())) {
										dc.setField2(NCAABMapping.get(key).toUpperCase());
									}
								}
							}
							break;
						case 2:
							dc.setField3(td.html().trim());
							break;
						case 3:
							dc.setField4(td.html().trim());
							break;
						case 4:
							dc.setField5(td.html().trim());
							break;
						case 5:
							dc.setField6(td.html().trim());
							break;
						default:
							break;
					}
					count++;
				}
				dataContainer.add(dc);
			}
		}
		
		return dataContainer;
	}

	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> parseLogin(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type)
			throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseTicketNumber(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected <T> List<T> getGameData(Elements elements) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}
/**
 * 
 */
package com.wooanalytics.dao.sites.kenpom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
public class KenPomParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(KenPomParser.class);
	private static final Map<String, String> NFLMapping = new HashMap<String, String>();
	private static final Map<String, String> MLBMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAABMapping = new HashMap<String, String>();
	// 2017-12-20
	private static final SimpleDateFormat GAME_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	// Fri Nov 10
	private static final SimpleDateFormat LOCATION_DATE_FORMAT = new SimpleDateFormat("E MMM dd");

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
		
		NCAABMapping.put("Abilene Christian", "Abilene Christian");
		NCAABMapping.put("Air Force", "Air Force");
		NCAABMapping.put("Akron","Akron");
		NCAABMapping.put("Alabama A&amp;M","Alabama A&M");
		NCAABMapping.put("Alabama","Alabama");
		NCAABMapping.put("Alabama St.","Alabama State");
		NCAABMapping.put("Albany","Albany");
		NCAABMapping.put("Alcorn St.","Alcorn State");
		NCAABMapping.put("American","American University");
		NCAABMapping.put("Appalachian St.","Appalachian State");
		NCAABMapping.put("Little Rock","Arkansas Little Rock");
		NCAABMapping.put("Arizona","Arizona");
		NCAABMapping.put("Arizona St.","Arizona State");
		NCAABMapping.put("Arkansas Pine Bluff","Arkansas Pine Bluff");
		NCAABMapping.put("Arkansas","Arkansas");
		NCAABMapping.put("Arkansas St.","Arkansas State");
		NCAABMapping.put("Army","Army");
		NCAABMapping.put("Auburn","Auburn");
		NCAABMapping.put("Austin Peay","Austin Peay");
		NCAABMapping.put("Ball St.","Ball State");
		NCAABMapping.put("Baylor","Baylor");
		NCAABMapping.put("Belmont","Belmont");
		NCAABMapping.put("Bethune Cookman","Bethune-Cookman");
		NCAABMapping.put("Binghamton","Binghamton");
		NCAABMapping.put("Boise St.","Boise State");
		NCAABMapping.put("Boston College","Boston College");
		NCAABMapping.put("Boston University","Boston University");
		NCAABMapping.put("Bowling Green","Bowling Green");
		NCAABMapping.put("Bradley","Bradley");
		NCAABMapping.put("Brown","Brown");
		NCAABMapping.put("Bryant","Bryant");
		NCAABMapping.put("Bucknell","Bucknell");
		NCAABMapping.put("Buffalo","Buffalo");
		NCAABMapping.put("Butler","Butler");
		NCAABMapping.put("BYU","BYU");
		NCAABMapping.put("Cal Poly","Cal Poly-SLO");
		NCAABMapping.put("Cal St. Northridge","CS Northridge");
		NCAABMapping.put("California","California");
		NCAABMapping.put("Campbell","Campbell");
		NCAABMapping.put("Canisius","Canisius");
		NCAABMapping.put("Central Arkansas","Central Arkansas");
		NCAABMapping.put("Central Connecticut","Central Connecticut State");
		NCAABMapping.put("UCF","Central Florida");
		NCAABMapping.put("Central Michigan","Central Michigan");
		NCAABMapping.put("Charleston Southern","Charleston Southern");
		NCAABMapping.put("Charlotte","Charlotte");
		NCAABMapping.put("Chattanooga","Chattanooga");
		NCAABMapping.put("Chicago St.","Chicago State");
		NCAABMapping.put("Cincinnati","Cincinnati");
		NCAABMapping.put("The Citadel","The Citadel");
		NCAABMapping.put("Clemson","Clemson");
		NCAABMapping.put("Cleveland St.","Cleveland State");
		NCAABMapping.put("Coastal Carolina","Coastal Carolina");
		NCAABMapping.put("College of Charleston","College of Charleston");
		NCAABMapping.put("Colgate","Colgate");
		NCAABMapping.put("Colorado","Colorado");
		NCAABMapping.put("Colorado St.","Colorado State");
		NCAABMapping.put("Columbia","Columbia");
		NCAABMapping.put("Connecticut","Connecticut");
		NCAABMapping.put("Coppin St.","Coppin State");
		NCAABMapping.put("Cornell","Cornell");
		NCAABMapping.put("Creighton","Creighton");
		NCAABMapping.put("Cal St. Bakersfield","CS Bakersfield");
		NCAABMapping.put("Cal St. Fullerton","CS Fullerton");
		NCAABMapping.put("Dartmouth","Dartmouth");
		NCAABMapping.put("Davidson","Davidson");
		NCAABMapping.put("Dayton","Dayton");
		NCAABMapping.put("Delaware","Delaware");
		NCAABMapping.put("Delaware St.","Delaware State");
		NCAABMapping.put("Denver","Denver");
		NCAABMapping.put("DePaul","DePaul");
		NCAABMapping.put("Detroit","Detroit Mercy");
		NCAABMapping.put("Drake","Drake");
		NCAABMapping.put("Drexel","Drexel");
		NCAABMapping.put("Duke","Duke");
		NCAABMapping.put("Duquesne","Duquesne");
		NCAABMapping.put("East Carolina","East Carolina");
		NCAABMapping.put("Eastern Illinois","Eastern Illinois");
		NCAABMapping.put("Eastern Kentucky","Eastern Kentucky");
		NCAABMapping.put("Eastern Michigan","Eastern Michigan");
		NCAABMapping.put("East Tennessee St.","East Tennessee State");
		NCAABMapping.put("Eastern Washington","Eastern Washington");
		NCAABMapping.put("Elon","Elon");
		NCAABMapping.put("Evansville","Evansville");
		NCAABMapping.put("Fairleigh Dickinson","Fairleigh Dickinson");
		NCAABMapping.put("Fairfield","Fairfield");
		NCAABMapping.put("Florida Atlantic","Florida Atlantic");
		NCAABMapping.put("Florida Gulf Coast","Florida Gulf Coast");
		NCAABMapping.put("Florida","Florida");
		NCAABMapping.put("Florida A&amp;M","Florida A&M");
		NCAABMapping.put("FIU","Florida International");
		NCAABMapping.put("Florida St.","Florida State");
		NCAABMapping.put("Fordham","Fordham");
		NCAABMapping.put("Fresno St.","Fresno State");
		NCAABMapping.put("Furman","Furman");
		NCAABMapping.put("Georgia Southern","Georgia Southern");
		NCAABMapping.put("Georgia Tech","Georgia Tech");
		NCAABMapping.put("Gardner Webb","Gardner-Webb");
		NCAABMapping.put("George Mason","George Mason");
		NCAABMapping.put("George Washington","George Washington");
		NCAABMapping.put("Georgetown","Georgetown");
		NCAABMapping.put("Georgia","Georgia");
		NCAABMapping.put("Georgia St.","Georgia State");
		NCAABMapping.put("Gonzaga","Gonzaga");
		NCAABMapping.put("Grambling St.","Grambling State");
		NCAABMapping.put("Grand Canyon","Grand Canyon");
		NCAABMapping.put("Hampton","Hampton");
		NCAABMapping.put("Hartford","Hartford");
		NCAABMapping.put("Harvard","Harvard");
		NCAABMapping.put("Hawaii","Hawaii");
		NCAABMapping.put("High Point","High Point");
		NCAABMapping.put("Hofstra","Hofstra");
		NCAABMapping.put("Holy Cross","Holy Cross");
		NCAABMapping.put("Houston","Houston");
		NCAABMapping.put("Houston Baptist","Houston Baptist");
		NCAABMapping.put("Howard","Howard");
		NCAABMapping.put("Idaho","Idaho");
		NCAABMapping.put("Idaho St.","Idaho State");
		NCAABMapping.put("Illinois Chicago","Illinois-Chicago");
		NCAABMapping.put("Illinois","Illinois");
		NCAABMapping.put("Illinois St.","Illinois State");
		NCAABMapping.put("Incarnate Word","Incarnate Word");
		NCAABMapping.put("Indiana","Indiana");
		NCAABMapping.put("Indiana St.","Indiana State");
		NCAABMapping.put("Iona","Iona");
		NCAABMapping.put("Iowa","Iowa");
		NCAABMapping.put("Iowa St.","Iowa State");
		NCAABMapping.put("Fort Wayne","Fort Wayne(IPFW)");
		NCAABMapping.put("IUPUI","IUPUI");
		NCAABMapping.put("Jackson St.","Jackson State");
		NCAABMapping.put("Jacksonville","Jacksonville");
		NCAABMapping.put("James Madison","James Madison");
		NCAABMapping.put("Jacksonville St.","Jacksonville State");
		NCAABMapping.put("Kansas","Kansas");
		NCAABMapping.put("Kansas St.","Kansas State");
		NCAABMapping.put("Kennesaw St.","Kennesaw State");
		NCAABMapping.put("Kent St.","Kent State");
		NCAABMapping.put("Kentucky","Kentucky");
		NCAABMapping.put("Lafayette","Lafayette");
		NCAABMapping.put("Louisiana Monroe","Louisiana-Monroe");
		NCAABMapping.put("La Salle","La Salle");
		NCAABMapping.put("Louisiana Tech","Louisiana Tech");
		NCAABMapping.put("Louisiana Lafayette","Louisiana Lafayette");
		NCAABMapping.put("Lamar","Lamar");
		NCAABMapping.put("Lehigh","Lehigh");
		NCAABMapping.put("Long Beach St.","Long Beach State");
		NCAABMapping.put("Liberty","Liberty");
		NCAABMapping.put("Lipscomb","Lipscomb");
		NCAABMapping.put("LIU Brooklyn","Long Island University");
		NCAABMapping.put("Longwood","Longwood");
		NCAABMapping.put("Louisville","Louisville");
		NCAABMapping.put("Loyola Marymount","Loyola Marymount");
		NCAABMapping.put("Loyola Chicago","Loyola-Chicago");
		NCAABMapping.put("Loyola MD","Loyola-Maryland");
		NCAABMapping.put("LSU","LSU");
		NCAABMapping.put("Maine","Maine");
		NCAABMapping.put("Manhattan","Manhattan");
		NCAABMapping.put("Marist","Marist");
		NCAABMapping.put("Marquette","Marquette");
		NCAABMapping.put("Marshall","Marshall");
		NCAABMapping.put("Maryland","Maryland");
		NCAABMapping.put("UMBC","Maryland BC");
		NCAABMapping.put("Maryland Eastern Shore","Maryland-Eastern Shore");
		NCAABMapping.put("UMass Lowell","Massachusetts Lowell");
		NCAABMapping.put("McNeese St.","McNeese State");
		NCAABMapping.put("Memphis","Memphis");
		NCAABMapping.put("Mercer","Mercer");
		NCAABMapping.put("Miami FL","Miami-Florida");
		NCAABMapping.put("Miami OH","Miami-Ohio");
		NCAABMapping.put("Michigan","Michigan");
		NCAABMapping.put("Michigan St.","Michigan State");
		NCAABMapping.put("Middle Tennessee","Middle Tennessee State");
		NCAABMapping.put("Minnesota","Minnesota");
		NCAABMapping.put("Mississippi St.","Mississippi State");
		NCAABMapping.put("Mississippi Valley St.","Mississippi Valley State");
		NCAABMapping.put("Mississippi","Mississippi");
		NCAABMapping.put("Missouri","Missouri");
		NCAABMapping.put("Missouri St.","Missouri State");
		NCAABMapping.put("Monmouth","Monmouth");
		NCAABMapping.put("Montana","Montana");
		NCAABMapping.put("Montana St.","Montana State");
		NCAABMapping.put("Morehead St.","Morehead State");
		NCAABMapping.put("Morgan St.","Morgan State");
		NCAABMapping.put("Mount St. Mary's","Mount St. Mary's");
		NCAABMapping.put("Murray St.","Murray State");
		NCAABMapping.put("North Alabama","North Alabama");
		NCAABMapping.put("Northern Arizona","Northern Arizona");
		NCAABMapping.put("North Carolina","North Carolina");
		NCAABMapping.put("Northern Colorado","Northern Colorado");
		NCAABMapping.put("North Dakota St.","North Dakota State");
		NCAABMapping.put("North Florida","North Florida");
		NCAABMapping.put("New Hampshire","New Hampshire");
		NCAABMapping.put("Northern Illinois","Northern Illinois");
		NCAABMapping.put("Northern Iowa","Northern Iowa");
		NCAABMapping.put("Northern Kentucky","Northern Kentucky");
		NCAABMapping.put("New Mexico St.","New Mexico State");
		NCAABMapping.put("Navy","Navy");
		NCAABMapping.put("North Carolina A&amp;T","NC A&T");
		NCAABMapping.put("North Carolina Central","NC Central");
		NCAABMapping.put("North Carolina St.","NC State");
		NCAABMapping.put("UNC Asheville","NC Asheville");
		NCAABMapping.put("UNC Greensboro","NC Greensboro");
		NCAABMapping.put("UNC Wilmington","NC Wilmington");
		NCAABMapping.put("Nebraska Omaha","Nebraska Omaha");
		NCAABMapping.put("Nebraska","Nebraska");
		NCAABMapping.put("Nevada","Nevada");
		NCAABMapping.put("New Mexico","New Mexico");
		NCAABMapping.put("New Orleans","New Orleans");
		NCAABMapping.put("Niagara","Niagara");
		NCAABMapping.put("Nicholls St.","Nicholls St");
		NCAABMapping.put("NJIT","NJIT");
		NCAABMapping.put("Norfolk St.","Norfolk State");
		NCAABMapping.put("North Dakota","North Dakota");
		NCAABMapping.put("North Texas","North Texas");
		NCAABMapping.put("Northeastern","Northeastern");
		NCAABMapping.put("Northwestern","Northwestern");
		NCAABMapping.put("Notre Dame","Notre Dame");
		NCAABMapping.put("Northwestern St.","Northwestern State");
		NCAABMapping.put("Oakland","Oakland");
		NCAABMapping.put("Ohio","Ohio");
		NCAABMapping.put("Ohio St.","Ohio State");
		NCAABMapping.put("Oklahoma","Oklahoma");
		NCAABMapping.put("Oklahoma St.","Oklahoma State");
		NCAABMapping.put("Old Dominion","Old Dominion");
		NCAABMapping.put("Oral Roberts","Oral Roberts");
		NCAABMapping.put("Oregon","Oregon");
		NCAABMapping.put("Oregon St.","Oregon State");
		NCAABMapping.put("Pacific","Pacific");
		NCAABMapping.put("Penn St.","Penn State");
		NCAABMapping.put("Pepperdine","Pepperdine");
		NCAABMapping.put("Pittsburgh","Pittsburgh");
		NCAABMapping.put("Portland","Portland");
		NCAABMapping.put("Portland St.","Portland State");
		NCAABMapping.put("Prairie View A&amp;M","Prairie View A&M");
		NCAABMapping.put("Presbyterian","Presbyterian");
		NCAABMapping.put("Princeton","Princeton");
		NCAABMapping.put("Providence","Providence");
		NCAABMapping.put("Purdue","Purdue");
		NCAABMapping.put("Quinnipiac","Quinnipiac");
		NCAABMapping.put("Radford","Radford");
		NCAABMapping.put("Rhode Island","Rhode Island");
		NCAABMapping.put("Rice","Rice");
		NCAABMapping.put("Richmond","Richmond");
		NCAABMapping.put("Rider","Rider");
		NCAABMapping.put("Robert Morris","Robert Morris");
		NCAABMapping.put("Rutgers","Rutgers");
		NCAABMapping.put("South Alabama","South Alabama");
		NCAABMapping.put("South Carolina St.","South Carolina State");
		NCAABMapping.put("South Carolina","South Carolina");
		NCAABMapping.put("South Dakota St.","South Dakota State");
		NCAABMapping.put("South Florida","South Florida");
		NCAABMapping.put("Southern Illinois","Southern Illinois");
		NCAABMapping.put("SMU","SMU");
		NCAABMapping.put("Southern Miss","Southern Miss");
		NCAABMapping.put("Southern Utah","Southern Utah");
		NCAABMapping.put("Sacramento St.","Sacramento State");
		NCAABMapping.put("Sacred Heart","Sacred Heart");
		NCAABMapping.put("Saint Louis","Saint Louis");
		NCAABMapping.put("Sam Houston St.","Sam Houston State");
		NCAABMapping.put("Samford","Samford");
		NCAABMapping.put("San Diego","San Diego");
		NCAABMapping.put("San Diego St.","San Diego State");
		NCAABMapping.put("San Francisco","San Francisco");
		NCAABMapping.put("San Jose St.","San Jose State");
		NCAABMapping.put("Santa Clara","Santa Clara");
		NCAABMapping.put("Savannah St.","Savannah State");
		NCAABMapping.put("USC Upstate","South Carolina Upstate");
		NCAABMapping.put("Southeastern Louisiana","SE Louisiana");
		NCAABMapping.put("Southeast Missouri St.","SE Missouri");
		NCAABMapping.put("Seattle","Seattle");
		NCAABMapping.put("Seton Hall","Seton Hall");
		NCAABMapping.put("Siena","Siena");
		NCAABMapping.put("SIU Edwardsville","SIU-Edwardsville");
		NCAABMapping.put("South Dakota","South Dakota");
		NCAABMapping.put("Southern","Southern U");
		NCAABMapping.put("St. Bonaventure","Saint Bonaventure");
		NCAABMapping.put("St. Francis NY","Saint Francis-NY");
		NCAABMapping.put("St. Francis PA","Saint Francis-Pa.");
		NCAABMapping.put("St. John's","St. John's");
		NCAABMapping.put("Saint Joseph's","Saint Joseph's-Pa.");
		NCAABMapping.put("Saint Mary's","Saint Mary's-California");
		NCAABMapping.put("Saint Peter's","Saint Peter's");
		NCAABMapping.put("Stanford","Stanford");
		NCAABMapping.put("Stephen F. Austin","Stephen F. Austin");
		NCAABMapping.put("Stetson","Stetson");
		NCAABMapping.put("Stony Brook","Stony Brook");
		NCAABMapping.put("Syracuse","Syracuse");
		NCAABMapping.put("Temple","Temple");
		NCAABMapping.put("Tennessee","Tennessee");
		NCAABMapping.put("Texas","Texas");
		NCAABMapping.put("Texas A&amp;M","Texas A&M");
		NCAABMapping.put("Texas St.","Texas State");
		NCAABMapping.put("Texas Tech","Texas Tech");
		NCAABMapping.put("Tennessee Martin","Tennessee-Martin");
		NCAABMapping.put("Tennessee St.","Tennessee State");
		NCAABMapping.put("Tennessee Tech","Tennessee Tech");
		NCAABMapping.put("Toledo","Toledo");
		NCAABMapping.put("Towson","Towson");
		NCAABMapping.put("Troy","Troy");
		NCAABMapping.put("Tulane","Tulane");
		NCAABMapping.put("Tulsa","Tulsa");
		NCAABMapping.put("Texas A&amp;M Corpus Chris","Texas A&M-CC");
		NCAABMapping.put("TCU","TCU");
		NCAABMapping.put("UTEP","UTEP");
		NCAABMapping.put("Texas Southern","Texas Southern");
		NCAABMapping.put("UT Arlington","Texas-Arlington");
		NCAABMapping.put("UT Rio Grande Valley","Texas Rio Grande Valley");
		NCAABMapping.put("UTSA","UTSA");
		NCAABMapping.put("Massachusetts","Massachusetts");
		NCAABMapping.put("Penn","Pennsylvania");
		NCAABMapping.put("UAB","UAB");
		NCAABMapping.put("UC Davis","UC Davis");
		NCAABMapping.put("UC Irvine","UC Irvine");
		NCAABMapping.put("UC Riverside","UC Riverside");
		NCAABMapping.put("UCLA","UCLA");
		NCAABMapping.put("UC Santa Barbara","UC Santa Barbara");
		NCAABMapping.put("UMKC","UMKC");
		NCAABMapping.put("UNLV","UNLV");
		NCAABMapping.put("USC","USC");
		NCAABMapping.put("Utah","Utah");
		NCAABMapping.put("Utah St.","Utah State");
		NCAABMapping.put("Utah Valley","Utah Valley State");
		NCAABMapping.put("VMI","VMI");
		NCAABMapping.put("Virginia Tech","Virginia Tech");
		NCAABMapping.put("Valparaiso","Valparaiso");
		NCAABMapping.put("Vanderbilt","Vanderbilt");
		NCAABMapping.put("VCU","VCU");
		NCAABMapping.put("Vermont","Vermont");
		NCAABMapping.put("Villanova","Villanova");
		NCAABMapping.put("Virginia","Virginia");
		NCAABMapping.put("Western Carolina","Western Carolina");
		NCAABMapping.put("Western Illinois","Western Illinois");
		NCAABMapping.put("Western Kentucky","Western Kentucky");
		NCAABMapping.put("Western Michigan","Western Michigan");
		NCAABMapping.put("West Virginia","West Virginia");
		NCAABMapping.put("Wagner","Wagner");
		NCAABMapping.put("Wake Forest","Wake Forest");
		NCAABMapping.put("Washington St.","Washington State");
		NCAABMapping.put("Washington","Washington");
		NCAABMapping.put("Weber St.","Weber State");
		NCAABMapping.put("Green Bay","Wisconsin Green Bay");
		NCAABMapping.put("Milwaukee","Wisconsin-Milwaukee");
		NCAABMapping.put("Wichita St.","Wichita State");
		NCAABMapping.put("Winthrop","Winthrop");
		NCAABMapping.put("Wisconsin","Wisconsin");
		NCAABMapping.put("William &amp; Mary","William & Mary");
		NCAABMapping.put("Wofford","Wofford");
		NCAABMapping.put("Wright St.","Wright State");
		NCAABMapping.put("Wyoming","Wyoming");
		NCAABMapping.put("Xavier","Xavier");
		NCAABMapping.put("Yale","Yale");
		NCAABMapping.put("Youngstown St.","Youngstown State");
	}

	/**
	 * Constructor
	 */
	public KenPomParser() {
		super();
		LOGGER.info("Entering SportsInsightsParser()");
		LOGGER.info("Exiting SportsInsightsParser()");
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
	 * @param ncaabDataList
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<KenPomData> parseNcaabSchedule(String dateOfEvent, List<KenPomData> ncaabDataList, String xhtml) throws BatchException {
		final Document doc = parseXhtml(xhtml);

		final Elements trs = doc.select("#fanmatch-table tbody tr");
		if (trs != null) {
			for (Element tr : trs) {
				final KenPomData kpd = new KenPomData();
				kpd.setDateString(dateOfEvent);
				try {
					Date gameDate = GAME_DATE_FORMAT.parse(dateOfEvent);
					kpd.setDateOfGame(gameDate);
				} catch (ParseException pe) {
					LOGGER.error(pe.getMessage(), pe);
				}

				int count = 0;
				final Elements tds = tr.select("td");
				for (Element td : tds) {
					switch (count) {
						case 0:
							parseTeams(kpd, td) ;
							break;
						case 1:
							parsePredictions(kpd, td);
							break;
						default:
							break;
					}
					count++;
				}

				LOGGER.debug("KenPomData: " + kpd);
				ncaabDataList.add(kpd);
			}
		}

		return ncaabDataList;
	}

	/**
	 * 
	 * @param kpd
	 * @param xhtml
	 * @param locationType
	 * @param team
	 * @throws BatchException
	 */
	public void parseNcaabLocation(KenPomData kpd, String xhtml, int locationType, String team) throws BatchException {
		LOGGER.debug("kpd: " + kpd);
		final Document doc = parseXhtml(xhtml);

		final Elements trs = doc.select("#schedule-table tbody tr");
		if (trs != null) {
			for (Element tr : trs) {
				int count = 0;
				boolean found = false;
				final Elements tds = tr.select("td");
				for (Element td : tds) {
					switch (count) {
						case 0:
							found = parseDate(kpd, td) ;
							break;
						case 7:
							if (found) {
								parseLocation(kpd, td, locationType, team);
							}
							break;
						default:
							break;
					}
					count++;
				}
			}
		}
	}

	/**
	 * 
	 * @param kpd
	 * @param td
	 */
	public boolean parseDate(KenPomData kpd, Element td) {
		// <a href="fanmatch.php?d=2017-11-10">Fri Nov 10</a>
		final Elements as = td.select("a");
		boolean found = false;
		
		if (as != null && as.size() > 0) {
			Element a = as.get(0);
			String dt = a.html().trim();

			try {
				final Date gameDate = LOCATION_DATE_FORMAT.parse(dt);
				final Calendar dayOfGame = Calendar.getInstance();
				dayOfGame.setTime(kpd.getDateOfGame());
				
				final Calendar locationDate = Calendar.getInstance();
				locationDate.setTime(gameDate);
				
				int monthdog = dayOfGame.get(Calendar.MONTH);
				int daydog = dayOfGame.get(Calendar.DAY_OF_MONTH);

				int monthld = locationDate.get(Calendar.MONTH);
				int dayld = locationDate.get(Calendar.DAY_OF_MONTH);
				
				LOGGER.debug("monthdog: " + monthdog);
				LOGGER.debug("daydog: " + daydog);
				LOGGER.debug("monthld: " + monthld);
				LOGGER.debug("dayld: " + dayld);

				if (monthdog == monthld && daydog == dayld) {
					found = true;
				}
			} catch (ParseException pe) {
				LOGGER.warn(pe.getMessage(), pe);
			}
		}

		return found;
	}

	/**
	 * 
	 * @param kpd
	 * @param td
	 * @param locationType
	 * @param team
	 */
	public void parseLocation(KenPomData kpd, Element td, int locationType, String team) {
		// <a title="Spokane, WA" href="https://maps.google.com?q=Spokane+Arena,+Spokane, WA&z=9&num=1&t=m" target="_blank">Semi-Home</a>
		LOGGER.debug("td: " + td);
		final Elements as = td.select("a");

		if (as != null && as.size() > 0) {
			final Element a = as.get(0);
			final String location = a.html().trim();
			LOGGER.debug("Location: " + location);

			if ("Home".equals(location)) {
				kpd.setSiteLocation(4);
				kpd.setSiteLocationType(1);
				if (locationType == 0 && kpd.getRoadTeam() != null && kpd.getRoadTeam().equals(team)) {
					String roadTeam = kpd.getRoadTeam();
					String homeTeam = kpd.getHomeTeam();
					// Swap them
					kpd.setRoadTeam(homeTeam);
					kpd.setHomeTeam(roadTeam);
					int roadScore = kpd.getRoadPoints();
					int homeScore = kpd.getHomePoints();
					kpd.setRoadPoints(homeScore);
					kpd.setHomePoints(roadScore);
					kpd.setSpread(roadScore - homeScore);
				}
			} else if ("Away".equals(location)) {
				kpd.setSiteLocation(4);
				kpd.setSiteLocationType(2);
				if (locationType == 1 && kpd.getHomeTeam() != null && kpd.getHomeTeam().equals(team)) {
					String roadTeam = kpd.getRoadTeam();
					String homeTeam = kpd.getHomeTeam();
					// Swap them
					kpd.setRoadTeam(homeTeam);
					kpd.setHomeTeam(roadTeam);
					int roadScore = kpd.getRoadPoints();
					int homeScore = kpd.getHomePoints();
					kpd.setHomePoints(roadScore);
					kpd.setRoadPoints(homeScore);
					kpd.setSpread(homeScore - roadScore);
				}
			} else if ("Semi-Home".equals(location)) {
				kpd.setSiteLocation(2);
				kpd.setSiteLocationType(3);
				if (locationType == 0 && kpd.getRoadTeam() != null && kpd.getRoadTeam().equals(team)) {
					String roadTeam = kpd.getRoadTeam();
					String homeTeam = kpd.getHomeTeam();
					// Swap them
					kpd.setRoadTeam(homeTeam);
					kpd.setHomeTeam(roadTeam);
					int roadScore = kpd.getRoadPoints();
					int homeScore = kpd.getHomePoints();
					kpd.setHomePoints(homeScore);
					kpd.setRoadPoints(roadScore);
					kpd.setSpread(roadScore - homeScore);
				}
			} else if ("Semi-Away".equals(location)) {
				kpd.setSiteLocation(2);
				kpd.setSiteLocationType(4);
				if (locationType == 1 && kpd.getHomeTeam() != null && kpd.getHomeTeam().equals(team)) {
					String roadTeam = kpd.getRoadTeam();
					String homeTeam = kpd.getHomeTeam();
					// Swap them
					kpd.setRoadTeam(homeTeam);
					kpd.setHomeTeam(roadTeam);
					int roadScore = kpd.getRoadPoints();
					int homeScore = kpd.getHomePoints();
					kpd.setHomePoints(roadScore);
					kpd.setRoadPoints(homeScore);
					kpd.setSpread(homeScore - roadScore);
				}
			} else if ("Neutral".equals(location)) {
				kpd.setSiteLocation(0);
				kpd.setSiteLocationType(5);
				LOGGER.debug("Swapping Neutral");
				// How to handle this one???
			}
		}
	}

	/**
	 * 
	 * @param kpd
	 * @param td
	 */
	public void parseTeams(KenPomData kpd, Element td) {
		// <a href="team.php?team=Gonzaga">Gonzaga</a>
		// <a href="team.php?team=San+Diego+St.">San Diego St.</a>
		final Elements as = td.select("a");

		if (as != null && as.size() > 1) {
			String team1 = as.get(0).html().trim();
			String href1 = as.get(0).attr("href").trim();
			LOGGER.debug("team1: " + team1);
			LOGGER.debug("href1: " + href1);
			final Iterator<String> itr = NCAABMapping.keySet().iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				if (key.equals(team1)) {
					kpd.setRoadTeam(NCAABMapping.get(key));
				}
			}
			kpd.setRoadUrlData(href1);

			String team2 = as.get(1).html().trim();
			String href2 = as.get(1).attr("href").trim();
			LOGGER.debug("team2: " + team2);
			LOGGER.debug("href2: " + href2);
			final Iterator<String> itr2 = NCAABMapping.keySet().iterator();
			while (itr2.hasNext()) {
				String key = itr2.next();
				if (key.equals(team2)) {
					kpd.setHomeTeam(NCAABMapping.get(key));
				}
			}
			kpd.setHomeUrlData(href2);
		}
	}

	/**
	 * 
	 * @param kpd
	 * @param td
	 */
	public void parsePredictions(KenPomData kpd, Element td) {
		String html = td.html().trim();
		if (html != null) {
			int index = html.lastIndexOf(" ");
			if (index != -1) {
				html = html.substring(0, index);
				index = html.lastIndexOf(" ");
				if (index != -1) {
					String favorite = html.substring(0, index).trim();
					final Iterator<String> itr = NCAABMapping.keySet().iterator();
					while (itr.hasNext()) {
						String key = itr.next();
						if (key.equals(favorite)) {
							favorite = NCAABMapping.get(key);
						}
					}

					String predictionScore = html.substring(index+1).trim();
					int hyphen = predictionScore.indexOf("-");
					if (hyphen != -1) {
						String score1 = predictionScore.substring(0, hyphen).trim();
						String score2 = predictionScore.substring(hyphen + 1).trim();

						int s1 = Integer.parseInt(score1);
						int s2 = Integer.parseInt(score2);

						if (kpd.getRoadTeam() != null && kpd.getRoadTeam().equals(favorite)) {
							kpd.setRoadPoints(s1);
							kpd.setHomePoints(s2);
							kpd.setSpread(s1 - s2);
						} else if (kpd.getHomeTeam() != null) {
							kpd.setHomePoints(s1);
							kpd.setRoadPoints(s2);
							kpd.setSpread(s2 - s1);							
						}
					}
				}
			}
		}
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
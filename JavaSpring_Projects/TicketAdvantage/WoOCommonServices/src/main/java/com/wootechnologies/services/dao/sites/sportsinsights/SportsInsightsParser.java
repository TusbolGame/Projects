/**
 * 
 */
package com.wootechnologies.services.dao.sites.sportsinsights;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.model.EventPackage;
import com.wootechnologies.model.TeamPackage;
import com.wootechnologies.services.dao.sites.SiteParser;
import com.wootechnologies.services.dao.sites.SiteTeamPackage;

/**
 * @author jmiller
 *
 */
public class SportsInsightsParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(SportsInsightsParser.class);
	private static final Map<String, String> NFLMapping = new HashMap<String, String>();
	private static final Map<String, String> MLBMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAABMapping = new HashMap<String, String>();
	private final SimpleDateFormat sdfmt1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
	private static final List<EventPackage> EVENT_PACKAGES = new ArrayList<EventPackage>();

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
		NCAABMapping.put("Appalachian St","Appalachian State");
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
		NCAABMapping.put("Charlotte U","Charlotte");
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
		NCAABMapping.put("Depaul","DePaul");
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
		NCAABMapping.put("Lafayette","Louisiana-Lafayette");
		NCAABMapping.put("UL Monroe","Louisiana-Monroe");
		NCAABMapping.put("La Salle","La Salle");
		NCAABMapping.put("Louisiana Tech","Louisiana Tech");
		NCAABMapping.put("UL Lafayette","Lafayette");
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
		NCAABMapping.put("SE Missouri State","SE Missouri");
		NCAABMapping.put("Seattle","Seattle");
		NCAABMapping.put("Seton Hall","Seton Hall");
		NCAABMapping.put("Siena","Siena");
		NCAABMapping.put("SIU Edwardsville","SIU-Edwardsville");
		NCAABMapping.put("South Dakota","South Dakota");
		NCAABMapping.put("Southern","Southern U");
		NCAABMapping.put("St. Bonaventure","Saint Bonaventure");
		NCAABMapping.put("St. Francis NY","Saint Francis-NY");
		NCAABMapping.put("St. Francis PA","Saint Francis-Pa.");
		NCAABMapping.put("St. Johns","St. John's");
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
		NCAABMapping.put("Cal Santa Barbara","UC Santa Barbara");
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
		NCAABMapping.put("Wisc Green Bay","Wisconsin Green Bay");
		NCAABMapping.put("Wisc Milwaukee","Wisconsin-Milwaukee");
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
	public SportsInsightsParser() {
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
	 * @param xhtml
	 * @return
	 */
	public Set<EventPackage> getAllEvents(String xhtml) {
		LOGGER.info("Entering getAllEvents()");
		LOGGER.debug("XHTML: " + xhtml);
		Set<EventPackage> eventPackages = null;

		try {
			if (xhtml != null) {
				final Document doc = Jsoup.parse(xhtml);
				if (doc != null) {
					final Elements divs = doc.select(".ag-pinned-left-cols-container div");
					if (divs != null) {
						processGames(divs);
						for (EventPackage eventPackage: EVENT_PACKAGES) {
							LOGGER.debug("EventPackage: " + eventPackage);
						}
						eventPackages = new LinkedHashSet<EventPackage>(EVENT_PACKAGES);
						LOGGER.debug("EventPackages: " + eventPackages);

/*
						ep.addEvents(processSport("NFL", divs, "nfllines"));
						ep.addEvents(processSport("NFL", divs, "nflfirst"));
						ep.addEvents(processSport("NFL", divs, "nflsecond"));
						ep.addEvents(processSport("NCAAF", divs, "ncaaflines"));
						ep.addEvents(processSport("NCAAF", divs, "ncaaffirst"));
						ep.addEvents(processSport("NCAAF", divs, "ncaafsecond"));
						ep.addEvents(processSport("NBA", divs, "nbalines"));
						ep.addEvents(processSport("NBA", divs, "nbafirst"));
						ep.addEvents(processSport("NBA", divs, "nbasecond"));
						ep.addEvents(processSport("NCAAB", divs, "ncaablines"));
						ep.addEvents(processSport("NCAAB", divs, "ncaabfirst"));
						ep.addEvents(processSport("NCAAB", divs, "ncaabsecond"));
						ep.addEvents(processSport("NHL", divs, "nhllines"));
						ep.addEvents(processSport("NHL", divs, "nhlfirst"));
						ep.addEvents(processSport("NHL", divs, "nhlsecond"));
						ep.addEvents(processSport("WNBA", divs, "wnbalines"));
						ep.addEvents(processSport("WNBA", divs, "wnbafirst"));
						ep.addEvents(processSport("WNBA", divs, "wnbasecond"));
						ep.addEvents(processSport("MLB", divs, "mlblines"));
						ep.addEvents(processSport("MLB", divs, "mlbfirst"));
						ep.addEvents(processSport("MLB", divs, "mlbsecond"));
						ep.addEvents(processSport("MLB", divs, "mlbthird"));
*/
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception processing events", e);
		}
		LOGGER.info("Exiting getAllEvents()");
		return eventPackages;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 */
	public Set<EventPackage> getSport(String xhtml, String sportType) {
		LOGGER.info("Entering getSport()");
		LOGGER.debug("XHTML: " + xhtml);
		LOGGER.debug("sportType: " + sportType);

		Set<EventPackage> eventPackages = null;
/*
		final Set<EventPackage> eventPackages = new LinkedHashSet<EventPackage>(EVENT_PACKAGES);
		LOGGER.debug("EventPackages: " + eventPackages);
		LOGGER.info("Exiting processSport()");
*/
		try {
			if (xhtml != null) {
				final Document doc = Jsoup.parse(xhtml);
				if (doc != null) {
					final Elements elements = doc.select(".ag-pinned-left-cols-container div");
					if (elements != null) {
/*
						if (sportType.contains("nfl")) {
							eventPackages = processSport("NFL", elements, sportType);
						} else if (sportType.contains("ncaaf")) {
							eventPackages = processSport("NCAAF", elements, sportType);
						} else if (sportType.contains("nba")) {
							eventPackages = processSport("NBA", elements, sportType);
						} else if (sportType.contains("ncaab")) {
							eventPackages = processSport("NCAAB", elements, sportType);
						} else if (sportType.contains("nhl")) {
							eventPackages = processSport("NHL", elements, sportType);
						} else if (sportType.contains("wnba")) {
							eventPackages = processSport("WNBA", elements, sportType);
						} else if (sportType.contains("mlb")) {
							eventPackages = processSport("MLB", elements, sportType);
						}
*/
					}
				}
			}
			LOGGER.debug("EventPackages: " + eventPackages);
		} catch (Exception e) {
			LOGGER.error("Exception processing " + sportType, e);
		}

		LOGGER.info("Exiting getSport()");
		return eventPackages;
	}

	/**
	 * 
	 * @param divs
	 * @param eventType
	 * @return
	 * @throws ParseException
	 */
	private List<EventPackage> processGames(Elements divs) throws ParseException {
		LOGGER.info("Entering processSport()");
		LOGGER.debug("Divs: "  + divs);
		String textDate = "";
		Date dateDate = null;
		String sportName = null;

		// Loop through everything
		for (Element div : divs) {
			// First try to get the date/sport
			if (hasDateOrType(div.html())) {
				String spanValue = getSpanValue(div);
				
				// Now check if date or type
				if (spanValue != null && spanValue.contains("/")) {
					// We have a date
					SimpleDateFormat sdfmt1 = new SimpleDateFormat("MM/dd/yyyy z");
					try {
						final Calendar now = Calendar.getInstance();
						int offset = now.get(Calendar.DST_OFFSET);
						textDate = spanValue + " " + timeZoneLookup("CT", offset);
						LOGGER.debug("textDate: " + textDate);
						dateDate = sdfmt1.parse(textDate);
					} catch (ParseException pe) {
						LOGGER.error("ParseException" , pe);
					}
				} else {
					// Get the sport name
					sportName = spanValue;
				}
			} else {
				// Get the game
				final EventPackage eventPackage = getGame(div, sportName, textDate, dateDate);
				EVENT_PACKAGES.add(eventPackage);
			}
		}
		return EVENT_PACKAGES;		
	}

	/**
	 * 
	 * @param divHtml
	 * @return
	 */
	private boolean hasDateOrType(String divHtml) {
		LOGGER.debug("Entering hasDateOrType()");
		boolean hasDateType = false;

		// Check if it has groupValue
		if (divHtml.contains("ag-group-value")) {
			hasDateType = true;
		}

		LOGGER.debug("Exiting hasDateOrType()");
		return hasDateType;
	}

	/**
	 * 
	 * @param div
	 * @return
	 */
	private String getSpanValue(Element div) {
		LOGGER.info("Entering getSpanValue()");
		String returnDate = "";

		// <div class="ag-row ag-row-no-focus ag-row-even ag-row-group ag-row-level-0 ag-row-group-expanded" row="0" style="top: 0px; height: 21px;">
		//	<span class="ag-group-cell-entire-row">
		//		<span>
		//			<span class="ag-group-expanded" style="display: inline;">
		//				<i class="fa fa-caret-down"></i>
		//			</span>
		//			<span class="ag-group-contracted" style="display: none;">
		//				<i class="fa fa-caret-right"></i>
		//			</span>
		//			<span class="ag-group-checkbox"></span>
		//			<span class="ag-group-value">06/16/2017</span>
		//			<span class="ag-group-child-count">(15)</span>
		//		</span>
		//	</span>
		// </div>

		final Elements spans = div.select("span span span");
		for (Element span : spans) {
			final String className = span.attr("class");
			if ("ag-group-value".equals(className)) {
				returnDate = super.reformatValues(span.html());
			}
		}

		LOGGER.info("Exiting getSpanValue()");
		return returnDate;
	}

	/**
	 * 
	 * @param element
	 * @param sportName
	 * @param textDate
	 * @param dateDate
	 */
	private EventPackage getGame(Element element, String sportName, String textDate, Date dateDate) {
		LOGGER.info("Entering getGame()");
		LOGGER.debug("Element: " + element);
		LOGGER.debug("sportName: " + sportName);
		LOGGER.debug("textDate: " + textDate);
		LOGGER.debug("dateDate: " + dateDate);

		// <div style="width: 80px; left: 53px;" class="ag-cell-no-focus ag-cell ag-cell-not-inline-editing ag-cell-value cell-pointer" colid="date">
		// 	 <div class=" cell-change-3">
		// 		<span class="score-span">0</span>
		// 		<span class="period-span">TOP</span>
		// 	 </div>
		// 	 <div class="cell-change-3">
		// 		<span class="score-span">4</span>
		// 		<span class="period-span">3</span>
		// 	 </div>
		// </div>
		// <div style="width: 50px; left: 133px;" class="ag-cell-no-focus ag-cell ag-cell-not-inline-editing ag-cell-value" colid="nss">
		//   <div>975</div>
		//   <div>976</div>
		// </div>
		// <div style="width: 135px; left: 183px;" class="ag-cell-last-left-pinned ag-cell-no-focus ag-cell ag-cell-not-inline-editing ag-cell-value" colid="team">
		// 	 <div class="">NYY-L Severino</div>
		// 	 <div class="">OAK-S Manaea (L)</div>
		// </div>
		final TeamPackage team1 = new TeamPackage();
		final TeamPackage team2 = new TeamPackage();
		final EventPackage eventPackage = new EventPackage();
		eventPackage.setTeamone(team1);
		eventPackage.setTeamtwo(team2);
		eventPackage.setEventtype(sportName);

		int x = 0;
		final Elements divs = element.select(".ag-cell-no-focus");
		for (Element div : divs) {
			switch (x++) {
				case 0:
				case 1:
					break;
				case 2:
					// Get the score/game time
					getGameData(div, eventPackage, team1, team2, textDate, dateDate);
				case 3:
					// Get the rotation ID's
					getRotations(div, team1, team2);
				case 4:
					// Get the teams
					getTeams(div, sportName, team1, team2);
					break;
			}
		}

		LOGGER.info("Exiting getGame()");
		return eventPackage;
	}

	/**
	 * 
	 * @param element
	 * @param eventPackage
	 * @param team1
	 * @param team2
	 * @param textDate
	 * @param dateDate
	 */
	private void getGameData(Element element, EventPackage eventPackage, TeamPackage team1, TeamPackage team2, String textDate, Date dateDate) {
		LOGGER.info("Entering getGameData()");
		String dateString = null;
		String timeString = null;
		
		int x = 0;
		final Elements divs = element.select("div");
		for (Element div : divs) {
			switch (x++) {
				case 0:
					{
						final Elements spans = div.select("span");
						if (spans != null && spans.size() > 0) {
							try {
								team1.setScore(Integer.parseInt(super.reformatValues(spans.get(0).html()))); // Score
								eventPackage.setPeriodType(super.reformatValues(spans.get(1).html())); // Period Type
							} catch (Throwable t) {
								LOGGER.error(t);
							}
						} else {
							// Means we only have a date
							dateString = div.html();
						}
					}
					break;
				case 1:
					{
						final Elements spans = div.select("span");
						if (spans != null && spans.size() > 0) {
							try {
								team2.setScore(Integer.parseInt(super.reformatValues(spans.get(0).html()))); // Score
								eventPackage.setPeriodNumber(super.reformatValues(spans.get(1).html())); // Period Number
							} catch (Throwable t) {
								LOGGER.error(t);
							}
						} else {
							// Means we only have a time
							timeString = div.html();
						}
						setupDates(eventPackage, team1, team2, dateString, timeString, textDate, dateDate);
					}
				break;
			}
		}

		LOGGER.info("Exiting getGameData()");
	}

	/**
	 * 
	 * @param eventPackage
	 * @param team1
	 * @param team2
	 * @param dateString
	 * @param timeString
	 * @param textDate
	 * @param dateDate
	 */
	private void setupDates(EventPackage eventPackage, TeamPackage team1, TeamPackage team2, String dateString, String timeString, String textDate, Date dateDate) {
		LOGGER.info("Entering setupDates()");
		LOGGER.debug("dateString: " + dateString);
		LOGGER.debug("timeString: " + timeString);
		LOGGER.debug("textDate: " + textDate);
		LOGGER.debug("dateDate: " + dateDate);
		Date eventDate = null;
		String combinedDate = null;

		try {

			final Calendar now = Calendar.getInstance();
			int offset = now.get(Calendar.DST_OFFSET);
			if (dateString != null && dateString.length() != 5) {
				dateString += "/" + String.valueOf(now.get(Calendar.YEAR));
			} else {
				if (textDate != null) {
					dateString = textDate;
				}
			}

			if (timeString != null && timeString.length() > 0) {
				combinedDate = dateString + " " + timeString + " " + timeZoneLookup("PT", offset);
			} else {
				int month = now.get(Calendar.MONTH) + 1;
				int day = now.get(Calendar.DAY_OF_MONTH);
				int year = now.get(Calendar.YEAR);
				int hour = now.get(Calendar.HOUR_OF_DAY);
				int minute = now.get(Calendar.MINUTE);

				if (dateString == null) {
					dateString = month + "/";
					if (day < 10) {
						dateString += "0" + day + "/";
					} else {
						dateString += day + "/";
					}
					dateString += year;
				}

				String ampm = "AM";
				if (hour > 12) {
					hour = hour - 12;
					ampm = "PM";
				}
				if (hour < 10) {
					timeString = "0" + hour + ":";
				} else {
					timeString = hour + ":";
				}
				if (minute < 10) {
					timeString += "0" + minute + " " + ampm;
				} else {
					timeString += minute + " " + ampm;
				}
				combinedDate = dateString + " " + timeString + " " + timeZoneLookup("PT", offset);
			}
			LOGGER.debug("combinedDate: " + combinedDate);
			eventDate = sdfmt1.parse(combinedDate);
		} catch (ParseException pe) {
			LOGGER.error("Error parsing date", pe);
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
			eventDate = now.getTime();
		}
		LOGGER.debug("eventDate: " + eventDate);
		LOGGER.debug("dateElement: " + dateString);
		LOGGER.debug("timeElement: " + timeString);
		team1.setEventdatetime(eventDate);
		team1.setDateofevent(dateString);
		team1.setTimeofevent(timeString);
		team2.setEventdatetime(eventDate);
		team2.setDateofevent(dateString);
		team2.setTimeofevent(timeString);

		eventPackage.setDateofevent(dateString);
		eventPackage.setTimeofevent(timeString);
		eventPackage.setEventdatetime(eventDate);

		LOGGER.info("Exiting setupDates()");
	}

	/**
	 * 
	 * @param element
	 * @param team1
	 * @param team2
	 */
	private void getRotations(Element element, TeamPackage team1, TeamPackage team2) {
		LOGGER.info("Entering getRotations()");

		int x = 0;
		final Elements divs = element.select("div");
		for (Element div : divs) {
			switch (x++) {
				case 0:
					setupTeamRotation(div.html(), team1);
					break;
				case 1:
					setupTeamRotation(div.html(), team2);
					break;
			}
		}

		LOGGER.info("Exiting getRotations()");
	}

	/**
	 * 
	 * @param element
	 * @param team1
	 * @param team2
	 */
	private void getTeams(Element element, String sportType, TeamPackage team1, TeamPackage team2) {
		LOGGER.info("Entering getTeams()");

		int x = 0;
		final Elements divs = element.select("div");
		for (Element div : divs) {
			switch (x++) {
				case 0:
					{
						setupTeamName(div.html(), sportType, team1);
					}
					break;
				case 1:
					{
						setupTeamName(div.html(), sportType, team2);
					}
					break;
			}
		}

		LOGGER.info("Exiting getTeams()");
	}

	/**
	 * 
	 * @param rotation
	 * @param team
	 */
	private void setupTeamRotation(String rotation, TeamPackage team) {
		LOGGER.info("Entering setupTeamRotation()");
		LOGGER.debug("rotation: " + rotation);

		team.setId(Integer.parseInt(rotation));
		team.setEventid(rotation);

		LOGGER.info("Exiting setupTeamRotation()");
	}

	/**
	 * 
	 * @param teamName
	 * @param sportType
	 * @param team
	 */
	private void setupTeamName(String teamName, String sportType, TeamPackage team) {
		LOGGER.info("Entering setupTeamName()");

		if (teamName != null) {
			int index = teamName.indexOf("-");
			if (index != -1) {
				teamName = teamName.substring(0, index);
			}
		}

		// Map the names
		if (sportType.equals("NFL")) {
			teamName = NFLMapping.get(teamName);
		} else if (sportType.equals("MLB")) {
			teamName = MLBMapping.get(teamName);
		} else if (sportType.equals("NCAAB")) {
			final Iterator<String> itr = NCAABMapping.keySet().iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				if (key.equals(teamName)) {
					teamName = NCAABMapping.get(key);
				}
			}
		}

		// Set the team name
		team.setTeam(teamName);

		LOGGER.info("Exiting setupTeamName()");
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
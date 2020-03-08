/**
 * 
 */
package com.wooanalytics.dao.sites.usatoday;

import java.util.ArrayList;
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
public class UsaTodayParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(UsaTodayParser.class);
	private static final Map<String, String> NFLMapping = new HashMap<String, String>();
	private static final Map<String, String> MLBMapping = new HashMap<String, String>();
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
		
		NCAABMapping.put("Abilene Christian","Abilene Christian");
		NCAABMapping.put("Air Force","Air Force");
		NCAABMapping.put("Akron","Akron");
		NCAABMapping.put("Alabama","Alabama");
		NCAABMapping.put("Alabama AM","Alabama A&M");
		NCAABMapping.put("Alabama State","Alabama State");
		NCAABMapping.put("Albany-NY","Albany");
		NCAABMapping.put("Alcorn State","Alcorn State");
		NCAABMapping.put("American U.","American University");
		NCAABMapping.put("Appalachian State","Appalachian State");
		NCAABMapping.put("Arizona","Arizona");
		NCAABMapping.put("Arizona State","Arizona State");
		NCAABMapping.put("Ark.-Pine Bluff","Arkansas Pine Bluff");
		NCAABMapping.put("Arkansas","Arkansas");
		NCAABMapping.put("Arkansas State","Arkansas State");
		NCAABMapping.put("Army West Point","Army");
		NCAABMapping.put("Auburn","Auburn");
		NCAABMapping.put("Austin Peay","Austin Peay");
		NCAABMapping.put("Ball State","Ball State");
		NCAABMapping.put("Baylor","Baylor");
		NCAABMapping.put("Belmont","Belmont");
		NCAABMapping.put("Bethune-Cookman","Bethune-Cookman");
		NCAABMapping.put("Binghamton-NY","Binghamton");
		NCAABMapping.put("Boise State","Boise State");
		NCAABMapping.put("Boston College","Boston College");
		NCAABMapping.put("Boston U.","Boston University");
		NCAABMapping.put("Bowling Green","Bowling Green");
		NCAABMapping.put("Bradley","Bradley");
		NCAABMapping.put("Brown","Brown");
		NCAABMapping.put("Bryant","Bryant");
		NCAABMapping.put("Bucknell","Bucknell");
		NCAABMapping.put("Buffalo","Buffalo");
		NCAABMapping.put("Butler","Butler");
		NCAABMapping.put("BYU","BYU");
		NCAABMapping.put("Cal Poly-SLO","Cal Poly-SLO");
		NCAABMapping.put("California","California");
		NCAABMapping.put("Campbell","Campbell");
		NCAABMapping.put("Canisius","Canisius");
		NCAABMapping.put("Central Arkansas","Central Arkansas");
		NCAABMapping.put("Central Connecticut St.","Central Connecticut State");
		NCAABMapping.put("Central Florida(UCF)","Central Florida");
		NCAABMapping.put("Central Michigan","Central Michigan");
		NCAABMapping.put("Charleston Southern","Charleston Southern");
		NCAABMapping.put("Charlotte","Charlotte");
		NCAABMapping.put("Chattanooga","Chattanooga");
		NCAABMapping.put("Chicago State","Chicago State");
		NCAABMapping.put("Cincinnati","Cincinnati");
		NCAABMapping.put("Clemson","Clemson");
		NCAABMapping.put("Cleveland State","Cleveland State");
		NCAABMapping.put("Coastal Carolina","Coastal Carolina");
		NCAABMapping.put("Colgate","Colgate");
		NCAABMapping.put("College of Charleston","College of Charleston");
		NCAABMapping.put("Colorado","Colorado");
		NCAABMapping.put("Colorado State","Colorado State");
		NCAABMapping.put("Columbia","Columbia");
		NCAABMapping.put("Connecticut","Connecticut");
		NCAABMapping.put("Coppin State","Coppin State");
		NCAABMapping.put("Cornell","Cornell");
		NCAABMapping.put("Creighton","Creighton");
		NCAABMapping.put("CS Bakersfield","CS Bakersfield");
		NCAABMapping.put("CS Fullerton","CS Fullerton");
		NCAABMapping.put("CS Northridge","CS Northridge");
		NCAABMapping.put("Dartmouth","Dartmouth");
		NCAABMapping.put("Davidson","Davidson");
		NCAABMapping.put("Dayton","Dayton");
		NCAABMapping.put("Delaware","Delaware");
		NCAABMapping.put("Delaware State","Delaware State");
		NCAABMapping.put("Denver","Denver");
		NCAABMapping.put("DePaul","DePaul");
		NCAABMapping.put("Detroit Mercy","Detroit Mercy");
		NCAABMapping.put("Drake","Drake");
		NCAABMapping.put("Drexel","Drexel");
		NCAABMapping.put("Duke","Duke");
		NCAABMapping.put("Duquesne","Duquesne");
		NCAABMapping.put("East Carolina","East Carolina");
		NCAABMapping.put("East Tennessee State(ETS","East Tennessee State");
		NCAABMapping.put("Eastern Illinois","Eastern Illinois");
		NCAABMapping.put("Eastern Kentucky","Eastern Kentucky");
		NCAABMapping.put("Eastern Michigan","Eastern Michigan");
		NCAABMapping.put("Eastern Washington","Eastern Washington");
		NCAABMapping.put("Elon","Elon");
		NCAABMapping.put("Evansville","Evansville");
		NCAABMapping.put("Fairfield","Fairfield");
		NCAABMapping.put("Fairleigh Dickinson","Fairleigh Dickinson");
		NCAABMapping.put("Fla. International","Florida International");
		NCAABMapping.put("Florida","Florida");
		NCAABMapping.put("Florida AM","Florida A&M");
		NCAABMapping.put("Florida Atlantic","Florida Atlantic");
		NCAABMapping.put("Florida Gulf Coast","Florida Gulf Coast");
		NCAABMapping.put("Florida State","Florida State");
		NCAABMapping.put("Fordham","Fordham");
		NCAABMapping.put("Fort Wayne(IPFW)","Fort Wayne(IPFW)");
		NCAABMapping.put("Fresno State","Fresno State");
		NCAABMapping.put("Furman","Furman");
		NCAABMapping.put("Gardner-Webb","Gardner-Webb");
		NCAABMapping.put("George Mason","George Mason");
		NCAABMapping.put("George Washington","George Washington");
		NCAABMapping.put("Georgetown","Georgetown");
		NCAABMapping.put("Georgia","Georgia");
		NCAABMapping.put("Georgia Southern","Georgia Southern");
		NCAABMapping.put("Georgia State","Georgia State");
		NCAABMapping.put("Georgia Tech","Georgia Tech");
		NCAABMapping.put("Gonzaga","Gonzaga");
		NCAABMapping.put("Grambling State","Grambling State");
		NCAABMapping.put("Grand Canyon","Grand Canyon");
		NCAABMapping.put("Green Bay","Wisconsin Green Bay");
		NCAABMapping.put("Hampton","Hampton");
		NCAABMapping.put("Hartford","Hartford");
		NCAABMapping.put("Harvard","Harvard");
		NCAABMapping.put("Hawai'i","Hawaii");
		NCAABMapping.put("High Point","High Point");
		NCAABMapping.put("Hofstra","Hofstra");
		NCAABMapping.put("Holy Cross","Holy Cross");
		NCAABMapping.put("Houston","Houston");
		NCAABMapping.put("Houston Baptist","Houston Baptist");
		NCAABMapping.put("Howard","Howard");
		NCAABMapping.put("Idaho","Idaho");
		NCAABMapping.put("Idaho State","Idaho State");
		NCAABMapping.put("Illinois","Illinois");
		NCAABMapping.put("Illinois State","Illinois State");
		NCAABMapping.put("Illinois-Chicago","Illinois-Chicago");
		NCAABMapping.put("Incarnate Word","Incarnate Word");
		NCAABMapping.put("Indiana","Indiana");
		NCAABMapping.put("Indiana State","Indiana State");
		NCAABMapping.put("Iona College","Iona");
		NCAABMapping.put("Iowa","Iowa");
		NCAABMapping.put("Iowa State","Iowa State");
		NCAABMapping.put("IUPUI","IUPUI");
		NCAABMapping.put("Jackson State","Jackson State");
		NCAABMapping.put("Jacksonville","Jacksonville");
		NCAABMapping.put("Jacksonville State","Jacksonville State");
		NCAABMapping.put("James Madison","James Madison");
		NCAABMapping.put("Kansas","Kansas");
		NCAABMapping.put("Kansas City(UMKC)","UMKC");
		NCAABMapping.put("Kansas State","Kansas State");
		NCAABMapping.put("Kennesaw State","Kennesaw State");
		NCAABMapping.put("Kent State","Kent State");
		NCAABMapping.put("Kentucky","Kentucky");
		NCAABMapping.put("La Salle","La Salle");
		NCAABMapping.put("Lafayette","Lafayette");
		NCAABMapping.put("Lamar","Lamar");
		NCAABMapping.put("Lehigh","Lehigh");
		NCAABMapping.put("Liberty","Liberty");
		NCAABMapping.put("Lipscomb","Lipscomb");
		NCAABMapping.put("Little Rock","Arkansas Little Rock");
		NCAABMapping.put("Long Beach State","Long Beach State");
		NCAABMapping.put("Long Island U.(LIU)","Long Island University");
		NCAABMapping.put("Longwood","Longwood");
		NCAABMapping.put("Louisiana Tech","Louisiana Tech");
		NCAABMapping.put("Louisiana-Lafayette","Louisiana-Lafayette");
		NCAABMapping.put("Louisiana-Monroe","Louisiana-Monroe");
		NCAABMapping.put("Louisville","Louisville");
		NCAABMapping.put("Loyola Marymount","Loyola Marymount");
		NCAABMapping.put("Loyola-Chicago","Loyola-Chicago");
		NCAABMapping.put("Loyola-Maryland","Loyola-Maryland");
		NCAABMapping.put("LSU","LSU");
		NCAABMapping.put("Maine","Maine");
		NCAABMapping.put("Manhattan","Manhattan");
		NCAABMapping.put("Marist","Marist");
		NCAABMapping.put("Marquette","Marquette");
		NCAABMapping.put("Marshall","Marshall");
		NCAABMapping.put("Maryland","Maryland");
		NCAABMapping.put("Massachusetts","Massachusetts");
		NCAABMapping.put("McNeese State","McNeese State");
		NCAABMapping.put("Md.-Eastern Shore(UMES)","Maryland-Eastern Shore");
		NCAABMapping.put("Memphis","Memphis");
		NCAABMapping.put("Mercer","Mercer");
		NCAABMapping.put("Miami-Florida","Miami-Florida");
		NCAABMapping.put("Miami-Ohio","Miami-Ohio");
		NCAABMapping.put("Michigan","Michigan");
		NCAABMapping.put("Michigan State","Michigan State");
		NCAABMapping.put("Middle Tennessee","Middle Tennessee State");
		NCAABMapping.put("Milwaukee","Wisconsin-Milwaukee");
		NCAABMapping.put("Minnesota","Minnesota");
		NCAABMapping.put("Mississippi","Mississippi");
		NCAABMapping.put("Mississippi State","Mississippi State");
		NCAABMapping.put("Missouri","Missouri");
		NCAABMapping.put("Missouri State","Missouri State");
		NCAABMapping.put("Monmouth-NJ","Monmouth");
		NCAABMapping.put("Montana","Montana");
		NCAABMapping.put("Montana State","Montana State");
		NCAABMapping.put("Morehead State","Morehead State");
		NCAABMapping.put("Morgan State","Morgan State");
		NCAABMapping.put("Mount St. Mary's","Mount St. Mary's");
		NCAABMapping.put("Murray State","Murray State");
		NCAABMapping.put("MVSU(Miss. Valley St.)","Mississippi Valley State");
		NCAABMapping.put("Navy","Navy");
		NCAABMapping.put("NC Asheville","NC Asheville");
		NCAABMapping.put("NC AT","NC A&T");
		NCAABMapping.put("NC Central","NC Central");
		NCAABMapping.put("NC Greensboro","NC Greensboro");
		NCAABMapping.put("NC State","NC State");
		NCAABMapping.put("NC Wilmington","NC Wilmington");
		NCAABMapping.put("Nebraska","Nebraska");
		NCAABMapping.put("Nevada","Nevada");
		NCAABMapping.put("New Hampshire","New Hampshire");
		NCAABMapping.put("New Mexico","New Mexico");
		NCAABMapping.put("New Mexico State","New Mexico State");
		NCAABMapping.put("New Orleans","New Orleans");
		NCAABMapping.put("Niagara","Niagara");
		NCAABMapping.put("Nicholls State","Nicholls St");
		NCAABMapping.put("NJIT(New Jersey Tech)","NJIT");
		NCAABMapping.put("Norfolk State","Norfolk State");
		NCAABMapping.put("North Carolina","North Carolina");
		NCAABMapping.put("North Alabama","North Alabama");
		NCAABMapping.put("North Dakota","North Dakota");
		NCAABMapping.put("North Dakota State","North Dakota State");
		NCAABMapping.put("North Florida(UNF)","North Florida");
		NCAABMapping.put("North Texas","North Texas");
		NCAABMapping.put("Northeastern","Northeastern");
		NCAABMapping.put("Northern Arizona","Northern Arizona");
		NCAABMapping.put("Northern Colorado","Northern Colorado");
		NCAABMapping.put("Northern Illinois","Northern Illinois");
		NCAABMapping.put("Northern Iowa","Northern Iowa");
		NCAABMapping.put("Northern Kentucky","Northern Kentucky");
		NCAABMapping.put("Northwestern","Northwestern");
		NCAABMapping.put("Northwestern State","Northwestern State");
		NCAABMapping.put("Notre Dame","Notre Dame");
		NCAABMapping.put("Oakland-Mich.","Oakland");
		NCAABMapping.put("Ohio","Ohio");
		NCAABMapping.put("Ohio State","Ohio State");
		NCAABMapping.put("Oklahoma","Oklahoma");
		NCAABMapping.put("Oklahoma State","Oklahoma State");
		NCAABMapping.put("Old Dominion","Old Dominion");
		NCAABMapping.put("Omaha(Neb.-Omaha)","Nebraska Omaha");
		NCAABMapping.put("Oral Roberts","Oral Roberts");
		NCAABMapping.put("Oregon","Oregon");
		NCAABMapping.put("Oregon State","Oregon State");
		NCAABMapping.put("Pacific","Pacific");
		NCAABMapping.put("Penn State","Penn State");
		NCAABMapping.put("Pennsylvania","Pennsylvania");
		NCAABMapping.put("Pepperdine","Pepperdine");
		NCAABMapping.put("Pittsburgh","Pittsburgh");
		NCAABMapping.put("Portland","Portland");
		NCAABMapping.put("Portland State","Portland State");
		NCAABMapping.put("Prairie View AM","Prairie View A&M");
		NCAABMapping.put("Presbyterian College","Presbyterian");
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
		NCAABMapping.put("Sacramento State","Sacramento State");
		NCAABMapping.put("Sacred Heart","Sacred Heart");
		NCAABMapping.put("Saint Francis-Pa.","Saint Francis-Pa.");
		NCAABMapping.put("Saint Joseph's-Pa.","Saint Joseph's-Pa.");
		NCAABMapping.put("Saint Louis","Saint Louis");
		NCAABMapping.put("Saint Mary's-Cal.","Saint Mary's-California");
		NCAABMapping.put("Saint Peter's","Saint Peter's");
		NCAABMapping.put("Sam Houston State","Sam Houston State");
		NCAABMapping.put("Samford","Samford");
		NCAABMapping.put("San Diego","San Diego");
		NCAABMapping.put("San Diego State","San Diego State");
		NCAABMapping.put("San Francisco","San Francisco");
		NCAABMapping.put("San Jose State","San Jose State");
		NCAABMapping.put("Santa Clara","Santa Clara");
		NCAABMapping.put("Savannah State","Savannah State");
		NCAABMapping.put("SC State","South Carolina State");
		NCAABMapping.put("SE Louisiana","SE Louisiana");
		NCAABMapping.put("SE Missouri State(SEMO)","SE Missouri");
		NCAABMapping.put("Seattle","Seattle");
		NCAABMapping.put("Seton Hall","Seton Hall");
		NCAABMapping.put("Siena","Siena");
		NCAABMapping.put("SIU-Edwardsville","SIU-Edwardsville");
		NCAABMapping.put("SMU","SMU");
		NCAABMapping.put("South Alabama","South Alabama");
		NCAABMapping.put("South Carolina","South Carolina");
		NCAABMapping.put("South Dakota","South Dakota");
		NCAABMapping.put("South Dakota State","South Dakota State");
		NCAABMapping.put("South Florida","South Florida");
		NCAABMapping.put("Southern California","USC");
		NCAABMapping.put("Southern Illinois","Southern Illinois");
		NCAABMapping.put("Southern Miss","Southern Miss");
		NCAABMapping.put("Southern U.","Southern U");
		NCAABMapping.put("Southern Utah","Southern Utah");
		NCAABMapping.put("St. Bonaventure","Saint Bonaventure");
		NCAABMapping.put("St. Francis-NY","Saint Francis-NY");
		NCAABMapping.put("St. John's","St. John's");
		NCAABMapping.put("Stanford","Stanford");
		NCAABMapping.put("Stephen F. Austin","Stephen F. Austin");
		NCAABMapping.put("Stetson","Stetson");
		NCAABMapping.put("Stony Brook-NY","Stony Brook");
		NCAABMapping.put("Syracuse","Syracuse");
		NCAABMapping.put("TCU","TCU");
		NCAABMapping.put("Temple","Temple");
		NCAABMapping.put("Tennessee","Tennessee");
		NCAABMapping.put("Tennessee State","Tennessee State");
		NCAABMapping.put("Tennessee Tech","Tennessee Tech");
		NCAABMapping.put("Tennessee-Martin","Tennessee-Martin");
		NCAABMapping.put("Texas","Texas");
		NCAABMapping.put("Texas AM","Texas A&M");
		NCAABMapping.put("Texas AM-CorpusChristi","Texas A&M-CC");
		NCAABMapping.put("Texas Southern","Texas Southern");
		NCAABMapping.put("Texas State","Texas State");
		NCAABMapping.put("Texas Tech","Texas Tech");
		NCAABMapping.put("Texas-Arlington","Texas-Arlington");
		NCAABMapping.put("The Citadel","The Citadel");
		NCAABMapping.put("Toledo","Toledo");
		NCAABMapping.put("Towson","Towson");
		NCAABMapping.put("Troy","Troy");
		NCAABMapping.put("Tulane","Tulane");
		NCAABMapping.put("Tulsa","Tulsa");
		NCAABMapping.put("UAB","UAB");
		NCAABMapping.put("UC Davis","UC Davis");
		NCAABMapping.put("UC Irvine","UC Irvine");
		NCAABMapping.put("UC Riverside","UC Riverside");
		NCAABMapping.put("UC Santa Barbara","UC Santa Barbara");
		NCAABMapping.put("UCLA","UCLA");
		NCAABMapping.put("UMass Lowell","Massachusetts Lowell");
		NCAABMapping.put("UMBC","Maryland BC");
		NCAABMapping.put("UNLV","UNLV");
		NCAABMapping.put("USC Upstate","South Carolina Upstate");
		NCAABMapping.put("Utah","Utah");
		NCAABMapping.put("Utah State","Utah State");
		NCAABMapping.put("Utah Valley","Utah Valley State");
		NCAABMapping.put("UTEP","UTEP");
		NCAABMapping.put("UTRGV","Texas Rio Grande Valley");
		NCAABMapping.put("UTSA","UTSA");
		NCAABMapping.put("Valparaiso","Valparaiso");
		NCAABMapping.put("Vanderbilt","Vanderbilt");
		NCAABMapping.put("VCU(Va. Commonwealth)","VCU");
		NCAABMapping.put("Vermont","Vermont");
		NCAABMapping.put("Villanova","Villanova");
		NCAABMapping.put("Virginia","Virginia");
		NCAABMapping.put("Virginia Tech","Virginia Tech");
		NCAABMapping.put("VMI","VMI");
		NCAABMapping.put("Wagner","Wagner");
		NCAABMapping.put("Wake Forest","Wake Forest");
		NCAABMapping.put("Washington","Washington");
		NCAABMapping.put("Washington State","Washington State");
		NCAABMapping.put("Weber State","Weber State");
		NCAABMapping.put("West Virginia","West Virginia");
		NCAABMapping.put("Western Carolina","Western Carolina");
		NCAABMapping.put("Western Illinois","Western Illinois");
		NCAABMapping.put("Western Kentucky","Western Kentucky");
		NCAABMapping.put("Western Michigan","Western Michigan");
		NCAABMapping.put("Wichita State","Wichita State");
		NCAABMapping.put("William  Mary","William & Mary");
		NCAABMapping.put("Winthrop","Winthrop");
		NCAABMapping.put("Wisconsin","Wisconsin");
		NCAABMapping.put("Wofford","Wofford");
		NCAABMapping.put("Wright State","Wright State");
		NCAABMapping.put("Wyoming","Wyoming");
		NCAABMapping.put("Xavier-Ohio","Xavier");
		NCAABMapping.put("Yale","Yale");
		NCAABMapping.put("Youngstown State","Youngstown State");
		
	}

	/**
	 * Constructor
	 */
	public UsaTodayParser() {
		super();
		LOGGER.info("Entering UsaTodayParser()");
		LOGGER.info("Exiting UsaTodayParser()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<SagarinData> parsSagarinRatings(String xhtml) throws BatchException {
		final List<SagarinData> data = new ArrayList<SagarinData>();
		final Document doc = parseXhtml(xhtml);
		final Elements fonts = doc.select("font");
		boolean start = false;
		SagarinData sd = null;

		for (Element font : fonts) {
			String color = font.attr("color");
			if (color != null && color.equals("#000000")) {
				String fontHtml = font.html();
				if (fontHtml != null && fontHtml.endsWith("=")) {
					fontHtml = fontHtml.replace("=", "").trim();
					fontHtml = fontHtml.replace("&nbsp;", "").trim();
					fontHtml = fontHtml.replace("&amp;", "").trim();
					fontHtml = fontHtml.replace("nbsp", "").trim();
					start = true;
					int index = fontHtml.indexOf(" ");
					if (index != -1) {
						String rank = fontHtml.substring(0, index);
						String team = fontHtml.substring(index + 1).trim();

						sd = new SagarinData();
						sd.setRank(Integer.parseInt(rank));
						
						final Iterator<String> itr = NCAABMapping.keySet().iterator();
						while (itr.hasNext()) {
							final String key = itr.next();
							if (key.equals(team)) {
								sd.setTeam(NCAABMapping.get(key));
							}
						}
					}
				}
			} else if (color != null && color.equals("#9900ff")) {
				if (start) {
					String fontHtml = font.html().trim();
					fontHtml = fontHtml.replace("&nbsp;", "").trim();
					start = false;
					sd.setValue(Float.parseFloat(fontHtml));
					data.add(sd);
				}
			}
		}

		return data;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<SagarinNcaafData> parseNcaafSagarinRatings(String xhtml) throws BatchException {
		final List<SagarinNcaafData> data = new ArrayList<SagarinNcaafData>();
		final Document doc = parseXhtml(xhtml);
		final Elements fonts = doc.select("pre font font font font");
		boolean start = false;
		SagarinNcaafData sd = null;

		for (Element font : fonts) {
			String color = font.attr("color");
			if (color != null && color.equals("#000000")) {
				String fontHtml = font.html();

				if (fontHtml != null && fontHtml.endsWith("=")) {
					sd = new SagarinNcaafData();

					//    1  Alabama              A  =
					fontHtml = fontHtml.replace("=", "").trim();
					fontHtml = fontHtml.replace("&nbsp;", "").trim();
					fontHtml = fontHtml.replace("&amp;", "").trim();
					fontHtml = fontHtml.replace("nbsp", "").trim();
					start = true;
					int index = fontHtml.indexOf(" ");
					if (index != -1) {
						final String rank = fontHtml.substring(0, index).trim();
						sd.setRank(Integer.parseInt(rank));

						fontHtml = fontHtml.substring(index + 1);
						index = fontHtml.lastIndexOf(" ");
						if (index != -1) {
							final String team = fontHtml.substring(0, index).trim();
							final Iterator<String> itr = NCAABMapping.keySet().iterator();
							boolean found = false;
							while (itr.hasNext()) {
								final String key = itr.next();
								if (key.equals(team)) {
									sd.setTeam(NCAABMapping.get(key));
									found = true;
								}
							}

							if (!found) {
								sd.setTeam(team);
							}

							fontHtml = fontHtml.substring(index + 1).trim();
							if (fontHtml.contains("AA")) {
								sd.setIsfbs(false);
							}
						}
					}
				} else if (fontHtml.contains("HOME ADVANTAGE=[")) {
					// <font color="#9900ff">  2.57</font>
					int index = fontHtml.indexOf("ff\">");
					if (index != -1) {
						fontHtml = fontHtml.substring(index + 4);
						index = fontHtml.indexOf("</font>");
						if (index != -1) {
							fontHtml = fontHtml.substring(0, index).replace("&nbsp;", "").trim();
							LOGGER.debug("fontHtml: " + fontHtml);
							try {
								sd.setHomeadvantage(Float.parseFloat(fontHtml));
							} catch (Throwable t) {
								LOGGER.error(t.getMessage(), t);
							}
						}
					}
				} else if (!fontHtml.contains("___") && !fontHtml.contains("College Football ") && !fontHtml.contains("W L T SCHEDL(RANK) VS top 10 | VS top 30")) {
					//     5   0   0   67.98(  44)   0  0  0 |   1  0  0
					fontHtml = fontHtml.replace("|", "");
					fontHtml = fontHtml.replace("&nbsp;", "").trim();
					int index = fontHtml.indexOf(" ");
					if (index != -1) {
						LOGGER.debug("font: " + font);
						final String win = fontHtml.substring(0, index).trim();
						LOGGER.debug("win: " + win);
						sd.setWins(Integer.parseInt(win));
						fontHtml = fontHtml.substring(index + 1).trim();
						index = fontHtml.indexOf(" ");
						if (index != -1) {
							final String loss = fontHtml.substring(0, index).trim();
							sd.setLosses(Integer.parseInt(loss));
							fontHtml = fontHtml.substring(index + 1).trim();
							index = fontHtml.indexOf(" ");
							if (index != -1) {
								final String tie = fontHtml.substring(0, index).trim();
								sd.setTies(Integer.parseInt(tie));

								fontHtml = fontHtml.substring(index + 1).trim();
								index = fontHtml.indexOf(")");
								if (index != -1) {
									final String sos = fontHtml.substring(0, index).trim();
									int lindex = sos.indexOf("(");
									if (lindex != -1) {
										String sosvalue = sos.substring(0, lindex);
										LOGGER.debug("sosvalue: " + sosvalue);
										sd.setSchedulestrength(Float.parseFloat(sosvalue));
										sosvalue = sos.substring(lindex + 1).trim();
										sd.setSchedulerank(Integer.parseInt(sosvalue));
									}
									
									fontHtml = fontHtml.substring(index + 1).trim();
									index = fontHtml.indexOf(" ");
									if (index != -1) {
										final String top10win = fontHtml.substring(0, index).trim();
										sd.setTop10wins(Integer.parseInt(top10win));

										fontHtml = fontHtml.substring(index + 1).trim();
										index = fontHtml.indexOf(" ");
										if (index != -1) {
											final String top10loss = fontHtml.substring(0, index).trim();
											sd.setTop10losses(Integer.parseInt(top10loss));

											fontHtml = fontHtml.substring(index + 1).trim();
											index = fontHtml.indexOf(" ");
											if (index != -1) {
												final String top10tie = fontHtml.substring(0, index).trim();
												sd.setTop10ties(Integer.parseInt(top10tie));
												
												fontHtml = fontHtml.substring(index + 1).trim();
												index = fontHtml.indexOf(" ");
												if (index != -1) {
													final String top30win = fontHtml.substring(0, index).trim();
													sd.setTop30wins(Integer.parseInt(top30win));

													fontHtml = fontHtml.substring(index + 1).trim();
													index = fontHtml.indexOf(" ");
													if (index != -1) {
														final String top30loss = fontHtml.substring(0, index).trim();
														sd.setTop30losses(Integer.parseInt(top30loss));

														fontHtml = fontHtml.substring(index + 1).trim();
														index = fontHtml.indexOf(" ");
														if (index != -1) {
															final String top30tie = fontHtml.substring(0, index).trim();
															sd.setTop30ties(Integer.parseInt(top30tie));
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} else if (color != null && color.equals("#0000ff")) {
				if (start) {
					//   103.02    1 
					String fontHtml = font.html().replace("&nbsp;", "").trim();
					int index = fontHtml.indexOf(" ");
					if (index != -1) {
						final String sdata = fontHtml.substring(0, index).trim();
						sd.setPredictor(Float.parseFloat(sdata));
						fontHtml = fontHtml.substring(index + 1).trim();
						sd.setPredictorrank(Integer.parseInt(fontHtml));
					}
				}
			} else if (color != null && color.equals("#ff0000")) {
				if (start) {
					//   103.02    1 
					String fontHtml = font.html().replace("&nbsp;", "").trim();
					fontHtml = fontHtml.replace("|", "");
					int index = fontHtml.indexOf(" ");
					if (index != -1) {
						final String sdata = fontHtml.substring(0, index).trim();
						sd.setMean(Float.parseFloat(sdata));
						fontHtml = fontHtml.substring(index + 1).trim();
						sd.setMeanrank(Integer.parseInt(fontHtml));
					}
				}
			} else if (color != null && color.equals("#4cc417")) {
				if (start) {
					//   103.02    1 
					String fontHtml = font.html().replace("&nbsp;", "").trim();
					int index = fontHtml.indexOf(" ");
					if (index != -1) {
						final String sdata = fontHtml.substring(0, index).trim();
						sd.setRecent(Float.parseFloat(sdata));
						fontHtml = fontHtml.substring(index + 1).trim();
						sd.setRecentrank(Integer.parseInt(fontHtml));
					}
					start = false;
				}
			} else if (color != null && color.equals("#9900ff")) {
				if (start) {
					String fontHtml = font.html().replace("&nbsp;", "").trim();
					if (!fontHtml.contains("RATING")) {
						fontHtml = fontHtml.replace("&nbsp;", "").replace("&amp;nbsp", "").trim();
						sd.setRating(Float.parseFloat(fontHtml));
						data.add(sd);
					}
				}
			}
		}

		return data;
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
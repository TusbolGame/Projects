/**
 * 
 */
package com.ticketadvantage.services.dao.sites.donbest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class EspnForDonBestParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(EspnForDonBestParser.class);
	private final static DateTimeFormatter inputDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'", Locale.ENGLISH);
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

		NCAAFMapping.put("Houston", "Houston U");
		NCAAFMapping.put("Miami (OH)", "Miami Ohio");
		NCAAFMapping.put("Northern Illinois", "No Illinois");
		NCAAFMapping.put("Ole Miss", "Mississippi");
		NCAAFMapping.put("Mississippi State", "Mississippi St");
		NCAAFMapping.put("Arkansas State", "Arkansas St");
		NCAAFMapping.put("Hawaii", "Hawaii");
		NCAAFMapping.put("North Carolina A&T", "N. Carolina A&amp;T");
		NCAAFMapping.put("Jacksonville State", "Jacksonville St");
		NCAAFMapping.put("Buffalo", "Buffalo U");
		NCAAFMapping.put("Pittsburgh", "Pittsburgh U");
		NCAAFMapping.put("Buffalo", "Buffalo U");
		NCAAFMapping.put("UCF", "Central Florida");
		NCAAFMapping.put("Cincinnati", "Cincinnati U");
		NCAAFMapping.put("Washington", "Washington U");
		NCAAFMapping.put("Minnesota", "Minnesota U");
		NCAAFMapping.put("UMass", "Massachusetts");
		NCAAFMapping.put("UConn", "Connecticut");
		NCAAFMapping.put("Louisiana", "UL Lafayette");
		NCAAFMapping.put("UTSA", "Tex San Antonio");
		NCAAFMapping.put("Miami", "Miami Florida");
		NCAAFMapping.put("Arizona", "Arizona U");
		NCAAFMapping.put("Texas A&M", "Texas A&amp;M");
		NCAAFMapping.put("Appalachian State", "Appalachian St");
		NCAAFMapping.put("Middle Tennessee", "Middle Tenn St");
		NCAAFMapping.put("Tennessee", "Tennessee U");
		NCAAFMapping.put("Southern Mississippi", "So Mississippi");
		NCAAFMapping.put("Florida International", "Florida Intl");
		NCAAFMapping.put("Alabama State", "Alabama St");
		NCAAFMapping.put("Mississippi Valley State", "Miss Valley State");
		NCAAFMapping.put("South Dakota State", "South Dakota St");
		NCAAFMapping.put("Bethune-Cookman", "Bethune Cookman");
		NCAAFMapping.put("Tennessee State", "Tennessee St");
		NCAAFMapping.put("Nicholls", "Nicholls State");
		NCAAFMapping.put("St Francis (PA)", "St. Francis (PA)");
		NCAAFMapping.put("McNeese", "McNeese State");
		NCAAFMapping.put("William & Mary", "William &amp; Mary");
		NCAAFMapping.put("The Citadel", "Citadel");
		NCAAFMapping.put("Gardner-Webb", "Gardner Webb");
		NCAAFMapping.put("South Carolina State", "South Carolina St");
		NCAAFMapping.put("Southeast Missouri State", "SE Missouri St");
		NCAAFMapping.put("Jackson State", "Jackson St");
		NCAAFMapping.put("UT Martin", "Tenn Martin");
		NCAAFMapping.put("Portland State", "Portland St");
		NCAAFMapping.put("Prairie View", "Prairie View A&amp;M");
		NCAAFMapping.put("Sacramento State", "CS Sacramento");
		NCAAFMapping.put("Morehead State", "Morehead St");
		NCAAFMapping.put("Youngstown State", "Youngstown St");
		NCAAFMapping.put("Sam Houston State", "Sam Houston St");
		NCAAFMapping.put("Florida A&M", "Florida A&amp;M");
		NCAAFMapping.put("Alabama A&M", "Alabama A&amp;M");
		NCAAFMapping.put("Southeastern Louisiana", "SE Louisiana");
		NCAAFMapping.put("Presbyterian College", "Presbyterian");
		NCAAFMapping.put("Norfolk State", "Norfolk St");
		NCAAFMapping.put("Central Connecticut", "Central Conn.");
		NCAAFMapping.put("North Carolina Central", "NC Central");
		NCAAFMapping.put("Arkansas-Pine Bluff", "Arkansas Pine Bluff");

		NCAABMapping.put("Abilene Christian", "Abilene Christian");
		NCAABMapping.put("Air Force", "Air Force");
		NCAABMapping.put("Akron","Akron");
		NCAABMapping.put("Alab A&amp;M","Alabama A&M");
		NCAABMapping.put("Alabama","Alabama");
		NCAABMapping.put("Alabama St","Alabama State");
		NCAABMapping.put("Albany","Albany");
		NCAABMapping.put("Alcorn State","Alcorn State");
		NCAABMapping.put("American","American University");
		NCAABMapping.put("Appalachian St.","Appalachian State");
		NCAABMapping.put("Little Rock","Arkansas Little Rock");
		NCAABMapping.put("Arizona","Arizona");
		NCAABMapping.put("Arizona St.","Arizona State");
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
		NCAABMapping.put("Binghamton","Binghamton");
		NCAABMapping.put("Boise State","Boise State");
		NCAABMapping.put("Boston College","Boston College");
		NCAABMapping.put("Boston U.","Boston University");
		NCAABMapping.put("Bowling Grn","Bowling Green");
		NCAABMapping.put("Bradley","Bradley");
		NCAABMapping.put("Brown","Brown");
		NCAABMapping.put("Bryant","Bryant");
		NCAABMapping.put("Bucknell","Bucknell");
		NCAABMapping.put("Buffalo","Buffalo");
		NCAABMapping.put("Butler","Butler");
		NCAABMapping.put("BYU","BYU");
		NCAABMapping.put("Cal Poly","Cal Poly-SLO");
		NCAABMapping.put("CSUN","CS Northridge");
		NCAABMapping.put("California","California");
		NCAABMapping.put("Campbell","Campbell");
		NCAABMapping.put("Canisius","Canisius");
		NCAABMapping.put("Central Ark.","Central Arkansas");
		NCAABMapping.put("Cent. Conn. St.","Central Connecticut State");
		NCAABMapping.put("UCF","Central Florida");
		NCAABMapping.put("Cent. Michigan","Central Michigan");
		NCAABMapping.put("Charleston","Charleston Southern");
		NCAABMapping.put("Charlotte","Charlotte");
		NCAABMapping.put("Chattanooga","Chattanooga");
		NCAABMapping.put("Chicago State","Chicago State");
		NCAABMapping.put("Cincinnati","Cincinnati");
		NCAABMapping.put("Citadel","The Citadel");
		NCAABMapping.put("Clemson","Clemson");
		NCAABMapping.put("Cleveland State","Cleveland State");
		NCAABMapping.put("Coastal Caro.","Coastal Carolina");
		NCAABMapping.put("Col Charlestn","College of Charleston");
		NCAABMapping.put("Colgate","Colgate");
		NCAABMapping.put("Colorado","Colorado");
		NCAABMapping.put("Colorado State","Colorado State");
		NCAABMapping.put("Columbia","Columbia");
		NCAABMapping.put("Connecticut","Connecticut");
		NCAABMapping.put("Coppin State","Coppin State");
		NCAABMapping.put("Cornell","Cornell");
		NCAABMapping.put("Creighton","Creighton");
		NCAABMapping.put("CSU Bakersfield","CS Bakersfield");
		NCAABMapping.put("CS Fullerton","CS Fullerton");
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
		NCAABMapping.put("Eastern Illinois","Eastern Illinois");
		NCAABMapping.put("Eastern Kentucky","Eastern Kentucky");
		NCAABMapping.put("E Michigan","Eastern Michigan");
		NCAABMapping.put("East Tenn. St.","East Tennessee State");
		NCAABMapping.put("Eastern Wash.","Eastern Washington");
		NCAABMapping.put("Elon","Elon");
		NCAABMapping.put("Evansville","Evansville");
		NCAABMapping.put("F Dickinson","Fairleigh Dickinson");
		NCAABMapping.put("Fairfield","Fairfield");
		NCAABMapping.put("Florida Atlantic","Florida Atlantic");
		NCAABMapping.put("FGCU","Florida Gulf Coast");
		NCAABMapping.put("Florida","Florida");
		NCAABMapping.put("Florida A&amp;M","Florida A&M");
		NCAABMapping.put("FIU","Florida International");
		NCAABMapping.put("Florida State","Florida State");
		NCAABMapping.put("Fordham","Fordham");
		NCAABMapping.put("Fresno State","Fresno State");
		NCAABMapping.put("Furman","Furman");
		NCAABMapping.put("Ga. Southern","Georgia Southern");
		NCAABMapping.put("Georgia Tech","Georgia Tech");
		NCAABMapping.put("Gardner-Webb","Gardner-Webb");
		NCAABMapping.put("George Mason","George Mason");
		NCAABMapping.put("Geo. Washington","George Washington");
		NCAABMapping.put("Georgetown","Georgetown");
		NCAABMapping.put("Georgia","Georgia");
		NCAABMapping.put("Georgia State","Georgia State");
		NCAABMapping.put("Gonzaga","Gonzaga");
		NCAABMapping.put("Grambling St","Grambling State");
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
		NCAABMapping.put("Idaho State","Idaho State");
		NCAABMapping.put("Illinois-Chicago","Illinois-Chicago");
		NCAABMapping.put("Illinois","Illinois");
		NCAABMapping.put("Illinois State","Illinois State");
		NCAABMapping.put("Incarnate Word","Incarnate Word");
		NCAABMapping.put("Indiana","Indiana");
		NCAABMapping.put("Indiana St","Indiana State");
		NCAABMapping.put("Iona","Iona");
		NCAABMapping.put("Iowa","Iowa");
		NCAABMapping.put("Iowa State","Iowa State");
		NCAABMapping.put("IPFW","Fort Wayne(IPFW)");
		NCAABMapping.put("IUPUI","IUPUI");
		NCAABMapping.put("Jackson State","Jackson State");
		NCAABMapping.put("Jacksonville","Jacksonville");
		NCAABMapping.put("James Madison","James Madison");
		NCAABMapping.put("Jacksonville St.","Jacksonville State");
		NCAABMapping.put("Kansas","Kansas");
		NCAABMapping.put("Kansas St.","Kansas State");
		NCAABMapping.put("Kennesaw State","Kennesaw State");
		NCAABMapping.put("Kent State","Kent State");
		NCAABMapping.put("Kentucky","Kentucky");
		NCAABMapping.put("Louisiana","Louisiana-Lafayette");
		NCAABMapping.put("La.-Monroe","Louisiana-Monroe");
		NCAABMapping.put("La Salle","La Salle");
		NCAABMapping.put("LA Tech","Louisiana Tech");
		NCAABMapping.put("Lafayette","Lafayette");
		NCAABMapping.put("Lamar University","Lamar");
		NCAABMapping.put("Lehigh","Lehigh");
		NCAABMapping.put("Lg Beach St","Long Beach State");
		NCAABMapping.put("Liberty","Liberty");
		NCAABMapping.put("Lipscomb","Lipscomb");
		NCAABMapping.put("LIU Brooklyn","Long Island University");
		NCAABMapping.put("Longwood","Longwood");
		NCAABMapping.put("Louisville","Louisville");
		NCAABMapping.put("Loyola Marym't","Loyola Marymount");
		NCAABMapping.put("Loyola-Chi","Loyola-Chicago");
		NCAABMapping.put("Loyola-MD","Loyola-Maryland");
		NCAABMapping.put("LSU","LSU");
		NCAABMapping.put("Maine","Maine");
		NCAABMapping.put("Manhattan","Manhattan");
		NCAABMapping.put("Marist","Marist");
		NCAABMapping.put("Marquette","Marquette");
		NCAABMapping.put("Marshall","Marshall");
		NCAABMapping.put("Maryland","Maryland");
		NCAABMapping.put("UMBC","Maryland BC");
		NCAABMapping.put("Md.-East. Shore","Maryland-Eastern Shore");
		NCAABMapping.put("Mass Lowell","Massachusetts Lowell");
		NCAABMapping.put("McNeese","McNeese State");
		NCAABMapping.put("Memphis","Memphis");
		NCAABMapping.put("Mercer","Mercer");
		NCAABMapping.put("Miami (FL)","Miami-Florida");
		NCAABMapping.put("Miami (Ohio)","Miami-Ohio");
		NCAABMapping.put("Michigan","Michigan");
		NCAABMapping.put("Mich. St.","Michigan State");
		NCAABMapping.put("Middle Tenn","Middle Tennessee State");
		NCAABMapping.put("Minnesota","Minnesota");
		NCAABMapping.put("Mississippi St.","Mississippi State");
		NCAABMapping.put("Miss. Valley St.","Mississippi Valley State");
		NCAABMapping.put("Mississippi","Mississippi");
		NCAABMapping.put("Missouri","Missouri");
		NCAABMapping.put("Missouri State","Missouri State");
		NCAABMapping.put("Monmouth","Monmouth");
		NCAABMapping.put("Montana","Montana");
		NCAABMapping.put("Montana St.","Montana State");
		NCAABMapping.put("Morehead State","Morehead State");
		NCAABMapping.put("Morgan St","Morgan State");
		NCAABMapping.put("Mt. St. Mary's","Mount St. Mary's");
		NCAABMapping.put("Murray State","Murray State");
		NCAABMapping.put("Northern Ariz.","Northern Arizona");
		NCAABMapping.put("North Carolina","North Carolina");
		NCAABMapping.put("Northern Colo.","Northern Colorado");
		NCAABMapping.put("North Dakota St.","North Dakota State");
		NCAABMapping.put("North Florida","North Florida");
		NCAABMapping.put("N Hampshire","New Hampshire");
		NCAABMapping.put("Northern Ill.","Northern Illinois");
		NCAABMapping.put("N Iowa","Northern Iowa");
		NCAABMapping.put("Northern Kentucky","Northern Kentucky");
		NCAABMapping.put("N Mex State","New Mexico State");
		NCAABMapping.put("Navy","Navy");
		NCAABMapping.put("N.C. A&amp;T","NC A&T");
		NCAABMapping.put("NC Central","NC Central");
		NCAABMapping.put("NC State","NC State");
		NCAABMapping.put("UNC Asheville","NC Asheville");
		NCAABMapping.put("UNC-Greensboro","NC Greensboro");
		NCAABMapping.put("UNCW","NC Wilmington");
		NCAABMapping.put("Omaha","Nebraska Omaha");
		NCAABMapping.put("Nebraska","Nebraska");
		NCAABMapping.put("Nevada","Nevada");
		NCAABMapping.put("New Mexico","New Mexico");
		NCAABMapping.put("New Orleans","New Orleans");
		NCAABMapping.put("Niagara","Niagara");
		NCAABMapping.put("Nicholls State","Nicholls St");
		NCAABMapping.put("NJIT","NJIT");
		NCAABMapping.put("Norfolk State","Norfolk State");
		NCAABMapping.put("North Dakota","North Dakota");
		NCAABMapping.put("North Texas","North Texas");
		NCAABMapping.put("Northeastern","Northeastern");
		NCAABMapping.put("Northwestern","Northwestern");
		NCAABMapping.put("Notre Dame","Notre Dame");
		NCAABMapping.put("N'western St.","Northwestern State");
		NCAABMapping.put("Oakland","Oakland");
		NCAABMapping.put("Ohio","Ohio");
		NCAABMapping.put("Ohio St.","Ohio State");
		NCAABMapping.put("Oklahoma","Oklahoma");
		NCAABMapping.put("Oklahoma State","Oklahoma State");
		NCAABMapping.put("Old Dominion","Old Dominion");
		NCAABMapping.put("Oral Roberts","Oral Roberts");
		NCAABMapping.put("Oregon","Oregon");
		NCAABMapping.put("Oregon St","Oregon State");
		NCAABMapping.put("Pacific","Pacific");
		NCAABMapping.put("Penn St.","Penn State");
		NCAABMapping.put("Pepperdine","Pepperdine");
		NCAABMapping.put("Pittsburgh","Pittsburgh");
		NCAABMapping.put("Portland","Portland");
		NCAABMapping.put("Portland St","Portland State");
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
		NCAABMapping.put("S Alabama","South Alabama");
		NCAABMapping.put("S. Carolina St.","South Carolina State");
		NCAABMapping.put("South Carolina","South Carolina");
		NCAABMapping.put("South Dakota St.","South Dakota State");
		NCAABMapping.put("South Florida","South Florida");
		NCAABMapping.put("Southern Ill.","Southern Illinois");
		NCAABMapping.put("SMU","SMU");
		NCAABMapping.put("S Mississippi","Southern Miss");
		NCAABMapping.put("Southern Utah","Southern Utah");
		NCAABMapping.put("Sacramento St.","Sacramento State");
		NCAABMapping.put("Sacred Hrt","Sacred Heart");
		NCAABMapping.put("Saint Louis","Saint Louis");
		NCAABMapping.put("Sam Houston St.","Sam Houston State");
		NCAABMapping.put("Samford","Samford");
		NCAABMapping.put("San Diego","San Diego");
		NCAABMapping.put("San Diego St","San Diego State");
		NCAABMapping.put("San Francisco","San Francisco");
		NCAABMapping.put("San Jose State","San Jose State");
		NCAABMapping.put("Santa Clara","Santa Clara");
		NCAABMapping.put("Savannah State","Savannah State");
		NCAABMapping.put("S.C. Upstate","South Carolina Upstate");
		NCAABMapping.put("Southeastern La.","SE Louisiana");
		NCAABMapping.put("Southeast Mo. St.","SE Missouri");
		NCAABMapping.put("Seattle U","Seattle");
		NCAABMapping.put("Seton Hall","Seton Hall");
		NCAABMapping.put("Siena","Siena");
		NCAABMapping.put("SIU Edward","SIU-Edwardsville");
		NCAABMapping.put("South Dakota","South Dakota");
		NCAABMapping.put("Southern Univ.","Southern U");
		NCAABMapping.put("St. Bonaventure","Saint Bonaventure");
		NCAABMapping.put("St Fran (NY)","Saint Francis-NY");
		NCAABMapping.put("St. Francis (Pa.)","Saint Francis-Pa.");
		NCAABMapping.put("St. John's","St. John's");
		NCAABMapping.put("Saint Joseph's","Saint Joseph's-Pa.");
		NCAABMapping.put("St. Mary's (Cal.)","Saint Mary's-California");
		NCAABMapping.put("St. Peter's","Saint Peter's");
		NCAABMapping.put("Stanford","Stanford");
		NCAABMapping.put("Steph. F. Austin","Stephen F. Austin");
		NCAABMapping.put("Stetson","Stetson");
		NCAABMapping.put("Stony Brook","Stony Brook");
		NCAABMapping.put("Syracuse","Syracuse");
		NCAABMapping.put("Temple","Temple");
		NCAABMapping.put("Tennessee","Tennessee");
		NCAABMapping.put("Texas","Texas");
		NCAABMapping.put("Texas A&amp;M","Texas A&M");
		NCAABMapping.put("Texas State","Texas State");
		NCAABMapping.put("Texas Tech","Texas Tech");
		NCAABMapping.put("Tennessee-Martin","Tennessee-Martin");
		NCAABMapping.put("Tennessee State","Tennessee State");
		NCAABMapping.put("Tennessee Tech","Tennessee Tech");
		NCAABMapping.put("Toledo","Toledo");
		NCAABMapping.put("Towson","Towson");
		NCAABMapping.put("Troy","Troy");
		NCAABMapping.put("Tulane","Tulane");
		NCAABMapping.put("Tulsa","Tulsa");
		NCAABMapping.put("Texas A&amp;M-C.C.","Texas A&M-CC");
		NCAABMapping.put("TCU","TCU");
		NCAABMapping.put("UTEP","UTEP");
		NCAABMapping.put("Texas Southern","Texas Southern");
		NCAABMapping.put("Texas-Arlington","Texas-Arlington");
		NCAABMapping.put("Rio Grande","Texas Rio Grande Valley");
		NCAABMapping.put("UTSA","UTSA");
		NCAABMapping.put("Massachusetts","Massachusetts");
		NCAABMapping.put("U Penn","Pennsylvania");
		NCAABMapping.put("UAB","UAB");
		NCAABMapping.put("UC Davis","UC Davis");
		NCAABMapping.put("UC Irvine","UC Irvine");
		NCAABMapping.put("UC Riverside","UC Riverside");
		NCAABMapping.put("UCLA","UCLA");
		NCAABMapping.put("UC Santa Barb.","UC Santa Barbara");
		NCAABMapping.put("UMKC","UMKC");
		NCAABMapping.put("UNLV","UNLV");
		NCAABMapping.put("USC","USC");
		NCAABMapping.put("Utah","Utah");
		NCAABMapping.put("Utah State","Utah State");
		NCAABMapping.put("Utah Valley","Utah Valley State");
		NCAABMapping.put("VA Military","VMI");
		NCAABMapping.put("Virginia Tech","Virginia Tech");
		NCAABMapping.put("Valparaiso","Valparaiso");
		NCAABMapping.put("Vanderbilt","Vanderbilt");
		NCAABMapping.put("VCU","VCU");
		NCAABMapping.put("Vermont","Vermont");
		NCAABMapping.put("Villanova","Villanova");
		NCAABMapping.put("Virginia","Virginia");
		NCAABMapping.put("Western Caro.","Western Carolina");
		NCAABMapping.put("Western Illinois","Western Illinois");
		NCAABMapping.put("Western Ky.","Western Kentucky");
		NCAABMapping.put("Western Mich.","Western Michigan");
		NCAABMapping.put("West Virginia","West Virginia");
		NCAABMapping.put("Wagner","Wagner");
		NCAABMapping.put("Wake Forest","Wake Forest");
		NCAABMapping.put("Washington St.","Washington State");
		NCAABMapping.put("Washington","Washington");
		NCAABMapping.put("Weber State","Weber State");
		NCAABMapping.put("Green Bay","Wisconsin Green Bay");
		NCAABMapping.put("WI-Milwkee","Wisconsin-Milwaukee");
		NCAABMapping.put("Wichita State","Wichita State");
		NCAABMapping.put("Winthrop","Winthrop");
		NCAABMapping.put("Wisconsin","Wisconsin");
		NCAABMapping.put("William &amp; Mary","William & Mary");
		NCAABMapping.put("Wofford","Wofford");
		NCAABMapping.put("Wright State","Wright State");
		NCAABMapping.put("Wyoming","Wyoming");
		NCAABMapping.put("Xavier","Xavier");
		NCAABMapping.put("Yale","Yale");
		NCAABMapping.put("Youngstown St.","Youngstown State");
	}

	/**
	 * Constructor
	 */
	public EspnForDonBestParser() {
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
			final LocalDateTime localdatetime = LocalDateTime.parse("2018-09-07T00:55Z", inputDateTimeFormatter);
			LOGGER.error("localdatetime: " + localdatetime);
			
			final EspnForDonBestParser dbp = new EspnForDonBestParser();
			String xhtml = "";
			final List<EspnForDonBestData> efdbds = dbp.parseFootballData(true, xhtml);

			for (EspnForDonBestData efdbd : efdbds) {
				LOGGER.error("EspnForDonBestData: " + efdbd);
			}
		} catch (Throwable t) {
			LOGGER.error("Error parsing date", t);
		}
	}

	/**
	 * 
	 * @param isncaaf
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<EspnForDonBestData> parseFootballData(boolean isncaaf, String xhtml) throws BatchException {
		LOGGER.info("Entering parseFootballData()");
		final List<EspnForDonBestData> efdbdList = new ArrayList<EspnForDonBestData>();
		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray events = obj.getJSONArray("events");
		final JSONObject week = obj.getJSONObject("week");
		int weeknumber = week.getInt("number");
		LOGGER.debug("events.length(): " + events.length());
		final JSONObject season = obj.getJSONObject("season");
		int year = season.getInt("year");

		if (events != null && !events.isNull(0)) {
			for (int x = 0; x < events.length(); x++) {
				final JSONObject event = events.getJSONObject(x);
				final EspnForDonBestData efdbd = new EspnForDonBestData();
				efdbd.setWeek(weeknumber);
				efdbd.setYear(year);
				final JSONArray competitions = event.getJSONArray("competitions");

				if (competitions != null && !competitions.isNull(0)) {
					for (int y = 0; y < competitions.length(); y++) {
						final JSONObject competition = competitions.getJSONObject(y);
						// 2018-08-25T21:30Z
						final String date = competition.getString("date");
//						LOGGER.error("date: " + date);

						final ZoneId arrivingZone = ZoneId.of("America/Los_Angeles");
						final LocalDateTime localdatetime = LocalDateTime.parse(date, inputDateTimeFormatter);
						final ZonedDateTime utcZoned = localdatetime.atZone(ZoneId.of("Greenwich"));
//						final ZonedDateTime laDateTime = utcZoned.withZoneSameInstant(arrivingZone);
						final LocalDate localDate = localdatetime.toLocalDate();
						final LocalTime localTime = localdatetime.toLocalTime();
						final LocalDateTime localDateTime = utcZoned.toLocalDateTime();
						efdbd.setDateofgame(localdatetime);
						efdbd.setDate(localDate);
						efdbd.setTime(localTime);

						final JSONArray competitors = competition.getJSONArray("competitors");

						if (competitors != null && !competitors.isNull(0)) {
							for (int z = 0; z < competitors.length(); z++) {
								final JSONObject competitor = competitors.getJSONObject(z);
								final String homeAway = competitor.getString("homeAway");
								final JSONObject team = competitor.getJSONObject("team");
								final String location = team.getString("location").replace("&#39;", "");

								if (homeAway.endsWith("home")) {
									efdbd.setHometeam(location);

									if (isncaaf) {
										final Iterator<String> itr = NCAAFMapping.keySet().iterator();									
										while (itr.hasNext()) {
											final String key = itr.next();
											if (key.toUpperCase().equals(location.toUpperCase())) {
												efdbd.setHometeam(NCAAFMapping.get(key));
											}
										}
									} else {
										if (location.equals("Los Angeles")) {
											final String name = team.getString("name").replace("&#39;", "");
											efdbd.setHometeam("LA " + name);
										} else if (location.equals("New York")) {
											final String name = team.getString("name").replace("&#39;", "");
											efdbd.setHometeam("NY " + name);											
										}
									}
								} else {
									efdbd.setVisitorteam(location);

									if (isncaaf) {
										final Iterator<String> itr = NCAAFMapping.keySet().iterator();
										while (itr.hasNext()) {
											final String key = itr.next();
											if (key.toUpperCase().equals(location.toUpperCase())) {
												efdbd.setVisitorteam(NCAAFMapping.get(key));
											}
										}
									} else {
										if (location.equals("Los Angeles")) {
											final String name = team.getString("name").replace("&#39;", "");
											efdbd.setVisitorteam("LA " + name);
										} else if (location.equals("New York")) {
											final String name = team.getString("name").replace("&#39;", "");
											efdbd.setVisitorteam("NY " + name);											
										}
									}
								}

								if (competitor.has("linescores")) {
									final JSONArray linescores = competitor.getJSONArray("linescores");
	
									if (linescores != null && !linescores.isNull(0)) {
										int firstscore = 0;
										int secondscore = 0;
										int gamescore = 0;
	
										for (int a = 0; a < linescores.length(); a++) {
											final JSONObject linescore = linescores.getJSONObject(a);
											int value = linescore.getInt("value");
	
											if (a == 0) {
												firstscore = value;
											} else if (a == 1) {
												firstscore += value;
											} else if (a == 2) {
												secondscore = value;
											} else {
												secondscore += value;
											}
										}
	
										gamescore = firstscore + secondscore;
										if (homeAway.endsWith("home")) {
											efdbd.setHome1hscore(firstscore);
											efdbd.setHome2hscore(secondscore);
											efdbd.setHomegamescore(gamescore);
										} else {
											efdbd.setVisitor1hscore(firstscore);
											efdbd.setVisitor2hscore(secondscore);
											efdbd.setVisitorgamescore(gamescore);
										}
									}
								}
							}
						}
					}
				}

				efdbdList.add(efdbd);
			}
		}

		LOGGER.info("Exiting parseFootballData()");
		return efdbdList;
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
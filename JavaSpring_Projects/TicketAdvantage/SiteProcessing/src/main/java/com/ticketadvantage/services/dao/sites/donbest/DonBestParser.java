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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.linemovement.dto.MovementData;
import com.linemovement.entity.LineMovement;
import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class DonBestParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(DonBestParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
	private static final DateTimeFormatter GAME_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ssa");
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

		NCAAFMapping.put("Air Force", "Air Force");
		NCAAFMapping.put("Akron", "Akron");
		NCAAFMapping.put("Alabama", "Alabama");
		NCAAFMapping.put("Appalachian State", "Appalachian State");
		NCAAFMapping.put("Arizona", "Arizona");
		NCAAFMapping.put("Arizona State", "Arizona State");
		NCAAFMapping.put("Arkansas", "Arkansas");
		NCAAFMapping.put("Arkansas State", "Arkansas State");
		NCAAFMapping.put("Army", "Army");
		NCAAFMapping.put("Auburn", "Auburn");
		NCAAFMapping.put("Ball State", "Ball State");
		NCAAFMapping.put("Baylor", "Baylor");
		NCAAFMapping.put("Boise State", "Boise State");
		NCAAFMapping.put("Boston College", "Boston College");
		NCAAFMapping.put("Bowling Green", "Bowling Green");
		NCAAFMapping.put("Buffalo", "Buffalo");
		NCAAFMapping.put("BYU", "BYU");
		NCAAFMapping.put("California", "California");
		NCAAFMapping.put("Central Michigan", "Central Michigan");
		NCAAFMapping.put("Charlotte", "Charlotte");
		NCAAFMapping.put("Cincinnati", "Cincinnati");
		NCAAFMapping.put("Clemson", "Clemson");
		NCAAFMapping.put("Coastal Carolina", "Coastal Carolina");
		NCAAFMapping.put("Colorado", "Colorado");
		NCAAFMapping.put("Colorado State", "Colorado State");
		NCAAFMapping.put("UConn", "Connecticut");
		NCAAFMapping.put("Duke", "Duke");
		NCAAFMapping.put("East Carolina", "East Carolina");
		NCAAFMapping.put("EAST TENNESSEE ST.", "EAST TENNESSEE STATE");
		NCAAFMapping.put("Eastern Michigan", "Eastern Michigan");
		NCAAFMapping.put("Florida Atlantic", "Florida Atlantic");
		NCAAFMapping.put("Florida", "Florida");
		NCAAFMapping.put("Florida Intl", "Florida Intl");
		NCAAFMapping.put("Florida State", "Florida State");
		NCAAFMapping.put("Fresno State", "Fresno State");
		NCAAFMapping.put("Georgia Southern", "Georgia Southern");
		NCAAFMapping.put("Georgia Tech", "Georgia Tech");
		NCAAFMapping.put("Georgia", "Georgia");
		NCAAFMapping.put("Georgia State", "Georgia State");
		NCAAFMapping.put("GRAMBLING STATE", "GRAMBLING"); 
		NCAAFMapping.put("Hawai'i", "Hawai'i");
		NCAAFMapping.put("Houston", "Houston");
		NCAAFMapping.put("Illinois", "Illinois");
		NCAAFMapping.put("Indiana", "Indiana");
		NCAAFMapping.put("Iowa", "Iowa");
		NCAAFMapping.put("Iowa State", "Iowa State");
		NCAAFMapping.put("Kansas", "Kansas");
		NCAAFMapping.put("Kansas State", "Kansas State");
		NCAAFMapping.put("Kent State", "Kent State");
		NCAAFMapping.put("Kentucky", "Kentucky");
		NCAAFMapping.put("Liberty", "Liberty");
		NCAAFMapping.put("Louisiana", "Louisiana-Lafayette");
		NCAAFMapping.put("UL Monroe", "Louisiana-Monroe");
		NCAAFMapping.put("Louisiana Monroe", "Louisiana-Monroe");
		NCAAFMapping.put("Louisiana Tech", "Louisiana Tech");
		NCAAFMapping.put("Louisville", "Louisville");
		NCAAFMapping.put("LSU", "LSU");
		NCAAFMapping.put("Marshall", "Marshall");
		NCAAFMapping.put("Maryland", "Maryland");
		NCAAFMapping.put("MCNEESE ST", "MCNEESE");
		NCAAFMapping.put("MCNEESE STATE", "MCNEESE");
		NCAAFMapping.put("Memphis", "Memphis");
		NCAAFMapping.put("Miami", "Miami-Florida");
		NCAAFMapping.put("Miami (OH)", "Miami-Ohio");
		NCAAFMapping.put("Michigan", "Michigan");
		NCAAFMapping.put("Michigan State", "Michigan State");
		NCAAFMapping.put("Middle Tennessee", "Middle Tennessee");
		NCAAFMapping.put("Minnesota", "Minnesota");
		NCAAFMapping.put("Mississippi State", "Mississippi State");
		NCAAFMapping.put("Ole Miss", "Mississippi");
		NCAAFMapping.put("Missouri", "Missouri");
		NCAAFMapping.put("NICHOLLS", "NICHOLLS STATE");
		NCAAFMapping.put("Northern Illinois", "Northern Illinois");
		NCAAFMapping.put("North Carolina", "North Carolina");
		NCAAFMapping.put("NORTH CAROLINA A&AMP;T", "NORTH CAROLINA AT");
		NCAAFMapping.put("Navy", "Navy");
		NCAAFMapping.put("NC State", "North Carolina State");
		NCAAFMapping.put("Nebraska", "Nebraska");
		NCAAFMapping.put("Nevada", "Nevada");
		NCAAFMapping.put("New Mexico", "New Mexico");
		NCAAFMapping.put("New Mexico State", "New Mexico State");
		NCAAFMapping.put("North Texas", "North Texas");
		NCAAFMapping.put("Northwestern", "Northwestern");
		NCAAFMapping.put("Notre Dame", "Notre Dame");
		NCAAFMapping.put("Ohio", "Ohio");
		NCAAFMapping.put("Ohio State", "Ohio State");
		NCAAFMapping.put("Oklahoma", "Oklahoma");
		NCAAFMapping.put("Oklahoma State", "Oklahoma State");
		NCAAFMapping.put("Old Dominion", "Old Dominion");
		NCAAFMapping.put("Oregon", "Oregon");
		NCAAFMapping.put("Oregon State", "Oregon State");
		NCAAFMapping.put("Penn State", "Penn State");
		NCAAFMapping.put("PENN", "PENNSYLVANIA");
		NCAAFMapping.put("Pittsburgh", "Pittsburgh");
		NCAAFMapping.put("PRAIRIE VIEW A&AMP;M", "PRAIRIE VIEW AM");
		NCAAFMapping.put("PRAIRIE VIEW", "PRAIRIE VIEW AM");
		NCAAFMapping.put("Purdue", "Purdue");
		NCAAFMapping.put("Rice", "Rice");
		NCAAFMapping.put("Rutgers", "Rutgers");
		NCAAFMapping.put("San Diego State", "San Diego State");
		NCAAFMapping.put("South Alabama", "South Alabama");
		NCAAFMapping.put("South Carolina", "South Carolina");
		NCAAFMapping.put("South Florida", "South Florida");
		NCAAFMapping.put("SMU", "SMU");
		NCAAFMapping.put("Southern Mississippi", "Southern Miss");
		NCAAFMapping.put("San Jose State", "San Jose State");
		NCAAFMapping.put("Stanford", "Stanford");
		NCAAFMapping.put("STEPHEN F. AUSTIN","STEPHEN F AUSTIN");
		NCAAFMapping.put("Syracuse", "Syracuse");
		NCAAFMapping.put("TCU", "TCU");
		NCAAFMapping.put("Temple", "Temple");
		NCAAFMapping.put("Tennessee", "Tennessee");
		NCAAFMapping.put("TENNESSEE-MARTIN", "UT MARTIN");
		NCAAFMapping.put("Texas", "Texas");
		NCAAFMapping.put("Texas A&AMP;M", "Texas AM");
		NCAAFMapping.put("Texas State", "Texas State");
		NCAAFMapping.put("Texas Tech", "Texas Tech");
		NCAAFMapping.put("Toledo", "Toledo");
		NCAAFMapping.put("Troy", "Troy");
		NCAAFMapping.put("Tulane", "Tulane");
		NCAAFMapping.put("Tulsa", "Tulsa");
		NCAAFMapping.put("UTEP", "UTEP");
		NCAAFMapping.put("UTSA", "UTSA");
		NCAAFMapping.put("UT SAN ANTONIO", "UTSA");
		NCAAFMapping.put("Umass", "Umass");
		NCAAFMapping.put("UAB", "UAB");
		NCAAFMapping.put("UCLA", "UCLA");
		NCAAFMapping.put("UCF", "UCF");
		NCAAFMapping.put("UNLV", "UNLV");
		NCAAFMapping.put("USC", "USC");
		NCAAFMapping.put("Utah", "Utah");
		NCAAFMapping.put("Utah State", "Utah State");
		NCAAFMapping.put("Virginia Tech", "Virginia Tech");
		NCAAFMapping.put("Vanderbilt", "Vanderbilt");
		NCAAFMapping.put("Virginia", "Virginia");
		NCAAFMapping.put("Western Kentucky", "Western Kentucky");
		NCAAFMapping.put("Western Michigan", "Western Michigan");
		NCAAFMapping.put("West Virginia", "West Virginia");
		NCAAFMapping.put("Wake Forest", "Wake Forest");
		NCAAFMapping.put("Washington State", "Washington State");
		NCAAFMapping.put("Washington", "Washington");
		NCAAFMapping.put("Wisconsin", "Wisconsin");
		NCAAFMapping.put("Wyoming", "Wyoming");
		NCAAFMapping.put("Arkansas-Pine Bluff", "Arkansas-Pine Bluff");

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
	public DonBestParser() {
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
			final ZoneId arrivingZone = ZoneId.of("America/Los_Angeles");
			final LocalDateTime utcDateTime = LocalDateTime.parse(("09/06/18"  + " " + "10:51:30PM"), GAME_DATE_FORMAT); 
			final ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
			final ZonedDateTime laDateTime = utcZoned.withZoneSameInstant(arrivingZone);
			final LocalDate localDate = laDateTime.toLocalDate();
			final LocalTime localTime = laDateTime.toLocalTime();
			LOGGER.error("localDate: " + localDate.toString());
			LOGGER.error("localTime: " + localTime.toString());

/*
			final DonBestParser dbp = new DonBestParser();
			String xhtml = "";
			DonBestData dbd = dbp.parseFootballData(true, 11, "ncaaf", 0, "Game", xhtml);

			for (LineMovement lm : dbd.getLines()) {
				LOGGER.error("LineMovement: " + lm);
			}
*/
		} catch (Throwable t) {
			LOGGER.error("Error parsing date", t);
		}
	}

	/**
	 * 
	 * @param isMl
	 * @param sportNumber
	 * @param sportType
	 * @param gameNumber
	 * @param gameType
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public DonBestData parseFootballData(boolean isMl, 
			int sportNumber, 
			String sportType,
			int gameNumber,
			String gameType,
			String xhtml) throws BatchException {
		LOGGER.info("Entering parseFootballData()");
		final Document doc = parseXhtml(xhtml);
		final Elements tables = doc.select(".Lhead2");
		final DonBestData dbd = new DonBestData();

		for (int x = 0; x < tables.size(); x++) {
			// Get the date
			if (x == 0) {
				final Element table = tables.get(x);
				getDate(table, dbd);
			} else {
				// Get book data
				final Element table = tables.get(x);
				getBookData(isMl, sportNumber, sportType, gameNumber, gameType, table, dbd) ;
			}
		}

		LOGGER.info("Exiting parseFootballData()");
		return dbd;
	}

	/**
	 * 
	 * @param isMl
	 * @param sportNumber
	 * @param sportType
	 * @param gameNumber
	 * @param gameType
	 * @param table
	 * @param dbd
	 */
	private void getBookData(boolean isMl, 
			int sportNumber, 
			String sportType, 
			int gameNumber, 
			String gameType, 
			Element table, 
			DonBestData dbd) {
		LOGGER.info("Entering getBookData()");
		LOGGER.debug("isMl: " + isMl);
		final Elements th = table.select("tbody tr th");
		// Book: Mirage - Game - All lines
		String book = "";

		if (th.size() > 0) {
			String html = th.get(0).html();
			LOGGER.debug("html: " + html);
			int ndex = html.indexOf("Book: ");

			if (ndex != -1) {
				html = html.substring(ndex + "Book: ".length());
				ndex = html.indexOf(" - ");

				if (ndex != -1) {
					book = html.substring(0, ndex).trim();

					if (book != null && book.length() > 0) {
						if (book.equals("Sports411")) {
							book = "BOOKMAKER LINE MOVEMENTS";
						} else if (book.equals("Mirage")) {
							book = "CAESARS/HARRAH'S LINE MOVEMENTS";
						} else if (book.equals("Pinnacle")) {
							book = "PINNACLESPORTS LINE MOVEMENTS";
						} else if (book.equals("Westgate")) {
							book = "WESTGATE SUPERBOOK LINE MOVEMENTS";
						}
					}
				}
			}
		}
		LOGGER.debug("Book: " + book);

		final Elements trs = table.select("tbody tr");
		LineMovement lm = null;
		int counter = 0;

		for (int x = 0; x < trs.size(); x++) {
			final Element tr = trs.get(x);

			switch (x) {
				case 0:
					break;
				case 1:
					{
						final Elements ths = tr.select("th");

						if (ths.size() > 0) {
							String html = ths.get(0).html();

							if (html != null && isMl && !html.contains("TOTALS")) {
								// LineMovement
								lm = new LineMovement();
								lm.setSportsbook(book);
								lm.setSportNumber(sportNumber);
								lm.setSportType(sportType);
								lm.setGameNumber(gameNumber);
								lm.setGameType(gameType);
								lm.setSiteName("DonBest");
							} else if (html != null && !isMl && !html.contains("TOTALS")) {
								// LineMovement
								lm = new LineMovement();
								lm.setSportsbook(book);
								lm.setSportNumber(sportNumber);
								lm.setSportType(sportType);
								lm.setGameNumber(gameNumber);
								lm.setGameType(gameType);
								lm.setSiteName("DonBest");
							} else if (html != null && !isMl && html.contains("TOTALS")) {
								// LineMovement
								lm = new LineMovement();
								lm.setSportsbook(book);
								lm.setSportNumber(sportNumber);
								lm.setSportType(sportType);
								lm.setGameNumber(gameNumber);
								lm.setGameType(gameType);
								lm.setSiteName("DonBest");
							}
						}
					}
					break;
				default:
					{
						final Elements ths = tr.select("th");

						if (ths.size() > 0) {
							Element thelement = ths.get(0);
							String html = thelement.html();

							if (html != null && !isMl && html.contains("TOTALS")) {
								addLineMovement(lm, dbd);

								// LineMovement
								lm = new LineMovement();
								lm.setSportsbook(book);
								lm.setSportNumber(sportNumber);
								lm.setSportType(sportType);
								lm.setGameNumber(gameNumber);
								lm.setGameType(gameType);
								lm.setSiteName("DonBest");
							} else if (html != null && isMl && !html.contains("TOTALS")) {
								LOGGER.debug("html: " + html);
								LOGGER.debug("counter: " + ++counter);
								addLineMovement(lm, dbd);

								// LineMovement
								lm = new LineMovement();
								lm.setSportsbook(book);
								lm.setSportNumber(sportNumber);
								lm.setSportType(sportType);
								lm.setGameNumber(gameNumber);
								lm.setGameType(gameType);
								lm.setSiteName("DonBest");
							} else if (html != null && !isMl && !html.contains("TOTALS")) {
								addLineMovement(lm, dbd);

								// LineMovement
								lm = new LineMovement();
								lm.setSportsbook(book);
								lm.setSportNumber(sportNumber);
								lm.setSportType(sportType);
								lm.setGameNumber(gameNumber);
								lm.setGameType(gameType);
								lm.setSiteName("DonBest");
							} else {
								addLineMovement(lm, dbd);

								lm = null;
							}
						}

						final Elements tds = tr.select("td");

						if (tds.size() == 6) {
							if (isMl) {
								// ML
								lm.setLineNumber(2);
								lm.setLineType("moneyline");
								try {
									getMlData(tds, book, sportNumber, sportType, gameNumber, gameType, lm, dbd);
								} catch (Exception e) {
									
								}
							} else {
								// SPREAD
								if (lm == null) {
									LOGGER.debug("tr: " + tr);
								}
								lm.setLineNumber(1);
								lm.setLineType("spread");
								try {
									getSpreadData(tds, book, sportNumber, sportType, gameNumber, gameType, lm, dbd);
								} catch (Exception e) {
									
								}
							}
						} else if (tds.size() == 4) {
							if (!isMl) {
								try {
									// TOTAL
									lm.setLineNumber(3);
									lm.setLineType("total");
									getTotalData(tds, book, sportNumber, sportType, gameNumber, gameType, lm, dbd);
								} catch (Throwable t) {
								}
							}
						}
					}
					break;
			}
		}

		// Add the final line movement
		addLineMovement(lm, dbd);

		LOGGER.info("Exiting getBookData()");
	}

	/**
	 * 
	 * @param lm
	 * @param dbd
	 */
	private void addLineMovement(LineMovement lm, DonBestData dbd) {
		if (lm != null && lm.getLinemovements() != null && !lm.getLinemovements().isEmpty()) {
			if (lm.getVisitorteam().equals("Jackson State") && lm.getHometeam().equals("So Mississippi")) {
				lm.setVisitorteam("Jackson St");
			}

			if ((!lm.getVisitorteam().equals("Ferris State") && !lm.getHometeam().equals("Valdosta St")) &&
				(!lm.getVisitorteam().equals("Mary Hardin Baylor") && !lm.getHometeam().equals("Mount Union"))) {
				dbd.addLine(lm);				
			}
		}		
	}

	/**
	 * 
	 * @param tds
	 * @param book
	 * @param sportNumber
	 * @param sportType
	 * @param gameNumber
	 * @param gameType
	 * @param llm
	 * @param dbd
	 */
	private void getMlData(Elements tds, 
			String book, 
			int sportNumber, 
			String sportType,
			int gameNumber,
			String gameType,
			LineMovement llm, 
			DonBestData dbd) throws Exception {
		// ML
		int i = 0;
		final MovementData md = new MovementData();
		LineMovement lm = llm;

		for (Element td : tds) {
			String html = td.html().trim();
			html = html.replace("½", ".5");
			html = html.replace("\uFFFD", ".5");
			html = html.replace("\u00bd", ".5");
	
			switch (i++) {
				case 0:
					// Date/time
					int index = html.indexOf(" ");

					if (index != -1) {
						final String date = html.substring(0, index);
						final String time = html.substring(index + 1);
						final ZoneId arrivingZone = ZoneId.of("America/Los_Angeles");
						final LocalDateTime utcDateTime = LocalDateTime.parse((date  + " " + time.toUpperCase()), GAME_DATE_FORMAT); 
//						final ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
//						final ZonedDateTime laDateTime = utcZoned.withZoneSameInstant(arrivingZone);
//						final LocalDate localDate = laDateTime.toLocalDate();
//						final LocalTime localTime = laDateTime.toLocalTime();
						md.setLineDate(utcDateTime);
						md.setLineTime(utcDateTime);						
					}
					break;
				case 1:
					final Integer rotationId = (Integer.parseInt(html) + 1);	

					if (html != null && html.length() == 4 && html.startsWith("9")) {
						throw new Exception();
					}

					final List<LineMovement> lms = dbd.getLines();

					for (LineMovement lmm : lms) {
						if (lmm.getVisitorRotationId() != null && lmm.getVisitorRotationId().equals(html) &&
							lmm.getLineType().equals("moneyline") &&
							lmm.getSportsbook().equals(book)) {
							LOGGER.debug("book: " + book);
							LOGGER.debug("lmm.getSportsbook(): " + lmm.getSportsbook());
							lm = lmm;
						}
					}

					lm.setVisitorRotationId(html);
					lm.setHomeRotationId(rotationId.toString());
					break;
				case 2:
					lm.setVisitorteam(html);
					break;
				case 3:
					{
						try {
							final Map<String, String> lines = parseMoneyLine(html);
	
							if (lines.size() > 0) {
								final String lineindicator = lines.get("lineindicator");
								md.setLineindicator1(lineindicator);
								final String line = lines.get("line");

								if (line != null && line.length() > 0) {
									if (lineindicator.equals("-")) {
										md.setLineone(Double.parseDouble(lineindicator + line));
									} else {
										md.setLineone(Double.parseDouble(line));
									}

									final String juiceindicator = lines.get("juiceindicator");
									md.setJuiceindicator1(juiceindicator);
									final String juice = lines.get("juice");

									if (juice != null && juice.length() > 0) {
										if (juiceindicator.equals("-")) {
											md.setJuiceone(Double.parseDouble(juiceindicator + juice));
										} else {
											md.setJuiceone(Double.parseDouble(juice));
										}
									}
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						}
					}
					break;
				case 4:
					lm.setHometeam(html);
					break;
				case 5:
					{
						final Map<String, String> lines = parseMoneyLine(html);

						try {
							if (lines.size() > 0) {
								final String lineindicator = lines.get("lineindicator");
								md.setLineindicator2(lineindicator);
								final String line = lines.get("line");

								if (line != null && line.length() > 0) {
									if (lineindicator.equals("-")) {
										md.setLinetwo(Double.parseDouble(lineindicator + line));
									} else {
										md.setLinetwo(Double.parseDouble(line));
									}

									final String juiceindicator = lines.get("juiceindicator");
									md.setJuiceindicator2(juiceindicator);
									final String juice = lines.get("juice");

									if (juice != null && juice.length() > 0) {
										if (juiceindicator.equals("-")) {
											md.setJuicetwo(Double.parseDouble(juiceindicator + juice));
										} else {
											md.setJuicetwo(Double.parseDouble(juice));
										}
									}
								}

								if (!md.getLineindicator1().equals("0")) {
									// Add a line
									lm.addLinemovement(md);
								}
							}
						} catch (Throwable t) {
							LOGGER.error(t.getMessage(), t);
						}
					}
					break;
				default:
					break;
			}
		}
	}

	/**
	 * 
	 * @param tds
	 * @param book
	 * @param sportNumber
	 * @param sportType
	 * @param gameNumber
	 * @param gameType
	 * @param llm
	 * @param dbd
	 */
	private void getTotalData(Elements tds, 
			String book, 
			int sportNumber, 
			String sportType, 
			int gameNumber, 
			String gameType, 
			LineMovement llm, 
			DonBestData dbd) throws Exception {
		// TOTAL
		int i = 0;
		final MovementData md = new MovementData();
		LineMovement lm = llm;

		for (Element td : tds) {
			String html = td.html().trim();
			html = html.replace("½", ".5");
			html = html.replace("\uFFFD", ".5");
			html = html.replace("\u00bd", ".5");

			switch (i++) {
				case 0:
					{
						// Date/time
						int index = html.indexOf(" ");

						if (index != -1) {
							final String date = html.substring(0, index);
							final String time = html.substring(index + 1);
							final ZoneId arrivingZone = ZoneId.of("America/Los_Angeles");
							final LocalDateTime utcDateTime = LocalDateTime.parse((date  + " " + time.toUpperCase()), GAME_DATE_FORMAT); 
//							final ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
//							final ZonedDateTime laDateTime = utcZoned.withZoneSameInstant(arrivingZone);
//							final LocalDate localDate = laDateTime.toLocalDate();
//							final LocalTime localTime = laDateTime.toLocalTime();
							md.setLineDate(utcDateTime);
							md.setLineTime(utcDateTime);						
						}
					}
					break;
				case 1:
					{
						final Integer rotationId = (Integer.parseInt(html) + 1);

						if (html != null && html.length() == 4 && html.startsWith("9")) {
							throw new Exception();
						}

						if (lm == null) {
							LOGGER.debug("td: " + td);
						}
						final List<LineMovement> lms = dbd.getLines();

						for (LineMovement lmm : lms) {
							if (lmm.getVisitorRotationId() != null && lmm.getVisitorRotationId().equals(html) &&
								lmm.getLineType().equals("total") &&
								lmm.getSportsbook().equals(book)) {
								lm = lmm;
							}
						}

						lm.setVisitorRotationId(html);
						lm.setHomeRotationId(rotationId.toString());
					}
					break;
				case 2:
					{
						int hindex = html.indexOf("/");
						if (hindex != -1) {
							lm.setVisitorteam(html.substring(0, hindex).trim());
							lm.setHometeam(html.substring(hindex + 1).trim());
						}
					}
					break;
				case 3:
					{
						if (html != null && html.length() > 0 && html.contains("Over")) {
							html = html.replace("½", ".5").trim();
							// 47½ Over -107 Under -107
							int ouindex = html.indexOf("Over");

							if (ouindex != -1) {
								final String line = html.substring(0, ouindex).trim();
								md.setLineindicator1("o");
								md.setLineone(Double.parseDouble(line));
								md.setLineindicator2("u");
								md.setLinetwo(Double.parseDouble(line));

								html = html.substring(ouindex + "Over".length());
								ouindex = html.indexOf("Under");
								final String over = html.substring(0, ouindex).trim();
								final String ojuiceindicator = over.substring(0, 1);
								md.setJuiceindicator1(ojuiceindicator);
								String juice = over.substring(1);

								if (juice != null && juice.length() < 3) {
									if (ojuiceindicator.equals("-")) {
										md.setJuiceone(Double.parseDouble(ojuiceindicator + "1" + juice));
									} else {
										md.setJuiceone(Double.parseDouble("1" + juice));
									}
								} else {
									md.setJuiceone(Double.parseDouble(juice));
									if (ojuiceindicator.equals("-")) {
										md.setJuiceone(Double.parseDouble(ojuiceindicator + juice));
									} else {
										md.setJuiceone(Double.parseDouble(juice));
									}
								}

								html = html.substring(ouindex + "Under".length());
								final String under = html.trim();
								final String ujuiceindicator = under.substring(0, 1);
								md.setJuiceindicator2(under.substring(0, 1));
								juice = under.substring(1);

								if (juice != null && juice.length() < 3) {
									if (ujuiceindicator.equals("-")) {
										md.setJuicetwo(Double.parseDouble(ujuiceindicator + "1" + juice));
									} else {
										md.setJuicetwo(Double.parseDouble("1" + juice));
									}
								} else {
									if (ujuiceindicator.equals("-")) {
										md.setJuicetwo(Double.parseDouble(ujuiceindicator + juice));
									} else {
										md.setJuicetwo(Double.parseDouble(juice));
									}
								}

								if (!md.getLineindicator1().equals("0")) {
									// Add a line
									lm.addLinemovement(md);
								}
							}
						} else if (html != null && html.length() > 0) {
							html = html.replace("½", ".5").trim();
							md.setLineindicator1("o");
							md.setLineone(Double.parseDouble(html));
							md.setJuiceindicator1("-");
							md.setJuiceone(Double.parseDouble("-110"));
							md.setLineindicator2("u");
							md.setLinetwo(Double.parseDouble(html));
							md.setJuiceindicator2("-");
							md.setJuicetwo(Double.parseDouble("-110"));

							if (!md.getLineindicator1().equals("0")) {
								// Add a line
								lm.addLinemovement(md);
							}
						}
					}
					break;
				default:
					break;
			}
		}		
	}

	/**
	 * 
	 * @param tds
	 * @param book
	 * @param sportNumber
	 * @param sportType
	 * @param gameNumber
	 * @param gameType
	 * @param llm
	 * @param dbd
	 */
	private void getSpreadData(Elements tds, 
			String book, 
			int sportNumber, 
			String sportType, 
			int gameNumber, 
			String gameType, 
			LineMovement llm, 
			DonBestData dbd) throws Exception {
		// SPREAD
		int i = 0;
		final MovementData md = new MovementData();
		LineMovement lm = llm;

		for (Element td : tds) {
			String html = td.html().trim();
			html = html.replace("½", ".5");
			html = html.replace("\uFFFD", ".5");
			html = html.replace("\u00bd", ".5");

			switch (i++) {
				case 0:
					{
						// Date/time
						int index = html.indexOf(" ");
	
						if (index != -1) {
							final String date = html.substring(0, index);
							final String time = html.substring(index + 1);
							final ZoneId arrivingZone = ZoneId.of("America/Los_Angeles");
							final LocalDateTime utcDateTime = LocalDateTime.parse((date  + " " + time.toUpperCase()), GAME_DATE_FORMAT); 
//							final ZonedDateTime utcZoned = utcDateTime.atZone(ZoneId.of("UTC"));
//							final ZonedDateTime laDateTime = utcZoned.withZoneSameInstant(arrivingZone);
//							final LocalDate localDate = laDateTime.toLocalDate();
//							final LocalTime localTime = laDateTime.toLocalTime();
							md.setLineDate(utcDateTime);
							md.setLineTime(utcDateTime);						
						}
					}
					break;
				case 1:
					{
						final Integer rotationId = (Integer.parseInt(html) + 1);

						if (html != null && html.length() == 4 && html.startsWith("9")) {
							throw new Exception();
						}

						final List<LineMovement> lms = dbd.getLines();

						for (LineMovement lmm : lms) {
							if (lmm.getVisitorRotationId() != null && lmm.getVisitorRotationId().equals(html) &&
								lmm.getLineType().equals("spread") &&
								lmm.getSportsbook().equals(book)) {
								lm = lmm;
							}
						}

						lm.setVisitorRotationId(html);
						lm.setHomeRotationId(rotationId.toString());
					}
					break;
				case 2:
					{
						lm.setVisitorteam(html);
					}
					break;
				case 3:
					{
						final Map<String, String> lines = parseLine(html);

						if (lines.size() > 0) {
							try {
								final String lineindicator = lines.get("lineindicator");
								md.setLineindicator1(lineindicator);
								final String line = lines.get("line");

								if (line != null && line.length() > 0) {
									if (lineindicator.equals("-")) {
										md.setLineone(Double.parseDouble(lineindicator + line));
									} else {
										md.setLineone(Double.parseDouble(line));
									}
									final String juiceindicator = lines.get("juiceindicator");
									md.setJuiceindicator1(juiceindicator);
									final String juice = lines.get("juice");

									if (juice != null && juice.length() > 0) {
										if (juiceindicator.equals("-")) {
											md.setJuiceone(Double.parseDouble(juiceindicator + juice));
										} else {
											md.setJuiceone(Double.parseDouble(juice));
										}
									}
								}
							} catch (Throwable t) {
								LOGGER.error(t.getMessage(), t);
								LOGGER.error("lm: " + lm);
								LOGGER.error("html: " + html);
							}
						}
					}
					break;
				case 4:
					{
						lm.setHometeam(html);
					}
					break;
				case 5:
					{
						final Map<String, String> lines = parseLine(html);

						if (lines.size() > 0) {
							try {
								final String lineindicator = lines.get("lineindicator");
								md.setLineindicator2(lineindicator);
								final String line = lines.get("line");

								if (line != null && line.length() > 0) {
									if (lineindicator.equals("-")) {
										md.setLinetwo(Double.parseDouble(lineindicator + line));
									} else {
										md.setLinetwo(Double.parseDouble(line));
									}

									final String juiceindicator = lines.get("juiceindicator");
									md.setJuiceindicator2(juiceindicator);
									final String juice = lines.get("juice");

									if (juice != null && juice.length() > 0) {
										if (juiceindicator.equals("-")) {
											md.setJuicetwo(Double.parseDouble(juiceindicator + juice));
										} else {
											md.setJuicetwo(Double.parseDouble(juice));
										}

										if (md.getLineindicator1() != null && !md.getLineindicator1().equals("0")) {
											// Add a line
											lm.addLinemovement(md);
										}
									}
								}
							} catch (Throwable t) {
								LOGGER.error(t.getMessage(), t);
								LOGGER.error("lm: " + lm);
								LOGGER.error("html: " + html);
								LOGGER.error("lines: " + lines);
							}
						}
					}
					break;
				default:
					break;
			}
		}
	}

	/**
	 * 
	 * @param html
	 * @return
	 */
	private Map<String, String> parseLine(String html) {
		LOGGER.info("Entering parseLine()");
		final Map<String, String> map = new HashMap<String, String>();

		// -22½ev
		if (html != null && html.length() > 0) {
			html = html.replace("½", ".5");

			if (html.contains("pk")) {
				map.put("lineindicator", "+");
				map.put("line", "0");

				int index = html.indexOf("pk");
				final String juice = html.substring(index + 2);
				LOGGER.debug("juice: " + juice);

				final Map<String, String> jmap = processJuice(juice);
				map.put("juiceindicator", jmap.get("juiceindicator"));
				map.put("juice", jmap.get("juice"));
			} else {
				map.put("lineindicator", html.substring(0, 1));
				html = html.substring(1);

				if (html.contains("ev")) {
					int lindex = html.indexOf("ev");

					if (lindex != -1) {
						map.put("line", html.substring(0, lindex));
						html = html.substring(lindex);

						map.put("juiceindicator", "+");
						map.put("juice", "100");
					}
				} else {
					int lindex = html.indexOf("-");
	
					if (lindex != -1) {
						map.put("line", html.substring(0, lindex));
						html = html.substring(lindex);
	
						final Map<String, String> jmap = processJuice(html);
						map.put("juiceindicator", jmap.get("juiceindicator"));
						map.put("juice", jmap.get("juice"));
					} else {
						lindex = html.indexOf("+");
		
						if (lindex != -1) {
							map.put("line", html.substring(0, lindex));
							html = html.substring(lindex);
	
							final Map<String, String> jmap = processJuice(html);
							map.put("juiceindicator", jmap.get("juiceindicator"));
							map.put("juice", jmap.get("juice"));
						}
					}
				}
			}
		}

		LOGGER.info("Exiting parseLine()");
		return map;
	}

	/**
	 * 
	 * @return
	 */
	private Map<String, String> processJuice(String juice) {
		LOGGER.info("Entering processJuice()");
		final Map<String, String> map = new HashMap<String, String>();

		if (juice.contains("ev")) {
			map.put("juiceindicator", "+");
			map.put("juice", "100");
		} else {
			map.put("juiceindicator", juice.substring(0, 1));
			juice = juice.substring(1);

			if (juice != null && juice.length() < 3) {
				map.put("juice", ("1" + juice));
			} else {
				map.put("juice", juice);	
			}
		}

		LOGGER.info("Exiting processJuice()");
		return map;
	}

	/**
	 * 
	 * @param html
	 * @return
	 */
	private Map<String, String> parseMoneyLine(String html) {
		LOGGER.info("Entering parseMoneyLine()");
		final Map<String, String> map = new HashMap<String, String>();

		// -22½ev
		if (html != null && html.length() > 0) {
			html = html.replace("½", ".5");
			if (html.contains("ev")) {
				int index = html.indexOf("ev");

				if (index != -1) {
					map.put("lineindicator", "+");
					map.put("line", "100");
					map.put("juiceindicator", "+");
					map.put("juice", "100");
				}
			} else {
				String indicator = html.substring(0, 1);
				String line = html.substring(1);
				map.put("lineindicator", indicator);
				map.put("line", line);
				map.put("juiceindicator", indicator);
				map.put("juice", line);
			}
		}

		LOGGER.info("Exiting parseMoneyLine()");
		return map;
	}

	/**
	 * 
	 * @param table
	 * @param dbd
	 */
	private void getDate(Element table, DonBestData dbd) {
		final Elements th = table.select("tbody tr th");

		if (th.size() > 0) {
			String html = th.get(0).html();
			int ndex = html.indexOf("Line history archive for ");

			if (ndex != -1) {
				html = html.substring(ndex + "Line history archive for ".length());
				dbd.setDate(html);
				try {
					dbd.setDateofgame(DATE_FORMAT.parse(html));
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
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
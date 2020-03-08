/**
 * 
 */
package com.ticketadvantage.services.dao.sites.masseyratings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class MasseyRatingsParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(MasseyRatingsParser.class);
	private static final Map<String, String> NFLMapping = new HashMap<String, String>();
	private static final Map<String, String> MLBMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAABMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAAFMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAAFFCSMapping = new HashMap<String, String>();

	// 10/08/17 1:25 PM (PST)
	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			// 2017-11-01
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

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
		NCAAFMapping.put("Akron","Akron");
		NCAAFMapping.put("Alabama","Alabama");
		NCAAFMapping.put("Appalachian St","Appalachian State");
		NCAAFMapping.put("Arizona","Arizona");
		NCAAFMapping.put("Arizona St","Arizona State");
		NCAAFMapping.put("Arkansas","Arkansas");
		NCAAFMapping.put("Arkansas State","Arkansas State");
		NCAAFMapping.put("Arkansas St","Arkansas State");
		NCAAFMapping.put("Army","Army");
		NCAAFMapping.put("Auburn","Auburn");
		NCAAFMapping.put("Ball St","Ball State");
		NCAAFMapping.put("Baylor","Baylor");
		NCAAFMapping.put("Boise St","Boise State");
		NCAAFMapping.put("Boise State","Boise State");
		NCAAFMapping.put("Boston College","Boston College");
		NCAAFMapping.put("Bowling Green","Bowling Green");
		NCAAFMapping.put("Buffalo","Buffalo");
		NCAAFMapping.put("BYU","BYU");
		NCAAFMapping.put("California","California");
		NCAAFMapping.put("C Michigan","Central Michigan");
		NCAAFMapping.put("Charlotte","Charlotte");
		NCAAFMapping.put("Cincinnati","Cincinnati");
		NCAAFMapping.put("Clemson","Clemson");
		NCAAFMapping.put("Coastal Car","Coastal Carolina");
		NCAAFMapping.put("Colorado","Colorado");
		NCAAFMapping.put("Colorado St","Colorado State");
		NCAAFMapping.put("Connecticut","Connecticut");
		NCAAFMapping.put("Duke","Duke");
		NCAAFMapping.put("East Carolina","East Carolina");
		NCAAFMapping.put("E Michigan","Eastern Michigan");
		NCAAFMapping.put("FL Atlantic","Florida Atlantic");
		NCAAFMapping.put("Florida","Florida");
		NCAAFMapping.put("Florida Intl","Florida Intl");
		NCAAFMapping.put("Florida St","Florida State");
		NCAAFMapping.put("Fresno St","Fresno State");
		NCAAFMapping.put("Ga Southern","Georgia Southern");
		NCAAFMapping.put("Georgia Tech","Georgia Tech");
		NCAAFMapping.put("Georgia","Georgia");
		NCAAFMapping.put("Georgia State","Georgia State");
		NCAAFMapping.put("Georgia St","Georgia State");
		NCAAFMapping.put("Hawaii","Hawai'i");
		NCAAFMapping.put("Houston","Houston");
		NCAAFMapping.put("Illinois","Illinois");
		NCAAFMapping.put("Indiana","Indiana");
		NCAAFMapping.put("Iowa","Iowa");
		NCAAFMapping.put("Iowa St","Iowa State");
		NCAAFMapping.put("Kansas","Kansas");
		NCAAFMapping.put("Kansas St","Kansas State");
		NCAAFMapping.put("Kent","Kent State");
		NCAAFMapping.put("Kentucky","Kentucky");
		NCAAFMapping.put("ULL","Louisiana-Lafayette");
		NCAAFMapping.put("ULM","Louisiana-Monroe");
		NCAAFMapping.put("Louisiana Tech","Louisiana Tech");
		NCAAFMapping.put("Louisville","Louisville");
		NCAAFMapping.put("LSU","LSU");
		NCAAFMapping.put("Liberty","Liberty");
		NCAAFMapping.put("Marshall","Marshall");
		NCAAFMapping.put("Maryland","Maryland");
		NCAAFMapping.put("Memphis","Memphis");
		NCAAFMapping.put("Miami FL","Miami-Florida");
		NCAAFMapping.put("Miami OH","Miami-Ohio");
		NCAAFMapping.put("Michigan","Michigan");
		NCAAFMapping.put("Michigan St","Michigan State");
		NCAAFMapping.put("MTSU","Middle Tennessee");
		NCAAFMapping.put("Middle Tenn St","Middle Tennessee");
		NCAAFMapping.put("Minnesota","Minnesota");
		NCAAFMapping.put("Mississippi St","Mississippi State");
		NCAAFMapping.put("Mississippi","Mississippi");
		NCAAFMapping.put("Missouri","Missouri");
		NCAAFMapping.put("N Illinois","Northern Illinois");
		NCAAFMapping.put("North Carolina","North Carolina");
		NCAAFMapping.put("Navy","Navy");
		NCAAFMapping.put("NC State","North Carolina State");
		NCAAFMapping.put("Nebraska","Nebraska");
		NCAAFMapping.put("Nevada","Nevada");
		NCAAFMapping.put("New Mexico","New Mexico");
		NCAAFMapping.put("New Mexico St","New Mexico State");
		NCAAFMapping.put("North Texas","North Texas");
		NCAAFMapping.put("Northwestern","Northwestern");
		NCAAFMapping.put("Notre Dame","Notre Dame");
		NCAAFMapping.put("Ohio","Ohio");
		NCAAFMapping.put("Ohio St","Ohio State");
		NCAAFMapping.put("Oklahoma","Oklahoma");
		NCAAFMapping.put("Oklahoma St","Oklahoma State");
		NCAAFMapping.put("Old Dominion","Old Dominion");
		NCAAFMapping.put("Oregon","Oregon");
		NCAAFMapping.put("Oregon St","Oregon State");
		NCAAFMapping.put("Penn St","Penn State");
		NCAAFMapping.put("Pittsburgh","Pittsburgh");
		NCAAFMapping.put("Purdue","Purdue");
		NCAAFMapping.put("Rice","Rice");
		NCAAFMapping.put("Rutgers","Rutgers");
		NCAAFMapping.put("San Diego St","San Diego State");
		NCAAFMapping.put("South Alabama","South Alabama");
		NCAAFMapping.put("South Carolina","South Carolina");
		NCAAFMapping.put("South Florida","South Florida");
		NCAAFMapping.put("SMU","SMU");		
		NCAAFMapping.put("Southern Miss","Southern Miss");
		NCAAFMapping.put("San Jose St","San Jose State");
		NCAAFMapping.put("Stanford","Stanford");
		NCAAFMapping.put("Syracuse","Syracuse");
		NCAAFMapping.put("TCU","TCU");
		NCAAFMapping.put("Temple","Temple");
		NCAAFMapping.put("Tennessee","Tennessee");
		NCAAFMapping.put("Texas","Texas");
		NCAAFMapping.put("Texas A&M","TEXAS AM");
		NCAAFMapping.put("Texas AM","TEXAS AM");
		NCAAFMapping.put("Texas St","Texas State");
		NCAAFMapping.put("Texas Tech","Texas Tech");
		NCAAFMapping.put("Toledo","Toledo");
		NCAAFMapping.put("Troy","Troy");
		NCAAFMapping.put("Tulane","Tulane");
		NCAAFMapping.put("Tulsa","Tulsa");
		NCAAFMapping.put("UTEP","UTEP");
		NCAAFMapping.put("UT San Antonio","UTSA");
		NCAAFMapping.put("Massachusetts","Umass");
		NCAAFMapping.put("UAB","UAB");
		NCAAFMapping.put("UCLA","UCLA");
		NCAAFMapping.put("UCF","UCF");
		NCAAFMapping.put("UNLV","UNLV");
		NCAAFMapping.put("USC","USC");
		NCAAFMapping.put("Utah","Utah");
		NCAAFMapping.put("Utah St","Utah State");
		NCAAFMapping.put("Virginia Tech","Virginia Tech");
		NCAAFMapping.put("Vanderbilt","Vanderbilt");
		NCAAFMapping.put("Virginia","Virginia");
		NCAAFMapping.put("WKU","Western Kentucky");
		NCAAFMapping.put("W Kentucky","Western Kentucky");
		NCAAFMapping.put("W Michigan","Western Michigan");
		NCAAFMapping.put("West Virginia","West Virginia");
		NCAAFMapping.put("Wake Forest","Wake Forest");
		NCAAFMapping.put("Washington St","Washington State");
		NCAAFMapping.put("Washington","Washington");
		NCAAFMapping.put("Wisconsin","Wisconsin");
		NCAAFMapping.put("Wyoming","Wyoming");		
		
		NCAAFFCSMapping.put("ABILENE CHR", "ABILENE CHRISTIAN");
		NCAAFFCSMapping.put("ABILENE CHRISTIAN", "ABILENE CHRISTIAN");
		NCAAFFCSMapping.put("ALABAMA A&M", "ALABAMA A&AMP;M");
		NCAAFFCSMapping.put("ALABAMA ST", "ALABAMA STATE");
		NCAAFFCSMapping.put("ALBANY NY", "ALBANY");
		NCAAFFCSMapping.put("ALCORN ST", "ALCORN STATE");
		NCAAFFCSMapping.put("ARK PINE BLUFF", "ARKANSAS-PINE BLUFF");
		NCAAFFCSMapping.put("AUSTIN PEAY", "AUSTIN PEAY");
		NCAAFFCSMapping.put("BETHUNE-COOKMAN", "BETHUNE-COOKMAN");
		NCAAFFCSMapping.put("BROWN", "BROWN");
		NCAAFFCSMapping.put("BRYANT", "BRYANT");
		NCAAFFCSMapping.put("BUCKNELL", "BUCKNELL");
		NCAAFFCSMapping.put("BUTLER", "BUTLER");
		NCAAFFCSMapping.put("CAL POLY", "CAL POLY");
		NCAAFFCSMapping.put("Cal Poly SLO", "CAL POLY");
		NCAAFFCSMapping.put("CAMPBELL", "CAMPBELL");
		NCAAFFCSMapping.put("CENT ARKANSAS", "CENTRAL ARKANSAS");
		NCAAFFCSMapping.put("CENTRAL CONN", "CENTRAL CONNECTICUT");
		NCAAFFCSMapping.put("CHARLESTON SO", "CHARLESTON SOUTHERN");
		NCAAFFCSMapping.put("CHATTANOOGA", "CHATTANOOGA");
		NCAAFFCSMapping.put("CITADEL", "THE CITADEL");
		NCAAFFCSMapping.put("COLGATE", "COLGATE");
		NCAAFFCSMapping.put("COLUMBIA", "COLUMBIA");
		NCAAFFCSMapping.put("CORNELL", "CORNELL");
		NCAAFFCSMapping.put("CS SACRAMENTO", "SACRAMENTO STATE");
		NCAAFFCSMapping.put("DARTMOUTH", "DARTMOUTH");
		NCAAFFCSMapping.put("DAVIDSON", "DAVIDSON");
		NCAAFFCSMapping.put("DAYTON", "DAYTON");
		NCAAFFCSMapping.put("DELAWARE", "DELAWARE");
		NCAAFFCSMapping.put("DELAWARE ST", "DELAWARE STATE");
		NCAAFFCSMapping.put("DRAKE", "DRAKE");
		NCAAFFCSMapping.put("DUQUESNE", "DUQUESNE");
		NCAAFFCSMapping.put("E ILLINOIS", "EASTERN ILLINOIS");
		NCAAFFCSMapping.put("E KENTUCKY", "EASTERN KENTUCKY");
		NCAAFFCSMapping.put("E WASHINGTON", "EASTERN WASHINGTON");
		NCAAFFCSMapping.put("ELON", "ELON");
		NCAAFFCSMapping.put("ETSU", "EAST TENNESSEE STATE");
		NCAAFFCSMapping.put("FLORIDA A&M", "FLORIDA A&AMP;M");
		NCAAFFCSMapping.put("FORDHAM", "FORDHAM");
		NCAAFFCSMapping.put("FURMAN", "FURMAN");
		NCAAFFCSMapping.put("GARDNER WEBB", "GARDNER-WEBB");
		NCAAFFCSMapping.put("GEORGIA STATE","GEORGIA STATE");
		NCAAFFCSMapping.put("GEORGETOWN", "GEORGETOWN");
		NCAAFFCSMapping.put("GRAMBLING", "GRAMBLING");
		NCAAFFCSMapping.put("HAMPTON", "HAMPTON");
		NCAAFFCSMapping.put("HARVARD", "HARVARD");
		NCAAFFCSMapping.put("HOLY CROSS", "HOLY CROSS");
		NCAAFFCSMapping.put("HOUSTON BAP", "HOUSTON BAPTIST");
		NCAAFFCSMapping.put("HOWARD", "HOWARD");
		NCAAFFCSMapping.put("IDAHO", "IDAHO");
		NCAAFFCSMapping.put("IDAHO ST", "IDAHO STATE");
		NCAAFFCSMapping.put("ILLINOIS ST", "ILLINOIS STATE");
		NCAAFFCSMapping.put("INCARNATE WORD", "INCARNATE WORD");
		NCAAFFCSMapping.put("INDIANA ST", "INDIANA STATE");
		NCAAFFCSMapping.put("JACKSON ST", "JACKSON STATE");
		NCAAFFCSMapping.put("JACKSONVILLE", "JACKSONVILLE");
		NCAAFFCSMapping.put("JACKSONVILLE ST", "JACKSONVILLE STATE");
		NCAAFFCSMapping.put("JAMES MADISON", "JAMES MADISON");
		NCAAFFCSMapping.put("KENNESAW", "KENNESAW STATE");
		NCAAFFCSMapping.put("LAFAYETTE", "LAFAYETTE");
		NCAAFFCSMapping.put("LAMAR", "LAMAR");
		NCAAFFCSMapping.put("LEHIGH", "LEHIGH");
		NCAAFFCSMapping.put("MAINE", "MAINE");
		NCAAFFCSMapping.put("MARIST", "MARIST");
		NCAAFFCSMapping.put("MCNEESE ST", "MCNEESE");
		NCAAFFCSMapping.put("MERCER", "MERCER");
		NCAAFFCSMapping.put("MISSOURI ST", "MISSOURI STATE");
		NCAAFFCSMapping.put("MONMOUTH NJ", "MONMOUTH");
		NCAAFFCSMapping.put("MONTANA", "MONTANA");
		NCAAFFCSMapping.put("MONTANA ST", "MONTANA STATE");
		NCAAFFCSMapping.put("MOREHEAD ST", "MOREHEAD STATE");
		NCAAFFCSMapping.put("MORGAN ST", "MORGAN STATE");
		NCAAFFCSMapping.put("MS VALLEY ST", "MISSISSIPPI VALLEY STATE");
		NCAAFFCSMapping.put("MURRAY ST", "MURRAY STATE");
		NCAAFFCSMapping.put("N COLORADO", "NORTHERN COLORADO");
		NCAAFFCSMapping.put("N DAKOTA ST", "NORTH DAKOTA STATE");
		NCAAFFCSMapping.put("NC A&T", "NORTH CAROLINA AT");
		NCAAFFCSMapping.put("NC CENTRAL", "NORTH CAROLINA CENTRAL");
		NCAAFFCSMapping.put("NEW HAMPSHIRE", "NEW HAMPSHIRE");
		NCAAFFCSMapping.put("NICHOLLS ST", "NICHOLLS STATE");
		NCAAFFCSMapping.put("NORFOLK ST", "NORFOLK STATE");
		NCAAFFCSMapping.put("NORTH ALABAMA", "NORTH ALABAMA");
		NCAAFFCSMapping.put("NORTH DAKOTA", "NORTH DAKOTA");
		NCAAFFCSMapping.put("NORTHERN ARIZONA", "NORTHERN ARIZONA");
		NCAAFFCSMapping.put("NORTHERN IOWA", "NORTHERN IOWA");
		NCAAFFCSMapping.put("NORTHWESTERN LA", "NORTHWESTERN STATE");
		NCAAFFCSMapping.put("PENN", "PENNSYLVANIA");
		NCAAFFCSMapping.put("PORTLAND ST", "PORTLAND STATE");
		NCAAFFCSMapping.put("PRAIRIE VIEW", "PRAIRIE VIEW AM");
		NCAAFFCSMapping.put("PRESBYTERIAN", "PRESBYTERIAN COLLEGE");
		NCAAFFCSMapping.put("PRINCETON", "PRINCETON");
		NCAAFFCSMapping.put("RHODE ISLAND", "RHODE ISLAND");
		NCAAFFCSMapping.put("RICHMOND", "RICHMOND");
		NCAAFFCSMapping.put("ROBERT MORRIS", "ROBERT MORRIS");
		NCAAFFCSMapping.put("S CAROLINA ST", "SOUTH CAROLINA STATE");
		NCAAFFCSMapping.put("S DAKOTA ST", "SOUTH DAKOTA STATE");
		NCAAFFCSMapping.put("S ILLINOIS", "SOUTHERN ILLINOIS");
		NCAAFFCSMapping.put("SACRED HEART", "SACRED HEART");
		NCAAFFCSMapping.put("SAM HOUSTON ST", "SAM HOUSTON STATE");
		NCAAFFCSMapping.put("SAMFORD", "SAMFORD");
		NCAAFFCSMapping.put("SAN DIEGO", "SAN DIEGO");
		NCAAFFCSMapping.put("SAVANNAH ST", "SAVANNAH STATE");
		NCAAFFCSMapping.put("SE LOUISIANA", "SOUTHEASTERN LOUISIANA");
		NCAAFFCSMapping.put("SE MISSOURI ST", "SOUTHEAST MISSOURI STATE");
		NCAAFFCSMapping.put("SF AUSTIN", "STEPHEN F AUSTIN");
		NCAAFFCSMapping.put("SOUTH DAKOTA", "SOUTH DAKOTA");
		NCAAFFCSMapping.put("SOUTHERN UNIV", "SOUTHERN");
		NCAAFFCSMapping.put("SOUTHERN UTAH", "SOUTHERN UTAH");
		NCAAFFCSMapping.put("ST FRANCIS PA", "ST FRANCIS (PA)");
		NCAAFFCSMapping.put("STETSON", "STETSON");
		NCAAFFCSMapping.put("STONY BROOK", "STONY BROOK");
		NCAAFFCSMapping.put("TENNESSEE ST", "TENNESSEE STATE");
		NCAAFFCSMapping.put("TENNESSEE TECH", "TENNESSEE TECH");
		NCAAFFCSMapping.put("TN MARTIN", "UT MARTIN");
		NCAAFFCSMapping.put("TOWSON", "TOWSON");
		NCAAFFCSMapping.put("TX SOUTHERN", "TEXAS SOUTHERN");
		NCAAFFCSMapping.put("UC DAVIS", "UC DAVIS");
		NCAAFFCSMapping.put("VALPARAISO", "VALPARAISO");
		NCAAFFCSMapping.put("VILLANOVA", "VILLANOVA");
		NCAAFFCSMapping.put("VMI", "VMI");
		NCAAFFCSMapping.put("W CAROLINA", "WESTERN CAROLINA");
		NCAAFFCSMapping.put("W ILLINOIS", "WESTERN ILLINOIS");
		NCAAFFCSMapping.put("WAGNER", "WAGNER");
		NCAAFFCSMapping.put("WEBER ST", "WEBER STATE");
		NCAAFFCSMapping.put("WILLIAM & MARY", "WILLIAM &AMP; MARY");
		NCAAFFCSMapping.put("WOFFORD", "WOFFORD");
		NCAAFFCSMapping.put("YALE", "YALE");
		NCAAFFCSMapping.put("YOUNGSTOWN ST", "YOUNGSTOWN STATE");

		NCAABMapping.put("ABILENE CHR" , "Abilene Christian");
		NCAABMapping.put("ABILENE CHRISTIAN" , "Abilene Christian");
		NCAABMapping.put("AIR FORCE" , "Air Force");
		NCAABMapping.put("AKRON" , "Akron");
		NCAABMapping.put("ALABAMA" , "Alabama");
		NCAABMapping.put("ALABAMA AM" , "Alabama AM");
		NCAABMapping.put("ALABAMA A&M" , "Alabama AM");
		NCAABMapping.put("ALABAMA ST" , "Alabama State");
		NCAABMapping.put("ALBANY NY" , "Albany");
		NCAABMapping.put("ALCORN ST" , "Alcorn State");
		NCAABMapping.put("AMERICAN UNIV" , "American");
		NCAABMapping.put("APPALACHIAN ST" , "APPALACHIAN State");
		NCAABMapping.put("ARIZONA" , "Arizona");
		NCAABMapping.put("ARIZONA STATE" , "Arizona State");
		NCAABMapping.put("ARIZONA ST" , "Arizona State");
		NCAABMapping.put("ARK LITTLE ROCK" , "Arkansas-Little Rock");
		NCAABMapping.put("ARKANSAS" , "Arkansas");
		NCAABMapping.put("ARKANSAS STATE" , "Arkansas State");
		NCAABMapping.put("ARKANSAS ST" , "Arkansas State");
		NCAABMapping.put("ARK PINE BLUFF" , "Arkansas-Pine Bluff");
		NCAABMapping.put("ARMY" , "Army");
		NCAABMapping.put("AUBURN" , "Auburn");
		NCAABMapping.put("AUSTIN PEAY" , "Austin Peay");
		NCAABMapping.put("BALL ST" , "Ball State");
		NCAABMapping.put("BAYLOR" , "Baylor");
		NCAABMapping.put("BELMONT" , "Belmont");
		NCAABMapping.put("BETHUNE-COOKMAN" , "Bethune-Cookman");
		NCAABMapping.put("Binghamton" , "Binghamton");
		NCAABMapping.put("BOISE ST" , "Boise State");
		NCAABMapping.put("BOSTON COLLEGE" , "Boston College");
		NCAABMapping.put("Boston Univ" , "BOSTON UNIVERSITY");
		NCAABMapping.put("BOWLING GREEN" , "Bowling Green");
		NCAABMapping.put("Bradley" , "Bradley");
		NCAABMapping.put("BROWN" , "Brown");
		NCAABMapping.put("BRYANT" , "Bryant");
		NCAABMapping.put("BUCKNELL" , "Bucknell");
		NCAABMapping.put("BUFFALO" , "Buffalo");
		NCAABMapping.put("BUTLER" , "Butler");
		NCAABMapping.put("BYU" , "BYU");
		NCAABMapping.put("Cal Poly SLO" , "Cal Poly");
		NCAABMapping.put("CALIFORNIA" , "California");
		NCAABMapping.put("CAMPBELL" , "Campbell");
		NCAABMapping.put("Canisius" , "Canisius");
		NCAABMapping.put("CENT ARKANSAS" , "Central Arkansas");
		NCAABMapping.put("CENTRAL CONN" , "Central Connecticut");
		NCAABMapping.put("C MICHIGAN" , "Central Michigan");
		NCAABMapping.put("CHARLESTON SO" , "Charleston Southern");
		NCAABMapping.put("CHARLOTTE" , "Charlotte");
		NCAABMapping.put("CHATTANOOGA" , "Chattanooga");
		NCAABMapping.put("Chicago St" , "Chicago State");
		NCAABMapping.put("CINCINNATI" , "Cincinnati");
		NCAABMapping.put("CLEMSON" , "Clemson");
		NCAABMapping.put("Cleveland St" , "Cleveland State");
		NCAABMapping.put("COASTAL CAR" , "Coastal Carolina");
		NCAABMapping.put("Col Charleston" , "Charleston");
		NCAABMapping.put("COLGATE" , "Colgate");
		NCAABMapping.put("COLORADO" , "Colorado");
		NCAABMapping.put("COLORADO St" , "Colorado State");
		NCAABMapping.put("COLUMBIA" , "Columbia");
		NCAABMapping.put("CONNECTICUT" , "UConn");
		NCAABMapping.put("Coppin St" , "Coppin State");
		NCAABMapping.put("CORNELL" , "Cornell");
		NCAABMapping.put("Creighton" , "Creighton");
		NCAABMapping.put("CS Bakersfield" , "CSU Bakersfield");
		NCAABMapping.put("CS Fullerton" , "CSU Fullerton");
		NCAABMapping.put("CS Northridge" , "CSU Northridge");
		NCAABMapping.put("CS SACRAMENTO" , "Sacramento State");
		NCAABMapping.put("DARTMOUTH" , "Dartmouth");
		NCAABMapping.put("DAVIDSON" , "Davidson");
		NCAABMapping.put("DAYTON" , "Dayton");
		NCAABMapping.put("DELAWARE" , "Delaware");
		NCAABMapping.put("DELAWARE ST" , "Delaware State");
		NCAABMapping.put("Denver" , "Denver");
		NCAABMapping.put("DePaul" , "DePaul");
		NCAABMapping.put("Detroit" , "Detroit Mercy");
		NCAABMapping.put("DRAKE" , "Drake");
		NCAABMapping.put("Drexel" , "Drexel");
		NCAABMapping.put("DUKE" , "Duke");
		NCAABMapping.put("DUQUESNE" , "Duquesne");
		NCAABMapping.put("EAST CAROLINA" , "East Carolina");
		NCAABMapping.put("ETSU" , "East Tennessee State");
		NCAABMapping.put("E ILLINOIS" , "Eastern Illinois");
		NCAABMapping.put("E KENTUCKY" , "Eastern Kentucky");
		NCAABMapping.put("E MICHIGAN" , "Eastern Michigan");
		NCAABMapping.put("E WASHINGTON" , "Eastern Washington");
		NCAABMapping.put("Edwardsville" , "SIU-Edwardsville");
		NCAABMapping.put("ELON" , "Elon");
		NCAABMapping.put("Evansville" , "Evansville");
		NCAABMapping.put("F Dickinson" , "Fairleigh Dickinson");
		NCAABMapping.put("Fairfield" , "Fairfield");
		NCAABMapping.put("FL Gulf Coast" , "Florida Gulf Coast");
		NCAABMapping.put("FLORIDA" , "Florida");
		NCAABMapping.put("FLORIDA A&M" , "Florida A&amp;M");
		NCAABMapping.put("FL ATLANTIC" , "Florida Atlantic");
		NCAABMapping.put("FLORIDA INTL" , "Florida International");
		NCAABMapping.put("FLORIDA ST" , "Florida State");
		NCAABMapping.put("FORDHAM" , "Fordham");
		NCAABMapping.put("FRESNO ST" , "Fresno State");
		NCAABMapping.put("FURMAN" , "Furman");
		NCAABMapping.put("G Washington" , "George Washington");
		NCAABMapping.put("GARDNER WEBB" , "Gardner-Webb");
		NCAABMapping.put("George Mason" , "George Mason");
		NCAABMapping.put("GEORGETOWN" , "Georgetown");
		NCAABMapping.put("GEORGIA" , "Georgia");
		NCAABMapping.put("GA SOUTHERN" , "Georgia Southern");
		NCAABMapping.put("GEORGIA ST" , "Georgia State");
		NCAABMapping.put("GEORGIA TECH" , "Georgia Tech");
		NCAABMapping.put("Gonzaga" , "Gonzaga");
		NCAABMapping.put("GRAMBLING" , "Grambling State");
		NCAABMapping.put("GRAMBLING ST" , "Grambling State");
		NCAABMapping.put("Grand Canyon" , "Grand Canyon");
		NCAABMapping.put("HAMPTON" , "Hampton");
		NCAABMapping.put("Hartford" , "Hartford");
		NCAABMapping.put("HARVARD" , "Harvard");
		NCAABMapping.put("HAWAII" , "Hawai'i");
		NCAABMapping.put("High Point" , "High Point");
		NCAABMapping.put("Hofstra" , "Hofstra");
		NCAABMapping.put("HOLY CROSS" , "Holy Cross");
		NCAABMapping.put("HOUSTON" , "Houston");
		NCAABMapping.put("HOUSTON BAP" , "Houston Baptist");
		NCAABMapping.put("HOWARD" , "Howard");
		NCAABMapping.put("IDAHO" , "Idaho");
		NCAABMapping.put("IDAHO ST" , "Idaho State");
		NCAABMapping.put("IL Chicago" , "UIC");
		NCAABMapping.put("ILLINOIS" , "Illinois");
		NCAABMapping.put("ILLINOIS ST" , "Illinois State");
		NCAABMapping.put("INCARNATE WORD" , "Incarnate Word");
		NCAABMapping.put("INDIANA" , "Indiana");
		NCAABMapping.put("INDIANA ST" , "Indiana State");
		NCAABMapping.put("Iona" , "Iona");
		NCAABMapping.put("IOWA" , "Iowa");
		NCAABMapping.put("IOWA ST" , "Iowa State");
		NCAABMapping.put("IPFW" , "Fort Wayne");
		NCAABMapping.put("IUPUI" , "IUPUI");
		NCAABMapping.put("JACKSON ST" , "Jackson State");
		NCAABMapping.put("JACKSONVILLE" , "Jacksonville");
		NCAABMapping.put("JACKSONVILLE ST" , "Jacksonville State");
		NCAABMapping.put("JAMES MADISON" , "James Madison");
		NCAABMapping.put("KANSAS" , "Kansas");
		NCAABMapping.put("KANSAS ST" , "Kansas State");
		NCAABMapping.put("KENNESAW" , "Kennesaw State");
		NCAABMapping.put("KENT" , "Kent State");
		NCAABMapping.put("KENTUCKY" , "Kentucky");
		NCAABMapping.put("La Salle" , "La Salle");
		NCAABMapping.put("LAFAYETTE" , "Lafayette");
		NCAABMapping.put("LAMAR" , "Lamar");
		NCAABMapping.put("LEHIGH" , "Lehigh");
		NCAABMapping.put("LIBERTY" , "Liberty");
		NCAABMapping.put("Lipscomb" , "Lipscomb");
		NCAABMapping.put("LONG BEACH ST" , "Long Beach State");
		NCAABMapping.put("Long Island" , "LIU Brooklyn");
		NCAABMapping.put("LIU Brooklyn" , "LIU Brooklyn");
		NCAABMapping.put("Longwood" , "Longwood");
		NCAABMapping.put("LOUISIANA TECH" , "Louisiana Tech");
		NCAABMapping.put("ULL" , "Louisiana");
		NCAABMapping.put("ULM" , "UL Monroe");
		NCAABMapping.put("LOUISVILLE" , "Louisville");
		NCAABMapping.put("Loy Marymount" , "Loyola Marymount");
		NCAABMapping.put("Loyola MD" , "Loyola (MD)");
		NCAABMapping.put("Loyola-Chicago" , "Loyola (Chi)");
		NCAABMapping.put("LSU" , "LSU");
		NCAABMapping.put("MA Lowell" , "UMass Lowell");
		NCAABMapping.put("MAINE" , "Maine");
		NCAABMapping.put("Manhattan" , "Manhattan");
		NCAABMapping.put("MARIST" , "Marist");
		NCAABMapping.put("Marquette" , "Marquette");
		NCAABMapping.put("MARSHALL" , "Marshall");
		NCAABMapping.put("MARYLAND" , "Maryland");
		NCAABMapping.put("MASSACHUSETTS" , "UMASS");
		NCAABMapping.put("MCNEESE ST" , "McNeese");
		NCAABMapping.put("MD E Shore" , "Maryland-Eastern Shore");
		NCAABMapping.put("MEMPHIS" , "Memphis");
		NCAABMapping.put("MERCER" , "Mercer");
		NCAABMapping.put("MIAMI FL" , "Miami");
		NCAABMapping.put("MIAMI OH" , "Miami (OH)");
		NCAABMapping.put("MICHIGAN" , "Michigan");
		NCAABMapping.put("MICHIGAN ST" , "Michigan State");
		NCAABMapping.put("MTSU" , "Middle Tennessee");
		NCAABMapping.put("MINNESOTA" , "Minnesota");
		NCAABMapping.put("MISSISSIPPI" , "Ole Miss");
		NCAABMapping.put("MISSISSIPPI ST" , "Mississippi State");
		NCAABMapping.put("MS VALLEY ST" , "MISSISSIPPI Valley State");
		NCAABMapping.put("MISSOURI" , "Missouri");
		NCAABMapping.put("Missouri KC" , "UMKC");
		NCAABMapping.put("MISSOURI ST" , "Missouri State");
		NCAABMapping.put("MONMOUTH NJ" , "Monmouth");
		NCAABMapping.put("MONTANA" , "Montana");
		NCAABMapping.put("MONTANA ST" , "Montana State");
		NCAABMapping.put("MOREHEAD ST" , "Morehead State");
		NCAABMapping.put("MORGAN ST" , "Morgan State");
		NCAABMapping.put("Mt St Mary's" , "Mt. St. Mary's");
		NCAABMapping.put("MURRAY ST" , "Murray State");
		NCAABMapping.put("N Kentucky" , "Northern Kentucky");
		NCAABMapping.put("NAVY" , "Navy");
		NCAABMapping.put("NE Omaha" , "Omaha");
		NCAABMapping.put("NEBRASKA" , "Nebraska");
		NCAABMapping.put("NEVADA" , "Nevada");
		NCAABMapping.put("NEW HAMPSHIRE" , "New Hampshire");
		NCAABMapping.put("NEW MEXICO" , "New Mexico");
		NCAABMapping.put("NEW MEXICO ST" , "New Mexico State");
		NCAABMapping.put("New Orleans" , "New Orleans");
		NCAABMapping.put("Niagara" , "Niagara");
		NCAABMapping.put("NICHOLLS ST" , "Nicholls State");
		NCAABMapping.put("NJIT" , "NJIT");
		NCAABMapping.put("NORFOLK ST" , "Norfolk State");
		NCAABMapping.put("NORTH CAROLINA" , "North Carolina");
		NCAABMapping.put("NC A&T" , "North Carolina A&amp;T");
		NCAABMapping.put("NC CENTRAL" , "North Carolina Central");
		NCAABMapping.put("NC STATE" , "NC State");
		NCAABMapping.put("NORTH DAKOTA" , "North Dakota");
		NCAABMapping.put("N DAKOTA ST" , "North Dakota State");
		NCAABMapping.put("NORTH FLORIDA" , "North Florida");
		NCAABMapping.put("NORTH TEXAS" , "North Texas");
		NCAABMapping.put("Northeastern" , "Northeastern");
		NCAABMapping.put("NORTHERN ARIZONA" , "Northern Arizona");
		NCAABMapping.put("N COLORADO" , "Northern Colorado");
		NCAABMapping.put("N ILLINOIS" , "Northern Illinois");
		NCAABMapping.put("NORTHERN IOWA" , "Northern Iowa");
		NCAABMapping.put("NORTHWESTERN" , "Northwestern");
		NCAABMapping.put("NORTHWESTERN LA" , "Northwestern State");
		NCAABMapping.put("NOTRE DAME" , "Notre Dame");
		NCAABMapping.put("Oakland" , "Oakland");
		NCAABMapping.put("OHIO" , "Ohio");
		NCAABMapping.put("OHIO ST" , "Ohio State");
		NCAABMapping.put("OKLAHOMA" , "Oklahoma");
		NCAABMapping.put("OKLAHOMA ST" , "Oklahoma State");
		NCAABMapping.put("OLD DOMINION" , "Old Dominion");
		NCAABMapping.put("Oral Roberts" , "Oral Roberts");
		NCAABMapping.put("OREGON" , "Oregon");
		NCAABMapping.put("OREGON ST" , "Oregon State");
		NCAABMapping.put("Pacific" , "Pacific");
		NCAABMapping.put("PENN ST" , "Penn State");
		NCAABMapping.put("Penn" , "PENNSYLVANIA");
		NCAABMapping.put("Pepperdine" , "Pepperdine");
		NCAABMapping.put("PITTSBURGH" , "Pittsburgh");
		NCAABMapping.put("Portland" , "Portland");
		NCAABMapping.put("PORTLAND ST" , "Portland State");
		NCAABMapping.put("PRAIRIE VIEW" , "Prairie View");
		NCAABMapping.put("PRESBYTERIAN" , "Presbyterian");
		NCAABMapping.put("PRINCETON" , "Princeton");
		NCAABMapping.put("Providence" , "Providence");
		NCAABMapping.put("PURDUE" , "Purdue");
		NCAABMapping.put("Quinnipiac" , "Quinnipiac");
		NCAABMapping.put("Radford" , "Radford");
		NCAABMapping.put("RHODE ISLAND" , "Rhode Island");
		NCAABMapping.put("RICE" , "Rice");
		NCAABMapping.put("RICHMOND" , "Richmond");
		NCAABMapping.put("Rider" , "Rider");
		NCAABMapping.put("ROBERT MORRIS" , "Robert Morris");
		NCAABMapping.put("RUTGERS" , "Rutgers");
		NCAABMapping.put("SACRED HEART" , "Sacred Heart");
		NCAABMapping.put("SAM HOUSTON ST" , "Sam Houston State");
		NCAABMapping.put("SAMFORD" , "Samford");
		NCAABMapping.put("SAN DIEGO" , "San Diego");
		NCAABMapping.put("SAN DIEGO ST" , "San Diego State");
		NCAABMapping.put("SAN FRANCISCO" , "San Francisco");
		NCAABMapping.put("SAN JOSE ST" , "San JosÃ© State");
		NCAABMapping.put("Santa Barbara" , "UC Santa Barbara");
		NCAABMapping.put("SANTA CLARA" , "Santa Clara");
		NCAABMapping.put("SAVANNAH ST" , "Savannah State");
		NCAABMapping.put("SC Upstate" , "South Carolina Upstate");
		NCAABMapping.put("Seattle" , "Seattle");
		NCAABMapping.put("Seton Hall" , "Seton Hall");
		NCAABMapping.put("Siena" , "Siena");
		NCAABMapping.put("SMU" , "SMU");
		NCAABMapping.put("SOUTH ALABAMA" , "South Alabama");
		NCAABMapping.put("SOUTH CAROLINA" , "South Carolina");
		NCAABMapping.put("S CAROLINA ST" , "South Carolina State");
		NCAABMapping.put("SOUTH DAKOTA" , "South Dakota");
		NCAABMapping.put("S DAKOTA ST" , "South Dakota State");
		NCAABMapping.put("SOUTH FLORIDA" , "South Florida");
		NCAABMapping.put("SE MISSOURI ST" , "SE MISSOURI STATE");
		NCAABMapping.put("SE LOUISIANA" , "SE Louisiana");
		NCAABMapping.put("SOUTHERN UNIV" , "Southern");
		NCAABMapping.put("S ILLINOIS" , "Southern Illinois");
		NCAABMapping.put("SOUTHERN MISS" , "Southern Miss");
		NCAABMapping.put("SOUTHERN UTAH" , "Southern Utah");
		NCAABMapping.put("St Bonaventure" , "St. Bonaventure");
		NCAABMapping.put("ST FRANCIS PA" , "St. Francis (PA)");
		NCAABMapping.put("St Francis NY" , "St. Francis BKN");
		NCAABMapping.put("St John's" , "St. John's");
		NCAABMapping.put("St Joseph's PA" , "Saint Joseph's");
		NCAABMapping.put("St Louis" , "MISSOURI-ST. LOUIS");
		NCAABMapping.put("St Mary's CA" , "Saint Mary's");
		NCAABMapping.put("St Peter's" , "Saint Peter's");
		NCAABMapping.put("STANFORD" , "Stanford");
		NCAABMapping.put("SF AUSTIN" , "Stephen F. Austin");
		NCAABMapping.put("STETSON" , "Stetson");
		NCAABMapping.put("STONY BROOK" , "Stony Brook");
		NCAABMapping.put("SYRACUSE" , "Syracuse");
		NCAABMapping.put("TAM C. Christi" , "Texas A&amp;M-CC");
		NCAABMapping.put("TCU" , "TCU");
		NCAABMapping.put("TEMPLE" , "Temple");
		NCAABMapping.put("TENNESSEE" , "Tennessee");
		NCAABMapping.put("TENNESSEE ST" , "Tennessee State");
		NCAABMapping.put("TENNESSEE TECH" , "Tennessee Tech");
		NCAABMapping.put("TEXAS" , "Texas");
		NCAABMapping.put("TEXAS A&M" , "Texas A&amp;M");
		NCAABMapping.put("TX SOUTHERN" , "Texas Southern");
		NCAABMapping.put("TEXAS ST" , "Texas State");
		NCAABMapping.put("TEXAS TECH" , "Texas Tech");
		NCAABMapping.put("CITADEL" , "The Citadel");
		NCAABMapping.put("TOLEDO" , "Toledo");
		NCAABMapping.put("TOWSON" , "Towson");
		NCAABMapping.put("TROY" , "Troy");
		NCAABMapping.put("TULANE" , "Tulane");
		NCAABMapping.put("TULSA" , "Tulsa");
		NCAABMapping.put("UAB" , "UAB");
		NCAABMapping.put("UC DAVIS" , "UC Davis");
		NCAABMapping.put("UC Irvine" , "UC Irvine");
		NCAABMapping.put("UC Riverside" , "UC Riverside");
		NCAABMapping.put("UCF" , "UCF");
		NCAABMapping.put("UCLA" , "UCLA");
		NCAABMapping.put("UMBC" , "UMBC");
		NCAABMapping.put("UNC Asheville" , "UNC Asheville");
		NCAABMapping.put("UNC Greensboro" , "UNC Greensboro");
		NCAABMapping.put("UNC Wilmington" , "UNC Wilmington");
		NCAABMapping.put("UNLV" , "UNLV");
		NCAABMapping.put("USC" , "USC");
		NCAABMapping.put("UT Arlington" , "UT Arlington");
		NCAABMapping.put("TN MARTIN" , "Tenn-Martin");
		NCAABMapping.put("UTAH" , "Utah");
		NCAABMapping.put("UTAH ST" , "Utah State");
		NCAABMapping.put("Utah Valley" , "Utah Valley");
		NCAABMapping.put("UTEP" , "UTEP");
		NCAABMapping.put("UTRGV" , "UT Rio Grande Valley");
		NCAABMapping.put("UT SAN ANTONIO" , "UTSA");
		NCAABMapping.put("VA Commonwealth" , "VCU");
		NCAABMapping.put("VALPARAISO" , "Valparaiso");
		NCAABMapping.put("VANDERBILT" , "Vanderbilt");
		NCAABMapping.put("Vermont" , "Vermont");
		NCAABMapping.put("VILLANOVA" , "Villanova");
		NCAABMapping.put("VIRGINIA" , "Virginia");
		NCAABMapping.put("VIRGINIA TECH" , "Virginia Tech");
		NCAABMapping.put("VMI" , "VMI");
		NCAABMapping.put("WAGNER" , "Wagner");
		NCAABMapping.put("WAKE FOREST" , "Wake Forest");
		NCAABMapping.put("WASHINGTON" , "Washington");
		NCAABMapping.put("WASHINGTON ST" , "Washington State");
		NCAABMapping.put("WEBER ST" , "Weber State");
		NCAABMapping.put("WEST VIRGINIA" , "West Virginia");
		NCAABMapping.put("W CAROLINA" , "Western Carolina");
		NCAABMapping.put("W ILLINOIS" , "Western Illinois");
		NCAABMapping.put("WESTERN KENTUCKY" , "WESTERN KENTUCKY");
		NCAABMapping.put("WKU" , "WESTERN KENTUCKY");
		NCAABMapping.put("W KENTUCKY" , "WESTERN KENTUCKY");
		NCAABMapping.put("W MICHIGAN" , "Western Michigan");
		NCAABMapping.put("WI Green Bay" , "Green Bay");
		NCAABMapping.put("WI Milwaukee" , "Milwaukee");
		NCAABMapping.put("Wichita St" , "Wichita State");
		NCAABMapping.put("WILLIAM & MARY" , "William &amp; Mary");
		NCAABMapping.put("Winthrop" , "Winthrop");
		NCAABMapping.put("WISCONSIN" , "Wisconsin");
		NCAABMapping.put("WOFFORD" , "Wofford");
		NCAABMapping.put("Wright St" , "Wright State");
		NCAABMapping.put("WYOMING" , "Wyoming");
		NCAABMapping.put("Xavier" , "Xavier");
		NCAABMapping.put("YALE" , "Yale");
		NCAABMapping.put("YOUNGSTOWN ST" , "Youngstown State");
	}

	/**
	 * Constructor
	 */
	public MasseyRatingsParser() {
		super();
		LOGGER.info("Entering MasseyRatingsParser()");
		LOGGER.info("Exiting MasseyRatingsParser()");
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
	 * @param week
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<MasseyRatingsNcaafData> parseNcaafRatings(String xhtml, Integer week, Integer year) throws BatchException {
		final List<MasseyRatingsNcaafData> data = new ArrayList<MasseyRatingsNcaafData>();
		
		int index = xhtml.indexOf("Mean Median St.Dev</a></font>");
		if (index != -1) {
			xhtml = xhtml.substring(index + "Mean Median St.Dev</a></font>".length());
			xhtml = xhtml.replace("<font color=\"#FF0000\">", "");
			xhtml = xhtml.replace("<font color=\"#0000FF\">", "");
			xhtml = xhtml.replace("<a href", "<ahref");
			xhtml = xhtml.replace("<font color=\"#0080FF\">LAB PIG DCI PGH DUN BRN MAS KPK KAM DII Rank, Team, Conf, Record SAG DOK PAY HOW TRP CTW RUD YAG BBT SMS FPI BCM BAS KLN PIR Rank, Team BDF ARG DP BIL DOL DWI MOR DES BMC RWP FEI CGV BWE NUT USA Rank, Team AP PFZ LAZ MAR KEL MGS ATC PPP CSL BOW LOG RTP DEZ HAT LSD <a href=\"../cf/aboutcomp.htm#key\"> Mean Median St.Dev</a></font>", "");

			index = xhtml.indexOf("--------------------");
			if (index != -1) {
				xhtml = xhtml.substring(0, index).trim() + " END";
				xhtml = xhtml.replace("</font>", "");
			}

			index = xhtml.indexOf(" ");
			MasseyRatingsNcaafData mrnd = null;
			int itemcounter = 0;
			int lastthreecounter = 0;
			boolean wasreset = false;
			while (index != -1) {
				String value = xhtml.substring(0, index).trim();
				LOGGER.debug("itemcounter: " + itemcounter);
				LOGGER.debug("value: " + value);
				switch (itemcounter++) {
					// LAB 
					case 0:
						mrnd = new MasseyRatingsNcaafData();
						mrnd.setWeek(week);
						mrnd.setYear(year);
						break;
					case 9:
						mrnd.setRank(Integer.parseInt(value));
						break;
					// Rank, 
					case 10:
						try {
//							LOGGER.error("value: " + value);
							mrnd.setRank(Integer.parseInt(value));
						} catch (Throwable t) {
							mrnd.setConf(value);
							LOGGER.error(t.getMessage(), t);
						}
						break;
					// Conf,
					case 11: {
							int rindex = value.indexOf("-");
							if (rindex != -1) {
								mrnd.setWins(Integer.parseInt(value.substring(0,rindex).trim()));
								mrnd.setLosses(Integer.parseInt(value.substring(rindex+1).trim()));
							} else {
								mrnd.setConf(value);
							}
						}
						break;
					// Record 
					case 12: {
						int rindex = value.indexOf("-");
							if (rindex != -1) {
								mrnd.setWins(Integer.parseInt(value.substring(0,rindex).trim()));
								mrnd.setLosses(Integer.parseInt(value.substring(rindex+1).trim()));
							}
						}
						break;
					default:
						if (value.contains(".")) {
							if (lastthreecounter == 0) {
								mrnd.setMean(Float.parseFloat(value));
								lastthreecounter++;
							} else if (lastthreecounter == 1) {
								mrnd.setMedian(Float.parseFloat(value));
								lastthreecounter++;
							} else if (lastthreecounter == 2) {
								if (value.contains("<font")) {
									value = value.replace("<font", "");
									int iindex = xhtml.indexOf("St.Dev</a>");
									if (iindex != -1) {
										xhtml = xhtml.substring(iindex + "St.Dev</a>".length()).trim();
//										LOGGER.error("xhtml: " + xhtml);
										wasreset = true;
									}
								}

								mrnd.setStDev(Float.parseFloat(value));
								data.add(mrnd);
								itemcounter = 0;
								lastthreecounter = 0;
							}
						}
						break;
				}

				if (!wasreset) {
					xhtml = xhtml.substring(index + 1).trim();
					if (xhtml.startsWith("<ahref")) {
						int rindex = xhtml.indexOf("\">");
						if (rindex != -1) {
							xhtml = xhtml.substring(rindex + 2);
							rindex = xhtml.indexOf("</a>");

							String team = xhtml.substring(0, rindex);
							Iterator<String> itr = NCAAFMapping.keySet().iterator();
							while (itr.hasNext()) {
								final String key = itr.next();
								if (key.toUpperCase().equals(team.toUpperCase())) {
									LOGGER.debug("key: " + key);
									LOGGER.debug("NCAAFMapping.get(key): " + NCAAFMapping.get(key));
									LOGGER.debug("NCAAFMapping.get(key).toUpperCase(): " + NCAAFMapping.get(key).toUpperCase());
									mrnd.setTeam(NCAAFMapping.get(key).toUpperCase());
								}
							}

							if (mrnd.getTeam() == null || mrnd.getTeam().length() == 0) {
								itr = NCAAFFCSMapping.keySet().iterator();
								while (itr.hasNext()) {
									final String key = itr.next();
									if (key.toUpperCase().equals(team.toUpperCase())) {
										LOGGER.debug("key: " + key);
										LOGGER.debug("NCAAFFCSMapping.get(key): " + NCAAFFCSMapping.get(key));
										LOGGER.debug("NCAAFFCSMapping.get(key).toUpperCase(): " + NCAAFFCSMapping.get(key).toUpperCase());
										mrnd.setTeam(NCAAFFCSMapping.get(key).toUpperCase());
									}
								}
							}

							if (mrnd.getTeam() == null || mrnd.getTeam().length() == 0) {
								mrnd.setTeam(team);
							}

							xhtml = xhtml.substring(rindex + 4).trim();
						}
					}
				} else {
					wasreset = false;
				}

				index = xhtml.indexOf(" ");
			}

			// 1 1 1 1 1 1 1 1 1 1 1 Alabama SEC 3-0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 
			// Alab 1 1 1 1 1 3 1 1 3 1 1 1 1 1 1 1 
			// Alab 1 1 1 1 1 1 1 1 1 2 1 2 1 1 1 1 
			// Alab 1 1 1 1 1 1 1 
		}

		return data;
	}

	/**
	 * 
	 * @param xhtml
	 * @param week
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<MasseyRatingsNcaafData> parseNcaafFCSRatings(String xhtml, Integer week, Integer year) throws BatchException {
		final List<MasseyRatingsNcaafData> data = new ArrayList<MasseyRatingsNcaafData>();
		
		int index = xhtml.indexOf("Mean Median St.Dev</a></font>");
		if (index != -1) {
			xhtml = xhtml.substring(index + "Mean Median St.Dev</a></font>".length());
			xhtml = xhtml.replace("<font color=\"#FF0000\">", "");
			xhtml = xhtml.replace("<font color=\"#0000FF\">", "");
			xhtml = xhtml.replace("<a href", "<ahref");
			xhtml = xhtml.replace("<font color=\"#0080FF\">LAB PIG DCI PGH DUN BRN MAS KPK KAM DII Rank, Team, Conf, Record SAG DOK PAY HOW TRP CTW RUD YAG BBT SMS FPI BCM BAS KLN PIR Rank, Team BDF ARG DP BIL DOL DWI MOR DES BMC RWP FEI CGV BWE NUT USA Rank, Team AP PFZ LAZ MAR KEL MGS ATC PPP CSL BOW LOG RTP DEZ HAT LSD <a href=\"../cf/aboutcomp.htm#key\"> Mean Median St.Dev</a></font>", "");

			index = xhtml.indexOf("--------------------");
			if (index != -1) {
				xhtml = xhtml.substring(0, index).trim() + " END";
				xhtml = xhtml.replace("</font>", "");
			}

			index = xhtml.indexOf(" ");
			MasseyRatingsNcaafData mrnd = null;
			int itemcounter = 0;
			int lastthreecounter = 0;
			boolean wasreset = false;
			while (index != -1) {
				String value = xhtml.substring(0, index).trim();
//				LOGGER.error("itemcounter: " + itemcounter);
//				LOGGER.error("value: " + value);
				switch (itemcounter++) {
					// LAB 
					case 0:
						mrnd = new MasseyRatingsNcaafData();
						mrnd.setWeek(week);
						mrnd.setYear(year);
						break;
					case 9:
						mrnd.setRank(Integer.parseInt(value));
						break;
					// Rank, 
					case 10:
						try {
//							LOGGER.error("value: " + value);
							mrnd.setRank(Integer.parseInt(value));
						} catch (Throwable t) {
							mrnd.setConf(value);
							LOGGER.error(t.getMessage(), t);
						}
						break;
					// Conf,
					case 11: {
							int rindex = value.indexOf("-");
							if (rindex != -1) {
								mrnd.setWins(Integer.parseInt(value.substring(0,rindex).trim()));
								mrnd.setLosses(Integer.parseInt(value.substring(rindex+1).trim()));
							} else {
								mrnd.setConf(value);
							}
						}
						break;
					// Record 
					case 12: {
						int rindex = value.indexOf("-");
							if (rindex != -1) {
								mrnd.setWins(Integer.parseInt(value.substring(0,rindex).trim()));
								mrnd.setLosses(Integer.parseInt(value.substring(rindex+1).trim()));
							}
						}
						break;
					default:
						if (value.contains(".")) {
							if (lastthreecounter == 0) {
								mrnd.setMean(Float.parseFloat(value));
								lastthreecounter++;
							} else if (lastthreecounter == 1) {
								mrnd.setMedian(Float.parseFloat(value));
								lastthreecounter++;
							} else if (lastthreecounter == 2) {
								if (value.contains("<font")) {
									value = value.replace("<font", "");
									int iindex = xhtml.indexOf("St.Dev</a>");
									if (iindex != -1) {
										xhtml = xhtml.substring(iindex + "St.Dev</a>".length()).trim();
//										LOGGER.error("xhtml: " + xhtml);
										wasreset = true;
									}
								}

								mrnd.setStDev(Float.parseFloat(value));
								data.add(mrnd);
								itemcounter = 0;
								lastthreecounter = 0;
							}
						}
						break;
				}

				if (!wasreset) {
					xhtml = xhtml.substring(index + 1).trim();
					if (xhtml.startsWith("<ahref")) {
						int rindex = xhtml.indexOf("\">");
						if (rindex != -1) {
							xhtml = xhtml.substring(rindex + 2);
							rindex = xhtml.indexOf("</a>");

							String team = xhtml.substring(0, rindex);
							mrnd.setTeam(team);

							xhtml = xhtml.substring(rindex + 4).trim();
						}
					}
				} else {
					wasreset = false;
				}

				index = xhtml.indexOf(" ");
			}

			// 1 1 1 1 1 1 1 1 1 1 1 Alabama SEC 3-0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 
			// Alab 1 1 1 1 1 3 1 1 3 1 1 1 1 1 1 1 
			// Alab 1 1 1 1 1 1 1 1 1 2 1 2 1 1 1 1 
			// Alab 1 1 1 1 1 1 1 
		}

		return data;
	}

	/**
	 * 
	 * @param xhtml
	 * @param week
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<MasseyRatingsNcaabData> parseNcaabRatings(String xhtml, Integer week, Integer year) throws BatchException {
		final List<MasseyRatingsNcaabData> data = new ArrayList<MasseyRatingsNcaabData>();
		
		int index = xhtml.indexOf("Mean Median St.Dev</a></font>");
		if (index != -1) {
			xhtml = xhtml.substring(index + "Mean Median St.Dev</a></font>".length());
			xhtml = xhtml.replace("<font color=\"#FF0000\">", "");
			xhtml = xhtml.replace("<font color=\"#0000FF\">", "");
			xhtml = xhtml.replace("<a href", "<ahref");
			xhtml = xhtml.replace("<font color=\"#0080FF\">LAB PIG DCI PGH DUN BRN MAS KPK KAM DII Rank, Team, Conf, Record SAG DOK PAY HOW TRP CTW RUD YAG BBT SMS FPI BCM BAS KLN PIR Rank, Team BDF ARG DP BIL DOL DWI MOR DES BMC RWP FEI CGV BWE NUT USA Rank, Team AP PFZ LAZ MAR KEL MGS ATC PPP CSL BOW LOG RTP DEZ HAT LSD <a href=\"../cf/aboutcomp.htm#key\"> Mean Median St.Dev</a></font>", "");

			index = xhtml.indexOf("--------------------");
			if (index != -1) {
				xhtml = xhtml.substring(0, index).trim() + " END";
				xhtml = xhtml.replace("</font>", "");
			}

			index = xhtml.indexOf(" ");
			MasseyRatingsNcaabData mrnd = null;
			int itemcounter = 0;
			int lastthreecounter = 0;
			boolean wasreset = false;
			while (index != -1) {
				String value = xhtml.substring(0, index).trim();
//				LOGGER.error("itemcounter: " + itemcounter);
//				LOGGER.error("value: " + value);
				switch (itemcounter++) {
					// LAB 
					case 0:
						mrnd = new MasseyRatingsNcaabData();
						mrnd.setWeek(week);
						mrnd.setYear(year);
						break;
					case 9:
						mrnd.setRank(Integer.parseInt(value));
						break;
					// Rank, 
					case 10:
						try {
//							LOGGER.error("value: " + value);
							mrnd.setRank(Integer.parseInt(value));
						} catch (Throwable t) {
							mrnd.setConf(value);
							LOGGER.debug(t.getMessage(), t);
						}
						break;
					// Conf,
					case 11: {
							int rindex = value.indexOf("-");
							if (rindex != -1) {
								mrnd.setWins(Integer.parseInt(value.substring(0,rindex).trim()));
								mrnd.setLosses(Integer.parseInt(value.substring(rindex+1).trim()));
							} else {
								mrnd.setConf(value);
							}
						}
						break;
					// Record 
					case 12: {
						int rindex = value.indexOf("-");
							if (rindex != -1) {
								mrnd.setWins(Integer.parseInt(value.substring(0,rindex).trim()));
								mrnd.setLosses(Integer.parseInt(value.substring(rindex+1).trim()));
							}
						}
						break;
					default:
						if (value.contains(".")) {
							if (lastthreecounter == 0) {
								mrnd.setMean(Float.parseFloat(value));
								lastthreecounter++;
							} else if (lastthreecounter == 1) {
								mrnd.setMedian(Float.parseFloat(value));
								lastthreecounter++;
							} else if (lastthreecounter == 2) {
								if (value.contains("<font")) {
									value = value.replace("<font", "");
									int iindex = xhtml.indexOf("St.Dev</a>");
									if (iindex != -1) {
										xhtml = xhtml.substring(iindex + "St.Dev</a>".length()).trim();
//										LOGGER.error("xhtml: " + xhtml);
										wasreset = true;
									}
								}

								mrnd.setStDev(Float.parseFloat(value));
								data.add(mrnd);
								itemcounter = 0;
								lastthreecounter = 0;
							}
						}
						break;
				}

				if (!wasreset) {
					xhtml = xhtml.substring(index + 1).trim();
					if (xhtml.startsWith("<ahref")) {
						int rindex = xhtml.indexOf("\">");
						if (rindex != -1) {
							xhtml = xhtml.substring(rindex + 2);
							rindex = xhtml.indexOf("</a>");

							String team = xhtml.substring(0, rindex);
							Iterator<String> itr = NCAABMapping.keySet().iterator();
							while (itr.hasNext()) {
								final String key = itr.next();
								if (key.toUpperCase().equals(team.toUpperCase())) {
									LOGGER.debug("key: " + key);
									LOGGER.debug("NCAABMapping.get(key): " + NCAABMapping.get(key));
									LOGGER.debug("NCAABMapping.get(key).toUpperCase(): " + NCAABMapping.get(key).toUpperCase());
									mrnd.setTeam(NCAABMapping.get(key).toUpperCase());
								}
							}

							if (mrnd.getTeam() == null || mrnd.getTeam().length() == 0) {
								mrnd.setTeam(team.toUpperCase());
							}

							xhtml = xhtml.substring(rindex + 4).trim();
						}
					}
				} else {
					wasreset = false;
				}

				index = xhtml.indexOf(" ");
			}

			// 1 1 1 1 1 1 1 1 1 1 1 Alabama SEC 3-0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 
			// Alab 1 1 1 1 1 3 1 1 3 1 1 1 1 1 1 1 
			// Alab 1 1 1 1 1 1 1 1 1 2 1 2 1 1 1 1 
			// Alab 1 1 1 1 1 1 1 
		}

		return data;
	}

	/**
	 * 
	 * @param xhtml
	 * @param week
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<MasseyRatingsNcaafData> parseNcaafXxxRatings(String xhtml, Integer week, Integer year) throws BatchException {
		final List<MasseyRatingsNcaafData> data = new ArrayList<MasseyRatingsNcaafData>();
		
		int index = xhtml.indexOf("Mean Median St.Dev</a></font>");
		if (index != -1) {
			xhtml = xhtml.substring(index + "Mean Median St.Dev</a></font>".length());
			xhtml = xhtml.replace("<font color=\"#FF0000\">", "");
			xhtml = xhtml.replace("<font color=\"#0000FF\">", "");
			xhtml = xhtml.replace("</a>", "");
			xhtml = xhtml.replace("<a href", "<ahref");
//			LOGGER.error("xhtml: " + xhtml);

			index = xhtml.indexOf("</pre>");
			if (index != -1) {
				xhtml = xhtml.substring(0, index).trim();
				xhtml = xhtml.replace("</font>", "");
			}

			index = xhtml.indexOf(" ");
			MasseyRatingsNcaafData mrnd = null;
			int itemcounter = 0;
			while (index != -1) {
				LOGGER.debug("xhtml: " + xhtml);
				String value = xhtml.substring(0, index).trim();
				LOGGER.debug("value: " + value);
				switch (itemcounter++) {
					// LAB 
					case 0:
						mrnd = new MasseyRatingsNcaafData();
						mrnd.setWeek(week);
						mrnd.setYear(year);
						mrnd.setLAB(Integer.parseInt(value));
						break;
					// ACU 
					case 1:
						mrnd.setACU(Integer.parseInt(value));
						break;
					// DUN 
					case 2:
						mrnd.setDUN(Integer.parseInt(value));
						break;
					// MAS 
					case 3:
						mrnd.setMAS(Integer.parseInt(value));
						break;
					// SMS 
					case 4:
						mrnd.setSMS(Integer.parseInt(value));
						break;
					// SOR 
					case 5:
						mrnd.setSOR(Integer.parseInt(value));
						break;
					// DCI
					case 6:
						mrnd.setDCI(Integer.parseInt(value));
						break;
					// DII 
					case 7:
						mrnd.setDII(Integer.parseInt(value));
						break;
					// BBT 
					case 8:
						mrnd.setBBT(Integer.parseInt(value));
						break;
					// HOW 
					case 9:
						mrnd.setHOW(Integer.parseInt(value));
						break;
					// Rank, 
					case 10:
						mrnd.setRank(Integer.parseInt(value));
						break;
					// Team,
					case 11:
/*						//<a href="../team.php?t=74&s=300937">Alabama</a>
						index = xhtml.indexOf("<a href=\"../team.php?");
						final StringBuffer sb = new StringBuffer(10000);
						while (index != -1) {
							int tempindex = xhtml.indexOf(">");
							sb.append(xhtml.substring(0, index));
							sb.append(xhtml.substring(tempindex + 1));
							xhtml = sb.toString();
							index = xhtml.indexOf("<a href=\"../team.php?");
						}
						xhtml = sb.toString();
*/
						mrnd.setTeam(value);
						break;
					// Conf,
					case 12:
						mrnd.setConf(value);
						break;
					// Record 
					case 13:
						int rindex = value.indexOf("-");
						if (rindex != -1) {
							mrnd.setWins(Integer.parseInt(value.substring(0,rindex).trim()));
							mrnd.setLosses(Integer.parseInt(value.substring(rindex+1).trim()));
						}
						break;
					// SAG 
					case 14:
						mrnd.setSAG(Integer.parseInt(value));
						break;
					// RUD 
					case 15:
						mrnd.setRUD(Integer.parseInt(value));
						break;
					// BRN
					case 16:
						mrnd.setBRN(Integer.parseInt(value));
						break;
					// PIG
					case 17:
						mrnd.setPIG(Integer.parseInt(value));
						break;
					// ARG 
					case 18:
						mrnd.setARG(Integer.parseInt(value));
						break;
					// BDF 
					case 19:
						mrnd.setBDF(Integer.parseInt(value));
						break;
					// PGH 
					case 20:
						mrnd.setPGH(Integer.parseInt(value));
						break;
					// TRP 
					case 21:
						mrnd.setTRP(Integer.parseInt(value));
						break;
					// MOR 
					case 22:
						mrnd.setMOR(Integer.parseInt(value));
						break;
					// DOK 
					case 23:
						mrnd.setDOK(Integer.parseInt(value));
						break;
					// CTW 
					case 24:
						mrnd.setCTW(Integer.parseInt(value));
						break;
					// BWE 
					case 25:
						mrnd.setBWE(Integer.parseInt(value));
						break;
					// DP 
					case 26:
						mrnd.setDP(Integer.parseInt(value));
						break;
					// FPI 
					case 27:
						mrnd.setFPI(Integer.parseInt(value));
						break;
					// KAM 
					case 28:
						mrnd.setKAM(Integer.parseInt(value));
						break;
					// Rank, 
					case 29:
//						mrnd.setR(Integer.parseInt(value));
						break;
					// Team 
					case 30:
//						mrnd.setMAS(Integer.parseInt(value));
						break;
					// PAY 
					case 31:
						mrnd.setPAY(Integer.parseInt(value));
						break;
					// RBA 
					case 32:
						mrnd.setRBA(Integer.parseInt(value));
						break;
					// SFX 
					case 33:
						mrnd.setSFX(Integer.parseInt(value));
						break;
					// KLN 
					case 34:
						mrnd.setKLN(Integer.parseInt(value));
						break;
					// KPK 
					case 35:
						mrnd.setKPK(Integer.parseInt(value));
						break;
					// CGV 
					case 36:
						mrnd.setCGV(Integer.parseInt(value));
						break;
					// BAS 
					case 37:
						mrnd.setBAS(Integer.parseInt(value));
						break;
					// KEL 
					case 38:
						mrnd.setKEL(Integer.parseInt(value));
						break;
					// BIL 
					case 39:
						mrnd.setBIL(Integer.parseInt(value));
						break;
					// PIR 
					case 40:
						mrnd.setPIR(Integer.parseInt(value));
						break;
					// YAG 
					case 41:
						mrnd.setYAG(Integer.parseInt(value));
						break;
					// RTP 
					case 42:
						mrnd.setRTP(Integer.parseInt(value));
						break;
					// BMC 
					case 43:
						mrnd.setBMC(Integer.parseInt(value));
						break;
					// S&P 
					case 44:
						mrnd.setSandP(Integer.parseInt(value));
						break;
					// DOL 
					case 45:
						mrnd.setDOL(Integer.parseInt(value));
						break;
					// Rank, 
					case 46:
//						mrnd.setMAS(Integer.parseInt(value));
						break;
					// Team 
					case 47:
//						mrnd.setMAS(Integer.parseInt(value));
						break;
					// BCM 
					case 48:
						mrnd.setBCM(Integer.parseInt(value));
						break;
					// RWP 
					case 49:
						mrnd.setRWP(Integer.parseInt(value));
						break;
					// NUT 
					case 50:
						mrnd.setNUT(Integer.parseInt(value));
						break;
					// TFG 
					case 51:
						mrnd.setTFG(Integer.parseInt(value));
						break;
					// PFZ 
					case 52:
						mrnd.setPFZ(Integer.parseInt(value));
						break;
					// DES 
					case 53:
						mrnd.setDES(Integer.parseInt(value));
						break;
					// DWI 
					case 54:
						mrnd.setDWI(Integer.parseInt(value));
						break;
					// BOW 
					case 55:
						mrnd.setBOW(Integer.parseInt(value));
						break;
					// TPR 
					case 56:
						mrnd.setTPR(Integer.parseInt(value));
						break;
					// FEI 
					case 57:
						mrnd.setFEI(Integer.parseInt(value));
						break;
					// LOG 
					case 58:
						mrnd.setLOG(Integer.parseInt(value));
						break;
					// ENG 
					case 59:
						mrnd.setENG(Integer.parseInt(value));
						break;
					// DEZ 
					case 60:
						mrnd.setDEZ(Integer.parseInt(value));
						break;
					// PPP 
					case 61:
						mrnd.setPPP(Integer.parseInt(value));
						break;
					// MGS 
					case 62:
						mrnd.setMGS(Integer.parseInt(value));
						break;
					// Rank, 
					case 63:
//						mrnd.setMAS(Integer.parseInt(value));
						break;
					// Team 
					case 64:
//						mrnd.setMAS(Integer.parseInt(value));
						break;
					// RT 
					case 65:
						mrnd.setRT(Integer.parseInt(value));
						break;
					// YCM 
					case 66:
						mrnd.setYCM(Integer.parseInt(value));
						break;
					// AP 
					case 67:
						mrnd.setAP(Integer.parseInt(value));
						break;
					// USA 
					case 68:
						mrnd.setUSA(Integer.parseInt(value));
						break;
					// KEN 
					case 69:
						mrnd.setKEN(Integer.parseInt(value));
						break;
					// MGN 
					case 70:
						mrnd.setMGN(Integer.parseInt(value));
						break;
					// LAZ 
					case 71:
						mrnd.setLAZ(Integer.parseInt(value));
						break;
					// CSL 
					case 72:
						mrnd.setCSL(Integer.parseInt(value));
						break;
					// HAT 
					case 73:
						mrnd.setHAT(Integer.parseInt(value));
						break;
					// BSS 
					case 74:
						mrnd.setBSS(Integer.parseInt(value));
						break;
					// LSD 
					case 75:
						mrnd.setLSD(Integer.parseInt(value));
						break;
					// MAR
					case 76:
						mrnd.setMAR(Integer.parseInt(value));
						break;
					// Mean 
					case 77:
						mrnd.setMean(Float.parseFloat(value));
						break;
					// Median 
					case 78:
						mrnd.setMedian(Float.parseFloat(value));
						break;
					// St.Dev
					case 79:
						mrnd.setStDev(Float.parseFloat(value));
						data.add(mrnd);
						itemcounter = 0;
						break;
					default:
						break;
				}
				xhtml = xhtml.substring(index + 1).trim();
				LOGGER.debug("xhtml: " + xhtml);
				index = xhtml.indexOf(" ");
			}

			// 1 1 1 1 1 1 1 1 1 1 1 Alabama SEC 3-0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 
			// Alab 1 1 1 1 1 3 1 1 3 1 1 1 1 1 1 1 
			// Alab 1 1 1 1 1 1 1 1 1 2 1 2 1 1 1 1 
			// Alab 1 1 1 1 1 1 1 
		}

		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.sites.SiteParser#parseIndex(java.lang.String)
	 */
	@Override
	public Map<String, String> parseIndex(String xhtml) throws BatchException {
		final Map<String, String> map = new HashMap<String, String>();
		
		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray items = obj.getJSONArray("DI");
		LOGGER.debug("array size: " + items.length());
		for (int y = 0; y < items.length(); y++) {
			JSONArray sitems = items.getJSONArray(y);
			JSONArray titems = sitems.getJSONArray(0);
			String team = titems.getString(0);
			String url = titems.getString(2);
			map.put(team, url);
		}

		return map;
	}

	/**
	 * 
	 * @param teamName
	 * @param xhtml
	 * @throws BatchException
	 */
	public TeamData parseGameInfo(String teamName, String xhtml) throws BatchException {
		final TeamData teamData = new TeamData();
		teamData.setTeam(teamName);

		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray items = obj.getJSONArray("DI");
		for (int y = 0; y < items.length(); y++) {
			final JSONArray sitems = items.getJSONArray(y);
				LOGGER.debug("sitems: " + sitems);
				final JSONArray zitems = sitems.getJSONArray(3);
				final MasseyRatingsData resultData = new MasseyRatingsData();

				final String team = zitems.getString(0);
				LOGGER.debug("team: " + team);
				resultData.setTeam(teamName);
				resultData.setOpponentTeam(team);

				final String dateString = sitems.getString(1);
				resultData.setDateString(dateString);
				try {
					Date dateOfGame = PENDING_DATE_FORMAT.get().parse(dateString);
					resultData.setDateOfGame(dateOfGame);
				} catch (ParseException pe) {
					LOGGER.error(pe.getMessage(), pe);
				}

				final String away = sitems.getString(2);
				if (away != null && away.equals("at")) {
					resultData.setAwayGame(true);
				}

				final JSONArray gitems = sitems.getJSONArray(7);
				final String gameResult = gitems.getString(0);
				if (gameResult != null && (gameResult.equals("W") || gameResult.equals("L"))) {
					resultData.setGameComplete(true);
					resultData.setUrlData(gitems.getString(2));
				}

				if (resultData.isGameComplete()) {
					resultData.setScore1(sitems.getInt(9));
					resultData.setScore2(sitems.getInt(10));
				} else {
					resultData.setPointsfor(sitems.getInt(9));
					resultData.setPointsagainst(sitems.getInt(10));
				}

				LOGGER.debug("ResultData: " + resultData);
				teamData.addResult(resultData);
		}

		LOGGER.debug("teamData: " + teamData);
		return teamData;
	}

	/**
	 * 
	 * @param resultData
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public MasseyRatingsData parseGameResults(MasseyRatingsData resultData, String xhtml) throws BatchException {
		boolean isfirst = true;
		// tname=["Florida St","Fordham"];
		int tindex = xhtml.indexOf("tname=[\"");
		if (tindex != -1) {
			String txhtml = xhtml.substring(tindex + "tname=[\"".length());
			tindex = xhtml.indexOf("\"]");
			if (tindex != -1) {
				txhtml = txhtml.substring(0, tindex);
				tindex = txhtml.indexOf("\",\"");
				if (tindex != -1) {
					if (txhtml.substring(0, tindex).equals(resultData.getTeam())) {
						isfirst = true;
					} else {
						isfirst = false;
					}
 				}
			}
		}
		
		// mle=[60,75];
		int index = xhtml.indexOf("mle=[");
		if (index != -1) {
			xhtml = xhtml.substring(index + "mle=[".length());
			index = xhtml.indexOf("]");
			if (index != -1) {
				xhtml = xhtml.substring(0, index);
				index = xhtml.indexOf(",");
				if (index != -1) {
					if (isfirst) {
						resultData.setPointsfor(Integer.parseInt(xhtml.substring(0, index)));
						resultData.setPointsagainst(Integer.parseInt(xhtml.substring(index + 1)));						
					} else {
						resultData.setPointsagainst(Integer.parseInt(xhtml.substring(0, index)));
						resultData.setPointsfor(Integer.parseInt(xhtml.substring(index + 1)));
					}
				}
			}
		}

		return resultData;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public Map<String, String> parseComposite(String xhtml) throws BatchException {
		LOGGER.info("Entering parseComposite()");
		final Map<String, String> map = new HashMap<String, String>();
		final Document doc = parseXhtml(xhtml);

		final Elements as = doc.select("pre a");
		if (as != null) {
			LOGGER.debug("Valid element");
			int count = 0;
			for (Element a : as) {
				final String href = a.attr("href");
				LOGGER.debug("href: " + href);
				if (href != null && href.contains("team.php?t=")) {
					count++;
					String keyName = null;
					String teamName = a.html().trim();
					final Iterator<String> itr = NCAABMapping.keySet().iterator();
					while (itr.hasNext()) {
						final String key = itr.next();
						if (key.equals(teamName)) {
							keyName = NCAABMapping.get(key);
						}
					}

					if (keyName == null) {
						LOGGER.warn("KEYNAME: " + teamName + " not found");
					}
					map.put(keyName, Integer.toString(count));
				}
			}
		}

		LOGGER.info("Exiting parseComposite()");
		return map;
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
/**
 * 
 */
package com.wooanalytics.dao.sites.masseyratings;

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

import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.services.dao.sites.SiteParser;
import com.wootechnologies.services.dao.sites.SiteTeamPackage;


/**
 * @author jmiller
 *
 */
public class MasseyRatingsParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(MasseyRatingsParser.class);
	private static final Map<String, String> NFLMapping = new HashMap<String, String>();
	private static final Map<String, String> MLBMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAABMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAABOldMapping = new HashMap<String, String>();
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
		NCAAFMapping.put("Appalachian St","Appalachian St.");
		NCAAFMapping.put("Arizona","Arizona");
		NCAAFMapping.put("Arizona St","Arizona St.");
		NCAAFMapping.put("Arkansas","Arkansas");
		NCAAFMapping.put("Arkansas St.","Arkansas St.");
		NCAAFMapping.put("Arkansas St","Arkansas St.");
		NCAAFMapping.put("Army","Army");
		NCAAFMapping.put("Auburn","Auburn");
		NCAAFMapping.put("Ball St","Ball St.");
		NCAAFMapping.put("Baylor","Baylor");
		NCAAFMapping.put("Boise St","Boise St.");
		NCAAFMapping.put("Boise St.","Boise St.");
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
		NCAAFMapping.put("Colorado St","Colorado St.");
		NCAAFMapping.put("Connecticut","Connecticut");
		NCAAFMapping.put("Duke","Duke");
		NCAAFMapping.put("East Carolina","East Carolina");
		NCAAFMapping.put("E Michigan","Eastern Michigan");
		NCAAFMapping.put("FL Atlantic","Florida Atlantic");
		NCAAFMapping.put("Florida","Florida");
		NCAAFMapping.put("Florida Intl","FIU");
		NCAAFMapping.put("Florida St","Florida St.");
		NCAAFMapping.put("Fresno St","Fresno St.");
		NCAAFMapping.put("Ga Southern","Georgia Southern");
		NCAAFMapping.put("Georgia Tech","Georgia Tech");
		NCAAFMapping.put("Georgia","Georgia");
		NCAAFMapping.put("Georgia St.","Georgia St.");
		NCAAFMapping.put("Georgia St","Georgia St.");
		NCAAFMapping.put("Hawaii","Hawaii");
		NCAAFMapping.put("Houston","Houston");
		NCAAFMapping.put("Illinois","Illinois");
		NCAAFMapping.put("Indiana","Indiana");
		NCAAFMapping.put("Iowa","Iowa");
		NCAAFMapping.put("Iowa St","Iowa St.");
		NCAAFMapping.put("Kansas","Kansas");
		NCAAFMapping.put("Kansas St","Kansas St.");
		NCAAFMapping.put("Kent","Kent St.");
		NCAAFMapping.put("Kentucky","Kentucky");
		NCAAFMapping.put("ULL","Louisiana");
		NCAAFMapping.put("ULM","Louisiana Monroe");
		NCAAFMapping.put("Louisiana Tech","Louisiana Tech");
		NCAAFMapping.put("Louisville","Louisville");
		NCAAFMapping.put("LSU","LSU");
		NCAAFMapping.put("Liberty","Liberty");
		NCAAFMapping.put("Marshall","Marshall");
		NCAAFMapping.put("Maryland","Maryland");
		NCAAFMapping.put("Memphis","Memphis");
		NCAAFMapping.put("Miami FL","Miami FL");
		NCAAFMapping.put("Miami OH","Miami OH");
		NCAAFMapping.put("Michigan","Michigan");
		NCAAFMapping.put("Michigan St","Michigan St.");
		NCAAFMapping.put("MTSU","Middle Tennessee");
		NCAAFMapping.put("Middle Tenn St","Middle Tennessee");
		NCAAFMapping.put("Minnesota","Minnesota");
		NCAAFMapping.put("Mississippi St","Mississippi St.");
		NCAAFMapping.put("Mississippi","Mississippi");
		NCAAFMapping.put("Missouri","Missouri");
		NCAAFMapping.put("N Illinois","Northern Illinois");
		NCAAFMapping.put("North Carolina","North Carolina");
		NCAAFMapping.put("Navy","Navy");
		NCAAFMapping.put("NC State","North Carolina St.");
		NCAAFMapping.put("Nebraska","Nebraska");
		NCAAFMapping.put("Nevada","Nevada");
		NCAAFMapping.put("New Mexico","New Mexico");
		NCAAFMapping.put("New Mexico St","New Mexico St.");
		NCAAFMapping.put("North Texas","North Texas");
		NCAAFMapping.put("Northwestern","Northwestern");
		NCAAFMapping.put("Notre Dame","Notre Dame");
		NCAAFMapping.put("Ohio","Ohio");
		NCAAFMapping.put("Ohio St","Ohio St.");
		NCAAFMapping.put("Oklahoma","Oklahoma");
		NCAAFMapping.put("Oklahoma St","Oklahoma St.");
		NCAAFMapping.put("Old Dominion","Old Dominion");
		NCAAFMapping.put("Oregon","Oregon");
		NCAAFMapping.put("Oregon St","Oregon St.");
		NCAAFMapping.put("Penn St","Penn St.");
		NCAAFMapping.put("Pittsburgh","Pittsburgh");
		NCAAFMapping.put("Purdue","Purdue");
		NCAAFMapping.put("Rice","Rice");
		NCAAFMapping.put("Rutgers","Rutgers");
		NCAAFMapping.put("San Diego St","San Diego St.");
		NCAAFMapping.put("South Alabama","South Alabama");
		NCAAFMapping.put("South Carolina","South Carolina");
		NCAAFMapping.put("South Florida","South Florida");
		NCAAFMapping.put("SMU","SMU");		
		NCAAFMapping.put("Southern Miss","Southern Miss");
		NCAAFMapping.put("San Jose St","San Jose St.");
		NCAAFMapping.put("Stanford","Stanford");
		NCAAFMapping.put("Syracuse","Syracuse");
		NCAAFMapping.put("TCU","TCU");
		NCAAFMapping.put("Temple","Temple");
		NCAAFMapping.put("Tennessee","Tennessee");
		NCAAFMapping.put("Texas","Texas");
		NCAAFMapping.put("Texas A&M","TEXAS A&M");
		NCAAFMapping.put("Texas AM","TEXAS A&M");
		NCAAFMapping.put("Texas St","Texas St.");
		NCAAFMapping.put("Texas Tech","Texas Tech");
		NCAAFMapping.put("Toledo","Toledo");
		NCAAFMapping.put("Troy","Troy");
		NCAAFMapping.put("Tulane","Tulane");
		NCAAFMapping.put("Tulsa","Tulsa");
		NCAAFMapping.put("UTEP","UTEP");
		NCAAFMapping.put("UT San Antonio","UTSA");
		NCAAFMapping.put("Massachusetts","Massachusetts");
		NCAAFMapping.put("UAB","UAB");
		NCAAFMapping.put("UCLA","UCLA");
		NCAAFMapping.put("UCF","UCF");
		NCAAFMapping.put("UNLV","UNLV");
		NCAAFMapping.put("USC","USC");
		NCAAFMapping.put("Utah","Utah");
		NCAAFMapping.put("Utah St","Utah St.");
		NCAAFMapping.put("Virginia Tech","Virginia Tech");
		NCAAFMapping.put("Vanderbilt","Vanderbilt");
		NCAAFMapping.put("Virginia","Virginia");
		NCAAFMapping.put("WKU","Western Kentucky");
		NCAAFMapping.put("W Kentucky","Western Kentucky");
		NCAAFMapping.put("W Michigan","Western Michigan");
		NCAAFMapping.put("West Virginia","West Virginia");
		NCAAFMapping.put("Wake Forest","Wake Forest");
		NCAAFMapping.put("Washington St","Washington St.");
		NCAAFMapping.put("Washington","Washington");
		NCAAFMapping.put("Wisconsin","Wisconsin");
		NCAAFMapping.put("Wyoming","Wyoming");		
		
		NCAAFFCSMapping.put("ABILENE CHR", "ABILENE CHRISTIAN");
		NCAAFFCSMapping.put("ABILENE CHRISTIAN", "ABILENE CHRISTIAN");
		NCAAFFCSMapping.put("ALABAMA A&M", "ALABAMA A&M");
		NCAAFFCSMapping.put("ALABAMA ST", "ALABAMA ST.");
		NCAAFFCSMapping.put("Albany NY", "ALBANY");
		NCAAFFCSMapping.put("SUNY Albany", "ALBANY");
		NCAAFFCSMapping.put("ALCORN ST", "ALCORN ST.");
		NCAAFFCSMapping.put("ARK PINE BLUFF", "ARKANSAS-PINE BLUFF");
		NCAAFFCSMapping.put("AUSTIN PEAY", "AUSTIN PEAY ST.");
		NCAAFFCSMapping.put("BETHUNE-COOKMAN", "BETHUNE-COOKMAN");
		NCAAFFCSMapping.put("BROWN", "BROWN");
		NCAAFFCSMapping.put("BRYANT", "BRYANT UNIVERSITY");
		NCAAFFCSMapping.put("BUCKNELL", "BUCKNELL");
		NCAAFFCSMapping.put("BUTLER", "BUTLER");
		NCAAFFCSMapping.put("CAL POLY", "Cal Poly-Slo");
		NCAAFFCSMapping.put("Cal Poly SLO", "Cal Poly-Slo");
		NCAAFFCSMapping.put("CAMPBELL", "CAMPBELL");
		NCAAFFCSMapping.put("CENT ARKANSAS", "CENTRAL ARKANSAS");
		NCAAFFCSMapping.put("CENTRAL CONN", "CENTRAL CONNECTICUT");
		NCAAFFCSMapping.put("CHARLESTON SO", "CHARLESTON SOUTHERN");
		NCAAFFCSMapping.put("CHATTANOOGA", "TENNESSEE-CHATTANOOGA");
		NCAAFFCSMapping.put("CITADEL", "THE CITADEL");
		NCAAFFCSMapping.put("COLGATE", "COLGATE");
		NCAAFFCSMapping.put("COLUMBIA", "COLUMBIA");
		NCAAFFCSMapping.put("CORNELL", "CORNELL");
		NCAAFFCSMapping.put("CS Sacramento", "California State-Sacramento");
		NCAAFFCSMapping.put("DARTMOUTH", "DARTMOUTH");
		NCAAFFCSMapping.put("DAVIDSON", "DAVIDSON COLLEGE");
		NCAAFFCSMapping.put("DAYTON", "DAYTON");
		NCAAFFCSMapping.put("DELAWARE", "DELAWARE");
		NCAAFFCSMapping.put("DELAWARE ST", "DELAWARE ST.");
		NCAAFFCSMapping.put("DRAKE", "DRAKE");
		NCAAFFCSMapping.put("DUQUESNE", "DUQUESNE");
		NCAAFFCSMapping.put("E ILLINOIS", "EASTERN ILLINOIS");
		NCAAFFCSMapping.put("E KENTUCKY", "EASTERN KENTUCKY");
		NCAAFFCSMapping.put("E WASHINGTON", "EASTERN WASHINGTON");
		NCAAFFCSMapping.put("ELON", "ELON");
		NCAAFFCSMapping.put("ETSU", "EAST TENNESSEE ST.");
		NCAAFFCSMapping.put("FLORIDA A&M", "FLORIDA A&M");
		NCAAFFCSMapping.put("FORDHAM", "FORDHAM");
		NCAAFFCSMapping.put("FURMAN", "FURMAN");
		NCAAFFCSMapping.put("GARDNER WEBB", "GARDNER-WEBB");
		NCAAFFCSMapping.put("GEORGIA STATE","GEORGIA ST.");
		NCAAFFCSMapping.put("GEORGETOWN", "GEORGETOWN");
		NCAAFFCSMapping.put("GRAMBLING", "GRAMBLING STATE");
		NCAAFFCSMapping.put("HAMPTON", "HAMPTON");
		NCAAFFCSMapping.put("HARVARD", "HARVARD");
		NCAAFFCSMapping.put("HOLY CROSS", "HOLY CROSS");
		NCAAFFCSMapping.put("HOUSTON BAP", "HOUSTON BAPTIST");
		NCAAFFCSMapping.put("HOWARD", "HOWARD");
		NCAAFFCSMapping.put("IDAHO", "IDAHO");
		NCAAFFCSMapping.put("IDAHO ST", "IDAHO ST.");
		NCAAFFCSMapping.put("ILLINOIS ST", "ILLINOIS ST.");
		NCAAFFCSMapping.put("INCARNATE WORD", "INCARNATE WORD");
		NCAAFFCSMapping.put("INDIANA ST", "INDIANA ST.");
		NCAAFFCSMapping.put("JACKSON ST", "JACKSON ST.");
		NCAAFFCSMapping.put("JACKSONVILLE", "JACKSONVILLE");
		NCAAFFCSMapping.put("JACKSONVILLE ST", "JACKSONVILLE ST.");
		NCAAFFCSMapping.put("JAMES MADISON", "JAMES MADISON");
		NCAAFFCSMapping.put("KENNESAW", "KENNESAW ST.");
		NCAAFFCSMapping.put("LAFAYETTE", "LAFAYETTE");
		NCAAFFCSMapping.put("LAMAR", "LAMAR");
		NCAAFFCSMapping.put("LEHIGH", "LEHIGH");
		NCAAFFCSMapping.put("MAINE", "MAINE");
		NCAAFFCSMapping.put("MARIST", "MARIST");
		NCAAFFCSMapping.put("MCNEESE ST", "MCNEESE");
		NCAAFFCSMapping.put("MERCER", "MERCER");
		NCAAFFCSMapping.put("MISSOURI ST", "MISSOURI ST.");
		NCAAFFCSMapping.put("MONMOUTH NJ", "MONMOUTH-NEW JERSEY");
		NCAAFFCSMapping.put("MONTANA", "MONTANA");
		NCAAFFCSMapping.put("MONTANA ST", "MONTANA ST.");
		NCAAFFCSMapping.put("MOREHEAD ST", "MOREHEAD ST.");
		NCAAFFCSMapping.put("Morgan St", "MORGAN ST.");
		NCAAFFCSMapping.put("MS Valley St", "Mississippi Valley St.");
		NCAAFFCSMapping.put("MURRAY ST", "MURRAY ST.");
		NCAAFFCSMapping.put("N COLORADO", "NORTHERN COLORADO");
		NCAAFFCSMapping.put("N DAKOTA ST", "NORTH DAKOTA ST.");
		NCAAFFCSMapping.put("NC A&T", "North Carolina A&T");
		NCAAFFCSMapping.put("NC CENTRAL", "NORTH CAROLINA CENTRAL");
		NCAAFFCSMapping.put("NEW HAMPSHIRE", "NEW HAMPSHIRE");
		NCAAFFCSMapping.put("NICHOLLS ST", "NICHOLLS");
		NCAAFFCSMapping.put("NORFOLK ST", "NORFOLK ST.");
		NCAAFFCSMapping.put("NORTH ALABAMA", "NORTH ALABAMA");
		NCAAFFCSMapping.put("NORTH DAKOTA", "NORTH DAKOTA");
		NCAAFFCSMapping.put("NORTHERN ARIZONA", "NORTHERN ARIZONA");
		NCAAFFCSMapping.put("NORTHERN IOWA", "NORTHERN IOWA");
		NCAAFFCSMapping.put("NORTHWESTERN LA", "NORTHWESTERN ST.");
		NCAAFFCSMapping.put("PENN", "PENNSYLVANIA");
		NCAAFFCSMapping.put("PORTLAND ST", "PORTLAND ST.");
		NCAAFFCSMapping.put("PRAIRIE VIEW", "PRAIRIE VIEW A&M");
		NCAAFFCSMapping.put("PRESBYTERIAN", "PRESBYTERIAN COLLEGE");
		NCAAFFCSMapping.put("PRINCETON", "PRINCETON");
		NCAAFFCSMapping.put("RHODE ISLAND", "RHODE ISLAND");
		NCAAFFCSMapping.put("RICHMOND", "RICHMOND");
		NCAAFFCSMapping.put("ROBERT MORRIS", "ROBERT MORRIS");
		NCAAFFCSMapping.put("S CAROLINA ST", "SOUTH CAROLINA ST.");
		NCAAFFCSMapping.put("S DAKOTA ST", "SOUTH DAKOTA ST.");
		NCAAFFCSMapping.put("S ILLINOIS", "SOUTHERN ILLINOIS");
		NCAAFFCSMapping.put("SACRED HEART", "SACRED HEART");
		NCAAFFCSMapping.put("SAM HOUSTON ST", "SAM HOUSTON ST.");
		NCAAFFCSMapping.put("SAMFORD", "SAMFORD");
		NCAAFFCSMapping.put("SAN DIEGO", "SAN DIEGO");
		NCAAFFCSMapping.put("SAVANNAH ST", "SAVANNAH ST.");
		NCAAFFCSMapping.put("SE LOUISIANA", "SOUTHEASTERN LOUISIANA");
		NCAAFFCSMapping.put("SE MISSOURI ST", "SOUTHEAST MISSOURI ST.");
		NCAAFFCSMapping.put("SF AUSTIN", "STEPHEN F. AUSTIN ST.");
		NCAAFFCSMapping.put("SOUTH DAKOTA", "SOUTH DAKOTA");
		NCAAFFCSMapping.put("SOUTH DAKOTA ST", "SOUTH DAKOTA St.");
		NCAAFFCSMapping.put("SOUTHERN UNIV", "SOUTHERN UNIVERSITY A&M");
		NCAAFFCSMapping.put("SOUTHERN UTAH", "SOUTHERN UTAH");
		NCAAFFCSMapping.put("ST FRANCIS PA", "SAINT FRANCIS-PENNSYLVANIA");
		NCAAFFCSMapping.put("STETSON", "STETSON");
		NCAAFFCSMapping.put("STONY BROOK", "STONY BROOK");
		NCAAFFCSMapping.put("TENNESSEE ST", "TENNESSEE ST.");
		NCAAFFCSMapping.put("TENNESSEE TECH", "TENNESSEE TECH");
		NCAAFFCSMapping.put("TN MARTIN", "UT MARTIN");
		NCAAFFCSMapping.put("TOWSON", "TOWSON");
		NCAAFFCSMapping.put("TX SOUTHERN", "TEXAS SOUTHERN");
		NCAAFFCSMapping.put("UC DAVIS", "California-Davis");
		NCAAFFCSMapping.put("VALPARAISO", "VALPARAISO");
		NCAAFFCSMapping.put("VILLANOVA", "VILLANOVA");
		NCAAFFCSMapping.put("VMI", "VIRGINIA MILITARY");
		NCAAFFCSMapping.put("W CAROLINA", "WESTERN CAROLINA");
		NCAAFFCSMapping.put("W ILLINOIS", "WESTERN ILLINOIS");
		NCAAFFCSMapping.put("WAGNER", "WAGNER");
		NCAAFFCSMapping.put("WEBER ST", "WEBER ST.");
		NCAAFFCSMapping.put("WILLIAM & MARY", "WILLIAM & MARY");
		NCAAFFCSMapping.put("WOFFORD", "WOFFORD");
		NCAAFFCSMapping.put("YALE", "YALE");
		NCAAFFCSMapping.put("YOUNGSTOWN ST", "YOUNGSTOWN ST.");

		NCAABMapping.put("ABILENE CHR" , "Abilene Christian");
		NCAABMapping.put("ABILENE CHRISTIAN" , "Abilene Christian");
		NCAABMapping.put("AIR FORCE" , "Air Force");
		NCAABMapping.put("AKRON" , "Akron");
		NCAABMapping.put("ALABAMA" , "Alabama");
		NCAABMapping.put("ALABAMA AM" , "Alabama AM");
		NCAABMapping.put("ALABAMA A&M" , "Alabama AM");
		NCAABMapping.put("ALABAMA A&AMP;M" , "Alabama AM");
		NCAABMapping.put("ALABAMA ST" , "Alabama St.");
		NCAABMapping.put("ALBANY NY" , "Albany");
		NCAABMapping.put("ALCORN ST" , "Alcorn St.");
		NCAABMapping.put("AMERICAN UNIV" , "American");
		NCAABMapping.put("APPALACHIAN ST" , "APPALACHIAN St.");
		NCAABMapping.put("ARIZONA" , "Arizona");
		NCAABMapping.put("ARIZONA STATE" , "Arizona St.");
		NCAABMapping.put("ARIZONA ST" , "Arizona St.");
		NCAABMapping.put("ARK LITTLE ROCK" , "Arkansas-Little Rock");
		NCAABMapping.put("ARKANSAS" , "Arkansas");
		NCAABMapping.put("ARKANSAS STATE" , "Arkansas St.");
		NCAABMapping.put("ARKANSAS ST" , "Arkansas St.");
		NCAABMapping.put("ARK PINE BLUFF" , "Arkansas-Pine Bluff");
		NCAABMapping.put("ARMY" , "Army");
		NCAABMapping.put("AUBURN" , "Auburn");
		NCAABMapping.put("AUSTIN PEAY" , "Austin Peay");
		NCAABMapping.put("BALL ST" , "Ball St.");
		NCAABMapping.put("BAYLOR" , "Baylor");
		NCAABMapping.put("BELMONT" , "Belmont");
		NCAABMapping.put("BETHUNE-COOKMAN" , "Bethune-Cookman");
		NCAABMapping.put("Binghamton" , "Binghamton");
		NCAABMapping.put("BOISE ST" , "Boise St.");
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
		NCAABMapping.put("Cal Baptist" , "CALIFORNIA BAPTIST");
		NCAABMapping.put("Cal Poly SLO" , "Cal Poly-Slo");
		NCAABMapping.put("CALIFORNIA" , "California");
		NCAABMapping.put("CAMPBELL" , "Campbell");
		NCAABMapping.put("Canisius" , "Canisius");
		NCAABMapping.put("CENT ARKANSAS" , "Central Arkansas");
		NCAABMapping.put("CENTRAL CONN" , "Central Connecticut");
		NCAABMapping.put("C MICHIGAN" , "Central Michigan");
		NCAABMapping.put("CHARLESTON SO" , "Charleston Southern");
		NCAABMapping.put("CHARLOTTE" , "Charlotte");
		NCAABMapping.put("CHATTANOOGA" , "Tennessee-Chattanooga");
		NCAABMapping.put("Chicago St" , "Chicago St.");
		NCAABMapping.put("CINCINNATI" , "Cincinnati");
		NCAABMapping.put("CLEMSON" , "Clemson");
		NCAABMapping.put("Cleveland St" , "Cleveland St.");
		NCAABMapping.put("COASTAL CAR" , "Coastal Carolina");
		NCAABMapping.put("Col Charleston" , "Charleston");
		NCAABMapping.put("COLGATE" , "Colgate");
		NCAABMapping.put("COLORADO" , "Colorado");
		NCAABMapping.put("COLORADO St" , "Colorado St.");
		NCAABMapping.put("COLUMBIA" , "Columbia");
		NCAABMapping.put("CONNECTICUT" , "UConn");
		NCAABMapping.put("Coppin St" , "Coppin St.");
		NCAABMapping.put("CORNELL" , "Cornell");
		NCAABMapping.put("Creighton" , "Creighton");
		NCAABMapping.put("CS Bakersfield" , "CSU Bakersfield");
		NCAABMapping.put("CS Fullerton" , "CSU Fullerton");
		NCAABMapping.put("CS Northridge" , "CSU Northridge");
		NCAABMapping.put("CS SACRAMENTO" , "California State-Sacramento");
		NCAABMapping.put("DARTMOUTH" , "Dartmouth");
		NCAABMapping.put("DAVIDSON" , "Davidson");
		NCAABMapping.put("DAYTON" , "Dayton");
		NCAABMapping.put("DELAWARE" , "Delaware");
		NCAABMapping.put("DELAWARE ST" , "Delaware St.");
		NCAABMapping.put("Denver" , "Denver");
		NCAABMapping.put("DePaul" , "DePaul");
		NCAABMapping.put("Detroit" , "Detroit Mercy");
		NCAABMapping.put("DRAKE" , "Drake");
		NCAABMapping.put("Drexel" , "Drexel");
		NCAABMapping.put("DUKE" , "Duke");
		NCAABMapping.put("DUQUESNE" , "Duquesne");
		NCAABMapping.put("EAST CAROLINA" , "East Carolina");
		NCAABMapping.put("ETSU" , "East Tennessee St.");
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
		NCAABMapping.put("FLORIDA ST" , "Florida St.");
		NCAABMapping.put("FORDHAM" , "Fordham");
		NCAABMapping.put("FRESNO ST" , "Fresno St.");
		NCAABMapping.put("FURMAN" , "Furman");
		NCAABMapping.put("G Washington" , "George Washington");
		NCAABMapping.put("GARDNER WEBB" , "Gardner-Webb");
		NCAABMapping.put("George Mason" , "George Mason");
		NCAABMapping.put("GEORGETOWN" , "Georgetown");
		NCAABMapping.put("GEORGIA" , "Georgia");
		NCAABMapping.put("GA SOUTHERN" , "Georgia Southern");
		NCAABMapping.put("GEORGIA ST" , "Georgia St.");
		NCAABMapping.put("GEORGIA TECH" , "Georgia Tech");
		NCAABMapping.put("Gonzaga" , "Gonzaga");
		NCAABMapping.put("GRAMBLING" , "Grambling");
		NCAABMapping.put("GRAMBLING ST" , "Grambling");
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
		NCAABMapping.put("IDAHO ST" , "Idaho St.");
		NCAABMapping.put("IL Chicago" , "UIC");
		NCAABMapping.put("ILLINOIS" , "Illinois");
		NCAABMapping.put("ILLINOIS ST" , "Illinois St.");
		NCAABMapping.put("INCARNATE WORD" , "Incarnate Word");
		NCAABMapping.put("INDIANA" , "Indiana");
		NCAABMapping.put("INDIANA ST" , "Indiana St.");
		NCAABMapping.put("Iona" , "Iona");
		NCAABMapping.put("IOWA" , "Iowa");
		NCAABMapping.put("IOWA ST" , "Iowa St.");
		NCAABMapping.put("IPFW" , "Fort Wayne");
		NCAABMapping.put("IUPUI" , "IUPUI");
		NCAABMapping.put("JACKSON ST" , "Jackson St.");
		NCAABMapping.put("JACKSONVILLE" , "Jacksonville");
		NCAABMapping.put("JACKSONVILLE ST" , "Jacksonville St.");
		NCAABMapping.put("JAMES MADISON" , "James Madison");
		NCAABMapping.put("KANSAS" , "Kansas");
		NCAABMapping.put("KANSAS ST" , "Kansas St.");
		NCAABMapping.put("KENNESAW" , "KENNESAW St.");
		NCAABMapping.put("KENT" , "Kent St.");
		NCAABMapping.put("KENTUCKY" , "Kentucky");
		NCAABMapping.put("La Salle" , "La Salle");
		NCAABMapping.put("LAFAYETTE" , "Lafayette");
		NCAABMapping.put("LAMAR" , "Lamar");
		NCAABMapping.put("LEHIGH" , "Lehigh");
		NCAABMapping.put("LIBERTY" , "Liberty");
		NCAABMapping.put("Lipscomb" , "Lipscomb");
		NCAABMapping.put("LONG BEACH ST" , "Long Beach St.");
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
		NCAABMapping.put("MICHIGAN ST" , "Michigan St.");
		NCAABMapping.put("MTSU" , "Middle Tennessee");
		NCAABMapping.put("MINNESOTA" , "Minnesota");
		NCAABMapping.put("MISSISSIPPI" , "Ole Miss");
		NCAABMapping.put("MISSISSIPPI ST" , "Mississippi St.");
		NCAABMapping.put("MS VALLEY ST" , "MISSISSIPPI Valley St.");
		NCAABMapping.put("MISSOURI" , "Missouri");
		NCAABMapping.put("Missouri KC" , "UMKC");
		NCAABMapping.put("MISSOURI ST" , "Missouri St.");
		NCAABMapping.put("MONMOUTH NJ" , "Monmouth");
		NCAABMapping.put("MONTANA" , "Montana");
		NCAABMapping.put("MONTANA ST" , "Montana St.");
		NCAABMapping.put("MOREHEAD ST" , "Morehead St.");
		NCAABMapping.put("MORGAN ST" , "Morgan St.");
		NCAABMapping.put("Mt St Mary's" , "Mt. St. Mary's");
		NCAABMapping.put("MURRAY ST" , "Murray St.");
		NCAABMapping.put("N Kentucky" , "Northern Kentucky");
		NCAABMapping.put("NAVY" , "Navy");
		NCAABMapping.put("NE Omaha" , "Omaha");
		NCAABMapping.put("NEBRASKA" , "Nebraska");
		NCAABMapping.put("NEVADA" , "Nevada");
		NCAABMapping.put("NEW HAMPSHIRE" , "New Hampshire");
		NCAABMapping.put("NEW MEXICO" , "New Mexico");
		NCAABMapping.put("NEW MEXICO ST" , "New Mexico St.");
		NCAABMapping.put("New Orleans" , "New Orleans");
		NCAABMapping.put("Niagara" , "Niagara");
		NCAABMapping.put("NICHOLLS ST" , "Nicholls St.");
		NCAABMapping.put("NJIT" , "NJIT");
		NCAABMapping.put("NORFOLK ST" , "Norfolk St.");
		NCAABMapping.put("NORTH CAROLINA" , "North Carolina");
		NCAABMapping.put("NC A&T" , "North Carolina A&amp;T");
		NCAABMapping.put("NC CENTRAL" , "North Carolina Central");
		NCAABMapping.put("NC STATE" , "NC St.");
		NCAABMapping.put("NORTH DAKOTA" , "North Dakota");
		NCAABMapping.put("N DAKOTA ST" , "North Dakota St.");
		NCAABMapping.put("NORTH FLORIDA" , "North Florida");
		NCAABMapping.put("NORTH TEXAS" , "North Texas");
		NCAABMapping.put("Northeastern" , "Northeastern");
		NCAABMapping.put("NORTHERN ARIZONA" , "Northern Arizona");
		NCAABMapping.put("N COLORADO" , "Northern Colorado");
		NCAABMapping.put("N ILLINOIS" , "Northern Illinois");
		NCAABMapping.put("NORTHERN IOWA" , "Northern Iowa");
		NCAABMapping.put("NORTHWESTERN" , "Northwestern");
		NCAABMapping.put("NORTHWESTERN LA" , "Northwestern St.");
		NCAABMapping.put("NOTRE DAME" , "Notre Dame");
		NCAABMapping.put("Oakland" , "Oakland");
		NCAABMapping.put("OHIO" , "Ohio");
		NCAABMapping.put("OHIO ST" , "Ohio St.");
		NCAABMapping.put("OKLAHOMA" , "Oklahoma");
		NCAABMapping.put("OKLAHOMA ST" , "Oklahoma St.");
		NCAABMapping.put("OLD DOMINION" , "Old Dominion");
		NCAABMapping.put("Oral Roberts" , "Oral Roberts");
		NCAABMapping.put("OREGON" , "Oregon");
		NCAABMapping.put("OREGON ST" , "Oregon St.");
		NCAABMapping.put("Pacific" , "Pacific");
		NCAABMapping.put("PENN ST" , "Penn St.");
		NCAABMapping.put("Penn" , "PENNSYLVANIA");
		NCAABMapping.put("Pepperdine" , "Pepperdine");
		NCAABMapping.put("PITTSBURGH" , "Pittsburgh");
		NCAABMapping.put("Portland" , "Portland");
		NCAABMapping.put("PORTLAND ST" , "Portland St.");
		NCAABMapping.put("PRAIRIE VIEW" , "Prairie View A&M");
		NCAABMapping.put("PRESBYTERIAN" , "Presbyterian");
		NCAABMapping.put("PRINCETON" , "Princeton");
		NCAABMapping.put("Providence" , "Providence");
		NCAABMapping.put("PURDUE" , "Purdue");
		NCAABMapping.put("PFW" , "FORT WAYNE");
		NCAABMapping.put("Quinnipiac" , "Quinnipiac");
		NCAABMapping.put("Radford" , "Radford");
		NCAABMapping.put("RHODE ISLAND" , "Rhode Island");
		NCAABMapping.put("RICE" , "Rice");
		NCAABMapping.put("RICHMOND" , "Richmond");
		NCAABMapping.put("Rider" , "Rider");
		NCAABMapping.put("ROBERT MORRIS" , "Robert Morris");
		NCAABMapping.put("RUTGERS" , "Rutgers");
		NCAABMapping.put("SACRED HEART" , "Sacred Heart");
		NCAABMapping.put("SAM HOUSTON ST" , "Sam Houston St.");
		NCAABMapping.put("SAMFORD" , "Samford");
		NCAABMapping.put("SAN DIEGO" , "San Diego");
		NCAABMapping.put("SAN DIEGO ST" , "San Diego St.");
		NCAABMapping.put("SAN FRANCISCO" , "San Francisco");
		NCAABMapping.put("SAN JOSE ST" , "San Jose St.");
		NCAABMapping.put("Santa Barbara" , "UC Santa Barbara");
		NCAABMapping.put("SANTA CLARA" , "Santa Clara");
		NCAABMapping.put("SAVANNAH ST" , "Savannah St.");
		NCAABMapping.put("SC Upstate" , "South Carolina Upstate");
		NCAABMapping.put("Seattle" , "Seattle");
		NCAABMapping.put("Seton Hall" , "Seton Hall");
		NCAABMapping.put("Siena" , "Siena");
		NCAABMapping.put("SMU" , "SMU");
		NCAABMapping.put("SOUTH ALABAMA" , "South Alabama");
		NCAABMapping.put("SOUTH CAROLINA" , "South Carolina");
		NCAABMapping.put("S CAROLINA ST" , "South Carolina St.");
		NCAABMapping.put("SOUTH DAKOTA" , "South Dakota");
		NCAABMapping.put("S DAKOTA ST" , "South Dakota St.");
		NCAABMapping.put("SOUTH FLORIDA" , "South Florida");
		NCAABMapping.put("SIUE" , "SIU EDWARDSVILLE");
		NCAABMapping.put("SE MISSOURI ST" , "SE MISSOURI STATE");
		NCAABMapping.put("SE LOUISIANA" , "SE Louisiana");
		NCAABMapping.put("SOUTHERN UNIV" , "Southern University A&M");
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
		NCAABMapping.put("TENNESSEE ST" , "Tennessee St.");
		NCAABMapping.put("TENNESSEE TECH" , "Tennessee Tech");
		NCAABMapping.put("TEXAS" , "Texas");
		NCAABMapping.put("TEXAS A&M" , "Texas A&amp;M");
		NCAABMapping.put("TX SOUTHERN" , "Texas Southern");
		NCAABMapping.put("TEXAS ST" , "Texas St.");
		NCAABMapping.put("TEXAS TECH" , "Texas Tech");
		NCAABMapping.put("TN MARTIN" , "Tenn-Martin");
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
		NCAABMapping.put("UTAH ST" , "Utah St.");
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
		NCAABMapping.put("WASHINGTON ST" , "Washington St.");
		NCAABMapping.put("WEBER ST" , "Weber St.");
		NCAABMapping.put("WEST VIRGINIA" , "West Virginia");
		NCAABMapping.put("W CAROLINA" , "Western Carolina");
		NCAABMapping.put("W ILLINOIS" , "Western Illinois");
		NCAABMapping.put("WESTERN KENTUCKY" , "WESTERN KENTUCKY");
		NCAABMapping.put("WKU" , "WESTERN KENTUCKY");
		NCAABMapping.put("W KENTUCKY" , "WESTERN KENTUCKY");
		NCAABMapping.put("W MICHIGAN" , "Western Michigan");
		NCAABMapping.put("WI Green Bay" , "Green Bay");
		NCAABMapping.put("WI Milwaukee" , "Milwaukee");
		NCAABMapping.put("Wichita St" , "Wichita St.");
		NCAABMapping.put("WILLIAM & MARY" , "William & Mary");
		NCAABMapping.put("Winthrop" , "Winthrop");
		NCAABMapping.put("WISCONSIN" , "Wisconsin");
		NCAABMapping.put("WOFFORD" , "Wofford");
		NCAABMapping.put("Wright St" , "Wright St.");
		NCAABMapping.put("WYOMING" , "Wyoming");
		NCAABMapping.put("Xavier" , "Xavier");
		NCAABMapping.put("YALE" , "Yale");
		NCAABMapping.put("YOUNGSTOWN ST" , "Youngstown St.");

		NCAABOldMapping.put("Abilene Chr", "Abilene Christian");
		NCAABOldMapping.put("Air Force", "Air Force");
		NCAABOldMapping.put("Akron","Akron");
		NCAABOldMapping.put("Alabama A&amp;M","Alabama A&M");
		NCAABOldMapping.put("Alabama","Alabama");
		NCAABOldMapping.put("Alabama St","Alabama St.");
		NCAABOldMapping.put("Albany NY","Albany");
		NCAABOldMapping.put("Alcorn St","Alcorn St.");
		NCAABOldMapping.put("American Univ","American University");
		NCAABOldMapping.put("Appalachian St","Appalachian St.");
		NCAABOldMapping.put("Ark Little Rock","Arkansas Little Rock");
		NCAABOldMapping.put("Arizona","Arizona");
		NCAABOldMapping.put("Arizona St","Arizona St.");
		NCAABOldMapping.put("Ark Pine Bluff","Arkansas Pine Bluff");
		NCAABOldMapping.put("Arkansas","Arkansas");
		NCAABOldMapping.put("Arkansas St","Arkansas St.");
		NCAABOldMapping.put("Army","Army");
		NCAABOldMapping.put("Auburn","Auburn");
		NCAABOldMapping.put("Austin Peay","Austin Peay");
		NCAABOldMapping.put("Ball St","Ball St.");
		NCAABOldMapping.put("Baylor","Baylor");
		NCAABOldMapping.put("Belmont","Belmont");
		NCAABOldMapping.put("Bethune-Cookman","Bethune-Cookman");
		NCAABOldMapping.put("Binghamton","Binghamton");
		NCAABOldMapping.put("Boise St","Boise St.");
		NCAABOldMapping.put("Boston College","Boston College");
		NCAABOldMapping.put("Boston Univ","Boston University");
		NCAABOldMapping.put("Bowling Green","Bowling Green");
		NCAABOldMapping.put("Bradley","Bradley");
		NCAABOldMapping.put("Brown","Brown");
		NCAABOldMapping.put("Bryant","Bryant");
		NCAABOldMapping.put("Bucknell","Bucknell");
		NCAABOldMapping.put("Buffalo","Buffalo");
		NCAABOldMapping.put("Butler","Butler");
		NCAABOldMapping.put("BYU","BYU");
		NCAABOldMapping.put("Cal Baptist","Cal Baptist");
		NCAABOldMapping.put("Cal Poly","Cal Poly-SLO");
		NCAABOldMapping.put("CS Northridge","CS Northridge");
		NCAABOldMapping.put("California","California");
		NCAABOldMapping.put("Campbell","Campbell");
		NCAABOldMapping.put("Canisius","Canisius");
		NCAABOldMapping.put("Cent Arkansas","Central Arkansas");
		NCAABOldMapping.put("Central Conn","Central Connecticut St.");
		NCAABOldMapping.put("UCF","Central Florida");
		NCAABOldMapping.put("C Michigan","Central Michigan");
		NCAABOldMapping.put("Charleston So","Charleston Southern");
		NCAABOldMapping.put("Charlotte","Charlotte");
		NCAABOldMapping.put("Chattanooga","Chattanooga");
		NCAABOldMapping.put("Chicago St","Chicago St.");
		NCAABOldMapping.put("Cincinnati","Cincinnati");
		NCAABOldMapping.put("Citadel","The Citadel");
		NCAABOldMapping.put("Clemson","Clemson");
		NCAABOldMapping.put("Cleveland St","Cleveland St.");
		NCAABOldMapping.put("Coastal Car","Coastal Carolina");
		NCAABOldMapping.put("Col Charleston","College of Charleston");
		NCAABOldMapping.put("Colgate","Colgate");
		NCAABOldMapping.put("Colorado","Colorado");
		NCAABOldMapping.put("Colorado St","Colorado St.");
		NCAABOldMapping.put("Columbia","Columbia");
		NCAABOldMapping.put("Connecticut","Connecticut");
		NCAABOldMapping.put("Coppin St","Coppin St.");
		NCAABOldMapping.put("Cornell","Cornell");
		NCAABOldMapping.put("Creighton","Creighton");
		NCAABOldMapping.put("CS Bakersfield","CS Bakersfield");
		NCAABOldMapping.put("CS Fullerton","CS Fullerton");
		NCAABOldMapping.put("Dartmouth","Dartmouth");
		NCAABOldMapping.put("Davidson","Davidson");
		NCAABOldMapping.put("Dayton","Dayton");
		NCAABOldMapping.put("Delaware","Delaware");
		NCAABOldMapping.put("Delaware St","Delaware St.");
		NCAABOldMapping.put("Denver","Denver");
		NCAABOldMapping.put("DePaul","DePaul");
		NCAABOldMapping.put("Detroit","Detroit Mercy");
		NCAABOldMapping.put("Drake","Drake");
		NCAABOldMapping.put("Drexel","Drexel");
		NCAABOldMapping.put("Duke","Duke");
		NCAABOldMapping.put("Duquesne","Duquesne");
		NCAABOldMapping.put("East Carolina","East Carolina");
		NCAABOldMapping.put("E Illinois","Eastern Illinois");
		NCAABOldMapping.put("E Kentucky","Eastern Kentucky");
		NCAABOldMapping.put("E Michigan","Eastern Michigan");
		NCAABOldMapping.put("ETSU","East Tennessee St.");
		NCAABOldMapping.put("E Washington","Eastern Washington");
		NCAABOldMapping.put("Elon","Elon");
		NCAABOldMapping.put("Evansville","Evansville");
		NCAABOldMapping.put("F Dickinson","Fairleigh Dickinson");
		NCAABOldMapping.put("Fairfield","Fairfield");
		NCAABOldMapping.put("FL Atlantic","Florida Atlantic");
		NCAABOldMapping.put("FL Gulf Coast","Florida Gulf Coast");
		NCAABOldMapping.put("Florida","Florida");
		NCAABOldMapping.put("Florida A&amp;M","Florida A&M");
		NCAABOldMapping.put("Florida Intl","Florida International");
		NCAABOldMapping.put("Florida St","Florida St.");
		NCAABOldMapping.put("Fordham","Fordham");
		NCAABOldMapping.put("Fresno St","Fresno St.");
		NCAABOldMapping.put("Furman","Furman");
		NCAABOldMapping.put("Ga Southern","Georgia Southern");
		NCAABOldMapping.put("Georgia Tech","Georgia Tech");
		NCAABOldMapping.put("Gardner Webb","Gardner-Webb");
		NCAABOldMapping.put("George Mason","George Mason");
		NCAABOldMapping.put("G Washington","George Washington");
		NCAABOldMapping.put("Georgetown","Georgetown");
		NCAABOldMapping.put("Georgia","Georgia");
		NCAABOldMapping.put("Georgia St","Georgia St.");
		NCAABOldMapping.put("Gonzaga","Gonzaga");
		NCAABOldMapping.put("Grambling","Grambling St.");
		NCAABOldMapping.put("Grand Canyon","Grand Canyon");
		NCAABOldMapping.put("Hampton","Hampton");
		NCAABOldMapping.put("Hartford","Hartford");
		NCAABOldMapping.put("Harvard","Harvard");
		NCAABOldMapping.put("Hawaii","Hawaii");
		NCAABOldMapping.put("High Point","High Point");
		NCAABOldMapping.put("Hofstra","Hofstra");
		NCAABOldMapping.put("Holy Cross","Holy Cross");
		NCAABOldMapping.put("Houston","Houston");
		NCAABOldMapping.put("Houston Bap","Houston Baptist");
		NCAABOldMapping.put("Howard","Howard");
		NCAABOldMapping.put("Idaho","Idaho");
		NCAABOldMapping.put("Idaho St","Idaho St.");
		NCAABOldMapping.put("IL Chicago","Illinois-Chicago");
		NCAABOldMapping.put("Illinois","Illinois");
		NCAABOldMapping.put("Illinois St","Illinois St.");
		NCAABOldMapping.put("Incarnate Word","Incarnate Word");
		NCAABOldMapping.put("Indiana","Indiana");
		NCAABOldMapping.put("Indiana St","Indiana St.");
		NCAABOldMapping.put("Iona","Iona");
		NCAABOldMapping.put("Iowa","Iowa");
		NCAABOldMapping.put("Iowa St","Iowa St.");
		NCAABOldMapping.put("PFW","Fort Wayne(IPFW)");
		NCAABOldMapping.put("IUPUI","IUPUI");
		NCAABOldMapping.put("Jackson St","Jackson St.");
		NCAABOldMapping.put("Jacksonville","Jacksonville");
		NCAABOldMapping.put("James Madison","James Madison");
		NCAABOldMapping.put("Jacksonville St","Jacksonville St.");
		NCAABOldMapping.put("Kansas","Kansas");
		NCAABOldMapping.put("Kansas St","Kansas St.");
		NCAABOldMapping.put("Kennesaw","Kennesaw St.");
		NCAABOldMapping.put("Kent","Kent St.");
		NCAABOldMapping.put("Kentucky","Kentucky");
		NCAABOldMapping.put("Lafayette","Lafayette");
		NCAABOldMapping.put("ULM","Louisiana-Monroe");
		NCAABOldMapping.put("La Salle","La Salle");
		NCAABOldMapping.put("Louisiana Tech","Louisiana Tech");
		NCAABOldMapping.put("Louisiana","Louisiana Lafayette");
		NCAABOldMapping.put("Lamar","Lamar");
		NCAABOldMapping.put("Lehigh","Lehigh");
		NCAABOldMapping.put("Long Beach St","Long Beach St.");
		NCAABOldMapping.put("Liberty","Liberty");
		NCAABOldMapping.put("Lipscomb","Lipscomb");
		NCAABOldMapping.put("LIU Brooklyn","Long Island University");
		NCAABOldMapping.put("Longwood","Longwood");
		NCAABOldMapping.put("Louisville","Louisville");
		NCAABOldMapping.put("Loy Marymount","Loyola Marymount");
		NCAABOldMapping.put("Loyola-Chicago","Loyola-Chicago");
		NCAABOldMapping.put("Loyola MD","Loyola-Maryland");
		NCAABOldMapping.put("LSU","LSU");
		NCAABOldMapping.put("Maine","Maine");
		NCAABOldMapping.put("Manhattan","Manhattan");
		NCAABOldMapping.put("Marist","Marist");
		NCAABOldMapping.put("Marquette","Marquette");
		NCAABOldMapping.put("Marshall","Marshall");
		NCAABOldMapping.put("Maryland","Maryland");
		NCAABOldMapping.put("UMBC","Maryland BC");
		NCAABOldMapping.put("MD E Shore","Maryland-Eastern Shore");
		NCAABOldMapping.put("MA Lowell","Massachusetts Lowell");
		NCAABOldMapping.put("McNeese St","McNeese St.");
		NCAABOldMapping.put("Memphis","Memphis");
		NCAABOldMapping.put("Mercer","Mercer");
		NCAABOldMapping.put("Miami FL","Miami-Florida");
		NCAABOldMapping.put("Miami OH","Miami-Ohio");
		NCAABOldMapping.put("Michigan","Michigan");
		NCAABOldMapping.put("Michigan St","Michigan St.");
		NCAABOldMapping.put("MTSU","Middle Tennessee St.");
		NCAABOldMapping.put("Minnesota","Minnesota");
		NCAABOldMapping.put("Mississippi St","Mississippi St.");
		NCAABOldMapping.put("MS Valley St","Mississippi Valley St.");
		NCAABOldMapping.put("Mississippi","Mississippi");
		NCAABOldMapping.put("Missouri","Missouri");
		NCAABOldMapping.put("Missouri St","Missouri St.");
		NCAABOldMapping.put("Monmouth NJ","Monmouth");
		NCAABOldMapping.put("Montana","Montana");
		NCAABOldMapping.put("Montana St","Montana St.");
		NCAABOldMapping.put("Morehead St","Morehead St.");
		NCAABOldMapping.put("Morgan St","Morgan St.");
		NCAABOldMapping.put("Mt St Mary's","Mount St Mary's");
		NCAABOldMapping.put("Murray St","Murray St.");
		NCAABOldMapping.put("North Alabama","North Alabama");
		NCAABOldMapping.put("Northern Arizona","Northern Arizona");
		NCAABOldMapping.put("North Carolina","North Carolina");
		NCAABOldMapping.put("N Colorado","Northern Colorado");
		NCAABOldMapping.put("N Dakota St","North Dakota St.");
		NCAABOldMapping.put("North Florida","North Florida");
		NCAABOldMapping.put("New Hampshire","New Hampshire");
		NCAABOldMapping.put("N Illinois","Northern Illinois");
		NCAABOldMapping.put("Northern Iowa","Northern Iowa");
		NCAABOldMapping.put("N Kentucky","Northern Kentucky");
		NCAABOldMapping.put("New Mexico St","New Mexico St.");
		NCAABOldMapping.put("Navy","Navy");
		NCAABOldMapping.put("NC A&amp;T","NC A&T");
		NCAABOldMapping.put("NC Central","NC Central");
		NCAABOldMapping.put("NC St.","NC St.");
		NCAABOldMapping.put("UNC Asheville","NC Asheville");
		NCAABOldMapping.put("UNC Greensboro","NC Greensboro");
		NCAABOldMapping.put("UNC Wilmington","NC Wilmington");
		NCAABOldMapping.put("NE Omaha","Nebraska Omaha");
		NCAABOldMapping.put("Nebraska","Nebraska");
		NCAABOldMapping.put("Nevada","Nevada");
		NCAABOldMapping.put("New Mexico","New Mexico");
		NCAABOldMapping.put("New Orleans","New Orleans");
		NCAABOldMapping.put("Niagara","Niagara");
		NCAABOldMapping.put("Nicholls St","Nicholls St");
		NCAABOldMapping.put("NJIT","NJIT");
		NCAABOldMapping.put("Norfolk St","Norfolk St.");
		NCAABOldMapping.put("North Dakota","North Dakota");
		NCAABOldMapping.put("North Texas","North Texas");
		NCAABOldMapping.put("Northeastern","Northeastern");
		NCAABOldMapping.put("Northwestern","Northwestern");
		NCAABOldMapping.put("Notre Dame","Notre Dame");
		NCAABOldMapping.put("Northwestern LA","Northwestern St.");
		NCAABOldMapping.put("Oakland","Oakland");
		NCAABOldMapping.put("Ohio","Ohio");
		NCAABOldMapping.put("Ohio St","Ohio St.");
		NCAABOldMapping.put("Oklahoma","Oklahoma");
		NCAABOldMapping.put("Oklahoma St","Oklahoma St.");
		NCAABOldMapping.put("Old Dominion","Old Dominion");
		NCAABOldMapping.put("Oral Roberts","Oral Roberts");
		NCAABOldMapping.put("Oregon","Oregon");
		NCAABOldMapping.put("Oregon St","Oregon St.");
		NCAABOldMapping.put("Pacific","Pacific");
		NCAABOldMapping.put("Penn St","Penn St.");
		NCAABOldMapping.put("Pepperdine","Pepperdine");
		NCAABOldMapping.put("Pittsburgh","Pittsburgh");
		NCAABOldMapping.put("Portland","Portland");
		NCAABOldMapping.put("Portland St","Portland St.");
		NCAABOldMapping.put("Prairie View","Prairie View A&M");
		NCAABOldMapping.put("Presbyterian","Presbyterian");
		NCAABOldMapping.put("Princeton","Princeton");
		NCAABOldMapping.put("Providence","Providence");
		NCAABOldMapping.put("Purdue","Purdue");
		NCAABOldMapping.put("Quinnipiac","Quinnipiac");
		NCAABOldMapping.put("Radford","Radford");
		NCAABOldMapping.put("Rhode Island","Rhode Island");
		NCAABOldMapping.put("Rice","Rice");
		NCAABOldMapping.put("Richmond","Richmond");
		NCAABOldMapping.put("Rider","Rider");
		NCAABOldMapping.put("Robert Morris","Robert Morris");
		NCAABOldMapping.put("Rutgers","Rutgers");
		NCAABOldMapping.put("South Alabama","South Alabama");
		NCAABOldMapping.put("S Carolina St","South Carolina St.");
		NCAABOldMapping.put("South Carolina","South Carolina");
		NCAABOldMapping.put("S Dakota St","South Dakota St.");
		NCAABOldMapping.put("South Florida","South Florida");
		NCAABOldMapping.put("S Illinois","Southern Illinois");
		NCAABOldMapping.put("SMU","SMU");
		NCAABOldMapping.put("Southern Miss","Southern Miss");
		NCAABOldMapping.put("Southern Utah","Southern Utah");
		NCAABOldMapping.put("CS Sacramento","Sacramento St.");
		NCAABOldMapping.put("Sacred Heart","Sacred Heart");
		NCAABOldMapping.put("St Louis","Saint Louis");
		NCAABOldMapping.put("Sam Houston St","Sam Houston St.");
		NCAABOldMapping.put("Samford","Samford");
		NCAABOldMapping.put("San Diego","San Diego");
		NCAABOldMapping.put("San Diego St","San Diego St.");
		NCAABOldMapping.put("San Francisco","San Francisco");
		NCAABOldMapping.put("San Jose St","San Jose St.");
		NCAABOldMapping.put("Santa Clara","Santa Clara");
		NCAABOldMapping.put("Savannah St","Savannah St.");
		NCAABOldMapping.put("SC Upstate","South Carolina Upstate");
		NCAABOldMapping.put("SE Louisiana","SE Louisiana");
		NCAABOldMapping.put("SE Missouri St","SE Missouri");
		NCAABOldMapping.put("Seattle","Seattle");
		NCAABOldMapping.put("Seton Hall","Seton Hall");
		NCAABOldMapping.put("Siena","Siena");
		NCAABOldMapping.put("SIUE","SIU-Edwardsville");
		NCAABOldMapping.put("South Dakota","South Dakota");
		NCAABOldMapping.put("Southern Univ","Southern U");
		NCAABOldMapping.put("St Bonaventure","Saint Bonaventure");
		NCAABOldMapping.put("St Francis NY","Saint Francis-NY");
		NCAABOldMapping.put("St Francis PA","Saint Francis-Pa.");
		NCAABOldMapping.put("St John's","St. John's");
		NCAABOldMapping.put("St Joseph's PA","Saint Joseph's-Pa.");
		NCAABOldMapping.put("St Mary's CA","Saint Mary's-California");
		NCAABOldMapping.put("St Peter's","Saint Peter's");
		NCAABOldMapping.put("Stanford","Stanford");
		NCAABOldMapping.put("SF Austin","Stephen F. Austin");
		NCAABOldMapping.put("Stetson","Stetson");
		NCAABOldMapping.put("Stony Brook","Stony Brook");
		NCAABOldMapping.put("Syracuse","Syracuse");
		NCAABOldMapping.put("Temple","Temple");
		NCAABOldMapping.put("Tennessee","Tennessee");
		NCAABOldMapping.put("Texas","Texas");
		NCAABOldMapping.put("Texas A&amp;M","Texas A&M");
		NCAABOldMapping.put("Texas St","Texas St.");
		NCAABOldMapping.put("Texas Tech","Texas Tech");
		NCAABOldMapping.put("TN Martin","Tennessee-Martin");
		NCAABOldMapping.put("Tennessee St","Tennessee St.");
		NCAABOldMapping.put("Tennessee Tech","Tennessee Tech");
		NCAABOldMapping.put("Toledo","Toledo");
		NCAABOldMapping.put("Towson","Towson");
		NCAABOldMapping.put("Troy","Troy");
		NCAABOldMapping.put("Tulane","Tulane");
		NCAABOldMapping.put("Tulsa","Tulsa");
		NCAABOldMapping.put("TAM C. Christi","Texas A&M-CC");
		NCAABOldMapping.put("TCU","TCU");
		NCAABOldMapping.put("UTEP","UTEP");
		NCAABOldMapping.put("TX Southern","Texas Southern");
		NCAABOldMapping.put("UT Arlington","Texas-Arlington");
		NCAABOldMapping.put("UTRGV","Texas Rio Grande Valley");
		NCAABOldMapping.put("UT San Antonio","UTSA");
		NCAABOldMapping.put("Massachusetts","Massachusetts");
		NCAABOldMapping.put("Penn","Pennsylvania");
		NCAABOldMapping.put("UAB","UAB");
		NCAABOldMapping.put("UC Davis","UC Davis");
		NCAABOldMapping.put("UC Irvine","UC Irvine");
		NCAABOldMapping.put("UC Riverside","UC Riverside");
		NCAABOldMapping.put("UCLA","UCLA");
		NCAABOldMapping.put("UC Santa Barbara","UC Santa Barbara");
		NCAABOldMapping.put("Missouri KC","UMKC");
		NCAABOldMapping.put("UNLV","UNLV");
		NCAABOldMapping.put("USC","USC");
		NCAABOldMapping.put("Utah","Utah");
		NCAABOldMapping.put("Utah St","Utah St.");
		NCAABOldMapping.put("Utah Valley","Utah Valley St.");
		NCAABOldMapping.put("VMI","VMI");
		NCAABOldMapping.put("Virginia Tech","Virginia Tech");
		NCAABOldMapping.put("Valparaiso","Valparaiso");
		NCAABOldMapping.put("Vanderbilt","Vanderbilt");
		NCAABOldMapping.put("VA Commonwealth","VCU");
		NCAABOldMapping.put("Vermont","Vermont");
		NCAABOldMapping.put("Villanova","Villanova");
		NCAABOldMapping.put("Virginia","Virginia");
		NCAABOldMapping.put("W Carolina","Western Carolina");
		NCAABOldMapping.put("W Illinois","Western Illinois");
		NCAABOldMapping.put("WKU","Western Kentucky");
		NCAABOldMapping.put("W Michigan","Western Michigan");
		NCAABOldMapping.put("West Virginia","West Virginia");
		NCAABOldMapping.put("Wagner","Wagner");
		NCAABOldMapping.put("Wake Forest","Wake Forest");
		NCAABOldMapping.put("Washington St","Washington St.");
		NCAABOldMapping.put("Washington","Washington");
		NCAABOldMapping.put("Weber St","Weber St.");
		NCAABOldMapping.put("WI Green Bay","Wisconsin Green Bay");
		NCAABOldMapping.put("WI Milwaukee","Wisconsin-Milwaukee");
		NCAABOldMapping.put("Wichita St","Wichita St.");
		NCAABOldMapping.put("Winthrop","Winthrop");
		NCAABOldMapping.put("Wisconsin","Wisconsin");
		NCAABOldMapping.put("William &amp; Mary","William & Mary");
		NCAABOldMapping.put("Wofford","Wofford");
		NCAABOldMapping.put("Wright St","Wright St.");
		NCAABOldMapping.put("Wyoming","Wyoming");
		NCAABOldMapping.put("Xavier","Xavier");
		NCAABOldMapping.put("Yale","Yale");
		NCAABOldMapping.put("Youngstown St","Youngstown St.");
	}

	// 10/08/17 1:25 PM (PST)
	protected static final ThreadLocal<SimpleDateFormat> GAME_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			// 2017-11-01
			return new SimpleDateFormat("E MM.dd.yyyy hh:mma z");
		}
	};

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

		try {
			String theNewDate = "Sun 01.06.2019 11:00am EST";
			LOGGER.error("theNewDate: " + theNewDate);
			final Date dateOfGame = GAME_DATE_FORMAT.get().parse(theNewDate);
			LOGGER.error("dateOfGame: " + dateOfGame);
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
						try {
							mrnd.setRank(Integer.parseInt(value));
						} catch (Throwable t) {
							
						}
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
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public List<MasseyRatingsData> parseGameData(String xhtml) throws BatchException {
		final List<MasseyRatingsData> mrds = new ArrayList<MasseyRatingsData>();

		// Get the document object
		final JSONObject obj = new JSONObject(xhtml);
		final JSONArray items = obj.getJSONArray("DI");

		for (int y = 0; y < items.length(); y++) {
			final JSONArray sitems = items.getJSONArray(y);

			final MasseyRatingsData resultData = new MasseyRatingsData();

			final JSONArray dates = sitems.getJSONArray(0);
			String date = dates.getString(0);

			final JSONArray times = sitems.getJSONArray(1);
			String time = times.getString(0);

			final Calendar cal = Calendar.getInstance();

			try {
				if (time.contains("FINAL") || time.contains("Final")) {
					LOGGER.debug("date: " + date);
					String theNewDate = date + "." + cal.get(Calendar.YEAR) + " " + "11:00am EST";
					LOGGER.debug("theNewDate: " + theNewDate);
					final Date dateOfGame = GAME_DATE_FORMAT.get().parse(theNewDate);
					resultData.setDateOfGame(dateOfGame);
				} else {
					time = time.replace("ET", "EST");
					final Date dateOfGame = GAME_DATE_FORMAT.get().parse(date + "." + cal.get(Calendar.YEAR) + " " + time);
					resultData.setDateOfGame(dateOfGame);
				}
			} catch (ParseException pe) {
				LOGGER.warn(pe.getMessage(), pe);
				try {
					resultData.setDateOfGame(new Date());
				} catch (Throwable t) {
					
				}
			}

			final JSONArray vistors = sitems.getJSONArray(2);
			String vistor = vistors.getString(0);
			
			String teamName = vistor.toUpperCase().replace("@", "").trim();
			Iterator<String> itr = NCAABOldMapping.keySet().iterator();
			while (itr.hasNext()) {
				final String key = itr.next();
				if (key.toUpperCase().equals(teamName.toUpperCase())) {
					teamName = NCAABOldMapping.get(key).toUpperCase();
				}
			}
			resultData.setOpponentTeam(teamName);

			final JSONArray homes = sitems.getJSONArray(3);
			String home = homes.getString(0);

			teamName = home.toUpperCase().replace("@", "").trim();
			itr = NCAABOldMapping.keySet().iterator();
			while (itr.hasNext()) {
				final String key = itr.next();
				if (key.toUpperCase().equals(teamName.toUpperCase())) {
					teamName = NCAABOldMapping.get(key).toUpperCase();
				}
			}
			resultData.setTeam(teamName);

			final JSONArray vistorscores = sitems.getJSONArray(8);
			int visitorscore = vistorscores.getInt(0);
			resultData.setScore1(visitorscore);

			final JSONArray homescores = sitems.getJSONArray(9);
			int homescore = homescores.getInt(0);
			resultData.setScore2(homescore);

			if (visitorscore > homescore) {
				final JSONArray visitorspreads = sitems.getJSONArray(12);
				float visitorspread = visitorspreads.getFloat(0);
				resultData.setSpread(visitorspread);
				resultData.setLinefavorite(resultData.getOpponentTeam());
			} else if (homescore > visitorscore) {
				final JSONArray homespreads = sitems.getJSONArray(13);
				float homespread = homespreads.getFloat(0);
				resultData.setSpread(homespread);
				resultData.setLinefavorite(resultData.getTeam());
			}

			LOGGER.debug("ResultData: " + resultData);
			mrds.add(resultData);
		}

		LOGGER.debug("mrds: " + mrds);
		return mrds;
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
					final Iterator<String> itr = NCAABOldMapping.keySet().iterator();
					while (itr.hasNext()) {
						final String key = itr.next();
						if (key.toLowerCase().equals(teamName.toLowerCase())) {
							keyName = NCAABOldMapping.get(key);
						}
					}

					if (keyName == null) {
						LOGGER.warn("KEYNAME: " + teamName + " not found");
						keyName = teamName;
					}
					map.put(keyName, Integer.toString(count));
					if (count == 353) {
						break;
					}
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
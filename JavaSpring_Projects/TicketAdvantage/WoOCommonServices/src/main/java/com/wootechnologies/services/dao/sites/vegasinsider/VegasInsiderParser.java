/**
 * 
 */
package com.wootechnologies.services.dao.sites.vegasinsider;

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
public class VegasInsiderParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(VegasInsiderParser.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E MMM dd, yyyy hh:mm a z");
	private static final SimpleDateFormat DATE_FORMAT_BB = new SimpleDateFormat("MM-dd-yyyy hh:mm a z");
	private static final Calendar now = Calendar.getInstance();
	private static final int offset = now.get(Calendar.DST_OFFSET);
	public static final Map<String, String> NCAAFMapping = new HashMap<String, String>();
	public static final Map<String, String> NCAAFFCSMapping = new HashMap<String, String>();
	public static final Map<String, String> NCAABMapping = new HashMap<String, String>();
	public static final Map<String, String> NCAABKPMapping = new HashMap<String, String>();

	static {
		NCAAFMapping.put("Air Force", "Air Force");
		NCAAFMapping.put("Akron","Akron");
		NCAAFMapping.put("Alabama","Alabama");
		NCAAFMapping.put("Alabama St.","Alabama St.");
		NCAAFMapping.put("Appalachian State","Appalachian St.");
		NCAAFMapping.put("Arizona","Arizona");
		NCAAFMapping.put("Arizona State","Arizona St.");
		NCAAFMapping.put("Arkansas","Arkansas");
		NCAAFMapping.put("Arkansas State","Arkansas St.");
		NCAAFMapping.put("Army","Army");
		NCAAFMapping.put("Auburn","Auburn");
		NCAAFMapping.put("Ball State","Ball St.");
		NCAAFMapping.put("Baylor","Baylor");
		NCAAFMapping.put("Boise State","Boise St.");
		NCAAFMapping.put("Boston College","Boston College");
		NCAAFMapping.put("Bowling Green State","Bowling Green");
		NCAAFMapping.put("SUNY-BUFFALO","Buffalo");
		NCAAFMapping.put("BYU","BYU");
		NCAAFMapping.put("California","California");
		NCAAFMapping.put("Central Michigan","Central Michigan");
		NCAAFMapping.put("UNC Charlotte","Charlotte");
		NCAAFMapping.put("Cincinnati","Cincinnati");
		NCAAFMapping.put("Clemson","Clemson");
		NCAAFMapping.put("Coastal Carolina","Coastal Carolina");
		NCAAFMapping.put("Colorado","Colorado");
		NCAAFMapping.put("Colorado State","Colorado St.");
		NCAAFMapping.put("Connecticut","Connecticut");
		NCAAFMapping.put("Duke","Duke");
		NCAAFMapping.put("East Carolina","East Carolina");
		NCAAFMapping.put("Eastern Michigan","Eastern Michigan");
		NCAAFMapping.put("Florida AM","Florida A&M");
		NCAAFMapping.put("Florida Atlantic","Florida Atlantic");
		NCAAFMapping.put("Florida","Florida");
		NCAAFMapping.put("Florida Intl","FIU");
		NCAAFMapping.put("Florida State","Florida St.");
		NCAAFMapping.put("Fresno State","Fresno St.");
		NCAAFMapping.put("Georgia Southern","Georgia Southern");
		NCAAFMapping.put("Georgia Tech","Georgia Tech");
		NCAAFMapping.put("Georgia","Georgia");
		NCAAFMapping.put("Georgia State","Georgia St.");
		NCAAFMapping.put("Hawaii","Hawaii");
		NCAAFMapping.put("Houston","Houston");
		NCAAFMapping.put("Illinois","Illinois");
		NCAAFMapping.put("Indiana","Indiana");
		NCAAFMapping.put("Iowa","Iowa");
		NCAAFMapping.put("Iowa State","Iowa St.");
		NCAAFMapping.put("Kansas","Kansas");
		NCAAFMapping.put("Kansas State","Kansas St.");
		NCAAFMapping.put("Kent State","Kent St.");
		NCAAFMapping.put("Kentucky","Kentucky");
		NCAAFMapping.put("Liberty","Liberty");
		NCAAFMapping.put("Louisiana-Lafayette","Louisiana");
		NCAAFMapping.put("Louisiana-Monroe","Louisiana Monroe");
		NCAAFMapping.put("Louisiana Tech","Louisiana Tech");
		NCAAFMapping.put("Louisville","Louisville");
		NCAAFMapping.put("LSU","LSU");
		NCAAFMapping.put("Marshall","Marshall");
		NCAAFMapping.put("Maryland","Maryland");
		NCAAFMapping.put("Memphis","Memphis");
		NCAAFMapping.put("Miami (FL)","Miami FL");
		NCAAFMapping.put("Miami (OH)","Miami OH");
		NCAAFMapping.put("Michigan","Michigan");
		NCAAFMapping.put("Michigan State","Michigan St.");
		NCAAFMapping.put("Middle Tennessee St.","Middle Tennessee");
		NCAAFMapping.put("Minnesota","Minnesota");
		NCAAFMapping.put("Mississippi State","Mississippi St.");
		NCAAFMapping.put("Mississippi","Mississippi");
		NCAAFMapping.put("Missouri","Missouri");
		NCAAFMapping.put("Morgan St.", "Morgan St.");
		NCAAFMapping.put("Northern Illinois","Northern Illinois");
		NCAAFMapping.put("North Carolina","North Carolina");
		NCAAFMapping.put("Navy","Navy");
		NCAAFMapping.put("North Carolina State","North Carolina St.");
		NCAAFMapping.put("Nebraska","Nebraska");
		NCAAFMapping.put("Nevada","Nevada");
		NCAAFMapping.put("New Mexico","New Mexico");
		NCAAFMapping.put("New Mexico State","New Mexico St.");
		NCAAFMapping.put("North Texas","North Texas");
		NCAAFMapping.put("Northwestern","Northwestern");
		NCAAFMapping.put("Notre Dame","Notre Dame");
		NCAAFMapping.put("Ohio","Ohio");
		NCAAFMapping.put("Ohio State","Ohio St.");
		NCAAFMapping.put("Oklahoma","Oklahoma");
		NCAAFMapping.put("Oklahoma State","Oklahoma St.");
		NCAAFMapping.put("Old Dominion","Old Dominion");
		NCAAFMapping.put("Oregon","Oregon");
		NCAAFMapping.put("Oregon State","Oregon St.");
		NCAAFMapping.put("Penn State","Penn St.");
		NCAAFMapping.put("Pittsburgh","Pittsburgh");
		NCAAFMapping.put("Purdue","Purdue");
		NCAAFMapping.put("Rice","Rice");
		NCAAFMapping.put("Rutgers","Rutgers");
		NCAAFMapping.put("San Diego State","San Diego St.");
		NCAAFMapping.put("South Alabama","South Alabama");
		NCAAFMapping.put("South Carolina","South Carolina");
		NCAAFMapping.put("South Florida","South Florida");
		NCAAFMapping.put("Southern Methodist","SMU");		
		NCAAFMapping.put("Southern Miss","Southern Miss");
		NCAAFMapping.put("San Jose State","San Jose St.");
		NCAAFMapping.put("Stanford","Stanford");
		NCAAFMapping.put("Syracuse","Syracuse");
		NCAAFMapping.put("Texas Christian","TCU");
		NCAAFMapping.put("Temple","Temple");
		NCAAFMapping.put("Tennessee","Tennessee");
		NCAAFMapping.put("Texas","Texas");
		NCAAFMapping.put("Texas AM","Texas A&M");
		NCAAFMapping.put("Texas State","Texas St.");
		NCAAFMapping.put("Texas Tech","Texas Tech");
		NCAAFMapping.put("Toledo","Toledo");
		NCAAFMapping.put("Troy","Troy");
		NCAAFMapping.put("Tulane","Tulane");
		NCAAFMapping.put("Tulsa","Tulsa");
		NCAAFMapping.put("UTEP","UTEP");
		NCAAFMapping.put("Texas-San Antonio","UTSA");
		NCAAFMapping.put("Massachusetts","Massachusetts");
		NCAAFMapping.put("UAB","UAB");
		NCAAFMapping.put("UCLA","UCLA");
		NCAAFMapping.put("Central Florida","UCF");
		NCAAFMapping.put("UNLV","UNLV");
		NCAAFMapping.put("Southern Cal","USC");
		NCAAFMapping.put("Utah","Utah");
		NCAAFMapping.put("Utah State","Utah St.");
		NCAAFMapping.put("Virginia Tech","Virginia Tech");
		NCAAFMapping.put("Vanderbilt","Vanderbilt");
		NCAAFMapping.put("Virginia","Virginia");
		NCAAFMapping.put("Western Kentucky","Western Kentucky");
		NCAAFMapping.put("Western Michigan","Western Michigan");
		NCAAFMapping.put("West Virginia","West Virginia");
		NCAAFMapping.put("Wake Forest","Wake Forest");
		NCAAFMapping.put("Washington State","Washington St.");
		NCAAFMapping.put("Washington","Washington");
		NCAAFMapping.put("Wisconsin","Wisconsin");
		NCAAFMapping.put("Wyoming","Wyoming");

		NCAAFFCSMapping.put("ABILENE CHRISTIAN","ABILENE CHRISTIAN");
		NCAAFFCSMapping.put("ALABAMA AM","ALABAMA AM");
		NCAAFFCSMapping.put("ALABAMA STATE","ALABAMA St.");
		NCAAFFCSMapping.put("ALBANY","ALBANY");
		NCAAFFCSMapping.put("ALCORN STATE","ALCORN St.");
		NCAAFFCSMapping.put("ARKANSAS-PINE BLUFF","ARKANSAS-PINE BLUFF");
		NCAAFFCSMapping.put("AUSTIN PEAY STATE","AUSTIN PEAY");
		NCAAFFCSMapping.put("BETHUNE COOKMAN","BETHUNE-COOKMAN");
		NCAAFFCSMapping.put("BREVARD","BREVARD COLLEGE");
		NCAAFFCSMapping.put("BRYANT","BRYANT");
		NCAAFFCSMapping.put("BUCKNELL","BUCKNELL");
		NCAAFFCSMapping.put("BUTLER","BUTLER");
		NCAAFFCSMapping.put("CAL POLY SLO","Cal Poly-Slo");
		NCAAFFCSMapping.put("CAMPBELL","CAMPBELL");
		NCAAFFCSMapping.put("CENTRAL ARKANSAS","CENTRAL ARKANSAS");
		NCAAFFCSMapping.put("CENTRAL CONNECTICUT ST.","CENTRAL CONNECTICUT");
		NCAAFFCSMapping.put("CHARLESTON SOUTHERN","CHARLESTON SOUTHERN");
		NCAAFFCSMapping.put("CLARION","CLARION");
		NCAAFFCSMapping.put("COLGATE","COLGATE");
		NCAAFFCSMapping.put("CS SACRAMENTO", "California State-Sacramento");
		NCAAFFCSMapping.put("DAVIDSON","DAVIDSON");
		NCAAFFCSMapping.put("DAYTON","DAYTON");
		NCAAFFCSMapping.put("DELAWARE","DELAWARE");
		NCAAFFCSMapping.put("DELAWARE STATE","DELAWARE St.");
		NCAAFFCSMapping.put("DRAKE","DRAKE");
		NCAAFFCSMapping.put("DUQUESNE","DUQUESNE");
		NCAAFFCSMapping.put("EAST TENNESSEE ST","EAST TENNESSEE St.");
		NCAAFFCSMapping.put("EASTERN ILLINOIS","EASTERN ILLINOIS");
		NCAAFFCSMapping.put("EASTERN KENTUCKY","EASTERN KENTUCKY");
		NCAAFFCSMapping.put("EASTERN WASHINGTON","EASTERN WASHINGTON");
		NCAAFFCSMapping.put("ELON","ELON");
		NCAAFFCSMapping.put("Florida AM","Florida A&M");
		NCAAFFCSMapping.put("FORDHAM","FORDHAM");
		NCAAFFCSMapping.put("FURMAN","FURMAN");
		NCAAFFCSMapping.put("GARDNER WEBB","GARDNER-WEBB");
		NCAAFFCSMapping.put("GRAMBLING STATE","GRAMBLING");
		NCAAFFCSMapping.put("GRAMBLING","GRAMBLING");
		NCAAFFCSMapping.put("HAMPTON","HAMPTON");
		NCAAFFCSMapping.put("HOLY CROSS","HOLY CROSS");
		NCAAFFCSMapping.put("HOUSTON BAPTIST","HOUSTON BAPTIST");
		NCAAFFCSMapping.put("HOWARD","HOWARD");
		NCAAFFCSMapping.put("IDAHO","IDAHO");
		NCAAFFCSMapping.put("IDAHO STATE","IDAHO St.");
		NCAAFFCSMapping.put("ILLINOIS STATE","ILLINOIS St.");
		NCAAFFCSMapping.put("INCARNATE WORD","INCARNATE WORD");
		NCAAFFCSMapping.put("INDIANA STATE","INDIANA St.");
		NCAAFFCSMapping.put("JACKSON STATE","JACKSON St.");
		NCAAFFCSMapping.put("JACKSONVILLE","JACKSONVILLE");
		NCAAFFCSMapping.put("JACKSONVILLE STATE","JACKSONVILLE St.");
		NCAAFFCSMapping.put("JAMES MADISON","JAMES MADISON");
		NCAAFFCSMapping.put("KENNESAW STATE","KENNESAW St.");
		NCAAFFCSMapping.put("KENTUCKY CHRISTIAN","KENTUCKY CHRISTIAN");
		NCAAFFCSMapping.put("KENTUCKY WESLEYAN","KENTUCKY WESLEYAN");
		NCAAFFCSMapping.put("LAFAYETTE","LAFAYETTE");
		NCAAFFCSMapping.put("LAMAR","LAMAR");
		NCAAFFCSMapping.put("LEHIGH","LEHIGH");
		NCAAFFCSMapping.put("LIMESTONE","LIMESTONE");
		NCAAFFCSMapping.put("LOCK HAVEN","LOCK HAVEN");
		NCAAFFCSMapping.put("MAINE","MAINE");
		NCAAFFCSMapping.put("MARIST","MARIST");
		NCAAFFCSMapping.put("MCNEESE STATE","MCNEESE");
		NCAAFFCSMapping.put("MERCER","MERCER");
		NCAAFFCSMapping.put("MERRIMACK","MERRIMACK");
		NCAAFFCSMapping.put("METHODIST","METHODIST");
		NCAAFFCSMapping.put("MILES","MILES");
		NCAAFFCSMapping.put("MISSISSIPPI VALLEY ST.","Mississippi Valley St.");
		NCAAFFCSMapping.put("MISSOURI STATE","MISSOURI St.");
		NCAAFFCSMapping.put("MONMOUTH (NJ)","MONMOUTH");
		NCAAFFCSMapping.put("MONTANA","MONTANA");
		NCAAFFCSMapping.put("MONTANA STATE","MONTANA St.");
		NCAAFFCSMapping.put("MONTANA-WESTERN","MONTANA-WESTERN");
		NCAAFFCSMapping.put("MOREHEAD STATE","MOREHEAD St.");
		NCAAFFCSMapping.put("MOREHOUSE","MOREHOUSE");
		NCAAFFCSMapping.put("Morgan STATE","Morgan St.");
		NCAAFFCSMapping.put("MURRAY STATE","MURRAY St.");
		NCAAFFCSMapping.put("NEW HAMPSHIRE","NEW HAMPSHIRE");
		NCAAFFCSMapping.put("NEWBERRY","NEWBERRY");
		NCAAFFCSMapping.put("NICHOLLS STATE","NICHOLLS St.");
		NCAAFFCSMapping.put("NORFOLK STATE","NORFOLK St.");
		NCAAFFCSMapping.put("NORTH CAROLINA AT","NORTH CAROLINA A&T");
		NCAAFFCSMapping.put("NORTH CAROLINA CENTRAL","NORTH CAROLINA CENTRAL");
		NCAAFFCSMapping.put("NORTH DAKOTA","NORTH DAKOTA");
		NCAAFFCSMapping.put("NORTH DAKOTA STATE","NORTH DAKOTA St.");
		NCAAFFCSMapping.put("NORTHERN ARIZONA","NORTHERN ARIZONA");
		NCAAFFCSMapping.put("NORTHERN IOWA","NORTHERN IOWA");
		NCAAFFCSMapping.put("NORTHWESTERN STATE","NORTHWESTERN St.");
		NCAAFFCSMapping.put("PENN", "PENNSYLVANIA");
		NCAAFFCSMapping.put("PORTLAND STATE","PORTLAND St.");
		NCAAFFCSMapping.put("PRAIRIE VIEW AM","Prairie View A&M");
		NCAAFFCSMapping.put("PRAIRIE VIEW","Prairie View A&M");
		NCAAFFCSMapping.put("PRESBYTERIAN","PRESBYTERIAN COLLEGE");
		NCAAFFCSMapping.put("RHODE ISLAND","RHODE ISLAND");
		NCAAFFCSMapping.put("RICHMOND","RICHMOND");
		NCAAFFCSMapping.put("ROBERT MORRIS","ROBERT MORRIS");
		NCAAFFCSMapping.put("SACRED HEART","SACRED HEART");
		NCAAFFCSMapping.put("SAM HOUSTON STATE","SAM HOUSTON St.");
		NCAAFFCSMapping.put("SAMFORD","SAMFORD");
		NCAAFFCSMapping.put("SAN DIEGO","SAN DIEGO");
		NCAAFFCSMapping.put("SE LOUISIANA","SOUTHEASTERN LOUISIANA");
		NCAAFFCSMapping.put("SE MISSOURI STATE","SOUTHEAST MISSOURI St.");
		NCAAFFCSMapping.put("SOUTH CAROLINA STATE","South Carolina St.");
		NCAAFFCSMapping.put("SOUTH DAKOTA","SOUTH DAKOTA");
		NCAAFFCSMapping.put("SOUTH DAKOTA STATE","SOUTH DAKOTA St.");
		NCAAFFCSMapping.put("SOUTHERN AM","Southern University A&M");
		NCAAFFCSMapping.put("SOUTHERN UTAH","SOUTHERN UTAH");
		NCAAFFCSMapping.put("ST. ANSELM","ST. ANSELM");
		NCAAFFCSMapping.put("ST. FRANCIS (PA)","Saint Francis-Pennsylvania");
		NCAAFFCSMapping.put("STEPHEN F. AUSTIN","STEPHEN F AUSTIN");
		NCAAFFCSMapping.put("STETSON","STETSON");
		NCAAFFCSMapping.put("STONY BROOK","STONY BROOK");
		NCAAFFCSMapping.put("TENNESSEE STATE","TENNESSEE St.");
		NCAAFFCSMapping.put("TENNESSEE TECH","TENNESSEE TECH");
		NCAAFFCSMapping.put("TEXAS SOUTHERN","TEXAS SOUTHERN");
		NCAAFFCSMapping.put("THE CITADEL","THE CITADEL");
		NCAAFFCSMapping.put("TOWSON","TOWSON");
		NCAAFFCSMapping.put("TUSKEGEE","TUSKEGEE");
		NCAAFFCSMapping.put("UC DAVIS","UC DAVIS");
		NCAAFFCSMapping.put("UT CHATTANOOGA","Tennessee-Chattanooga");
		NCAAFFCSMapping.put("UT MARTIN","UT MARTIN");
		NCAAFFCSMapping.put("VALPARAISO","VALPARAISO");
		NCAAFFCSMapping.put("VILLANOVA","VILLANOVA");
		NCAAFFCSMapping.put("VIRGINIA MILITARY","VMI");
		NCAAFFCSMapping.put("VIRGINIA STATE","VIRGINIA St.");
		NCAAFFCSMapping.put("WAGNER","WAGNER");
		NCAAFFCSMapping.put("WEBER STATE","WEBER St.");
		NCAAFFCSMapping.put("WESTERN ILLINOIS","WESTERN ILLINOIS");
		NCAAFFCSMapping.put("WESTERN NEW MEXICO","WESTERN NEW MEXICO");
		NCAAFFCSMapping.put("WESTERN OREGON","WESTERN OREGON");
		NCAAFFCSMapping.put("WILLIAM MARY","WILLIAM & MARY");
		NCAAFFCSMapping.put("WOFFORD","WOFFORD");
		NCAAFFCSMapping.put("YOUNGSTOWN STATE","YOUNGSTOWN St.");

		NCAABMapping.put("ABILENE CHRISTIAN" , "Abilene Christian");
		NCAABMapping.put("AIR FORCE" , "Air Force");
		NCAABMapping.put("AKRON" , "Akron");
		NCAABMapping.put("ALABAMA" , "Alabama");
		NCAABMapping.put("ALABAMA AM" , "Alabama AM");
		NCAABMapping.put("ALABAMA A&M" , "Alabama AM");
		NCAABMapping.put("ALABAMA STATE" , "Alabama State");
		NCAABMapping.put("ALBANY" , "Albany");
		NCAABMapping.put("ALCORN STATE" , "Alcorn State");
		NCAABMapping.put("AMERICAN" , "American");
		NCAABMapping.put("APPALACHIAN STATE" , "Appalachian State");
		NCAABMapping.put("ARIZONA" , "Arizona");
		NCAABMapping.put("ARIZONA STATE" , "Arizona State");
		NCAABMapping.put("ARKANSAS-LITTLE ROCK" , "Arkansas-Little Rock");
		NCAABMapping.put("ARKANSAS" , "Arkansas");
		NCAABMapping.put("ARKANSAS STATE" , "Arkansas State");
		NCAABMapping.put("ARKANSAS-PINE BLUFF" , "Arkansas-Pine Bluff");
		NCAABMapping.put("ARMY" , "Army");
		NCAABMapping.put("AUBURN" , "Auburn");
		NCAABMapping.put("AUSTIN PEAY STATE" , "Austin Peay");
		NCAABMapping.put("BALL STATE" , "Ball State");
		NCAABMapping.put("BAYLOR" , "Baylor");
		NCAABMapping.put("BELMONT" , "Belmont");
		NCAABMapping.put("BETHUNE COOKMAN" , "Bethune-Cookman");
		NCAABMapping.put("BINGHAMTON" , "Binghamton");
		NCAABMapping.put("BOISE STATE" , "Boise State");
		NCAABMapping.put("BOSTON COLLEGE" , "Boston College");
		NCAABMapping.put("BOSTON UNIVERSITY" , "BOSTON UNIVERSITY");
		NCAABMapping.put("BOWLING GREEN STATE" , "Bowling Green");
		NCAABMapping.put("BRADLEY" , "Bradley");
		NCAABMapping.put("BROWN" , "Brown");
		NCAABMapping.put("BRYANT" , "Bryant");
		NCAABMapping.put("BUCKNELL" , "Bucknell");
		NCAABMapping.put("SUNY-BUFFALO" , "Buffalo");
		NCAABMapping.put("BUTLER" , "Butler");
		NCAABMapping.put("BYU" , "BYU");
		NCAABMapping.put("CAL POLY SLO" , "Cal Poly");
		NCAABMapping.put("CALIFORNIA" , "California");
		NCAABMapping.put("CAMPBELL" , "Campbell");
		NCAABMapping.put("CANISIUS" , "Canisius");
		NCAABMapping.put("CENTRAL ARKANSAS" , "Central Arkansas");
		NCAABMapping.put("CENTRAL CONNECTICUT ST." , "Central Connecticut");
		NCAABMapping.put("CENTRAL MICHIGAN" , "Central Michigan");
		NCAABMapping.put("CHARLESTON SOUTHERN" , "Charleston Southern");
		NCAABMapping.put("UNC CHARLOTTE" , "Charlotte");
		NCAABMapping.put("UT CHATTANOOGA" , "Chattanooga");
		NCAABMapping.put("CHICAGO STATE" , "Chicago State");
		NCAABMapping.put("CINCINNATI" , "Cincinnati");
		NCAABMapping.put("CLEMSON" , "Clemson");
		NCAABMapping.put("CLEVELAND STATE" , "Cleveland State");
		NCAABMapping.put("COASTAL CAROLINA" , "Coastal Carolina");
		NCAABMapping.put("COLLEGE OF CHARLESTON" , "Charleston");
		NCAABMapping.put("COLGATE" , "Colgate");
		NCAABMapping.put("COLORADO" , "Colorado");
		NCAABMapping.put("COLORADO STATE" , "Colorado State");
		NCAABMapping.put("COLUMBIA" , "Columbia");
		NCAABMapping.put("CONNECTICUT" , "UConn");
		NCAABMapping.put("COPPIN STATE" , "Coppin State");
		NCAABMapping.put("CORNELL" , "Cornell");
		NCAABMapping.put("CREIGHTON" , "Creighton");
		NCAABMapping.put("CS BAKERSFIELD" , "CSU BAKERSFIELD");
		NCAABMapping.put("CS FULLERTON" , "CSU Fullerton");
		NCAABMapping.put("CS NORTHRIDGE" , "CSU Northridge");
		NCAABMapping.put("DARTMOUTH" , "Dartmouth");
		NCAABMapping.put("DAVIDSON" , "Davidson");
		NCAABMapping.put("DAYTON" , "Dayton");
		NCAABMapping.put("DELAWARE" , "Delaware");
		NCAABMapping.put("DELAWARE STATE" , "Delaware State");
		NCAABMapping.put("DENVER" , "Denver");
		NCAABMapping.put("DEPAUL" , "DePaul");
		NCAABMapping.put("DETROIT MERCY" , "Detroit Mercy");
		NCAABMapping.put("DRAKE" , "Drake");
		NCAABMapping.put("DREXEL" , "Drexel");
		NCAABMapping.put("DUKE" , "Duke");
		NCAABMapping.put("DUQUESNE" , "Duquesne");
		NCAABMapping.put("EAST CAROLINA" , "East Carolina");
		NCAABMapping.put("EAST TENNESSEE ST." , "East Tennessee State");
		NCAABMapping.put("EASTERN ILLINOIS" , "Eastern Illinois");
		NCAABMapping.put("EASTERN KENTUCKY" , "Eastern Kentucky");
		NCAABMapping.put("EASTERN MICHIGAN" , "Eastern Michigan");
		NCAABMapping.put("EASTERN WASHINGTON" , "Eastern Washington");
		NCAABMapping.put("ELON" , "Elon");
		NCAABMapping.put("EVANSVILLE" , "Evansville");
		NCAABMapping.put("FAIRLEIGH DICKINSON" , "Fairleigh Dickinson");
		NCAABMapping.put("FAIRFIELD" , "Fairfield");
		NCAABMapping.put("FLORIDA GULF COAST" , "Florida Gulf Coast");
		NCAABMapping.put("FLORIDA" , "Florida");
		NCAABMapping.put("FLORIDA AM" , "Florida A&amp;M");
		NCAABMapping.put("FLORIDA ATLANTIC" , "Florida Atlantic");
		NCAABMapping.put("FLORIDA INTL" , "Florida International");
		NCAABMapping.put("FLORIDA INTERNATIONAL" , "Florida International");
		NCAABMapping.put("FLORIDA STATE" , "Florida State");
		NCAABMapping.put("FORDHAM" , "Fordham");
		NCAABMapping.put("FRESNO STATE" , "Fresno State");
		NCAABMapping.put("FURMAN" , "Furman");
		NCAABMapping.put("GEORGE WASHINGTON" , "George Washington");
		NCAABMapping.put("GARDNER WEBB" , "Gardner-Webb");
		NCAABMapping.put("GEORGE MASON" , "George Mason");
		NCAABMapping.put("GEORGETOWN" , "Georgetown");
		NCAABMapping.put("GEORGIA" , "Georgia");
		NCAABMapping.put("GEORGIA SOUTHERN" , "Georgia Southern");
		NCAABMapping.put("GEORGIA STATE" , "Georgia State");
		NCAABMapping.put("GEORGIA TECH" , "Georgia Tech");
		NCAABMapping.put("GONZAGA" , "Gonzaga");
		NCAABMapping.put("GRAMBLING STATE" , "Grambling");
		NCAABMapping.put("GRAND CANYON" , "Grand Canyon");
		NCAABMapping.put("HAMPTON" , "Hampton");
		NCAABMapping.put("HARTFORD" , "Hartford");
		NCAABMapping.put("HARVARD" , "Harvard");
		NCAABMapping.put("HAWAII" , "Hawai'i");
		NCAABMapping.put("HIGH POINT" , "High Point");
		NCAABMapping.put("HOFSTRA" , "Hofstra");
		NCAABMapping.put("HOLY CROSS" , "Holy Cross");
		NCAABMapping.put("HOUSTON" , "Houston");
		NCAABMapping.put("HOUSTON BAPTIST" , "Houston Baptist");
		NCAABMapping.put("HOWARD" , "Howard");
		NCAABMapping.put("IDAHO" , "Idaho");
		NCAABMapping.put("IDAHO STATE" , "Idaho State");
		NCAABMapping.put("ILLINOIS" , "Illinois");
		NCAABMapping.put("ILLINOIS-CHICAGO" , "UIC");
		NCAABMapping.put("ILLINOIS STATE" , "Illinois State");
		NCAABMapping.put("INCARNATE WORD" , "Incarnate Word");
		NCAABMapping.put("INDIANA" , "Indiana");
		NCAABMapping.put("INDIANA STATE" , "Indiana State");
		NCAABMapping.put("IONA" , "Iona");
		NCAABMapping.put("IOWA" , "Iowa");
		NCAABMapping.put("IOWA STATE" , "Iowa State");
		NCAABMapping.put("IUPU-FORT WAYNE" , "FORT WAYNE");
		NCAABMapping.put("INDIANA-PURDUE" , "IUPUI");
		NCAABMapping.put("JACKSON STATE" , "Jackson State");
		NCAABMapping.put("JACKSONVILLE" , "Jacksonville");
		NCAABMapping.put("JACKSONVILLE STATE" , "Jacksonville State");
		NCAABMapping.put("JAMES MADISON" , "James Madison");
		NCAABMapping.put("KANSAS" , "Kansas");
		NCAABMapping.put("KANSAS STATE" , "Kansas State");
		NCAABMapping.put("KENNESAW STATE" , "Kennesaw State");
		NCAABMapping.put("KENT STATE" , "Kent State");
		NCAABMapping.put("KENTUCKY" , "Kentucky");
		NCAABMapping.put("LA SALLE" , "La Salle");
		NCAABMapping.put("LAFAYETTE" , "Lafayette");
		NCAABMapping.put("LAMAR" , "Lamar");
		NCAABMapping.put("LEHIGH" , "Lehigh");
		NCAABMapping.put("LIBERTY" , "Liberty");
		NCAABMapping.put("LIPSCOMB" , "Lipscomb");
		NCAABMapping.put("CS LONG BEACH" , "Long Beach State");
		NCAABMapping.put("LIU BROOKLYN" , "LIU Brooklyn");
		NCAABMapping.put("LIU BROOKLYN" , "LIU Brooklyn");
		NCAABMapping.put("LONGWOOD" , "Longwood");
		NCAABMapping.put("LOUISIANA TECH" , "Louisiana Tech");
		NCAABMapping.put("LOUISIANA-LAFAYETTE" , "Louisiana");
		NCAABMapping.put("LOUISIANA-MONROE" , "UL Monroe");
		NCAABMapping.put("LOUISVILLE" , "Louisville");
		NCAABMapping.put("LOYOLA-MARYMOUNT" , "Loyola Marymount");
		NCAABMapping.put("LOYOLA-MARYLAND" , "Loyola (MD)");
		NCAABMapping.put("LOYOLA-CHICAGO" , "Loyola (Chi)");
		NCAABMapping.put("LOUISIANA STATE" , "LSU");
		NCAABMapping.put("UMASS LOWELL" , "UMass Lowell");
		NCAABMapping.put("MAINE" , "Maine");
		NCAABMapping.put("MANHATTAN" , "Manhattan");
		NCAABMapping.put("MARIST" , "Marist");
		NCAABMapping.put("MARQUETTE" , "Marquette");
		NCAABMapping.put("MARSHALL" , "Marshall");
		NCAABMapping.put("MARYLAND" , "Maryland");
		NCAABMapping.put("MCNEESE STATE" , "McNeese");
		NCAABMapping.put("MD-EASTERN SHORE" , "Maryland-Eastern Shore");
		NCAABMapping.put("MEMPHIS" , "Memphis");
		NCAABMapping.put("MERCER" , "Mercer");
		NCAABMapping.put("MIAMI (FL)" , "Miami");
		NCAABMapping.put("MIAMI (OH)" , "Miami (OH)");
		NCAABMapping.put("MICHIGAN" , "Michigan");
		NCAABMapping.put("MICHIGAN STATE" , "Michigan State");
		NCAABMapping.put("MIDDLE TENNESSEE ST." , "Middle Tennessee");
		NCAABMapping.put("MINNESOTA" , "Minnesota");
		NCAABMapping.put("MISSISSIPPI" , "Ole Miss");
		NCAABMapping.put("MISSISSIPPI STATE" , "Mississippi State");
		NCAABMapping.put("MISSISSIPPI VALLEY ST." , "MISSISSIPPI Valley State");
		NCAABMapping.put("MISSOURI" , "Missouri");
		NCAABMapping.put("MISSOURI STATE" , "Missouri State");
		NCAABMapping.put("MISSOURI-KANSAS CITY" , "UMKC");
		NCAABMapping.put("MONMOUTH (NJ)" , "Monmouth");
		NCAABMapping.put("MONTANA" , "Montana");
		NCAABMapping.put("MONTANA STATE" , "Montana State");
		NCAABMapping.put("MOREHEAD STATE" , "Morehead State");
		NCAABMapping.put("MORGAN STATE" , "Morgan State");
		NCAABMapping.put("MOUNT ST. MARYS" , "Mt. St. Mary's");
		NCAABMapping.put("MURRAY STATE" , "Murray State");
		NCAABMapping.put("NORTHERN KENTUCKY" , "Northern Kentucky");
		NCAABMapping.put("NAVY" , "Navy");
		NCAABMapping.put("NEBRASKA-OMAHA" , "Omaha");
		NCAABMapping.put("NEBRASKA" , "Nebraska");
		NCAABMapping.put("NEVADA" , "Nevada");
		NCAABMapping.put("NEW HAMPSHIRE" , "New Hampshire");
		NCAABMapping.put("NEW MEXICO" , "New Mexico");
		NCAABMapping.put("NEW MEXICO STATE" , "New Mexico State");
		NCAABMapping.put("NEW ORLEANS" , "New Orleans");
		NCAABMapping.put("NIAGARA" , "Niagara");
		NCAABMapping.put("NICHOLLS STATE" , "Nicholls State");
		NCAABMapping.put("NEW JERSEY TECH" , "NJIT");
		NCAABMapping.put("NJIT" , "NJIT");
		NCAABMapping.put("NORFOLK STATE" , "Norfolk State");
		NCAABMapping.put("NORTH CAROLINA" , "North Carolina");
		NCAABMapping.put("NORTH CAROLINA AT" , "North Carolina A&amp;T");
		NCAABMapping.put("NORTH CAROLINA CENTRAL" , "North Carolina Central");
		NCAABMapping.put("NORTH CAROLINA STATE" , "NC State");
		NCAABMapping.put("NORTH DAKOTA" , "North Dakota");
		NCAABMapping.put("NORTH DAKOTA STATE" , "North Dakota State");
		NCAABMapping.put("NORTH FLORIDA" , "North Florida");
		NCAABMapping.put("NORTH TEXAS" , "North Texas");
		NCAABMapping.put("NORTHEASTERN" , "Northeastern");
		NCAABMapping.put("NORTHERN ARIZONA" , "Northern Arizona");
		NCAABMapping.put("NORTHERN COLORADO" , "Northern Colorado");
		NCAABMapping.put("NORTHERN ILLINOIS" , "Northern Illinois");
		NCAABMapping.put("NORTHERN IOWA" , "Northern Iowa");
		NCAABMapping.put("NORTHWESTERN" , "Northwestern");
		NCAABMapping.put("NORTHWESTERN STATE" , "Northwestern State");
		NCAABMapping.put("NOTRE DAME" , "Notre Dame");
		NCAABMapping.put("OAKLAND" , "Oakland");
		NCAABMapping.put("OHIO" , "Ohio");
		NCAABMapping.put("OHIO STATE" , "Ohio State");
		NCAABMapping.put("OKLAHOMA" , "Oklahoma");
		NCAABMapping.put("OKLAHOMA STATE" , "Oklahoma State");
		NCAABMapping.put("OLD DOMINION" , "Old Dominion");
		NCAABMapping.put("ORAL ROBERTS" , "Oral Roberts");
		NCAABMapping.put("OREGON" , "Oregon");
		NCAABMapping.put("OREGON STATE" , "Oregon State");
		NCAABMapping.put("PACIFIC" , "Pacific");
		NCAABMapping.put("PENN STATE" , "Penn State");
		NCAABMapping.put("PENNSYLVANIA" , "PENNSYLVANIA");
		NCAABMapping.put("PEPPERDINE" , "Pepperdine");
		NCAABMapping.put("PITTSBURGH" , "Pittsburgh");
		NCAABMapping.put("PORTLAND" , "Portland");
		NCAABMapping.put("PORTLAND STATE" , "Portland State");
		NCAABMapping.put("PRAIRIE VIEW AM" , "PRAIRIE VIEW AM");
		NCAABMapping.put("PRESBYTERIAN" , "Presbyterian");
		NCAABMapping.put("PRINCETON" , "Princeton");
		NCAABMapping.put("PROVIDENCE" , "Providence");
		NCAABMapping.put("PURDUE" , "Purdue");
		NCAABMapping.put("QUINNIPIAC" , "Quinnipiac");
		NCAABMapping.put("RADFORD" , "Radford");
		NCAABMapping.put("RHODE ISLAND" , "Rhode Island");
		NCAABMapping.put("RICE" , "Rice");
		NCAABMapping.put("RICHMOND" , "Richmond");
		NCAABMapping.put("RIDER" , "Rider");
		NCAABMapping.put("ROBERT MORRIS" , "Robert Morris");
		NCAABMapping.put("RUTGERS" , "Rutgers");
		NCAABMapping.put("CS SACRAMENTO" , "Sacramento State");
		NCAABMapping.put("SACRED HEART" , "Sacred Heart");
		NCAABMapping.put("SAM HOUSTON STATE" , "Sam Houston State");
		NCAABMapping.put("SAMFORD" , "Samford");
		NCAABMapping.put("SAN DIEGO" , "San Diego");
		NCAABMapping.put("SAN DIEGO STATE" , "San Diego State");
		NCAABMapping.put("SAN FRANCISCO" , "San Francisco");
		NCAABMapping.put("SAN JOSE STATE" , "San Jose State");
		NCAABMapping.put("UC SANTA BARBARA" , "UC Santa Barbara");
		NCAABMapping.put("SANTA CLARA" , "Santa Clara");
		NCAABMapping.put("SAVANNAH STATE" , "Savannah State");
		NCAABMapping.put("USC UPSTATE" , "South Carolina Upstate");
		NCAABMapping.put("SEATTLE" , "Seattle");
		NCAABMapping.put("SETON HALL" , "Seton Hall");
		NCAABMapping.put("SIENA" , "Siena");
		NCAABMapping.put("SOUTHERN METHODIST" , "SMU");
		NCAABMapping.put("SOUTH ALABAMA" , "South Alabama");
		NCAABMapping.put("SOUTH CAROLINA" , "South Carolina");
		NCAABMapping.put("SOUTH CAROLINA STATE" , "South Carolina State");
		NCAABMapping.put("SOUTH DAKOTA" , "South Dakota");
		NCAABMapping.put("SOUTH DAKOTA STATE" , "South Dakota State");
		NCAABMapping.put("SOUTH FLORIDA" , "South Florida");
		NCAABMapping.put("SE MISSOURI STATE" , "SE MISSOURI STATE");
		NCAABMapping.put("SE LOUISIANA" , "SE Louisiana");
		NCAABMapping.put("SOUTHERN AM" , "Southern");
		NCAABMapping.put("SIU EDWARDSVILLE", "SIU Edwardsville");
		NCAABMapping.put("SOUTHERN ILLINOIS" , "Southern Illinois");
		NCAABMapping.put("SOUTHERN MISS" , "Southern Miss");
		NCAABMapping.put("SOUTHERN UTAH" , "Southern Utah");
		NCAABMapping.put("ST. BONAVENTURE" , "St. Bonaventure");
		NCAABMapping.put("ST. FRANCIS (PA)" , "St. Francis (PA)");
		NCAABMapping.put("ST. FRANCIS (NY)" , "St. Francis BKN");
		NCAABMapping.put("ST. JOHNS" , "St. John's");
		NCAABMapping.put("ST. JOSEPHS (PA)" , "Saint Joseph's");
		NCAABMapping.put("ST. LOUIS" , "MISSOURI-ST. LOUIS");
		NCAABMapping.put("ST. MARYS (CA)" , "Saint Mary's");
		NCAABMapping.put("ST. PETERS" , "Saint Peter's");
		NCAABMapping.put("STANFORD" , "Stanford");
		NCAABMapping.put("STEPHEN F. AUSTIN" , "Stephen F. Austin");
		NCAABMapping.put("STETSON" , "Stetson");
		NCAABMapping.put("STONY BROOK" , "Stony Brook");
		NCAABMapping.put("SYRACUSE" , "Syracuse");
		NCAABMapping.put("TEXAS CHRISTIAN" , "TCU");
		NCAABMapping.put("TEMPLE" , "Temple");
		NCAABMapping.put("TENNESSEE" , "Tennessee");
		NCAABMapping.put("TENNESSEE STATE" , "Tennessee State");
		NCAABMapping.put("TENNESSEE TECH" , "Tennessee Tech");
		NCAABMapping.put("TEXAS" , "Texas");
		NCAABMapping.put("TEXAS AM" , "Texas A&amp;M");
		NCAABMapping.put("TEXAS AM-CC" , "Texas A&amp;M-CC");
		NCAABMapping.put("TEXAS SOUTHERN" , "Texas Southern");
		NCAABMapping.put("TEXAS STATE" , "Texas State");
		NCAABMapping.put("TEXAS TECH" , "Texas Tech");
		NCAABMapping.put("THE CITADEL" , "The Citadel");
		NCAABMapping.put("TOLEDO" , "Toledo");
		NCAABMapping.put("TOWSON" , "Towson");
		NCAABMapping.put("TROY" , "Troy");
		NCAABMapping.put("TULANE" , "Tulane");
		NCAABMapping.put("TULSA" , "Tulsa");
		NCAABMapping.put("UAB" , "UAB");
		NCAABMapping.put("UC DAVIS" , "UC Davis");
		NCAABMapping.put("UC IRVINE" , "UC Irvine");
		NCAABMapping.put("UC RIVERSIDE" , "UC Riverside");
		NCAABMapping.put("CENTRAL FLORIDA" , "UCF");
		NCAABMapping.put("UCLA" , "UCLA");
		NCAABMapping.put("MASSACHUSETTS" , "UMASS");
		NCAABMapping.put("MD-BALTIMORE COUNTY" , "UMBC");
		NCAABMapping.put("UNC ASHEVILLE" , "UNC Asheville");
		NCAABMapping.put("UNC GREENSBORO" , "UNC Greensboro");
		NCAABMapping.put("UNC WILMINGTON" , "UNC Wilmington");
		NCAABMapping.put("UNLV" , "UNLV");
		NCAABMapping.put("SOUTHERN CAL" , "USC");
		NCAABMapping.put("TEXAS-ARLINGTON" , "UT ARLINGTON");
		NCAABMapping.put("UT MARTIN" , "Tenn-Martin");
		NCAABMapping.put("UTAH" , "Utah");
		NCAABMapping.put("UTAH STATE" , "Utah State");
		NCAABMapping.put("UTAH VALLEY" , "Utah Valley");
		NCAABMapping.put("UTEP" , "UTEP");
		NCAABMapping.put("TEXAS RIO GRANDE" , "UT Rio Grande Valley");
		NCAABMapping.put("TEXAS-SAN ANTONIO" , "UTSA");
		NCAABMapping.put("VIRGINIA COMMONWEALTH" , "VCU");
		NCAABMapping.put("VALPARAISO" , "Valparaiso");
		NCAABMapping.put("VANDERBILT" , "Vanderbilt");
		NCAABMapping.put("VERMONT" , "Vermont");
		NCAABMapping.put("VILLANOVA" , "Villanova");
		NCAABMapping.put("VIRGINIA" , "Virginia");
		NCAABMapping.put("VIRGINIA TECH" , "Virginia Tech");
		NCAABMapping.put("VIRGINIA MILITARY" , "VMI");
		NCAABMapping.put("WAGNER" , "Wagner");
		NCAABMapping.put("WAKE FOREST" , "Wake Forest");
		NCAABMapping.put("WASHINGTON" , "Washington");
		NCAABMapping.put("WASHINGTON STATE" , "Washington State");
		NCAABMapping.put("WEBER STATE" , "Weber State");
		NCAABMapping.put("WEST VIRGINIA" , "West Virginia");
		NCAABMapping.put("WESTERN CAROLINA" , "Western Carolina");
		NCAABMapping.put("WESTERN ILLINOIS" , "Western Illinois");
		NCAABMapping.put("WESTERN KENTUCKY" , "Western Kentucky");
		NCAABMapping.put("WESTERN MICHIGAN" , "Western Michigan");
		NCAABMapping.put("UW GREEN BAY" , "Green Bay");
		NCAABMapping.put("UW MILWAUKEE" , "Milwaukee");
		NCAABMapping.put("WICHITA STATE" , "Wichita State");
		NCAABMapping.put("WILLIAM MARY" , "William &amp; Mary");
		NCAABMapping.put("WINTHROP" , "Winthrop");
		NCAABMapping.put("WISCONSIN" , "Wisconsin");
		NCAABMapping.put("WOFFORD" , "Wofford");
		NCAABMapping.put("WRIGHT STATE" , "Wright State");
		NCAABMapping.put("WYOMING" , "Wyoming");
		NCAABMapping.put("XAVIER" , "Xavier");
		NCAABMapping.put("YALE" , "Yale");
		NCAABMapping.put("YOUNGSTOWN STATE" , "Youngstown State");

		NCAABKPMapping.put("Abilene Christian", "Abilene Christian");
		NCAABKPMapping.put("Air Force", "Air Force");
		NCAABKPMapping.put("Akron","Akron");
		NCAABKPMapping.put("ALABAMA AM","Alabama AM");
		NCAABKPMapping.put("ALABAMA A&M","Alabama AM");
		NCAABKPMapping.put("Alabama","Alabama");
		NCAABKPMapping.put("Alabama State","Alabama State");
		NCAABKPMapping.put("Albany","Albany");
		NCAABKPMapping.put("Alcorn State","Alcorn State");
		NCAABKPMapping.put("AMERICAN","American University");
		NCAABKPMapping.put("Appalachian State","Appalachian State");
		NCAABKPMapping.put("ARKANSAS-LITTLE ROCK","Arkansas Little Rock");
		NCAABKPMapping.put("Arizona","Arizona");
		NCAABKPMapping.put("Arizona State","Arizona State");
		NCAABKPMapping.put("ARKANSAS-PINE BLUFF","Arkansas Pine Bluff");
		NCAABKPMapping.put("Arkansas","Arkansas");
		NCAABKPMapping.put("Arkansas State","Arkansas State");
		NCAABKPMapping.put("Army","Army");
		NCAABKPMapping.put("Auburn","Auburn");
		NCAABKPMapping.put("AUSTIN PEAY STATE","Austin Peay");
		NCAABKPMapping.put("Ball State","Ball State");
		NCAABKPMapping.put("Baylor","Baylor");
		NCAABKPMapping.put("Belmont","Belmont");
		NCAABKPMapping.put("Bethune Cookman","Bethune-Cookman");
		NCAABKPMapping.put("Binghamton","Binghamton");
		NCAABKPMapping.put("Boise State","Boise State");
		NCAABKPMapping.put("Boston College","Boston College");
		NCAABKPMapping.put("Boston University","Boston University");
		NCAABKPMapping.put("Bowling Green State","Bowling Green");
		NCAABKPMapping.put("Bradley","Bradley");
		NCAABKPMapping.put("Brown","Brown");
		NCAABKPMapping.put("Bryant","Bryant");
		NCAABKPMapping.put("Bucknell","Bucknell");
		NCAABKPMapping.put("SUNY-BUFFALO","Buffalo");
		NCAABKPMapping.put("Butler","Butler");
		NCAABKPMapping.put("BYU","BYU");
		NCAABKPMapping.put("Cal Poly SLO","Cal Poly-SLO");
		NCAABKPMapping.put("CS Northridge","CS Northridge");
		NCAABKPMapping.put("California","California");
		NCAABKPMapping.put("Campbell","Campbell");
		NCAABKPMapping.put("Canisius","Canisius");
		NCAABKPMapping.put("Central Arkansas","Central Arkansas");
		NCAABKPMapping.put("Central Connecticut St.","Central Connecticut State");
		NCAABKPMapping.put("CENTRAL FLORIDA","Central Florida");
		NCAABKPMapping.put("Central Michigan","Central Michigan");
		NCAABKPMapping.put("Charleston Southern","Charleston Southern");
		NCAABKPMapping.put("UNC Charlotte","Charlotte");
		NCAABKPMapping.put("UT Chattanooga","Chattanooga");
		NCAABKPMapping.put("Chicago State","Chicago State");
		NCAABKPMapping.put("Cincinnati","Cincinnati");
		NCAABKPMapping.put("The Citadel","The Citadel");
		NCAABKPMapping.put("Clemson","Clemson");
		NCAABKPMapping.put("Cleveland State","Cleveland State");
		NCAABKPMapping.put("Coastal Carolina","Coastal Carolina");
		NCAABKPMapping.put("College of Charleston","College of Charleston");
		NCAABKPMapping.put("Colgate","Colgate");
		NCAABKPMapping.put("Colorado","Colorado");
		NCAABKPMapping.put("Colorado State","Colorado State");
		NCAABKPMapping.put("Columbia","Columbia");
		NCAABKPMapping.put("Connecticut","Connecticut");
		NCAABKPMapping.put("Coppin State","Coppin State");
		NCAABKPMapping.put("Cornell","Cornell");
		NCAABKPMapping.put("Creighton","Creighton");
		NCAABKPMapping.put("CS Bakersfield","CS Bakersfield");
		NCAABKPMapping.put("CS Fullerton","CS Fullerton");
		NCAABKPMapping.put("Dartmouth","Dartmouth");
		NCAABKPMapping.put("Davidson","Davidson");
		NCAABKPMapping.put("Dayton","Dayton");
		NCAABKPMapping.put("Delaware","Delaware");
		NCAABKPMapping.put("Delaware State","Delaware State");
		NCAABKPMapping.put("Denver","Denver");
		NCAABKPMapping.put("DePaul","DePaul");
		NCAABKPMapping.put("Detroit MERCY","Detroit Mercy");
		NCAABKPMapping.put("Drake","Drake");
		NCAABKPMapping.put("Drexel","Drexel");
		NCAABKPMapping.put("Duke","Duke");
		NCAABKPMapping.put("Duquesne","Duquesne");
		NCAABKPMapping.put("East Carolina","East Carolina");
		NCAABKPMapping.put("Eastern Illinois","Eastern Illinois");
		NCAABKPMapping.put("Eastern Kentucky","Eastern Kentucky");
		NCAABKPMapping.put("Eastern Michigan","Eastern Michigan");
		NCAABKPMapping.put("East Tennessee ST.","East Tennessee State");
		NCAABKPMapping.put("Eastern Washington","Eastern Washington");
		NCAABKPMapping.put("Elon","Elon");
		NCAABKPMapping.put("Evansville","Evansville");
		NCAABKPMapping.put("Fairleigh Dickinson","Fairleigh Dickinson");
		NCAABKPMapping.put("Fairfield","Fairfield");
		NCAABKPMapping.put("Florida Atlantic","Florida Atlantic");
		NCAABKPMapping.put("Florida Gulf Coast","Florida Gulf Coast");
		NCAABKPMapping.put("Florida","Florida");
		NCAABKPMapping.put("Florida AM","Florida A&M");
		NCAABKPMapping.put("FLORIDA INTERNATIONAL","Florida International");
		NCAABKPMapping.put("Florida State","Florida State");
		NCAABKPMapping.put("Fordham","Fordham");
		NCAABKPMapping.put("Fresno State","Fresno State");
		NCAABKPMapping.put("Furman","Furman");
		NCAABKPMapping.put("Georgia Southern","Georgia Southern");
		NCAABKPMapping.put("Georgia Tech","Georgia Tech");
		NCAABKPMapping.put("Gardner Webb","Gardner-Webb");
		NCAABKPMapping.put("George Mason","George Mason");
		NCAABKPMapping.put("George Washington","George Washington");
		NCAABKPMapping.put("Georgetown","Georgetown");
		NCAABKPMapping.put("Georgia","Georgia");
		NCAABKPMapping.put("Georgia State","Georgia State");
		NCAABKPMapping.put("Gonzaga","Gonzaga");
		NCAABKPMapping.put("Grambling State","Grambling State");
		NCAABKPMapping.put("Grand Canyon","Grand Canyon");
		NCAABKPMapping.put("Hampton","Hampton");
		NCAABKPMapping.put("Hartford","Hartford");
		NCAABKPMapping.put("Harvard","Harvard");
		NCAABKPMapping.put("Hawaii","Hawaii");
		NCAABKPMapping.put("High Point","High Point");
		NCAABKPMapping.put("Hofstra","Hofstra");
		NCAABKPMapping.put("Holy Cross","Holy Cross");
		NCAABKPMapping.put("Houston","Houston");
		NCAABKPMapping.put("Houston Baptist","Houston Baptist");
		NCAABKPMapping.put("Howard","Howard");
		NCAABKPMapping.put("Idaho","Idaho");
		NCAABKPMapping.put("Idaho State","Idaho State");
		NCAABKPMapping.put("Illinois-Chicago","Illinois-Chicago");
		NCAABKPMapping.put("Illinois","Illinois");
		NCAABKPMapping.put("Illinois State","Illinois State");
		NCAABKPMapping.put("Incarnate Word","Incarnate Word");
		NCAABKPMapping.put("Indiana","Indiana");
		NCAABKPMapping.put("Indiana State","Indiana State");
		NCAABKPMapping.put("Iona","Iona");
		NCAABKPMapping.put("Iowa","Iowa");
		NCAABKPMapping.put("Iowa State","Iowa State");
		NCAABKPMapping.put("FORT WAYNE","IUPUI");
		NCAABKPMapping.put("IUPU-FORT WAYNE","IUPUI");
		NCAABKPMapping.put("INDIANA-PURDUE","IUPUI");
		NCAABKPMapping.put("Jackson State","Jackson State");
		NCAABKPMapping.put("Jacksonville","Jacksonville");
		NCAABKPMapping.put("James Madison","James Madison");
		NCAABKPMapping.put("Jacksonville State","Jacksonville State");
		NCAABKPMapping.put("Kansas","Kansas");
		NCAABKPMapping.put("Kansas State","Kansas State");
		NCAABKPMapping.put("Kennesaw State","Kennesaw State");
		NCAABKPMapping.put("Kent State","Kent State");
		NCAABKPMapping.put("Kentucky","Kentucky");
		NCAABKPMapping.put("Lafayette","Lafayette");
		NCAABKPMapping.put("Louisiana-Monroe","Louisiana-Monroe");
		NCAABKPMapping.put("La Salle","La Salle");
		NCAABKPMapping.put("Louisiana Tech","Louisiana Tech");
		NCAABKPMapping.put("Louisiana-Lafayette","Louisiana Lafayette");
		NCAABKPMapping.put("Lamar","Lamar");
		NCAABKPMapping.put("Lehigh","Lehigh");
		NCAABKPMapping.put("CS LONG BEACH","Long Beach State");
		NCAABKPMapping.put("Liberty","Liberty");
		NCAABKPMapping.put("Lipscomb","Lipscomb");
		NCAABKPMapping.put("LIU Brooklyn","Long Island University");
		NCAABKPMapping.put("Longwood","Longwood");
		NCAABKPMapping.put("Louisville","Louisville");
		NCAABKPMapping.put("Loyola-Marymount","Loyola Marymount");
		NCAABKPMapping.put("Loyola-Chicago","Loyola-Chicago");
		NCAABKPMapping.put("Loyola-MARYLAND","Loyola-Maryland");
		NCAABKPMapping.put("LOUISIANA STATE","LSU");
		NCAABKPMapping.put("Maine","Maine");
		NCAABKPMapping.put("Manhattan","Manhattan");
		NCAABKPMapping.put("Marist","Marist");
		NCAABKPMapping.put("Marquette","Marquette");
		NCAABKPMapping.put("Marshall","Marshall");
		NCAABKPMapping.put("Maryland","Maryland");
		NCAABKPMapping.put("MD-BALTIMORE COUNTY","Maryland BC");
		NCAABKPMapping.put("MD-EASTERN SHORE","Maryland-Eastern Shore");
		NCAABKPMapping.put("UMass Lowell","Massachusetts Lowell");
		NCAABKPMapping.put("McNeese State","McNeese State");
		NCAABKPMapping.put("Memphis","Memphis");
		NCAABKPMapping.put("Mercer","Mercer");
		NCAABKPMapping.put("MIAMI (FL)","Miami-Florida");
		NCAABKPMapping.put("MIAMI (OH)","Miami-Ohio");
		NCAABKPMapping.put("Michigan","Michigan");
		NCAABKPMapping.put("Michigan State","Michigan State");
		NCAABKPMapping.put("MIDDLE TENNESSEE ST.","Middle Tennessee State");
		NCAABKPMapping.put("Minnesota","Minnesota");
		NCAABKPMapping.put("Mississippi State","Mississippi State");
		NCAABKPMapping.put("Mississippi Valley ST.","Mississippi Valley State");
		NCAABKPMapping.put("Mississippi","Mississippi");
		NCAABKPMapping.put("Missouri","Missouri");
		NCAABKPMapping.put("Missouri State","Missouri State");
		NCAABKPMapping.put("MONMOUTH (NJ)","Monmouth");
		NCAABKPMapping.put("Montana","Montana");
		NCAABKPMapping.put("Montana State","Montana State");
		NCAABKPMapping.put("Morehead State","Morehead State");
		NCAABKPMapping.put("Morgan State","Morgan State");
		NCAABKPMapping.put("MOUNT ST. MARYS","Mount State Mary's");
		NCAABKPMapping.put("Murray State","Murray State");
		NCAABKPMapping.put("North Alabama","North Alabama");
		NCAABKPMapping.put("Northern Arizona","Northern Arizona");
		NCAABKPMapping.put("North Carolina","North Carolina");
		NCAABKPMapping.put("Northern Colorado","Northern Colorado");
		NCAABKPMapping.put("North Dakota State","North Dakota State");
		NCAABKPMapping.put("North Florida","North Florida");
		NCAABKPMapping.put("New Hampshire","New Hampshire");
		NCAABKPMapping.put("Northern Illinois","Northern Illinois");
		NCAABKPMapping.put("Northern Iowa","Northern Iowa");
		NCAABKPMapping.put("Northern Kentucky","Northern Kentucky");
		NCAABKPMapping.put("New Mexico State","New Mexico State");
		NCAABKPMapping.put("Navy","Navy");
		NCAABKPMapping.put("North Carolina AT","NC A&T");
		NCAABKPMapping.put("North Carolina Central","NC Central");
		NCAABKPMapping.put("North Carolina State","NC State");
		NCAABKPMapping.put("UNC Asheville","NC Asheville");
		NCAABKPMapping.put("UNC Greensboro","NC Greensboro");
		NCAABKPMapping.put("UNC Wilmington","NC Wilmington");
		NCAABKPMapping.put("Nebraska-Omaha","Nebraska Omaha");
		NCAABKPMapping.put("Nebraska","Nebraska");
		NCAABKPMapping.put("Nevada","Nevada");
		NCAABKPMapping.put("New Mexico","New Mexico");
		NCAABKPMapping.put("New Orleans","New Orleans");
		NCAABKPMapping.put("Niagara","Niagara");
		NCAABKPMapping.put("Nicholls State","Nicholls St");
		NCAABKPMapping.put("NEW JERSEY TECH","NJIT");
		NCAABKPMapping.put("NJIT","NJIT");
		NCAABKPMapping.put("Norfolk State","Norfolk State");
		NCAABKPMapping.put("North Dakota","North Dakota");
		NCAABKPMapping.put("North Texas","North Texas");
		NCAABKPMapping.put("Northeastern","Northeastern");
		NCAABKPMapping.put("Northwestern","Northwestern");
		NCAABKPMapping.put("Notre Dame","Notre Dame");
		NCAABKPMapping.put("Northwestern State","Northwestern State");
		NCAABKPMapping.put("Oakland","Oakland");
		NCAABKPMapping.put("Ohio","Ohio");
		NCAABKPMapping.put("Ohio State","Ohio State");
		NCAABKPMapping.put("Oklahoma","Oklahoma");
		NCAABKPMapping.put("Oklahoma State","Oklahoma State");
		NCAABKPMapping.put("Old Dominion","Old Dominion");
		NCAABKPMapping.put("Oral Roberts","Oral Roberts");
		NCAABKPMapping.put("Oregon","Oregon");
		NCAABKPMapping.put("Oregon State","Oregon State");
		NCAABKPMapping.put("Pacific","Pacific");
		NCAABKPMapping.put("Penn State","Penn State");
		NCAABKPMapping.put("Pepperdine","Pepperdine");
		NCAABKPMapping.put("Pittsburgh","Pittsburgh");
		NCAABKPMapping.put("Portland","Portland");
		NCAABKPMapping.put("Portland State","Portland State");
		NCAABKPMapping.put("Prairie View AM","Prairie View AM");
		NCAABKPMapping.put("Presbyterian","Presbyterian");
		NCAABKPMapping.put("Princeton","Princeton");
		NCAABKPMapping.put("Providence","Providence");
		NCAABKPMapping.put("Purdue","Purdue");
		NCAABKPMapping.put("Quinnipiac","Quinnipiac");
		NCAABKPMapping.put("Radford","Radford");
		NCAABKPMapping.put("Rhode Island","Rhode Island");
		NCAABKPMapping.put("Rice","Rice");
		NCAABKPMapping.put("Richmond","Richmond");
		NCAABKPMapping.put("Rider","Rider");
		NCAABKPMapping.put("Robert Morris","Robert Morris");
		NCAABKPMapping.put("Rutgers","Rutgers");
		NCAABKPMapping.put("South Alabama","South Alabama");
		NCAABKPMapping.put("South Carolina State","South Carolina State");
		NCAABKPMapping.put("South Carolina","South Carolina");
		NCAABKPMapping.put("South Dakota State","South Dakota State");
		NCAABKPMapping.put("South Florida","South Florida");
		NCAABKPMapping.put("Southern Illinois","Southern Illinois");
		NCAABKPMapping.put("SOUTHERN METHODIST","SMU");
		NCAABKPMapping.put("Southern Miss","Southern Miss");
		NCAABKPMapping.put("Southern Utah","Southern Utah");
		NCAABKPMapping.put("CS SACRAMENTO","Sacramento State");
		NCAABKPMapping.put("Sacred Heart","Sacred Heart");
		NCAABKPMapping.put("Missouri-St. Louis","Saint Louis");
		NCAABKPMapping.put("Sam Houston State","Sam Houston State");
		NCAABKPMapping.put("Samford","Samford");
		NCAABKPMapping.put("San Diego","San Diego");
		NCAABKPMapping.put("San Diego State","San Diego State");
		NCAABKPMapping.put("San Francisco","San Francisco");
		NCAABKPMapping.put("San Jose State","San Jose State");
		NCAABKPMapping.put("Santa Clara","Santa Clara");
		NCAABKPMapping.put("Savannah State","Savannah State");
		NCAABKPMapping.put("USC Upstate","South Carolina Upstate");
		NCAABKPMapping.put("SE LOUISIANA","SE Louisiana");
		NCAABKPMapping.put("SE MISSOURI STATE","SE Missouri");
		NCAABKPMapping.put("Seattle","Seattle");
		NCAABKPMapping.put("Seton Hall","Seton Hall");
		NCAABKPMapping.put("Siena","Siena");
		NCAABKPMapping.put("SIU Edwardsville","SIU-Edwardsville");
		NCAABKPMapping.put("South Dakota","South Dakota");
		NCAABKPMapping.put("Southern AM","Southern U");
		NCAABKPMapping.put("ST. Bonaventure","Saint Bonaventure");
		NCAABKPMapping.put("ST. FRANCIS (NY)","Saint Francis-NY");
		NCAABKPMapping.put("ST. FRANCIS (PA)","Saint Francis-Pa.");
		NCAABKPMapping.put("ST. JOHNS","St. John's");
		NCAABKPMapping.put("ST. JOSEPHS (PA)","Saint Joseph's-Pa.");
		NCAABKPMapping.put("ST. MARYS (CA)","Saint Mary's-California");
		NCAABKPMapping.put("ST. PETERS","Saint Peter's");
		NCAABKPMapping.put("Stanford","Stanford");
		NCAABKPMapping.put("Stephen F. Austin","Stephen F. Austin");
		NCAABKPMapping.put("Stetson","Stetson");
		NCAABKPMapping.put("Stony Brook","Stony Brook");
		NCAABKPMapping.put("Syracuse","Syracuse");
		NCAABKPMapping.put("Temple","Temple");
		NCAABKPMapping.put("Tennessee","Tennessee");
		NCAABKPMapping.put("Texas","Texas");
		NCAABKPMapping.put("Texas AM","Texas A&M");
		NCAABKPMapping.put("Texas State","Texas State");
		NCAABKPMapping.put("Texas Tech","Texas Tech");
		NCAABKPMapping.put("UT Martin","Tennessee-Martin");
		NCAABKPMapping.put("Tennessee State","Tennessee State");
		NCAABKPMapping.put("Tennessee Tech","Tennessee Tech");
		NCAABKPMapping.put("Toledo","Toledo");
		NCAABKPMapping.put("Towson","Towson");
		NCAABKPMapping.put("Troy","Troy");
		NCAABKPMapping.put("Tulane","Tulane");
		NCAABKPMapping.put("Tulsa","Tulsa");
		NCAABKPMapping.put("TEXAS AM-CC","Texas A&M-CC");
		NCAABKPMapping.put("TEXAS CHRISTIAN","TCU");
		NCAABKPMapping.put("UTEP","UTEP");
		NCAABKPMapping.put("Texas Southern","Texas Southern");
		NCAABKPMapping.put("TEXAS-ARLINGTON","Texas-Arlington");
		NCAABKPMapping.put("TEXAS RIO GRANDE","Texas Rio Grande Valley");
		NCAABKPMapping.put("TEXAS-SAN ANTONIO","UTSA");
		NCAABKPMapping.put("Massachusetts","Massachusetts");
		NCAABKPMapping.put("PENNSYLVANIA","Pennsylvania");
		NCAABKPMapping.put("UAB","UAB");
		NCAABKPMapping.put("UC Davis","UC Davis");
		NCAABKPMapping.put("UC Irvine","UC Irvine");
		NCAABKPMapping.put("UC Riverside","UC Riverside");
		NCAABKPMapping.put("UCLA","UCLA");
		NCAABKPMapping.put("UC Santa Barbara","UC Santa Barbara");
		NCAABKPMapping.put("MISSOURI-KANSAS CITY","UMKC");
		NCAABKPMapping.put("UNLV","UNLV");
		NCAABKPMapping.put("SOUTHERN CAL","USC");
		NCAABKPMapping.put("Utah","Utah");
		NCAABKPMapping.put("Utah State","Utah State");
		NCAABKPMapping.put("Utah Valley","Utah Valley State");
		NCAABKPMapping.put("VIRGINIA MILITARY","VMI");
		NCAABKPMapping.put("Virginia Tech","Virginia Tech");
		NCAABKPMapping.put("Valparaiso","Valparaiso");
		NCAABKPMapping.put("Vanderbilt","Vanderbilt");
		NCAABKPMapping.put("VIRGINIA COMMONWEALTH","VCU");
		NCAABKPMapping.put("Vermont","Vermont");
		NCAABKPMapping.put("Villanova","Villanova");
		NCAABKPMapping.put("Virginia","Virginia");
		NCAABKPMapping.put("Western Carolina","Western Carolina");
		NCAABKPMapping.put("Western Illinois","Western Illinois");
		NCAABKPMapping.put("Western Kentucky","Western Kentucky");
		NCAABKPMapping.put("Western Michigan","Western Michigan");
		NCAABKPMapping.put("West Virginia","West Virginia");
		NCAABKPMapping.put("Wagner","Wagner");
		NCAABKPMapping.put("Wake Forest","Wake Forest");
		NCAABKPMapping.put("Washington State","Washington State");
		NCAABKPMapping.put("Washington","Washington");
		NCAABKPMapping.put("Weber State","Weber State");
		NCAABKPMapping.put("UW GREEN BAY","Wisconsin Green Bay");
		NCAABKPMapping.put("UW MILWAUKEE","Wisconsin-Milwaukee");
		NCAABKPMapping.put("Wichita State","Wichita State");
		NCAABKPMapping.put("Winthrop","Winthrop");
		NCAABKPMapping.put("Wisconsin","Wisconsin");
		NCAABKPMapping.put("WILLIAM MARY","William & Mary");
		NCAABKPMapping.put("Wofford","Wofford");
		NCAABKPMapping.put("Wright State","Wright State");
		NCAABKPMapping.put("Wyoming","Wyoming");
		NCAABKPMapping.put("Xavier","Xavier");
		NCAABKPMapping.put("Yale","Yale");
		NCAABKPMapping.put("Youngstown State","Youngstown State");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * @param xhtml
	 * @param week
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<VegasInsiderGame> getNcaafGameData(String xhtml, Integer week, Integer year) throws BatchException {
		LOGGER.info("Entering getNcaafGameData()");
		final List<VegasInsiderGame> gameData = new ArrayList<VegasInsiderGame>();

		final Document doc = parseXhtml(xhtml);
		final Elements trs = doc.select(".SLTables4 table tbody tr");
		String gamedate = null;
		String gametime = null;

		for (Element tr : trs) {
			if (tr.hasAttr("valign")) {
				final Elements tddates = tr.select(".A3");
				if (tddates != null && tddates.size() > 0) {
					final Element td = tddates.get(0);
					final Elements strongs = td.select("strong");
					if (strongs != null && strongs.size() > 0) {
						final Element strong = strongs.get(0);
						gamedate = strong.html();
						int index = gamedate.indexOf(" - ");
						if (index != -1) {
							gamedate = gamedate.substring(index + 3).trim();
							// Thursday September 27, 2018
						}
					}
				}
			} else {
				final VegasInsiderGameData awayteam = new VegasInsiderGameData();
				final VegasInsiderGameData hometeam = new VegasInsiderGameData();
				awayteam.setWeek(week);
				hometeam.setWeek(week);
				awayteam.setYear(year);
				hometeam.setYear(year);

				final Elements gametrs = tr.select(".sportPicksBorder table tbody tr");
				int gametrcount = 0;
				String thedata = "";
				for (Element gametr : gametrs) {
					try {
						switch (gametrcount++) {
							case 0:
								final Elements gametds = gametr.select("td");
								parseTeams(gametds, awayteam, hometeam);
								break;
							case 1:
								if (!gametr.html().contains("Final Score")) {
									final Elements tds = gametr.select("td");
									if (tds != null && tds.size() > 1) {
										//  8:00 PM Game Time 
										final Element td = tds.get(1);
										gametime = td.html();
										int index = gametime.indexOf("Game Time");
										if (index != -1) {
											gametime = gametime.substring(0, index).trim();
										}
									}
								} else {
									gametime = "12:00 AM";
								}
								break;
							case 3:
								final Elements gameinfoawaytds = gametr.select("td");
								parseTeamInfo(gameinfoawaytds, awayteam);
								break;
							case 4:
								final Elements gameinfohometds = gametr.select("td");
								parseTeamInfo(gameinfohometds, hometeam);
	
								int finalscore = hometeam.getFinalscore() - awayteam.getFinalscore();
								if (finalscore > 0) {
									hometeam.setWin(true);
								} else {
									awayteam.setWin(false);
								}
	
								final VegasInsiderGame vegasInsiderGame = new VegasInsiderGame();
								vegasInsiderGame.setAwayteamdata(awayteam);
								vegasInsiderGame.setHometeamdata(hometeam);
								if (awayteam.getLine() != null && awayteam.getLine() != 0) {
									vegasInsiderGame.setLine(awayteam.getLine());
									vegasInsiderGame.setLinefavorite(awayteam.getLinefavorite());
									vegasInsiderGame.setTotal(hometeam.getTotal());
								} else {
									vegasInsiderGame.setLine(hometeam.getLine());
									vegasInsiderGame.setLinefavorite(hometeam.getLinefavorite());
									vegasInsiderGame.setTotal(awayteam.getTotal());
								}
	
								Date dateofgame = null;
								try {
									// Thursday September 27, 2018 08:00 PM
									if (gamedate != null) {
										thedata = gametime;
										LOGGER.debug("gametime: " + gametime);

										if (gametime != null && gametime.length() > 0) {
											if (gametime.contains("PPD")) {
												throw new Exception("Postponed game");
											}

											gametime = gametime.replace("<span class=\"sub_title_red\">PPD</span>","");
											gametime = gametime.replace("<span class=\"sub_title_red\"></span>","");
											gametime = gametime.replace("<span class=\"sub_title_red\">","");
											if (gametime.contains(" - ")) {
												gametime = "12:00 AM";
											}
											
											if (gametime.length() > 0) {
												String ddate = gamedate + " " + gametime + " " + timeZoneLookup("ET", offset);
												LOGGER.debug("ddate: " + ddate);
												dateofgame = DATE_FORMAT.parse(ddate);
											} else {
												String ddate = gamedate + " " + "12:00 AM" + " " + timeZoneLookup("ET", offset);
												LOGGER.debug("ddate: " + ddate);
												dateofgame = DATE_FORMAT.parse(ddate);												
											}
										}
									} else {
										String ddate = gamedate + " " + "12:00 AM" + " " + timeZoneLookup("ET", offset);
										ddate = ddate.replace("<span class=\"sub_title_red\">PPD</span>","");
										ddate = ddate.replace("<span class=\"sub_title_red\"></span>","");
										ddate = ddate.replace("<span class=\"sub_title_red\">","");
										dateofgame = DATE_FORMAT.parse(ddate);
										dateofgame = new Date();
									}
								} catch (ParseException pe) {
									LOGGER.debug("thedata: " + thedata);
									LOGGER.debug(pe.getMessage(), pe);
								}
	
								vegasInsiderGame.setGameid(awayteam.getTeamname() + hometeam.getTeamname() + gamedate);
								awayteam.setDate(dateofgame);
								hometeam.setDate(dateofgame);
								vegasInsiderGame.setWeek(week);
								vegasInsiderGame.setYear(year);
								vegasInsiderGame.setDate(dateofgame);
								LOGGER.debug("vegasInsiderGameXXX: " + vegasInsiderGame);
								LOGGER.debug("gameData.size(): " + gameData.size());
	
								if (gameData.size() == 0) {
									gameData.add(vegasInsiderGame);
								} else {
									boolean found = false;
									for (int x = 0; x < gameData.size(); x++) {
										final VegasInsiderGame vig = gameData.get(x);
										if (vig.getGameid().equals(vegasInsiderGame.getGameid())) {
											found = true;
										}
									}
	
									if (!found) {
										gameData.add(vegasInsiderGame);
									}
								}
								break;
							default:
								break;
						}
					} catch (Throwable t) {
						LOGGER.error("thedata: " + thedata);
						LOGGER.error(t.getMessage(), t);
					}
				}
			}
		}

		LOGGER.info("Exiting getNcaafGameData()");
		return gameData;
	}

	/**
	 * 
	 * @param xhtml
	 * @param week
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<VegasInsiderGame> getNcaafFCSGameData(String xhtml, Integer week, Integer year) throws BatchException {
		LOGGER.info("Entering getNcaafFCSGameData()");
		final List<VegasInsiderGame> gameData = new ArrayList<VegasInsiderGame>();

		final Document doc = parseXhtml(xhtml);
		final Elements trs = doc.select(".SLTables4 table tbody tr");
		String gamedate = null;
		String gametime = null;

		for (Element tr : trs) {
			if (tr.hasAttr("valign")) {
				final Elements tddates = tr.select(".A3");
				if (tddates != null && tddates.size() > 0) {
					final Element td = tddates.get(0);
					final Elements strongs = td.select("strong");
					if (strongs != null && strongs.size() > 0) {
						final Element strong = strongs.get(0);
						gamedate = strong.html();
						int index = gamedate.indexOf(" - ");
						if (index != -1) {
							gamedate = gamedate.substring(index + 3).trim();
							// Thursday September 27, 2018
						}
					}
				}
			} else {
				final VegasInsiderGameData awayteam = new VegasInsiderGameData();
				final VegasInsiderGameData hometeam = new VegasInsiderGameData();
				awayteam.setWeek(week);
				hometeam.setWeek(week);
				awayteam.setYear(year);
				hometeam.setYear(year);

				final Elements gametrs = tr.select(".sportPicksBorder table tbody tr");
				int gametrcount = 0;
				String thedata = "";
				for (Element gametr : gametrs) {
					try {
						switch (gametrcount++) {
							case 0:
								final Elements gametds = gametr.select("td");
								parseTeams(gametds, awayteam, hometeam);
								break;
							case 1:
								if (!gametr.html().contains("Final Score")) {
									final Elements tds = gametr.select("td");
									if (tds != null && tds.size() > 1) {
										//  8:00 PM Game Time 
										final Element td = tds.get(1);
										gametime = td.html();
										int index = gametime.indexOf("Game Time");
										if (index != -1) {
											gametime = gametime.substring(0, index).trim();
										}
									}
								} else {
									gametime = "11:00 AM";
								}
								break;
							case 3:
								final Elements gameinfoawaytds = gametr.select("td");
								parseTeamInfo(gameinfoawaytds, awayteam);
								break;
							case 4:
								final Elements gameinfohometds = gametr.select("td");
								parseTeamInfo(gameinfohometds, hometeam);
	
								int finalscore = hometeam.getFinalscore() - awayteam.getFinalscore();
								if (finalscore > 0) {
									hometeam.setWin(true);
								} else {
									awayteam.setWin(false);
								}
	
								final VegasInsiderGame vegasInsiderGame = new VegasInsiderGame();
								vegasInsiderGame.setAwayteamdata(awayteam);
								vegasInsiderGame.setHometeamdata(hometeam);
								if (awayteam.getLine() != null && awayteam.getLine() != 0) {
									vegasInsiderGame.setLine(awayteam.getLine());
									vegasInsiderGame.setLinefavorite(awayteam.getLinefavorite());
									vegasInsiderGame.setTotal(hometeam.getTotal());
								} else {
									vegasInsiderGame.setLine(hometeam.getLine());
									vegasInsiderGame.setLinefavorite(hometeam.getLinefavorite());
									vegasInsiderGame.setTotal(awayteam.getTotal());
								}
	
								Date dateofgame = null;
								try {
									// Thursday September 27, 2018 08:00 PM
									if (gamedate != null) {
										thedata = gametime;
										LOGGER.debug("gametime: " + gametime);

										if (gametime != null && gametime.length() > 0) {
											if (gametime.contains("PPD")) {
												throw new Exception("Postponed game");
											}

											gametime = gametime.replace("<span class=\"sub_title_red\">PPD</span>","");
											gametime = gametime.replace("<span class=\"sub_title_red\"></span>","");
											gametime = gametime.replace("<span class=\"sub_title_red\">","");
											if (gametime.contains(" - ")) {
												gametime = "12:00 PM";
											}
											
											if (gametime.length() > 0) {
												String ddate = gamedate + " " + gametime + " " + timeZoneLookup("ET", offset);
												LOGGER.debug("ddate: " + ddate);
												dateofgame = DATE_FORMAT.parse(ddate);
											} else {
												String ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
												LOGGER.debug("ddate: " + ddate);
												dateofgame = DATE_FORMAT.parse(ddate);												
											}
										}
									} else {
										String ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
										ddate = ddate.replace("<span class=\"sub_title_red\">PPD</span>","");
										ddate = ddate.replace("<span class=\"sub_title_red\"></span>","");
										ddate = ddate.replace("<span class=\"sub_title_red\">","");
										dateofgame = DATE_FORMAT.parse(ddate);
										dateofgame = new Date();
									}
								} catch (ParseException pe) {
									LOGGER.error("thedata: " + thedata);
									LOGGER.error(pe.getMessage(), pe);
								}
	
								vegasInsiderGame.setGameid(awayteam.getTeamname() + hometeam.getTeamname() + gamedate);
								awayteam.setDate(dateofgame);
								hometeam.setDate(dateofgame);
								vegasInsiderGame.setWeek(week);
								vegasInsiderGame.setYear(year);
								vegasInsiderGame.setDate(dateofgame);
								LOGGER.debug("vegasInsiderGameXXX: " + vegasInsiderGame);
								LOGGER.debug("gameData.size(): " + gameData.size());
	
								if (gameData.size() == 0) {
									gameData.add(vegasInsiderGame);
								} else {
									boolean found = false;
									for (int x = 0; x < gameData.size(); x++) {
										final VegasInsiderGame vig = gameData.get(x);
										if (vig.getGameid().equals(vegasInsiderGame.getGameid())) {
											found = true;
										}
									}
	
									if (!found) {
										gameData.add(vegasInsiderGame);
									}
								}
								break;
							default:
								break;
						}
					} catch (Throwable t) {
						LOGGER.error("thedata: " + thedata);
						LOGGER.error(t.getMessage(), t);
					}
				}
			}
		}

		LOGGER.info("Exiting getNcaafFCSGameData()");
		return gameData;
	}

	/**
	 * 
	 * @param xhtml
	 * @param month
	 * @param day
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<VegasInsiderGame> getNcaabGameData(String xhtml, Integer month, Integer day, Integer year) throws BatchException {
		LOGGER.info("Entering getNcaabGameData()");
		final List<VegasInsiderGame> gameData = new ArrayList<VegasInsiderGame>();

		final Document doc = parseXhtml(xhtml);
		final Elements tds = doc.select(".SLTables4 table tbody tr td");
		String thedata = "";
		String gamedate = month + "-" + day + "-" + year;
		String gametime = null;

		for (Element td : tds) {
			final Elements trs = td.select("table tbody tr");
			final VegasInsiderGame vegasInsiderGame = new VegasInsiderGame();
			final VegasInsiderGameData awayteam = new VegasInsiderGameData();
			final VegasInsiderGameData hometeam = new VegasInsiderGameData();
			int x = 0;

			// Loop through the data
			for (Element tr : trs) {
				switch (x++) {
					case 0:
						final Elements as = tr.select("td a");
						int y = 0;

						if (as != null && as.size() > 1) {
							for (Element a : as) {
								switch (y++) {
									case 0:
										awayteam.setTeamname(parseBasketballTeams(a.html().trim()));
										break;
									case 1:
										hometeam.setTeamname(parseBasketballTeams(a.html().trim()));
										break;
									default:
										break;
								}
							}
						} else {
							final Elements tdds = tr.select("td");
							if (tdds != null && tdds.size() > 0) {
								// JOHNSON WALES (CO) @
								// <a class="black" href="/college-basketball/teams/team-page.cfm/team/air-force">AIR FORCE</a>
								final String tdhtml = tdds.get(0).html();
								int index = tdhtml.indexOf("@");
								if (index != -1) {
									final String before = tdhtml.substring(0, index).trim();
									int bindex = before.indexOf("</a>");
									if (bindex != -1) {
										int bbindex = before.indexOf("\">");
										if (bbindex != -1) {
											awayteam.setTeamname(parseBasketballTeams(before.substring(bbindex + 2, bindex).trim()));
										}
									} else {
										awayteam.setTeamname(parseBasketballTeams(before.trim()));
									}

									final String after = tdhtml.substring(index + 1).trim();
									bindex = after.indexOf("</a>");
									if (bindex != -1) {
										int bbindex = after.indexOf("\">");
										if (bbindex != -1) {
											hometeam.setTeamname(parseBasketballTeams(after.substring(bbindex + 2, bindex).trim()));
										}
									} else {
										hometeam.setTeamname(parseBasketballTeams(after.trim()));
									}
								}
							}
						}
						break;
					case 1:
						final Elements gtrs = tr.select(".sportPicksBorder tbody tr");
						int z = 0;

						if (gtrs != null && gtrs.size() == 4) {
							for (Element gtr : gtrs) {
								try {
									switch (z++) {
										case 0:
											if (!gtr.html().contains("Final Score")) {
												final Elements gtds = gtr.select("td");
												if (gtds != null && gtds.size() > 0) {
													//  8:00 PM Game Time 
													final Element gtd = gtds.get(0);
													gametime = gtd.html();
													int index = gametime.indexOf("Game Time");
													if (index != -1) {
														gametime = gametime.substring(0, index).trim();
													}
													LOGGER.debug("gametime: " + gametime);
												}
											} else {
												gametime = "11:00 AM";
											}
											break;
										case 2:
											final Elements gameinfoawaytds = gtr.select("td");
											parseTeamInfo(gameinfoawaytds, awayteam);
											break;
										case 3:
											final Elements gameinfohometds = gtr.select("td");
											parseTeamInfo(gameinfohometds, hometeam);
				
											int finalscore = hometeam.getFinalscore() - awayteam.getFinalscore();
											if (finalscore > 0) {
												hometeam.setWin(true);
											} else {
												awayteam.setWin(false);
											}
											
											// Set the teams now
											vegasInsiderGame.setAwayteamdata(awayteam);
											vegasInsiderGame.setHometeamdata(hometeam);
		
											if (awayteam.getLine() != null && awayteam.getLine() != 0) {
												vegasInsiderGame.setLine(awayteam.getLine());
												vegasInsiderGame.setLinefavorite(awayteam.getLinefavorite());
												vegasInsiderGame.setTotal(hometeam.getTotal());
											} else {
												vegasInsiderGame.setLine(hometeam.getLine());
												vegasInsiderGame.setLinefavorite(hometeam.getLinefavorite());
												vegasInsiderGame.setTotal(awayteam.getTotal());
											}
				
											Date dateofgame = null;
											try {
												// Thursday September 27, 2018 08:00 PM
												if (gamedate != null) {
													thedata = gametime;
													LOGGER.debug("gametime: " + gametime);
		
													if (gametime != null && gametime.length() > 0) {
														if (gametime.contains("PPD")) {
															throw new Exception("Postponed game");
														}
		
														gametime = gametime.replace("<span class=\"sub_title_red\">PPD</span>","");
														gametime = gametime.replace("<span class=\"sub_title_red\"></span>","");
														gametime = gametime.replace("<span class=\"sub_title_red\">","");
														if (gametime.contains(" - ")) {
															gametime = "12:00 PM";
														}
														
														if (gametime.length() > 0) {
															String ddate = gamedate + " " + gametime + " " + timeZoneLookup("ET", offset);
															LOGGER.debug("ddate: " + ddate);
															dateofgame = DATE_FORMAT_BB.parse(ddate);
														} else {
															String ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
															LOGGER.debug("ddate: " + ddate);
															dateofgame = DATE_FORMAT_BB.parse(ddate);												
														}
													}
												} else {
													String ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
													ddate = ddate.replace("<span class=\"sub_title_red\">PPD</span>","");
													ddate = ddate.replace("<span class=\"sub_title_red\"></span>","");
													ddate = ddate.replace("<span class=\"sub_title_red\">","");
													dateofgame = DATE_FORMAT_BB.parse(ddate);
													dateofgame = new Date();
												}
											} catch (ParseException pe) {
												LOGGER.debug("thedata: " + thedata);
												LOGGER.debug(pe.getMessage(), pe);
											}
				
											vegasInsiderGame.setGameid(awayteam.getTeamname() + hometeam.getTeamname() + gamedate);
											awayteam.setDate(dateofgame);
											hometeam.setDate(dateofgame);
											vegasInsiderGame.setMonth(month);
											vegasInsiderGame.setDay(day);
											vegasInsiderGame.setYear(year);
											vegasInsiderGame.setDate(dateofgame);
											LOGGER.debug("vegasInsiderGameXXX: " + vegasInsiderGame);
											LOGGER.debug("gameData.size(): " + gameData.size());
				
											if (gameData.size() == 0) {
												gameData.add(vegasInsiderGame);
											} else {
												boolean found = false;
												for (int a = 0; a < gameData.size(); a++) {
													final VegasInsiderGame vig = gameData.get(a);
													if (vig.getGameid().equals(vegasInsiderGame.getGameid())) {
														found = true;
													}
												}
				
												if (!found) {
													gameData.add(vegasInsiderGame);
												}
											}
											break;
										case 5:
										{
											final Elements lineurls = gtr.select("td a");
											if (lineurls != null && lineurls.size() > 0) {
												vegasInsiderGame.setLineurl(lineurls.get(0).attr("href"));
											}
											break;
										}
										default:
											break;
									}
								} catch (Throwable t) {
									LOGGER.error(t.getMessage(), t);
								}
							}
						} else if (gtrs != null && gtrs.size() == 6) {
							parseNba(month, 
									day, 
									year,
									gamedate, 
									gtrs, 
									awayteam, 
									hometeam, 
									vegasInsiderGame,
									gameData);
						}
						break;
					default:
						break;
				}
			}
		}

		LOGGER.info("Exiting getNcaabGameData()");
		return gameData;
	}

	/**
	 * 
	 * @param xhtml
	 * @param month
	 * @param day
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<VegasInsiderGame> getKPNcaabGameData(String xhtml, Integer month, Integer day, Integer year) throws BatchException {
		LOGGER.info("Entering getKPNcaabGameData()");
		final List<VegasInsiderGame> gameData = new ArrayList<VegasInsiderGame>();

		final Document doc = parseXhtml(xhtml);
		final Elements tds = doc.select(".SLTables4 table tbody tr td");
		String thedata = "";
		String gamedate = month + "-" + day + "-" + year;
		String gametime = null;

		for (Element td : tds) {
			final Elements trs = td.select("table tbody tr");
			final VegasInsiderGame vegasInsiderGame = new VegasInsiderGame();
			final VegasInsiderGameData awayteam = new VegasInsiderGameData();
			final VegasInsiderGameData hometeam = new VegasInsiderGameData();
			int x = 0;

			// Loop through the data
			for (Element tr : trs) {
				switch (x++) {
					case 0:
						final Elements as = tr.select("td a");
						int y = 0;

						if (as != null && as.size() > 1) {
							for (Element a : as) {
								switch (y++) {
									case 0:
										awayteam.setTeamname(parseKPBasketballTeams(a.html().trim()));
										break;
									case 1:
										hometeam.setTeamname(parseKPBasketballTeams(a.html().trim()));
										break;
									default:
										break;
								}
							}
						} else {
							final Elements tdds = tr.select("td");
							if (tdds != null && tdds.size() > 0) {
								// JOHNSON WALES (CO) @
								// <a class="black" href="/college-basketball/teams/team-page.cfm/team/air-force">AIR FORCE</a>
								final String tdhtml = tdds.get(0).html();
								int index = tdhtml.indexOf("@");
								if (index != -1) {
									final String before = tdhtml.substring(0, index).trim();
									int bindex = before.indexOf("</a>");
									if (bindex != -1) {
										int bbindex = before.indexOf("\">");
										if (bbindex != -1) {
											awayteam.setTeamname(parseKPBasketballTeams(before.substring(bbindex + 2, bindex).trim()));
										}
									} else {
										awayteam.setTeamname(parseKPBasketballTeams(before.trim()));
									}

									final String after = tdhtml.substring(index + 1).trim();
									bindex = after.indexOf("</a>");
									if (bindex != -1) {
										int bbindex = after.indexOf("\">");
										if (bbindex != -1) {
											hometeam.setTeamname(parseKPBasketballTeams(after.substring(bbindex + 2, bindex).trim()));
										}
									} else {
										hometeam.setTeamname(parseKPBasketballTeams(after.trim()));
									}
								}
							}
						}
						break;
					case 1:
						final Elements gtrs = tr.select(".sportPicksBorder tbody tr");
						int z = 0;

						if (gtrs != null && gtrs.size() == 4) {
							for (Element gtr : gtrs) {
								try {
									switch (z++) {
										case 0:
											if (!gtr.html().contains("Final Score")) {
												final Elements gtds = gtr.select("td");
												if (gtds != null && gtds.size() > 0) {
													//  8:00 PM Game Time 
													final Element gtd = gtds.get(0);
													gametime = gtd.html();
													int index = gametime.indexOf("Game Time");
													if (index != -1) {
														gametime = gametime.substring(0, index).trim();
													}
													LOGGER.debug("gametime: " + gametime);
												}
											} else {
												gametime = "11:00 AM";
											}
											break;
										case 2:
											final Elements gameinfoawaytds = gtr.select("td");
											parseTeamInfo(gameinfoawaytds, awayteam);
											break;
										case 3:
											final Elements gameinfohometds = gtr.select("td");
											parseTeamInfo(gameinfohometds, hometeam);
				
											int finalscore = hometeam.getFinalscore() - awayteam.getFinalscore();
											if (finalscore > 0) {
												hometeam.setWin(true);
											} else {
												awayteam.setWin(false);
											}
											
											// Set the teams now
											vegasInsiderGame.setAwayteamdata(awayteam);
											vegasInsiderGame.setHometeamdata(hometeam);
		
											if (awayteam.getLine() != null && awayteam.getLine() != 0) {
												vegasInsiderGame.setLine(awayteam.getLine());
												vegasInsiderGame.setLinefavorite(awayteam.getLinefavorite());
												vegasInsiderGame.setTotal(hometeam.getTotal());
											} else {
												vegasInsiderGame.setLine(hometeam.getLine());
												vegasInsiderGame.setLinefavorite(hometeam.getLinefavorite());
												vegasInsiderGame.setTotal(awayteam.getTotal());
											}
				
											Date dateofgame = null;
											try {
												// Thursday September 27, 2018 08:00 PM
												if (gamedate != null) {
													thedata = gametime;
													LOGGER.debug("gametime: " + gametime);
		
													if (gametime != null && gametime.length() > 0) {
														if (gametime.contains("PPD")) {
															throw new Exception("Postponed game");
														}
		
														gametime = gametime.replace("<span class=\"sub_title_red\">PPD</span>","");
														gametime = gametime.replace("<span class=\"sub_title_red\"></span>","");
														gametime = gametime.replace("<span class=\"sub_title_red\">","");
														if (gametime.contains(" - ")) {
															gametime = "12:00 PM";
														}

														try {
															if (gametime.length() > 0) {
																String ddate = gamedate + " " + gametime + " " + timeZoneLookup("ET", offset);
																LOGGER.debug("ddate: " + ddate);
																dateofgame = DATE_FORMAT_BB.parse(ddate);
															} else {
																String ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
																LOGGER.debug("ddate: " + ddate);
																dateofgame = DATE_FORMAT_BB.parse(ddate);												
															}
														} catch (Throwable t) {
															LOGGER.warn(t.getMessage(), t);
														}
													}
												} else {
													String ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
													ddate = ddate.replace("<span class=\"sub_title_red\">PPD</span>","");
													ddate = ddate.replace("<span class=\"sub_title_red\"></span>","");
													ddate = ddate.replace("<span class=\"sub_title_red\">","");
													dateofgame = DATE_FORMAT_BB.parse(ddate);
													dateofgame = new Date();
												}
											} catch (ParseException pe) {
												LOGGER.debug("thedata: " + thedata);
												LOGGER.debug(pe.getMessage(), pe);
											}
				
											vegasInsiderGame.setGameid(awayteam.getTeamname() + hometeam.getTeamname() + gamedate);
											awayteam.setDate(dateofgame);
											hometeam.setDate(dateofgame);
											vegasInsiderGame.setMonth(month);
											vegasInsiderGame.setDay(day);
											vegasInsiderGame.setYear(year);
											vegasInsiderGame.setDate(dateofgame);
											LOGGER.debug("vegasInsiderGameXXX: " + vegasInsiderGame);
											LOGGER.debug("gameData.size(): " + gameData.size());
				
											if (gameData.size() == 0) {
												gameData.add(vegasInsiderGame);
											} else {
												boolean found = false;
												for (int a = 0; a < gameData.size(); a++) {
													final VegasInsiderGame vig = gameData.get(a);
													if (vig.getGameid().equals(vegasInsiderGame.getGameid())) {
														found = true;
													}
												}
				
												if (!found) {
													gameData.add(vegasInsiderGame);
												}
											}
											break;
										case 5:
										{
											final Elements lineurls = gtr.select("td a");
											if (lineurls != null && lineurls.size() > 0) {
												vegasInsiderGame.setLineurl(lineurls.get(0).attr("href"));
											}
											break;
										}
										default:
											break;
									}
								} catch (Throwable t) {
									LOGGER.error(t.getMessage(), t);
								}
							}
						} else if (gtrs != null && gtrs.size() == 6) {
							parseNba(month, 
									day, 
									year,
									gamedate, 
									gtrs, 
									awayteam, 
									hometeam, 
									vegasInsiderGame,
									gameData);
						}
						break;
					default:
						break;
				}
			}
		}

		LOGGER.info("Exiting getKPNcaabGameData()");
		return gameData;
	}

	/**
	 * 
	 * @param month
	 * @param day
	 * @param year
	 * @param gamedate
	 * @param gtrs
	 * @param awayteam
	 * @param hometeam
	 * @param vegasInsiderGame
	 * @param gameData
	 */
	private void parseNba(Integer month, 
			Integer day, 
			Integer year,
			String gamedate, 
			Elements gtrs, 
			VegasInsiderGameData awayteam, 
			VegasInsiderGameData hometeam, 
			VegasInsiderGame vegasInsiderGame,
			List<VegasInsiderGame> gameData) {
		int z = 0;
		String thedata = "";
		String gametime = null;

		for (Element gtr : gtrs) {
			try {
				switch (z++) {
					case 0:
						if (!gtr.html().contains("Final Score")) {
							final Elements gtds = gtr.select("td");
							if (gtds != null && gtds.size() > 0) {
								//  8:00 PM Game Time 
								final Element gtd = gtds.get(0);
								gametime = gtd.html().replace("&nbsp;", "");
								int index = gametime.indexOf("Game Time");
								if (index != -1) {
									gametime = gametime.substring(0, index).trim();
								}
								LOGGER.debug("gametime: " + gametime);
							}
						} else {
							gametime = "11:00 AM";
						}
						break;
					case 3:
						final Elements gameinfoawaytds = gtr.select("td");
						parseTeamInfo(gameinfoawaytds, awayteam);
						break;
					case 4:
						final Elements gameinfohometds = gtr.select("td");
						parseTeamInfo(gameinfohometds, hometeam);

						int finalscore = hometeam.getFinalscore() - awayteam.getFinalscore();
						if (finalscore > 0) {
							hometeam.setWin(true);
						} else {
							awayteam.setWin(false);
						}
						
						// Set the teams now
						vegasInsiderGame.setAwayteamdata(awayteam);
						vegasInsiderGame.setHometeamdata(hometeam);

						if (awayteam.getLine() != null && awayteam.getLine() != 0) {
							vegasInsiderGame.setLine(awayteam.getLine());
							vegasInsiderGame.setLinefavorite(awayteam.getLinefavorite());
							vegasInsiderGame.setTotal(hometeam.getTotal());
						} else {
							vegasInsiderGame.setLine(hometeam.getLine());
							vegasInsiderGame.setLinefavorite(hometeam.getLinefavorite());
							vegasInsiderGame.setTotal(awayteam.getTotal());
						}

						Date dateofgame = null;
						try {
							// Thursday September 27, 2018 08:00 PM
							if (gamedate != null) {
								thedata = gametime;
								LOGGER.debug("gametime: " + gametime);

								if (gametime != null && gametime.length() > 0) {
									if (gametime.contains("PPD")) {
										throw new Exception("Postponed game");
									}

									gametime = gametime.replace("<span class=\"sub_title_red\">PPD</span>","");
									gametime = gametime.replace("<span class=\"sub_title_red\"></span>","");
									gametime = gametime.replace("<span class=\"sub_title_red\">","");
									if (gametime.contains(" - ")) {
										gametime = "12:00 PM";
									}
									
									if (gametime.length() > 0) {
										String ddate = gamedate + " " + gametime + " " + timeZoneLookup("ET", offset);
										LOGGER.debug("ddate: " + ddate);
										dateofgame = DATE_FORMAT_BB.parse(ddate);
									} else {
										String ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
										LOGGER.debug("ddate: " + ddate);
										dateofgame = DATE_FORMAT_BB.parse(ddate);												
									}
								}
							} else {
								String ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
								ddate = ddate.replace("<span class=\"sub_title_red\">PPD</span>","");
								ddate = ddate.replace("<span class=\"sub_title_red\"></span>","");
								ddate = ddate.replace("<span class=\"sub_title_red\">","");
								dateofgame = DATE_FORMAT_BB.parse(ddate);
								dateofgame = new Date();
							}
						} catch (ParseException pe) {
							LOGGER.debug("thedata: " + thedata);
							LOGGER.debug(pe.getMessage(), pe);
						}

						vegasInsiderGame.setGameid(awayteam.getTeamname() + hometeam.getTeamname() + gamedate);
						awayteam.setDate(dateofgame);
						hometeam.setDate(dateofgame);
						vegasInsiderGame.setMonth(month);
						vegasInsiderGame.setDay(day);
						vegasInsiderGame.setYear(year);
						vegasInsiderGame.setDate(dateofgame);
						LOGGER.debug("vegasInsiderGameXXX: " + vegasInsiderGame);
						LOGGER.debug("gameData.size(): " + gameData.size());

						if (gameData.size() == 0) {
							gameData.add(vegasInsiderGame);
						} else {
							boolean found = false;
							for (int a = 0; a < gameData.size(); a++) {
								final VegasInsiderGame vig = gameData.get(a);
								if (vig.getGameid().equals(vegasInsiderGame.getGameid())) {
									found = true;
								}
							}

							if (!found) {
								gameData.add(vegasInsiderGame);
							}
						}
						break;
					case 5:
					{
						final Elements lineurls = gtr.select("td a");
						if (lineurls != null && lineurls.size() > 0) {
							vegasInsiderGame.setLineurl(lineurls.get(0).attr("href"));
						}
						break;
					}
					default:
						break;
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		}		
	}

	/**
	 * 
	 * @param xhtml
	 * @param month
	 * @param day
	 * @param year
	 * @return
	 * @throws BatchException
	 */
	public List<EventPackage> getNcaabGames(String xhtml, Integer month, Integer day, Integer year) throws BatchException {
		LOGGER.info("Entering getNcaabGameData()");
		final List<EventPackage> gameData = new ArrayList<EventPackage>();

		final Document doc = parseXhtml(xhtml);
		final Elements tds = doc.select(".SLTables4 table tbody tr td");
		String thedata = "";
		String gamedate = month + "-" + day + "-" + year;
		String gametime = null;

		for (Element td : tds) {
			final Elements trs = td.select("table tbody tr");
			final EventPackage ep = new EventPackage();
			ep.setSporttype("NCAAB");
			final TeamPackage awayteam = new TeamPackage();
			final TeamPackage hometeam = new TeamPackage();
			int x = 0;

			// Loop through the data
			for (Element tr : trs) {
				switch (x++) {
					case 0:
						final Elements as = tr.select("td a");
						int y = 0;

						if (as != null && as.size() == 2) {
							for (Element a : as) {
								switch (y++) {
									case 0:
										awayteam.setTeam(parseBasketballTeams(a.html().trim()));
										break;
									case 1:
										hometeam.setTeam(parseBasketballTeams(a.html().trim()));
										break;
									default:
										break;
								}
							}
						} else {
							final Elements tdds = tr.select("td");
							if (tdds != null && tdds.size() > 0) {
								// JOHNSON WALES (CO) @
								// <a class="black" href="/college-basketball/teams/team-page.cfm/team/air-force">AIR FORCE</a>
								final String tdhtml = tdds.get(0).html();
								int index = tdhtml.indexOf("@");
								if (index != -1) {
									final String before = tdhtml.substring(0, index).trim();
									int bindex = before.indexOf("</a>");
									if (bindex != -1) {
										int bbindex = before.indexOf("\">");
										if (bbindex != -1) {
											awayteam.setTeam(parseBasketballTeams(before.substring(bbindex + 2, bindex).trim()));
										}
									} else {
										awayteam.setTeam(parseBasketballTeams(before.trim()));
									}

									final String after = tdhtml.substring(index + 1).trim();
									bindex = after.indexOf("</a>");
									if (bindex != -1) {
										int bbindex = after.indexOf("\">");
										if (bbindex != -1) {
											hometeam.setTeam(parseBasketballTeams(after.substring(bbindex + 2, bindex).trim()));
										}
									} else {
										hometeam.setTeam(parseBasketballTeams(after.trim()));
									}
								}
							}
						}
						break;
					case 1:
						final Elements gtrs = tr.select(".sportPicksBorder tbody tr");
						int z = 0;

						for (Element gtr : gtrs) {
							try {
								switch (z++) {
									case 0:
										if (!gtr.html().contains("Final Score")) {
											final Elements gtds = gtr.select("td");
											if (gtds != null && gtds.size() > 0) {
												//  8:00 PM Game Time 
												final Element gtd = gtds.get(0);
												gametime = gtd.html();
												int index = gametime.indexOf("Game Time");
												if (index != -1) {
													gametime = gametime.substring(0, index).trim();
												}
												LOGGER.debug("gametime: " + gametime);
											}
										} else {
											gametime = "11:00 AM";
										}
										break;
									case 2:
										final Elements gameinfoawaytds = gtr.select("td");
										parseTeamInfo(gameinfoawaytds, awayteam);
										break;
									case 3:
										final Elements gameinfohometds = gtr.select("td");
										parseTeamInfo(gameinfohometds, hometeam);
			
										
										// Set the teams now
										ep.setTeamone(awayteam);
										ep.setTeamtwo(hometeam);

										Date dateofgame = null;
										String ddate = "";

										try {
											// Thursday September 27, 2018 08:00 PM
											if (gamedate != null) {
												thedata = gametime;
												LOGGER.debug("gametime: " + gametime);
	
												if (gametime != null && gametime.length() > 0) {
													if (gametime.contains("PPD")) {
														throw new Exception("Postponed game");
													}
	
													gametime = gametime.replace("<span class=\"sub_title_red\">PPD</span>","");
													gametime = gametime.replace("<span class=\"sub_title_red\"></span>","");
													gametime = gametime.replace("<span class=\"sub_title_red\">","");
													if (gametime.contains(" - ")) {
														gametime = "12:00 PM";
													}
													
													if (gametime.length() > 0) {
														ddate = gamedate + " " + gametime + " " + timeZoneLookup("ET", offset);
														LOGGER.debug("ddate: " + ddate);
														dateofgame = DATE_FORMAT_BB.parse(ddate);
													} else {
														ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
														LOGGER.debug("ddate: " + ddate);
														dateofgame = DATE_FORMAT_BB.parse(ddate);												
													}
												}
											} else {
												ddate = gamedate + " " + "12:00 PM" + " " + timeZoneLookup("ET", offset);
												ddate = ddate.replace("<span class=\"sub_title_red\">PPD</span>","");
												ddate = ddate.replace("<span class=\"sub_title_red\"></span>","");
												ddate = ddate.replace("<span class=\"sub_title_red\">","");
												dateofgame = DATE_FORMAT_BB.parse(ddate);
												dateofgame = new Date();
											}
										} catch (ParseException pe) {
											LOGGER.debug("thedata: " + thedata);
											LOGGER.debug(pe.getMessage(), pe);
										}
			
										ep.setId(awayteam.getId());
										ep.setDateofevent(gamedate);
										ep.setTimeofevent(gametime);
										ep.setDateofevent(ddate);
										ep.setEventdatetime(dateofgame);
										awayteam.setDateofevent(gamedate);
										awayteam.setTimeofevent(gametime);
										hometeam.setDateofevent(gamedate);
										hometeam.setTimeofevent(gametime);
										awayteam.setEventdatetime(dateofgame);
										hometeam.setEventdatetime(dateofgame);
										LOGGER.debug("gameData.size(): " + gameData.size());
			
										if (gameData.size() == 0) {
											gameData.add(ep);
										} else {
											boolean found = false;
											for (int a = 0; a < gameData.size(); a++) {
												final EventPackage tempep = gameData.get(a);
												if (ep.getId() == tempep.getId()) {
													found = true;
												}
											}
			
											if (!found) {
												gameData.add(ep);
											}
										}
										break;
									default:
										break;
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

		LOGGER.info("Exiting getNcaabGameData()");
		return gameData;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public VegasInsiderLineMovement getLineMovement(String xhtml) throws BatchException {
		final VegasInsiderLineMovement vegasInsiderLineMovement = new VegasInsiderLineMovement();

		final Document doc = parseXhtml(xhtml);
		final Elements tables = doc.select(".SLTables1 table");

		if (tables != null && tables.size() > 0) {
			int x = 0;
			for (Element table : tables) {
				switch (x++) {
					case 0:
						parseLineTeams(table, vegasInsiderLineMovement);
						break;
					case 1:
						parseLineDateTimes(table, vegasInsiderLineMovement);
						break;
					default:
						parseLinePoints(table, vegasInsiderLineMovement);
						break;
				}
			}
		}

		return vegasInsiderLineMovement;
	}

	/**
	 * 
	 * @param table
	 * @param vegasInsiderLineMovement
	 */
	private void parseLinePoints(Element table, VegasInsiderLineMovement vegasInsiderLineMovement) {
		final Elements tables = table.select(".rt_railbox_border table");
		if (table.html() != null && 
			table.html().contains("VI CONSENSUS LINE MOVEMENTS") && 
			tables != null && 
			tables.size() > 1) {
			final Elements tds1 = tables.get(0).select("tbody tr td");
			String lineprovider = null;

			if (tds1 != null && tds1.size() > 0) {
				String name = tds1.get(0).html();
				int index = name.indexOf("</a>");
				if (index != -1) {
					lineprovider = name.substring(index + 4).trim();
					vegasInsiderLineMovement.setLineprovider(lineprovider);
				}
			}

			final Elements trs2 = tables.get(1).select("tbody tr");
			if (trs2 != null && trs2.size() > 0) {
				int count = 0;
				for (Element tr : trs2) {
					if (count++ > 1) {
						final VegasInsiderLinePoint vilp = new VegasInsiderLinePoint();
						final Elements tds = tr.select("td");
						int tdcount = 0;

						for (Element td : tds) {
							String tdhtml = td.html().replace("&nbsp;", "").replace("NJN", "BKN").trim();
							switch (tdcount++) {
								case 0:
									vilp.setDate(tdhtml);
									break;
								case 1:
									vilp.setTime(tdhtml);
									break;
								case 2:
									if (tdhtml != null && tdhtml.length() > 0) {
										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setMlfavteam(tdhtml.substring(0, findex).trim());	
												}
												vilp.setMlfav(fonts.get(0).html().trim());												
											} else {
												vilp.setMlfavteam(fonts.get(0).html().trim());
												vilp.setMlfav(fonts.get(1).html().trim());
											}
										} else {
											int index = tdhtml.indexOf("-");
											if (index != -1) {
												vilp.setMlfavteam(tdhtml.substring(0, index));
												vilp.setMlfav(tdhtml.substring(0, index));
											} else {
												index = tdhtml.indexOf("+");
												if (index != -1) {
													vilp.setMlfavteam(tdhtml.substring(0, index));
													vilp.setMlfav(tdhtml.substring(0, index));
												}
											}
										}
									} else {
										vilp.setMlfavteam("");
										vilp.setMlfav("");										
									}
									break;
								case 3:
									if (tdhtml != null && tdhtml.length() > 0) {
										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setMldogteam(tdhtml.substring(0, findex).trim());	
												}
												vilp.setMldog(fonts.get(0).html().trim());												
											} else {
												vilp.setMldogteam(fonts.get(0).html().trim());
												vilp.setMldog(fonts.get(1).html().trim());
											}
										} else {
											int index = tdhtml.indexOf("+");
											if (index != -1) {
												vilp.setMldogteam(tdhtml.substring(0, index));
												vilp.setMldog(tdhtml.substring(0, index));
											} else {
												index = tdhtml.indexOf("-");
												if (index != -1) {
													vilp.setMldogteam(tdhtml.substring(0, index));
													vilp.setMldog(tdhtml.substring(0, index));
												}
											}
										}
									} else {
										vilp.setMldogteam("");
										vilp.setMldog("");										
									}
									break;
								case 4:
									if (tdhtml != null && tdhtml.length() > 0) {
										tdhtml = tdhtml.replace("PK ", "-0 ");

										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setSpreadfavteam(tdhtml.substring(0, findex).replace("PK ", "-0 ").trim());	
												}
												String sfav = fonts.get(0).html().replace("PK ", "-0 ").trim();
												findex = sfav.indexOf(" ");
												if (findex != -1) {
													String sf = sfav.substring(0, findex).replace("PK ", "-0 ").trim();
													vilp.setSpreadfav(sf);													
												}
											} else {
												vilp.setSpreadfavteam(fonts.get(0).html().trim());
												String sp = fonts.get(1).html().replace("PK ", "-0 ").trim();
												int findex = sp.indexOf(" ");
												if (findex != -1) {
													String sf = sp.substring(0, findex).replace("PK ", "-0 ").trim();
													vilp.setSpreadfav(sf);
												} else {
													vilp.setSpreadfav(sp);
												}
											}
										} else {
											int index = tdhtml.indexOf("-");
											if (index != -1) {
												vilp.setSpreadfavteam(tdhtml.substring(0, index));
												tdhtml = tdhtml.substring(index);
												index = tdhtml.indexOf(" ");
												if (index != -1) {
													vilp.setSpreadfav(tdhtml.substring(0, index));
												} else {
													vilp.setSpreadfav(tdhtml);
												}
											} else {
												index = tdhtml.indexOf("+");
												if (index != -1) {
													vilp.setSpreadfavteam(tdhtml.substring(0, index));
													tdhtml = tdhtml.substring(index);
													index = tdhtml.indexOf(" ");
													if (index != -1) {
														vilp.setSpreadfav(tdhtml.substring(0, index));
													} else {
														vilp.setSpreadfav(tdhtml);
													}
												}
											}
										}
									} else {
										vilp.setSpreadfavteam("");
										vilp.setSpreadfav("");										
									}
									break;
								case 5:
									if (tdhtml != null && tdhtml.length() > 0) {
										tdhtml = tdhtml.replace("PK ", " +0");

										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													String sf = tdhtml.substring(0, findex).replace("PK ", " +0").trim();
													vilp.setSpreaddogteam(sf);	
												}
												String sfav = fonts.get(0).html().trim();
												findex = sfav.indexOf(" ");
												if (findex != -1) {
													String sf = sfav.substring(0, findex).replace("PK ", " +0").trim();
													vilp.setSpreaddog(sf);													
												}												
											} else {
												vilp.setSpreaddogteam(fonts.get(0).html().trim());
												String sp = fonts.get(1).html().replace("PK ", " +0").trim();
												int findex = sp.indexOf(" ");
												if (findex != -1) {
													vilp.setSpreaddog(sp.substring(0, findex));
												} else {
													vilp.setSpreaddog(sp);
												}
											}
										} else {
											int index = tdhtml.indexOf("+");
											if (index != -1) {
												vilp.setSpreaddogteam(tdhtml.substring(0, index));
												tdhtml = tdhtml.substring(index);
												index = tdhtml.indexOf(" ");
												if (index != -1) {
													vilp.setSpreaddog(tdhtml.substring(0, index));
												} else {
													vilp.setSpreaddog(tdhtml);
												}
											} else {
												index = tdhtml.indexOf("-");
												if (index != -1) {
													vilp.setSpreaddogteam(tdhtml.substring(0, index));
													tdhtml = tdhtml.substring(index);
													index = tdhtml.indexOf(" ");
													if (index != -1) {
														vilp.setSpreaddog(tdhtml.substring(0, index));
													} else {
														vilp.setSpreaddog(tdhtml);
													}
												}
											}
										}
									} else {
										vilp.setSpreaddogteam("");
										vilp.setSpreaddog("");										
									}
									break;
								case 6:
									if (tdhtml != null && tdhtml.length() > 0) {
										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setTotalfavteam(tdhtml.substring(0, findex).trim());	
												}
												vilp.setTotalfav(fonts.get(0).html().trim());												
											} else {
												vilp.setTotalfavteam(fonts.get(1).html().trim());
												vilp.setTotalfav(fonts.get(0).html().trim());
											}
										} else {
											int index = tdhtml.indexOf(" ");
											if (index != -1) {
												vilp.setTotalfav(tdhtml.substring(0, index));
												vilp.setTotalfavteam(tdhtml.substring(index + 1));
											} else {
												vilp.setTotalfav(tdhtml);
											}
										}
									} else {
										vilp.setTotalfavteam("");
										vilp.setTotalfav("");										
									}
									break;
								case 7:
									if (tdhtml != null && tdhtml.length() > 0) {
										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setTotaldogteam(tdhtml.substring(0, findex).trim());	
												}
												vilp.setTotaldog(fonts.get(0).html().trim());												
											} else {
												vilp.setTotaldogteam(fonts.get(0).html().trim());
												vilp.setTotaldog(fonts.get(1).html().trim());
											}
										} else {
											int index = tdhtml.indexOf(" ");
											if (index != -1) {
												vilp.setTotaldog(tdhtml.substring(0, index));
												vilp.setTotaldogteam(tdhtml.substring(index + 1));
											} else {
												vilp.setTotaldog(tdhtml);
											}
										}
									} else {
										vilp.setTotaldogteam("");
										vilp.setTotaldog("");										
									}
									break;
								case 8:
									if (tdhtml != null && tdhtml.length() > 0) {
										tdhtml = tdhtml.replace("PK ", " -0");

										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setSpread1hfavteam(tdhtml.substring(0, findex).trim());	
												}
												String sfav = fonts.get(0).html().trim();
												findex = sfav.indexOf(" ");
												if (findex != -1) {
													vilp.setSpread1hfav(sfav.substring(0, findex).trim());													
												}												
											} else {
												vilp.setSpread1hfavteam(fonts.get(0).html().trim());
												vilp.setSpread1hfav(fonts.get(1).html().trim());
											}
										} else {
											int index = tdhtml.indexOf("-");
											if (index != -1) {
												vilp.setSpread1hfavteam(tdhtml.substring(0, index));
												tdhtml = tdhtml.substring(index);
												index = tdhtml.indexOf(" ");
												if (index != -1) {
													vilp.setSpread1hfav(tdhtml.substring(0, index));
												} else {
													vilp.setSpread1hfav(tdhtml);
												}
											} else {
												index = tdhtml.indexOf("+");
												if (index != -1) {
													vilp.setSpread1hfavteam(tdhtml.substring(0, index));
													tdhtml = tdhtml.substring(index);
													index = tdhtml.indexOf(" ");
													if (index != -1) {
														vilp.setSpread1hfav(tdhtml.substring(0, index));
													} else {
														vilp.setSpread1hfav(tdhtml);
													}
												}
											}
										}
									} else {
										vilp.setSpread1hfavteam("");
										vilp.setSpread1hfav("");										
									}
									break;
								case 9:
									if (tdhtml != null && tdhtml.length() > 0) {
										tdhtml = tdhtml.replace("PK ", " +0");

										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setSpread1hdogteam(tdhtml.substring(0, findex).trim());	
												}
												String sfav = fonts.get(0).html().trim();
												findex = sfav.indexOf(" ");
												if (findex != -1) {
													vilp.setSpread1hdog(sfav.substring(0, findex).trim());													
												}												
											} else {
												vilp.setSpread1hdogteam(fonts.get(0).html().trim());
												vilp.setSpread1hdog(fonts.get(1).html().trim());
											}
										} else {
											int index = tdhtml.indexOf("+");
											if (index != -1) {
												vilp.setSpread1hdogteam(tdhtml.substring(0, index));
												tdhtml = tdhtml.substring(index);
												index = tdhtml.indexOf(" ");
												if (index != -1) {
													vilp.setSpread1hdog(tdhtml.substring(0, index));
												} else {
													vilp.setSpread1hdog(tdhtml);
												}
											} else {
												index = tdhtml.indexOf("-");
												if (index != -1) {
													vilp.setSpread1hdogteam(tdhtml.substring(0, index));
													tdhtml = tdhtml.substring(index);
													index = tdhtml.indexOf(" ");
													if (index != -1) {
														vilp.setSpread1hdog(tdhtml.substring(0, index));
													} else {
														vilp.setSpread1hdog(tdhtml);
													}
												}
											}
										}
									} else {
										vilp.setSpread1hdogteam("");
										vilp.setSpread1hdog("");										
									}
									break;
								case 10:
									if (tdhtml != null && tdhtml.length() > 0) {
										tdhtml = tdhtml.replace("PK ", " -0");

										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setSpread2hfavteam(tdhtml.substring(0, findex).trim());	
												}
												String sfav = fonts.get(0).html().trim();
												findex = sfav.indexOf(" ");
												if (findex != -1) {
													vilp.setSpread2hfav(sfav.substring(0, findex).trim());													
												}												
											} else {
												vilp.setSpread2hfavteam(fonts.get(0).html().trim());
												vilp.setSpread2hfav(fonts.get(1).html().trim());
											}
										} else {
											int index = tdhtml.indexOf("-");
											if (index != -1) {
												vilp.setSpread2hfavteam(tdhtml.substring(0, index));
												tdhtml = tdhtml.substring(index);
												index = tdhtml.indexOf(" ");
												if (index != -1) {
													vilp.setSpread2hfav(tdhtml.substring(0, index));
												} else {
													vilp.setSpread2hfav(tdhtml);
												}
											} else {
												index = tdhtml.indexOf("+");
												if (index != -1) {
													vilp.setSpread2hfavteam(tdhtml.substring(0, index));
													tdhtml = tdhtml.substring(index);
													index = tdhtml.indexOf(" ");
													if (index != -1) {
														vilp.setSpread2hfav(tdhtml.substring(0, index));
													} else {
														vilp.setSpread2hfav(tdhtml);
													}
												}
											}
										}
									} else {
										vilp.setSpread2hfavteam("");
										vilp.setSpread2hfav("");										
									}
									break;
								case 11:
									if (tdhtml != null && tdhtml.length() > 0) {
										tdhtml = tdhtml.replace("PK ", " +0");

										if (tdhtml.contains("font")) {
											final Elements fonts = td.select("font");
											if (fonts != null && fonts.size() == 1) {
												int findex = tdhtml.indexOf("<font");
												if (findex != -1) {
													vilp.setSpread2hdogteam(tdhtml.substring(0, findex).trim());	
												}
												String sfav = fonts.get(0).html().trim();
												findex = sfav.indexOf(" ");
												if (findex != -1) {
													vilp.setSpread2hdog(sfav.substring(0, findex).trim());													
												}												
											} else {
												vilp.setSpread2hdogteam(fonts.get(0).html().trim());
												vilp.setSpread2hdog(fonts.get(1).html().trim());
											}
										} else {
											int index = tdhtml.indexOf("+");
											if (index != -1) {
												vilp.setSpread2hdogteam(tdhtml.substring(0, index));
												tdhtml = tdhtml.substring(index);
												index = tdhtml.indexOf(" ");
												if (index != -1) {
													vilp.setSpread2hdog(tdhtml.substring(0, index));
												} else {
													vilp.setSpread2hdog(tdhtml);
												}
											} else {
												index = tdhtml.indexOf("-");
												if (index != -1) {
													vilp.setSpread2hdogteam(tdhtml.substring(0, index));
													tdhtml = tdhtml.substring(index);
													index = tdhtml.indexOf(" ");
													if (index != -1) {
														vilp.setSpread2hdog(tdhtml.substring(0, index));
													} else {
														vilp.setSpread2hdog(tdhtml);
													}
												}
											}
										}
									} else {
										vilp.setSpread2hdogteam("");
										vilp.setSpread2hdog("");										
									}

									vilp.setLineprovider(lineprovider);
									vegasInsiderLineMovement.addLinepoint(vilp);
									break;
								default:
									break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param table
	 * @param vegasInsiderLineMovement
	 */
	private void parseLineDateTimes(Element table, VegasInsiderLineMovement vegasInsiderLineMovement) {
		final Elements fonts = table.select("tr td");
		if (fonts != null && fonts.size() > 1) {
			String date = fonts.get(0).html();
			int index = date.indexOf("</b>");
			if (index != -1) {
				vegasInsiderLineMovement.setGamedate(date.substring(index + 4).replace("&nbsp;", "").trim());
			}

			String time = fonts.get(1).html();
			index = date.indexOf("</b>");
			if (index != -1) {
				vegasInsiderLineMovement.setGametime(time.substring(index + 4).replace("&nbsp;", "").trim());
			}
		}
	}

	/**
	 * 
	 * @param table
	 * @param vegasInsiderLineMovement
	 */
	private void parseLineTeams(Element table, VegasInsiderLineMovement vegasInsiderLineMovement) {
		final Elements fonts = table.select("tr td font");
		if (fonts != null && fonts.size() > 0) {
			String teams = fonts.get(0).html();
			
			int index = teams.indexOf("@");
			if (index != -1) {
				vegasInsiderLineMovement.setAwayteam(teams.substring(0, index).replace("&nbsp;", "").trim().toUpperCase());
				vegasInsiderLineMovement.setHometeam(teams.substring(index + 1).replace("&nbsp;", "").trim().toUpperCase());
			}
		}
	}

	/**
	 * 
	 * @param gametds
	 * @param awayteam
	 * @param hometeam
	 */
	protected void parseFCSTeams(Elements gametds, VegasInsiderGameData awayteam, VegasInsiderGameData hometeam) {
		if (gametds != null && gametds.size() > 1) {
			final Element td = gametds.get(1);
			final Elements as = td.select("a");
			
			if (as != null && as.size() > 1) {
				final String away = as.get(0).html().trim().toUpperCase();
				final String home = as.get(1).html().trim().toUpperCase();

				Iterator<String> itr = NCAAFMapping.keySet().iterator();
				while (itr.hasNext()) {
					final String key = itr.next();
					if (key.toUpperCase().equals(away)) {
						awayteam.setIsfbs(true);
					}
				}
				awayteam.setTeamname(away);

				itr = NCAAFMapping.keySet().iterator();
				while (itr.hasNext()) {
					final String key = itr.next();
					if (key.toUpperCase().equals(home)) {
						hometeam.setIsfbs(true);
					}
				}

				hometeam.setTeamname(home);
			}
		}
	}

	/**
	 * 
	 * @param gametds
	 * @param awayteam
	 * @param hometeam
	 */
	protected void parseTeams(Elements gametds, VegasInsiderGameData awayteam, VegasInsiderGameData hometeam) {
		if (gametds != null && gametds.size() > 1) {
			final Element td = gametds.get(1);
			final Elements as = td.select("a");
			
			if (as != null && as.size() > 1) {
				final String away = as.get(0).html().trim().toUpperCase();
				final String home = as.get(1).html().trim().toUpperCase();

				boolean found = false;
				String keyName = null;
				Iterator<String> itr = NCAAFMapping.keySet().iterator();
				while (itr.hasNext()) {
					final String key = itr.next();
					if (key.toUpperCase().equals(away)) {
						keyName = NCAAFMapping.get(key);
						awayteam.setTeamname(keyName.toUpperCase());
						awayteam.setIsfbs(true);
						found = true;
					}
				}

				if (!found) {
					itr = NCAAFFCSMapping.keySet().iterator();
					while (itr.hasNext()) {
						final String key = itr.next();
						if (key.toUpperCase().equals(away)) {
							keyName = NCAAFFCSMapping.get(key);
							awayteam.setTeamname(keyName.toUpperCase());
							awayteam.setIsfbs(false);
							found = true;
						}
					}
				}
				if (!found) {
					awayteam.setTeamname(away);
				}

				found = false;
				itr = NCAAFMapping.keySet().iterator();
				while (itr.hasNext()) {
					final String key = itr.next();
					if (key.toUpperCase().equals(home)) {
						keyName = NCAAFMapping.get(key);
						hometeam.setTeamname(keyName.toUpperCase());
						hometeam.setIsfbs(true);
						found = true;
					}
				}
				if (!found) {
					itr = NCAAFFCSMapping.keySet().iterator();
					while (itr.hasNext()) {
						final String key = itr.next();
						if (key.toUpperCase().equals(home)) {
							keyName = NCAAFFCSMapping.get(key);
							hometeam.setTeamname(keyName.toUpperCase());
							hometeam.setIsfbs(false);
							found = true;
						}
					}
				}
				if (!found) {
					hometeam.setTeamname(home);
				}
			}
		}
	}

	/**
	 * 
	 * @param team
	 * @return
	 */
	protected String parseBasketballTeams(String team) {
		String teamName = team.toUpperCase();
		Iterator<String> itr = NCAABMapping.keySet().iterator();
		while (itr.hasNext()) {
			final String key = itr.next();
			if (key.toUpperCase().equals(team.toUpperCase())) {
				teamName = NCAABMapping.get(key).toUpperCase();
			}
		}

		return teamName;
	}

	/**
	 * 
	 * @param team
	 * @return
	 */
	protected String parseKPBasketballTeams(String team) {
		String teamName = team.toUpperCase();
		Iterator<String> itr = NCAABKPMapping.keySet().iterator();
		while (itr.hasNext()) {
			final String key = itr.next();
			if (key.toUpperCase().equals(team.toUpperCase())) {
				teamName = NCAABKPMapping.get(key).toUpperCase();
			}
		}

		return teamName;
	}

	/**
	 * 
	 * @param gametds
	 * @param team
	 */
	protected void parseTeamInfo(Elements gametds, VegasInsiderGameData team) {
		int gametdscount = 0;
		LOGGER.debug("gametds.size(): " + gametds.size());

		if (gametds.size() == 9 || gametds.size() == 10) {
			for (Element gametd : gametds) {
				switch (gametdscount++) {
					case 1: {
						String rotId = gametd.html();

						final Elements as = gametd.select("a");
						if (as != null && as.size() > 0) {
							team.setShortname(as.get(0).html().trim());
						}

						int index = rotId.indexOf("<b>");
						if (index != -1) {
							if (index != -1) {
								rotId = rotId.substring(0, index).replace("&nbsp;", "").trim();
								if (rotId != null && rotId.length() > 0) {
									if (rotId.contains("<a")) {
										int p = rotId.indexOf("<a");
										if (p != -1) {
											try {
												team.setRotationid(Integer.parseInt(rotId.substring(0, p).trim()));
											} catch (Throwable t) {
												
											}
										}
									} else {
										try {
											team.setRotationid(Integer.parseInt(rotId.trim()));
										} catch (Throwable t) {
											
										}
									}
								}
							}							
						} else {
							index = rotId.indexOf("<a class");
							if (index != -1) {
								rotId = rotId.substring(0, index).replace("&nbsp;", "").trim();
								if (rotId != null && rotId.length() > 0) {
									try {
										team.setRotationid(Integer.parseInt(rotId.trim()));
									} catch (Throwable t) {
										
									}
								}
							}
						}
						break;
					}
					case 2: {
						final String oddtotal = gametd.html().replace("&nbsp;", "").trim();
						if (oddtotal != null && oddtotal.startsWith("-")) {
							team.setLine(Float.parseFloat(oddtotal));
							team.setLineindicator("-");
							team.setLinevalue(Float.parseFloat(oddtotal.substring(1)));
							team.setLinefavorite(team.getTeamname());
						} else if (oddtotal != null && oddtotal.length() > 0) {
							if (oddtotal.equals("PK")) {
								team.setLine(Float.valueOf("0"));
								team.setLineindicator("+");
								team.setLinevalue(Float.valueOf("0"));
								team.setLinefavorite(team.getTeamname());
							} else {
								team.setTotal(Float.parseFloat(oddtotal));
							}
						}
						break;
					}
					case 3: {
						final String score = gametd.html().replace("&nbsp;", "").trim();
						if (score != null && score.length() > 0) {
							team.setFirstquarterscore(Integer.parseInt(score));
						}
						break;
					}
					case 4: {
						final String score = gametd.html().replace("&nbsp;", "").trim();
						if (score != null && score.length() > 0) {
							team.setSecondquarterscore(Integer.parseInt(score));
							team.setFirsthalfscore(team.getFirstquarterscore() + team.getSecondquarterscore());
						}
						break;
					}
					case 5: {
						final String score = gametd.html().replace("&nbsp;", "").trim();
						if (score != null && score.length() > 0) {
							team.setThirdquarterscore(Integer.parseInt(score));
						}
						break;
					}
					case 6: {
						final String score = gametd.html().replace("&nbsp;", "").trim();
						if (score != null && score.length() > 0) {
							team.setFourthquarterscore(Integer.parseInt(score));
							team.setSecondhalfscore(team.getThirdquarterscore() + team.getFourthquarterscore());
						}
						break;
					}
					case 7: {
						if (gametds.size() == 9) {
							final Elements bs = gametd.select("font b");
							if (bs != null && bs.size() > 0) {
								String bsstring = bs.get(0).html().replace("&nbsp;", "").trim();
								if (bsstring != null && bsstring.length() > 0) {
									team.setFinalscore(Integer.parseInt(bsstring));
								}
							}
						} else {
							final String ot = gametd.html().replace("&nbsp;", "").trim();
							if (ot != null && ot.length() > 0) {
								team.setOtscore(Integer.parseInt(ot));
								team.setSecondhalfscore(team.getSecondhalfscore() + team.getOtscore());
							}
						}
						break;
					}
					case 8: {
						if (gametds.size() == 9) {
							String cover = gametd.html().replace("&nbsp;", "").trim();
							int index = cover.indexOf("Cover: ");
							if (index != -1) {
								cover = cover.substring(index + "Cover: ".length()).trim();
								team.setCoverspreadamount(Float.parseFloat(cover));
							} else {
								index = cover.indexOf("Over: ");
								if (index != -1) {
									cover = cover.substring(index + "Over: ".length()).trim();
									team.setCovertotalamount(Float.parseFloat(cover));
								}
							}
						} else {
							final Elements bs = gametd.select("font b");
							if (bs != null && bs.size() > 0) {
								team.setFinalscore(Integer.parseInt(bs.get(0).html().replace("&nbsp;", "").trim()));
							}
						}
						break;
					}
					case 9: {
						String cover = gametd.html().trim();
						int index = cover.indexOf("Cover: ");
						if (index != -1) {
							cover = cover.substring(index + "Cover: ".length()).trim().replace("&nbsp;", "");
							team.setCoverspreadamount(Float.parseFloat(cover));
						} else {
							index = cover.indexOf("Over: ");
							if (index != -1) {
								cover = cover.substring(index + "Over: ".length()).trim().replace("&nbsp;", "");
								team.setCovertotalamount(Float.parseFloat(cover));
							}
						}
						break;
					}				
					default:
						break;
				}
			}
		} else if (gametds.size() == 6 || gametds.size() == 7) {
			boolean isnotplayed = false;
			for (Element gametd : gametds) {
				switch (gametdscount++) {
					case 0: {
						String rotId = gametd.html();

						final Elements as = gametd.select("a");
						if (as != null && as.size() > 0) {
							team.setShortname(as.get(0).html().trim());
						}

						int index = rotId.indexOf("<a class");
						if (index != -1) {
							rotId = rotId.substring(0, index).replace("&nbsp;", "").trim();
							if (rotId != null && rotId.length() > 0) {
								team.setRotationid(Integer.parseInt(rotId));
							}
						}
						break;
					}
					case 1: {
						final String oddtotal = gametd.html().replace("&nbsp;", "").trim();
						if (oddtotal != null && oddtotal.startsWith("-")) {
							team.setLine(Float.parseFloat(oddtotal));
							team.setLineindicator("-");
							team.setLinevalue(Float.parseFloat(oddtotal.substring(1)));
							team.setLinefavorite(team.getTeamname());
						} else if (oddtotal != null && oddtotal.length() > 0) {
							if (oddtotal.equals("PK")) {
								team.setLine(Float.valueOf("0"));
								team.setLineindicator("+");
								team.setLinevalue(Float.valueOf("0"));
								team.setLinefavorite(team.getTeamname());
							} else {
								team.setTotal(Float.parseFloat(oddtotal));
							}
						}
						break;
					}
					case 2: {
						final String score = gametd.html().replace("&nbsp;", "").trim();
						if (score != null && score.length() > 0) {
							if (score.contains("(")) {
								isnotplayed = true;
							} else {
								team.setFirsthalfscore(Integer.parseInt(score));
							}
						}
						break;
					}
					case 3: {
						final String score = gametd.html().replace("&nbsp;", "").trim();
						if (score != null && score.length() > 0) {
							if (!isnotplayed) {
								team.setSecondhalfscore(Integer.parseInt(score));
							}
						}
						break;
					}
					case 4: {
						if (gametds.size() == 6) {
							final Elements bs = gametd.select("font b");
							if (bs != null && bs.size() > 0) {
								String bsstring = bs.get(0).html().replace("&nbsp;", "").trim();
								if (bsstring != null && bsstring.length() > 0) {
									if (!isnotplayed) {
										team.setFinalscore(Integer.parseInt(bsstring));
									}
								}
							} else {
								String bsstring = gametd.html().replace("&nbsp;", "").trim();
								if (bsstring != null && bsstring.length() > 0) {
									if (!isnotplayed) {
										team.setFinalscore(Integer.parseInt(bsstring));
									}
								}								
							}
						} else {
							final String ot = gametd.html().replace("&nbsp;", "").trim();
							if (ot != null && ot.length() > 0) {
								team.setOtscore(Integer.parseInt(ot));
								team.setSecondhalfscore(team.getSecondhalfscore() + team.getOtscore());
							}
						}
						break;
					}
					case 5: {
						if (gametds.size() == 6) {
							String cover = gametd.html().replace("&nbsp;", "").trim();
							if (!isnotplayed) {
								int index = cover.indexOf("Cover: ");
								if (index != -1) {
									cover = cover.substring(index + "Cover: ".length()).replace("&nbsp;", "").trim();
									team.setCoverspreadamount(Float.parseFloat(cover));
								} else {
									index = cover.indexOf("Over: ");
									if (index != -1) {
										cover = cover.substring(index + "Over: ".length()).replace("&nbsp;", "").trim();
										team.setCovertotalamount(Float.parseFloat(cover));
									}
								}
							}
						} else {
							final Elements bs = gametd.select("font b");
							if (bs != null && bs.size() > 0) {
								if (!isnotplayed) {
									team.setFinalscore(Integer.parseInt(bs.get(0).html().replace("&nbsp;", "").trim()));
								}
							} else {
								String bsstring = gametd.html().replace("&nbsp;", "").trim();
								if (bsstring != null && bsstring.length() > 0) {
									if (!isnotplayed) {
										team.setFinalscore(Integer.parseInt(bsstring));
									}
								}
							}
						}
						break;
					}
					case 6: {
						String cover = gametd.html().trim();
						int index = cover.indexOf("Cover: ");
						if (index != -1) {
							if (!isnotplayed) {
								cover = cover.substring(index + "Cover: ".length()).replace("&nbsp;", "").trim();
								team.setCoverspreadamount(Float.parseFloat(cover));
							}
						} else {
							index = cover.indexOf("Over: ");
							if (index != -1) {
								if (!isnotplayed) {
									cover = cover.substring(index + "Over: ".length()).replace("&nbsp;", "").trim();
									team.setCovertotalamount(Float.parseFloat(cover));
								}
							}
						}
						break;
					}
					default:
						break;
				}
			}
		} else if (gametds.size() == 4) {
			for (Element gametd : gametds) {
				switch (gametdscount++) {
					case 1: {
						String rotId = gametd.html();

						final Elements as = gametd.select("a");
						if (as != null && as.size() > 0) {
							team.setShortname(as.get(0).html().trim());
						}

						int index = rotId.indexOf("<a class");
						if (index != -1) {
							rotId = rotId.substring(0, index).replace("&nbsp;", "").trim();
							if (rotId != null && rotId.length() > 0) {
								team.setRotationid(Integer.parseInt(rotId));
							}
						}
						break;
					}
					case 2: {
						final String oddtotal = gametd.html().replace("&nbsp;", "").trim();
						if (oddtotal != null && oddtotal.startsWith("-")) {
							team.setLine(Float.parseFloat(oddtotal));
							team.setLineindicator("-");
							team.setLinevalue(Float.parseFloat(oddtotal.substring(1)));
							team.setLinefavorite(team.getTeamname());
						} else if (oddtotal != null && oddtotal.length() > 0) {
							if (oddtotal.equals("PK")) {
								team.setLine(Float.valueOf("0"));
								team.setLineindicator("+");
								team.setLinevalue(Float.valueOf("0"));
								team.setLinefavorite(team.getTeamname());
							} else {
								team.setTotal(Float.parseFloat(oddtotal));
							}
						}
						break;
					}
					default:
						break;
				}
			}
		} else {
			boolean isnotplayed = false;
			for (Element gametd : gametds) {
				switch (gametdscount++) {
					case 0: {
						String rotId = gametd.html();
	
						final Elements as = gametd.select("a");
						if (as != null && as.size() > 0) {
							team.setShortname(as.get(0).html().trim());
						}
	
						int index = rotId.indexOf("<a class");
						if (index != -1) {
							rotId = rotId.substring(0, index).replace("&nbsp;", "").trim();
							if (rotId != null && rotId.length() > 0) {
								team.setRotationid(Integer.parseInt(rotId));
							}
						}
						break;
					}
					case 1: {
						final String oddtotal = gametd.html().replace("&nbsp;", "").trim();
						if (oddtotal != null && oddtotal.startsWith("-")) {
							team.setLine(Float.parseFloat(oddtotal));
							team.setLineindicator("-");
							team.setLinevalue(Float.parseFloat(oddtotal.substring(1)));
							team.setLinefavorite(team.getTeamname());
						} else if (oddtotal != null && oddtotal.length() > 0) {
							if (oddtotal.equals("PK")) {
								team.setLine(Float.valueOf("0"));
								team.setLineindicator("+");
								team.setLinevalue(Float.valueOf("0"));
								team.setLinefavorite(team.getTeamname());
							} else {
								try {
									team.setTotal(Float.parseFloat(oddtotal));
								} catch (Throwable t) {
									
								}
							}
						}
						break;
					}
					case 2: {
						final String score = gametd.html().replace("&nbsp;", "").trim();
						if (score != null && score.length() > 0) {
							if (score.contains("(")) {
								isnotplayed = true;
							} else {
								team.setFirsthalfscore(Integer.parseInt(score));
							}
						}
						break;
					}
					case 3: {
						final String score = gametd.html().replace("&nbsp;", "").trim();
						if (score != null && score.length() > 0) {
							if (!isnotplayed) {
								team.setSecondhalfscore(Integer.parseInt(score));
							}
						}
						break;
					}
					case 4: {
						if (gametds.size() == 6) {
							final Elements bs = gametd.select("font b");
							if (bs != null && bs.size() > 0) {
								String bsstring = bs.get(0).html().replace("&nbsp;", "").trim();
								if (bsstring != null && bsstring.length() > 0) {
									if (!isnotplayed) {
										team.setFinalscore(Integer.parseInt(bsstring));
									}
								}
							} else {
								String bsstring = gametd.html().replace("&nbsp;", "").trim();
								if (bsstring != null && bsstring.length() > 0) {
									if (!isnotplayed) {
										team.setFinalscore(Integer.parseInt(bsstring));
									}
								}								
							}
						} else {
							final String ot = gametd.html().replace("&nbsp;", "").trim();
							if (ot != null && ot.length() > 0) {
								team.setOtscore(Integer.parseInt(ot));
								team.setSecondhalfscore(team.getSecondhalfscore() + team.getOtscore());
							}
						}
						break;
					}
					case 5: {
						if (gametds.size() == 6) {
							String cover = gametd.html().replace("&nbsp;", "").trim();
							if (!isnotplayed) {
								int index = cover.indexOf("Cover: ");
								if (index != -1) {
									cover = cover.substring(index + "Cover: ".length()).replace("&nbsp;", "").trim();
									team.setCoverspreadamount(Float.parseFloat(cover));
								} else {
									index = cover.indexOf("Over: ");
									if (index != -1) {
										cover = cover.substring(index + "Over: ".length()).replace("&nbsp;", "").trim();
										team.setCovertotalamount(Float.parseFloat(cover));
									}
								}
							}
						} else {
							final Elements bs = gametd.select("font b");
							if (bs != null && bs.size() > 0) {
								if (!isnotplayed) {
									team.setFinalscore(Integer.parseInt(bs.get(0).html().replace("&nbsp;", "").trim()));
								}
							} else {
								String bsstring = gametd.html().replace("&nbsp;", "").trim();
								if (bsstring != null && bsstring.length() > 0) {
									if (!isnotplayed) {
										team.setFinalscore(Integer.parseInt(bsstring));
									}
								}
							}
						}
						break;
					}
					case 6: {
						String cover = gametd.html().trim();
						int index = cover.indexOf("Cover: ");
						if (index != -1) {
							if (!isnotplayed) {
								cover = cover.substring(index + "Cover: ".length()).replace("&nbsp;", "").trim();
								team.setCoverspreadamount(Float.parseFloat(cover));
							}
						} else {
							index = cover.indexOf("Over: ");
							if (index != -1) {
								if (!isnotplayed) {
									cover = cover.substring(index + "Over: ".length()).replace("&nbsp;", "").trim();
									team.setCovertotalamount(Float.parseFloat(cover));
								}
							}
						}
						break;
					}
					default:
						break;
				}
			}
		}
	}

	/**
	 * 
	 * @param gametds
	 * @param team
	 */
	protected void parseTeamInfo(Elements gametds, TeamPackage team) {
		int gametdscount = 0;
		LOGGER.debug("gametds.size(): " + gametds.size());

		if (gametds.size() == 9 || gametds.size() == 10) {
			for (Element gametd : gametds) {
				switch (gametdscount++) {
					case 1: {
						String rotId = gametd.html();

						int index = rotId.indexOf("<a class");
						if (index != -1) {
							rotId = rotId.substring(0, index).replace("&nbsp;", "").trim();
							if (rotId != null && rotId.length() > 0) {
								team.setId(Integer.parseInt(rotId));
							}
						}
						break;
					}		
					default:
						break;
				}
			}
		} else if (gametds.size() == 6 || gametds.size() == 7) {
			boolean isnotplayed = false;
			for (Element gametd : gametds) {
				switch (gametdscount++) {
					case 0: {
						String rotId = gametd.html();
						int index = rotId.indexOf("<a class");
						if (index != -1) {
							rotId = rotId.substring(0, index).replace("&nbsp;", "").trim();
							if (rotId != null && rotId.length() > 0) {
								team.setId(Integer.parseInt(rotId));
							}
						}
						break;
					}
					default:
						break;
				}
			}
		} else if (gametds.size() == 4) {
			for (Element gametd : gametds) {
				switch (gametdscount++) {
					case 1: {
						String rotId = gametd.html();
						int index = rotId.indexOf("<a class");
						if (index != -1) {
							rotId = rotId.substring(0, index).replace("&nbsp;", "").trim();
							if (rotId != null && rotId.length() > 0) {
								team.setId(Integer.parseInt(rotId));
							}
						}
						break;
					}
					default:
						break;
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

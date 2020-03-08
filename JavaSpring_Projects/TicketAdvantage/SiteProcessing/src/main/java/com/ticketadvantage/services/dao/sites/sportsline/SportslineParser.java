/**
 * 
 */
package com.ticketadvantage.services.dao.sites.sportsline;

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

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class SportslineParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(SportslineParser.class);
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
		
		NCAABMapping.put("Abl Christian", "Abilene Christian");
		NCAABMapping.put("Air Force", "Air Force");
		NCAABMapping.put("Akron","Akron");
		NCAABMapping.put("Alab A&amp;M","Alabama A&M");
		NCAABMapping.put("Alabama","Alabama");
		NCAABMapping.put("Alabama St","Alabama State");
		NCAABMapping.put("Albany","Albany");
		NCAABMapping.put("Alcorn State","Alcorn State");
		NCAABMapping.put("American","American University");
		NCAABMapping.put("App State","Appalachian State");
		NCAABMapping.put("AR Lit Rock","Arkansas Little Rock");
		NCAABMapping.put("Arizona","Arizona");
		NCAABMapping.put("Arizona St","Arizona State");
		NCAABMapping.put("Ark Pine Bl","Arkansas Pine Bluff");
		NCAABMapping.put("Arkansas","Arkansas");
		NCAABMapping.put("Arkansas St","Arkansas State");
		NCAABMapping.put("Army","Army");
		NCAABMapping.put("Auburn","Auburn");
		NCAABMapping.put("Austin Peay","Austin Peay");
		NCAABMapping.put("Ball State","Ball State");
		NCAABMapping.put("Baylor","Baylor");
		NCAABMapping.put("Belmont","Belmont");
		NCAABMapping.put("Beth-Cook","Bethune-Cookman");
		NCAABMapping.put("Binghamton","Binghamton");
		NCAABMapping.put("Boise State","Boise State");
		NCAABMapping.put("Boston Col","Boston College");
		NCAABMapping.put("Boston U","Boston University");
		NCAABMapping.put("Bowling Grn","Bowling Green");
		NCAABMapping.put("Bradley","Bradley");
		NCAABMapping.put("Brown","Brown");
		NCAABMapping.put("Bryant","Bryant");
		NCAABMapping.put("Bucknell","Bucknell");
		NCAABMapping.put("Buffalo","Buffalo");
		NCAABMapping.put("Butler","Butler");
		NCAABMapping.put("BYU","BYU");
		NCAABMapping.put("Cal Poly","Cal Poly-SLO");
		NCAABMapping.put("Cal St Nrdge","CS Northridge");
		NCAABMapping.put("California","California");
		NCAABMapping.put("Campbell","Campbell");
		NCAABMapping.put("Canisius","Canisius");
		NCAABMapping.put("Central Ark","Central Arkansas");
		NCAABMapping.put("Central Conn","Central Connecticut State");
		NCAABMapping.put("Central FL","Central Florida");
		NCAABMapping.put("Central Mich","Central Michigan");
		NCAABMapping.put("Charl South","Charleston Southern");
		NCAABMapping.put("Charlotte","Charlotte");
		NCAABMapping.put("Chattanooga","Chattanooga");
		NCAABMapping.put("Chicago St","Chicago State");
		NCAABMapping.put("Cincinnati","Cincinnati");
		NCAABMapping.put("Citadel","The Citadel");
		NCAABMapping.put("Clemson","Clemson");
		NCAABMapping.put("Cleveland St","Cleveland State");
		NCAABMapping.put("Coastal Car","Coastal Carolina");
		NCAABMapping.put("Col Charlestn","College of Charleston");
		NCAABMapping.put("Colgate","Colgate");
		NCAABMapping.put("Colorado","Colorado");
		NCAABMapping.put("Colorado St","Colorado State");
		NCAABMapping.put("Columbia","Columbia");
		NCAABMapping.put("Connecticut","Connecticut");
		NCAABMapping.put("Coppin State","Coppin State");
		NCAABMapping.put("Cornell","Cornell");
		NCAABMapping.put("Creighton","Creighton");
		NCAABMapping.put("CS Bakersfld","CS Bakersfield");
		NCAABMapping.put("CS Fullerton","CS Fullerton");
		NCAABMapping.put("Dartmouth","Dartmouth");
		NCAABMapping.put("Davidson","Davidson");
		NCAABMapping.put("Dayton","Dayton");
		NCAABMapping.put("Delaware","Delaware");
		NCAABMapping.put("Delaware St","Delaware State");
		NCAABMapping.put("Denver","Denver");
		NCAABMapping.put("DePaul","DePaul");
		NCAABMapping.put("Detroit","Detroit Mercy");
		NCAABMapping.put("Drake","Drake");
		NCAABMapping.put("Drexel","Drexel");
		NCAABMapping.put("Duke","Duke");
		NCAABMapping.put("Duquesne","Duquesne");
		NCAABMapping.put("E Carolina","East Carolina");
		NCAABMapping.put("E Illinois","Eastern Illinois");
		NCAABMapping.put("E Kentucky","Eastern Kentucky");
		NCAABMapping.put("E Michigan","Eastern Michigan");
		NCAABMapping.put("E Tenn St","East Tennessee State");
		NCAABMapping.put("E Washingtn","Eastern Washington");
		NCAABMapping.put("Elon","Elon");
		NCAABMapping.put("Evansville","Evansville");
		NCAABMapping.put("F Dickinson","Fairleigh Dickinson");
		NCAABMapping.put("Fairfield","Fairfield");
		NCAABMapping.put("Fla Atlantic","Florida Atlantic");
		NCAABMapping.put("Fla Gulf Cst","Florida Gulf Coast");
		NCAABMapping.put("Florida","Florida");
		NCAABMapping.put("Florida A&amp;M","Florida A&M");
		NCAABMapping.put("Florida Intl","Florida International");
		NCAABMapping.put("Florida St","Florida State");
		NCAABMapping.put("Fordham","Fordham");
		NCAABMapping.put("Fresno St","Fresno State");
		NCAABMapping.put("Furman","Furman");
		NCAABMapping.put("GA Southern","Georgia Southern");
		NCAABMapping.put("GA Tech","Georgia Tech");
		NCAABMapping.put("Gard-Webb","Gardner-Webb");
		NCAABMapping.put("Geo Mason","George Mason");
		NCAABMapping.put("Geo Wshgtn","George Washington");
		NCAABMapping.put("Georgetown","Georgetown");
		NCAABMapping.put("Georgia","Georgia");
		NCAABMapping.put("Georgia St","Georgia State");
		NCAABMapping.put("Gonzaga","Gonzaga");
		NCAABMapping.put("Grambling St","Grambling State");
		NCAABMapping.put("Grd Canyon","Grand Canyon");
		NCAABMapping.put("Hampton","Hampton");
		NCAABMapping.put("Hartford","Hartford");
		NCAABMapping.put("Harvard","Harvard");
		NCAABMapping.put("Hawaii","Hawaii");
		NCAABMapping.put("High Point","High Point");
		NCAABMapping.put("Hofstra","Hofstra");
		NCAABMapping.put("Holy Cross","Holy Cross");
		NCAABMapping.put("Houston","Houston");
		NCAABMapping.put("Houston Bap","Houston Baptist");
		NCAABMapping.put("Howard","Howard");
		NCAABMapping.put("Idaho","Idaho");
		NCAABMapping.put("Idaho State","Idaho State");
		NCAABMapping.put("IL-Chicago","Illinois-Chicago");
		NCAABMapping.put("Illinois","Illinois");
		NCAABMapping.put("Illinois St","Illinois State");
		NCAABMapping.put("Incar Word","Incarnate Word");
		NCAABMapping.put("Indiana","Indiana");
		NCAABMapping.put("Indiana St","Indiana State");
		NCAABMapping.put("Iona","Iona");
		NCAABMapping.put("Iowa","Iowa");
		NCAABMapping.put("Iowa State","Iowa State");
		NCAABMapping.put("IPFW","Fort Wayne(IPFW)");
		NCAABMapping.put("IUPUI","IUPUI");
		NCAABMapping.put("Jackson St","Jackson State");
		NCAABMapping.put("Jacksonville","Jacksonville");
		NCAABMapping.put("James Mad","James Madison");
		NCAABMapping.put("Jksnville St","Jacksonville State");
		NCAABMapping.put("Kansas","Kansas");
		NCAABMapping.put("Kansas St","Kansas State");
		NCAABMapping.put("Kennesaw St","Kennesaw State");
		NCAABMapping.put("Kent State","Kent State");
		NCAABMapping.put("Kentucky","Kentucky");
		NCAABMapping.put("LA Lafayette","Louisiana-Lafayette");
		NCAABMapping.put("LA Monroe","Louisiana-Monroe");
		NCAABMapping.put("La Salle","La Salle");
		NCAABMapping.put("LA Tech","Louisiana Tech");
		NCAABMapping.put("Lafayette","Lafayette");
		NCAABMapping.put("Lamar","Lamar");
		NCAABMapping.put("Lehigh","Lehigh");
		NCAABMapping.put("Lg Beach St","Long Beach State");
		NCAABMapping.put("Liberty","Liberty");
		NCAABMapping.put("Lipscomb","Lipscomb");
		NCAABMapping.put("LIU-Brooklyn","Long Island University");
		NCAABMapping.put("Longwood","Longwood");
		NCAABMapping.put("Louisville","Louisville");
		NCAABMapping.put("Loyola Mymt","Loyola Marymount");
		NCAABMapping.put("Loyola-Chi","Loyola-Chicago");
		NCAABMapping.put("Loyola-MD","Loyola-Maryland");
		NCAABMapping.put("LSU","LSU");
		NCAABMapping.put("Maine","Maine");
		NCAABMapping.put("Manhattan","Manhattan");
		NCAABMapping.put("Marist","Marist");
		NCAABMapping.put("Marquette","Marquette");
		NCAABMapping.put("Marshall","Marshall");
		NCAABMapping.put("Maryland","Maryland");
		NCAABMapping.put("Maryland BC","Maryland BC");
		NCAABMapping.put("Maryland ES","Maryland-Eastern Shore");
		NCAABMapping.put("Mass Lowell","Massachusetts Lowell");
		NCAABMapping.put("McNeese St","McNeese State");
		NCAABMapping.put("Memphis","Memphis");
		NCAABMapping.put("Mercer","Mercer");
		NCAABMapping.put("Miami (FL)","Miami-Florida");
		NCAABMapping.put("Miami (OH)","Miami-Ohio");
		NCAABMapping.put("Michigan","Michigan");
		NCAABMapping.put("Michigan St","Michigan State");
		NCAABMapping.put("Middle Tenn","Middle Tennessee State");
		NCAABMapping.put("Minnesota","Minnesota");
		NCAABMapping.put("Miss State","Mississippi State");
		NCAABMapping.put("Miss Val St","Mississippi Valley State");
		NCAABMapping.put("Mississippi","Mississippi");
		NCAABMapping.put("Missouri","Missouri");
		NCAABMapping.put("Missouri St","Missouri State");
		NCAABMapping.put("Monmouth","Monmouth");
		NCAABMapping.put("Montana","Montana");
		NCAABMapping.put("Montana St","Montana State");
		NCAABMapping.put("Morehead St","Morehead State");
		NCAABMapping.put("Morgan St","Morgan State");
		NCAABMapping.put("Mt St Marys","Mount St. Mary's");
		NCAABMapping.put("Murray St","Murray State");
		NCAABMapping.put("N Arizona","Northern Arizona");
		NCAABMapping.put("N Carolina","North Carolina");
		NCAABMapping.put("N Colorado","Northern Colorado");
		NCAABMapping.put("N Dakota St","North Dakota State");
		NCAABMapping.put("N Florida","North Florida");
		NCAABMapping.put("N Hampshire","New Hampshire");
		NCAABMapping.put("N Illinois","Northern Illinois");
		NCAABMapping.put("N Iowa","Northern Iowa");
		NCAABMapping.put("N Kentucky","Northern Kentucky");
		NCAABMapping.put("N Mex State","New Mexico State");
		NCAABMapping.put("Navy","Navy");
		NCAABMapping.put("NC A&amp;T","NC A&T");
		NCAABMapping.put("NC Central","NC Central");
		NCAABMapping.put("NC State","NC State");
		NCAABMapping.put("NC-Asheville","NC Asheville");
		NCAABMapping.put("NC-Grnsboro","NC Greensboro");
		NCAABMapping.put("NC-Wilmgton","NC Wilmington");
		NCAABMapping.put("Neb Omaha","Nebraska Omaha");
		NCAABMapping.put("Nebraska","Nebraska");
		NCAABMapping.put("Nevada","Nevada");
		NCAABMapping.put("New Mexico","New Mexico");
		NCAABMapping.put("New Orleans","New Orleans");
		NCAABMapping.put("Niagara","Niagara");
		NCAABMapping.put("Nicholls St","Nicholls St");
		NCAABMapping.put("NJIT","NJIT");
		NCAABMapping.put("Norfolk St","Norfolk State");
		NCAABMapping.put("North Dakota","North Dakota");
		NCAABMapping.put("North Texas","North Texas");
		NCAABMapping.put("Northeastrn","Northeastern");
		NCAABMapping.put("Northwestern","Northwestern");
		NCAABMapping.put("Notre Dame","Notre Dame");
		NCAABMapping.put("NW State","Northwestern State");
		NCAABMapping.put("Oakland","Oakland");
		NCAABMapping.put("Ohio","Ohio");
		NCAABMapping.put("Ohio State","Ohio State");
		NCAABMapping.put("Oklahoma","Oklahoma");
		NCAABMapping.put("Oklahoma St","Oklahoma State");
		NCAABMapping.put("Old Dominion","Old Dominion");
		NCAABMapping.put("Oral Roberts","Oral Roberts");
		NCAABMapping.put("Oregon","Oregon");
		NCAABMapping.put("Oregon St","Oregon State");
		NCAABMapping.put("Pacific","Pacific");
		NCAABMapping.put("Penn State","Penn State");
		NCAABMapping.put("Pepperdine","Pepperdine");
		NCAABMapping.put("Pittsburgh","Pittsburgh");
		NCAABMapping.put("Portland","Portland");
		NCAABMapping.put("Portland St","Portland State");
		NCAABMapping.put("Prairie View","Prairie View A&M");
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
		NCAABMapping.put("Rob Morris","Robert Morris");
		NCAABMapping.put("Rutgers","Rutgers");
		NCAABMapping.put("S Alabama","South Alabama");
		NCAABMapping.put("S Car State","South Carolina State");
		NCAABMapping.put("S Carolina","South Carolina");
		NCAABMapping.put("S Dakota St","South Dakota State");
		NCAABMapping.put("S Florida","South Florida");
		NCAABMapping.put("S Illinois","Southern Illinois");
		NCAABMapping.put("S Methodist","SMU");
		NCAABMapping.put("S Mississippi","Southern Miss");
		NCAABMapping.put("S Utah","Southern Utah");
		NCAABMapping.put("Sac State","Sacramento State");
		NCAABMapping.put("Sacred Hrt","Sacred Heart");
		NCAABMapping.put("Saint Louis","Saint Louis");
		NCAABMapping.put("Sam Hous St","Sam Houston State");
		NCAABMapping.put("Samford","Samford");
		NCAABMapping.put("San Diego","San Diego");
		NCAABMapping.put("San Diego St","San Diego State");
		NCAABMapping.put("San Fransco","San Francisco");
		NCAABMapping.put("San Jose St","San Jose State");
		NCAABMapping.put("Santa Clara","Santa Clara");
		NCAABMapping.put("Savannah St","Savannah State");
		NCAABMapping.put("SC Upstate","South Carolina Upstate");
		NCAABMapping.put("SE Louisiana","SE Louisiana");
		NCAABMapping.put("SE Missouri","SE Missouri");
		NCAABMapping.put("Seattle","Seattle");
		NCAABMapping.put("Seton Hall","Seton Hall");
		NCAABMapping.put("Siena","Siena");
		NCAABMapping.put("SIU Edward","SIU-Edwardsville");
		NCAABMapping.put("South Dakota","South Dakota");
		NCAABMapping.put("Southern","Southern U");
		NCAABMapping.put("St Bonavent","Saint Bonaventure");
		NCAABMapping.put("St Fran (NY)","Saint Francis-NY");
		NCAABMapping.put("St Fran (PA)","Saint Francis-Pa.");
		NCAABMapping.put("St Johns","St. John's");
		NCAABMapping.put("St Josephs","Saint Joseph's-Pa.");
		NCAABMapping.put("St Marys","Saint Mary's-California");
		NCAABMapping.put("St Peters","Saint Peter's");
		NCAABMapping.put("Stanford","Stanford");
		NCAABMapping.put("Ste F Austin","Stephen F. Austin");
		NCAABMapping.put("Stetson","Stetson");
		NCAABMapping.put("Stony Brook","Stony Brook");
		NCAABMapping.put("Syracuse","Syracuse");
		NCAABMapping.put("Temple","Temple");
		NCAABMapping.put("Tennessee","Tennessee");
		NCAABMapping.put("Texas","Texas");
		NCAABMapping.put("Texas A&amp;M","Texas A&M");
		NCAABMapping.put("Texas State","Texas State");
		NCAABMapping.put("Texas Tech","Texas Tech");
		NCAABMapping.put("TN Martin","Tennessee-Martin");
		NCAABMapping.put("TN State","Tennessee State");
		NCAABMapping.put("TN Tech","Tennessee Tech");
		NCAABMapping.put("Toledo","Toledo");
		NCAABMapping.put("Towson","Towson");
		NCAABMapping.put("Troy","Troy");
		NCAABMapping.put("Tulane","Tulane");
		NCAABMapping.put("Tulsa","Tulsa");
		NCAABMapping.put("TX A&amp;M-CC","Texas A&M-CC");
		NCAABMapping.put("TX Christian","TCU");
		NCAABMapping.put("TX El Paso","UTEP");
		NCAABMapping.put("TX Southern","Texas Southern");
		NCAABMapping.put("TX-Arlington","Texas-Arlington");
		NCAABMapping.put("TX-Pan Am","Texas Rio Grande Valley");
		NCAABMapping.put("TX-San Ant","UTSA");
		NCAABMapping.put("U Mass","Massachusetts");
		NCAABMapping.put("U Penn","Pennsylvania");
		NCAABMapping.put("UAB","UAB");
		NCAABMapping.put("UC Davis","UC Davis");
		NCAABMapping.put("UC Irvine","UC Irvine");
		NCAABMapping.put("UC Riverside","UC Riverside");
		NCAABMapping.put("UCLA","UCLA");
		NCAABMapping.put("UCSB","UC Santa Barbara");
		NCAABMapping.put("UMKC","UMKC");
		NCAABMapping.put("UNLV","UNLV");
		NCAABMapping.put("USC","USC");
		NCAABMapping.put("Utah","Utah");
		NCAABMapping.put("Utah State","Utah State");
		NCAABMapping.put("Utah Val St","Utah Valley State");
		NCAABMapping.put("VA Military","VMI");
		NCAABMapping.put("VA Tech","Virginia Tech");
		NCAABMapping.put("Valparaiso","Valparaiso");
		NCAABMapping.put("Vanderbilt","Vanderbilt");
		NCAABMapping.put("VCU","VCU");
		NCAABMapping.put("Vermont","Vermont");
		NCAABMapping.put("Villanova","Villanova");
		NCAABMapping.put("Virginia","Virginia");
		NCAABMapping.put("W Carolina","Western Carolina");
		NCAABMapping.put("W Illinois","Western Illinois");
		NCAABMapping.put("W Kentucky","Western Kentucky");
		NCAABMapping.put("W Michigan","Western Michigan");
		NCAABMapping.put("W Virginia","West Virginia");
		NCAABMapping.put("Wagner","Wagner");
		NCAABMapping.put("Wake Forest","Wake Forest");
		NCAABMapping.put("Wash State","Washington State");
		NCAABMapping.put("Washington","Washington");
		NCAABMapping.put("Weber State","Weber State");
		NCAABMapping.put("WI-Grn Bay","Wisconsin Green Bay");
		NCAABMapping.put("WI-Milwkee","Wisconsin-Milwaukee");
		NCAABMapping.put("Wichita St","Wichita State");
		NCAABMapping.put("Winthrop","Winthrop");
		NCAABMapping.put("Wisconsin","Wisconsin");
		NCAABMapping.put("Wm &amp; Mary","William & Mary");
		NCAABMapping.put("Wofford","Wofford");
		NCAABMapping.put("Wright State","Wright State");
		NCAABMapping.put("Wyoming","Wyoming");
		NCAABMapping.put("Xavier","Xavier");
		NCAABMapping.put("Yale","Yale");
		NCAABMapping.put("Youngs St","Youngstown State");
	}

	/**
	 * Constructor
	 */
	public SportslineParser() {
		super();
		LOGGER.info("Entering EspnParser()");
		LOGGER.info("Exiting EspnParser()");
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
	 */
	public List<SportslineOdds> parseGames(String xhtml) {
		LOGGER.info("Entering parseGames()");
		final List<SportslineOdds> odds = new ArrayList<SportslineOdds>();

		try {
			final Document doc = parseXhtml(xhtml);
			final Elements divs = doc.select(".odds-table div");
			String day = null;
			String time = null;

			// Divs
			if (divs != null && divs.size() > 0) {
				for (Element div : divs) {
					final String classname = div.attr("class");

					// Get the date
					if (classname != null && classname.contains("date")) {
						final Elements divs2 = div.select("div");
						if (divs2 != null && divs2.size() > 0) {
							final Element div2 = divs2.get(0);
							day = div2.html().trim();
						}
					} else if (classname != null && classname.contains("row-group")) {
						final Elements adivs = div.select("a .col");
						int adivcounter = 0;
						for (Element adiv : adivs) {
							switch (adivcounter++) {
								case 0:
									
									break;
								case 1:
									break;
								case 2:
									break;
								case 3:
									break;
								default:
									break;
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting parseGames()");
		return odds;
	}

	/**
	 * 
	 * @param div
	 * @return
	 */
	private String gameTime(Element div) {
		String divhtml = div.html();
		int index = divhtml.indexOf("</i>");
		if (index != -1) {
			
		}
		return null;
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
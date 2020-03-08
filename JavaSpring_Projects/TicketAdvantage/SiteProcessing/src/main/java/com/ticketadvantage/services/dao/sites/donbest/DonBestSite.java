/**
 * 
 */
package com.ticketadvantage.services.dao.sites.donbest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.linemovement.dto.MovementData;
import com.linemovement.entity.LineMovement;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.ticketadvantage.services.dao.sites.SiteProcessor;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.dao.sites.SiteTransaction;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.AccountEvent;
import com.ticketadvantage.services.model.BaseRecordEvent;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.MlRecordEvent;
import com.ticketadvantage.services.model.SpreadRecordEvent;
import com.ticketadvantage.services.model.TotalRecordEvent;

/**
 * @author jmiller
 *
 */
public class DonBestSite extends SiteProcessor {
	private final static Logger LOGGER = Logger.getLogger(DonBestSite.class);
	private static final DonBestParser donbestparser = new DonBestParser();
	private static final Map<String, String> NFLMapping = new HashMap<String, String>();
	private static final Map<String, String> NCAAFMapping = new HashMap<String, String>();

	static {
		NFLMapping.put("Arizona", "Arizona Cardinals");
		NFLMapping.put("Atlanta", "Atlanta Falcons");
		NFLMapping.put("Baltimore", "Baltimore Ravens");
		NFLMapping.put("Buffalo", "Buffalo Bills");
		NFLMapping.put("Carolina", "Carolina Panthers");
		NFLMapping.put("Chicago", "Chicago Bears");
		NFLMapping.put("Cincinnati", "Cincinnati Bengals");
		NFLMapping.put("Cleveland", "Cleveland Browns");
		NFLMapping.put("Dallas", "Dallas Cowboys");
		NFLMapping.put("Denver", "Denver Broncos");
		NFLMapping.put("Detroit", "Detroit Lions");
		NFLMapping.put("Green Bay", "Green Bay Packers");
		NFLMapping.put("Houston", "Houston Texans");
		NFLMapping.put("Indianapolis", "Indianapolis Colts");
		NFLMapping.put("Jacksonville", "Jacksonville Jaguars");
		NFLMapping.put("Kansas City", "Kansas City Chiefs");
		NFLMapping.put("LA Chargers", "Los Angeles Chargers");
		NFLMapping.put("LA Rams", "Los Angeles Rams");
		NFLMapping.put("Miami", "Miami Dolphins");
		NFLMapping.put("Minnesota", "Minnesota Vikings");
		NFLMapping.put("New England", "New England Patriots");
		NFLMapping.put("New Orleans", "New Orleans Saints");
		NFLMapping.put("NY Giants", "New York Giants");
		NFLMapping.put("NY Jets", "New York Jets");
		NFLMapping.put("Oakland", "Oakland Raiders");
		NFLMapping.put("Philadelphia", "Philadelphia Eagles");
		NFLMapping.put("Pittsburgh", "Pittsburgh Steelers");
		NFLMapping.put("San Francisco", "San Francisco 49ers");
		NFLMapping.put("Seattle", "Seattle Seahawks");
		NFLMapping.put("Tampa Bay", "Tampa Bay Buccaneers");
		NFLMapping.put("Tennessee", "Tennessee Titans");
		NFLMapping.put("Washington", "Washington Redskins");		
	}

	static {
		NCAAFMapping.put("Abilene Christian", "Abilene Christian Wildcats");
		NCAAFMapping.put("Air Force", "Air Force Falcons");
		NCAAFMapping.put("Akron", "Akron Zips");
		NCAAFMapping.put("Alabama A&amp;M", "Alabama A&M Bulldogs");
		NCAAFMapping.put("Alabama", "Alabama Crimson Tide");
		NCAAFMapping.put("Alabama St", "Alabama State Hornets");
		NCAAFMapping.put("UAB", "Alabama-Birmingham Blazers");
		NCAAFMapping.put("Albany", "Albany Great Danes");
		NCAAFMapping.put("Alcorn State", "Alcorn State Braves");
		NCAAFMapping.put("Appalachian St", "Appalachian State Mountaineers");
		NCAAFMapping.put("Arizona State", "Arizona State Sun Devils");
		NCAAFMapping.put("Arizona U", "Arizona Wildcats");
		NCAAFMapping.put("Arkansas", "Arkansas Razorbacks");
		NCAAFMapping.put("Arkansas St", "Arkansas State Red Wolves");
		NCAAFMapping.put("Arkansas Pine Bluff", "Arkansas-Pine Bluff Golden Lions");
		NCAAFMapping.put("Army", "Army Black Knights");
		NCAAFMapping.put("Auburn", "Auburn Tigers");
		NCAAFMapping.put("Austin Peay", "Austin Peay State Governors");
		NCAAFMapping.put("Ball State", "Ball State Cardinals");
		NCAAFMapping.put("Baylor", "Baylor Bears");
		NCAAFMapping.put("Bethune Cookman", "Bethune Cookman Wildcats");
		NCAAFMapping.put("Boise State", "Boise State Broncos");
		NCAAFMapping.put("Boston College", "Boston College Eagles");
		NCAAFMapping.put("Bowling Green", "Bowling Green State Falcons");
		NCAAFMapping.put("BYU", "Brigham Young Cougars");
		NCAAFMapping.put("Brown", "Brown Bears");
		NCAAFMapping.put("Bryant", "Bryant University Bulldogs");
		NCAAFMapping.put("Bucknell", "Bucknell Bison");
		NCAAFMapping.put("Buffalo U", "Buffalo Bulls");
		NCAAFMapping.put("Butler", "Butler Bulldogs");
		NCAAFMapping.put("Cal Poly", "Cal Poly-Slo Mustangs");
		NCAAFMapping.put("California", "California Golden Bears");
		NCAAFMapping.put("CS Sacramento", "California State-Sacramento Hornets");
		NCAAFMapping.put("UC Davis", "California-Davis Aggies");
		NCAAFMapping.put("Campbell", "Campbell Fighting Camels");
		NCAAFMapping.put("Central Arkansas", "Central Arkansas Bears");
		NCAAFMapping.put("Central Conn.", "Central Connecticut State Blue Devils");
		NCAAFMapping.put("Central Florida", "Central Florida Knights");
		NCAAFMapping.put("Central Michigan", "Central Michigan Chippewas");
		NCAAFMapping.put("Charleston Southern", "Charleston Southern Buccaneers");
		NCAAFMapping.put("Cincinnati U", "Cincinnati Bearcats");
		NCAAFMapping.put("Citadel", "Citadel Bulldogs");
		NCAAFMapping.put("Clemson", "Clemson Tigers");
		NCAAFMapping.put("Coastal Carolina", "Coastal Carolina Chanticleers");
		NCAAFMapping.put("Colgate", "Colgate Raiders");
		NCAAFMapping.put("Colorado", "Colorado Buffaloes");
		NCAAFMapping.put("Colorado State", "Colorado State Rams");
		NCAAFMapping.put("Columbia", "Columbia Lions");
		NCAAFMapping.put("Connecticut", "Connecticut Huskies");
		NCAAFMapping.put("Cornell", "Cornell Big Red");
		NCAAFMapping.put("Dartmouth", "Dartmouth Big Green");
		NCAAFMapping.put("Davidson", "Davidson College Wildcats");
		NCAAFMapping.put("Dayton", "Dayton Flyers");
		NCAAFMapping.put("Delaware", "Delaware Blue Hens");
		NCAAFMapping.put("Delaware State", "Delaware State Hornets");
		NCAAFMapping.put("Drake", "Drake Bulldogs");
		NCAAFMapping.put("Duke", "Duke Blue Devils");
		NCAAFMapping.put("Duquesne", "Duquesne Dukes");
		NCAAFMapping.put("East Carolina", "East Carolina Pirates");
		NCAAFMapping.put("East Tennessee State", "East Tennessee State Buccaneers");
		NCAAFMapping.put("Eastern Illinois", "Eastern Illinois Panthers");
		NCAAFMapping.put("Eastern Kentucky", "Eastern Kentucky Colonels");
		NCAAFMapping.put("Eastern Michigan", "Eastern Michigan Eagles");
		NCAAFMapping.put("Eastern Washington", "Eastern Washington Eagles");
		NCAAFMapping.put("Elon", "Elon Phoenix");
		NCAAFMapping.put("Florida A&amp;M", "Florida A&M Rattlers");
		NCAAFMapping.put("Florida Atlantic", "Florida Atlantic Owls");
		NCAAFMapping.put("Florida", "Florida Gators");
		NCAAFMapping.put("Florida Intl", "Florida International Golden Panthers");
		NCAAFMapping.put("Florida State", "Florida State Seminoles");
		NCAAFMapping.put("Fordham", "Fordham Rams");
		NCAAFMapping.put("Fresno State", "Fresno State Bulldogs");
		NCAAFMapping.put("Furman", "Furman Paladins");
		NCAAFMapping.put("Gardner Webb", "Gardner Webb Runnin Bulldogs");
		NCAAFMapping.put("Georgetown", "Georgetown Hoyas");
		NCAAFMapping.put("Georgia", "Georgia Bulldogs");
		NCAAFMapping.put("Georgia Southern", "Georgia Southern Eagles");
		NCAAFMapping.put("Georgia State", "Georgia State Panthers");
		NCAAFMapping.put("Georgia Tech", "Georgia Tech Yellow Jackets");
		NCAAFMapping.put("Grambling", "Grambling State Tigers");
		NCAAFMapping.put("Hampton", "Hampton Pirates");
		NCAAFMapping.put("Harvard", "Harvard Crimson");
		NCAAFMapping.put("Hawaii", "Hawaii Warriors");
		NCAAFMapping.put("Holy Cross", "Holy Cross Crusaders");
		NCAAFMapping.put("Houston Baptist", "Houston Baptist Huskies");
		NCAAFMapping.put("Houston U", "Houston Cougars");
		NCAAFMapping.put("Howard", "Howard Bison");
		NCAAFMapping.put("Idaho State", "Idaho State Bengals");
		NCAAFMapping.put("Idaho", "Idaho Vandals");
		NCAAFMapping.put("Illinois", "Illinois Fighting Illini");
		NCAAFMapping.put("Illinois State", "Illinois State Redbirds");
		NCAAFMapping.put("Incarnate Word", "Incarnate Word Cardinals");
		NCAAFMapping.put("Indiana", "Indiana Hoosiers");
		NCAAFMapping.put("Indiana State", "Indiana State Sycamores");
		NCAAFMapping.put("Iowa", "Iowa Hawkeyes");
		NCAAFMapping.put("Iowa State", "Iowa State Cyclones");
		NCAAFMapping.put("Jackson St", "Jackson State Tigers");
		NCAAFMapping.put("Jacksonville", "Jacksonville Dolphins");
		NCAAFMapping.put("Jacksonville St", "Jacksonville State Gamecocks");
		NCAAFMapping.put("James Madison", "James Madison Dukes");
		NCAAFMapping.put("Kansas", "Kansas Jayhawks");
		NCAAFMapping.put("Kansas State", "Kansas State Wildcats");
		NCAAFMapping.put("Kennesaw State", "Kennesaw State Owls");
		NCAAFMapping.put("Kent State", "Kent State Golden Flashes");
		NCAAFMapping.put("Kentucky", "Kentucky Wildcats");
		NCAAFMapping.put("Lafayette", "Lafayette Leopards");
		NCAAFMapping.put("Lamar", "Lamar Cardinals");
		NCAAFMapping.put("Lehigh", "Lehigh Mountain Hawks");
		NCAAFMapping.put("Liberty", "Liberty Flames");
		NCAAFMapping.put("LSU", "Louisiana State Tigers");
		NCAAFMapping.put("Louisiana Tech", "Louisiana Tech Bulldogs");
		NCAAFMapping.put("UL Lafayette", "Louisiana-Lafayette Ragin Cajuns");
		NCAAFMapping.put("UL Monroe", "Louisiana-Monroe Warhawks");
		NCAAFMapping.put("Louisville", "Louisville Cardinals");
		NCAAFMapping.put("Maine", "Maine Black Bears");
		NCAAFMapping.put("Marist", "Marist Red Foxes");
		NCAAFMapping.put("Marshall", "Marshall Thundering Herd");
		NCAAFMapping.put("Maryland", "Maryland Terrapins");
		NCAAFMapping.put("Massachusetts", "Massachusetts Minutemen");
		NCAAFMapping.put("McNeese State", "McNeese State Cowboys");
		NCAAFMapping.put("Memphis", "Memphis Tigers");
		NCAAFMapping.put("Mercer", "Mercer Bears");
		NCAAFMapping.put("Miami Florida", "Miami-Florida Hurricanes");
		NCAAFMapping.put("Miami Ohio", "Miami-Ohio Redhawks");
		NCAAFMapping.put("Michigan State", "Michigan State Spartans");
		NCAAFMapping.put("Michigan", "Michigan Wolverines");
		NCAAFMapping.put("Middle Tenn St", "Middle Tennessee State Blue Raiders");
		NCAAFMapping.put("Minnesota U", "Minnesota Golden Gophers");
		NCAAFMapping.put("Mississippi", "Mississippi Rebels");
		NCAAFMapping.put("Mississippi St", "Mississippi State Bulldogs");
		NCAAFMapping.put("Miss Valley State", "Mississippi Valley State Delta Devils");
		NCAAFMapping.put("Missouri State", "Missouri State Bears");
		NCAAFMapping.put("Missouri", "Missouri Tigers");
		NCAAFMapping.put("Monmouth", "Monmouth-New Jersey Hawks");
		NCAAFMapping.put("Montana", "Montana Grizzlies");
		NCAAFMapping.put("Montana State", "Montana State Bobcats");
		NCAAFMapping.put("Morehead St", "Morehead State Eagles");
		NCAAFMapping.put("Morgan State", "Morgan State Bears");
		NCAAFMapping.put("Murray State", "Murray State Racers");
		NCAAFMapping.put("Navy", "Navy Midshipmen");
		NCAAFMapping.put("Nebraska", "Nebraska Cornhuskers");
		NCAAFMapping.put("Nevada", "Nevada Wolf Pack");
		NCAAFMapping.put("New Hampshire", "New Hampshire Wildcats");
		NCAAFMapping.put("New Mexico", "New Mexico Lobos");
		NCAAFMapping.put("New Mexico State", "New Mexico State Aggies");
		NCAAFMapping.put("Nicholls State", "Nicholls State Colonels");
		NCAAFMapping.put("Norfolk St", "Norfolk State Spartans");
		NCAAFMapping.put("North Alabama", "North Alabama");
		NCAAFMapping.put("N. Carolina A&amp;T", "North Carolina A&T Aggies");
		NCAAFMapping.put("NC Central", "North Carolina Central Eagles");
		NCAAFMapping.put("NC State", "North Carolina State Wolfpack");
		NCAAFMapping.put("North Carolina", "North Carolina Tar Heels");
		NCAAFMapping.put("Charlotte", "North Carolina-Charlotte 49ers");
		NCAAFMapping.put("North Dakota", "North Dakota Fighting Hawks");
		NCAAFMapping.put("North Dakota State", "North Dakota State Bison");
		NCAAFMapping.put("North Texas", "North Texas Mean Green");
		NCAAFMapping.put("Northern Arizona", "Northern Arizona Lumberjacks");
		NCAAFMapping.put("Northern Colorado", "Northern Colorado Bears");
		NCAAFMapping.put("No Illinois", "Northern Illinois Huskies");
		NCAAFMapping.put("Northern Iowa", "Northern Iowa Panthers");
		NCAAFMapping.put("Northwestern State", "Northwestern State Demons");
		NCAAFMapping.put("Northwestern", "Northwestern Wildcats");
		NCAAFMapping.put("Notre Dame", "Notre Dame Fighting Irish");
		NCAAFMapping.put("Ohio", "Ohio Bobcats");
		NCAAFMapping.put("Ohio State", "Ohio State Buckeyes");
		NCAAFMapping.put("Oklahoma", "Oklahoma Sooners");
		NCAAFMapping.put("Oklahoma State", "Oklahoma State Cowboys");
		NCAAFMapping.put("Old Dominion", "Old Dominion Monarchs");
		NCAAFMapping.put("Oregon", "Oregon Ducks");
		NCAAFMapping.put("Oregon State", "Oregon State Beavers");
		NCAAFMapping.put("Penn State", "Penn State Nittany Lions");
		NCAAFMapping.put("Pennsylvania", "Pennsylvania Quakers");
		NCAAFMapping.put("Pittsburgh U", "Pittsburgh Panthers");
		NCAAFMapping.put("Portland St", "Portland State Vikings");
		NCAAFMapping.put("Prairie View A&amp;M", "Prairie View A&M Panthers");
		NCAAFMapping.put("Presbyterian", "Presbyterian Blue Hose");
		NCAAFMapping.put("Princeton", "Princeton Tigers");
		NCAAFMapping.put("Purdue", "Purdue Boilermakers");
		NCAAFMapping.put("Rhode Island", "Rhode Island Rams");
		NCAAFMapping.put("Rice", "Rice Owls");
		NCAAFMapping.put("Richmond", "Richmond Spiders");
		NCAAFMapping.put("Robert Morris", "Robert Morris Colonials");
		NCAAFMapping.put("Rutgers", "Rutgers Scarlet Knights");
		NCAAFMapping.put("Sacred Heart", "Sacred Heart Pioneers");
		NCAAFMapping.put("St. Francis (PA)", "Saint Francis-Pennsylvania Red Flash");
		NCAAFMapping.put("Sam Houston St", "Sam Houston State Bearkats");
		NCAAFMapping.put("Samford", "Samford Bulldogs");
		NCAAFMapping.put("San Diego State", "San Diego State Aztecs");
		NCAAFMapping.put("San Diego", "San Diego Toreros");
		NCAAFMapping.put("San Jose State", "San Jose State Spartans");
		NCAAFMapping.put("Savannah State", "Savannah State Tigers");
		NCAAFMapping.put("South Alabama", "South Alabama Jaguars");
		NCAAFMapping.put("South Carolina", "South Carolina Gamecocks");
		NCAAFMapping.put("South Carolina St", "South Carolina State Bulldogs");
		NCAAFMapping.put("South Dakota", "South Dakota Coyotes");
		NCAAFMapping.put("South Dakota St", "South Dakota State Jackrabbits");
		NCAAFMapping.put("South Florida", "South Florida Bulls");
		NCAAFMapping.put("SE Missouri St", "Southeast Missouri State Redhawks");
		NCAAFMapping.put("SE Louisiana", "Southeastern Louisiana Lions");
		NCAAFMapping.put("USC", "Southern California Trojans");
		NCAAFMapping.put("Southern Illinois", "Southern Illinois Salukis");
		NCAAFMapping.put("SMU", "Southern Methodist Mustangs");
		NCAAFMapping.put("So Mississippi", "Southern Miss Golden Eagles");
		NCAAFMapping.put("Southern", "Southern University A&M Jaguars");
		NCAAFMapping.put("Southern Utah", "Southern Utah Thunderbirds");
		NCAAFMapping.put("Stanford", "Stanford Cardinal");
		NCAAFMapping.put("Stephen F. Austin", "Stephen F. Austin State Lumberjacks");
		NCAAFMapping.put("Stetson", "Stetson Hatters");
		NCAAFMapping.put("Stony Brook", "Stony Brook Seawolves");
		NCAAFMapping.put("Syracuse", "Syracuse Orange");
		NCAAFMapping.put("Temple", "Temple Owls");
		NCAAFMapping.put("Tennessee St", "Tennessee State Tigers");
		NCAAFMapping.put("Tennessee Tech", "Tennessee Tech Golden Eagles");
		NCAAFMapping.put("Tennessee U", "Tennessee Volunteers");
		NCAAFMapping.put("Chattanooga", "Tennessee-Chattanooga Mocs");
		NCAAFMapping.put("Tenn Martin", "Tennessee-Martin Skyhawks");
		NCAAFMapping.put("Texas A&amp;M", "Texas A&M Aggies");
		NCAAFMapping.put("TCU", "Texas Christian Horned Frogs");
		NCAAFMapping.put("Texas", "Texas Longhorns");
		NCAAFMapping.put("Texas Southern", "Texas Southern Tigers");
		NCAAFMapping.put("Texas State", "Texas State Bobcats");
		NCAAFMapping.put("Texas Tech", "Texas Tech Red Raiders");
		NCAAFMapping.put("UTEP", "Texas-El Paso Miners");
		NCAAFMapping.put("Tex San Antonio", "Texas-San Antonio Roadrunners");
		NCAAFMapping.put("Toledo", "Toledo Rockets");
		NCAAFMapping.put("Towson", "Towson Tigers");
		NCAAFMapping.put("Troy", "Troy Trojans");
		NCAAFMapping.put("Tulane", "Tulane Green Wave");
		NCAAFMapping.put("Tulsa", "Tulsa Golden Hurricane");
		NCAAFMapping.put("UCLA", "UCLA Bruins");
		NCAAFMapping.put("UNLV", "UNLV Rebels");
		NCAAFMapping.put("Utah State", "Utah State Aggies");
		NCAAFMapping.put("Utah", "Utah Utes");
		NCAAFMapping.put("Valparaiso", "Valparaiso Crusaders");
		NCAAFMapping.put("Vanderbilt", "Vanderbilt Commodores");
		NCAAFMapping.put("Villanova", "Villanova Wildcats");
		NCAAFMapping.put("Virginia", "Virginia Cavaliers");
		NCAAFMapping.put("VMI", "Virginia Military Keydets");
		NCAAFMapping.put("Virginia Tech", "Virginia Tech Hokies");
		NCAAFMapping.put("Wagner", "Wagner Seahawks");
		NCAAFMapping.put("Wake Forest", "Wake Forest Demon Deacons");
		NCAAFMapping.put("Washington U", "Washington Huskies");
		NCAAFMapping.put("Washington State", "Washington State Cougars");
		NCAAFMapping.put("Weber State", "Weber State Wildcats");
		NCAAFMapping.put("West Virginia", "West Virginia Mountaineers");
		NCAAFMapping.put("Western Carolina", "Western Carolina Catamounts");
		NCAAFMapping.put("Western Illinois", "Western Illinois Leathernecks");
		NCAAFMapping.put("Western Kentucky", "Western Kentucky Hilltoppers");
		NCAAFMapping.put("Western Michigan", "Western Michigan Broncos");
		NCAAFMapping.put("William &amp; Mary", "William & Mary Tribe");
		NCAAFMapping.put("Wisconsin", "Wisconsin Badgers");
		NCAAFMapping.put("Wofford", "Wofford Terriers");
		NCAAFMapping.put("Wyoming", "Wyoming Cowboys");
		NCAAFMapping.put("Yale", "Yale Bulldogs");
		NCAAFMapping.put("Youngstown St", "Youngstown State Penguins");
	}

	/**
	 * 
	 */
	public DonBestSite(String host, String username, String password) {
		super("NcaaSite", host, username, password, false, false);
		LOGGER.info("Entering NcaaSite()");
		LOGGER.info("Exiting NcaaSite()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final DonBestSite dbs = new DonBestSite("", "", "");
//			final List<LineMovement> lms = dbs.getNcaafData();
			final List<LineMovement> lms = dbs.getNflData();
//			final List<LineMovement> lms = dbs.getSingleData();
//			LOGGER.error("LineMovements: " + lms);

//			for (LineMovement lm : lms) {
//				LOGGER.error("LineMovement: " + lm);
//			}

            // Connecting To The MongoDb Server Listening On A Default Port (i.e. 27017).
            final MongoClient mongoClntObj = new MongoClient("3.14.246.242", 27017);

            // Get MongoDb Database. If The Database Doesn't Exists, MongoDb Will Automatically Create It For You
            final DB dbObj = mongoClntObj.getDB("archivedb");

            // Get MongoDb Collection. If The Collection Doesn't Exists, MongoDb Will Automatically Create It For You
            final DBCollection collectionObj = dbObj.getCollection("lineMovement");

            // Creating The MongoDb Documents To Store Key-Value Pair
            BasicDBObject documentObj = null;

			for (LineMovement n : lms) {
                documentObj = new BasicDBObject();
                documentObj.append("id", n.getId());
                documentObj.append("eventDateTime", n.getEventDateTime());               
                documentObj.append("eventDate", n.getEventDate());              
                documentObj.append("sportNumber", n.getSportNumber());
                documentObj.append("sportType", n.getSportType());
                documentObj.append("gameNumber", n.getGameNumber());
                documentObj.append("gameType", n.getGameType());
                documentObj.append("lineNumber", n.getLineNumber());
                documentObj.append("lineType", n.getLineType());
                documentObj.append("year", n.getYear());
                documentObj.append("week", n.getWeek());
                documentObj.append("sportsbook", n.getSportsbook());
                documentObj.append("visitorteam", n.getVisitorteam());
                documentObj.append("hometeam", n.getHometeam());
                documentObj.append("siteName", n.getSiteName());
                documentObj.append("homeScore", n.getHomeScore());
                documentObj.append("visitorScore", n.getVisitorScore());
                documentObj.append("visitorRotationId", n.getVisitorRotationId());
                documentObj.append("homeRotationId", n.getHomeRotationId());
                final List<MovementData> linemovements = n.getLinemovements();
                final BasicDBList lmArray = new BasicDBList();

                for (MovementData md : linemovements) {
                		final BasicDBObject mdObject = new BasicDBObject();
                		mdObject.append("lineDate", md.getLineDate());
                		mdObject.append("lineTime", md.getLineTime());
                		mdObject.append("lineone", md.getLineone());
                		mdObject.append("linetwo", md.getLinetwo());
                		mdObject.append("juiceone", md.getJuiceone());
                		mdObject.append("juicetwo", md.getJuicetwo());
                		mdObject.append("lineindicator1", md.getLineindicator1());
                		mdObject.append("lineindicator2", md.getLineindicator2());                		
                		mdObject.append("juiceindicator1", md.getJuiceindicator1());
                		mdObject.append("juiceindicator2", md.getJuiceindicator2());
                		lmArray.add(mdObject);
                }
                documentObj.append("linemovements", lmArray);

                collectionObj.insert(documentObj);
			}

            mongoClntObj.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<LineMovement> getNflData() {
		final List<LineMovement> nd = new ArrayList<LineMovement>();

		try {
			// https://site.api.espn.com/apis/site/v2/sports/football/college-football/scoreboard?lang=en&region=us&calendartype=blacklist&limit=300&dates=2018&seasontype=2&week=1
			final EspnForDonBestSite efdbs = new EspnForDonBestSite("https://www.espn.com", "", "");
			efdbs.getHttpClientWrapper().setupHttpClient("None");
			List<EspnForDonBestData> nflList = efdbs.getNflData();

			try {
				try (Stream<Path> filePathStream = Files.walk(Paths.get("/Users/jmiller/Documents/WoOTechnology/LineMovementData/NFLDBHistoricalLines"))) {
				    filePathStream.forEach(filePath -> {
				        if (Files.isRegularFile(filePath)) {
				            try {
				            		final InputStream fin = new FileInputStream(filePath.toString());
				            		String data = "";
								int i = 0;

								do {
									byte[] buf = new byte[1024];
									i = fin.read(buf);
	
									String value = new String(buf, StandardCharsets.UTF_8);
									data += value;
								} while (i != -1);

								boolean isMl = false;
								int sportNumber = 1;
								String sportType = "nfl";
								int gameNumber = 0;
								String gameType = "";
	
								if (filePath.toString().contains("Moneyline")) {
									isMl = true;
								}
	
								if (filePath.toString().contains("FullGame")) {
									gameNumber = 0;
									gameType = "game";
								} else if (filePath.toString().contains("1stHalf")) {
									gameNumber = 1;
									gameType = "first";								
								} else if (filePath.toString().contains("2ndHalf")) {
									gameNumber = 2;
									gameType = "second";
								}

								LOGGER.debug("filePath.toString(): " + filePath.toString());
								final DonBestData dbd = donbestparser.parseFootballData(isMl, sportNumber, sportType, gameNumber, gameType, data);

								List<LineMovement> quickCheck = dbd.getLines();
								LOGGER.debug("quickCheck.size(): " + quickCheck.size());
								final Map<String, List<LineMovement>> games = new HashMap<String, List<LineMovement>>();

								for (LineMovement lm : quickCheck) {
									if (games.size() == 0) {
						            		List<LineMovement> lms = new ArrayList<LineMovement>();
						            		lms.add(lm);
										games.put(lm.getHomeRotationId() + lm.getLineType(), lms);
									} else {
										boolean didfind = false;

										for (Map.Entry<String, List<LineMovement>> entry : games.entrySet()) {
								            String key = entry.getKey();
								            List<LineMovement> values = entry.getValue();

								            if (key.equals(lm.getHomeRotationId() + lm.getLineType())) {
								            		values.add(lm);
								            		didfind = true;
								            }
										}

										if (!didfind) {
											List<LineMovement> lms = new ArrayList<LineMovement>();
											lms.add(lm);
											games.put(lm.getHomeRotationId() + lm.getLineType(), lms);
										}
									}
								}

								final Set<String> keys = games.keySet();
								for (String key : keys) {
									List<LineMovement> lines = games.get(key);

									if (lines.size() != 4) {
										LOGGER.error("lines.size(): " + lines.size());
										LOGGER.error("RotationID of " + key + " in file " + filePath.toString() + " are missing");

										for (LineMovement lm : lines) {
//											LOGGER.error("ROT LineMovement: " + lm);
										}
									}
								}

								final List<LineMovement> templms = new ArrayList<LineMovement>();

								for (LineMovement lm : dbd.getLines()) {
									LOGGER.debug("LineMovement: " + lm);

									if (lm.getLinemovements() == null || lm.getLinemovements().isEmpty()) {
										LOGGER.error("LineMovement isEmpty: " + lm);
									}

									if (nflList != null && !nflList.isEmpty()) {
										boolean didFind = false;

										for (EspnForDonBestData efdbd : nflList) {
											final LocalDateTime eld = efdbd.getDateofgame();
											final List<MovementData> linemovements = lm.getLinemovements();

											if (linemovements != null && !linemovements.isEmpty()) {
												final MovementData md = linemovements.get(0);	
												final LocalDate ldt = md.getLineDate().toLocalDate();
												final LocalDate eldPlus1 = eld.plusDays(1).toLocalDate();
	
												if ((ldt.isBefore(eld.toLocalDate()) || ldt.isEqual(eld.toLocalDate())) ||
													(lm.getGameType().equals("second") &&
													 (ldt.isBefore(eldPlus1) || ldt.isEqual(eldPlus1)))) {
													String lvt = lm.getVisitorteam();
													String lht = lm.getHometeam();
													String evt = efdbd.getVisitorteam();
													String eht = efdbd.getHometeam();
	
													if ((lvt.equals(evt) && lht.equals(eht)) ||
														(lvt.equals(eht) && lht.equals(evt))) {
														didFind = true;
														String visitorTeam = lm.getVisitorteam();
														String homeTeam = lm.getHometeam();
														boolean found = false;
														Iterator<String> itr = NFLMapping.keySet().iterator();

														while (itr.hasNext()) {
															final String key = itr.next();

															if (key.toUpperCase().equals(visitorTeam.toUpperCase())) {
																found = true;
																lm.setVisitorteam(NFLMapping.get(key));
															}
														}
														if (!found) {
															LOGGER.error("visitorTeam not found: " + visitorTeam);
														}
														found = false;
														itr = NFLMapping.keySet().iterator();

														while (itr.hasNext()) {
															final String key = itr.next();

															if (key.toUpperCase().equals(homeTeam.toUpperCase())) {
																found = true;
																lm.setHometeam(NFLMapping.get(key));
															}
														}
														if (!found) {
															LOGGER.error("homeTeam not found: " + homeTeam);
														}

														lm.setEventDateTime(efdbd.getDateofgame());
														lm.setEventDate(efdbd.getDateofgame());
														lm.setYear(efdbd.getYear());
														lm.setWeek(efdbd.getWeek());
														final String id = (lm.getSportType() +
																lm.getGameType() +
																lm.getLineType() +
																lm.getHometeam() +
																lm.getEventDateTime().toString() +
																lm.getSportsbook()).replaceAll(" ", "");
														lm.setId(id);

														if (lm.getGameNumber() == 0) {
															lm.setVisitorScore(efdbd.getVisitorgamescore().toString());
															lm.setHomeScore(efdbd.getHomegamescore().toString());
														} else if (lm.getGameNumber() == 1) {
															lm.setVisitorScore(efdbd.getVisitor1hscore().toString());
															lm.setHomeScore(efdbd.getHome1hscore().toString());														
														} else if (lm.getGameNumber() == 2) {
															lm.setVisitorScore(efdbd.getVisitor2hscore().toString());
															lm.setHomeScore(efdbd.getHome2hscore().toString());														
														}

														nd.add(lm);
														templms.add(lm);
													}
												}											
											}
										}

										if (!didFind) {
											LOGGER.error("Did not find: " + lm);
										}
									}
								}

								final Comparator<MovementData> byLineDate = new Comparator<MovementData>() {
									public int compare(MovementData left, MovementData right) {
										final LocalDateTime leftDateTime = left.getLineDate();
										final LocalDateTime rightDateTime = right.getLineDate();

										if (leftDateTime.isAfter(rightDateTime)) {
											return -1;
										} else {
											return 1;
										}
									}
								};

								final List<String> uniqueGames = new ArrayList<String>();

								for (LineMovement lm : templms) {
									if (uniqueGames.isEmpty()) {
										uniqueGames.add(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam());
									} else {
										boolean found = false;
										for (String ug : uniqueGames) {
											if (!ug.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
												found = true;
											}
										}

										if (!found) {
											uniqueGames.add(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam());
										}
									}
								}

								for (int x = 0; x < uniqueGames.size(); x++) {
									final String uniqueGame = uniqueGames.get(x);
									final List<MovementData> spreads = new ArrayList<MovementData>();
									final List<MovementData> totals = new ArrayList<MovementData>();
									final List<MovementData> mls = new ArrayList<MovementData>();

									for (LineMovement lm : templms) {
										if (lm.getLineType().equals("spread") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
											final List<MovementData> mds = lm.getLinemovements();

											for (MovementData md : mds) {
												spreads.add(md);									
											}
										} else if (lm.getLineType().equals("total") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
											final List<MovementData> mds = lm.getLinemovements();

											for (MovementData md : mds) {
												totals.add(md);									
											}
										} else if (lm.getLineType().equals("moneyline") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
											final List<MovementData> mds = lm.getLinemovements();

											for (MovementData md : mds) {
												mls.add(md);									
											}
										}
									}

									if (!spreads.isEmpty()) {
										final List<MovementData> consensusSpreads = processSpreadConsensusLines(spreads, byLineDate);
										final LineMovement clm = setupLineMovement("spread", uniqueGame, templms);
										clm.setLinemovements(consensusSpreads);

										nd.add(clm);
									}

									if (!totals.isEmpty()) {
										final List<MovementData> consensusTotals = processConsensusTotals(totals, byLineDate);
										final LineMovement clm = setupLineMovement("total", uniqueGame, templms);
										clm.setLinemovements(consensusTotals);

										nd.add(clm);
									}

									if (!mls.isEmpty()) {
										final List<MovementData> consensusMls = processMlConsensusLines(mls, byLineDate);
										final LineMovement clm = setupLineMovement("moneyline", uniqueGame, templms);
										clm.setLinemovements(consensusMls);

										nd.add(clm);
									}
								}

				        			fin.close();
				            } catch (Throwable t) {
				            		LOGGER.error(t.getMessage(), t);
				            }
				        }
				    });
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return nd;
	}

	/**
	 * 
	 * @return
	 */
	public List<LineMovement> getNcaafData() {
		final List<LineMovement> nd = new ArrayList<LineMovement>();

		try {
			// https://site.api.espn.com/apis/site/v2/sports/football/college-football/scoreboard?lang=en&region=us&calendartype=blacklist&limit=300&dates=2018&seasontype=2&week=1
			final EspnForDonBestSite efdbs = new EspnForDonBestSite("https://www.espn.com", "", "");
			efdbs.getHttpClientWrapper().setupHttpClient("None");
			List<EspnForDonBestData> ncaafList = efdbs.getNcaafData();

			try {			
				try (Stream<Path> filePathStream = Files.walk(Paths.get("/Users/jmiller/Documents/WoOTechnology/LineMovementData/CollegeDBHistoricalLines"))) {
				    filePathStream.forEach(filePath -> {
				        if (Files.isRegularFile(filePath)) {
				            try {
				            		final InputStream fin = new FileInputStream(filePath.toString());
				            		String data = "";
								int i = 0;

								do {
									byte[] buf = new byte[1024];
									i = fin.read(buf);
	
									String value = new String(buf, StandardCharsets.UTF_8);
									data += value;
								} while (i != -1);

								boolean isMl = false;
								int sportNumber = 11;
								String sportType = "ncaaf";
								int gameNumber = 0;
								String gameType = "";
	
								if (filePath.toString().contains("Moneyline")) {
									isMl = true;
								}
	
								if (filePath.toString().contains("FullGame")) {
									gameNumber = 0;
									gameType = "game";
								} else if (filePath.toString().contains("1stHalf")) {
									gameNumber = 1;
									gameType = "first";								
								} else if (filePath.toString().contains("2ndHalf")) {
									gameNumber = 2;
									gameType = "second";
								}
	
								LOGGER.debug("filePath.toString(): " + filePath.toString());
								final DonBestData dbd = donbestparser.parseFootballData(isMl, sportNumber, sportType, gameNumber, gameType, data);

								List<LineMovement> quickCheck = dbd.getLines();
								LOGGER.debug("quickCheck.size(): " + quickCheck.size());
								final Map<String, List<LineMovement>> games = new HashMap<String, List<LineMovement>>();

								for (LineMovement lm : quickCheck) {
									if (games.size() == 0) {
						            		List<LineMovement> lms = new ArrayList<LineMovement>();
						            		lms.add(lm);
										games.put(lm.getHomeRotationId() + lm.getLineType(), lms);
									} else {
										boolean didfind = false;

										for (Map.Entry<String, List<LineMovement>> entry : games.entrySet()) {
								            String key = entry.getKey();
								            List<LineMovement> values = entry.getValue();

								            if (key.equals(lm.getHomeRotationId() + lm.getLineType())) {
								            		values.add(lm);
								            		didfind = true;
								            }
										}

										if (!didfind) {
											List<LineMovement> lms = new ArrayList<LineMovement>();
											lms.add(lm);
											games.put(lm.getHomeRotationId() + lm.getLineType(), lms);
										}
									}
								}

								final Set<String> keys = games.keySet();
								for (String key : keys) {
									List<LineMovement> lines = games.get(key);

									if (lines.size() != 4) {
										LOGGER.error("lines.size(): " + lines.size());
										LOGGER.error("RotationID of " + key + " in file " + filePath.toString() + " are missing");

										for (LineMovement lm : lines) {
//											LOGGER.error("ROT LineMovement: " + lm);
										}
									}
								}

								final List<LineMovement> templms = new ArrayList<LineMovement>();

								for (LineMovement lm : dbd.getLines()) {
									LOGGER.debug("LineMovement: " + lm);

									if (lm.getLinemovements() == null || lm.getLinemovements().isEmpty()) {
										LOGGER.error("LineMovement isEmpty: " + lm);
									}

									if (ncaafList != null && !ncaafList.isEmpty()) {
										boolean didFind = false;

										for (EspnForDonBestData efdbd : ncaafList) {
											final LocalDateTime eld = efdbd.getDateofgame();
											LOGGER.debug("LocalDateTime: " + eld);
											LOGGER.debug("Home: " + efdbd.getHometeam());
											LOGGER.debug("Visitor: " + efdbd.getVisitorteam());
											final List<MovementData> linemovements = lm.getLinemovements();

											if (linemovements != null && !linemovements.isEmpty()) {
												final MovementData md = linemovements.get(0);	
												final LocalDate ldt = md.getLineDate().toLocalDate();
												final LocalDate eldPlus1 = eld.plusDays(1).toLocalDate();
	
												if ((ldt.isBefore(eld.toLocalDate()) || ldt.isEqual(eld.toLocalDate())) ||
													(lm.getGameType().equals("second") &&
													 (ldt.isBefore(eldPlus1) || ldt.isEqual(eldPlus1)))) {
													String lvt = lm.getVisitorteam();
													String lht = lm.getHometeam();
													String evt = efdbd.getVisitorteam();
													String eht = efdbd.getHometeam();
	
													if ((lvt.equals(evt) && lht.equals(eht)) ||
														(lvt.equals(eht) && lht.equals(evt))) {
														didFind = true;
														String visitorTeam = lm.getVisitorteam();
														String homeTeam = lm.getHometeam();
														boolean found = false;
														Iterator<String> itr = NCAAFMapping.keySet().iterator();

														while (itr.hasNext()) {
															final String key = itr.next();

															if (key.toUpperCase().equals(visitorTeam.toUpperCase())) {
																found = true;
																lm.setVisitorteam(NCAAFMapping.get(key));
															}
														}
														if (!found) {
															LOGGER.error("visitorTeam not found: " + visitorTeam);
														}
														found = false;
														itr = NCAAFMapping.keySet().iterator();

														while (itr.hasNext()) {
															final String key = itr.next();

															if (key.toUpperCase().equals(homeTeam.toUpperCase())) {
																found = true;
																lm.setHometeam(NCAAFMapping.get(key));
															}
														}
														if (!found) {
															LOGGER.error("homeTeam not found: " + homeTeam);
														}

														lm.setEventDateTime(efdbd.getDateofgame());
														lm.setEventDate(efdbd.getDateofgame());
														lm.setYear(efdbd.getYear());
														lm.setWeek(efdbd.getWeek());
														final String id = (lm.getSportType() +
																lm.getGameType() +
																lm.getLineType() +
																lm.getHometeam() +
																lm.getEventDateTime().toString() +
																lm.getSportsbook()).replaceAll(" ", "");
														lm.setId(id);

														if (lm.getGameNumber() == 0) {
															lm.setVisitorScore(efdbd.getVisitorgamescore().toString());
															lm.setHomeScore(efdbd.getHomegamescore().toString());
														} else if (lm.getGameNumber() == 1) {
															lm.setVisitorScore(efdbd.getVisitor1hscore().toString());
															lm.setHomeScore(efdbd.getHome1hscore().toString());														
														} else if (lm.getGameNumber() == 2) {
															lm.setVisitorScore(efdbd.getVisitor2hscore().toString());
															lm.setHomeScore(efdbd.getHome2hscore().toString());														
														}

														nd.add(lm);
														templms.add(lm);
													}
												}											
											}
										}

										if (!didFind) {
											LOGGER.error("Did not find: " + lm);
										}
									}
								}

								final Comparator<MovementData> byLineDate = new Comparator<MovementData>() {
									public int compare(MovementData left, MovementData right) {
										final LocalDateTime leftDateTime = left.getLineDate();
										final LocalDateTime rightDateTime = right.getLineDate();

										if (leftDateTime.isAfter(rightDateTime)) {
											return -1;
										} else {
											return 1;
										}
									}
								};

								final List<String> uniqueGames = new ArrayList<String>();

								for (LineMovement lm : templms) {
									if (uniqueGames.isEmpty()) {
										uniqueGames.add(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam());
									} else {
										boolean found = false;
										for (String ug : uniqueGames) {
											if (!ug.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
												found = true;
											}
										}

										if (!found) {
											uniqueGames.add(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam());
										}
									}
								}

								for (int x = 0; x < uniqueGames.size(); x++) {
									final String uniqueGame = uniqueGames.get(x);
									final List<MovementData> spreads = new ArrayList<MovementData>();
									final List<MovementData> totals = new ArrayList<MovementData>();
									final List<MovementData> mls = new ArrayList<MovementData>();

									for (LineMovement lm : templms) {
										if (lm.getLineType().equals("spread") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
											final List<MovementData> mds = lm.getLinemovements();

											for (MovementData md : mds) {
												spreads.add(md);									
											}
										} else if (lm.getLineType().equals("total") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
											final List<MovementData> mds = lm.getLinemovements();

											for (MovementData md : mds) {
												totals.add(md);									
											}
										} else if (lm.getLineType().equals("moneyline") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
											final List<MovementData> mds = lm.getLinemovements();

											for (MovementData md : mds) {
												mls.add(md);									
											}
										}
									}

									if (!spreads.isEmpty()) {
										final List<MovementData> consensusSpreads = processSpreadConsensusLines(spreads, byLineDate);
										final LineMovement clm = setupLineMovement("spread", uniqueGame, templms);
										clm.setLinemovements(consensusSpreads);

										nd.add(clm);
									}

									if (!totals.isEmpty()) {
										final List<MovementData> consensusTotals = processConsensusTotals(totals, byLineDate);
										final LineMovement clm = setupLineMovement("total", uniqueGame, templms);
										clm.setLinemovements(consensusTotals);

										nd.add(clm);
									}

									if (!mls.isEmpty()) {
										final List<MovementData> consensusMls = processMlConsensusLines(mls, byLineDate);
										final LineMovement clm = setupLineMovement("moneyline", uniqueGame, templms);
										clm.setLinemovements(consensusMls);

										nd.add(clm);
									}
								}

				        			fin.close();
				            } catch (Throwable t) {
				            		LOGGER.error(t.getMessage(), t);
				            }
				        }
				    });
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return nd;
	}

	/**
	 * 
	 * @return
	 */
	public List<LineMovement> getSingleData() {
		final List<LineMovement> nd = new ArrayList<LineMovement>();

		try {
			// https://site.api.espn.com/apis/site/v2/sports/football/college-football/scoreboard?lang=en&region=us&calendartype=blacklist&limit=300&dates=2018&seasontype=2&week=1
			final EspnForDonBestSite efdbs = new EspnForDonBestSite("https://www.espn.com", "", "");
			efdbs.getHttpClientWrapper().setupHttpClient("None");
			List<EspnForDonBestData> ncaafList = efdbs.getNflData();

			try {
				try {
					String filePath = "/Users/jmiller/Documents/WoOTechnology/LineMovementData/NFLDBHistoricalLines/NFLFullGameSpreads090618.html";
					final InputStream fin = new FileInputStream(filePath);
					String data = "";
					int i = 0;

					do {
						byte[] buf = new byte[1024];
						i = fin.read(buf);

						String value = new String(buf, StandardCharsets.UTF_8);
						data += value;
					} while (i != -1);

					boolean isMl = false;
					int sportNumber = 1;
					String sportType = "nfl";
					int gameNumber = 0;
					String gameType = "";

					if (filePath.toString().contains("Moneyline")) {
						isMl = true;
					}

					if (filePath.toString().contains("FullGame")) {
						gameNumber = 0;
						gameType = "game";
					} else if (filePath.toString().contains("1stHalf")) {
						gameNumber = 1;
						gameType = "first";
					} else if (filePath.toString().contains("2ndHalf")) {
						gameNumber = 2;
						gameType = "second";
					}

					LOGGER.debug("filePath.toString(): " + filePath.toString());
					final DonBestData dbd = donbestparser.parseFootballData(isMl, sportNumber, sportType, gameNumber,
							gameType, data);

					List<LineMovement> quickCheck = dbd.getLines();
					LOGGER.error("quickCheck.size(): " + quickCheck.size());
					final Map<String, List<LineMovement>> games = new HashMap<String, List<LineMovement>>();

					for (LineMovement lm : quickCheck) {
						if (games.size() == 0) {
			            		List<LineMovement> lms = new ArrayList<LineMovement>();
			            		lms.add(lm);
							games.put(lm.getHomeRotationId() + lm.getLineType(), lms);
						} else {
							boolean didfind = false;

							for (Map.Entry<String, List<LineMovement>> entry : games.entrySet()) {
					            String key = entry.getKey();
					            List<LineMovement> values = entry.getValue();

					            if (key.equals(lm.getHomeRotationId() + lm.getLineType())) {
					            		values.add(lm);
					            		didfind = true;
					            }
							}

							if (!didfind) {
								List<LineMovement> lms = new ArrayList<LineMovement>();
								lms.add(lm);
								games.put(lm.getHomeRotationId() + lm.getLineType(), lms);
							}
						}
					}

					final Set<String> keys = games.keySet();
					for (String key : keys) {
						List<LineMovement> lines = games.get(key);

						if (lines.size() != 4) {
							LOGGER.error("lines.size(): " + lines.size());
							LOGGER.error("RotationID of " + key + " in file " + filePath.toString() + " are missing");

							for (LineMovement lm : lines) {
//								LOGGER.error("ROT LineMovement: " + lm);
							}
						}
					}

					final List<LineMovement> templms = new ArrayList<LineMovement>();
					LOGGER.error("dbd.getLines().size(): " + dbd.getLines().size());

					for (LineMovement lm : dbd.getLines()) {
						LOGGER.error(lm.getSportsbook() + " " + lm.getLineType() + " (" + lm.getVisitorRotationId() + ") " + lm.getVisitorteam() + "vs" + lm.getHometeam());

						if (lm.getLinemovements() == null || lm.getLinemovements().isEmpty()) {
							LOGGER.debug("LineMovement isEmpty: " + lm);
						}

						if (ncaafList != null && !ncaafList.isEmpty()) {
							boolean didFind = false;

							for (EspnForDonBestData efdbd : ncaafList) {
								final LocalDateTime eld = efdbd.getDateofgame();
								final List<MovementData> linemovements = lm.getLinemovements();

								if (linemovements != null && !linemovements.isEmpty()) {
									final MovementData md = linemovements.get(0);	
									final LocalDate ldt = md.getLineDate().toLocalDate();
									final LocalDate eldPlus1 = eld.plusDays(1).toLocalDate();

									if ((ldt.isBefore(eld.toLocalDate()) || ldt.isEqual(eld.toLocalDate())) ||
										(lm.getGameType().equals("second") &&
										 (ldt.isBefore(eldPlus1) || ldt.isEqual(eldPlus1)))) {
										String lvt = lm.getVisitorteam();
										String lht = lm.getHometeam();
										String evt = efdbd.getVisitorteam();
										String eht = efdbd.getHometeam();

										if ((lvt.equals(evt) && lht.equals(eht))
												|| (lvt.equals(eht) && lht.equals(evt))) {
											didFind = true;

											lm.setEventDateTime(efdbd.getDateofgame());
											lm.setEventDate(efdbd.getDateofgame());
											lm.setYear(efdbd.getYear());
											lm.setWeek(efdbd.getWeek());
											final String id = (lm.getSportType() +
													lm.getGameType() +
													lm.getLineType() +
													lm.getHometeam() +
													lm.getEventDateTime().toString() +
													lm.getSportsbook()).replaceAll(" ", "");
											lm.setId(id);

											if (lm.getGameNumber() == 0) {
												lm.setVisitorScore(efdbd.getVisitorgamescore().toString());
												lm.setHomeScore(efdbd.getHomegamescore().toString());
											} else if (lm.getGameNumber() == 1) {
												lm.setVisitorScore(efdbd.getVisitor1hscore().toString());
												lm.setHomeScore(efdbd.getHome1hscore().toString());
											} else if (lm.getGameNumber() == 2) {
												lm.setVisitorScore(efdbd.getVisitor2hscore().toString());
												lm.setHomeScore(efdbd.getHome2hscore().toString());
											}

											nd.add(lm);
											templms.add(lm);
										}
									}
								}
							}

							if (!didFind) {
								LOGGER.debug("Did not find: " + lm);
							}
						}
					}

					final Comparator<MovementData> byLineDate = new Comparator<MovementData>() {
						public int compare(MovementData left, MovementData right) {
							final LocalDateTime leftDateTime = left.getLineDate();
							final LocalDateTime rightDateTime = right.getLineDate();

							if (leftDateTime.isAfter(rightDateTime)) {
								return -1;
							} else {
								return 1;
							}
						}
					};

					final List<String> uniqueGames = new ArrayList<String>();

					for (LineMovement lm : templms) {
						if (uniqueGames.isEmpty()) {
							uniqueGames.add(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam());
						} else {
							boolean found = false;
							for (String ug : uniqueGames) {
								if (!ug.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
									found = true;
								}
							}

							if (!found) {
								uniqueGames.add(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam());
							}
						}
					}

					for (int x = 0; x < uniqueGames.size(); x++) {
						final String uniqueGame = uniqueGames.get(x);
						final List<MovementData> spreads = new ArrayList<MovementData>();
						final List<MovementData> totals = new ArrayList<MovementData>();
						final List<MovementData> mls = new ArrayList<MovementData>();

						for (LineMovement lm : templms) {
							if (lm.getLineType().equals("spread") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
								final List<MovementData> mds = lm.getLinemovements();

								for (MovementData md : mds) {
									spreads.add(md);									
								}
							} else if (lm.getLineType().equals("total") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
								final List<MovementData> mds = lm.getLinemovements();

								for (MovementData md : mds) {
									totals.add(md);									
								}
							} else if (lm.getLineType().equals("moneyline") && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
								final List<MovementData> mds = lm.getLinemovements();

								for (MovementData md : mds) {
									mls.add(md);									
								}
							}
						}

						if (!spreads.isEmpty()) {
							final List<MovementData> consensusSpreads = processSpreadConsensusLines(spreads, byLineDate);
							final LineMovement clm = setupLineMovement("spread", uniqueGame, templms);
							clm.setLinemovements(consensusSpreads);

							nd.add(clm);
						}

						if (!totals.isEmpty()) {
							final List<MovementData> consensusTotals = processConsensusTotals(totals, byLineDate);
							final LineMovement clm = setupLineMovement("total", uniqueGame, templms);
							clm.setLinemovements(consensusTotals);

							nd.add(clm);
						}

						if (!mls.isEmpty()) {
							final List<MovementData> consensusMls = processMlConsensusLines(mls, byLineDate);
							final LineMovement clm = setupLineMovement("moneyline", uniqueGame, templms);
							clm.setLinemovements(consensusMls);

							nd.add(clm);
						}
					}

					fin.close();
				} catch (Throwable t) {
					LOGGER.error(t.getMessage(), t);
				}
			} catch (Throwable t) {
				LOGGER.error(t.getMessage(), t);
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		return nd;
	}

	/**
	 * 
	 * @param uniqueGame
	 * @param templms
	 * @return
	 */
	private LineMovement setupLineMovement(String lineType, String uniqueGame, List<LineMovement> templms) {
		final LineMovement clm = new LineMovement();

		for (LineMovement lm : templms) {
			if (lineType.equals(lm.getLineType()) && uniqueGame.equals(lm.getVisitorRotationId() + lm.getVisitorteam() + lm.getHometeam())) {
				clm.setEventDate(lm.getEventDate());
				clm.setEventDateTime(lm.getEventDateTime());
				clm.setGameNumber(lm.getGameNumber());
				clm.setGameType(lm.getGameType());
				clm.setHomeRotationId(lm.getHomeRotationId());
				clm.setHomeScore(lm.getHomeScore());
				clm.setHometeam(lm.getHometeam());
				clm.setLineNumber(lm.getLineNumber());
				clm.setLineType(lm.getLineType());
				clm.setSiteName(lm.getSiteName());
				clm.setSportNumber(lm.getSportNumber());
				clm.setSportsbook("VI CONSENSUS LINE MOVEMENTS");
				clm.setSportType(lm.getSportType());
				clm.setVisitorRotationId(lm.getVisitorRotationId());
				clm.setVisitorScore(lm.getVisitorScore());
				clm.setVisitorteam(lm.getVisitorteam());
				clm.setYear(lm.getYear());
				break;
			}
		}

		return clm;
	}

	/**
	 * 
	 * @param mls
	 * @param byLineDate
	 * @return
	 */
	private List<MovementData> processSpreadConsensusLines(List<MovementData> mds, Comparator<MovementData> byLineDate) {
		final List<MovementData> consensusMds = new ArrayList<MovementData>();

		Collections.sort(mds, byLineDate);
		
		// Get first and last line move
		final MovementData mdfirst = mds.get(0);
		final MovementData mdlast = mds.get(mds.size() - 1);

		LocalDateTime mdfirstDateTime = mdfirst.getLineDate();
		LocalDateTime mdlastDateTime = mdlast.getLineDate();

		do {
			final List<MovementData> md1 = new ArrayList<MovementData>();

			for (MovementData mmd : mds) {
				final LocalDateTime currentDate = mmd.getLineDate();

				if ((currentDate.isAfter(mdlastDateTime) || currentDate.equals(mdlastDateTime)) && currentDate.isBefore(mdlastDateTime.plusMinutes(10))) {
					md1.add(mmd);
				}
			}

			if (!md1.isEmpty()) {
				boolean isfound = false;
				final MovementData md = new MovementData();
				double line1 = 0;
				double line2 = 0;
				double juice1 = 0;
				double juice2 = 0;
				int counter = 0;

				for (MovementData mmd : md1) {
					if (mmd.getLineDate().toString().equals("2018-08-24")) {
						isfound = true;
					}

					double tmpline1 = mmd.getLineone();
					double tmpline2 = mmd.getLinetwo();
					double tmpjuice1 = mmd.getJuiceone();
					double tmpjuice2 = mmd.getJuicetwo();

					line1 += tmpline1;
					line2 += tmpline2;

					if (counter == 0) {
						if (tmpjuice1 == 100) {
							juice1 = -100;
						} else {
							juice1 = tmpjuice1;
						}
					} else {
						if (tmpjuice1 >= 100) {
							double tmp1 = -(100 - (tmpjuice1 - 100));
							juice1 += tmp1;
						} else {
							juice1 += tmpjuice1;
						}
					}

					if (counter == 0) {
						if (tmpjuice2 == 100) {
							juice2 = -100;
						} else {
							juice2 = tmpjuice2;
						}
					} else {
						if (tmpjuice2 >= 100) {
							double tmp2 = -(100 - (tmpjuice2 - 100));
							juice2 += tmp2;
						} else {
							juice2 += tmpjuice2;
						}
					}

					if (isfound) {
						LOGGER.debug("tmpjuice1: " + tmpjuice1);
						LOGGER.debug("tmpjuice2: " + tmpjuice2);
						LOGGER.debug("juice1: " + juice1);
						LOGGER.debug("juice2: " + juice2);
					}

					counter++;
				}

				line1 = line1 / counter;
				line2 = line2 / counter;
				juice1 = juice1 / counter;
				juice2 = juice2 / counter;

				if (juice1 != 100 && juice1 >= -100) {
					juice1 = (juice1 + 100) + 100;
				}

				if (juice2 != 100 && juice2 >= -100) {
					juice2 = (juice2 + 100) + 100;
				}

				if (isfound) {
					LOGGER.debug("line1: " + line1);
					LOGGER.debug("line2: " + line2);
					LOGGER.debug("juice1: " + juice1);
					LOGGER.debug("juice2: " + juice2);
					LOGGER.debug("counter: " + counter);
				}

				String dline1 = checkDouble(line1, isfound);
				String dline2 = checkDouble(line2, isfound);
				String djuice1 = checkDouble(juice1, isfound);
				String djuice2 = checkDouble(juice2, isfound);

				if (dline1.startsWith("-")) {
					md.setLineindicator1("-");	
				} else {
					md.setLineindicator1("+");
				}

				if (dline2.startsWith("-")) {
					md.setLineindicator2("-");	
				} else {
					md.setLineindicator2("+");
				}

				md.setLineone(Double.parseDouble(dline1));
				md.setLinetwo(Double.parseDouble(dline2));
				
				if (djuice1.startsWith("-")) {
					md.setJuiceindicator1("-");	
				} else {
					md.setJuiceindicator1("+");
				}

				if (djuice2.startsWith("-")) {
					md.setJuiceindicator2("-");	
				} else {
					md.setJuiceindicator2("+");
				}

				md.setJuiceone(Double.parseDouble(djuice1));
				md.setJuicetwo(Double.parseDouble(djuice2));
				md.setLineDate(mdlastDateTime);
				md.setLineTime(mdlastDateTime);

				consensusMds.add(md);
			}

		    mdlastDateTime = mdlastDateTime.plusMinutes(10);
		} while (mdlastDateTime.isBefore(mdfirstDateTime));

		return consensusMds;
	}

	/**
	 * 
	 * @param lineValue
	 * @return
	 */
	private String checkDouble(Double lineValue, boolean isfound) {
		String numberStr = Double.toString(lineValue);
		int index = numberStr.indexOf('.');
		String fractionalStr = null;

		if (index != -1) {
			fractionalStr = numberStr.substring(index + 1);

			if (fractionalStr.length() > 5) {
				fractionalStr = fractionalStr.substring(0, 5);
			}
		} else {
			fractionalStr = "0";
		}

		int fractional = 0;

		if (fractionalStr.length() >= 2) {
			fractional = Integer.valueOf(fractionalStr.substring(0, 2));
		} else {
			fractional = Integer.valueOf(fractionalStr + "0");
		}
		if (isfound) {
			LOGGER.debug("fractional: " + fractional);
		}

		if (fractional >= 25 && fractional <= 75) {
			numberStr = numberStr.substring(0, numberStr.indexOf('.')) + ".5"; 
		} else if (fractional > 75) {
			if (lineValue < 0) {
				double number = Double.parseDouble(numberStr.substring(0, numberStr.indexOf('.'))) - 1;
				numberStr = Double.toString(number);				
			} else {
				double number = Double.parseDouble(numberStr.substring(0, numberStr.indexOf('.'))) + 1;
				numberStr = Double.toString(number);
			}
		} else if (fractional < 25) {
			double number = Double.parseDouble(numberStr.substring(0, numberStr.indexOf('.')));
			numberStr = Double.toString(number);
		}

		return numberStr;
	}

	/**
	 * 
	 * @param mls
	 * @param byLineDate
	 * @return
	 */
	private List<MovementData> processMlConsensusLines(List<MovementData> mds, Comparator<MovementData> byLineDate) {
		final List<MovementData> consensusMds = new ArrayList<MovementData>();

		Collections.sort(mds, byLineDate);

		// Get first and last line move
		final MovementData mdfirst = mds.get(0);
		final MovementData mdlast = mds.get(mds.size() - 1);
		LocalDateTime mdfirstDateTime = mdfirst.getLineDate();
		LocalDateTime mdlastDateTime = mdlast.getLineDate();

		do {
			final List<MovementData> md1 = new ArrayList<MovementData>();

			for (MovementData mmd : mds) {
				final LocalDateTime currentDate = mmd.getLineDate();

				if (currentDate.isAfter(mdlastDateTime) && currentDate.isBefore(mdlastDateTime.plusMinutes(10))) {
					md1.add(mmd);
				}
			}

			if (!md1.isEmpty()) {
				boolean isfound = false;
				final MovementData md = new MovementData();
				double line1 = 0;
				double line2 = 0;
				double juice1 = 0;
				double juice2 = 0;
				int counter = 0;

				for (MovementData mmd : md1) {
					double tmpline1 = mmd.getLineone();
					double tmpline2 = mmd.getLinetwo();
					double tmpjuice1 = mmd.getJuiceone();
					double tmpjuice2 = mmd.getJuicetwo();

					if (counter == 0) {
						if (tmpline1 == 100) {
							line1 = -100;
						} else {
							line1 = tmpline1;
						}
					} else {
						if (tmpline1 >= 100) {
							double tmp1 = -(100 - (tmpline1 - 100));
							line1 += tmp1;
						} else {
							line1 += tmpline1;
						}
					}

					if (counter == 0) {
						if (tmpline2 == 100) {
							line2 = -100;
						} else {
							line2 = tmpline2;
						}
					} else {
						if (tmpline2 >= 100) {
							double tmp2 = -(100 - (tmpline2 - 100));
							line2 += tmp2;
						} else {
							line2 += tmpline2;
						}
					}

					if (counter == 0) {
						line1 = tmpline1;
					} else {
						line1 = (line1 + tmpline1) / 2;

						if (line1 >= 0) {
							line1 += 100;
						} else if (line1 > -100) {
							line1 = line1 - 100;
						}
					}

					if (counter == 0) {
						line2 = tmpline2;
					} else {
						line2 = (line2 + tmpline2) / 2;

						if (line2 >= 0) {
							line2 += 100;
						} else if (line2 > -100) {
							line2 = line2 - 100;
						}
					}

					if (counter == 0) {
						if (tmpjuice1 == 100) {
							juice1 = -100;
						} else {
							juice1 = tmpjuice1;
						}
					} else {
						if (tmpjuice1 >= 100) {
							double tmp1 = -(100 - (tmpjuice1 - 100));
							juice1 += tmp1;
						} else {
							juice1 += tmpjuice1;
						}
					}

					if (counter == 0) {
						if (tmpjuice2 == 100) {
							juice2 = -100;
						} else {
							juice2 = tmpjuice2;
						}
					} else {
						if (tmpjuice2 >= 100) {
							double tmp2 = -(100 - (tmpjuice2 - 100));
							juice2 += tmp2;
						} else {
							juice2 += tmpjuice2;
						}
					}

					if (isfound) {
						LOGGER.debug("tmpjuice1: " + tmpjuice1);
						LOGGER.debug("tmpjuice2: " + tmpjuice2);
						LOGGER.debug("juice1: " + juice1);
						LOGGER.debug("juice2: " + juice2);
					}

					counter++;
				}

				line1 = line1 / counter;
				line2 = line2 / counter;
				juice1 = juice1 / counter;
				juice2 = juice2 / counter;

				if (line1 != 100 && line1 >= -100) {
					line1 = (line1 + 100) + 100;
				}

				if (line2 != 100 && line2 >= -100) {
					line2 = (line2 + 100) + 100;
				}

				if (juice1 != 100 && juice1 >= -100) {
					juice1 = (juice1 + 100) + 100;
				}

				if (juice2 != 100 && juice2 >= -100) {
					juice2 = (juice2 + 100) + 100;
				}

				if (isfound) {
					LOGGER.debug("line1: " + line1);
					LOGGER.debug("line2: " + line2);
					LOGGER.debug("juice1: " + juice1);
					LOGGER.debug("juice2: " + juice2);
					LOGGER.debug("counter: " + counter);
				}

				String dline1 = checkDouble(line1, isfound);
				String dline2 = checkDouble(line2, isfound);
				String djuice1 = checkDouble(juice1, isfound);
				String djuice2 = checkDouble(juice2, isfound);

				if (dline1.startsWith("-")) {
					md.setLineindicator1("-");	
				} else {
					md.setLineindicator1("+");
				}

				if (dline2.startsWith("-")) {
					md.setLineindicator2("-");	
				} else {
					md.setLineindicator2("+");
				}

				md.setLineone(Double.parseDouble(dline1));
				md.setLinetwo(Double.parseDouble(dline2));
				
				if (djuice1.startsWith("-")) {
					md.setJuiceindicator1("-");	
				} else {
					md.setJuiceindicator1("+");
				}

				if (djuice2.startsWith("-")) {
					md.setJuiceindicator2("-");	
				} else {
					md.setJuiceindicator2("+");
				}

				md.setJuiceone(Double.parseDouble(djuice1));
				md.setJuicetwo(Double.parseDouble(djuice2));
				md.setLineDate(mdlastDateTime);
				md.setLineTime(mdlastDateTime);

				consensusMds.add(md);
			}

		    mdlastDateTime = mdlastDateTime.plusMinutes(10);
		} while (mdlastDateTime.isBefore(mdfirstDateTime));

		return consensusMds;
	}

	/**
	 * 
	 * @param mls
	 * @param byLineDate
	 * @return
	 */
	private List<MovementData> processConsensusTotals(List<MovementData> mds, Comparator<MovementData> byLineDate) {
		final List<MovementData> consensusMds = new ArrayList<MovementData>();

		Collections.sort(mds, byLineDate);

		// Get first and last line move
		final MovementData mdfirst = mds.get(0);
		final MovementData mdlast = mds.get(mds.size() - 1);
		LocalDateTime mdfirstDateTime = mdfirst.getLineDate();
		LocalDateTime mdlastDateTime = mdlast.getLineDate();

		do {
			final List<MovementData> md1 = new ArrayList<MovementData>();

			for (MovementData mmd : mds) {
				final LocalDateTime currentDate = mmd.getLineDate();

				if (currentDate.isAfter(mdlastDateTime) && currentDate.isBefore(mdlastDateTime.plusMinutes(10))) {
					md1.add(mmd);
				}
			}

			if (!md1.isEmpty()) {
				boolean isfound = false;
				final MovementData md = new MovementData();
				double line1 = 0;
				double line2 = 0;
				double juice1 = 0;
				double juice2 = 0;
				int counter = 0;

				for (MovementData mmd : md1) {
					double tmpline1 = mmd.getLineone();
					double tmpline2 = mmd.getLinetwo();
					double tmpjuice1 = mmd.getJuiceone();
					double tmpjuice2 = mmd.getJuicetwo();
					
					line1 += tmpline1;
					line2 += tmpline2;

					if (counter == 0) {
						if (tmpjuice1 == 100) {
							juice1 = -100;
						} else {
							juice1 = tmpjuice1;
						}
					} else {
						if (tmpjuice1 >= 100) {
							double tmp1 = -(100 - (tmpjuice1 - 100));
							juice1 += tmp1;
						} else {
							juice1 += tmpjuice1;
						}
					}

					if (counter == 0) {
						if (tmpjuice2 == 100) {
							juice2 = -100;
						} else {
							juice2 = tmpjuice2;
						}
					} else {
						if (tmpjuice2 >= 100) {
							double tmp2 = -(100 - (tmpjuice2 - 100));
							juice2 += tmp2;
						} else {
							juice2 += tmpjuice2;
						}
					}

					if (isfound) {
						LOGGER.debug("tmpjuice1: " + tmpjuice1);
						LOGGER.debug("tmpjuice2: " + tmpjuice2);
						LOGGER.debug("juice1: " + juice1);
						LOGGER.debug("juice2: " + juice2);
					}

					counter++;
				}
				
				line1 = line1 / counter;
				line2 = line2 / counter;
				juice1 = juice1 / counter;
				juice2 = juice2 / counter;

				if (juice1 != 100 && juice1 >= -100) {
					juice1 = (juice1 + 100) + 100;
				}

				if (juice2 != 100 && juice2 >= -100) {
					juice2 = (juice2 + 100) + 100;
				}

				if (isfound) {
					LOGGER.debug("line1: " + line1);
					LOGGER.debug("line2: " + line2);
					LOGGER.debug("juice1: " + juice1);
					LOGGER.debug("juice2: " + juice2);
					LOGGER.debug("counter: " + counter);
				}

				String dline1 = checkDouble(line1, isfound);
				String dline2 = checkDouble(line2, isfound);
				String djuice1 = checkDouble(juice1, isfound);
				String djuice2 = checkDouble(juice2, isfound);
				
				md.setLineindicator1("o");
				md.setLineindicator2("u");
				md.setLineone(Double.parseDouble(dline1));
				md.setLinetwo(Double.parseDouble(dline2));
				
				if (djuice1.startsWith("-")) {
					md.setJuiceindicator1("-");	
				} else {
					md.setJuiceindicator1("+");
				}

				if (djuice2.startsWith("-")) {
					md.setJuiceindicator2("-");	
				} else {
					md.setJuiceindicator2("+");
				}

				md.setJuiceone(Double.parseDouble(djuice1));
				md.setJuicetwo(Double.parseDouble(djuice2));
				md.setLineDate(mdlastDateTime);
				md.setLineTime(mdlastDateTime);

				consensusMds.add(md);
			}

		    mdlastDateTime = mdlastDateTime.plusMinutes(10);
		} while (mdlastDateTime.isBefore(mdfirstDateTime));

		return consensusMds;
	}

	/**
	 * 
	 * @param proxy
	 */
	public void setProxy(String proxy) {
		LOGGER.info("Entering setProxy()");

		try {
			this.httpClientWrapper.setupHttpClient(proxy);
		} catch (BatchException be) {
			LOGGER.error(be.getMessage(), be);
		}

		LOGGER.info("Exiting setProxy()");
	}

	@Override
	public SiteTransaction createSpreadTransaction(SiteTeamPackage siteTeamPackage,
			SpreadRecordEvent spreadRecordEvent, int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createTotalTransaction(SiteTeamPackage siteTeamPackage, TotalRecordEvent totalRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiteTransaction createMoneyLineTransaction(SiteTeamPackage siteTeamPackage, MlRecordEvent mlRecordEvent,
			int arrayNum, float juice, AccountEvent accountEvent) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SiteTransaction createSiteTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String selectSport(String type) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseEventSelection(String xhtml, SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String selectEvent(SiteTransaction siteTransaction, EventPackage eventPackage, BaseRecordEvent event,
			AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String completeTransaction(SiteTransaction siteTransaction, EventPackage eventPackage,
			BaseRecordEvent event, AccountEvent ae) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String parseTicketTransaction(String xhtml) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String loginToSite(String username, String password) throws BatchException {
		// TODO Auto-generated method stub
		return null;
	}
}
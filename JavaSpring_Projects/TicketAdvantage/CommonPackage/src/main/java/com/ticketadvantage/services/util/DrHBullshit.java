/**
 * 
 */
package com.ticketadvantage.services.util;

import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.model.EmailEvent;
import com.ticketadvantage.services.model.EventContainer;
import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.EventsPackage;
import com.ticketadvantage.services.model.TeamPackage;

/**
 * @author jmiller
 *
 */
public class DrHBullshit {
	private static final Logger LOGGER = Logger.getLogger(DrHBullshit.class);
	EventsPackage eventsPackage;
	EmailEvent theEmail;
	
	private final String[][] LEVEL_1 = new String[][] {
		{"versus", "vs"},
		{"against", "vs"},
		{"v.", "vs"},
		{" v ", " vs "},
		{" 1/2", ".5"},
		{" .5 ", ".5"},
		{"one", "1"},
		{"two", "2"},
		{"tw0", "2"},
		{"three", "3"},
		{"thr33", "3"},
		{"four", "4"},
		{"f0ur", "4"},	
		{"five", "5"},
		{"f1ve", "5"},
		{"six", "6"},
		{"s1x", "6"},
		{"seven", "7"},
		{"s3v3n", "7"},
		{"eight", "8"},
		{"3ight", "8"},
		{"nine", "9"},
		{"n1n3", "9"},
		{"zero", "0"},
		{"z3r0", "0"},
		{"ten", "10"},
		{"t3n", "10"},
		{"eleven", "11"},
		{"3l3v3n", "11"},
		{"twelve", "12"},
		{"tw3lv3", "12"},
		{"tw31v3", "12"},
		{"thirteen", "13"},
		{"thirt33n", "13"},
		{"fourteen", "14"},
		{"fourt33n", "14"},
		{"fifteen", "15"},
		{"fift33n", "15"},
		{"sixteen", "16"},
		{"sixt33n", "16"},
		{"seventeen", "17"},
		{"sevent33n", "17"},
		{"eighteen", "18"},
		{"eight33n", "18"},
		{"3ight33n", "18"},
		{"nineteen", "19"},
		{"n1n3t33n", "19"},
		{"twenty", "20"},
		{"tw3nty", "20"},
		{"thirty", "30"},
		{"th1rty", "30"},
		{"forty", "40"},
		{"f0rty", "40"},
		{"fifty", "50"},
		{"f1fty", "50"},
		{"sixty", "60"},
		{"s1xty", "60"},
		{"seventy", "70"},
		{"s3v3nty", "70"},
		{"eighty", "80"},
		{"3ighty", "80"},
		{"ninety", "90"},
		{"n1n3ty", "90"},
		{"oh", "0"},
		{"rot #", ""},
		{"rot :", ""},
		{"rot:", ""}
	};

	private final String[][] REPLACE_WORDS = new String[][] {
		{"then", "than"},
		{"above than", "over"},
		{"above", "over"},
		{"greater than", "over"},
		{"larger than", "over"},
		{"higher than", "over"},
		{"more than", "over"},
		{"plus than", "over"},
		{">", "over"},
		{"less than", "under"},
		{"smaller than", "under"},
		{"lower than", "under"},
		{"minus than", "under"},
		{"below than", "under"},
		{"below", "under"},
		{"beneath than", "under"},
		{"beneath", "under"},
		{"underneath than", "under"},
		{"underneath", "under"},
		{"against", "vs"},
		{"<", "under"},
		{"()", "O"},
		{"plus", "+"},
		{"puls", "+"},
		{"minus", "-"},
		{"munis", "-"},
		{"(", ""},
		{")", ""},
		{"'", ""},
		{"runs", ""}
	};

	private final String[][] TEAM_WORDS = new String[][] {
		{"nym", "new york mets"},
		{"sdp", "san diego padres"},
		{"nyy", "new york yankees"},
		{"lad", "los angeles dodgers"},
		{"laa", "los angeles angels"},
		{"kcr", "kansas city royals"},
		{"sfg", "san francisco giants"}
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String line = "kjklj;8.5nnnjknlk";
		line = line.replaceAll("[^.0-9]", "");
		LOGGER.error("line: " + line);

		DrHBullshit ueb = new DrHBullshit();
//		EventContainer ec = ueb.parseTokens("Estr4adaa/More greater than ten minus one oh five\n");
//		EventContainer ec = ueb.parseTokens("M!+CH3LL/COAL below 8.5 -105");
//		EventContainer ec = ueb.parseTokens("Fires/Lo-pez less than 9 minus 120");
//		EventContainer ec = ueb.parseTokens("Derg0m minus one forty versus T3h3r4n");
//		EventContainer ec = ueb.parseTokens("H0LLLADN/R1CHardZ over 8.5 -105");
//		EventContainer ec = ueb.parseTokens("Davrishh -124 v 4nd3rs0n");
//		EventContainer ec = ueb.parseTokens("SD P4DR3S +180");
//		EventContainer ec = ueb.parseTokens("Baylee/Matrin3z ovr 8 +100");
//		EventContainer ec = ueb.parseTokens("Ham3l/N0RR15 und 9 -110");
//		EventContainer ec = ueb.parseTokens("911 Rosss +130");
//		EventContainer ec = ueb.parseTokens("New-combe +140 against Synndergard");
//		EventContainer ec = ueb.parseTokens("Garvemann +130 v Pomrenanz");
//		EventContainer ec = ueb.parseTokens("Stratonn plus 1 forty v. Rayy");
//		EventContainer ec = ueb.parseTokens("Gasman/Boid above 9 -105");
//		EventContainer ec = ueb.parseTokens("Malhe|Daveiz higher then 8.5 -105");
//		EventContainer ec = ueb.parseTokens("G4RC14 -1O2 against H4M.ELS");
//		EventContainer ec = ueb.parseTokens("H4PPY -180 vs K3N3Dy");
//		EventContainer ec = ueb.parseTokens("Pirce/Othani OV 7.5 -105");
//		EventContainer ec = ueb.parseTokens("N1N3-7EVEN-4OUR Cay-hill below 9 minus 100");
//		EventContainer ec = ueb.parseTokens("P1v3TTT4/Foteylnewizc UNDER 8.5 +105");
//		EventContainer ec = ueb.parseTokens("G Gonzlaez -105");
//		EventContainer ec = ueb.parseTokens("brualtt -125");
//		EventContainer ec = ueb.parseTokens("R!5HARDS -150 (Joonis)");
//		EventContainer ec = ueb.parseTokens("960 UNDER 8,5 -115");
//		EventContainer ec = ueb.parseTokens("C0LoR_4DO/Wash.ing.ton above 8.5 runs -105");
//		EventContainer ec = ueb.parseTokens("Baley v No-La und 8 -110");
//		EventContainer ec = ueb.parseTokens("G'onzalesz plus 130 v Linn");
//		EventContainer ec = ueb.parseTokens("Fires/Lo-pez less than 9 minus 120");
//		EventContainer ec = ueb.parseTokens("M|TCH3LL plus 1 oh 2 vs B3TT15");
//		EventContainer ec = ueb.parseTokens("#N1N3-2WO-N1N3 OVER 8.5 -115");
//		EventContainer ec = ueb.parseTokens("Lucheesi plus 1fifty5 (Cobrin)");
//		EventContainer ec = ueb.parseTokens("Chi Cubbies (150)");
//		EventContainer ec = ueb.parseTokens("Bailey plus 117");
//		EventContainer ec = ueb.parseTokens("Lynne -108");
//		EventContainer ec = ueb.parseTokens("NYM/SDP less than 8 1/2 (-105)");
//		EventContainer ec = ueb.parseTokens("C4$HN3R -120 versus Liraino");
//		EventContainer ec = ueb.parseTokens("905/906 UNDER 8.5 -105");
//		EventContainer ec = ueb.parseTokens("N1N3-T3N Smi+h + 125");
//		EventContainer ec = ueb.parseTokens("Marl|ns +122");
//		EventContainer ec = ueb.parseTokens("Rot # 969 OVER 7.5 -115");
//		EventContainer ec = ueb.parseTokens("Sail -185 v Estarda");
//		EventContainer ec = ueb.parseTokens("Packston +106 (Clevingr)");
//		EventContainer ec = ueb.parseTokens("9O| Newcome -128");
//		EventContainer ec = ueb.parseTokens("#N1N3-2WO-N1N3 OVER 8.5 -115");
//		EventContainer ec = ueb.parseTokens("Lucheesi plus 1fifty5 (Cobrin)");
		EventContainer ec = ueb.parseTokens("H4PPY -180 vs K3N3Dy");
		
		LOGGER.debug("ec: " + ec);

//		ueb.EVENTSDB.getAllEvents();
//		ueb.eventsPackage = ueb.EVENTSDB.getNextDaySport("mlblines");
//		EmailEvent ee = ueb.parseTokens("Matrin3z/Keneddy OVER 8.5 -115");
//		EventPackage ep = ueb.determineEvent(ee);

/*		
		UserEmailBatch ueb = new UserEmailBatch();
		EmailEvent ee = ueb.parseTokens("Duffy -105 versus Hamels");
		ee.setBody("Duffy -105 versus Hamels");
		ee.setLinetype("game");
		ee.setGamesport("Baseball");
		ee.setGametype("MLB Baseball");
		ee.setUserid(new Long(1));
		ee.setPendingtype("user");

		// Now determine which game (if any)
		ee = ueb.determineEvent(ee);
		LOGGER.debug("ee: " + ee);

		UserEmailBatch ueb = new UserEmailBatch();
		// WH33LER +131 vs WIANWRIHGT
		// SM1+H plus 152 vs M()()R3
		// Strialy/mo0r3 above 8 -120
		// N()LASC0 v HAM3LZ above 9.5 -120
		// Fotlynewizc +165 vs Gonzaelz
		// C3UT()/SANHC3Z over 9.5 -115
		// K03LHER +145 v WAHCA
		// PACKSTON -135 vs CHAV3Z
		// STRAILEY -115 vs GUERA
		// C()BB/GUASMAN above 10 -115
		// N()RISS -153 vs BALCH
		// F3|DMAN/GREY UDNER 12 -115
		final Scrapper scrapper = new Scrapper();
		scrapper.setMlmaxamount("0");
		scrapper.setMllineadjustment("5");
		scrapper.setMlonoff(true);
		scrapper.setTotalmaxamount("0");
		scrapper.setTotallineadjustment("5");
		scrapper.setTotalonoff(true);
		scrapper.setOnoff(true);
		scrapper.setPullinginterval("1");
		scrapper.setScrappername("TAEmail");
		scrapper.setUserid(new Long(1));
		//scrapper.setTelegramnumber("7852173453@txt.att.net");
		scrapper.setTelegramnumber("9132195234@vtext.com");
		scrapper.setId(new Long(1));
		Set<Accounts> destinations = new HashSet<Accounts>();
		Accounts account1 = new Accounts();
		account1.setSitetype("MetallicaMobile");
		account1.setUsername("kwd2202");
		account1.setPassword("jh");
		account1.setUrl("http://m.iron69.com");
		account1.setIsactive(true);
		account1.setTimezone("CT");
		destinations.add(account1);
		scrapper.setDestinations(destinations);
		ueb.scrapper = scrapper;

//		EmailEvent ee = ueb.parseTokens("F3|DMAN/GREY UDNER 12 -115");
//		System.out.println("EmailEvent: " + ee);
		try {
			ueb.conn.setAutoCommit(true);
			ueb.EMAILEVENTDB.setConn(ueb.conn);
			ueb.EVENTSDB.setConn(ueb.conn);
			ueb.RECORDEVENTDB.setConn(ueb.conn);

			ueb.retrieveEmail.connect();
			// boolean found = ueb.checkForWarning();
			boolean found = true;
			if (found) {
				ueb.EVENTSDB.getAllEvents();
				final List<EmailContainer> emails = ueb.retrieveEmail.getEmail();
				for (EmailContainer email : emails) {
					LOGGER.debug("EmailContainer: " + email);
					String body = email.getBody();
					if (!body.contains("plays in about")) {
						try {
							// Parse the data
							EmailEvent ee = ueb.parseTokens(body);
							ee.setBody(body);
							ee.setFromemail(email.getFrom());
							ee.setSubject(email.getSubject());
							ee.setDatecreated(email.getDateReceived());
							ee.setDatesent(email.getDateSent());
							ee.setToemail(email.getTo());
							ee.setLinetype("game");
							ee.setGamesport("Baseball");
							ee.setGametype("MLB Baseball");
							ee.setUserid(new Long(1));
							ee.setPendingtype("user");
	
							// Now determine which game (if any)
							ee = ueb.determineEvent(ee);
							if (ee.getFoundgame()) {
								ueb.placeEmailTransaction(scrapper, ee);
							}
							ueb.EMAILEVENTDB.persist(ee);
						} catch (Throwable t) {
							t.printStackTrace();
						}
					}
				}
			} else {
				// Close the email since we are going to wait a while
				ueb.retrieveEmail.close();
			}
			ueb.EMAILEVENTDB.complete();
			ueb.EVENTSDB.complete();
			ueb.RECORDEVENTDB.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	}

	/**
	 * 
	 * @param body
	 * @return
	 */
	public EventContainer parseTokens(String body) {
		LOGGER.info("Entering parseTokens()");
		EventContainer eventContainer = null;

		try {
			if (body != null && body.length() > 200) {
				return null;
			}
			body = body.toLowerCase();
			LOGGER.debug("body: " + body);
		
			// Replace what we can
			for (int x = 0; x < LEVEL_1.length; x++) {
				body = body.replace(LEVEL_1[x][0], LEVEL_1[x][1]);
			}
			body = body.trim();
			LOGGER.debug("body: " + body);

			int index1 = body.indexOf("minus");
			if (index1 != -1) {
				String tempBody = body.substring(index1);
				int index2 = tempBody.indexOf("vs");
				if (index2 != -1) {
					String nbody = tempBody.substring(0, index2);
					nbody = nbody.replace(" ", "");
					body = body.substring(0, index1) + nbody + " " + tempBody.substring(index2);
				} else {
					tempBody = body.substring(index1).replace(" ", "");
					body = body.substring(0, index1) + " " + tempBody;
				}
			} else {
				index1 = body.indexOf("plus");
				if (index1 != -1) {
					String tempBody = body.substring(index1);
					int index2 = tempBody.indexOf("vs");
					if (index2 != -1) {
						String nbody = tempBody.substring(0, index2);
						nbody = nbody.replace(" ", "");
						body = body.substring(0, index1) + nbody + " " + tempBody.substring(index2);
					} else {
						tempBody = body.substring(index1).replace(" ", "");
						body = body.substring(0, index1) + " " + tempBody;
					}
				}
			}
			LOGGER.debug("body2: " + body);

			// Replace what we can
			for (int x = 0; x < REPLACE_WORDS.length; x++) {
				body = body.replace(REPLACE_WORDS[x][0], REPLACE_WORDS[x][1]);
			}
			LOGGER.debug("body: " + body);

			// parse the body
			final StringTokenizer st = new StringTokenizer(body," ");
			int tcount = 0;
			boolean ml = false;
			boolean total = false;
			// Sail/Nolacso ABOVE 7 minus one twenty
			// Sail/Nolacso ABOVE 7 - 1 20
			// Davrish/Kobb over 7.5 -110
			// Chaavez -112 vs Jaxson
			// Newcombe +110 (Lacky)
			// WH33LER +131 vs WIANWRIHGT
			// SM1+H plus 152 vs M()()R3
			// Strialy/mo0r3 above 8 -120
			// N()LASC0 v HAM3LZ above 9.5 -120
			// Fotlynewizc +165 vs Gonzaelz
			// C3UT()/SANHC3Z over 9.5 -115
			// K03LHER +145 v WAHCA
			// PACKSTON -135 vs CHAV3Z
			// STRAILEY -115 vs GUERA
			// C()BB/GUASMAN above 10 -115
			// N()RISS -153 vs BALCH
			// F3|DMAN/GREY UDNER 12 -115
			// Zimerman/Vragas > 9.5 -105
			// Wianwright/Wheelr < 9 +110
			boolean longParse = false;
			eventContainer = new EventContainer();

			int size = st.countTokens();
			LOGGER.debug("size: " + size);
			while (st.hasMoreTokens()) {
				if (size == 2) {
					String token = st.nextToken().toLowerCase();
					if (tcount == 0) {
						token = token.replace(".", "");
						eventContainer.setTeam1(token);
						eventContainer.setPitcher(token);
					} else {
						ml = true;
						eventContainer.setEventtype("ml");
						eventContainer.setJuiceplusminus(token.substring(0, 1));
						String line = token.substring(1);
						line = line.replaceAll("[^.0-9]", "");
						eventContainer.setJuice(line);
						eventContainer.setLineplusminus(token.substring(0, 1));
						eventContainer.setLine(line);						
					}
				} else if (size == 3) {
					String token = st.nextToken().toLowerCase();
					if (tcount == 0) {
						token = token.replace(".", "");
						eventContainer.setTeam1(token);
					} else if (tcount == 1) {
						ml = true;
						token = token.replace("o", "0");
						token = token.replace("l", "1");

						if (token.contains("-") || token.contains("+")) {
							eventContainer.setEventtype("ml");
							eventContainer.setJuiceplusminus(token.substring(0, 1));
							String line = token.substring(1);
							line = line.replaceAll("[^.0-9]", "");
							eventContainer.setJuice(line);
							eventContainer.setLineplusminus(token.substring(0, 1));
							eventContainer.setLine(line);
						} else {
							eventContainer.setTeam2(token);
						}
					} else if (tcount == 2) {
						token = token.replace(".", "");
						if (token.contains("-") || token.contains("+")) {
							eventContainer.setEventtype("ml");
							eventContainer.setJuiceplusminus(token.substring(0, 1));
							String line = token.substring(1);
							line = line.replaceAll("[^.0-9]", "");
							eventContainer.setJuice(line);
							eventContainer.setLineplusminus(token.substring(0, 1));
							eventContainer.setLine(line);
						} else {
							eventContainer.setTeam2(token);
						}
					}
				} else if (size == 4) {
					// #N1N3-2WO-N1N3 OVER 8.5 -115
					// n1n3-t3n smi+h + 125
					// sail -185 vs estarda
					String token = st.nextToken().toLowerCase();
					LOGGER.debug("token: " + token);
					LOGGER.debug("tcount: " + tcount);

					if (tcount == 0 && token.contains("/")) {
						eventContainer.setEventtype("total");

						// Total
						// Parse Pitcher
						int index = token.indexOf("/");
						if (index != -1) {
							token = token.replace(".", "");
							String pitcher1 = token.substring(0, index);
							String pitcher2 = token.substring(index + 1);
							eventContainer.setTeam1(pitcher1);
							eventContainer.setTeam2(pitcher2);
						}
					} else if (tcount == 0 && token.contains("|") && (token.contains("und") || token.contains("ov"))) {
						eventContainer.setEventtype("total");

						// First check if it might be a rotation ID
						String tempId = token;
						tempId = tempId.replace("1", "i");
						tempId = tempId.replace("0", "o");
						tempId = tempId.replace("3", "e");
						tempId = tempId.replace("4", "f");
						tempId = tempId.replace("7", "s");
						tempId = tempId.replace("2", "t");
						tempId = tempId.replaceAll("[^a-zA-Z0-9]", "");
						// Replace what we can
						for (int x = 0; x < LEVEL_1.length; x++) {
							tempId = tempId.replace(LEVEL_1[x][0], LEVEL_1[x][1]);
						}
						LOGGER.debug("tempId: " + tempId);

						boolean isRotation = false;
						try {
							Integer.parseInt(tempId);
							isRotation = true;
						} catch (Throwable t) {
							LOGGER.debug(t.getMessage(), t);
						}

						if (isRotation) {
							eventContainer.setTeam1(tempId);
						} else {
							// Total
							// Parse Pitcher
							int index = token.indexOf("|");
							if (index != -1) {
								token = token.replace(".", "");
								String pitcher1 = token.substring(0, index);
								String pitcher2 = token.substring(index + 1);
								eventContainer.setTeam1(pitcher1);
								eventContainer.setTeam2(pitcher2);
							} else {
								eventContainer.setTeam1(token);
							}
						}
					} else if (tcount == 0 && (body.contains("un") || body.contains("ov"))) {
						eventContainer.setEventtype("total");

						// First check if it might be a rotation ID
						String tempId = token;
						tempId = tempId.replace("1", "i");
						tempId = tempId.replace("0", "o");
						tempId = tempId.replace("3", "e");
						tempId = tempId.replace("4", "f");
						tempId = tempId.replace("7", "s");
						tempId = tempId.replace("2", "t");
						tempId = tempId.replaceAll("[^a-zA-Z0-9]", "");
						// Replace what we can
						for (int x = 0; x < LEVEL_1.length; x++) {
							tempId = tempId.replace(LEVEL_1[x][0], LEVEL_1[x][1]);
						}
						LOGGER.debug("tempId: " + tempId);

						boolean isRotation = false;
						try {
							Integer.parseInt(tempId);
							isRotation = true;
						} catch (Throwable t) {
							LOGGER.debug(t.getMessage(), t);
						}

						if (isRotation) {
							eventContainer.setTeam1(tempId);
						} else {
							// Total
							// Parse Pitcher
							int index = token.indexOf("|");
							if (index != -1) {
								token = token.replace(".", "");
								String pitcher1 = token.substring(0, index);
								String pitcher2 = token.substring(index + 1);
								eventContainer.setTeam1(pitcher1);
								eventContainer.setTeam2(pitcher2);
							} else {
								eventContainer.setTeam1(token);
							}
						}
					} else if (tcount == 0) {
						eventContainer.setEventtype("ml");
						ml = true;

						// First Pitcher
						token = token.replace(".", "").toLowerCase();
						eventContainer.setTeam1(token);
					} else if (tcount == 1 && (token.startsWith("-") || token.startsWith("+"))) {
						LOGGER.debug("token: " + token);
						// Money Line
						ml = true;
						token = token.replace("o", "0");
						token = token.replace("l", "1");

						eventContainer.setEventtype("ml");
						eventContainer.setJuiceplusminus(token.substring(0, 1));
						String line = token.substring(1);
						line = line.replaceAll("[^.0-9]", "");
						eventContainer.setJuice(line);
						eventContainer.setLineplusminus(token.substring(0, 1));
						eventContainer.setLine(line);
					} else if (tcount == 1 && (token.contains("u") || token.contains("U") || token.contains("o") || token.contains("O"))) {
						// Over/Under value
						if (token.contains("u") || token.contains("U")) {
							// Under
							eventContainer.setLineplusminus("u");
						} else {
							// Over
							eventContainer.setLineplusminus("o");
						}
					} else if (tcount == 1) {
						eventContainer.setTeam2(token);
						eventContainer.setPitcher2(token);
					} else if (tcount == 2 && !ml) {
						// over/under value
						token = token.replace("o", "0");
						token = token.replace("l", "1");
						token = token.replace(",", ".");

						String line = token;
						line = line.replaceAll("[^.0-9]", "");
						eventContainer.setLine(line);
					} else if (tcount == 2) {
						if (token.contains("vs")) {
							// Do nothing
						} else {
							eventContainer.setLineplusminus(token);
							eventContainer.setJuiceplusminus(token);
						}
					} else if (tcount == 3 && ml) {
						boolean isnumber = false;
						try {
							Integer.parseInt(token);
							isnumber = true;
						} catch (Throwable t) {
							LOGGER.debug(t.getMessage(), t);
						}
						if (isnumber) {
							eventContainer.setLine(token);
							eventContainer.setJuice(token);
						} else {
							// Second Pitcher
							token = token.replace(".", "");
							eventContainer.setTeam2(token);
						}
					} else if (tcount == 3) {
						// Juice over/under
						eventContainer.setJuiceplusminus(token.substring(0, 1));
						String line = token.substring(1);
						line = line.replaceAll("[^.0-9]", "");
						eventContainer.setJuice(line);
					} else {
						// Unknown
					}
				} else if (size == 5) {
					// SM1+H plus 152 vs M()()R3
					// Zimerman/Vragas larger than 9.5 -105
					// NYM/SDP under 8 1/2 (-105)
					String token = st.nextToken().toLowerCase();
					if (tcount == 0) {
						if (body.contains("under") || body.contains("over")) {
							total = true;
							eventContainer.setEventtype("total");
						} else {
							ml = true;
							eventContainer.setEventtype("ml");
						}
						// First check if it might be a rotation ID
						String tempId = token;
						tempId = tempId.replace("1", "i");
						tempId = tempId.replace("0", "o");
						tempId = tempId.replace("3", "e");
						tempId = tempId.replace("4", "f");
						tempId = tempId.replace("7", "s");
						tempId = tempId.replace("2", "t");
						tempId = tempId.replaceAll("[^a-zA-Z0-9]", "");

						// Replace what we can
						for (int x = 0; x < LEVEL_1.length; x++) {
							tempId = tempId.replace(LEVEL_1[x][0], LEVEL_1[x][1]);
						}

						boolean isRotation = false;
						try {
							Integer.parseInt(tempId);
							isRotation = true;
						} catch (Throwable t) {
							LOGGER.debug(t.getMessage(), t);
						}

						if (isRotation) {
							eventContainer.setTeam1(tempId);
						} else {
							int index = token.indexOf("/");
							if (index != -1) {
								eventContainer.setTeam1(token.substring(0, index));
								eventContainer.setTeam2(token.substring(index + 1));
							} else {
								eventContainer.setTeam1(token);
							}
						}
					} else if (tcount == 1) {
						if (total) {
							if (token.startsWith("u") || token.startsWith("o")) {
								if (token.startsWith("u")) {
									eventContainer.setLineplusminus("u");
								} else {
									eventContainer.setLineplusminus("o");
								}
							} else {
								eventContainer.setTeam2(token);
							}
						} else {
							eventContainer.setLineplusminus(token);
							eventContainer.setJuiceplusminus(token);
						}
					} else if (tcount == 2) {
						if (total) {
							if (token.startsWith("u") || token.startsWith("o")) {
								if (token.startsWith("u")) {
									eventContainer.setLineplusminus("u");
								} else {
									eventContainer.setLineplusminus("o");
								}
							} else {
								String line = token;
								line = line.replaceAll("[^.0-9]", "");
								eventContainer.setLine(line);
							}
						} else {
							String line = token;
							line = line.replaceAll("[^.0-9]", "");
							eventContainer.setLine(line);
							eventContainer.setJuice(line);
						}
					} else if (tcount == 3) {
						if (total) {
							if (token.startsWith("u") || token.startsWith("o")) {
								eventContainer.setJuiceplusminus(token);
							} else {
								String line = token;
								line = line.replaceAll("[^.0-9]", "");
								eventContainer.setLine(line);
							}
						}
					} else if (tcount == 4) {
						if (total) {
							if (token.startsWith("-") || token.startsWith("+")) {
								// Juice over/under
								eventContainer.setJuiceplusminus(token.substring(0, 1));
								eventContainer.setJuice(token.substring(1));
							} else {
								String line = token;
								line = line.replaceAll("[^.0-9]", "");
								eventContainer.setJuice(line);
							}
						} else {
							token = token.replace(".", "");
							eventContainer.setTeam2(token);
						}
					} else {
						// Unknown
					}
				} else if (size == 6) {
					// N()LASC0 v HAM3LZ above 9.5 -120
					// Sail/Nolacso ABOVE 7 minus one twenty
					// derg0m minus one forty versus t3h3r4n
					String token = st.nextToken().toLowerCase();
					if (tcount == 0) {
						int index = token.indexOf("/");
						if (index != -1) {
							eventContainer.setEventtype("total");
							token = token.replace(".", "");
							String pitcher1 = token.substring(0, index);
							String pitcher2 = token.substring(index + 1);
							eventContainer.setTeam1(pitcher1);
							eventContainer.setTeam2(pitcher2);
							longParse = true;
						} else {
							index = token.indexOf("|");
							if (index != -1) {
								eventContainer.setEventtype("total");
								token = token.replace(".", "");
								String pitcher1 = token.substring(0, index);
								String pitcher2 = token.substring(index + 1);
								eventContainer.setTeam1(pitcher1);
								eventContainer.setTeam2(pitcher2);
								longParse = true;
							} else {
								if (body.contains("und") || body.contains("ov")) {
									eventContainer.setEventtype("total");
									// First Pitcher
									token = token.replace(".", "");
									eventContainer.setTeam1(token);									
								} else if (body.contains("+") || body.contains("-")) {
									eventContainer.setEventtype("ml");
								} else {
									eventContainer.setEventtype("total");
									// First Pitcher
									token = token.replace(".", "");
									eventContainer.setTeam1(token);
								}
							}
						}
					} else if (tcount == 1) {
						if (longParse) {
							if (token.contains("o")) {
								eventContainer.setLineplusminus("o");						
							} else {
								eventContainer.setLineplusminus("u");
							}
						} else {
							eventContainer.setJuiceplusminus(token);
						}
					} else if (tcount == 2) {
						// Second Pitcher
						if (longParse) {
							String line = token;
							line = line.replaceAll("[^.0-9]", "");
							eventContainer.setLine(line);
						} else {
							token = token.replace(".", "");
							eventContainer.setTeam2(token);
						}
					} else if (tcount == 3) {
						if (longParse) {
							if (token.length() == 1) {
								eventContainer.setJuiceplusminus(token.substring(0, 1));
							} else {
								if (token.startsWith("m")) {
									eventContainer.setJuiceplusminus("-");
								} else {
									eventContainer.setJuiceplusminus("+");
								}
							}
						} else {
							if (token.contains("o")) {
								eventContainer.setLineplusminus("o");
							} else {
								eventContainer.setLineplusminus("u");
							}
						}
					} else if (tcount == 4) {
						if (longParse) {
							String line = token;
							line = line.replaceAll("[^.0-9]", "");
							eventContainer.setJuice(line);
						} else {
							String line = token;
							line = line.replaceAll("[^.0-9]", "");
							eventContainer.setLine(line);
						}
					} else if (tcount == 5) {
						if (longParse) {
							eventContainer.setJuice(eventContainer.getJuice() + token);
						} else {
							// Juice over/under
							eventContainer.setJuiceplusminus(token.substring(0, 1));
							eventContainer.setJuice(token.substring(1));
						}
					} else {
						// Unknown
					}
				} else if (size == 7) {
					// N()LASC0 v HAM3LZ above 9.5 -1 2 0
					// Sail/Nolacso ABOVE 7 minus one twenty
					String token = st.nextToken().toLowerCase();
					if (tcount == 0) {
						int index = token.indexOf("/");
						if (index != -1) {
							eventContainer.setEventtype("total");
							token = token.replace(".", "");
							String pitcher1 = token.substring(0, index);
							String pitcher2 = token.substring(index + 1);
							eventContainer.setTeam1(pitcher1);
							eventContainer.setTeam2(pitcher2);
						} else {
							index = token.indexOf("|");
							if (index != -1) {
								eventContainer.setEventtype("total");
								token = token.replace(".", "");
								String pitcher1 = token.substring(0, index);
								String pitcher2 = token.substring(index + 1);
								eventContainer.setTeam1(pitcher1);
								eventContainer.setTeam2(pitcher2);
							}
						}
					} else if (tcount == 1) {
						if (token.contains("o")) {
							eventContainer.setLineplusminus("o");
						} else {
							eventContainer.setLineplusminus("u");
						}
					} else if (tcount == 2) {
						String line = token;
						line = line.replaceAll("[^.0-9]", "");
						eventContainer.setLine(line);
					} else if (tcount == 3) {
						if (token.length() == 1) {
							eventContainer.setJuiceplusminus(token.substring(0, 1));
						} else {
							if (token.startsWith("m")) {
								eventContainer.setJuiceplusminus("-");
							} else {
								eventContainer.setJuiceplusminus("+");
							}
						}
					} else if (tcount == 4) {
						String line = token;
						line = line.replaceAll("[^.0-9]", "");
						eventContainer.setJuice(line);
					} else if (tcount == 5) {
						eventContainer.setJuice(eventContainer.getJuice() + token);
					} else if (tcount == 6) {
						eventContainer.setJuice(eventContainer.getJuice() + token);
					} else {
						// Unknown
					}
				} else {
					// Do nothing
					st.nextToken();
				}

				tcount++;
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		boolean isRotation = false;
		String team1 = eventContainer.getTeam1();
		try {
			if (team1 != null) {
				String tempteam1 = team1.replaceAll("[^a-zA-Z0-9]", "");
				Integer.parseInt(tempteam1);
				isRotation = true;
				team1 = tempteam1;
				eventContainer.setTeam1(team1);
			}
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}
		
		if (!isRotation) {
			if (team1 != null && team1.length() > 0) {
				team1 = team1.replace("1", "i");
				team1 = team1.replace("4", "a");
				team1 = team1.replace("3", "e");
				team1 = team1.replace("0", "o");
				team1 = team1.replace("$", "s");
				team1 = team1.replace("@", "a");
				team1 = team1.replace("+", "t");
				team1 = team1.replace("5", "s");
				team1 = team1.replace("7", "z");
				team1 = team1.replace("2", "z");
				team1 = team1.replaceAll("[^a-zA-Z0-9]", "");

				// Replace what we can
				for (int x = 0; x < TEAM_WORDS.length; x++) {
					team1 = team1.replace(TEAM_WORDS[x][0], TEAM_WORDS[x][1]);
				}

				eventContainer.setTeam1(team1);
				eventContainer.setPitcher1(team1);
			}
		}

		isRotation = false;
		String team2 = eventContainer.getTeam2();
		try {
			if (team2 != null) {
				String tempteam2 = team2.replaceAll("[^a-zA-Z0-9]", "");
				Integer.parseInt(tempteam2);
				isRotation = true;
				team2 = tempteam2;
				eventContainer.setTeam2(team2);
			}
		} catch (Throwable t) {
			LOGGER.debug(t.getMessage(), t);
		}

		if (!isRotation) {
			if (team2 != null && team2.length() > 0) {
				team2 = team2.replace("1", "i");
				team2 = team2.replace("4", "a");
				team2 = team2.replace("3", "e");
				team2 = team2.replace("0", "o");
				team2 = team2.replace("$", "s");
				team2 = team2.replace("@", "a");
				team2 = team2.replace("+", "t");
				team2 = team2.replace("5", "s");
				team2 = team2.replace("7", "z");
				team2 = team2.replace("2", "z");
				team2 = team2.replaceAll("[^a-zA-Z0-9]", "");
				
				// Replace what we can
				for (int x = 0; x < TEAM_WORDS.length; x++) {
					team2 = team2.replace(TEAM_WORDS[x][0], TEAM_WORDS[x][1]);
				}

				eventContainer.setTeam2(team2);
				eventContainer.setPitcher2(team2);
			}
		}

		LOGGER.debug("EventContainer: " + eventContainer);
		if (eventContainer != null && eventContainer.getEventtype() == null &&
			eventContainer.getTeam1() != null) {
			eventContainer = null;
		} else {
			if (eventContainer.getEventtype().equals("ml")) {
				String line = eventContainer.getLine();
				if (line != null && line.length() > 3) {
					if (line.startsWith("1") || line.startsWith("2") || line.startsWith("3")) {
						// take the first 3
						line = line.substring(0, 3);
						eventContainer.setLine(line);
						eventContainer.setJuice(line);
					}
				}
			}
		}

		LOGGER.info("Exiting parseTokens()");
		return eventContainer;
	}

	/**
	 * 
	 * @param eventsPackage
	 * @param eventContainer
	 * @param body
	 * @return
	 */
	public EventContainer determineEvent(EventsPackage eventsPackage, EventContainer eventContainer, String body) {
		LOGGER.info("Entering determineEvent()");

		try {
			EventPackage ePackage = null;
			boolean team1Found = false;
			boolean found = false;
			Iterator<EventPackage> itr = eventsPackage.getEvents().iterator();
			
			while (!found && itr.hasNext()) {
				EventPackage eventPackage = itr.next();
				final String team1Pitcher = eventPackage.getTeamone().getPitcher().toLowerCase();
				final String team2Pitcher = eventPackage.getTeamtwo().getPitcher().toLowerCase();
				final String team1team = eventPackage.getTeamone().getTeam().toLowerCase();
				final String team2team = eventPackage.getTeamtwo().getTeam().toLowerCase();
				final String team1Shortteam = eventPackage.getTeamone().getTeamshort().toLowerCase();
				final String team2Shortteam = eventPackage.getTeamtwo().getTeamshort().toLowerCase();
				final String team1ID = eventPackage.getTeamone().getId().toString();
				final String team2ID = eventPackage.getTeamtwo().getId().toString();

				if (eventContainer != null && eventContainer.getTeam1() != null) {
					final String pitcher1 = eventContainer.getTeam1().trim().toLowerCase();
					String pitcher2 = null;

					if (eventContainer.getTeam2() != null && eventContainer.getTeam2().length() > 0) {
						pitcher2 = eventContainer.getTeam2().trim().toLowerCase();
					}
					LOGGER.debug("pitcher1: " + pitcher1);
					LOGGER.debug("pitcher2: " + pitcher2);
					LOGGER.debug("team1Pitcher: " + team1Pitcher);
					LOGGER.debug("team2Pitcher: " + team2Pitcher);

					if (pitcher1 != null && pitcher1.equals(team1ID)) {
						LOGGER.debug("Found team1");
						team1Found = true;
						ePackage = eventPackage;
						found = true;						
					} else if (pitcher1 != null && pitcher1.equals(team2ID)) {
						LOGGER.debug("Found team1");
						team1Found = true;
						ePackage = eventPackage;
						found = true;
					} else if (pitcher1 != null && pitcher1.equals(team1team)) {
						LOGGER.debug("Found team1");
						team1Found = true;
						ePackage = eventPackage;
						found = true;
					} else if (pitcher1 != null && pitcher1.equals(team2team)) {
						LOGGER.debug("Found team1");
						team1Found = true;
						ePackage = eventPackage;
						found = true;
					} else if (pitcher1 != null && team1team.contains(pitcher1)) {
						LOGGER.debug("Found team1");
						team1Found = true;
						ePackage = eventPackage;
						found = true;
					} else if (pitcher1 != null && team2team.contains(pitcher1)) {
						LOGGER.debug("Found team1");
						team1Found = true;
						ePackage = eventPackage;
						found = true;
					} else if (pitcher1 != null && pitcher1.equals(team1Shortteam)) {
						LOGGER.debug("Found team1");
						team1Found = true;
						ePackage = eventPackage;
						found = true;
					} else if (pitcher1 != null && pitcher1.equals(team2Shortteam)) {
						LOGGER.debug("Found team1");
						team1Found = true;
						ePackage = eventPackage;
						found = true;
					} else if (pitcher1 != null && pitcher2 != null && team1Pitcher != null && team2Pitcher != null) {
						if (pitcher1.length() > 2 && team1Pitcher.startsWith(pitcher1.substring(0, 3)) && team2Pitcher.startsWith(pitcher2.substring(0, 1))) {
							LOGGER.debug("Found team1");
							team1Found = true;
							ePackage = eventPackage;
							found = true;
						} else if (pitcher1.length() > 2 && team2Pitcher.startsWith(pitcher1.substring(0, 3)) && team1Pitcher.startsWith(pitcher2.substring(0, 1))) {
							LOGGER.debug("Found team2");
							ePackage = eventPackage;
							found = true;
						} else if (pitcher2.length() > 2 && team2Pitcher.startsWith(pitcher2.substring(0, 3)) && team1Pitcher.startsWith(pitcher1.substring(0, 1))) {
							LOGGER.debug("Found team2");
							ePackage = eventPackage;
							found = true;
						} else if (pitcher2.length() > 2 && team1Pitcher.startsWith(pitcher2.substring(0, 3)) && team2Pitcher.startsWith(pitcher1.substring(0, 1))) {
							LOGGER.debug("Found team1");
							team1Found = true;
							ePackage = eventPackage;
							found = true;
						} else {
							if (eventPackage.getTeamone().findPitcher(pitcher1) && eventPackage.getTeamtwo().findPitcher(pitcher2)) {
								LOGGER.debug("Found team1");
								team1Found = true;
								ePackage = eventPackage;
								found = true;
							} else if (eventPackage.getTeamone().findPitcher(pitcher2) && eventPackage.getTeamtwo().findPitcher(pitcher1)) {
								LOGGER.debug("Found team2");
								ePackage = eventPackage;
								found = true;
							} else if ((pitcher1.length() == team1Pitcher.length() && pitcher2.length() == team2Pitcher.length()) &&
									(team1Pitcher.startsWith(pitcher1.substring(0, 1)) && team2Pitcher.startsWith(pitcher2.substring(0, 1)))) {
									LOGGER.debug("Found team1");
								team1Found = true;
								ePackage = eventPackage;
								found = true;
							} else if ((pitcher1.length() == team2Pitcher.length() && pitcher2.length() == team1Pitcher.length()) && 
									(team2Pitcher.startsWith(pitcher1.substring(0, 1)) &&team1Pitcher.startsWith(pitcher2.substring(0, 1)))) {
									LOGGER.debug("Found team2");
								ePackage = eventPackage;
								found = true;
							} else {
								if (pitcher1.length() > 2 && team1team.startsWith(pitcher1.substring(0, 3)) && team2team.startsWith(pitcher2.substring(0, 1))) {
									LOGGER.debug("Found team1");
									team1Found = true;
									ePackage = eventPackage;
									found = true;
								} else if (pitcher1.length() > 2 && team2team.startsWith(pitcher1.substring(0, 3)) && team1team.startsWith(pitcher2.substring(0, 1))) {
									LOGGER.debug("Found team2");
									ePackage = eventPackage;
									found = true;
								} else {
									
								}
							}
						}
					} else if (pitcher1 != null && team1Pitcher != null && team2Pitcher != null) {
						if (pitcher1.length() == team1Pitcher.length() &&
								   team1Pitcher.equals(pitcher1)) {
								LOGGER.debug("Found team1");
							team1Found = true;
							ePackage = eventPackage;
							found = true;
						} else if (pitcher1.length() == team2Pitcher.length() &&
								   team2Pitcher.equals(pitcher1)) {
								LOGGER.debug("Found team2");
							ePackage = eventPackage;
							found = true;
						} else if (pitcher1.length() > 2 && team1Pitcher.startsWith(pitcher1.substring(0, 3))) {
							LOGGER.debug("Found team1");
							team1Found = true;
							ePackage = eventPackage;
							found = true;
						} else if (pitcher1.length() > 2 && team2Pitcher.startsWith(pitcher1.substring(0, 3))) {
							LOGGER.debug("Found team2");
							ePackage = eventPackage;
							found = true;
						} else if (pitcher2 != null && pitcher2.length() > 2 && team1Pitcher.startsWith(pitcher2.substring(0, 3))) {
							LOGGER.debug("Found team1");
							team1Found = true;
							ePackage = eventPackage;
							found = true;
						} else if (pitcher2 != null && pitcher2.length() > 2 && team2Pitcher.startsWith(pitcher2.substring(0, 3))) {
							LOGGER.debug("Found team2");
							ePackage = eventPackage;
							found = true;
						} else {
							if (eventPackage.getTeamone().findPitcher(pitcher1)) {
								LOGGER.debug("Found team1");
								team1Found = true;
								ePackage = eventPackage;
								found = true;
							} else if (eventPackage.getTeamtwo().findPitcher(pitcher1)) {
								LOGGER.debug("Found team2");
								ePackage = eventPackage;
								found = true;
							} else {
								
							}
						}		
					}
				} else {
					// Chaavez -112 vs Jaxson
					// Newcombe +110 (Lacky)
					// WH33LER +131 vs WIANWRIHGT
					// SM1+H plus 152 vs M()()R3
					// Strialy/mo0r3 above 8 -120
					// N()LASC0 v HAM3LZ above 9.5 -120
					// Fotlynewizc +165 vs Gonzaelz
					// C3UT()/SANHC3Z over 9.5 -115
					// K03LHER +145 v WAHCA
					// PACKSTON -135 vs CHAV3Z
					// STRAILEY -115 vs GUERA
					// C()BB/GUASMAN above 10 -115
					// N()RISS -153 vs BALCH
					// F3|DMAN/GREY UDNER 12 -115
					// Zimerman/Vragas > 9.5 -105
					// Wianwright/Wheelr < 9 +110

					// Replace what we can
					// Replace what we can
					for (int x = 0; x < REPLACE_WORDS.length; x++) {
						body = body.replace(REPLACE_WORDS[x][0], REPLACE_WORDS[x][1]);
					}
					final StringTokenizer st = new StringTokenizer(body," ");
					int count = 0;
					String team1 = null;
					while (st.hasMoreTokens()) {
						String token = st.nextToken();
						if (count == 0) {
							token = token.replace(".", "");
							team1 = token;
							if (team1.length() > 2 && team1Pitcher.startsWith(team1.substring(0, 3))) {
								LOGGER.debug("Found team1");
								team1Found = true;
								ePackage = eventPackage;
								found = true;
							} else if (team1.length() > 2 && team2Pitcher.startsWith(team1.substring(0, 3))) {
								LOGGER.debug("Found team1");
								team1Found = true;
								ePackage = eventPackage;
								found = true;
							}
						} else if (count == 1) {
							if (token.startsWith("+") || token.startsWith("-")) {
								eventContainer.setEventtype("ml");
								eventContainer.setJuiceplusminus(token.substring(0, 1));
								eventContainer.setJuice(token.substring(1));
								eventContainer.setLineplusminus(token.substring(0, 1));
								eventContainer.setLine(token.substring(1));
							}
						}
						count++;
					}
				}
			}

			if (!found) {
				team1Found = false;
				found = false;
				itr = eventsPackage.getEvents().iterator();
				while (!found && itr.hasNext()) {
					EventPackage eventPackage = itr.next();
					final String team1Pitcher = eventPackage.getTeamone().getPitcher().toLowerCase();
					final String team2Pitcher = eventPackage.getTeamtwo().getPitcher().toLowerCase();

					if (eventContainer != null && eventContainer.getTeam1() != null && eventContainer.getTeam2() != null) {
						final String pitcher1 = eventContainer.getTeam1().trim().toLowerCase();
						final String pitcher2 = eventContainer.getTeam2().trim().toLowerCase();
						LOGGER.debug("pitcher1: " + pitcher1);
						LOGGER.debug("pitcher2: " + pitcher2);
						LOGGER.debug("team1Pitcher: " + team1Pitcher);
						LOGGER.debug("team2Pitcher: " + team2Pitcher);
				
						if (pitcher1 != null && pitcher1.length() > 2 &&
							pitcher2 != null && pitcher2.length() > 2 &&
							team1Pitcher.startsWith(pitcher1.substring(0, 2)) &&
							team2Pitcher.startsWith(pitcher2.substring(0, 2))) {
							LOGGER.debug("Found team1");
							team1Found = true;
							ePackage = eventPackage;
							found = true;
						} else if (pitcher1 != null && pitcher1.length() > 2 &&
								pitcher2 != null && pitcher2.length() > 2 &&
								team2Pitcher.startsWith(pitcher1.substring(0, 2)) &&
								team1Pitcher.startsWith(pitcher2.substring(0, 2))) {
							LOGGER.debug("Found team2");
							ePackage = eventPackage;
							found = true;
						} else if (pitcher1 != null && pitcher1.length() > 2 && 
								team1Pitcher.startsWith(pitcher1.substring(0, 3))) {
							LOGGER.debug("Found team1");
							team1Found = true;
							ePackage = eventPackage;
							found = true;
						} else if (pitcher1 != null && pitcher1.length() > 2 &&
								team2Pitcher.startsWith(pitcher1.substring(0, 3))) {
							LOGGER.debug("Found team1");
							team1Found = true;
							ePackage = eventPackage;
							found = true;
						} else if (pitcher2 != null && pitcher2.length() > 2 &&
								team2Pitcher.startsWith(pitcher2.substring(0, 3))) {
							LOGGER.debug("Found team2");
							ePackage = eventPackage;
							found = true;
						} else if (pitcher2 != null && pitcher2.length() > 2 &&
								team1Pitcher.startsWith(pitcher2.substring(0, 3))) {
							LOGGER.debug("Found team2");
							ePackage = eventPackage;
							found = true;
							
						} else {
							// now take a chance
							LOGGER.debug("team1Pitcher: " + team1Pitcher);
							LOGGER.debug("pitcher1: " + pitcher1);
							LOGGER.debug("team2Pitcher: " + team2Pitcher);
							LOGGER.debug("pitcher2: " + pitcher2);
					
							if (pitcher1.length() > 2 && team1Pitcher.endsWith(pitcher1.substring(pitcher1.length() - 3, pitcher1.length()))) {
								LOGGER.debug("Found team1");
								team1Found = true;
								ePackage = eventPackage;
								found = true;
							} else if (pitcher1.length() > 2 && team2Pitcher.endsWith(pitcher1.substring(pitcher1.length() - 3, pitcher1.length()))) {
								LOGGER.debug("Found team1");
								team1Found = true;
								ePackage = eventPackage;
								found = true;
							} else if (pitcher2.length() > 2 && team2Pitcher.endsWith(pitcher2.substring(pitcher2.length() - 3, pitcher2.length()))) {
								LOGGER.debug("Found team2");
								ePackage = eventPackage;
								found = true;
							} else if (pitcher2.length() > 2 && team1Pitcher.endsWith(pitcher2.substring(pitcher2.length() - 3, pitcher2.length()))) {
								LOGGER.debug("Found team2");
								ePackage = eventPackage;
								found = true;
							} else {
								LOGGER.debug("Not found!");
							}
						}
					}
				}
			}

			LOGGER.debug("ePackage: " + ePackage);
			if (ePackage != null) {
				LOGGER.debug("Found the game");
				// Found the game; now determine which side
				eventContainer = determineEventInfo(ePackage, eventContainer, team1Found);
			} 
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}

		LOGGER.info("Exiting determineEvent()");
		return eventContainer;
	}

	/**
	 * 
	 * @param ePackage
	 * @param emailEvent
	 * @param team1found
	 * @return
	 */
	private EventContainer determineEventInfo(EventPackage ePackage, 
			EventContainer eventContainer,
			boolean team1found) {
		LOGGER.info("Entering determineEventInfo()");
		LOGGER.debug("EventContainer: " + eventContainer);

		final TeamPackage team1 = ePackage.getTeamone();
		final TeamPackage team2 = ePackage.getTeamtwo();
		final String eteam1 = eventContainer.getTeam1();
		final String eteam2 = eventContainer.getTeam2();
		
		// first check for rotation ID
		LOGGER.debug("team1.getId(): " + team1.getId());
		LOGGER.debug("team2.getId(): " + team2.getId());
		if (eteam1 != null && team1.getId().toString().equals(eteam1)) {
			eventContainer.setRotationid(team1.getId().toString());
			eventContainer.setTeam(team1.getTeam());
			eventContainer.setPitcher(team1.getPitcher());			
		} else if (eteam1 != null && team2.getId().toString().equals(eteam1)) {
			eventContainer.setRotationid(team2.getId().toString());
			eventContainer.setTeam(team2.getTeam());
			eventContainer.setPitcher(team2.getPitcher());			
		} else {
			if (eteam1 != null && team1.getTeam().toLowerCase().equals(eteam1)) {
				eventContainer.setRotationid(team1.getId().toString());
				eventContainer.setTeam(team1.getTeam());
				eventContainer.setPitcher(team1.getPitcher());			
			} else if (eteam1 != null && team2.getTeam().toLowerCase().equals(eteam1)) {
				eventContainer.setRotationid(team2.getId().toString());
				eventContainer.setTeam(team2.getTeam());
				eventContainer.setPitcher(team2.getPitcher());
			} else if (eteam1 != null && team1.getTeamshort().toLowerCase().equals(eteam1)) {
				eventContainer.setRotationid(team1.getId().toString());
				eventContainer.setTeam(team1.getTeam());
				eventContainer.setPitcher(team1.getPitcher());			
			} else if (eteam1 != null && team2.getTeamshort().toLowerCase().equals(eteam1)) {
				eventContainer.setRotationid(team2.getId().toString());
				eventContainer.setTeam(team2.getTeam());
				eventContainer.setPitcher(team2.getPitcher());
			} else {
				if (eteam1 != null && eteam1.startsWith(team1.getPitcher().toLowerCase().substring(0, 1))) {
					eventContainer.setRotationid(team1.getId().toString());
					eventContainer.setTeam(team1.getTeam());
					eventContainer.setPitcher(team1.getPitcher());
				} else if (eteam1 != null && eteam1.startsWith(team2.getPitcher().toLowerCase().substring(0, 1))) {
					eventContainer.setRotationid(team2.getId().toString());
					eventContainer.setTeam(team2.getTeam());
					eventContainer.setPitcher(team2.getPitcher());
				} else {
					if (eteam2 != null && eteam2.startsWith(team1.getPitcher().toLowerCase().substring(0, 1))) {
						eventContainer.setRotationid(team2.getId().toString());
						eventContainer.setTeam(team2.getTeam());
						eventContainer.setPitcher(team2.getPitcher());
					} else if (eteam2 != null && eteam2.startsWith(team2.getPitcher().toLowerCase().substring(0, 1))) {
						eventContainer.setRotationid(team1.getId().toString());
						eventContainer.setTeam(team1.getTeam());
						eventContainer.setPitcher(team1.getPitcher());
					}
				}
			}
		}

		eventContainer.setRotationid1(team1.getId().toString());
		eventContainer.setRotationid2(team2.getId().toString());
		eventContainer.setTeam1(team1.getTeam());
		eventContainer.setTeam2(team2.getTeam());
		eventContainer.setPitcher1(team1.getPitcher());
		eventContainer.setPitcher2(team2.getPitcher());
		eventContainer.setEventdate(ePackage.getDateofevent() + " " + ePackage.getTimeofevent());
		eventContainer.setGamedate(ePackage.getEventdatetime());
		eventContainer.setLinetype("game");
		eventContainer.setGamesport("Baseball");
		eventContainer.setGametype("MLB");

		LOGGER.info("Exiting determineEventInfo()");
		return eventContainer;
	}
}
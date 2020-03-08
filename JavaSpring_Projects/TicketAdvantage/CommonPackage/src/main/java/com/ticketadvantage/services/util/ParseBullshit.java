package com.ticketadvantage.services.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.model.EventPackage;
import com.ticketadvantage.services.model.PendingEvent;
import com.ticketadvantage.services.model.TeamPackage;

public class ParseBullshit {
	private static final Logger LOGGER = Logger.getLogger(ParseBullshit.class);
	protected static final ThreadLocal<SimpleDateFormat> PENDING_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
		}
	};
	private static final String[][] REPLACE_WORDS = new String[][] {
		{"moar", "more"},
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
		{"<", "under"},
		{"()", "O"},
		{"plus", "+"},
		{"puls", "+"},
		{"minus", "-"},
		{"munis", "-"},
		{"(", ""},
		{")", ""},
		{"one", "1"},
		{"two", "2"},
		{"three", "3"},
		{"four", "4"},
		{"five", "5"},
		{"six", "6"},
		{"seven", "7"},
		{"eight", "8"},
		{"nine", "9"},
		{"zero", "0"},
		{"ten", "10"},
		{"eleven", "11"},
		{"twelve", "12"},
		{"thirteen", "13"},
		{"fourteen", "14"},
		{"fifteen", "15"},
		{"sixteen", "16"},
		{"seventeen", "17"},
		{"eighteen", "18"},
		{"nineteen", "19"},
		{"twenty", "20"},
		{"thirty", "30"},
		{"forty", "40"},
		{"fifty", "50"},
		{"sixty", "60"},
		{"seventy", "70"},
		{"eighty", "80"},
		{"ninety", "90"}
	};

	/**
	 * 
	 */
	public ParseBullshit() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final ParseBullshit parseBullshit = new ParseBullshit();
		final String DATA[] = {
			"1ST HALF: Oregon +1",
			"#271 Steelers -2.5 -115",
			"#207 Colorado State -7",
			"#197 Troy -6",
			"#363 TCU -2.5",
			"267 TB/ARI 1st half over 22.5",
			"272 $teelers/Lions under 47",
			"FAU/West Kent under SIXTY-NINE",
			"FIU/Marshall over 45",
			"C@L/Colorado over 51",
			"1.4-7 DuK3/V1rg1n1a T3ch ov47",
			"0n3-2-9 Mi4mi/UNC 0v3r 5I.5",
			"M1$$ $T/T3X4S 4&M m0r3 th4n 54",
			"254 CHI/NO under 48",
			"Three-1-Nine TR0Y/Ge0rg1a St over 48",
			"U$C/N0TR3 D4M3 less than 66",
			"#175 Colorado/Oregon State over 55",
			"A&M/Fl0r1da over 50.5",
			"ARK4N$4$/AL@BAM@ under 56",
			"Bee-Y-U/Mi$si$sippi $t ov3r Forty-6.5",
			"(0n3-E1ght-N1n3) M1SS ST/4UBURN 0v3r FIFTY-1",
			"(On3-8-S3ven) MT$U/F4U moar than 58.5",
			"(Three-2-3) BG$U/M1A 0H ov48",
			"Bee-Y-U/Mi$si$sippi $t ov3r Forty-6.5",
			"M4rsha11/Ch4rl0tt3 more than 49",
			"W3st Va/TX Christian less than 71",		
			"#204 Nevada/Fresno State under 62.5",
			"#157 Texas State/Wyoming over 46",
			"#152 Indiana/Penn State under 69.5",
			"#113 Charlotte/Florida Intl over 45.5",
			"#129 Akron/Bowling Green over 54.5",
			"#400 UL Monroe/UL Lafayette under 61",
			"#275 Chicago/Tampa Bay over 43",
			"#265 Buffalo/Carolina over 42.5",
			"#369 Toledo/Nevada over 65"
		};

		for (int x = 0; x < DATA.length; x++) {
			final PendingEvent pe = new PendingEvent();
			LOGGER.debug("DATA[x]: " + DATA[x]);
			parseBullshit.parse(DATA[x], pe);
			LOGGER.debug("PendingEvent: " + pe);
		}
	}

	/**
	 * 
	 * @param ep
	 * @param pe
	 * @return
	 */
	public boolean findGame(EventPackage ep, PendingEvent pe) {
		LOGGER.info("Entering findGame()");
		final TeamPackage team1 = ep.getTeamone();
		final TeamPackage team2 = ep.getTeamtwo();
		
		if (team1 != null && team2 != null) {
			String teamName1 = team1.getTeam();
			String teamName2 = team2.getTeam();
			String pendingTeam = pe.getTeam();
			String pendingTeam2 = pe.getPitcher();

			if (teamName1 != null && teamName2 != null && pendingTeam != null) {
				teamName1 = teamName1.toLowerCase();
				teamName2 = teamName2.toLowerCase();
				pendingTeam = pendingTeam.toLowerCase();
				LOGGER.debug("teamName1: " + teamName1);
				LOGGER.debug("teamName2: " + teamName2);
				LOGGER.debug("pendingTeam: " + pendingTeam);

				if (teamName1.equals(pendingTeam)) {
					LOGGER.debug("EP: " + ep);
					return true;
				} else if (teamName2.equals(pendingTeam)) {
					LOGGER.debug("EP: " + ep);
					return true;
				} else {
					if (pendingTeam2 != null && pendingTeam2.length() > 0) {
						pendingTeam2 = pendingTeam2.toLowerCase();
						LOGGER.debug("pendingTeam2: " + pendingTeam2);
						if (teamName1.equals(pendingTeam2)) {
							LOGGER.debug("EP: " + ep);
							return true;
						} else if (teamName2.equals(pendingTeam2)) {
							LOGGER.debug("EP: " + ep);
							return true;
						}
					}
				}
			} 
		}

		LOGGER.info("Exiting findGame()");
		return false;
	}

	/**
	 * 
	 * @param ep
	 * @param pe
	 * @return
	 */
	public boolean findGameShort(EventPackage ep, PendingEvent pe) {
		LOGGER.info("Entering findGameShort()");
		final TeamPackage team1 = ep.getTeamone();
		final TeamPackage team2 = ep.getTeamtwo();
		
		if (team1 != null && team2 != null) {
			String teamName1 = team1.getTeam();
			String teamName2 = team2.getTeam();
			String shortTeamName1 = team1.getTeamshort();
			String shortTeamName2 = team2.getTeamshort();
			String pendingTeam = pe.getTeam();
			String pendingTeam2 = pe.getPitcher();

			if (teamName1 != null && teamName2 != null && pendingTeam != null) {
				teamName1 = teamName1.toLowerCase();
				teamName2 = teamName2.toLowerCase();
				pendingTeam = pendingTeam.toLowerCase();

				if (teamName1.contains(pendingTeam.substring(0, 3))) {
					LOGGER.debug("EP: " + ep);
					return true;
				} else if (teamName2.contains(pendingTeam.substring(0, 3))) {
					LOGGER.debug("EP: " + ep);
					return true;
				} else {
					if (pendingTeam2 != null && pendingTeam2.length() > 0) {
						pendingTeam2 = pendingTeam2.toLowerCase();
						if (teamName1.contains(pendingTeam2.substring(0, 3))) {
							LOGGER.debug("EP: " + ep);
							return true;
						} else if (teamName2.contains(pendingTeam2.substring(0, 3))) {
							LOGGER.debug("EP: " + ep);
							return true;
						}
					}
				}
			} else if (shortTeamName1 != null && shortTeamName2 != null && pendingTeam != null) {
				if (shortTeamName1.contains(pendingTeam)) {
					LOGGER.debug("EP: " + ep);
					return true;
				} else if (shortTeamName2.contains(pendingTeam)) {
					LOGGER.debug("EP: " + ep);
					return true;
				} else if (shortTeamName1.contains(pendingTeam.substring(0, 3))) {
					LOGGER.debug("EP: " + ep);
					return true;
				} else if (shortTeamName2.contains(pendingTeam.substring(0, 3))) {
					LOGGER.debug("EP: " + ep);
					return true;
				} else {
					if (pendingTeam2 != null && pendingTeam2.length() > 0) {
						if (shortTeamName1.contains(pendingTeam2)) {
							LOGGER.debug("EP: " + ep);
							return true;
						} else if (shortTeamName2.contains(pendingTeam2)) {
							LOGGER.debug("EP: " + ep);
							return true;
						} else if (shortTeamName1.contains(pendingTeam2.substring(0, 3))) {
							LOGGER.debug("EP: " + ep);
							return true;
						} else if (shortTeamName2.contains(pendingTeam2.substring(0, 3))) {
							LOGGER.debug("EP: " + ep);
							return true;
						}
					}
				}
			}
		}

		LOGGER.info("Exiting findGameShort()");
		return false;
	}

	/**
	 * 
	 * @param data
	 * @param pe
	 */
	public void parse(String data, PendingEvent pe) {
		LOGGER.info("Entering parse()");

		// 1ST HALF: Oregon +1 
		// #271 Steelers -2.5 -115
		// #207 Colorado State -7
		// #197 Troy -6
		// #363 TCU -2.5

		// 267 TB/ARI 1st half over 22.5
		// 272 $teelers/Lions under 47
		// 254 CHI/NO under 48
		// #175 Colorado/Oregon State over 55

		// FAU/West Kent under SIXTY-NINE
		// FIU/Marshall over 45
		// C@L/Colorado over 51
		// M1$$ $T/T3X4S 4&M m0r3 th4n 54
		// U$C/N0TR3 D4M3 less than 66
		// A&M/Fl0r1da over 50.5
		// ARK4N$4$/AL@BAM@ under 56
		// Bee-Y-U/Mi$si$sippi $t ov3r Forty-6.5
		// M4rsha11/Ch4rl0tt3 more than 49
		// W3st Va/TX Christian less than 71

		// 1.4-7 DuK3/V1rg1n1a T3ch ov47
		// 0n3-2-9 Mi4mi/UNC 0v3r 5I.5
		// (0n3-E1ght-N1n3) M1SS ST/4UBURN 0v3r FIFTY-1
		// (On3-8-S3ven) MT$U/F4U moar than 58.5
		// (Three-2-3) BG$U/M1A 0H ov48
		// Three-1-Nine TR0Y/Ge0rg1a St over 48

		// #204 Nevada/Fresno State under 62.5
		// #157 Texas State/Wyoming over 46
		// #152 Indiana/Penn State under 69.5
		// #113 Charlotte/Florida Intl over 45.5
		// #129 Akron/Bowling Green over 54.5
		// #400 UL Monroe/UL Lafayette under 61
		// #275 Chicago/Tampa Bay over 43 
		// #265 Buffalo/Carolina over 42.5 
		// #369 Toledo/Nevada over 65

		// 
		// CONCLUSION: format is rotationID or Teams, line and juice (sometimes)
		// 

		if (data != null) {
			// Make it lower case
			data = data.toLowerCase();

			// Ticket # created dynamically
			setupTicketNumber(pe);

			// Setup dates
			setupDates(pe);
			
			// Set to not do Post url
			pe.setPosturl("");
			pe.setDoposturl(false);

			// Get 1st half info if there is one
			data = checkForHalf(data, pe);
			LOGGER.debug("data: " + data);

			// Try to get rotation # if there is one
			String rotId = rotNumber(data);
			LOGGER.debug("rotId: " + rotId);

			// Either don't have a rotation # or there is a problem getting it
			if (rotId != null && rotId.length() > 0) {
				pe.setRotationid(rotId);
				int index = data.indexOf(" ");
				if (index != -1) {
					data = data.substring(index + 1);
				}
			}

			int lindex = data.lastIndexOf(" ");
			if (lindex != -1) {
				String lastNumber = data.substring(lindex + 1);
				if (lastNumber != null && lastNumber.length() > 0) {
					lastNumber.toLowerCase().trim();
					if (lastNumber.startsWith("o")) {
						// Make sure it's an over
						if (lastNumber.startsWith("ov")) {
							pe.setLineplusminus("o");
							pe.setEventtype("total");

							lastNumber = massageNumber(lastNumber);
							pe.setLine(lastNumber);
						} else {
							// Something else 
						}
					} else if (lastNumber.startsWith("u")) {
						pe.setLineplusminus("u");
						pe.setEventtype("total");
						lastNumber = massageNumber(lastNumber);
						pe.setLine(lastNumber);
					} else if (lastNumber.startsWith("-")) {
						lastNumber = massageNumber(lastNumber);
						pe.setLineplusminus("-");
						pe.setLine(lastNumber);
						pe.setEventtype("spread");
						pe.setJuiceplusminus("-");
						pe.setJuice("110");
					} else if (lastNumber.startsWith("+")) {
						lastNumber = massageNumber(lastNumber);
						pe.setLineplusminus("+");
						pe.setLine(lastNumber);
						pe.setEventtype("spread");
						pe.setJuiceplusminus("-");
						pe.setJuice("110");
					} else {
						lastNumber = massageNumberDos(lastNumber);
						pe.setLine(lastNumber);						
					}
				}

				// Massage the data
				data = massageData(data.substring(0, lindex));

				// Setup any event types
				lindex = data.lastIndexOf(" ");
				data = setupEventType(lindex, data, pe);
				LOGGER.debug("data: " + data);

				if (data != null) {
					// Check for spread and juice
					data = checkForSpreadAndJuice(data, pe);
					LOGGER.debug("data: " + data);

					// Set up the team information
					setupTeamInfo(data, pe);
				}
			}

			LOGGER.debug("data: " + data);
		}
	}

	/**
	 * 
	 * @param data
	 * @param pe
	 */
	private void setupTeamInfo(String data, PendingEvent pe) {
		LOGGER.info("Entering setupTeamInfo()");

		// Replace any dashes
		data = data.replace("-", "");
		int lindex = data.lastIndexOf("/");

		if (lindex != -1) {
			String team1 = data.substring(0, lindex).trim();
			String team2 = data.substring(lindex + 1).trim();
			data = data.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}]", "");

			if (team1.equals("beeyu")) {
				team1 = "byu";
			}

			if (team2.equals("beeyu")) {
				team2 = "byu";
			}

			pe.setTeam(team1);
			pe.setPitcher(team2);
		} else {
			data = data.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}^\\ ]", "").trim();

			if (data.equals("beeyu")) {
				data = "byu";
			}

			pe.setTeam(data);
		}

		LOGGER.info("Exiting setupTeamInfo()");
	}

	/**
	 * 
	 * @param data
	 * @param pe
	 * @return
	 */
	private String checkForSpreadAndJuice(String data, PendingEvent pe) {
		LOGGER.info("Entering checkForSpreadAndJuice()");

		// Check for a spread value
		if (data.contains("-") || data.contains("+")) {
			int dindex = data.lastIndexOf(" ");

			if (dindex != -1) {
				final String nData = data.substring(dindex + 1);

				if (nData.startsWith("-")) {
					pe.setEventtype("spread");
					pe.setJuiceplusminus(pe.getLineplusminus());
					pe.setJuice(pe.getLine());
					pe.setLineplusminus("-");
					pe.setLine(nData.substring(1));
					data = data.substring(0, dindex);
				} else if (nData.startsWith("+")) {
					pe.setEventtype("spread");
					pe.setJuiceplusminus(pe.getLineplusminus());
					pe.setJuice(pe.getLine());
					pe.setLineplusminus("+");
					pe.setLine(nData.substring(1));
					data = data.substring(0, dindex);
				}
			}
		}

		LOGGER.info("Exiting checkForSpreadAndJuice()");
		return data;
	}

	/**
	 * 
	 * @param lindex
	 * @param data
	 * @param pe
	 * @return
	 */
	private String setupEventType(int lindex, String data, PendingEvent pe) {
		LOGGER.info("Entering setupEventType()");

		if (lindex != -1) {
			String tempWork = data.substring(lindex + 1);
			if (tempWork != null) {
				tempWork = tempWork.trim();
				if ("over".equals(tempWork)) {
					pe.setEventtype("total");
					pe.setLineplusminus("o");
					data = data.substring(0, lindex);
					pe.setJuiceplusminus("-");
					pe.setJuice("110");
				} else if ("under".equals(tempWork)) {
					pe.setEventtype("total");
					pe.setLineplusminus("u");
					data = data.substring(0, lindex);
					pe.setJuiceplusminus("-");
					pe.setJuice("110");
				} else if ("-".equals(tempWork)) {
					pe.setEventtype("spread");
					pe.setJuiceplusminus(pe.getLineplusminus());
					pe.setJuice(pe.getLine());
					pe.setLineplusminus("-");
					pe.setLine(tempWork.substring(1));
				} else if ("+".equals(tempWork)) {
					pe.setEventtype("spread");
					pe.setJuiceplusminus(pe.getLineplusminus());
					pe.setJuice(pe.getLine());
					pe.setLineplusminus("+");
					pe.setLine(tempWork.substring(1));
				} else {
					pe.setEventtype("spread");
					pe.setJuiceplusminus("-");
					pe.setJuice("110");
				}
			}
		} else {
			pe.setEventtype("spread");
			pe.setJuiceplusminus("-");
			pe.setJuice("110");					
		}

		LOGGER.info("Exiting setupEventType()");
		return data;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private String massageData(String data) {
		LOGGER.info("Entering massageData()");

		// $teelers/Lions under 47
		// CHI/NO under 48
		// Colorado/Oregon State over 55
		// FAU/West Kent under SIXTY-NINE
		// FIU/Marshall over 45
		// C@L/Colorado over 51
		// M1$$ $T/T3X4S 4&M m0r3 th4n 54
		// U$C/N0TR3 D4M3 less than 66
		// A&M/Fl0r1da over 50.5
		// ARK4N$4$/AL@BAM@ under 56
		// Bee-Y-U/Mi$si$sippi $t ov3r Forty-6.5
		// M4rsha11/Ch4rl0tt3 more than 49
		// W3st Va/TX Christian less than 71
		// DuK3/V1rg1n1a T3ch ov47
		// Mi4mi/UNC 0v3r 5I.5
		// M1SS ST/4UBURN 0v3r FIFTY-1
		// MT$U/F4U moar than 58.5
		// BG$U/M1A 0H ov48
		// TR0Y/Ge0rg1a St
		data = data.replace("$", "s");
		data = data.replace("@", "a");
		data = data.replace("1", "i");
		data = data.replace("3", "e");
		data = data.replace("4", "a");
		data = data.replace("0", "o");

		// Replace words
		for (int x = 0; x < REPLACE_WORDS.length; x++) {
			data = data.replace(REPLACE_WORDS[x][0], REPLACE_WORDS[x][1]);
		}

		LOGGER.info("Exiting massageData()");
		return data;
	}

	/**
	 * 
	 * @param lastNumber
	 * @return
	 */
	private String massageNumber(String lastNumber) {
		LOGGER.info("Entering massageNumber()");

		lastNumber = lastNumber.replace("twenty", "2");
		lastNumber = lastNumber.replace("thirty", "3");
		lastNumber = lastNumber.replace("forty", "4");
		lastNumber = lastNumber.replace("fifty", "5");
		lastNumber = lastNumber.replace("sixty", "6");
		lastNumber = lastNumber.replace("seventy", "7");
		lastNumber = lastNumber.replace("eighty", "8");
		lastNumber = lastNumber.replace("ninety", "9");
	
		// Replace what needs to be replaced
		for (int x = 0; x < REPLACE_WORDS.length; x++) {
			lastNumber = lastNumber.replace(REPLACE_WORDS[x][0], REPLACE_WORDS[x][1]);
		}

		// Last Number
		lastNumber = lastNumber.replaceAll("[^\\p{IsDigit}^\\.]", "");

		LOGGER.info("Exiting massageNumber()");
		return lastNumber;
	}

	/**
	 * 
	 * @param lastNumber
	 * @return
	 */
	private String massageNumberDos(String lastNumber) {
		LOGGER.info("Entering massageNumberDos()");
		LOGGER.debug("lastNumber: " + lastNumber);

		lastNumber = lastNumber.replace("twenty", "2");
		lastNumber = lastNumber.replace("thirty", "3");
		lastNumber = lastNumber.replace("forty", "4");
		lastNumber = lastNumber.replace("fifty", "5");
		lastNumber = lastNumber.replace("sixty", "6");
		lastNumber = lastNumber.replace("seventy", "7");
		lastNumber = lastNumber.replace("eighty", "8");
		lastNumber = lastNumber.replace("ninety", "9");
		lastNumber = lastNumber.replace("one", "1");
		lastNumber = lastNumber.replace("two", "2");
		lastNumber = lastNumber.replace("three", "3");
		lastNumber = lastNumber.replace("four", "4");
		lastNumber = lastNumber.replace("five", "5");
		lastNumber = lastNumber.replace("six", "6");
		lastNumber = lastNumber.replace("seven", "7");
		lastNumber = lastNumber.replace("eight", "8");
		lastNumber = lastNumber.replace("nine", "9");
		lastNumber = lastNumber.replace("ten", "10");
		lastNumber = lastNumber.replace("i", "1");
		lastNumber = lastNumber.replace("!", "1");
		lastNumber = lastNumber.replace("l", "1");
		lastNumber = lastNumber.replace("o", "0");
		lastNumber = lastNumber.replace("e", "3");
		lastNumber = lastNumber.replace("a", "4");
		lastNumber = lastNumber.replaceAll("[^\\p{IsDigit}^\\.]", "");
		LOGGER.debug("lastNumber: " + lastNumber);

		LOGGER.info("Exiting massageNumberDos()");
		return lastNumber;
	}

	/**
	 * 
	 * @param data
	 * @param pe
	 * @return
	 */
	private String checkForHalf(String data, PendingEvent pe) {
		LOGGER.info("Entering checkForHalf()");

		int findex = data.indexOf("1st half");
		if (findex != -1) {
			pe.setLinetype("first");
			data = data.substring(0, findex) + data.substring(findex + "1st half".length() + 1);
		} else {
			findex = data.indexOf("1h");
			if (findex != -1) {
				pe.setLinetype("first");
				data = data.substring(0, findex) + data.substring(findex + "1h".length() + 1);
			} else {
				findex = data.indexOf("first half");
				if (findex != -1) {
					pe.setLinetype("first");
					data = data.substring(0, findex) + data.substring(findex + "first half".length() + 1);
				} else {
					pe.setLinetype("game");
				}
			}
		}

		LOGGER.info("Exiting checkForHalf()");
		return data;
	}

	/**
	 * 
	 * @param pe
	 */
	private void setupTicketNumber(PendingEvent pe) {
		LOGGER.info("Entering setupTicketNumber()");

		// Ticket # created dynamically
		Long time = System.currentTimeMillis();
		String ticketNum = Long.toString(time);
		if (ticketNum.length() > 20) {
			ticketNum = ticketNum.substring(0, 20);
		}
		LOGGER.debug("ticketNum: " + ticketNum);
		pe.setTicketnum(ticketNum);

		LOGGER.info("Exiting setupTicketNumber()");
	}

	/**
	 * 
	 * @param pe
	 */
	private void setupDates(PendingEvent pe) {
		LOGGER.info("Entering setupDates()");

		final Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		pe.setDateaccepted(PENDING_DATE_FORMAT.get().format(now.getTime()));
		pe.setDatecreated(now.getTime());
		pe.setDatemodified(now.getTime());

		LOGGER.info("Exiting setupDates()");
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	protected String rotNumber(String data) {
		String rotId = "";
		int spaceIndex = data.indexOf(" ");
		if (spaceIndex != -1) {
			String rotation = data.substring(0, spaceIndex).toLowerCase().trim();
			rotation = rotation.replace("(", "");
			rotation = rotation.replace(")", "");
			rotation = rotation.replace("[", "");
			rotation = rotation.replace("]", "");
			rotation = rotation.replace("#", "");

			if (rotation.contains("/")) {
				// Means we don't have a rotation ID
				rotId = null;
			} else {
				String[] tokens = rotation.split("-");
				if (tokens != null && tokens.length > 0) {
					for (int x = 0; (tokens != null && x < tokens.length); x++) {
						String number = getNumber(tokens[x]);
						rotId += number;
						LOGGER.debug("rotId: " + rotId);
					}
				} else {
					tokens = rotation.split(".");
					if (tokens != null && tokens.length > 0) {
						for (int x = 0; (tokens != null && x < tokens.length); x++) {
							String number = getNumber(tokens[x]);
							rotId += number;
							LOGGER.debug("rotId: " + rotId);
						}
					}
				}

				// Remove anything but digits/characters
				LOGGER.debug("rotId: " + rotId);
				rotId = rotId.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}]", "");

				if (rotId.matches("[0-9]+") && rotId.length() > 2) {
					
				} else {
					LOGGER.debug("Setting rotId to null!");
					rotId = null;
				}
			}
		}

		LOGGER.debug("rotId: " + rotId);
		LOGGER.info("Entering parse()");
		return rotId;
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	protected String getNumber(String number) {
		LOGGER.debug("number: " + number);
		String rotationId = "";

		if (number.matches("[0-9]+") && number.length() > 2) {
			rotationId = number;
		} else if (number.length() == 3) { // now disect the values
			// 1, 2, 6
			number = number.replace("0", "o");
			number = number.replace("3", "e");
			number = number.replace("l", "i");
			number = number.replace("!", "i");
			number = number.replace("$", "s");
			number = number.replace("1", "i");
			number = number.replace("@", "a");
			number = number.replace("+", "t");
			if (number.equals("one")) {
				rotationId = "1";
			} else if (number.equals("two")) {
				rotationId = "2";
			} else if (number.equals("six")) {
				rotationId = "6";
			} else {
				rotationId = number;
			}
		} else if (number.length() == 4) {
			// 0, 4, 5, 9
			number = number.replace("3", "e");
			number = number.replace("0", "o");
			number = number.replace("$", "s");
			number = number.replace("1", "i");
			number = number.replace("!", "i");
			number = number.replace("l", "i");
			number = number.replace("5", "s");
			number = number.replace("7", "z");
			number = number.replace("+", "t");
			if (number.equals("zero")) {
				rotationId = "0";
			} else if (number.equals("four")) {
				rotationId = "4";
			} else if (number.equals("five")) {
				rotationId = "5";
			} else if (number.equals("nine")) {
				rotationId = "9";
			} else {
				rotationId = number;
			}
		} else if (number.length() == 5) {
			// 3, 7, 8
			number = number.replace("0", "o");
			number = number.replace("3", "e");
			number = number.replace("l", "i");
			number = number.replace("!", "i");
			number = number.replace("$", "s");
			number = number.replace("1", "i");
			number = number.replace("@", "a");
			number = number.replace("+", "t");
			if (number.equals("three")) {
				rotationId = "3";
			} else if (number.equals("seven")) {
				rotationId = "7";
			} else if (number.equals("eight")) {
				rotationId = "8";
			} else {
				rotationId = number;
			}
		} else if (number.length() == 1) {
			number = number.replace("!", "1");
			number = number.replace("l", "1");
			number = number.replace("o", "0");
			number = number.replace("e", "3");
			number = number.replace("a", "4");
			rotationId = number;
		}

		// Now check for a valid #
		if (!rotationId.matches("[0-9]+")) {
			rotationId = rotationId.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}]", "");
			rotationId = rotationId.replace("i", "1");
			rotationId = rotationId.replace("!", "1");
			rotationId = rotationId.replace("l", "1");
			rotationId = rotationId.replace("o", "0");
			rotationId = rotationId.replace("e", "3");
			rotationId = rotationId.replace("a", "4");
		}

		LOGGER.debug("rotationId: " + rotationId);
		return rotationId;
	}
}
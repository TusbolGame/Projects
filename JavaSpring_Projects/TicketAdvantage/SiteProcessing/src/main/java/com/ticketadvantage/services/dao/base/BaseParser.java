package com.ticketadvantage.services.dao.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ticketadvantage.services.errorhandling.AppErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchErrorCodes;
import com.ticketadvantage.services.errorhandling.BatchErrorMessage;
import com.ticketadvantage.services.errorhandling.BatchException;

public abstract class BaseParser {
	private final static Logger LOGGER = Logger.getLogger(BaseParser.class);
	protected String timezone;
	protected String sportType;

	public BaseParser() {
		super();
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the sportType
	 */
	public String getSportType() {
		return sportType;
	}

	/**
	 * @param sportType the sportType to set
	 */
	public void setSportType(String sportType) {
		this.sportType = sportType;
	}

	/**
	 * 
	 * @param xhtml
	 * @param captchaInfo
	 * @return
	 * @throws BatchException
	 */
	public boolean checkCaptcha(String xhtml, String captchaInfo, String elementId) throws BatchException {
		LOGGER.info("Entering checkCaptcha()");
		boolean retValue = false;

		// Get the document object
		final Document doc = parseXhtml(xhtml);
		
		if (xhtml != null && xhtml.length() > 0 && xhtml.contains(captchaInfo)) {
			Element element = doc.getElementById(elementId);
			if (element != null) {
				retValue = true;
			}
		}

		LOGGER.info("Exiting checkCaptcha()");
		return retValue;
	}
	
	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	protected Document parseXhtml(String xhtml) throws BatchException {
		LOGGER.info("Entering parseXhtml()");
		Document doc = null;
		
		// Check for a valid xhtml
		if (xhtml != null) {
			// Parse the xhtml
			doc = Jsoup.parse(xhtml, "iso-8859-1");
			if (doc == null) {
				throw new BatchException(BatchErrorCodes.XHTML_DOC_PARSING_EXCEPTION, 
					BatchErrorMessage.XHTML_DOC_PARSING_EXCEPTION, "Cannot parse HTML", "<![CDATA[" + xhtml + "]]>");
			}
		} else {
			throw new BatchException(BatchErrorCodes.XHTML_DOC_PARSING_EXCEPTION, 
					BatchErrorMessage.XHTML_DOC_PARSING_EXCEPTION, "XHTML is null", "<![CDATA[" + xhtml + "]]>");
		}

		LOGGER.info("Exiting parseXhtml()");
		return doc;
	}

	/**
	 * 
	 * @param juice
	 * @param endDelimiter
	 * @return
	 */
	protected Map<String, String> parseJuice(String juice, String beginDelimiter, String endDelimiter) {
		LOGGER.info("Entering parseJuice()");
		LOGGER.debug("juice: " + juice);
		LOGGER.debug("endDelimiter: " + endDelimiter);
		final Map<String, String> values = new HashMap<String, String>();
		
		// Check if we have no ending bracket or not
		if (juice != null && beginDelimiter != null && endDelimiter != null) {
			int index = juice.indexOf(beginDelimiter);
			if (index != -1) {
				juice = juice.substring(index + 1);
				index = juice.indexOf(endDelimiter);
				if (index != -1) {
					juice = juice.substring(0, index);
					String juiceindicator = juice.substring(0, 1);
	
					if (juiceindicator.startsWith("e") || juiceindicator.startsWith("E")) {
						juice = "100";
						juiceindicator = "+";
					} else {
						juice = juice.substring(1);
						juice = juice.replaceAll("\u00BD", ".5");
						juice = juice.replaceAll("&nbsp;", "");
						juice = juice.trim();
					}
	
					values.put("juice", juice);
					values.put("juiceindicator", juiceindicator);
				}
			}
		} else if (juice != null && beginDelimiter != null) {
			int index = juice.indexOf(beginDelimiter);
			if (index != -1) {
				juice = juice.substring(index + 1);
				String juiceindicator = juice.substring(0, 1);

				if (juiceindicator.startsWith("e") || juiceindicator.startsWith("E")) {
					juice = "100";
					juiceindicator = "+";
				} else {
					juice = juice.substring(1);
					juice = juice.replaceAll("\u00BD", ".5");
					juice = juice.replaceAll("&nbsp;", "");
					juice = juice.trim();
				}

				values.put("juice", juice);
				values.put("juiceindicator", juiceindicator);
			}
		} else if (juice != null) {
			String juiceindicator = juice.substring(0, 1);
			if (juiceindicator != null && juiceindicator.length() > 0) {
				if (juiceindicator.startsWith("e") || juiceindicator.startsWith("E")) {
					juice = "100";
					juiceindicator = "+";
				} else {
					juice = juice.substring(1);
					juice = juice.replaceAll("\u00BD", ".5");
					juice = juice.replaceAll("&nbsp;", "");
					juice = juice.trim();
				}
			}

			values.put("juice", juice);
			values.put("juiceindicator", juiceindicator);							
		}
		LOGGER.debug("Map<String, String>: " + values);
		LOGGER.info("Exiting parseJuice()");
		return values;
	}

	/**
	 * 
	 * @param timezone
	 * @param DATE_FORMAT
	 * @param team1Date
	 * @param team2Date
	 * @return
	 * @throws BatchException
	 */
	protected Date setupDate(SimpleDateFormat DATE_FORMAT, String team1Date, String team2Date) throws BatchException {
		LOGGER.info("Entering setupDate()");
		LOGGER.debug("timezone: " + timezone);
		LOGGER.debug("DATE_FORMAT: " + DATE_FORMAT);
		LOGGER.debug("team1Date: " + team1Date);
		LOGGER.debug("team2Date: " + team2Date);

		// Now that we have both, set the dates correctly
		String cDate = "";
		Date newDate = null;

		try {
			final Calendar now = Calendar.getInstance();
			int offset = now.get(Calendar.DST_OFFSET);
			cDate = team1Date + " " + String.valueOf(now.get(Calendar.YEAR)) + " " + team2Date;
			cDate += " " + timeZoneLookup(timezone, offset);

			newDate = DATE_FORMAT.parse(cDate);
		} catch (ParseException pe) {
			LOGGER.error("ParseExeption for " + cDate, pe);
			// Throw an exception
			throw new BatchException(AppErrorMessage.SITE_PARSER_EXCEPTION  + " Date parsing exception for " + cDate);
		}

		LOGGER.info("Exiting setupDate()");
		return newDate;
	}

	/**
	 * 
	 * @param DATE_FORMAT
	 * @param gameDate
	 * @return
	 * @throws BatchException
	 */
	protected Date setupGameDate(SimpleDateFormat DATE_FORMAT, String gameDate) throws BatchException {
		LOGGER.info("Entering setupGameDate()");
		LOGGER.debug("timezone: " + timezone);
		LOGGER.debug("DATE_FORMAT: " + DATE_FORMAT);
		LOGGER.debug("gameDate: " + gameDate);
		Date newDate = null;

		try {
			newDate = DATE_FORMAT.parse(gameDate);
		} catch (ParseException pe) {
			LOGGER.error("ParseExeption for " + gameDate, pe);
			// Throw an exception
			throw new BatchException(AppErrorMessage.SITE_PARSER_EXCEPTION  + " Date parsing exception for " + gameDate);
		}

		LOGGER.info("Exiting setupGameDate()");
		return newDate;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected String reformatValues(String value) {
		LOGGER.info("Entering reformatValues()");
		LOGGER.debug("value: " + value);

		if (value != null && value.length() > 0) {
			// first clean up the data
			value = value.trim();
			value = value.replaceAll("&nbsp;", "");
			value = value.replaceAll(" ", "");
			value = value.replaceAll("\\(", "");
			value = value.replaceAll("\\)", "");
			value = value.replaceAll("Â", "");
			value = value.replaceAll("½", ".5");
			value = value.replaceAll("&#189;", ".5");
			value = value.replaceAll("â²", "o");
			value = value.replaceAll("â¼", "u");

			// Need to handle the following scenarios
			// 1) -1 (+101)
			// 2) -1 (-110)
			// 3) +1 (+105)
			// 4) +1 (-110)
			int num = stringOccurencesOf(value, "-");
			if (num == 1) {
				if (!value.startsWith("-")) {
					int index = value.lastIndexOf("-");
					if (index != -1) {
						value = value.substring(0, index) + " " + value.substring(index);
					}
				} else {
					int index = value.lastIndexOf("+");
					if (index != -1) {
						value = value.substring(0, index) + " " + value.substring(index);
					} else {
						index = value.lastIndexOf("E");
						if (index != -1) {
							value = value.substring(0, index) + " " + value.substring(index);
						} else {
							index = value.lastIndexOf("e");
							if (index != -1) {
								value = value.substring(0, index) + " " + value.substring(index);
							}
						}
					}
				}
			} else if (num == 2) {
				int index = value.lastIndexOf("-");
				if (index != -1) {
					value = value.substring(0, index) + " " + value.substring(index);
				}
			} else {
				num = stringOccurencesOf(value, "+");
				if (num == 1) {
					if (!value.startsWith("+")) {
						int index = value.lastIndexOf("+");
						if (index != -1) {
							value = value.substring(0, index) + " " + value.substring(index);
						}					
					} else {
						int index = value.lastIndexOf("E");
						if (index != -1) {
							value = value.substring(0, index) + " " + value.substring(index);
						} else {
							index = value.lastIndexOf("e");
							if (index != -1) {
								value = value.substring(0, index) + " " + value.substring(index);
							}
						}
					}
				} else if (num == 2) {
					int index = value.lastIndexOf("+");
					if (index != -1) {
						value = value.substring(0, index) + " " + value.substring(index);
					}
				} else {
					num = stringOccurencesOf(value, "E");
					if (num == 1) {
						if (!value.startsWith("E")) {
							int index = value.lastIndexOf("E");
							if (index != -1) {
								value = value.substring(0, index) + " " + value.substring(index);
							}					
						} else {
							int index = value.lastIndexOf("e");
							if (index != -1) {
								value = value.substring(0, index) + " " + value.substring(index);
							}
						}
					} else {
						num = stringOccurencesOf(value, "e");
						if (num == 1) {
							if (!value.startsWith("e")) {
								int index = value.lastIndexOf("e");
								if (index != -1) {
									value = value.substring(0, index) + " " + value.substring(index);
								}					
							}
						}
					}
				}
			}

			// Now change everything to be all numbers
			value = value.replaceAll("pk", "+0");
			value = value.replaceAll("PK", "+0");
			value = value.replaceAll("Pk", "+0");
			value = value.replaceAll("even", "+100");
			value = value.replaceAll("Even", "+100");
			value = value.replaceAll("EV", "+100");
			value = value.replaceAll("ev", "+100");
			value = value.replaceAll("Ev", "+100");
			value = value.replaceAll("O", "o");
			value = value.replaceAll("U", "u");
			value = value.replaceAll("Over", "o");
			value = value.replaceAll("Under", "u");
		}
		LOGGER.debug("value: " + value);

		LOGGER.info("Exiting reformatValues()");
		return value;
	}

	/**
	 * 
	 * @param timezone
	 * @param offset
	 * @return
	 */
	protected String timeZoneLookup(String timezone, int offset) {
		LOGGER.info("Entering timeZoneLookup()");
		LOGGER.debug("timezone: " + timezone);
		LOGGER.debug("offset: " + offset);
		String tzDate = "";

		if (timezone != null && timezone.length() > 0) {
			if ("ET".equals(timezone)) {
				if (offset != 0) {
					tzDate = "EDT";
				} else {
					tzDate = "EST";
				}
			} else if ("CT".equals(timezone)) {
				if (offset != 0) {
					tzDate = "CDT";
				} else {
					tzDate = "CST";
				}
			} else if ("MT".equals(timezone)) {
				if (offset != 0) {
					tzDate = "MDT";
				} else {
					tzDate = "MST";
				}
			} else if ("PT".equals(timezone)) {
				if (offset != 0) {
					tzDate = "PDT";
				} else {
					tzDate = "PST";
				}
			}
		}
		
		LOGGER.info("Exiting timeZoneLookup()");
		return tzDate;
	}
	
	/**
	 * 
	 * @param str
	 * @param value
	 * @return
	 */
	protected int stringOccurencesOf(String str, String value) {
		LOGGER.info("Entering stringOccurencesOf()");
		
		// Check for "dangling" character such as +
		if (value != null && "+".equals(value)) {
			value = "\\" + value;
		}
		final Pattern p = Pattern.compile(value);
		final Matcher m = p.matcher(str);

		int count = 0;
		while (m.find()){
		    count +=1;
		}

		LOGGER.info("Exiting stringOccurencesOf()");
		return count;
	}

	/**
	 * 
	 * @param gameDate
	 * @return
	 * @throws BatchException
	 */
	protected String determineDateString(String gameDate) {
		LOGGER.info("Entering determineDateString()");
		LOGGER.debug("gameDate: " + gameDate);
		String theDate = null;

		final Calendar now = Calendar.getInstance();
		int offset = now.get(Calendar.DST_OFFSET);

		int index = gameDate.indexOf(" ");
		if (index != -1) {
			String tempString = gameDate.substring(index + 1);
			theDate = gameDate.substring(0, index) + " ";
			int nIndex = tempString.indexOf(" ");
			if (nIndex != -1) {
				String nString = tempString.substring(nIndex + 1);
				theDate += tempString.substring(0, nIndex) + " " + String.valueOf(now.get(Calendar.YEAR)) + " ";
				theDate += nString;
			}
		}
		theDate += " " + timeZoneLookup(timezone, offset);

		LOGGER.info("Exiting determineDateString()");
		return theDate;
	}
}
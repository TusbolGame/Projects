/**
 * 
 */
package com.wootechnologies.services.dao.sites;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wootechnologies.errorhandling.AppErrorCodes;
import com.wootechnologies.errorhandling.AppErrorMessage;
import com.wootechnologies.errorhandling.BatchException;
import com.wootechnologies.model.PendingEvent;
import com.wootechnologies.services.dao.base.BaseParser;

/**
 * @author jmiller
 *
 */
public abstract class SiteParser extends BaseParser {
	private final static Logger LOGGER = Logger.getLogger(SiteParser.class);

	/**
	 * Constructor
	 */
	public SiteParser() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param xhtml
	 * @param accountName
	 * @param accountId
	 * @return
	 * @throws BatchException
	 */
	public Set<PendingEvent> parsePendingBets(String xhtml, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.info("Exiting parsePendingBets()");
		return null;
	}

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public abstract Map<String, String> parseIndex(String xhtml) throws BatchException;
	
	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public abstract Map<String, String> parseLogin(String xhtml) throws BatchException;

	/**
	 * 
	 * @param xhtml
	 * @param type
	 * @param sport
	 * @return
	 * @throws BatchException
	 */
	public abstract Map<String, String> parseMenu(String xhtml, String[] type, String[] sport) throws BatchException;

	/**
	 * 
	 * @param xhtml
	 * @param type
	 * @param inputFields
	 * @return
	 * @throws BatchException
	 */
	public abstract <T> List<T> parseGames(String xhtml, String type, Map<String, String> inputFields) throws BatchException;

	/**
	 * 
	 * @param xhtml
	 * @param siteTeamPackage
	 * @param type
	 * @return
	 * @throws BatchException
	 */
	public abstract Map<String, String> parseEventSelection(String xhtml, SiteTeamPackage siteTeamPackage, String type) throws BatchException;

	/**
	 * 
	 * @param xhtml
	 * @return
	 * @throws BatchException
	 */
	public abstract String parseTicketNumber(String xhtml) throws BatchException;

	/**
	 * 
	 * @param elements
	 * @return
	 * @throws BatchException
	 */
	protected abstract <T> List<T> getGameData(Elements elements) throws BatchException;

	/**
	 * 
	 * @param inputs
	 * @param inputNum
	 * @return
	 */
	protected Map<String, String> parseInputField(Elements inputs, int inputNum) {
		LOGGER.info("Entering parseInputField()");
		LOGGER.debug("Elements: " + inputs);

		final Map<String, String> inputMap = new HashMap<String, String>();
		if (inputs != null && inputs.size() > 0) {
			LOGGER.debug("inputs.size(): " + inputs.size());
			Element input = null;
			if (inputs.size() == inputNum) {
				input = inputs.get(inputNum - 1);
			} else {
				input = inputs.get(inputNum);
			}

			if (input != null) {
				final String id = input.attr("id");
				final String name = input.attr("name");
				final String value = input.attr("value");
				final String rel = input.attr("rel");
				LOGGER.debug("input id: " + id);
				LOGGER.debug("input name: " + name);
				LOGGER.debug("input value: " + value);

				inputMap.put("id", id);
				inputMap.put("name", name);
				inputMap.put("value", value);
				inputMap.put("rel", rel);
			}
		}

		LOGGER.info("Exiting parseInputField()");
		return inputMap;
	}

	/**
	 * 
	 * @param selects
	 * @return
	 */
	protected Map<String, String> parseSelectField(Elements selects) {
		LOGGER.info("Entering parseSelectField()");
		LOGGER.debug("Elements: " + selects);
		final Map<String, String> selectMap = new HashMap<String, String>();

		if (selects != null && selects.size() > 0) {
			final Element select = selects.get(0);
			if (select != null) {
				final String id = select.attr("id");
				final String name = select.attr("name");
				final String value = select.attr("value");
				final String forAttr = select.attr("for");
				LOGGER.debug("select id: " + id);
				LOGGER.debug("select name: " + name);
				LOGGER.debug("select value: " + value);
				LOGGER.debug("select for: " + forAttr);

				selectMap.put("id", id);
				selectMap.put("name", name);
				selectMap.put("value", value);
				selectMap.put("for", forAttr);
			}
		}

		LOGGER.info("Exiting parseSelectField()");
		return selectMap;
	}

	/**
	 * 
	 * @param selects
	 * @param num
	 * @param selectMap
	 */
	protected void parseSelectFieldByNumBlank(Elements selects, int num, Map<String, String> selectMap) {
		LOGGER.info("Entering parseSelectFieldByNumBlank()");
		LOGGER.debug("Elements: " + selects);
		LOGGER.debug("num: " + num);

		if (selects != null && selects.size() > 0) {
			final Element select = selects.get(num);
			if (select != null) {
				final String name = select.attr("name");
				selectMap.put(name, "");
			}
		}

		LOGGER.info("Exiting parseSelectFieldByNumBlank()");
	}

	/**
	 * 
	 * @param selects
	 * @param num
	 * @param selectMap
	 */
	protected void parseSelectFieldByNum(Elements selects, int num, Map<String, String> selectMap) {
		LOGGER.info("Entering parseSelectFieldByNum()");
		LOGGER.debug("Elements: " + selects);
		LOGGER.debug("num: " + num);

		if (selects != null && selects.size() > 0) {
			final Element select = selects.get(num);
			if (select != null) {
				final String id = select.attr("id");
				final String name = select.attr("name");
				final String value = select.attr("value");
				final String forAttr = select.attr("for");
				LOGGER.debug("select id: " + id);
				LOGGER.debug("select name: " + name);
				LOGGER.debug("select value: " + value);
				LOGGER.debug("select for: " + forAttr);

				selectMap.put("id", id);
				selectMap.put("name", name);
				selectMap.put("value", value);
				selectMap.put("for", forAttr);
			}
		}

		LOGGER.info("Exiting parseSelectFieldByNum()");
	}

	/**
	 * 
	 * @param selects
	 * @param map
	 * @return
	 */
	protected Map<String, String> parseSelectFieldWithMap(Elements selects, Map<String, String> map) {
		LOGGER.info("Entering parseSelectFieldWithMap()");
		LOGGER.debug("Elements: " + selects);

		if (selects != null && selects.size() > 0) {
			final Element select = selects.get(0);
			if (select != null) {
				final String id = select.attr("id");
				final String name = select.attr("name");
				final String value = select.attr("value");
				final String forAttr = select.attr("for");
				LOGGER.debug("select id: " + id);
				LOGGER.debug("select name: " + name);
				LOGGER.debug("select value: " + value);
				LOGGER.debug("select for: " + forAttr);

				map.put("id", id);
				map.put("name", name);
				map.put("value", value);
				map.put("for", forAttr);
			}
		}

		LOGGER.info("Exiting parseSelectFieldWithMap()");
		return map;
	}

	/**
	 * 
	 * @param options
	 * @return
	 */
	protected Map<String, SelectOptionData> parseSelectOptionField(Elements options) {
		LOGGER.info("Entering parseSelectOptionField()");
		LOGGER.debug("Elements: " + options);
		final Map<String, SelectOptionData> selectOptionMap = new HashMap<String, SelectOptionData>();

		// Validate there are options
		if (options != null && options.size() > 0) {
			for (int x = 0; x < options.size(); x++) {
				final Element option = options.get(x);
				if (option != null) {
					final String id = option.attr("id");
					final String name = option.attr("name");
					final String className = option.attr("className");
					final String value = option.attr("value");
					final String selected = option.attr("selected");
					final String data = option.html();
					
					final SelectOptionData sod = new SelectOptionData();
					sod.setClassName(className);
					sod.setData(data);
					sod.setId(id);
					sod.setName(name);
					sod.setSelected(selected);
					sod.setValue(value);
					LOGGER.debug("SelectOptionData: " + sod);

					selectOptionMap.put(Integer.toString(x), sod);
				}
			}
		}

		LOGGER.info("Exiting parseSelectOptionField()");
		return selectOptionMap;
	}

	/**
	 * 
	 * @param options
	 * @return
	 */
	protected Map<String, SelectOptionData> parseSpreadField(Elements options) {
		LOGGER.info("Entering parseSelectOptionField()");
		LOGGER.debug("Elements: " + options);
		final Map<String, SelectOptionData> selectOptionMap = new HashMap<String, SelectOptionData>();

		// Validate there are options
		if (options != null && options.size() > 0) {
			for (int x = 0; x < options.size(); x++) {
				final Element option = options.get(x);
				if (option != null) {
					final String id = option.attr("id");
					final String name = option.attr("name");
					final String className = option.attr("className");
					final String value = option.attr("value");
					final String selected = option.attr("selected");
					final String data = option.html();
					
					final SelectOptionData sod = new SelectOptionData();
					sod.setClassName(className);
					sod.setData(data);
					sod.setId(id);
					sod.setName(name);
					sod.setSelected(selected);
					sod.setValue(value);
					LOGGER.debug("SelectOptionData: " + sod);

					selectOptionMap.put(Integer.toString(x), sod);
				}
			}
		}

		LOGGER.info("Exiting parseSelectOptionField()");
		return selectOptionMap;
	}

	/**
	 * 
	 * @param spreadString
	 * @param optionNumber
	 * @param beginDelimiter
	 * @param endDelimiter
	 * @param team
	 * @return
	 */
	protected SiteTeamPackage parseSpreadData(String spreadString, int optionNumber, String beginDelimiter, String endDelimiter, SiteTeamPackage team) {
		LOGGER.info("Entering parseSpreadData()");
		LOGGER.debug("spreadString: " + spreadString);
		LOGGER.debug("optionNumber: " + optionNumber);
		LOGGER.debug("beginDelimiter: " + beginDelimiter);
		LOGGER.debug("endDelimiter: " + endDelimiter);
		LOGGER.debug("SiteTeamPackage: " + team);

		// Format is +6 -110 or +6 (-110)
		if (spreadString != null && spreadString.length() > 0) {
			spreadString = spreadString.replaceAll("&nbsp;", " ");
			spreadString = spreadString.trim();
			int index = spreadString.indexOf(beginDelimiter);
			
			// Check for valid delimiter
			if (index != -1) {
				final Map<String, String> vals = parseSpread(spreadString, index);
				team.addGameSpreadOptionIndicator(Integer.toString(optionNumber), vals.get("valindicator"));
				team.addGameSpreadOptionNumber(Integer.toString(optionNumber), vals.get("val"));

				final String juice = spreadString.substring(index);
				Map<String, String> juices = parseJuice(juice, beginDelimiter, endDelimiter);
				team.addGameSpreadOptionJuiceIndicator(Integer.toString(optionNumber), juices.get("juiceindicator"));
				team.addGameSpreadOptionJuiceNumber(Integer.toString(optionNumber), juices.get("juice"));
			}
		}

		LOGGER.info("Exiting parseSpreadData()");
		return team;
	}

	/**
	 * 
	 * @param mlString
	 * @param optionNumber
	 * @param team
	 * @return
	 */
	protected SiteTeamPackage parseMlData(String mlString, int optionNumber, SiteTeamPackage team) {
		LOGGER.info("Entering parseMlData()");
		LOGGER.debug("mlString: " + mlString);
		LOGGER.debug("optionNumber: " + optionNumber);
		LOGGER.debug("SiteTeamPackage: " + team);

		// Format is -110
		if (mlString != null && mlString.length() > 0) {
			mlString = mlString.replaceAll("&nbsp;", " ");
			mlString = mlString.trim();
			final Map<String, String> juices = parseJuice(mlString, null, null);
			LOGGER.debug("juices: " + juices);

			team.addGameMLOptionJuiceIndicator(Integer.toString(optionNumber), juices.get("juiceindicator"));
			team.addGameMLOptionJuiceNumber(Integer.toString(optionNumber), juices.get("juice"));
		}
		LOGGER.info("Exiting parseMlData()");
		return team;
	}

	/**
	 * 
	 * @param totalString
	 * @param optionNumber
	 * @param beginDelimiter
	 * @param endDelimiter
	 * @param team
	 * @return
	 */
	protected SiteTeamPackage parseTotalData(String totalString, int optionNumber, String beginDelimiter, String endDelimiter, SiteTeamPackage team) {
		LOGGER.info("Entering parseTotalData()");
		LOGGER.debug("totalString: " + totalString);
		LOGGER.debug("optionNumber: " + optionNumber);
		LOGGER.debug("beginDelimiter: " + beginDelimiter);
		LOGGER.debug("endDelimiter: " + endDelimiter);
		LOGGER.debug("SiteTeamPackage: " + team);

		// Format is o42 -110 or o42 (-110)
		if (totalString != null && totalString.length() > 0) {
			totalString = totalString.replaceAll("&nbsp;", " ");
			totalString = totalString.trim();

			if (beginDelimiter == null) {
				int minusindex = totalString.indexOf("-");
				if (minusindex != -1) {
					final Map<String, String> vals = parseTotal(totalString.substring(0 , minusindex), -1);
					if (vals != null && !vals.isEmpty()) {
						team.addGameTotalOptionIndicator(Integer.toString(optionNumber), vals.get("valindicator"));
						team.addGameTotalOptionNumber(Integer.toString(optionNumber), vals.get("val"));
					}
					final Map<String, String> juices = parseJuice(totalString.substring(minusindex), null, null);
					if (juices != null && !juices.isEmpty()) {
						team.addGameTotalOptionJuiceIndicator(Integer.toString(optionNumber), juices.get("juiceindicator"));
						team.addGameTotalOptionJuiceNumber(Integer.toString(optionNumber), juices.get("juice"));
					}
				} else {
					int plusindex = totalString.indexOf("+");
					if (plusindex != -1) {
						final Map<String, String> vals = parseTotal(totalString.substring(0 , plusindex), -1);
						if (vals != null && !vals.isEmpty()) {
							team.addGameTotalOptionIndicator(Integer.toString(optionNumber), vals.get("valindicator"));
							team.addGameTotalOptionNumber(Integer.toString(optionNumber), vals.get("val"));
						}
						final Map<String, String> juices = parseJuice(totalString.substring(plusindex), null, null);
						if (juices != null && !juices.isEmpty()) {
							team.addGameTotalOptionJuiceIndicator(Integer.toString(optionNumber), juices.get("juiceindicator"));
							team.addGameTotalOptionJuiceNumber(Integer.toString(optionNumber), juices.get("juice"));
						}
					}
				}
			} else {
				int index = -1;
				
				// First check for O 42 -110 scenario
				if (totalString.substring(1, 2) != null && !totalString.substring(1, 2).equals(" ")) {
					index = totalString.indexOf(beginDelimiter);				
				} else {
					totalString = totalString.substring(0, 1) + totalString.substring(2);
					index = totalString.indexOf(beginDelimiter);
				}
	
				// Check for valid delimiter
				if (index != -1) {
					final Map<String, String> vals = parseTotal(totalString, index);
					if (vals != null && !vals.isEmpty()) {
						team.addGameTotalOptionIndicator(Integer.toString(optionNumber), vals.get("valindicator"));
						team.addGameTotalOptionNumber(Integer.toString(optionNumber), vals.get("val"));
					}
	
					final String juice = totalString.substring(index);
					Map<String, String> juices = parseJuice(juice, beginDelimiter, endDelimiter);
					if (juices != null && !juices.isEmpty()) {
						team.addGameTotalOptionJuiceIndicator(Integer.toString(optionNumber), juices.get("juiceindicator"));
						team.addGameTotalOptionJuiceNumber(Integer.toString(optionNumber), juices.get("juice"));
					}
				}
			}
		}
		LOGGER.info("Exiting parseTotalData()");
		return team;
	}

	/**
	 * 
	 * @param spreadString
	 * @param index
	 * @return
	 */
	protected Map<String, String> parseSpread(String spreadString, int index) {
		LOGGER.info("Entering parseSpread()");
		LOGGER.debug("spreadString: " + spreadString);
		LOGGER.debug("index: " + index);
		final Map<String, String> values = new HashMap<String, String>();

		// Check for valid string
		if (spreadString != null) {
			String val = null;
			if (index != -1) {
				val = spreadString.substring(0, index);
			} else {
				val = spreadString;
			}

			String valindicator = val.substring(0,1);
			if (valindicator != null) {
				if ("-".equals(valindicator) || "+".equals(valindicator)) {
					val = val.substring(1);		
				} else {
					// check for a PK
					if ("p".equalsIgnoreCase(valindicator)) {
						valindicator = "+";
						val = "0";
					} else if ("e".equalsIgnoreCase(valindicator)) {
						valindicator = "+";
						val = "0";
					}
				}
			}
			val = val.replaceAll("\u00BD", ".5");
			val = val.replaceAll("&nbsp;", "");
			val = val.trim();
			values.put("val", val);
			values.put("valindicator", valindicator);
		}

		LOGGER.info("values: " + values);
		LOGGER.info("Exiting parseSpread()");
		return values;
	}

	/**
	 * 
	 * @param totalString
	 * @param index
	 * @return
	 */
	protected Map<String, String> parseTotal(String totalString, int index) {
		LOGGER.info("Entering parseTotal()");
		LOGGER.debug("totalString: " + totalString);
		LOGGER.debug("index: " + index);
		final Map<String, String> values = new HashMap<String, String>();

		// Check for valid string
		if (totalString != null && index != -1) {
			if (totalString.substring(1, 2) != null && !totalString.substring(1, 2).equals(" ")) {
				// Do nothing
			} else {
				totalString = totalString.substring(0, 1) + totalString.substring(2);
			}

			String val = null;
			if (index != -1) {
				val = totalString.substring(0, index);
			} else {
				val = totalString;
			}

			String valindicator = val.substring(0,1);
			if (valindicator != null) {
				if ("o".equalsIgnoreCase(valindicator) || "u".equalsIgnoreCase(valindicator)) {
					val = val.substring(1);
				}
			}
			val = val.replaceAll("\u00BD", ".5");
			val = val.replaceAll("&nbsp;", "");
			val = val.trim();
			values.put("val", val);
			values.put("valindicator", valindicator);
		} else if (totalString != null && totalString.length() > 0) {
			String val = null;
			String valindicator = totalString.substring(0,1);
			LOGGER.debug("valindicator: " + valindicator);
			if (valindicator != null) {
				if ("o".equalsIgnoreCase(valindicator) || "u".equalsIgnoreCase(valindicator)) {
					val = totalString.substring(1);
					val = super.reformatValues(val);
					LOGGER.debug("val: " + val);
					values.put("val", val);
					values.put("valindicator", valindicator);
				}
			}
		}

		LOGGER.info("values: " + values);
		LOGGER.info("Exiting parseTotal()");
		return values;
	}

	/**
	 * 
	 * @param doc
	 * @param id
	 * @return
	 */
	protected String getValueById(Document doc, String id) {
		LOGGER.info("Entering getValueById()");
		LOGGER.debug("id: " + id);
		String retValue = "";

		final Element element = doc.getElementById(id);
		if (element != null) {
			retValue = element.attr("value");
		}

		LOGGER.info("Value: " + retValue);
		LOGGER.info("Exiting getValueById()");
		return retValue;
	}

	/**
	 * 
	 * @param doc
	 * @param name
	 * @return
	 */
	protected String getValueByName(Document doc, String name) {
		LOGGER.info("Entering getValueByName()");
		LOGGER.debug("name: " + name);
		String retValue = "";

		final Elements elements = doc.getElementsByAttribute("name");
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			Element element = elements.get(x);
			if (element != null && name.equals(element.attr("name"))) {
				retValue = element.attr("value");
			}
		}

		LOGGER.info("Value: " + retValue);
		LOGGER.info("Exiting getValueByName()");
		return retValue;
	}

	/**
	 * 
	 * @param doc
	 * @param name
	 * @return
	 */
	protected String getValueByName(Document doc, String elementName, String name) {
		LOGGER.info("Entering getValueByName()");
		LOGGER.debug("name: " + name);
		String retValue = "";

		final Elements elements = doc.select(elementName);
		if (elements != null && elements.size() > 0) {
			for (int x = 0; x < elements.size(); x++) {
				final Element element = elements.get(x);
				if (element != null) {
					String eName = element.attr("name");
					if (eName != null && eName.length() > 0 && eName.equals(name)) {
						retValue = element.attr("value");
					}
				}
			}
		}

		LOGGER.info("Value: " + retValue);
		LOGGER.info("Exiting getValueByName()");
		return retValue;
	}

	/**
	 * 
	 * @param doc
	 * @param name
	 * @param attrName
	 * @return
	 */
	protected String getElementByName(Document doc, String name, String attrName) {
		LOGGER.info("Entering getElementByName()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("attrName: " + attrName);
		String retValue = "";

		final Elements elements = doc.select(name);
		if (elements != null && elements.size() > 0) {
			final Element element = elements.get(0);
			if (element != null) {
				retValue = element.attr(attrName);
			}
		}

		LOGGER.info("Exiting getElementByName()");
		return retValue;
	}

	/**
	 * 
	 * @param doc
	 * @param name
	 * @param attrName
	 * @param inputFields
	 */
	protected void getAllElementsByName(Document doc, String name, String attrName, Map<String, String> inputFields) {
		LOGGER.info("Entering getAllElementsByName()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("attrName: " + attrName);

		final Elements elements = doc.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element element = elements.get(x);
			String dataValue = null;
			if (element != null) {
				String eleName = element.attr("name");
				dataValue = element.attr(attrName);
				if (dataValue == null || dataValue.length() == 0) {
					String type = element.attr("type");
					if (type != null && type.equals("checkbox")) {
						dataValue = "on";
					} else {
						dataValue = "";
					}
				}
				inputFields.put(eleName, dataValue);
			}
		}

		LOGGER.info("Exiting getAllElementsByName()");
	}

	/**
	 * 
	 * @param element
	 * @param name
	 * @param attrName
	 * @param inputFields
	 */
	protected Map<String, String> getAllElementsByNameByElement(Element element, String name, String attrName, Map<String, String> inputFields) {
		LOGGER.info("Entering getAllElementsByNameByElement()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("attrName: " + attrName);

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			String dataValue = null;
			if (ele != null) {
				String eleName = ele.attr("name");
				dataValue = ele.attr(attrName);
				if (dataValue == null || dataValue.length() == 0) {
					String type = ele.attr("type");
					if (type != null && type.equals("checkbox")) {
						dataValue = "on";
					} else {
						dataValue = "";
					}
				}
				inputFields.put(eleName, dataValue);
			}
		}

		LOGGER.info("Exiting getAllElementsByNameByElement()");
		return inputFields;
	}

	/**
	 * 
	 * @param doc
	 * @param name
	 * @param attrName
	 * @param inputFields
	 */
	protected void getAllInputsByNameWithoutCheckbox(Document doc, String name, String attrName, Map<String, String> inputFields) {
		LOGGER.info("Entering getAllInputsByNameWithoutCheckbox()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("attrName: " + attrName);

		final Elements elements = doc.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element element = elements.get(x);
			String dataValue = null;
			if (element != null) {
				String eleName = element.attr("name");
				String type = element.attr("type");
				if (type != null && !type.equals("checkbox") && eleName != null) {
					dataValue = element.attr(attrName);
					inputFields.put(eleName, dataValue);
				}
			}
		}

		LOGGER.info("Exiting getAllInputsByNameWithoutCheckbox()");
	}

	/**
	 * 
	 * @param doc
	 * @param name
	 * @param attrName
	 * @param inputFields
	 * @param types
	 * @return
	 */
	protected Map<String, String> getAllElementsByNameForClass(Document doc, String name, String attrName, Map<String, String> inputFields, String[] types) {
		LOGGER.info("Entering getAllElementsByNameForClass()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("attrName: " + attrName);

		final Elements elements = doc.select(name);
		if (elements != null && elements.size() > 0) {
			for (int y = 0; (elements != null && y < elements.size()); y++) {
				final Element element = elements.get(y);
				LOGGER.debug("Element: " + element);
				String className = element.attr("name");
				for (int x = 0; x < types.length; x++) {
					if (className != null && types[x].equals(className)) {
						String dataValue = element.attr(attrName);
						if (dataValue == null || dataValue.length() == 0) {
							String type = element.attr("type");
							if (type != null && type.equals("checkbox")) {
								dataValue = "on";
							} else {
								dataValue = "";
							}
						}
						inputFields.put(element.attr("name"), dataValue);
					}
				}
			}
		}

		LOGGER.info("Exiting getAllElementsByNameForClass()");
		return inputFields;
	}

	/**
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	protected String getHtmlFromElement(Element element, String name, int position, boolean gethtml) {
		LOGGER.info("Entering getHtmlFromElement()");
		LOGGER.debug("Element: " + element);
		LOGGER.debug("name: " + name);
		LOGGER.debug("position: " + position);
		String retValue = null;

		final Elements elements = element.select(name);
		if (elements != null && elements.size() > 0) {
			final Element ele = elements.get(position);
			if (ele != null) {
				retValue = ele.html();
				if (retValue != null && retValue.length() > 0) {
					retValue = retValue.replaceAll("&nbsp;", "");
					retValue = retValue.trim();
				}
			}
		} else if (gethtml) {
			retValue = element.html();
			if (retValue != null) {
				retValue = retValue.replaceAll("&nbsp;", "");
				retValue = retValue.trim();
			}
		}

		LOGGER.info("Exiting getHtmlFromElement()");
		return retValue;
	}

	/**
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	protected String getHtmlFromAllElements(Element element, String name) {
		LOGGER.info("Entering getHtmlFromAllElements()");
		LOGGER.debug("Element: " + element);
		LOGGER.debug("name: " + name);
		String retValue = "";

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			if (ele != null) {
				retValue = ele.html();
				if (retValue != null && retValue.length() > 0) {
					retValue = retValue.replaceAll("&nbsp;", "");
					retValue = retValue.trim();
				}
			}
		}

		LOGGER.info("Exiting getHtmlFromAllElements()");
		return retValue;
	}

	/**
	 * 
	 * @param doc
	 * @param name
	 * @param position
	 * @return
	 */
	protected String getHtmlFromDocument(Document doc, String name, int position) {
		LOGGER.info("Entering getHtmlFromDocument()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("position: " + position);
		String retValue = "";

		final Elements elements = doc.select(name);
		if (elements != null) {
			final Element ele = elements.get(position);
			if (ele != null) {
				retValue = ele.html();
				if (retValue != null && retValue.length() > 0) {
					retValue = retValue.replaceAll("&nbsp;", "");
					retValue = retValue.trim();
				}
			}
		}

		LOGGER.info("Exiting getHtmlFromDocument()");
		return retValue;
	}

	/**
	 * 
	 * @param doc
	 * @param name
	 * @param position
	 * @return
	 */
	protected String getHtmlFromDocumentByClass(Document doc, String name, int position) {
		LOGGER.info("Entering getHtmlFromDocumentByClass()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("position: " + position);
		String retValue = "";

		final Elements elements = doc.getElementsByClass(name);
		if (elements != null && elements.size() > 0) {
			final Element ele = elements.get(position);
			if (ele != null) {
				retValue = ele.html();
				if (retValue != null && retValue.length() > 0) {
					retValue = retValue.replaceAll("&nbsp;", "");
					retValue = retValue.trim();
				}
			}
		}

		LOGGER.info("Exiting getHtmlFromDocumentByClass()");
		return retValue;
	}

	/**
	 * 
	 * @param element
	 * @param name
	 * @param position
	 * @return
	 */
	protected String getHtmlByClassName(Element element, String name, int position) {
		LOGGER.info("Entering getHtmlByClassName()");
		LOGGER.debug("Element: " + element);
		LOGGER.debug("name: " + name);
		LOGGER.debug("position: " + position);
		String retValue = "";

		final Elements elements = element.getElementsByClass(name);
		if (elements != null && elements.size() > 0) {
			final Element ele = elements.get(position);
			if (ele != null) {
				retValue = ele.html();
				if (retValue != null && retValue.length() > 0) {
					retValue = retValue.replaceAll("&nbsp;", "");
					retValue = retValue.trim();
				}
			}
		}

		LOGGER.info("Exiting getHtmlByClassName()");
		return retValue;
	}

	/**
	 * 
	 * @param element
	 * @param name
	 * @param attrName
	 * @param inputFields
	 */
	protected void getAllElementsByName(Element element, String name, String attrName, Map<String, String> inputFields) {
		LOGGER.info("Entering getAllElementsByName()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("attrName: " + attrName);

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			String dataValue = null;
			if (ele != null) {
				String eleName = ele.attr("name");
				dataValue = ele.attr(attrName);
				if (dataValue == null || dataValue.length() == 0) {
					String type = ele.attr("type");
					if (type != null && type.equals("checkbox")) {
						dataValue = "on";
					} else {
						dataValue = "";
					}
				}
				inputFields.put(eleName, dataValue);
			}
		}

		LOGGER.info("Exiting getAllElementsByName()");
	}

	/**
	 * 
	 * @param element
	 * @param name
	 * @param types
	 * @param inputFields
	 */
	protected void getAllElementsByType(Element element, String name, String[] types, Map<String, String> inputFields) {
		LOGGER.info("Entering getAllElementsByType()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("types: " + java.util.Arrays.toString(types));

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			if (ele != null) {
				final String type = ele.attr("type");
				for (int z = 0; z < types.length; z++) {
					if (type != null && types[z].equals(type)) {
						final String elementName = ele.attr("name");
						final String elementValue = ele.attr("value");
						if (elementName != null && elementName.length() > 0) {
							inputFields.put(elementName, elementValue);
						}
					}
				}
			}
		}

		LOGGER.info("Exiting getAllElementsByType()");
	}

	/**
	 * 
	 * @param element
	 * @param name
	 * @param types
	 * @param inputFields
	 */
	protected void getAllElementsByTypeWithCheckbox(Element element, String name, String[] types, Map<String, String> inputFields) {
		LOGGER.info("Entering getAllElementsByTypeWithCheckbox()");
		LOGGER.debug("name: " + name);
		LOGGER.debug("types: " + java.util.Arrays.toString(types));

		final Elements elements = element.select(name);
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element ele = elements.get(x);
			if (ele != null) {
				final String type = ele.attr("type");
				for (int z = 0; z < types.length; z++) {
					if (type != null && types[z].equals(type)) {
						final String elementName = ele.attr("name");
						String elementValue = ele.attr("value");
						if (type.equals("checkbox")) {
							elementValue = "on";
						}
						inputFields.put(elementName, elementValue);
					}
				}
			}
		}

		LOGGER.info("Exiting getAllElementsByTypeWithCheckbox()");
	}

	/**
	 * 
	 * @param element
	 * @param inputFields
	 */
	protected void getAllSelects(Element element, Map<String, String> inputFields) {
		LOGGER.info("Entering getAllElementsByType()");

		final Elements elements = element.select("select");
		for (int x = 0; (elements != null && x < elements.size()); x++) {
			final Element select = elements.get(x);
			if (select != null) {
				final String selectName = select.attr("name");
				inputFields.put(selectName, "0");
			}
		}

		LOGGER.info("Exiting getAllElementsByType()");
	}

	/**
	 * 
	 * @param options
	 * @param team
	 * @param begin
	 * @param end
	 * @return
	 * @throws BatchException
	 */
	protected SiteTeamPackage parseSpreadSelectOption(Elements options, SiteTeamPackage team, String begin, String end) throws BatchException {
		LOGGER.info("Entering parseSpreadSelectOption()");
		LOGGER.debug("Elements: " + options);
		LOGGER.debug("SiteTeamPackage: " + team);
		LOGGER.debug("begin: " + begin);
		LOGGER.debug("end: " + end);
		
		final Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
		for (int x = 0; (optionsMap != null && x < optionsMap.size()); x++) {
			final SelectOptionData option = optionsMap.get(Integer.toString(x));
			if (option != null) {
				team.addGameSpreadOptionValue(Integer.toString(x), option.getValue());

				// -2½  +200; Now parse the data
				team = parseSpreadData(reformatValues(option.getData()), x, " ", null, team);
			} else {
				// Throw an exception
				throw new BatchException(AppErrorCodes.SITE_PARSER_EXCEPTION,  
						AppErrorMessage.SITE_PARSER_EXCEPTION, "SiteParser option is null");					
			}
		}
		
		LOGGER.info("Exiting parseSpreadSelectOption()");
		return team;
	}

	/**
	 * 
	 * @param options
	 * @param team
	 * @param begin
	 * @param end
	 * @return
	 * @throws BatchException
	 */
	protected SiteTeamPackage parseTotalSelectOption(Elements options, SiteTeamPackage team, String begin, String end) throws BatchException {
		LOGGER.info("Entering parseTotalSelectOption()");
		LOGGER.debug("Elements: " + options);
		LOGGER.debug("SiteTeamPackage: " + team);
		LOGGER.debug("begin: " + begin);
		LOGGER.debug("end: " + end);
		
		final Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
		for (int x = 0; (optionsMap != null && x < optionsMap.size()); x++) {
			final SelectOptionData option = optionsMap.get(Integer.toString(x));
			if (option != null) {
				team.addGameTotalOptionValue(Integer.toString(x), option.getValue());

				// o 42½  +200; Now parse the data
				team = parseTotalData(reformatValues(option.getData()), x, " ", null, team);
			} else {
				// Throw an exception
				throw new BatchException(AppErrorCodes.SITE_PARSER_EXCEPTION,  
						AppErrorMessage.SITE_PARSER_EXCEPTION, "SiteParser option is null");					
			}
		}
		
		LOGGER.info("Exiting parseTotalSelectOption()");
		return team;
	}

	/**
	 * 
	 * @param options
	 * @param team
	 * @param begin
	 * @param end
	 * @return
	 * @throws BatchException
	 * newly added on 31-10-18
	 */
	protected SiteTeamPackage parseTeamTotalSelectOption(Elements options, SiteTeamPackage team, String begin, String end) throws BatchException {
		LOGGER.info("Entering parseTeamTotalSelectOption()");
		LOGGER.debug("Elements: " + options);
		LOGGER.debug("SiteTeamPackage: " + team);
		LOGGER.debug("begin: " + begin);
		LOGGER.debug("end: " + end);
		
		final Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
		for (int x = 0; (optionsMap != null && x < optionsMap.size()); x++) {
			final SelectOptionData option = optionsMap.get(Integer.toString(x));
			if (option != null) {
				team.addGameTeamTotalOptionValue(Integer.toString(x), option.getValue());

				// o 42½  +200; Now parse the data
				team = parseTotalData(reformatValues(option.getData()), x, " ", null, team);
			} else {
				// Throw an exception
				throw new BatchException(AppErrorCodes.SITE_PARSER_EXCEPTION,  
						AppErrorMessage.SITE_PARSER_EXCEPTION, "SiteParser option is null");					
			}
		}
		
		LOGGER.info("Exiting parseTeamTotalSelectOption()");
		return team;
	}
	
	/**
	 * 
	 * @param options
	 * @param team
	 * @param begin
	 * @param end
	 * @return
	 * @throws BatchException
	 * newly added on 31-10-18
	 */
	protected SiteTeamPackage parseTeamTotalSelectOptionSecond(Elements options, SiteTeamPackage team, String begin, String end) throws BatchException {
		LOGGER.info("Entering parseTeamTotalSelectOption()");
		LOGGER.debug("Elements: " + options);
		LOGGER.debug("SiteTeamPackage: " + team);
		LOGGER.debug("begin: " + begin);
		LOGGER.debug("end: " + end);
		
		final Map<String, SelectOptionData> optionsMap = parseSelectOptionField(options);
		for (int x = 0; (optionsMap != null && x < optionsMap.size()); x++) {
			final SelectOptionData option = optionsMap.get(Integer.toString(x));
			if (option != null) {
				team.addGameTeamTotalOptionValueSecond(Integer.toString(x), option.getValue());

				// o 42½  +200; Now parse the data
				team = parseTotalData(reformatValues(option.getData()), x, " ", null, team);
			} else {
				// Throw an exception
				throw new BatchException(AppErrorCodes.SITE_PARSER_EXCEPTION,  
						AppErrorMessage.SITE_PARSER_EXCEPTION, "SiteParser option is null");					
			}
		}
		
		LOGGER.info("Exiting parseTeamTotalSelectOption()");
		return team;
	}
	
	/**
	 * 
	 * @param totalString
	 * @param optionNumber
	 * @param beginDelimiter
	 * @param endDelimiter
	 * @param team
	 * @return
	 * newly added on 31-10-18
	 */
	protected SiteTeamPackage parseTeamTotalData(String totalString, int optionNumber, String beginDelimiter, String endDelimiter, SiteTeamPackage team) {
		LOGGER.info("Entering parseTeamTotalData()");
		LOGGER.debug("totalString: " + totalString);
		LOGGER.debug("optionNumber: " + optionNumber);
		LOGGER.debug("beginDelimiter: " + beginDelimiter);
		LOGGER.debug("endDelimiter: " + endDelimiter);
		LOGGER.debug("SiteTeamPackage: " + team);

		// Format is o42 -110 or o42 (-110)
		if (totalString != null && totalString.length() > 0) {
			totalString = totalString.replaceAll("&nbsp;", " ");
			totalString = totalString.trim();

			if (beginDelimiter == null) {
				int minusindex = totalString.indexOf("-");
				if (minusindex != -1) {
					final Map<String, String> vals = parseTotal(totalString.substring(0 , minusindex), -1);
					if (vals != null && !vals.isEmpty()) {
						team.addGameTeamTotalOptionIndicator(Integer.toString(optionNumber), vals.get("valindicator"));
						team.addGameTeamTotalOptionNumber(Integer.toString(optionNumber), vals.get("val"));
					}
					final Map<String, String> juices = parseJuice(totalString.substring(minusindex), null, null);
					if (juices != null && !juices.isEmpty()) {
						team.addGameTeamTotalOptionJuiceIndicator(Integer.toString(optionNumber), juices.get("juiceindicator"));
						team.addGameTeamTotalOptionJuiceNumber(Integer.toString(optionNumber), juices.get("juice"));
					}
				} else {
					int plusindex = totalString.indexOf("+");
					if (plusindex != -1) {
						final Map<String, String> vals = parseTotal(totalString.substring(0 , plusindex), -1);
						if (vals != null && !vals.isEmpty()) {
							team.addGameTeamTotalOptionIndicator(Integer.toString(optionNumber), vals.get("valindicator"));
							team.addGameTeamTotalOptionNumber(Integer.toString(optionNumber), vals.get("val"));
						}
						final Map<String, String> juices = parseJuice(totalString.substring(plusindex), null, null);
						if (juices != null && !juices.isEmpty()) {
							team.addGameTeamTotalOptionJuiceIndicator(Integer.toString(optionNumber), juices.get("juiceindicator"));
							team.addGameTeamTotalOptionJuiceNumber(Integer.toString(optionNumber), juices.get("juice"));
						}
					}
				}
			} else {
				int index = -1;
				
				// First check for O 42 -110 scenario
				if (totalString.substring(1, 2) != null && !totalString.substring(1, 2).equals(" ")) {
					index = totalString.indexOf(beginDelimiter);				
				} else {
					totalString = totalString.substring(0, 1) + totalString.substring(2);
					index = totalString.indexOf(beginDelimiter);
				}
	
				// Check for valid delimiter
				if (index != -1) {
					final Map<String, String> vals = parseTotal(totalString, index);
					if (vals != null && !vals.isEmpty()) {
						team.addGameTeamTotalOptionIndicator(Integer.toString(optionNumber), vals.get("valindicator"));
						team.addGameTeamTotalOptionNumber(Integer.toString(optionNumber), vals.get("val"));
					}
	
					final String juice = totalString.substring(index);
					Map<String, String> juices = parseJuice(juice, beginDelimiter, endDelimiter);
					if (juices != null && !juices.isEmpty()) {
						team.addGameTeamTotalOptionJuiceIndicator(Integer.toString(optionNumber), juices.get("juiceindicator"));
						team.addGameTeamTotalOptionJuiceNumber(Integer.toString(optionNumber), juices.get("juice"));
					}
				}
			}
		}
		LOGGER.info("Exiting parseTeamTotalData()");
		return team;
	}
	
	
	/**
	 * 
	 * @param totalString
	 * @param optionNumber
	 * @param beginDelimiter
	 * @param endDelimiter
	 * @param team
	 * @return
	 * newly added on 31-10-18
	 */
	protected SiteTeamPackage parseTeamTotalDataSecond(String totalString, int optionNumber, String beginDelimiter, String endDelimiter, SiteTeamPackage team) {
		LOGGER.info("Entering parseTeamTotalData()");
		LOGGER.debug("totalString: " + totalString);
		LOGGER.debug("optionNumber: " + optionNumber);
		LOGGER.debug("beginDelimiter: " + beginDelimiter);
		LOGGER.debug("endDelimiter: " + endDelimiter);
		LOGGER.debug("SiteTeamPackage: " + team);

		// Format is o42 -110 or o42 (-110)
		if (totalString != null && totalString.length() > 0) {
			totalString = totalString.replaceAll("&nbsp;", " ");
			totalString = totalString.trim();

			if (beginDelimiter == null) {
				int minusindex = totalString.indexOf("-");
				if (minusindex != -1) {
					final Map<String, String> vals = parseTotal(totalString.substring(0 , minusindex), -1);
					if (vals != null && !vals.isEmpty()) {
						team.addGameTeamTotalOptionIndicatorSecond(Integer.toString(optionNumber), vals.get("valindicator"));
						team.addGameTeamTotalOptionNumberSecond(Integer.toString(optionNumber), vals.get("val"));
					}
					final Map<String, String> juices = parseJuice(totalString.substring(minusindex), null, null);
					if (juices != null && !juices.isEmpty()) {
						team.addGameTeamTotalOptionJuiceIndicatorSecond(Integer.toString(optionNumber), juices.get("juiceindicator"));
						team.addGameTeamTotalOptionJuiceNumberSecond(Integer.toString(optionNumber), juices.get("juice"));
					}
				} else {
					int plusindex = totalString.indexOf("+");
					if (plusindex != -1) {
						final Map<String, String> vals = parseTotal(totalString.substring(0 , plusindex), -1);
						if (vals != null && !vals.isEmpty()) {
							team.addGameTeamTotalOptionIndicatorSecond(Integer.toString(optionNumber), vals.get("valindicator"));
							team.addGameTeamTotalOptionNumberSecond(Integer.toString(optionNumber), vals.get("val"));
						}
						final Map<String, String> juices = parseJuice(totalString.substring(plusindex), null, null);
						if (juices != null && !juices.isEmpty()) {
							team.addGameTeamTotalOptionJuiceIndicatorSecond(Integer.toString(optionNumber), juices.get("juiceindicator"));
							team.addGameTeamTotalOptionJuiceNumberSecond(Integer.toString(optionNumber), juices.get("juice"));
						}
					}
				}
			} else {
				int index = -1;
				
				// First check for O 42 -110 scenario
				if (totalString.substring(1, 2) != null && !totalString.substring(1, 2).equals(" ")) {
					index = totalString.indexOf(beginDelimiter);				
				} else {
					totalString = totalString.substring(0, 1) + totalString.substring(2);
					index = totalString.indexOf(beginDelimiter);
				}
	
				// Check for valid delimiter
				if (index != -1) {
					final Map<String, String> vals = parseTotal(totalString, index);
					if (vals != null && !vals.isEmpty()) {
						team.addGameTeamTotalOptionIndicatorSecond(Integer.toString(optionNumber), vals.get("valindicator"));
						team.addGameTeamTotalOptionNumberSecond(Integer.toString(optionNumber), vals.get("val"));
					}
	
					final String juice = totalString.substring(index);
					Map<String, String> juices = parseJuice(juice, beginDelimiter, endDelimiter);
					if (juices != null && !juices.isEmpty()) {
						team.addGameTeamTotalOptionJuiceIndicatorSecond(Integer.toString(optionNumber), juices.get("juiceindicator"));
						team.addGameTeamTotalOptionJuiceNumberSecond(Integer.toString(optionNumber), juices.get("juice"));
					}
				}
			}
		}
		LOGGER.info("Exiting parseTeamTotalData()");
		return team;
	}
	
	/**
	 * 
	 * @param html
	 * @param beforeString
	 * @return
	 */
	protected String parseHtmlBefore(String html, String beforeString) {
		LOGGER.info("Entering parseHtmlBefore()");
		LOGGER.debug("html: " + html);
		LOGGER.debug("beforeString: " + beforeString);
		String retValue = "";

		if (html != null && html.length() > 0) {
			int index = html.indexOf(beforeString);
			if (index != -1) {
				retValue = html.substring(0 ,index);
			}
		}

		LOGGER.info("Exiting parseHtmlBefore()");
		return retValue;
	}

	/**
	 * 
	 * @param html
	 * @param afterString
	 * @return
	 */
	protected String parseHtmlAfter(String html, String afterString) {
		LOGGER.info("Entering parseHtmlAfter()");
		LOGGER.debug("html: " + html);
		LOGGER.debug("afterString: " + afterString);
		String retValue = "";

		if (html != null && html.length() > 0) {
			int index = html.indexOf(afterString);
			if (index != -1) {
				retValue = html.substring(index + afterString.length());
			}
		}

		LOGGER.info("Exiting parseHtmlAfter()");
		return retValue;
	}

	/**
	 * 
	 * @param td
	 * @param lastIndex
	 * @return
	 */
	protected String getHtmlFromLastIndex(Element td, String lastIndex) {
		LOGGER.info("Entering getHtmlFromLastIndex()");
		LOGGER.debug("td: " + td);
		LOGGER.debug("lastIndex: " + lastIndex);

		String data = td.html();
		if (data != null) {
			int index = data.lastIndexOf(lastIndex);
			if (index != -1) {
				data = data.substring(index + 1);
				data = data.replaceAll("&nbsp;", " ");
				data = data.trim();
			}
		}
		
		LOGGER.info("Entering getHtmlFromLastIndex()");
		return data;
	}

	/**
	 * 
	 * @param td
	 * @return
	 */
	protected String getHtmlFromElement(Element td) {
		LOGGER.info("Entering getHtmlFromElement()");
		LOGGER.debug("td: " + td);

		String data = td.html();
		if (data != null) {
			data = data.replaceAll("&nbsp;", "");
			data = data.trim();
		}

		LOGGER.info("Entering getHtmlFromElement()");
		return data;
	}

	/**
	 * 
	 * @param html
	 * @param pe
	 * @return
	 */
	protected String parseJuice(String html, PendingEvent pe) {
		LOGGER.info("Entering parseJuice()");
		LOGGER.debug("html: " + html);

		int plusdIndex = html.indexOf("+");
		int minusIndex = html.indexOf("-");
		if (plusdIndex != -1 || minusIndex != -1) {
			pe.setJuiceplusminus(html.substring(0, 1));
			html = html.substring(1);
			
			int index = html.indexOf(" ");
			if (index != -1) {
				pe.setJuice(reformatValues(html.substring(0, index)));
				html = html.substring(1);
			}
		} else {
			int pkIndex = html.indexOf("pk");
			int PKIndex = html.indexOf("PK");
			int evIndex = html.indexOf("ev");
			int EVIndex = html.indexOf("EV");
			if (pkIndex != -1 || PKIndex != -1 || evIndex != -1 || EVIndex != -1) {
				pe.setJuiceplusminus("+");
				pe.setJuice("100");
				html = html.substring(2);
			} else {
				int evenIndex = html.indexOf("even");
				int EvenIndex = html.indexOf("Even");
				if (evenIndex != -1 || EvenIndex != -1) {
					pe.setLineplusminus("+");
					pe.setLine("100");
					html = html.substring(4);
				}
			}
		}

		LOGGER.info("Exiting parseJuice()");
		return html;
	}

	/**
	 * 
	 * @param data
	 * @param PENDING_DATE_FORMAT
	 * @return
	 * @throws ParseException
	 */
	public static Date fixDate(String data, SimpleDateFormat PENDING_DATE_FORMAT) throws ParseException {
		LOGGER.info("Entering fixDate()");

		final Calendar currentCal = Calendar.getInstance();
		final Calendar parsedCal = Calendar.getInstance();
		int CAL_YEAR = Calendar.YEAR;

		parsedCal.setTime(PENDING_DATE_FORMAT.parse(data));
		parsedCal.set(CAL_YEAR, currentCal.get(CAL_YEAR));

		// Check if it's the next year
		if (parsedCal.get(Calendar.MONTH) == 0 &&
			(parsedCal.get(CAL_YEAR) != currentCal.get(CAL_YEAR))) {
			parsedCal.set(CAL_YEAR, parsedCal.get(CAL_YEAR) + 1);
		}

		LOGGER.info("Exiting fixDate()");
		return parsedCal.getTime();
	}

	/**
	 * 
	 * @param timezone
	 * @return
	 */
	public static String determineTimeZone(String timezone) {
		LOGGER.info("Entering determineTimeZone()");
		LOGGER.debug("timezone: " + timezone);
		String tZone = "EST";

		if (timezone.equals("ET")) {
			if (TimeZone.getTimeZone("America/New_York").inDaylightTime(new Date())) {
				tZone = "EDT";
			} else {
				tZone = "EST";
			}
		} else if (timezone.equals("CT")) {
			if (TimeZone.getTimeZone("America/Chicago").inDaylightTime(new Date())) {
				tZone = "CDT";
			} else {
				tZone = "CST";
			}
		} else if (timezone.equals("MT")) {
			if (TimeZone.getTimeZone("America/Denver").inDaylightTime(new Date())) {
				tZone = "MDT";
			} else {
				tZone = "MST";
			}
		} else if (timezone.equals("PT")) {
			if (TimeZone.getTimeZone("America/Los_Angeles").inDaylightTime(new Date())) {
				tZone = "PDT";
			} else {
				tZone = "PST";
			}
		} else if (timezone.equals("AKT")) {
			if (TimeZone.getTimeZone("America/Anchorage").inDaylightTime(new Date())) {
				tZone = "AKDT";
			} else {
				tZone = "AKST";
			}
		} else if (timezone.equals("HT")) {
			if (TimeZone.getTimeZone("America/Hawaii").inDaylightTime(new Date())) {
				tZone = "HDT";
			} else {
				tZone = "HST";
			}
		}

		LOGGER.debug("tZone: " + tZone);
		LOGGER.info("Exiting determineTimeZone()");
		return tZone;
	}
}
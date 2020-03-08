/**
 * 
 */
package com.ticketadvantage.services.dao.twitter.poissonsports;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.ticketadvantage.services.dao.sites.SiteParser;
import com.ticketadvantage.services.dao.sites.SiteTeamPackage;
import com.ticketadvantage.services.errorhandling.BatchException;

/**
 * @author jmiller
 *
 */
public class PoissonSportsParser extends SiteParser {
	private final static Logger LOGGER = Logger.getLogger(PoissonSportsParser.class);

	/**
	 * Constructor
	 */
	public PoissonSportsParser() {
		super();
		LOGGER.info("Entering PoissonSportsParser()");
		LOGGER.info("Exiting PoissonSportsParser()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param postNumber
	 * @param xhtml
	 */
	public String parseGames(String postNumber, String xhtml) {
		String games = null;

		try {
			final Document doc = parseXhtml(xhtml);
			final Elements blockquotes = doc.select("#post_message_" + postNumber + " blockquote");

			if (blockquotes != null && blockquotes.size() > 0) {
				games = blockquotes.get(0).html();
				games = games.replace("\n<b>", " ");
				games = games.replace("</b>", "");
				games = games.replace("<br>", "\n");
			}
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
		
		return games;
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
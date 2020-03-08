/**
 * 
 */
package com.wooanalytics.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.wooanalytics.model.EspnCollegeFootballGameData;

/**
 * @author jmiller
 *
 */
public class SearchRestControllerTest {
	private static final Logger log = LoggerFactory.getLogger(SearchRestControllerTest.class);

	/**
	 * 
	 */
	public SearchRestControllerTest() {
		super();
		log.debug("Entering SearchResourceTest()");
		log.debug("Exiting SearchResourceTest()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		final SearchRestController searchResource = (SearchRestController)ctx.getBean(SearchRestController.class);

		try {
			final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("searchterm", "KANSAS STATE");
			params.add("week", "1");
			params.add("year", "2017");
			params.add("year", "2018");

			final Set<EspnCollegeFootballGameData> results = searchResource.searchNcaaFootball(params);

			SearchRestControllerTest.log.info("results: " + results);
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		} finally {
			((ConfigurableApplicationContext)ctx).close();
		}
	}
}
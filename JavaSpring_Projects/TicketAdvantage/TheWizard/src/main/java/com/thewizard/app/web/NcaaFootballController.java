package com.thewizard.app.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wooanalytics.model.EspnCollegeFootballGameData;
import com.wooanalytics.service.SearchRestController;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class NcaaFootballController {
	final static Logger LOGGER = LoggerFactory.getLogger(NcaaFootballController.class);

	@Autowired
	private SearchRestController searchRestController;

	/**
	 * 
	 * @return
	 */
	@GetMapping("/ncaafootball")
    public String home(HttpServletRequest request) {
		final List<String> names = new ArrayList<String>();
		final Method[] methods = EspnCollegeFootballGameData.class.getMethods();

		for (Method method : methods) {
			String name = method.getName();
			
			if (name.startsWith("get")) {
				name = name.replace("get", "").toLowerCase();

				if (!name.contains("class")) {
					LOGGER.debug("name: " + name);
					names.add(name);
				}
			}
		}
		request.setAttribute("names", names);
		request.setAttribute("activetab", "ncaaf");

        return "site.ncaafootball";
    }

	/**
	 * 
	 * @param params
	 * @return
	 */
	@GetMapping("/searchncaaf")
    public String search(@RequestParam(required=false) MultiValueMap<String, String> params, HttpServletRequest request) {
		final Set<EspnCollegeFootballGameData> games = searchRestController.searchNcaaFootball(params);
		final List<EspnCollegeFootballGameData> listGames = new ArrayList<EspnCollegeFootballGameData>(games);
		
		for (EspnCollegeFootballGameData ecfgd : listGames) {
			LOGGER.error("EspnCollegeFootballGameData: " + ecfgd);
		}

		request.setAttribute("activetab", "ncaaf");
		request.setAttribute("espnGames", listGames);
        return "site.ncaafootballresults";
    }
}
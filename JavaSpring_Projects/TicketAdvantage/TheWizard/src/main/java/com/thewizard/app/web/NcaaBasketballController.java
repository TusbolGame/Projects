package com.thewizard.app.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class NcaaBasketballController {
	final static Logger LOGGER = LoggerFactory.getLogger(NcaaBasketballController.class);

	/**
	 * 
	 * @return
	 */
	@GetMapping("/ncaabasketball")
    public String home(HttpServletRequest request) {
		request.setAttribute("activetab", "ncaab");
        return "site.ncaabasketball";
    }
}
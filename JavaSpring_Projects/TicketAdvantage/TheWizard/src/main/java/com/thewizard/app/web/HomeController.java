package com.thewizard.app.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class HomeController {

	/**
	 * 
	 * @return
	 */
	@GetMapping("/")
    public String home(HttpServletRequest request) {
		request.setAttribute("activetab", "ncaaf");
        return "site.home";
    }
}
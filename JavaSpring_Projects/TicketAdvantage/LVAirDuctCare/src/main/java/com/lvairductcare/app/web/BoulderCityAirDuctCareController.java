package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class BoulderCityAirDuctCareController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/bouldercityairductcare")
    public String bouldercityairductcare() {
        return "site.bouldercityairductcare";
    }
}
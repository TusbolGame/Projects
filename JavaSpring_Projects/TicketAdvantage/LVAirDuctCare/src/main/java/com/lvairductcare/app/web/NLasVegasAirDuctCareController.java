package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class NLasVegasAirDuctCareController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/nlasvegasairductcare")
    public String nlasvegasairductcare() {
        return "site.nlasvegasairductcare";
    }
}
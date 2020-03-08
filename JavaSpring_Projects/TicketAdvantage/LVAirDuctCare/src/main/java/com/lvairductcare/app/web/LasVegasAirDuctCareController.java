package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class LasVegasAirDuctCareController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/lasvegasairductcare")
    public String lasvegasairductcare() {
        return "site.lasvegasairductcare";
    }
}
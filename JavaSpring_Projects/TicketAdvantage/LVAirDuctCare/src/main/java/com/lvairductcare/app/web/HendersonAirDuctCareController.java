package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class HendersonAirDuctCareController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/hendersonairductcare")
    public String hendersonairductcare() {
        return "site.hendersonairductcare";
    }
}
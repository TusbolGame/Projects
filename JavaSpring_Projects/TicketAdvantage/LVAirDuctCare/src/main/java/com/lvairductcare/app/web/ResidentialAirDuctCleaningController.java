package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class ResidentialAirDuctCleaningController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/residentialairductcleaning")
    public String residentialairductcleaning() {
        return "site.residentialairductcleaning";
    }
}
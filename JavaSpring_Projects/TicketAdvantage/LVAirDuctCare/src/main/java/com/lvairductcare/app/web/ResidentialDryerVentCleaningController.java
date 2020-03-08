package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class ResidentialDryerVentCleaningController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/residentialdryerventcleaning")
    public String residentialdryerventcleaning() {
        return "site.residentialdryerventcleaning";
    }
}
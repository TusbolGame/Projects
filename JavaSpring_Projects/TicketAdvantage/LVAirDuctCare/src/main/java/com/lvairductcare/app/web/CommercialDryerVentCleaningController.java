package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class CommercialDryerVentCleaningController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/commercialdryerventcleaning")
    public String commercialdryerventcleaning() {
        return "site.commercialdryerventcleaning";
    }
}
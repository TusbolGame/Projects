package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class ResidentialFurnaceCleaningController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/residentialfurnacecleaning")
    public String residentialfurnacecleaning() {
        return "site.residentialfurnacecleaning";
    }
}
package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class LaughlinAirDuctCareController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/laughlinairductcare")
    public String laughlinairductcare() {
        return "site.laughlinairductcare";
    }
}
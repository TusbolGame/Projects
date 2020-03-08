package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class PahrumpAirDuctCareController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/pahrumpairductcare")
    public String pahrumpairductcare() {
        return "site.pahrumpairductcare";
    }
}
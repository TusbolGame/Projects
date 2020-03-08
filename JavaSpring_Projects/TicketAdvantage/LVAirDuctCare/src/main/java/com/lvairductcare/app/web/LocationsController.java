package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class LocationsController {

	/**
	 * 
	 * @return
	 */
	@GetMapping("/locations")
    public String locations() {
        return "site.locations";
    }
}
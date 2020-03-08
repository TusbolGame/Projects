package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class AirDuctController {

	/**
	 * 
	 * @return
	 */
	@GetMapping("/airduct")
    public String home() {
        return "site.airduct";
    }
}
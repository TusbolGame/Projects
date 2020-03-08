package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class ContactController {

    /**
     * Load the new widget page.
     * @param model
     * @return
     */
	@GetMapping("/contact")
    public String contact() {
        return "site.contact";
    }
}
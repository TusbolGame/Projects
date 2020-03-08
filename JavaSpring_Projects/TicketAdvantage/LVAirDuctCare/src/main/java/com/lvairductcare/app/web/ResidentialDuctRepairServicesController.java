package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class ResidentialDuctRepairServicesController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/residentialductrepairservices")
    public String residentialductrepairservices() {
        return "site.residentialductrepairservices";
    }
}
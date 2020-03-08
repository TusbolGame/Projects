package com.lvairductcare.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author jmiller
 *
 */
@Controller
public class MesquiteAirDuctCareController {

	/**
	 * 
	 * @return
	 */
    @GetMapping("/mesquiteairductcare")
    public String mesquiteairductcare() {
        return "site.mesquiteairductcare";
    }
}
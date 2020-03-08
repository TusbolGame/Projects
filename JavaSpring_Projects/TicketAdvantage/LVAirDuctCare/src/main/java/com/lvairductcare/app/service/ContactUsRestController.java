package com.lvairductcare.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lvairductcare.app.model.EmailData;
import com.ticketadvantage.services.email.SendText;
import com.ticketadvantage.services.email.ticketadvantage.TicketAdvantageGmailOath;

/**
 * 
 * @author jmiller
 *
 */
@RequestMapping(path="/restapi")
@RestController
@Service
public class ContactUsRestController {
	private static final Logger LOGGER = LogManager.getLogger(ContactUsRestController.class);
    private static final TicketAdvantageGmailOath TICKETADVANTAGEGMAILOATH = new TicketAdvantageGmailOath();

    /**
     * 
     * @param widget
     * @return
     */
    @PutMapping(path="/email", consumes={MediaType.APPLICATION_JSON_VALUE})
    public String emailContact(@RequestBody EmailData emailData) {
    		String outcome = "success";

        try {
			final SendText sendText = new SendText();
			sendText.setOAUTH2_TOKEN(TICKETADVANTAGEGMAILOATH.getAccessToken());
			sendText.sendTextWithMessage("9132195234@vtext.com", "LV Air Duct contact: " + emailData.getPhone());
			sendText.sendTextWithMessage("contact@lvairductcare.com", emailData.toString());
        } catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			outcome = "Problem saving appointment";
        }

        return outcome;
    }
}
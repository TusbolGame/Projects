/**
 * 
 */
package com.ticketadvantage.services.service;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import com.ticketadvantage.services.model.Accounts;

/**
 * @author jmiller
 *
 */
public class ProcessEventsClient extends BaseClient {
	static {
		try {
			jc = JAXBContext.newInstance(Accounts.class);
	        marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public ProcessEventsClient() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ProcessEventsClient.processEvents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doNothing() {
		
	}

	private static void processEvents() throws Exception {
        ProcessEventsClient.jsonResponse = target.path("restapi").path("processevents").request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();		
		System.out.println(jsonResponse.readEntity(String.class));
	}
}

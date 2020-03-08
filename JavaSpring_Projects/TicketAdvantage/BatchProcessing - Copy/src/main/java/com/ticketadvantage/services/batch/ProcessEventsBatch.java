/**
 * 
 */
package com.ticketadvantage.services.batch;

import java.net.URI;
import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;

/**
 * @author jmiller
 *
 */
@Service
public class ProcessEventsBatch implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(ProcessEventsBatch.class);
	private static ProcessEventsBatch PROGRESSEVENTSBATCH = null;
	
	// Upon startup, only create one
	static {
		if (PROGRESSEVENTSBATCH == null) {
//			PROGRESSEVENTSBATCH = new ProcessEventsBatch();
//			new Thread(PROGRESSEVENTSBATCH).start();
		}
	}

	/**
	 * 
	 */
	public ProcessEventsBatch() {
		super();
		LOGGER.info("Entering ProcessEventsBatch()");
		LOGGER.info("Exiting ProcessEventsBatch()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		while(true) {
			try {
				Thread.sleep(600000);
//				processRecords();
			} catch (Exception e) {
				LOGGER.error("Exception in thread", e);
			}
		}
	}
	
	/**
	 * 
	 */
	public void processRecords() {
		LOGGER.info("Entering processRecords()");
		try {
			final ClientConfig config = new ClientConfig();
			final Client client = ClientBuilder.newClient(config);
			final WebTarget target = client.target(getBaseURI());
			
			String token = "1343433343";
	        String bAuth = "jmiller" + ":" + "password";
	        String encodedCredentials = Base64.getEncoder().encodeToString(bAuth.getBytes("utf-8"));
			String basicAuth = " Basic " + encodedCredentials;
			Response jsonResponse = target.path("restapi").path("process/processevents").request(MediaType.APPLICATION_JSON).header("Authorization", basicAuth).cookie("CSRF-TOKEN", token).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).get();
			String retValue = jsonResponse.readEntity(String.class);
			LOGGER.debug("Response: " + retValue);
		} catch (Throwable t) {
			LOGGER.error("Throwable processRecords()", t);
		}
		LOGGER.info("Entering processRecords()");
	}

	/**
	 * 
	 * @return
	 */
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/ticketadvantage").build();
	}
}
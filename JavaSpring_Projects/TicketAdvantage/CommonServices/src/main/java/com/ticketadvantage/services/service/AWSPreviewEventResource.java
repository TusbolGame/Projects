/**
 * 
 */
package com.ticketadvantage.services.service;

import java.io.StringWriter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.ticketadvantage.services.model.PreviewInput;
import com.ticketadvantage.services.model.PreviewOutput;

/**
 * @author jmiller
 *
 */
public class AWSPreviewEventResource implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(AWSPreviewEventResource.class);
	private ClientConfig config;
	private Client client;
	private WebTarget target;
    private final StringWriter sw = new StringWriter();
	private JAXBContext jc;
	private Marshaller marshaller;
	private PreviewInput previewInput;
	private PreviewOutput previewOutput;

	/**
	 * 
	 */
	public AWSPreviewEventResource() {
		super();
		LOGGER.debug("Entering AWSPreviewEventResource()");

		try {
			jc = JAXBContext.newInstance(PreviewInput.class);
			marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

	        config = new ClientConfig();
	        config = config.property(ClientProperties.CONNECT_TIMEOUT, 30000);
	        config = config.property(ClientProperties.READ_TIMEOUT, 60000);

	        client = ClientBuilder.newClient(config);
//			target = client.target(UriBuilder.fromUri("https://192qnn73g1.execute-api.sa-east-1.amazonaws.com/prod").build());
	        	target = client.target(UriBuilder.fromUri("https://n3kwusybo1.execute-api.us-east-1.amazonaws.com/prod").build());
		} catch (Exception e) {
			LOGGER.error("Exception setting up JAXBContext and Marshaller", e);
		}

		LOGGER.debug("Exiting AWSPreviewEventResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final AWSPreviewEventResource awsTransactionEventResource = new AWSPreviewEventResource();
		PreviewInput previewInput = new PreviewInput();
		previewInput.setIsmobile(new Boolean(true));
		previewInput.setLineindicator("-");
		previewInput.setLinetype("spread");
		previewInput.setPassword("jh");
		previewInput.setProxyname("Dallas");
		previewInput.setRotationid(new Integer(693));
		previewInput.setShowrequestresponse(new Boolean(true));
		previewInput.setSitetype("MetallicaMobile");
		previewInput.setSporttype("ncaablines");
		previewInput.setTeam1("San Francisco");
		previewInput.setTeam2("Santa Clara");
		previewInput.setTimezone("PT");
		previewInput.setUrl("http://m.iron69.com");
		previewInput.setUsername("kwd2202");

		awsTransactionEventResource.setPreviewInput(previewInput);
		// awsTransactionEventResource.setAccountEventId(new Long(491526));
		awsTransactionEventResource.run();	
	}

	/**
	 * @return the previewInput
	 */
	public PreviewInput getPreviewInput() {
		return previewInput;
	}

	/**
	 * @param previewInput the previewInput to set
	 */
	public void setPreviewInput(PreviewInput previewInput) {
		this.previewInput = previewInput;
	}

	/**
	 * @return the previewOutput
	 */
	public PreviewOutput getPreviewOutput() {
		return previewOutput;
	}

	/**
	 * @param previewOutput the previewOutput to set
	 */
	public void setPreviewOutput(PreviewOutput previewOutput) {
		this.previewOutput = previewOutput;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		LOGGER.error("AWS Preview Entering Time: " + new java.util.Date());

		try {
			LOGGER.debug("previewInput: " + previewInput);
	        marshaller.marshal(previewInput, sw);
	        final Builder request = target.request(MediaType.APPLICATION_JSON);

	        // overridden timeout value for this request
	        request.property(ClientProperties.CONNECT_TIMEOUT, 45000);
	        request.property(ClientProperties.READ_TIMEOUT, 45000);

	        // Call the service asynchronously
	        final Response jsonResponse = request.header("Content-Type", MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);
	        previewOutput = jsonResponse.readEntity(PreviewOutput.class);
			if (previewOutput != null) {
				LOGGER.error("PreviewOutput: " + previewOutput);
			}
		} catch (Throwable t) {
			LOGGER.error("Throwable for PreviewInput " + previewInput, t);
		}

		LOGGER.error("AWS Preview Exiting Time: " + new java.util.Date());
		LOGGER.info("Exiting run()");
	}
}
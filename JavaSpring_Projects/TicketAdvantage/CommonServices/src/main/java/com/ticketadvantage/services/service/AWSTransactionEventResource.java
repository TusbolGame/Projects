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

import com.ticketadvantage.services.model.ProcessSiteInput;
import com.ticketadvantage.services.model.ProcessSiteOutput;

/**
 * @author jmiller
 *
 */
public class AWSTransactionEventResource implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(AWSTransactionEventResource.class);
	private ClientConfig config = null;
	private Client client = null;
	private WebTarget target = null;
	private JAXBContext jc = null;
	private Marshaller marshaller = null;
	private Long accountEventId = new Long(0);

	/**
	 * 
	 */
	public AWSTransactionEventResource() {
		super();
		LOGGER.debug("Entering AWSTransactionEventResource()");
		LOGGER.debug("Exiting AWSTransactionEventResource()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final AWSTransactionEventResource awsTransactionEventResource = new AWSTransactionEventResource();
		awsTransactionEventResource.setAccountEventId(new Long(491526));
		awsTransactionEventResource.run();	
	}

	/**
	 * @return the accountEventId
	 */
	public Long getAccountEventId() {
		return accountEventId;
	}

	/**
	 * @param accountEventId the accountEventId to set
	 */
	public void setAccountEventId(Long accountEventId) {
		this.accountEventId = accountEventId;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		LOGGER.error("AWS Entering Time: " + new java.util.Date());

		try {
			LOGGER.debug("accountEventId: " + accountEventId);
			jc = JAXBContext.newInstance(ProcessSiteInput.class);
			marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

			config = new ClientConfig();
			config = config.property(ClientProperties.CONNECT_TIMEOUT, 30000);
			config = config.property(ClientProperties.READ_TIMEOUT, 60000);

			client = ClientBuilder.newClient(config);
//			target = client.target(UriBuilder.fromUri("https://gr9gn86vp0.execute-api.sa-east-1.amazonaws.com/prod").build());
			target = client.target(UriBuilder.fromUri("https://n3kwusybo1.execute-api.us-east-1.amazonaws.com/prod").build());

			final ProcessSiteInput processSiteInput = new ProcessSiteInput();
			processSiteInput.setInputvalue(accountEventId.intValue());
			LOGGER.error("ProcessSiteInput: " + processSiteInput);

			final StringWriter sw = new StringWriter();
			marshaller.marshal(processSiteInput, sw);
			final Builder request = target.request(MediaType.APPLICATION_JSON);

			// overridden timeout value for this request
			request.property(ClientProperties.CONNECT_TIMEOUT, 45000);
			request.property(ClientProperties.READ_TIMEOUT, 45000);

			// Call the service asynchronously
			LOGGER.error("AWS Entering Time: " + new java.util.Date());
			final Response jsonResponse = request.header("Content-Type", MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);

			// Valid response?
			LOGGER.debug("jsonResponse.getStatus(): " + jsonResponse.getStatus());
			if (jsonResponse.getStatus() == 200) { 
				final ProcessSiteOutput retValue = jsonResponse.readEntity(ProcessSiteOutput.class);
				LOGGER.error("AWS Exiting Time: " + new java.util.Date());
				LOGGER.error("ProcessSiteOutput: " + retValue);
	
				if (retValue != null && retValue.getOutputvalue() != null && retValue.getOutputvalue().intValue() == 0) {
					LOGGER.error("Successful!");
				}
			} else if (jsonResponse.getStatus() == 504) {
				LOGGER.error("Timeout status: " + jsonResponse.getStatus());
			} else {
				LOGGER.error("Unknow status: " + jsonResponse.getStatus());
			}
		} catch (Throwable t) {
			LOGGER.error("Throwable for AccountEventId " + accountEventId, t);
		}

		LOGGER.error("AWS Exiting Time: " + new java.util.Date());
		LOGGER.info("Exiting run()");
	}
}
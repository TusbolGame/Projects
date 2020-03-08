/**
 * 
 */
package com.ticketadvantage.services.service;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.glassfish.jersey.client.ClientConfig;

/**
 * @author jmiller
 *
 */
public abstract class BaseClient {
	protected final static ClientConfig config = new ClientConfig();
	protected final static Client client = ClientBuilder.newClient(config);
	protected final static WebTarget target = client.target(getBaseURI());
	protected static JAXBContext jc;
	protected static Marshaller marshaller;
	protected static Unmarshaller unmarshaller;
	protected static Response jsonResponse;

	/**
	 * 
	 */
	public BaseClient() {
		super();
	}

	public abstract void doNothing();
	
	protected static URI getBaseURI() {
		return UriBuilder.fromUri("http://ec2-52-88-106-210.us-west-2.compute.amazonaws.com:8080/TicketAdvantage").build();
	}
}

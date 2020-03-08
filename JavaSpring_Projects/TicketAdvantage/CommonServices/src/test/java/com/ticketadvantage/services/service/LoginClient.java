/**
 * 
 */
package com.ticketadvantage.services.service;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

/**
 * @author jmiller
 *
 */
public class LoginClient {

	/**
	 * 
	 */
	public LoginClient() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		final WebTarget target = client.target(getBaseURI());
	    SecureRandom random = new SecureRandom();
	    String token =  new BigInteger(130, random).toString(32);
	    Cookie cookie = new Cookie("CSRF-TOKEN", token);

		final String payloadJSON = "{\"id\":0,\"username\":\"jmiller\",\"password\":\"password\",\"email\":\"\",\"mobilenumber\":\"\",\"isactive\":false,\"datecreated\":\"\",\"datemodified\":\"\"}";
		final String payloadXML = "<user><id></id><username>jmiller</username><password>password</password><email></email><mobilenumber></mobilenumber><isactive></isactive><datecreated></datecreated><datemodified></datemodified></user>";
//		final String payloadXML = "<testmedia><username>jmiller</username></testmedia>";
		System.out.println(payloadJSON);
		System.out.println(payloadXML);
		Response jsonResponse = target.path("restapi").path("login").request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).cookie(cookie).post(Entity.entity(payloadJSON, MediaType.APPLICATION_JSON), Response.class);
        Response xmlResponse = target.path("restapi").path("login").request().header("Content-Type", MediaType.APPLICATION_XML).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_XML).cookie(cookie).post(Entity.entity(payloadXML, MediaType.APPLICATION_XML), Response.class);
//        xmlResponse = target.path("restapi").path("login").request().header("Content-Type", MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get(Response.class);
        System.out.println(jsonResponse.getStatus());
        System.out.println(jsonResponse.readEntity(String.class));
        System.out.println(xmlResponse.readEntity(String.class));
	}

	/**
	 * 
	 * @return
	 */
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/TicketAdvantage").build();
	}
}

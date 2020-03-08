/**
 * 
 */
package com.ticketadvantage.services.service;

import java.io.StringWriter;
import java.net.URI;
import java.util.Base64;
import java.util.Calendar;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.glassfish.jersey.client.ClientConfig;

import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.Groups;
import com.ticketadvantage.services.model.RecordEvent;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
public class TestAllClient {

	/**
	 * 
	 */
	public TestAllClient() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ClientConfig config = new ClientConfig();
		final Client client = ClientBuilder.newClient(config);
		final WebTarget target = client.target(getBaseURI());
		final String payloadJSON = "{\"id\":\"\",\"username\":\"jmiller\",\"password\":\"test\",\"email\":\"jmiller@me.com\",\"mobilenumber\":\"9132195234\",\"isactive\":true,\"datecreated\":\"\",\"datemodified\":\"\"}";
//		Response jsonResponse = target.path("restapi").path("user").request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(payloadJSON, MediaType.APPLICATION_JSON), Response.class);
//		String retValue = jsonResponse.readEntity(String.class);
//		System.out.println("User Create: " + retValue);

		Response jsonResponse;
		String retValue;
		
		try {
			// Create a JaxBContext
			JAXBContext jc = JAXBContext.newInstance(User.class);
			// Create the Unmarshaller Object using the JaxB Context
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			// Set the Unmarshaller media type to JSON or XML
			unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
			// Set it to true if you need to include the JSON root element in the
			// JSON input
			unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
			// Create the StreamSource by creating StringReader using the JSON input
			//StreamSource json = new StreamSource(new StringReader(retValue));
//			JAXBElement<User> jaxbElement = unmarshaller.unmarshal(json, User.class);
//			System.out.println("User: " + jaxbElement.getValue());
	        // Getting the employee pojo again from the json
			//User user = unmarshaller.unmarshal(json, User.class).getValue();
			User user = new User();
			user.setId(new Long(0));
			user.setDatecreated(Calendar.getInstance().getTime());
			user.setDatemodified(Calendar.getInstance().getTime());
			user.setEmail("me@me.com");
			user.setIsactive(true);
			user.setMobilenumber("9132195234");
			user.setPassword("password");
			user.setUsername("jmiller");
			user.setAccounts(null);

			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
	        StringWriter sw = new StringWriter();
	        marshaller.marshal(user, sw);
			String token = "1343433343";
			jsonResponse = target.path("restapi").path("user").request(MediaType.APPLICATION_JSON).cookie("CSRF-TOKEN", token).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);

			marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
	        sw = new StringWriter();
	        marshaller.marshal(user, sw);
			jsonResponse = target.path("restapi").path("login").request(MediaType.APPLICATION_JSON).cookie("CSRF-TOKEN", token).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);

			Accounts account1 = new Accounts();
			account1.setName("JW Sports");
			account1.setUsername("jw423");
			account1.setPassword("jw423");
			account1.setUrl("http://backend.jwsportsinfo.com");
			account1.setOwnerpercentage(new Integer(90));
			account1.setPartnerpercentage(new Integer(10));
			account1.setProxylocation("");

			jc = JAXBContext.newInstance(Accounts.class);
	        marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
	        sw = new StringWriter();
	        marshaller.marshal(account1, sw);
    		
	        String bAuth = "jmiller" + ":" + "password";
	        String encodedCredentials = Base64.getEncoder().encodeToString(bAuth.getBytes("utf-8"));
    		String basicAuth = " Basic " + encodedCredentials;
    		System.out.println("BasicAuth: " + basicAuth);
			jsonResponse = target.path("restapi").path("account/id/1").request(MediaType.APPLICATION_JSON).header("Authorization", basicAuth).cookie("CSRF-TOKEN", token).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);
			retValue = jsonResponse.readEntity(String.class);

			account1 = new Accounts();
			account1.setName("BetBuckeye");
			account1.setUsername("zt8219");
			account1.setPassword("red3");
			account1.setUrl("http://betbuckeyesports.com");
			account1.setOwnerpercentage(new Integer(90));
			account1.setPartnerpercentage(new Integer(10));
			account1.setProxylocation("Chicago");

			jc = JAXBContext.newInstance(Accounts.class);
	        marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
	        sw = new StringWriter();
	        marshaller.marshal(account1, sw);
    		
    		System.out.println("BasicAuth: " + basicAuth);
			jsonResponse = target.path("restapi").path("account/id/1").request(MediaType.APPLICATION_JSON).header("Authorization", basicAuth).cookie("CSRF-TOKEN", token).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);
			retValue = jsonResponse.readEntity(String.class);
		
			account1 = new Accounts();
			account1.setName("Stub");
			account1.setUsername("kwd2202");
			account1.setPassword("jh");
			account1.setUrl("http://www.stub.com");
			account1.setOwnerpercentage(new Integer(90));
			account1.setPartnerpercentage(new Integer(10));
			account1.setProxylocation("");

			jc = JAXBContext.newInstance(Accounts.class);
	        marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
	        sw = new StringWriter();
	        marshaller.marshal(account1, sw);
			jsonResponse = target.path("restapi").path("account/id/1").request(MediaType.APPLICATION_JSON).header("Authorization", basicAuth).cookie("CSRF-TOKEN", token).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);
			retValue = jsonResponse.readEntity(String.class);

			Groups group1 = new Groups();
			group1.setName("Group1");
			jc = JAXBContext.newInstance(Groups.class);
	        marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
	        sw = new StringWriter();
	        marshaller.marshal(group1, sw);
			jsonResponse = target.path("restapi").path("group/id/1").request(MediaType.APPLICATION_JSON).header("Authorization", basicAuth).cookie("CSRF-TOKEN", token).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);
			retValue = jsonResponse.readEntity(String.class);

			Groups group2 = new Groups();
			group2.setName("Group 2");
			jc = JAXBContext.newInstance(Groups.class);
	        marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
	        sw = new StringWriter();
	        marshaller.marshal(group2, sw);
	        jsonResponse = target.path("restapi").path("group/id/1").request(MediaType.APPLICATION_JSON).header("Authorization", basicAuth).cookie("CSRF-TOKEN", token).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);
			retValue = jsonResponse.readEntity(String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/ticketadvantage").build();
	}
}
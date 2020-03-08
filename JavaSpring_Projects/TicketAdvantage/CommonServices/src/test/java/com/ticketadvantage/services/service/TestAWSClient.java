/**
 * 
 */
package com.ticketadvantage.services.service;

import java.io.StringWriter;
import java.net.URI;
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

import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
public class TestAWSClient {

	/**
	 * 
	 */
	public TestAWSClient() {
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
/*			User user = new User();
//			user.setId(new Long(1));
			user.setDatecreated(Calendar.getInstance().getTime());
			user.setDatemodified(Calendar.getInstance().getTime());
			user.setEmail("j@h.com");
			user.setIsactive(true);
			user.setMobilenumber("8885551212");
			user.setPassword("3id39d");
			user.setUsername("jhmojax");
			user.setAccounts(null);

			User user = new User();
			user.setDatecreated(Calendar.getInstance().getTime());
			user.setDatemodified(Calendar.getInstance().getTime());
			user.setEmail("t@v.com");
			user.setIsactive(true);
			user.setMobilenumber("8005551212");
			user.setPassword("vegas5432");
			user.setUsername("tommy");
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
			System.out.println("Response: " + jsonResponse);
			retValue = jsonResponse.readEntity(String.class);
*/


			User user = new User();
			user.setDatecreated(Calendar.getInstance().getTime());
			user.setDatemodified(Calendar.getInstance().getTime());
			user.setEmail("j@m.com");
			user.setIsactive(true);
			user.setMobilenumber("8885551212");
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
			System.out.println("Response: " + jsonResponse);			
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
		return UriBuilder.fromUri("http://ec2-52-88-106-210.us-west-2.compute.amazonaws.com:8080/ticketadvantage").build();
//		return UriBuilder.fromUri("http://localhost:8080/ticketadvantage").build();
	}
}
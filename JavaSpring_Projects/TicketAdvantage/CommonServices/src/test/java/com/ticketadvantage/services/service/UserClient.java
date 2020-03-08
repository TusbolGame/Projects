package com.ticketadvantage.services.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.User;

/**
 * @author jmiller
 *
 */
public class UserClient extends BaseClient {
	static {
		try {
			jc = JAXBContext.newInstance(User.class);
	        marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
			unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public UserClient() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			UserClient.createAll();
			Long id = createUser();
			System.out.println("ID: " + id);
			getAllUsers();
//			createAccount(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doNothing() {
		
	}

	private static void createAll() throws Exception {
		int x = 1;
		User user = new User();
		user.setEmail("me@me.com");
		user.setIsactive(true);
		user.setMobilenumber("9132195234");
		user.setPassword("password");
		user.setUsername("jmiller");
		user.setId(new Long(0));
		user.setDatecreated(Calendar.getInstance().getTime());
		user.setDatemodified(Calendar.getInstance().getTime());

		Accounts account = new Accounts();
		account.setName("Test" + x);
		account.setUsername("test" + x);
		account.setPassword("password");
		account.setUrl("http://www.test.com/");
        
		Set<Accounts> accounts = new HashSet<Accounts>();
		accounts.add(account);
		user.setAccounts(accounts);

        StringWriter sw = new StringWriter();
        marshaller.marshal(user, sw);
        System.out.println("Object: " + sw.toString());

		jsonResponse = target.path("restapi").path("user").request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);		
		System.out.println(jsonResponse.readEntity(String.class));
	}
	
	private static Long createUser() throws Exception {
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

        StringWriter sw = new StringWriter();
        marshaller.marshal(user, sw);
        System.out.println("Object: " + sw.toString());
	    SecureRandom random = new SecureRandom();
	    String token =  new BigInteger(130, random).toString(32);
	    Cookie cookie = new Cookie("CSRF-TOKEN", token);
        
		jsonResponse = target.path("restapi").path("user").request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).cookie(cookie).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);		
		String retValue = jsonResponse.readEntity(String.class);
		StreamSource json = new StreamSource(new StringReader(retValue));
		JAXBElement<User> jaxbElement = unmarshaller.unmarshal(json, User.class);
		User returnUser = jaxbElement.getValue();
		System.out.println("User: " + returnUser);
		return returnUser.getId();
	}

	private static void createAccount(Long id) throws Exception {
		int x = 1;
		Accounts account = new Accounts();
		account.setName("Test" + x);
		account.setUsername("test" + x);
		account.setPassword("password");
		account.setUrl("http://www.test.com/");

        final StringWriter sw = new StringWriter();
    	JAXBContext jcAccount = JAXBContext.newInstance(Accounts.class);
    	Marshaller marshallerAccount = jcAccount.createMarshaller();
    	marshallerAccount.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
    	marshallerAccount.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	marshallerAccount.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    	marshallerAccount.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
    	marshallerAccount.marshal(account, sw);
        System.out.println("Object: " + sw.toString());
        jsonResponse = target.path("restapi").path("account/id/" + id).request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);		
		String retValue = jsonResponse.readEntity(String.class);
		StreamSource json = new StreamSource(new StringReader(retValue));
		Unmarshaller unmarshallerAccount = jcAccount.createUnmarshaller();
		unmarshallerAccount.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
		unmarshallerAccount.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
		JAXBElement<Accounts> jaxbElement = unmarshallerAccount.unmarshal(json, Accounts.class);
		Accounts returnAccount = jaxbElement.getValue();
		System.out.println("Account: " + returnAccount);
	}
	private static void getAllUsers() throws Exception {
		int id = 1;
	    ClientConfig clientConfig = new ClientConfig();
	    HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("jmiller", "password");
	    clientConfig.register(feature) ;
	    Client client = ClientBuilder.newClient(clientConfig);
	    WebTarget target = client.target(getBaseURI());
	    SecureRandom random = new SecureRandom();
	    String token =  new BigInteger(130, random).toString(32);
	    Cookie cookie = new Cookie("CSRF-TOKEN", token);
        jsonResponse = target.path("restapi").path("/user/id/" + id).request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON).header("X-CSRF-TOKEN", token).accept(MediaType.APPLICATION_JSON).cookie(cookie).get();

		String retValue = jsonResponse.readEntity(String.class);
		StreamSource json = new StreamSource(new StringReader(retValue));
    	JAXBContext jcAccount = JAXBContext.newInstance(User.class);
		Unmarshaller unmarshallerAccount = jcAccount.createUnmarshaller();
		unmarshallerAccount.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
		unmarshallerAccount.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
		JAXBElement<User> jaxbElement = unmarshallerAccount.unmarshal(json, User.class);
		User returnAccount = jaxbElement.getValue();
		System.out.println("User: " + returnAccount);
	}
}
/**
 * 
 */
package com.ticketadvantage.services.service;

import java.io.StringWriter;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import com.ticketadvantage.services.model.Accounts;

/**
 * @author jmiller
 *
 */
public class GroupClient extends BaseClient {
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
	public GroupClient() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			GroupClient.createAccount(1);
//			GroupClient.getAllGroups();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doNothing() {
		
	}

	private static void createAccount(int x) throws Exception {
		Accounts account = new Accounts();
		account.setName("Test" + x);
		account.setUsername("test" + x);
		account.setPassword("password");
		account.setUrl("http://www.test.com/");

        StringWriter sw = new StringWriter();
        GroupClient.marshaller.marshal(account, sw);
        System.out.println("Object: " + sw.toString());

        GroupClient.jsonResponse = target.path("restapi").path("account").request(MediaType.APPLICATION_JSON).header("Content-Type", MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.entity(sw.toString(), MediaType.APPLICATION_JSON), Response.class);		
		System.out.println(jsonResponse.readEntity(String.class));
	}	
}

/**
 * 
 */
package com.ticketadvantage.services.dao;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ticketadvantage.services.model.Accounts;

/**
 * @author jmiller
 *
 */
public class AccountDAOImplTest {
	private static final Logger log = LoggerFactory.getLogger(AccountDAOImplTest.class);

	/**
	 * 
	 */
	public AccountDAOImplTest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		final AccountDAOImpl accountDAO = ctx.getBean(AccountDAOImpl.class);
		try {
			Accounts account = new Accounts();
			
			for (int x = 0; x < 1400; x++) {
				account.setDatecreated(Calendar.getInstance().getTime());
				account.setDatemodified(Calendar.getInstance().getTime());
				account.setId(null);
				account.setIsactive(new Boolean(true));
				account.setName("Account #" + x);
				account.setPassword("mrpassword" + x);
				account.setUrl("http://www.someurl.com");
				account.setUsername("username" + x);
				account = accountDAO.persist(account);
			}

			account = accountDAO.getAccount(new Long(1));
			AccountDAOImplTest.log.info("Account: " + account);
		} catch (Throwable t) {
			log.error("AccountDAOImplTest Exception", t);
		}
	}
}

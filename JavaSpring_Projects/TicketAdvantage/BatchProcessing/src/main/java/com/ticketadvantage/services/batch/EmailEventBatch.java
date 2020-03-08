/**
 * 
 */
package com.ticketadvantage.services.batch;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ticketadvantage.services.model.Accounts;
import com.ticketadvantage.services.model.EmailScrapper;

/**
 * @author jmiller
 *
 */
@WebListener
@Service
public class EmailEventBatch implements Runnable, ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(EmailEventBatch.class);
	private volatile boolean shutdown = false;

	/**
	 * 
	 */
	public EmailEventBatch() {
		super();
		LOGGER.info("Entering EmailEventBatch()");
		LOGGER.info("Exiting EmailEventBatch()");
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final EmailEventBatch emailEventBatch = new EmailEventBatch();
		emailEventBatch.run();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	LOGGER.error("Entering contextInitialized()");
//		new Thread(this).start();
		LOGGER.error("Exiting contextInitialized()");
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	LOGGER.error("Entering contextDestroyed()");
    	this.shutdown = true;
    	LOGGER.error("Exiting contextDestroyed()");
    }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Entering run()");
		boolean start = false;
		final EmailScrapper scrapper = new EmailScrapper();
		scrapper.setSpreadmaxamount("0");
		scrapper.setSpreadlineadjustment("0");
		scrapper.setMlbmlonoff(true);
		scrapper.setMlbspreadonoff(false);
		scrapper.setMlbtotalonoff(true);
		scrapper.setMlmaxamount("2000");
		scrapper.setMllineadjustment("5");
		scrapper.setTotalmaxamount("2000");
		scrapper.setTotallineadjustment("0");
		scrapper.setTotaljuiceadjustment("5");
		scrapper.setOnoff(true);
		scrapper.setScrappername("TAEmail");
		scrapper.setTelegramnumber("9132195234@vtext.com");
		scrapper.setId(new Long(765));
		scrapper.setUserid(new Long(1));

		final Set<Accounts> destinations = new HashSet<Accounts>();
		final Accounts account1 = new Accounts();
		account1.setId(new Long(130));
		account1.setName("blackdog.ag-y8207");
		account1.setSitetype("MetallicaMobile");
		account1.setUsername("Y8207");
		account1.setPassword("green16");
		account1.setUrl("http://m.blackdog.ag");
		account1.setIsactive(true);
		account1.setTimezone("ET");
		account1.setSpreadlimitamount(1000);
		account1.setMllimitamount(1000);
		account1.setTotallimitamount(1000);
		account1.setProxylocation("Baltimore");
		destinations.add(account1);

		final Accounts account2 = new Accounts();
		account2.setId(new Long(114));
		account2.setName("sports411-A3002");
		account2.setSitetype("Sports411Mobile");
		account2.setUsername("9461");
		account2.setPassword("lenny");
		account2.setUrl("http://www.sports411.ag");
		account2.setIsactive(true);
		account2.setTimezone("PT");
		account2.setSpreadlimitamount(2000);
		account2.setMllimitamount(2000);
		account2.setTotallimitamount(2000);
		account2.setProxylocation("Los Angeles");
		destinations.add(account2);
		scrapper.setDestinations(destinations);

		while (!shutdown) {
			try {
				if (!start) {
					// final UserEmailBatch ueb = new UserEmailBatch(scrapper);
					start = true;
				}
				Thread.sleep(100000000);
			} catch (InterruptedException e) {
				LOGGER.error("InterruptedException");
				// good practice
				Thread.currentThread().interrupt();
				return;
			} catch (Throwable  t) {
				LOGGER.error("Throwable in thread", t);
			} finally {
			}
		}
	}
}
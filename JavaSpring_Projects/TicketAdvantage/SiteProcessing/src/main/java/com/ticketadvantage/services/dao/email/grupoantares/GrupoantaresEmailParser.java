package com.ticketadvantage.services.dao.email.grupoantares;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ticketadvantage.services.dao.email.EmailParser;
import com.ticketadvantage.services.errorhandling.BatchException;
import com.ticketadvantage.services.model.PendingEvent;

/**
 * 
 * @author jmiller
 *
 */
public class GrupoantaresEmailParser extends EmailParser {
	private static final Logger LOGGER = Logger.getLogger(GrupoantaresEmailParser.class);

	/**
	 * 
	 */
	public GrupoantaresEmailParser() {
		super();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String emailString = "\t\t\t\t\tCustomer Id:	WC110\r\n\t\t\t\t\t\r\nInet:	betwc\t\t\t\t\t\r\nTicket #:	192764543\t\t\t\t\t\r\nAccepted:	Sep, 28 03:47 PM\r\n\t\t\t\t\tRisk/Win:	$55.00 to win $50.00\r\n\t\t\t\t\tScheduled:	Sep, 29 09:00 PM\r\n\t\t\t\t\tSport:	WNBA\r\n\t\t\t\t\tDescription	#667 - Minnesota Lynx/Los Angeles Sparks o155½ (-110)";
			String emailString = "CustomerId:KEV105.. \n" + 
//					"S:Basketball #871 Ball St +5 -150 for Game [ buying points : 2 ] \n" +
					"L:Basketball #726 Texas Southern / Wisc Green Bay U 84 -110 for 1st Half \n" + 
					"TicketWriter: Internet \n" + 
					"Amount: 750\n" + 
					"To Win: 500";
			// String emailString = "\t\t\t\t\tCustomer Id:	WC110\r\n\t\t\t\t\t\r\nInet:	betwc\t\t\t\t\t\r\nTicket #:	192764543\t\t\t\t\t\r\nAccepted:	Sep, 28 03:47 PM\r\n\t\t\t\t\tRisk/Win:	$55.00 to win $50.00\r\n\t\t\t\t\tScheduled:	Sep, 29 09:00 PM\r\n\t\t\t\t\tSport:	WNBA\r\n\t\t\t\t\tPitcher:	Action\r\n\t\t\t\t\tDescription	#110 - Utah St pk (-105) 1st Half";

			GrupoantaresEmailParser metallicaEmailParser = new GrupoantaresEmailParser();
			Set<PendingEvent> events = metallicaEmailParser.parsePendingBets(emailString, "acct1", "acct");
			boolean retValue = metallicaEmailParser.isPendingBet(emailString, "690sports", "POP402");
			LOGGER.error("retValue: " + retValue);
			for (PendingEvent event : events) {
				System.out.println("Event: " + event);
			}
		} catch (Throwable be) {
			be.printStackTrace();
		}
	}

	/**
	 * 
	 * @param body
	 * @param accountName
	 * @param accountId
	 * @return
	 */
	public boolean isPendingBet(String body, String accountName, String accountId) {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("body: " + body);
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		boolean retValue = false;

//		Customer Id:	WC553
		String customerId = parseField(body, "CustomerId:", "..").replace("\t", "").trim();
		if (customerId != null && accountId.equals(customerId)) {
			retValue = true;
		}

		return retValue;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	public boolean hasRotationId(String line) {
		LOGGER.info("Entering hasRotationId()");
		boolean retValue = false;
		final StringTokenizer st = new StringTokenizer(line, " ");

		if (st.hasMoreElements()) {
			while (st.hasMoreElements() && !retValue) {
				Object element = st.nextElement();
				retValue = isRotationId(element);
			}
		} else {
			retValue = isRotationId(line);
		}

		LOGGER.info("Exiting hasRotationId()");
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ticketadvantage.services.dao.email.EmailParser#parsePendingBets(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<PendingEvent> parsePendingBets(String body, String accountName, String accountId) throws BatchException {
		LOGGER.info("Entering parsePendingBets()");
		LOGGER.debug("body: " + body);
		LOGGER.debug("accountName: " + accountName);
		LOGGER.debug("accountId: " + accountId);
		final Set<PendingEvent> pendingEvents = new HashSet<PendingEvent>();

//		CustomerId:KEV105..
//		S:Basketball #871 Ball St +5 -150 for Game [ buying points : 2 ]
//		TicketWriter: Internet 
//		Amount: 750
//		To Win: 500
		final PendingEvent pendingEvent = new PendingEvent();
		String tempBody = body;

		String customerid = parseField(body, "CustomerId:", "..").replace("\t", "").replace("\n", "").trim();
		int index = body.indexOf("..");
		if (index != -1) {
			body = body.substring(index + 2).trim();
			index = body.indexOf("TicketWriter");

			if (index != -1) {
				body = body.substring(0, index).replace("\n", "").trim();
				LOGGER.debug("body: " + body);

				if (body.startsWith("S")) {
					pendingEvent.setEventtype("spread");
				} else if (body.startsWith("L")) {
					pendingEvent.setEventtype("total");
				} else {
					pendingEvent.setEventtype("ml");
				}
				body = body.substring(2);
				index = body.indexOf(" ");

				if (index != -1) {
					body = body.substring(index + 1);

					// Get the rotation id
					body = setRotationid(body, pendingEvent);
					
					// Get line, juice and team
					parseLineandJuice(body, pendingEvent);
				}
			}
		}
		String risk = parseField(tempBody, "Amount:", "To Win:").replace("\t", "").trim();
		String win = parseField(tempBody, "To Win:", null).replace("\t", "").trim();

		pendingEvent.setPosturl("");
		pendingEvent.setCustomerid(customerid);
		pendingEvent.setInet(accountName);
		pendingEvent.setAccountname(accountName);
		pendingEvent.setAccountid(accountId);
		pendingEvent.setTicketnum(Long.toString(System.currentTimeMillis()));
		pendingEvent.setDateaccepted((new Date()).toString());
		pendingEvent.setDatecreated(new Date());
		pendingEvent.setDatemodified(new Date());
		pendingEvent.setRisk(risk);
		pendingEvent.setWin(win);
		pendingEvent.setPitcher("");

		// Add to list
		pendingEvents.add(pendingEvent);

		LOGGER.info("Exiting parsePendingBets()");
		return pendingEvents;
	}

	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 */
	private void parseLineandJuice(String line, PendingEvent pendingEvent) {
		LOGGER.debug("line: " + line);

		// #871 Ball St +5 -150 for Game [ buying points : 2 ]
		// #884 Oakland / No Kentucky U 152 -110 for Game
		// #871 Ball St -150 for Game
		if (line.contains("for Game")) {
			pendingEvent.setLinetype("game");
		} else if (line.contains("for 1st ")) {
			pendingEvent.setLinetype("first");
		} else if (line.contains("for 2nd ")) {
			pendingEvent.setLinetype("second");
		} else if (line.contains("for 3rd ")) {
			pendingEvent.setLinetype("third");
		}

		int lastIndex = line.lastIndexOf("for");
		if (lastIndex != -1) {
			line = line.substring(0, lastIndex).trim();
			LOGGER.debug("line: " + line);
			LOGGER.debug("pendingEvent.getEventtype(): " + pendingEvent.getEventtype());

			if (pendingEvent.getEventtype().equals("spread")) {
				lastIndex = line.lastIndexOf(" ");
				String juice = line.substring(lastIndex + 1);
				juice = juice.replace("½", ".5").trim();
				pendingEvent.setJuiceplusminus(juice.substring(0, 1));
				pendingEvent.setJuice(juice.substring(1));
				
				line = line.substring(0, lastIndex);
				lastIndex = line.lastIndexOf(" ");

				String spread = line.substring(lastIndex + 1);
				spread = spread.replace("½", ".5").trim();
				pendingEvent.setLineplusminus(spread.substring(0, 1));
				pendingEvent.setLine(spread.substring(1));
				
				line = line.substring(0, lastIndex).trim();
				pendingEvent.setTeam(line);
			} else if (pendingEvent.getEventtype().equals("total")) {
				lastIndex = line.lastIndexOf(" ");
				String juice = line.substring(lastIndex + 1);
				juice = juice.replace("½", ".5").trim();
				pendingEvent.setJuiceplusminus(juice.substring(0, 1));
				pendingEvent.setJuice(juice.substring(1));
				
				line = line.substring(0, lastIndex);
				lastIndex = line.lastIndexOf(" ");

				String total = line.substring(lastIndex + 1);
				total = total.replace("½", ".5").trim();
				pendingEvent.setLine(total);

				line = line.substring(0, lastIndex);
				lastIndex = line.lastIndexOf(" ");
				final String totaltype = line.substring(lastIndex + 1);
				pendingEvent.setLineplusminus(totaltype.toLowerCase());

				line = line.substring(0, lastIndex).trim();
				pendingEvent.setTeam(line);
			} else if (pendingEvent.getEventtype().equals("ml")) {
				lastIndex = line.lastIndexOf(" ");
				String juice = line.substring(lastIndex + 1);
				juice = juice.replace("½", ".5").trim();
				pendingEvent.setJuiceplusminus(juice.substring(0, 1));
				pendingEvent.setLineplusminus(juice.substring(0, 1));
				pendingEvent.setJuice(juice.substring(1));
				pendingEvent.setLine(juice.substring(1));

				line = line.substring(0, lastIndex).trim();
				pendingEvent.setTeam(line);
			}
		}
	}

	/**
	 * 
	 * @param body
	 * @param start
	 * @param end
	 * @return
	 */
	private String parseField(String body, String start, String end) {
		LOGGER.info("Entering parseField()");
		String retValue = null;

		if (start != null && end != null) {
			int bindex = body.indexOf(start);
			int eindex = body.indexOf(end);
			if (bindex != -1 && eindex != -1) {
				retValue = body.substring(bindex + start.length(), eindex);
			}
		} else if (start != null && end == null) {
			int bindex = body.indexOf(start);
			if (bindex != -1) {
				retValue = body.substring(bindex + start.length());
			}
		}

		LOGGER.info("Exiting parseField()");
		return retValue;
	}

	/**
	 * 
	 * @param line
	 * @param pendingEvent
	 * @return
	 */
	private String setRotationid(String line, PendingEvent pendingEvent) {
		LOGGER.info("Entering setRotationid()");
		LOGGER.debug("line: " + line);

		// #667 - Minnesota Lynx/Los Angeles Sparks o155½ (-110)
		if (line != null) {
			int bindex = line.indexOf("#");
			int eindex = line.indexOf(" ");
			if (bindex != -1 && eindex != -1) {
				String rotationid = line.substring(bindex + 1, eindex);
				pendingEvent.setRotationid(rotationid);
				line = line.substring(eindex + 1);
			}
		}

		LOGGER.info("Exiting setRotationid()");
		return line;
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	private boolean isRotationId(Object element) {
		LOGGER.info("Entering isRotationId()");
		boolean retValue = false;

		try {
			if (element != null) {
				String str = (String)element;
				LOGGER.debug("Str: " + str);
				if (str != null && str.startsWith("#")) {
					// Now try to convert into Integer
					Integer rotationId = Integer.parseInt(str.substring(1));
					if (rotationId != null) {
						retValue = true;
					}
				}
			}
		} catch (Throwable t) {
			// do nothing and move to next element
		}

		LOGGER.debug("retValue: " + retValue);
		LOGGER.info("Exiting isRotationId()");
		return retValue;
	}
}
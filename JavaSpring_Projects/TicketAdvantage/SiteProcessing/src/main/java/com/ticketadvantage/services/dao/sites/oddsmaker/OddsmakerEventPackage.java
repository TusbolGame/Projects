/**
 * 
 */
package com.ticketadvantage.services.dao.sites.oddsmaker;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class OddsmakerEventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String inetWagerNumber;
	private String eventdate;
	private String eventtime;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public OddsmakerEventPackage() {
		super();
	}
	
	/**
	 * @return the inetWagerNumber
	 */
	public String getInetWagerNumber() {
		return inetWagerNumber;
	}

	/**
	 * @param inetWagerNumber the inetWagerNumber to set
	 */
	public void setInetWagerNumber(String inetWagerNumber) {
		this.inetWagerNumber = inetWagerNumber;
	}

	/**
	 * @return the eventdate
	 */
	public String getEventdate() {
		return eventdate;
	}

	/**
	 * @param eventdate the eventdate to set
	 */
	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}

	/**
	 * @return the eventtime
	 */
	public String getEventtime() {
		return eventtime;
	}

	/**
	 * @param eventtime the eventtime to set
	 */
	public void setEventtime(String eventtime) {
		this.eventtime = eventtime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OddsmakerEventPackage [inetWagerNumber=" + inetWagerNumber + ", eventdate=" + eventdate + ", eventtime="
				+ eventtime + "] " + super.toString();
	}
}
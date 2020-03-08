/**
 * 
 */
package com.ticketadvantage.services.dao.sites.stub;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ticketadvantage.services.dao.sites.SiteEventPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class StubEventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement
	private String inetWagerNumber;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public StubEventPackage() {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StubEventPackage [inetWagerNumber=" + inetWagerNumber + ", " + super.toString() + "]";
	}
}
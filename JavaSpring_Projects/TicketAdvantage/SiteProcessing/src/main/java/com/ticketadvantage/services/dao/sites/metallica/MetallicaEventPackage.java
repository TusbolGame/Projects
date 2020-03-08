/**
 * 
 */
package com.ticketadvantage.services.dao.sites.metallica;

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
public class MetallicaEventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String inetWagerNumber;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public MetallicaEventPackage() {
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
		return "MetallicaEventPackage [inetWagerNumber=" + inetWagerNumber + ",\n " + super.toString() + "]";
	}
}
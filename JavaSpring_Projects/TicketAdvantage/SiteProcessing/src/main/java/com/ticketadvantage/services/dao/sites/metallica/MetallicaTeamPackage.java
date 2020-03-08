/**
 * 
 */
package com.ticketadvantage.services.dao.sites.metallica;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class MetallicaTeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MetallicaTeamPackage() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MetallicaTeamPackage []\n" + super.toString();
	}
}
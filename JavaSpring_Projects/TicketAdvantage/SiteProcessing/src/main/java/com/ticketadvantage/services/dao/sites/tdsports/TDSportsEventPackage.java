/**
 * 
 */
package com.ticketadvantage.services.dao.sites.tdsports;

import java.io.Serializable;
import java.util.Map;

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
public class TDSportsEventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, String> hiddenData;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public TDSportsEventPackage() {
		super();
	}

	/**
	 * @return the hiddenData
	 */
	public Map<String, String> getHiddenData() {
		return hiddenData;
	}

	/**
	 * @param hiddenData the hiddenData to set
	 */
	public void setHiddenData(Map<String, String> hiddenData) {
		this.hiddenData = hiddenData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TDSportsEventPackage [hiddenData=" + hiddenData + "]\n" + super.toString();
	}
}
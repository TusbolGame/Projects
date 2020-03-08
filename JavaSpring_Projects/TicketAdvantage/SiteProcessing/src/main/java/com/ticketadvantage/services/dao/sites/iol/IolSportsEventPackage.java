/**
 * 
 */
package com.ticketadvantage.services.dao.sites.iol;

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
public class IolSportsEventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer spreadMax;
	private Integer totalMax;
	private Integer mlMax;
	private Map<String, String> hiddenData;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public IolSportsEventPackage() {
		super();
	}

	/**
	 * @return the spreadMax
	 */
	public Integer getSpreadMax() {
		return spreadMax;
	}

	/**
	 * @param spreadMax the spreadMax to set
	 */
	public void setSpreadMax(Integer spreadMax) {
		this.spreadMax = spreadMax;
	}

	/**
	 * @return the totalMax
	 */
	public Integer getTotalMax() {
		return totalMax;
	}

	/**
	 * @param totalMax the totalMax to set
	 */
	public void setTotalMax(Integer totalMax) {
		this.totalMax = totalMax;
	}

	/**
	 * @return the mlMax
	 */
	public Integer getMlMax() {
		return mlMax;
	}

	/**
	 * @param mlMax the mlMax to set
	 */
	public void setMlMax(Integer mlMax) {
		this.mlMax = mlMax;
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
		return "HeritageSportsEventPackage [spreadMax=" + spreadMax + ", totalMax=" + totalMax + ", mlMax=" + mlMax
				+ ", hiddenData=" + hiddenData + "]\n" + super.toString();
	}
}
/**
 * 
 */
package com.ticketadvantage.services.dao.sites.oddsmaker;

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
public class OddsmakerTeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer spreadwagerOrder = new Integer(1);
	private Integer spreadwagerType = new Integer(1);
	private Integer spreadlineID = new Integer(1);
	private Integer spreadlineType = new Integer(1);
	private Integer spreadlineOrder = new Integer(1);
	private Integer totalwagerOrder = new Integer(1);
	private Integer totalwagerType = new Integer(1);
	private Integer totallineID = new Integer(1);
	private Integer totallineType = new Integer(1);
	private Integer totallineOrder = new Integer(1);
	private Integer mlwagerOrder = new Integer(1);
	private Integer mlwagerType = new Integer(1);
	private Integer mllineID = new Integer(1);
	private Integer mllineType = new Integer(1);
	private Integer mllineOrder = new Integer(1);

	/**
	 * 
	 */
	public OddsmakerTeamPackage() {
		super();
	}

	/**
	 * @return the spreadwagerOrder
	 */
	public Integer getSpreadwagerOrder() {
		return spreadwagerOrder;
	}

	/**
	 * @param spreadwagerOrder the spreadwagerOrder to set
	 */
	public void setSpreadwagerOrder(Integer spreadwagerOrder) {
		this.spreadwagerOrder = spreadwagerOrder;
	}

	/**
	 * @return the spreadwagerType
	 */
	public Integer getSpreadwagerType() {
		return spreadwagerType;
	}

	/**
	 * @param spreadwagerType the spreadwagerType to set
	 */
	public void setSpreadwagerType(Integer spreadwagerType) {
		this.spreadwagerType = spreadwagerType;
	}

	/**
	 * @return the spreadlineID
	 */
	public Integer getSpreadlineID() {
		return spreadlineID;
	}

	/**
	 * @param spreadlineID the spreadlineID to set
	 */
	public void setSpreadlineID(Integer spreadlineID) {
		this.spreadlineID = spreadlineID;
	}

	/**
	 * @return the spreadlineType
	 */
	public Integer getSpreadlineType() {
		return spreadlineType;
	}

	/**
	 * @param spreadlineType the spreadlineType to set
	 */
	public void setSpreadlineType(Integer spreadlineType) {
		this.spreadlineType = spreadlineType;
	}

	/**
	 * @return the spreadlineOrder
	 */
	public Integer getSpreadlineOrder() {
		return spreadlineOrder;
	}

	/**
	 * @param spreadlineOrder the spreadlineOrder to set
	 */
	public void setSpreadlineOrder(Integer spreadlineOrder) {
		this.spreadlineOrder = spreadlineOrder;
	}

	/**
	 * @return the totalwagerOrder
	 */
	public Integer getTotalwagerOrder() {
		return totalwagerOrder;
	}

	/**
	 * @param totalwagerOrder the totalwagerOrder to set
	 */
	public void setTotalwagerOrder(Integer totalwagerOrder) {
		this.totalwagerOrder = totalwagerOrder;
	}

	/**
	 * @return the totalwagerType
	 */
	public Integer getTotalwagerType() {
		return totalwagerType;
	}

	/**
	 * @param totalwagerType the totalwagerType to set
	 */
	public void setTotalwagerType(Integer totalwagerType) {
		this.totalwagerType = totalwagerType;
	}

	/**
	 * @return the totallineID
	 */
	public Integer getTotallineID() {
		return totallineID;
	}

	/**
	 * @param totallineID the totallineID to set
	 */
	public void setTotallineID(Integer totallineID) {
		this.totallineID = totallineID;
	}

	/**
	 * @return the totallineType
	 */
	public Integer getTotallineType() {
		return totallineType;
	}

	/**
	 * @param totallineType the totallineType to set
	 */
	public void setTotallineType(Integer totallineType) {
		this.totallineType = totallineType;
	}

	/**
	 * @return the totallineOrder
	 */
	public Integer getTotallineOrder() {
		return totallineOrder;
	}

	/**
	 * @param totallineOrder the totallineOrder to set
	 */
	public void setTotallineOrder(Integer totallineOrder) {
		this.totallineOrder = totallineOrder;
	}

	/**
	 * @return the mlwagerOrder
	 */
	public Integer getMlwagerOrder() {
		return mlwagerOrder;
	}

	/**
	 * @param mlwagerOrder the mlwagerOrder to set
	 */
	public void setMlwagerOrder(Integer mlwagerOrder) {
		this.mlwagerOrder = mlwagerOrder;
	}

	/**
	 * @return the mlwagerType
	 */
	public Integer getMlwagerType() {
		return mlwagerType;
	}

	/**
	 * @param mlwagerType the mlwagerType to set
	 */
	public void setMlwagerType(Integer mlwagerType) {
		this.mlwagerType = mlwagerType;
	}

	/**
	 * @return the mllineID
	 */
	public Integer getMllineID() {
		return mllineID;
	}

	/**
	 * @param mllineID the mllineID to set
	 */
	public void setMllineID(Integer mllineID) {
		this.mllineID = mllineID;
	}

	/**
	 * @return the mllineType
	 */
	public Integer getMllineType() {
		return mllineType;
	}

	/**
	 * @param mllineType the mllineType to set
	 */
	public void setMllineType(Integer mllineType) {
		this.mllineType = mllineType;
	}

	/**
	 * @return the mllineOrder
	 */
	public Integer getMllineOrder() {
		return mllineOrder;
	}

	/**
	 * @param mllineOrder the mllineOrder to set
	 */
	public void setMllineOrder(Integer mllineOrder) {
		this.mllineOrder = mllineOrder;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OddsmakerTeamPackage [spreadwagerOrder=" + spreadwagerOrder + ", spreadwagerType=" + spreadwagerType
				+ ", spreadlineID=" + spreadlineID + ", spreadlineType=" + spreadlineType + ", spreadlineOrder="
				+ spreadlineOrder + ", totalwagerOrder=" + totalwagerOrder + ", totalwagerType=" + totalwagerType
				+ ", totallineID=" + totallineID + ", totallineType=" + totallineType + ", totallineOrder="
				+ totallineOrder + ", mlwagerOrder=" + mlwagerOrder + ", mlwagerType=" + mlwagerType + ", mllineID="
				+ mllineID + ", mllineType=" + mllineType + ", mllineOrder=" + mllineOrder + "] " + super.toString();
	}
}
/**
 * 
 */
package com.ticketadvantage.services.dao.sites.oddsmaker;

import com.ticketadvantage.services.dao.sites.SiteTransaction;

/**
 * @author jmiller
 *
 */
public class OddsmakerSiteTransaction extends SiteTransaction {
	private Integer wagerOrder = new Integer(1);
	private Integer wagerType = new Integer(1);
	private Integer lineID = new Integer(1);
	private Integer lineType = new Integer(1);
	private Integer lineOrder = new Integer(1);

	/**
	 * 
	 */
	public OddsmakerSiteTransaction() {
		super();
	}

	/**
	 * @return the wagerOrder
	 */
	public Integer getWagerOrder() {
		return wagerOrder;
	}

	/**
	 * @param wagerOrder the wagerOrder to set
	 */
	public void setWagerOrder(Integer wagerOrder) {
		this.wagerOrder = wagerOrder;
	}

	/**
	 * @return the wagerType
	 */
	public Integer getWagerType() {
		return wagerType;
	}

	/**
	 * @param wagerType the wagerType to set
	 */
	public void setWagerType(Integer wagerType) {
		this.wagerType = wagerType;
	}

	/**
	 * @return the lineID
	 */
	public Integer getLineID() {
		return lineID;
	}

	/**
	 * @param lineID the lineID to set
	 */
	public void setLineID(Integer lineID) {
		this.lineID = lineID;
	}

	/**
	 * @return the lineType
	 */
	public Integer getLineType() {
		return lineType;
	}

	/**
	 * @param lineType the lineType to set
	 */
	public void setLineType(Integer lineType) {
		this.lineType = lineType;
	}

	/**
	 * @return the lineOrder
	 */
	public Integer getLineOrder() {
		return lineOrder;
	}

	/**
	 * @param lineOrder the lineOrder to set
	 */
	public void setLineOrder(Integer lineOrder) {
		this.lineOrder = lineOrder;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OddsmakerSiteTransaction [wagerOrder=" + wagerOrder + ", wagerType=" + wagerType + ", lineID=" + lineID
				+ ", lineType=" + lineType + ", lineOrder=" + lineOrder + "] " + super.toString();
	}
}
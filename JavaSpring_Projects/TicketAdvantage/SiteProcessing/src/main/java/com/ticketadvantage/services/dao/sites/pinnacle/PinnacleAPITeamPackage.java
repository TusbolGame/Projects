/**
 * 
 */
package com.ticketadvantage.services.dao.sites.pinnacle;

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
public class PinnacleAPITeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer lineId;
	private Integer altLineId;
	private Integer number;
	private Integer status;

	/**
	 * 
	 */
	public PinnacleAPITeamPackage() {
		super();
	}

	/**
	 * @return the lineId
	 */
	public Integer getLineId() {
		return lineId;
	}

	/**
	 * @param lineId the lineId to set
	 */
	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	/**
	 * @return the altLineId
	 */
	public Integer getAltLineId() {
		return altLineId;
	}

	/**
	 * @param altLineId the altLineId to set
	 */
	public void setAltLineId(Integer altLineId) {
		this.altLineId = altLineId;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PinnacleAPITeamPackage [lineId=" + lineId + ", altLineId=" + altLineId + ", number=" + number
				+ ", status=" + status + "]\n" + super.toString();
	}
}
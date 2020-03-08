/**
 * 
 */
package com.ticketadvantage.services.dao.sites.pinnacle;

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
public class PinnacleAPIEventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer eventid;
	private Integer lineId;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public PinnacleAPIEventPackage() {
		super();
	}

	/**
	 * @return the eventid
	 */
	public Integer getEventid() {
		return eventid;
	}

	/**
	 * @param eventid the eventid to set
	 */
	public void setEventid(Integer eventid) {
		this.eventid = eventid;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PinnacleAPIEventPackage [eventid=" + eventid + ", lineId=" + lineId + "]\n" + super.toString();
	}
}
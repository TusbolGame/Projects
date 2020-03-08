/**
 * 
 */
package com.wootechnologies.services.dao.sites;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wootechnologies.model.EventPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class SiteEventPackage extends EventPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private SiteTeamPackage siteteamone;
	
	@XmlElement
	private SiteTeamPackage siteteamtwo;

	@XmlElement	
	private String eventdate;

	@XmlElement	
	private String eventtime;

	@XmlElement
	private Integer spreadMax;

	@XmlElement
	private Integer totalMax;

	@XmlElement
	private Integer mlMax;
	
	@XmlElement
	private Integer teamTotalMax;	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Do nothing
	}

	/**
	 * 
	 */
	public SiteEventPackage() {
		super();
	}

	/**
	 * @return the siteteamone
	 */
	public SiteTeamPackage getSiteteamone() {
		return siteteamone;
	}

	/**
	 * @param siteteamone the siteteamone to set
	 */
	public void setSiteteamone(SiteTeamPackage siteteamone) {
		this.siteteamone = siteteamone;
	}

	/**
	 * @return the siteteamtwo
	 */
	public SiteTeamPackage getSiteteamtwo() {
		return siteteamtwo;
	}

	/**
	 * @param siteteamtwo the siteteamtwo to set
	 */
	public void setSiteteamtwo(SiteTeamPackage siteteamtwo) {
		this.siteteamtwo = siteteamtwo;
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

	public Integer getTeamTotalMax() {
		return teamTotalMax;
	}

	public void setTeamTotalMax(Integer teamTotalMax) {
		this.teamTotalMax = teamTotalMax;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SiteEventPackage [siteteamone=" + siteteamone + ",\n siteteamtwo=" + siteteamtwo + ",\n eventdate="
				+ eventdate + ",\n eventtime=" + eventtime + ",\n spreadMax=" + spreadMax + ",\n totalMax=" + totalMax
				+ ",\n teamTotalMax=" + teamTotalMax
				+ ",\n mlMax=" + mlMax + "]" + super.toString();
	}
}

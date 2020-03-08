/**
 * 
 */
package com.ticketadvantage.services.dao.sites.play305;

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
public class Play305EventPackage extends SiteEventPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String FavoredTeamID;
	private int GameNum;
	private String Status;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
	public Play305EventPackage() {
		super();
	}

	/**
	 * @return the favoredTeamID
	 */
	public String getFavoredTeamID() {
		return FavoredTeamID;
	}

	/**
	 * @param favoredTeamID the favoredTeamID to set
	 */
	public void setFavoredTeamID(String favoredTeamID) {
		FavoredTeamID = favoredTeamID;
	}

	/**
	 * @return the gameNum
	 */
	public int getGameNum() {
		return GameNum;
	}

	/**
	 * @param gameNum the gameNum to set
	 */
	public void setGameNum(int gameNum) {
		GameNum = gameNum;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Play305EventPackage [FavoredTeamID=" + FavoredTeamID + ", GameNum=" + GameNum + ", Status=" + Status
				+ "] " + super.toString();
	}
}
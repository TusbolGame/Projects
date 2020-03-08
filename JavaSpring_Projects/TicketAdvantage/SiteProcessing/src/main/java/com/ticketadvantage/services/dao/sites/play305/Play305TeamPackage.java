/**
 * 
 */
package com.ticketadvantage.services.dao.sites.play305;

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
public class Play305TeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	private int GameNum;
	private String Store;
	private String FavoredTeamID;
	private int period;

	/**
	 * 
	 */
	public Play305TeamPackage() {
		super();
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
	 * @return the store
	 */
	public String getStore() {
		return Store;
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(String store) {
		Store = store;
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
	 * @return the period
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Play305TeamPackage [GameNum=" + GameNum + ", Store=" + Store + ", FavoredTeamID=" + FavoredTeamID
				+ ", period=" + period + "] " + super.toString();
	}
}
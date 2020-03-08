package com.ticketadvantage.services.dao.sites.vegasinsider;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jmiller
 *
 */
public class VegasInsiderLineMovement {
	private String lineprovider;
	private String awayteam;
	private String hometeam;
	private String gamedate;
	private String gametime;
	private final List<VegasInsiderLinePoint> linepoints = new ArrayList<VegasInsiderLinePoint>();

	/**
	 * 
	 */
	public VegasInsiderLineMovement() {
		super();
	}

	/**
	 * @return the lineprovider
	 */
	public String getLineprovider() {
		return lineprovider;
	}

	/**
	 * @param lineprovider the lineprovider to set
	 */
	public void setLineprovider(String lineprovider) {
		this.lineprovider = lineprovider;
	}

	/**
	 * @return the awayteam
	 */
	public String getAwayteam() {
		return awayteam;
	}

	/**
	 * @param awayteam the awayteam to set
	 */
	public void setAwayteam(String awayteam) {
		this.awayteam = awayteam;
	}

	/**
	 * @return the hometeam
	 */
	public String getHometeam() {
		return hometeam;
	}

	/**
	 * @param hometeam the hometeam to set
	 */
	public void setHometeam(String hometeam) {
		this.hometeam = hometeam;
	}

	/**
	 * @return the gamedate
	 */
	public String getGamedate() {
		return gamedate;
	}

	/**
	 * @param gamedate the gamedate to set
	 */
	public void setGamedate(String gamedate) {
		this.gamedate = gamedate;
	}

	/**
	 * @return the gametime
	 */
	public String getGametime() {
		return gametime;
	}

	/**
	 * @param gametime the gametime to set
	 */
	public void setGametime(String gametime) {
		this.gametime = gametime;
	}

	/**
	 * @return the mllinepoints
	 */
	public List<VegasInsiderLinePoint> getLinepoints() {
		return linepoints;
	}

	/**
	 * 
	 * @param vilp
	 */
	public void addLinepoint(VegasInsiderLinePoint vilp) {
		linepoints.add(vilp);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VegasInsiderLineMovement [lineprovider=" + lineprovider + ", awayteam=" + awayteam + ", hometeam="
				+ hometeam + ", gamedate=" + gamedate + ", gametime=" + gametime + ", linepoints=" + linepoints + "]";
	}
}
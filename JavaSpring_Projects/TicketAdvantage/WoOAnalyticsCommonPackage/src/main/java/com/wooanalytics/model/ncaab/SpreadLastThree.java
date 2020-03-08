/**
 * 
 */
package com.wooanalytics.model.ncaab;

import java.io.Serializable;

/**
 * @author jmiller
 *
 */
public class SpreadLastThree implements Serializable {
	private static final long serialVersionUID = 5543190998252853502L;
	private String rotation1;
	private String rotation2;
	private String roadteam;
	private String hometeam;
	private Float homespread;
	private Float kenpomspread;

	/**
	 * 
	 */
	public SpreadLastThree() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the rotation1
	 */
	public String getRotation1() {
		return rotation1;
	}

	/**
	 * @param rotation1 the rotation1 to set
	 */
	public void setRotation1(String rotation1) {
		this.rotation1 = rotation1;
	}

	/**
	 * @return the rotation2
	 */
	public String getRotation2() {
		return rotation2;
	}

	/**
	 * @param rotation2 the rotation2 to set
	 */
	public void setRotation2(String rotation2) {
		this.rotation2 = rotation2;
	}

	/**
	 * @return the roadteam
	 */
	public String getRoadteam() {
		return roadteam;
	}

	/**
	 * @param roadteam the roadteam to set
	 */
	public void setRoadteam(String roadteam) {
		this.roadteam = roadteam;
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
	 * @return the homespread
	 */
	public Float getHomespread() {
		return homespread;
	}

	/**
	 * @param homespread the homespread to set
	 */
	public void setHomespread(Float homespread) {
		this.homespread = homespread;
	}

	/**
	 * @return the kenpomspread
	 */
	public Float getKenpomspread() {
		return kenpomspread;
	}

	/**
	 * @param kenpomspread the kenpomspread to set
	 */
	public void setKenpomspread(Float kenpomspread) {
		this.kenpomspread = kenpomspread;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SpreadLastThree [rotation1=" + rotation1 + ", rotation2=" + rotation2 + ", roadteam=" + roadteam
				+ ", hometeam=" + hometeam + ", homespread=" + homespread + ", kenpomspread=" + kenpomspread + "]";
	}
}
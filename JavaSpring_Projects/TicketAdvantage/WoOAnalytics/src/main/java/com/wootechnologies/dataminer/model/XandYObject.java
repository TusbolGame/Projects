package com.wootechnologies.dataminer.model;

/**
 * 
 * @author jmiller
 *
 */
public class XandYObject {
	public String x;
	public String y;
	public String linefavorite;
	public Float line;
	public String game;
	public Integer awayscore;
	public Integer homescore;
	public Integer scoredelta;
	public Integer week;
	public Integer year;

	/**
	 * 
	 */
	public XandYObject() {
		super();
	}

	/**
	 * @return the x
	 */
	public String getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(String x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public String getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(String y) {
		this.y = y;
	}

	/**
	 * @return the linefavorite
	 */
	public String getLinefavorite() {
		return linefavorite;
	}

	/**
	 * @param linefavorite the linefavorite to set
	 */
	public void setLinefavorite(String linefavorite) {
		this.linefavorite = linefavorite;
	}

	/**
	 * @return the line
	 */
	public Float getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Float line) {
		this.line = line;
	}

	/**
	 * @return the game
	 */
	public String getGame() {
		return game;
	}

	/**
	 * @param game the game to set
	 */
	public void setGame(String game) {
		this.game = game;
	}

	/**
	 * @return the awayscore
	 */
	public Integer getAwayscore() {
		return awayscore;
	}

	/**
	 * @param awayscore the awayscore to set
	 */
	public void setAwayscore(Integer awayscore) {
		this.awayscore = awayscore;
	}

	/**
	 * @return the homescore
	 */
	public Integer getHomescore() {
		return homescore;
	}

	/**
	 * @param homescore the homescore to set
	 */
	public void setHomescore(Integer homescore) {
		this.homescore = homescore;
	}

	/**
	 * @return the scoredelta
	 */
	public Integer getScoredelta() {
		return scoredelta;
	}

	/**
	 * @param scoredelta the scoredelta to set
	 */
	public void setScoredelta(Integer scoredelta) {
		this.scoredelta = scoredelta;
	}

	/**
	 * @return the week
	 */
	public Integer getWeek() {
		return week;
	}

	/**
	 * @param week the week to set
	 */
	public void setWeek(Integer week) {
		this.week = week;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "XandYObject [x=" + x + ", y=" + y + ", linefavorite=" + linefavorite + ", line=" + line + ", game="
				+ game + ", awayscore=" + awayscore + ", homescore=" + homescore + ", scoredelta=" + scoredelta
				+ ", week=" + week + ", year=" + year + "]";
	}
}
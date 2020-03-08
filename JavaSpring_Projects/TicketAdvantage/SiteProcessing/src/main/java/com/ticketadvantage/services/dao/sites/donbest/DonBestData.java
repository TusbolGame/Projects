/**
 * 
 */
package com.ticketadvantage.services.dao.sites.donbest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.linemovement.entity.LineMovement;

/**
 * @author jmiller
 *
 */
public class DonBestData {
	private String date;
	private String time;
	private Date dateofgame;
	private List<LineMovement> lines = new ArrayList<LineMovement>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the lines
	 */
	public List<LineMovement> getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(List<LineMovement> lines) {
		this.lines = lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void addLine(LineMovement line) {
		lines.add(line);
	}

	/**
	 * @return the dateofgame
	 */
	public Date getDateofgame() {
		return dateofgame;
	}

	/**
	 * @param dateofgame the dateofgame to set
	 */
	public void setDateofgame(Date dateofgame) {
		this.dateofgame = dateofgame;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DonBestData [date=" + date + ", time=" + time + ", dateofgame=" + dateofgame + ", lines=" + lines + "]";
	}
}
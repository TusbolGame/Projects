/**
 * 
 */
package com.wooanalytics.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author jmiller
 *
 */
@Entity
@Data
@Table (name="espnfootballgame")
@XmlRootElement(name = "espnfootballgame")
@XmlAccessorType(XmlAccessType.NONE)
public class EspnCollegeFootballGameData extends EspnFootballGameData {

	@Column(name = "isconferencegame", nullable = false)
	@XmlElement
	private Boolean isconferencegame = new Boolean(false);

	@Column(name = "neutralsite", nullable = false)
	@XmlElement
	private Boolean neutralsite = new Boolean(false);

	@Column(name = "awaycollegename", nullable = false)
	@XmlElement
	private String awaycollegename = "";

	@Column(name = "homecollegename", nullable = false)
	@XmlElement
	private String homecollegename = "";

	@Column(name = "awaymascotname", nullable = false)
	@XmlElement
	private String awaymascotname = "";

	@Column(name = "homemascotname", nullable = false)
	@XmlElement
	private String homemascotname = "";

	@Column(name = "awayshortname", nullable = false)
	@XmlElement
	private String awayshortname = "";

	@Column(name = "homeshortname", nullable = false)
	@XmlElement
	private String homeshortname = "";

	@Column(name = "awayranking", nullable = false)
	@XmlElement
	private Integer awayranking = new Integer(0);

	@Column(name = "homeranking", nullable = false)
	@XmlElement
	private Integer homeranking = new Integer(0);

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnCollegeFootballGameData [isconferencegame=" + isconferencegame + ", neutralsite=" + neutralsite
				+ ", awaycollegename=" + awaycollegename + ", homecollegename=" + homecollegename + ", awaymascotname="
				+ awaymascotname + ", homemascotname=" + homemascotname + ", awayshortname=" + awayshortname
				+ ", homeshortname=" + homeshortname + ", awayranking=" + awayranking + ", homeranking=" + homeranking
				+ "] " + super.toString();
	}
}
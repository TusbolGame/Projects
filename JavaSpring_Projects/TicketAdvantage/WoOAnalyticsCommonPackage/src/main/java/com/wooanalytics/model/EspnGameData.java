/**
 * 
 */
package com.wooanalytics.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

/**
 * @author jmiller
 *
 */
@Data
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS) //slightly more normalized
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public class EspnGameData {
    @Id
	@Column(name = "espngameid", nullable = false)
    @XmlElement
	private Integer espngameid;

	@Column(name = "week", nullable = false)
	@XmlElement
	private Integer week = new Integer(0);

	@Column(name = "seasonyear", nullable = false)
	@XmlElement
	private Integer seasonyear = new Integer(0);

	@Column(name = "month", nullable = false)
	@XmlElement
	private Integer month = new Integer(0);

	@Column(name = "day", nullable = false)
	@XmlElement
	private Integer day = new Integer(0);

	@Column(name = "year", nullable = false)
	@XmlElement
	private Integer year = new Integer(0);

	@Column(name = "hour", nullable = false)
	@XmlElement
	private Integer hour = new Integer(0);

	@Column(name = "minute", nullable = false)
	@XmlElement
	private Integer minute = new Integer(0);

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "gamedate", nullable = false)
	@XmlElement
	private Date gamedate = new Date();

	@Column(name = "ampm", nullable = false)
	@XmlElement
	private String ampm = "";

	@Column(name = "timezone", nullable = false)
	@XmlElement
	private String timezone = "";

	@Column(name = "city", nullable = false)
	@XmlElement
	private String city = "";

	@Column(name = "state", nullable = false)
	@XmlElement
	private String state = "";

	@Column(name = "zipcode", nullable = false)
	@XmlElement
	private Integer zipcode = new Integer(0);

	@Column(name = "linefavorite", nullable = false)
	@XmlElement
	private String linefavorite = "";

	@Column(name = "lineindicator", nullable = false)
	@XmlElement
	private String lineindicator = "";

	@Column(name = "linevalue", nullable = false)
	@XmlElement
	private Float linevalue = new Float(0);

	@Column(name = "line", nullable = false)
	@XmlElement
	private Float line = new Float(0);

	@Column(name = "total", nullable = false)
	@XmlElement
	private Float total = new Float(0);

	@Column(name = "attendance", nullable = false)
	@XmlElement
	private Integer attendance = new Integer(0);

	@Column(name = "tv", nullable = false)
	@XmlElement
	private String tv = "";

	@Column(name = "eventlocation", nullable = false)
	@XmlElement
	private String eventlocation = "";

	@Column(name = "awayteam", nullable = false)
	@XmlElement
	private String awayteam = "";

	@Column(name = "hometeam", nullable = false)
	@XmlElement
	private String hometeam = "";

	@Column(name = "awaywin", nullable = false)
	@XmlElement
	private Boolean awaywin = new Boolean(false);

	@Column(name = "homewin", nullable = false)
	@XmlElement
	private Boolean homewin = new Boolean(false);

	@Column(name = "awaywins", nullable = false)
	@XmlElement
	private Integer awaywins = new Integer(0);

	@Column(name = "homewins", nullable = false)
	@XmlElement
	private Integer homewins = new Integer(0);

	@Column(name = "awaylosses", nullable = false)
	@XmlElement
	private Integer awaylosses = new Integer(0);

	@Column(name = "homelosses", nullable = false)
	@XmlElement
	private Integer homelosses = new Integer(0);

	@Column(name = "awayfirstquarterscore", nullable = false)
	@XmlElement
	private Integer awayfirstquarterscore = new Integer(0);

	@Column(name = "homefirstquarterscore", nullable = false)
	@XmlElement
	private Integer homefirstquarterscore = new Integer(0);

	@Column(name = "awaysecondquarterscore", nullable = false)
	@XmlElement
	private Integer awaysecondquarterscore = new Integer(0);

	@Column(name = "homesecondquarterscore", nullable = false)
	@XmlElement
	private Integer homesecondquarterscore = new Integer(0);

	@Column(name = "awayfirsthalfscore", nullable = false)
	@XmlElement
	private Integer awayfirsthalfscore = new Integer(0);

	@Column(name = "homefirsthalfscore", nullable = false)
	@XmlElement
	private Integer homefirsthalfscore = new Integer(0);

	@Column(name = "awaythirdquarterscore", nullable = false)
	@XmlElement
	private Integer awaythirdquarterscore = new Integer(0);

	@Column(name = "homethirdquarterscore", nullable = false)
	@XmlElement
	private Integer homethirdquarterscore = new Integer(0);

	@Column(name = "awayfourthquarterscore", nullable = false)
	@XmlElement
	private Integer awayfourthquarterscore = new Integer(0);

	@Column(name = "homefourthquarterscore", nullable = false)
	@XmlElement
	private Integer homefourthquarterscore = new Integer(0);

	@Column(name = "awaysecondhalfscore", nullable = false)
	@XmlElement
	private Integer awaysecondhalfscore = new Integer(0);

	@Column(name = "homesecondhalfscore", nullable = false)
	@XmlElement
	private Integer homesecondhalfscore = new Integer(0);

	@Column(name = "awayotonescore", nullable = false)
	@XmlElement
	private Integer awayotonescore = new Integer(0);

	@Column(name = "homeotonescore", nullable = false)
	@XmlElement
	private Integer homeotonescore = new Integer(0);

	@Column(name = "awayottwoscore", nullable = false)
	@XmlElement
	private Integer awayottwoscore = new Integer(0);

	@Column(name = "homeottwoscore", nullable = false)
	@XmlElement
	private Integer homeottwoscore = new Integer(0);

	@Column(name = "awayotthreescore", nullable = false)
	@XmlElement
	private Integer awayotthreescore = new Integer(0);

	@Column(name = "homeotthreescore", nullable = false)
	@XmlElement
	private Integer homeotthreescore = new Integer(0);

	@Column(name = "awayotfourscore", nullable = false)
	@XmlElement
	private Integer awayotfourscore = new Integer(0);

	@Column(name = "homeotfourscore", nullable = false)
	@XmlElement
	private Integer homeotfourscore = new Integer(0);

	@Column(name = "awayotfivescore", nullable = false)
	@XmlElement
	private Integer awayotfivescore = new Integer(0);

	@Column(name = "homeotfivescore", nullable = false)
	@XmlElement
	private Integer homeotfivescore = new Integer(0);

	@Column(name = "awayotsixscore", nullable = false)
	@XmlElement
	private Integer awayotsixscore = new Integer(0);

	@Column(name = "homeotsixscore", nullable = false)
	@XmlElement
	private Integer homeotsixscore = new Integer(0);

	@Column(name = "awaysecondhalfotscore", nullable = false)
	@XmlElement
	private Integer awaysecondhalfotscore = new Integer(0);

	@Column(name = "homesecondhalfotscore", nullable = false)
	@XmlElement
	private Integer homesecondhalfotscore = new Integer(0);

	@Column(name = "awayfinalscore", nullable = false)
	@XmlElement
	private Integer awayfinalscore = new Integer(0);

	@Column(name = "homefinalscore", nullable = false)
	@XmlElement
	private Integer homefinalscore = new Integer(0);

	@Column(name = "awaysagrinrating", nullable = false)
	@XmlElement
	private Float awaysagrinrating = new Float(0);

	@Column(name = "homesagrinrating", nullable = false)
	@XmlElement
	private Float homesagrinrating = new Float(0);

	@Column(name = "awaymasseyrating", nullable = false)
	@XmlElement
	private Float awaymasseyrating = new Float(0);

	@Column(name = "homemasseyrating", nullable = false)
	@XmlElement
	private Float homemasseyrating = new Float(0);

	@Column(name = "awaymasseyranking", nullable = false)
	@XmlElement
	private Integer awaymasseyranking = new Integer(0);

	@Column(name = "homemasseyranking", nullable = false)
	@XmlElement
	private Integer homemasseyranking = new Integer(0);

	@Column(name = "awaysos", nullable = false)
	@XmlElement
	private Float awaysos = new Float(0);

	@Column(name = "homesos", nullable = false)
	@XmlElement
	private Float homesos = new Float(0);

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnGameData [espngameid=" + espngameid + ", week=" + week + ", seasonyear=" + seasonyear + ", month="
				+ month + ", day=" + day + ", year=" + year + ", hour=" + hour + ", minute=" + minute + ", gamedate="
				+ gamedate + ", ampm=" + ampm + ", timezone=" + timezone + ", city=" + city + ", state=" + state
				+ ", zipcode=" + zipcode + ", linefavorite=" + linefavorite + ", lineindicator=" + lineindicator
				+ ", linevalue=" + linevalue + ", line=" + line + ", total=" + total + ", attendance=" + attendance
				+ ", tv=" + tv + ", eventlocation=" + eventlocation + ", awayteam=" + awayteam + ", hometeam="
				+ hometeam + ", awaywin=" + awaywin + ", homewin=" + homewin + ", awaywins=" + awaywins + ", homewins="
				+ homewins + ", awaylosses=" + awaylosses + ", homelosses=" + homelosses + ", awayfirstquarterscore="
				+ awayfirstquarterscore + ", homefirstquarterscore=" + homefirstquarterscore
				+ ", awaysecondquarterscore=" + awaysecondquarterscore + ", homesecondquarterscore="
				+ homesecondquarterscore + ", awayfirsthalfscore=" + awayfirsthalfscore + ", homefirsthalfscore="
				+ homefirsthalfscore + ", awaythirdquarterscore=" + awaythirdquarterscore + ", homethirdquarterscore="
				+ homethirdquarterscore + ", awayfourthquarterscore=" + awayfourthquarterscore
				+ ", homefourthquarterscore=" + homefourthquarterscore + ", awaysecondhalfscore=" + awaysecondhalfscore
				+ ", homesecondhalfscore=" + homesecondhalfscore + ", awayotonescore=" + awayotonescore
				+ ", homeotonescore=" + homeotonescore + ", awayottwoscore=" + awayottwoscore + ", homeottwoscore="
				+ homeottwoscore + ", awayotthreescore=" + awayotthreescore + ", homeotthreescore=" + homeotthreescore
				+ ", awayotfourscore=" + awayotfourscore + ", homeotfourscore=" + homeotfourscore + ", awayotfivescore="
				+ awayotfivescore + ", homeotfivescore=" + homeotfivescore + ", awayotsixscore=" + awayotsixscore
				+ ", homeotsixscore=" + homeotsixscore + ", awaysecondhalfotscore=" + awaysecondhalfotscore
				+ ", homesecondhalfotscore=" + homesecondhalfotscore + ", awayfinalscore=" + awayfinalscore
				+ ", homefinalscore=" + homefinalscore + ", awaysagrinrating=" + awaysagrinrating
				+ ", homesagrinrating=" + homesagrinrating + ", awaymasseyrating=" + awaymasseyrating
				+ ", homemasseyrating=" + homemasseyrating + ", awaymasseyranking=" + awaymasseyranking
				+ ", homemasseyranking=" + homemasseyranking + ", awaysos=" + awaysos + ", homesos=" + homesos + "]";
	}
}
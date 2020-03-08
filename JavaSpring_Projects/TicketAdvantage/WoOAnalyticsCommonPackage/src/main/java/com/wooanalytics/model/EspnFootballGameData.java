/**
 * 
 */
package com.wooanalytics.model;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
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
public class EspnFootballGameData extends EspnGameData {

	@Column(name = "awayfirstdowns", nullable = false)
	@XmlElement
	private Integer awayfirstdowns = new Integer(0);

	@Column(name = "homefirstdowns", nullable = false)
	@XmlElement
	private Integer homefirstdowns = new Integer(0);

	@Column(name = "awaythirdefficiencymade", nullable = false)
	@XmlElement
	private Integer awaythirdefficiencymade = new Integer(0);

	@Column(name = "homethirdefficiencymade", nullable = false)
	@XmlElement
	private Integer homethirdefficiencymade = new Integer(0);

	@Column(name = "awaythirdefficiencyattempts", nullable = false)
	@XmlElement
	private Integer awaythirdefficiencyattempts = new Integer(0);

	@Column(name = "homethirdefficiencyattempts", nullable = false)
	@XmlElement
	private Integer homethirdefficiencyattempts = new Integer(0);

	@Column(name = "awayfourthefficiencymade", nullable = false)
	@XmlElement
	private Integer awayfourthefficiencymade = new Integer(0);

	@Column(name = "homefourthefficiencymade", nullable = false)
	@XmlElement
	private Integer homefourthefficiencymade = new Integer(0);

	@Column(name = "awayfourthefficiencyattempts", nullable = false)
	@XmlElement
	private Integer awayfourthefficiencyattempts = new Integer(0);

	@Column(name = "homefourthefficiencyattempts", nullable = false)
	@XmlElement
	private Integer homefourthefficiencyattempts = new Integer(0);

	@Column(name = "awaytotalyards", nullable = false)
	@XmlElement
	private Integer awaytotalyards = new Integer(0);

	@Column(name = "hometotalyards", nullable = false)
	@XmlElement
	private Integer hometotalyards = new Integer(0);

	@Column(name = "awaypassingyards", nullable = false)
	@XmlElement
	private Integer awaypassingyards = new Integer(0);

	@Column(name = "homepassingyards", nullable = false)
	@XmlElement
	private Integer homepassingyards = new Integer(0);

	@Column(name = "awaypasscomp", nullable = false)
	@XmlElement
	private Integer awaypasscomp = new Integer(0);

	@Column(name = "homepasscomp", nullable = false)
	@XmlElement
	private Integer homepasscomp = new Integer(0);

	@Column(name = "awaypassattempts", nullable = false)
	@XmlElement
	private Integer awaypassattempts = new Integer(0);

	@Column(name = "homepassattempts", nullable = false)
	@XmlElement
	private Integer homepassattempts = new Integer(0);

	@Column(name = "awayyardsperpass", nullable = false)
	@XmlElement
	private Float awayyardsperpass = new Float(0);

	@Column(name = "homeyardsperpass", nullable = false)
	@XmlElement
	private Float homeyardsperpass = new Float(0);

	@Column(name = "awayrushingyards", nullable = false)
	@XmlElement
	private Integer awayrushingyards = new Integer(0);

	@Column(name = "homerushingyards", nullable = false)
	@XmlElement
	private Integer homerushingyards = new Integer(0);

	@Column(name = "awayrushingattempts", nullable = false)
	@XmlElement
	private Integer awayrushingattempts = new Integer(0);

	@Column(name = "homerushingattempts", nullable = false)
	@XmlElement
	private Integer homerushingattempts = new Integer(0);

	@Column(name = "awayyardsperrush", nullable = false)
	@XmlElement
	private Float awayyardsperrush = new Float(0);

	@Column(name = "homeyardsperrush", nullable = false)
	@XmlElement
	private Float homeyardsperrush = new Float(0);

	@Column(name = "awaypenalties", nullable = false)
	@XmlElement
	private Integer awaypenalties = new Integer(0);

	@Column(name = "homepenalties", nullable = false)
	@XmlElement
	private Integer homepenalties = new Integer(0);

	@Column(name = "awaypenaltyyards", nullable = false)
	@XmlElement
	private Integer awaypenaltyyards = new Integer(0);

	@Column(name = "homepenaltyyards", nullable = false)
	@XmlElement
	private Integer homepenaltyyards = new Integer(0);

	@Column(name = "awayturnovers", nullable = false)
	@XmlElement
	private Integer awayturnovers = new Integer(0);

	@Column(name = "hometurnovers", nullable = false)
	@XmlElement
	private Integer hometurnovers = new Integer(0);

	@Column(name = "awayfumbleslost", nullable = false)
	@XmlElement
	private Integer awayfumbleslost = new Integer(0);

	@Column(name = "homefumbleslost", nullable = false)
	@XmlElement
	private Integer homefumbleslost = new Integer(0);

	@Column(name = "awayinterceptions", nullable = false)
	@XmlElement
	private Integer awayinterceptions = new Integer(0);

	@Column(name = "homeinterceptions", nullable = false)
	@XmlElement
	private Integer homeinterceptions = new Integer(0);

	@Column(name = "awaypossessionminutes", nullable = false)
	@XmlElement
	private Integer awaypossessionminutes = new Integer(0);

	@Column(name = "homepossessionminutes", nullable = false)
	@XmlElement
	private Integer homepossessionminutes = new Integer(0);

	@Column(name = "awaypossessionseconds", nullable = false)
	@XmlElement
	private Integer awaypossessionseconds = new Integer(0);

	@Column(name = "homepossessionseconds", nullable = false)
	@XmlElement
	private Integer homepossessionseconds = new Integer(0);

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

	@Column(name = "awaysos", nullable = false)
	@XmlElement
	private Float awaysos = new Float(0);

	@Column(name = "homesos", nullable = false)
	@XmlElement
	private Float homesos = new Float(0);

	@Column(name = "awayisfbs", nullable = false)
	@XmlElement
	private Boolean awayisfbs = new Boolean(false);

	@Column(name = "homeisfbs", nullable = false)
	@XmlElement
	private Boolean homeisfbs = new Boolean(false);

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EspnFootballGameData [awayfirstdowns=" + awayfirstdowns + ", homefirstdowns=" + homefirstdowns
				+ ", awaythirdefficiencymade=" + awaythirdefficiencymade + ", homethirdefficiencymade="
				+ homethirdefficiencymade + ", awaythirdefficiencyattempts=" + awaythirdefficiencyattempts
				+ ", homethirdefficiencyattempts=" + homethirdefficiencyattempts + ", awayfourthefficiencymade="
				+ awayfourthefficiencymade + ", homefourthefficiencymade=" + homefourthefficiencymade
				+ ", awayfourthefficiencyattempts=" + awayfourthefficiencyattempts + ", homefourthefficiencyattempts="
				+ homefourthefficiencyattempts + ", awaytotalyards=" + awaytotalyards + ", hometotalyards="
				+ hometotalyards + ", awaypassingyards=" + awaypassingyards + ", homepassingyards=" + homepassingyards
				+ ", awaypasscomp=" + awaypasscomp + ", homepasscomp=" + homepasscomp + ", awaypassattempts="
				+ awaypassattempts + ", homepassattempts=" + homepassattempts + ", awayyardsperpass=" + awayyardsperpass
				+ ", homeyardsperpass=" + homeyardsperpass + ", awayrushingyards=" + awayrushingyards
				+ ", homerushingyards=" + homerushingyards + ", awayrushingattempts=" + awayrushingattempts
				+ ", homerushingattempts=" + homerushingattempts + ", awayyardsperrush=" + awayyardsperrush
				+ ", homeyardsperrush=" + homeyardsperrush + ", awaypenalties=" + awaypenalties + ", homepenalties="
				+ homepenalties + ", awaypenaltyyards=" + awaypenaltyyards + ", homepenaltyyards=" + homepenaltyyards
				+ ", awayturnovers=" + awayturnovers + ", hometurnovers=" + hometurnovers + ", awayfumbleslost="
				+ awayfumbleslost + ", homefumbleslost=" + homefumbleslost + ", awayinterceptions=" + awayinterceptions
				+ ", homeinterceptions=" + homeinterceptions + ", awaypossessionminutes=" + awaypossessionminutes
				+ ", homepossessionminutes=" + homepossessionminutes + ", awaypossessionseconds="
				+ awaypossessionseconds + ", homepossessionseconds=" + homepossessionseconds + ", awaysagrinrating="
				+ awaysagrinrating + ", homesagrinrating=" + homesagrinrating + ", awaymasseyrating=" + awaymasseyrating
				+ ", homemasseyrating=" + homemasseyrating + ", awaysos=" + awaysos + ", homesos=" + homesos
				+ ", awayisfbs=" + awayisfbs + ", homeisfbs=" + homeisfbs + "] " + super.toString();
	}
}
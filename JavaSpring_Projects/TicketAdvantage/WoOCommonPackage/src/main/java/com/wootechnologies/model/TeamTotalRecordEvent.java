/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author jmiller
 *
 */
@Entity
@Table (name="teamtotalrecordevent")
@AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
@XmlRootElement(name = "teamtotalrecordevent")
@XmlAccessorType(XmlAccessType.NONE)
public class TeamTotalRecordEvent extends BaseRecordEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "teamtotalinputfirstone", nullable = true)
	@XmlElement
	private String teamtotalinputfirstone;

	@Column(name = "teamtotaljuiceplusminusfirstone", nullable = true)
	@XmlElement
	private String teamtotaljuiceplusminusfirstone;

	@Column(name = "teamtotalinputjuicefirstone", nullable = true)
	@XmlElement
	private String teamtotalinputjuicefirstone;

	@Column(name = "teamtotalinputfirsttwo", nullable = true)
	@XmlElement
	private String teamtotalinputfirsttwo;

	@Column(name = "teamtotaljuiceplusminusfirsttwo", nullable = true)
	@XmlElement
	private String teamtotaljuiceplusminusfirsttwo;

	@Column(name = "teamtotalinputjuicefirsttwo", nullable = true)
	@XmlElement
	private String teamtotalinputjuicefirsttwo;

	@Column(name = "teamtotalinputsecondone", nullable = true)
	@XmlElement
	private String teamtotalinputsecondone;

	@Column(name = "teamtotaljuiceplusminussecondone", nullable = true)
	@XmlElement
	private String teamtotaljuiceplusminussecondone;

	@Column(name = "teamtotalinputjuicesecondone", nullable = true)
	@XmlElement
	private String teamtotalinputjuicesecondone;

	@Column(name = "teamtotalinputsecondtwo", nullable = true)
	@XmlElement
	private String teamtotalinputsecondtwo;

	@Column(name = "teamtotaljuiceplusminussecondtwo", nullable = true)
	@XmlElement
	private String teamtotaljuiceplusminussecondtwo;

	@Column(name = "teamtotalinputjuicesecondtwo", nullable = true)
	@XmlElement
	private String teamtotalinputjuicesecondtwo;
	
	@Column(name = "teamtotaloverunder", nullable = true)
	@XmlElement
	private String teamtotaloverunder;

	/**
	 * 
	 */
	public TeamTotalRecordEvent() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the teamtotalinputfirstone
	 */
	public String getTeamTotalinputfirstone() {
		return teamtotalinputfirstone;
	}

	/**
	 * @param teamtotalinputfirstone the teamtotalinputfirstone to set
	 */
	public void setTeamTotalinputfirstone(String teamtotalinputfirstone) {
		this.teamtotalinputfirstone = teamtotalinputfirstone;
	}

	/**
	 * @return the teamtotaljuiceplusminusfirstone
	 */
	public String getTeamTotaljuiceplusminusfirstone() {
		return teamtotaljuiceplusminusfirstone;
	}

	/**
	 * @param teamtotaljuiceplusminusfirstone the teamtotaljuiceplusminusfirstone to set
	 */
	public void setTeamTotaljuiceplusminusfirstone(String teamtotaljuiceplusminusfirstone) {
		this.teamtotaljuiceplusminusfirstone = teamtotaljuiceplusminusfirstone;
	}

	/**
	 * @return the teamtotalinputjuicefirstone
	 */
	public String getTeamTotalinputjuicefirstone() {
		return teamtotalinputjuicefirstone;
	}

	/**
	 * @param teamtotalinputjuicefirstone the teamtotalinputjuicefirstone to set
	 */
	public void setTeamTotalinputjuicefirstone(String teamtotalinputjuicefirstone) {
		this.teamtotalinputjuicefirstone = teamtotalinputjuicefirstone;
	}

	/**
	 * @return the teamtotalinputfirsttwo
	 */
	public String getTeamTotalinputfirsttwo() {
		return teamtotalinputfirsttwo;
	}

	/**
	 * @param teamtotalinputfirsttwo the teamtotalinputfirsttwo to set
	 */
	public void setTeamTotalinputfirsttwo(String teamtotalinputfirsttwo) {
		this.teamtotalinputfirsttwo = teamtotalinputfirsttwo;
	}

	/**
	 * @return the teamtotaljuiceplusminusfirsttwo
	 */
	public String getTeamTotaljuiceplusminusfirsttwo() {
		return teamtotaljuiceplusminusfirsttwo;
	}

	/**
	 * @param teamtotaljuiceplusminusfirsttwo the teamtotaljuiceplusminusfirsttwo to set
	 */
	public void setTeamTotaljuiceplusminusfirsttwo(String teamtotaljuiceplusminusfirsttwo) {
		this.teamtotaljuiceplusminusfirsttwo = teamtotaljuiceplusminusfirsttwo;
	}

	/**
	 * @return the teamtotalinputjuicefirsttwo
	 */
	public String getTeamTotalinputjuicefirsttwo() {
		return teamtotalinputjuicefirsttwo;
	}

	/**
	 * @param teamtotalinputjuicefirsttwo the teamtotalinputjuicefirsttwo to set
	 */
	public void setTeamTotalinputjuicefirsttwo(String teamtotalinputjuicefirsttwo) {
		this.teamtotalinputjuicefirsttwo = teamtotalinputjuicefirsttwo;
	}

	/**
	 * @return the teamtotalinputsecondone
	 */
	public String getTeamTotalinputsecondone() {
		return teamtotalinputsecondone;
	}

	/**
	 * @param teamtotalinputsecondone the teamtotalinputsecondone to set
	 */
	public void setTeamTotalinputsecondone(String teamtotalinputsecondone) {
		this.teamtotalinputsecondone = teamtotalinputsecondone;
	}

	/**
	 * @return the teamtotaljuiceplusminussecondone
	 */
	public String getTeamTotaljuiceplusminussecondone() {
		return teamtotaljuiceplusminussecondone;
	}

	/**
	 * @param teamtotaljuiceplusminussecondone the teamtotaljuiceplusminussecondone to set
	 */
	public void setTeamTotaljuiceplusminussecondone(String teamtotaljuiceplusminussecondone) {
		this.teamtotaljuiceplusminussecondone = teamtotaljuiceplusminussecondone;
	}

	/**
	 * @return the teamtotalinputjuicesecondone
	 */
	public String getTeamTotalinputjuicesecondone() {
		return teamtotalinputjuicesecondone;
	}

	/**
	 * @param teamtotalinputjuicesecondone the teamtotalinputjuicesecondone to set
	 */
	public void setTeamTotalinputjuicesecondone(String teamtotalinputjuicesecondone) {
		this.teamtotalinputjuicesecondone = teamtotalinputjuicesecondone;
	}

	/**
	 * @return the teamtotalinputsecondtwo
	 */
	public String getTeamTotalinputsecondtwo() {
		return teamtotalinputsecondtwo;
	}

	/**
	 * @param teamtotalinputsecondtwo the teamtotalinputsecondtwo to set
	 */
	public void setTeamTotalinputsecondtwo(String teamtotalinputsecondtwo) {
		this.teamtotalinputsecondtwo = teamtotalinputsecondtwo;
	}

	/**
	 * @return the teamtotaljuiceplusminussecondtwo
	 */
	public String getTeamTotaljuiceplusminussecondtwo() {
		return teamtotaljuiceplusminussecondtwo;
	}

	/**
	 * @param teamtotaljuiceplusminussecondtwo the teamtotaljuiceplusminussecondtwo to set
	 */
	public void setTeamTotaljuiceplusminussecondtwo(String teamtotaljuiceplusminussecondtwo) {
		this.teamtotaljuiceplusminussecondtwo = teamtotaljuiceplusminussecondtwo;
	}

	/**
	 * @return the teamtotalinputjuicesecondtwo
	 */
	public String getTeamTotalinputjuicesecondtwo() {
		return teamtotalinputjuicesecondtwo;
	}

	/**
	 * @param teamtotalinputjuicesecondtwo the teamtotalinputjuicesecondtwo to set
	 */
	public void setTeamTotalinputjuicesecondtwo(String teamtotalinputjuicesecondtwo) {
		this.teamtotalinputjuicesecondtwo = teamtotalinputjuicesecondtwo;
	}

	public String getTeamtotalinputfirstone() {
		return teamtotalinputfirstone;
	}

	public void setTeamtotalinputfirstone(String teamtotalinputfirstone) {
		this.teamtotalinputfirstone = teamtotalinputfirstone;
	}

	public String getTeamtotaljuiceplusminusfirstone() {
		return teamtotaljuiceplusminusfirstone;
	}

	public void setTeamtotaljuiceplusminusfirstone(String teamtotaljuiceplusminusfirstone) {
		this.teamtotaljuiceplusminusfirstone = teamtotaljuiceplusminusfirstone;
	}

	public String getTeamtotalinputjuicefirstone() {
		return teamtotalinputjuicefirstone;
	}

	public void setTeamtotalinputjuicefirstone(String teamtotalinputjuicefirstone) {
		this.teamtotalinputjuicefirstone = teamtotalinputjuicefirstone;
	}

	public String getTeamtotalinputfirsttwo() {
		return teamtotalinputfirsttwo;
	}

	public void setTeamtotalinputfirsttwo(String teamtotalinputfirsttwo) {
		this.teamtotalinputfirsttwo = teamtotalinputfirsttwo;
	}

	public String getTeamtotaljuiceplusminusfirsttwo() {
		return teamtotaljuiceplusminusfirsttwo;
	}

	public void setTeamtotaljuiceplusminusfirsttwo(String teamtotaljuiceplusminusfirsttwo) {
		this.teamtotaljuiceplusminusfirsttwo = teamtotaljuiceplusminusfirsttwo;
	}

	public String getTeamtotalinputjuicefirsttwo() {
		return teamtotalinputjuicefirsttwo;
	}

	public void setTeamtotalinputjuicefirsttwo(String teamtotalinputjuicefirsttwo) {
		this.teamtotalinputjuicefirsttwo = teamtotalinputjuicefirsttwo;
	}

	public String getTeamtotalinputsecondone() {
		return teamtotalinputsecondone;
	}

	public void setTeamtotalinputsecondone(String teamtotalinputsecondone) {
		this.teamtotalinputsecondone = teamtotalinputsecondone;
	}

	public String getTeamtotaljuiceplusminussecondone() {
		return teamtotaljuiceplusminussecondone;
	}

	public void setTeamtotaljuiceplusminussecondone(String teamtotaljuiceplusminussecondone) {
		this.teamtotaljuiceplusminussecondone = teamtotaljuiceplusminussecondone;
	}

	public String getTeamtotalinputjuicesecondone() {
		return teamtotalinputjuicesecondone;
	}

	public void setTeamtotalinputjuicesecondone(String teamtotalinputjuicesecondone) {
		this.teamtotalinputjuicesecondone = teamtotalinputjuicesecondone;
	}

	public String getTeamtotalinputsecondtwo() {
		return teamtotalinputsecondtwo;
	}

	public void setTeamtotalinputsecondtwo(String teamtotalinputsecondtwo) {
		this.teamtotalinputsecondtwo = teamtotalinputsecondtwo;
	}

	public String getTeamtotaljuiceplusminussecondtwo() {
		return teamtotaljuiceplusminussecondtwo;
	}

	public void setTeamtotaljuiceplusminussecondtwo(String teamtotaljuiceplusminussecondtwo) {
		this.teamtotaljuiceplusminussecondtwo = teamtotaljuiceplusminussecondtwo;
	}

	public String getTeamtotalinputjuicesecondtwo() {
		return teamtotalinputjuicesecondtwo;
	}

	public void setTeamtotalinputjuicesecondtwo(String teamtotalinputjuicesecondtwo) {
		this.teamtotalinputjuicesecondtwo = teamtotalinputjuicesecondtwo;
	}

	
	public String getTeamtotaloverunder() {
		return teamtotaloverunder;
	}

	public void setTeamtotaloverunder(String teamtotaloverunder) {
		this.teamtotaloverunder = teamtotaloverunder;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TeamTotalPointsEvent [teamtotalinputfirstone=" + teamtotalinputfirstone + ",\n teamtotaljuiceplusminusfirstone="
				+ teamtotaljuiceplusminusfirstone + ",\n teamtotalinputjuicefirstone=" + teamtotalinputjuicefirstone
				+ ",\n teamtotalinputfirsttwo=" + teamtotalinputfirsttwo + ",\n teamtotaljuiceplusminusfirsttwo="
				+ teamtotaljuiceplusminusfirsttwo + ",\n teamtotalinputjuicefirsttwo=" + teamtotalinputjuicefirsttwo
				+ ",\n teamtotalinputsecondone=" + teamtotalinputsecondone + ",\n teamtotaljuiceplusminussecondone="
				+ teamtotaljuiceplusminussecondone + ",\n teamtotalinputjuicesecondone=" + teamtotalinputjuicesecondone
				+ ",\n teamtotalinputsecondtwo=" + teamtotalinputsecondtwo + ",\n teamtotaljuiceplusminussecondtwo="
				+ teamtotaljuiceplusminussecondtwo + ",\n teamtotalinputjuicesecondtwo=" + teamtotalinputjuicesecondtwo
				
				+ ",\n teamtotaloverunder=" + teamtotaloverunder

				+"] \n" + super.toString();
	}
}
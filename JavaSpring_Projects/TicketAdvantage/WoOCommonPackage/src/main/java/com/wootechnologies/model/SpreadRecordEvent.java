/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

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
@Table (name="spreadrecordevents")
// @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
@XmlRootElement(name = "spreadrecordevent")
@XmlAccessorType(XmlAccessType.NONE)
public class SpreadRecordEvent extends BaseRecordEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "spreadinputfirstone", nullable = true)
	@XmlElement
	private String spreadinputfirstone;
	
	@Column(name = "spreadplusminusfirstone", nullable = true)
	@XmlElement	
	private String spreadplusminusfirstone;
	
	@Column(name = "spreadinputjuicefirstone", nullable = true)
	@XmlElement		
	private String spreadinputjuicefirstone;

	@Column(name = "spreadjuiceplusminusfirstone", nullable = true)
	@XmlElement		
	private String spreadjuiceplusminusfirstone;

	@Column(name = "spreadinputfirsttwo", nullable = true)
	@XmlElement
	private String spreadinputfirsttwo;
	
	@Column(name = "spreadplusminusfirsttwo", nullable = true)
	@XmlElement	
	private String spreadplusminusfirsttwo;
	
	@Column(name = "spreadinputjuicefirsttwo", nullable = true)
	@XmlElement		
	private String spreadinputjuicefirsttwo;

	@Column(name = "spreadjuiceplusminusfirsttwo", nullable = true)
	@XmlElement		
	private String spreadjuiceplusminusfirsttwo;

	@Column(name = "spreadinputsecondone", nullable = true)
	@XmlElement
	private String spreadinputsecondone;
	
	@Column(name = "spreadplusminussecondone", nullable = true)
	@XmlElement	
	private String spreadplusminussecondone;
	
	@Column(name = "spreadinputjuicesecondone", nullable = true)
	@XmlElement		
	private String spreadinputjuicesecondone;

	@Column(name = "spreadjuiceplusminussecondone", nullable = true)
	@XmlElement		
	private String spreadjuiceplusminussecondone;

	@Column(name = "spreadinputsecondtwo", nullable = true)
	@XmlElement
	private String spreadinputsecondtwo;
	
	@Column(name = "spreadplusminussecondtwo", nullable = true)
	@XmlElement	
	private String spreadplusminussecondtwo;
	
	@Column(name = "spreadinputjuicesecondtwo", nullable = true)
	@XmlElement		
	private String spreadinputjuicesecondtwo;

	@Column(name = "spreadjuiceplusminussecondtwo", nullable = true)
	@XmlElement		
	private String spreadjuiceplusminussecondtwo;

	/**
	 * 
	 */
	public SpreadRecordEvent() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the spreadinputfirstone
	 */
	public String getSpreadinputfirstone() {
		return spreadinputfirstone;
	}

	/**
	 * @param spreadinputfirstone the spreadinputfirstone to set
	 */
	public void setSpreadinputfirstone(String spreadinputfirstone) {
		this.spreadinputfirstone = spreadinputfirstone;
	}

	/**
	 * @return the spreadplusminusfirstone
	 */
	public String getSpreadplusminusfirstone() {
		return spreadplusminusfirstone;
	}

	/**
	 * @param spreadplusminusfirstone the spreadplusminusfirstone to set
	 */
	public void setSpreadplusminusfirstone(String spreadplusminusfirstone) {
		this.spreadplusminusfirstone = spreadplusminusfirstone;
	}

	/**
	 * @return the spreadinputjuicefirstone
	 */
	public String getSpreadinputjuicefirstone() {
		return spreadinputjuicefirstone;
	}

	/**
	 * @param spreadinputjuicefirstone the spreadinputjuicefirstone to set
	 */
	public void setSpreadinputjuicefirstone(String spreadinputjuicefirstone) {
		this.spreadinputjuicefirstone = spreadinputjuicefirstone;
	}

	/**
	 * @return the spreadjuiceplusminusfirstone
	 */
	public String getSpreadjuiceplusminusfirstone() {
		return spreadjuiceplusminusfirstone;
	}

	/**
	 * @param spreadjuiceplusminusfirstone the spreadjuiceplusminusfirstone to set
	 */
	public void setSpreadjuiceplusminusfirstone(String spreadjuiceplusminusfirstone) {
		this.spreadjuiceplusminusfirstone = spreadjuiceplusminusfirstone;
	}

	/**
	 * @return the spreadinputfirsttwo
	 */
	public String getSpreadinputfirsttwo() {
		return spreadinputfirsttwo;
	}

	/**
	 * @param spreadinputfirsttwo the spreadinputfirsttwo to set
	 */
	public void setSpreadinputfirsttwo(String spreadinputfirsttwo) {
		this.spreadinputfirsttwo = spreadinputfirsttwo;
	}

	/**
	 * @return the spreadplusminusfirsttwo
	 */
	public String getSpreadplusminusfirsttwo() {
		return spreadplusminusfirsttwo;
	}

	/**
	 * @param spreadplusminusfirsttwo the spreadplusminusfirsttwo to set
	 */
	public void setSpreadplusminusfirsttwo(String spreadplusminusfirsttwo) {
		this.spreadplusminusfirsttwo = spreadplusminusfirsttwo;
	}

	/**
	 * @return the spreadinputjuicefirsttwo
	 */
	public String getSpreadinputjuicefirsttwo() {
		return spreadinputjuicefirsttwo;
	}

	/**
	 * @param spreadinputjuicefirsttwo the spreadinputjuicefirsttwo to set
	 */
	public void setSpreadinputjuicefirsttwo(String spreadinputjuicefirsttwo) {
		this.spreadinputjuicefirsttwo = spreadinputjuicefirsttwo;
	}

	/**
	 * @return the spreadjuiceplusminusfirsttwo
	 */
	public String getSpreadjuiceplusminusfirsttwo() {
		return spreadjuiceplusminusfirsttwo;
	}

	/**
	 * @param spreadjuiceplusminusfirsttwo the spreadjuiceplusminusfirsttwo to set
	 */
	public void setSpreadjuiceplusminusfirsttwo(String spreadjuiceplusminusfirsttwo) {
		this.spreadjuiceplusminusfirsttwo = spreadjuiceplusminusfirsttwo;
	}

	/**
	 * @return the spreadinputsecondone
	 */
	public String getSpreadinputsecondone() {
		return spreadinputsecondone;
	}

	/**
	 * @param spreadinputsecondone the spreadinputsecondone to set
	 */
	public void setSpreadinputsecondone(String spreadinputsecondone) {
		this.spreadinputsecondone = spreadinputsecondone;
	}

	/**
	 * @return the spreadplusminussecondone
	 */
	public String getSpreadplusminussecondone() {
		return spreadplusminussecondone;
	}

	/**
	 * @param spreadplusminussecondone the spreadplusminussecondone to set
	 */
	public void setSpreadplusminussecondone(String spreadplusminussecondone) {
		this.spreadplusminussecondone = spreadplusminussecondone;
	}

	/**
	 * @return the spreadinputjuicesecondone
	 */
	public String getSpreadinputjuicesecondone() {
		return spreadinputjuicesecondone;
	}

	/**
	 * @param spreadinputjuicesecondone the spreadinputjuicesecondone to set
	 */
	public void setSpreadinputjuicesecondone(String spreadinputjuicesecondone) {
		this.spreadinputjuicesecondone = spreadinputjuicesecondone;
	}

	/**
	 * @return the spreadjuiceplusminussecondone
	 */
	public String getSpreadjuiceplusminussecondone() {
		return spreadjuiceplusminussecondone;
	}

	/**
	 * @param spreadjuiceplusminussecondone the spreadjuiceplusminussecondone to set
	 */
	public void setSpreadjuiceplusminussecondone(String spreadjuiceplusminussecondone) {
		this.spreadjuiceplusminussecondone = spreadjuiceplusminussecondone;
	}

	/**
	 * @return the spreadinputsecondtwo
	 */
	public String getSpreadinputsecondtwo() {
		return spreadinputsecondtwo;
	}

	/**
	 * @param spreadinputsecondtwo the spreadinputsecondtwo to set
	 */
	public void setSpreadinputsecondtwo(String spreadinputsecondtwo) {
		this.spreadinputsecondtwo = spreadinputsecondtwo;
	}

	/**
	 * @return the spreadplusminussecondtwo
	 */
	public String getSpreadplusminussecondtwo() {
		return spreadplusminussecondtwo;
	}

	/**
	 * @param spreadplusminussecondtwo the spreadplusminussecondtwo to set
	 */
	public void setSpreadplusminussecondtwo(String spreadplusminussecondtwo) {
		this.spreadplusminussecondtwo = spreadplusminussecondtwo;
	}

	/**
	 * @return the spreadinputjuicesecondtwo
	 */
	public String getSpreadinputjuicesecondtwo() {
		return spreadinputjuicesecondtwo;
	}

	/**
	 * @param spreadinputjuicesecondtwo the spreadinputjuicesecondtwo to set
	 */
	public void setSpreadinputjuicesecondtwo(String spreadinputjuicesecondtwo) {
		this.spreadinputjuicesecondtwo = spreadinputjuicesecondtwo;
	}

	/**
	 * @return the spreadjuiceplusminussecondtwo
	 */
	public String getSpreadjuiceplusminussecondtwo() {
		return spreadjuiceplusminussecondtwo;
	}

	/**
	 * @param spreadjuiceplusminussecondtwo the spreadjuiceplusminussecondtwo to set
	 */
	public void setSpreadjuiceplusminussecondtwo(String spreadjuiceplusminussecondtwo) {
		this.spreadjuiceplusminussecondtwo = spreadjuiceplusminussecondtwo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SpreadRecordEvent [spreadinputfirstone=" + spreadinputfirstone
				+ ",\n spreadplusminusfirstone=" + spreadplusminusfirstone + ",\n spreadinputjuicefirstone="
				+ spreadinputjuicefirstone + ",\n spreadjuiceplusminusfirstone=" + spreadjuiceplusminusfirstone
				+ ",\n spreadinputfirsttwo=" + spreadinputfirsttwo + ",\n spreadplusminusfirsttwo="
				+ spreadplusminusfirsttwo + ",\n spreadinputjuicefirsttwo=" + spreadinputjuicefirsttwo
				+ ",\n spreadjuiceplusminusfirsttwo=" + spreadjuiceplusminusfirsttwo + ",\n spreadinputsecondone="
				+ spreadinputsecondone + ",\n spreadplusminussecondone=" + spreadplusminussecondone
				+ ",\n spreadinputjuicesecondone=" + spreadinputjuicesecondone + ",\n spreadjuiceplusminussecondone="
				+ spreadjuiceplusminussecondone + ",\n spreadinputsecondtwo=" + spreadinputsecondtwo
				+ ",\n spreadplusminussecondtwo=" + spreadplusminussecondtwo + ",\n spreadinputjuicesecondtwo="
				+ spreadinputjuicesecondtwo + ",\n spreadjuiceplusminussecondtwo=" + spreadjuiceplusminussecondtwo
				+ "] \n" + super.toString();
	}
}
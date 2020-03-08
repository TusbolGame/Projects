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
@Table (name="totalrecordevents")
@AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
@XmlRootElement(name = "totalrecordevent")
@XmlAccessorType(XmlAccessType.NONE)
public class TotalRecordEvent extends BaseRecordEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "totalinputfirstone", nullable = true)
	@XmlElement
	private String totalinputfirstone;

	@Column(name = "totaljuiceplusminusfirstone", nullable = true)
	@XmlElement
	private String totaljuiceplusminusfirstone;

	@Column(name = "totalinputjuicefirstone", nullable = true)
	@XmlElement
	private String totalinputjuicefirstone;

	@Column(name = "totalinputfirsttwo", nullable = true)
	@XmlElement
	private String totalinputfirsttwo;

	@Column(name = "totaljuiceplusminusfirsttwo", nullable = true)
	@XmlElement
	private String totaljuiceplusminusfirsttwo;

	@Column(name = "totalinputjuicefirsttwo", nullable = true)
	@XmlElement
	private String totalinputjuicefirsttwo;

	@Column(name = "totalinputsecondone", nullable = true)
	@XmlElement
	private String totalinputsecondone;

	@Column(name = "totaljuiceplusminussecondone", nullable = true)
	@XmlElement
	private String totaljuiceplusminussecondone;

	@Column(name = "totalinputjuicesecondone", nullable = true)
	@XmlElement
	private String totalinputjuicesecondone;

	@Column(name = "totalinputsecondtwo", nullable = true)
	@XmlElement
	private String totalinputsecondtwo;

	@Column(name = "totaljuiceplusminussecondtwo", nullable = true)
	@XmlElement
	private String totaljuiceplusminussecondtwo;

	@Column(name = "totalinputjuicesecondtwo", nullable = true)
	@XmlElement
	private String totalinputjuicesecondtwo;

	/**
	 * 
	 */
	public TotalRecordEvent() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the totalinputfirstone
	 */
	public String getTotalinputfirstone() {
		return totalinputfirstone;
	}

	/**
	 * @param totalinputfirstone the totalinputfirstone to set
	 */
	public void setTotalinputfirstone(String totalinputfirstone) {
		this.totalinputfirstone = totalinputfirstone;
	}

	/**
	 * @return the totaljuiceplusminusfirstone
	 */
	public String getTotaljuiceplusminusfirstone() {
		return totaljuiceplusminusfirstone;
	}

	/**
	 * @param totaljuiceplusminusfirstone the totaljuiceplusminusfirstone to set
	 */
	public void setTotaljuiceplusminusfirstone(String totaljuiceplusminusfirstone) {
		this.totaljuiceplusminusfirstone = totaljuiceplusminusfirstone;
	}

	/**
	 * @return the totalinputjuicefirstone
	 */
	public String getTotalinputjuicefirstone() {
		return totalinputjuicefirstone;
	}

	/**
	 * @param totalinputjuicefirstone the totalinputjuicefirstone to set
	 */
	public void setTotalinputjuicefirstone(String totalinputjuicefirstone) {
		this.totalinputjuicefirstone = totalinputjuicefirstone;
	}

	/**
	 * @return the totalinputfirsttwo
	 */
	public String getTotalinputfirsttwo() {
		return totalinputfirsttwo;
	}

	/**
	 * @param totalinputfirsttwo the totalinputfirsttwo to set
	 */
	public void setTotalinputfirsttwo(String totalinputfirsttwo) {
		this.totalinputfirsttwo = totalinputfirsttwo;
	}

	/**
	 * @return the totaljuiceplusminusfirsttwo
	 */
	public String getTotaljuiceplusminusfirsttwo() {
		return totaljuiceplusminusfirsttwo;
	}

	/**
	 * @param totaljuiceplusminusfirsttwo the totaljuiceplusminusfirsttwo to set
	 */
	public void setTotaljuiceplusminusfirsttwo(String totaljuiceplusminusfirsttwo) {
		this.totaljuiceplusminusfirsttwo = totaljuiceplusminusfirsttwo;
	}

	/**
	 * @return the totalinputjuicefirsttwo
	 */
	public String getTotalinputjuicefirsttwo() {
		return totalinputjuicefirsttwo;
	}

	/**
	 * @param totalinputjuicefirsttwo the totalinputjuicefirsttwo to set
	 */
	public void setTotalinputjuicefirsttwo(String totalinputjuicefirsttwo) {
		this.totalinputjuicefirsttwo = totalinputjuicefirsttwo;
	}

	/**
	 * @return the totalinputsecondone
	 */
	public String getTotalinputsecondone() {
		return totalinputsecondone;
	}

	/**
	 * @param totalinputsecondone the totalinputsecondone to set
	 */
	public void setTotalinputsecondone(String totalinputsecondone) {
		this.totalinputsecondone = totalinputsecondone;
	}

	/**
	 * @return the totaljuiceplusminussecondone
	 */
	public String getTotaljuiceplusminussecondone() {
		return totaljuiceplusminussecondone;
	}

	/**
	 * @param totaljuiceplusminussecondone the totaljuiceplusminussecondone to set
	 */
	public void setTotaljuiceplusminussecondone(String totaljuiceplusminussecondone) {
		this.totaljuiceplusminussecondone = totaljuiceplusminussecondone;
	}

	/**
	 * @return the totalinputjuicesecondone
	 */
	public String getTotalinputjuicesecondone() {
		return totalinputjuicesecondone;
	}

	/**
	 * @param totalinputjuicesecondone the totalinputjuicesecondone to set
	 */
	public void setTotalinputjuicesecondone(String totalinputjuicesecondone) {
		this.totalinputjuicesecondone = totalinputjuicesecondone;
	}

	/**
	 * @return the totalinputsecondtwo
	 */
	public String getTotalinputsecondtwo() {
		return totalinputsecondtwo;
	}

	/**
	 * @param totalinputsecondtwo the totalinputsecondtwo to set
	 */
	public void setTotalinputsecondtwo(String totalinputsecondtwo) {
		this.totalinputsecondtwo = totalinputsecondtwo;
	}

	/**
	 * @return the totaljuiceplusminussecondtwo
	 */
	public String getTotaljuiceplusminussecondtwo() {
		return totaljuiceplusminussecondtwo;
	}

	/**
	 * @param totaljuiceplusminussecondtwo the totaljuiceplusminussecondtwo to set
	 */
	public void setTotaljuiceplusminussecondtwo(String totaljuiceplusminussecondtwo) {
		this.totaljuiceplusminussecondtwo = totaljuiceplusminussecondtwo;
	}

	/**
	 * @return the totalinputjuicesecondtwo
	 */
	public String getTotalinputjuicesecondtwo() {
		return totalinputjuicesecondtwo;
	}

	/**
	 * @param totalinputjuicesecondtwo the totalinputjuicesecondtwo to set
	 */
	public void setTotalinputjuicesecondtwo(String totalinputjuicesecondtwo) {
		this.totalinputjuicesecondtwo = totalinputjuicesecondtwo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TotalRecordEvent [totalinputfirstone=" + totalinputfirstone + ",\n totaljuiceplusminusfirstone="
				+ totaljuiceplusminusfirstone + ",\n totalinputjuicefirstone=" + totalinputjuicefirstone
				+ ",\n totalinputfirsttwo=" + totalinputfirsttwo + ",\n totaljuiceplusminusfirsttwo="
				+ totaljuiceplusminusfirsttwo + ",\n totalinputjuicefirsttwo=" + totalinputjuicefirsttwo
				+ ",\n totalinputsecondone=" + totalinputsecondone + ",\n totaljuiceplusminussecondone="
				+ totaljuiceplusminussecondone + ",\n totalinputjuicesecondone=" + totalinputjuicesecondone
				+ ",\n totalinputsecondtwo=" + totalinputsecondtwo + ",\n totaljuiceplusminussecondtwo="
				+ totaljuiceplusminussecondtwo + ",\n totalinputjuicesecondtwo=" + totalinputjuicesecondtwo
				+ "] \n" + super.toString();
	}
}
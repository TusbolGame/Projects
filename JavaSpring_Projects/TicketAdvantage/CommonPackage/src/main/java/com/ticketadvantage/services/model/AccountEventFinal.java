/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author calderson
 *
 */
@Entity
@Table (name="accounteventfinal")
@XmlRootElement(name = "accounteventfinal")
@XmlAccessorType(XmlAccessType.NONE)
public class AccountEventFinal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounteventfinal_generator")
	@SequenceGenerator(name="accounteventfinal_generator", sequenceName = "accounteventfinal_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id;

	@Column(name = "accounteventid", unique = false, nullable = false)
	@XmlElement
	private Long accounteventid;

	@Column(name = "rotation1", nullable = true, length = 100)
	@XmlElement
	private String rotation1;

	@Column(name = "rotation2", nullable = true, length = 100)
	@XmlElement
	private String rotation2;

	@Column(name = "rotation1team", nullable = true, length = 100)
	@XmlElement
	private String rotation1team;

	@Column(name = "rotation2team", nullable = true, length = 100)
	@XmlElement
	private String rotation2team;
	
	@Column(name = "rotation1score", nullable = true, length = 100)
	@XmlElement
	private String rotation1score;

	@Column(name = "rotation2score", nullable = true, length = 100)
	@XmlElement
	private String rotation2score;

	@Column(name = "outcomewin", nullable = true)
	@XmlElement
	private Boolean outcomewin = new Boolean(false);

	@Column(name = "spreadindicator", nullable = true)
	@XmlElement
	private String spreadindicator;

	@Column(name = "spreadnumber", nullable = true)
	@XmlElement
	private Float spreadnumber;

	@Column(name = "spreadjuiceindicator", nullable = true)
	@XmlElement
	private String spreadjuiceindicator;

	@Column(name = "spreadjuicenumber", nullable = true)
	@XmlElement
	private Float spreadjuicenumber;

	@Column(name = "totalindicator", nullable = true)
	@XmlElement
	private String totalindicator;

	@Column(name = "totalnumber", nullable = true)
	@XmlElement
	private Float totalnumber;

	@Column(name = "totaljuiceindicator", nullable = true)
	@XmlElement
	private String totaljuiceindicator;

	@Column(name = "totaljuicenumber", nullable = true)
	@XmlElement
	private Float totaljuicenumber;

	@Column(name = "mlindicator", nullable = true)
	@XmlElement
	private String mlindicator;

	@Column(name = "mlnumber", nullable = true)
	@XmlElement
	private Float mlnumber;

	/**
	 * 
	 */
	public AccountEventFinal() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
    @PrePersist
    protected void onCreate() {
    	
    }

    /**
     * 
     */
    @PreUpdate
    protected void onUpdate() {
    	
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the rotation1team
	 */
	public String getRotation1team() {
		return rotation1team;
	}

	/**
	 * @param rotation1team the rotation1team to set
	 */
	public void setRotation1team(String rotation1team) {
		this.rotation1team = rotation1team;
	}

	/**
	 * @return the rotation2team
	 */
	public String getRotation2team() {
		return rotation2team;
	}

	/**
	 * @param rotation2team the rotation2team to set
	 */
	public void setRotation2team(String rotation2team) {
		this.rotation2team = rotation2team;
	}

	/**
	 * @return the rotation1score
	 */
	public String getRotation1score() {
		return rotation1score;
	}

	/**
	 * @param rotation1score the rotation1score to set
	 */
	public void setRotation1score(String rotation1score) {
		this.rotation1score = rotation1score;
	}

	/**
	 * @return the rotation2score
	 */
	public String getRotation2score() {
		return rotation2score;
	}

	/**
	 * @param rotation2score the rotation2score to set
	 */
	public void setRotation2score(String rotation2score) {
		this.rotation2score = rotation2score;
	}

	/**
	 * @return the outcomewin
	 */
	public Boolean getOutcomewin() {
		return outcomewin;
	}

	/**
	 * @param outcomewin the outcomewin to set
	 */
	public void setOutcomewin(Boolean outcomewin) {
		this.outcomewin = outcomewin;
	}

	/**
	 * @return the spreadindicator
	 */
	public String getSpreadindicator() {
		return spreadindicator;
	}

	/**
	 * @param spreadindicator the spreadindicator to set
	 */
	public void setSpreadindicator(String spreadindicator) {
		this.spreadindicator = spreadindicator;
	}

	/**
	 * @return the spreadnumber
	 */
	public Float getSpreadnumber() {
		return spreadnumber;
	}

	/**
	 * @param spreadnumber the spreadnumber to set
	 */
	public void setSpreadnumber(Float spreadnumber) {
		this.spreadnumber = spreadnumber;
	}

	/**
	 * @return the spreadjuiceindicator
	 */
	public String getSpreadjuiceindicator() {
		return spreadjuiceindicator;
	}

	/**
	 * @param spreadjuiceindicator the spreadjuiceindicator to set
	 */
	public void setSpreadjuiceindicator(String spreadjuiceindicator) {
		this.spreadjuiceindicator = spreadjuiceindicator;
	}

	/**
	 * @return the spreadjuicenumber
	 */
	public Float getSpreadjuicenumber() {
		return spreadjuicenumber;
	}

	/**
	 * @param spreadjuicenumber the spreadjuicenumber to set
	 */
	public void setSpreadjuicenumber(Float spreadjuicenumber) {
		this.spreadjuicenumber = spreadjuicenumber;
	}

	/**
	 * @return the totalindicator
	 */
	public String getTotalindicator() {
		return totalindicator;
	}

	/**
	 * @param totalindicator the totalindicator to set
	 */
	public void setTotalindicator(String totalindicator) {
		this.totalindicator = totalindicator;
	}

	/**
	 * @return the totalnumber
	 */
	public Float getTotalnumber() {
		return totalnumber;
	}

	/**
	 * @param totalnumber the totalnumber to set
	 */
	public void setTotalnumber(Float totalnumber) {
		this.totalnumber = totalnumber;
	}

	/**
	 * @return the totaljuiceindicator
	 */
	public String getTotaljuiceindicator() {
		return totaljuiceindicator;
	}

	/**
	 * @param totaljuiceindicator the totaljuiceindicator to set
	 */
	public void setTotaljuiceindicator(String totaljuiceindicator) {
		this.totaljuiceindicator = totaljuiceindicator;
	}

	/**
	 * @return the totaljuicenumber
	 */
	public Float getTotaljuicenumber() {
		return totaljuicenumber;
	}

	/**
	 * @param totaljuicenumber the totaljuicenumber to set
	 */
	public void setTotaljuicenumber(Float totaljuicenumber) {
		this.totaljuicenumber = totaljuicenumber;
	}

	/**
	 * @return the mlindicator
	 */
	public String getMlindicator() {
		return mlindicator;
	}

	/**
	 * @param mlindicator the mlindicator to set
	 */
	public void setMlindicator(String mlindicator) {
		this.mlindicator = mlindicator;
	}

	/**
	 * @return the mlnumber
	 */
	public Float getMlnumber() {
		return mlnumber;
	}

	/**
	 * @param mlnumber the mlnumber to set
	 */
	public void setMlnumber(Float mlnumber) {
		this.mlnumber = mlnumber;
	}

	/**
	 * @return the accounteventid
	 */
	public Long getAccounteventid() {
		return accounteventid;
	}

	/**
	 * @param accounteventid the accounteventid to set
	 */
	public void setAccounteventid(Long accounteventid) {
		this.accounteventid = accounteventid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccountEventFinal [id=" + id + ", accounteventid=" + accounteventid + ", rotation1=" + rotation1
				+ ", rotation2=" + rotation2 + ", rotation1team=" + rotation1team + ", rotation2team=" + rotation2team
				+ ", rotation1score=" + rotation1score + ", rotation2score=" + rotation2score + ", outcomewin="
				+ outcomewin + ", spreadindicator=" + spreadindicator + ", spreadnumber=" + spreadnumber
				+ ", spreadjuiceindicator=" + spreadjuiceindicator + ", spreadjuicenumber=" + spreadjuicenumber
				+ ", totalindicator=" + totalindicator + ", totalnumber=" + totalnumber + ", totaljuiceindicator="
				+ totaljuiceindicator + ", totaljuicenumber=" + totaljuicenumber + ", mlindicator=" + mlindicator
				+ ", mlnumber=" + mlnumber + "]";
	}
}
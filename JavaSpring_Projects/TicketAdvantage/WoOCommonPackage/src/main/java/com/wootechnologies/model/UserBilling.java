/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author calderson
 *
 */
@Entity
@Table (name="userbilling")
@XmlRootElement(name = "userbilling")
@XmlAccessorType(XmlAccessType.NONE)
public class UserBilling implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userbilling_generator")
	@SequenceGenerator(name="userbilling_generator", sequenceName = "userbilling_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id;

	@Column(name = "userid", nullable = false)
	@XmlElement
	private Long userid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "weekstartdate", nullable = false)
	@XmlElement
	private Date weekstartdate;
	
	@Column(name = "accountrate", nullable = true)
	@XmlElement
	private BigDecimal accountrate;
	
	@Column(name = "ispaid", nullable = true)
	@XmlElement
	private Boolean ispaid = new Boolean(false);
	
	@Column(name = "weeklybalance", nullable = true)
	@XmlElement
	private BigDecimal weeklybalance;

	@Column(name = "numberofaccounts", nullable = true)
	@XmlElement
	private Integer numberofaccounts;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USERID", referencedColumnName="ID")
	private User user;
	
	/**
	 * 
	 */
	public UserBilling() {
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
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Date getWeekstartdate() {
		return weekstartdate;
	}

	public void setWeekstartdate(Date weekstartdate) {
		this.weekstartdate = weekstartdate;
	}

	public BigDecimal getAccountrate() {
		return accountrate;
	}

	public void setAccountrate(BigDecimal accountrate) {
		this.accountrate = accountrate;
	}

	public Boolean getIspaid() {
		return ispaid;
	}

	public void setIspaid(Boolean ispaid) {
		this.ispaid = ispaid;
	}

	public BigDecimal getWeeklybalance() {
		return weeklybalance;
	}

	public void setWeeklybalance(BigDecimal weeklybalance) {
		this.weeklybalance = weeklybalance;
	}

	public Integer getNumberofaccounts() {
		return numberofaccounts;
	}

	public void setNumberofaccounts(Integer numberofaccounts) {
		this.numberofaccounts = numberofaccounts;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserBilling [id=" + id + ", userid=" + userid
				+ ", weekstartdate=" + weekstartdate + ", accountrate=" + accountrate + ", ispaid=" + ispaid + ", weeklybalance=" + weeklybalance
				+ ", numberofaccounts=" + numberofaccounts + "]";
	}
}
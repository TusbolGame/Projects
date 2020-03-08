/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author calderson
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.NONE)
public class WeeklyFigures implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private Long userId;

	@Id
	@XmlElement
	private Long accountId;

	//@XmlElement
	//private Long accountEventId;

	@XmlElement
	private String accountName;

	@XmlElement
	private String accountPassword;
	
	@XmlElement
	private Float mon;
	
	@XmlElement
	private Float tue;
	
	@XmlElement
	private Float wed;
	
	@XmlElement
	private Float thu;
	
	@XmlElement
	private Float fri;
	
	@XmlElement
	private Float sat;
	
	@XmlElement
	private Float sun;
	
	@XmlElement
	private Float week;
	
	@XmlElement
	private Float pending;
	
	@XmlElement
	private Float balance;

	@XmlElement
	private Float carry;

	/**
	 * 
	 */
	public WeeklyFigures() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	//public Long getAccountEventId() {
	//	return accountEventId;
	//}

	//public void setAccountEventId(Long accountEventId) {
	//	this.accountEventId = accountEventId;
	//}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public Float getMon() {
		if (this.mon != null) {
			return this.mon;
		} else {
			return new Float(0);
		}
	}

	public void setMon(Float mon) {
		this.mon = mon;
	}

	public Float getTue() {
		if (this.tue != null) {
			return this.tue;
		} else {
			return new Float(0);
		}
	}

	public void setTue(Float tue) {
		this.tue = tue;
	}

	public Float getWed() {
		if (this.wed != null) {
			return this.wed;
		} else {
			return new Float(0);
		}
	}

	public void setWed(Float wed) {
		this.wed = wed;
	}

	public Float getThu() {
		if (this.thu != null) {
			return this.thu;
		} else {
			return new Float(0);
		}
	}

	public void setThu(Float thu) {
		this.thu = thu;
	}

	public Float getFri() {
		if (this.fri != null) {
			return this.fri;
		} else {
			return new Float(0);
		}
	}

	public void setFri(Float fri) {
		this.fri = fri;
	}

	public Float getSat() {
		if (this.sat != null) {
			return this.sat;
		} else {
			return new Float(0);
		}
	}

	public void setSat(Float sat) {
		this.sat = sat;
	}

	public Float getSun() {
		if (this.sun != null) {
			return this.sun;
		} else {
			return new Float(0);
		}
	}

	public void setSun(Float sun) {
		this.sun = sun;
	}

	public Float getWeek() {
		if (this.week != null) {
			return this.week;
		} else {
			return new Float(0);
		}
	}

	public void setWeek(Float week) {
		this.week = week;
	}

	public Float getPending() {
		if (this.pending != null) {
			return this.pending;
		} else {
			return new Float(0);
		}
	}

	public void setPending(Float pending) {
		this.pending = pending;
	}

	public Float getBalance() {
		if (this.balance != null) {
			return this.balance;
		} else {
			return new Float(0);
		}
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Float getCarry() {
		if (this.carry != null) {
			return this.carry;
		} else {
			return new Float(0);
		}
	}

	public void setCarry(Float carry) {
		this.carry = carry;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WeeklyFigures [userId=" + userId + ", accountId=" + accountId + ", accountName=" + accountName
				+ ", accountPassword=" + accountPassword + ", mon=" + mon + ", tue=" + tue + ", wed=" + wed + ", thu="
				+ thu + ", fri=" + fri + ", sat=" + sat + ", sun=" + sun + ", week=" + week + ", pending=" + pending
				+ ", balance=" + balance + ", carry=" + carry + "]";
	}
}
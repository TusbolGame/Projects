/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table (name="scrapperdestinations")
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class ScrapperDestinations implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "scrapperid", unique = false, nullable = false)
	@XmlElement
	private Long scrapperid;

	@Column(name = "accountid", unique = false, nullable = false)
	@XmlElement
	private Long accountid;

	/**
	 * 
	 */
	public ScrapperDestinations() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the scrapperid
	 */
	public Long getScrapperid() {
		return scrapperid;
	}

	/**
	 * @param scrapperid the scrapperid to set
	 */
	public void setScrapperid(Long scrapperid) {
		this.scrapperid = scrapperid;
	}

	/**
	 * @return the accountid
	 */
	public Long getAccountid() {
		return accountid;
	}

	/**
	 * @param accountid the accountid to set
	 */
	public void setAccountid(Long accountid) {
		this.accountid = accountid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScrapperDestinations [scrapperid=" + scrapperid + ", accountid=" + accountid + "]";
	}
}
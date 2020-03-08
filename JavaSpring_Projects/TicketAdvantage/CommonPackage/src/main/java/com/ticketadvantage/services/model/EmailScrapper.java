/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
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
@Table(name = "emailscrapper")
@AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
@XmlRootElement(name = "emailscrapper")
@XmlAccessorType(XmlAccessType.NONE)
public class EmailScrapper extends BaseScrapper implements Serializable, Comparable<EmailScrapper> {
	private static final long serialVersionUID = 1L;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="emailscrappersources", 
    		joinColumns={@JoinColumn(name="emailid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="emailaccountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<EmailAccounts> sources;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="emailscrapperdestinations", 
    		joinColumns={@JoinColumn(name="emailid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> destinations;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="emailmiddledestinations", 
    		joinColumns={@JoinColumn(name="emailid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> emailmiddledestinations;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="emailorderdestinations", 
    		joinColumns={@JoinColumn(name="emailid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> emailorderdestinations;

	/**
	 * @return the sources
	 */
	public Set<EmailAccounts> getSources() {
		return sources;
	}

	/**
	 * @param sources the sources to set
	 */
	public void setSources(Set<EmailAccounts> sources) {
		this.sources = sources;
	}

	/**
	 * @return the destinations
	 */
	public Set<Accounts> getDestinations() {
		return destinations;
	}

	/**
	 * @param destinations the destinations to set
	 */
	public void setDestinations(Set<Accounts> destinations) {
		this.destinations = destinations;
	}

	/**
	 * @return the emailmiddledestinations
	 */
	public Set<Accounts> getEmailmiddledestinations() {
		return emailmiddledestinations;
	}

	/**
	 * @param emailmiddledestinations the emailmiddledestinations to set
	 */
	public void setEmailmiddledestinations(Set<Accounts> emailmiddledestinations) {
		this.emailmiddledestinations = emailmiddledestinations;
	}

	/**
	 * @return the emailorderdestinations
	 */
	public Set<Accounts> getEmailorderdestinations() {
		return emailorderdestinations;
	}

	/**
	 * @param emailorderdestinations the emailorderdestinations to set
	 */
	public void setEmailorderdestinations(Set<Accounts> emailorderdestinations) {
		this.emailorderdestinations = emailorderdestinations;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EmailScrapper o) {
	    if (getScrappername() == null || o.getScrappername() == null)
	        return 0;
	    return getScrappername().compareTo(o.getScrappername());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailScrapper [sources=" + sources + ", destinations=" + destinations + ", emailmiddledestinations="
				+ emailmiddledestinations + ", emailorderdestinations=" + emailorderdestinations + "] " + super.toString();
	}
}
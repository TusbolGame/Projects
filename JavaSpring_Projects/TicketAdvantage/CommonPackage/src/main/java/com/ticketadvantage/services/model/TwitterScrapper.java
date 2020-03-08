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
@Table(name = "twitterscrapper")
@AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
@XmlRootElement(name = "twitterscrapper")
@XmlAccessorType(XmlAccessType.NONE)
public class TwitterScrapper extends BaseScrapper implements Serializable, Comparable<TwitterScrapper> {
	private static final long serialVersionUID = 1L;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="twitterscrappersources", 
    		joinColumns={@JoinColumn(name="twitterid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="twitteraccountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<TwitterAccounts> sources;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="twitterscrapperdestinations", 
    		joinColumns={@JoinColumn(name="twitterid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> destinations;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="twittermiddledestinations", 
    		joinColumns={@JoinColumn(name="twitterid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> twittermiddledestinations;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="twitterorderdestinations", 
    		joinColumns={@JoinColumn(name="twitterid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> twitterorderdestinations;

	/**
	 * @return the sources
	 */
	public Set<TwitterAccounts> getSources() {
		return sources;
	}

	/**
	 * @param sources the sources to set
	 */
	public void setSources(Set<TwitterAccounts> sources) {
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
	 * @return the twittermiddledestinations
	 */
	public Set<Accounts> getTwittermiddledestinations() {
		return twittermiddledestinations;
	}

	/**
	 * @param twittermiddledestinations the twittermiddledestinations to set
	 */
	public void setTwittermiddledestinations(Set<Accounts> twittermiddledestinations) {
		this.twittermiddledestinations = twittermiddledestinations;
	}

	/**
	 * @return the twitterorderdestinations
	 */
	public Set<Accounts> getTwitterorderdestinations() {
		return twitterorderdestinations;
	}

	/**
	 * @param twitterorderdestinations the twitterorderdestinations to set
	 */
	public void setTwitterorderdestinations(Set<Accounts> twitterorderdestinations) {
		this.twitterorderdestinations = twitterorderdestinations;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TwitterScrapper o) {
	    if (getScrappername() == null || o.getScrappername() == null)
	        return 0;
	    return getScrappername().compareTo(o.getScrappername());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TwitterScrapper [sources=" + sources + ", destinations=" + destinations + ", twittermiddledestinations="
				+ twittermiddledestinations + ", twitterorderdestinations=" + twitterorderdestinations + "]";
	}
}
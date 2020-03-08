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
@Table(name = "webscrapper")
@AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
@XmlRootElement(name = "webscrapper")
@XmlAccessorType(XmlAccessType.NONE)
public class WebScrapper extends BaseScrapper implements Serializable, Comparable<WebScrapper> {
	private static final long serialVersionUID = 1L;
	
	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="scrappersources", 
    		joinColumns={@JoinColumn(name="scrapperid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> sources;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="scrapperdestinations", 
    		joinColumns={@JoinColumn(name="scrapperid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> destinations;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="middledestinations", 
    		joinColumns={@JoinColumn(name="scrapperid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> middledestinations;

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="orderdestinations", 
    		joinColumns={@JoinColumn(name="scrapperid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> orderdestinations;
	
	/**
	 * @return the sources
	 */
	public Set<Accounts> getSources() {
		return sources;
	}

	/**
	 * @param sources the sources to set
	 */
	public void setSources(Set<Accounts> sources) {
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
	 * @return the middledestinations
	 */
	public Set<Accounts> getMiddledestinations() {
		return middledestinations;
	}

	/**
	 * @param middledestinations the middledestinations to set
	 */
	public void setMiddledestinations(Set<Accounts> middledestinations) {
		this.middledestinations = middledestinations;
	}

	/**
	 * @return the orderdestinations
	 */
	public Set<Accounts> getOrderdestinations() {
		return orderdestinations;
	}

	/**
	 * @param orderdestinations the orderdestinations to set
	 */
	public void setOrderdestinations(Set<Accounts> orderdestinations) {
		this.orderdestinations = orderdestinations;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(WebScrapper o) {
	    if (getScrappername() == null || o.getScrappername() == null)
	        return 0;
	    return getScrappername().compareTo(o.getScrappername());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebScrapper [sources=" + sources + ", destinations=" + destinations + ", middledestinations="
				+ middledestinations + ", orderdestinations=" + orderdestinations + "] " + super.toString();
	}
}
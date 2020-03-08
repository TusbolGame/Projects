/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class ProxyLocation implements Serializable, Comparable<ProxyLocation> {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private String name;

	/**
	 * 
	 */
	public ProxyLocation() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ProxyLocation o) {
	    if (getName() == null || o.getName() == null)
	        return 0;
	    return getName().compareTo(o.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProxyLocations [name=" + name + "]";
	}
}
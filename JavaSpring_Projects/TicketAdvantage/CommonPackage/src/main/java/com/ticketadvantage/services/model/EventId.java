/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author jmiller
 *
 */
@XmlRootElement(name = "eventid")
@XmlAccessorType(XmlAccessType.NONE)
public class EventId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "id", unique = true, nullable = false)
	@XmlElement
	private Long id;

    /**
	 * 
	 */
	public EventId() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventId [id=" + id + "]";
	}
}
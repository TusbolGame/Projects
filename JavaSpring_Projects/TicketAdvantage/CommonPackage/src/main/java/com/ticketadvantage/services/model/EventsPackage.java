/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
public class EventsPackage implements Serializable {
	private static final long serialVersionUID = 1L;

    @XmlElement
	private Long id;
	
    @XmlElement
	private Set<EventPackage> events;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Do nothing
	}

	/**
	 * 
	 */
	public EventsPackage() {
		super();
	}

	/**
	 * @return the events
	 */
	public Set<EventPackage> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(Set<EventPackage> events) {
		this.events = events;
	}

	/**
	 * @param events
	 */
	public void addEvents(Set<EventPackage> events) {
		if (events != null) {
			Iterator<EventPackage> itr = events.iterator();
			while (itr != null && itr.hasNext()) {
				if (this.events == null) {
					this.events = events;
				} else {
					this.events.add(itr.next());
				}
			}
		}
	}

	/**
	 * @param events
	 */
	public void addEvent(EventPackage event) {
		if (event != null) {
			if (this.events == null) {
				this.events = new HashSet<EventPackage>();
			}
			this.events.add(event);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventsPackage [events=" + events + "]";
	}
}

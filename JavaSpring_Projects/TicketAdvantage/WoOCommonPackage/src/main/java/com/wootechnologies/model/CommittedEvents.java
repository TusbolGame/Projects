/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;
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
public class CommittedEvents extends BaseRecordEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private Set<CommittedEvent> committedevents;

	/**
	 * 
	 */
	public CommittedEvents() {
		super();
	}

	/**
	 * @return the committedevents
	 */
	public Set<CommittedEvent> getCommittedevents() {
		return committedevents;
	}

	/**
	 * @param committedevents the committedevents to set
	 */
	public void setCommittedevents(Set<CommittedEvent> committedevents) {
		this.committedevents = committedevents;
	}

	/**
	 * 
	 * @param committedevent
	 */
	public void addCommittedevent(CommittedEvent committedevent) {
		this.committedevents.add(committedevent);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommittedEvents [committedevents=" + committedevents + "]";
	}
}
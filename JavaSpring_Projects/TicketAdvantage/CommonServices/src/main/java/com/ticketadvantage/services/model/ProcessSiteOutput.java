/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author jmiller
 *
 */
@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.NONE)
public class ProcessSiteOutput implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private Integer outputvalue = null;

    /**
	 * 
	 */
	public ProcessSiteOutput() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the outputvalue
	 */
	public Integer getOutputvalue() {
		return outputvalue;
	}

	/**
	 * @param outputvalue the outputvalue to set
	 */
	public void setOutputvalue(Integer outputvalue) {
		this.outputvalue = outputvalue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProcessSiteOutput [outputvalue=" + outputvalue + "]";
	}
}
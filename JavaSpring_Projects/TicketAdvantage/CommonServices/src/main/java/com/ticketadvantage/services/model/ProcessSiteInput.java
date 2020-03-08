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
@XmlRootElement(name = "ProcessSiteInput")
@XmlAccessorType(XmlAccessType.NONE)
public class ProcessSiteInput implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private Integer inputvalue = null;

    /**
	 * 
	 */
	public ProcessSiteInput() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the inputvalue
	 */
	public Integer getInputvalue() {
		return inputvalue;
	}

	/**
	 * @param inputvalue the inputvalue to set
	 */
	public void setInputvalue(Integer inputvalue) {
		this.inputvalue = inputvalue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProcessSiteInput [inputvalue=" + inputvalue + "]";
	}
}
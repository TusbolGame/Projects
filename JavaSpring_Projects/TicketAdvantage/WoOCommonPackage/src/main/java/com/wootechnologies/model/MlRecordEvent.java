/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table (name="mlrecordevents")
@AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
@XmlRootElement(name = "mlrecordevent")
@XmlAccessorType(XmlAccessType.NONE)
public class MlRecordEvent extends BaseRecordEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "mlinputfirstone", nullable = true)
	@XmlElement
	private String mlinputfirstone;

	@Column(name = "mlplusminusfirstone", nullable = true)
	@XmlElement
	private String mlplusminusfirstone;

	@Column(name = "mlinputfirsttwo", nullable = true)
	@XmlElement
	private String mlinputfirsttwo;

	@Column(name = "mlplusminusfirsttwo", nullable = true)
	@XmlElement
	private String mlplusminusfirsttwo;

	@Column(name = "mlinputsecondone", nullable = true)
	@XmlElement
	private String mlinputsecondone;

	@Column(name = "mlplusminussecondone", nullable = true)
	@XmlElement
	private String mlplusminussecondone;

	@Column(name = "mlinputsecondtwo", nullable = true)
	@XmlElement
	private String mlinputsecondtwo;
	
	@Column(name = "mlplusminussecondtwo", nullable = true)
	@XmlElement
	private String mlplusminussecondtwo;

	/**
	 * 
	 */
	public MlRecordEvent() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the mlinputfirstone
	 */
	public String getMlinputfirstone() {
		return mlinputfirstone;
	}

	/**
	 * @param mlinputfirstone the mlinputfirstone to set
	 */
	public void setMlinputfirstone(String mlinputfirstone) {
		this.mlinputfirstone = mlinputfirstone;
	}

	/**
	 * @return the mlplusminusfirstone
	 */
	public String getMlplusminusfirstone() {
		return mlplusminusfirstone;
	}

	/**
	 * @param mlplusminusfirstone the mlplusminusfirstone to set
	 */
	public void setMlplusminusfirstone(String mlplusminusfirstone) {
		this.mlplusminusfirstone = mlplusminusfirstone;
	}

	/**
	 * @return the mlinputfirsttwo
	 */
	public String getMlinputfirsttwo() {
		return mlinputfirsttwo;
	}

	/**
	 * @param mlinputfirsttwo the mlinputfirsttwo to set
	 */
	public void setMlinputfirsttwo(String mlinputfirsttwo) {
		this.mlinputfirsttwo = mlinputfirsttwo;
	}

	/**
	 * @return the mlplusminusfirsttwo
	 */
	public String getMlplusminusfirsttwo() {
		return mlplusminusfirsttwo;
	}

	/**
	 * @param mlplusminusfirsttwo the mlplusminusfirsttwo to set
	 */
	public void setMlplusminusfirsttwo(String mlplusminusfirsttwo) {
		this.mlplusminusfirsttwo = mlplusminusfirsttwo;
	}

	/**
	 * @return the mlinputsecondone
	 */
	public String getMlinputsecondone() {
		return mlinputsecondone;
	}

	/**
	 * @param mlinputsecondone the mlinputsecondone to set
	 */
	public void setMlinputsecondone(String mlinputsecondone) {
		this.mlinputsecondone = mlinputsecondone;
	}

	/**
	 * @return the mlplusminussecondone
	 */
	public String getMlplusminussecondone() {
		return mlplusminussecondone;
	}

	/**
	 * @param mlplusminussecondone the mlplusminussecondone to set
	 */
	public void setMlplusminussecondone(String mlplusminussecondone) {
		this.mlplusminussecondone = mlplusminussecondone;
	}

	/**
	 * @return the mlinputsecondtwo
	 */
	public String getMlinputsecondtwo() {
		return mlinputsecondtwo;
	}

	/**
	 * @param mlinputsecondtwo the mlinputsecondtwo to set
	 */
	public void setMlinputsecondtwo(String mlinputsecondtwo) {
		this.mlinputsecondtwo = mlinputsecondtwo;
	}

	/**
	 * @return the mlplusminussecondtwo
	 */
	public String getMlplusminussecondtwo() {
		return mlplusminussecondtwo;
	}

	/**
	 * @param mlplusminussecondtwo the mlplusminussecondtwo to set
	 */
	public void setMlplusminussecondtwo(String mlplusminussecondtwo) {
		this.mlplusminussecondtwo = mlplusminussecondtwo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MlRecordEvent [mlinputfirstone=" + mlinputfirstone + ",\n mlplusminusfirstone=" + mlplusminusfirstone
				+ ",\n mlinputfirsttwo=" + mlinputfirsttwo + ",\n mlplusminusfirsttwo=" + mlplusminusfirsttwo
				+ ",\n mlinputsecondone=" + mlinputsecondone + ",\n mlplusminussecondone=" + mlplusminussecondone
				+ ",\n mlinputsecondtwo=" + mlinputsecondtwo + ",\n mlplusminussecondtwo=" + mlplusminussecondtwo
				+ "] \n" + super.toString();
	}
}
/**
 * 
 */
package com.wootechnologies.dataminer.model;

/**
 * @author jmiller
 *
 */
public class FloatData {
	private Float floatdata = new Float(0);
	private Float oppfloatdata = new Float(0);
	private Float floatdatatotal = new Float(0);
	private Float oppfloatdatatotal = new Float(0);
	private Integer numgames = new Integer(0);

	/**
	 * 
	 */
	public FloatData() {
		super();
	}

	/**
	 * @return the floatdata
	 */
	public Float getFloatdata() {
		return floatdata;
	}

	/**
	 * @param floatdata the floatdata to set
	 */
	public void setFloatdata(Float floatdata) {
		this.floatdata = floatdata;
	}

	/**
	 * @return the oppfloatdata
	 */
	public Float getOppfloatdata() {
		return oppfloatdata;
	}

	/**
	 * @param oppfloatdata the oppfloatdata to set
	 */
	public void setOppfloatdata(Float oppfloatdata) {
		this.oppfloatdata = oppfloatdata;
	}

	/**
	 * @return the floatdatatotal
	 */
	public Float getFloatdatatotal() {
		return floatdatatotal;
	}

	/**
	 * @param floatdatatotal the floatdatatotal to set
	 */
	public void setFloatdatatotal(Float floatdatatotal) {
		this.floatdatatotal = floatdatatotal;
	}

	/**
	 * @return the oppfloatdatatotal
	 */
	public Float getOppfloatdatatotal() {
		return oppfloatdatatotal;
	}

	/**
	 * @param oppfloatdatatotal the oppfloatdatatotal to set
	 */
	public void setOppfloatdatatotal(Float oppfloatdatatotal) {
		this.oppfloatdatatotal = oppfloatdatatotal;
	}

	/**
	 * @return the numgames
	 */
	public Integer getNumgames() {
		return numgames;
	}

	/**
	 * @param numgames the numgames to set
	 */
	public void setNumgames(Integer numgames) {
		this.numgames = numgames;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FloatData [floatdata=" + floatdata + ", oppfloatdata=" + oppfloatdata + ", floatdatatotal="
				+ floatdatatotal + ", oppfloatdatatotal=" + oppfloatdatatotal + ", numgames=" + numgames + "]";
	}
}
/**
 * 
 */
package com.wootechnologies.dataminer.model;

/**
 * @author jmiller
 *
 */
public class Efficiencies extends FloatData {
	private Float made;
	private Float attempts;
	private Float oppmade;
	private Float oppattempts;
	private Float madetotal;
	private Float attemptstotal;
	private Float oppmadetotal;
	private Float oppattemptstotal;

	/**
	 * 
	 */
	public Efficiencies() {
		super();
	}

	/**
	 * @return the made
	 */
	public Float getMade() {
		return made;
	}

	/**
	 * @param made the made to set
	 */
	public void setMade(Float made) {
		this.made = made;
	}

	/**
	 * @return the attempts
	 */
	public Float getAttempts() {
		return attempts;
	}

	/**
	 * @param attempts the attempts to set
	 */
	public void setAttempts(Float attempts) {
		this.attempts = attempts;
	}

	/**
	 * @return the oppmade
	 */
	public Float getOppmade() {
		return oppmade;
	}

	/**
	 * @param oppmade the oppmade to set
	 */
	public void setOppmade(Float oppmade) {
		this.oppmade = oppmade;
	}

	/**
	 * @return the oppattempts
	 */
	public Float getOppattempts() {
		return oppattempts;
	}

	/**
	 * @param oppattempts the oppattempts to set
	 */
	public void setOppattempts(Float oppattempts) {
		this.oppattempts = oppattempts;
	}

	/**
	 * @return the madetotal
	 */
	public Float getMadetotal() {
		return madetotal;
	}

	/**
	 * @param madetotal the madetotal to set
	 */
	public void setMadetotal(Float madetotal) {
		this.madetotal = madetotal;
	}

	/**
	 * @return the attemptstotal
	 */
	public Float getAttemptstotal() {
		return attemptstotal;
	}

	/**
	 * @param attemptstotal the attemptstotal to set
	 */
	public void setAttemptstotal(Float attemptstotal) {
		this.attemptstotal = attemptstotal;
	}

	/**
	 * @return the oppmadetotal
	 */
	public Float getOppmadetotal() {
		return oppmadetotal;
	}

	/**
	 * @param oppmadetotal the oppmadetotal to set
	 */
	public void setOppmadetotal(Float oppmadetotal) {
		this.oppmadetotal = oppmadetotal;
	}

	/**
	 * @return the oppattemptstotal
	 */
	public Float getOppattemptstotal() {
		return oppattemptstotal;
	}

	/**
	 * @param oppattemptstotal the oppattemptstotal to set
	 */
	public void setOppattemptstotal(Float oppattemptstotal) {
		this.oppattemptstotal = oppattemptstotal;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Efficiencies [made=" + made + ", attempts=" + attempts + ", oppmade=" + oppmade + ", oppattempts="
				+ oppattempts + ", madetotal=" + madetotal + ", attemptstotal=" + attemptstotal + ", oppmadetotal="
				+ oppmadetotal + ", oppattemptstotal=" + oppattemptstotal + "] " + super.toString();
	}
}
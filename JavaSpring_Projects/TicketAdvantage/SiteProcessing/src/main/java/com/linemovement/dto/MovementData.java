package com.linemovement.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
* @author SureshKoumar @ Pinesucceed
* 06-May-2019
*/

@JsonAutoDetect
public class MovementData {

	/**
	 * line date
	 */
	@JsonProperty
	private LocalDateTime lineDate;
	
	/**
	 * game date and time
	 */
	@JsonProperty
	private LocalDateTime lineTime;
	
	/**
	 * // visit team line (Fav)
	 */
	@JsonProperty
	private double lineone; 
	
	/**
	 * // home team line (Dog)
	 */
	@JsonProperty
	private double linetwo; 

	/**
	 * // visit team juice (Fav)
	 */
	@JsonProperty
	private double juiceone; 

	/**
	 * // home team juice ((Dog)
	 */
	@JsonProperty
	private double juicetwo; 
	
	/**
	 * // visit team indicator (Fav)
	 */
	@JsonProperty
	private String lineindicator1; 
	
	/**
	 * // home team indicatortwo (Dog)
	 */
	@JsonProperty
	private String lineindicator2;
	
	/**
	 * // visit team indicator (Fav)
	 */
	@JsonProperty
	private String juiceindicator1; 
	
	/**
	 * // home team indicatortwo (Dog)
	 */
	@JsonProperty
	private String juiceindicator2;

	
	@Override
	public boolean equals(Object obj) {
		MovementData object = (MovementData)obj;
		
		if((this.getJuiceone() == object.getJuiceone()) && (this.getLineone() == object.getLineone()))
			return true;
		else return false;
	}
	
	@Override
	public int hashCode() {
		int hash = (this.getLineindicator1()+this.getLineone()+this.getJuiceindicator1()+this.getJuiceone()+
				this.getLineindicator2()+this.getLinetwo()+this.getJuiceindicator2()+this.getJuicetwo()+"").hashCode();
		return hash;
	}
	
	public LocalDateTime getLineDate() {
		return lineDate;
	}

	public void setLineDate(LocalDateTime lineDate) {
		this.lineDate = lineDate;
	}

	public LocalDateTime getLineTime() {
		return lineTime;
	}

	public void setLineTime(LocalDateTime lineTime) {
		this.lineTime = lineTime;
	}

	public double getLineone() {
		return lineone;
	}

	public void setLineone(double lineone) {
		this.lineone = lineone;
	}

	public double getLinetwo() {
		return linetwo;
	}

	public void setLinetwo(double linetwo) {
		this.linetwo = linetwo;
	}

	public double getJuiceone() {
		return juiceone;
	}

	public void setJuiceone(double juiceone) {
		this.juiceone = juiceone;
	}

	public double getJuicetwo() {
		return juicetwo;
	}

	public void setJuicetwo(double juicetwo) {
		this.juicetwo = juicetwo;
	}

	public String getLineindicator1() {
		return lineindicator1;
	}

	public void setLineindicator1(String lineindicator1) {
		this.lineindicator1 = lineindicator1;
	}

	public String getLineindicator2() {
		return lineindicator2;
	}

	public void setLineindicator2(String lineindicator2) {
		this.lineindicator2 = lineindicator2;
	}

	public String getJuiceindicator1() {
		return juiceindicator1;
	}

	public void setJuiceindicator1(String juiceindicator1) {
		this.juiceindicator1 = juiceindicator1;
	}

	public String getJuiceindicator2() {
		return juiceindicator2;
	}

	public void setJuiceindicator2(String juiceindicator2) {
		this.juiceindicator2 = juiceindicator2;
	}
	
	@Override
	public String toString() {
		return "MovementData [lineDate=" + lineDate + ",\n lineTime=" + lineTime + ",\n lineone=" + lineone + ",\n linetwo="
				+ linetwo + ",\n juiceone=" + juiceone + ",\n juicetwo=" + juicetwo + ",\n lineindicator1=" + lineindicator1
				+ ",\n lineindicator2=" + lineindicator2 + ",\n juiceindicator1=" + juiceindicator1 + ",\n juiceindicator2="
				+ juiceindicator2 + "]";
	}	
}
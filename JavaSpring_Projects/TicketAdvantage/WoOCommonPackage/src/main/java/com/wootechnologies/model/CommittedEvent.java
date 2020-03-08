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
public class CommittedEvent extends BaseRecordEvent implements Serializable, Comparable<CommittedEvent> {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private String spreadinputfirstone;
	
	@XmlElement	
	private String spreadplusminusfirstone;
	
	@XmlElement		
	private String spreadinputjuicefirstone;

	@XmlElement		
	private String spreadjuiceplusminusfirstone;

	@XmlElement
	private String spreadinputfirsttwo;
	
	@XmlElement	
	private String spreadplusminusfirsttwo;
	
	@XmlElement		
	private String spreadinputjuicefirsttwo;

	@XmlElement		
	private String spreadjuiceplusminusfirsttwo;

	@XmlElement
	private String spreadinputsecondone;
	
	@XmlElement	
	private String spreadplusminussecondone;
	
	@XmlElement		
	private String spreadinputjuicesecondone;

	@XmlElement		
	private String spreadjuiceplusminussecondone;

	@XmlElement
	private String spreadinputsecondtwo;
	
	@XmlElement	
	private String spreadplusminussecondtwo;
	
	@XmlElement		
	private String spreadinputjuicesecondtwo;

	@XmlElement		
	private String spreadjuiceplusminussecondtwo;

	@XmlElement
	private String totalinputfirstone;

	@XmlElement
	private String totaljuiceplusminusfirstone;

	@XmlElement
	private String totalinputjuicefirstone;

	@XmlElement
	private String totalinputfirsttwo;

	@XmlElement
	private String totaljuiceplusminusfirsttwo;

	@XmlElement
	private String totalinputjuicefirsttwo;

	@XmlElement
	private String totalinputsecondone;

	@XmlElement
	private String totaljuiceplusminussecondone;

	@XmlElement
	private String totalinputjuicesecondone;

	@XmlElement
	private String totalinputsecondtwo;

	@XmlElement
	private String totaljuiceplusminussecondtwo;

	@XmlElement
	private String totalinputjuicesecondtwo;

	@XmlElement
	private String mlinputfirstone;

	@XmlElement
	private String mlplusminusfirstone;

	@XmlElement
	private String mlinputfirsttwo;

	@XmlElement
	private String mlplusminusfirsttwo;

	@XmlElement
	private String mlinputsecondone;

	@XmlElement
	private String mlplusminussecondone;

	@XmlElement
	private String mlinputsecondtwo;
	
	@XmlElement
	private String mlplusminussecondtwo;

	@XmlElement
	private String riskAmount;

	@XmlElement
	private String toWinAmount;

	@XmlElement
	private Set<AccountEvent> accountevents;

	/**
	 * 
	 */
	public CommittedEvent() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return the spreadinputfirstone
	 */
	public String getSpreadinputfirstone() {
		return spreadinputfirstone;
	}

	/**
	 * @param spreadinputfirstone the spreadinputfirstone to set
	 */
	public void setSpreadinputfirstone(String spreadinputfirstone) {
		this.spreadinputfirstone = spreadinputfirstone;
	}

	/**
	 * @return the spreadplusminusfirstone
	 */
	public String getSpreadplusminusfirstone() {
		return spreadplusminusfirstone;
	}

	/**
	 * @param spreadplusminusfirstone the spreadplusminusfirstone to set
	 */
	public void setSpreadplusminusfirstone(String spreadplusminusfirstone) {
		this.spreadplusminusfirstone = spreadplusminusfirstone;
	}

	/**
	 * @return the spreadinputjuicefirstone
	 */
	public String getSpreadinputjuicefirstone() {
		return spreadinputjuicefirstone;
	}

	/**
	 * @param spreadinputjuicefirstone the spreadinputjuicefirstone to set
	 */
	public void setSpreadinputjuicefirstone(String spreadinputjuicefirstone) {
		this.spreadinputjuicefirstone = spreadinputjuicefirstone;
	}

	/**
	 * @return the spreadjuiceplusminusfirstone
	 */
	public String getSpreadjuiceplusminusfirstone() {
		return spreadjuiceplusminusfirstone;
	}

	/**
	 * @param spreadjuiceplusminusfirstone the spreadjuiceplusminusfirstone to set
	 */
	public void setSpreadjuiceplusminusfirstone(String spreadjuiceplusminusfirstone) {
		this.spreadjuiceplusminusfirstone = spreadjuiceplusminusfirstone;
	}

	/**
	 * @return the spreadinputfirsttwo
	 */
	public String getSpreadinputfirsttwo() {
		return spreadinputfirsttwo;
	}

	/**
	 * @param spreadinputfirsttwo the spreadinputfirsttwo to set
	 */
	public void setSpreadinputfirsttwo(String spreadinputfirsttwo) {
		this.spreadinputfirsttwo = spreadinputfirsttwo;
	}

	/**
	 * @return the spreadplusminusfirsttwo
	 */
	public String getSpreadplusminusfirsttwo() {
		return spreadplusminusfirsttwo;
	}

	/**
	 * @param spreadplusminusfirsttwo the spreadplusminusfirsttwo to set
	 */
	public void setSpreadplusminusfirsttwo(String spreadplusminusfirsttwo) {
		this.spreadplusminusfirsttwo = spreadplusminusfirsttwo;
	}

	/**
	 * @return the spreadinputjuicefirsttwo
	 */
	public String getSpreadinputjuicefirsttwo() {
		return spreadinputjuicefirsttwo;
	}

	/**
	 * @param spreadinputjuicefirsttwo the spreadinputjuicefirsttwo to set
	 */
	public void setSpreadinputjuicefirsttwo(String spreadinputjuicefirsttwo) {
		this.spreadinputjuicefirsttwo = spreadinputjuicefirsttwo;
	}

	/**
	 * @return the spreadjuiceplusminusfirsttwo
	 */
	public String getSpreadjuiceplusminusfirsttwo() {
		return spreadjuiceplusminusfirsttwo;
	}

	/**
	 * @param spreadjuiceplusminusfirsttwo the spreadjuiceplusminusfirsttwo to set
	 */
	public void setSpreadjuiceplusminusfirsttwo(String spreadjuiceplusminusfirsttwo) {
		this.spreadjuiceplusminusfirsttwo = spreadjuiceplusminusfirsttwo;
	}

	/**
	 * @return the spreadinputsecondone
	 */
	public String getSpreadinputsecondone() {
		return spreadinputsecondone;
	}

	/**
	 * @param spreadinputsecondone the spreadinputsecondone to set
	 */
	public void setSpreadinputsecondone(String spreadinputsecondone) {
		this.spreadinputsecondone = spreadinputsecondone;
	}

	/**
	 * @return the spreadplusminussecondone
	 */
	public String getSpreadplusminussecondone() {
		return spreadplusminussecondone;
	}

	/**
	 * @param spreadplusminussecondone the spreadplusminussecondone to set
	 */
	public void setSpreadplusminussecondone(String spreadplusminussecondone) {
		this.spreadplusminussecondone = spreadplusminussecondone;
	}

	/**
	 * @return the spreadinputjuicesecondone
	 */
	public String getSpreadinputjuicesecondone() {
		return spreadinputjuicesecondone;
	}

	/**
	 * @param spreadinputjuicesecondone the spreadinputjuicesecondone to set
	 */
	public void setSpreadinputjuicesecondone(String spreadinputjuicesecondone) {
		this.spreadinputjuicesecondone = spreadinputjuicesecondone;
	}

	/**
	 * @return the spreadjuiceplusminussecondone
	 */
	public String getSpreadjuiceplusminussecondone() {
		return spreadjuiceplusminussecondone;
	}

	/**
	 * @param spreadjuiceplusminussecondone the spreadjuiceplusminussecondone to set
	 */
	public void setSpreadjuiceplusminussecondone(String spreadjuiceplusminussecondone) {
		this.spreadjuiceplusminussecondone = spreadjuiceplusminussecondone;
	}

	/**
	 * @return the spreadinputsecondtwo
	 */
	public String getSpreadinputsecondtwo() {
		return spreadinputsecondtwo;
	}

	/**
	 * @param spreadinputsecondtwo the spreadinputsecondtwo to set
	 */
	public void setSpreadinputsecondtwo(String spreadinputsecondtwo) {
		this.spreadinputsecondtwo = spreadinputsecondtwo;
	}

	/**
	 * @return the spreadplusminussecondtwo
	 */
	public String getSpreadplusminussecondtwo() {
		return spreadplusminussecondtwo;
	}

	/**
	 * @param spreadplusminussecondtwo the spreadplusminussecondtwo to set
	 */
	public void setSpreadplusminussecondtwo(String spreadplusminussecondtwo) {
		this.spreadplusminussecondtwo = spreadplusminussecondtwo;
	}

	/**
	 * @return the spreadinputjuicesecondtwo
	 */
	public String getSpreadinputjuicesecondtwo() {
		return spreadinputjuicesecondtwo;
	}

	/**
	 * @param spreadinputjuicesecondtwo the spreadinputjuicesecondtwo to set
	 */
	public void setSpreadinputjuicesecondtwo(String spreadinputjuicesecondtwo) {
		this.spreadinputjuicesecondtwo = spreadinputjuicesecondtwo;
	}

	/**
	 * @return the spreadjuiceplusminussecondtwo
	 */
	public String getSpreadjuiceplusminussecondtwo() {
		return spreadjuiceplusminussecondtwo;
	}

	/**
	 * @param spreadjuiceplusminussecondtwo the spreadjuiceplusminussecondtwo to set
	 */
	public void setSpreadjuiceplusminussecondtwo(String spreadjuiceplusminussecondtwo) {
		this.spreadjuiceplusminussecondtwo = spreadjuiceplusminussecondtwo;
	}

	/**
	 * @return the totalinputfirstone
	 */
	public String getTotalinputfirstone() {
		return totalinputfirstone;
	}

	/**
	 * @param totalinputfirstone the totalinputfirstone to set
	 */
	public void setTotalinputfirstone(String totalinputfirstone) {
		this.totalinputfirstone = totalinputfirstone;
	}

	/**
	 * @return the totaljuiceplusminusfirstone
	 */
	public String getTotaljuiceplusminusfirstone() {
		return totaljuiceplusminusfirstone;
	}

	/**
	 * @param totaljuiceplusminusfirstone the totaljuiceplusminusfirstone to set
	 */
	public void setTotaljuiceplusminusfirstone(String totaljuiceplusminusfirstone) {
		this.totaljuiceplusminusfirstone = totaljuiceplusminusfirstone;
	}

	/**
	 * @return the totalinputjuicefirstone
	 */
	public String getTotalinputjuicefirstone() {
		return totalinputjuicefirstone;
	}

	/**
	 * @param totalinputjuicefirstone the totalinputjuicefirstone to set
	 */
	public void setTotalinputjuicefirstone(String totalinputjuicefirstone) {
		this.totalinputjuicefirstone = totalinputjuicefirstone;
	}

	/**
	 * @return the totalinputfirsttwo
	 */
	public String getTotalinputfirsttwo() {
		return totalinputfirsttwo;
	}

	/**
	 * @param totalinputfirsttwo the totalinputfirsttwo to set
	 */
	public void setTotalinputfirsttwo(String totalinputfirsttwo) {
		this.totalinputfirsttwo = totalinputfirsttwo;
	}

	/**
	 * @return the totaljuiceplusminusfirsttwo
	 */
	public String getTotaljuiceplusminusfirsttwo() {
		return totaljuiceplusminusfirsttwo;
	}

	/**
	 * @param totaljuiceplusminusfirsttwo the totaljuiceplusminusfirsttwo to set
	 */
	public void setTotaljuiceplusminusfirsttwo(String totaljuiceplusminusfirsttwo) {
		this.totaljuiceplusminusfirsttwo = totaljuiceplusminusfirsttwo;
	}

	/**
	 * @return the totalinputjuicefirsttwo
	 */
	public String getTotalinputjuicefirsttwo() {
		return totalinputjuicefirsttwo;
	}

	/**
	 * @param totalinputjuicefirsttwo the totalinputjuicefirsttwo to set
	 */
	public void setTotalinputjuicefirsttwo(String totalinputjuicefirsttwo) {
		this.totalinputjuicefirsttwo = totalinputjuicefirsttwo;
	}

	/**
	 * @return the totalinputsecondone
	 */
	public String getTotalinputsecondone() {
		return totalinputsecondone;
	}

	/**
	 * @param totalinputsecondone the totalinputsecondone to set
	 */
	public void setTotalinputsecondone(String totalinputsecondone) {
		this.totalinputsecondone = totalinputsecondone;
	}

	/**
	 * @return the totaljuiceplusminussecondone
	 */
	public String getTotaljuiceplusminussecondone() {
		return totaljuiceplusminussecondone;
	}

	/**
	 * @param totaljuiceplusminussecondone the totaljuiceplusminussecondone to set
	 */
	public void setTotaljuiceplusminussecondone(String totaljuiceplusminussecondone) {
		this.totaljuiceplusminussecondone = totaljuiceplusminussecondone;
	}

	/**
	 * @return the totalinputjuicesecondone
	 */
	public String getTotalinputjuicesecondone() {
		return totalinputjuicesecondone;
	}

	/**
	 * @param totalinputjuicesecondone the totalinputjuicesecondone to set
	 */
	public void setTotalinputjuicesecondone(String totalinputjuicesecondone) {
		this.totalinputjuicesecondone = totalinputjuicesecondone;
	}

	/**
	 * @return the totalinputsecondtwo
	 */
	public String getTotalinputsecondtwo() {
		return totalinputsecondtwo;
	}

	/**
	 * @param totalinputsecondtwo the totalinputsecondtwo to set
	 */
	public void setTotalinputsecondtwo(String totalinputsecondtwo) {
		this.totalinputsecondtwo = totalinputsecondtwo;
	}

	/**
	 * @return the totaljuiceplusminussecondtwo
	 */
	public String getTotaljuiceplusminussecondtwo() {
		return totaljuiceplusminussecondtwo;
	}

	/**
	 * @param totaljuiceplusminussecondtwo the totaljuiceplusminussecondtwo to set
	 */
	public void setTotaljuiceplusminussecondtwo(String totaljuiceplusminussecondtwo) {
		this.totaljuiceplusminussecondtwo = totaljuiceplusminussecondtwo;
	}

	/**
	 * @return the totalinputjuicesecondtwo
	 */
	public String getTotalinputjuicesecondtwo() {
		return totalinputjuicesecondtwo;
	}

	/**
	 * @param totalinputjuicesecondtwo the totalinputjuicesecondtwo to set
	 */
	public void setTotalinputjuicesecondtwo(String totalinputjuicesecondtwo) {
		this.totalinputjuicesecondtwo = totalinputjuicesecondtwo;
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

	/**
	 * @return the riskAmount
	 */
	public String getRiskAmount() {
		return riskAmount;
	}

	/**
	 * @param riskAmount the riskAmount to set
	 */
	public void setRiskAmount(String riskAmount) {
		this.riskAmount = riskAmount;
	}

	/**
	 * @return the toWinAmount
	 */
	public String getToWinAmount() {
		return toWinAmount;
	}

	/**
	 * @param toWinAmount the toWinAmount to set
	 */
	public void setToWinAmount(String toWinAmount) {
		this.toWinAmount = toWinAmount;
	}

	/**
	 * @return the accountevents
	 */
	public Set<AccountEvent> getAccountevents() {
		return accountevents;
	}

	/**
	 * @param accountevents the accountevents to set
	 */
	public void setAccountevents(Set<AccountEvent> accountevents) {
		this.accountevents = accountevents;
	}

	/**
	 * 
	 * @param accountevent
	 */
	public void addAccountevent(AccountEvent accountevent) {
		this.accountevents.add(accountevent);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(CommittedEvent o) {
	    if (getDatecreated() == null || o.getDatecreated() == null)
	        return 0;
	    return getDatecreated().compareTo(o.getDatecreated());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommittedEvent [spreadinputfirstone=" + spreadinputfirstone + ", spreadplusminusfirstone="
				+ spreadplusminusfirstone + ", spreadinputjuicefirstone=" + spreadinputjuicefirstone
				+ ", spreadjuiceplusminusfirstone=" + spreadjuiceplusminusfirstone + ", spreadinputfirsttwo="
				+ spreadinputfirsttwo + ", spreadplusminusfirsttwo=" + spreadplusminusfirsttwo
				+ ", spreadinputjuicefirsttwo=" + spreadinputjuicefirsttwo + ", spreadjuiceplusminusfirsttwo="
				+ spreadjuiceplusminusfirsttwo + ", spreadinputsecondone=" + spreadinputsecondone
				+ ", spreadplusminussecondone=" + spreadplusminussecondone + ", spreadinputjuicesecondone="
				+ spreadinputjuicesecondone + ", spreadjuiceplusminussecondone=" + spreadjuiceplusminussecondone
				+ ", spreadinputsecondtwo=" + spreadinputsecondtwo + ", spreadplusminussecondtwo="
				+ spreadplusminussecondtwo + ", spreadinputjuicesecondtwo=" + spreadinputjuicesecondtwo
				+ ", spreadjuiceplusminussecondtwo=" + spreadjuiceplusminussecondtwo + ", totalinputfirstone="
				+ totalinputfirstone + ", totaljuiceplusminusfirstone=" + totaljuiceplusminusfirstone
				+ ", totalinputjuicefirstone=" + totalinputjuicefirstone + ", totalinputfirsttwo=" + totalinputfirsttwo
				+ ", totaljuiceplusminusfirsttwo=" + totaljuiceplusminusfirsttwo + ", totalinputjuicefirsttwo="
				+ totalinputjuicefirsttwo + ", totalinputsecondone=" + totalinputsecondone
				+ ", totaljuiceplusminussecondone=" + totaljuiceplusminussecondone + ", totalinputjuicesecondone="
				+ totalinputjuicesecondone + ", totalinputsecondtwo=" + totalinputsecondtwo
				+ ", totaljuiceplusminussecondtwo=" + totaljuiceplusminussecondtwo + ", totalinputjuicesecondtwo="
				+ totalinputjuicesecondtwo + ", mlinputfirstone=" + mlinputfirstone + ", mlplusminusfirstone="
				+ mlplusminusfirstone + ", mlinputfirsttwo=" + mlinputfirsttwo + ", mlplusminusfirsttwo="
				+ mlplusminusfirsttwo + ", mlinputsecondone=" + mlinputsecondone + ", mlplusminussecondone="
				+ mlplusminussecondone + ", mlinputsecondtwo=" + mlinputsecondtwo + ", mlplusminussecondtwo="
				+ mlplusminussecondtwo + ", riskAmount=" + riskAmount + ", toWinAmount=" + toWinAmount
				+ ", accountevents=" + accountevents + "] " + super.toString();
	}
}
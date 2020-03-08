/**
 * 
 */
package com.ticketadvantage.services.dao.sites.stub;

import java.io.Serializable;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ticketadvantage.services.dao.sites.SiteTeamPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class StubTeamPackage extends SiteTeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private String gameSpreadInputId;
	
	@XmlElement
	private String gameSpreadInputName;
	
	@XmlElement
	private String gameSpreadSelectId;
	
	@XmlElement
	private String gameSpreadSelectName;
	
	@XmlElement
	private LinkedHashMap<String, String> gameSpreadOptionValue = new LinkedHashMap<String, String>();
	
	@XmlElement
	private String gameMLInputId;
	
	@XmlElement
	private String gameMLInputName;
	
	@XmlElement
	private LinkedHashMap<String, String> gameMLInputValue = new LinkedHashMap<String, String>();
	
	@XmlElement
	private String gameTotalInputId;
	
	@XmlElement
	private String gameTotalInputName;
	
	@XmlElement
	private String gameTotalSelectId;
	
	@XmlElement
	private String gameTotalSelectName;
	
	@XmlElement
	private LinkedHashMap<String, String> gameTotalOptionValue = new LinkedHashMap<String, String>();

	/**
	 * 
	 */
	public StubTeamPackage() {
		super();
	}

	/**
	 * @return the gameSpreadInputId
	 */
	public String getGameSpreadInputId() {
		return gameSpreadInputId;
	}

	/**
	 * @param gameSpreadInputId the gameSpreadInputId to set
	 */
	public void setGameSpreadInputId(String gameSpreadInputId) {
		this.gameSpreadInputId = gameSpreadInputId;
	}

	/**
	 * @return the gameSpreadInputName
	 */
	public String getGameSpreadInputName() {
		return gameSpreadInputName;
	}

	/**
	 * @param gameSpreadInputName the gameSpreadInputName to set
	 */
	public void setGameSpreadInputName(String gameSpreadInputName) {
		this.gameSpreadInputName = gameSpreadInputName;
	}

	/**
	 * @return the gameSpreadSelectId
	 */
	public String getGameSpreadSelectId() {
		return gameSpreadSelectId;
	}

	/**
	 * @param gameSpreadSelectId the gameSpreadSelectId to set
	 */
	public void setGameSpreadSelectId(String gameSpreadSelectId) {
		this.gameSpreadSelectId = gameSpreadSelectId;
	}

	/**
	 * @return the gameSpreadSelectName
	 */
	public String getGameSpreadSelectName() {
		return gameSpreadSelectName;
	}

	/**
	 * @param gameSpreadSelectName the gameSpreadSelectName to set
	 */
	public void setGameSpreadSelectName(String gameSpreadSelectName) {
		this.gameSpreadSelectName = gameSpreadSelectName;
	}

	/**
	 * @return the gameSpreadOptionValue
	 */
	public LinkedHashMap<String, String> getGameSpreadOptionValue() {
		return gameSpreadOptionValue;
	}

	/**
	 * @param gameSpreadOptionValue the gameSpreadOptionValue to set
	 */
	public void setGameSpreadOptionValue(LinkedHashMap<String, String> gameSpreadOptionValue) {
		this.gameSpreadOptionValue = gameSpreadOptionValue;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameSpreadOptionValue(String key, String value) {
		this.gameSpreadOptionValue.put(key, value);
	}

	/**
	 * @return the gameMLInputId
	 */
	public String getGameMLInputId() {
		return gameMLInputId;
	}

	/**
	 * @param gameMLInputId the gameMLInputId to set
	 */
	public void setGameMLInputId(String gameMLInputId) {
		this.gameMLInputId = gameMLInputId;
	}

	/**
	 * @return the gameMLInputName
	 */
	public String getGameMLInputName() {
		return gameMLInputName;
	}

	/**
	 * @param gameMLInputName the gameMLInputName to set
	 */
	public void setGameMLInputName(String gameMLInputName) {
		this.gameMLInputName = gameMLInputName;
	}

	/**
	 * @return the gameMLInputValue
	 */
	public LinkedHashMap<String, String> getGameMLInputValue() {
		return gameMLInputValue;
	}

	/**
	 * @param gameMLInputValue the gameMLInputValue to set
	 */
	public void setGameMLInputValue(LinkedHashMap<String, String> gameMLInputValue) {
		this.gameMLInputValue = gameMLInputValue;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameMLInputValue(String key, String value) {
		this.gameMLInputValue.put(key, value);
	}


	/**
	 * @return the gameTotalInputId
	 */
	public String getGameTotalInputId() {
		return gameTotalInputId;
	}

	/**
	 * @param gameTotalInputId the gameTotalInputId to set
	 */
	public void setGameTotalInputId(String gameTotalInputId) {
		this.gameTotalInputId = gameTotalInputId;
	}

	/**
	 * @return the gameTotalInputName
	 */
	public String getGameTotalInputName() {
		return gameTotalInputName;
	}

	/**
	 * @param gameTotalInputName the gameTotalInputName to set
	 */
	public void setGameTotalInputName(String gameTotalInputName) {
		this.gameTotalInputName = gameTotalInputName;
	}

	/**
	 * @return the gameTotalOptionValue
	 */
	public LinkedHashMap<String, String> getGameTotalOptionValue() {
		return gameTotalOptionValue;
	}

	/**
	 * @param gameTotalOptionValue the gameTotalOptionValue to set
	 */
	public void setGameTotalOptionValue(LinkedHashMap<String, String> gameTotalOptionValue) {
		this.gameTotalOptionValue = gameTotalOptionValue;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameTotalOptionValue(String key, String value) {
		this.gameTotalOptionValue.put(key, value);
	}

	/**
	 * @return the gameTotalSelectId
	 */
	public String getGameTotalSelectId() {
		return gameTotalSelectId;
	}

	/**
	 * @param gameTotalSelectId the gameTotalSelectId to set
	 */
	public void setGameTotalSelectId(String gameTotalSelectId) {
		this.gameTotalSelectId = gameTotalSelectId;
	}

	/**
	 * @return the gameTotalSelectName
	 */
	public String getGameTotalSelectName() {
		return gameTotalSelectName;
	}

	/**
	 * @param gameTotalSelectName the gameTotalSelectName to set
	 */
	public void setGameTotalSelectName(String gameTotalSelectName) {
		this.gameTotalSelectName = gameTotalSelectName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StubTeamPackage [gameSpreadInputId=" + gameSpreadInputId + ", gameSpreadInputName="
				+ gameSpreadInputName + ", gameSpreadSelectId=" + gameSpreadSelectId + ", gameSpreadSelectName="
				+ gameSpreadSelectName + ", gameSpreadOptionValue=" + gameSpreadOptionValue + ", gameMLInputId="
				+ gameMLInputId + ", gameMLInputName=" + gameMLInputName + ", gameMLInputValue=" + gameMLInputValue
				+ ", gameTotalInputId=" + gameTotalInputId + ", gameTotalInputName=" + gameTotalInputName
				+ ", gameTotalSelectId=" + gameTotalSelectId + ", gameTotalSelectName=" + gameTotalSelectName
				+ ", gameTotalOptionValue=" + gameTotalOptionValue + "]" + super.toString();
	}
}
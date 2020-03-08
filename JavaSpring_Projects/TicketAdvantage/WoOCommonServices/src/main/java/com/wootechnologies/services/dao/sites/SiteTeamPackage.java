/**
 * 
 */
package com.wootechnologies.services.dao.sites;

import java.io.Serializable;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wootechnologies.model.TeamPackage;

/**
 * @author jmiller
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class SiteTeamPackage extends TeamPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement
	private LinkedHashMap<String, String> gameSpreadOptionIndicator = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameSpreadOptionNumber = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameSpreadOptionJuiceIndicator = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameSpreadOptionJuiceNumber = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameMLOptionJuiceIndicator = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameMLOptionJuiceNumber = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTotalOptionIndicator = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTotalOptionNumber = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTotalOptionJuiceIndicator = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTotalOptionJuiceNumber = new LinkedHashMap<String, String>();

	@XmlElement
	private String gameSpreadInputId;
	
	@XmlElement
	private String gameSpreadInputName;

	@XmlElement
	private String gameSpreadInputValue;

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
	private String gameMLSelectId;
	
	@XmlElement
	private String gameMLSelectName;

	@XmlElement
	private LinkedHashMap<String, String> gameMLOptionValue = new LinkedHashMap<String, String>();

	@XmlElement
	private String gameTotalInputId;
	
	@XmlElement
	private String gameTotalInputName;
	
	@XmlElement
	private String gameTotalInputValue;

	@XmlElement
	private String gameTotalSelectId;
	
	@XmlElement
	private String gameTotalSelectName;
	
	@XmlElement
	private LinkedHashMap<String, String> gameTotalOptionValue = new LinkedHashMap<String, String>();

	// newly added fields on 31-10-18
	@XmlElement
	private String gameTeamTotalInputId;

	@XmlElement
	private String gameTeamTotalInputName;

	@XmlElement
	private String gameTeamTotalInputValue;

	@XmlElement
	private String gameTeamTotalSelectId;

	@XmlElement
	private String gameTeamTotalSelectName;

	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionValue = new LinkedHashMap<String, String>();

	// newly added fields on 31-10-18
	@XmlElement
	private String gameTeamTotalInputIdSecond;

	@XmlElement
	private String gameTeamTotalInputNameSecond;

	@XmlElement
	private String gameTeamTotalInputValueSecond;

	@XmlElement
	private String gameTeamTotalSelectIdSecond;

	@XmlElement
	private String gameTeamTotalSelectNameSecond;

	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionValueSecond = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionIndicator = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionNumber = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionJuiceIndicator = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionJuiceNumber = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionIndicatorSecond = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionNumberSecond = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionJuiceIndicatorSecond = new LinkedHashMap<String, String>();
	
	@XmlElement
	private LinkedHashMap<String, String> gameTeamTotalOptionJuiceNumberSecond = new LinkedHashMap<String, String>();
	
	/**
	 * 
	 */
	public SiteTeamPackage() {
		super();
	}

	/**
	 * @return the gameSpreadOptionIndicator
	 */
	public LinkedHashMap<String, String> getGameSpreadOptionIndicator() {
		return gameSpreadOptionIndicator;
	}

	/**
	 * @param gameSpreadOptionIndicator the gameSpreadOptionIndicator to set
	 */
	public void setGameSpreadOptionIndicator(LinkedHashMap<String, String> gameSpreadOptionIndicator) {
		this.gameSpreadOptionIndicator = gameSpreadOptionIndicator;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameSpreadOptionIndicator(String key, String value) {
		gameSpreadOptionIndicator.put(key, value);
	}

	/**
	 * @return the gameSpreadOptionNumber
	 */
	public LinkedHashMap<String, String> getGameSpreadOptionNumber() {
		return gameSpreadOptionNumber;
	}

	/**
	 * @param gameSpreadOptionNumber the gameSpreadOptionNumber to set
	 */
	public void setGameSpreadOptionNumber(LinkedHashMap<String, String> gameSpreadOptionNumber) {
		this.gameSpreadOptionNumber = gameSpreadOptionNumber;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameSpreadOptionNumber(String key, String value) {
		gameSpreadOptionNumber.put(key, value);
	}

	/**
	 * @return the gameSpreadOptionJuiceIndicator
	 */
	public LinkedHashMap<String, String> getGameSpreadOptionJuiceIndicator() {
		return gameSpreadOptionJuiceIndicator;
	}

	/**
	 * @param gameSpreadOptionJuiceIndicator the gameSpreadOptionJuiceIndicator to set
	 */
	public void setGameSpreadOptionJuiceIndicator(LinkedHashMap<String, String> gameSpreadOptionJuiceIndicator) {
		this.gameSpreadOptionJuiceIndicator = gameSpreadOptionJuiceIndicator;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameSpreadOptionJuiceIndicator(String key, String value) {
		gameSpreadOptionJuiceIndicator.put(key, value);
	}

	/**
	 * @return the gameSpreadOptionJuiceNumber
	 */
	public LinkedHashMap<String, String> getGameSpreadOptionJuiceNumber() {
		return gameSpreadOptionJuiceNumber;
	}

	/**
	 * @param gameSpreadOptionJuiceNumber the gameSpreadOptionJuiceNumber to set
	 */
	public void setGameSpreadOptionJuiceNumber(LinkedHashMap<String, String> gameSpreadOptionJuiceNumber) {
		this.gameSpreadOptionJuiceNumber = gameSpreadOptionJuiceNumber;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameSpreadOptionJuiceNumber(String key, String value) {
		gameSpreadOptionJuiceNumber.put(key, value);
	}

	/**
	 * @return the gameMLOptionJuiceIndicator
	 */
	public LinkedHashMap<String, String> getGameMLOptionJuiceIndicator() {
		return gameMLOptionJuiceIndicator;
	}

	/**
	 * @param gameMLOptionJuiceIndicator the gameMLOptionJuiceIndicator to set
	 */
	public void setGameMLOptionJuiceIndicator(LinkedHashMap<String, String> gameMLOptionJuiceIndicator) {
		this.gameMLOptionJuiceIndicator = gameMLOptionJuiceIndicator;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameMLOptionJuiceIndicator(String key, String value) {
		gameMLOptionJuiceIndicator.put(key, value);
	}

	/**
	 * @return the gameMLOptionJuiceNumber
	 */
	public LinkedHashMap<String, String> getGameMLOptionJuiceNumber() {
		return gameMLOptionJuiceNumber;
	}

	/**
	 * @param gameMLOptionJuiceNumber the gameMLOptionJuiceNumber to set
	 */
	public void setGameMLOptionJuiceNumber(LinkedHashMap<String, String> gameMLOptionJuiceNumber) {
		this.gameMLOptionJuiceNumber = gameMLOptionJuiceNumber;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameMLOptionJuiceNumber(String key, String value) {
		gameMLOptionJuiceNumber.put(key, value);
	}

	/**
	 * @return the gameTotalOptionIndicator
	 */
	public LinkedHashMap<String, String> getGameTotalOptionIndicator() {
		return gameTotalOptionIndicator;
	}

	/**
	 * @param gameTotalOptionIndicator the gameTotalOptionIndicator to set
	 */
	public void setGameTotalOptionIndicator(LinkedHashMap<String, String> gameTotalOptionIndicator) {
		this.gameTotalOptionIndicator = gameTotalOptionIndicator;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameTotalOptionIndicator(String key, String value) {
		gameTotalOptionIndicator.put(key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setGameTotalOptionIndicator(String key, String value) {
		gameTotalOptionIndicator.put(key, value);
	}

	/**
	 * @return the gameTotalOptionNumber
	 */
	public LinkedHashMap<String, String> getGameTotalOptionNumber() {
		return gameTotalOptionNumber;
	}

	/**
	 * @param gameTotalOptionNumber the gameTotalOptionNumber to set
	 */
	public void setGameTotalOptionNumber(LinkedHashMap<String, String> gameTotalOptionNumber) {
		this.gameTotalOptionNumber = gameTotalOptionNumber;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameTotalOptionNumber(String key, String value) {
		gameTotalOptionNumber.put(key, value);
	}

	/**
	 * @return the gameTotalOptionJuiceIndicator
	 */
	public LinkedHashMap<String, String> getGameTotalOptionJuiceIndicator() {
		return gameTotalOptionJuiceIndicator;
	}

	/**
	 * @param gameTotalOptionJuiceIndicator the gameTotalOptionJuiceIndicator to set
	 */
	public void setGameTotalOptionJuiceIndicator(LinkedHashMap<String, String> gameTotalOptionJuiceIndicator) {
		this.gameTotalOptionJuiceIndicator = gameTotalOptionJuiceIndicator;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameTotalOptionJuiceIndicator(String key, String value) {
		gameTotalOptionJuiceIndicator.put(key, value);
	}

	/**
	 * @return the gameTotalOptionJuiceNumber
	 */
	public LinkedHashMap<String, String> getGameTotalOptionJuiceNumber() {
		return gameTotalOptionJuiceNumber;
	}

	/**
	 * @param gameTotalOptionJuiceNumber the gameTotalOptionJuiceNumber to set
	 */
	public void setGameTotalOptionJuiceNumber(LinkedHashMap<String, String> gameTotalOptionJuiceNumber) {
		this.gameTotalOptionJuiceNumber = gameTotalOptionJuiceNumber;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameTotalOptionJuiceNumber(String key, String value) {
		gameTotalOptionJuiceNumber.put(key, value);
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
	 * @return the gameSpreadInputValue
	 */
	public String getGameSpreadInputValue() {
		return gameSpreadInputValue;
	}

	/**
	 * @param gameSpreadInputValue the gameSpreadInputValue to set
	 */
	public void setGameSpreadInputValue(String gameSpreadInputValue) {
		this.gameSpreadInputValue = gameSpreadInputValue;
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
		gameSpreadOptionValue.put(key, value);
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
		gameMLInputValue.put(key, value);
	}

	/**
	 * @return the gameMLSelectId
	 */
	public String getGameMLSelectId() {
		return gameMLSelectId;
	}

	/**
	 * @param gameMLSelectId the gameMLSelectId to set
	 */
	public void setGameMLSelectId(String gameMLSelectId) {
		this.gameMLSelectId = gameMLSelectId;
	}

	/**
	 * @return the gameMLSelectName
	 */
	public String getGameMLSelectName() {
		return gameMLSelectName;
	}

	/**
	 * @param gameMLSelectName the gameMLSelectName to set
	 */
	public void setGameMLSelectName(String gameMLSelectName) {
		this.gameMLSelectName = gameMLSelectName;
	}

	/**
	 * @return the gameMLOptionValue
	 */
	public LinkedHashMap<String, String> getGameMLOptionValue() {
		return gameMLOptionValue;
	}

	/**
	 * @param gameMLOptionValue the gameMLOptionValue to set
	 */
	public void setGameMLOptionValue(LinkedHashMap<String, String> gameMLOptionValue) {
		this.gameMLOptionValue = gameMLOptionValue;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addGameMLOptionValue(String key, String value) {
		gameMLOptionValue.put(key, value);
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
	 * @return the gameTotalInputValue
	 */
	public String getGameTotalInputValue() {
		return gameTotalInputValue;
	}

	/**
	 * @param gameTotalInputValue the gameTotalInputValue to set
	 */
	public void setGameTotalInputValue(String gameTotalInputValue) {
		this.gameTotalInputValue = gameTotalInputValue;
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
		gameTotalOptionValue.put(key, value);
	}

	
	// newly added getters and settrs on 17-10-18
		public String getGameTeamTotalInputId() {
			return gameTeamTotalInputId;
		}

		public void setGameTeamTotalInputId(String gameTeamTotalInputId) {
			this.gameTeamTotalInputId = gameTeamTotalInputId;
		}

		public String getGameTeamTotalInputName() {
			return gameTeamTotalInputName;
		}

		public void setGameTeamTotalInputName(String gameTeamTotalInputName) {
			this.gameTeamTotalInputName = gameTeamTotalInputName;
		}

		public String getGameTeamTotalInputValue() {
			return gameTeamTotalInputValue;
		}

		public void setGameTeamTotalInputValue(String gameTeamTotalInputValue) {
			this.gameTeamTotalInputValue = gameTeamTotalInputValue;
		}

		public String getGameTeamTotalSelectId() {
			return gameTeamTotalSelectId;
		}

		public void setGameTeamTotalSelectId(String gameTeamTotalSelectId) {
			this.gameTeamTotalSelectId = gameTeamTotalSelectId;
		}

		public String getGameTeamTotalSelectName() {
			return gameTeamTotalSelectName;
		}

		public void setGameTeamTotalSelectName(String gameTeamTotalSelectName) {
			this.gameTeamTotalSelectName = gameTeamTotalSelectName;
		}

		public LinkedHashMap<String, String> getGameTeamTotalOptionValue() {
			return gameTeamTotalOptionValue;
		}

		public void setGameTeamTotalOptionValue(LinkedHashMap<String, String> gameTeamTotalOptionValue) {
			this.gameTeamTotalOptionValue = gameTeamTotalOptionValue;
		}

		public void addGameTeamTotalOptionValue(String key, String value) {
			gameTeamTotalOptionValue.put(key, value);
		}
		
		public void addGameTeamTotalOptionValueSecond(String key, String value) {
			gameTeamTotalOptionValueSecond.put(key, value);
		}
		
		public LinkedHashMap<String, String> getGameTeamTotalOptionNumber() {
			return gameTeamTotalOptionNumber;
		}

		public void setGameTeamTotalOptionNumber(LinkedHashMap<String, String> gameTeamTotalOptionNumber) {
			this.gameTeamTotalOptionNumber = gameTeamTotalOptionNumber;
		}

		public LinkedHashMap<String, String> getGameTeamTotalOptionJuiceNumber() {
			return gameTeamTotalOptionJuiceNumber;
		}

		public void setGameTeamTotalOptionJuiceNumber(LinkedHashMap<String, String> gameTeamTotalOptionJuiceNumber) {
			this.gameTeamTotalOptionJuiceNumber = gameTeamTotalOptionJuiceNumber;
		}

		public String getGameTeamTotalInputIdSecond() {
			return gameTeamTotalInputIdSecond;
		}

		public void setGameTeamTotalInputIdSecond(String gameTeamTotalInputIdSecond) {
			this.gameTeamTotalInputIdSecond = gameTeamTotalInputIdSecond;
		}

		public String getGameTeamTotalInputNameSecond() {
			return gameTeamTotalInputNameSecond;
		}

		public void setGameTeamTotalInputNameSecond(String gameTeamTotalInputNameSecond) {
			this.gameTeamTotalInputNameSecond = gameTeamTotalInputNameSecond;
		}

		public String getGameTeamTotalInputValueSecond() {
			return gameTeamTotalInputValueSecond;
		}

		public void setGameTeamTotalInputValueSecond(String gameTeamTotalInputValueSecond) {
			this.gameTeamTotalInputValueSecond = gameTeamTotalInputValueSecond;
		}

		public String getGameTeamTotalSelectIdSecond() {
			return gameTeamTotalSelectIdSecond;
		}

		public void setGameTeamTotalSelectIdSecond(String gameTeamTotalSelectIdSecond) {
			this.gameTeamTotalSelectIdSecond = gameTeamTotalSelectIdSecond;
		}

		public String getGameTeamTotalSelectNameSecond() {
			return gameTeamTotalSelectNameSecond;
		}

		public void setGameTeamTotalSelectNameSecond(String gameTeamTotalSelectNameSecond) {
			this.gameTeamTotalSelectNameSecond = gameTeamTotalSelectNameSecond;
		}

		public LinkedHashMap<String, String> getGameTeamTotalOptionValueSecond() {
			return gameTeamTotalOptionValueSecond;
		}

		public void setGameTeamTotalOptionValueSecond(LinkedHashMap<String, String> gameTeamTotalOptionValueSecond) {
			this.gameTeamTotalOptionValueSecond = gameTeamTotalOptionValueSecond;
		}

		public LinkedHashMap<String, String> getGameTeamTotalOptionIndicator() {
			return gameTeamTotalOptionIndicator;
		}

		public void setGameTeamTotalOptionIndicator(LinkedHashMap<String, String> gameTeamTotalOptionIndicator) {
			this.gameTeamTotalOptionIndicator = gameTeamTotalOptionIndicator;
		}

		public LinkedHashMap<String, String> getGameTeamTotalOptionJuiceIndicator() {
			return gameTeamTotalOptionJuiceIndicator;
		}

		public void setGameTeamTotalOptionJuiceIndicator(LinkedHashMap<String, String> gameTeamTotalOptionJuiceIndicator) {
			this.gameTeamTotalOptionJuiceIndicator = gameTeamTotalOptionJuiceIndicator;
		}

		public void addGameTeamTotalOptionIndicator(String key, String value) {
			gameTeamTotalOptionIndicator.put(key, value);
		}
		
		public void addGameTeamTotalOptionNumber(String key, String value) {
			gameTeamTotalOptionNumber.put(key, value);
		}
		
		public void addGameTeamTotalOptionJuiceIndicator(String key, String value) {
			gameTeamTotalOptionJuiceIndicator.put(key, value);
		}
		
		public void addGameTeamTotalOptionJuiceNumber(String key, String value) {
			gameTeamTotalOptionJuiceNumber.put(key, value);
		}
		
		
		public void addGameTeamTotalOptionIndicatorSecond(String key, String value) {
			gameTeamTotalOptionIndicatorSecond.put(key, value);
		}
		
		public void addGameTeamTotalOptionNumberSecond(String key, String value) {
			gameTeamTotalOptionNumberSecond.put(key, value);
		}
		
		public void addGameTeamTotalOptionJuiceIndicatorSecond(String key, String value) {
			gameTeamTotalOptionJuiceIndicatorSecond.put(key, value);
		}
		
		public void addGameTeamTotalOptionJuiceNumberSecond(String key, String value) {
			gameTeamTotalOptionJuiceNumberSecond.put(key, value);
		}
		

	public LinkedHashMap<String, String> getGameTeamTotalOptionIndicatorSecond() {
			return gameTeamTotalOptionIndicatorSecond;
		}

		public void setGameTeamTotalOptionIndicatorSecond(LinkedHashMap<String, String> gameTeamTotalOptionIndicatorSecond) {
			this.gameTeamTotalOptionIndicatorSecond = gameTeamTotalOptionIndicatorSecond;
		}

		public LinkedHashMap<String, String> getGameTeamTotalOptionNumberSecond() {
			return gameTeamTotalOptionNumberSecond;
		}

		public void setGameTeamTotalOptionNumberSecond(LinkedHashMap<String, String> gameTeamTotalOptionNumberSecond) {
			this.gameTeamTotalOptionNumberSecond = gameTeamTotalOptionNumberSecond;
		}

		public LinkedHashMap<String, String> getGameTeamTotalOptionJuiceIndicatorSecond() {
			return gameTeamTotalOptionJuiceIndicatorSecond;
		}

		public void setGameTeamTotalOptionJuiceIndicatorSecond(
				LinkedHashMap<String, String> gameTeamTotalOptionJuiceIndicatorSecond) {
			this.gameTeamTotalOptionJuiceIndicatorSecond = gameTeamTotalOptionJuiceIndicatorSecond;
		}

		public LinkedHashMap<String, String> getGameTeamTotalOptionJuiceNumberSecond() {
			return gameTeamTotalOptionJuiceNumberSecond;
		}

		public void setGameTeamTotalOptionJuiceNumberSecond(
				LinkedHashMap<String, String> gameTeamTotalOptionJuiceNumberSecond) {
			this.gameTeamTotalOptionJuiceNumberSecond = gameTeamTotalOptionJuiceNumberSecond;
		}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SiteTeamPackage [gameSpreadOptionIndicator=" + gameSpreadOptionIndicator + ", gameSpreadOptionNumber="
				+ gameSpreadOptionNumber + ", gameSpreadOptionJuiceIndicator=" + gameSpreadOptionJuiceIndicator
				+ ", gameSpreadOptionJuiceNumber=" + gameSpreadOptionJuiceNumber + ", gameMLOptionJuiceIndicator="
				+ gameMLOptionJuiceIndicator + ", gameMLOptionJuiceNumber=" + gameMLOptionJuiceNumber
				+ ", gameTotalOptionIndicator=" + gameTotalOptionIndicator + ", gameTotalOptionNumber="
				+ gameTotalOptionNumber + ", gameTotalOptionJuiceIndicator=" + gameTotalOptionJuiceIndicator
				+ ", gameTotalOptionJuiceNumber=" + gameTotalOptionJuiceNumber + ", gameSpreadInputId="
				+ gameSpreadInputId + ", gameSpreadInputName=" + gameSpreadInputName + ", gameSpreadInputValue="
				+ gameSpreadInputValue + ", gameSpreadSelectId=" + gameSpreadSelectId + ", gameSpreadSelectName="
				+ gameSpreadSelectName + ", gameSpreadOptionValue=" + gameSpreadOptionValue + ", gameMLInputId="
				+ gameMLInputId + ", gameMLInputName=" + gameMLInputName + ", gameMLInputValue=" + gameMLInputValue
				+ ", gameMLSelectId=" + gameMLSelectId + ", gameMLSelectName=" + gameMLSelectName
				+ ", gameMLOptionValue=" + gameMLOptionValue + ", gameTotalInputId=" + gameTotalInputId
				+ ", gameTotalInputName=" + gameTotalInputName + ", gameTotalInputValue=" + gameTotalInputValue
				+ ", gameTotalSelectId=" + gameTotalSelectId + ", gameTotalSelectName=" + gameTotalSelectName
				+ ", gameTotalOptionValue=" + gameTotalOptionValue
				
				+ ", gameTeamTotalInputId=" + gameTeamTotalInputId + ", gameTeamTotalInputName="
				+ gameTeamTotalInputName + ", gameTeamTotalInputValue=" + gameTeamTotalInputValue
				+ ", gameTeamTotalSelectId=" + gameTeamTotalSelectId + ", gameTeamTotalSelectName="
				+ gameTeamTotalSelectName + ", gameTeamTotalOptionValue=" + gameTeamTotalOptionValue
				
				+ ", gameTeamTotalInputIdSecond=" + gameTeamTotalInputIdSecond + ", gameTeamTotalInputNameSecond="
				+ gameTeamTotalInputNameSecond + ", gameTeamTotalInputValueSecond=" + gameTeamTotalInputValueSecond
				+ ", gameTeamTotalSelectIdSecond=" + gameTeamTotalSelectIdSecond + ", gameTeamTotalSelectNameSecond="
				+ gameTeamTotalSelectNameSecond + ", gameTeamTotalOptionValueSecond=" + gameTeamTotalOptionValueSecond
				
				+ ", gameTeamTotalOptionIndicator=" + gameTeamTotalOptionIndicator + ", gameTeamTotalOptionNumber="
				+ gameTeamTotalOptionNumber + ", gameTeamTotalOptionJuiceIndicator=" + gameTeamTotalOptionJuiceIndicator
				+ ", gameTeamTotalOptionJuiceNumber=" + gameTeamTotalOptionJuiceNumber
				
				+ ", gameTeamTotalOptionIndicatorSecond=" + gameTeamTotalOptionIndicatorSecond + ", gameTeamTotalOptionNumberSecond="
				+ gameTeamTotalOptionNumberSecond + ", gameTeamTotalOptionJuiceIndicatorSecond=" + gameTeamTotalOptionJuiceIndicatorSecond
				+ ", gameTeamTotalOptionJuiceNumberSecond=" + gameTeamTotalOptionJuiceNumberSecond
				
				+ "]\n" + super.toString();
	}
}
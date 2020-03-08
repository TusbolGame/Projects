/**
 * 
 */
package com.ticketadvantage.services.dao.sites;

/**
 * @author jmiller
 *
 */
public class SiteTransaction {
	private String amount;
	private String selectName;
	private String selectId;
	private String optionValue;
	private String inputId;
	private String inputName;
	private String inputValue;
	private Integer riskorwin;

	/**
	 * 
	 */
	public SiteTransaction() {
		super();
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the selectName
	 */
	public String getSelectName() {
		return selectName;
	}

	/**
	 * @param selectName the selectName to set
	 */
	public void setSelectName(String selectName) {
		this.selectName = selectName;
	}

	/**
	 * @return the selectId
	 */
	public String getSelectId() {
		return selectId;
	}

	/**
	 * @param selectId the selectId to set
	 */
	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

	/**
	 * @return the optionValue
	 */
	public String getOptionValue() {
		return optionValue;
	}

	/**
	 * @param optionValue the optionValue to set
	 */
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	/**
	 * @return the inputId
	 */
	public String getInputId() {
		return inputId;
	}

	/**
	 * @param inputId the inputId to set
	 */
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	/**
	 * @return the inputName
	 */
	public String getInputName() {
		return inputName;
	}

	/**
	 * @param inputName the inputName to set
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	/**
	 * @return the inputValue
	 */
	public String getInputValue() {
		return inputValue;
	}

	/**
	 * @param inputValue the inputValue to set
	 */
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	/**
	 * @return the riskorwin
	 */
	public Integer getRiskorwin() {
		return riskorwin;
	}

	/**
	 * @param riskorwin the riskorwin to set
	 */
	public void setRiskorwin(Integer riskorwin) {
		this.riskorwin = riskorwin;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SiteTransaction [amount=" + amount + ", selectName=" + selectName + ", selectId=" + selectId
				+ ", optionValue=" + optionValue + ", inputId=" + inputId + ", inputName=" + inputName + ", inputValue="
				+ inputValue + ", riskorwin=" + riskorwin + "]";
	}
}
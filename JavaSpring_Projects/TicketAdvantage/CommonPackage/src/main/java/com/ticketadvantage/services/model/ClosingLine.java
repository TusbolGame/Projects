package com.ticketadvantage.services.model;

public class ClosingLine {

	private Integer sportsInsightsEventId;
	private Double line;
	private Double money1;
	private Double money2;
	private Double money3;
	private Boolean homeTeamFavored;
	private String createdDate;
	private Integer linePercent1;
	private Integer linePercent2;
	private Integer linePercent3;
	private Integer linePercentMoney1;
	private Integer linePercentMoney2;
	private String sportsBook;
	private Integer signalType;
	private Integer lineType;

	public ClosingLine() {
		super();
	}

	/**
	 * @return the sportsInsightsEventId
	 */
	public Integer getSportsInsightsEventId() {
		return sportsInsightsEventId;
	}

	/**
	 * @param sportsInsightsEventId the sportsInsightsEventId to set
	 */
	public void setSportsInsightsEventId(Integer sportsInsightsEventId) {
		this.sportsInsightsEventId = sportsInsightsEventId;
	}

	/**
	 * @return the line
	 */
	public Double getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Double line) {
		this.line = line;
	}

	/**
	 * @return the money1
	 */
	public Double getMoney1() {
		return money1;
	}

	/**
	 * @param money1 the money1 to set
	 */
	public void setMoney1(Double money1) {
		this.money1 = money1;
	}

	/**
	 * @return the money2
	 */
	public Double getMoney2() {
		return money2;
	}

	/**
	 * @param money2 the money2 to set
	 */
	public void setMoney2(Double money2) {
		this.money2 = money2;
	}

	/**
	 * @return the money3
	 */
	public Double getMoney3() {
		return money3;
	}

	/**
	 * @param money3 the money3 to set
	 */
	public void setMoney3(Double money3) {
		this.money3 = money3;
	}

	/**
	 * @return the homeTeamFavored
	 */
	public Boolean getHomeTeamFavored() {
		return homeTeamFavored;
	}

	/**
	 * @param homeTeamFavored the homeTeamFavored to set
	 */
	public void setHomeTeamFavored(Boolean homeTeamFavored) {
		this.homeTeamFavored = homeTeamFavored;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the linePercent1
	 */
	public Integer getLinePercent1() {
		return linePercent1;
	}

	/**
	 * @param linePercent1 the linePercent1 to set
	 */
	public void setLinePercent1(Integer linePercent1) {
		this.linePercent1 = linePercent1;
	}

	/**
	 * @return the linePercent2
	 */
	public Integer getLinePercent2() {
		return linePercent2;
	}

	/**
	 * @param linePercent2 the linePercent2 to set
	 */
	public void setLinePercent2(Integer linePercent2) {
		this.linePercent2 = linePercent2;
	}

	/**
	 * @return the linePercent3
	 */
	public Integer getLinePercent3() {
		return linePercent3;
	}

	/**
	 * @param linePercent3 the linePercent3 to set
	 */
	public void setLinePercent3(Integer linePercent3) {
		this.linePercent3 = linePercent3;
	}

	/**
	 * @return the linePercentMoney1
	 */
	public Integer getLinePercentMoney1() {
		return linePercentMoney1;
	}

	/**
	 * @param linePercentMoney1 the linePercentMoney1 to set
	 */
	public void setLinePercentMoney1(Integer linePercentMoney1) {
		this.linePercentMoney1 = linePercentMoney1;
	}

	/**
	 * @return the linePercentMoney2
	 */
	public Integer getLinePercentMoney2() {
		return linePercentMoney2;
	}

	/**
	 * @param linePercentMoney2 the linePercentMoney2 to set
	 */
	public void setLinePercentMoney2(Integer linePercentMoney2) {
		this.linePercentMoney2 = linePercentMoney2;
	}

	/**
	 * @return the sportsBook
	 */
	public String getSportsBook() {
		return sportsBook;
	}

	/**
	 * @param sportsBook the sportsBook to set
	 */
	public void setSportsBook(String sportsBook) {
		this.sportsBook = sportsBook;
	}

	/**
	 * @return the signalType
	 */
	public Integer getSignalType() {
		return signalType;
	}

	/**
	 * @param signalType the signalType to set
	 */
	public void setSignalType(Integer signalType) {
		this.signalType = signalType;
	}

	/**
	 * @return the lineType
	 */
	public Integer getLineType() {
		return lineType;
	}

	/**
	 * @param lineType the lineType to set
	 */
	public void setLineType(Integer lineType) {
		this.lineType = lineType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClosingLine [sportsInsightsEventId=" + sportsInsightsEventId + ", line=" + line + ", money1=" + money1
				+ ", money2=" + money2 + ", money3=" + money3 + ", homeTeamFavored=" + homeTeamFavored
				+ ", createdDate=" + createdDate + ", linePercent1=" + linePercent1 + ", linePercent2=" + linePercent2
				+ ", linePercent3=" + linePercent3 + ", linePercentMoney1=" + linePercentMoney1 + ", linePercentMoney2="
				+ linePercentMoney2 + ", sportsBook=" + sportsBook + ", signalType=" + signalType + ", lineType="
				+ lineType + "]";
	}
}

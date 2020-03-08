/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author jmiller
 *
 */
@Entity
@Table(name = "emailevent")
@XmlRootElement(name = "emailevent")
@XmlAccessorType(XmlAccessType.NONE)
public class EmailEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailevent_generator")
	@SequenceGenerator(name="emailevent_generator", sequenceName = "emailevent_id_seq", allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement
	private Long id;

	@Column(name = "messagenum", unique = false, nullable = false)
	@XmlElement(nillable=false)
	private Integer messagenum;

	@Column(name = "fromemail", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private String fromemail;

	@Column(name = "toemail", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private String toemail;

	@Column(name = "ccemail", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private String ccemail;

	@Column(name = "bccemail", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private String bccemail;

	@Column(name = "subject", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private String subject;

	@Column(name = "bodytext", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String bodytext = "";

	@Column(name = "bodyhtml", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String bodyhtml = "";

	@Column(name = "emailname", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String emailname;

	@Column(name = "inet", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String inet;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datesent", unique = false, nullable = true)
	@XmlElement
	private Date datesent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datereceived", unique = false, nullable = true)
	@XmlElement
	private Date datereceived;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", unique = false, nullable = true)
	@XmlElement
	private Date datecreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", unique = false, nullable = true)
	@XmlElement
	private Date datemodified;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the messagenum
	 */
	public Integer getMessagenum() {
		return messagenum;
	}

	/**
	 * @param messagenum the messagenum to set
	 */
	public void setMessagenum(Integer messagenum) {
		this.messagenum = messagenum;
	}

	/**
	 * @return the fromemail
	 */
	public String getFromemail() {
		return fromemail;
	}

	/**
	 * @param fromemail the fromemail to set
	 */
	public void setFromemail(String fromemail) {
		this.fromemail = fromemail;
	}

	/**
	 * @return the toemail
	 */
	public String getToemail() {
		return toemail;
	}

	/**
	 * @param toemail the toemail to set
	 */
	public void setToemail(String toemail) {
		this.toemail = toemail;
	}

	/**
	 * @return the ccemail
	 */
	public String getCcemail() {
		return ccemail;
	}

	/**
	 * @param ccemail the ccemail to set
	 */
	public void setCcemail(String ccemail) {
		this.ccemail = ccemail;
	}

	/**
	 * @return the bccemail
	 */
	public String getBccemail() {
		return bccemail;
	}

	/**
	 * @param bccemail the bccemail to set
	 */
	public void setBccemail(String bccemail) {
		this.bccemail = bccemail;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the bodytext
	 */
	public String getBodytext() {
		return bodytext;
	}

	/**
	 * @param bodytext the bodytext to set
	 */
	public void setBodytext(String bodytext) {
		this.bodytext = bodytext;
	}

	/**
	 * @return the bodyhtml
	 */
	public String getBodyhtml() {
		return bodyhtml;
	}

	/**
	 * @param bodyhtml the bodyhtml to set
	 */
	public void setBodyhtml(String bodyhtml) {
		this.bodyhtml = bodyhtml;
	}

	/**
	 * @return the datesent
	 */
	public Date getDatesent() {
		return datesent;
	}

	/**
	 * @param datesent the datesent to set
	 */
	public void setDatesent(Date datesent) {
		this.datesent = datesent;
	}

	/**
	 * @return the datereceived
	 */
	public Date getDatereceived() {
		return datereceived;
	}

	/**
	 * @param datereceived the datereceived to set
	 */
	public void setDatereceived(Date datereceived) {
		this.datereceived = datereceived;
	}

	/**
	 * @return the emailname
	 */
	public String getEmailname() {
		return emailname;
	}

	/**
	 * @param emailname the emailname to set
	 */
	public void setEmailname(String emailname) {
		this.emailname = emailname;
	}

	/**
	 * @return the inet
	 */
	public String getInet() {
		return inet;
	}

	/**
	 * @param inet the inet to set
	 */
	public void setInet(String inet) {
		this.inet = inet;
	}

	/**
	 * @return the datecreated
	 */
	public Date getDatecreated() {
		return datecreated;
	}

	/**
	 * @param datecreated the datecreated to set
	 */
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	/**
	 * @return the datemodified
	 */
	public Date getDatemodified() {
		return datemodified;
	}

	/**
	 * @param datemodified the datemodified to set
	 */
	public void setDatemodified(Date datemodified) {
		this.datemodified = datemodified;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailEvent [id=" + id + ", messagenum=" + messagenum + ", fromemail=" + fromemail
				+ ", toemail=" + toemail + ", ccemail=" + ccemail + ", bccemail=" + bccemail + ", subject=" + subject
				+ ", bodytext=" + bodytext + ", bodyhtml=" + bodyhtml + ", emailname=" + emailname + ", inet=" + inet
				+ ", datesent=" + datesent + ", datereceived=" + datereceived + ", datecreated=" + datecreated
				+ ", datemodified=" + datemodified + "]";
	}
}
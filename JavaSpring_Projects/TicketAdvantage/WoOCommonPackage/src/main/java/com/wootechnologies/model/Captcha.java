/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
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
@Table (name="captcha")
@XmlRootElement(name = "captcha")
@XmlAccessorType(XmlAccessType.NONE)
public class Captcha implements Serializable {
	private static final long serialVersionUID = 3156058159245563904L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "captcha_generator")
	@SequenceGenerator(name="captcha_generator", sequenceName = "captcha_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id;

	@Column(name = "userid", nullable = false)
	@XmlElement
	private Long userid;

	@Column(name = "imagedata", nullable = true, columnDefinition="text")
	@XmlElement
	@Lob
	private String imagedata = "";

	@Column(name = "textdata", unique = true, nullable = false, length = 10)
	@XmlElement
	private String textdata = "";

	/**
	 * 
	 */
	public Captcha() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

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
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	/**
	 * @return the imagedata
	 */
	public String getImagedata() {
		return imagedata;
	}

	/**
	 * @param imagedata the imagedata to set
	 */
	public void setImagedata(String imagedata) {
		this.imagedata = imagedata;
	}

	/**
	 * @return the textdata
	 */
	public String getTextdata() {
		return textdata;
	}

	/**
	 * @param textdata the textdata to set
	 */
	public void setTextdata(String textdata) {
		this.textdata = textdata;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CaptchaInput [id=" + id + ", userid=" + userid + ", imagedata=" + imagedata + ", textdata=" + textdata
				+ "]";
	}
}
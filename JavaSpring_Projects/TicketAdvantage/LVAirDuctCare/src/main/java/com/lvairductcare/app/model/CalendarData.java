/**
 * 
 */
package com.lvairductcare.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

/**
 * @author jmiller
 *
 */
@Entity
@Table(name = "calendardata", schema="public")
@XmlAccessorType(XmlAccessType.NONE)
@Data
public class CalendarData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="calendardata_id_seq")
    @SequenceGenerator(name="calendardata_id_seq", sequenceName="calendardata_id_seq", allocationSize=1)
    @Column(name = "id")
    private Long id;

	@XmlElement
	private String firstname;

	@XmlElement
	private String lastname;

	@XmlElement
	private String phone;

	@XmlElement
	private String email;
	
	@XmlElement
	private String address;

	@XmlElement
	private String city;

	@XmlElement
	private Integer zipcode;

	@XmlElement
	private String servicetype;

	@XmlElement
	private String serviceunits;

	@XmlElement
	private String message;

	@XmlElement
	private Integer appointmentstarttime;

	@XmlElement
	private Integer appointmentendtime;

	@XmlElement
	private String appointmentday;

	/**
	 * 
	 */
	public CalendarData() {
		super();
	}
}
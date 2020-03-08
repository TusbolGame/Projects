/**
 * 
 */
package com.ticketadvantage.services.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
@Table (name="accountsta")
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.NONE)
public class Accounts implements Serializable, Comparable<Accounts> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountsta_generator")
	@SequenceGenerator(name="accountsta_generator", sequenceName = "accountsta_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id;

	@Column(name = "name", unique = true, nullable = false, length = 100)
	@XmlElement
	private String name;
	
	@Column(name = "username", nullable = false, length = 100)
	@XmlElement
	private String username;

	@Column(name = "password", nullable = true, length = 100)
	@XmlElement
	private String password;
	
	@Column(name = "url", nullable = false, length = 200)
	@XmlElement
	private String url;

	@Column(name = "spreadlimitamount", nullable = true)
	@XmlElement
	private Integer spreadlimitamount;

	@Column(name = "mllimitamount", nullable = true)
	@XmlElement
	private Integer mllimitamount;

	@Column(name = "totallimitamount", nullable = true)
	@XmlElement
	private Integer totallimitamount;

	@Column(name = "timezone", nullable = true)
	@XmlElement
	private String timezone = new String("ET");

	@Column(name = "ownerpercentage", nullable = true)
	@XmlElement(nillable=true)
	private Integer ownerpercentage = new Integer(100);

	@Column(name = "partnerpercentage", nullable = true)
	@XmlElement(nillable=true)
	private Integer partnerpercentage = new Integer(0);

	@Column(name = "proxylocation", nullable = true, length = 200)
	@XmlElement(nillable=true)
	private String proxylocation;

	@Column(name = "sitetype", nullable = true)
	@XmlElement(nillable=true)
	private String sitetype;

	@Column(name = "hourbefore", nullable = true)
	@XmlElement(nillable=true)
	private String hourbefore;
	
	@Column(name = "minutebefore", nullable = true)
	@XmlElement(nillable=true)
	private String minutebefore;

	@Column(name = "hourafter", nullable = true)
	@XmlElement(nillable=true)
	private String hourafter;

	@Column(name = "minuteafter", nullable = true)
	@XmlElement(nillable=true)
	private String minuteafter;

	@Column(name = "isactive", nullable = false)
	@XmlElement(nillable=true)
	private Boolean isactive = new Boolean(true);
	
	@Column(name = "ismobile", nullable = true)
	@XmlElement(nillable=true)
	private Boolean ismobile = new Boolean(false);

	@Column(name = "showrequestresponse", nullable = true)
	@XmlElement(nillable=true)
	private Boolean showrequestresponse = new Boolean(false);

	@Column(name = "iscomplexcaptcha", nullable = true)
	@XmlElement(nillable=true)
	private Boolean iscomplexcaptcha = new Boolean(false);

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", nullable = false)
	@XmlElement(nillable=true)
	private Date datecreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", nullable = false)
	@XmlElement(nillable=true)
	private Date datemodified;

	/*
    @OneToMany(mappedBy="account", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @XmlElement(nillable=true)
    private List<AccountEvent> accountEvents;
    */

	/**
	 * 
	 */
	public Accounts() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 
	 */
    @PrePersist
    protected void onCreate() {
    	datecreated = datemodified = new Date();
    	if (this.isactive == null) {
    		this.isactive = new Boolean(true);
    	}
    }

    /**
     * 
     */
    @PreUpdate
    protected void onUpdate() {
    	datemodified = new Date();
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the spreadlimitamount
	 */
	public Integer getSpreadlimitamount() {
		return spreadlimitamount;
	}

	/**
	 * @param spreadlimitamount the spreadlimitamount to set
	 */
	public void setSpreadlimitamount(Integer spreadlimitamount) {
		this.spreadlimitamount = spreadlimitamount;
	}

	/**
	 * @return the mllimitamount
	 */
	public Integer getMllimitamount() {
		return mllimitamount;
	}

	/**
	 * @param mllimitamount the mllimitamount to set
	 */
	public void setMllimitamount(Integer mllimitamount) {
		this.mllimitamount = mllimitamount;
	}

	/**
	 * @return the totallimitamount
	 */
	public Integer getTotallimitamount() {
		return totallimitamount;
	}

	/**
	 * @param totallimitamount the totallimitamount to set
	 */
	public void setTotallimitamount(Integer totallimitamount) {
		this.totallimitamount = totallimitamount;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the ownerpercentage
	 */
	public Integer getOwnerpercentage() {
		return ownerpercentage;
	}

	/**
	 * @param ownerpercentage the ownerpercentage to set
	 */
	public void setOwnerpercentage(Integer ownerpercentage) {
		this.ownerpercentage = ownerpercentage;
	}

	/**
	 * @return the partnerpercentage
	 */
	public Integer getPartnerpercentage() {
		return partnerpercentage;
	}

	/**
	 * @param partnerpercentage the partnerpercentage to set
	 */
	public void setPartnerpercentage(Integer partnerpercentage) {
		this.partnerpercentage = partnerpercentage;
	}

	/**
	 * @return the proxylocation
	 */
	public String getProxylocation() {
		return proxylocation;
	}

	/**
	 * @param proxylocation the proxylocation to set
	 */
	public void setProxylocation(String proxylocation) {
		this.proxylocation = proxylocation;
	}

	/**
	 * @return the isactive
	 */
	public Boolean getIsactive() {
		return isactive;
	}

	/**
	 * @param isactive the isactive to set
	 */
	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
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

	/**
	 * @return the sitetype
	 */
	public String getSitetype() {
		return sitetype;
	}

	/**
	 * @param sitetype the sitetype to set
	 */
	public void setSitetype(String sitetype) {
		this.sitetype = sitetype;
	}

	/**
	 * @return the hourbefore
	 */
	public String getHourbefore() {
		return hourbefore;
	}

	/**
	 * @param hourbefore the hourbefore to set
	 */
	public void setHourbefore(String hourbefore) {
		this.hourbefore = hourbefore;
	}

	/**
	 * @return the minutebefore
	 */
	public String getMinutebefore() {
		return minutebefore;
	}

	/**
	 * @param minutebefore the minutebefore to set
	 */
	public void setMinutebefore(String minutebefore) {
		this.minutebefore = minutebefore;
	}

	/**
	 * @return the hourafter
	 */
	public String getHourafter() {
		return hourafter;
	}

	/**
	 * @param hourafter the hourafter to set
	 */
	public void setHourafter(String hourafter) {
		this.hourafter = hourafter;
	}

	/**
	 * @return the minuteafter
	 */
	public String getMinuteafter() {
		return minuteafter;
	}

	/**
	 * @param minuteafter the minuteafter to set
	 */
	public void setMinuteafter(String minuteafter) {
		this.minuteafter = minuteafter;
	}

	/**
	 * @return the ismobile
	 */
	public Boolean getIsmobile() {
		return ismobile;
	}

	/**
	 * @param ismobile the ismobile to set
	 */
	public void setIsmobile(Boolean ismobile) {
		this.ismobile = ismobile;
	}

	/**
	 * @return the showrequestresponse
	 */
	public Boolean getShowrequestresponse() {
		return showrequestresponse;
	}

	/**
	 * @param showrequestresponse the showrequestresponse to set
	 */
	public void setShowrequestresponse(Boolean showrequestresponse) {
		this.showrequestresponse = showrequestresponse;
	}

	/**
	 * @return the iscomplexcaptcha
	 */
	public Boolean getIscomplexcaptcha() {
		return iscomplexcaptcha;
	}

	/**
	 * @param iscomplexcaptcha the iscomplexcaptcha to set
	 */
	public void setIscomplexcaptcha(Boolean iscomplexcaptcha) {
		this.iscomplexcaptcha = iscomplexcaptcha;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Accounts o) {
	    if (getName() == null || o.getName() == null)
	        return 0;
	    return getName().compareTo(o.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Accounts [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password + ", url="
				+ url + ", spreadlimitamount=" + spreadlimitamount + ", mllimitamount=" + mllimitamount
				+ ", totallimitamount=" + totallimitamount + ", timezone=" + timezone + ", ownerpercentage="
				+ ownerpercentage + ", partnerpercentage=" + partnerpercentage + ", proxylocation=" + proxylocation
				+ ", sitetype=" + sitetype + ", hourbefore=" + hourbefore + ", minutebefore=" + minutebefore
				+ ", hourafter=" + hourafter + ", minuteafter=" + minuteafter + ", isactive=" + isactive + ", ismobile="
				+ ismobile + ", showrequestresponse=" + showrequestresponse + ", iscomplexcaptcha=" + iscomplexcaptcha
				+ ", datecreated=" + datecreated + ", datemodified=" + datemodified + "]";
	}
}
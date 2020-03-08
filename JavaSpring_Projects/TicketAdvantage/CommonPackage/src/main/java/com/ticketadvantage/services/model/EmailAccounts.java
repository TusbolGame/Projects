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
@Table (name="emailaccountsta")
@XmlRootElement(name = "emailaccount")
@XmlAccessorType(XmlAccessType.NONE)
public class EmailAccounts implements Serializable, Comparable<EmailAccounts> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailaccountsta_generator")
	@SequenceGenerator(name="emailaccountsta_generator", sequenceName = "emailaccountsta_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id;

	@Column(name = "name", nullable = false)
	@XmlElement
	private String name;

	@Column(name = "accountid", unique = true, nullable = false)
	@XmlElement
	private String accountid;

	@Column(name = "inet", unique = true, nullable = false)
	@XmlElement
	private String inet;
	
	@Column(name = "address", unique = false, nullable = false)
	@XmlElement(nillable=false)
	private String address;

	@Column(name = "password", unique = false, nullable = false)
	@XmlElement(nillable=false)
	private String password;

	@Column(name = "host", unique = false, nullable = false)
	@XmlElement(nillable=false)
	private String host;

	@Column(name = "port", unique = false, nullable = false)
	@XmlElement(nillable=false)
	private String port;

	@Column(name = "tls", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean tls;

	@Column(name = "timezone", nullable = true)
	@XmlElement(nillable=true)
	private String timezone;

	@Column(name = "sitetype", nullable = true)
	@XmlElement(nillable=true)
	private String sitetype;

	@Column(name = "provider", nullable = true)
	@XmlElement(nillable=true)
	private String provider;

	@Column(name = "emailtype", nullable = true)
	@XmlElement(nillable=true)
	private String emailtype;

	@Column(name = "authenticationtype", nullable = true)
	@XmlElement(nillable=true)
	private String authenticationtype;

	@Column(name = "clientid", nullable = true)
	@XmlElement(nillable=true)
	private String clientid;

	@Column(name = "clientsecret", nullable = true)
	@XmlElement(nillable=true)
	private String clientsecret;

	@Column(name = "refreshtoken", nullable = true)
	@XmlElement(nillable=true)
	private String refreshtoken;

	@Column(name = "granttype", nullable = true)
	@XmlElement(nillable=true)
	private String granttype;
	
	@Column(name = "isactive", nullable = false)
	@XmlElement(nillable=true)
	private Boolean isactive;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", nullable = false)
	@XmlElement(nillable=true)
	private Date datecreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", nullable = false)
	@XmlElement(nillable=true)
	private Date datemodified;

	/**
	 * 
	 */
	public EmailAccounts() {
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
	 * @return the accountid
	 */
	public String getAccountid() {
		return accountid;
	}

	/**
	 * @param accountid the accountid to set
	 */
	public void setAccountid(String accountid) {
		this.accountid = accountid;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the tls
	 */
	public Boolean getTls() {
		return tls;
	}

	/**
	 * @param tls the tls to set
	 */
	public void setTls(Boolean tls) {
		this.tls = tls;
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
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * @return the emailtype
	 */
	public String getEmailtype() {
		return emailtype;
	}

	/**
	 * @param emailtype the emailtype to set
	 */
	public void setEmailtype(String emailtype) {
		this.emailtype = emailtype;
	}

	/**
	 * @return the authenticationtype
	 */
	public String getAuthenticationtype() {
		return authenticationtype;
	}

	/**
	 * @param authenticationtype the authenticationtype to set
	 */
	public void setAuthenticationtype(String authenticationtype) {
		this.authenticationtype = authenticationtype;
	}

	/**
	 * @return the clientid
	 */
	public String getClientid() {
		return clientid;
	}

	/**
	 * @param clientid the clientid to set
	 */
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	/**
	 * @return the clientsecret
	 */
	public String getClientsecret() {
		return clientsecret;
	}

	/**
	 * @param clientsecret the clientsecret to set
	 */
	public void setClientsecret(String clientsecret) {
		this.clientsecret = clientsecret;
	}

	/**
	 * @return the refreshtoken
	 */
	public String getRefreshtoken() {
		return refreshtoken;
	}

	/**
	 * @param refreshtoken the refreshtoken to set
	 */
	public void setRefreshtoken(String refreshtoken) {
		this.refreshtoken = refreshtoken;
	}

	/**
	 * @return the granttype
	 */
	public String getGranttype() {
		return granttype;
	}

	/**
	 * @param granttype the granttype to set
	 */
	public void setGranttype(String granttype) {
		this.granttype = granttype;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EmailAccounts o) {
	    if (getName() == null || o.getName() == null)
	        return 0;
	    return getName().compareTo(o.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailAccounts [id=" + id + ", name=" + name + ", accountid=" + accountid + ", inet=" + inet
				+ ", address=" + address + ", password=" + password + ", host=" + host + ", port=" + port + ", tls="
				+ tls + ", timezone=" + timezone + ", sitetype=" + sitetype + ", provider=" + provider + ", emailtype="
				+ emailtype + ", authenticationtype=" + authenticationtype + ", clientid=" + clientid
				+ ", clientsecret=" + clientsecret + ", refreshtoken=" + refreshtoken + ", granttype=" + granttype
				+ ", isactive=" + isactive + ", datecreated=" + datecreated + ", datemodified=" + datemodified + "]";
	}
}
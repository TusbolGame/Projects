/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "usersta")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersta_generator")
	@SequenceGenerator(name="usersta_generator", sequenceName = "usersta_seq", initialValue=1, allocationSize=50)
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement
	private Long id;

	@Column(name = "username", unique = true, nullable = false, length = 50)
	@XmlElement
	private String username;
	
	@Column(name = "password", unique = false, nullable = true, length = 50)
	@XmlElement(nillable=true)
	private String password;
	
	@Column(name = "email", unique = false, nullable = true, length = 100)
	@XmlElement(nillable=true)
	private String email;

	@Column(name = "mobilenumber", unique = false, nullable = true, length = 16)
	@XmlElement(nillable=true)
	private String mobilenumber;

	@Column(name = "isactive")
	@XmlElement
	private Boolean isactive;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", unique = false, nullable = true)
	@XmlElement
	private Date datecreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", unique = false, nullable = true)
	@XmlElement
	private Date datemodified;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @OrderBy("name ASC")
    @JoinTable(name="usersaccounts", joinColumns={@JoinColumn(name="usersid", referencedColumnName="id")}
    , inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> accounts;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @OrderBy("name ASC")
    @JoinTable(name="usersemailaccounts", joinColumns={@JoinColumn(name="usersid", referencedColumnName="id")}
    , inverseJoinColumns={@JoinColumn(name="emailaccountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<EmailAccounts> emailaccounts;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @OrderBy("name ASC")
    @JoinTable(name="usersgroups", joinColumns={@JoinColumn(name="usersid", referencedColumnName="id")}
    , inverseJoinColumns={@JoinColumn(name="groupsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Groups> groups;
 
/*
    @OneToMany(mappedBy="user", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @OrderBy("weekstartdate DESC")
    @XmlElement(nillable=true)
    private List<UserBilling> userBillings;
*/
    
    /**
	 * 
	 */
	public User() {
		super();
//		log.debug("Entering User()");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mobilenumber
	 */
	public String getMobilenumber() {
		return mobilenumber;
	}

	/**
	 * @param mobilenumber the mobilenumber to set
	 */
	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
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
	 * @return the accounts
	 */
	public Set<Accounts> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(Set<Accounts> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the user billings
	 */
//	public List<UserBilling> getUserBillings() {
//		return userBillings;
//	}

	/**
	 * @param user billings the user billings to set
	 */
//	public void setUserBillings(List<UserBilling> userBillings) {
//		this.userBillings = userBillings;
//	}

	/**
	 * @return the emailaccounts
	 */
	public Set<EmailAccounts> getEmailaccounts() {
		return emailaccounts;
	}

	/**
	 * @param emailaccounts the emailaccounts to set
	 */
	public void setEmailaccounts(Set<EmailAccounts> emailaccounts) {
		this.emailaccounts = emailaccounts;
	}

	/**
	 * @return the groups
	 */
	public Set<Groups> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Set<Groups> groups) {
		this.groups = groups;
	}

	/**
	 * 
	 */
    @PrePersist
    protected void onCreate() {
    	datecreated = datemodified = new Date();
    }

    /**
     * 
     */
    @PreUpdate
    protected void onUpdate() {
    	datecreated = datemodified = new Date();
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", mobilenumber=" + mobilenumber + ", isactive=" + isactive + ", datecreated=" + datecreated
				+ ", datemodified=" + datemodified + ", accounts=" + accounts + ", emailaccounts=" + emailaccounts
				+ ", groups=" + groups + "]";
	}
}
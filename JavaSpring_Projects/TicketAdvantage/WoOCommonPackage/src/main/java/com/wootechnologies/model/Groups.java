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
import javax.persistence.ManyToMany;
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
@Table (name="groupsta")
@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.NONE)
public class Groups implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupsta_generator")
	@SequenceGenerator(name="groupsta_generator", sequenceName = "groupsta_id_seq", allocationSize=1)
	@Column(name = "id", updatable = false, nullable = false)
	@XmlElement(nillable=true)
	private Long id = null;

	@Column(name = "name", unique = true, nullable = false, length = 100)
	@XmlElement
	private String name;

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

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("name ASC")
    @JoinTable(
    		name="groupsaccounts", 
    		joinColumns={@JoinColumn(name="groupsid", referencedColumnName="id")}, 
    		inverseJoinColumns={@JoinColumn(name="accountsid", referencedColumnName="id")})
    @XmlElement(nillable=true)
    private Set<Accounts> accounts;

	/**
	 * 
	 */
	public Groups() {
		super();
//		log.debug("Entering User()");
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Groups [id=" + id + ", name=" + name + ", isactive=" + isactive
				+ ", datecreated=" + datecreated + ", datemodified=" + datemodified + ", accounts=" + accounts + "]";
	}
}
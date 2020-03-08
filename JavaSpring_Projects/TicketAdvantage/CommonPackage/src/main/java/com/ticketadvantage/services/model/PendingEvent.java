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
@Table(name = "pendingevents")
@XmlRootElement(name = "pendingevents")
@XmlAccessorType(XmlAccessType.NONE)
public class PendingEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pendingevents_generator")
	@SequenceGenerator(name="pendingevents_generator", sequenceName = "pendingevents_id_seq", allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	@XmlElement
	private Long id;

	@Column(name = "userid", unique = true, nullable = false)
	@XmlElement(nillable=true)
	private Long userid;

	@Column(name = "ticketnum", unique = true, nullable = false)
	@XmlElement(nillable=true)
	private String ticketnum;

	@Column(name = "eventtype", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String eventtype;

	@Column(name = "linetype", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String linetype;
	
	@Column(name = "eventdate", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String eventdate;	

	@Column(name = "gamesport", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String gamesport;

	@Column(name = "gametype", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String gametype;

	@Column(name = "team", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String team;

	@Column(name = "rotationid", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String rotationid;

	@Column(name = "line", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String line;

	@Column(name = "lineplusminus", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String lineplusminus;

	@Column(name = "juice", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String juice;

	@Column(name = "juiceplusminus", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String juiceplusminus;

	@Column(name = "pitcher", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String pitcher;
	
	@Column(name = "risk", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String risk;

	@Column(name = "win", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String win;

	@Column(name = "pendingtype", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String pendingtype;

	@Column(name = "accountname", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String accountname;

	@Column(name = "accountid", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String accountid;

	@Column(name = "doposturl", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean doposturl = new Boolean(false);

	@Column(name = "posturl", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String posturl;

	@Column(name = "customerid", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String customerid = "";

	@Column(name = "inet", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String inet = "";

	@Column(name = "dateaccepted", unique = false, nullable = true)
	@XmlElement
	private String dateaccepted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "gamedate", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Date gamedate;	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", unique = false, nullable = true)
	@XmlElement
	private Date datecreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", unique = false, nullable = true)
	@XmlElement
	private Date datemodified;

	@Column(name = "pitcher1", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String pitcher1 = "";

	@Column(name = "pitcher2", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String pitcher2 = "";

	@Column(name = "listedpitchers", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean listedpitchers = new Boolean(true);

	@Column(name = "transactiontype", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String transactiontype = new String("Straight");

	@Column(name = "parlaynumber", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Long parlaynumber = new Long(0);

	@Column(name = "amountrisk", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String amountrisk = "";

	@Column(name = "amountwin", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private String amountwin = "";

	@Column(name = "parlayseqnum", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Integer parlayseqnum = new Integer(0);

	@Column(name = "numunits", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Float numunits = new Float(0);

	@Column(name = "islean", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean islean = new Boolean(false);

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
	 * @return the ticketnum
	 */
	public String getTicketnum() {
		return ticketnum;
	}

	/**
	 * @param ticketnum the ticketnum to set
	 */
	public void setTicketnum(String ticketnum) {
		this.ticketnum = ticketnum;
	}

	/**
	 * @return the eventtype
	 */
	public String getEventtype() {
		return eventtype;
	}

	/**
	 * @param eventtype the eventtype to set
	 */
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	/**
	 * @return the linetype
	 */
	public String getLinetype() {
		return linetype;
	}

	/**
	 * @param linetype the linetype to set
	 */
	public void setLinetype(String linetype) {
		this.linetype = linetype;
	}

	/**
	 * @return the eventdate
	 */
	public String getEventdate() {
		return eventdate;
	}

	/**
	 * @param eventdate the eventdate to set
	 */
	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}

	/**
	 * @return the gamesport
	 */
	public String getGamesport() {
		return gamesport;
	}

	/**
	 * @param gamesport the gamesport to set
	 */
	public void setGamesport(String gamesport) {
		this.gamesport = gamesport;
	}

	/**
	 * @return the gametype
	 */
	public String getGametype() {
		return gametype;
	}

	/**
	 * @param gametype the gametype to set
	 */
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * @return the rotationid
	 */
	public String getRotationid() {
		return rotationid;
	}

	/**
	 * @param rotationid the rotationid to set
	 */
	public void setRotationid(String rotationid) {
		this.rotationid = rotationid;
	}

	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}

	/**
	 * @return the lineplusminus
	 */
	public String getLineplusminus() {
		return lineplusminus;
	}

	/**
	 * @param lineplusminus the lineplusminus to set
	 */
	public void setLineplusminus(String lineplusminus) {
		this.lineplusminus = lineplusminus;
	}

	/**
	 * @return the juice
	 */
	public String getJuice() {
		return juice;
	}

	/**
	 * @param juice the juice to set
	 */
	public void setJuice(String juice) {
		this.juice = juice;
	}

	/**
	 * @return the juiceplusminus
	 */
	public String getJuiceplusminus() {
		return juiceplusminus;
	}

	/**
	 * @param juiceplusminus the juiceplusminus to set
	 */
	public void setJuiceplusminus(String juiceplusminus) {
		this.juiceplusminus = juiceplusminus;
	}

	/**
	 * @return the pitcher
	 */
	public String getPitcher() {
		return pitcher;
	}

	/**
	 * @param pitcher the pitcher to set
	 */
	public void setPitcher(String pitcher) {
		this.pitcher = pitcher;
	}

	/**
	 * @return the risk
	 */
	public String getRisk() {
		return risk;
	}

	/**
	 * @param risk the risk to set
	 */
	public void setRisk(String risk) {
		this.risk = risk;
	}

	/**
	 * @return the win
	 */
	public String getWin() {
		return win;
	}

	/**
	 * @param win the win to set
	 */
	public void setWin(String win) {
		this.win = win;
	}

	/**
	 * @return the pendingtype
	 */
	public String getPendingtype() {
		return pendingtype;
	}

	/**
	 * @param pendingtype the pendingtype to set
	 */
	public void setPendingtype(String pendingtype) {
		this.pendingtype = pendingtype;
	}

	/**
	 * @return the accountname
	 */
	public String getAccountname() {
		return accountname;
	}

	/**
	 * @param accountname the accountname to set
	 */
	public void setAccountname(String accountname) {
		this.accountname = accountname;
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
	 * @return the doposturl
	 */
	public Boolean getDoposturl() {
		return doposturl;
	}

	/**
	 * @param doposturl the doposturl to set
	 */
	public void setDoposturl(Boolean doposturl) {
		this.doposturl = doposturl;
	}

	/**
	 * @return the posturl
	 */
	public String getPosturl() {
		return posturl;
	}

	/**
	 * @param posturl the posturl to set
	 */
	public void setPosturl(String posturl) {
		this.posturl = posturl;
	}

	/**
	 * @return the customerid
	 */
	public String getCustomerid() {
		return customerid;
	}

	/**
	 * @param customerid the customerid to set
	 */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
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
	 * @return the dateaccepted
	 */
	public String getDateaccepted() {
		return dateaccepted;
	}

	/**
	 * @param dateaccepted the dateaccepted to set
	 */
	public void setDateaccepted(String dateaccepted) {
		this.dateaccepted = dateaccepted;
	}

	/**
	 * @return the gamedate
	 */
	public Date getGamedate() {
		return gamedate;
	}

	/**
	 * @param gamedate the gamedate to set
	 */
	public void setGamedate(Date gamedate) {
		this.gamedate = gamedate;
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
	 * @return the pitcher1
	 */
	public String getPitcher1() {
		return pitcher1;
	}

	/**
	 * @param pitcher1 the pitcher1 to set
	 */
	public void setPitcher1(String pitcher1) {
		this.pitcher1 = pitcher1;
	}

	/**
	 * @return the pitcher2
	 */
	public String getPitcher2() {
		return pitcher2;
	}

	/**
	 * @param pitcher2 the pitcher2 to set
	 */
	public void setPitcher2(String pitcher2) {
		this.pitcher2 = pitcher2;
	}

	/**
	 * @return the listedpitchers
	 */
	public Boolean getListedpitchers() {
		return listedpitchers;
	}

	/**
	 * @param listedpitchers the listedpitchers to set
	 */
	public void setListedpitchers(Boolean listedpitchers) {
		this.listedpitchers = listedpitchers;
	}

	/**
	 * @return the transactiontype
	 */
	public String getTransactiontype() {
		return transactiontype;
	}

	/**
	 * @param transactiontype the transactiontype to set
	 */
	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}

	/**
	 * @return the parlaynumber
	 */
	public Long getParlaynumber() {
		return parlaynumber;
	}

	/**
	 * @param parlaynumber the parlaynumber to set
	 */
	public void setParlaynumber(Long parlaynumber) {
		this.parlaynumber = parlaynumber;
	}

	/**
	 * @return the amountrisk
	 */
	public String getAmountrisk() {
		return amountrisk;
	}

	/**
	 * @param amountrisk the amountrisk to set
	 */
	public void setAmountrisk(String amountrisk) {
		this.amountrisk = amountrisk;
	}

	/**
	 * @return the amountwin
	 */
	public String getAmountwin() {
		return amountwin;
	}

	/**
	 * @param amountwin the amountwin to set
	 */
	public void setAmountwin(String amountwin) {
		this.amountwin = amountwin;
	}

	/**
	 * @return the parlayseqnum
	 */
	public Integer getParlayseqnum() {
		return parlayseqnum;
	}

	/**
	 * @param parlayseqnum the parlayseqnum to set
	 */
	public void setParlayseqnum(Integer parlayseqnum) {
		this.parlayseqnum = parlayseqnum;
	}

	/**
	 * @return the numunits
	 */
	public Float getNumunits() {
		return numunits;
	}

	/**
	 * @param numunits the numunits to set
	 */
	public void setNumunits(Float numunits) {
		this.numunits = numunits;
	}

	/**
	 * @return the islean
	 */
	public Boolean getIslean() {
		return islean;
	}

	/**
	 * @param islean the islean to set
	 */
	public void setIslean(Boolean islean) {
		this.islean = islean;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PendingEvent [id=" + id + ", userid=" + userid + ", ticketnum=" + ticketnum + ", eventtype=" + eventtype
				+ ", linetype=" + linetype + ", eventdate=" + eventdate + ", gamesport=" + gamesport + ", gametype="
				+ gametype + ", team=" + team + ", rotationid=" + rotationid + ", line=" + line + ", lineplusminus="
				+ lineplusminus + ", juice=" + juice + ", juiceplusminus=" + juiceplusminus + ", pitcher=" + pitcher
				+ ", risk=" + risk + ", win=" + win + ", pendingtype=" + pendingtype + ", accountname=" + accountname
				+ ", accountid=" + accountid + ", doposturl=" + doposturl + ", posturl=" + posturl + ", customerid="
				+ customerid + ", inet=" + inet + ", dateaccepted=" + dateaccepted + ", gamedate=" + gamedate
				+ ", datecreated=" + datecreated + ", datemodified=" + datemodified + ", pitcher1=" + pitcher1
				+ ", pitcher2=" + pitcher2 + ", listedpitchers=" + listedpitchers + ", transactiontype="
				+ transactiontype + ", parlaynumber=" + parlaynumber + ", amountrisk=" + amountrisk + ", amountwin="
				+ amountwin + ", parlayseqnum=" + parlayseqnum + ", numunits=" + numunits + ", islean=" + islean + "]";
	}
}
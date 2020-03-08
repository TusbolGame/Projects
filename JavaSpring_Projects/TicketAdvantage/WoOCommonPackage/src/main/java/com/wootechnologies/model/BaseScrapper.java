/**
 * 
 */
package com.wootechnologies.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author jmiller
 *
 */
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS) //slightly more normalized
@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public class BaseScrapper implements Serializable, Comparable<BaseScrapper> {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
    @XmlElement
	private Long id;

	@Column(name = "scrappername", unique = false, nullable = false, length = 10)
	@XmlElement(nillable=true)
	private String scrappername;

	@Column(name = "userid", unique = true, nullable = false)
	@XmlElement(nillable=true)
	private Long userid;

	@Column(name = "spreadlineadjustment", unique = false, nullable = false, length = 10)
	@XmlElement(nillable=true)
	private String spreadlineadjustment;

	@Column(name = "spreadjuiceindicator", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String spreadjuiceindicator;

	@Column(name = "spreadjuice", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String spreadjuice;

	@Column(name = "spreadjuiceadjustment", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String spreadjuiceadjustment;

	@Column(name = "spreadmaxamount", unique = false, nullable = false, length = 10)
	@XmlElement(nillable=true)
	private String spreadmaxamount;

	@Column(name = "spreadsourceamount", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Float spreadsourceamount = new Float(0);

	@Column(name = "totallineadjustment", unique = false, nullable = false, length = 10)
	@XmlElement(nillable=true)
	private String totallineadjustment;

	@Column(name = "totaljuiceindicator", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String totaljuiceindicator;

	@Column(name = "totaljuice", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String totaljuice;

	@Column(name = "totaljuiceadjustment", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String totaljuiceadjustment;

	@Column(name = "totalmaxamount", unique = false, nullable = false, length = 10)
	@XmlElement(nillable=true)
	private String totalmaxamount;

	@Column(name = "totalsourceamount", unique = false, nullable = false)
	@XmlElement(nillable=true)
	private Float totalsourceamount = new Float(0);

	@Column(name = "mlindicator", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String mlindicator;

	@Column(name = "mlline", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String mlline;

	@Column(name = "mllineadjustment", unique = false, nullable = true, length = 10)
	@XmlElement(nillable=true)
	private String mllineadjustment;

	@Column(name = "mlmaxamount", unique = false, nullable = false, length = 10)
	@XmlElement(nillable=true)
	private String mlmaxamount;

	@Column(name = "mlsourceamount", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Float mlsourceamount = new Float(0);

	@Column(name = "pullinginterval", unique = false, nullable = false, length = 10)
	@XmlElement(nillable=true)
	private String pullinginterval;

	@Column(name = "mobiletext", unique = false, nullable = false, length = 50)
	@XmlElement(nillable=true)
	private String mobiletext;	

	@Column(name = "telegramnumber", unique = false, nullable = false, length = 12)
	@XmlElement(nillable=true)
	private String telegramnumber;

	@Column(name = "servernumber", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Integer servernumber = new Integer(2);

	@Column(name = "enableretry", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean enableretry = new Boolean(false);		

	@Column(name = "fullfill", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean fullfill = new Boolean(false);		

	@Column(name = "orderamount", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Integer orderamount = new Integer(0);

	@Column(name = "middlerules", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean middlerules = new Boolean(false);	

	@Column(name = "checkdupgame", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean checkdupgame = new Boolean(true);	

	@Column(name = "playotherside", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean playotherside = new Boolean(false);

	@Column(name = "bestprice", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean bestprice = new Boolean(false);	

	@Column(name = "sendtextforaccount", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean sendtextforaccount = new Boolean(false);

	@Column(name = "onoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean onoff = new Boolean(true);

	@Column(name = "gameonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean gameonoff = new Boolean(true);

	@Column(name = "firstonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean firstonoff = new Boolean(true);

	@Column(name = "secondonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean secondonoff = new Boolean(true);

	@Column(name = "thirdonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean thirdonoff = new Boolean(true);

	@Column(name = "nflspreadonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nflspreadonoff = new Boolean(true);

	@Column(name = "nfltotalonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nfltotalonoff = new Boolean(true);

	@Column(name = "nflmlonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nflmlonoff = new Boolean(true);

	@Column(name = "ncaafspreadonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean ncaafspreadonoff = new Boolean(true);

	@Column(name = "ncaaftotalonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean ncaaftotalonoff = new Boolean(true);

	@Column(name = "ncaafmlonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean ncaafmlonoff = new Boolean(true);

	@Column(name = "nbaspreadonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nbaspreadonoff = new Boolean(true);

	@Column(name = "nbatotalonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nbatotalonoff = new Boolean(true);

	@Column(name = "nbamlonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nbamlonoff = new Boolean(true);

	@Column(name = "ncaabspreadonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean ncaabspreadonoff = new Boolean(true);

	@Column(name = "ncaabtotalonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean ncaabtotalonoff = new Boolean(true);

	@Column(name = "ncaabmlonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean ncaabmlonoff = new Boolean(true);

	@Column(name = "wnbaspreadonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean wnbaspreadonoff = new Boolean(true);

	@Column(name = "wnbatotalonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean wnbatotalonoff = new Boolean(true);

	@Column(name = "wnbamlonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean wnbamlonoff = new Boolean(true);

	@Column(name = "nhlspreadonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nhlspreadonoff = new Boolean(true);

	@Column(name = "nhltotalonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nhltotalonoff = new Boolean(true);

	@Column(name = "nhlmlonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean nhlmlonoff = new Boolean(true);

	@Column(name = "mlbspreadonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean mlbspreadonoff = new Boolean(true);

	@Column(name = "mlbtotalonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean mlbtotalonoff = new Boolean(true);

	@Column(name = "mlbmlonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean mlbmlonoff = new Boolean(true);

	@Column(name = "internationalbaseballspreadonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean internationalbaseballspreadonoff = new Boolean(true);

	@Column(name = "internationalbaseballtotalonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean internationalbaseballtotalonoff = new Boolean(true);

	@Column(name = "internationalbaseballmlonoff", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean internationalbaseballmlonoff = new Boolean(true);

	@Column(name = "keynumber", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean keynumber = new Boolean(false);

	@Column(name = "humanspeed", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean humanspeed = new Boolean(false);

	@Column(name = "unitsenabled", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean unitsenabled = new Boolean(false);

	@Column(name = "spreadunit", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Double spreadunit = new Double(0);

	@Column(name = "totalunit", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Double totalunit = new Double(0);

	@Column(name = "mlunit", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Double mlunit = new Double(0);

	@Column(name = "leanssenabled", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Boolean leanssenabled = new Boolean(false);

	@Column(name = "spreadlean", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Double spreadlean = new Double(0);

	@Column(name = "totallean", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Double totallean = new Double(0);

	@Column(name = "mllean", unique = false, nullable = true)
	@XmlElement(nillable=true)
	private Double mllean = new Double(0);

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datecreated", unique = false, nullable = true)
	@XmlElement
	private Date datecreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datemodified", unique = false, nullable = true)
	@XmlElement
	private Date datemodified;

	/**
	 * 
	 */
    @PrePersist
    protected void onCreate() {
    	datecreated = datemodified = new Date();
    	if (this.middlerules == null) {
    		middlerules = new Boolean(false);
    	}
    	if (this.checkdupgame == null) {
    		checkdupgame = new Boolean(true);
    	}
    	if (this.playotherside == null) {
    		playotherside = new Boolean(false);
    	}
    	if (this.bestprice == null) {
    		bestprice = new Boolean(false);
    	}
    	if (gameonoff == null) {
    		gameonoff = new Boolean(true);
    	}
    	if (firstonoff == null) {
    		firstonoff = new Boolean(true);
    	}
    	if (secondonoff == null) {
    		secondonoff = new Boolean(true);
    	}
    	if (thirdonoff == null) {
    		thirdonoff = new Boolean(true);
    	}
    	if (nflspreadonoff == null) {
    		nflspreadonoff = new Boolean(true);
    	}
    	if (nfltotalonoff == null) {
    		nfltotalonoff = new Boolean(true);
    	}
    	if (nflmlonoff == null) {
    		nflmlonoff = new Boolean(true);
    	}
    	if (ncaafspreadonoff == null) {
    		ncaafspreadonoff = new Boolean(true);
    	}
    	if (ncaaftotalonoff == null) {
    		ncaaftotalonoff = new Boolean(true);
    	}
    	if (ncaafmlonoff == null) {
    		ncaafmlonoff = new Boolean(true);
    	}
    	if (nbaspreadonoff == null) {
    		nbaspreadonoff = new Boolean(true);
    	}
    	if (nbatotalonoff == null) {
    		nbatotalonoff = new Boolean(true);
    	}
    	if (nbamlonoff == null) {
    		nbamlonoff = new Boolean(true);
    	}
    	if (ncaabspreadonoff == null) {
    		ncaabspreadonoff = new Boolean(true);
    	}
    	if (ncaabtotalonoff == null) {
    		ncaabtotalonoff = new Boolean(true);
    	}
    	if (ncaabmlonoff == null) {
    		ncaabmlonoff = new Boolean(true);
    	}
    	if (wnbaspreadonoff == null) {
    		wnbaspreadonoff = new Boolean(true);
    	}
    	if (wnbatotalonoff == null) {
    		wnbatotalonoff = new Boolean(true);
    	}
    	if (wnbamlonoff == null) {
    		wnbamlonoff = new Boolean(true);
    	}
    	if (nhlspreadonoff == null) {
    		nhlspreadonoff = new Boolean(true);
    	}
    	if (nhltotalonoff == null) {
    		nhltotalonoff = new Boolean(true);
    	}
    	if (nhlmlonoff == null) {
    		nhlmlonoff = new Boolean(true);
    	}
    	if (mlbtotalonoff == null) {
    		mlbtotalonoff = new Boolean(true);
    	}
    	if (mlbmlonoff == null) {
    		mlbmlonoff = new Boolean(true);
    	}
    	if (internationalbaseballspreadonoff == null) {
    		internationalbaseballspreadonoff = new Boolean(true);
    	}
    	if (internationalbaseballtotalonoff == null) {
    		internationalbaseballtotalonoff = new Boolean(true);
    	}
    	if (internationalbaseballmlonoff == null) {
    		internationalbaseballmlonoff = new Boolean(true);
    	}
    	if (this.onoff == null) {
    		this.onoff = new Boolean(true);
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
	 * @return the scrappername
	 */
	public String getScrappername() {
		return scrappername;
	}

	/**
	 * @param scrappername the scrappername to set
	 */
	public void setScrappername(String scrappername) {
		this.scrappername = scrappername;
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
	 * @return the spreadlineadjustment
	 */
	public String getSpreadlineadjustment() {
		return spreadlineadjustment;
	}

	/**
	 * @param spreadlineadjustment the spreadlineadjustment to set
	 */
	public void setSpreadlineadjustment(String spreadlineadjustment) {
		this.spreadlineadjustment = spreadlineadjustment;
	}

	/**
	 * @return the spreadjuiceindicator
	 */
	public String getSpreadjuiceindicator() {
		return spreadjuiceindicator;
	}

	/**
	 * @param spreadjuiceindicator the spreadjuiceindicator to set
	 */
	public void setSpreadjuiceindicator(String spreadjuiceindicator) {
		this.spreadjuiceindicator = spreadjuiceindicator;
	}

	/**
	 * @return the spreadjuice
	 */
	public String getSpreadjuice() {
		return spreadjuice;
	}

	/**
	 * @param spreadjuice the spreadjuice to set
	 */
	public void setSpreadjuice(String spreadjuice) {
		this.spreadjuice = spreadjuice;
	}

	/**
	 * @return the spreadjuiceadjustment
	 */
	public String getSpreadjuiceadjustment() {
		return spreadjuiceadjustment;
	}

	/**
	 * @param spreadjuiceadjustment the spreadjuiceadjustment to set
	 */
	public void setSpreadjuiceadjustment(String spreadjuiceadjustment) {
		this.spreadjuiceadjustment = spreadjuiceadjustment;
	}

	/**
	 * @return the spreadmaxamount
	 */
	public String getSpreadmaxamount() {
		return spreadmaxamount;
	}

	/**
	 * @param spreadmaxamount the spreadmaxamount to set
	 */
	public void setSpreadmaxamount(String spreadmaxamount) {
		this.spreadmaxamount = spreadmaxamount;
	}

	/**
	 * @return the totallineadjustment
	 */
	public String getTotallineadjustment() {
		return totallineadjustment;
	}

	/**
	 * @param totallineadjustment the totallineadjustment to set
	 */
	public void setTotallineadjustment(String totallineadjustment) {
		this.totallineadjustment = totallineadjustment;
	}

	/**
	 * @return the totaljuiceindicator
	 */
	public String getTotaljuiceindicator() {
		return totaljuiceindicator;
	}

	/**
	 * @param totaljuiceindicator the totaljuiceindicator to set
	 */
	public void setTotaljuiceindicator(String totaljuiceindicator) {
		this.totaljuiceindicator = totaljuiceindicator;
	}

	/**
	 * @return the totaljuice
	 */
	public String getTotaljuice() {
		return totaljuice;
	}

	/**
	 * @param totaljuice the totaljuice to set
	 */
	public void setTotaljuice(String totaljuice) {
		this.totaljuice = totaljuice;
	}

	/**
	 * @return the totaljuiceadjustment
	 */
	public String getTotaljuiceadjustment() {
		return totaljuiceadjustment;
	}

	/**
	 * @param totaljuiceadjustment the totaljuiceadjustment to set
	 */
	public void setTotaljuiceadjustment(String totaljuiceadjustment) {
		this.totaljuiceadjustment = totaljuiceadjustment;
	}

	/**
	 * @return the totalmaxamount
	 */
	public String getTotalmaxamount() {
		return totalmaxamount;
	}

	/**
	 * @param totalmaxamount the totalmaxamount to set
	 */
	public void setTotalmaxamount(String totalmaxamount) {
		this.totalmaxamount = totalmaxamount;
	}

	/**
	 * @return the mlindicator
	 */
	public String getMlindicator() {
		return mlindicator;
	}

	/**
	 * @param mlindicator the mlindicator to set
	 */
	public void setMlindicator(String mlindicator) {
		this.mlindicator = mlindicator;
	}

	/**
	 * @return the mlline
	 */
	public String getMlline() {
		return mlline;
	}

	/**
	 * @param mlline the mlline to set
	 */
	public void setMlline(String mlline) {
		this.mlline = mlline;
	}

	/**
	 * @return the mllineadjustment
	 */
	public String getMllineadjustment() {
		return mllineadjustment;
	}

	/**
	 * @param mllineadjustment the mllineadjustment to set
	 */
	public void setMllineadjustment(String mllineadjustment) {
		this.mllineadjustment = mllineadjustment;
	}

	/**
	 * @return the mlmaxamount
	 */
	public String getMlmaxamount() {
		return mlmaxamount;
	}

	/**
	 * @param mlmaxamount the mlmaxamount to set
	 */
	public void setMlmaxamount(String mlmaxamount) {
		this.mlmaxamount = mlmaxamount;
	}

	/**
	 * @return the telegramnumber
	 */
	public String getTelegramnumber() {
		return telegramnumber;
	}

	/**
	 * @return the servernumber
	 */
	public Integer getServernumber() {
		return servernumber;
	}

	/**
	 * @param servernumber the servernumber to set
	 */
	public void setServernumber(Integer servernumber) {
		this.servernumber = servernumber;
	}

	/**
	 * @param telegramnumber the telegramnumber to set
	 */
	public void setTelegramnumber(String telegramnumber) {
		this.telegramnumber = telegramnumber;
	}

	/**
	 * @return the pullinginterval
	 */
	public String getPullinginterval() {
		return pullinginterval;
	}

	/**
	 * @param pullinginterval the pullinginterval to set
	 */
	public void setPullinginterval(String pullinginterval) {
		this.pullinginterval = pullinginterval;
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
	 * @return the mobiletext
	 */
	public String getMobiletext() {
		return mobiletext;
	}

	/**
	 * @param mobiletext the mobiletext to set
	 */
	public void setMobiletext(String mobiletext) {
		this.mobiletext = mobiletext;
	}

	/**
	 * @return the enableretry
	 */
	public Boolean getEnableretry() {
		return enableretry;
	}

	/**
	 * @param enableretry the enableretry to set
	 */
	public void setEnableretry(Boolean enableretry) {
		this.enableretry = enableretry;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getFullfill() {
		return fullfill;
	}

	/**
	 * 
	 * @param fullfill
	 */
	public void setFullfill(Boolean fullfill) {
		this.fullfill = fullfill;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getOrderamount() {
		return orderamount;
	}

	/**
	 * 
	 * @param orderamount
	 */
	public void setOrderamount(Integer orderamount) {
		this.orderamount = orderamount;
	}

	/**
	 * @return the middlerules
	 */
	public Boolean getMiddlerules() {
		return middlerules;
	}

	/**
	 * @param middlerules the middlerules to set
	 */
	public void setMiddlerules(Boolean middlerules) {
		this.middlerules = middlerules;
	}

	/**
	 * @return the checkdupgame
	 */
	public Boolean getCheckdupgame() {
		return checkdupgame;
	}

	/**
	 * @param checkdupgame the checkdupgame to set
	 */
	public void setCheckdupgame(Boolean checkdupgame) {
		this.checkdupgame = checkdupgame;
	}

	/**
	 * @return the playotherside
	 */
	public Boolean getPlayotherside() {
		return playotherside;
	}

	/**
	 * @param playotherside the playotherside to set
	 */
	public void setPlayotherside(Boolean playotherside) {
		this.playotherside = playotherside;
	}

	/**
	 * @return the bestprice
	 */
	public Boolean getBestprice() {
		return bestprice;
	}

	/**
	 * @param bestprice the bestprice to set
	 */
	public void setBestprice(Boolean bestprice) {
		this.bestprice = bestprice;
	}

	/**
	 * @return the gameonoff
	 */
	public Boolean getGameonoff() {
		return gameonoff;
	}

	/**
	 * @param gameonoff the gameonoff to set
	 */
	public void setGameonoff(Boolean gameonoff) {
		this.gameonoff = gameonoff;
	}

	/**
	 * @return the firstonoff
	 */
	public Boolean getFirstonoff() {
		return firstonoff;
	}

	/**
	 * @param firstonoff the firstonoff to set
	 */
	public void setFirstonoff(Boolean firstonoff) {
		this.firstonoff = firstonoff;
	}

	/**
	 * @return the secondonoff
	 */
	public Boolean getSecondonoff() {
		return secondonoff;
	}

	/**
	 * @param secondonoff the secondonoff to set
	 */
	public void setSecondonoff(Boolean secondonoff) {
		this.secondonoff = secondonoff;
	}

	/**
	 * @return the thirdonoff
	 */
	public Boolean getThirdonoff() {
		return thirdonoff;
	}

	/**
	 * @param thirdonoff the thirdonoff to set
	 */
	public void setThirdonoff(Boolean thirdonoff) {
		this.thirdonoff = thirdonoff;
	}

	/**
	 * @return the nflspreadonoff
	 */
	public Boolean getNflspreadonoff() {
		return nflspreadonoff;
	}

	/**
	 * @param nflspreadonoff the nflspreadonoff to set
	 */
	public void setNflspreadonoff(Boolean nflspreadonoff) {
		this.nflspreadonoff = nflspreadonoff;
	}

	/**
	 * @return the nfltotalonoff
	 */
	public Boolean getNfltotalonoff() {
		return nfltotalonoff;
	}

	/**
	 * @param nfltotalonoff the nfltotalonoff to set
	 */
	public void setNfltotalonoff(Boolean nfltotalonoff) {
		this.nfltotalonoff = nfltotalonoff;
	}

	/**
	 * @return the nflmlonoff
	 */
	public Boolean getNflmlonoff() {
		return nflmlonoff;
	}

	/**
	 * @param nflmlonoff the nflmlonoff to set
	 */
	public void setNflmlonoff(Boolean nflmlonoff) {
		this.nflmlonoff = nflmlonoff;
	}

	/**
	 * @return the ncaafspreadonoff
	 */
	public Boolean getNcaafspreadonoff() {
		return ncaafspreadonoff;
	}

	/**
	 * @param ncaafspreadonoff the ncaafspreadonoff to set
	 */
	public void setNcaafspreadonoff(Boolean ncaafspreadonoff) {
		this.ncaafspreadonoff = ncaafspreadonoff;
	}

	/**
	 * @return the ncaaftotalonoff
	 */
	public Boolean getNcaaftotalonoff() {
		return ncaaftotalonoff;
	}

	/**
	 * @param ncaaftotalonoff the ncaaftotalonoff to set
	 */
	public void setNcaaftotalonoff(Boolean ncaaftotalonoff) {
		this.ncaaftotalonoff = ncaaftotalonoff;
	}

	/**
	 * @return the ncaafmlonoff
	 */
	public Boolean getNcaafmlonoff() {
		return ncaafmlonoff;
	}

	/**
	 * @param ncaafmlonoff the ncaafmlonoff to set
	 */
	public void setNcaafmlonoff(Boolean ncaafmlonoff) {
		this.ncaafmlonoff = ncaafmlonoff;
	}

	/**
	 * @return the nbaspreadonoff
	 */
	public Boolean getNbaspreadonoff() {
		return nbaspreadonoff;
	}

	/**
	 * @param nbaspreadonoff the nbaspreadonoff to set
	 */
	public void setNbaspreadonoff(Boolean nbaspreadonoff) {
		this.nbaspreadonoff = nbaspreadonoff;
	}

	/**
	 * @return the nbatotalonoff
	 */
	public Boolean getNbatotalonoff() {
		return nbatotalonoff;
	}

	/**
	 * @param nbatotalonoff the nbatotalonoff to set
	 */
	public void setNbatotalonoff(Boolean nbatotalonoff) {
		this.nbatotalonoff = nbatotalonoff;
	}

	/**
	 * @return the nbamlonoff
	 */
	public Boolean getNbamlonoff() {
		return nbamlonoff;
	}

	/**
	 * @param nbamlonoff the nbamlonoff to set
	 */
	public void setNbamlonoff(Boolean nbamlonoff) {
		this.nbamlonoff = nbamlonoff;
	}

	/**
	 * @return the ncaabspreadonoff
	 */
	public Boolean getNcaabspreadonoff() {
		return ncaabspreadonoff;
	}

	/**
	 * @param ncaabspreadonoff the ncaabspreadonoff to set
	 */
	public void setNcaabspreadonoff(Boolean ncaabspreadonoff) {
		this.ncaabspreadonoff = ncaabspreadonoff;
	}

	/**
	 * @return the ncaabtotalonoff
	 */
	public Boolean getNcaabtotalonoff() {
		return ncaabtotalonoff;
	}

	/**
	 * @param ncaabtotalonoff the ncaabtotalonoff to set
	 */
	public void setNcaabtotalonoff(Boolean ncaabtotalonoff) {
		this.ncaabtotalonoff = ncaabtotalonoff;
	}

	/**
	 * @return the ncaabmlonoff
	 */
	public Boolean getNcaabmlonoff() {
		return ncaabmlonoff;
	}

	/**
	 * @param ncaabmlonoff the ncaabmlonoff to set
	 */
	public void setNcaabmlonoff(Boolean ncaabmlonoff) {
		this.ncaabmlonoff = ncaabmlonoff;
	}

	/**
	 * @return the wnbaspreadonoff
	 */
	public Boolean getWnbaspreadonoff() {
		return wnbaspreadonoff;
	}

	/**
	 * @param wnbaspreadonoff the wnbaspreadonoff to set
	 */
	public void setWnbaspreadonoff(Boolean wnbaspreadonoff) {
		this.wnbaspreadonoff = wnbaspreadonoff;
	}

	/**
	 * @return the wnbatotalonoff
	 */
	public Boolean getWnbatotalonoff() {
		return wnbatotalonoff;
	}

	/**
	 * @param wnbatotalonoff the wnbatotalonoff to set
	 */
	public void setWnbatotalonoff(Boolean wnbatotalonoff) {
		this.wnbatotalonoff = wnbatotalonoff;
	}

	/**
	 * @return the wnbamlonoff
	 */
	public Boolean getWnbamlonoff() {
		return wnbamlonoff;
	}

	/**
	 * @param wnbamlonoff the wnbamlonoff to set
	 */
	public void setWnbamlonoff(Boolean wnbamlonoff) {
		this.wnbamlonoff = wnbamlonoff;
	}

	/**
	 * @return the nhlspreadonoff
	 */
	public Boolean getNhlspreadonoff() {
		return nhlspreadonoff;
	}

	/**
	 * @param nhlspreadonoff the nhlspreadonoff to set
	 */
	public void setNhlspreadonoff(Boolean nhlspreadonoff) {
		this.nhlspreadonoff = nhlspreadonoff;
	}

	/**
	 * @return the nhltotalonoff
	 */
	public Boolean getNhltotalonoff() {
		return nhltotalonoff;
	}

	/**
	 * @param nhltotalonoff the nhltotalonoff to set
	 */
	public void setNhltotalonoff(Boolean nhltotalonoff) {
		this.nhltotalonoff = nhltotalonoff;
	}

	/**
	 * @return the nhlmlonoff
	 */
	public Boolean getNhlmlonoff() {
		return nhlmlonoff;
	}

	/**
	 * @param nhlmlonoff the nhlmlonoff to set
	 */
	public void setNhlmlonoff(Boolean nhlmlonoff) {
		this.nhlmlonoff = nhlmlonoff;
	}

	/**
	 * @return the mlbspreadonoff
	 */
	public Boolean getMlbspreadonoff() {
		return mlbspreadonoff;
	}

	/**
	 * @param mlbspreadonoff the mlbspreadonoff to set
	 */
	public void setMlbspreadonoff(Boolean mlbspreadonoff) {
		this.mlbspreadonoff = mlbspreadonoff;
	}

	/**
	 * @return the mlbtotalonoff
	 */
	public Boolean getMlbtotalonoff() {
		return mlbtotalonoff;
	}

	/**
	 * @param mlbtotalonoff the mlbtotalonoff to set
	 */
	public void setMlbtotalonoff(Boolean mlbtotalonoff) {
		this.mlbtotalonoff = mlbtotalonoff;
	}

	/**
	 * @return the mlbmlonoff
	 */
	public Boolean getMlbmlonoff() {
		return mlbmlonoff;
	}

	/**
	 * @param mlbmlonoff the mlbmlonoff to set
	 */
	public void setMlbmlonoff(Boolean mlbmlonoff) {
		this.mlbmlonoff = mlbmlonoff;
	}

	/**
	 * @return the internationalbaseballspreadonoff
	 */
	public Boolean getInternationalbaseballspreadonoff() {
		return internationalbaseballspreadonoff;
	}

	/**
	 * @param internationalbaseballspreadonoff the internationalbaseballspreadonoff to set
	 */
	public void setInternationalbaseballspreadonoff(Boolean internationalbaseballspreadonoff) {
		this.internationalbaseballspreadonoff = internationalbaseballspreadonoff;
	}

	/**
	 * @return the internationalbaseballtotalonoff
	 */
	public Boolean getInternationalbaseballtotalonoff() {
		return internationalbaseballtotalonoff;
	}

	/**
	 * @param internationalbaseballtotalonoff the internationalbaseballtotalonoff to set
	 */
	public void setInternationalbaseballtotalonoff(Boolean internationalbaseballtotalonoff) {
		this.internationalbaseballtotalonoff = internationalbaseballtotalonoff;
	}

	/**
	 * @return the internationalbaseballmlonoff
	 */
	public Boolean getInternationalbaseballmlonoff() {
		return internationalbaseballmlonoff;
	}

	/**
	 * @param internationalbaseballmlonoff the internationalbaseballmlonoff to set
	 */
	public void setInternationalbaseballmlonoff(Boolean internationalbaseballmlonoff) {
		this.internationalbaseballmlonoff = internationalbaseballmlonoff;
	}

	/**
	 * @return the onoff
	 */
	public Boolean getOnoff() {
		return onoff;
	}

	/**
	 * @param onoff the onoff to set
	 */
	public void setOnoff(Boolean onoff) {
		this.onoff = onoff;
	}

	/**
	 * @return the sendtextforaccount
	 */
	public Boolean getSendtextforaccount() {
		return sendtextforaccount;
	}

	/**
	 * @param sendtextforaccount the sendtextforaccount to set
	 */
	public void setSendtextforaccount(Boolean sendtextforaccount) {
		this.sendtextforaccount = sendtextforaccount;
	}

	/**
	 * @return the spreadsourceamount
	 */
	public Float getSpreadsourceamount() {
		return spreadsourceamount;
	}

	/**
	 * @param spreadsourceamount the spreadsourceamount to set
	 */
	public void setSpreadsourceamount(Float spreadsourceamount) {
		this.spreadsourceamount = spreadsourceamount;
	}

	/**
	 * @return the totalsourceamount
	 */
	public Float getTotalsourceamount() {
		return totalsourceamount;
	}

	/**
	 * @param totalsourceamount the totalsourceamount to set
	 */
	public void setTotalsourceamount(Float totalsourceamount) {
		this.totalsourceamount = totalsourceamount;
	}

	/**
	 * @return the mlsourceamount
	 */
	public Float getMlsourceamount() {
		return mlsourceamount;
	}

	/**
	 * @param mlsourceamount the mlsourceamount to set
	 */
	public void setMlsourceamount(Float mlsourceamount) {
		this.mlsourceamount = mlsourceamount;
	}

	/**
	 * @return the keynumber
	 */
	public Boolean getKeynumber() {
		return keynumber;
	}

	/**
	 * @param keynumber the keynumber to set
	 */
	public void setKeynumber(Boolean keynumber) {
		this.keynumber = keynumber;
	}

	/**
	 * @return the humanspeed
	 */
	public Boolean getHumanspeed() {
		return humanspeed;
	}

	/**
	 * @param humanspeed the humanspeed to set
	 */
	public void setHumanspeed(Boolean humanspeed) {
		this.humanspeed = humanspeed;
	}

	/**
	 * @return the unitsenabled
	 */
	public Boolean getUnitsenabled() {
		return unitsenabled;
	}

	/**
	 * @param unitsenabled the unitsenabled to set
	 */
	public void setUnitsenabled(Boolean unitsenabled) {
		this.unitsenabled = unitsenabled;
	}

	/**
	 * @return the spreadunit
	 */
	public Double getSpreadunit() {
		return spreadunit;
	}

	/**
	 * @param spreadunit the spreadunit to set
	 */
	public void setSpreadunit(Double spreadunit) {
		this.spreadunit = spreadunit;
	}

	/**
	 * @return the totalunit
	 */
	public Double getTotalunit() {
		return totalunit;
	}

	/**
	 * @param totalunit the totalunit to set
	 */
	public void setTotalunit(Double totalunit) {
		this.totalunit = totalunit;
	}

	/**
	 * @return the mlunit
	 */
	public Double getMlunit() {
		return mlunit;
	}

	/**
	 * @param mlunit the mlunit to set
	 */
	public void setMlunit(Double mlunit) {
		this.mlunit = mlunit;
	}

	/**
	 * @return the leanssenabled
	 */
	public Boolean getLeanssenabled() {
		return leanssenabled;
	}

	/**
	 * @param leanssenabled the leanssenabled to set
	 */
	public void setLeanssenabled(Boolean leanssenabled) {
		this.leanssenabled = leanssenabled;
	}

	/**
	 * @return the spreadlean
	 */
	public Double getSpreadlean() {
		return spreadlean;
	}

	/**
	 * @param spreadlean the spreadlean to set
	 */
	public void setSpreadlean(Double spreadlean) {
		this.spreadlean = spreadlean;
	}

	/**
	 * @return the totallean
	 */
	public Double getTotallean() {
		return totallean;
	}

	/**
	 * @param totallean the totallean to set
	 */
	public void setTotallean(Double totallean) {
		this.totallean = totallean;
	}

	/**
	 * @return the mllean
	 */
	public Double getMllean() {
		return mllean;
	}

	/**
	 * @param mllean the mllean to set
	 */
	public void setMllean(Double mllean) {
		this.mllean = mllean;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(BaseScrapper o) {
	    if (getUserid() == null || o.getUserid() == null)
	        return 0;
	    return getUserid().compareTo(o.getUserid());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseScrapper [id=" + id + ", scrappername=" + scrappername + ", userid=" + userid
				+ ", spreadlineadjustment=" + spreadlineadjustment + ", spreadjuiceindicator=" + spreadjuiceindicator
				+ ", spreadjuice=" + spreadjuice + ", spreadjuiceadjustment=" + spreadjuiceadjustment
				+ ", spreadmaxamount=" + spreadmaxamount + ", spreadsourceamount=" + spreadsourceamount
				+ ", totallineadjustment=" + totallineadjustment + ", totaljuiceindicator=" + totaljuiceindicator
				+ ", totaljuice=" + totaljuice + ", totaljuiceadjustment=" + totaljuiceadjustment + ", totalmaxamount="
				+ totalmaxamount + ", totalsourceamount=" + totalsourceamount + ", mlindicator=" + mlindicator
				+ ", mlline=" + mlline + ", mllineadjustment=" + mllineadjustment + ", mlmaxamount=" + mlmaxamount
				+ ", mlsourceamount=" + mlsourceamount + ", pullinginterval=" + pullinginterval + ", mobiletext="
				+ mobiletext + ", telegramnumber=" + telegramnumber + ", servernumber=" + servernumber
				+ ", enableretry=" + enableretry + ", fullfill=" + fullfill + ", orderamount=" + orderamount
				+ ", middlerules=" + middlerules + ", checkdupgame=" + checkdupgame + ", playotherside=" + playotherside
				+ ", bestprice=" + bestprice + ", sendtextforaccount=" + sendtextforaccount + ", onoff=" + onoff
				+ ", gameonoff=" + gameonoff + ", firstonoff=" + firstonoff + ", secondonoff=" + secondonoff
				+ ", thirdonoff=" + thirdonoff + ", nflspreadonoff=" + nflspreadonoff + ", nfltotalonoff="
				+ nfltotalonoff + ", nflmlonoff=" + nflmlonoff + ", ncaafspreadonoff=" + ncaafspreadonoff
				+ ", ncaaftotalonoff=" + ncaaftotalonoff + ", ncaafmlonoff=" + ncaafmlonoff + ", nbaspreadonoff="
				+ nbaspreadonoff + ", nbatotalonoff=" + nbatotalonoff + ", nbamlonoff=" + nbamlonoff
				+ ", ncaabspreadonoff=" + ncaabspreadonoff + ", ncaabtotalonoff=" + ncaabtotalonoff + ", ncaabmlonoff="
				+ ncaabmlonoff + ", wnbaspreadonoff=" + wnbaspreadonoff + ", wnbatotalonoff=" + wnbatotalonoff
				+ ", wnbamlonoff=" + wnbamlonoff + ", nhlspreadonoff=" + nhlspreadonoff + ", nhltotalonoff="
				+ nhltotalonoff + ", nhlmlonoff=" + nhlmlonoff + ", mlbspreadonoff=" + mlbspreadonoff
				+ ", mlbtotalonoff=" + mlbtotalonoff + ", mlbmlonoff=" + mlbmlonoff
				+ ", internationalbaseballspreadonoff=" + internationalbaseballspreadonoff
				+ ", internationalbaseballtotalonoff=" + internationalbaseballtotalonoff
				+ ", internationalbaseballmlonoff=" + internationalbaseballmlonoff + ", keynumber=" + keynumber
				+ ", humanspeed=" + humanspeed + ", unitsenabled=" + unitsenabled + ", spreadunit=" + spreadunit
				+ ", totalunit=" + totalunit + ", mlunit=" + mlunit + ", leanssenabled=" + leanssenabled
				+ ", spreadlean=" + spreadlean + ", totallean=" + totallean + ", mllean=" + mllean + ", datecreated="
				+ datecreated + ", datemodified=" + datemodified + "]";
	}
}
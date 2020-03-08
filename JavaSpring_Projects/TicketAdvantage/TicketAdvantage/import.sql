DROP TABLE public.accountevent;
DROP TABLE public.accountsta;
DROP TABLE public.groupaccounts;
DROP TABLE public.groupsaccounts;
DROP TABLE public.groupsta;
DROP TABLE public.hibernate_sequences;
DROP TABLE public.mlrecordevents;
DROP TABLE public.spreadrecordevents;
DROP TABLE public.totalrecordevents;
DROP TABLE public.usersaccounts;
DROP TABLE public.usersgroups;
DROP TABLE public.usersta;
DROP TABLE public.pendingevents;
DROP TABLE public.userbilling;

DROP SEQUENCE public.accountevent_id_seq;
DROP SEQUENCE public.accountsta_id_seq;
DROP SEQUENCE public.groupssta_id_seq;
DROP SEQUENCE public.mlrecordevents_id_seq;
DROP SEQUENCE public.spreadrecordevents_id_seq;
DROP SEQUENCE public.totalrecordevents_id_seq;
DROP SEQUENCE public.scrapper_id_seq;
DROP SEQUENCE public.usersta_id_seq;
DROP SEQUENCE public.pendingevents_seq;
DROP SEQUENCE public.userbilling_id_seq;
  
create table public.accountevent (id serial not null, accountconfirmation varchar(4000), accounthtml TEXT, accountid bigint not null, amount varchar(20), actualamount varchar(20), attempts integer not null, attempttime timestamp with time zone, currentattempts integer, datecreated timestamp with time zone not null, datemodified timestamp with time zone not null, errorcode integer, errorexception varchar(4000), errormessage varchar(4000), eventdatetime timestamp with time zone not null, eventid integer, eventname varchar(100) not null, groupid bigint not null, iscompleted boolean, maxspreadamount integer, maxtotalamount integer, maxmlamount integer, mlindicator varchar(10), timezone varchar(100), ml float, mlid bigint, mljuice float, name varchar(100) not null, ownerpercentage integer, partnerpercentage integer, proxy varchar(255), sport varchar(100) not null, spreadindicator varchar(10), spread float, spreadid bigint, spreadjuice float, total float, totalid bigint, totalindicator varchar(10), totaljuice float, type varchar(100) not null, userid bigint not null, wagertype varchar(10), status varchar(20), riskamount varchar(20), towinamount varchar(20), accesstoken character varying(200), primary key (id))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.accountevent
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.accountevent TO public;
CREATE INDEX accountevent_accountid_idx ON public.accountevent (accountid);
CREATE INDEX accountevent_datecreated_idx ON public.accountevent (datecreated);
CREATE INDEX accountevent_userid_idx ON public.accountevent (userid);

ALTER TABLE public.accountevent ADD COLUMN accesstoken character varying(200);


create table public.accountsta (id serial not null, datecreated timestamp with time zone not null, datemodified timestamp with time zone not null, isactive boolean not null, spreadlimitamount integer, totallimitamount integer, mllimitamount integer, name varchar(100) not null, timezone varchar(100), ownerpercentage integer, partnerpercentage integer, password varchar(100), proxylocation varchar(200), sitetype varchar(255), url varchar(200) not null, 
username varchar(100),
ismobile boolean, 
showrequestresponse boolean, 
primary key (id))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.accountsta
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.accountsta TO public;
CREATE INDEX accountsta_isactive_idx ON public.accountsta (isactive);
CREATE INDEX accountsta_name_idx ON public.accountsta (name);

create table groupaccounts (groupid bigint not null, accountid bigint not null, primary key (groupid, accountid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.groupaccounts
  OWNER TO ticketadvantage;
GRANT SELECT(groupid), UPDATE(groupid), INSERT(groupid), REFERENCES(groupid) ON public.groupaccounts TO public;

create table groupsaccounts (groupsid bigint not null, accountsid bigint not null, primary key (groupsid, accountsid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.groupsaccounts
  OWNER TO ticketadvantage;
GRANT SELECT(groupsid), UPDATE(groupsid), INSERT(groupsid), REFERENCES(groupsid) ON public.groupsaccounts TO public;

create table groupsta (id serial not null, datecreated timestamp with time zone not null, datemodified timestamp with time zone not null, isactive boolean not null, name varchar(100) not null, primary key (id))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.groupsta
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.groupsta TO public;
CREATE INDEX groupsta_isactive_idx ON public.groupsta (isactive);
CREATE INDEX groupsta_name_idx ON public.groupsta (name);

create table pendingevents (id serial not null, 
	userid bigint not null, 
	ticketnum varchar(20) not null, 
	eventtype varchar(10), 
	linetype varchar(20), 
	eventdate varchar(80),
	gamesport varchar(80), 
	gametype varchar(80), 
	team varchar(100), 
	rotationid varchar(10), 
	line varchar(20), 
	lineplusminus varchar(10), 
	juice varchar(10), 
	juiceplusminus varchar(10), 
	risk varchar(20), 
	win varchar(20), 
	pendingtype varchar(10), 
	accountname varchar(200), 
	accountid varchar(100),
	doposturl boolean, 
	posturl varchar(255), 
	customerid varchar(100), 
	inet varchar(100), 
	dateaccepted varchar(50), 
	datecreated timestamp with time zone, 
	datemodified timestamp with time zone, 
	primary key (id))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.pendingevents
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.pendingevents TO public;
CREATE INDEX pendingevents_datecreated_idx ON public.pendingevents (datecreated);
CREATE INDEX pendingevents_ticketnum_idx ON public.pendingevents (ticketnum);
CREATE INDEX pendingevents_userid_idx ON public.pendingevents (userid);
CREATE INDEX pendingevents_pendingtype_idx ON public.pendingevents (pendingtype);

ALTER TABLE public.pendingevents ADD COLUMN customerid character varying(200);
ALTER TABLE public.pendingevents ADD COLUMN inet character varying(200);

create table emailevents (id serial not null,
	foundgame boolean,
	fromemail varchar(100),
	toemail varchar(100),
	subject varchar(1000),
	body varchar(4000),
	eventtype varchar(10), 
	linetype varchar(20), 
	eventdate varchar(80),
	gamesport varchar(80), 
	gametype varchar(80),
	team varchar(200),
	team1 varchar(200),
	team2 varchar(200),
	rotationid varchar(10),
	rotationid1 varchar(10),
	rotationid2 varchar(10),
	line varchar(20), 
	lineplusminus varchar(10), 
	juice varchar(10), 
	juiceplusminus varchar(10), 
	risk varchar(20), 
	win varchar(20), 
	pendingtype varchar(10), 
	emailname varchar(100), 
	datesent timestamp with time zone, 
	datecreated timestamp with time zone, 
	datemodified timestamp with time zone, 
	primary key (id))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailevents
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.emailevents TO public;
CREATE INDEX emailevents_datecreated_idx ON public.emailevents (datecreated);
CREATE INDEX emailevents_subject_idx ON public.emailevents (subject);
CREATE INDEX emailevents_userid_idx ON public.emailevents (userid);
CREATE INDEX emailevents_pendingtype_idx ON public.emailevents (pendingtype);

create table emailevent (id serial not null,
	messagenum integer,
	emailname varchar(100),
	fromemail varchar(400),
	toemail varchar(400),
	ccemail varchar(400),
	bccemail varchar(400),
	subject varchar(400),
	bodytext varchar(4000),
	bodyhtml varchar(4000),
	inet varchar(50),
	datesent timestamp with time zone,
	datereceived timestamp with time zone,
	datecreated timestamp with time zone, 
	datemodified timestamp with time zone, 
	primary key (id))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailevent
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.emailevent TO public;
CREATE INDEX emailevent_messagenum_idx ON public.emailevent (messagenum);
CREATE INDEX emailevent_datecreated_idx ON public.emailevent (datecreated);
CREATE INDEX emailevent_subject_idx ON public.emailevent (subject);


create table mlrecordevents (id serial not null, accountid bigint, amount varchar(10), attempts integer not null, attempttime timestamp with time zone, currentattempts integer, datecreated timestamp with time zone not null, datemodified timestamp with time zone, datentime timestamp with time zone not null, eventdatetime timestamp with time zone not null, eventid integer not null, eventid1 integer, eventid2 integer, eventname varchar(100), eventteam1 varchar(100), eventteam2 varchar(100), eventtype varchar(100) not null, groupid bigint, iscompleted boolean not null, sport varchar(100) not null, userid bigint not null, wtype varchar(10), mlinputfirstone varchar(255), mlinputfirsttwo varchar(255), mlinputsecondone varchar(255), mlinputsecondtwo varchar(255), mlplusminusfirstone varchar(255), mlplusminusfirsttwo varchar(255), mlplusminussecondone varchar(255), 
	mlplusminussecondtwo varchar(255),
	scrappername varchar(100),
	actiontype varchar(10),
	textnumber varchar(40),
	primary key (id))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.mlrecordevents
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.mlrecordevents TO public;
CREATE INDEX mlrecordevents_datecreated_idx ON public.mlrecordevents (datecreated);
CREATE INDEX mlrecordevents_datentime_idx ON public.mlrecordevents (datentime);
CREATE INDEX mlrecordevents_eventdatetime_idx ON public.mlrecordevents (eventdatetime);
CREATE INDEX mlrecordevents_eventid_idx ON public.mlrecordevents (eventid);
CREATE INDEX mlrecordevents_userid_idx ON public.mlrecordevents (userid);

ALTER TABLE public.mlrecordevents ADD COLUMN scrappername varchar(100);
ALTER TABLE public.mlrecordevents ADD COLUMN actiontype varchar(10);
ALTER TABLE public.mlrecordevents ADD COLUMN textnumber varchar(40);

create table spreadrecordevents (id serial not null, accountid bigint, amount varchar(10), attempts integer not null, attempttime timestamp with time zone, currentattempts integer, datecreated timestamp with time zone not null, datemodified timestamp with time zone, datentime timestamp with time zone not null, eventdatetime timestamp with time zone not null, eventid integer not null, eventid1 integer, eventid2 integer, eventname varchar(100), eventteam1 varchar(100), eventteam2 varchar(100), eventtype varchar(100) not null, groupid bigint, iscompleted boolean not null, sport varchar(100) not null, userid bigint not null, wtype varchar(10), spreadinputfirstone varchar(255), spreadinputfirsttwo varchar(255), spreadinputjuicefirstone varchar(255), spreadinputjuicefirsttwo varchar(255), spreadinputjuicesecondone varchar(255), spreadinputjuicesecondtwo varchar(255), spreadinputsecondone varchar(255), spreadinputsecondtwo varchar(255), spreadjuiceplusminusfirstone varchar(255), spreadjuiceplusminusfirsttwo varchar(255), spreadjuiceplusminussecondone varchar(255), spreadjuiceplusminussecondtwo varchar(255), spreadplusminusfirstone varchar(255), spreadplusminusfirsttwo varchar(255), spreadplusminussecondone varchar(255), 
	spreadplusminussecondtwo varchar(255), 
	scrappername varchar(100),
	actiontype varchar(10),
	textnumber varchar(40),
	primary key (id, datecreated, datentime, eventdatetime, eventid, userid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.spreadrecordevents
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.spreadrecordevents TO public;
CREATE INDEX spreadrecordevents_datecreated_idx ON public.spreadrecordevents (datecreated);
CREATE INDEX spreadrecordevents_datentime_idx ON public.spreadrecordevents (datentime);
CREATE INDEX spreadrecordevents_eventdatetime_idx ON public.spreadrecordevents (eventdatetime);
CREATE INDEX spreadrecordevents_eventid_idx ON public.spreadrecordevents (eventid);
CREATE INDEX spreadrecordevents_userid_idx ON public.spreadrecordevents (userid);

ALTER TABLE public.spreadrecordevents ADD COLUMN scrappername varchar(100);
ALTER TABLE public.spreadrecordevents ADD COLUMN actiontype varchar(10);
ALTER TABLE public.spreadrecordevents ADD COLUMN textnumber varchar(40);

create table totalrecordevents (id serial not null, accountid bigint, amount varchar(10), attempts integer not null, attempttime timestamp with time zone, currentattempts integer, datecreated timestamp with time zone not null, datemodified timestamp with time zone, datentime timestamp with time zone not null, eventdatetime timestamp with time zone not null, eventid integer not null, eventid1 integer, eventid2 integer, eventname varchar(100), eventteam1 varchar(100), eventteam2 varchar(100), eventtype varchar(100) not null, groupid bigint, iscompleted boolean not null, sport varchar(100) not null, userid bigint not null, wtype varchar(10), totalinputfirstone varchar(255), totalinputfirsttwo varchar(255), totalinputjuicefirstone varchar(255), totalinputjuicefirsttwo varchar(255), totalinputjuicesecondone varchar(255), totalinputjuicesecondtwo varchar(255), totalinputsecondone varchar(255), totalinputsecondtwo varchar(255), totaljuiceplusminusfirstone varchar(255), totaljuiceplusminusfirsttwo varchar(255), totaljuiceplusminussecondone varchar(255), 
	totaljuiceplusminussecondtwo varchar(255), into
	scrappername varchar(100),
	actiontype varchar(10),
	textnumber varchar(40),	scrappername varchar(100),
	actiontype varchar(10),
	textnumber varchar(40),
	primary key (id, datecreated, datentime, eventdatetime, eventid, userid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.totalrecordevents
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.totalrecordevents TO public;
CREATE INDEX totalrecordevents_datecreated_idx ON public.totalrecordevents (datecreated);
CREATE INDEX totalrecordevents_datentime_idx ON public.totalrecordevents (datentime);
CREATE INDEX totalrecordevents_eventdatetime_idx ON public.totalrecordevents (eventdatetime);
CREATE INDEX totalrecordevents_eventid_idx ON public.totalrecordevents (eventid);
CREATE INDEX totalrecordevents_userid_idx ON public.totalrecordevents (userid);

ALTER TABLE public.totalrecordevents ADD COLUMN scrappername varchar(100);
ALTER TABLE public.totalrecordevents ADD COLUMN actiontype varchar(10);
ALTER TABLE public.totalrecordevents ADD COLUMN textnumber varchar(40);

create table scrapper (id bigint not null, 
	userid bigint not null, 
	scrappername varchar(100) not null, 
	spreadlineadjustment varchar(10), 
	spreadjuiceindicator varchar(10), 
	spreadjuice varchar(10), 
	spreadjuiceadjustment varchar(10), 
	spreadmaxamount varchar(10), 
	spreadonoff boolean not null, 
	totallineadjustment varchar(10), 
	totaljuiceindicator varchar(10), 
	totaljuice varchar(10), 
	totaljuiceadjustment varchar(10), 
	totalmaxamount varchar(10), 
	totalonoff boolean not null, 
	mlindicator varchar(10), 
	mlline varchar(10), 
	mllineadjustment varchar(10), 
	mlmaxamount varchar(10), 
	mlonoff boolean not null, 
	pullinginterval varchar(10), 
	telegramnumber varchar(12), 
	onoff boolean not null,
	nflonoff boolean not null,
	ncaafonoff boolean not null,
	nbaonoff boolean not null,
	ncaabonoff boolean not null,
	wnbaonoff boolean not null,
	nhlonoff boolean not null,
	mlbonoff boolean not null,
	datecreated timestamp with time zone not null, 
	datemodified timestamp with time zone not null, 
	primary key (id, scrappername, userid)) 
WITH (
  OIDS=FALSE
);
ALTER TABLE public.scrapper
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.scrapper TO public;
CREATE INDEX scrapper_userid_idx ON public.scrapper (userid);
CREATE INDEX scrapper_name_idx ON public.scrapper (scrappername);

ALTER TABLE public.scrapper ADD COLUMN nflonoff boolean;
ALTER TABLE public.scrapper ADD COLUMN ncaafonoff boolean;
ALTER TABLE public.scrapper ADD COLUMN nbaonoff boolean;
ALTER TABLE public.scrapper ADD COLUMN ncaabonoff boolean;
ALTER TABLE public.scrapper ADD COLUMN wnbaonoff boolean;
ALTER TABLE public.scrapper ADD COLUMN nhlonoff boolean;
ALTER TABLE public.scrapper ADD COLUMN mlbonoff boolean;

create table webscrapper (id bigint not null, 
	userid bigint not null, 
	scrappername varchar(100) not null, 
	spreadlineadjustment varchar(10), 
	spreadjuiceindicator varchar(10), 
	spreadjuice varchar(10), 
	spreadjuiceadjustment varchar(10), 
	spreadmaxamount varchar(10),  
	totallineadjustment varchar(10), 
	totaljuiceindicator varchar(10), 
	totaljuice varchar(10), 
	totaljuiceadjustment varchar(10), 
	totalmaxamount varchar(10),  
	mlindicator varchar(10), 
	mlline varchar(10), 
	mllineadjustment varchar(10), 
	mlmaxamount varchar(10),  
	pullinginterval varchar(10),
	mobiletext varchar(50),
	telegramnumber varchar(12),
	middlerules boolean not null,
	onoff boolean not null,
	gameonoff boolean not null, 
	firstonoff boolean not null, 
	secondonoff boolean not null, 
	thirdonoff boolean not null,
	nflspreadonoff boolean not null,
	nfltotalonoff boolean not null,
	nflmlonoff boolean not null,
	ncaafspreadonoff boolean not null,
	ncaaftotalonoff boolean not null,
	ncaafmlonoff boolean not null,
	nbaspreadonoff boolean not null,
	nbatotalonoff boolean not null,
	nbamlonoff boolean not null,
	ncaabspreadonoff boolean not null,
	ncaabtotalonoff boolean not null,
	ncaabmlonoff boolean not null,
	wnbaspreadonoff boolean not null,
	wnbatotalonoff boolean not null,
	wnbamlonoff boolean not null,
	nhlspreadonoff boolean not null,
	nhltotalonoff boolean not null,
	nhlmlonoff boolean not null,
	mlbspreadonoff boolean not null,
	mlbtotalonoff boolean not null,
	mlbmlonoff boolean not null,
	internationalbaseballspreadonoff boolean,
	internationalbaseballtotalonoff boolean,
	internationalbaseballmlonoff boolean,
	datecreated timestamp with time zone not null, 
	datemodified timestamp with time zone not null, 
	primary key (id, scrappername, userid)) 
WITH (
  OIDS=FALSE
);
ALTER TABLE public.webscrapper
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.webscrapper TO public;
CREATE INDEX webscrapper_userid_idx ON public.webscrapper (userid);
CREATE INDEX webscrapper_name_idx ON public.webscrapper (scrappername);

ALTER TABLE public.webscrapper ADD COLUMN internationalbaseballspreadonoff boolean;
ALTER TABLE public.webscrapper ADD COLUMN internationalbaseballtotalonoff boolean;
ALTER TABLE public.webscrapper ADD COLUMN internationalbaseballmlonoff boolean;

CREATE TABLE public.emailscrapper(id bigint NOT NULL,
  userid bigint NOT NULL,
  scrappername character varying(100) NOT NULL,
  spreadlineadjustment character varying(10),
  spreadjuiceindicator character varying(10),
  spreadjuice character varying(10),
  spreadjuiceadjustment character varying(10),
  spreadmaxamount character varying(10),
  totallineadjustment character varying(10),
  totaljuiceindicator character varying(10),
  totaljuice character varying(10),
  totaljuiceadjustment character varying(10),
  totalmaxamount character varying(10),
  mlindicator character varying(10),
  mlline character varying(10),
  mllineadjustment character varying(10),
  mlmaxamount character varying(10),
  pullinginterval character varying(10),
  mobiletext character varying(50),
  telegramnumber character varying(12),
  middlerules boolean NOT NULL,
  onoff boolean NOT NULL,
  gameonoff boolean NOT NULL,
  firstonoff boolean NOT NULL,
  secondonoff boolean NOT NULL,
  thirdonoff boolean NOT NULL,
  nflspreadonoff boolean NOT NULL,
  nfltotalonoff boolean NOT NULL,
  nflmlonoff boolean NOT NULL,
  ncaafspreadonoff boolean NOT NULL,
  ncaaftotalonoff boolean NOT NULL,
  ncaafmlonoff boolean NOT NULL,
  nbaspreadonoff boolean NOT NULL,
  nbatotalonoff boolean NOT NULL,
  nbamlonoff boolean NOT NULL,
  ncaabspreadonoff boolean NOT NULL,
  ncaabtotalonoff boolean NOT NULL,
  ncaabmlonoff boolean NOT NULL,
  wnbaspreadonoff boolean NOT NULL,
  wnbatotalonoff boolean NOT NULL,
  wnbamlonoff boolean NOT NULL,
  nhlspreadonoff boolean NOT NULL,
  nhltotalonoff boolean NOT NULL,
  nhlmlonoff boolean NOT NULL,
  mlbspreadonoff boolean NOT NULL,
  mlbtotalonoff boolean NOT NULL,
  mlbmlonoff boolean NOT NULL,
  internationalbaseballspreadonoff boolean,
  internationalbaseballtotalonoff boolean,
  internationalbaseballmlonoff boolean,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  CONSTRAINT emailscrapper_pkey PRIMARY KEY (id, scrappername, userid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailscrapper
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.emailscrapper TO public;

CREATE INDEX emailscrapper_name_idx ON public.emailscrapper USING btree (scrappername COLLATE pg_catalog."default");
CREATE INDEX emailscrapper_userid_idx ON public.emailscrapper USING btree (userid);

ALTER TABLE public.emailscrapper ADD COLUMN internationalbaseballspreadonoff boolean;
ALTER TABLE public.emailscrapper ADD COLUMN internationalbaseballtotalonoff boolean;
ALTER TABLE public.emailscrapper ADD COLUMN internationalbaseballmlonoff boolean;


create table emailaccountsta (id bigint not null, 
	name varchar(100) not null,
	accountid varchar(100) not null,
	inet varchar(100) not null,
	address varchar(100) not null,
	password varchar(50) not null,
	host varchar(50) not null,
	port varchar(6) not null,
	tls boolean not null,
	timezone varchar(50),
	sitetype varchar(50) not null,
	provider varchar (50) not null,
	emailtype varchar (20) not null,
	authenticationtype varchar(20),
	clientid varchar(200),
	clientsecret varchar(100),
	refreshtoken varchar(200),
	granttype varchar(50),
	isactive boolean not null,
	datecreated timestamp with time zone not null, 
	datemodified timestamp with time zone not null, 
	primary key (id, name, address)) 
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailaccountsta
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.emailaccountsta TO public;
CREATE INDEX emailaccountsta_name_idx ON public.emailaccountsta (name);
CREATE INDEX emailaccountsta_address_idx ON public.emailaccountsta (address);

create table scrappersources (scrapperid bigint not null, accountsid bigint not null, primary key (scrapperid, accountsid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.scrappersources
  OWNER TO ticketadvantage;
GRANT SELECT(scrapperid), UPDATE(scrapperid), INSERT(scrapperid), REFERENCES(scrapperid) ON public.scrappersources TO public;

create table scrapperdestinations (scrapperid bigint not null, accountsid bigint not null, primary key (scrapperid, accountsid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.scrapperdestinations
  OWNER TO ticketadvantage;
GRANT SELECT(scrapperid), UPDATE(scrapperid), INSERT(scrapperid), REFERENCES(scrapperid) ON public.scrapperdestinations TO public;

create table middledestinations (scrapperid bigint not null, accountsid bigint not null, primary key (scrapperid, accountsid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.middledestinations
  OWNER TO ticketadvantage;
GRANT SELECT(scrapperid), UPDATE(scrapperid), INSERT(scrapperid), REFERENCES(scrapperid) ON public.middledestinations TO public;

CREATE TABLE public.emailscrappersources
(
  emailid bigint NOT NULL,
  emailaccountsid bigint NOT NULL,
  CONSTRAINT emailscrappersources_pkey PRIMARY KEY (emailid, emailaccountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailscrappersources
  OWNER TO ticketadvantage;
GRANT SELECT(emailid), UPDATE(emailid), INSERT(emailid), REFERENCES(emailid) ON public.emailscrappersources TO public;

CREATE TABLE public.emailscrapperdestinations
(
  emailid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT emailscrapperdestinations_pkey PRIMARY KEY (emailid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailscrapperdestinations
  OWNER TO ticketadvantage;
GRANT SELECT(emailid), UPDATE(emailid), INSERT(emailid), REFERENCES(emailid) ON public.emailscrapperdestinations TO public;

create table emailmiddledestinations (emailid bigint not null, accountsid bigint not null, primary key (emailid, accountsid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailmiddledestinations
  OWNER TO ticketadvantage;
GRANT SELECT(emailid), UPDATE(emailid), INSERT(emailid), REFERENCES(emailid) ON public.emailmiddledestinations TO public;


create table usersaccounts (usersid bigint not null, accountsid bigint not null, primary key (usersid, accountsid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usersaccounts
  OWNER TO ticketadvantage;
GRANT SELECT(usersid), UPDATE(usersid), INSERT(usersid), REFERENCES(usersid) ON public.usersaccounts TO public;

create table usersgroups (usersid bigint not null, groupsid bigint not null, primary key (usersid, groupsid))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usersgroups
  OWNER TO ticketadvantage;
GRANT SELECT(usersid), UPDATE(usersid), INSERT(usersid), REFERENCES(usersid) ON public.usersgroups TO public;

create table usersta (id serial not null, datecreated timestamp with time zone not null, datemodified timestamp with time zone not null, email varchar(100), isactive boolean not null, mobilenumber varchar(16), password varchar(50), username varchar(50) not null, primary key (id))
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usersta
  OWNER TO ticketadvantage;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.usersta TO public;
CREATE INDEX usersta_isactive_idx ON public.usersta (isactive);
CREATE INDEX usersta_username_idx ON public.usersta (username);

create table hibernate_sequences (sequence_name varchar(255), sequence_next_hi_value integer)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.hibernate_sequences
  OWNER TO ticketadvantage;
GRANT SELECT(sequence_name), UPDATE(sequence_name), INSERT(sequence_name), REFERENCES(sequence_name) ON public.hibernate_sequences TO public;

ALTER TABLE public.accountevent
    ADD COLUMN eventresult character varying(20);

ALTER TABLE public.accountevent
    ADD COLUMN eventresultamount numeric(10, 2);
    
CREATE SEQUENCE public.userbilling_id_seq
;

ALTER SEQUENCE public.userbilling_id_seq
    OWNER TO ticketadvantage;
    
CREATE TABLE public.userbilling
(
    id bigint NOT NULL DEFAULT nextval('userbilling_id_seq'::regclass),
    userid bigint NOT NULL,
    weekstartdate date NOT NULL,
    accountrate numeric(8,2),
    ispaid boolean NOT NULL,
    weeklybalance numeric(8,2),
    numberofaccounts integer NOT NULL DEFAULT 0,
    CONSTRAINT userbilling_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.userbilling
    OWNER to ticketadvantage;

    GRANT ALL(id) ON public.userbilling TO PUBLIC;

CREATE INDEX userbilling_userid_idx ON public.userbilling (userid);

CREATE INDEX userbilling_week_idx
    ON public.userbilling USING btree
    (weekstartdate ASC NULLS LAST)
    TABLESPACE pg_default;    
    
CREATE SEQUENCE public.accounteventfinal_id_seq
;

ALTER SEQUENCE public.accounteventfinal_id_seq
    OWNER TO ticketadvantage;
    
CREATE TABLE public.accounteventfinal
(
    id bigint NOT NULL DEFAULT nextval('accounteventfinal_id_seq'::regclass),
    accounteventid bigint NOT NULL,
    rotation1 varchar(100),
    rotation2 varchar(100),
    rotation1team varchar(100),
    rotation2team varchar(100),
    rotation1score varchar(100),
    rotation2score varchar(100),
    outcomewin boolean,
    spreadindicator varchar(5),
    spreadnumber integer,
    spreadjuiceindicator varchar(5),
    spreadjuicenumber integer,
    totalindicator varchar(5),
    totalnumber integer,
    totaljuiceindicator varchar(5),
    totaljuicenumber integer,
    CONSTRAINT accounteventfinal_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.accounteventfinal
    OWNER to ticketadvantage;

    GRANT ALL(id) ON public.accounteventfinal TO PUBLIC;

CREATE INDEX accounteventfinal_accounteventid_idx ON public.accounteventfinal (accounteventid);


-- Sequence: public.twitteraccountsta_id_seq

-- DROP SEQUENCE public.twitteraccountsta_id_seq;

CREATE SEQUENCE public.twitteraccountsta_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.twitteraccountsta_id_seq
  OWNER TO tauser;

-- Table: public.twitteraccountsta

-- DROP TABLE public.twitteraccountsta;

CREATE TABLE public.twitteraccountsta
(
  id bigint NOT NULL DEFAULT nextval('twitteraccountsta_id_seq'::regclass),
  name character varying(100) NOT NULL,
  inet character varying(50) NOT NULL,
  accountid character varying(100),
  screenname character varying(100) NOT NULL,
  handleid character varying(50) NOT NULL,
  sitetype character varying(50) NOT NULL,
  isactive boolean NOT NULL,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  CONSTRAINT twitteraccountsta_pkey PRIMARY KEY (id, name, screenname)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.twitteraccountsta
  OWNER TO tauser;

-- Sequence: public.twitterscrapper_id_seq

-- DROP SEQUENCE public.twitterscrapper_id_seq;

CREATE SEQUENCE public.twitterscrapper_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.twitterscrapper_id_seq
  OWNER TO tauser;

-- Table: public.twitterscrapper

-- DROP TABLE public.twitterscrapper;

CREATE TABLE public.twitterscrapper
(
  id bigint NOT NULL DEFAULT nextval('twitterscrapper_id_seq'::regclass),
  userid bigint NOT NULL,
  scrappername character varying(100) NOT NULL,
  spreadlineadjustment character varying(10),
  spreadjuiceindicator character varying(10),
  spreadjuice character varying(10),
  spreadjuiceadjustment character varying(10),
  spreadmaxamount character varying(10),
  totallineadjustment character varying(10),
  totaljuiceindicator character varying(10),
  totaljuice character varying(10),
  totaljuiceadjustment character varying(10),
  totalmaxamount character varying(10),
  mlindicator character varying(10),
  mlline character varying(10),
  mllineadjustment character varying(10),
  mlmaxamount character varying(10),
  pullinginterval character varying(10),
  mobiletext character varying(200),
  telegramnumber character varying(12),
  middlerules boolean NOT NULL,
  onoff boolean NOT NULL,
  gameonoff boolean NOT NULL,
  firstonoff boolean NOT NULL,
  secondonoff boolean NOT NULL,
  thirdonoff boolean NOT NULL,
  nflspreadonoff boolean NOT NULL,
  nfltotalonoff boolean NOT NULL,
  nflmlonoff boolean NOT NULL,
  ncaafspreadonoff boolean NOT NULL,
  ncaaftotalonoff boolean NOT NULL,
  ncaafmlonoff boolean NOT NULL,
  nbaspreadonoff boolean NOT NULL,
  nbatotalonoff boolean NOT NULL,
  nbamlonoff boolean NOT NULL,
  ncaabspreadonoff boolean NOT NULL,
  ncaabtotalonoff boolean NOT NULL,
  ncaabmlonoff boolean NOT NULL,
  wnbaspreadonoff boolean NOT NULL,
  wnbatotalonoff boolean NOT NULL,
  wnbamlonoff boolean NOT NULL,
  nhlspreadonoff boolean NOT NULL,
  nhltotalonoff boolean NOT NULL,
  nhlmlonoff boolean NOT NULL,
  mlbspreadonoff boolean NOT NULL,
  mlbtotalonoff boolean NOT NULL,
  mlbmlonoff boolean NOT NULL,
  internationalbaseballspreadonoff boolean NOT NULL,
  internationalbaseballtotalonoff boolean NOT NULL,
  internationalbaseballmlonoff boolean NOT NULL,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  servernumber integer DEFAULT 2,
  enableretry boolean DEFAULT false,
  fullfill boolean DEFAULT false,
  orderamount integer DEFAULT 0,
  checkdupgame boolean DEFAULT true,
  playotherside boolean DEFAULT false,
  sendtextforaccount boolean DEFAULT false,
  bestprice boolean,
  spreadsourceamount double precision DEFAULT 0,
  totalsourceamount double precision DEFAULT 0,
  mlsourceamount double precision DEFAULT 0,
  keynumber boolean DEFAULT false,
  humanspeed boolean DEFAULT false,
  unitsenabled boolean DEFAULT false,
  spreadunit double precision DEFAULT 0,
  totalunit double precision DEFAULT 0,
  mlunit double precision DEFAULT 0,
  leanssenabled boolean DEFAULT false,
  spreadlean double precision DEFAULT 0,
  totallean double precision DEFAULT 0,
  mllean double precision DEFAULT 0,
  sendtextforgame boolean DEFAULT false,
  CONSTRAINT twitterscrapper_pkey PRIMARY KEY (id, scrappername, userid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.twitterscrapper
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.twitterscrapper TO public;


-- Index: public.emailscrapper_name_idx

-- DROP INDEX public.emailscrapper_name_idx;

CREATE INDEX emailscrapper_name_idx
  ON public.emailscrapper
  USING btree
  (scrappername COLLATE pg_catalog."default");

-- Index: public.emailscrapper_userid_idx

-- DROP INDEX public.emailscrapper_userid_idx;

CREATE INDEX emailscrapper_userid_idx
  ON public.emailscrapper
  USING btree
  (userid);

-- Index: public.twitterscrapper_name_idx

-- DROP INDEX public.twitterscrapper_name_idx;

CREATE INDEX twitterscrapper_name_idx
  ON public.twitterscrapper
  USING btree
  (scrappername COLLATE pg_catalog."default");

-- Index: public.twitterscrapper_userid_idx

-- DROP INDEX public.twitterscrapper_userid_idx;

CREATE INDEX twitterscrapper_userid_idx
  ON public.twitterscrapper
  USING btree
  (userid);

-- Table: public.twitterscrapperdestinations

-- DROP TABLE public.twitterscrapperdestinations;

CREATE TABLE public.twitterscrapperdestinations
(
  twitterid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT twitterscrapperdestinations_pkey PRIMARY KEY (twitterid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.twitterscrapperdestinations
  OWNER TO tauser;
GRANT SELECT(twitterid), UPDATE(twitterid), INSERT(twitterid), REFERENCES(twitterid) ON public.twitterscrapperdestinations TO public;

-- Table: public.twitterscrappersources

-- DROP TABLE public.twitterscrappersources;

CREATE TABLE public.twitterscrappersources
(
  twitterid bigint NOT NULL,
  twitteraccountsid bigint NOT NULL,
  CONSTRAINT twitterscrappersources_pkey PRIMARY KEY (twitterid, twitteraccountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.twitterscrappersources
  OWNER TO tauser;
GRANT SELECT(twitterid), UPDATE(twitterid), INSERT(twitterid), REFERENCES(twitterid) ON public.twitterscrappersources TO public;

-- Table: public.twittermiddledestinations

-- DROP TABLE public.twittermiddledestinations;

CREATE TABLE public.twittermiddledestinations
(
  twitterid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT twittermiddledestinations_pkey PRIMARY KEY (twitterid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.twittermiddledestinations
  OWNER TO tauser;
GRANT SELECT(twitterid), UPDATE(twitterid), INSERT(twitterid), REFERENCES(twitterid) ON public.twittermiddledestinations TO public;

-- Table: public.twitterorderdestinations

-- DROP TABLE public.twitterorderdestinations;

CREATE TABLE public.twitterorderdestinations
(
  twitterid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT twitterorderdestinations_pkey PRIMARY KEY (twitterid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.twitterorderdestinations
  OWNER TO tauser;
GRANT SELECT(twitterid), UPDATE(twitterid), INSERT(twitterid), REFERENCES(twitterid) ON public.twitterorderdestinations TO public;


-- Sequence: public.twittertweet_id_seq

-- DROP SEQUENCE public.twittertweet_id_seq;

CREATE SEQUENCE public.twittertweet_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.twittertweet_id_seq
  OWNER TO tauser;

-- Table: public.twittertweet

-- DROP TABLE public.twittertweet;

CREATE TABLE public.twittertweet
(
  id bigint NOT NULL DEFAULT nextval('twittertweet_id_seq'::regclass),
  tweetid bigint,
  tweettext character varying(4000),
  username character varying(100),
  screenname character varying(100),
  tweetdate timestamp with time zone,
  datecreated timestamp with time zone,
  datemodified timestamp with time zone,
  CONSTRAINT twittertweet_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.twittertweet
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.twittertweet TO public;

-- Index: public.twittertweet_tweetid_idx

-- DROP INDEX public.twittertweet_tweetid_idx;

CREATE INDEX twittertweet_tweetid_idx
  ON public.twittertweet
  USING btree
  (tweetid);


INSERT INTO public.usersta(datecreated, datemodified, email, isactive, mobilenumber, password, username) VALUES ('2016-10-20 18:15:16.785', '2016-10-20 18:15:16.785', 'j@h.com',TRUE,'8885551212','3id39d','jhmojax');
INSERT INTO public.usersta(datecreated, datemodified, email, isactive, mobilenumber, password, username) VALUES ('2016-10-20 18:15:16.785', '2016-10-20 18:15:16.785', 'j@m.com',TRUE,'8885551212','password','jmiller');
INSERT INTO public.usersta(datecreated, datemodified, email, isactive, mobilenumber, password, username) VALUES ('2016-10-20 18:15:16.785', '2016-10-20 18:15:16.785', 't@v.com',TRUE,'8885551212','vegas5432','tommy');
INSERT INTO public.usersta(datecreated, datemodified, email, isactive, mobilenumber, password, username) VALUES ('2016-10-20 18:15:16.785', '2016-10-20 18:15:16.785', 'b@n.com',TRUE,'8885551212','notakufan','brett');
INSERT INTO public.usersta(datecreated, datemodified, email, isactive, mobilenumber, password, username) VALUES ('2017-08-03 18:15:16.785', '2017-08-03 18:15:16.785', 'b@n.com',TRUE,'8885551212','scrapper','email');
INSERT INTO public.usersta(datecreated, datemodified, email, isactive, mobilenumber, password, username) VALUES ('2017-08-03 18:15:16.785', '2017-08-03 18:15:16.785', 'r@n.com',TRUE,'8885551212','botboys','vegas');

INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:15:16.785', '2016-10-20 18:15:16.785', true, 1000, 1000, 1000, 'ABC Grand', 'ET', 100, 0, 'Utah', 'Baltimore', 'TDSports', 'http://www.abcgrand.ag', 'q3913');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:16:31.252', '2016-10-20 18:16:31.252', true, 2500, 2500, 1000, 'Vision Wager', 'ET', 100, 0, '69', 'Asheville', 'TDSports', 'http://www.visionwager.com', 'Me232');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:17:30.65', '2016-10-20 18:21:05.914', true, 1000, 1000, 1000, 'Play the Dog', 'ET', 100, 0, 'pat', 'Asheville', 'TDSports', 'http://www.playthedog.net', 'Hd114');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:25:06.278', '2016-10-20 18:25:06.278', true, 500, 500, 500, 'Sun Wager', 'ET', 100, 0, 'John', 'Phoenix', 'Metallica', 'http://www.sunwager.com', '4488');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:27:09.842', '2016-10-20 18:27:09.842', true, 1000, 1000, 1000, 'Toby Sports', 'ET', 100, 0, 'pgf', 'Chicago', 'BetBuckeye', 'http://www.tobysports.net', 'Orc101');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:27:50.975', '2016-10-20 18:27:50.975', true, 2000, 1000, 1000, 'Bet Buckeye', 'ET', 100, 0, 'red3', 'Chicago', 'BetBuckeye', 'http://betbuckeyesports.com', 'zt8219');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:30:20.652', '2016-10-20 18:30:20.652', true, 200, 200, 200, 'Sliding Home', 'ET', 100, 0, 'flyers', 'Baltimore', 'AGSoftware', 'http://www.slidinghome.com', 'Pa1547');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:33:41.731', '2016-10-20 18:33:41.731', true, 500, 500, 250, 'Rustic Lodge', 'ET', 100, 0, 'Denver', 'Dallas', 'LineTracker', 'http://rusticlodge.club', 'Kb408');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:47:51.488', '2016-10-20 18:47:51.488', true, 1000, 1000, 1000, 'Game Day Play', 'ET', 100, 0, 'jags', 'Chicago', 'TDSportsNew', 'http://www.gamedayplay.net', 'Pc1795');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 18:59:04.516', '2016-10-20 18:59:04.516', true, 500, 250, 300, 'Green Isle Club', 'ET', 100, 0, 'Iowa', 'Dallas', 'Metallica', 'http://www.greenisle.club', 'Go508');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 19:09:31.552', '2016-10-20 19:09:31.552', true, 100, 100, 100, 'Betting 4 Entertainment', 'ET', 100, 0, 'ap6', 'Baltimore', 'AGSoftware', 'http://www.betting4entertainment.com', '51336');
INSERT INTO accountsta (datecreated, datemodified, isactive, spreadlimitamount, totallimitamount, mllimitamount, name, timezone, ownerpercentage, partnerpercentage, password, proxylocation, sitetype, url, username) VALUES ('2016-10-20 19:12:06.96', '2016-10-20 19:12:06.96', true, 500, 500, 500, 'Bet Windy City', 'ET', 100, 0, 'Phillies', 'Chicago', 'Metallica', 'http://www.betwindycity.com', 'w102');

INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 1);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 2);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 3);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 4);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 5);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 6);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 7);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 8);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 9);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 10);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 11);
INSERT INTO usersaccounts (usersid, accountsid) VALUES (3, 12);
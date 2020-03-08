-- Sequence: public.accountevent_id_seq

-- DROP SEQUENCE public.accountevent_id_seq;

CREATE SEQUENCE public.accountevent_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.accountevent_id_seq
  OWNER TO tauser;

-- Sequence: public.accounteventfinal_id_seq

-- DROP SEQUENCE public.accounteventfinal_id_seq;

CREATE SEQUENCE public.accounteventfinal_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.accounteventfinal_id_seq
  OWNER TO tauser;

-- Sequence: public.accountsta_id_seq

-- DROP SEQUENCE public.accountsta_id_seq;

CREATE SEQUENCE public.accountsta_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.accountsta_id_seq
  OWNER TO tauser;

-- Sequence: public.captcha_id_seq

-- DROP SEQUENCE public.captcha_id_seq;

CREATE SEQUENCE public.captcha_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.captcha_id_seq
  OWNER TO tauser;

-- Sequence: public.emailaccountsta_id_seq

-- DROP SEQUENCE public.emailaccountsta_id_seq;

CREATE SEQUENCE public.emailaccountsta_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.emailaccountsta_id_seq
  OWNER TO tauser;

-- Sequence: public.emailevent_id_seq

-- DROP SEQUENCE public.emailevent_id_seq;

CREATE SEQUENCE public.emailevent_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.emailevent_id_seq
  OWNER TO tauser;

-- Sequence: public.emailscrapper_id_seq

-- DROP SEQUENCE public.emailscrapper_id_seq;

CREATE SEQUENCE public.emailscrapper_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.emailscrapper_id_seq
  OWNER TO tauser;

-- Sequence: public.groupsta_id_seq

-- DROP SEQUENCE public.groupsta_id_seq;

CREATE SEQUENCE public.groupsta_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.groupsta_id_seq
  OWNER TO tauser;

-- Sequence: public.mlrecordevents_id_seq

-- DROP SEQUENCE public.mlrecordevents_id_seq;

CREATE SEQUENCE public.mlrecordevents_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.mlrecordevents_id_seq
  OWNER TO tauser;

-- Sequence: public.parlayaccountevent_id_seq

-- DROP SEQUENCE public.parlayaccountevent_id_seq;

CREATE SEQUENCE public.parlayaccountevent_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.parlayaccountevent_id_seq
  OWNER TO tauser;

-- Sequence: public.parlayrecordevent_id_seq

-- DROP SEQUENCE public.parlayrecordevent_id_seq;

CREATE SEQUENCE public.parlayrecordevent_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.parlayrecordevent_id_seq
  OWNER TO tauser;

-- Sequence: public.pendingevents_id_seq

-- DROP SEQUENCE public.pendingevents_id_seq;

CREATE SEQUENCE public.pendingevents_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.pendingevents_id_seq
  OWNER TO tauser;

-- Sequence: public.scrapper_id_seq

-- DROP SEQUENCE public.scrapper_id_seq;

CREATE SEQUENCE public.scrapper_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.scrapper_id_seq
  OWNER TO postgres;

-- Sequence: public.spreadrecordevents_id_seq

-- DROP SEQUENCE public.spreadrecordevents_id_seq;

CREATE SEQUENCE public.spreadrecordevents_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.spreadrecordevents_id_seq
  OWNER TO tauser;

-- Sequence: public.totalrecordevents_id_seq

-- DROP SEQUENCE public.totalrecordevents_id_seq;

CREATE SEQUENCE public.totalrecordevents_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.totalrecordevents_id_seq
  OWNER TO tauser;

-- Sequence: public.userbilling_id_seq

-- DROP SEQUENCE public.userbilling_id_seq;

CREATE SEQUENCE public.userbilling_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.userbilling_id_seq
  OWNER TO tauser;

-- Sequence: public.usersta_id_seq

-- DROP SEQUENCE public.usersta_id_seq;

CREATE SEQUENCE public.usersta_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.usersta_id_seq
  OWNER TO tauser;

-- Sequence: public.webscrapper_id_seq

-- DROP SEQUENCE public.webscrapper_id_seq;

CREATE SEQUENCE public.webscrapper_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.webscrapper_id_seq
  OWNER TO tauser;

-- Table: public.accountevent

-- DROP TABLE public.accountevent;

CREATE TABLE public.accountevent
(
  id bigint NOT NULL DEFAULT nextval('accountevent_id_seq'::regclass),
  id bigint NOT NULL DEFAULT nextval('accountevent_id_seq'::regclass),
  accountconfirmation character varying(4000),
  accounthtml text,
  accountid bigint NOT NULL,
  amount character varying(20),
  actualamount character varying(20),
  attempts integer NOT NULL,
  attempttime timestamp with time zone,
  currentattempts integer,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  errorcode integer,
  errorexception character varying(4000),
  errormessage character varying(4000),
  eventdatetime timestamp with time zone NOT NULL,
  eventid integer,
  eventname character varying(100) NOT NULL,
  groupid bigint NOT NULL,
  iscompleted boolean,
  maxspreadamount integer,
  maxtotalamount integer,
  maxmlamount integer,
  mlindicator character varying(10),
  timezone character varying(100),
  ml double precision,
  mlid bigint,
  mljuice double precision,
  name character varying(100) NOT NULL,
  ownerpercentage integer,
  partnerpercentage integer,
  proxy character varying(255),
  sport character varying(100) NOT NULL,
  spreadindicator character varying(10),
  spread double precision,
  spreadid bigint,
  spreadjuice double precision,
  total double precision,
  totalid bigint,
  totalindicator character varying(10),
  totaljuice double precision,
  type character varying(100) NOT NULL,
  userid bigint NOT NULL,
  wagertype character varying(10),
  status character varying(20),
  riskamount character varying(20),
  towinamount character varying(20),
  eventresult character varying(20),
  eventresultamount numeric(10,2),
  accesstoken character varying(200),
  CONSTRAINT accountevent_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.accountevent
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.accountevent TO public;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.accountevent TO public;


-- Index: public.accountevent_accountid_idx

-- DROP INDEX public.accountevent_accountid_idx;

CREATE INDEX accountevent_accountid_idx
  ON public.accountevent
  USING btree
  (accountid);

-- Index: public.accountevent_datecreated_idx

-- DROP INDEX public.accountevent_datecreated_idx;

CREATE INDEX accountevent_datecreated_idx
  ON public.accountevent
  USING btree
  (datecreated);

-- Index: public.accountevent_userid_idx

-- DROP INDEX public.accountevent_userid_idx;

CREATE INDEX accountevent_userid_idx
  ON public.accountevent
  USING btree
  (userid);

-- Table: public.accounteventfinal

-- DROP TABLE public.accounteventfinal;

CREATE TABLE public.accounteventfinal
(
  id bigint NOT NULL DEFAULT nextval('accounteventfinal_id_seq'::regclass),
  accounteventid bigint NOT NULL,
  rotation1 character varying(100),
  rotation2 character varying(100),
  rotation1team character varying(100),
  rotation2team character varying(100),
  rotation1score character varying(100),
  rotation2score character varying(100),
  outcomewin boolean,
  spreadindicator character varying(5),
  spreadnumber integer,
  spreadjuiceindicator character varying(5),
  spreadjuicenumber integer,
  totalindicator character varying(5),
  totalnumber integer,
  totaljuiceindicator character varying(5),
  totaljuicenumber integer,
  mlindicator character varying(5),
  mlnumber integer,
  CONSTRAINT accounteventfinal_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.accounteventfinal
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.accounteventfinal TO public;


-- Index: public.accounteventfinal_accounteventid_idx

-- DROP INDEX public.accounteventfinal_accounteventid_idx;

CREATE INDEX accounteventfinal_accounteventid_idx
  ON public.accounteventfinal
  USING btree
  (accounteventid);

-- Table: public.accountsta

-- DROP TABLE public.accountsta;

CREATE TABLE public.accountsta
(
  id bigint NOT NULL DEFAULT nextval('accountsta_id_seq'::regclass),
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  isactive boolean NOT NULL,
  spreadlimitamount integer,
  totallimitamount integer,
  mllimitamount integer,
  name character varying(100) NOT NULL,
  timezone character varying(100),
  ownerpercentage integer,
  partnerpercentage integer,
  password character varying(100),
  proxylocation character varying(200),
  sitetype character varying(255),
  url character varying(200) NOT NULL,
  username character varying(100),
  ismobile boolean,
  showrequestresponse boolean,
  hourbefore character varying(2) DEFAULT '00'::character varying,
  minutebefore character varying(2) DEFAULT '00'::character varying,
  hourafter character varying(2) DEFAULT '00'::character varying,
  minuteafter character varying(2) DEFAULT '00'::character varying,
  CONSTRAINT accountsta_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.accountsta
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.accountsta TO public;


-- Index: public.accountsta_isactive_idx

-- DROP INDEX public.accountsta_isactive_idx;

CREATE INDEX accountsta_isactive_idx
  ON public.accountsta
  USING btree
  (isactive);

-- Index: public.accountsta_name_idx

-- DROP INDEX public.accountsta_name_idx;

CREATE INDEX accountsta_name_idx
  ON public.accountsta
  USING btree
  (name COLLATE pg_catalog."default");

-- Table: public.captcha

-- DROP TABLE public.captcha;

CREATE TABLE public.captcha
(
  id bigint NOT NULL DEFAULT nextval('captcha_id_seq'::regclass),
  userid bigint,
  imagedata text,
  textdata character varying(10),
  CONSTRAINT captcha_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.captcha
  OWNER TO tauser;
GRANT ALL ON TABLE public.captcha TO tauser;
GRANT ALL ON TABLE public.captcha TO public;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.captcha TO public;

-- Table: public.emailaccountsta

-- DROP TABLE public.emailaccountsta;

CREATE TABLE public.emailaccountsta
(
  id bigint NOT NULL DEFAULT nextval('emailaccountsta_id_seq'::regclass),
  name character varying(100) NOT NULL,
  inet character varying(50) NOT NULL,
  address character varying(100) NOT NULL,
  password character varying(50) NOT NULL,
  host character varying(50) NOT NULL,
  port character varying(6) NOT NULL,
  tls boolean NOT NULL,
  timezone character varying(50),
  sitetype character varying(50) NOT NULL,
  provider character varying(50) NOT NULL,
  emailtype character varying(20) NOT NULL,
  authenticationtype character varying(20),
  clientid character varying(200),
  clientsecret character varying(100),
  refreshtoken character varying(200),
  granttype character varying(50),
  isactive boolean NOT NULL,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  accountid character varying(100),
  CONSTRAINT emailaccountsta_pkey PRIMARY KEY (id, name, address)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailaccountsta
  OWNER TO tauser;
 
-- Table: public.emailevent

-- DROP TABLE public.emailevent;

CREATE TABLE public.emailevent
(
  id bigint NOT NULL DEFAULT nextval('emailevent_id_seq'::regclass),
  messagenum integer,
  emailname character varying(100),
  fromemail character varying(400),
  toemail character varying(400),
  ccemail character varying(400),
  bccemail character varying(400),
  subject character varying(400),
  bodytext character varying(4000),
  bodyhtml character varying(4000),
  inet character varying(50),
  datesent timestamp with time zone,
  datereceived timestamp with time zone,
  datecreated timestamp with time zone,
  datemodified timestamp with time zone,
  CONSTRAINT emailevent_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailevent
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.emailevent TO public;


-- Index: public.emailevent_datecreated_idx

-- DROP INDEX public.emailevent_datecreated_idx;

CREATE INDEX emailevent_datecreated_idx
  ON public.emailevent
  USING btree
  (datecreated);

-- Index: public.emailevent_messagenum_idx

-- DROP INDEX public.emailevent_messagenum_idx;

CREATE INDEX emailevent_messagenum_idx
  ON public.emailevent
  USING btree
  (messagenum);

-- Index: public.emailevent_subject_idx

-- DROP INDEX public.emailevent_subject_idx;

CREATE INDEX emailevent_subject_idx
  ON public.emailevent
  USING btree
  (subject COLLATE pg_catalog."default");

-- Table: public.emailmiddledestinations

-- DROP TABLE public.emailmiddledestinations;

CREATE TABLE public.emailmiddledestinations
(
  emailid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT emailmiddledestinations_pkey PRIMARY KEY (emailid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailmiddledestinations
  OWNER TO tauser;
GRANT SELECT(emailid), UPDATE(emailid), INSERT(emailid), REFERENCES(emailid) ON public.emailmiddledestinations TO public;

-- Table: public.emailorderdestinations

-- DROP TABLE public.emailorderdestinations;

CREATE TABLE public.emailorderdestinations
(
  emailid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT emailorderdestinations_pkey PRIMARY KEY (emailid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailorderdestinations
  OWNER TO tauser;
GRANT SELECT(emailid), UPDATE(emailid), INSERT(emailid), REFERENCES(emailid) ON public.emailorderdestinations TO public;

-- Table: public.emailscrapper

-- DROP TABLE public.emailscrapper;

CREATE TABLE public.emailscrapper
(
  id bigint NOT NULL DEFAULT nextval('emailscrapper_id_seq'::regclass),
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
  CONSTRAINT emailscrapper_pkey PRIMARY KEY (id, scrappername, userid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.emailscrapper
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.emailscrapper TO public;


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

-- Table: public.emailscrapperdestinations

-- DROP TABLE public.emailscrapperdestinations;

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
  OWNER TO tauser;
GRANT SELECT(emailid), UPDATE(emailid), INSERT(emailid), REFERENCES(emailid) ON public.emailscrapperdestinations TO public;

-- Table: public.emailscrappersources

-- DROP TABLE public.emailscrappersources;

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
  OWNER TO tauser;
GRANT SELECT(emailid), UPDATE(emailid), INSERT(emailid), REFERENCES(emailid) ON public.emailscrappersources TO public;

-- Table: public.groupsaccounts

-- DROP TABLE public.groupsaccounts;

CREATE TABLE public.groupsaccounts
(
  groupsid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT groupsaccounts_pkey PRIMARY KEY (groupsid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.groupsaccounts
  OWNER TO tauser;
GRANT SELECT(groupsid), UPDATE(groupsid), INSERT(groupsid), REFERENCES(groupsid) ON public.groupsaccounts TO public;

-- Table: public.groupsta

-- DROP TABLE public.groupsta;

CREATE TABLE public.groupsta
(
  id bigint NOT NULL DEFAULT nextval('groupsta_id_seq'::regclass),
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  isactive boolean NOT NULL,
  name character varying(100) NOT NULL,
  CONSTRAINT groupsta_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.groupsta
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.groupsta TO public;


-- Index: public.groupsta_isactive_idx

-- DROP INDEX public.groupsta_isactive_idx;

CREATE INDEX groupsta_isactive_idx
  ON public.groupsta
  USING btree
  (isactive);

-- Index: public.groupsta_name_idx

-- DROP INDEX public.groupsta_name_idx;

CREATE INDEX groupsta_name_idx
  ON public.groupsta
  USING btree
  (name COLLATE pg_catalog."default");

-- Table: public.hibernate_sequences

-- DROP TABLE public.hibernate_sequences;

CREATE TABLE public.hibernate_sequences
(
  sequence_name character varying(255),
  sequence_next_hi_value integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.hibernate_sequences
  OWNER TO tauser;
GRANT SELECT(sequence_name), UPDATE(sequence_name), INSERT(sequence_name), REFERENCES(sequence_name) ON public.hibernate_sequences TO public;

-- Table: public.middledestinations

-- DROP TABLE public.middledestinations;

CREATE TABLE public.middledestinations
(
  scrapperid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT middledestinations_pkey PRIMARY KEY (scrapperid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.middledestinations
  OWNER TO tauser;
GRANT SELECT(scrapperid), UPDATE(scrapperid), INSERT(scrapperid), REFERENCES(scrapperid) ON public.middledestinations TO public;

-- Table: public.mlrecordevents

-- DROP TABLE public.mlrecordevents;

CREATE TABLE public.mlrecordevents
(
  id bigint NOT NULL DEFAULT nextval('mlrecordevents_id_seq'::regclass),
  accountid bigint,
  amount character varying(10),
  attempts integer NOT NULL,
  attempttime timestamp with time zone,
  currentattempts integer,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone,
  datentime timestamp with time zone NOT NULL,
  eventdatetime timestamp with time zone,
  eventid integer NOT NULL,
  eventid1 integer,
  eventid2 integer,
  eventname character varying(100),
  eventteam1 character varying(100),
  eventteam2 character varying(100),
  eventtype character varying(100) NOT NULL,
  groupid bigint,
  iscompleted boolean NOT NULL,
  sport character varying(100) NOT NULL,
  userid bigint NOT NULL,
  wtype character varying(10),
  mlinputfirstone character varying(255),
  mlinputfirsttwo character varying(255),
  mlinputsecondone character varying(255),
  mlinputsecondtwo character varying(255),
  mlplusminusfirstone character varying(255),
  mlplusminusfirsttwo character varying(255),
  mlplusminussecondone character varying(255),
  mlplusminussecondtwo character varying(255),
  scrappername character varying(100),
  actiontype character varying(10),
  textnumber character varying(200),
  rotationid integer,
  CONSTRAINT mlrecordevents_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.mlrecordevents
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.mlrecordevents TO public;


-- Index: public.mlrecordevents_datecreated_idx

-- DROP INDEX public.mlrecordevents_datecreated_idx;

CREATE INDEX mlrecordevents_datecreated_idx
  ON public.mlrecordevents
  USING btree
  (datecreated);

-- Index: public.mlrecordevents_datentime_idx

-- DROP INDEX public.mlrecordevents_datentime_idx;

CREATE INDEX mlrecordevents_datentime_idx
  ON public.mlrecordevents
  USING btree
  (datentime);

-- Index: public.mlrecordevents_eventdatetime_idx

-- DROP INDEX public.mlrecordevents_eventdatetime_idx;

CREATE INDEX mlrecordevents_eventdatetime_idx
  ON public.mlrecordevents
  USING btree
  (eventdatetime);

-- Index: public.mlrecordevents_eventid_idx

-- DROP INDEX public.mlrecordevents_eventid_idx;

CREATE INDEX mlrecordevents_eventid_idx
  ON public.mlrecordevents
  USING btree
  (eventid);

-- Index: public.mlrecordevents_userid_idx

-- DROP INDEX public.mlrecordevents_userid_idx;

CREATE INDEX mlrecordevents_userid_idx
  ON public.mlrecordevents
  USING btree
  (userid);

-- Table: public.orderdestinations

-- DROP TABLE public.orderdestinations;

CREATE TABLE public.orderdestinations
(
  scrapperid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT orderdestinations_pkey PRIMARY KEY (scrapperid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.orderdestinations
  OWNER TO tauser;

-- Table: public.parlayaccountevent

-- DROP TABLE public.parlayaccountevent;

CREATE TABLE public.parlayaccountevent
(
  id bigint NOT NULL DEFAULT nextval('parlayaccountevent_id_seq'::regclass),
  userid bigint NOT NULL,
  parlayid bigint NOT NULL,
  eventid integer,
  eventname character varying(100) NOT NULL,
  accountid bigint NOT NULL,
  accountname character varying(100) NOT NULL,
  groupid bigint NOT NULL,
  sport character varying(20) NOT NULL,
  type character varying(10),
  wagertype character varying(10),
  amount numeric(10,2),
  actualamount numeric(10,2),
  maxspreadamount numeric(10,2),
  maxtotalamount numeric(10,2),
  maxmlamount numeric(10,2),
  timezone character varying(20),
  ownerpercentage integer,
  partnerpercentage integer,
  spreadindicator character varying(10),
  spreaddata character varying(10),
  spread double precision,
  spreadjuiceindicator character varying(10),
  spreadjuicedata character varying(10),
  spreadjuice double precision,
  totalindicator character varying(10),
  totaldata character varying(10),
  total double precision,
  totaljuiceindicator character varying(10),
  totaljuicedata character varying(10),
  totaljuice double precision,
  mlindicator character varying(10),
  mldata character varying(10),
  ml double precision,
  accountconfirmation character varying(4000),
  accounthtml text,
  iscompleted boolean,
  proxy character varying(255),
  attempts integer NOT NULL,
  currentattempts integer,
  attempttime timestamp with time zone,
  errormessage character varying(4000),
  errorexception character varying(4000),
  errorcode integer,
  status character varying(20),
  riskamount numeric(10,2),
  towinamount numeric(10,2),
  eventresult character varying(20),
  eventresultamount numeric(10,2),
  accesstoken character varying(200),
  eventdatetime timestamp with time zone NOT NULL,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  parlaytype character varying(20),
  CONSTRAINT parlayaccountevent_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.parlayaccountevent
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.parlayaccountevent TO public;


-- Index: public.parlayaccountevent_accountid_idx

-- DROP INDEX public.parlayaccountevent_accountid_idx;

CREATE INDEX parlayaccountevent_accountid_idx
  ON public.parlayaccountevent
  USING btree
  (accountid);

-- Index: public.parlayaccountevent_datecreated_idx

-- DROP INDEX public.parlayaccountevent_datecreated_idx;

CREATE INDEX parlayaccountevent_datecreated_idx
  ON public.parlayaccountevent
  USING btree
  (datecreated);

-- Index: public.parlayaccountevent_userid_idx

-- DROP INDEX public.parlayaccountevent_userid_idx;

CREATE INDEX parlayaccountevent_userid_idx
  ON public.parlayaccountevent
  USING btree
  (userid);

-- Table: public.parlayrecordevent

-- DROP TABLE public.parlayrecordevent;

CREATE TABLE public.parlayrecordevent
(
  id bigint NOT NULL DEFAULT nextval('parlayrecordevent_id_seq'::regclass),
  userid bigint NOT NULL,
  parlaytype character varying(20),
  totalrisk numeric(10,2),
  totalwin numeric(10,2),
  description character varying(100),
  scrappername character varying(100),
  actiontype character varying(20),
  textnumber character varying(40),
  wtype character varying(10),
  iscompleted boolean NOT NULL,
  attempts integer NOT NULL,
  attempttime timestamp with time zone,
  currentattempts integer,
  riskamount numeric(10,2),
  winamount numeric(10,2),
  eventresult character varying(10),
  eventresultamount numeric(10,2),
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone,
  CONSTRAINT parlayrecordevent_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.parlayrecordevent
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.parlayrecordevent TO public;


-- Index: public.parlayrecordevent_datecreated_idx

-- DROP INDEX public.parlayrecordevent_datecreated_idx;

CREATE INDEX parlayrecordevent_datecreated_idx
  ON public.parlayrecordevent
  USING btree
  (datecreated);

-- Index: public.parlayrecordevent_userid_idx

-- DROP INDEX public.parlayrecordevent_userid_idx;

CREATE INDEX parlayrecordevent_userid_idx
  ON public.parlayrecordevent
  USING btree
  (userid);

-- Table: public.pendingevents

-- DROP TABLE public.pendingevents;

CREATE TABLE public.pendingevents
(
  id bigint NOT NULL DEFAULT nextval('pendingevents_id_seq'::regclass),
  userid bigint NOT NULL,
  ticketnum character varying(20) NOT NULL,
  eventtype character varying(10),
  linetype character varying(20),
  eventdate character varying(80),
  gamesport character varying(80),
  gametype character varying(80),
  team character varying(100),
  rotationid character varying(10),
  line character varying(20),
  lineplusminus character varying(10),
  juice character varying(10),
  juiceplusminus character varying(10),
  risk character varying(20),
  win character varying(20),
  pendingtype character varying(10),
  accountname character varying(200),
  accountid character varying(100),
  doposturl boolean,
  posturl character varying(255),
  customerid character varying(200),
  inet character varying(200),
  dateaccepted character varying(50),
  datecreated timestamp with time zone,
  datemodified timestamp with time zone,
  gamedate timestamp with time zone,
  pitcher1 character varying(100),
  pitcher2 character varying(100),
  listedpitchers boolean,
  transactiontype character varying(20),
  parlaynumber bigint,
  amountrisk character varying(10),
  amountwin character varying(10),
  parlayseqnum integer,
  CONSTRAINT pendingevents_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.pendingevents
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.pendingevents TO public;


-- Index: public.pendingevents_datecreated_idx

-- DROP INDEX public.pendingevents_datecreated_idx;

CREATE INDEX pendingevents_datecreated_idx
  ON public.pendingevents
  USING btree
  (datecreated);

-- Index: public.pendingevents_pendingtype_idx

-- DROP INDEX public.pendingevents_pendingtype_idx;

CREATE INDEX pendingevents_pendingtype_idx
  ON public.pendingevents
  USING btree
  (pendingtype COLLATE pg_catalog."default");

-- Index: public.pendingevents_ticketnum_idx

-- DROP INDEX public.pendingevents_ticketnum_idx;

CREATE INDEX pendingevents_ticketnum_idx
  ON public.pendingevents
  USING btree
  (ticketnum COLLATE pg_catalog."default");

-- Index: public.pendingevents_userid_idx

-- DROP INDEX public.pendingevents_userid_idx;

CREATE INDEX pendingevents_userid_idx
  ON public.pendingevents
  USING btree
  (userid);

-- Table: public.scrapperdestinations

-- DROP TABLE public.scrapperdestinations;

CREATE TABLE public.scrapperdestinations
(
  scrapperid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT scrapperdestinations_pkey PRIMARY KEY (scrapperid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.scrapperdestinations
  OWNER TO tauser;
GRANT SELECT(scrapperid), UPDATE(scrapperid), INSERT(scrapperid), REFERENCES(scrapperid) ON public.scrapperdestinations TO public;

-- Table: public.scrappersources

-- DROP TABLE public.scrappersources;

CREATE TABLE public.scrappersources
(
  scrapperid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT scrappersources_pkey PRIMARY KEY (scrapperid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.scrappersources
  OWNER TO tauser;
GRANT SELECT(scrapperid), UPDATE(scrapperid), INSERT(scrapperid), REFERENCES(scrapperid) ON public.scrappersources TO public;

-- Table: public.siteactive

-- DROP TABLE public.siteactive;

CREATE TABLE public.siteactive
(
  sitetype character varying(50),
  username character varying(50)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.siteactive
  OWNER TO tauser;

-- Index: public.sitetype_index

-- DROP INDEX public.sitetype_index;

CREATE INDEX sitetype_index
  ON public.siteactive
  USING btree
  (sitetype COLLATE pg_catalog."default");

-- Index: public.username_index

-- DROP INDEX public.username_index;

CREATE INDEX username_index
  ON public.siteactive
  USING btree
  (username COLLATE pg_catalog."default");

-- Table: public.spreadrecordevents

-- DROP TABLE public.spreadrecordevents;

CREATE TABLE public.spreadrecordevents
(
  id bigint NOT NULL DEFAULT nextval('spreadrecordevents_id_seq'::regclass),
  accountid bigint,
  amount character varying(10),
  attempts integer NOT NULL,
  attempttime timestamp with time zone,
  currentattempts integer,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone,
  datentime timestamp with time zone NOT NULL,
  eventdatetime timestamp with time zone NOT NULL,
  eventid integer NOT NULL,
  eventid1 integer,
  eventid2 integer,
  eventname character varying(100),
  eventteam1 character varying(100),
  eventteam2 character varying(100),
  eventtype character varying(100) NOT NULL,
  groupid bigint,
  iscompleted boolean NOT NULL,
  sport character varying(100) NOT NULL,
  userid bigint NOT NULL,
  wtype character varying(10),
  spreadinputfirstone character varying(255),
  spreadinputfirsttwo character varying(255),
  spreadinputjuicefirstone character varying(255),
  spreadinputjuicefirsttwo character varying(255),
  spreadinputjuicesecondone character varying(255),
  spreadinputjuicesecondtwo character varying(255),
  spreadinputsecondone character varying(255),
  spreadinputsecondtwo character varying(255),
  spreadjuiceplusminusfirstone character varying(255),
  spreadjuiceplusminusfirsttwo character varying(255),
  spreadjuiceplusminussecondone character varying(255),
  spreadjuiceplusminussecondtwo character varying(255),
  spreadplusminusfirstone character varying(255),
  spreadplusminusfirsttwo character varying(255),
  spreadplusminussecondone character varying(255),
  spreadplusminussecondtwo character varying(255),
  scrappername character varying(100),
  actiontype character varying(10),
  textnumber character varying(200),
  rotationid integer,
  CONSTRAINT spreadrecordevents_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.spreadrecordevents
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.spreadrecordevents TO public;


-- Index: public.spreadrecordevents_datecreated_idx

-- DROP INDEX public.spreadrecordevents_datecreated_idx;

CREATE INDEX spreadrecordevents_datecreated_idx
  ON public.spreadrecordevents
  USING btree
  (datecreated);

-- Index: public.spreadrecordevents_datentime_idx

-- DROP INDEX public.spreadrecordevents_datentime_idx;

CREATE INDEX spreadrecordevents_datentime_idx
  ON public.spreadrecordevents
  USING btree
  (datentime);

-- Index: public.spreadrecordevents_eventdatetime_idx

-- DROP INDEX public.spreadrecordevents_eventdatetime_idx;

CREATE INDEX spreadrecordevents_eventdatetime_idx
  ON public.spreadrecordevents
  USING btree
  (eventdatetime);

-- Index: public.spreadrecordevents_eventid_idx

-- DROP INDEX public.spreadrecordevents_eventid_idx;

CREATE INDEX spreadrecordevents_eventid_idx
  ON public.spreadrecordevents
  USING btree
  (eventid);

-- Index: public.spreadrecordevents_userid_idx

-- DROP INDEX public.spreadrecordevents_userid_idx;

CREATE INDEX spreadrecordevents_userid_idx
  ON public.spreadrecordevents
  USING btree
  (userid);

-- Table: public.totalrecordevents

-- DROP TABLE public.totalrecordevents;

CREATE TABLE public.totalrecordevents
(
  id bigint NOT NULL DEFAULT nextval('totalrecordevents_id_seq'::regclass),
  accountid bigint,
  amount character varying(10),
  attempts integer NOT NULL,
  attempttime timestamp with time zone,
  currentattempts integer,
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone,
  datentime timestamp with time zone NOT NULL,
  eventdatetime timestamp with time zone NOT NULL,
  eventid integer NOT NULL,
  eventid1 integer,
  eventid2 integer,
  eventname character varying(100),
  eventteam1 character varying(100),
  eventteam2 character varying(100),
  eventtype character varying(100) NOT NULL,
  groupid bigint,
  iscompleted boolean NOT NULL,
  sport character varying(100) NOT NULL,
  userid bigint NOT NULL,
  wtype character varying(10),
  totalinputfirstone character varying(255),
  totalinputfirsttwo character varying(255),
  totalinputjuicefirstone character varying(255),
  totalinputjuicefirsttwo character varying(255),
  totalinputjuicesecondone character varying(255),
  totalinputjuicesecondtwo character varying(255),
  totalinputsecondone character varying(255),
  totalinputsecondtwo character varying(255),
  totaljuiceplusminusfirstone character varying(255),
  totaljuiceplusminusfirsttwo character varying(255),
  totaljuiceplusminussecondone character varying(255),
  totaljuiceplusminussecondtwo character varying(255),
  scrappername character varying(100),
  actiontype character varying(10),
  textnumber character varying(200),
  rotationid integer,
  CONSTRAINT totalrecordevents_pkey PRIMARY KEY (id, datecreated, datentime, eventdatetime, eventid, userid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.totalrecordevents
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.totalrecordevents TO public;


-- Index: public.totalrecordevents_datecreated_idx

-- DROP INDEX public.totalrecordevents_datecreated_idx;

CREATE INDEX totalrecordevents_datecreated_idx
  ON public.totalrecordevents
  USING btree
  (datecreated);

-- Index: public.totalrecordevents_datentime_idx

-- DROP INDEX public.totalrecordevents_datentime_idx;

CREATE INDEX totalrecordevents_datentime_idx
  ON public.totalrecordevents
  USING btree
  (datentime);

-- Index: public.totalrecordevents_eventdatetime_idx

-- DROP INDEX public.totalrecordevents_eventdatetime_idx;

CREATE INDEX totalrecordevents_eventdatetime_idx
  ON public.totalrecordevents
  USING btree
  (eventdatetime);

-- Index: public.totalrecordevents_eventid_idx

-- DROP INDEX public.totalrecordevents_eventid_idx;

CREATE INDEX totalrecordevents_eventid_idx
  ON public.totalrecordevents
  USING btree
  (eventid);

-- Index: public.totalrecordevents_userid_idx

-- DROP INDEX public.totalrecordevents_userid_idx;

CREATE INDEX totalrecordevents_userid_idx
  ON public.totalrecordevents
  USING btree
  (userid);

-- Table: public.userbilling

-- DROP TABLE public.userbilling;

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
  OIDS=FALSE
);
ALTER TABLE public.userbilling
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.userbilling TO public;


-- Index: public.userbilling_userid_idx

-- DROP INDEX public.userbilling_userid_idx;

CREATE INDEX userbilling_userid_idx
  ON public.userbilling
  USING btree
  (userid);

-- Index: public.userbilling_week_idx

-- DROP INDEX public.userbilling_week_idx;

CREATE INDEX userbilling_week_idx
  ON public.userbilling
  USING btree
  (weekstartdate);

-- Table: public.usersaccounts

-- DROP TABLE public.usersaccounts;

CREATE TABLE public.usersaccounts
(
  usersid bigint NOT NULL,
  accountsid bigint NOT NULL,
  CONSTRAINT usersaccounts_pkey PRIMARY KEY (usersid, accountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usersaccounts
  OWNER TO tauser;
GRANT SELECT(usersid), UPDATE(usersid), INSERT(usersid), REFERENCES(usersid) ON public.usersaccounts TO public;

-- Table: public.usersemailaccounts

-- DROP TABLE public.usersemailaccounts;

CREATE TABLE public.usersemailaccounts
(
  usersid bigint NOT NULL,
  emailaccountsid bigint NOT NULL,
  CONSTRAINT usersemailaccounts_pkey PRIMARY KEY (usersid, emailaccountsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usersemailaccounts
  OWNER TO tauser;
GRANT SELECT(usersid), UPDATE(usersid), INSERT(usersid), REFERENCES(usersid) ON public.usersemailaccounts TO public;

-- Table: public.usersgroups

-- DROP TABLE public.usersgroups;

CREATE TABLE public.usersgroups
(
  usersid bigint NOT NULL,
  groupsid bigint NOT NULL,
  CONSTRAINT usersgroups_pkey PRIMARY KEY (usersid, groupsid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usersgroups
  OWNER TO tauser;
GRANT SELECT(usersid), UPDATE(usersid), INSERT(usersid), REFERENCES(usersid) ON public.usersgroups TO public;

-- Table: public.usersta

-- DROP TABLE public.usersta;

CREATE TABLE public.usersta
(
  id bigint NOT NULL DEFAULT nextval('usersta_id_seq'::regclass),
  id bigint NOT NULL DEFAULT nextval('usersta_id_seq'::regclass),
  datecreated timestamp with time zone NOT NULL,
  datemodified timestamp with time zone NOT NULL,
  email character varying(100),
  isactive boolean NOT NULL,
  mobilenumber character varying(300),
  password character varying(50),
  username character varying(50) NOT NULL,
  CONSTRAINT usersta_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.usersta
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.usersta TO public;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.usersta TO public;


-- Index: public.usersta_isactive_idx

-- DROP INDEX public.usersta_isactive_idx;

CREATE INDEX usersta_isactive_idx
  ON public.usersta
  USING btree
  (isactive);

-- Index: public.usersta_username_idx

-- DROP INDEX public.usersta_username_idx;

CREATE INDEX usersta_username_idx
  ON public.usersta
  USING btree
  (username COLLATE pg_catalog."default");

-- Table: public.webscrapper

-- DROP TABLE public.webscrapper;

CREATE TABLE public.webscrapper
(
  id bigint NOT NULL DEFAULT nextval('webscrapper_id_seq'::regclass),
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
  middlerules boolean,
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
  onoff boolean NOT NULL,
  internationalbaseballspreadonoff boolean NOT NULL DEFAULT true,
  internationalbaseballtotalonoff boolean NOT NULL DEFAULT true,
  internationalbaseballmlonoff boolean NOT NULL DEFAULT true,
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
  CONSTRAINT webscrapper_pkey PRIMARY KEY (id, scrappername, userid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.webscrapper
  OWNER TO tauser;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.webscrapper TO public;


-- Index: public.webscrapper_name_idx

-- DROP INDEX public.webscrapper_name_idx;

CREATE INDEX webscrapper_name_idx
  ON public.webscrapper
  USING btree
  (scrappername COLLATE pg_catalog."default");

-- Index: public.webscrapper_userid_idx

-- DROP INDEX public.webscrapper_userid_idx;

CREATE INDEX webscrapper_userid_idx
  ON public.webscrapper
  USING btree
  (userid);

psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy accountsta TO './accountsta.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy emailaccountsta TO './emailaccountsta.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy emailmiddledestinations TO './emailmiddledestinations.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy emailorderdestinations TO './emailorderdestinations.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy emailscrapper TO './emailscrapper.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy emailscrapperdestinations TO './emailscrapperdestinations.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy emailscrappersources TO './emailscrappersources.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy groupsaccounts TO './groupsaccounts.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy groupsta TO './groupsta.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy hibernate_sequences TO './hibernate_sequences.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy middledestinations TO './middledestinations.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy orderdestinations TO './orderdestinations.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy scrapperdestinations TO './scrapperdestinations.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy scrappersources TO './scrappersources.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy usersaccounts TO './usersaccounts.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy usersemailaccounts TO './usersemailaccounts.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy usersgroups TO './usersgroups.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy usersta TO './usersta.csv' with (format csv,header true, delimiter ',');"
psql -h ec2-54-87-137-30.compute-1.amazonaws.com -p 38293 -U postgres -d tadatabase -c "\copy webscrapper TO './usersta.csv' with (format csv,header true, delimiter ',');"
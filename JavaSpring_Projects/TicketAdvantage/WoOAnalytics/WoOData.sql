-- Table: public.espnbasketballgame

-- DROP TABLE public.espnbasketballgame;

CREATE TABLE public.espnbasketballgame
(
  espngameid integer NOT NULL,
  week integer,
  hour integer,
  minute integer,
  ampm character varying(2),
  timezone character varying(3),
  month integer,
  day integer,
  year integer,
  gamedate timestamp with time zone,
  tv character varying(50),
  city character varying(150),
  state character varying(50),
  zipcode integer,
  eventlocation character varying(120),
  isconferencegame boolean,
  neutralsite boolean,
  linefavorite character varying(120),
  lineindicator character varying(2),
  linevalue double precision,
  line double precision,
  total double precision,
  attendance integer,
  awaycollegename character varying(120),
  homecollegename character varying(120),
  awaymascotname character varying(100),
  homemascotname character varying(100),
  awayshortname character varying(50),
  homeshortname character varying(50),
  awayranking integer,
  homeranking integer,
  awaywins integer,
  homewins integer,
  awaylosses integer,
  homelosses integer,
  awayfirstquarterscore integer,
  homefirstquarterscore integer,
  awaysecondquarterscore integer,
  homesecondquarterscore integer,
  awayfirsthalfscore integer,
  homefirsthalfscore integer,
  awaythirdquarterscore integer,
  homethirdquarterscore integer,
  awayfourthquarterscore integer,
  homefourthquarterscore integer,
  awaysecondhalfscore integer,
  homesecondhalfscore integer,
  awayotonescore integer,
  homeotonescore integer,
  awayottwoscore integer,
  homeottwoscore integer,
  awayotthreescore integer,
  homeotthreescore integer,
  awayotfourscore integer,
  homeotfourscore integer,
  awayotfivescore integer,
  homeotfivescore integer,
  awaysecondhalfotscore integer,
  homesecondhalfotscore integer,
  awayfinalscore integer,
  homefinalscore integer,
  awayfgmade integer,
  homefgmade integer,
  awayfgattempt integer,
  homefgattempt integer,
  awayfgpercentage double precision,
  homefgpercentage double precision,
  away3ptfgmade integer,
  home3ptfgmade integer,
  away3ptfgattempt integer,
  home3ptfgattempt integer,
  away3ptfgpercentage double precision,
  home3ptfgpercentage double precision,
  awayftmade integer,
  homeftmade integer,
  awayftattempt integer,
  homeftattempt integer,
  awayftpercentage double precision,
  homeftpercentage double precision,
  awaytotalrebounds integer,
  hometotalrebounds integer,
  awayoffrebounds integer,
  homeoffrebounds integer,
  awaydefrebounds integer,
  homedefrebounds integer,
  awayassists integer,
  homeassists integer,
  awaysteals integer,
  homesteals integer,
  awayblocks integer,
  homeblocks integer,
  awaytotalturnovers integer,
  hometotalturnovers integer,
  awaypersonalfouls integer,
  homepersonalfouls integer,
  awaytechnicalfouls integer,
  hometechnicalfouls integer,
  ref1 character varying(100),
  ref2 character varying(100),
  ref3 character varying(100),
  datecreated timestamp with time zone,
  datemodified timestamp with time zone,
  CONSTRAINT emailevent_pkey PRIMARY KEY (espngameid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.espnbasketballgame
  OWNER TO wooanalytics;
GRANT SELECT(espngameid), UPDATE(espngameid), INSERT(espngameid), REFERENCES(espngameid) ON public.espnbasketballgame TO public;


-- Index: public.ampm

-- DROP INDEX public.ampm;

CREATE INDEX ampm
  ON public.espnbasketballgame
  USING btree
  (ampm COLLATE pg_catalog."default");

-- Index: public.attendance

-- DROP INDEX public.attendance;

CREATE INDEX attendance
  ON public.espnbasketballgame
  USING btree
  (attendance);

-- Index: public.away3ptfgattempt

-- DROP INDEX public.away3ptfgattempt;

CREATE INDEX away3ptfgattempt
  ON public.espnbasketballgame
  USING btree
  (away3ptfgattempt);

-- Index: public.away3ptfgmade

-- DROP INDEX public.away3ptfgmade;

CREATE INDEX away3ptfgmade
  ON public.espnbasketballgame
  USING btree
  (away3ptfgmade);

-- Index: public.away3ptfgpercentage

-- DROP INDEX public.away3ptfgpercentage;

CREATE INDEX away3ptfgpercentage
  ON public.espnbasketballgame
  USING btree
  (away3ptfgpercentage);

-- Index: public.awayassists

-- DROP INDEX public.awayassists;

CREATE INDEX awayassists
  ON public.espnbasketballgame
  USING btree
  (awayassists);

-- Index: public.awayblocks

-- DROP INDEX public.awayblocks;

CREATE INDEX awayblocks
  ON public.espnbasketballgame
  USING btree
  (awayblocks);

-- Index: public.awaycollegename

-- DROP INDEX public.awaycollegename;

CREATE INDEX awaycollegename
  ON public.espnbasketballgame
  USING btree
  (awaycollegename COLLATE pg_catalog."default");

-- Index: public.awaydefrebounds

-- DROP INDEX public.awaydefrebounds;

CREATE INDEX awaydefrebounds
  ON public.espnbasketballgame
  USING btree
  (awaydefrebounds);

-- Index: public.awayfgattempt

-- DROP INDEX public.awayfgattempt;

CREATE INDEX awayfgattempt
  ON public.espnbasketballgame
  USING btree
  (awayfgattempt);

-- Index: public.awayfgmade

-- DROP INDEX public.awayfgmade;

CREATE INDEX awayfgmade
  ON public.espnbasketballgame
  USING btree
  (awayfgmade);

-- Index: public.awayfgpercentage

-- DROP INDEX public.awayfgpercentage;

CREATE INDEX awayfgpercentage
  ON public.espnbasketballgame
  USING btree
  (awayfgpercentage);

-- Index: public.awayfinalscore

-- DROP INDEX public.awayfinalscore;

CREATE INDEX awayfinalscore
  ON public.espnbasketballgame
  USING btree
  (awayfinalscore);

-- Index: public.awayfirsthalfscore

-- DROP INDEX public.awayfirsthalfscore;

CREATE INDEX awayfirsthalfscore
  ON public.espnbasketballgame
  USING btree
  (awayfirsthalfscore);

-- Index: public.awayfirstquarterscore

-- DROP INDEX public.awayfirstquarterscore;

CREATE INDEX awayfirstquarterscore
  ON public.espnbasketballgame
  USING btree
  (awayfirstquarterscore);

-- Index: public.awayfourthquarterscore

-- DROP INDEX public.awayfourthquarterscore;

CREATE INDEX awayfourthquarterscore
  ON public.espnbasketballgame
  USING btree
  (awayfourthquarterscore);

-- Index: public.awayftattempt

-- DROP INDEX public.awayftattempt;

CREATE INDEX awayftattempt
  ON public.espnbasketballgame
  USING btree
  (awayftattempt);

-- Index: public.awayftmade

-- DROP INDEX public.awayftmade;

CREATE INDEX awayftmade
  ON public.espnbasketballgame
  USING btree
  (awayftmade);

-- Index: public.awayftpercentage

-- DROP INDEX public.awayftpercentage;

CREATE INDEX awayftpercentage
  ON public.espnbasketballgame
  USING btree
  (awayftpercentage);

-- Index: public.awaylosses

-- DROP INDEX public.awaylosses;

CREATE INDEX awaylosses
  ON public.espnbasketballgame
  USING btree
  (awaylosses);

-- Index: public.awaymascotname

-- DROP INDEX public.awaymascotname;

CREATE INDEX awaymascotname
  ON public.espnbasketballgame
  USING btree
  (awaymascotname COLLATE pg_catalog."default");

-- Index: public.awayoffrebounds

-- DROP INDEX public.awayoffrebounds;

CREATE INDEX awayoffrebounds
  ON public.espnbasketballgame
  USING btree
  (awayoffrebounds);

-- Index: public.awaypersonalfouls

-- DROP INDEX public.awaypersonalfouls;

CREATE INDEX awaypersonalfouls
  ON public.espnbasketballgame
  USING btree
  (awaypersonalfouls);

-- Index: public.awayranking

-- DROP INDEX public.awayranking;

CREATE INDEX awayranking
  ON public.espnbasketballgame
  USING btree
  (awayranking);

-- Index: public.awaysecondhalfscore

-- DROP INDEX public.awaysecondhalfscore;

CREATE INDEX awaysecondhalfscore
  ON public.espnbasketballgame
  USING btree
  (awaysecondhalfscore);

-- Index: public.awaysecondquarterscore

-- DROP INDEX public.awaysecondquarterscore;

CREATE INDEX awaysecondquarterscore
  ON public.espnbasketballgame
  USING btree
  (awaysecondquarterscore);

-- Index: public.awayshortname

-- DROP INDEX public.awayshortname;

CREATE INDEX awayshortname
  ON public.espnbasketballgame
  USING btree
  (awayshortname COLLATE pg_catalog."default");

-- Index: public.awaysteals

-- DROP INDEX public.awaysteals;

CREATE INDEX awaysteals
  ON public.espnbasketballgame
  USING btree
  (awaysteals);

-- Index: public.awaytechnicalfouls

-- DROP INDEX public.awaytechnicalfouls;

CREATE INDEX awaytechnicalfouls
  ON public.espnbasketballgame
  USING btree
  (awaytechnicalfouls);

-- Index: public.awaythirdquarterscore

-- DROP INDEX public.awaythirdquarterscore;

CREATE INDEX awaythirdquarterscore
  ON public.espnbasketballgame
  USING btree
  (awaythirdquarterscore);

-- Index: public.awaytotalrebounds

-- DROP INDEX public.awaytotalrebounds;

CREATE INDEX awaytotalrebounds
  ON public.espnbasketballgame
  USING btree
  (awaytotalrebounds);

-- Index: public.awaytotalturnovers

-- DROP INDEX public.awaytotalturnovers;

CREATE INDEX awaytotalturnovers
  ON public.espnbasketballgame
  USING btree
  (awaytotalturnovers);

-- Index: public.awaywins

-- DROP INDEX public.awaywins;

CREATE INDEX awaywins
  ON public.espnbasketballgame
  USING btree
  (awaywins);

-- Index: public.city

-- DROP INDEX public.city;

CREATE INDEX city
  ON public.espnbasketballgame
  USING btree
  (city COLLATE pg_catalog."default");

-- Index: public.gamedate

-- DROP INDEX public.gamedate;

CREATE INDEX gamedate
  ON public.espnbasketballgame
  USING btree
  (gamedate);

-- Index: public.day

-- DROP INDEX public.day;

CREATE INDEX day
  ON public.espnbasketballgame
  USING btree
  (day);

-- Index: public.home3ptfgattempt

-- DROP INDEX public.home3ptfgattempt;

CREATE INDEX home3ptfgattempt
  ON public.espnbasketballgame
  USING btree
  (home3ptfgattempt);

-- Index: public.home3ptfgmade

-- DROP INDEX public.home3ptfgmade;

CREATE INDEX home3ptfgmade
  ON public.espnbasketballgame
  USING btree
  (home3ptfgmade);

-- Index: public.home3ptfgpercentage

-- DROP INDEX public.home3ptfgpercentage;

CREATE INDEX home3ptfgpercentage
  ON public.espnbasketballgame
  USING btree
  (home3ptfgpercentage);

-- Index: public.homeassists

-- DROP INDEX public.homeassists;

CREATE INDEX homeassists
  ON public.espnbasketballgame
  USING btree
  (homeassists);

-- Index: public.homeblocks

-- DROP INDEX public.homeblocks;

CREATE INDEX homeblocks
  ON public.espnbasketballgame
  USING btree
  (homeblocks);

-- Index: public.homecollegename

-- DROP INDEX public.homecollegename;

CREATE INDEX homecollegename
  ON public.espnbasketballgame
  USING btree
  (homecollegename COLLATE pg_catalog."default");

-- Index: public.homedefrebounds

-- DROP INDEX public.homedefrebounds;

CREATE INDEX homedefrebounds
  ON public.espnbasketballgame
  USING btree
  (homedefrebounds);

-- Index: public.homefgattempt

-- DROP INDEX public.homefgattempt;

CREATE INDEX homefgattempt
  ON public.espnbasketballgame
  USING btree
  (homefgattempt);

-- Index: public.homefgmade

-- DROP INDEX public.homefgmade;

CREATE INDEX homefgmade
  ON public.espnbasketballgame
  USING btree
  (homefgmade);

-- Index: public.homefgpercentage

-- DROP INDEX public.homefgpercentage;

CREATE INDEX homefgpercentage
  ON public.espnbasketballgame
  USING btree
  (homefgpercentage);

-- Index: public.homefinalscore

-- DROP INDEX public.homefinalscore;

CREATE INDEX homefinalscore
  ON public.espnbasketballgame
  USING btree
  (homefinalscore);

-- Index: public.homefirsthalfscore

-- DROP INDEX public.homefirsthalfscore;

CREATE INDEX homefirsthalfscore
  ON public.espnbasketballgame
  USING btree
  (homefirsthalfscore);

-- Index: public.homefirstquarterscore

-- DROP INDEX public.homefirstquarterscore;

CREATE INDEX homefirstquarterscore
  ON public.espnbasketballgame
  USING btree
  (homefirstquarterscore);

-- Index: public.homefourthquarterscore

-- DROP INDEX public.homefourthquarterscore;

CREATE INDEX homefourthquarterscore
  ON public.espnbasketballgame
  USING btree
  (homefourthquarterscore);

-- Index: public.homeftattempt

-- DROP INDEX public.homeftattempt;

CREATE INDEX homeftattempt
  ON public.espnbasketballgame
  USING btree
  (homeftattempt);

-- Index: public.homeftmade

-- DROP INDEX public.homeftmade;

CREATE INDEX homeftmade
  ON public.espnbasketballgame
  USING btree
  (homeftmade);

-- Index: public.homeftpercentage

-- DROP INDEX public.homeftpercentage;

CREATE INDEX homeftpercentage
  ON public.espnbasketballgame
  USING btree
  (homeftpercentage);

-- Index: public.homelosses

-- DROP INDEX public.homelosses;

CREATE INDEX homelosses
  ON public.espnbasketballgame
  USING btree
  (homelosses);

-- Index: public.homemascotname

-- DROP INDEX public.homemascotname;

CREATE INDEX homemascotname
  ON public.espnbasketballgame
  USING btree
  (homemascotname COLLATE pg_catalog."default");

-- Index: public.homeoffrebounds

-- DROP INDEX public.homeoffrebounds;

CREATE INDEX homeoffrebounds
  ON public.espnbasketballgame
  USING btree
  (homeoffrebounds);

-- Index: public.homepersonalfouls

-- DROP INDEX public.homepersonalfouls;

CREATE INDEX homepersonalfouls
  ON public.espnbasketballgame
  USING btree
  (homepersonalfouls);

-- Index: public.homeranking

-- DROP INDEX public.homeranking;

CREATE INDEX homeranking
  ON public.espnbasketballgame
  USING btree
  (homeranking);

-- Index: public.homesecondhalfscore

-- DROP INDEX public.homesecondhalfscore;

CREATE INDEX homesecondhalfscore
  ON public.espnbasketballgame
  USING btree
  (homesecondhalfscore);

-- Index: public.homesecondquarterscore

-- DROP INDEX public.homesecondquarterscore;

CREATE INDEX homesecondquarterscore
  ON public.espnbasketballgame
  USING btree
  (homesecondquarterscore);

-- Index: public.homeshortname

-- DROP INDEX public.homeshortname;

CREATE INDEX homeshortname
  ON public.espnbasketballgame
  USING btree
  (homeshortname COLLATE pg_catalog."default");

-- Index: public.homesteals

-- DROP INDEX public.homesteals;

CREATE INDEX homesteals
  ON public.espnbasketballgame
  USING btree
  (homesteals);

-- Index: public.hometechnicalfouls

-- DROP INDEX public.hometechnicalfouls;

CREATE INDEX hometechnicalfouls
  ON public.espnbasketballgame
  USING btree
  (hometechnicalfouls);

-- Index: public.homethirdquarterscore

-- DROP INDEX public.homethirdquarterscore;

CREATE INDEX homethirdquarterscore
  ON public.espnbasketballgame
  USING btree
  (homethirdquarterscore);

-- Index: public.hometotalrebounds

-- DROP INDEX public.hometotalrebounds;

CREATE INDEX hometotalrebounds
  ON public.espnbasketballgame
  USING btree
  (hometotalrebounds);

-- Index: public.hometotalturnovers

-- DROP INDEX public.hometotalturnovers;

CREATE INDEX hometotalturnovers
  ON public.espnbasketballgame
  USING btree
  (hometotalturnovers);

-- Index: public.homewins

-- DROP INDEX public.homewins;

CREATE INDEX homewins
  ON public.espnbasketballgame
  USING btree
  (homewins);

-- Index: public.hour

-- DROP INDEX public.hour;

CREATE INDEX hour
  ON public.espnbasketballgame
  USING btree
  (hour);

-- Index: public.iespngameid

-- DROP INDEX public.iespngameid;

CREATE INDEX iespngameid
  ON public.espnbasketballgame
  USING btree
  (espngameid);

-- Index: public.isconferencegame

-- DROP INDEX public.isconferencegame;

CREATE INDEX isconferencegame
  ON public.espnbasketballgame
  USING btree
  (isconferencegame);

-- Index: public.line

-- DROP INDEX public.line;

CREATE INDEX line
  ON public.espnbasketballgame
  USING btree
  (line);

-- Index: public.linefavorite

-- DROP INDEX public.linefavorite;

CREATE INDEX linefavorite
  ON public.espnbasketballgame
  USING btree
  (linefavorite COLLATE pg_catalog."default");

-- Index: public.lineindicator

-- DROP INDEX public.lineindicator;

CREATE INDEX lineindicator
  ON public.espnbasketballgame
  USING btree
  (lineindicator COLLATE pg_catalog."default");

-- Index: public.linevalue

-- DROP INDEX public.linevalue;

CREATE INDEX linevalue
  ON public.espnbasketballgame
  USING btree
  (linevalue);

-- Index: public.minute

-- DROP INDEX public.minute;

CREATE INDEX minute
  ON public.espnbasketballgame
  USING btree
  (minute);

-- Index: public.month

-- DROP INDEX public.month;

CREATE INDEX month
  ON public.espnbasketballgame
  USING btree
  (month);

-- Index: public.neutralsite

-- DROP INDEX public.neutralsite;

CREATE INDEX neutralsite
  ON public.espnbasketballgame
  USING btree
  (neutralsite);

-- Index: public.ref1

-- DROP INDEX public.ref1;

CREATE INDEX ref1
  ON public.espnbasketballgame
  USING btree
  (ref1 COLLATE pg_catalog."default");

-- Index: public.ref2

-- DROP INDEX public.ref2;

CREATE INDEX ref2
  ON public.espnbasketballgame
  USING btree
  (ref2 COLLATE pg_catalog."default");

-- Index: public.ref3

-- DROP INDEX public.ref3;

CREATE INDEX ref3
  ON public.espnbasketballgame
  USING btree
  (ref3 COLLATE pg_catalog."default");

-- Index: public.eventlocation

-- DROP INDEX public.eventlocation;

CREATE INDEX eventlocation
  ON public.espnbasketballgame
  USING btree
  (eventlocation COLLATE pg_catalog."default");

-- Index: public.state

-- DROP INDEX public.state;

CREATE INDEX state
  ON public.espnbasketballgame
  USING btree
  (state COLLATE pg_catalog."default");

-- Index: public.timezone

-- DROP INDEX public.timezone;

CREATE INDEX timezone
  ON public.espnbasketballgame
  USING btree
  (timezone COLLATE pg_catalog."default");

-- Index: public.total

-- DROP INDEX public.total;

CREATE INDEX total
  ON public.espnbasketballgame
  USING btree
  (total);

-- Index: public.tv

-- DROP INDEX public.tv;

CREATE INDEX tv
  ON public.espnbasketballgame
  USING btree
  (tv COLLATE pg_catalog."default");

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX week
  ON public.espnbasketballgame
  USING btree
  (week);

-- Index: public.zipcode

-- DROP INDEX public.zipcode;

CREATE INDEX zipcode
  ON public.espnbasketballgame
  USING btree
  (zipcode);
  
CREATE INDEX awayotonescore
  ON public.espnbasketballgame
  USING btree
  (awayotonescore);

CREATE INDEX homeotonescore
  ON public.espnbasketballgame
  USING btree
  (homeotonescore);

CREATE INDEX awayottwoscore
  ON public.espnbasketballgame
  USING btree
  (awayotonescore);

CREATE INDEX homeottwoscore
  ON public.espnbasketballgame
  USING btree
  (homeotonescore);

CREATE INDEX awayotthreescore
  ON public.espnbasketballgame
  USING btree
  (awayotonescore);

CREATE INDEX homeotthreescore
  ON public.espnbasketballgame
  USING btree
  (homeotonescore);

CREATE INDEX awayotfourscore
  ON public.espnbasketballgame
  USING btree
  (awayotonescore);

CREATE INDEX homeotfourscore
  ON public.espnbasketballgame
  USING btree
  (homeotonescore);

CREATE INDEX awayotfivescore
  ON public.espnbasketballgame
  USING btree
  (awayotonescore);

CREATE INDEX homeotfivescore
  ON public.espnbasketballgame
  USING btree
  (homeotonescore);

CREATE INDEX awaysecondhalfotscore
  ON public.espnbasketballgame
  USING btree
  (awayotonescore);

CREATE INDEX homesecondhalfotscore
  ON public.espnbasketballgame
  USING btree
  (homeotonescore);
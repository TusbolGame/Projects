-- Table: public.espnfootballgame

-- DROP TABLE public.espnfootballgame;

CREATE TABLE public.espnfootballgame
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
  awaywin boolean,
  homewin boolean,
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
  awayotsixscore integer,
  homeotsixscore integer,
  awaysecondhalfotscore integer,
  homesecondhalfotscore integer,
  awayfinalscore integer,
  homefinalscore integer,
  awayfirstdowns integer,
  homefirstdowns integer,
  awaythirdefficiencymade integer,
  homethirdefficiencymade integer,
  awaythirdefficiencyattempts integer,
  homethirdefficiencyattempts integer,
  awayfourthefficiencymade integer,
  homefourthefficiencymade integer,
  awayfourthefficiencyattempts integer,
  homefourthefficiencyattempts integer,
  awaytotalyards integer,
  hometotalyards integer,
  awaypassingyards integer,
  homepassingyards integer,
  awaypasscomp integer,
  homepasscomp integer,
  awaypassattempts integer,
  homepassattempts integer,
  awayyardsperpass double precision,
  homeyardsperpass double precision,
  awayrushingyards integer,
  homerushingyards integer,
  awayrushingattempts integer,
  homerushingattempts integer,
  awayyardsperrush double precision,
  homeyardsperrush double precision,
  awaypenalties integer,
  homepenalties integer,
  awaypenaltyyards integer,
  homepenaltyyards integer,
  awayturnovers integer,
  hometurnovers integer,
  awayfumbleslost integer,
  homefumbleslost integer,
  awayinterceptions integer,
  homeinterceptions integer,
  awaypossessionminutes integer,
  homepossessionminutes integer,
  awaypossessionseconds integer,
  homepossessionseconds integer,
  awaysagrinrating double precision,
  homesagrinrating double precision,
  awaymasseyrating double precision,
  homemasseyrating double precision,
  awaymasseyranking integer,
  homemasseyranking integer,
  awaysos double precision,
  homesos double precision,
  awayisfbs boolean,
  homeisfbs boolean,
  datecreated timestamp with time zone,
  datemodified timestamp with time zone,
  CONSTRAINT espnfootballgameid_pkey PRIMARY KEY (espngameid, week, year)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.espnfootballgame
  OWNER TO wooanalytics;
GRANT SELECT(espngameid), UPDATE(espngameid), INSERT(espngameid), REFERENCES(espngameid) ON public.espnfootballgame TO public;


-- Index: public.ampmf

-- DROP INDEX public.ampmf;

CREATE INDEX ampmf
  ON public.espnfootballgame
  USING btree
  (ampm COLLATE pg_catalog."default");

-- Index: public.attendancef

-- DROP INDEX public.attendancef;

CREATE INDEX attendancef
  ON public.espnfootballgame
  USING btree
  (attendance);

-- Index: public.awaycollegenamef

-- DROP INDEX public.awaycollegenamef;

CREATE INDEX awaycollegenamef
  ON public.espnfootballgame
  USING btree
  (awaycollegename COLLATE pg_catalog."default");

-- Index: public.awayfinalscoref

-- DROP INDEX public.awayfinalscoref;

CREATE INDEX awayfinalscoref
  ON public.espnfootballgame
  USING btree
  (awayfinalscore);

-- Index: public.awayfirstdowns

-- DROP INDEX public.awayfirstdowns;

CREATE INDEX awayfirstdowns
  ON public.espnfootballgame
  USING btree
  (awayfirstdowns);

-- Index: public.awayfirsthalfscoref

-- DROP INDEX public.awayfirsthalfscoref;

CREATE INDEX awayfirsthalfscoref
  ON public.espnfootballgame
  USING btree
  (awayfirsthalfscore);

-- Index: public.awayfirstquarterscoref

-- DROP INDEX public.awayfirstquarterscoref;

CREATE INDEX awayfirstquarterscoref
  ON public.espnfootballgame
  USING btree
  (awayfirstquarterscore);

-- Index: public.awayfourthefficiencyattempts

-- DROP INDEX public.awayfourthefficiencyattempts;

CREATE INDEX awayfourthefficiencyattempts
  ON public.espnfootballgame
  USING btree
  (awayfourthefficiencyattempts);

-- Index: public.awayfourthefficiencymade

-- DROP INDEX public.awayfourthefficiencymade;

CREATE INDEX awayfourthefficiencymade
  ON public.espnfootballgame
  USING btree
  (awayfourthefficiencymade);

-- Index: public.awayfourthquarterscoref

-- DROP INDEX public.awayfourthquarterscoref;

CREATE INDEX awayfourthquarterscoref
  ON public.espnfootballgame
  USING btree
  (awayfourthquarterscore);

-- Index: public.awayfumbleslost

-- DROP INDEX public.awayfumbleslost;

CREATE INDEX awayfumbleslost
  ON public.espnfootballgame
  USING btree
  (awayfumbleslost);

-- Index: public.awayinterceptions

-- DROP INDEX public.awayinterceptions;

CREATE INDEX awayinterceptions
  ON public.espnfootballgame
  USING btree
  (awayinterceptions);

-- Index: public.awaylossesf

-- DROP INDEX public.awaylossesf;

CREATE INDEX awaylossesf
  ON public.espnfootballgame
  USING btree
  (awaylosses);

-- Index: public.awaymascotnamef

-- DROP INDEX public.awaymascotnamef;

CREATE INDEX awaymascotnamef
  ON public.espnfootballgame
  USING btree
  (awaymascotname COLLATE pg_catalog."default");

-- Index: public.awayotfivescoref

-- DROP INDEX public.awayotfivescoref;

CREATE INDEX awayotfivescoref
  ON public.espnfootballgame
  USING btree
  (awayotfivescore);

-- Index: public.awayotfourscoref

-- DROP INDEX public.awayotfourscoref;

CREATE INDEX awayotfourscoref
  ON public.espnfootballgame
  USING btree
  (awayotfourscore);

-- Index: public.awayotonescoref

-- DROP INDEX public.awayotonescoref;

CREATE INDEX awayotonescoref
  ON public.espnfootballgame
  USING btree
  (awayotonescore);

-- Index: public.awayotthreescoref

-- DROP INDEX public.awayotthreescoref;

CREATE INDEX awayotthreescoref
  ON public.espnfootballgame
  USING btree
  (awayotthreescore);

-- Index: public.awayottwoscoref

-- DROP INDEX public.awayottwoscoref;

CREATE INDEX awayottwoscoref
  ON public.espnfootballgame
  USING btree
  (awayottwoscore);

-- Index: public.awaypassattempts

-- DROP INDEX public.awaypassattempts;

CREATE INDEX awaypassattempts
  ON public.espnfootballgame
  USING btree
  (awaypassattempts);

-- Index: public.awaypasscomp

-- DROP INDEX public.awaypasscomp;

CREATE INDEX awaypasscomp
  ON public.espnfootballgame
  USING btree
  (awaypasscomp);

-- Index: public.awaypassingyards

-- DROP INDEX public.awaypassingyards;

CREATE INDEX awaypassingyards
  ON public.espnfootballgame
  USING btree
  (awaypassingyards);

-- Index: public.awaypenalties

-- DROP INDEX public.awaypenalties;

CREATE INDEX awaypenalties
  ON public.espnfootballgame
  USING btree
  (awaypenalties);

-- Index: public.awaypenaltyyards

-- DROP INDEX public.awaypenaltyyards;

CREATE INDEX awaypenaltyyards
  ON public.espnfootballgame
  USING btree
  (awaypenaltyyards);

-- Index: public.awaypossessionminutes

-- DROP INDEX public.awaypossessionminutes;

CREATE INDEX awaypossessionminutes
  ON public.espnfootballgame
  USING btree
  (awaypossessionminutes);

-- Index: public.awaypossessionseconds

-- DROP INDEX public.awaypossessionseconds;

CREATE INDEX awaypossessionseconds
  ON public.espnfootballgame
  USING btree
  (awaypossessionseconds);

-- Index: public.awayrankingf

-- DROP INDEX public.awayrankingf;

CREATE INDEX awayrankingf
  ON public.espnfootballgame
  USING btree
  (awayranking);

-- Index: public.awayrushingattempts

-- DROP INDEX public.awayrushingattempts;

CREATE INDEX awayrushingattempts
  ON public.espnfootballgame
  USING btree
  (awayrushingattempts);

-- Index: public.awayrushingyards

-- DROP INDEX public.awayrushingyards;

CREATE INDEX awayrushingyards
  ON public.espnfootballgame
  USING btree
  (awayrushingyards);

-- Index: public.awaysecondhalfotscoref

-- DROP INDEX public.awaysecondhalfotscoref;

CREATE INDEX awaysecondhalfotscoref
  ON public.espnfootballgame
  USING btree
  (awayotonescore);

-- Index: public.awaysecondhalfscoref

-- DROP INDEX public.awaysecondhalfscoref;

CREATE INDEX awaysecondhalfscoref
  ON public.espnfootballgame
  USING btree
  (awaysecondhalfscore);

-- Index: public.awaysecondquarterscoref

-- DROP INDEX public.awaysecondquarterscoref;

CREATE INDEX awaysecondquarterscoref
  ON public.espnfootballgame
  USING btree
  (awaysecondquarterscore);

-- Index: public.awayshortnamef

-- DROP INDEX public.awayshortnamef;

CREATE INDEX awayshortnamef
  ON public.espnfootballgame
  USING btree
  (awayshortname COLLATE pg_catalog."default");

-- Index: public.awaythirdefficiencyattempts

-- DROP INDEX public.awaythirdefficiencyattempts;

CREATE INDEX awaythirdefficiencyattempts
  ON public.espnfootballgame
  USING btree
  (awaythirdefficiencyattempts);

-- Index: public.awaythirdefficiencymade

-- DROP INDEX public.awaythirdefficiencymade;

CREATE INDEX awaythirdefficiencymade
  ON public.espnfootballgame
  USING btree
  (awaythirdefficiencymade);

-- Index: public.awaythirdquarterscoref

-- DROP INDEX public.awaythirdquarterscoref;

CREATE INDEX awaythirdquarterscoref
  ON public.espnfootballgame
  USING btree
  (awaythirdquarterscore);

-- Index: public.awaytotalyards

-- DROP INDEX public.awaytotalyards;

CREATE INDEX awaytotalyards
  ON public.espnfootballgame
  USING btree
  (awaytotalyards);

-- Index: public.awayturnovers

-- DROP INDEX public.awayturnovers;

CREATE INDEX awayturnovers
  ON public.espnfootballgame
  USING btree
  (awayturnovers);

-- Index: public.awaywinf

-- DROP INDEX public.awaywinf;

CREATE INDEX awaywinf
  ON public.espnfootballgame
  USING btree
  (awaywin);

-- Index: public.awaywinsf

-- DROP INDEX public.awaywinsf;

CREATE INDEX awaywinsf
  ON public.espnfootballgame
  USING btree
  (awaywins);

-- Index: public.awayyardsperpass

-- DROP INDEX public.awayyardsperpass;

CREATE INDEX awayyardsperpass
  ON public.espnfootballgame
  USING btree
  (awayyardsperpass);

-- Index: public.awayyardsperrush

-- DROP INDEX public.awayyardsperrush;

CREATE INDEX awayyardsperrush
  ON public.espnfootballgame
  USING btree
  (awayyardsperrush);

-- Index: public.cityf

-- DROP INDEX public.cityf;

CREATE INDEX cityf
  ON public.espnfootballgame
  USING btree
  (city COLLATE pg_catalog."default");

-- Index: public.datef

-- DROP INDEX public.datef;

CREATE INDEX gamedatef
  ON public.espnfootballgame
  USING btree
  (gamedate);

-- Index: public.dayf

-- DROP INDEX public.dayf;

CREATE INDEX dayf
  ON public.espnfootballgame
  USING btree
  (day);

-- Index: public.homecollegenamef

-- DROP INDEX public.homecollegenamef;

CREATE INDEX homecollegenamef
  ON public.espnfootballgame
  USING btree
  (homecollegename COLLATE pg_catalog."default");

-- Index: public.homefinalscoref

-- DROP INDEX public.homefinalscoref;

CREATE INDEX homefinalscoref
  ON public.espnfootballgame
  USING btree
  (homefinalscore);

-- Index: public.homefirstdowns

-- DROP INDEX public.homefirstdowns;

CREATE INDEX homefirstdowns
  ON public.espnfootballgame
  USING btree
  (homefirstdowns);

-- Index: public.homefirsthalfscoref

-- DROP INDEX public.homefirsthalfscoref;

CREATE INDEX homefirsthalfscoref
  ON public.espnfootballgame
  USING btree
  (homefirsthalfscore);

-- Index: public.homefirstquarterscoref

-- DROP INDEX public.homefirstquarterscoref;

CREATE INDEX homefirstquarterscoref
  ON public.espnfootballgame
  USING btree
  (homefirstquarterscore);

-- Index: public.homefourthefficiencyattempts

-- DROP INDEX public.homefourthefficiencyattempts;

CREATE INDEX homefourthefficiencyattempts
  ON public.espnfootballgame
  USING btree
  (homefourthefficiencyattempts);

-- Index: public.homefourthefficiencymade

-- DROP INDEX public.homefourthefficiencymade;

CREATE INDEX homefourthefficiencymade
  ON public.espnfootballgame
  USING btree
  (homefourthefficiencymade);

-- Index: public.homefourthquarterscoref

-- DROP INDEX public.homefourthquarterscoref;

CREATE INDEX homefourthquarterscoref
  ON public.espnfootballgame
  USING btree
  (homefourthquarterscore);

-- Index: public.homefumbleslost

-- DROP INDEX public.homefumbleslost;

CREATE INDEX homefumbleslost
  ON public.espnfootballgame
  USING btree
  (homefumbleslost);

-- Index: public.homeinterceptions

-- DROP INDEX public.homeinterceptions;

CREATE INDEX homeinterceptions
  ON public.espnfootballgame
  USING btree
  (homeinterceptions);

-- Index: public.homelossesf

-- DROP INDEX public.homelossesf;

CREATE INDEX homelossesf
  ON public.espnfootballgame
  USING btree
  (homelosses);

-- Index: public.homemascotnamef

-- DROP INDEX public.homemascotnamef;

CREATE INDEX homemascotnamef
  ON public.espnfootballgame
  USING btree
  (homemascotname COLLATE pg_catalog."default");

-- Index: public.homeotfivescoref

-- DROP INDEX public.homeotfivescoref;

CREATE INDEX homeotfivescoref
  ON public.espnfootballgame
  USING btree
  (homeotfivescore);

-- Index: public.homeotfourscoref

-- DROP INDEX public.homeotfourscoref;

CREATE INDEX homeotfourscoref
  ON public.espnfootballgame
  USING btree
  (homeotfourscore);

-- Index: public.homeotonescoref

-- DROP INDEX public.homeotonescoref;

CREATE INDEX homeotonescoref
  ON public.espnfootballgame
  USING btree
  (homeotonescore);

-- Index: public.homeotsixscoref

-- DROP INDEX public.homeotsixscoref;

CREATE INDEX homeotsixscoref
  ON public.espnfootballgame
  USING btree
  (homeotsixscore);

-- Index: public.homeotthreescoref

-- DROP INDEX public.homeotthreescoref;

CREATE INDEX homeotthreescoref
  ON public.espnfootballgame
  USING btree
  (homeotthreescore);

-- Index: public.homeottwoscoref

-- DROP INDEX public.homeottwoscoref;

CREATE INDEX homeottwoscoref
  ON public.espnfootballgame
  USING btree
  (homeottwoscore);

-- Index: public.homepassattempts

-- DROP INDEX public.homepassattempts;

CREATE INDEX homepassattempts
  ON public.espnfootballgame
  USING btree
  (homepassattempts);

-- Index: public.homepasscomp

-- DROP INDEX public.homepasscomp;

CREATE INDEX homepasscomp
  ON public.espnfootballgame
  USING btree
  (homepasscomp);

-- Index: public.homepassingyards

-- DROP INDEX public.homepassingyards;

CREATE INDEX homepassingyards
  ON public.espnfootballgame
  USING btree
  (homepassingyards);

-- Index: public.homepenalties

-- DROP INDEX public.homepenalties;

CREATE INDEX homepenalties
  ON public.espnfootballgame
  USING btree
  (homepenalties);

-- Index: public.homepenaltyyards

-- DROP INDEX public.homepenaltyyards;

CREATE INDEX homepenaltyyards
  ON public.espnfootballgame
  USING btree
  (homepenaltyyards);

-- Index: public.homepossessionminutes

-- DROP INDEX public.homepossessionminutes;

CREATE INDEX homepossessionminutes
  ON public.espnfootballgame
  USING btree
  (homepossessionminutes);

-- Index: public.homepossessionseconds

-- DROP INDEX public.homepossessionseconds;

CREATE INDEX homepossessionseconds
  ON public.espnfootballgame
  USING btree
  (homepossessionseconds);

-- Index: public.homerankingf

-- DROP INDEX public.homerankingf;

CREATE INDEX homerankingf
  ON public.espnfootballgame
  USING btree
  (homeranking);

-- Index: public.homerushingattempts

-- DROP INDEX public.homerushingattempts;

CREATE INDEX homerushingattempts
  ON public.espnfootballgame
  USING btree
  (homerushingattempts);

-- Index: public.homerushingyards

-- DROP INDEX public.homerushingyards;

CREATE INDEX homerushingyards
  ON public.espnfootballgame
  USING btree
  (homerushingyards);

-- Index: public.homesecondhalfotscoref

-- DROP INDEX public.homesecondhalfotscoref;

CREATE INDEX homesecondhalfotscoref
  ON public.espnfootballgame
  USING btree
  (homeotonescore);

-- Index: public.homesecondhalfscoref

-- DROP INDEX public.homesecondhalfscoref;

CREATE INDEX homesecondhalfscoref
  ON public.espnfootballgame
  USING btree
  (homesecondhalfscore);

-- Index: public.homesecondquarterscoref

-- DROP INDEX public.homesecondquarterscoref;

CREATE INDEX homesecondquarterscoref
  ON public.espnfootballgame
  USING btree
  (homesecondquarterscore);

-- Index: public.homeshortnamef

-- DROP INDEX public.homeshortnamef;

CREATE INDEX homeshortnamef
  ON public.espnfootballgame
  USING btree
  (homeshortname COLLATE pg_catalog."default");

-- Index: public.homethirdefficiencyattempts

-- DROP INDEX public.homethirdefficiencyattempts;

CREATE INDEX homethirdefficiencyattempts
  ON public.espnfootballgame
  USING btree
  (homethirdefficiencyattempts);

-- Index: public.homethirdefficiencymade

-- DROP INDEX public.homethirdefficiencymade;

CREATE INDEX homethirdefficiencymade
  ON public.espnfootballgame
  USING btree
  (homethirdefficiencymade);

-- Index: public.homethirdquarterscoref

-- DROP INDEX public.homethirdquarterscoref;

CREATE INDEX homethirdquarterscoref
  ON public.espnfootballgame
  USING btree
  (homethirdquarterscore);

-- Index: public.hometotalyards

-- DROP INDEX public.hometotalyards;

CREATE INDEX hometotalyards
  ON public.espnfootballgame
  USING btree
  (hometotalyards);

-- Index: public.hometurnovers

-- DROP INDEX public.hometurnovers;

CREATE INDEX hometurnovers
  ON public.espnfootballgame
  USING btree
  (hometurnovers);

-- Index: public.homewinf

-- DROP INDEX public.homewinf;

CREATE INDEX homewinf
  ON public.espnfootballgame
  USING btree
  (homewin);

-- Index: public.homewinsf

-- DROP INDEX public.homewinsf;

CREATE INDEX homewinsf
  ON public.espnfootballgame
  USING btree
  (homewins);

-- Index: public.homeyardsperpass

-- DROP INDEX public.homeyardsperpass;

CREATE INDEX homeyardsperpass
  ON public.espnfootballgame
  USING btree
  (homeyardsperpass);

-- Index: public.homeyardsperrush

-- DROP INDEX public.homeyardsperrush;

CREATE INDEX homeyardsperrush
  ON public.espnfootballgame
  USING btree
  (homeyardsperrush);

-- Index: public.hourf

-- DROP INDEX public.hourf;

CREATE INDEX hourf
  ON public.espnfootballgame
  USING btree
  (hour);

-- Index: public.iespngameidf

-- DROP INDEX public.iespngameidf;

CREATE INDEX iespngameidf
  ON public.espnfootballgame
  USING btree
  (espngameid);

-- Index: public.isconferencegamef

-- DROP INDEX public.isconferencegamef;

CREATE INDEX isconferencegamef
  ON public.espnfootballgame
  USING btree
  (isconferencegame);

-- Index: public.linef

-- DROP INDEX public.linef;

CREATE INDEX linef
  ON public.espnfootballgame
  USING btree
  (line);

-- Index: public.linefavoritef

-- DROP INDEX public.linefavoritef;

CREATE INDEX linefavoritef
  ON public.espnfootballgame
  USING btree
  (linefavorite COLLATE pg_catalog."default");

-- Index: public.lineindicatorf

-- DROP INDEX public.lineindicatorf;

CREATE INDEX lineindicatorf
  ON public.espnfootballgame
  USING btree
  (lineindicator COLLATE pg_catalog."default");

-- Index: public.linevaluef

-- DROP INDEX public.linevaluef;

CREATE INDEX linevaluef
  ON public.espnfootballgame
  USING btree
  (linevalue);

-- Index: public.minutef

-- DROP INDEX public.minutef;

CREATE INDEX minutef
  ON public.espnfootballgame
  USING btree
  (minute);

-- Index: public.monthf

-- DROP INDEX public.monthf;

CREATE INDEX monthf
  ON public.espnfootballgame
  USING btree
  (month);

-- Index: public.neutralsitef

-- DROP INDEX public.neutralsitef;

CREATE INDEX neutralsitef
  ON public.espnfootballgame
  USING btree
  (neutralsite);

-- Index: public.stadiumf

-- DROP INDEX public.stadiumf;

CREATE INDEX eventlocationf
  ON public.espnfootballgame
  USING btree
  (eventlocation COLLATE pg_catalog."default");

-- Index: public.statef

-- DROP INDEX public.statef;

CREATE INDEX statef
  ON public.espnfootballgame
  USING btree
  (state COLLATE pg_catalog."default");

-- Index: public.timezonef

-- DROP INDEX public.timezonef;

CREATE INDEX timezonef
  ON public.espnfootballgame
  USING btree
  (timezone COLLATE pg_catalog."default");

-- Index: public.totalf

-- DROP INDEX public.totalf;

CREATE INDEX totalf
  ON public.espnfootballgame
  USING btree
  (total);

-- Index: public.tvf

-- DROP INDEX public.tvf;

CREATE INDEX tvf
  ON public.espnfootballgame
  USING btree
  (tv COLLATE pg_catalog."default");

-- Index: public.weekf

-- DROP INDEX public.weekf;

CREATE INDEX weekf
  ON public.espnfootballgame
  USING btree
  (week);

-- Index: public.zipcodef

-- DROP INDEX public.zipcodef;

CREATE INDEX zipcodef
  ON public.espnfootballgame
  USING btree
  (zipcode);

-- Index: public.awaysagrinratingf

-- DROP INDEX public.awaysagrinratingf;

CREATE INDEX awaysagrinratingf
  ON public.espnfootballgame
  USING btree
  (awaysagrinrating);

-- Index: public.homesagrinratingf

-- DROP INDEX public.homesagrinratingf;

CREATE INDEX homesagrinratingf
  ON public.espnfootballgame
  USING btree
  (homesagrinrating);

-- Index: public.awaymasseyratingf

-- DROP INDEX public.awaymasseyratingf;

CREATE INDEX awaymasseyratingf
  ON public.espnfootballgame
  USING btree
  (awaymasseyrating);
  
-- Index: public.homemasseyratingf

-- DROP INDEX public.homemasseyratingf;

CREATE INDEX homemasseyratingf
  ON public.espnfootballgame
  USING btree
  (homemasseyrating);

-- Index: public.awaymasseyrankingf

-- DROP INDEX public.awaymasseyrankingf;

CREATE INDEX awaymasseyrankingf
  ON public.espnfootballgame
  USING btree
  (awaymasseyranking);
  
-- Index: public.homemasseyrankingf

-- DROP INDEX public.homemasseyrankingf;

CREATE INDEX homemasseyrankingf
  ON public.espnfootballgame
  USING btree
  (homemasseyranking);

-- Index: public.awaysosf

-- DROP INDEX public.awaysosf;

CREATE INDEX awaysosf
  ON public.espnfootballgame
  USING btree
  (awaysos);

-- Index: public.homesosf

-- DROP INDEX public.homesosf;

CREATE INDEX homesosf
  ON public.espnfootballgame
  USING btree
  (homesos);

-- Index: public.awayisfbsf

-- DROP INDEX public.awayisfbsf;

CREATE INDEX awayisfbsf
  ON public.espnfootballgame
  USING btree
  (awayisfbs);

-- Index: public.homeisfbsf

-- DROP INDEX public.homeisfbsf;

CREATE INDEX homeisfbsf
  ON public.espnfootballgame
  USING btree
  (homeisfbs);

CREATE TABLE public.footballfirstdownspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  firstdownspergame double precision,
  oppfirstdownspergame double precision,
  firstdownspergametotal double precision,
  oppfirstdownspergametotal double precision,
  numgames integer,
  CONSTRAINT footballfirstdownspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballfirstdownspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekffdpg
  ON public.footballfirstdownspergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearffdpg
  ON public.footballfirstdownspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenameffdpg
  ON public.footballfirstdownspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.firstdownspergame

-- DROP INDEX public.firstdownspergame;

CREATE INDEX firstdownspergameffdpg
  ON public.footballfirstdownspergame
  USING btree
  (firstdownspergame);

-- Index: public.oppfirstdownspergame

-- DROP INDEX public.oppfirstdownspergame;

CREATE INDEX oppfirstdownspergameffdpg
  ON public.footballfirstdownspergame
  USING btree
  (oppfirstdownspergame);

CREATE INDEX stattypeffdpg
  ON public.footballfirstdownspergame
  USING btree
  (stattype);

CREATE TABLE public.footballfourthdowneffpergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  fourthdowneffpergame double precision,
  oppfourthdowneffpergame double precision,
  fourthdowneffpergametotal double precision,
  oppfourthdowneffpergametotal double precision,
  numgames double precision,
  made double precision,
  attempts double precision,
  oppmade double precision,
  oppattempts double precision,
  madetotal double precision,
  attemptstotal double precision,
  oppmadetotal double precision,
  oppattemptstotal double precision,
  CONSTRAINT footballfourthdowneffpergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballfourthdowneffpergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekffdepg
  ON public.footballfourthdowneffpergame
  USING btree
  (week);

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX yearffdepg
  ON public.footballfourthdowneffpergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenameffdepg
  ON public.footballfourthdowneffpergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.fourthdowneffpergame

-- DROP INDEX public.fourthdowneffpergame;

CREATE INDEX fourthdowneffpergameffdepg
  ON public.footballfourthdowneffpergame
  USING btree
  (fourthdowneffpergame);

-- Index: public.oppfourthdowneffpergame

-- DROP INDEX public.oppfourthdowneffpergame;

CREATE INDEX oppfourthdowneffpergameffdepg
  ON public.footballfourthdowneffpergame
  USING btree
  (oppfourthdowneffpergame);

CREATE INDEX stattypeffepg
  ON public.footballfourthdowneffpergame
  USING btree
  (stattype);

CREATE TABLE public.footballpassattemptspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  passattemptspergame double precision,
  opppassattemptspergame double precision,
  passattemptspergametotal double precision,
  opppassattemptspergametotal double precision,
  numgames integer,
  CONSTRAINT footballpassattemptspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballpassattemptspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfpapg
  ON public.footballpassattemptspergame
  USING btree
  (week);

-- Index: public.collegename

-- DROP INDEX public.collegename;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX yearfpapg
  ON public.footballpassattemptspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefpapg
  ON public.footballpassattemptspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.passattemptspergame

-- DROP INDEX public.passattemptspergame;

CREATE INDEX passattemptspergamefpapg
  ON public.footballpassattemptspergame
  USING btree
  (passattemptspergame);

-- Index: public.opppassattemptspergame

-- DROP INDEX public.opppassattemptspergame;

CREATE INDEX opppassattemptspergamefpapg
  ON public.footballpassattemptspergame
  USING btree
  (opppassattemptspergame);

CREATE INDEX stattypefpapg
  ON public.footballpassattemptspergame
  USING btree
  (stattype);

CREATE TABLE public.footballpasscompletionspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  passcompletionspergame double precision,
  opppasscompletionspergame double precision,
  passcompletionspergametotal double precision,
  opppasscompletionspergametotal double precision,
  numgames integer,
  CONSTRAINT footballpasscompletionspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballpasscompletionspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (week);

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX yearfpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.passcompletionspergame

-- DROP INDEX public.passcompletionspergame;

CREATE INDEX passcompletionspergamefpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (passcompletionspergame);

-- Index: public.opppasscompletionspergame

-- DROP INDEX public.opppasscompletionspergame;

CREATE INDEX opppasscompletionspergamefpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (opppasscompletionspergame);

CREATE INDEX stattypefpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (stattype);

CREATE TABLE public.footballpassyardspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  passyardspergame double precision,
  opppassyardspergame double precision,
  passyardspergametotal double precision,
  opppassyardspergametotal double precision,
  numgames integer,
  CONSTRAINT footballpassyardspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballpassyardspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfpypg
  ON public.footballpassyardspergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearfpypg
  ON public.footballpassyardspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefpypg
  ON public.footballpassyardspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.passyardspergame

-- DROP INDEX public.passyardspergame;

CREATE INDEX passyardspergamefpypg
  ON public.footballpassyardspergame
  USING btree
  (passyardspergame);

-- Index: public.opppassyardspergame

-- DROP INDEX public.opppassyardspergame;

CREATE INDEX opppassyardspergamefpypg
  ON public.footballpassyardspergame
  USING btree
  (opppassyardspergame);
  
CREATE INDEX stattypefpypg
  ON public.footballpassyardspergame
  USING btree
  (stattype);

CREATE TABLE public.footballpointspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  pointspergame double precision,
  opppointspergame double precision,
  pointspergametotal double precision,
  opppointspergametotal double precision,
  numgames integer,
  CONSTRAINT footballpointspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballpointspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfppg
  ON public.footballpointspergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearfppg
  ON public.footballpointspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefppg
  ON public.footballpointspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.pointspergame

-- DROP INDEX public.pointspergame;

CREATE INDEX pointspergamefppg
  ON public.footballpointspergame
  USING btree
  (pointspergame);

-- Index: public.opppointspergame

-- DROP INDEX public.opppointspergame;

CREATE INDEX opppointspergamefppg
  ON public.footballpointspergame
  USING btree
  (opppointspergame);

CREATE INDEX stattypefppg
  ON public.footballpointspergame
  USING btree
  (stattype);

CREATE TABLE public.footballpossessiontimepergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  possessiontimepergame double precision,
  opppossessiontimepergame double precision,
  possessiontimepergametotal double precision,
  opppossessiontimepergametotal double precision,
  numgames integer,
  CONSTRAINT footballpossessiontimepergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballpossessiontimepergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfptpg
  ON public.footballpossessiontimepergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearfptpg
  ON public.footballpossessiontimepergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefptpg
  ON public.footballpossessiontimepergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.possessiontimepergame

-- DROP INDEX public.possessiontimepergame;

CREATE INDEX possessiontimepergamefptpg
  ON public.footballpossessiontimepergame
  USING btree
  (possessiontimepergame);

-- Index: public.opppossessiontimepergame

-- DROP INDEX public.opppossessiontimepergame;

CREATE INDEX opppossessiontimepergamefptpg
  ON public.footballpossessiontimepergame
  USING btree
  (opppossessiontimepergame);

CREATE INDEX stattypefptpg
  ON public.footballpossessiontimepergame
  USING btree
  (stattype);

CREATE TABLE public.footballrushingattemptspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  rushingattemptspergame double precision,
  opprushingattemptspergame double precision,
  rushingattemptspergametotal double precision,
  opprushingattemptspergametotal double precision,
  numgames integer,
  CONSTRAINT footballrushingattemptspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballrushingattemptspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfrapg
  ON public.footballrushingattemptspergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearfrapg
  ON public.footballrushingattemptspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefrapg
  ON public.footballrushingattemptspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.rushingattemptspergame

-- DROP INDEX public.rushingattemptspergame;

CREATE INDEX rushingattemptspergamefrapg
  ON public.footballrushingattemptspergame
  USING btree
  (rushingattemptspergame);

-- Index: public.opprushingattemptspergame

-- DROP INDEX public.opprushingattemptspergame;

CREATE INDEX opprushingattemptspergamefrapg
  ON public.footballrushingattemptspergame
  USING btree
  (opprushingattemptspergame);

CREATE INDEX stattypefrapg
  ON public.footballrushingattemptspergame
  USING btree
  (stattype);

CREATE TABLE public.footballrushingyardspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  rushingyardspergame double precision,
  opprushingyardspergame double precision,
  rushingyardspergametotal double precision,
  opprushingyardspergametotal double precision,
  numgames integer,
  CONSTRAINT footballrushingyardspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballrushingyardspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfrypg
  ON public.footballrushingyardspergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearfrypg
  ON public.footballrushingyardspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefrypg
  ON public.footballrushingyardspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.rushingyardspergame

-- DROP INDEX public.rushingyardspergame;

CREATE INDEX rushingyardspergamefrypg
  ON public.footballrushingyardspergame
  USING btree
  (rushingyardspergame);

-- Index: public.oppfirstdownspergame

-- DROP INDEX public.oppfirstdownspergame;

CREATE INDEX opprushingyardspergamefrypg
  ON public.footballrushingyardspergame
  USING btree
  (opprushingyardspergame);

CREATE INDEX stattypefrypg
  ON public.footballrushingyardspergame
  USING btree
  (stattype);

CREATE TABLE public.footballthirddowneffpergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  thirddowneffpergame double precision,
  oppthirddowneffpergame double precision,
  thirddowneffpergametotal double precision,
  oppthirddowneffpergametotal double precision,
  numgames integer,
  made double precision,
  attempts double precision,
  oppmade double precision,
  oppattempts double precision,
  madetotal double precision,
  attemptstotal double precision,
  oppmadetotal double precision,
  oppattemptstotal double precision,
  CONSTRAINT footballthirddowneffpergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballthirddowneffpergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekftdepg
  ON public.footballthirddowneffpergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearftdepg
  ON public.footballthirddowneffpergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenameftdepg
  ON public.footballthirddowneffpergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.thirddowneffpergame

-- DROP INDEX public.thirddowneffpergame;

CREATE INDEX thirddowneffpergameftdepg
  ON public.footballthirddowneffpergame
  USING btree
  (thirddowneffpergame);

-- Index: public.oppthirddowneffpergame

-- DROP INDEX public.oppthirddowneffpergame;

CREATE INDEX oppthirddowneffpergameftdepg
  ON public.footballthirddowneffpergame
  USING btree
  (oppthirddowneffpergame);

CREATE INDEX stattypeftdepg
  ON public.footballthirddowneffpergame
  USING btree
  (stattype);

CREATE TABLE public.footballtotalyardspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  totalyardspergame double precision,
  opptotalyardspergame double precision,
  totalyardspergametotal double precision,
  opptotalyardspergametotal double precision,
  numgames integer,
  CONSTRAINT footballtotalyardspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballtotalyardspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekftypg
  ON public.footballtotalyardspergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearftypg
  ON public.footballtotalyardspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenameftypg
  ON public.footballtotalyardspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.totalyardspergame

-- DROP INDEX public.totalyardspergame;

CREATE INDEX totalyardspergameftypg
  ON public.footballtotalyardspergame
  USING btree
  (totalyardspergame);

-- Index: public.opptotalyardspergame

-- DROP INDEX public.opptotalyardspergame;

CREATE INDEX opptotalyardspergameftypg
  ON public.footballtotalyardspergame
  USING btree
  (opptotalyardspergame);

CREATE INDEX stattypeftypg
  ON public.footballtotalyardspergame
  USING btree
  (stattype);

CREATE TABLE public.footballturnoverspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  turnoverspergame double precision,
  oppturnoverspergame double precision,
  turnoverspergametotal double precision,
  oppturnoverspergametotal double precision,
  numgames integer,
  CONSTRAINT footballturnoverspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballturnoverspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekftpg
  ON public.footballturnoverspergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearftpg
  ON public.footballturnoverspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenameftpg
  ON public.footballturnoverspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.turnoverspergame

-- DROP INDEX public.turnoverspergame;

CREATE INDEX turnoverspergameftpg
  ON public.footballturnoverspergame
  USING btree
  (turnoverspergame);

-- Index: public.oppturnoverspergame

-- DROP INDEX public.oppturnoverspergame;

CREATE INDEX oppturnoverspergameftpg
  ON public.footballturnoverspergame
  USING btree
  (oppturnoverspergame);

CREATE INDEX stattypeftpg
  ON public.footballturnoverspergame
  USING btree
  (stattype);

CREATE TABLE public.footballyardsperpass
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  yardsperpass double precision,
  oppyardsperpass double precision,
  yardsperpasstotal double precision,
  oppyardsperpasstotal double precision,
  numgames integer,
  CONSTRAINT footballyardsperpass_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballyardsperpass OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfypp
  ON public.footballyardsperpass
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearfypp
  ON public.footballyardsperpass
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefypp
  ON public.footballyardsperpass
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.yardsperpass

-- DROP INDEX public.yardsperpass;

CREATE INDEX yardsperpassfypp
  ON public.footballyardsperpass
  USING btree
  (yardsperpass);

-- Index: public.oppyardsperpass

-- DROP INDEX public.oppyardsperpass;

CREATE INDEX oppyardsperpassfypp
  ON public.footballyardsperpass
  USING btree
  (oppyardsperpass);

CREATE INDEX stattypefypp
  ON public.footballyardsperpass
  USING btree
  (stattype);

CREATE TABLE public.footballyardsperrush
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  yardsperrush double precision,
  oppyardsperrush double precision,
  yardsperrushtotal double precision,
  oppyardsperrushtotal double precision,
  numgames integer,
  CONSTRAINT footballyardsperrush_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballyardsperrush OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfypr
  ON public.footballyardsperrush
  USING btree
  (week);

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX yearfypr
  ON public.footballyardsperrush
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefypr
  ON public.footballyardsperrush
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.yardsperrush

-- DROP INDEX public.yardsperrush;

CREATE INDEX yardsperrushfypr
  ON public.footballyardsperrush
  USING btree
  (yardsperrush);

-- Index: public.oppyardsperrush

-- DROP INDEX public.oppyardsperrush;

CREATE INDEX oppyardsperrushfypr
  ON public.footballyardsperrush
  USING btree
  (oppyardsperrush);

CREATE INDEX stattypefypr
  ON public.footballyardsperrush
  USING btree
  (stattype);
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
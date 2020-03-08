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
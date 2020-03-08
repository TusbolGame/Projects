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
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
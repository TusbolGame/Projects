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
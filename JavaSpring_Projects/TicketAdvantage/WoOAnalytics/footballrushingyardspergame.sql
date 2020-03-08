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
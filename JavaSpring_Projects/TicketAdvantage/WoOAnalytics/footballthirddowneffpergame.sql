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
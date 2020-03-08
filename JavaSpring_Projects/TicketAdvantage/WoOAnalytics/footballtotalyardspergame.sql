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
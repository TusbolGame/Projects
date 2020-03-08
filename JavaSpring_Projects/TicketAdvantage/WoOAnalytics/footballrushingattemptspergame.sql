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
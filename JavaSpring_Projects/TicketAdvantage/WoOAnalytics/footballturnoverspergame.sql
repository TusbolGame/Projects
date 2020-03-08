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
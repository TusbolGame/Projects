CREATE TABLE public.footballpasscompletionspergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  passcompletionspergame double precision,
  opppasscompletionspergame double precision,
  passcompletionspergametotal double precision,
  opppasscompletionspergametotal double precision,
  numgames integer,
  CONSTRAINT footballpasscompletionspergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballpasscompletionspergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (week);

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX yearfpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.passcompletionspergame

-- DROP INDEX public.passcompletionspergame;

CREATE INDEX passcompletionspergamefpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (passcompletionspergame);

-- Index: public.opppasscompletionspergame

-- DROP INDEX public.opppasscompletionspergame;

CREATE INDEX opppasscompletionspergamefpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (opppasscompletionspergame);

CREATE INDEX stattypefpcpg
  ON public.footballpasscompletionspergame
  USING btree
  (stattype);
CREATE TABLE public.footballpossessiontimepergame
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  possessiontimepergame double precision,
  opppossessiontimepergame double precision,
  possessiontimepergametotal double precision,
  opppossessiontimepergametotal double precision,
  numgames integer,
  CONSTRAINT footballpossessiontimepergame_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballpossessiontimepergame OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfptpg
  ON public.footballpossessiontimepergame
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearfptpg
  ON public.footballpossessiontimepergame
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefptpg
  ON public.footballpossessiontimepergame
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.possessiontimepergame

-- DROP INDEX public.possessiontimepergame;

CREATE INDEX possessiontimepergamefptpg
  ON public.footballpossessiontimepergame
  USING btree
  (possessiontimepergame);

-- Index: public.opppossessiontimepergame

-- DROP INDEX public.opppossessiontimepergame;

CREATE INDEX opppossessiontimepergamefptpg
  ON public.footballpossessiontimepergame
  USING btree
  (opppossessiontimepergame);

CREATE INDEX stattypefptpg
  ON public.footballpossessiontimepergame
  USING btree
  (stattype);
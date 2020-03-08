CREATE TABLE public.footballyardsperrush
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  yardsperrush double precision,
  oppyardsperrush double precision,
  yardsperrushtotal double precision,
  oppyardsperrushtotal double precision,
  numgames integer,
  CONSTRAINT footballyardsperrush_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballyardsperrush OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfypr
  ON public.footballyardsperrush
  USING btree
  (week);

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX yearfypr
  ON public.footballyardsperrush
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefypr
  ON public.footballyardsperrush
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.yardsperrush

-- DROP INDEX public.yardsperrush;

CREATE INDEX yardsperrushfypr
  ON public.footballyardsperrush
  USING btree
  (yardsperrush);

-- Index: public.oppyardsperrush

-- DROP INDEX public.oppyardsperrush;

CREATE INDEX oppyardsperrushfypr
  ON public.footballyardsperrush
  USING btree
  (oppyardsperrush);

CREATE INDEX stattypefypr
  ON public.footballyardsperrush
  USING btree
  (stattype);
CREATE TABLE public.footballyardsperpass
(
  week integer,
  year integer,
  stattype character varying(40),
  collegename character varying(120),
  yardsperpass double precision,
  oppyardsperpass double precision,
  yardsperpasstotal double precision,
  oppyardsperpasstotal double precision,
  numgames integer,
  CONSTRAINT footballyardsperpass_pkey PRIMARY KEY (week, year, stattype, collegename)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.footballyardsperpass OWNER TO tauser;

-- Index: public.week

-- DROP INDEX public.week;

CREATE INDEX weekfypp
  ON public.footballyardsperpass
  USING btree
  (week);

-- Index: public.year

-- DROP INDEX public.year;

CREATE INDEX yearfypp
  ON public.footballyardsperpass
  USING btree
  (year);

-- Index: public.collegename

-- DROP INDEX public.collegename;

CREATE INDEX collegenamefypp
  ON public.footballyardsperpass
  USING btree
  (collegename COLLATE pg_catalog."default");

-- Index: public.yardsperpass

-- DROP INDEX public.yardsperpass;

CREATE INDEX yardsperpassfypp
  ON public.footballyardsperpass
  USING btree
  (yardsperpass);

-- Index: public.oppyardsperpass

-- DROP INDEX public.oppyardsperpass;

CREATE INDEX oppyardsperpassfypp
  ON public.footballyardsperpass
  USING btree
  (oppyardsperpass);

CREATE INDEX stattypefypp
  ON public.footballyardsperpass
  USING btree
  (stattype);
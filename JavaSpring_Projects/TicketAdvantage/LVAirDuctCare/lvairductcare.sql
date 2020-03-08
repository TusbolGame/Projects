-- Sequence: public.calendardata_id_seq

-- DROP SEQUENCE public.calendardata_id_seq;

CREATE SEQUENCE public.calendardata_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.calendardata_id_seq
  OWNER TO lvairductcare;

-- Table: public.calendardata

-- DROP TABLE public.calendardata;

CREATE TABLE public.calendardata
(
  id bigint NOT NULL DEFAULT nextval('calendardata_id_seq'::regclass),
  firstname character varying(100),
  lastname character varying(100),
  phone character varying(12) NOT NULL,
  email character varying(150),
  address character varying(400) NOT NULL,
  city character varying(100) NOT NULL,
  zipcode integer NOT NULL,
  servicetype character varying(100) NOT NULL,
  serviceunits character varying(10) NOT NULL,
  message character varying(500),
  appointmentstarttime integer NOT NULL,
  appointmentendtime integer NOT NULL,
  appointmentday character varying(10) NOT NULL,
  CONSTRAINT calendardata_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.calendardata
  OWNER TO lvairductcare;
GRANT SELECT(id), UPDATE(id), INSERT(id), REFERENCES(id) ON public.calendardata TO public;

-- Index: public.calendardata_appointmentday_idx

-- DROP INDEX public.calendardata_appointmentday_idx;

CREATE INDEX calendardata_appointmentday_idx
  ON public.calendardata
  USING btree
  (appointmentday COLLATE pg_catalog."default");

-- Index: public.calendardata_appointmentendtime_idx

-- DROP INDEX public.calendardata_appointmentendtime_idx;

CREATE INDEX calendardata_appointmentendtime_idx
  ON public.calendardata
  USING btree
  (appointmentendtime);

-- Index: public.calendardata_appointmentstarttime_idx

-- DROP INDEX public.calendardata_appointmentstarttime_idx;

CREATE INDEX calendardata_appointmentstarttime_idx
  ON public.calendardata
  USING btree
  (appointmentstarttime);

-- Index: public.calendardata_phone_idx

-- DROP INDEX public.calendardata_phone_idx;

CREATE INDEX calendardata_phone_idx
  ON public.calendardata
  USING btree
  (phone COLLATE pg_catalog."default");
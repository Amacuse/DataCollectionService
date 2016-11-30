# --- !Ups
CREATE TABLE public.field
(
  id        BIGSERIAL             NOT NULL,
  label     CHARACTER VARYING(30) NOT NULL,
  required  BOOLEAN               NOT NULL,
  is_active BOOLEAN               NOT NULL,
  type      CHARACTER VARYING(30) NOT NULL,
  PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.field
  OWNER TO postgres;

# --- !Downs
DROP TABLE public.field;
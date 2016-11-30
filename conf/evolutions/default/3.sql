# --- !Ups
CREATE TABLE public.admin_data
(
  id       BIGSERIAL              NOT NULL,
  content  CHARACTER VARYING(255) NOT NULL,
  field_id BIGINT                 NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT field_id FOREIGN KEY (field_id)
  REFERENCES public.field (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.admin_data
  OWNER TO postgres;

# --- !Downs
DROP TABLE public.admin_data;
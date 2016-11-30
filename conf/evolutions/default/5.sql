# --- !Ups

CREATE TABLE public.user_answer_content
(
  id             BIGSERIAL NOT NULL,
  string_content TEXT,
  date_content   DATE,
  slider_content SMALLINT,
  user_id        BIGINT    NOT NULL,
  field_id       BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT to_user_answer FOREIGN KEY (user_id)
  REFERENCES public.user_answer (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION,
  CONSTRAINT to_field FOREIGN KEY (field_id)
  REFERENCES public.field (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE SET NULL
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.user_answer_content
  OWNER TO postgres;

# --- !Downs
DROP TABLE public.user_answer_content;
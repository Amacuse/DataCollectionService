# --- !Ups

CREATE TABLE public.user_answer
(
  id bigserial NOT NULL,
  PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.user_answer
  OWNER to postgres;

# --- !Downs
DROP TABLE public.user_answer;
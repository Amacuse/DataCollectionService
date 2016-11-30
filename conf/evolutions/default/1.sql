# --- !Ups
CREATE TABLE public."admin"
(
  id       BIGSERIAL             NOT NULL,
  login    CHARACTER VARYING(15) NOT NULL,
  password CHARACTER VARYING(15) NOT NULL,
  PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."admin"
  OWNER TO postgres;

INSERT INTO admin (login, password) VALUES ('admin', 'admin');

# --- !Downs
DROP TABLE public."admin";




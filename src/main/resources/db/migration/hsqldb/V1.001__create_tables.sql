CREATE SCHEMA IF NOT EXISTS shorteningfat;

CREATE SEQUENCE shorteningfat.urls_id_seq START WITH 1;

CREATE TABLE IF NOT EXISTS shorteningfat.urls (
  id BIGINT GENERATED BY DEFAULT AS SEQUENCE shorteningfat.urls_id_seq PRIMARY KEY ,
  url LONGVARCHAR NOT NULL UNIQUE,
  shortened_code LONGVARCHAR
);

INSERT INTO shorteningfat.urls (url) VALUES ('http://google.com'), ('http://foo.com'), ('https://bar.com');


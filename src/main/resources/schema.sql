DROP TABLE IF EXISTS film_genre;

DROP TABLE IF EXISTS genre;

DROP TABLE IF EXISTS friend;

DROP TABLE IF EXISTS film_like;

DROP TABLE IF EXISTS film;

DROP TABLE IF EXISTS mpa;

DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
  user_id       BIGSERIAL PRIMARY KEY,
  email    VARCHAR UNIQUE NOT NULL,
  login    VARCHAR UNIQUE NOT NULL,
  name     VARCHAR NOT NULL,
  birthday DATE    NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa
(
  mpa_id     BIGINT PRIMARY KEY,
  mpa_name   VARCHAR
);

CREATE TABLE IF NOT EXISTS film
(
  id           BIGSERIAL    PRIMARY KEY,
  name         VARCHAR      NOT NULL,
  description  VARCHAR(200) NULL,
  release_date DATE         NOT NULL,
  duration     INT          NOT NULL,
  mpa_id       BIGINT       NULL REFERENCES mpa (mpa_id)
);

CREATE TABLE IF NOT EXISTS film_like
(
  user_id BIGINT NOT NULL REFERENCES users (user_id),
  film_id BIGINT NOT NULL REFERENCES film (id)
);

CREATE TABLE IF NOT EXISTS friend
(
  user_id   BIGINT NOT NULL REFERENCES users (user_id),
  friend_id BIGINT NOT NULL REFERENCES users (user_id),
  status    BOOLEAN DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS genre
(
  genre_id   INT PRIMARY KEY,
  genre_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre
(
  film_id  BIGINT NOT NULL REFERENCES film (id),
  genre_id BIGINT NOT NULL REFERENCES genre (genre_id)
);
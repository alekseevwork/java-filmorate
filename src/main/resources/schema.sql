DROP TABLE IF EXISTS film_genre;

DROP TABLE IF EXISTS genre;

DROP TABLE IF EXISTS friend;

DROP TABLE IF EXISTS film_like;

DROP TABLE IF EXISTS film;

DROP TABLE IF EXISTS rating;

DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
  id       BIGSERIAL PRIMARY KEY,
  email    VARCHAR UNIQUE NOT NULL,
  login    VARCHAR UNIQUE NOT NULL,
  name     VARCHAR NOT NULL,
  birthday DATE    NOT NULL
);

CREATE TABLE IF NOT EXISTS rating
(
  id     INT PRIMARY KEY,
  rating VARCHAR
);

CREATE TABLE IF NOT EXISTS film
(
  id           BIGSERIAL PRIMARY KEY,
  name         VARCHAR      UNIQUE NOT NULL,
  description  VARCHAR(200) NULL,
  release_date DATE         NOT NULL,
  duration     INT          NOT NULL,
  rating_id    BIGINT       NULL REFERENCES rating (id)
);

CREATE TABLE IF NOT EXISTS film_like
(
  user_id BIGINT NOT NULL REFERENCES users (id),
  film_id BIGINT NOT NULL REFERENCES film (id)
);

CREATE TABLE IF NOT EXISTS friend
(
  user_id   BIGINT NOT NULL REFERENCES users (id),
  friend_id BIGINT NOT NULL REFERENCES users (id),
  status    BOOLEAN DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS genre
(
  id   INT PRIMARY KEY,
  genre VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre
(
  film_id  BIGINT NOT NULL REFERENCES film (id),
  genre_id BIGINT NOT NULL REFERENCES genre (id)
);
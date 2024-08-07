INSERT INTO mpa (mpa_id, mpa_name)
SELECT 1, 'G'
WHERE NOT EXISTS (Select 1 FROM mpa WHERE mpa_id = 1);

INSERT INTO mpa (mpa_id, mpa_name)
SELECT 2, 'PG'
WHERE NOT EXISTS (Select 2 FROM mpa WHERE mpa_id = 2);

INSERT INTO mpa (mpa_id, mpa_name)
SELECT 3, 'PG-13'
WHERE NOT EXISTS (Select 3 FROM mpa WHERE mpa_id = 3);

INSERT INTO mpa (mpa_id, mpa_name)
SELECT 4, 'R'
WHERE NOT EXISTS (Select 4 FROM mpa WHERE mpa_id = 4);

INSERT INTO mpa (mpa_id, mpa_name)
SELECT 5, 'NC-17'
WHERE NOT EXISTS (Select 5 FROM mpa WHERE mpa_id = 5);

--------------------------------------------------------

INSERT INTO genre (genre_id, genre_name)
SELECT 1, 'Комедия'
WHERE NOT EXISTS (Select 1 FROM genre WHERE genre_id = 1);

INSERT INTO genre (genre_id, genre_name)
SELECT 2, 'Драма'
WHERE NOT EXISTS (Select 2 FROM genre WHERE genre_id = 2);

INSERT INTO genre (genre_id, genre_name)
SELECT 3, 'Мультфильм'
WHERE NOT EXISTS (Select 3 FROM genre WHERE genre_id = 3);

INSERT INTO genre (genre_id, genre_name)
SELECT 4, 'Триллер'
WHERE NOT EXISTS (Select 4 FROM genre WHERE genre_id = 4);

INSERT INTO genre (genre_id, genre_name)
SELECT 5, 'Документальный'
WHERE NOT EXISTS (Select 5 FROM genre WHERE genre_id = 5);

INSERT INTO genre (genre_id, genre_name)
SELECT 6, 'Боевик'
WHERE NOT EXISTS (Select 6 FROM genre WHERE genre_id = 6);
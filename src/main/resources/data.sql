--INSERT INTO rating (id, rating) VALUES
--    (1, 'G'),
--    (2, 'PG'),
--    (3, 'PG-13'),
--    (4, 'R'),
--    (5, 'NC-17');

--INSERT INTO genre (id, genre) VALUES
--    (1, 'Комедия'),
--    (2, 'Драма'),
--    (3, 'Мультфильм'),
--    (4, 'Триллер'),
--    (5, 'Документальный'),
--    (6, 'Боевик');

INSERT INTO rating (id, rating)
SELECT 1, 'G'
WHERE NOT EXISTS (Select 1 FROM rating WHERE id = 1);

INSERT INTO rating (id, rating)
SELECT 2, 'PG'
WHERE NOT EXISTS (Select 2 FROM rating WHERE id = 2);

INSERT INTO rating (id, rating)
SELECT 3, 'PG-13'
WHERE NOT EXISTS (Select 3 FROM rating WHERE id = 3);

INSERT INTO rating (id, rating)
SELECT 4, 'R'
WHERE NOT EXISTS (Select 4 FROM rating WHERE id = 4);

INSERT INTO rating (id, rating)
SELECT 5, 'NC-17'
WHERE NOT EXISTS (Select 5 FROM rating WHERE id = 5);

--------------------------------------------------------

INSERT INTO genre (id, genre)
SELECT 1, 'Комедия'
WHERE NOT EXISTS (Select 1 FROM genre WHERE id = 1);

INSERT INTO genre (id, genre)
SELECT 2, 'Драма'
WHERE NOT EXISTS (Select 2 FROM genre WHERE id = 2);

INSERT INTO genre (id, genre)
SELECT 3, 'Мультфильм'
WHERE NOT EXISTS (Select 3 FROM genre WHERE id = 3);

INSERT INTO genre (id, genre)
SELECT 4, 'Триллер'
WHERE NOT EXISTS (Select 4 FROM genre WHERE id = 4);

INSERT INTO genre (id, genre)
SELECT 5, 'Документальный'
WHERE NOT EXISTS (Select 5 FROM genre WHERE id = 5);

INSERT INTO genre (id, genre)
SELECT 6, 'Боевик'
WHERE NOT EXISTS (Select 6 FROM genre WHERE id = 6);
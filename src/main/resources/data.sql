INSERT INTO mpa (id, name)
SELECT 1, 'G'
WHERE NOT EXISTS (Select 1 FROM mpa WHERE id = 1);

INSERT INTO mpa (id, name)
SELECT 2, 'PG'
WHERE NOT EXISTS (Select 2 FROM mpa WHERE id = 2);

INSERT INTO mpa (id, name)
SELECT 3, 'PG-13'
WHERE NOT EXISTS (Select 3 FROM mpa WHERE id = 3);

INSERT INTO mpa (id, name)
SELECT 4, 'R'
WHERE NOT EXISTS (Select 4 FROM mpa WHERE id = 4);

INSERT INTO mpa (id, name)
SELECT 5, 'NC-17'
WHERE NOT EXISTS (Select 5 FROM mpa WHERE id = 5);

--------------------------------------------------------

INSERT INTO genre (id, name)
SELECT 1, 'Комедия'
WHERE NOT EXISTS (Select 1 FROM genre WHERE id = 1);

INSERT INTO genre (id, name)
SELECT 2, 'Драма'
WHERE NOT EXISTS (Select 2 FROM genre WHERE id = 2);

INSERT INTO genre (id, name)
SELECT 3, 'Мультфильм'
WHERE NOT EXISTS (Select 3 FROM genre WHERE id = 3);

INSERT INTO genre (id, name)
SELECT 4, 'Триллер'
WHERE NOT EXISTS (Select 4 FROM genre WHERE id = 4);

INSERT INTO genre (id, name)
SELECT 5, 'Документальный'
WHERE NOT EXISTS (Select 5 FROM genre WHERE id = 5);

INSERT INTO genre (id, name)
SELECT 6, 'Боевик'
WHERE NOT EXISTS (Select 6 FROM genre WHERE id = 6);
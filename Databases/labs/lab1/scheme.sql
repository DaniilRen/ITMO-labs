BEGIN;

CREATE TYPE sex AS ENUM ('М', 'Ж');

CREATE TYPE action_types AS ENUM (
    'опустить', 'поднять'
);

CREATE TYPE celestial_types AS ENUM (
    'спутник', 'планета'
);

CREATE TYPE orbit_types AS ENUM (
    'круговая', 'эллиптическая'
);

CREATE TYPE light_colors AS ENUM (
    'красный', 'синий', 'зеленый'
);

CREATE TYPE moods AS ENUM (
    'унылый', 'веселый', 'нейтральный'
);

CREATE TABLE orbits (
    id SERIAL PRIMARY KEY,
    type orbit_types NOT NULL UNIQUE
);

CREATE TABLE spaceships (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    location INT REFERENCES orbits(id)
);

CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    sex sex NOT NULL,
    current_ship INT REFERENCES spaceships(id)
);

CREATE TABLE landscapes (
    id SERIAL PRIMARY KEY,
    appearance_description TEXT NOT NULL
);

CREATE TABLE shadows (
    id SERIAL PRIMARY KEY,
    is_sharp BOOLEAN NOT NULL DEFAULT FALSE UNIQUE
);

CREATE TABLE lights (
    id SERIAL PRIMARY KEY,
    light_color light_colors NOT NULL,
    mood moods NOT NULL
);

CREATE TABLE athmospheres (
    id SERIAL PRIMARY KEY,
    shadow INT REFERENCES shadows(id),
    light INT NOT NULL REFERENCES lights(id)
);

CREATE TABLE celestial_bodies (
    id SERIAL PRIMARY KEY,
    type celestial_types NOT NULL,
    distance_description TEXT,
    orbit INT NOT NULL REFERENCES orbits(id),
    landscape INT REFERENCES landscapes(id),
    athmosphere INT REFERENCES athmospheres(id)
);

CREATE TABLE actions (
    id SERIAL PRIMARY KEY,
    type action_types NOT NULL,
    object INT NOT NULL REFERENCES spaceships(id),
    subject INT NOT NULL REFERENCES people(id)
);

INSERT INTO people(name, sex, current_ship) VALUES
('Василий', 'М', NULL),
('Антон', 'М', NULL),
('Анастасия', 'Ж', NULL);

INSERT INTO orbits(type) VALUES 
('круговая'), ('эллиптическая');

INSERT INTO spaceships(name, location) VALUES 
('Корабль1', 2),
('Корабль2', 1);

UPDATE people SET current_ship = 1 WHERE name = 'Василий';
UPDATE people SET current_ship = 2 WHERE name IN ('Антон', 'Анастасия');

INSERT INTO landscapes(appearance_description) VALUES 
('впечатление было такое, будто все сущее здесь окунули в кровь'),
('было странное впечатление, как будто вся природа синяя');

INSERT INTO lights(light_color, mood) VALUES 
('красный', 'унылый'), ('синий', 'веселый');

INSERT INTO shadows(is_sharp) VALUES (TRUE), (FALSE);

INSERT INTO athmospheres(presence, shadow, light) VALUES 
(TRUE, 1, 1), (FALSE, 1, 2);

INSERT INTO celestial_bodies(type, distance_description, orbit, landscape, athmosphere) VALUES 
('спутник', 'не потребовалось предупреждения от сложной системы защиты, чтобы понять, что атмосферы здесь нет', 1, 1, 1),
('планета', 'ничем не примечательное небесное тело', 2, 2, 1);

INSERT INTO actions(type, object, subject) VALUES 
('опустить', 1, 1), ('поднять', 2, 3);

COMMIT;

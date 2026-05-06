BEGIN;

CREATE TABLE orbits (
    id SERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE spaceships (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    location INT REFERENCES orbits(id)
);

CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    sex VARCHAR(2) NOT NULL CHECK (sex in ('М', 'Ж')),
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
    light_color VARCHAR(20) NOT NULL,
    mood VARCHAR(20) NOT NULL,
    UNIQUE (light_color, mood)
);

CREATE TABLE athmospheres (
    id SERIAL PRIMARY KEY,
    shadow INT REFERENCES shadows(id),
    light INT NOT NULL REFERENCES lights(id)
);

CREATE TABLE celestial_bodies (
    id SERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    distance_description TEXT,
    orbit INT NOT NULL REFERENCES orbits(id),
    landscape INT REFERENCES landscapes(id),
    athmosphere INT REFERENCES athmospheres(id)
);

CREATE TABLE actions (
    type VARCHAR(20) NOT NULL,
    object INT NOT NULL REFERENCES spaceships(id),
    subject INT NOT NULL REFERENCES people(id),
    PRIMARY KEY (type, object, subject)
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

INSERT INTO athmospheres(shadow, light) VALUES 
(1, 1), (1, 2);

INSERT INTO celestial_bodies(type, distance_description, orbit, landscape, athmosphere) VALUES 
('спутник', 'не потребовалось предупреждения от сложной системы защиты, чтобы понять, что атмосферы здесь нет', 1, 1, 1),
('планета', 'ничем не примечательное небесное тело', 2, 2, 1);

INSERT INTO actions(type, object, subject) VALUES 
('опустить', 1, 1), ('поднять', 2, 3);

COMMIT;

BEGIN;

-- ENUMs для типов
CREATE TYPE celestial_body_types AS ENUM ('спутник', 'планета', 'солнце');
CREATE TYPE orbit_types AS ENUM ('круговая');
CREATE TYPE light_colors AS ENUM ('красный');
CREATE TYPE people_sex AS ENUM ('М', 'Ж');
CREATE TYPE actions AS ENUM (
    'опустить пониже',
    'понять отсутствие атмосферы'
);
CREATE TYPE entity_tables AS ENUM (
    'people', 'spaceships', 'celestial_bodies', 'orbits', 'atmospheres',
    'lights', 'shadows', 'landscapes', 'locations'
);
CREATE DOMAIN uint2 AS int4 CHECK(VALUE >= 0 AND VALUE < 65536);

-- Базовая таблица для сущностей
CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    type entity_tables NOT NULL
);

-- Люди
CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    sex people_sex NOT NULL,
    item_id INT REFERENCES items(id)
);

-- Корабли
CREATE TABLE spaceships (
    id SERIAL PRIMARY KEY,
    item_id INT REFERENCES items(id)
);

-- Небесные тела
CREATE TABLE celestial_bodies (
    id SERIAL PRIMARY KEY,
    type celestial_body_types NOT NULL,
    distance_description VARCHAR(50),
    light_color light_colors,
    item_id INT REFERENCES items(id)
);

-- Орбиты
CREATE TABLE orbits (
    id SERIAL PRIMARY KEY,
    type orbit_types NOT NULL,
    item_id INT REFERENCES items(id),
    celestial_body_id INT REFERENCES celestial_bodies(id)
);

-- Атмосферы
CREATE TABLE atmospheres (
    id SERIAL PRIMARY KEY,
    has_atmosphere BOOLEAN NOT NULL DEFAULT FALSE,
    item_id INT REFERENCES items(id),
    celestial_body_id INT NOT NULL REFERENCES celestial_bodies(id)
);

-- Свет
CREATE TABLE lights (
    id SERIAL PRIMARY KEY,
    color light_colors NOT NULL,
    mood VARCHAR(20) DEFAULT 'унылый',
    item_id INT REFERENCES items(id),
    celestial_body_id INT REFERENCES celestial_bodies(id)
);

-- Тени
CREATE TABLE shadows (
    id SERIAL PRIMARY KEY,
    is_sharp BOOLEAN NOT NULL DEFAULT TRUE,
    item_id INT REFERENCES items(id),
    landscape_id INT REFERENCES landscapes(id)
);

-- Пейзажи
CREATE TABLE landscapes (
    id SERIAL PRIMARY KEY,
    appearance_description VARCHAR(100) DEFAULT 'залит красным светом, окунут в кровь',
    item_id INT REFERENCES items(id),
    celestial_body_id INT REFERENCES celestial_bodies(id)
);

-- Локации
CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) DEFAULT 'поверхность спутника',
    position VARCHAR(50),
    item_id INT REFERENCES items(id),
    celestial_body_id INT REFERENCES celestial_bodies(id)
);

-- Действия
CREATE TABLE people_actions (
    id SERIAL PRIMARY KEY,
    action actions NOT NULL,
    subject_id INT REFERENCES people(id),
    object_id INT REFERENCES items(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Связи: корабль в орбите
CREATE TABLE spaceship_orbits (
    spaceship_id INT NOT NULL REFERENCES spaceships(id),
    orbit_id INT NOT NULL REFERENCES orbits(id),
    PRIMARY KEY (spaceship_id, orbit_id)
);

-- Связи: спутник планеты
CREATE TABLE satellite_planets (
    satellite_id INT NOT NULL REFERENCES celestial_bodies(id),
    planet_id INT NOT NULL REFERENCES celestial_bodies(id),
    PRIMARY KEY (satellite_id, planet_id)
);

-- Связи: орбита над горизонтом (локацией)
CREATE TABLE orbit_locations (
    orbit_id INT NOT NULL REFERENCES orbits(id),
    location_id INT NOT NULL REFERENCES locations(id),
    PRIMARY KEY (orbit_id, location_id)
);

-- Связи: пейзаж освещен светом
CREATE TABLE landscape_lights (
    landscape_id INT NOT NULL REFERENCES landscapes(id),
    light_id INT NOT NULL REFERENCES lights(id),
    PRIMARY KEY (landscape_id, light_id)
);

-- INSERT данные из текста
INSERT INTO items(type) VALUES ('celestial_bodies');
INSERT INTO celestial_bodies(type, distance_description, light_color, item_id)
VALUES ('планета', NULL, NULL, 1);

INSERT INTO items(type) VALUES ('celestial_bodies');
INSERT INTO celestial_bodies(type, distance_description, light_color, item_id)
VALUES ('спутник', NULL, NULL, 2);

INSERT INTO satellite_planets(satellite_id, planet_id) VALUES (2, 1);

INSERT INTO items(type) VALUES ('atmospheres');
INSERT INTO atmospheres(has_atmosphere, celestial_body_id, item_id) VALUES (FALSE, 2, 3);

INSERT INTO items(type) VALUES ('locations');
INSERT INTO locations(name, position, celestial_body_id, item_id)
VALUES ('поверхность спутника', 'низко над поверхностью', 2, 4);

INSERT INTO items(type) VALUES ('celestial_bodies');
INSERT INTO celestial_bodies(type, distance_description, light_color, item_id)
VALUES ('солнце', 'наиболее удаленное', 'красный', 5);

INSERT INTO items(type) VALUES ('lights');
INSERT INTO lights(color, celestial_body_id, item_id) VALUES ('красный', 5, 6);

INSERT INTO items(type) VALUES ('locations');
INSERT INTO locations(name, position, celestial_body_id, item_id)
VALUES ('горизонт', 'над горизонтом', 2, 7);

INSERT INTO items(type) VALUES ('orbits');
INSERT INTO orbits(type, celestial_body_id, item_id) VALUES ('круговая', 2, 8);

INSERT INTO orbit_locations(orbit_id, location_id) VALUES (1, 2);

INSERT INTO items(type) VALUES ('landscapes');
INSERT INTO landscapes(celestial_body_id, item_id) VALUES (2, 9);

INSERT INTO items(type) VALUES ('shadows');
INSERT INTO shadows(landscape_id, item_id) VALUES (1, 10);

INSERT INTO landscape_lights(landscape_id, light_id) VALUES (1, 1);

INSERT INTO items(type) VALUES ('spaceships');
INSERT INTO spaceships(item_id) VALUES (11);

INSERT INTO spaceship_orbits(spaceship_id, orbit_id) VALUES (1, 1);

INSERT INTO items(type) VALUES ('people');
INSERT INTO people(name, sex, item_id) VALUES ('Олвин', 'М', 12);

INSERT INTO people_actions(action, subject_id, object_id)
VALUES ('опустить пониже', 1, 11);

COMMIT;

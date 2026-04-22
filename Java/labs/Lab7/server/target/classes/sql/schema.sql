BEGIN;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    password VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS coordinates (
    id SERIAL PRIMARY KEY,
    x DOUBLE PRECISION NOT NULL,
    y INTEGER NOT NULL,
    UNIQUE(x, y)
);

CREATE TABLE IF NOT EXISTS location3Dimension (
    id SERIAL PRIMARY KEY,
    x DOUBLE PRECISION,
    y DOUBLE PRECISION NOT NULL,
    z INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    UNIQUE(x, y, z, name)
);

CREATE TABLE IF NOT EXISTS location2Dimension (
    id SERIAL PRIMARY KEY,
    x INTEGER NOT NULL,
    y DOUBLE PRECISION,
    name VARCHAR(255) NOT NULL,
    UNIQUE(x, y, name)
);

CREATE TABLE IF NOT EXISTS route (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    distance INTEGER CONSTRAINT valid_distance CHECK (distance > 1),
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    coordinates_id INTEGER NOT NULL REFERENCES coordinates(id),
    from_location_id INTEGER NOT NULL REFERENCES location2Dimension(id),
    to_location_id INTEGER NOT NULL REFERENCES location3Dimension(id)
);

CREATE INDEX IF NOT EXISTS idx_routes_coordinates_id ON route(coordinates_id);
CREATE INDEX IF NOT EXISTS idx_routes_from_location_id ON route(from_location_id);
CREATE INDEX IF NOT EXISTS idx_routes_to_location_id ON route(to_location_id);

COMMIT;
BEGIN;

INSERT INTO users (name, password, is_admin) VALUES
('admin', '5d2fa642ee3c7daf8b0b4658bcd950fcf90c419c9ed47411c8d96c667e140f31', TRUE),
('user1', '2946e11fb8b7cac4149a7160a79e1be7bb630b5060c08bf49a0fd4dd67b80fb9', FALSE);

INSERT INTO coordinates (x, y) VALUES
(10, 20),
(30, 40),
(50, 60),
(70, 80),
(90, 100);

INSERT INTO location2Dimension (x, y, name) VALUES
(-250, -180, 'West Point'),
(-120, 140, 'North City'),
(40, -60, 'Central Hub'),
(180, 120, 'East District'),
(260, -140, 'South Port');

INSERT INTO location3Dimension (x, y, z, name) VALUES
(-150, 200, 50, 'Airport'),
(120, 220, 80, 'University'),
(280, 40, 120, 'Industrial Zone'),
(-40, -260, 150, 'Harbor'),
(-300, 20, 200, 'Old Town');

INSERT INTO route (
    name,
    distance,
    coordinates_id,
    from_location_id,
    to_location_id,
    author
) VALUES

('Route Alpha',   120, 1, 1, 1, 'admin'),
('Route Beta',    180, 2, 2, 2, 'admin'),
('Route Gamma',   220, 3, 3, 3, 'admin'),
('Route Delta',   260, 4, 4, 4, 'admin'),
('Route Epsilon', 300, 5, 5, 5, 'admin'),

('Route Zeta',    150, 1, 1, 2, 'user1'),
('Route Eta',     170, 2, 2, 3, 'user1'),
('Route Theta',   210, 3, 3, 4, 'user1'),
('Route Iota',    240, 4, 4, 5, 'user1'),
('Route Kappa',   280, 5, 5, 1, 'user1');

COMMIT;
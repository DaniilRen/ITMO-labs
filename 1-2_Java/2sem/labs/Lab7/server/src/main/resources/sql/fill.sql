BEGIN;

INSERT INTO users (name, password) VALUES
('admin', '5d2fa642ee3c7daf8b0b4658bcd950fcf90c419c9ed47411c8d96c667e140f31'), -- admin --
('user1', '2946e11fb8b7cac4149a7160a79e1be7bb630b5060c08bf49a0fd4dd67b80fb9'); -- password1 --

INSERT INTO coordinates (x, y) VALUES
(10.5, 20),
(25.5, 35),
(40.2, 50),
(55.8, 65),
(70.3, 80);

INSERT INTO location2Dimension (x, y, name) VALUES
(100, 200.5, 'Moscow Central'),
(250, 350.2, 'Saint Petersburg Square'),
(400, 450.8, 'Novosibirsk Station'),
(550, 600.3, 'Kazan Kremlin'),
(700, 750.0, 'Sochi Park');

INSERT INTO location3Dimension (x, y, z, name) VALUES
(1000.5, 2000.3, 300, 'Moscow Airport'),
(2500.2, 3500.7, 450, 'SPB Sea Port'),
(4000.8, 4500.1, 600, 'Novosibirsk Mall'),
(5500.0, 6000.4, 750, 'Kazan Stadium'),
(7000.6, 7500.9, 900, 'Sochi Beach');

INSERT INTO route (name, distance, coordinates_id, from_location_id, to_location_id, author) VALUES
('Route Alpha', 150, 1, 1, 1, 'admin'),
('Route Beta', 250, 2, 2, 2, 'admin'),
('Route Gamma', 350, 3, 3, 3, 'admin'),
('Route Delta', 450, 4, 4, 4, 'admin'),
('Route Epsilon', 550, 5, 5, 5, 'admin'),
('Route Zeta', 200, 1, 2, 3, 'user1'),
('Route Eta', 300, 2, 3, 4, 'user1'),
('Route Theta', 400, 3, 4, 5, 'user1'),
('Route Iota', 500, 4, 5, 1, 'user1'),
('Route Kappa', 600, 5, 1, 2, 'user1');

COMMIT;
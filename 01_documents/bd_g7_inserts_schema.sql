INSERT INTO Role (Name) VALUES ('Administrador');
INSERT INTO Role (Name) VALUES ('Usuario');
INSERT INTO Role (Name) VALUES ('Recepcionista');

-- Usuarios
INSERT INTO [users] (identification, password_hash, email, first_name, second_name, last_name, second_last_name, phone, is_active)
VALUES
('1001', 'hash123', 'admin@club.com', 'Carlos', 'Andrés', 'Pérez', 'Gómez', '3001112233', 1),
('1002', 'hash456', 'usuario1@club.com', 'María', NULL, 'Ramírez', 'Torres', '3102223344', 1),
('1003', 'hash789', 'recepcion@club.com', 'José', 'Luis', 'Rodríguez', 'Martínez', '3203334455', 1);

-- Relación Usuario - Rol
-- Admin
INSERT INTO UserRole (User_id, Role_id) VALUES (1, 1);
-- Usuario normal
INSERT INTO UserRole (User_id, Role_id) VALUES (2, 2);
-- Recepcionista
INSERT INTO UserRole (User_id, Role_id) VALUES (3, 3);

-- Deportes
INSERT INTO Sport (Name) VALUES ('Fútbol');
INSERT INTO Sport (Name) VALUES ('Tenis');
INSERT INTO Sport (Name) VALUES ('Baloncesto');

-- Canchas
INSERT INTO Court (Name, Location, Sport_id, Price_Per_Hour, Is_Active)
VALUES
('Cancha Sintética Norte', 'Sede Norte - Bogotá', 1, 120000, 1),
('Cancha Tenis Central', 'Sede Norte - Bogotá', 2, 80000, 1),
('Coliseo Baloncesto', 'Sede Sur - Medellín', 3, 60000, 1);

-- Reservas
INSERT INTO Reservation (Court_id, User_id, StartAt, EndAt, Status_Code, Notes)
VALUES
(1, 2, '2025-10-01 09:00:00', '2025-10-01 10:00:00', 'CONFIRMADO', 'Juego amistoso'),
(2, 2, '2025-10-02 15:00:00', '2025-10-02 16:30:00', 'PENDIENTE', 'Clase particular'),
(3, 3, '2025-10-03 18:00:00', '2025-10-03 20:00:00', 'CANCELADO', 'Torneo cancelado');
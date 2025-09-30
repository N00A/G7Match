-- Roles
INSERT INTO Role (Name) VALUES (N'Administrador');
INSERT INTO Role (Name) VALUES (N'Usuario');
INSERT INTO Role (Name) VALUES (N'Recepcionista');

-- Usuarios
INSERT INTO [users] (
    identification, password_hash, email,
    first_name, second_name, last_name, second_last_name,
    phone, is_active
)
VALUES
    (N'1001', N'hash123', N'admin@club.com',  N'Carlos', N'Andrés',  N'Pérez',     N'Gómez',     N'3001112233', 1),
    (N'1002', N'hash456', N'usuario1@club.com', N'María',  N'Antonia', N'Ramírez',  N'Torres',    N'3102223344', 1),
    (N'1003', N'hash789', N'recepcion@club.com', N'José',   N'Luis',    N'Rodríguez',N'Martínez',  N'3203334455', 1);

-- Relación Usuario - Rol
-- Admin
INSERT INTO UserRole (User_id, Role_id) VALUES (1, 1);
-- Usuario normal
INSERT INTO UserRole (User_id, Role_id) VALUES (2, 2);
-- Recepcionista
INSERT INTO UserRole (User_id, Role_id) VALUES (3, 3);

-- Deportes
INSERT INTO Sport (Name) VALUES (N'Fútbol');
INSERT INTO Sport (Name) VALUES (N'Tenis');
INSERT INTO Sport (Name) VALUES (N'Baloncesto');

-- Canchas
INSERT INTO Court (Name, Location, Sport_id, Price_Per_Hour, Is_Active)
VALUES
    (N'Cancha Sintética Norte', N'Sede Norte - Bogotá', 1, 120000, 1),
    (N'Cancha Tenis Central',   N'Sede Norte - Bogotá', 2,  80000, 1),
    (N'Coliseo Baloncesto',     N'Sede Sur - Medellín', 3,  60000, 1);

-- Reservas
INSERT INTO Reservation (Court_id, User_id, StartAt, EndAt, Status_Code, Notes)
VALUES
    (1, 2, '2025-10-01 09:00:00', '2025-10-01 10:00:00', N'CONFIRMADO', N'Juego amistoso'),
    (2, 2, '2025-10-02 15:00:00', '2025-10-02 16:30:00', N'PENDIENTE',  N'Clase particular'),
    (3, 3, '2025-10-03 18:00:00', '2025-10-03 20:00:00', N'CANCELADO',  N'Torneo cancelado');
USE bd_g7_match;

GO
    /* =========================
     Tablas base
     ========================= */
    -- Roles
    CREATE TABLE Role (
        RoleId INT IDENTITY(1, 1) PRIMARY KEY,
        Name VARCHAR(50) NOT NULL UNIQUE
    );

-- Usuarios
CREATE TABLE [users] (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    identification NVARCHAR(50) NOT NULL,
    password_hash NVARCHAR(255) NOT NULL,
    email NVARCHAR(150) NOT NULL,
    first_name NVARCHAR(100) NULL,
    second_name NVARCHAR(100) NULL,
    last_name NVARCHAR(100) NULL,
    second_last_name NVARCHAR(100) NULL,
    phone NVARCHAR(20) NULL,
);

-- Relación Usuario - Rol
CREATE TABLE UserRole (
    UserId INT NOT NULL,
    RoleId INT NOT NULL,
    CONSTRAINT PK_UserRole PRIMARY KEY (UserId, RoleId),
    CONSTRAINT FK_UserRole_User FOREIGN KEY (UserId) REFERENCES [User](UserId),
    CONSTRAINT FK_UserRole_Role FOREIGN KEY (RoleId) REFERENCES Role(RoleId)
);

-- Deportes
CREATE TABLE Sport (
    SportId INT IDENTITY(1, 1) PRIMARY KEY,
    Name VARCHAR(60) NOT NULL UNIQUE
);

-- Canchas
CREATE TABLE Court (
    CourtId INT IDENTITY(1, 1) PRIMARY KEY,
    Name VARCHAR(80) NOT NULL UNIQUE,
    Location VARCHAR(150) NULL,
    SportId INT NOT NULL,
    PricePerHour DECIMAL(10, 2) NOT NULL,
    IsActive BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Court_Sport FOREIGN KEY (SportId) REFERENCES Sport(SportId)
);

-- Estados de reservas
CREATE TABLE ReservationStatus (
    StatusCode VARCHAR(15) NOT NULL PRIMARY KEY,
    -- Ej: ACTIVE, CANCELLED, COMPLETED
    Description VARCHAR(80) NULL
);

-- Reservas
CREATE TABLE Reservation (
    ReservationId INT IDENTITY(1, 1) PRIMARY KEY,
    CourtId INT NOT NULL,
    UserId INT NOT NULL,
    StartAt DATETIME2(0) NOT NULL,
    EndAt DATETIME2(0) NOT NULL,
    StatusCode VARCHAR(15) NOT NULL,
    Notes VARCHAR(250) NULL,
    CONSTRAINT FK_Reservation_Court FOREIGN KEY (CourtId) REFERENCES Court(CourtId),
    CONSTRAINT FK_Reservation_User FOREIGN KEY (UserId) REFERENCES [User](UserId),
    CONSTRAINT FK_Reservation_Status FOREIGN KEY (StatusCode) REFERENCES ReservationStatus(StatusCode)
);
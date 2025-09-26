                                                                                                            USE bd_g7_match;

GO
    /* =========================
     Tablas base
     ========================= */
    -- Roles
    CREATE TABLE Role (
        id INT IDENTITY(1, 1) PRIMARY KEY,
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
    id INT IDENTITY(1,1) PRIMARY KEY,
    User_id INT NOT NULL,
    Role_id INT NOT NULL,
    CONSTRAINT PK_UserRole PRIMARY KEY (User_id, Role_id),
    CONSTRAINT FK_UserRole_User FOREIGN KEY (User_id) REFERENCES [User](id),
    CONSTRAINT FK_UserRole_Role FOREIGN KEY (Role_id) REFERENCES Role(id)
);

-- Deportes
CREATE TABLE Sport (
    id INT IDENTITY(1, 1) PRIMARY KEY,
    Name VARCHAR(60) NOT NULL UNIQUE
);

-- Canchas
CREATE TABLE Court (
    id INT IDENTITY(1, 1) PRIMARY KEY,
    Name VARCHAR(80) NOT NULL UNIQUE,
    Location VARCHAR(150) NULL,
    Sport_id INT NOT NULL,
    Price_Per_Hour DECIMAL(10, 2) NOT NULL,
    Is_Active BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Court_Sport FOREIGN KEY (Sport_id) REFERENCES Sport(id)
);

-- Reservas
CREATE TABLE Reservation (
    id INT IDENTITY(1, 1) PRIMARY KEY,
    Court_id INT NOT NULL,
    User_id INT NOT NULL,
    StartAt DATETIME2(0) NOT NULL,
    EndAt DATETIME2(0) NOT NULL,
    Status_Code VARCHAR(15) NOT NULL,
    Notes VARCHAR(250) NULL,
    CONSTRAINT FK_Reservation_Court FOREIGN KEY (Court_id) REFERENCES Court(id),
    CONSTRAINT FK_Reservation_User FOREIGN KEY (User_id) REFERENCES [User](id)
);
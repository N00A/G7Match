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
)
create database biblioteca;

use biblioteca;

create table usuario (
	idUsuario INT PRIMARY KEY,
    Nombre VARCHAR(50),
    Email VARCHAR(40) UNIQUE,
    Telefono INT,
    Direccion VARCHAR(60),
    Rol BOOLEAN
);

CREATE TABLE libro (
	idLibro INT PRIMARY KEY,
    titulo VARCHAR(30),
    autor VARCHAR(150),
    genero VARCHAR(20),
    cantidadPrest INT
);

CREATE TABLE prestamos (
	idPrestamos INT PRIMARY KEY,
    idUsuario INT,
    idLibro INT,
    fechaInicio DATE,
    fechaFin DATE,
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),
    FOREIGN KEY (idLibro) REFERENCES libro(idLibro)
);

drop table prestamos;
drop table prestamosExpress;

CREATE TABLE prestamosExpress (
	idPrestamosExpress INT PRIMARY KEY,
    idPrestamos INT,
    fechaDevolucion DATE,
    FOREIGN KEY (idPrestamos) REFERENCES prestamos(idPrestamos)
);

CREATE TABLE reservas (
	idReservas INT PRIMARY KEY,
    idUsuario INT,
    idLibro INT,
    estadoReserva BOOLEAN,
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),
    FOREIGN KEY (idLibro) REFERENCES libro(idLibro)
);

CREATE TABLE estadoLibro (
	Estado VARCHAR(15) PRIMARY KEY,
    idLibro INT,
    FOREIGN KEY (idLibro) REFERENCES libro(idLibro)
);

insert into usuario (idUsuario, Nombre, Email, Telefono, Direccion) value
	(123456, 'Ana Coñoetumadre', 'Anita@gmail.com', 555666777, 'Calle Fermin 55');
    
Select * from usuario;
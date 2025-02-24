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

ALTER TABLE libro MODIFY titulo VARCHAR(150);


CREATE TABLE prestamos (
	idPrestamos INT PRIMARY KEY,
    idUsuario INT,
    idLibro INT,
    fechaInicio DATE,
    fechaFin DATE,
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),
    FOREIGN KEY (idLibro) REFERENCES libro(idLibro)
);



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

INSERT INTO usuario (idUsuario, Nombre, Email, Telefono, Direccion) VALUES
(1, 'Santiago Restrepo', 'santiago.restrepo@email.com', 123456789, 'Calle 45 #12-34'),
(2, 'Luis Goncalves', 'luis.goncalves@email.com', 987654321, 'Avenida Central 567'),
(3, 'Ana Martínez', 'ana.martinez@email.com', 314159265, 'Carrera 8 #23-45'),
(4, 'Carlos Fernández', 'carlos.fernandez@email.com', 271828182, 'Calle 10 #5-67'),
(5, 'Mariana López', 'mariana.lopez@email.com', 161803399, 'Diagonal 20 #8-90');


INSERT INTO libro (idLibro, titulo, autor, genero, cantidadPrest) VALUES
(1, 'Cien años de soledad', 'Gabriel García Márquez', 'Novela', 5),
(2, '1984', 'George Orwell', 'Ciencia Ficción', 3),
(3, 'El principito', 'Antoine de Saint-Exupéry', 'Fábula', 7),
(4, 'Don Quijote de la Mancha', 'Miguel de Cervantes', 'Clásico', 2),
(5, 'Crónica de una muerte anunciada', 'Gabriel García Márquez', 'Novela', 4);

Delete from libro where idLibro = 5;

Select * from usuario;
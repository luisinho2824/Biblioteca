DELIMITER //

-- Trigger para disminuir cantidadLib al realizar un préstamo
CREATE TRIGGER trg_prestamo_libro
AFTER INSERT ON prestamos
FOR EACH ROW
BEGIN
    -- Disminuir la cantidad de libros disponibles
    UPDATE libro 
    SET cantidadLib = cantidadLib - 1, 
        cantidadPrest = cantidadPrest + 1
    WHERE idLibro = NEW.idLibro;

    -- Si la cantidad disponible llega a 0, actualizar el estado del libro
    UPDATE estadoLibro
    SET Estado = 'No disponible'
    WHERE idLibro = NEW.idLibro AND (SELECT cantidadLib FROM libro WHERE idLibro = NEW.idLibro) = 0;
END;
//

-- Trigger para aumentar cantidadLib al devolver un libro
CREATE TRIGGER trg_devolucion_libro
AFTER INSERT ON prestamosExpress
FOR EACH ROW
BEGIN
    -- Aumentar la cantidad de libros disponibles
    UPDATE libro 
    SET cantidadLib = cantidadLib + 1
    WHERE idLibro = (SELECT idLibro FROM prestamos WHERE idPrestamos = NEW.idPrestamos);

    -- Si el libro estaba "No disponible" y ahora hay unidades disponibles, actualizar su estado a "Disponible"
    UPDATE estadoLibro
    SET Estado = 'Disponible'
    WHERE idLibro = (SELECT idLibro FROM prestamos WHERE idPrestamos = NEW.idPrestamos)
    AND (SELECT cantidadLib FROM libro WHERE idLibro = (SELECT idLibro FROM prestamos WHERE idPrestamos = NEW.idPrestamos)) > 0;
END;
//

DELIMITER ;

ALTER TABLE libro ADD cantidadLib INT;

#le cambie a los libros el id, la idea es que si id no sea un solo numero si no que 5 por lo que cambia los id a 11111, 22222... como esta abajo :)
#si, tienes que hacerlo 5 veces xddd

UPDATE libro 
SET idLibro = 22222
WHERE idLibro = 2;

# true/1 = Administrador false/0 = Usuario. tambien le puse roles a los usuarios. Puedes hacerlo como con el ejemplo de abajo

UPDATE usuario 
SET Rol = true
WHERE idUsuario < 3;

#Tambien cambie los valores de las columnas cantidadLib y cantidadPrest ese ya me da flojera escribirtelo pero es con la misma logica del update, tu puedes
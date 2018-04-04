
CREATE DATABASE IF NOT EXISTS db_tp3_tssi;
USE db_tp3_tssi;

CREATE TABLE IF NOT EXISTS PalosCartas (
	id_palo INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre_palo VARCHAR(20) NOT NULL,
    CONSTRAINT pk_id_palo PRIMARY KEY(id_palo),
    CONSTRAINT unq_nombre_palo UNIQUE(nombre_palo)
);

CREATE TABLE IF NOT EXISTS Cartas (
	id_carta INTEGER(50) UNSIGNED NOT NULL AUTO_INCREMENT,
    numero_carta INTEGER(12) UNSIGNED NOT NULL,
    id_palocarta INTEGER(20) UNSIGNED NOT NULL,
    CONSTRAINT pk_id_carta PRIMARY KEY(id_carta),
    CONSTRAINT fk_id_palocarta FOREIGN KEY(id_palocarta) REFERENCES PalosCartas(id_palo) ON UPDATE CASCADE,
    CONSTRAINT unq_carta UNIQUE(numero_carta, id_palocarta)
);

CREATE TABLE IF NOT EXISTS Jugadores (
	id_jugador INTEGER(50) UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    edad INTEGER(100) UNSIGNED NOT NULL,
    CONSTRAINT pk_id_jugador PRIMARY KEY(id_jugador),
    CONSTRAINT unq_nombre_apellido UNIQUE(nombre, apellido)
);

CREATE TABLE IF NOT EXISTS Repartidores (
	id_repartidor INTEGER(50) UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    edad INTEGER(100) UNSIGNED NOT NULL,
    CONSTRAINT pk_id_repartidor PRIMARY KEY(id_repartidor)
);

CREATE TABLE IF NOT EXISTS Partidas (
	id_partida INTEGER(50) UNSIGNED NOT NULL AUTO_INCREMENT,
    id_repartidor INTEGER(50) UNSIGNED NOT NULL,
    CONSTRAINT pk_id_partida PRIMARY KEY(id_partida),
    CONSTRAINT fk_id_repartidor FOREIGN KEY(id_repartidor) REFERENCES Repartidores(id_repartidor)
);

CREATE TABLE IF NOT EXISTS Ganadores_x_Partida (
	id_partida INTEGER(50) UNSIGNED NOT NULL,
    id_jugador INTEGER(50) UNSIGNED NOT NULL,
    puntos_totales DOUBLE UNSIGNED NOT NULL,
    CONSTRAINT fk_id_partida FOREIGN KEY(id_partida) REFERENCES Partidas(id_partida),
    CONSTRAINT fk_id_jugador FOREIGN KEY(id_jugador) REFERENCES Jugadores(id_jugador),
    CONSTRAINT pk_jug_x_part PRIMARY KEY(id_partida, id_jugador),
    CONSTRAINT unq_jug_x_part UNIQUE(id_partida, id_jugador)
);

INSERT INTO PalosCartas(nombre_palo) VALUES ('Espada'), ('Oro'), ('Copa'), ('Basto');
INSERT INTO Cartas(numero_carta, id_palocarta) VALUES 	(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1), (9, 1), (10, 1), (11, 1), (12, 1),
														(1, 2), (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2), (8, 2), (9, 2), (10, 2), (11, 2), (12, 2),
														(1, 3), (2, 3), (3, 3), (4, 3), (5, 3), (6, 3), (7, 3), (8, 3), (9, 3), (10, 3), (11, 3), (12, 3),
														(1, 4), (2, 4), (3, 4), (4, 4), (5, 4), (6, 4), (7, 4), (8, 4), (9, 4), (10, 4), (11, 4), (12, 4);


DELIMITER $$
CREATE PROCEDURE addJugador (IN vNombre VARCHAR(50), IN vApellido VARCHAR(50), IN vEdad INT, OUT vId INT)
BEGIN

    DECLARE vIdBuscar INT DEFAULT 0;
    
	IF(vNombre IS NULL OR vNombre = '') THEN
		SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'Nombre no Cargado';
        
	ELSEIF (vApellido IS NULL OR vApellido = '') THEN
		SIGNAL SQLSTATE '45002' SET MESSAGE_TEXT = 'Apellido no Cargado';
        
	ELSEIF (vEdad IS NULL OR vEdad = '') THEN
		SIGNAL SQLSTATE '45003' SET MESSAGE_TEXT = 'Edad no Cargada';
	
    END IF;
    
    
    SELECT id_jugador INTO vIdBuscar FROM jugadores WHERE nombre = vNombre AND apellido = vApellido;
    
    IF(vIdBuscar = 0) THEN
		INSERT INTO jugadores (nombre, apellido, edad) VALUES (vNombre, vApellido, vEdad);
        SET vId = last_insert_id();
	
    ELSE 
		SET vId = vIdBuscar;
	
    END IF;	
END;
$$

DELIMITER $$
CREATE PROCEDURE addRepartidor (IN vNombre VARCHAR(50), IN vApellido VARCHAR(50), IN vEdad INT, OUT vId INT)
BEGIN

    DECLARE vIdBuscar INT DEFAULT 0;
    
	IF(vNombre IS NULL OR vNombre = '') THEN
		SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'Nombre no Cargado';
        
	ELSEIF (vApellido IS NULL OR vApellido = '') THEN
		SIGNAL SQLSTATE '45002' SET MESSAGE_TEXT = 'Apellido no Cargado';
        
	ELSEIF (vEdad IS NULL OR vEdad = '') THEN
		SIGNAL SQLSTATE '45003' SET MESSAGE_TEXT = 'Edad no Cargada';
	
    END IF;
    
    
    SELECT id_repartidor INTO vIdBuscar FROM repartidores WHERE nombre = vNombre AND apellido = vApellido;
    
    IF(vIdBuscar = 0) THEN
		INSERT INTO repartidores (nombre, apellido, edad) VALUES (vNombre, vApellido, vEdad);
        SET vId = last_insert_id();
	
    ELSE 
		SET vId = vIdBuscar;
	
    END IF;	
END;
$$

DELIMITER $$
CREATE PROCEDURE addPartida (IN vIdRepartidor INT, OUT vId INT)
BEGIN
    
	IF(vIdRepartidor IS NULL OR vIdRepartidor = 0) THEN
		SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'ID del repartidor no Cargado';
    END IF;
    
    IF EXISTS(SELECT * FROM repartidores WHERE id_repartidor = vIdRepartidor) THEN

		INSERT INTO partidas (id_repartidor) VALUES (vIdRepartidor);
        SET vId = last_insert_id();
	
    ELSE 
		SIGNAL SQLSTATE '46000' SET MESSAGE_TEXT = 'No existe un repartidor con esa ID';
	
    END IF;	
END;
$$

DELIMITER $$
CREATE PROCEDURE addGanadorPartida (IN vIdJugador INT, IN vIdPartida INT, IN vPuntos DOUBLE, OUT vId INT)
BEGIN
    
	IF(vIdJugador IS NULL OR vIdJugador = 0) THEN
		SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'ID del Jugador no Cargado';
        
	ELSEIF(vIdPartida IS NULL OR vIdPartida = 0) THEN
		SIGNAL SQLSTATE '45002' SET MESSAGE_TEXT = 'ID de la Partida no Cargado';
        
	ELSEIF(vPuntos IS NULL OR vPuntos = 0) THEN
		SIGNAL SQLSTATE '45003' SET MESSAGE_TEXT = 'Puntos no Cargado';
        
    END IF;
    
    IF EXISTS(SELECT * FROM jugadores WHERE id_jugador = vIdJugador) THEN
    
		IF EXISTS(SELECT * FROM partidas WHERE id_partida = vIdPartida) THEN
        
			INSERT INTO ganadores_x_partida (id_partida, id_jugador, puntos_totales) VALUES (vIdPartida, vIdJugador, vPuntos);
			SET vId = last_insert_id();
	
		ELSE 
			SIGNAL SQLSTATE '46000' SET MESSAGE_TEXT = 'No existe una partida con esa ID';
		
        END IF;
        
    ELSE 
		SIGNAL SQLSTATE '46000' SET MESSAGE_TEXT = 'No existe un jugador con esa ID';
	
    END IF;	
END;
$$


			
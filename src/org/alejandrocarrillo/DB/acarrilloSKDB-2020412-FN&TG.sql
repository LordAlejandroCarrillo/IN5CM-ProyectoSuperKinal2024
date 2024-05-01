-- Empleados
-- 

DELIMITER $$
CREATE PROCEDURE sp_asignarEncargado(IN empId INT, IN encId INT)
BEGIN
	UPDATE Empleados
		SET
			encargadoId = encId
				WHERE empleadoId = empId;
        
END$$
DELIMITER ;



-- TICKET SOPORTE
-- 

DELIMITER $$
CREATE FUNCTION fn_establecerEstatus(numEstatus INT) RETURNS VARCHAR(30) DETERMINISTIC
BEGIN
	DECLARE estatus VARCHAR(30) DEFAULT '';
	IF numEstatus = 1 THEN
		SET estatus = 'En proceso';
    END IF;
    
    IF numEstatus = 2 THEN
		SET estatus = 'Finalizado';
    END IF;
    
    RETURN estatus;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_cambiarEstatus(IN ticId INT,IN est INT)
BEGIN
	UPDATE TicketSoporte
		SET
            estatus = fn_establecerEstatus(est)
				WHERE ticketSoporteId = ticId;
END $$
DELIMITER ;

-- DEMAS / EXTRAS
--


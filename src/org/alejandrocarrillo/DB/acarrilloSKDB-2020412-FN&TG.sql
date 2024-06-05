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

-- FACTURAS
--

DELIMITER $$
CREATE PROCEDURE sp_asignarTotalFact(IN factId INT, IN toFact DECIMAL(10,2))
BEGIN
	UPDATE Facturas
		SET total = toFact
			WHERE facturaId = factId;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER tg_totalFact
AFTER INSERT ON DetalleFactura
FOR EACH ROW
BEGIN
	DECLARE totalFact DECIMAL (10,2);
    
    SET totalFact = fn_promo(NEW.facturaId);
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fn_calcularStock(proId INT) RETURNS INT DETERMINISTIC
BEGIN
	DECLARE nuevoStock INT DEFAULT 0;
    DECLARE obtenerCantidad INT;
    
	SET obtenerCantidad = (SELECT Productos.cantidadStock FROM Productos WHERE productoId = proId);
	SET nuevoStock = obtenerCantidad - 1;
    
    CALL sp_asignarNuevoStock(proId, nuevoStock);
    
    RETURN nuevoStock;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_asignarNuevoStock(IN proId INT, IN toStock INT)
BEGIN
	UPDATE Productos
		SET cantidadStock  = toStock
			WHERE productoId = proId;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER tg_cantidadProducto
AFTER INSERT ON DetalleFactura
FOR EACH ROW
BEGIN
	DECLARE stock INT DEFAULT 0;
    
    SET stock = fn_calcularStock(NEW.productoId);
END $$
DELIMITER ;

-- TICKET SOPORTE
-- 

DELIMITER $$
CREATE FUNCTION fn_establecerEstatus(numEstatus INT) RETURNS VARCHAR(16) DETERMINISTIC
BEGIN
	DECLARE estatus VARCHAR(30) DEFAULT '';
    IF numEstatus = 0 THEN
		SET estatus = 'Recién Creado';
    END IF;
    
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

-- COMPRAS
--

DELIMITER $$
CREATE FUNCTION fn_sumaCompra(comId INT) RETURNS DECIMAL(10,2) DETERMINISTIC
BEGIN
	DECLARE total DECIMAL(10,2) DEFAULT 0;
    DECLARE i INT DEFAULT 1;
    DECLARE obtenerPrecio DECIMAL(10,2);
    DECLARE stockCompra INT;
    
    contador : LOOP
    
    IF comId = (SELECT DetalleCompra.compraId FROM DetalleCompra WHERE detalleCompraId = i) THEN
		SET obtenerPrecio = (SELECT Productos.precioCompra FROM Productos WHERE productoId = (SELECT productoId FROM DetalleCompra WHERE detalleCompraId = i));
        SET stockCompra = (SELECT DetalleCompra.cantidadCompra FROM DetalleCompra WHERE detalleCompraId = i);
        SET total = total + (obtenerPrecio*stockCompra);
    END IF;
    IF i = (SELECT COUNT(*) FROM DetalleCompra) THEN
		LEAVE contador;
    END IF;
    
    SET i = i + 1;
    END LOOP contador;
    
    CALL sp_asignarTotalComp(comId, total);
    
    RETURN total;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_asignarTotalComp(IN comId INT, IN toCom DECIMAL(10,2))
BEGIN
	UPDATE Compras
		SET totalCompra = toCom
			WHERE compraId = comId;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER tg_totalCompra
AFTER INSERT ON DetalleCompra
FOR EACH ROW
BEGIN
	DECLARE totalCompra DECIMAL (10,2);
    
    SET totalCompra = fn_sumaCompra(NEW.compraId);
END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION fn_calcularStockCom(proId INT, detComId INT) RETURNS INT DETERMINISTIC
BEGIN
	DECLARE nuevoStock INT DEFAULT 0;
    DECLARE i INT DEFAULT 1;
    DECLARE obtenerCantidad INT;
    DECLARE stockCompra INT;
    

		SET obtenerCantidad = (SELECT Productos.cantidadStock FROM Productos WHERE productoId = proId);
		SET stockCompra = (SELECT DT.cantidadCompra from DetalleCompra DT where detalleCompraId = detComId);
		SET nuevoStock = (obtenerCantidad + stockCompra);
        
    CALL sp_asignarNuevoStockCom(proId, nuevoStock);
    
    RETURN nuevoStock;
END$$
DELIMITER ;
-- drop FUNCTION fn_calcularStockCom;
-- SELECT fn_calcularStockCom(2,1);
-- SELECT * FROM DetalleCompra;
DELIMITER $$
CREATE PROCEDURE sp_asignarNuevoStockCom(IN proId INT, IN toStock INT)
BEGIN
	UPDATE Productos
		SET cantidadStock = toStock
			WHERE productoId = proId;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER tg_cantidadProductoCom
AFTER INSERT ON DetalleCompra
FOR EACH ROW
BEGIN
	DECLARE stock INT DEFAULT 0;
    
    SET stock = fn_calcularStockCom(NEW.productoId, NEW.detalleCompraId);
END $$
DELIMITER ;
-- drop trigger tg_cantidadProductoCom;

-- CALL sp_agregarDetalleCompra(50,2,1);
-- SELECT * FROM Compras;
-- SELECT * FROM Productos;
-- SELECT * FROM DetalleCompra;


-- Promociones
-- 
DELIMITER $$
CREATE FUNCTION fn_promo(factId INT) RETURNS DOUBLE(10,2) DETERMINISTIC
BEGIN
	DECLARE promocion DECIMAL(10,2);
    DECLARE obtenerPrecio DECIMAL(10,2);
    DECLARE fecha DATE;
    DECLARE precioFinal DECIMAL(10,2);
    DECLARE total DECIMAL(10,2) DEFAULT 0;
    DECLARE i INT DEFAULT 1;
    
    contador : LOOP
    
    IF factId = (SELECT DetalleFactura.facturaId FROM DetalleFactura WHERE detalleFacturaId = i) THEN
		SET obtenerPrecio = (SELECT Productos.precioVentaUnitario FROM Productos WHERE productoId = (SELECT productoId FROM DetalleFactura WHERE detalleFacturaId = i));
        SET fecha = (SELECT Promociones.fechaFinalizacion FROM Promociones WHERE productoId = (SELECT D.productoId FROM DetalleFactura D WHERE detalleFacturaId = i));
        SET promocion = (SELECT Promociones.precioPromocion FROM Promociones WHERE productoId = (SELECT D.productoId FROM DetalleFactura D WHERE detalleFacturaId = i));
        
        IF fecha >= NOW() THEN
			SET precioFinal = promocion;
		END IF;
        
		IF fecha < NOW() THEN
			SET precioFinal = obtenerPrecio;
		END IF;
        
		SET total = total + precioFinal;
    END IF;
    IF i = (SELECT COUNT(*) FROM DetalleFactura) THEN
		LEAVE contador;
    END IF;
    
    SET i = i + 1;
    END LOOP contador;
    
    CALL sp_asignarTotalFact(factId, total);
    
    RETURN total;
END $$
DELIMITER ;
call sp_editarpromociones(1,36,'20%', '2024/01/24', 1);



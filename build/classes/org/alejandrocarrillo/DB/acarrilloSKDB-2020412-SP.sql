-- SP

-- CLIENTE
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarClientes(IN n17 VARCHAR(15), IN nom VARCHAR(30), IN ape VARCHAR(30), IN tel VARCHAR(15), IN dir VARCHAR(200))
BEGIN
	INSERT INTO Clientes(nit, nombre, apellido, telefono, direccion, logoId) VALUES
		(n17,nom,ape,tel,dir,1);
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_listarClientes()
BEGIN
	SELECT
		Clientes.clienteId,
		Clientes.nit,
        Clientes.nombre,
        Clientes.apellido,
        Clientes.telefono,
        Clientes.direccion
			FROM Clientes;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarClientes(IN cliId INT)
BEGIN
	SELECT
		Clientes.clienteId,
		Clientes.nit,
        Clientes.nombre,
        Clientes.apellido,
        Clientes.telefono,
        Clientes.direccion
			FROM Clientes
				WHERE clienteId = cliId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarClientes(IN cliId INT)
BEGIN
	DELETE FROM Clientes
		WHERE clienteId = cliId;
		
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarClientes(IN cliId INT, IN n17 VARCHAR(15), IN nom VARCHAR(30), IN ape VARCHAR(30), IN tel VARCHAR(15), IN dir VARCHAR(200))
BEGIN
	UPDATE Clientes
		SET
			nit = n17,
            nombre = nom,
            apellido = ape,
            telefono = tel,
            direccion = dir,
            logoId = 1
				WHERE clienteId = cliId;
END$$
DELIMITER ;


-- CARGOS
--
DELIMITER $$
CREATE PROCEDURE sp_agregarCargos(IN nom VARCHAR(30), IN des VARCHAR(100))
BEGIN
	INSERT INTO Cargos(nombre, descripcionCargo) VALUES
		(nom,des);
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_listarCargos()
BEGIN
	SELECT
		Cargos.cargoId,
		Cargos.nombre,
        Cargos.descripcionCargo
			FROM Cargos;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarCargos(IN carId INT)
BEGIN
	SELECT
		Cargos.cargoId,
		Cargos.nombre,
        Cargos.descripcionCargo
			FROM Cargos
				WHERE cargoId = carId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarCargos(IN carId INT)
BEGIN
	DELETE FROM Cargos
		WHERE cargoId = carId;
		
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarCargos(IN carId INT, IN nom VARCHAR(30), IN des VARCHAR(100))
BEGIN
	UPDATE Cargos
		SET
			nombre = nom,
            descripcionCargo = des
				WHERE cargoId = carId;
        
END$$
DELIMITER ;


-- EMPLEADOS
--
DELIMITER $$
CREATE PROCEDURE sp_agregarEmpleados(IN nom VARCHAR(30), IN ape VARCHAR(30), IN sue DECIMAL(10,2), IN ent TIME, IN sal TIME, IN carId INT)
BEGIN
	INSERT INTO Empleados(nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId) VALUES
		(nom,ape,sue,ent,sal,carId);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarEmpleados()
BEGIN
	SELECT
		E.empleadoId, E.nombreEmpleado, E.apellidoEmpleado, E.sueldo, E.horaEntrada, E.horaSalida, 
        CONCAT('Id: ',C.cargoId,' | ', C.nombre) AS 'cargo', encargadoId FROM Empleados E
		JOIN Cargos C on E.cargoId = C.cargoId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_listarEmpleados2()
BEGIN
	SELECT
		E.empleadoId, E.nombreEmpleado, E.apellidoEmpleado FROM Empleados E;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarEmpleados(IN empId INT)
BEGIN
	SELECT
		E.empleadoId, E.nombreEmpleado, E.apellidoEmpleado, E.sueldo, E.horaEntrada, E.horaSalida, 
        CONCAT('Id: ',C.cargoId,' | ', C.nombre) AS 'cargo', encargadoId FROM Empleados E
		JOIN Cargos C on E.cargoId = C.cargoId
				WHERE empleadoId = empId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarEmpleados(IN empId INT)
BEGIN
	DELETE FROM Empleados
		WHERE empleadoId = empId;
		
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarEmpleados(IN empId INT,IN nom VARCHAR(30), IN ape VARCHAR(30), IN sue DECIMAL(10,2), IN ent TIME, IN sal TIME, IN carId INT)
BEGIN
	UPDATE Empleados
		SET
			empleadoId = empId,
            nombreEmpleado = nom,
            apellidoEmpleado = ape,
            sueldo = sue,
            horaEntrada = ent,
            horaSalida = sal,
            cargoId = carId
				WHERE empleadoId = empId;
        
END$$
DELIMITER ;


-- COMPRAS
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarCompras(IN fec DATE, IN tot DECIMAL(10,2))
BEGIN
	INSERT INTO Compras(fechaCompra,totalCompra) VALUES
		(fec,tot);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarCompras()
BEGIN
	SELECT
		Compras.compraId,
		Compras.fechaCompra,
        Compras.totalCompra 
			FROM Compras;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarCompras(IN comId INT)
BEGIN
	SELECT
		Compras.compraId,
		Compras.fechaCompra,
        Compras.totalCompra
			FROM Compras
				WHERE compraId  = comId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarCompras(IN comId INT)
BEGIN
	DELETE FROM Compras
		WHERE compraId = comId;
		
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarCompras(IN comId INT, IN fec DATE, IN tot DECIMAL(10,2))
BEGIN
	UPDATE Compras
		SET
			fechaCompra = fec,
            totalCompra = tot
				WHERE compraId = comId;
        
END$$
DELIMITER ;


-- CATEGORIA PRODUCTOS
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarCategoriaProductos(IN nom VARCHAR(30), IN des VARCHAR(100))
BEGIN
	INSERT INTO CategoriaProductos(nombreCategoria ,descripcionCategoria) VALUES
		(nom,des);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarCategoriaProductos()
BEGIN
	SELECT
		CategoriaProductos.categoriaProductosId,
		CategoriaProductos.nombreCategoria,
        CategoriaProductos.descripcionCategoria 
			FROM CategoriaProductos;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarCategoriaProductos(IN cprId INT)
BEGIN
	SELECT
		CategoriaProductos.categoriaProductosId,
		CategoriaProductos.nombreCategoria,
        CategoriaProductos.descripcionCategoria 
			FROM CategoriaProductos
				WHERE categoriaProductosId = cprId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarCategoriaProductos(IN cprId INT)
BEGIN
	DELETE FROM CategoriaProductos
		WHERE categoriaProductosId = cprId;
		
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarCategoriaProductos(IN cprId INT, IN nom VARCHAR(30), IN des VARCHAR(100))
BEGIN
	UPDATE CategoriaProductos
		SET
			nombreCategoria  = nom,
            descripcionCategoria  = des
				WHERE categoriaProductosId = cprId;
        
END$$
DELIMITER ;

-- FACTURAS
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarFacturas(IN cliId INT, IN empId INT)
BEGIN
	INSERT INTO Facturas(fecha,hora, clienteId, empleadoId, total, logoId) VALUES
		(NOW(),CURTIME(),cliId,empId,null,1);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarFacturas()
BEGIN
	SELECT
		F.facturaId, F.fecha, F.hora,
        CONCAT('Id: ',C.clienteId,' | ', C.nombre,' ',C.apellido) AS 'cliente', 
        CONCAT('Id: ',E.empleadoId,' | ', E.nombreEmpleado,' ',E.apellidoEmpleado) AS 'empleado',
        total FROM Facturas F
		JOIN Clientes C ON F.clienteId = C.clienteId
        JOIN Empleados E ON F.empleadoId = E.empleadoId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarFacturas(IN facId INT)
BEGIN
	SELECT
		F.facturaId, F.fecha, F.hora,
        CONCAT('Id: ',C.clienteId,' | ', C.nombre,' ',C.apellido) AS 'cliente', 
        CONCAT('Id: ',E.empleadoId,' | ', E.nombreEmpleado,' ',E.apellidoEmpleado) AS 'empleado',
        total FROM Facturas F
		JOIN Clientes C ON F.clienteId = C.clienteId
        JOIN Empleados E ON F.empleadoId = E.empleadoId
				WHERE facturaId = facId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarFacturas(IN facId INT)
BEGIN
	DELETE FROM Facturas
		WHERE facturaId = facId;
		
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarFacturas(IN cliId INT, IN empId INT)
BEGIN
	UPDATE Facturas
		SET
            clienteId = cliId,
            empleadoId = empId,
            logoId = 1
				WHERE facturaId = facId;
END$$
DELIMITER ;


-- TICKET SOPORTE
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarTicketSoporte(IN des VARCHAR(250), IN cliId INT, IN facId INT)
BEGIN
	INSERT INTO TicketSoporte(descripcionTicket, estatus, clienteId, facturaId) VALUES
		(des,'Recién Creado',cliId,facId);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarTicketSoporte()
BEGIN
	SELECT
		T.ticketSoporteId, T.descripcionTicket, T.estatus, 
        CONCAT('Id: ',C.clienteId,' | ', C.nombre,' ', C.apellido) AS 'cliente', CONCAT('Id: ',F.facturaId,' | ', F.fecha) AS 'factura' FROM TicketSoporte T
		JOIN Clientes C on T.clienteId = C.clienteId
        JOIN Facturas F on T.facturaId = F.facturaId;

END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarTicketSoporte(IN ticId INT)
BEGIN
	SELECT
		T.ticketSoporteId, T.descripcionTicket, T.estatus, 
        CONCAT('Id: ',C.clienteId,' | ', C.nombre,' ', C.apellido) AS 'cliente', CONCAT('Id: ',F.facturaId,' | ', F.fecha) AS 'factura' FROM TicketSoporte T
		JOIN Clientes C on T.clienteId = C.clienteId
        JOIN Facturas F on T.facturaId = F.facturaId
				WHERE ticketSoporteId = ticId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarTicketSoporte(IN ticId INT)
BEGIN
	DELETE FROM TicketSoporte
		WHERE ticketSoporteId = ticId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarTicketSoporte(IN ticId INT, IN des VARCHAR(250), IN est VARCHAR(30), IN cliId INT, IN facId INT)
BEGIN
	UPDATE TicketSoporte
		SET
			descripcionTicket = des,
            estatus = est,
            clienteId = cliId,
            facturaId = facId
				WHERE ticketSoporteId = ticId;
END$$
DELIMITER ;


-- DISTRIBUIDORES
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarDistribuidores(IN nom VARCHAR(30), IN dir VARCHAR(200), IN nit VARCHAR(15), IN tel VARCHAR(15), IN w3b VARCHAR(50))
BEGIN
	INSERT INTO Distribuidores(nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web) VALUES
		(nom,dir,nit,tel,w3b);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarDistribuidores()
BEGIN
	SELECT
		Distribuidores.distribuidorId ,
		Distribuidores.nombreDistribuidor,
        Distribuidores.direccionDistribuidor,
        Distribuidores.nitDistribuidor,
        Distribuidores.telefonoDistribuidor,
        Distribuidores.web
			FROM Distribuidores;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarDistribuidores(IN disId INT)
BEGIN
	SELECT
		Distribuidores.distribuidorId ,
		Distribuidores.nombreDistribuidor,
        Distribuidores.direccionDistribuidor,
        Distribuidores.nitDistribuidor,
        Distribuidores.telefonoDistribuidor,
        Distribuidores.web
			FROM Distribuidores
				WHERE distribuidorId = disId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarDistribuidores(IN disId INT)
BEGIN
	DELETE FROM Distribuidores
		WHERE distribuidorId = disId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarDistribuidores(IN disId INT, IN nom VARCHAR(30), IN dir VARCHAR(200), IN nit VARCHAR(15), IN tel VARCHAR(15), IN w3b VARCHAR(50))
BEGIN
	UPDATE Distribuidores
		SET
			nombreDistribuidor = nom,
            direccionDistribuidor = dir,
            nitDistribuidor = nit,
            telefonoDistribuidor = tel,
            web = w3b
				WHERE distribuidorId = disId;
END$$
DELIMITER ;


-- PRODUCTOS
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarProductos(IN nom VARCHAR(50), IN des VARCHAR(100), IN can INT, IN uni DECIMAL(10,2), IN may DECIMAL(10,2), IN com DECIMAL(10,2), IN ima LONGBLOB, IN disId INT, IN catId INT)
BEGIN
	INSERT INTO Productos(nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidorId, categoriaProductosId, logoId) VALUES
		(nom,des,can,uni,may,com,ima,disId,catId,1);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarProductos()
BEGIN
	SELECT
		P.productoId, P.nombreProducto, P.descripcionProducto, P.cantidadStock, P.precioVentaUnitario, P.precioVentaMayor, P.precioCompra, P.imagenProducto, 
        CONCAT('Id: ',D.distribuidorId,' | ', D.nombreDistribuidor) AS 'distribuidor',
        CONCAT('Id: ',C.categoriaProductosId,' | ', C.nombreCategoria) AS 'categoriaProductos'
        FROM Productos P
		JOIN Distribuidores D ON P.distribuidorId = D.distribuidorId
        JOIN CategoriaProductos C ON P.categoriaProductosId = C.categoriaProductosId;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarProductos(IN proId INT)
BEGIN
	SELECT
		P.productoId, P.nombreProducto, P.descripcionProducto, P.cantidadStock, P.precioVentaUnitario, P.precioVentaMayor, P.precioCompra, P.imagenProducto, 
        CONCAT('Id: ',D.distribuidorId,' | ', D.nombreDistribuidor) AS 'distribuidor',
        CONCAT('Id: ',C.categoriaProductosId,' | ', C.nombreCategoria) AS 'categoriaProductos'
        FROM Productos P
		JOIN Distribuidores D ON P.distribuidorId = D.distribuidorId
        JOIN CategoriaProductos C ON P.categoriaProductosId = C.categoriaProductosId
				WHERE productoId = proId;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarProductos(IN proId INT)
BEGIN
	DELETE FROM Productos
		WHERE productoId = proId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarProductos(IN proId INT, IN nom VARCHAR(50), IN des VARCHAR(100), IN can INT, IN uni DECIMAL(10,2), IN may DECIMAL(10,2), IN com DECIMAL(10,2), IN ima LONGBLOB, IN disId INT, IN catId INT)
BEGIN
	UPDATE Productos
		SET
			nombreProducto = nom,
            descripcionProducto = des,
            cantidadStock = can,
            precioVentaUnitario = uni,
            precioVentaMayor = may,
            precioCompra = com,
            imagenProducto = ima,
            distribuidorId = disId,
            categoriaProductosId = catId,
            logoId = 1
				WHERE productoId = proId;
END$$
DELIMITER ;


-- DETALLE COMPRA
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarDetalleCompra(IN can INT, IN proId INT, IN comId INT)
BEGIN
	INSERT INTO DetalleCompra(cantidadCompra, productoId, compraId) VALUES
		(can,proId,comId);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarDetalleCompra()
BEGIN
	SELECT
		D.detalleCompraId, D.cantidadCompra,
        CONCAT('Id: ',P.productoId,' | ', P.nombreProducto) AS 'producto',
        CONCAT('Id: ',C.compraId,' | ', C.fechaCompra) AS 'compra',
        C.totalCompra
        FROM DetalleCompra D
        JOIN Productos P ON D.productoId = P.productoId
        JOIN Compras C ON D.compraId = C.compraId;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarDetalleCompra(IN comId INT)
BEGIN
	SELECT
		D.detalleCompraId, D.cantidadCompra,
        CONCAT('Id: ',P.productoId,' | ', P.nombreProducto) AS 'producto',
        CONCAT('Id: ',C.compraId,' | ', C.fechaCompra) AS 'compra'
        FROM DetalleCompra D
        JOIN Productos P ON D.productoId = P.productoId
        JOIN Compras C ON D.compraId = C.compraId
				WHERE detalleCompraId = comId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleCompra(IN dcomId INT)
BEGIN
	DELETE FROM DetalleCompra
		WHERE detalleCompraId = dcomId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarDetalleCompra(IN dcomId INT, IN can INT, IN proId INT, IN comId INT)
BEGIN
	UPDATE DetalleCompra
		SET
			cantidadCompra = can,
            productoId = proId,
            compraId = comId
				WHERE detalleCompraId = dcomId;
END$$
DELIMITER ;


-- PROMOCIONES
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarPromociones(IN pre DECIMAL(10,2), IN des VARCHAR(100), IN fin DATE, IN proId INT)
BEGIN
	INSERT INTO Promociones(precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, productoId) VALUES
		(pre,des,NOW(),fin,proId);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarPromociones()
BEGIN
	SELECT
		P.promocionId, P.precioPromocion, P.descripcionPromocion, P.fechaInicio, P.fechaFinalizacion,
        CONCAT('Id: ',Pr.productoId,' | ', Pr.nombreProducto) AS 'producto' FROM Promociones P
		JOIN Productos Pr on P.productoId = Pr.productoId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarPromociones(IN promId INT)
BEGIN
	SELECT
		P.promocionId, P.precioPromocion, P.descripcionPromocion, P.fechaInicio, P.fechaFinalizacion,
        CONCAT('Id: ',Pr.productoId,' | ', Pr.nombreProducto) AS 'producto' FROM Promociones P
		JOIN Productos Pr on P.productoId = Pr.productoId
				WHERE promocionId = promId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarPromociones(IN promId INT)
BEGIN
	DELETE FROM Promociones
		WHERE promocionId = promId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarPromociones(IN promId INT, IN pre DECIMAL(10,2), IN des VARCHAR(100), IN fin DATE, IN proId INT)
BEGIN
	UPDATE Promociones
		SET
			precioPromocion = pre,
            descripcionPromocion = des,
            fechaFinalizacion = fin,
            productoId = proId
				WHERE promocionId = promId;
END$$
DELIMITER ;


-- DETALLE FACTURA
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarDetalleFactura(IN facId INT, IN proId INT)
BEGIN
	INSERT INTO DetalleFactura(facturaId, productoId) VALUES
		(facId,proId);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarDetalleFactura()
BEGIN
	SELECT
		D.detalleFacturaId, 
        CONCAT('Id: ',F.facturaId,' | ', F.fecha) AS 'factura',
        CONCAT('Id: ',P.productoId,' | ', P.nombreProducto) AS 'producto'
        FROM DetalleFactura D
		JOIN Facturas F ON D.facturaId = F.facturaId
        JOIN Productos P ON D.productoId = P.productoId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_buscarDetalleFactura(IN detFId INT)
BEGIN
	SELECT
		D.detalleFacturaId, 
        CONCAT('Id: ',F.facturaId,' | ', F.fecha) AS 'factura',
        CONCAT('Id: ',P.productoId,' | ', P.nombreProducto) AS 'producto'
        FROM DetalleFactura D
		JOIN Facturas F ON D.facturaId = F.facturaId
        JOIN Productos P ON D.productoId = P.productoId
				WHERE detalleFacturaId = detFId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleFactura(IN detFId INT)
BEGIN
	DELETE FROM DetalleFactura
		WHERE detalleFacturaId = detFId;
END$$
DELIMITER ; 

DELIMITER $$
CREATE PROCEDURE sp_editarDetalleFactura(IN detFId INT,IN facId INT, IN proId INT)
BEGIN
	UPDATE DetalleFactura
		SET
			facturaId = facId,
            productoId = proId
				WHERE detalleFacturaId = detFId;
END$$
DELIMITER ;

-- NIVELES ACCESO
-- 
DELIMITER $$
CREATE PROCEDURE sp_listarNivelesAcceso()
BEGIN
	SELECT * FROM NivelesAcceso;
END $$
DELIMITER ;


-- USUARIOS
-- 
DELIMITER $$
CREATE PROCEDURE sp_agregarUsuarios(IN usu VARCHAR(30), IN con VARCHAR(250), IN nivId INT, IN empId INT)
BEGIN
	INSERT INTO Usuarios(usuario, contra, nivelAccesoId, empleadoId) VALUES
		(usu,con,nivId,empId);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarUsuarios(us VARCHAR(30))
BEGIN
	SELECT * FROM Usuarios
		WHERE usuario = us;
END $$
DELIMITER ;
DROP DATABASE IF EXISTS acarrilloSK_20204121;

CREATE DATABASE IF NOT EXISTS acarrilloSK_20204121;

USE acarrilloSK_20204121;

CREATE TABLE Clientes(
	clienteId INT NOT NULL AUTO_INCREMENT,
    nit VARCHAR(15),
    nombre VARCHAR(30) NOT NULL,
    apellido VARCHAR(30) NOT NULL,
    telefono VARCHAR(15),
    direccion VARCHAR(200) NOT NULL	,
    PRIMARY KEY PK_clienteId (clienteId)
);

CREATE TABLE Cargos(
	cargoId INT NOT NULL AUTO_INCREMENT,
	nombre VARCHAR(30) NOT NULL,
    descripcionCargo VARCHAR(100) NOT NULL,
    PRIMARY KEY PK_cargoId (cargoId)
);

CREATE TABLE Empleados(
	empleadoId INT NOT NULL AUTO_INCREMENT,
    nombreEmpleado VARCHAR(30) NOT NULL,
    apellidoEmpleado VARCHAR(30) NOT NULL,
    sueldo DECIMAL(10,2) NOT NULL,
    horaEntrada TIME NOT NULL,
    horaSalida TIME NOT NULL,
    cargoId INT NOT NULL,
    encargadoId INT,
    PRIMARY KEY PK_empleadoId (empleadoId), 
    CONSTRAINT FK_Empleados_Cargos FOREIGN KEY(cargoId)
		REFERENCES Cargos(cargoId),
	CONSTRAINT FK_Encargado FOREIGN KEY(encargadoId)
		REFERENCES Empleados(empleadoId)
);

CREATE TABLE Compras(
	compraId INT NOT NULL AUTO_INCREMENT,
    fechaCompra DATE NOT NULL,
    totalCompra DECIMAL(10,2),
    PRIMARY KEY PK_compraId (compraId)
);

CREATE TABLE CategoriaProductos(
	categoriaProductosId INT NOT NULL AUTO_INCREMENT,
    nombreCategoria VARCHAR(30) NOT NULL,
    descripcionCategoria VARCHAR(100) NOT NULL,
    PRIMARY KEY PK_categoriaProductosId (categoriaProductosId)
);

CREATE TABLE Facturas(
	facturaId INT NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    clienteId INT NOT NULL,
    empleadoId INT NOT NULL,
    total DECIMAL(10,2),
    PRIMARY KEY PK_facturaId (facturaId),
    CONSTRAINT FK_Facturas_Clientes FOREIGN KEY (clienteId)
		REFERENCES Clientes(clienteId),
	CONSTRAINT FK_Facturas_Empleados FOREIGN KEY (empleadoId)
		REFERENCES Empleados(empleadoId)
); 

CREATE TABLE TicketSoporte(
	ticketSoporteId INT NOT NULL AUTO_INCREMENT,
    descripcionTicket VARCHAR(250) NOT NULL,
    estatus VARCHAR(30) NOT NULL,
    clienteId INT NOT NULL,
    facturaId INT,
    PRIMARY KEY PK_ticketSoporteId (ticketSoporteId),
    CONSTRAINT FK_TicketSoporte_Clientes FOREIGN KEY (clienteId)
	 	REFERENCES Clientes(clienteId),
	CONSTRAINT FK_TicketSoporte_Facturas FOREIGN KEY (facturaId)
	 	REFERENCES Facturas(facturaId)
);

CREATE TABLE Distribuidores(
	distribuidorId INT NOT NULL AUTO_INCREMENT,
    nombreDistribuidor VARCHAR(30) NOT NULL,
    direccionDistribuidor VARCHAR(200) NOT NULL,
    nitDistribuidor VARCHAR(15) NOT NULL,
    telefonoDistribuidor VARCHAR(15) NOT NULL,
    web VARCHAR(50),
    PRIMARY KEY PK_distribuidorId (distribuidorId)
);

CREATE TABLE Productos(
	productoId INT NOT NULL AUTO_INCREMENT,
    nombreProducto VARCHAR(50) NOT NULL,
    descripcionProducto VARCHAR(100),
    cantidadStock INT NOT NULL,
    precioVentaUnitario DECIMAL(10,2) NOT NULL,	
    precioVentaMayor DECIMAL(10,2) NOT NULL,
    precioCompra DECIMAL(10,2) NOT NULL,
    imagenProducto LONGBLOB,
    distribuidorId INT NOT NULL,
    categoriaProductosId INT NOT NULL,
    PRIMARY KEY PK_productoId (productoId),
    CONSTRAINT FK_Productos_Distribuidores FOREIGN KEY (distribuidorId)
		REFERENCES Distribuidores(distribuidorId),
	CONSTRAINT FK_Productos_CategoriaProductos FOREIGN KEY (categoriaProductosId)
		REFERENCES CategoriaProductos(categoriaProductosId)
);

CREATE TABLE DetalleCompra(
	detalleCompraId INT NOT NULL AUTO_INCREMENT,
    cantidadCompra INT NOT NULL,
    productoId INT NOT NULL,
    compraId INT NOT NULL,
    PRIMARY KEY PK_detalleCompraId (detalleCompraId),
    CONSTRAINT FK_DetalleCompra_Productos FOREIGN KEY (productoId)
		REFERENCES Productos(productoId),
	CONSTRAINT FK_DetalleCompra_Compras FOREIGN KEY (compraId)
		REFERENCES Compras(compraId)
);

CREATE TABLE Promociones(
	promocionId INT NOT NULL AUTO_INCREMENT,
    precioPromocion DECIMAL(10,2) NOT NULL,
    descripcionPromocion VARCHAR(100),
    fechaInicio DATE NOT NULL,
    fechaFinalizacion DATE NOT NULL,
    productoId INT NOT NULL,
    PRIMARY KEY PK_promocionId (promocionId),
    CONSTRAINT FK_Promociones_Productos FOREIGN KEY (productoId)
		REFERENCES Productos(productoId)
);

CREATE TABLE DetalleFactura(
	detalleFacturaId INT NOT NULL AUTO_INCREMENT,
    facturaId INT NOT NULL,
    productoId INT NOT NULL,
    PRIMARY KEY PK_detalleFacturaId (detalleFacturaId),
    CONSTRAINT FK_DetalleFactura_Facturas FOREIGN KEY (facturaId)
		REFERENCES Facturas(facturaId),
	CONSTRAINT FK_DetalleFactura_Productos FOREIGN KEY (productoId)
		REFERENCES Productos(productoId)
);

CREATE TABLE NivelesAcceso(
	nivelAccesoId INT NOT NULL AUTO_INCREMENT,
    nivelAcceso VARCHAR(40) NOT NULL,
    PRIMARY KEY PK_nivelAccesoId (nivelAccesoId)
);

CREATE TABLE Usuarios(
	usuarioId INT NOT NULL AUTO_INCREMENT,
    usuario VARCHAR(30) NOT NULL,
    contra VARCHAR(250) NOT NULL,
    nivelAccesoId INT NOT NULL,
    empleadoId INT NOT NULL,
    PRIMARY KEY PK_usuarioId (usuarioId),
    CONSTRAINT FK_Usuarios_NivelesAcceso FOREIGN KEY (nivelAccesoId)
		REFERENCES NivelesAcceso(nivelAccesoId),
	CONSTRAINT FK_Usuarios_Empleados FOREIGN KEY (empleadoId)
		REFERENCES Empleados(empleadoId)
);

CREATE TABLE Logo(
	logoId INT NOT NULL AUTO_INCREMENT,
    logo LONGBLOB,
    detalleFacturaId INT,
    detalleCompraId INT,
    productoId INT,
    PRIMARY KEY PK_logoId (logoId),
    CONSTRAINT FK_Logo_DetalleFactura FOREIGN KEY (detalleFacturaId)
		REFERENCES DetalleFactura(detalleFacturaId),
	CONSTRAINT FK_Logo_DetalleCompra FOREIGN KEY (detalleCompraId)
		REFERENCES DetalleCompra(detalleCompraId),
	CONSTRAINT FK_Logo_Productos FOREIGN KEY (productoId)
		REFERENCES Productos(productoId)
);

-- INSERTAR VALORES
	  
INSERT INTO Clientes(nit, nombre, apellido, telefono, direccion) VALUES
	('090912-7','Juan','Fernandez','4301-2312','Ciudad'),
    ('785412-4','Hugo','Velasquez','5310-7897','Ciudad');
	  
INSERT INTO Cargos(nombre, descripcionCargo) VALUES
	('Cargo1','Cargo1'),
    ('Cargo2','Cargo2');
    
INSERT INTO Empleados(nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId, encargadoId) VALUES
	('Pedro','Juarez',850,'08:20', '17:30',1,NULL),
    ('Jose','Barrera',1500,'08:20', '17:30',2,NULL);
    
INSERT INTO Compras(fechaCompra, totalCompra) VALUES
	(NOW(), NULL),
    (NOW(), NULL);
    
INSERT INTO CategoriaProductos(nombreCategoria, descripcionCategoria) VALUES
	('Categoria1','Categoria1'),
    ('Categoria2','Categoria');
    
INSERT INTO Facturas(fecha, hora, clienteId, empleadoId, total) VALUES
	(NOW(), CURTIME(),1,2,NULL),
    (NOW(), CURTIME(),2,1,NULL);
    
INSERT INTO TicketSoporte(descripcionTicket, estatus, clienteId, facturaId) VALUES
	('Ticket Soporte 1', 'Recién Creado',1,2),
    ('Ticket Soporte 2', 'Recién Creado',2,1);
    
INSERT INTO Distribuidores(nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web) VALUES
	('Jorge','Casa','54641-4','5678-9812', NULL),
    ('Paco','Casa','48212-7','3645-2456', NULL);
    
INSERT INTO Productos(nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidorId, categoriaProductosId) VALUES
	('Cereal','Cereal de Chocolate',100,45,40,35,NULL,1,2),
    ('Pan','Pan Rodajado',120,74,67,61,NULL,2,1);
    
INSERT INTO DetalleCompra(cantidadCompra, productoId, compraId) VALUES
	(46,1,2),
    (58,2,1);
    
INSERT INTO Promociones(precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, productoId) VALUES
	(36,'20%', '2024/02/12','2024/03/12',1),
    (55.5,'25%', '2024/02/12','2024/03/12',2);
    
INSERT INTO DetalleFactura(facturaId , productoId) VALUES
	(1,2),
    (2,1);
    
INSERT INTO NivelesAcceso(nivelAcceso) VALUES
	('Admin'),
    ('Usuario');
    
INSERT INTO Usuarios(usuario, contra, nivelAccesoId, empleadoId) VALUES
	('acarrillo','$2a$10$P4a3hk.4fPTms8Ay8HmdNuokVmiBY8zfGJmv759YIyfGpRsN1b86m',1,1),
    ('phernandez','hola123',2,1);
    
INSERT INTO Logo(logo,detalleFacturaId,detalleCompraId, productoId) VALUES
	(NULL,1,1,1),
    (NULL,2,2,2),
    (NULL,1,2,1),
    (NULL,2,1,2);
    
SELECT * FROM Clientes;
SELECT * FROM Cargos;
SELECT * FROM Empleados;
SELECT * FROM Compras;
SELECT * FROM CategoriaProductos;
SELECT * FROM Facturas;
SELECT * FROM TicketSoporte;
SELECT * FROM Distribuidores;
SELECT * FROM Productos;
SELECT * FROM DetalleCompra;
SELECT * FROM Promociones;
SELECT * FROM DetalleFactura;
SELECT * FROM NivelesAcceso;
SELECT * FROM Usuarios;
SELECT * FROM Logo;
 
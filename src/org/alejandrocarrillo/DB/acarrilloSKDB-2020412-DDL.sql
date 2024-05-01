DROP DATABASE IF EXISTS acarrilloSK_2020412;

CREATE DATABASE IF NOT EXISTS acarrilloSK_2020412;

USE acarrilloSK_2020412;

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
    imagenProducto BLOB,
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
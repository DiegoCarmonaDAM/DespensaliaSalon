CREATE DATABASE DespensaliaSalon;
USE DespensaliaSalon;

-- =========================
-- TABLA CLIENTES
-- =========================
CREATE TABLE TClientes (
    IdCliente VARCHAR(12) PRIMARY KEY,
    Password VARCHAR(12) NOT NULL,
    Nombre VARCHAR(20) NOT NULL,
    Apellidos VARCHAR(40) NOT NULL,
    Telefono VARCHAR(10),
    Email VARCHAR(50) NOT NULL
);

-- =========================
-- TABLA PRODUCTOS
-- =========================
CREATE TABLE TProductos (
    IdProducto INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(20) NOT NULL,
    Tipo VARCHAR(10) NOT NULL,   -- Entrante, Plato, Postre, Varios
    Descripcion VARCHAR(50),
    Precio DECIMAL(4,2) NOT NULL,
    Disponible TINYINT NOT NULL
);

-- =========================
-- TABLA PEDIDOS (SALÓN)
-- =========================
CREATE TABLE TPedidos (
    IdPedido INT AUTO_INCREMENT PRIMARY KEY,
    IdCliente VARCHAR(12) NOT NULL,
    FHPedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FHReserva DATETIME NOT NULL,     -- Fecha y hora de reserva en el salón
    Importe DECIMAL(6,2) NOT NULL,
    Observaciones VARCHAR(100),

    CONSTRAINT FK_Pedidos_Clientes
        FOREIGN KEY (IdCliente)
        REFERENCES TClientes(IdCliente)
);

-- =========================
-- TABLA LÍNEAS DE PEDIDO
-- =========================
CREATE TABLE TPedidoLineas (
    IdLinea INT AUTO_INCREMENT PRIMARY KEY,
    IdPedido INT NOT NULL,
    IdProducto INT NOT NULL,
    Cantidad TINYINT NOT NULL,
    Precio DECIMAL(4,2) NOT NULL,

    CONSTRAINT FK_Lineas_Pedidos
        FOREIGN KEY (IdPedido)
        REFERENCES TPedidos(IdPedido),

    CONSTRAINT FK_Lineas_Productos
        FOREIGN KEY (IdProducto)
        REFERENCES TProductos(IdProducto)
);

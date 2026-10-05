-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS LirioSeda
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE LirioSeda;

-- ============================================================
-- CREACIÓN DE TABLAS
-- ============================================================

CREATE TABLE DetalleEntrada (
    iddetalleentrada    INT NOT NULL AUTO_INCREMENT,
    cantidad            INT,
    preciounitario      DECIMAL(10,2),
    Entrada_identrada   VARCHAR(10) NOT NULL,
    Producto_idproducto VARCHAR(10) NOT NULL,
    PRIMARY KEY (iddetalleentrada)
) ENGINE=InnoDB;

CREATE TABLE DetallePedido (
    iddetallepedido     INT NOT NULL AUTO_INCREMENT,
    cantidad            INT,
    Pedido_idpedido     VARCHAR(10) NOT NULL,
    Producto_idproducto VARCHAR(10) NOT NULL,
    PRIMARY KEY (iddetallepedido)
) ENGINE=InnoDB;

CREATE TABLE DetalleSalida (
    iddetallesalida     INT NOT NULL AUTO_INCREMENT,
    cantidad            INT,
    Producto_idproducto VARCHAR(10) NOT NULL,
    Salida_idsalida     VARCHAR(10) NOT NULL,
    idproducto          VARCHAR(10),
    idsalida            VARCHAR(10),
    PRIMARY KEY (iddetallesalida)
) ENGINE=InnoDB;

CREATE TABLE Entrada (
    identrada             VARCHAR(10) NOT NULL,
    fechaentrada          DATE,
    importe_total         DECIMAL(10,2),
    idsedeusuario         INT,
    Proveedor_idproveedor VARCHAR(10) NOT NULL,
    Usuario_idusuario     INT NOT NULL,
    PRIMARY KEY (identrada)
) ENGINE=InnoDB;

CREATE TABLE Estado (
    idestado    INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(50),
    PRIMARY KEY (idestado)
) ENGINE=InnoDB;

CREATE TABLE Modelo (
    idmodelo    INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(150),
    PRIMARY KEY (idmodelo)
) ENGINE=InnoDB;

CREATE TABLE Motivo (
    idmotivo    INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(150),
    PRIMARY KEY (idmotivo)
) ENGINE=InnoDB;

CREATE TABLE Pedido (
    idpedido          VARCHAR(10) NOT NULL,
    fechapedido       DATE,
    fechaaprobacion   DATE,
    idsedeusuario     INT,
    Estado_idestado   INT NOT NULL,
    Usuario_idusuario INT NOT NULL,
    PRIMARY KEY (idpedido)
) ENGINE=InnoDB;

CREATE TABLE Producto (
    idproducto      VARCHAR(10) NOT NULL,
    nombre          VARCHAR(150),
    precio          DECIMAL(10,2),
    estado          CHAR(1),
    Modelo_idmodelo INT NOT NULL,
    PRIMARY KEY (idproducto)
) ENGINE=InnoDB;

CREATE TABLE ProductoSede (
    stock               INT,
    Sede_idsede         INT NOT NULL,
    Producto_idproducto VARCHAR(10) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE Proveedor (
    idproveedor VARCHAR(10) NOT NULL,
    nombre      VARCHAR(250),
    telefono    VARCHAR(50),
    correo      VARCHAR(150),
    direccion   VARCHAR(250),
    ruc         VARCHAR(20),
    PRIMARY KEY (idproveedor)
) ENGINE=InnoDB;

CREATE TABLE Rol (
    idrol       INT NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(510),
    descripcion VARCHAR(150),
    PRIMARY KEY (idrol)
) ENGINE=InnoDB;

CREATE TABLE Salida (
    idsalida          VARCHAR(10) NOT NULL,
    fechasalida       DATE,
    idsedeusuario     INT,
    Motivo_idmotivo   INT NOT NULL,
    Usuario_idusuario INT NOT NULL,
    PRIMARY KEY (idsalida)
) ENGINE=InnoDB;

CREATE TABLE Sede (
    idsede      INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(250),
    direccion   VARCHAR(250),
    telefono    VARCHAR(50),
    PRIMARY KEY (idsede)
) ENGINE=InnoDB;

CREATE TABLE Usuario (
    idusuario     INT NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(150),
    apellido      VARCHAR(150),
    correo        VARCHAR(150),
    telefono      VARCHAR(50),
    documento     VARCHAR(20),
    fechacreacion DATE,
    clave         VARCHAR(250),
    activo        CHAR(1),
    Rol_idrol     INT NOT NULL,
    Sede_idsede   INT NOT NULL,
    PRIMARY KEY (idusuario)
) ENGINE=InnoDB;

-- ============================================================
-- CLAVES FORÁNEAS
-- ============================================================

ALTER TABLE DetalleEntrada
    ADD CONSTRAINT DetalleEntrada_Entrada_FK FOREIGN KEY (Entrada_identrada)
    REFERENCES Entrada (identrada);

ALTER TABLE DetalleEntrada
    ADD CONSTRAINT DetalleEntrada_Producto_FK FOREIGN KEY (Producto_idproducto)
    REFERENCES Producto (idproducto);

ALTER TABLE DetallePedido
    ADD CONSTRAINT DetallePedido_Pedido_FK FOREIGN KEY (Pedido_idpedido)
    REFERENCES Pedido (idpedido);

ALTER TABLE DetallePedido
    ADD CONSTRAINT DetallePedido_Producto_FK FOREIGN KEY (Producto_idproducto)
    REFERENCES Producto (idproducto);

ALTER TABLE DetalleSalida
    ADD CONSTRAINT DetalleSalida_Producto_FK FOREIGN KEY (Producto_idproducto)
    REFERENCES Producto (idproducto);

ALTER TABLE DetalleSalida
    ADD CONSTRAINT DetalleSalida_Salida_FK FOREIGN KEY (Salida_idsalida)
    REFERENCES Salida (idsalida);

ALTER TABLE Entrada
    ADD CONSTRAINT Entrada_Proveedor_FK FOREIGN KEY (Proveedor_idproveedor)
    REFERENCES Proveedor (idproveedor);

ALTER TABLE Entrada
    ADD CONSTRAINT Entrada_Usuario_FK FOREIGN KEY (Usuario_idusuario)
    REFERENCES Usuario (idusuario);

ALTER TABLE Pedido
    ADD CONSTRAINT Pedido_Estado_FK FOREIGN KEY (Estado_idestado)
    REFERENCES Estado (idestado);

ALTER TABLE Pedido
    ADD CONSTRAINT Pedido_Usuario_FK FOREIGN KEY (Usuario_idusuario)
    REFERENCES Usuario (idusuario);

ALTER TABLE Producto
    ADD CONSTRAINT Producto_Modelo_FK FOREIGN KEY (Modelo_idmodelo)
    REFERENCES Modelo (idmodelo);

ALTER TABLE ProductoSede
    ADD CONSTRAINT ProductoSede_Producto_FK FOREIGN KEY (Producto_idproducto)
    REFERENCES Producto (idproducto);

ALTER TABLE ProductoSede
    ADD CONSTRAINT ProductoSede_Sede_FK FOREIGN KEY (Sede_idsede)
    REFERENCES Sede (idsede);

ALTER TABLE Salida
    ADD CONSTRAINT Salida_Motivo_FK FOREIGN KEY (Motivo_idmotivo)
    REFERENCES Motivo (idmotivo);

ALTER TABLE Salida
    ADD CONSTRAINT Salida_Usuario_FK FOREIGN KEY (Usuario_idusuario)
    REFERENCES Usuario (idusuario);

ALTER TABLE Usuario
    ADD CONSTRAINT Usuario_Rol_FK FOREIGN KEY (Rol_idrol)
    REFERENCES Rol (idrol);

ALTER TABLE Usuario
    ADD CONSTRAINT Usuario_Sede_FK FOREIGN KEY (Sede_idsede)
    REFERENCES Sede (idsede);
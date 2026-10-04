USE `LirioSeda`;

-- =============================================
-- 1. Rol
-- =============================================
INSERT INTO Rol (idrol, nombre, descripcion) VALUES
(1, 'ADMIN',          'Acceso total al sistema'),
(2, 'Almacenero',     'Registra entradas, salidas e incidencias'),
(3, 'Jefe Logistica', 'Autoriza bajas y revisa incidencias'),
(4, 'Vendedor',       'Acceso limitado a ventas'),
(5, 'Invitado',       'Acceso solo lectura');

-- =============================================
-- 2. Estado
-- =============================================
INSERT INTO Estado (idestado, descripcion) VALUES
(1, 'Anulado'),
(2, 'Aprobado'),
(3, 'Rechazado');

-- =============================================
-- 3. Motivo
-- =============================================
INSERT INTO Motivo (idmotivo, descripcion) VALUES
(1, 'Devolución'),
(2, 'Pérdida'),
(3, 'Traslado'),
(4, 'Despacho');

-- =============================================
-- 4. Modelo
-- =============================================
INSERT INTO Modelo (idmodelo, descripcion) VALUES
(1,  'Polera Deportiva'),
(2,  'Polo Básico'),
(3,  'Casaca Denim'),
(4,  'Pantalón Jogger'),
(5,  'Blusa Casual'),
(6,  'Short Deportivo'),
(7,  'Zapatillas Running'),
(8,  'Leggings Yoga'),
(9,  'Casaca Invierno'),
(10, 'Jeans Skinny'),
(11, 'Chaqueta Urbana'),
(12, 'Vestido Casual');

-- =============================================
-- 5. Sede
-- =============================================
INSERT INTO Sede (idsede, descripcion, direccion, telefono) VALUES
(1, 'Sede Central Lima',    'Av. Los Héroes 100 - Lima',  '01-5550001'),
(2, 'Sede Miraflores',      'Av. Larco 500 - Miraflores', '01-5550002'),
(3, 'Sede Surco',           'Av. Primavera 200 - Surco',  '01-5550003'),
(4, 'Sede Área Producción', 'Jr. Industrial 50 - Ate',    '01-5550004'),
(5, 'Sede Trujillo',        'Av. España 800 - Trujillo',  '044-555005');

-- =============================================
-- 6. Proveedor
-- =============================================
INSERT INTO Proveedor (idproveedor, nombre, telefono, correo, direccion, ruc) VALUES
('P001', 'Textiles Andinos',     '987650001', 'contacto@andinos.com', 'Av. Garcilaso 101', 'RUC001'),
('P002', 'Moda Peruana',         '987650002', 'ventas@modaperu.com',  'Av. Grau 102',      'RUC002'),
('P003', 'Confecciones Rivera',  '987650003', 'info@riveraconf.com',  'Jr. Cusco 103',     'RUC003'),
('P004', 'Distribuidora Alpaca', '987650004', 'ventas@alpaca.com',    'Av. Arequipa 104',  'RUC004'),
('P005', 'Fashion Imports',      '987650005', 'contact@fashimp.com',  'Av. Brasil 105',    'RUC005');

-- =============================================
-- 7. Usuario (CORREGIDO: se agrega Sede_idsede NOT NULL)
-- =============================================
INSERT INTO Usuario
    (idusuario, nombre, apellido, correo, telefono, documento, fechacreacion, clave, activo, Rol_idrol, Sede_idsede)
VALUES
(1, 'Alexandra', 'Vilchez Peña',     'avilchez@lirio.com', '987654321', '12345678', '2026-09-24 19:36:54', '$2a$12$c9GQfqf3r6fUD0TIjLZuaeCwlwsgGdQvP4N/rwzxN8SLmwS4YH43.', 1, 1, 1),
(2, 'Joseph',    'Aguirre Barja',    'jaguirre@lirio.com', '987654322', '12345679', '2026-09-24 19:36:54', '$2a$12$c9GQfqf3r6fUD0TIjLZuaeCwlwsgGdQvP4N/rwzxN8SLmwS4YH43.', 1, 2, 1),
(3, 'Maria',     'Querevalu Cherre', 'mquere@lirio.com',   '987654323', '12345670', '2026-09-24 19:36:54', '$2a$12$c9GQfqf3r6fUD0TIjLZuaeCwlwsgGdQvP4N/rwzxN8SLmwS4YH43.', 1, 3, 4),
(4, 'Lucía',     'Torres',           'ltorres@lirio.com',  '987111222', '87654321', '2026-09-24 19:36:54', '$2a$12$c9GQfqf3r6fUD0TIjLZuaeCwlwsgGdQvP4N/rwzxN8SLmwS4YH43.', 1, 4, 2),
(5, 'Carlos',    'Ramirez Lopez',    'cramirez@lirio.com', '987333444', '44556677', '2026-09-24 19:36:54', '$2a$12$c9GQfqf3r6fUD0TIjLZuaeCwlwsgGdQvP4N/rwzxN8SLmwS4YH43.', 1, 2, 3);

-- =============================================
-- 8. Producto (CORREGIDO: PR12 con nombre propio)
-- =============================================
INSERT INTO Producto (idproducto, nombre, precio, estado, Modelo_idmodelo) VALUES
('PR01', 'Polera Deportiva Hombre', 89.90,  1, 1),
('PR02', 'Polo Básico Algodón',     29.90,  1, 2),
('PR03', 'Casaca Denim',            159.00, 1, 3),
('PR04', 'Pantalón Jogger',         99.00,  1, 4),
('PR05', 'Blusa Casual Mujer',      59.90,  1, 5),
('PR06', 'Short Deportivo',         49.90,  1, 6),
('PR07', 'Zapatillas Running',      299.00, 1, 7),
('PR08', 'Leggings Yoga',           79.90,  1, 8),
('PR09', 'Casaca Invierno Mujer',   189.00, 1, 9),
('PR10', 'Jeans Skinny Mujer',      139.00, 1, 10),
('PR12', 'Polera Deportiva Mujer',  89.90,  1, 1);

-- =============================================
-- 9. Entrada (CORREGIDO: idsedeusuario llenado)
-- =============================================
INSERT INTO Entrada (identrada, fechaentrada, importe_total, idsedeusuario, Proveedor_idproveedor, Usuario_idusuario) VALUES
('E00001', '2026-01-05 09:00:00', 8990.00, 1, 'P001', 2),
('E00002', '2026-01-12 10:30:00', 5980.00, 1, 'P002', 2),
('E00003', '2026-02-03 08:45:00', 7950.00, 4, 'P003', 5),
('E00004', '2026-02-18 14:20:00', 5940.00, 1, 'P004', 2);

-- =============================================
-- 10. Salida (CORREGIDO: idsedeusuario llenado)
-- =============================================
INSERT INTO Salida (idsalida, fechasalida, idsedeusuario, Motivo_idmotivo, Usuario_idusuario) VALUES
('S00001', '2026-01-20 11:00:00', 1, 3, 2),
('S00002', '2026-02-05 15:30:00', 3, 3, 5),
('S00003', '2026-03-02 10:15:00', 1, 4, 2);

-- =============================================
-- 11. Pedido (CORREGIDO: idsedeusuario llenado)
-- =============================================
INSERT INTO Pedido (idpedido, fechapedido, fechaaprobacion, idsedeusuario, Estado_idestado, Usuario_idusuario) VALUES
('PD0001', '2026-01-08 09:00:00', '2026-01-09 09:00:00', 1, 2, 1),
('PD0002', '2026-02-10 10:00:00', '2026-02-11 10:00:00', 4, 2, 3),
('PD0003', '2026-03-01 11:30:00', '2026-03-02 11:30:00', 1, 2, 1);

-- =============================================
-- 12. DetalleEntrada
-- =============================================
INSERT INTO DetalleEntrada (iddetalleentrada, cantidad, preciounitario, Entrada_identrada, Producto_idproducto) VALUES
(1, 50,  89.90,  'E00001', 'PR01'),
(2, 50,  49.90,  'E00001', 'PR06'),
(3, 20,  89.90,  'E00001', 'PR01'),
(4, 100, 29.90,  'E00002', 'PR02'),
(5, 10,  299.00, 'E00002', 'PR07'),
(6, 30,  159.00, 'E00003', 'PR03'),
(7, 40,  79.50,  'E00003', 'PR08'),
(8, 30,  99.00,  'E00004', 'PR04'),
(9, 15,  198.00, 'E00004', 'PR09');

-- =============================================
-- 13. DetallePedido
-- =============================================
INSERT INTO DetallePedido (iddetallepedido, cantidad, Pedido_idpedido, Producto_idproducto) VALUES
(1, 50,  'PD0001', 'PR01'),
(2, 100, 'PD0002', 'PR02'),
(3, 10,  'PD0002', 'PR07'),
(4, 20,  'PD0003', 'PR10');

-- =============================================
-- 14. DetalleSalida (CORREGIDO: idproducto e idsalida llenados)
-- =============================================
INSERT INTO DetalleSalida (iddetallesalida, cantidad, Producto_idproducto, Salida_idsalida, idproducto, idsalida) VALUES
(1, 20, 'PR01', 'S00001', 'PR01', 'S00001'),
(2, 20, 'PR02', 'S00002', 'PR02', 'S00002'),
(3, 8,  'PR06', 'S00002', 'PR06', 'S00002'),
(4, 10, 'PR03', 'S00003', 'PR03', 'S00003');

-- =============================================
-- 15. ProductoSede
-- =============================================
INSERT INTO ProductoSede (stock, Sede_idsede, Producto_idproducto) VALUES
(100, 1, 'PR01'), (30, 2, 'PR01'), (20, 3, 'PR01'),
(180, 1, 'PR02'), (40, 2, 'PR02'), (30, 3, 'PR02'),
(60,  1, 'PR03'), (10, 2, 'PR03'), (10, 3, 'PR03'),
(100, 1, 'PR04'), (30, 2, 'PR04'), (20, 3, 'PR04'),
(50,  1, 'PR05'), (15, 2, 'PR05'), (5,  3, 'PR05'),
(80,  1, 'PR06'), (20, 2, 'PR06'), (10, 3, 'PR06'),
(40,  1, 'PR07'), (10, 2, 'PR07'), (10, 3, 'PR07'),
(100, 1, 'PR08'), (25, 2, 'PR08'), (15, 3, 'PR08'),
(35,  1, 'PR09'), (12, 2, 'PR09'), (8,  3, 'PR09'),
(60,  1, 'PR10'), (20, 2, 'PR10'), (10, 3, 'PR10');
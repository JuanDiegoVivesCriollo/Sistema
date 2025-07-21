-- Script SQL para insertar datos de ejemplo en TechStore

-- Insertar categorías
INSERT INTO categorias (nombre_categoria) VALUES 
('Laptops'),
('Smartphones'),
('Accesorios'),
('Audio'),
('Componentes');

-- Insertar productos
INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, imagen_url) VALUES
('Laptop Gaming ASUS ROG', 'Laptop para gaming con RTX 4060, 16GB RAM, SSD 512GB', 2499.99, 15, 1, 'laptop_asus.jpg'),
('iPhone 15 Pro', 'Smartphone Apple con chip A17 Pro, 256GB', 1299.99, 25, 2, 'iphone15.jpg'),
('Monitor 4K LG', 'Monitor 27" 4K UHD con HDR', 399.99, 20, 3, 'monitor_lg.jpg'),
('Teclado Mecánico Corsair', 'Teclado gaming mecánico RGB', 149.99, 30, 3, 'teclado_corsair.jpg'),
('Mouse Logitech G Pro', 'Mouse gaming profesional 25600 DPI', 89.99, 40, 3, 'mouse_logitech.jpg'),
('Auriculares Sony WH-1000XM5', 'Auriculares inalámbricos con cancelación de ruido', 299.99, 18, 4, 'auriculares_sony.jpg'),
('SSD Samsung 1TB', 'Disco sólido NVMe M.2 de alta velocidad', 129.99, 35, 5, 'ssd_samsung.jpg'),
('Webcam Logitech C920', 'Cámara web HD 1080p para streaming', 79.99, 22, 3, 'webcam_logitech.jpg'),
('MacBook Pro M3', 'Laptop Apple con chip M3, 16GB RAM, 512GB SSD', 2299.99, 12, 1, 'macbook_pro.jpg'),
('Samsung Galaxy S24', 'Smartphone Android con 256GB y cámara de 200MP', 899.99, 28, 2, 'galaxy_s24.jpg');

-- Insertar usuario administrador
INSERT INTO usuarios (email, password, rol) VALUES 
('admin@techstore.com', 'admin123', 'admin');

-- Insertar usuarios clientes de ejemplo
INSERT INTO usuarios (email, password, rol) VALUES 
('juan.perez@email.com', '123456', 'cliente'),
('maria.garcia@email.com', '123456', 'cliente'),
('carlos.lopez@email.com', '123456', 'cliente'),
('ana.martinez@email.com', '123456', 'cliente');

-- Insertar clientes
INSERT INTO clientes (id_usuario, nombre, direccion, telefono) VALUES
(2, 'Juan Pérez', 'Av. Principal 123, Lima', '987654321'),
(3, 'María García', 'Jr. Comercio 456, Arequipa', '976543210'),
(4, 'Carlos López', 'Av. Los Olivos 789, Trujillo', '965432109'),
(5, 'Ana Martínez', 'Calle Real 321, Cusco', '954321098');

-- Insertar descuentos
INSERT INTO descuentos (nombre, tipo_descuento, valor, descripcion) VALUES
('Descuento Black Friday', 'porcentaje', 20.0, 'Descuento del 20% por Black Friday'),
('Descuento Estudiante', 'porcentaje', 15.0, 'Descuento del 15% para estudiantes'),
('Descuento Primera Compra', 'fijo', 50.0, 'Descuento de $50 en tu primera compra'),
('Descuento VIP', 'porcentaje', 25.0, 'Descuento del 25% para clientes VIP'),
('Descuento Verano', 'fijo', 30.0, 'Descuento de $30 en compras de verano');

-- Insertar algunos pedidos de ejemplo
INSERT INTO pedidos (id_cliente, fecha_pedido, estado, metodo_pago, metodo_envio, total) VALUES
(1, NOW(), 'pendiente', 'Tarjeta de Crédito', 'Envío Express', 2649.98),
(2, NOW() - INTERVAL 1 DAY, 'procesando', 'PayPal', 'Envío Estándar', 1389.98),
(3, NOW() - INTERVAL 2 DAY, 'enviado', 'Tarjeta de Débito', 'Recojo en Tienda', 489.98),
(4, NOW() - INTERVAL 3 DAY, 'entregado', 'Transferencia Bancaria', 'Envío Express', 219.98);

-- Insertar detalles de pedidos
INSERT INTO detalles_pedido (id_pedido, id_producto, cantidad, precio) VALUES
(1, 1, 1, 2499.99),
(1, 4, 1, 149.99),
(2, 2, 1, 1299.99),
(2, 5, 1, 89.99),
(3, 3, 1, 399.99),
(3, 5, 1, 89.99),
(4, 4, 1, 149.99),
(4, 8, 1, 79.99);

-- Insertar notificaciones de ejemplo
INSERT INTO notificaciones (id_pedido, mensaje) VALUES
(1, 'Su pedido ha sido recibido y está siendo procesado'),
(2, 'Su pedido está en proceso de preparación'),
(3, 'Su pedido ha sido enviado y llegará en 2-3 días hábiles'),
(4, 'Su pedido ha sido entregado exitosamente');

COMMIT;

# Sistema de E-commerce TechStore

## Descripción del Proyecto

Sistema de comercio electrónico desarrollado en Java con JavaFX que implementa los principios SOLID y múltiples patrones de diseño. El sistema permite la gestión completa de productos, clientes, pedidos y descuentos, con roles diferenciados para administradores y clientes.

## Arquitectura y Patrones de Diseño Implementados

### 1. Arquitectura MVC (Model-View-Controller)
- **Model**: Modelos de datos (User, Product, Order, Customer, etc.)
- **View**: Interfaces JavaFX (MainApplicationFX, vistas específicas)
- **Controller**: Controladores de lógica de negocio (MainController, OrderService)

### 2. Patrones de Diseño Implementados

#### **Patrón Strategy**
- **Ubicación**: `com.sistema.util.strategy`
- **Implementación**: Estrategias de descuento intercambiables
- **Clases**:
  - `DiscountStrategy` (interfaz)
  - `PercentageDiscountStrategy` (descuento por porcentaje)
  - `FixedDiscountStrategy` (descuento fijo)

#### **Patrón Factory**
- **Ubicación**: `com.sistema.util.factory`
- **Implementación**: Factory para crear estrategias de descuento
- **Clase**: `DiscountStrategyFactory`

#### **Patrón Observer**
- **Ubicación**: `com.sistema.util.observer`
- **Implementación**: Sistema de notificaciones por email
- **Clases**:
  - `OrderObserver` (interfaz)
  - `EmailNotificationObserver` (observador concreto)
  - Integrado en el modelo `Order`

#### **Patrón Singleton**
- **Ubicación**: `com.sistema.service`
- **Implementación**: Servicio de carrito de compras
- **Clase**: `CartService`

#### **Patrón DAO (Data Access Object)**
- **Ubicación**: `com.sistema.model.dao`
- **Implementación**: Acceso a datos desacoplado
- **Interfaces y implementaciones para todas las entidades**

## Requisitos Funcionales Implementados

### ✅ Funcionalidades Completas

1. **Registro, modificación y eliminación de productos** (Solo administrador)
2. **Visualización de catálogo por parte del cliente**
3. **Creación de pedidos con carrito de compras**
4. **Selección del tipo de pago y método de envío**
5. **Cálculo de descuentos utilizando estrategias intercambiables**
6. **Gestión del estado de pedidos por parte del administrador**
7. **Envío de notificaciones al cliente por email/simulación**

### Funcionalidades Adicionales
- Sistema de autenticación por roles
- Registro de nuevos clientes
- Gestión de descuentos
- Persistencia en base de datos MySQL
- Interfaz gráfica moderna con JavaFX

## Estructura de la Base de Datos

```sql
-- Tablas principales
usuarios (id_usuario, email, password, rol)
clientes (id_cliente, id_usuario, nombre, direccion, telefono)
categorias (id_categoria, nombre_categoria)
productos (id_producto, nombre, descripcion, precio, stock, categoria_id, imagen_url)
pedidos (id_pedido, id_cliente, fecha_pedido, estado, metodo_pago, metodo_envio, total)
detalles_pedido (id_detalle, id_pedido, id_producto, cantidad, precio)
descuentos (id_descuento, nombre, tipo_descuento, valor, descripcion)
notificaciones (id_notificacion, id_pedido, mensaje, fecha_notificacion)
```

## Configuración del Proyecto

### Requisitos
- Java 11 o superior
- JavaFX SDK
- MySQL Server
- Maven

### Configuración de Base de Datos
1. Crear base de datos `TechStore` en MySQL
2. Ejecutar los scripts SQL de creación de tablas
3. Ejecutar `datos_ejemplo.sql` para datos de prueba
4. Configurar conexión en `DBConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/TechStore";
   private static final String USER = "root";
   private static final String PASSWORD = "tu_password";
   ```

### Ejecución
```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn javafx:run
```

## Usuarios de Prueba

### Administrador
- **Email**: admin@techstore.com
- **Password**: admin123

### Clientes de Ejemplo
- **Email**: juan.perez@email.com | **Password**: 123456
- **Email**: maria.garcia@email.com | **Password**: 123456

## Estructura del Proyecto

```
src/main/java/com/sistema/
├── MainFX.java                 # Clase principal
├── controller/                 # Controladores
│   └── MainController.java
├── model/                      # Modelos de datos
│   ├── User.java
│   ├── Product.java
│   ├── Order.java
│   ├── Customer.java
│   ├── Discount.java
│   └── dao/                    # Data Access Objects
├── service/                    # Servicios de negocio
│   ├── OrderService.java
│   └── CartService.java
├── util/                       # Utilidades
│   ├── DBConnection.java
│   ├── EmailService.java
│   ├── DataInitializer.java
│   ├── strategy/               # Patrón Strategy
│   ├── factory/                # Patrón Factory
│   └── observer/               # Patrón Observer
└── view/                       # Interfaces JavaFX
    └── MainApplicationFX.java
```

## Principios SOLID Aplicados

### **S - Single Responsibility Principle**
- Cada clase tiene una responsabilidad específica
- DAOs solo se encargan del acceso a datos
- Services manejan lógica de negocio
- Views solo manejan la interfaz

### **O - Open/Closed Principle**
- Sistema abierto para extensión (nuevas estrategias de descuento)
- Cerrado para modificación (interfaces estables)

### **L - Liskov Substitution Principle**
- Las implementaciones de DAO son intercambiables
- Las estrategias de descuento son sustituibles

### **I - Interface Segregation Principle**
- Interfaces específicas para cada tipo de operación
- Clientes no dependen de métodos que no usan

### **D - Dependency Inversion Principle**
- Dependencia de abstracciones, no de implementaciones concretas
- Inyección de dependencias en servicios

## Características Técnicas

- **Persistencia**: MySQL con JDBC
- **Interfaz**: JavaFX con controles modernos
- **Arquitectura**: MVC con separación clara de responsabilidades
- **Patrones**: Strategy, Factory, Observer, Singleton, DAO
- **Manejo de errores**: Try-catch con logging apropiado
- **Concurrencia**: Uso de ConcurrentHashMap en CartService

## Demo y Funcionalidades

### Para Administradores:
- Gestión completa de productos (CRUD)
- Visualización y gestión de todos los pedidos
- Cambio de estados de pedidos
- Creación de descuentos
- Dashboard completo

### Para Clientes:
- Navegación del catálogo de productos
- Gestión del carrito de compras
- Proceso de checkout completo
- Visualización de pedidos propios
- Aplicación de descuentos

El sistema está completamente funcional e implementa todos los requisitos solicitados con las mejores prácticas de desarrollo de software.

# Sistema de Gestión de Comercio Electrónico

## 📋 Descripción
Sistema de e-commerce desarrollado en Java con JavaFX que permite gestionar productos, clientes, pedidos y descuentos.

## 🛠️ Tecnologías
- **Java 17**
- **JavaFX** - Interfaz gráfica
- **Maven** - Gestión de dependencias
- **MySQL** - Base de datos
- **JDBC** - Conexión a base de datos

## 🏗️ Arquitectura
- **Patrón MVC** (Model-View-Controller)
- **Patrón DAO** (Data Access Object)
- **Patrón Strategy** (Sistema de descuentos)

## ⚙️ Funcionalidades
- Sistema de autenticación (Admin/Cliente)
- Gestión de productos (CRUD)
- Carrito de compras
- Gestión de pedidos
- Sistema de descuentos
- Notificaciones

## 🚀 Cómo ejecutar
```bash
# Compilar
mvn compile

# Ejecutar
mvn javafx:run
```

## 📁 Estructura del Proyecto
```
src/main/java/com/sistema/
├── model/          # Entidades y modelos
├── dao/            # Acceso a datos
├── controller/     # Controladores
├── view/           # Interfaces gráficas
└── util/           # Utilidades
```

## 🗄️ Base de Datos
- MySQL con tablas: usuarios, clientes, productos, pedidos, descuentos, notificaciones

## 👥 Autor
TodCodePer

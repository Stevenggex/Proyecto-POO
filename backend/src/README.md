Backend - Tienda de Videojuegos (CRUD JavaFX)
Aplicación de escritorio desarrollada en Java con JavaFX. Permite registrar, editar, eliminar y consultar videojuegos (nombre, precio, cantidad, imagen y descripción), almacenando todo en una base de datos PostgreSQL.

Tecnologías
Java
JavaFX
PostgreSQL (vía JDBC)
IntelliJ IDEA
Estructura del código (pendiente de desarrollar)
modelo/ → clases de dominio (ej. Videojuego)
dao/ → acceso a datos y conexión con PostgreSQL
controlador/ → controladores de las vistas JavaFX
vista/ → archivos FXML
db/ → scripts de base de datos
Configuración de la base de datos
Instalar PostgreSQL localmente (o usar un servicio en la nube).
Crear la base de datos y la tabla videojuego.
Configurar la URL de conexión JDBC en el proyecto (host, puerto, usuario, contraseña).
Exportación al frontend
Cada operación de creación/edición exportará automáticamente:

Los datos a ../frontend/data/
Las imágenes a ../frontend/img/
Generar el ejecutable (.exe)
Se utilizará jpackage para empaquetar la aplicación junto con el driver JDBC de PostgreSQL.
-- Crear la base de datos
CREATE DATABASE tienda_videojuegos;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('admin', 'cliente', 'vendedor'))
);

-- Tabla de productos (videojuegos)
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    imagen VARCHAR(200), -- ruta de la imagen exportada al frontend
    fecha_lanzamiento DATE
);

-- Relación opcional: quién registró el producto
ALTER TABLE productos
ADD COLUMN usuario_id INT REFERENCES usuarios(id) ON DELETE SET NULL;

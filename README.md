# Proyecto POO - Tienda de Videojuegos

Proyecto final de la materia de Programación Orientada a Objetos (POO).

Sistema CRUD de una tienda de videojuegos, compuesto por dos partes dentro de este monorepo:

## Estructura

- `frontend/` → Página web estática que muestra (solo lectura) los videojuegos registrados desde la app de escritorio.
- `backend/` → Aplicación de escritorio en JavaFX donde se realiza el CRUD completo (crear, leer, actualizar, eliminar) de los videojuegos, conectada a una base de datos PostgreSQL.

## Tecnologías

| Parte     | Herramientas |
|-----------|--------------|
| Frontend  | VS Code, HTML, CSS, JavaScript |
| Backend   | IntelliJ IDEA, Java, JavaFX, PostgreSQL (JDBC) |

## Cómo se comunican frontend y backend

El backend (JavaFX) exporta los datos de los videojuegos (y sus imágenes) hacia la carpeta `frontend/data/` y `frontend/img/`. El frontend solo lee esos archivos y los muestra; no tiene lógica de negocio ni conexión directa a la base de datos.

## Cómo ejecutar cada parte

Ver instrucciones específicas en:
- [frontend/README.md](./frontend/README.md)
- [backend/README.md](./backend/README.md)

## Autor

Tu nombre aquí

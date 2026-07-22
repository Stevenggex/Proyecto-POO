# Proyecto POO - GAME-VAULT: Tienda de Videojuegos

Proyecto final de la materia de Programación Orientada a Objetos (POO).

## Integrantes

- Steven Gallo
- Nicolay Barreno

## Estructura del proyecto

- `backend/` — Aplicación de escritorio en JavaFX con Maven que gestiona el CRUD completo de videojuegos, autenticación de usuarios con roles, y registro de compras. Conectada a Firebase Firestore.
- `diseño-poo/` — Página web en React + Vite + TailwindCSS que muestra el catálogo de videojuegos (solo lectura) y permite registro/inicio de sesión.
- `database/` — Script SQL de la base de datos PostgreSQL (modelo relacional).

## Tecnologías

| Parte     | Herramientas |
|-----------|--------------|
| Backend   | Java, JavaFX, Maven, Firebase Firestore |
| Frontend  | React, TypeScript, Vite, TailwindCSS, Firebase |
| Base de datos | Firebase Firestore (NoSQL) + PostgreSQL (modelo relacional) |

## Roles del sistema

- **ADMINISTRADOR** — Gestiona el inventario, edita y elimina productos
- **EMPLEADO** — Gestiona el inventario, registra y actualiza productos
- **USUARIO** — Navega el catálogo, busca juegos y registra compras

## Funcionalidades principales

- Inicio de sesión con autenticación por roles
- Dashboard con CRUD de videojuegos (crear, leer, actualizar, eliminar)
- Búsqueda y filtrado de productos en tiempo real
- Compra de videojuegos con generación de factura
- Catálogo web con filtros por género, plataforma y ordenamiento

## Cómo ejecutar

### Backend (escritorio)

```bash
cd backend
mvn clean javafx:run
```

### Frontend (web)

```bash
cd diseño-poo
pnpm install
pnpm dev
```

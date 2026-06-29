package dao;

import modelo.Videojuego;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDAO implements CrudOperaciones<Videojuego>{
    @Override
    public void crear(Videojuego videojuego) {
        // Aquí irá el INSERT INTO videojuego ... con JDBC
    }

    @Override
    public Videojuego buscarPorId(int id) {
        // Aquí irá el SELECT ... WHERE id = ?
        return null;
    }

    @Override
    public List<Videojuego> listarTodos() {
        // Aquí irá el SELECT * FROM videojuego
        return new ArrayList<>();
    }

    @Override
    public void actualizar(Videojuego videojuego) {
        // Aquí irá el UPDATE videojuego SET ... WHERE id = ?
    }

    @Override
    public void eliminar(int id) {
        // Aquí irá el DELETE FROM videojuego WHERE id = ?
    }

}

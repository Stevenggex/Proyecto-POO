package dao;

import java.util.List;

public interface CrudOperaciones <T>{
    void crear(T objeto);
    T buscarPorId(int id);
    List<T> listarTodos();
    void actualizar(T objeto);
    void eliminar(int id);
}

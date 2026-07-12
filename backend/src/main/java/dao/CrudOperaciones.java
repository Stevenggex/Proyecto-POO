package dao;

import java.util.List;

public interface CrudOperaciones <T>{
    void crear(T objeto);
    T buscarPorId(String id);
    List<T> listarTodos();
    void actualizar(T objeto);
    void eliminar(String id);
}

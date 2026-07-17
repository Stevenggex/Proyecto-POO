package dao;

import com.google.cloud.firestore.*;
import modelo.FirebaseConfig;
import modelo.Rol;
import modelo.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioDAO implements CrudOperaciones<Usuario> {

    private static final String COLECCION = "Usuarios";

    private Firestore getDb() {
        return FirebaseConfig.getDb();
    }

    @Override
    public void crear(Usuario usuario) {
        DocumentReference docRef = getDb().collection(COLECCION).document();
        docRef.set(usuarioAMap(usuario));
    }

    @Override
    public Usuario buscarPorId(String id) {
        try {
            DocumentSnapshot doc = getDb().collection(COLECCION).document(id).get().get();
            if (doc.exists()) {
                return documentoAUsuario(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        try {
            QuerySnapshot docs = getDb().collection(COLECCION).get().get();
            for (DocumentSnapshot doc : docs.getDocuments()) {
                lista.add(documentoAUsuario(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void actualizar(Usuario usuario) {
        if (usuario.getId() <= 0) return;
        getDb().collection(COLECCION).document(String.valueOf(usuario.getId())).set(usuarioAMap(usuario));
    }

    @Override
    public void eliminar(String id) {
        getDb().collection(COLECCION).document(id).delete();
    }

    public Rol autenticar(String nombreUsuario, String password) {
        try {
            QuerySnapshot query = getDb().collection(COLECCION)
                    .whereEqualTo("usuario", nombreUsuario)
                    .whereEqualTo("password", password)
                    .get()
                    .get();

            if (query.isEmpty()) {
                return null;
            }

            String rol = query.getDocuments().get(0).getString("rol");
            return switch (rol) {
                case "Administrador" -> Rol.ADMINISTRADOR;
                case "Empleado" -> Rol.EMPLEADO;
                case "Usuario" -> Rol.USUARIO;
                default -> null;
            };
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> usuarioAMap(Usuario u) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", u.getNombre() != null ? u.getNombre() : "");
        map.put("usuario", u.getNombreUsuario() != null ? u.getNombreUsuario() : "");
        map.put("password", u.getContrasena() != null ? u.getContrasena() : "");
        map.put("rol", u.getRol() != null ? u.getRol().toString() : "Usuario");
        return map;
    }

    private Usuario documentoAUsuario(DocumentSnapshot doc) {
        String nombre = getString(doc, "nombre");
        String usuario = getString(doc, "usuario");
        String password = getString(doc, "password");
        String rolStr = getString(doc, "rol");
        Rol rol = switch (rolStr) {
            case "ADMINISTRADOR", "Administrador" -> Rol.ADMINISTRADOR;
            case "EMPLEADO", "Empleado" -> Rol.EMPLEADO;
            default -> Rol.USUARIO;
        };
        Usuario u = new Usuario(nombre, usuario, password, rol);
        u.setId(Integer.parseInt(doc.getId()));
        return u;
    }

    private String getString(DocumentSnapshot doc, String field) {
        Object val = doc.get(field);
        return val != null ? val.toString() : "";
    }
}

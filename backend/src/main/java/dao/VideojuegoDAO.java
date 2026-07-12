package dao;

import com.google.cloud.firestore.*;
import modelo.FirebaseConfig;
import modelo.Videojuego;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideojuegoDAO implements CrudOperaciones<Videojuego> {

    private static final String COLECCION = "videojuegos";

    private Firestore getDb() {
        return FirebaseConfig.getDb();
    }

    @Override
    public void crear(Videojuego videojuego) {
        DocumentReference docRef = getDb().collection(COLECCION).document();
        videojuego.setId(docRef.getId());
        docRef.set(videojuegoAmap(videojuego));
    }

    @Override
    public Videojuego buscarPorId(String id) {
        try {
            DocumentSnapshot doc = getDb().collection(COLECCION).document(id).get().get();
            if (doc.exists()) {
                return documentoAVideojuego(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Videojuego> listarTodos() {
        List<Videojuego> lista = new ArrayList<>();
        try {
            QuerySnapshot docs = getDb().collection(COLECCION).get().get();
            for (DocumentSnapshot doc : docs.getDocuments()) {
                lista.add(documentoAVideojuego(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void actualizar(Videojuego videojuego) {
        if (videojuego.getId() == null) return;
        getDb().collection(COLECCION).document(videojuego.getId()).set(videojuegoAmap(videojuego));
    }

    @Override
    public void eliminar(String id) {
        getDb().collection(COLECCION).document(id).delete();
    }

    private Map<String, Object> videojuegoAmap(Videojuego v) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", v.getId() != null ? v.getId() : "");
        map.put("nombre", v.getNombre() != null ? v.getNombre() : "");
        map.put("desarrollador", v.getDesarrollador() != null ? v.getDesarrollador() : "");
        map.put("genero", v.getGenero() != null ? v.getGenero() : "");
        map.put("plataformas", v.getPlataformas() != null ? v.getPlataformas() : "");
        map.put("precio", v.getPrecio());
        map.put("cantidad", v.getCantidad());
        map.put("stock", v.isStock());
        map.put("descripcion", v.getDescripcion() != null ? v.getDescripcion() : "");
        map.put("imagenPath", v.getImagenPath() != null ? v.getImagenPath() : "");
        map.put("fechaLanzamiento", v.getFechaLanzamiento() != null ? v.getFechaLanzamiento() : "");
        map.put("puntuacion", v.getPuntuacion());
        map.put("votos", v.getVotos());
        map.put("destacado", v.isDestacado());
        return map;
    }

    private Videojuego documentoAVideojuego(DocumentSnapshot doc) {
        Videojuego v = new Videojuego();
        v.setId(doc.getId());
        v.setNombre(getString(doc, "nombre"));
        v.setDesarrollador(getString(doc, "desarrollador"));
        v.setGenero(getString(doc, "genero"));
        v.setPlataformas(getString(doc, "plataformas"));
        v.setPrecio(getDouble(doc, "precio"));
        v.setCantidad(getInt(doc, "cantidad"));
        v.setStock(getBoolean(doc, "stock"));
        v.setDescripcion(getString(doc, "descripcion"));
        v.setImagenPath(getString(doc, "imagenPath"));
        v.setFechaLanzamiento(getString(doc, "fechaLanzamiento"));
        v.setPuntuacion(getDouble(doc, "puntuacion"));
        v.setVotos(getInt(doc, "votos"));
        v.setDestacado(getBoolean(doc, "destacado"));
        return v;
    }

    private String getString(DocumentSnapshot doc, String field) {
        Object val = doc.get(field);
        return val != null ? val.toString() : "";
    }

    private double getDouble(DocumentSnapshot doc, String field) {
        Object val = doc.get(field);
        if (val instanceof Number) return ((Number) val).doubleValue();
        return 0.0;
    }

    private int getInt(DocumentSnapshot doc, String field) {
        Object val = doc.get(field);
        if (val instanceof Number) return ((Number) val).intValue();
        return 0;
    }

    private boolean getBoolean(DocumentSnapshot doc, String field) {
        Object val = doc.get(field);
        if (val instanceof Boolean) return (Boolean) val;
        return false;
    }
}

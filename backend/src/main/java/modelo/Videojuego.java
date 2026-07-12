package modelo;

public class Videojuego {
    private String id;
    private String nombre;
    private String desarrollador;
    private String genero;
    private String plataformas;
    private double precio;
    private int cantidad;
    private boolean stock;
    private String descripcion;
    private String imagenPath;
    private String fechaLanzamiento;
    private double puntuacion;
    private int votos;
    private boolean destacado;

    public Videojuego() {}

    public Videojuego(String nombre, String desarrollador, String genero, String plataformas,
                      double precio, int cantidad, String descripcion, String imagenPath,
                      String fechaLanzamiento, double puntuacion, int votos, boolean destacado) {
        this.nombre = nombre;
        this.desarrollador = desarrollador;
        this.genero = genero;
        this.plataformas = plataformas;
        this.precio = precio;
        this.cantidad = cantidad;
        this.stock = cantidad > 0;
        this.descripcion = descripcion;
        this.imagenPath = imagenPath;
        this.fechaLanzamiento = fechaLanzamiento;
        this.puntuacion = puntuacion;
        this.votos = votos;
        this.destacado = destacado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDesarrollador() { return desarrollador; }
    public void setDesarrollador(String desarrollador) { this.desarrollador = desarrollador; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getPlataformas() { return plataformas; }
    public void setPlataformas(String plataformas) { this.plataformas = plataformas; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.stock = cantidad > 0;
    }
    public boolean isStock() { return stock; }
    public void setStock(boolean stock) { this.stock = stock; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenPath() { return imagenPath; }
    public void setImagenPath(String imagenPath) { this.imagenPath = imagenPath; }
    public String getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(String fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }
    public double getPuntuacion() { return puntuacion; }
    public void setPuntuacion(double puntuacion) { this.puntuacion = puntuacion; }
    public int getVotos() { return votos; }
    public void setVotos(int votos) { this.votos = votos; }
    public boolean isDestacado() { return destacado; }
    public void setDestacado(boolean destacado) { this.destacado = destacado; }
}

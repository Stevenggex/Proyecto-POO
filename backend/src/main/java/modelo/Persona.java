package modelo;

public abstract class Persona {
    private int id;
    private String nombre;
    private String correo;

    public Persona(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    // Método abstracto: cada clase hija lo implementa a su manera (polimorfismo)
    public abstract String obtenerDescripcion();

    // Getters y setters (encapsulamiento: atributos privados, acceso solo por aquí)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}

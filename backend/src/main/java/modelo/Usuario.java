package modelo;

public class Usuario extends Persona{
    private String contrasena;
    private Rol rol;

    public Usuario(String nombre, String correo, String contrasena, Rol rol) {
        super(nombre, correo);
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Polimorfismo: sobreescribe el método abstracto del padre
    @Override
    public String obtenerDescripcion() {
        return getNombre() + " - Rol: " + rol;
    }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}

package modelo;

public class Usuario extends Persona{
    private String nombreUsuario;
    private String contrasena;
    private Rol rol;

    public Usuario(String nombre, String nombreUsuario, String contrasena, Rol rol) {
        super(nombre);
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    @Override
    public String obtenerDescripcion() {
        return getNombre() + " - Rol: " + rol;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}

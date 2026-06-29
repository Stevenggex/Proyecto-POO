package modelo;

import  java.time.LocalDateTime;

public class Venta {
    private int id;
    private int idUsuario;
    private int idVideojuego;
    private int cantidad;
    private double total;
    private LocalDateTime fecha;

    public Venta(int idUsuario, int idVideojuego, int cantidad, double total) {
        this.idUsuario = idUsuario;
        this.idVideojuego = idVideojuego;
        this.cantidad = cantidad;
        this.total = total;
        this.fecha = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}

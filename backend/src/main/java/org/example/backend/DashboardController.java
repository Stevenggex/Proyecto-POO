package org.example.backend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import modelo.Videojuego;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DashboardController {

    @FXML private TextField busquedaField;
    @FXML private TextField nombreField;
    @FXML private TextField precioField;
    @FXML private TextField stockField;
    @FXML private ComboBox<String> categoriaCombo;
    @FXML private TextArea descripcionArea;
    @FXML private Button registrarBtn;
    @FXML private GridPane inventoryGrid;

    @FXML private Button dashboardBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button addProductBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button searchBtn;
    @FXML private Button logoutBtn;

    private final List<Videojuego> videojuegos = new ArrayList<>();
    private int editandoIndex = -1;

    @FXML
    private void initialize() {
        categoriaCombo.getItems().addAll("Action / RPG", "Strategy", "Simulator", "Horror", "Indie");

        nombreField.textProperty().addListener((obs, old, val) -> limpiarEstiloError(nombreField));
        precioField.textProperty().addListener((obs, old, val) -> limpiarEstiloError(precioField));
        stockField.textProperty().addListener((obs, old, val) -> limpiarEstiloError(stockField));
    }

    @FXML
    private void handleRegistrar() {
        if (editandoIndex >= 0) {
            actualizarProducto();
            return;
        }

        String nombre = nombreField.getText().trim();
        String precioText = precioField.getText().trim();
        String stockText = stockField.getText().trim();
        String descripcion = descripcionArea.getText().trim();

        boolean valido = true;

        if (nombre.isEmpty()) { marcarError(nombreField); valido = false; }
        if (precioText.isEmpty()) { marcarError(precioField); valido = false; }
        if (stockText.isEmpty()) { marcarError(stockField); valido = false; }
        if (!valido) return;

        double precio;
        int stock;
        try { precio = Double.parseDouble(precioText); } catch (NumberFormatException e) { marcarError(precioField); return; }
        try { stock = Integer.parseInt(stockText); } catch (NumberFormatException e) { marcarError(stockField); return; }

        Videojuego juego = new Videojuego(nombre, precio, stock, descripcion, "");
        juego.setId(videojuegos.size() + 1);
        videojuegos.add(juego);

        limpiarFormulario();
        System.out.println("Producto registrado: " + juego.getNombre() + " (ID: " + juego.getId() + ")");
    }

    private void actualizarProducto() {
        Videojuego juego = videojuegos.get(editandoIndex);
        juego.setNombre(nombreField.getText().trim());
        juego.setPrecio(Double.parseDouble(precioField.getText().trim()));
        juego.setCantidad(Integer.parseInt(stockField.getText().trim()));
        juego.setDescripcion(descripcionArea.getText().trim());

        editandoIndex = -1;
        limpiarFormulario();
        registrarBtn.setText("Registrar Producto");
        System.out.println("Producto actualizado: " + juego.getNombre());
    }

    @FXML
    private void handleEditar() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Editar Producto");
        dialog.setHeaderText("Ingrese el ID del producto a editar:");
        dialog.setContentText("ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr.trim());
                for (int i = 0; i < videojuegos.size(); i++) {
                    if (videojuegos.get(i).getId() == id) {
                        editandoIndex = i;
                        Videojuego j = videojuegos.get(i);
                        nombreField.setText(j.getNombre());
                        precioField.setText(String.valueOf(j.getPrecio()));
                        stockField.setText(String.valueOf(j.getCantidad()));
                        descripcionArea.setText(j.getDescripcion());
                        registrarBtn.setText("Actualizar Producto");
                        return;
                    }
                }
                mostrarAlerta("Error", "No se encontró un producto con ID " + id);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID inválido. Ingrese un número.");
            }
        });
    }

    @FXML
    private void handleEliminar() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Eliminar Producto");
        dialog.setHeaderText("Ingrese el ID del producto a eliminar:");
        dialog.setContentText("ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr.trim());
                for (int i = 0; i < videojuegos.size(); i++) {
                    if (videojuegos.get(i).getId() == id) {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Confirmar eliminación");
                        confirm.setHeaderText("¿Eliminar " + videojuegos.get(i).getNombre() + "?");
                        confirm.setContentText("Esta acción no se puede deshacer.");

                        Optional<ButtonType> btn = confirm.showAndWait();
                        if (btn.isPresent() && btn.get() == ButtonType.OK) {
                            videojuegos.remove(i);
                            System.out.println("Producto ID " + id + " eliminado.");
                        }
                        return;
                    }
                }
                mostrarAlerta("Error", "No se encontró un producto con ID " + id);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID inválido. Ingrese un número.");
            }
        });
    }

    @FXML
    private void handleBuscar() {
        busquedaField.requestFocus();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene loginScene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Portal del Sistema - Iniciar Sesión");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard() {
        System.out.println("Dashboard selected");
    }

    @FXML
    private void handleInventory() {
        System.out.println("Inventory selected");
    }

    @FXML
    private void handleAddProduct() {
        limpiarFormulario();
        editandoIndex = -1;
        registrarBtn.setText("Registrar Producto");
    }

    private void limpiarFormulario() {
        nombreField.clear();
        precioField.clear();
        stockField.clear();
        descripcionArea.clear();
        categoriaCombo.getSelectionModel().clearSelection();
    }

    private void marcarError(TextField field) {
        field.setStyle("-fx-border-color: #ff4444; -fx-border-width: 2px; -fx-border-radius: 8px;");
    }

    private void limpiarEstiloError(TextField field) {
        field.setStyle("");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

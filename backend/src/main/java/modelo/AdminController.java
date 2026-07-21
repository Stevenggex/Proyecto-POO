package modelo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import dao.VideojuegoDAO;

import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML private TextField busquedaField;
    @FXML private TextField nombreField;
    @FXML private TextField precioField;
    @FXML private TextField stockField;
    @FXML private ComboBox<String> categoriaCombo;
    @FXML private TextArea descripcionArea;
    @FXML private Button registrarBtn;
    @FXML private GridPane inventoryGrid;
    @FXML private TextField imagenUrlField;
    @FXML private ImageView imagenPreview;
    @FXML private Label formTitle;

    @FXML private Button dashboardBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button searchBtn;
    @FXML private Button logoutBtn;

    private final VideojuegoDAO dao = new VideojuegoDAO();
    private String editandoId = null;

    @FXML
    private void initialize() {
        categoriaCombo.getItems().addAll("Accion / RPG", "Estrategia", "Simulador", "Terror", "Indie");
        nombreField.textProperty().addListener((obs, old, val) -> limpiarEstiloError(nombreField));
        precioField.textProperty().addListener((obs, old, val) -> limpiarEstiloError(precioField));
        stockField.textProperty().addListener((obs, old, val) -> limpiarEstiloError(stockField));

        busquedaField.textProperty().addListener((obs, old, val) -> {
            String filtro = val.toLowerCase().trim();
            if (filtro.isEmpty()) {
                cargarInventario();
            } else {
                cargarInventarioFiltrado(filtro);
            }
        });
        cargarInventario();
    }

    // ==================== INVENTARIO ====================

    private void cargarInventario() {
        List<Videojuego> lista = dao.listarTodos();
        renderizarInventario(lista);
    }

    private void cargarInventarioFiltrado(String filtro) {
        List<Videojuego> lista = dao.listarTodos().stream()
                .filter(j -> j.getNombre().toLowerCase().contains(filtro))
                .toList();
        renderizarInventario(lista);
    }

    private void renderizarInventario(List<Videojuego> lista) {
        inventoryGrid.getChildren().clear();
        inventoryGrid.getColumnConstraints().clear();
        inventoryGrid.getRowConstraints().clear();

        int cols = 3;
        for (int i = 0; i < cols; i++) {
            inventoryGrid.getColumnConstraints().add(new ColumnConstraints());
        }

        int row = 0, col = 0;
        for (Videojuego juego : lista) {
            VBox card = crearCardConAcciones(juego);
            GridPane.setColumnIndex(card, col);
            GridPane.setRowIndex(card, row);
            inventoryGrid.getChildren().add(card);
            col++;
            if (col >= cols) {
                col = 0;
                row++;
                inventoryGrid.getRowConstraints().add(new RowConstraints());
            }
        }
    }

    private VBox crearCardConAcciones(Videojuego juego) {
        HBox imagenBox = new HBox();
        imagenBox.setAlignment(Pos.CENTER);
        imagenBox.setPrefHeight(120.0);
        imagenBox.setStyle("-fx-background-color: #171f33; -fx-background-radius: 8px;");
        imagenBox.setPadding(new Insets(8));

        if (juego.getImagenPath() != null && !juego.getImagenPath().isEmpty()) {
            try {
                Image image = new Image(juego.getImagenPath(), 80, 80, true, true);
                if (!image.isError()) {
                    ImageView imgView = new ImageView(image);
                    imgView.setFitHeight(80.0);
                    imgView.setFitWidth(80.0);
                    imgView.setPreserveRatio(true);
                    imagenBox.getChildren().add(imgView);
                } else {
                    imagenBox.getChildren().add(new Label("🎮") {{ setStyle("-fx-font-size: 36px;"); }});
                }
            } catch (Exception e) {
                imagenBox.getChildren().add(new Label("🎮") {{ setStyle("-fx-font-size: 36px;"); }});
            }
        } else {
            imagenBox.getChildren().add(new Label("🎮") {{ setStyle("-fx-font-size: 36px;"); }});
        }

        Label badge = new Label(juego.getCantidad() > 0 ? "EN STOCK" : "SIN STOCK");
        badge.getStyleClass().addAll("badge", juego.getCantidad() > 0 ? "badge-in-stock" : "badge-out-stock");

        HBox badgeBox = new HBox(badge);
        badgeBox.setAlignment(Pos.TOP_RIGHT);
        badgeBox.setPadding(new Insets(0, 8, 0, 0));

        StackPane stackPane = new StackPane(imagenBox, badgeBox);
        StackPane.setAlignment(badgeBox, Pos.TOP_RIGHT);

        Label nombreLabel = new Label(juego.getNombre());
        nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nombreLabel.getStyleClass().add("text-white");
        nombreLabel.setWrapText(true);

        Label precioLabel = new Label("$" + String.format("%.2f", juego.getPrecio()));
        precioLabel.getStyleClass().add("text-secondary-color");
        precioLabel.setStyle("-fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox nombrePrecio = new HBox(8, nombreLabel, spacer, precioLabel);
        nombrePrecio.setAlignment(Pos.CENTER_LEFT);

        Label descLabel = new Label(juego.getDescripcion() != null ? juego.getDescripcion() : "");
        descLabel.getStyleClass().add("text-muted");
        descLabel.setWrapText(true);
        descLabel.setPrefHeight(40.0);
        descLabel.setOpacity(0.8);

        Label catLabel = new Label(juego.getGenero() != null ? juego.getGenero() : "");
        catLabel.getStyleClass().add("text-primary-color");
        catLabel.setStyle("-fx-font-size: 11px;");

        HBox stockBox = new HBox(4,
                new Label("Stock: ") {{ getStyleClass().add("text-muted"); }},
                new Label(String.valueOf(juego.getCantidad())) {{ getStyleClass().add("text-white"); }}
        );
        stockBox.setAlignment(Pos.CENTER_LEFT);
        stockBox.setStyle("-fx-border-color: #31394d transparent transparent transparent; -fx-border-width: 1px;");
        stockBox.setPadding(new Insets(8, 0, 0, 0));

        Button editBtn = new Button("Editar");
        editBtn.setStyle("-fx-background-color: #7aa2f7; -fx-text-fill: #121826; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-cursor: hand; -fx-font-size: 12px;");
        editBtn.setPrefHeight(28.0);
        editBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(editBtn, Priority.ALWAYS);
        editBtn.setOnAction(e -> editarProducto(juego));

        Button deleteBtn = new Button("Eliminar");
        deleteBtn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-cursor: hand; -fx-font-size: 12px;");
        deleteBtn.setPrefHeight(28.0);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(deleteBtn, Priority.ALWAYS);
        deleteBtn.setOnAction(e -> eliminarProducto(juego));

        HBox botonesBox = new HBox(8, editBtn, deleteBtn);
        botonesBox.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, stackPane, nombrePrecio, catLabel, descLabel, stockBox, botonesBox);
        card.setPrefWidth(280.0);
        card.getStyleClass().add("game-card");
        card.setPadding(new Insets(12));
        return card;
    }

    // ==================== EDITAR / ELIMINAR ====================

    private void editarProducto(Videojuego juego) {
        editandoId = juego.getId();
        nombreField.setText(juego.getNombre());
        precioField.setText(String.valueOf(juego.getPrecio()));
        stockField.setText(String.valueOf(juego.getCantidad()));
        descripcionArea.setText(juego.getDescripcion());
        if (juego.getGenero() != null && !juego.getGenero().isEmpty()) {
            categoriaCombo.setValue(juego.getGenero());
        }
        if (juego.getImagenPath() != null && !juego.getImagenPath().isEmpty()) {
            imagenUrlField.setText(juego.getImagenPath());
            try {
                Image image = new Image(juego.getImagenPath(), 80, 80, true, true);
                if (!image.isError()) {
                    imagenPreview.setImage(image);
                    imagenPreview.setVisible(true);
                }
            } catch (Exception ignored) {}
        }
        formTitle.setText("Editar Producto");
        registrarBtn.setText("Actualizar Producto");
        registrarBtn.setDisable(false);
    }

    @FXML
    private void handleRegistrar() {
        if (editandoId == null) return;

        String nombre = nombreField.getText().trim();
        String precioText = precioField.getText().trim();
        String stockText = stockField.getText().trim();
        String descripcion = descripcionArea.getText().trim();
        String genero = categoriaCombo.getValue();

        boolean valido = true;
        if (nombre.isEmpty()) { marcarError(nombreField); valido = false; }
        if (precioText.isEmpty()) { marcarError(precioField); valido = false; }
        if (stockText.isEmpty()) { marcarError(stockField); valido = false; }
        if (!valido) return;

        double precio;
        int stock;
        try { precio = Double.parseDouble(precioText); } catch (NumberFormatException e) { marcarError(precioField); return; }
        try { stock = Integer.parseInt(stockText); } catch (NumberFormatException e) { marcarError(stockField); return; }

        Videojuego juego = dao.buscarPorId(editandoId);
        if (juego == null) {
            mostrarAlerta("Error", "No se encontro el producto.", Alert.AlertType.ERROR);
            return;
        }

        juego.setNombre(nombre);
        juego.setPrecio(precio);
        juego.setCantidad(stock);
        juego.setDescripcion(descripcion);
        if (genero != null) juego.setGenero(genero);
        String imgPath = imagenUrlField.getText().trim();
        if (!imgPath.isEmpty()) juego.setImagenPath(imgPath);

        dao.actualizar(juego);
        editandoId = null;
        limpiarFormulario();
        cargarInventario();
        mostrarAlerta("Exito", "Producto '" + juego.getNombre() + "' actualizado correctamente.", Alert.AlertType.INFORMATION);
    }

    private void eliminarProducto(Videojuego juego) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminacion");
        confirm.setHeaderText("¿Eliminar '" + juego.getNombre() + "'?");
        confirm.setContentText("Esta accion no se puede deshacer.");

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                dao.eliminar(juego.getId());
                if (editandoId != null && editandoId.equals(juego.getId())) {
                    editandoId = null;
                    limpiarFormulario();
                }
                cargarInventario();
                mostrarAlerta("Exito", "Producto '" + juego.getNombre() + "' eliminado correctamente.", Alert.AlertType.INFORMATION);
            }
        });
    }

    // ==================== IMAGEN ====================

    @FXML
    private void cargarPreviewUrl() {
        String url = imagenUrlField.getText().trim();
        if (url.isEmpty()) {
            imagenPreview.setImage(null);
            imagenPreview.setVisible(false);
            return;
        }
        try {
            Image image = new Image(url, 80, 80, true, true);
            if (!image.isError()) {
                imagenPreview.setImage(image);
                imagenPreview.setVisible(true);
            } else {
                imagenPreview.setImage(null);
                imagenPreview.setVisible(false);
                mostrarAlerta("Advertencia", "No se pudo cargar la imagen desde esa URL.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            imagenPreview.setImage(null);
            imagenPreview.setVisible(false);
            mostrarAlerta("Error", "URL de imagen no valida.", Alert.AlertType.ERROR);
        }
    }

    // ==================== NAVEGACION ====================

    @FXML
    private void handleDashboard() {
        mostrarAlerta("Panel", "Vista de administracion.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleInventory() {
        busquedaField.clear();
        limpiarFormulario();
        cargarInventario();
    }

    @FXML
    private void handleBuscar() {
        busquedaField.clear();
        busquedaField.requestFocus();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/backend/Login.fxml"));
            Scene loginScene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Portal del Sistema - Iniciar Sesion");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==================== UTILIDADES ====================

    private void limpiarFormulario() {
        nombreField.clear();
        precioField.clear();
        stockField.clear();
        descripcionArea.clear();
        categoriaCombo.getSelectionModel().clearSelection();
        imagenUrlField.clear();
        imagenPreview.setImage(null);
        imagenPreview.setVisible(false);
        formTitle.setText("Editar Producto");
        registrarBtn.setText("Actualizar Producto");
        registrarBtn.setDisable(true);
    }

    private void marcarError(TextField field) {
        field.setStyle("-fx-border-color: #ff4444; -fx-border-width: 2px; -fx-border-radius: 8px;");
    }

    private void limpiarEstiloError(TextField field) {
        field.setStyle("");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

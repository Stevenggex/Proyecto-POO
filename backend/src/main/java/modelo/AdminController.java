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

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML private TextField busquedaField;
    @FXML private GridPane inventoryGrid;
    @FXML private Button dashboardBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button searchBtn;
    @FXML private Button logoutBtn;

    private final VideojuegoDAO dao = new VideojuegoDAO();

    @FXML
    private void initialize() {
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

    // ==================== INVENTARIO SOLO LECTURA ====================

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
            VBox card = crearCardSoloLectura(juego);
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

    private VBox crearCardSoloLectura(Videojuego juego) {
        HBox imagenBox = new HBox();
        imagenBox.setAlignment(Pos.CENTER);
        imagenBox.setPrefHeight(120.0);
        imagenBox.setStyle("-fx-background-color: #171f33; -fx-background-radius: 8px;");
        imagenBox.setPadding(new Insets(8));

        if (juego.getImagenPath() != null && !juego.getImagenPath().isEmpty()) {
            try {
                File file = new File(juego.getImagenPath());
                if (file.exists()) {
                    ImageView imgView = new ImageView(new Image(file.toURI().toString()));
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

        VBox card = new VBox(6, stackPane, nombrePrecio, catLabel, descLabel, stockBox);
        card.setPrefWidth(280.0);
        card.getStyleClass().add("game-card");
        card.setPadding(new Insets(12));
        return card;
    }

    // ==================== NAVEGACION ====================

    @FXML
    private void handleDashboard() {
        mostrarAlerta("Panel", "Vista de administracion - Solo lectura.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleInventory() {
        busquedaField.clear();
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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

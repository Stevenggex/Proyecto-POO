package org.example.backend;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Videojuego;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompraController {

    @FXML private TextField searchField;
    @FXML private GridPane productGrid;
    @FXML private TextField nombreClienteField;
    @FXML private TextField direccionField;
    @FXML private TextField correoField;
    @FXML private TextField telefonoField;
    @FXML private ComboBox<String> pagoCombo;
    @FXML private TableView<LineaFactura> facturaTable;
    @FXML private TableColumn<LineaFactura, String> colProducto;
    @FXML private TableColumn<LineaFactura, Integer> colCant;
    @FXML private TableColumn<LineaFactura, Double> colPrecio;
    @FXML private TableColumn<LineaFactura, Double> colSubtotal;
    @FXML private Label totalLabel;
    @FXML private Button registrarCompraBtn;

    private final List<Videojuego> catalogo = new ArrayList<>();
    private final ObservableList<LineaFactura> facturaItems = FXCollections.observableArrayList();
    private double totalCompra = 0;

    @FXML
    private void initialize() {
        pagoCombo.getItems().addAll("Efectivo", "Tarjeta de Credito", "Tarjeta de Debito", "Transferencia");

        colProducto.setCellValueFactory(data -> data.getValue().productoProperty());
        colCant.setCellValueFactory(data -> data.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(data -> data.getValue().precioProperty().asObject());
        colSubtotal.setCellValueFactory(data -> data.getValue().subtotalProperty().asObject());

        facturaTable.setItems(facturaItems);

        catalogo.add(new Videojuego("Void Hunter: Genesis", 59.99, 142, "Deep-space extraction RPG", ""));
        catalogo.add(new Videojuego("Neon Katana", 34.50, 0, "Fast-paced cyber-slasher", ""));
        catalogo.add(new Videojuego("Stellar Drift", 45.00, 88, "Open-world space exploration", ""));
        catalogo.add(new Videojuego("Pixel Kingdoms", 19.99, 230, "Retro-style strategy game", ""));
        catalogo.add(new Videojuego("Echoes of the Void", 79.99, 55, "Horror adventure", ""));
        catalogo.add(new Videojuego("Factory Tycoon", 24.99, 120, "Build and manage your factory", ""));

        for (int i = 0; i < catalogo.size(); i++) {
            catalogo.get(i).setId(i + 1);
        }

        poblarGrid(catalogo);

        searchField.textProperty().addListener((obs, old, val) -> {
            String filtro = val.toLowerCase().trim();
            if (filtro.isEmpty()) {
                poblarGrid(catalogo);
            } else {
                poblarGrid(catalogo.stream()
                        .filter(j -> j.getNombre().toLowerCase().contains(filtro))
                        .collect(Collectors.toList()));
            }
        });
    }

    private void poblarGrid(List<Videojuego> lista) {
        productGrid.getChildren().clear();
        productGrid.getColumnConstraints().clear();
        productGrid.getRowConstraints().clear();

        int cols = 3;
        for (int i = 0; i < cols; i++) {
            productGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints());
        }

        int row = 0, col = 0;
        for (Videojuego juego : lista) {
            VBox card = crearCard(juego);
            GridPane.setColumnIndex(card, col);
            GridPane.setRowIndex(card, row);
            productGrid.getChildren().add(card);
            col++;
            if (col >= cols) {
                col = 0;
                row++;
                productGrid.getRowConstraints().add(new javafx.scene.layout.RowConstraints());
            }
        }
    }

    private VBox crearCard(Videojuego juego) {
        Label stockLabel = new Label("Stock: " + juego.getCantidad());
        stockLabel.getStyleClass().add("text-muted");

        Button addBtn = new Button("+ Agregar");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setPrefHeight(32.0);
        addBtn.getStyleClass().add("btn-primary-neon");

        if (juego.getCantidad() <= 0) {
            addBtn.setDisable(true);
            addBtn.setText("Sin Stock");
        }

        addBtn.setOnAction(e -> agregarAlCarrito(juego));

        VBox card = new VBox(8.0,
                new HBox(10.0,
                        new Label("🎮") {{
                            setStyle("-fx-font-size: 36px;");
                        }}
                ) {{
                    setAlignment(Pos.CENTER);
                    setPrefHeight(100.0);
                    setStyle("-fx-background-color: #171f33; -fx-background-radius: 8px;");
                }},
                new Label(juego.getNombre()) {{
                    setStyle("-fx-font-weight: bold;");
                    getStyleClass().add("text-white");
                }},
                new Label("$" + String.format("%.2f", juego.getPrecio())) {{
                    getStyleClass().add("text-secondary-color");
                }},
                stockLabel,
                addBtn
        );
        card.setPrefWidth(240.0);
        card.setSpacing(8.0);
        card.getStyleClass().add("game-card");
        VBox.setMargin(card, new Insets(0));
        card.setPadding(new Insets(12.0));
        return card;
    }

    private void agregarAlCarrito(Videojuego juego) {
        for (LineaFactura item : facturaItems) {
            if (item.getProducto().equals(juego.getNombre())) {
                item.setCantidad(item.getCantidad() + 1);
                actualizarTotal();
                return;
            }
        }
        facturaItems.add(new LineaFactura(juego.getNombre(), juego.getPrecio(), 1));
        actualizarTotal();
    }

    private void actualizarTotal() {
        totalCompra = facturaItems.stream()
                .mapToDouble(LineaFactura::getSubtotal)
                .sum();
        totalLabel.setText("Total: $" + String.format("%.2f", totalCompra));
    }

    @FXML
    private void handleRegistrarCompra() {
        String nombre = nombreClienteField.getText().trim();
        String direccion = direccionField.getText().trim();
        String correo = correoField.getText().trim();
        String telefono = telefonoField.getText().trim();
        String pago = pagoCombo.getValue();

        if (nombre.isEmpty() || direccion.isEmpty() || correo.isEmpty() || telefono.isEmpty() || pago == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Complete todos los datos del cliente y seleccione un método de pago.");
            alert.showAndWait();
            return;
        }

        if (facturaItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Carrito vacío");
            alert.setHeaderText(null);
            alert.setContentText("Agregue al menos un producto a la factura.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar Compra");
        confirm.setHeaderText("¿Registrar la compra de " + nombre + " por $" + String.format("%.2f", totalCompra) + "?");
        Optional<ButtonType> btn = confirm.showAndWait();
        if (btn.isEmpty() || btn.get() != ButtonType.OK) return;

        System.out.println("=================================");
        System.out.println("         FACTURA");
        System.out.println("=================================");
        System.out.println("Cliente: " + nombre);
        System.out.println("Correo:  " + correo);
        System.out.println("Telefono: " + telefono);
        System.out.println("Dir:     " + direccion);
        System.out.println("Pago:    " + pago);
        System.out.println("---------------------------------");
        for (LineaFactura item : facturaItems) {
            System.out.printf("%-20s x%d  $%.2f\n", item.getProducto(), item.getCantidad(), item.getSubtotal());
        }
        System.out.println("---------------------------------");
        System.out.printf("TOTAL:              $%.2f\n", totalCompra);
        System.out.println("=================================");

        limpiarFormulario();
    }

    private void limpiarFormulario() {
        nombreClienteField.clear();
        direccionField.clear();
        correoField.clear();
        telefonoField.clear();
        pagoCombo.getSelectionModel().clearSelection();
        facturaItems.clear();
        totalLabel.setText("Total: $0.00");
        totalCompra = 0;
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene loginScene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) registrarCompraBtn.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Portal del Sistema - Iniciar Sesión");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class LineaFactura {
        private final StringProperty producto = new SimpleStringProperty();
        private final IntegerProperty cantidad = new SimpleIntegerProperty();
        private final DoubleProperty precio = new SimpleDoubleProperty();
        private final DoubleProperty subtotal = new SimpleDoubleProperty();

        public LineaFactura(String producto, double precio, int cantidad) {
            this.producto.set(producto);
            this.precio.set(precio);
            this.cantidad.set(cantidad);
            this.subtotal.set(precio * cantidad);
        }

        public String getProducto() { return producto.get(); }
        public StringProperty productoProperty() { return producto; }
        public int getCantidad() { return cantidad.get(); }
        public void setCantidad(int cantidad) {
            this.cantidad.set(cantidad);
            this.subtotal.set(this.precio.get() * cantidad);
        }
        public IntegerProperty cantidadProperty() { return cantidad; }
        public double getPrecio() { return precio.get(); }
        public DoubleProperty precioProperty() { return precio; }
        public double getSubtotal() { return subtotal.get(); }
        public DoubleProperty subtotalProperty() { return subtotal; }
    }
}

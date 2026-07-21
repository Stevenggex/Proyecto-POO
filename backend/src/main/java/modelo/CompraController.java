package modelo;

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
import dao.VideojuegoDAO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @FXML private TableColumn<LineaFactura, Void> colEliminar;
    @FXML private Label totalLabel;
    @FXML private Button registrarCompraBtn;

    private final VideojuegoDAO dao = new VideojuegoDAO();
    private List<Videojuego> catalogo;
    private final ObservableList<LineaFactura> facturaItems = FXCollections.observableArrayList();
    private double totalCompra = 0;

    @FXML
    private void initialize() {
        pagoCombo.getItems().addAll("Efectivo", "Tarjeta de Credito", "Tarjeta de Debito", "Transferencia");

        colProducto.setCellValueFactory(data -> data.getValue().productoProperty());
        colCant.setCellValueFactory(data -> data.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(data -> data.getValue().precioProperty().asObject());
        colSubtotal.setCellValueFactory(data -> data.getValue().subtotalProperty().asObject());

        colEliminar.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Button btn = new Button("X");
                    btn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4px; -fx-cursor: hand; -fx-font-size: 11px;");
                    btn.setPrefSize(36, 22);
                    btn.setOnAction(e -> {
                        LineaFactura itemToRemove = getTableView().getItems().get(getIndex());
                        facturaItems.remove(itemToRemove);
                        actualizarTotal();
                    });
                    setGraphic(btn);
                }
            }
        });

        facturaTable.setItems(facturaItems);

        catalogo = dao.listarTodos();
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
            mostrarAlerta("Campos incompletos", "Complete todos los datos del cliente y seleccione un metodo de pago.", Alert.AlertType.ERROR);
            return;
        }

        if (facturaItems.isEmpty()) {
            mostrarAlerta("Carrito vacio", "Agregue al menos un producto a la factura.", Alert.AlertType.ERROR);
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

        guardarEnArchivo(nombre, telefono, direccion, correo, pago);

        mostrarAlerta("Exito", "Compra registrada correctamente.", Alert.AlertType.INFORMATION);
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

    private void guardarEnArchivo(String nombre, String telefono, String direccion, String correo, String pago) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Cliente: ").append(nombre).append("\n");
        sb.append("Telefono: ").append(telefono).append("\n");
        sb.append("Direccion: ").append(direccion).append("\n");
        sb.append("Correo: ").append(correo).append("\n");
        sb.append("Metodo de pago: ").append(pago).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("Juegos:\n");
        for (LineaFactura item : facturaItems) {
            sb.append("  - ").append(item.getProducto())
              .append(" x").append(item.getCantidad())
              .append("  $").append(String.format("%.2f", item.getPrecio()))
              .append("  Subtotal: $").append(String.format("%.2f", item.getSubtotal()))
              .append("\n");
        }
        sb.append("----------------------------------------\n");
        sb.append("TOTAL: $").append(String.format("%.2f", totalCompra)).append("\n");
        sb.append("========================================\n\n");

        try {
            File archivo = new File("Compras.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
                writer.write(sb.toString());
            }
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo guardar el registro de compra.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/backend/Login.fxml"));
            Scene loginScene = new Scene(loader.load(), 800, 600);
            Stage stage = (Stage) registrarCompraBtn.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Portal del Sistema - Iniciar Sesion");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCatalogo() {
        catalogo = dao.listarTodos();
        poblarGrid(catalogo);
        searchField.clear();
    }

    @FXML
    private void handleMisCompras() {
        mostrarAlerta("Mis Compras", "No tiene compras registradas aun.", Alert.AlertType.INFORMATION);
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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

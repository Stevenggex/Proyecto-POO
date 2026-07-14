package modelo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private ToggleGroup rolToggleGroup;
    @FXML private ToggleButton adminToggle;
    @FXML private ToggleButton empleadoToggle;
    @FXML private ToggleButton usuarioToggle;
    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;
    @FXML private Label mensajeErrorLabel;
    @FXML private Button loginButton;

    @FXML
    private void initialize() {
        usuarioField.textProperty().addListener((obs, old, val) -> ocultarError());
        passwordField.textProperty().addListener((obs, old, val) -> ocultarError());
        rolToggleGroup.selectedToggleProperty().addListener((obs, old, val) -> ocultarError());
    }

    @FXML
    private void handleLogin() {
        String usuarioTexto = usuarioField.getText().trim();
        String passwordTexto = passwordField.getText();

        if (usuarioTexto.isEmpty() || passwordTexto.isEmpty()) {
            mostrarAlerta("Advertencia", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        Toggle selected = rolToggleGroup.getSelectedToggle();
        if (selected == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un rol.", Alert.AlertType.WARNING);
            return;
        }

        String rolSeleccionado = selected.getUserData().toString();
        Rol rolUsuario = autenticar(usuarioTexto, passwordTexto);

        if (rolUsuario == null) {
            mostrarAlerta("Error", "Usuario o contrasena incorrectos.", Alert.AlertType.ERROR);
            return;
        }

        String rolEsperado = switch (rolUsuario) {
            case ADMINISTRADOR -> "Administrador";
            case EMPLEADO -> "Empleado";
            case USUARIO -> "Usuario";
        };

        if (!rolSeleccionado.equals(rolEsperado)) {
            mostrarAlerta("Error", "El rol seleccionado no coincide con el usuario.", Alert.AlertType.ERROR);
            return;
        }

        ocultarError();
        mostrarAlerta("Bienvenido", "Inicio de sesion exitoso como " + rolEsperado + ".", Alert.AlertType.INFORMATION);
        abrirDashboard(rolUsuario);
    }

    private Rol autenticar(String usuario, String password) {
        try {
            Firestore db = FirebaseConfig.getDb();
            var query = db.collection("usuarios")
                    .whereEqualTo("usuario", usuario)
                    .whereEqualTo("password", password)
                    .get()
                    .get();
            if (query.isEmpty()) {
                return null;
            }
            String rol = query.getDocuments().get(0).getString("rol");

            return switch (rol) {
                case "Administrador" -> Rol.ADMINISTRADOR;
                case "Empleado" -> Rol.EMPLEADO;
                case "Usuario" -> Rol.USUARIO;
                default -> null;
            };

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al autenticar: " + e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }


    @FXML
    private void handleOlvidoContrasena() {
        mostrarAlerta("Ayuda", "Contacte al administrador del sistema para restablecer su contrasena.", Alert.AlertType.INFORMATION);
    }

    private void mostrarError(String mensaje) {
        mensajeErrorLabel.setText(mensaje);
        mensajeErrorLabel.setVisible(true);
        mensajeErrorLabel.setManaged(true);
    }

    private void ocultarError() {
        mensajeErrorLabel.setVisible(false);
        mensajeErrorLabel.setManaged(false);
    }

    private void abrirDashboard(Rol rol) {
        try {
            String fxml = switch (rol) {
                case ADMINISTRADOR -> "/org/example/backend/Admin.fxml";
                case EMPLEADO -> "/org/example/backend/Dashboard.fxml";
                case USUARIO -> "/org/example/backend/CompraView.fxml";
            };
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene dashboardScene = new Scene(loader.load(), 1280, 768);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("QuestLog - " + rol);
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar el panel: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

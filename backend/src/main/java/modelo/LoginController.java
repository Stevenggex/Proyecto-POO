package modelo;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;
    @FXML private Label mensajeErrorLabel;
    @FXML private Button loginButton;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void initialize() {
        usuarioField.textProperty().addListener((obs, old, val) -> ocultarError());
        passwordField.textProperty().addListener((obs, old, val) -> ocultarError());
    }

    @FXML
    private void handleLogin() {
        String usuarioTexto = usuarioField.getText().trim();
        String passwordTexto = passwordField.getText();

        if (usuarioTexto.isEmpty() || passwordTexto.isEmpty()) {
            mostrarAlerta("Advertencia", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        Rol rolUsuario = usuarioDAO.autenticar(usuarioTexto, passwordTexto);

        if (rolUsuario == null) {
            mostrarAlerta("Error", "Usuario o contrasena incorrectos.", Alert.AlertType.ERROR);
            return;
        }

        ocultarError();
        String rolNombre = switch (rolUsuario) {
            case ADMINISTRADOR -> "Administrador";
            case EMPLEADO -> "Empleado";
            case USUARIO -> "Usuario";
        };
        mostrarAlerta("Bienvenido", "Inicio de sesion exitoso como " + rolNombre + ".", Alert.AlertType.INFORMATION);
        abrirDashboard(rolUsuario);
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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

package org.example.backend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML private TextField busquedaField;
    @FXML private GridPane inventoryGrid;
    @FXML private Button dashboardBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button searchBtn;
    @FXML private Button logoutBtn;

    @FXML
    private void handleDashboard() {
        System.out.println("Admin - Dashboard");
    }

    @FXML
    private void handleInventory() {
        System.out.println("Admin - Inventario");
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
}

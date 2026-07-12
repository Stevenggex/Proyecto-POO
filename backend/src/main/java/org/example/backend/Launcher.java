package org.example.backend;

import javafx.application.Application;
import modelo.FirebaseConfig;

public class Launcher {
    public static void main(String[] args) {
        try {
            FirebaseConfig.initFirebase();
            System.out.println("Firebase conectado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al conectar con Firebase: " + e.getMessage());
            e.printStackTrace();
        }
        Application.launch(HelloApplication.class, args);
    }
}

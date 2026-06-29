module org.example.backend {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.backend to javafx.fxml;
    exports org.example.backend;
}
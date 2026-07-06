module org.example.backend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.backend to javafx.fxml;
    opens modelo to javafx.fxml;
    exports org.example.backend;
}
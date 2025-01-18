module com.example {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.sql;

    opens com.example to javafx.fxml;
    exports com.example;
    opens com.example.controller to javafx.fxml;
    exports com.example.controller;
}
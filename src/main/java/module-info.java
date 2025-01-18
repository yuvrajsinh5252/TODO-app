module com.example {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires transitive javafx.graphics;

    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    exports com.example;
}

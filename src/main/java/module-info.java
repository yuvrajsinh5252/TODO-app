module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires transitive java.sql;
    requires transitive javafx.graphics;
    requires mysql.connector.j;

    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    exports com.example;
    exports com.example.connection;
}
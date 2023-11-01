package Javafx.Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection dataBaseLink;

    public Connection getConnection() {
        String databaseName = "sys";
        String dataBaseUser = "root";
        String dataBasePassword = "8)Kw(M%L34G9";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            dataBaseLink = DriverManager.getConnection(url, dataBaseUser, dataBasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataBaseLink;
    }
}

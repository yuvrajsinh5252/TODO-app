package com.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private Connection databaseLink;
    private static final String DATABASE_NAME = "sys";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "8)Kw(M%L34G9";
    private static final String SERVER_TIMEZONE = "UTC";

    public Connection getConnection() throws SQLException {
        if (databaseLink != null && !databaseLink.isClosed()) {
            return databaseLink;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = String.format(
                "jdbc:mysql://localhost:3306/%s?serverTimezone=%s&useSSL=false&allowPublicKeyRetrieval=true",
                DATABASE_NAME,
                SERVER_TIMEZONE
            );

            databaseLink = DriverManager.getConnection(url, DATABASE_USER, DATABASE_PASSWORD);
            return databaseLink;

        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    public void closeConnection() {
        if (databaseLink != null) {
            try {
                databaseLink.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
package Javafx;

import java.sql.*;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class DButils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String Username) {
        Parent root = null;

        if (Username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DButils.class.getResource(fxmlFile));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DButils.class.getResource(fxmlFile));
            } catch (IOException e) {
                System.out.println("Error ChangeScene: " + e.getMessage());                
            }
        }
        if (title == "Add Item") {
            ScrollPane scroll = (ScrollPane) root.lookup("#TaskScroll");
            scroll.setVisible(false);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }    

    public static void SignUpUser (ActionEvent event, String Username, String Password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement PsCheckUserExists = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "8)Kw(M%L34G9");
            PsCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            PsCheckUserExists.setString(1, Username);
            rs = PsCheckUserExists.executeQuery();

            if (rs.isBeforeFirst()) {
                System.out.println("User already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You cannot use this username");
                alert.showAndWait();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
                psInsert.setString(1, Username);
                psInsert.setString(2, Password);
                psInsert.executeUpdate();
                System.out.println("User added");
                changeScene(event, "view/Interface.fxml", "Welcome", null);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } 
            if (PsCheckUserExists != null) {
                try {
                    PsCheckUserExists.close();
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    public static void LoginUser (ActionEvent event, String username, String Password) {
        Connection connection = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet rs = null;
        
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "8)Kw(M%L34G9");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            rs = psCheckUserExists.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) { 
                    if (rs.getString("password").equals(Password)) {
                        System.out.println("User logged in");
                        updateui.UpdateAddItemFXML(event, username);
                    } else {
                        System.out.println("Wrong password");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Wrong password");
                        alert.showAndWait();
                    }
                }
            } else {
                System.out.println("User does not exist");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("User does not exist");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } 
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateCheckColumn(String completed, int taskId) {
        Connection connection = null;
        PreparedStatement psUpdate = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "8)Kw(M%L34G9");
            psUpdate = connection.prepareStatement("UPDATE task SET `check` = ? WHERE task_id = ?");
            psUpdate.setString(1, completed);
            psUpdate.setInt(2, taskId);
            psUpdate.executeUpdate();
            System.out.println("Check column updated");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (psUpdate != null) {
                try {
                    psUpdate.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void SaveToDB(ActionEvent event, String username, String TaskText, String description, String time) {
        Connection connection = null;
        PreparedStatement psCheckUserExists = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "8)Kw(M%L34G9");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM task WHERE username = ?");
            psCheckUserExists.setString(1, username);

            PreparedStatement psInsert = connection.prepareStatement("INSERT INTO task (username, task, description, time) VALUES (?, ?, ?, ?)");
            psInsert.setString(1, username);
            psInsert.setString(2, TaskText);
            psInsert.setString(3, description);
            psInsert.setString(4, time);
            psInsert.executeUpdate();
            System.out.println("Task added");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void DeleteTask(String username, int taskId) {
        Connection connection = null;
        PreparedStatement psDeleteTask = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "8)Kw(M%L34G9");
            psDeleteTask = connection.prepareStatement("DELETE FROM task WHERE username = ? AND task_id = ?");
            psDeleteTask.setString(1, username);
            psDeleteTask.setInt(2, taskId);
            int rowsAffected = psDeleteTask.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task deleted");
            } else {
                System.out.println("Task not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (psDeleteTask != null) {
                try {
                    psDeleteTask.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
package Javafx;

import java.sql.*;
import java.util.HashMap;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
                        UpdateAddItemFXML(event, username);
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

    public static void UpdateAddItemFXML (ActionEvent event, String username) {
        Connection connection = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "8)Kw(M%L34G9");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM task WHERE username = ?");
            psCheckUserExists.setString(1, username);
            rs = psCheckUserExists.executeQuery();

            HashMap<String, String> getAllCheck = new HashMap<String, String>();

            if (rs.isBeforeFirst()) {
                FXMLLoader loader = new FXMLLoader(DButils.class.getResource("view/AddItem.fxml"));
                Parent root = loader.load();

                Label user = (Label) root.lookup(".user");
                user.setText(username);

                ScrollPane scroll = (ScrollPane) root.lookup("#TaskScroll");
                VBox Vbox = new VBox();
                scroll.setStyle("-fx-background-color: #FCFCFC; -fx-background: transparent; -fx-border-color: transparent;");
                Vbox.setStyle("-fx-background-color: #FCFCFC; -fx-padding: 10 10 10 20;-fx-spacing: 10px;");

                while (rs.next()) {
                    if (rs.getString("check") != null) {
                        getAllCheck.put(rs.getString("task_id"), rs.getString("check"));
                    } 

                    HBox rootThumb = new HBox();

                    Label TASK = new Label(rs.getString("task"));
                    Label DESCRIPTION = new Label(rs.getString("description"));
                    Label TIME = new Label(rs.getString("time"));

                    TASK.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-text-fill: #808080;");
                    DESCRIPTION.setStyle("-fx-font-size: 15px; -fx-text-fill: #808080;");
                    TIME.setStyle("-fx-font-size: 15px; -fx-text-fill: #808080;");

                    VBox data = new VBox();
                    data.setPrefHeight(60);
                    data.setPrefWidth(200);
                    
                    data.getChildren().add(TASK);
                    data.getChildren().add(DESCRIPTION);
                    data.getChildren().add(TIME);

                    ImageView check = new ImageView();
                    ImageView thumb = new ImageView();
                    check.setId("check" + " " + rs.getString("task_id"));
                    thumb.setId("thumb" + " " + rs.getString("task_id"));
                    System.out.println(thumb.getId());

                    if (getAllCheck.get(rs.getString("task_id")) != null) {
                        check.setImage(new Image("Javafx/Assets/Check.png"));
                        rootThumb.getStyleClass().add("checked");
                    } 
                    else {
                        check.setImage(new Image("Javafx/Assets/Uncheck.png"));
                        rootThumb.getStyleClass().add("unchecked");
                    }
                    thumb.setImage(new Image("Javafx/Assets/Thumb.png"));
                    thumb.getStyleClass().add("thumb");
                    check.getStyleClass().add("check");

                    check.setFitHeight(40);
                    check.setFitWidth(40);
                    thumb.setFitHeight(40);
                    thumb.setFitWidth(40);

                    HBox checkBox = new HBox();
                    HBox DeleteBox = new HBox();
                    checkBox.setStyle("-fx-cursor: hand;");
                    DeleteBox.setStyle("-fx-cursor: hand;");

                    checkBox.getChildren().add(check);
                    DeleteBox.getChildren().add(thumb);
                    checkBox.setId("checkBox");
                    DeleteBox.setId("DeleteBox");

                    checkBox.alignmentProperty().set(javafx.geometry.Pos.CENTER);
                    DeleteBox.alignmentProperty().set(javafx.geometry.Pos.CENTER);

                    check.setStyle("-fx-padding: 20px");

                    rootThumb.getChildren().add(data);
                    rootThumb.getChildren().add(checkBox);
                    rootThumb.getChildren().add(DeleteBox);

                    Vbox.getChildren().add(rootThumb);
                }

                scroll.setOnMouseClicked(e -> {
                    Node clickedNode = e.getPickResult().getIntersectedNode();
                    if (clickedNode instanceof HBox) {
                        String id = clickedNode.getId();
                        System.out.println(id);

                        if (id.contains("checkBox")) {
                            HBox checkBox = (HBox) clickedNode;
                            ImageView check = (ImageView) checkBox.getChildren().get(0);
                            Node parent = check.getParent().getParent();

                            if (check.getImage().getUrl().contains("Check.png")) {
                                updateCheckColumn(null, Integer.parseInt(check.getId().split(" ")[1]));
                                getAllCheck.put(check.getId().split(" ")[1], "1");
                                parent.getStyleClass().remove("checked");
                                parent.getStyleClass().add("unchecked");
                                check.setImage(new Image("Javafx/Assets/Uncheck.png"));
                            } else {
                                updateCheckColumn("1", Integer.parseInt(check.getId().split(" ")[1]));
                                getAllCheck.put(check.getId().split(" ")[1], null);
                                parent.getStyleClass().remove("unchecked");
                                parent.getStyleClass().add("checked");
                                check.setImage(new Image("Javafx/Assets/Check.png"));
                            }
                        } else if (id.contains("DeleteBox")) {
                            HBox DeleteBox = (HBox) clickedNode;
                            ImageView thumb = (ImageView) DeleteBox.getChildren().get(0);

                            DeleteTask(username, Integer.parseInt(thumb.getId().split(" ")[1]));
                            Vbox.getChildren().remove(clickedNode.getParent());
                        }
                    }

                    if (Vbox.getChildren().isEmpty()) {
                        scroll.setVisible(false);
                    }
                });

                scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
                scroll.setVbarPolicy(ScrollBarPolicy.NEVER);

                scroll.setContent(Vbox);
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                FXMLLoader loader = new FXMLLoader(DButils.class.getResource("view/AddItem.fxml"));
                Parent root = loader.load();

                Label user = (Label) root.lookup(".user");
                user.setText(username);

                ScrollPane scroll = (ScrollPane) root.lookup("#TaskScroll");
                scroll.setVisible(false);

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

                System.out.println("No tasks");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e){
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
                } catch (SQLException e){
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
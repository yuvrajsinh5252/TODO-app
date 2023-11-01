package Javafx;

import java.sql.*;
import java.util.HashMap;
import Javafx.Connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateUI {
    public static void UpdateAddItemFXML (ActionEvent event, String username) {
        Connection connection = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet rs = null;

        try {
            DBConnection Connection = new DBConnection();
            connection = Connection.getConnection();
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
                    thumb.getStyleClass().add("thumb");
                    check.getStyleClass().add("check");

                    if (getAllCheck.get(rs.getString("task_id")) != null) {
                        check.setImage(new Image("Javafx/Assets/Check.png"));
                        rootThumb.getStyleClass().add("checked");
                    } 
                    else {
                        check.setImage(new Image("Javafx/Assets/Uncheck.png"));
                        rootThumb.getStyleClass().add("unchecked");
                    }

                    thumb.setImage(new Image("Javafx/Assets/Thumb.png"));
                    
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

                    rootThumb.getChildren().addAll(data, checkBox, DeleteBox);
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
                                DButils.updateCheckColumn(null, Integer.parseInt(check.getId().split(" ")[1]));
                                getAllCheck.put(check.getId().split(" ")[1], "1");
                                parent.getStyleClass().remove("checked");
                                parent.getStyleClass().add("unchecked");
                                check.setImage(new Image("Javafx/Assets/Uncheck.png"));
                            } else {
                                DButils.updateCheckColumn("1", Integer.parseInt(check.getId().split(" ")[1]));
                                getAllCheck.put(check.getId().split(" ")[1], null);
                                parent.getStyleClass().remove("unchecked");
                                parent.getStyleClass().add("checked");
                                check.setImage(new Image("Javafx/Assets/Check.png"));
                            }
                        } else if (id.contains("DeleteBox")) {
                            HBox DeleteBox = (HBox) clickedNode;
                            ImageView thumb = (ImageView) DeleteBox.getChildren().get(0);

                            DButils.DeleteTask(username, Integer.parseInt(thumb.getId().split(" ")[1]));
                            Vbox.getChildren().remove(clickedNode.getParent());
                        }
                    }

                    if (Vbox.getChildren().isEmpty()) {scroll.setVisible(false);}
                });

                scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
                scroll.setVbarPolicy(ScrollBarPolicy.NEVER);

                scroll.setContent(Vbox);
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
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
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {rs.close();} 
                catch (SQLException e){e.printStackTrace();}
            }
            if (psCheckUserExists != null) {
                try {psCheckUserExists.close();}
                catch (SQLException e) {e.printStackTrace();}
            }
            if (connection != null) {
                try {connection.close();}
                catch (SQLException e){e.printStackTrace();}
            }
        }
    }
}

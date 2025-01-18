package com.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import java.util.Optional;

import com.example.connection.DBConnection;

public class UpdateUI {
    /**
     * @param event
     * @param username
     */
    public static void UpdateAddItemFXML(ActionEvent event, String username) {
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
                FXMLLoader loader = new FXMLLoader(DButils.class.getResource("/com/example/AddItem.fxml"));
                Parent root = loader.load();

                Label user = (Label) root.lookup(".user");
                user.setText(username);

                ScrollPane scroll = (ScrollPane) root.lookup("#TaskScroll");
                VBox Vbox = new VBox();
                scroll.setStyle(
                        "-fx-background-color: #FCFCFC; -fx-background: transparent; -fx-border-color: transparent;");
                Vbox.setStyle("-fx-background-color: #FCFCFC; -fx-padding: 10 10 10 20;-fx-spacing: 10px;");

                // Get all the tasks from the database
                while (rs.next()) {
                    // Store the check column in a hashmap
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
                        check.setImage(new Image(UpdateUI.class.getResource("/com/assets/Check.png").toExternalForm()));
                        rootThumb.getStyleClass().add("checked");
                    } else {
                        check.setImage(new Image(UpdateUI.class.getResource("/com/assets/Uncheck.png").toExternalForm()));
                        rootThumb.getStyleClass().add("unchecked");
                    }

                    thumb.setImage(new Image(UpdateUI.class.getResource("/com/assets/Thumb.png").toExternalForm()));


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

                    // Add the task to the UI

                    rootThumb.getChildren().addAll(data, checkBox, DeleteBox);
                    Vbox.getChildren().add(rootThumb);
                }

                scroll.setOnMouseClicked(e -> {
                    // if the user double clicks on the task, they can edit the task description and
                    // date

                    if (e.getClickCount() == 2) {
                        Node clickedNode = e.getPickResult().getIntersectedNode();

                        if (clickedNode instanceof VBox) {
                            VBox rootThumb = (VBox) clickedNode;
                            String id = clickedNode.getId();

                            System.out.println(id);

                            Label TASK = (Label) rootThumb.getChildren().get(0);
                            Label DESCRIPTION = (Label) rootThumb.getChildren().get(1);
                            Label TIME = (Label) rootThumb.getChildren().get(2);

                            Dialog<String> dialog = new Dialog<>();
                            dialog.setTitle("Edit Task");
                            dialog.setHeaderText("Edit Task Description and Date");

                            // Set the button types
                            ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
                            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                            // Create the labels and text fields
                            Label taskLabel = new Label("Task:");
                            TextField TaskTextField = new TextField();
                            Label descriptionLabel = new Label("Description:");
                            TextField descriptionTextField = new TextField();
                            Label dateLabel = new Label("Date:");
                            DatePicker datePicker = new DatePicker();

                            // Set the initial values
                            TaskTextField.setText(TASK.getText());
                            descriptionTextField.setText(DESCRIPTION.getText());
                            datePicker.setValue(LocalDate.parse(TIME.getText()));

                            // Enable/disable the save button based on the input
                            Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
                            saveButton.setDisable(true);
                            descriptionTextField.textProperty().addListener((_, _, newValue) -> {
                                saveButton.setDisable(newValue.trim().isEmpty());
                            });

                            // Create the layout
                            GridPane grid = new GridPane();
                            grid.setHgap(10);
                            grid.setVgap(10);
                            grid.setPadding(new Insets(20, 150, 10, 10));
                            grid.add(taskLabel, 0, 0);
                            grid.add(TaskTextField, 1, 0);
                            grid.add(descriptionLabel, 0, 1);
                            grid.add(descriptionTextField, 1, 2);
                            grid.add(dateLabel, 0, 1);

                            // Style the dialog box
                            dialog.getDialogPane().getStyleClass().add("dialog-pane");
                            dialog.getDialogPane().setPrefSize(400, 300);
                            dialog.getDialogPane().setMinSize(400, 300);
                            dialog.getDialogPane().setMaxSize(400, 300);
                            dialog.getDialogPane().setPadding(new Insets(10));
                            dialog.getDialogPane().setBorder(new Border(
                                    new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                                            new BorderWidths(1))));
                            dialog.getDialogPane().setBackground(
                                    new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

                            // Style the labels and text fields
                            taskLabel.getStyleClass().add("label");
                            descriptionLabel.getStyleClass().add("label");
                            dateLabel.getStyleClass().add("label");
                            TaskTextField.getStyleClass().add("text-field");
                            descriptionTextField.getStyleClass().add("text-field");
                            datePicker.getStyleClass().add("date-picker");
                            grid.add(datePicker, 1, 1);

                            dialog.getDialogPane().setContent(grid);

                            // Request focus on the description text field by default
                            Platform.runLater(() -> descriptionTextField.requestFocus());

                            // Convert the result to a string when the save button is clicked
                            dialog.setResultConverter(dialogButton -> {
                                if (dialogButton == saveButtonType) {
                                    return descriptionTextField.getText() + " - " + datePicker.getValue().toString();
                                }
                                return null;
                            });

                            // Show the dialog and wait for the user's response
                            Optional<String> result = dialog.showAndWait();

                            // Update the task description and date if the user clicked save
                            result.ifPresent(descriptionDate -> {
                                String task = TaskTextField.getText();
                                String[] parts = descriptionDate.split(" - ");

                                String newTask = task;
                                String newDescription = parts[0];
                                String newDate = parts[1];

                                String oldTask = TASK.getText();
                                String oldDescription = DESCRIPTION.getText();
                                String oldDate = TIME.getText();

                                // Update the task description and date in the database
                                DButils.UpdateTask(username, newTask, newDescription, newDate, oldTask, oldDescription,
                                        oldDate);

                                // Update the UI with the new task description and date
                                TASK.setText(newTask);
                                DESCRIPTION.setText(newDescription);
                                TIME.setText(newDate);
                            });
                        }
                    } else {
                        // If the user clicks on the checkbox, update the check column in the database

                        Node clickedNode = e.getPickResult().getIntersectedNode();
                        if (clickedNode instanceof HBox) {
                            String id = clickedNode.getId();

                            if (id.contains("checkBox")) {
                                HBox checkBox = (HBox) clickedNode;
                                ImageView check = (ImageView) checkBox.getChildren().get(0);
                                Node parent = check.getParent().getParent();

                                if (check.getImage().getUrl().contains("Check.png")) {
                                    DButils.updateCheckColumn(null, Integer.parseInt(check.getId().split(" ")[1]));
                                    getAllCheck.put(check.getId().split(" ")[1], "1");
                                    parent.getStyleClass().remove("checked");
                                    parent.getStyleClass().add("unchecked");
                                    check.setImage(new Image(UpdateUI.class.getResource("/com/assets/Uncheck.png").toExternalForm()));
                                } else {
                                    DButils.updateCheckColumn("1", Integer.parseInt(check.getId().split(" ")[1]));
                                    getAllCheck.put(check.getId().split(" ")[1], null);
                                    parent.getStyleClass().remove("unchecked");
                                    parent.getStyleClass().add("checked");
                                    check.setImage(new Image(UpdateUI.class.getResource("/com/assets/Check.png").toExternalForm()));
                                }
                            } else if (id.contains("DeleteBox")) {
                                HBox DeleteBox = (HBox) clickedNode;
                                ImageView thumb = (ImageView) DeleteBox.getChildren().get(0);

                                DButils.DeleteTask(username, Integer.parseInt(thumb.getId().split(" ")[1]));
                                Vbox.getChildren().remove(clickedNode.getParent());
                            }
                        }

                        if (Vbox.getChildren().isEmpty()) {
                            scroll.setVisible(false);
                        }
                    }
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
                // If the user has no tasks, show the add item screen

                FXMLLoader loader = new FXMLLoader(DButils.class.getResource("/com/example/AddItem.fxml"));
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

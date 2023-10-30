package Javafx.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import Javafx.DButils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class TaskController implements Initializable {

    @FXML
    private JFXButton SaveBtn;

    @FXML
    private TextField TaskText;

    @FXML
    private TextField description;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField time;

    @FXML
    private Label UserTask;

    @Override
    public void initialize(URL loaction, ResourceBundle resources) {
        SaveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = ((Label) root.lookup(".UserTask")).getText();
                UserTask.setText(username);
                DButils.SaveToDB(event, username, TaskText.getText(), description.getText(), time.getText());
                DButils.UpdateAddItemFXML(event, username);
            }
        });
    }
}

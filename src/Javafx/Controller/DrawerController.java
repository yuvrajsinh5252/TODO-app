package Javafx.Controller;

import com.jfoenix.controls.JFXButton;

import Javafx.DButils;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class DrawerController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton logout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.logout.setOnAction(ActionEvent -> {
            DButils.changeScene(ActionEvent, "view/Interface.fxml", "Logout", null);
        });
    }

}

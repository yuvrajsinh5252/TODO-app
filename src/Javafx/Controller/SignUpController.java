package Javafx.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import Javafx.DButils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class SignUpController implements Initializable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView Close;

    @FXML
    private Button LoginButton;

    @FXML
    private Button SignupButton;

    @FXML
    private PasswordField SignupPassword;

    @FXML
    private TextField SignupUsername;

    @FXML
    private PasswordField signupConf;

    @Override
    public void initialize(URL loaction, ResourceBundle resources) {
        this.SignupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!SignupUsername.getText().trim().isEmpty() 
                &&  !SignupPassword.getText().trim().isEmpty()
                ) {
                    if (SignupPassword.getText().equals(signupConf.getText())) {
                        DButils.SignUpUser(event, SignupUsername.getText(), SignupPassword.getText());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Passwords do not match");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Empty Fields");
                    alert.showAndWait();
                }
                
            }
        });

        this.LoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DButils.changeScene(event, "View/Interface.fxml", "Login", null);
            }
        });

        Close.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }
    
}

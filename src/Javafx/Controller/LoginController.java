package Javafx.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView Close;

    @FXML
    private Button LoginButton;

    @FXML
    private PasswordField LoginPassword;

    @FXML
    private TextField LoginUsername;

    @FXML
    void initialize() {
        LoginButton.setOnAction(e -> {
            System.out.println("Username: " + LoginUsername.getText());
            System.out.println("Password: " + LoginPassword.getText());     
        });
    }
}

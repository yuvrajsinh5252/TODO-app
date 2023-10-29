package Javafx.Controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AddItemController implements Initializable {
    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
        });
    }

}

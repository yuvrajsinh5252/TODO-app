package Javafx.Controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import Javafx.DButils;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class AddItemController implements Initializable {
    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private ImageView Additems;

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView EmptyTaskImage;

    @FXML
    private ScrollPane TaskScroll;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
        });

        Additems.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (e) -> {
            System.out.println("Add items");
            EmptyTaskImage.setVisible(false);
            try {
                
                AnchorPane pane = FXMLLoader.load(getClass().getResource("../view/Task.fxml"));
                FadeTransition rootTransition = new FadeTransition(Duration.millis(1000), pane);
                
                Label user = (Label) pane.lookup(".UserTask");
                user.setText(((Label) root.lookup(".user")).getText());
               
                rootTransition.setFromValue(0.0);
                rootTransition.setToValue(1.0);
                rootTransition.setCycleCount(1);
                rootTransition.setAutoReverse(true);
                rootTransition.play();
                root.getChildren().setAll(pane);

            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
    }
}

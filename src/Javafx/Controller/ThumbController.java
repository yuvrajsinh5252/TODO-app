package Javafx.Controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ThumbController {
    @FXML
    private Label description;

    @FXML
    private Label task;

    @FXML
    private Label time;

    @FXML
    private VBox vbox;

    public void setData(String task, String description, String time) {
        this.task.setText(task);
        this.description.setText(description);
        this.time.setText(time);
    }
}

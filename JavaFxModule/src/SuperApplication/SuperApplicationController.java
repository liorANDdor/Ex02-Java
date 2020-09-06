package SuperApplication;


import SDMModel.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;


public class SuperApplicationController {

    SystemManager sys = new SystemManager();

    @FXML
    private FlowPane myPane;

    @FXML
    void showItems(ActionEvent event) {
        myPane.getChildren().add(new)
    }
}

package SuperApplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SuperApplicationController {

    @FXML
    private FlowPane myPane;

    @FXML
    void addSaleHandler(ActionEvent event) {

    }

    @FXML
    void deleteItemFromStoreHandler(ActionEvent event) {

    }

    @FXML
    void loadXMLHandler(ActionEvent event) throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../OrderWindow/Order.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());

        Scene scene = new Scene(root, 200, 200);

        stg.setTitle("asd");
        stg.setScene(scene);
        stg.showAndWait();
    }

    @FXML
    void showCustomersHandler(ActionEvent event) {

    }

    @FXML
    void showItemsHandler(ActionEvent event) {

    }

    @FXML
    void showMapHandler(ActionEvent event) {

    }

    @FXML
    void showOrdersHandler(ActionEvent event) {

    }

    @FXML
    void showStoresHandler(ActionEvent event) {

    }

}

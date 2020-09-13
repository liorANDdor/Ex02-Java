package UpdateStoreItemsView;

import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UpdateStoreItemsController {

    private SystemManager systemManager = SystemManager.getInstance();

    @FXML
    private ComboBox<Store> storeCB;

    @FXML
    private ComboBox<String> optionCB;

    @FXML
    private Button commitBtn;

    @FXML
    private ComboBox<?> ItemsCB;

    @FXML
    private TextField priceText;

    @FXML
    private void initialize(){
        optionCB.getItems().add("Delete item");
        optionCB.getItems().add("Chane price of item");
        optionCB.getItems().add("Add new item");
        systemManager.getSuperMarket().getStores();
        storeCB.getItems().addAll(systemManager
                .getSuperMarket()
                .getStores()
                .values());
    }

    @FXML
    void commitChangesHandler(ActionEvent event) {

    }

}

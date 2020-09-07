package StoreView;

import SDMModel.Store;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;

import java.awt.*;
import java.util.HashMap;

public class StoreTileController {

    @FXML
    private Label LocationLable;
    @FXML private Label PPKLabel;
    @FXML private Label STELabel;
    @FXML private ChoiceBox<String> storeView = new ChoiceBox<>();;
    @FXML private Label ID;
    @FXML private Button itemsBtn;
    @FXML private Button ordersBtn;


    private SimpleStringProperty location = new SimpleStringProperty("");
    private SimpleStringProperty ppk = new SimpleStringProperty("");
    private SimpleStringProperty ste = new SimpleStringProperty("");
    private SimpleStringProperty id = new SimpleStringProperty("");



    @FXML public void initialize(HashMap<Integer, Store> stores) {
        HashMap<Integer, Store> storesInChoiceBox = new HashMap<>();
        Integer index = 0;
        for(Store store:stores.values()){
            storeView.getItems().add(store.getName());
            storesInChoiceBox.put(index, store);
            index++;
        }

        LocationLable.textProperty().bind(Bindings.format("Location: %s", location));
        PPKLabel.textProperty().bind(Bindings.format("PPK: %s", ppk));
        STELabel.textProperty().bind(Bindings.format("Shipment Earning: %s", ste));
        ID.textProperty().bind(Bindings.format("Store Id: %s", id));
        storeView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Store storeToShow = storesInChoiceBox.get(newValue);
                id.set(Integer.toString(storeToShow.getId()));
                ppk.set(Integer.toString(storeToShow.getDeliveryPpk()));
               location.set("X:" +Integer.toString(storeToShow.getLocation().x) +" Y:" +Integer.toString(storeToShow.getLocation().y) );

            }
        });
      }


    public void setLocation(String Location){
        location.set(Location);
    }
    public void setSte(String STE){
        ste.set(STE);
    }
    public void setId(String ID){
        id.set(ID);
    }
    public void setPPK(String PPK){
        ppk.set(PPK);
    }
}

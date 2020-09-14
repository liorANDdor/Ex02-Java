package AddStore;

import SDMModel.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AddItemToStoreController {

    @FXML private Button addItemBtn;
    @FXML private ComboBox<Item> ItemsCB;

    @FXML private Button finishBtn;
    @FXML private TextField priceTxt;

    private Store store;
    private SystemManager systemManager = SystemManager.getInstance();
    @FXML void commitChangesHandler(ActionEvent event) {
        if (store.getItemsToSell().size() == 0){

        }
        else {
            systemManager.getSuperMarket().getStores().put(store.getId(), store);
        }

    }

    @FXML void addItem() {
        if(priceTxt.getText()!="" || priceTxt.getText()!="Price" ) {
            Item item = ItemsCB.getSelectionModel().getSelectedItem();
            for(Sell sell : store.getItemsToSell()) {
                if (sell.getItemId() == item.getId()) {
                    store.getItemsToSell().remove(sell);
                }
            }
            store.getItemsToSell().add(new Sell(item.getId(), Double.parseDouble(priceTxt.getText())));
        }
    }

    @FXML void initialize(){
        priceTxt.setOnMouseClicked(x->priceTxt.setText(""));
        priceTxt.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    priceTxt.setText(oldValue);
                }
                addItemBtn.setDisable(false);
                finishBtn.setDisable(false);
                if(newValue.equals("")){
                    addItemBtn.setDisable(true);
                    finishBtn.setDisable(true);
                }
            }
        });
        initItems(systemManager.getSuperMarket().getItems());

    }

    @FXML void initItems(HashMap<Integer, Item> items) {
        ItemsCB.getItems().addAll(systemManager.getSuperMarket().getItems().values().stream()
                .collect(Collectors.toList()));
        ItemsCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                priceTxt.setDisable(false);

            }
        });


    }
    @FXML void cleanTxt(){
        priceTxt.setText("");
    }

    public void setStore(Store store) {
        this.store=store;
    }
}

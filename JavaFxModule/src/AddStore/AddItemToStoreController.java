package AddStore;

import SDMModel.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AddItemToStoreController {

    @FXML private Button addItemBtn;
    @FXML private ComboBox<Item> ItemsCB;

    @FXML private Button finishBtn;
    @FXML private TextField priceTxt;
    private AddStoreController storeController;
    private Store store;
    private SystemManager systemManager = SystemManager.getInstance();
    @FXML void commitChangesHandler(ActionEvent event) {
        if (store.getItemsToSell().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Issue");
            alert.setContentText("Please make sure at least one item was added");
            alert.showAndWait();
        } else {
            systemManager.getSuperMarket().getStores().put(store.getId(), store);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Store Created!");
            alert.setContentText("New Store Was Added");
            alert.showAndWait();
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        }

    }



    @FXML void addItem() {
        if(!priceTxt.getText().equals("") && !priceTxt.getText().equals("Price") && !priceTxt.getText().equals("0")) {
            Item item = ItemsCB.getSelectionModel().getSelectedItem();
            int sellIndex = 0;
            List<Sell> filteredSells = new ArrayList<Sell>();
            List<Sell> newList = new ArrayList<Sell>(store.getItemsToSell());
            for(Sell sell : newList) {
                if (sell.getItemId() == item.getId()) {
                    store.getItemsToSell().remove(sellIndex);
                }
                sellIndex++;
            }
            store.getItemsToSell().add(new Sell(item.getId(), Double.parseDouble(priceTxt.getText())));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Item");
            alert.setContentText("Item Was Added");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Issue");
                alert.setContentText("Please make sure positive price was added");
                alert.showAndWait();

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

    public void setParent(AddStoreController addStoreController) {
        storeController=addStoreController;
    }
}

package AddItem;

import SDMModel.Item;
import SDMModel.Sell;
import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;

public class AddItemToSupermarketController {

    @FXML
    private Button addToStoreBtn;

    @FXML
    private Button finishBtn;

    @FXML
    private TextField priceTxt;
    @FXML private Button nextBtn;
    @FXML private ComboBox<String> storeCB;
    private HashMap<Integer, Store> storesBox = new HashMap<>();
    @FXML private TextField nameTxt;
    @FXML private RadioButton weightRb;
    @FXML private RadioButton quantityRB;
    @FXML private TextField IdTxt;
    private ToggleGroup tg = new ToggleGroup();
    private SystemManager systemManager = SystemManager.getInstance();
    private Item itemToAdd;
    @FXML
    void addItem(ActionEvent event) {
        if(nameTxt.getText().equals("") || nameTxt.getText().equals("Name")
        || IdTxt.getText().equals("") || IdTxt.getText().equals("ID")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("New Item");
            alert.setContentText("Make sure to set ID and name");
            alert.showAndWait();
        }
        else if(systemManager.getSuperMarket().getItems().containsKey(Integer.parseInt(IdTxt.getText())))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("New Item");
            alert.setContentText("Another item has this ID");
            alert.showAndWait();
        }
        else{
            RadioButton selectedRadioButton = (RadioButton) tg.getSelectedToggle();
            Item.PurchaseCategory category = selectedRadioButton.getText() == "Weight" ? Item.PurchaseCategory.WEIGHT : Item.PurchaseCategory.QUANTITY;
            Item item = new Item(nameTxt.getText(), Integer.parseInt(IdTxt.getText()), category);
            //check if Item valid

            itemToAdd = item;
            IdTxt.setDisable(true);
            nameTxt.setDisable(true);
            weightRb.setDisable(true);
            quantityRB.setDisable(true);
            storeCB.setDisable(false);

        }

    }

    @FXML void addItemToStore() {
        Store store = storesBox.get(storeCB.getSelectionModel().getSelectedIndex());
        for (Sell sell : store.getItemsToSell()) {
            if (sell.getItemId() == itemToAdd.getId()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("New Item");
                alert.setContentText("Store already sell the item");
                alert.showAndWait();
                return;
            }
        }
        if(Double.parseDouble(priceTxt.getText()) == 0.0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("New Item");
            alert.setContentText("Item price cannot be 0");
            alert.showAndWait();
            return;
        }

        if (!systemManager.getSuperMarket().getItems().containsKey(itemToAdd.getId()))
            systemManager.getSuperMarket().getItems().put(itemToAdd.getId(), itemToAdd);

        store.getItemsToSell().add(new Sell(itemToAdd.getId(), Double.parseDouble(priceTxt.getText())));
        itemToAdd.getStoresWhoSellTheItem().add(store);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Item");
        alert.setContentText("Item was added to store, choose more stores or press finish");
        alert.showAndWait();
        finishBtn.setDisable(false);

    }

    @FXML private void storeChosen(){
        priceTxt.setDisable(false);
    }

    @FXML void initialize(){
        initStores(systemManager.getSuperMarket().getStores());
        priceTxt.setOnMouseClicked(x->priceTxt.setText(""));
        nameTxt.setOnMouseClicked(x->nameTxt.setText(""));
        IdTxt.setOnMouseClicked(x->IdTxt.setText(""));
        quantityRB.setToggleGroup(tg);
        weightRb.setToggleGroup(tg);

        IdTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[1-50]")) {
                    IdTxt.setText(newValue.replaceAll("[^\\d]", ""));
                }

                if(newValue.equals("")){
                    addToStoreBtn.setDisable(true);
                    finishBtn.setDisable(true);
                    //nextBtn.setDisable(true);
                }}
        });

        nameTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {


                if(newValue.equals("")){
                    addToStoreBtn.setDisable(true);
                    finishBtn.setDisable(true);
                }
                else{
                    weightRb.setDisable(false);
                    //nextBtn.setDisable(true);
                    quantityRB.setDisable(false);
                }

            }
        });
        tg.selectedToggleProperty().addListener(x->nextBtn.setDisable(false));
        priceTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    priceTxt.setText(oldValue);
                }
                if(newValue.equals("")){
                    addToStoreBtn.setDisable(true);
                    finishBtn.setDisable(true);
                }
                addToStoreBtn.setDisable(false);
            }
        });

    }

    private void initStores(HashMap<Integer, Store> stores) {
        Integer storeIndex = 0;
        for (Store store : stores.values()) {
            storeCB.getItems().add(store.getName());
            storeCB.setPromptText("Choose store");
            storesBox.put(storeIndex,store);
            storeIndex++;
        }
    }

    @FXML
    void commitChangesHandler(ActionEvent event) {
        if(itemToAdd.getStoresWhoSellTheItem().size()!=0)
        {
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("New Item");
            alert.setContentText("At least one store must sell the item");
            alert.showAndWait();
        }
    }

}

package AddSale;

import SDMModel.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class AddSaleController {

    @FXML private ComboBox<Store> storeCB;
    @FXML private ComboBox<SystemManager.optionsForSale> optionCB;
    @FXML private Button commitBtn;
    @FXML private ComboBox<Item> ItemsCB;
    @FXML private Spinner quantityTF = new Spinner();
    @FXML private ComboBox<Item> whatYouGetCB;
    @FXML private TextField forAdditionTv;
    @FXML private Button addOptionBtn;
    @FXML private Button NextBtn;
    @FXML private Spinner quantitySpinner = new Spinner();
    @FXML private TextField discountNameTf;

    private Sale sale;
    private SystemManager systemManager = SystemManager.getInstance();

    @FXML private void initialize() {
        discountNameTf.textProperty().addListener(x->storeCB.setDisable(false));
        optionCB.getItems().addAll(SystemManager.optionsForSale.values());
        optionCB.getSelectionModel().selectedItemProperty().addListener(x->NextBtn.setDisable(false));



        systemManager.getSuperMarket().getStores();
        storeCB.getItems().addAll(systemManager
                .getSuperMarket()
                .getStores()
                .values());

        storeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            ItemsCB.setDisable(false);
            ItemsCB.getSelectionModel().clearSelection();
            whatYouGetCB.getItems().clear();
            whatYouGetCB.setDisable(true);
            Store store = storeCB.getSelectionModel().getSelectedItem();
            ItemsCB.getItems().addAll(systemManager.getSuperMarket().getItems().values().stream()
                    .filter(item -> store.getItemsToSell().stream().anyMatch(el -> el.getItemId() == item.getId()))
                    .collect(Collectors.toList()));
            whatYouGetCB.getItems().addAll(systemManager.getSuperMarket().getItems().values().stream()
                    .filter(item -> store.getItemsToSell().stream().anyMatch(el -> el.getItemId() == item.getId()))
                    .collect(Collectors.toList()));
        });
        ItemsCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)-> {
                    optionCB.setDisable(false);
                    quantitySpinner.setDisable(false);
                    if (ItemsCB.getSelectionModel().getSelectedItem().getPurchaseCategory() == Item.PurchaseCategory.QUANTITY) {
                        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 500, 0, 1);
                        quantitySpinner.setValueFactory(valueFactory);
                    }
                    else {
                        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 500, 0, 0.1);
                        quantitySpinner.setValueFactory(valueFactory);
                    }
                }

        );
        forAdditionTv.setOnMouseClicked(x->forAdditionTv.setText(""));
        discountNameTf.setOnMouseClicked(x->forAdditionTv.setText(""));
        whatYouGetCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)-> {
            addOptionBtn.setDisable(false);
            addOptionBtn.setVisible(true);
                    if (whatYouGetCB.getSelectionModel().getSelectedItem().getPurchaseCategory() == Item.PurchaseCategory.QUANTITY) {
                        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 500, 0, 1);
                        quantityTF.setValueFactory(valueFactory);
                    }
                    else {
                        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 500, 0, 0.1);
                        quantityTF.setValueFactory(valueFactory);
                    }
                }
        );
        forAdditionTv.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    forAdditionTv.setText(oldValue);
                }
            }
        });

    }

    @FXML void addWhatYouGet(){
        if(forAdditionTv.getText().equals("") || forAdditionTv.getText().equals("Price") ||
        quantityTF.getValue().equals(0.0))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Sale");
            alert.setContentText("make sure price and quantity set");
            alert.showAndWait();
            return;
        }
        commitBtn.setDisable(false);
        commitBtn.setVisible(true);
        sale.getNeedToGet().getOffers().add(new Offer(whatYouGetCB.getSelectionModel().getSelectedItem().getId(), Double.parseDouble(quantityTF.getValue().toString()),
                Integer.parseInt(forAdditionTv.getText())));

    }
    @FXML void goToItemsDecision(){
        if(quantitySpinner.getValue().equals(0.0)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Sale");
            alert.setContentText("Quantity cannot be zero");
            alert.showAndWait();
            return;
        }
        else if( discountNameTf.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Sale");
            alert.setContentText("Insert discount name");
            alert.showAndWait();
            return;
        }
        else {
            discountNameTf.setDisable(true);
            storeCB.setDisable(true);
            ItemsCB.setDisable(true);
            optionCB.setDisable(true);
            NextBtn.setDisable(true);
            NextBtn.setVisible(false);
            quantitySpinner.setDisable(true);

            forAdditionTv.setVisible(true);
            whatYouGetCB.setVisible(true);
            quantityTF.setVisible(true);
            forAdditionTv.setVisible(true);
            forAdditionTv.setVisible(true);
            whatYouGetCB.setDisable(false);
            Store store = storeCB.getSelectionModel().getSelectedItem();
            Item item = ItemsCB.getSelectionModel().getSelectedItem();
            if (optionCB.getSelectionModel().getSelectedItem().toString().equals("ONE-OF"))
                sale = new Sale(discountNameTf.getText(), storeCB.getSelectionModel().getSelectedItem(), "ONE-OF", item, Double.parseDouble(quantitySpinner.getValue().toString()));
            else
                sale = new Sale(discountNameTf.getText(), storeCB.getSelectionModel().getSelectedItem(), "ALL-OR-NOTHING", item, Double.parseDouble(quantitySpinner.getValue().toString()));
        }

    }


    @FXML
    void commitChangesHandler(ActionEvent event) {
        if(sale.getNeedToGet().getSdmOffer().size()!=0)
        {
            SystemManager.addSale(sale);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Sale");
            alert.setContentText("Sale Was Added!");
            alert.showAndWait();
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

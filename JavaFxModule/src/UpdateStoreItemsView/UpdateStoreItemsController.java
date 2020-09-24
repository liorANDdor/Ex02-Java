package UpdateStoreItemsView;

import SDMModel.Item;
import SDMModel.Sale;
import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateStoreItemsController {



    private SystemManager systemManager = SystemManager.getInstance();

    @FXML
    private ComboBox<Store> storeCB;

    @FXML
    private ComboBox<SystemManager.optionsForUpdate> optionCB;

    @FXML
    private Button commitBtn;

    @FXML
    private ComboBox<Item> ItemsCB;

    @FXML
    private TextField priceText;

    @FXML
    private void initialize() {
        optionCB.getItems().addAll(SystemManager.optionsForUpdate.values());
        systemManager.getSuperMarket().getStores();
        storeCB.getItems().addAll(systemManager
                .getSuperMarket()
                .getStores()
                .values());

        storeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            optionCB.setDisable(false);
            optionCB.getSelectionModel().clearSelection();
            ItemsCB.getItems().clear();
        });
        optionCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ItemsCB.setDisable(false);
            ItemsCB.getItems().clear();
            priceText.visibleProperty().setValue(false);
            commitBtn.disableProperty().setValue(true);
            if(newValue!=null)
            switch (newValue){
                case AddNewItem:
                    ItemsCB.setPromptText("Choose item");
                    ItemsCB.getItems().addAll(systemManager.getSuperMarket().getItems().values().stream()
                            .filter(item -> storeCB.getSelectionModel().getSelectedItem().getItemsToSell()
                                    .stream()
                                    .noneMatch(el -> el.getItemId() == item.getId()))
                            .collect(Collectors.toList()));
                    priceText.visibleProperty().setValue(true);
                    break;
                case DeleteItem:
                    List<Item> lst = systemManager.getItemsThatCanBeDeleted(storeCB.getSelectionModel().getSelectedItem());
                    if(lst != null && lst.size() !=1)
                        ItemsCB.getItems().addAll(lst);
                    else{
                        ItemsCB.setPromptText("No items to delete");
                        commitBtn.disableProperty().setValue(false);
                        ItemsCB.setDisable(true);
                    }

                    break;
                case ChangePriceOfItem:
                    ItemsCB.setPromptText("Choose item");
                    ItemsCB.getItems().addAll(systemManager.getSuperMarket().getItems().values().stream()
                            .filter(item -> storeCB.getSelectionModel().getSelectedItem().getItemsToSell()
                                    .stream()
                                    .anyMatch(el -> el.getItemId() == item.getId()))
                            .collect(Collectors.toList()));
                    priceText.visibleProperty().setValue(true);


            }
            else
                ItemsCB.setDisable(true);
        });
        ItemsCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            commitBtn.disableProperty().setValue(false);
        }
        );


    }


    @FXML
    void commitChangesHandler(ActionEvent event) {
        double price = 0;
        boolean correctPrice = true;
        if (!optionCB.getSelectionModel().getSelectedItem().equals(SystemManager.optionsForUpdate.DeleteItem))
            try {
                price = Double.parseDouble(priceText.getText());
                if(price == 0)
                {
                    priceText.setText("Price cannot be 0");
                    correctPrice = false;
                }
                else
                    correctPrice = true;
            } catch (Exception e) {
                priceText.setText("Must be a number");
                correctPrice = false;
            }
        if (correctPrice) {
            List<Sale> saleDeleted = SystemManager.changeValueOfItem(storeCB.getSelectionModel().getSelectedItem()
                    , ItemsCB.getSelectionModel().getSelectedItem()
                    , optionCB.getSelectionModel().getSelectedItem()
                    , price);
            if(saleDeleted.size()!=0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                String SalesRemovedInfo = "Sales which were removed:\n";
                for(Sale sale: saleDeleted) {
                    SalesRemovedInfo +=sale.getName() + "\n" ;
                }
                alert.setContentText(SalesRemovedInfo);
                alert.showAndWait();
            }
            //((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }
    }

}

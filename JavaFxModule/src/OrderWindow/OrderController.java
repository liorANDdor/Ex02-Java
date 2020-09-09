package OrderWindow;

import SDMModel.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;

import javax.security.auth.callback.Callback;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderController {

    @FXML private Label ID;
    @FXML private Label shipmentLabel;
    @FXML private ComboBox<String> clientCB;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> storeCB;
    @FXML private CheckBox dynamicBtn;
    @FXML private FlowPane itemsFlowPan;
    @FXML private TableView<ItemSetterGetter> itemsTableView;


    TableColumn NameCol;
    TableColumn IdCol;
    TableColumn TypeCol;
    TableColumn QuantityCol ;
    TableColumn PriceCol;
    TableColumn purchasesCol;
    Order order = new Order();
    HashMap<Integer,Store> storesBox = new HashMap<>();
    @FXML
    public void initialize(SystemManager sys) {

        HashMap<Integer, Customer> customers = sys.getSuperMarket().getCostumers();
         NameCol = new TableColumn("Name");
         IdCol = new TableColumn("ID");
         TypeCol = new TableColumn("Purchase Category");
         QuantityCol = new TableColumn("Quantity");
         PriceCol = new TableColumn("Price");
         purchasesCol = new TableColumn("Purchased");

        purchasesCol.setCellValueFactory(new PropertyValueFactory<>("addButton"));
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantitySpinner"));

        NameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("purchaseCategory"));
        //QuantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        itemsTableView.getColumns().addAll(IdCol, NameCol, QuantityCol, PriceCol, purchasesCol);
        int clientIndex = 0;
        for (Customer customer : customers.values()) {
            clientCB.getItems().add(customer.getName());
            clientIndex++;
        }
        clientCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                return;
            }
        });

        initStores(sys.getSuperMarket().getStores(), sys);
        Store store = storesBox.get(storeCB.getSelectionModel().getSelectedIndex());
        dynamicBtn.selectedProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue.equals(true)) {
                    //itemsTableView.getColumns(3).clear();
                    storeCB.getItems().clear();
                    storesBox.clear();
                    itemsTableView.getColumns().clear();
                    itemsTableView.getColumns().addAll(IdCol, NameCol, QuantityCol, purchasesCol);
                    List data = new ArrayList<>();
                    for (Item item : sys.getSuperMarket().getItems().values()) {
                        data.add(
                                new ItemSetterGetter(Double.toString(sys.getItemLowestPrice(item.getId()).getItemPrice(item.getId())), //itemLowestPrice
                                        item.getName(), Integer.toString(item.getId()), item.getPurchaseCategory().toString(), order, sys));
                    }
                    itemsTableView.setItems(FXCollections.observableList(data));
                }
                else {
                    initStores(sys.getSuperMarket().getStores(), sys);
                    itemsTableView.getItems().clear();
                    itemsTableView.getColumns().clear();
                    itemsTableView.getColumns().addAll(IdCol, NameCol, QuantityCol, PriceCol, purchasesCol);
                }
            }
        });
    }

    private void initStores(HashMap<Integer, Store> stores, SystemManager sys ) {
        int storeIndex = 0;
        HashMap<Integer,Item> items = sys.getSuperMarket().getItems();
        for (Store store : stores.values()) {
            storeCB.getItems().add(store.getName());
            storesBox.put(storeIndex,store);
            storeIndex++;
        }

            storeCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (!dynamicBtn.isSelected()) {
                        Store store = storesBox.get(newValue);
                        List data = new ArrayList<>();
                        for (Sell sell : store.getItemsToSell()) {
                            Item item = sys.getSuperMarket().getItemByID(sell.getItemId());
                            data.add(
                                    new ItemSetterGetter(Double.toString(sell.getPrice()),
                                            item.getName(), Integer.toString(sell.getItemId()), item.getPurchaseCategory().toString(), order, sys));
                        }
                        itemsTableView.setItems(FXCollections.observableList(data));
                    }
                }
            });

    }

}


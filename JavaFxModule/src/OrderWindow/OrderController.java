package OrderWindow;

import SDMModel.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderController {

    @FXML private Label ID;
    @FXML private Label shipmentLabel;
    @FXML private ComboBox<String> customerCB;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> storeCB;
    @FXML private CheckBox dynamicBtn;
    @FXML private FlowPane itemsFlowPan;
    @FXML private TableView<ItemSetterGetter> itemsTableView;

    SimpleStringProperty shipmentPrice = new SimpleStringProperty("Shipment Price:");
    TableColumn NameCol;
    TableColumn IdCol;
    TableColumn TypeCol;
    TableColumn QuantityCol ;
    TableColumn PriceCol;
    TableColumn purchasesCol;
    TableColumn totalQuantity;
    TableColumn totalPrice;
    Order order = new Order();
    HashMap<Integer,Store> storesBox = new HashMap<>();
    HashMap<Integer,Customer> customerBox= new HashMap<>();
    @FXML
    public void initialize(SystemManager sys) {

        HashMap<Integer, Customer> customers = sys.getSuperMarket().getCostumers();
         NameCol = new TableColumn("Name");
         IdCol = new TableColumn("ID");
         TypeCol = new TableColumn("Purchase Category");
         QuantityCol = new TableColumn("Quantity");
         PriceCol = new TableColumn("Price");
        purchasesCol = new TableColumn("Add To Order");
        totalQuantity = new TableColumn("Your Quantity");
        totalPrice = new TableColumn("Total Price");

        shipmentLabel.textProperty().bind(Bindings.format("Shipment Price: %s" , shipmentPrice));
        purchasesCol.setCellValueFactory(new PropertyValueFactory<>("addButton"));
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantitySpinner"));

        NameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("purchaseCategory"));
        totalQuantity.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        itemsTableView.getColumns().addAll(IdCol, NameCol, QuantityCol, PriceCol, purchasesCol, totalQuantity,totalPrice);
        int customerIndex = 0;
        for (Customer customer : customers.values()) {
            customerCB.getItems().add(customer.getName());
            customerBox.put(customerIndex, customer);
            customerIndex++;
        }

        customerCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                order.setOrderCustomer(customerBox.get(customerCB.getSelectionModel().getSelectedIndex()));
                itemsTableView.getItems().clear();
                storeCB.getSelectionModel().clearSelection();
                shipmentPrice.set("Shipment Price:");
                return;
            }
        });
        initStores(sys.getSuperMarket().getStores(), sys);
        Store store = storesBox.get(storeCB.getSelectionModel().getSelectedIndex());
        dynamicBtn.selectedProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                order = new Order();
                if (newValue.equals(true)) {
                    //itemsTableView.getColumns(3).clear();
                    storeCB.getItems().clear();
                    storesBox.clear();

                    itemsTableView.getColumns().clear();
                    itemsTableView.getColumns().addAll(IdCol, NameCol, QuantityCol, purchasesCol, totalQuantity,totalPrice);
                    List data = new ArrayList<>();
                    for (Item item : sys.getSuperMarket().getItems().values()) {
                        Store storeLowestItemPrice = sys.getItemLowestPrice(item.getId());
                        data.add(
                                new ItemSetterGetter(Double.toString(sys.getItemLowestPrice(item.getId()).getItemPrice(item.getId())), //itemLowestPrice
                                        item.getName(), Integer.toString(item.getId()), item.getPurchaseCategory().toString(), order, storeLowestItemPrice, sys));
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

        //Store store = storesBox.get(storeCB.getSelectionModel().getSelectedIndex());

            storeCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (!newValue.equals( -1)) {
                        Store store = storesBox.get(newValue);
                        Customer customer = customerBox.get(customerCB.getSelectionModel().getSelectedIndex());
                        double deliveryDistance = Math.sqrt((customer.getLocation().x - store.getLocation().x) * (customer.getLocation().x - store.getLocation().x)
                                + (customer.getLocation().y - store.getLocation().y) * (customer.getLocation().y - store.getLocation().y));
                        shipmentPrice.set(String.format("%.2f", store.getDeliveryPpk() * deliveryDistance));
                        order = new Order();
                        if (!dynamicBtn.isSelected()) {

                            List data = new ArrayList<>();
                            for (Sell sell : store.getItemsToSell()) {
                                Item item = sys.getSuperMarket().getItemByID(sell.getItemId());
                                data.add(
                                        new ItemSetterGetter(Double.toString(sell.getPrice()),
                                                item.getName(), Integer.toString(sell.getItemId()), item.getPurchaseCategory().toString(), order, store, sys));
                            }
                            itemsTableView.setItems(FXCollections.observableList(data));
                        }
                    }
                }
            });

    }

}


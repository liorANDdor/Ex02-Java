package OrderWindow;

import DynamicOrderView.DynamicInfoController;
import SDMModel.*;
import SaleView.SalesController;
import StoreView.StoreTileController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderController {

    @FXML private Label ID;
    @FXML private Label shipmentLabel;
    @FXML private Label totalPriceLabel;
    @FXML private ComboBox<String> customerCB;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> storeCB;
    @FXML private CheckBox dynamicBtn;
    @FXML private FlowPane itemsFlowPan;
    @FXML private TableView<ItemSetterGetter> itemsTableView;
    @FXML private Button commitBtn;
    @FXML private Button orderInfoBtn;
    @FXML private AnchorPane mainAnchor;
    SimpleStringProperty shipmentPrice = new SimpleStringProperty("");
    SystemManager systemManager = SystemManager.getInstance();
    SimpleBooleanProperty isCustomerChosen = new SimpleBooleanProperty(false);
    SimpleBooleanProperty isDateChosen = new SimpleBooleanProperty(false);
    SimpleBooleanProperty isCommitReady = new SimpleBooleanProperty(false);
    SimpleBooleanProperty isDynamic = new SimpleBooleanProperty(false);

    TableColumn NameCol;
    TableColumn IdCol;
    TableColumn TypeCol;
    TableColumn QuantityCol ;
    TableColumn PriceCol;
    TableColumn purchasesCol;
    TableColumn totalQuantity;
    TableColumn totalPrice;
    Order order = new Order();
    HashMap<Integer, Order> subOrders = new HashMap<>();
    HashMap<Integer,Store> storesBox = new HashMap<>();
    HashMap<Integer,Customer> customerBox= new HashMap<>();
    @FXML
    public void initialize(SystemManager sys) {
        //commitBtn.setStyle("-fx-background-color: lightsteelblue");
        HashMap<Integer, Customer> customers = sys.getSuperMarket().getCostumers();
         NameCol = new TableColumn("Name");
         IdCol = new TableColumn("ID");
         TypeCol = new TableColumn("Purchase Category");
         QuantityCol = new TableColumn("Quantity");
         PriceCol = new TableColumn("Price");
        purchasesCol = new TableColumn("Add To Order");
        totalQuantity = new TableColumn("Your Quantity");
        totalPrice = new TableColumn("Total Price");
        datePicker.disableProperty().bind(isCustomerChosen.not());
        storeCB.disableProperty().bind(isDateChosen.not());
        dynamicBtn.disableProperty().bind(isDateChosen.not());
        commitBtn.disableProperty().bind(isCommitReady.not());
        orderInfoBtn.disableProperty().bind(isDynamic.not());
        shipmentLabel.textProperty().bind(Bindings.format("Shipment Price: %s" , shipmentPrice));
        totalPriceLabel.textProperty().bind(Bindings.format("Total Item Price: %s" , ItemSetterGetter.getTotalItemPrice()));
        purchasesCol.setCellValueFactory(new PropertyValueFactory<>("addButton"));
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantitySpinner"));

        NameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("purchaseCategory"));
        totalQuantity.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        datePicker.setOnAction(x -> isDateChosen.set(true));
        itemsTableView.getColumns().addAll(IdCol, NameCol, QuantityCol, PriceCol, purchasesCol, totalQuantity,totalPrice);
        customerCB.setPromptText("Select Customer");
        storeCB.setPromptText("Select Store");
        datePicker.setPromptText("Select Date");

        int customerIndex = 0;
        for (Customer customer : customers.values()) {
            customerCB.getItems().add(customer.getName());
            customerBox.put(customerIndex, customer);
            customerIndex++;
        }

        customerCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                order = new Order();
                ItemSetterGetter.getTotalItemPrice().set("0");
                shipmentPrice.set("0");
                Customer customer = customerBox.get(customerCB.getSelectionModel().getSelectedIndex());
                order.setOrderCustomer(customer);
                if(dynamicBtn.isSelected()==false) {
                    itemsTableView.getItems().clear();
                    storeCB.getSelectionModel().clearSelection();
                    shipmentPrice.set("");
                    isCustomerChosen.set(true);
                }
                else{
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
                                        item.getName(), Integer.toString(item.getId()), item.getPurchaseCategory().toString(), order,subOrders, storeLowestItemPrice, sys));
                    }
                    itemsTableView.setItems(FXCollections.observableList(data));
                }
            }
        });
        initStores(sys.getSuperMarket().getStores(), sys);
        Store store = storesBox.get(storeCB.getSelectionModel().getSelectedIndex());
        dynamicBtn.selectedProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                isCommitReady.set(true);

                ItemSetterGetter.getTotalItemPrice().set("0");
                shipmentPrice.set("0");
                order = new Order();
                subOrders = new HashMap<>();
                Customer customer = customerBox.get(customerCB.getSelectionModel().getSelectedIndex());
                order.setOrderCustomer(customer);
                if (newValue.equals(true)) {
                    storeCB.setPromptText("-----------");
                    isDynamic.set(true);
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
                                        item.getName(), Integer.toString(item.getId()), item.getPurchaseCategory().toString(), order, subOrders, storeLowestItemPrice, sys));
                    }
                    itemsTableView.setItems(FXCollections.observableList(data));
                }
                else {
                    storeCB.setPromptText("Choose Store");
                    isDynamic.set(false);
                    initStores(sys.getSuperMarket().getStores(), sys);
                    itemsTableView.getItems().clear();
                    itemsTableView.getColumns().clear();
                    itemsTableView.getColumns().addAll(IdCol, NameCol, QuantityCol, PriceCol, purchasesCol, totalQuantity,totalPrice);
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
                    isCommitReady.set(true);
                    ItemSetterGetter.getTotalItemPrice().set("0");
                    shipmentPrice.set("0");
                    if (!newValue.equals( -1)) {
                        Store store = storesBox.get(newValue);
                        Customer customer = customerBox.get(customerCB.getSelectionModel().getSelectedIndex());
                        double deliveryDistance = Math.sqrt((customer.getLocation().x - store.getLocation().x) * (customer.getLocation().x - store.getLocation().x)
                                + (customer.getLocation().y - store.getLocation().y) * (customer.getLocation().y - store.getLocation().y));
                        shipmentPrice.set(String.format("%.2f", store.getDeliveryPpk() * deliveryDistance));
                        order = new Order();
                        subOrders = new HashMap<>();
                        order.setOrderCustomer(customer);
                        if (!dynamicBtn.isSelected()) {

                            List data = new ArrayList<>();
                            for (Sell sell : store.getItemsToSell()) {
                                Item item = sys.getSuperMarket().getItemByID(sell.getItemId());
                                data.add(
                                        new ItemSetterGetter(Double.toString(sell.getPrice()),
                                                item.getName(), Integer.toString(sell.getItemId()), item.getPurchaseCategory().toString(), order, subOrders, store, sys));
                            }
                            itemsTableView.setItems(FXCollections.observableList(data));
                        }
                    }
                    else
                    {
                        order = new Order();
                        subOrders = new HashMap<>();
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
                                                item.getName(), Integer.toString(item.getId()), item.getPurchaseCategory().toString(), order, subOrders, storeLowestItemPrice, sys));
                            }
                            itemsTableView.setItems(FXCollections.observableList(data));
                        }
                    }
                }
            });

    }

    @FXML
    void showDynamicOrder() throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../StoreView/DynamicOrder.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        OrderController o = fxmlLoader.getController();
        o.initialize(systemManager);
        stg.initModality(Modality.APPLICATION_MODAL);
        // newWindow.initOwner(primaryStage);
        Scene scene = new Scene(root, 900, 700);

        stg.setTitle("Your Order");
        stg.setScene(scene);
        stg.show();


        Node storeView = fxmlLoader.load();
        StoreTileController storeViewController = fxmlLoader.getController();
        storeViewController.initialize(systemManager.getSuperMarket().getStores(), systemManager);


        //myPane.getChildren().add(storeView);
    }

    @FXML
    void commitOrder(ActionEvent event) throws IOException {
        if(ItemSetterGetter.getTotalItemPrice().getValue().equals("0")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("New Order");
            alert.setHeaderText("Cannot commit order");
            alert.setContentText("No product selected");
            alert.showAndWait();
        }
            else {
                order.setOrderCustomer(customerBox.get(customerCB.getSelectionModel().getSelectedIndex()));
            order.setDateOfOrder(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            offerSales();




        }
    }

    private void offerSales() throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../SaleView/Sale.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        SalesController saleController = fxmlLoader.getController();
        saleController.initData(order);
        if(checkIfThereAreSales()) {
            saleController.showSales();
        }
        else {
            systemManager.commitOrder(order);
            saleController.showFinalOrder();
        }
        stg.initModality(Modality.APPLICATION_MODAL);
        // newWindow.initOwner(primaryStage);
        Scene scene = new Scene(root, 750, 700);

        stg.setTitle("YOU DESERVE SOME DISCOUNTS!");

        stg.setScene(scene);
        stg.showAndWait();

        Stage stage = (Stage) itemsFlowPan.getScene().getWindow();
        stage.close();
    }

    private boolean checkIfThereAreSales() {
        for (Store store : order.getStoresToOrderFrom().keySet()) {
            for (Sale sale : store.getSales()) {
                IfBuy conditonForSale = sale.getIfBuy();
                Item itemSale = systemManager.getSuperMarket().getItemByID(conditonForSale.getItemId());
                if (order.getItemsQuantity().containsKey(itemSale))
                    for (double i = 0; i < Math.floor(order.getItemsQuantity().get(itemSale) / conditonForSale.getQuantity()); i++) {
                        return true;
                    }

            }
        }
        return false;

    }


    @FXML
    void showDynamicOrder(ActionEvent event) throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../DynamicOrderView/DynamicOrder.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        DynamicInfoController o = fxmlLoader.getController();
        o.initData(order);
        o.showStores();

        stg.initModality(Modality.APPLICATION_MODAL);
        // newWindow.initOwner(primaryStage);
        Scene scene = new Scene(root, 900, 700);

        stg.setTitle("Your Order Info");
        stg.setScene(scene);
        stg.show();
    }


}


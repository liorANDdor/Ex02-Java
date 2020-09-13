package OrdersView;

import SDMModel.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.*;

public class OrdersSummaryController {

    @FXML
    private Label ID;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private ComboBox<String> storeCB;
    @FXML
    private ComboBox<String> ordersCB;
    @FXML
    private HashMap<Integer, Store> storeBox = new HashMap<>();
    @FXML
    private HashMap<Integer, Order> orderBox = new HashMap<>();
    @FXML
    private CheckBox orderTypeLabel;
    @FXML
    private Label shipmentLabel;
    @FXML
    private Button NextBtn;
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private FlowPane itemsFlowPan;
    @FXML
    private Label totalPriceForOrder;
    @FXML
    private Label customerName;
    @FXML
    private TableView<?> itemsTableView;
    private SystemManager systemManager = SystemManager.getInstance();
    TableColumn NameCol;
    TableColumn IdCol;
    TableColumn TypeCol;
    TableColumn purchasesCol;
    TableColumn totalQuantity;
    TableColumn totalPrice;
    TableColumn Price;
    TableColumn Discount;
    SimpleBooleanProperty isOrderSelected = new SimpleBooleanProperty(false);
    SimpleStringProperty shipmentPrice = new SimpleStringProperty("");
    SimpleStringProperty itemsPrice = new SimpleStringProperty("");
    SimpleStringProperty totalItemsPrice = new SimpleStringProperty("");
    HashMap<Integer, Store> storesBox = new HashMap<>();

    @FXML
    void BackToMainMenu(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize(HashMap<Integer, Order> orders, SystemManager sys) {

        HashMap<Integer, Customer> customers = sys.getSuperMarket().getCostumers();
        NameCol = new TableColumn("Name");
        IdCol = new TableColumn("ID");
        TypeCol = new TableColumn("Purchase Category");
        purchasesCol = new TableColumn("Add To Order");
        totalQuantity = new TableColumn("Your Quantity");
        totalPrice = new TableColumn("Total Price");
        Price = new TableColumn("Price");
        Discount = new TableColumn("From Discoumt");
        shipmentLabel.textProperty().bind(Bindings.format("Shipment Price: %s", shipmentPrice));
        totalPriceLabel.textProperty().bind(Bindings.format("Total Items Price for store: %s", itemsPrice));
        totalPriceForOrder.textProperty().bind(Bindings.format("Total Items Price overall: %s", totalItemsPrice));
        NameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("purchaseCategory"));
        totalQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        Discount.setCellValueFactory(new PropertyValueFactory<>("Discount"));
        storeCB.disableProperty().bind(isOrderSelected.not());
        //totalItemsPrice.set(String.format("%.2f", order.getItemsPrice()));
        itemsTableView.getColumns().addAll(IdCol, NameCol, Price, totalQuantity, totalPrice, Discount);

        initOrders(orders);

    }

    private void initOrders(HashMap<Integer, Order> orders) {
        int orderIndex = 0;
        for (Order order : orders.values()) {
            ordersCB.getItems().add(order.getOrderNumber().toString());
            orderBox.put(orderIndex, order);
            orderIndex++;
            ordersCB.setPromptText("Pick An Order");
        }
        ordersCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                isOrderSelected.set(true);
                Order order = orderBox.get(ordersCB.getSelectionModel().getSelectedIndex());
                if (order.getStoresToOrderFrom().size() == 1)
                    isOrderSelected.set(false);
                else
                    initStores(order.getStoresToOrderFrom().keySet());


                List data = new ArrayList<>();

                for (Store store : order.getStoresToOrderFrom().keySet()) {
                    for (Map.Entry<Item, Double> entry : store.getOrders().get(order.getOrderNumber()).getItemsQuantity().entrySet()) {
                        Item item = entry.getKey();
                        data.add(
                                new ItemSumamry(String.valueOf(item.getId()), //itemLowestPrice
                                        String.valueOf(item.getName()), item.getPurchaseCategory().toString(), String.valueOf(entry.getValue()), String.valueOf(store.getItemPrice(item.getId())),
                                        String.valueOf(entry.getValue() * store.getItemPrice(item.getId())), "No"));


                        if (order.getSalesByStoreId().get(store.getId()) != null) {
                            for (Offer offer : order.getSalesByStoreId().get(store.getId())) {
                                item = systemManager.getSuperMarket().getItemByID(offer.getItemId());
                                data.add(
                                        new ItemSumamry(String.valueOf(item.getId()), //itemLowestPrice
                                                String.valueOf(item.getName()), item.getPurchaseCategory().toString(), String.valueOf(offer.getQuantity()), String.valueOf(offer.getForAdditional()),
                                                String.valueOf(offer.getForAdditional()), "Yes"));
                            }
                        }
                    }

                    itemsTableView.setItems(FXCollections.observableList(data));
//                ItemSetterGetter.getTotalItemPrice().set(String.valueOf(order.getItemsPrice()));
//                shipmentPrice.set("0");
//                itemsPrice.set(String.format("%.2f",store.getOrders().get(order.getOrderNumber()).getItemsPrice()));
//                double deliveryDistance = Math.sqrt((order.getOrderCustomer().getLocation().x - store.getLocation().x) * (order.getOrderCustomer().getLocation().x - store.getLocation().x)
//                        + (order.getOrderCustomer().getLocation().y - store.getLocation().y) * (order.getOrderCustomer().getLocation().y - store.getLocation().y));
//                shipmentPrice.set(String.format("%.2f", store.getDeliveryPpk() * deliveryDistance));
                }
            }

        });
    }


    private void initStores(Set<Store> stores) {
        int storeIndex = 1;
        isOrderSelected.set(true);
        storeCB.getItems().add("Aggregated order");
        for (Store store :stores) {
            storeCB.getItems().add(store.getName());
            storeCB.setPromptText("Store bought from");
            storesBox.put(storeIndex,store);
            storeIndex++;
            storeCB.setPromptText("Choose Store For Order Info");
        }
        storeCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Order order = orderBox.get(ordersCB.getSelectionModel().getSelectedIndex());
                Store specficStore;
                if(storeCB.getSelectionModel().getSelectedIndex() != 0) {
                    specficStore = storesBox.get(storeCB.getSelectionModel().getSelectedIndex());
                    order = specficStore.getOrders().get(order.getOrderNumber());
                }
                List data = new ArrayList<>();

                for (Store store : order.getStoresToOrderFrom().keySet()) {
                    for (Map.Entry<Item,Double> entry: store.getOrders().get(order.getOrderNumber()).getItemsQuantity().entrySet()) {

                        Item item = entry.getKey();
                        data.add(
                                new ItemSumamry(String.valueOf(item.getId()), //itemLowestPrice
                                        String.valueOf(item.getName()), item.getPurchaseCategory().toString(), String.valueOf(entry.getValue()), String.valueOf(store.getItemPrice(item.getId())),
                                        String.valueOf(entry.getValue() * store.getItemPrice(item.getId())), "No"));


                        if (order.getSalesByStoreId().get(store.getId()) != null) {
                            for (Offer offer : order.getSalesByStoreId().get(store.getId())) {
                                item = systemManager.getSuperMarket().getItemByID(offer.getItemId());
                                data.add(
                                        new ItemSumamry(String.valueOf(item.getId()), //itemLowestPrice
                                                String.valueOf(item.getName()), item.getPurchaseCategory().toString(), String.valueOf(offer.getQuantity()), String.valueOf(offer.getForAdditional()),
                                                String.valueOf(offer.getForAdditional()), "Yes"));
                            }
                        }
                    }
                }
                itemsTableView.getItems().clear();
                itemsTableView.setItems(FXCollections.observableList(data));
//                ItemSetterGetter.getTotalItemPrice().set(String.valueOf(order.getItemsPrice()));
//                shipmentPrice.set("0");
//                itemsPrice.set(String.format("%.2f",store.getOrders().get(order.getOrderNumber()).getItemsPrice()));
//                double deliveryDistance = Math.sqrt((order.getOrderCustomer().getLocation().x - store.getLocation().x) * (order.getOrderCustomer().getLocation().x - store.getLocation().x)
//                        + (order.getOrderCustomer().getLocation().y - store.getLocation().y) * (order.getOrderCustomer().getLocation().y - store.getLocation().y));
//                shipmentPrice.set(String.format("%.2f", store.getDeliveryPpk() * deliveryDistance));
            }
        });

    }



    public static class ItemSumamry {
        private final SimpleStringProperty ID;
        private final SimpleStringProperty Name;
        private final SimpleStringProperty PurchaseCategory;
        private final SimpleStringProperty Quantity;
        private final SimpleStringProperty Price;
        private final SimpleStringProperty TotalPrice;
        private final SimpleStringProperty Discount;

        private ItemSumamry(String ID, String Name, String PurchaseCategory, String Quantity, String Price, String TotalPrice, String Discount) {
            this.ID = new SimpleStringProperty(ID);
            this.Name = new SimpleStringProperty(Name);
            this.PurchaseCategory = new SimpleStringProperty(PurchaseCategory);
            this.Quantity = new SimpleStringProperty(Quantity);
            this.Price = new SimpleStringProperty(Price);
            this.TotalPrice = new SimpleStringProperty(TotalPrice);
            this.Discount = new SimpleStringProperty(Discount);
        }


        public void setID(String ID) {
            this.ID.set(ID);
        }

        public void setName(String name) {
            this.Name.set(name);
        }

        public void setPurchaseCategory(String purchaseCategory) {
            this.PurchaseCategory.set(purchaseCategory);
        }

        public void setQuantity(String quantity) {
            this.Quantity.set(quantity);
        }

        public void setPrice(String price) {
            this.Price.set(price);
        }

        public void setTotalPrice(String totalPrice) {
            this.TotalPrice.set(totalPrice);
        }

        public void setDiscount(String discount) {
            this.Discount.set(discount);
        }



        public String getID() {
            return ID.get();
        }

        public SimpleStringProperty IDProperty() {
            return ID;
        }

        public String getName() {
            return Name.get();
        }

        public SimpleStringProperty nameProperty() {
            return Name;
        }

        public String getPurchaseCategory() {
            return PurchaseCategory.get();
        }

        public SimpleStringProperty purchaseCategoryProperty() {
            return PurchaseCategory;
        }

        public String getQuantity() {
            return Quantity.get();
        }

        public SimpleStringProperty quantityProperty() {
            return Quantity;
        }

        public String getPrice() {
            return Price.get();
        }

        public SimpleStringProperty priceProperty() {
            return Price;
        }

        public String getTotalPrice() {
            return TotalPrice.get();
        }

        public SimpleStringProperty totalPriceProperty() {
            return TotalPrice;
        }

        public String getDiscount() {
            return Discount.get();
        }

        public SimpleStringProperty discountProperty() {
            return Discount;
        }


    }
}

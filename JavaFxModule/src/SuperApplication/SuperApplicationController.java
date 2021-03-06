package SuperApplication;
import MapView.MapCreator;
import OrderWindow.OrderController;
import OrdersView.OrdersSummaryController;
import SDMModel.*;
import SaleView.OrderSummaryController;
import StoreView.StoreTileController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tile.tileController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperApplicationController {

    SystemManager systemManager = SystemManager.getInstance();
    private Map<Integer, Node> itemsTilesController = new HashMap<>();
    private Map<Integer, Node> storesTilesController = new HashMap<>();

    @FXML private Button xmlBtn;
    @FXML private Button storesBtn;
    @FXML private Button itemBtn;
    @FXML private Button ordersBtn;
    @FXML private Button customersBtn;
    @FXML private Button mapBtn;
    @FXML private Button addOrderBtn;
    @FXML private Button newItemBtn;
    @FXML private Button newStoreBtn;
    @FXML private Button newSaleBtn;
    @FXML private FlowPane myPane;
    @FXML private Button modifyItemBtn;
    @FXML private AnchorPane mainAnchor;


    @FXML private void initialize() throws IOException {

        storesBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        itemBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        ordersBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        customersBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        mapBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        addOrderBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        modifyItemBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        newSaleBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        newItemBtn.disableProperty().bind(systemManager.isXmlLoaded().not());
        newStoreBtn.disableProperty().bind(systemManager.isXmlLoaded().not());

    }


    @FXML
    void createOrder(ActionEvent event) throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/OrderWindow/Order.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        OrderController o = fxmlLoader.getController();
        o.initialize(systemManager);
        myPane.getChildren().clear();
        stg.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 900, 700);

        stg.setTitle("New Order");
        stg.setScene(scene);
        stg.show();
    }

    @FXML
    void loadXMLHandler(ActionEvent event) throws IOException {

        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/XmlLoderView/XmlLoader.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        Scene scene = new Scene(root, 400, 200);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setTitle("Load XML");
        stg.setScene(scene);
        stg.show();

    }

    @FXML
    void showCustomersHandler(ActionEvent event) {
        HashMap<Integer, Customer> customers =  systemManager.getSuperMarket().getCostumers();
        myPane.getChildren().clear();
        for(Customer customer: customers.values()){
            createCustomerTile(customer);
        }
    }

    private void createCustomerTile(Customer customer) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/tile/tile.fxml");
            fxmlLoader.setLocation(url);
            Node singleCustomerTile = fxmlLoader.load();
            List<Customer.InfoOptions> list = new ArrayList<Customer.InfoOptions>();
            list.add(Customer.InfoOptions.Name);
            list.add(Customer.InfoOptions.CustomerId);
            list.add(Customer.InfoOptions.Location);
            list.add(Customer.InfoOptions.NumberOfOrders);
            list.add(Customer.InfoOptions.AverageItemPrice);
            list.add(Customer.InfoOptions.AverageShipmentPrice);
            tileController tileController = fxmlLoader.getController();

            tileController.initialize(systemManager.getCustomerInfo(customer,list));
            myPane.getChildren().add(singleCustomerTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showItemsHandler(ActionEvent event) {
        myPane.getChildren().clear();
        HashMap<Integer, Item> items = systemManager.getSuperMarket().getItems();
        for(Item item: items.values()){
            createItemTile(item);
        }
    }

    private void createItemTile(Item item) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/tile/tile.fxml");
            fxmlLoader.setLocation(url);
            Node singleWordTile = fxmlLoader.load();
            List<Item.InfoOptions> list = new ArrayList<Item.InfoOptions>();
            list.add(Item.InfoOptions.ItemId);
            list.add(Item.InfoOptions.Name);
            list.add(Item.InfoOptions.Category);
            list.add(Item.InfoOptions.NumberOfStoresSellTheItem);
            list.add(Item.InfoOptions.ItemAveragePrice);
            list.add(Item.InfoOptions.NumberOfTimesItemWasSold);
            tileController tileController = fxmlLoader.getController();

            tileController.initialize(systemManager.getinfoItem(item,list));
            Platform.runLater(
                    () -> tileController.setName(item.getName())
            );

            myPane.getChildren().add(singleWordTile);
            itemsTilesController.put(item.getId(), singleWordTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void showMapHandler(ActionEvent event) throws IOException {
        Stage stg = new MapCreator().getMap();
        stg.show();
    }

    @FXML
    void showOrdersHandler(ActionEvent event) throws IOException {

        myPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/OrdersView/OrdersSummary.fxml");
        fxmlLoader.setLocation(url);
        Node orderSummary = fxmlLoader.load();
        OrdersSummaryController ordersSummaryController = fxmlLoader.getController();
        ordersSummaryController.initialize(systemManager.getSuperMarket().getOrders(), systemManager);
        myPane.getChildren().add(orderSummary);

    }

    @FXML
    void showStoresHandler(ActionEvent event) {
        myPane.getChildren().clear();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/StoreView/Store.fxml");
            fxmlLoader.setLocation(url);
            Node storeView = fxmlLoader.load();
            StoreTileController storeViewController = fxmlLoader.getController();
            storeViewController.initialize(systemManager.getSuperMarket().getStores(), systemManager);
            myPane.getChildren().add(storeView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void modifyItemHandler(ActionEvent event) throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/UpdateStoreItemsView/UpdateStoreItems.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        Scene scene = new Scene(root, 250, 300);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setTitle("Modify item");
        stg.setScene(scene);
        stg.show();
    }

    @FXML
    void addStoreHandler() throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/AddStore/AddStore.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        Scene scene = new Scene(root, 500, 500);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setTitle("Add Store");
        stg.setScene(scene);
        stg.show();
    }
    @FXML
    void addItemStoreHandler() throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/AddItem/AddItemToSupermarket.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        Scene scene = new Scene(root, 500, 500);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setTitle("Add Item");
        stg.setScene(scene);
        stg.show();
    }

    @FXML void addSaleHandler() throws IOException {
            Stage stg = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/AddSale/AddSale.fxml");
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
            Scene scene = new Scene(root, 300, 350);
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setTitle("Add Sale");
            stg.setScene(scene);
            stg.show();

    }
}

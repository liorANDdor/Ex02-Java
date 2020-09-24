package DynamicOrderView;

import SDMModel.Order;
import SDMModel.Sell;
import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import tile.tileController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicInfoController {

    @FXML
    private FlowPane storesPane;
    private SystemManager systemManager=SystemManager.getInstance();
    private Order order;


    public void  initData(Order order){
        this.order = order;

    }
    @FXML
    public void showStores() {
        HashMap<Integer, Store> stores = systemManager.getSuperMarket().getStores();

        HashMap<Store, List<Sell>> storesFromOrder = order.getStoresToOrderFrom();

        HashMap<Integer, Store> relevantStores = (HashMap<Integer, Store>) stores.entrySet()
                .stream()
                .filter(store -> storesFromOrder.keySet().stream().anyMatch(rev -> rev.getId() == store.getKey()))
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));

        for (Store store : relevantStores.values()) {
            showOrderInfoByStore(store);
        }
    }

    private void showOrderInfoByStore(Store store) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/tile/tile.fxml");
            fxmlLoader.setLocation(url);
            Node storeView = fxmlLoader.load();
            tileController tileController = fxmlLoader.getController();


            Order specificStoreOrder = store.getOrders().get(order.getOrderNumber());
            specificStoreOrder.setOrderCustomer(order.getOrderCustomer());
            Point clientLocation = order.getOrderCustomer().getLocation();
            double storeShipmentPrice=0.0;
            Point storeLocation = store.getLocation();
            double deliveryDistance = Math.sqrt((clientLocation.x - storeLocation.x) * (clientLocation.x - storeLocation.x)
                        + (clientLocation.y - storeLocation.y) * (clientLocation.y - storeLocation.y));
            specificStoreOrder.setDeliveryDistance(deliveryDistance);
            storeShipmentPrice = deliveryDistance * store.getDeliveryPpk();
            specificStoreOrder.setShipmentPrice(storeShipmentPrice);
            List<Order.InfoOptions> orderAttributes = new ArrayList<>();
            orderAttributes.add(Order.InfoOptions.AmountOfKindsOfItems);
            orderAttributes.add(Order.InfoOptions.ItemsPrice);
            orderAttributes.add(Order.InfoOptions.ShipmentPrice);
            orderAttributes.add(Order.InfoOptions.DeliveryDistance);
            List<Store.InfoOptions> storeAttributes = new ArrayList<>();
            storeAttributes.add(Store.InfoOptions.Id);
            storeAttributes.add(Store.InfoOptions.Name);
            storeAttributes.add(Store.InfoOptions.Location);
            storeAttributes.add(Store.InfoOptions.DeliveryPpk);

            tileController.initialize(systemManager.getStoreInfo(store,storeAttributes).append(
                                      systemManager.getinfoOrder(specificStoreOrder,orderAttributes)));

            storesPane.getChildren().add(storeView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

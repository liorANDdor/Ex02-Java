package SDMModel;

import XmlLoderView.XmlLoaderController;
import javafx.beans.property.SimpleBooleanProperty;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SystemManager {

     private static SystemManager manager = null;
     private SuperMarket superMarket;

     private SimpleBooleanProperty thereIsXmlLoaded = new SimpleBooleanProperty(false);


    public SuperMarket getSuperMarket() {
        return superMarket;
    }

    public void LoadXMLFileAndCheckIt(String fullPath, XmlLoaderController controller) {
        Consumer<SuperMarket> superMarketConsumer = value -> {
            this.superMarket = value;
        };

        Consumer<Boolean> xmlLoadedConsumer = value -> {
            this.thereIsXmlLoaded.set(value);
        };
        XmlLoaderTask task = new XmlLoaderTask(fullPath, superMarketConsumer, xmlLoadedConsumer);
        controller.bindUIToTask(task);
        new Thread(task).start();

    }

    private SystemManager(){

    }
     public static SystemManager getInstance()
     {
         if (manager == null)
             manager = new SystemManager();

         return manager;
     }
     public SimpleBooleanProperty isXmlLoaded() {
        return thereIsXmlLoaded;
    }

    public StringBuilder getinfoItem(Item item, List<Item.InfoOptions>list){
        StringBuilder itemInfo = new StringBuilder();
        for (Item.InfoOptions option : list) {
            itemInfo
                    .append(String.join(" ", option.toString().split("(?=[A-Z])")))
                    .append(": ").append(option.getInfo(item))
                    .append("\n");
        }

        return itemInfo;
    }

    public StringBuilder getinfoOrder(Order item, List<Order.InfoOptions>list){
        StringBuilder itemInfo = new StringBuilder();
        for (Order.InfoOptions option : list) {
            itemInfo
                    .append(String.join(" ", option.toString().split("(?=[A-Z])")))
                    .append(": ").append(option.getInfo(item))
                    .append("\n");
        }

        return itemInfo;
    }

    public StringBuilder getInfoSell(Sell sell, List<Sell.InfoOptions>list){
        StringBuilder sellInfo = new StringBuilder();
        for (Sell.InfoOptions option : list) {
            sellInfo
                    .append(String.join(" ", option.toString().split("(?=[A-Z])")))
                    .append(": ").append(option.getInfo(sell))
                    .append("\n");
        }
        return sellInfo;
    }

    public StringBuilder getStoreInfo(Store store , List<Store.InfoOptions>list){
        StringBuilder storeInfo = new StringBuilder();
        for (Store.InfoOptions option : list) {
            storeInfo
                    .append(String.join(" ", option.toString().split("(?=[A-Z])")))
                    .append(": ").append(option.getInfo(store))
                    .append("\n");
        }
        return storeInfo;
    }

    public StringBuilder getCustomerInfo(Customer customer , List<Customer.InfoOptions>list){
        StringBuilder customerInfo = new StringBuilder();
        for (Customer.InfoOptions option : list) {
            customerInfo
                    .append(String.join(" ", option.toString().split("(?=[A-Z])")))
                    .append(": ").append(option.getInfo(customer))
                    .append("\n");
        }
        return customerInfo;
    }

    public Order getEmptyOrder() {
        Order order = new Order();
        return order;
    }

    public boolean isValidStoreId(int storeID) {
        return superMarket
                .getStores()
                .keySet()
                .stream()
                .filter(key -> key == storeID)
                .count() == 1;
    }

    public void setStoreOfOrderByID(int storeID, Order emptyOrder) {
        emptyOrder.getStoresToOrderFrom().put(superMarket.getStores().get(storeID), new ArrayList<>());
    }

    public String getPriceOfItemByStoreId(Item item, int finalStoreID) {
        Store store = superMarket.getStores().get(finalStoreID);
        Sell sellFound = (Sell)store
                .getItemsToSell()
                .stream()
                .filter(sell->sell.getItemId()==item.getId())
                .findAny()
                .orElse(null);
        if(sellFound != null)
            return String.valueOf(sellFound.getPrice());
        else
            return "No Price (not for sale)";
    }

    public boolean isValidItemId(int itemId) {
        return superMarket
                .getItems()
                .keySet()
                .stream()
                .anyMatch(key->itemId == key);
    }

    public Item.PurchaseCategory getPurchaseCategory(int itemId) {
        return superMarket
                .getItems()
                .get(itemId)
                .getPurchaseCategory();
    }

    public boolean checkIfStoreSellAnItem(int storeID, int itemId) {
        Store store = superMarket.getStores().get(storeID);
        return store.isItemSold(itemId);
    }

    public void commitOrder(Order order) {
        order.getStoresToOrderFrom().keySet().stream().forEach(store -> store.getOrders().remove(null));
        Integer orderNumber = superMarket.getNumberOfOrders() + 1;
        superMarket.increaseOrderNumber();
        order.setOrderNumber(orderNumber);
        //order.calculatAndSetDistance();
        superMarket.addOrder(order);
        for (Item itemFromXml : order.getItemsQuantity().keySet()) {
            Item item = superMarket.getItemByID(itemFromXml.getId());
            item.increaseNumberOfTimesItemWasSold(order.getItemsQuantity().get(item));
        }
        for(Integer storeId: order.getSalesByStoreId().keySet()) {
            for (Offer offer : order.getSalesByStoreId().get(storeId)) {
                Item item = superMarket.getItemByID(offer.getItemId());
                item.increaseNumberOfTimesItemWasSold(offer.getQuantity());
            }
        }
        for (Map.Entry<Store, List<Sell>> entry : order.getStoresToOrderFrom().entrySet()) {
            Store store = superMarket.getStores().get(entry.getKey().getId());
             Order.crateSubOrder(store, order,  superMarket.getItems().values());
        }
        order.getOrderCustomer().addTotalItemPrice(order.getItemsPrice());
        order.getOrderCustomer().addTotalShipmentPrice(order.getShipmentPrice());
        order.getOrderCustomer().increaseNumberOfOrders();

    }

    public void addAnItemToOrder(Order order, HashMap<Integer, Order> subOrders, Store store, int itemId, double quantity) {
        Item item = superMarket.getItemByID(itemId);
        if  (order.getItemsQuantity().containsKey(item)){
            order.getItemsQuantity().put(item, (order.getItemsQuantity().get(item) + quantity));
        }
        else {
            if(order.getStoresToOrderFrom().containsKey(store)){
                order.getStoresToOrderFrom().get(store).add(store.getSellById(itemId));
            }
            else {
                order.getStoresToOrderFrom().put(store, new ArrayList<Sell> ());
                order.getStoresToOrderFrom().get(store).add(store.getSellById(itemId));
            }
            order.getItemsQuantity().put(item, quantity);
        }

        double itemPrice = superMarket.getStores().get(store.getId()).getItemPrice(itemId);
        order.increaseOrderTotalPrice(itemPrice * quantity);
        subOrders.put(store.getId(), addAnItemToSubOrder(subOrders.getOrDefault(store.getId(), getEmptyOrder()), store, itemId, quantity));
    }

    public Order addAnItemToSubOrder(Order order, Store store, int itemId, double quantity) {
        Item item = superMarket.getItemByID(itemId);
        if  (order.getItemsQuantity().containsKey(item)){
            order.getItemsQuantity().put(item, (order.getItemsQuantity().get(item) + quantity));
        }
        else {
            if(order.getStoresToOrderFrom().containsKey(store)){
                order.getStoresToOrderFrom().get(store).add(store.getSellById(itemId));
            }
            else {
                order.getStoresToOrderFrom().put(store, new ArrayList<Sell> ());
                order.getStoresToOrderFrom().get(store).add(store.getSellById(itemId));
            }
            order.getItemsQuantity().put(item, quantity);
        }

        double itemPrice = superMarket.getStores().get(store.getId()).getItemPrice(itemId);
        order.increaseOrderTotalPrice(itemPrice * quantity);
        store.getOrders().put(order.getOrderNumber(), order );
        return order;
    }

    public void isfixedLocationAndSetToOrder(Point point, Order emptyOrder) throws Exception {
        if(point.x>50||point.x<1||point.y>50||point.y<1)
            throw new Exception("Locaion should be 1-50");
        else if (isOrderLocaionAndStoresLocationMatch(point))
            throw new Exception("Locaion is like a store location");
        else
            emptyOrder.setLocationOfClient(point);
    }

    private boolean isOrderLocaionAndStoresLocationMatch(Point point) {
        return superMarket
                .getStores()
                .values()
                .stream()
                .anyMatch(store -> store.getLocation().equals(point));
    }

    public Store getItemLowestPrice(Integer itemId) {
        Double itemLowstPrice = Double.POSITIVE_INFINITY;
        Store storeLowestItemPrice = null;
        for(Store store:superMarket.getStores().values()){
            if(store.isItemSold(itemId))
                if(store.getItemPrice(itemId)<itemLowstPrice) {
                    itemLowstPrice = store.getItemPrice(itemId);
                    storeLowestItemPrice = store;
                }
        }
        return storeLowestItemPrice;
    }



}

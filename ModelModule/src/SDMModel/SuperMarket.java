package SDMModel;

import SDMGenerated.SDMCustomer;
import SDMGenerated.SDMItem;
import SDMGenerated.SDMStore;
import SDMGenerated.SuperDuperMarketDescriptor;

import java.util.ArrayList;
import java.util.stream.*;
import java.util.HashMap;
import java.util.List;

public class SuperMarket {

    private Integer numberOfOrders = 0;
    private HashMap<Integer, Store> stores = new HashMap<>();
    private HashMap<Integer ,Item> items = new HashMap<>();
    private HashMap<Integer, Order> orders = new HashMap<>();
    private HashMap<Integer, Customer> costumers = new HashMap<>();
    public HashMap<Integer, Store> getStores() {
        return stores;
    }

    public void setStores(HashMap<Integer, Store> stores) {
        this.stores = stores;
    }

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public static SuperMarket creatInstance(SuperDuperMarketDescriptor superMarketSDM) {
        SuperMarket instance = new SuperMarket();

        List<SDMItem>itemsSDM = superMarketSDM.getSDMItems().getSDMItem();
        List<SDMStore>storesSDM = superMarketSDM.getSDMStores().getSDMStore();
        List<SDMCustomer>customersSDM = superMarketSDM.getSDMCustomers().getSDMCustomer();

        for(SDMStore sdmStore : storesSDM){
            Store newStore = Store.createInstanceBySDM(sdmStore);
            instance.getStores().put(newStore.getId(), newStore);
        }

        for(SDMItem sdmItem : itemsSDM){
            List<Store> listWhoSellTheItem =
                    instance.getStores().values().stream()
                            .filter(store -> store.isItemSold(sdmItem.getId())).collect(Collectors.toList());
            Item newItem = Item.createInstanceBySDM(sdmItem, listWhoSellTheItem);
            instance.getItems().put(newItem.getId(),newItem);
        }

        for(SDMCustomer sdmCustomer : customersSDM){
            Customer newCustomer = Customer.createInstanceBySDM(sdmCustomer);
            instance.getCostumers().put(newCustomer.getId(),newCustomer);
        }

        return instance;
    }

    public Item getItemByID(int itemId){
        Item item = items.get(itemId);
        return item;
    }

    public Integer getNumberOfOrders() {
        return numberOfOrders;
    }

    public void increaseOrderNumber() {
        this.numberOfOrders+=1;
    }

    public HashMap<Integer, Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.put(order.getOrderNumber(), order);
    }

    public HashMap<Integer, Customer> getCostumers() {
        return costumers;
    }

    public void setCostumers(HashMap<Integer, Customer> costumers) {
        this.costumers = costumers;
    }

    public void addStore(Store store) {
        stores.put(store.getId(), store);
        for(Sell sell:store.getItemsToSell()){
            Item item = getItemByID(sell.getItemId());
            item.getStoresWhoSellTheItem().add(store);
        }
    }
}

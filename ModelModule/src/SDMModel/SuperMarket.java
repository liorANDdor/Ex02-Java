package SDMModel;

import SDMGenerated.SDMItem;
import SDMGenerated.SDMStore;
import SDMGenerated.SuperDuperMarketDescriptor;

import java.util.ArrayList;
import java.util.stream.*;
import java.util.HashMap;
import java.util.List;

public class SuperMarket {

    private Integer numberOfOrders = 0;
    private HashMap<Integer, Store> stores = new  HashMap<Integer ,Store>();
    private HashMap<Integer ,Item> items = new  HashMap<Integer ,Item>();
    private HashMap<Integer, Order> orders = new  HashMap<Integer ,Order>();
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

        for(SDMStore sdmStore : storesSDM){
            Store newStore = Store.createInstanceBySDM(sdmStore);
            instance.getStores().put(newStore.getId(), newStore);
        }
        List<Store> result = new ArrayList<>();

        for(SDMItem sdmItem : itemsSDM){
            List<Store> listWhoSellTheItem =
                    (List<Store>) instance.getStores().values().stream()
                            .filter(store -> store.isItemSold(sdmItem.getId())).collect(Collectors.toList());
            Item newItem = Item.createInstanceBySDM(sdmItem, listWhoSellTheItem);
            instance.getItems().put(newItem.getId(),newItem);
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
}

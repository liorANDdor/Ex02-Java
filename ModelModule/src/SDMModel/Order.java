package SDMModel;

import java.io.Serializable;
import java.util.*;
import java.util.List;

import java.awt.*;

public class Order implements Serializable {

    public enum InfoOptions {
        OrderId, Date, ItemsPrice, ShipmentPrice, TotalPrice, DeliveryDistance, AmountOfKindsOfItems, AmountOfAllItems, StoresThatSupplyTheOrder;
        public String getInfo(Order order) {

            switch (this) {
                case Date:
                    return order.getDateOfOrder().toString();
                case OrderId:
                    return String.valueOf(order.getOrderNumber());
                case ShipmentPrice:
                    return String.valueOf(order.getShipmentPrice());
                case ItemsPrice:
                    return String.valueOf(order.getItemsPrice());
                case TotalPrice:
                    return String.valueOf((order.getItemsPrice() + order.getShipmentPrice()));
                case DeliveryDistance:
                    return String.valueOf(order.getDeliveryDistance());
                case AmountOfKindsOfItems:
                    return String.valueOf(order.getAmountOfKindsOfItems());
                case AmountOfAllItems:
                    return String.valueOf(order.getAmountOfAllItems());
                case StoresThatSupplyTheOrder:
                    return order.getStoresAsString();
                default:
                    return "Unknown";
            }
        }
    }


    private Integer orderNumber;
    private HashMap<Store, List<Sell>> storesToOrderFrom = new HashMap<Store, List<Sell>>();
    private Point locationOfClient;
    private HashMap<Item , Double> itemsQuantity = new  HashMap<Item ,Double>();
    private Date dateOfOrder;
    private Double itemsPrice = 0.0;
    private double deliveryDistance;
    private Double shipmentPrice = 0.0;
    private Customer orderCustomer;
    private HashMap<Integer, List<Offer>> salesByStoreId = new HashMap<>();

    public HashMap<Integer, List<Offer>> getSalesByStoreId() {
        return salesByStoreId;
    }






    public void setOrderCustomer(Customer orderCustomer) {
        this.setLocationOfClient(orderCustomer.getLocation());
        this.orderCustomer = orderCustomer;

    }

    public Customer getOrderCustomer() {
        return orderCustomer;
    }

    public static void crateSubOrder(Store store, Order order, Collection<Item> items){
        Order subOrder = new Order();
        subOrder.setDateOfOrder(order.getDateOfOrder());
        subOrder.setOrderNumber(order.getOrderNumber());
        subOrder.getStoresToOrderFrom().put(store, order.getStoresToOrderFrom().get(store));
        subOrder.setLocationOfClient(order.getLocationOfClient());
        subOrder.calculatAndSetDistance();
        subOrder.setOrderCustomer(order.getOrderCustomer());
        double itemPrice = 0.0;
        for (Sell sell : subOrder.getStoresToOrderFrom().get(store)) {
            Sell sellOfStore = store.getSellById(sell.getItemId());
            Item item = items.stream().filter(itemEl->itemEl.getId()==sell.getItemId()).findAny().get();
            itemPrice = itemPrice + sell.getPrice() * order.getItemsQuantity().get(item);
            sellOfStore.increaseNumberOfTimesItemWasSold(order.getItemsQuantity().get(item));
            subOrder.getItemsQuantity().put(item, order.getItemsQuantity().get(item));
        }
        List<Offer> newOfferlist = new ArrayList<>();
        subOrder.getSalesByStoreId().put(store.getId(), newOfferlist);
        if(order.getSalesByStoreId().get(store.getId()) != null) {
            for (Offer offer : order.getSalesByStoreId().get(store.getId())) {
                Item item = items.stream().filter(itemEl -> itemEl.getId() == offer.getItemId()).findAny().get();
                itemPrice = itemPrice + offer.getForAdditional();
                store.getSellById(offer.getItemId()).increaseNumberOfTimesItemWasSold(offer.getQuantity());
                subOrder.getSalesByStoreId().get(store.getId()).add(offer);
            }
        }
        subOrder.setItemsPrice(itemPrice);
        store.addToTotalEarning(itemPrice + subOrder.getShipmentPrice());
        store.getOrders().put(order.getOrderNumber(), subOrder);
    }

    private String getStoresAsString() {
        String stores = "";
        for (Store store : storesToOrderFrom.keySet())
            stores += store.getName() + " " + store.getId() + " ";
        return  stores;
    }

    public double getItemPrice(Integer itemID) {
        Double itemPrice = 0.0;
        for(List<Sell> sells:storesToOrderFrom.values()){
            for(Sell sell:sells)
                if (sell.getItemId() == itemID)
                    itemPrice = sell.getPrice();
        }
        return itemPrice;
    }

    public void setItemsPrice(double itemPrice) {
        this.itemsPrice = (double)Math.round( itemPrice * 100.0d) / 100.0d;

    }

    public void calculatAndSetDistance() {
        Point clientLocation = getLocationOfClient();
        double totalShipmentPrice=0.0, storeShipmentPrice=0.0, totalDistance = 0.0;
        for(Store store: getStoresToOrderFrom().keySet()) {
            Point storeLocation = store.getLocation();
            double deliveryDistance = Math.sqrt((clientLocation.x - storeLocation.x) * (clientLocation.x - storeLocation.x)
                    + (clientLocation.y - storeLocation.y) * (clientLocation.y - storeLocation.y));
            store.addToTotalShipmentEarning(deliveryDistance * store.getDeliveryPpk());
            storeShipmentPrice = deliveryDistance * store.getDeliveryPpk();
            totalShipmentPrice = totalShipmentPrice + storeShipmentPrice;
            totalDistance =totalDistance + deliveryDistance;
        }
        this.setShipmentPrice(totalShipmentPrice);
        this.setDeliveryDistance(totalDistance);

    }

    private int getAmountOfAllItems() {
        double amountOfAllItems = 0.0;
        for(Item item:itemsQuantity.keySet())
            if(item.getPurchaseCategory()== Item.PurchaseCategory.WEIGHT)
                amountOfAllItems++;
            else
                amountOfAllItems = amountOfAllItems + itemsQuantity.get(item);
        return (int)amountOfAllItems;
    }

    private int getAmountOfKindsOfItems() {
        return itemsQuantity.keySet().size();
    }

    public void setDeliveryDistance(double deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public double getDeliveryDistance() {
        return (double)Math.round( deliveryDistance * 100.0d) / 100.0d;
    }

    public void increaseOrderTotalPrice(double itemPrice) {
        this.itemsPrice=this.itemsPrice+itemPrice;
    }

    public Double getItemsPrice() {
        return itemsPrice;
    }

    public Double getShipmentPrice() {
        return (double)Math.round( shipmentPrice * 100.0d) / 100.0d;
    }

    public void setShipmentPrice(Double price) {
        shipmentPrice = price;
    }

    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public java.util.Date getDateOfOrder() {
        return dateOfOrder;
    }

    public HashMap<Store, List<Sell>> getStoresToOrderFrom() {
        return storesToOrderFrom;
    }

    public void setStoresToOrderFrom(HashMap<Store, List<Sell>> storesToOrderFrom) {
        this.storesToOrderFrom=storesToOrderFrom;
    }

    public HashMap<Item, Double> getItemsQuantity() {
        return itemsQuantity;
    }

    public Point getLocationOfClient() {
        return locationOfClient;
    }

    public void setLocationOfClient(Point locationOfClient) {
        this.locationOfClient = locationOfClient;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }
    @Override
    public int hashCode() {
        return this.getOrderNumber();
    }

    @Override
    public boolean equals(Object o)
    {
        return o.hashCode()==this.hashCode() ;
    }
}

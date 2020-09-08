package SDMModel;

import SDMGenerated.SDMDiscount;
import SDMGenerated.SDMDiscounts;
import SDMGenerated.SDMSell;
import SDMGenerated.SDMStore;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Store implements Serializable {

    public enum InfoOptions {
        Name, Id, Location, DeliveryPpk, TotalEarning, TotalShipmentEarning;

        public String getInfo(Store store) {
            switch (this) {
                case Name:
                    return store.getName();
                case Id:
                    return String.valueOf(store.getId());
                case Location:
                    return String.valueOf(store.getLocation());
                case DeliveryPpk:
                    return String.valueOf(store.getDeliveryPpk());
                case TotalEarning:
                    return String.valueOf(store.getTotalEarning());
                case TotalShipmentEarning:
                    return String.valueOf(store.getTotalShipmentEarning());
                default:
                    return "Unknown";
            }
        }
    }



    private Double totalEarning = 0.0;
    private Double totalShipmentEarning = 0.0;
    private HashMap<Integer, Order> orders = new HashMap<>();
    private String name;
    private int deliveryPpk;
    private Point location;
    private List<Sell> itemsToSell = new ArrayList<>();
    private List<Sale> sales = new ArrayList<>();
    private int id;

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }


    public Double getTotalEarning() {

        return totalEarning;
    }
    public void addToTotalEarning(double earning) {

         totalEarning = totalEarning +earning;
    }

    public static Store createInstanceBySDM(SDMStore sdmStore) {
        Store newStore = new Store();

        newStore.setId(sdmStore.getId());
        newStore.setDeliveryPpk(sdmStore.getDeliveryPpk());
        newStore.setName(sdmStore.getName());
        newStore.setLocation(new Point(sdmStore.getLocation().getX(),sdmStore.getLocation().getY()));
        List<SDMSell>itemsToSellSDM = sdmStore.getSDMPrices().getSDMSell();
        SDMDiscounts sdmDiscounts = sdmStore.getSDMDiscounts();
        if(sdmDiscounts!= null) {
            List<SDMDiscount> discounts = sdmStore.getSDMDiscounts().getSDMDiscount();
            for(SDMDiscount discount : discounts){
                Sale newSale = Sale.createInstanceBySDM(discount);
                newStore.getSales().add(newSale);
            }
        }
        for(SDMSell sell : itemsToSellSDM){
            Sell newSell= Sell.createInstanceBySDM(sell);
            newStore.getItemsToSell().add(newSell);
        }

        return newStore;
    }

    public int getDeliveryPpk() {
        return deliveryPpk;
    }

    public void setDeliveryPpk(int deliveryPpk) {
        this.deliveryPpk = deliveryPpk;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Sell getSellById(int itemId) {
        Sell sellById = null;
        for (Sell sell:itemsToSell){
            if (sell.getItemId() == itemId)
                sellById = sell;
        }
        return sellById;
    }

    public List<Sell> getItemsToSell() {
        return itemsToSell;
    }

    public boolean isItemSold(int itemId){
        for (Sell sell:itemsToSell){
            if (sell.getItemId()==itemId)
                    return true;
        }
        return false;
    }

    public HashMap<Integer, Order> getOrders() {
        return orders;
    }

    public int getId() {
        return id;
    }

    public void addOrder(Integer orderNumber, Order order) {
        HashMap<Store, List<Sell>> storesOrderedFrom= new HashMap<>();
        storesOrderedFrom.put(this, order.getStoresToOrderFrom().get(this));
        order.setStoresToOrderFrom(storesOrderedFrom);
        getOrders().put(orderNumber, order);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getItemPrice(int itemId) {
        for(Sell sell:itemsToSell){
            if(sell.getItemId() == itemId){
                return sell.getPrice();
            }
        }
        return 0; //TODO:ERROR raise
    }
    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(Object o)
    {
        return o.hashCode()==this.hashCode() ;
    }

    public double getTotalShipmentEarning() {
        return this.totalShipmentEarning;
    }
    public void addToTotalShipmentEarning(double newShipmentPrice) {
        totalShipmentEarning = totalEarning + newShipmentPrice;
    }
}

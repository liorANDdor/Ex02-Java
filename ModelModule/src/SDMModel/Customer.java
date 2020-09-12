package SDMModel;

import SDMGenerated.SDMCustomer;

import java.awt.*;
import java.util.HashMap;


public class Customer {

    public Customer() {
    }



    public enum InfoOptions {
        Name, CustomerId, Location,AverageShipmentPrice, AverageItemPrice;

        public String getInfo(Customer customer) {
            switch (this) {
                case CustomerId:
                    return String.valueOf(customer.getId());
                case Name:
                    return customer.getName();
                case Location:
                    return String.valueOf(customer.getLocation());
                case AverageShipmentPrice:
                    return String.valueOf(customer.getTotalShipmentPrice() / customer.getNumberOfOrders()==0 ? 1: 0);
                case AverageItemPrice:
                    return String.valueOf(customer.getTotalItemPrice() / customer.getNumberOfOrders()==0 ? 1: 0);
                default:
                    return "Unknown";
            }
        }
    }

    private String name;
    private Point location;
    private int id;


    public Integer getNumberOfOrders() {
        return numberOfOrders;
    }

    private Integer numberOfOrders = 0;
    private double totalShipmentPrice = 0.0;
    private double totalItemPrice = 0.0;


    public void increaseNumberOfOrders() {
        numberOfOrders++;
    }


    public double getTotalShipmentPrice() {
        return totalShipmentPrice;
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void addTotalShipmentPrice(double shipmentPrice) {
        this.totalShipmentPrice =  this.totalShipmentPrice + shipmentPrice;
    }

    public void addTotalItemPrice(double itemsPrice) {
        this.totalItemPrice = this.totalItemPrice + itemsPrice;
    }


    private HashMap<Integer, Order> orders = new HashMap<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Customer createInstanceBySDM(SDMCustomer sdmCustomer) {
        Customer customer = new Customer();
        customer.setId(sdmCustomer.getId());
        customer.setName(sdmCustomer.getName());
        customer.setLocation(new Point(sdmCustomer.getLocation().getX(),sdmCustomer.getLocation().getY()));
        return  customer;

    }
}


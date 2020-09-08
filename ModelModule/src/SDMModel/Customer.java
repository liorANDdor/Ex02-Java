package SDMModel;

import SDMGenerated.Location;
import SDMGenerated.SDMCustomer;

import java.awt.*;
import java.util.HashMap;


public class Customer {

    public enum InfoOptions {
        Name, CustomerId, Location;

        public String getInfo(Customer customer) {
            switch (this) {
                case CustomerId:
                    return String.valueOf(customer.getId());
                case Name:
                    return customer.getName();
                case Location:
                    return String.valueOf(customer.getLocation());
                default:
                    return "Unknown";
            }
        }
    }

    private String name;
    private Point location;
    private int id;
    private HashMap<Integer, Order> orders = new HashMap<>();
    private Integer numberOfOrders = 0;

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


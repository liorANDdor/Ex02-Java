package SDMModel;


import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.io.Serializable;
import java.util.HashMap;

public class ItemSetterGetter implements Serializable {


    public ItemSetterGetter(String price, String name, String ID, String purchaseCategory, Order order, HashMap<Integer, Order> subOrders , Store store, SystemManager sys) {
        this.Price = new SimpleStringProperty(price);
        this.Name = new SimpleStringProperty(name);
        this.ID = new SimpleStringProperty(ID);
        this.purchaseCategory = new SimpleStringProperty(purchaseCategory);
        this.totalPrice = new SimpleStringProperty("0");
        this.totalQuantity = new SimpleStringProperty("0");
        this.addButton = new Button("Add To Cart");

        if (purchaseCategory.equals("Weight")) {
            SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 500, 0, 0.1);
            quantitySpinner.setValueFactory(valueFactory);
        } else {
            SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 500, 0, 1);
            quantitySpinner.setValueFactory(valueFactory);
        }
        addButton.setOnAction(x -> {
            if(!quantitySpinner.getValue().equals(0) && !quantitySpinner.getValue().equals(0.0)) {
                int itemID = Integer.parseInt(ID);
                sys.addAnItemToOrder(order,subOrders, store, itemID, quantitySpinner.getValue());
                double totalDobuleQuantity = order.getItemsQuantity().get(sys.getSuperMarket().getItemByID(itemID));
                totalQuantity.set(String.format("%.2f", totalDobuleQuantity)); // can be taken from order..
                totalPrice.set(String.format("%.2f",totalDobuleQuantity * Double.parseDouble(price)));

                totalItemPrice.set(String.format(
                        "%.2f",Double.parseDouble(totalItemPrice.get()) + quantitySpinner.getValue() * store.getItemPrice(Integer.parseInt(ID))));
                quantitySpinner.getValueFactory().setValue(0.0);
            }
        });
    }
    private SimpleStringProperty Price;
    private SimpleStringProperty purchaseCategory;
    private SimpleStringProperty Name;
    private SimpleStringProperty ID;
    private SimpleStringProperty totalPrice;
    private SimpleStringProperty totalQuantity;
    private Button addButton;
    private SimpleStringProperty Store;
    public String getStore() {
        return Store.get();
    }

    public SimpleStringProperty storeProperty() {
        return Store;
    }

    public void setStore(String store) {
        this.Store.set(store);
    }


    public static SimpleStringProperty getTotalItemPrice() {
        return totalItemPrice;
    }

    public SimpleStringProperty itemsPriceProperty() {
        return totalItemPrice;
    }

    static SimpleStringProperty totalItemPrice = new SimpleStringProperty("");
    public void setTotalPrice(String totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity.set(String.format("%.2f",totalQuantity));
    }


    public String getTotalPrice() {
        return totalPrice.get();
    }

    public SimpleStringProperty totalPriceProperty() {
        return totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity.get();
    }

    public SimpleStringProperty totalQuantityProperty() {
        return totalQuantity;
    }

    Spinner<Double> quantitySpinner = new Spinner<Double>();


    public Spinner<Double> getQuantitySpinner() {
        return quantitySpinner;
    }

    public void setQuantitySpinner(Spinner<Double> quantitySpinner) {
        this.quantitySpinner = quantitySpinner;
    }


    public Button getAddButton() {
        return addButton;
    }

    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }



    public void setPrice(String price) {
        Price.set(price);
    }

    public void setName(String name) {
        Name.set(name);
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getPrice() {
        return Price.get();
    }

    public String getID() {
        return ID.get();
    }



    public void setPurchaseCategory(String purchaseCategory) {
        this.purchaseCategory.set( purchaseCategory);
    }




    public String getName() {
        return Name.get();
    }

    public String getPurchaseCategory() {
        return purchaseCategory.get();
    }

    public String getId() {
        return ID.get();
    }




}

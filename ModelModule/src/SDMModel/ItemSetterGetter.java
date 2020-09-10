package SDMModel;


import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.io.Serializable;

public class ItemSetterGetter implements Serializable {

    public ItemSetterGetter( String price, String name, String ID, String purchaseCategory, Order order, Store store,SystemManager sys) {
        this.Price = new SimpleStringProperty(price);
        this.Name = new SimpleStringProperty(name);
        this.ID = new SimpleStringProperty(ID);
        this.purchaseCategory = new SimpleStringProperty(purchaseCategory);
        this.totalPrice = new SimpleStringProperty("0");
        this.totalQuantity = new SimpleStringProperty("0");
        this.addButton = new Button("Add");
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
                sys.addAnItemToOrder(order, store, itemID, quantitySpinner.getValue());
                double totalDobuleQuantity = order.getItemsQuantity().get(sys.getSuperMarket().getItemByID(itemID));
                totalQuantity.set(String.valueOf(totalDobuleQuantity)); // can be taken from order..
                totalPrice.set(String.valueOf(Math.round(totalDobuleQuantity * Double.parseDouble(price) * 100 / 100)));
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

    public void setTotalPrice(String totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity.set(totalQuantity);
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

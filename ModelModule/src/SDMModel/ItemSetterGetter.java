package SDMModel;


import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.io.Serializable;

public class ItemSetterGetter implements Serializable {

    public ItemSetterGetter( String price, String name, String ID, String purchaseCategory, Order order, SystemManager sys) {
        Price = new SimpleStringProperty(price);
        Name = new SimpleStringProperty(name);
        this.ID = new SimpleStringProperty(ID);
        this.purchaseCategory = new SimpleStringProperty(purchaseCategory);
        this.addButton = new Button("Purchase");
        if (purchaseCategory.equals("Weight")) {
            SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 500, 0, 0.1);
            quantitySpinner.setValueFactory(valueFactory);
        } else {
            SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 500, 0, 1);
            quantitySpinner.setValueFactory(valueFactory);
        }
        addButton.setOnAction(x -> {
            Store storeLowestItemPrice = sys.getItemLowestPrice(Integer.parseInt(ID));
            sys.addAnItemToOrder(order, storeLowestItemPrice, Integer.parseInt(ID),quantitySpinner.getValue());
        });
    }
    private SimpleStringProperty Price;
    private SimpleStringProperty purchaseCategory;
    private SimpleStringProperty Name;
    private SimpleStringProperty ID;
    private Button addButton;
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

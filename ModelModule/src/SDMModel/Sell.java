package SDMModel;


import SDMGenerated.SDMSell;

import java.io.Serializable;

public class Sell implements Serializable {

    public enum InfoOptions {
        Price, ID, TimesWasSold;

        public String getInfo(Sell sell) {
            switch (this) {
                case ID:
                    return String.valueOf(sell.getItemId());
                case Price:
                    return String.valueOf(sell.getPrice());
                case TimesWasSold:
                    return String.valueOf(sell.getNumberOfTimesItemWasSold());
                default:
                    return "Unknown";
            }
        }
    }

    private double price;
    private int itemId;
    private double numberOfTimesItemWasSold;
    private Item item;

    public static Sell createInstanceBySDM(SDMSell sell) {
        Sell newSell= new Sell();
        newSell.setItemId(sell.getItemId());
        newSell.setPrice(sell.getPrice());
        return newSell;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getNumberOfTimesItemWasSold() {
        return numberOfTimesItemWasSold;
    }

    public void increaseNumberOfTimesItemWasSold(double quantity) {
        this.numberOfTimesItemWasSold = numberOfTimesItemWasSold + quantity;
    }

}

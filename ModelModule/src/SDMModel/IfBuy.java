package SDMModel;

import SDMGenerated.IfYouBuy;


public class IfBuy {

    private double quantity;
    private int itemId;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }


    public static IfBuy createInstanceBySDM(IfYouBuy ifYouBuy) {
        IfBuy needToBuy = new IfBuy();
        needToBuy.setItemId(ifYouBuy.getItemId());
        needToBuy.setQuantity(ifYouBuy.getQuantity());
        return  needToBuy;
    }
}

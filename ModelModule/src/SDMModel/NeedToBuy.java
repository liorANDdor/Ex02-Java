package SDMModel;

import SDMGenerated.IfYouBuy;


public class NeedToBuy {

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


    public static NeedToBuy createInstanceBySDM(IfYouBuy ifYouBuy) {
        NeedToBuy needToBuy = new NeedToBuy();
        needToBuy.setItemId(ifYouBuy.getItemId());
        needToBuy.setQuantity(ifYouBuy.getQuantity());
        return  needToBuy;
    }
}

package SDMModel;


import SDMGenerated.SDMOffer;

public class Offer {
    public Offer(){}
    public Offer(int itemId, double quantity, int forAdditional){
        this.itemId = itemId;
        this.quantity = quantity;
        this.forAdditional = forAdditional;
    }

    private double quantity;
    private int itemId;
    private int forAdditional;

    public static Offer createInstanceBySDM(SDMOffer sdmOffer) {
        Offer offer = new Offer();
        offer.setForAdditional(sdmOffer.getForAdditional());
        offer.setItemId(sdmOffer.getItemId());
        offer.setQuantity(sdmOffer.getQuantity());
        return offer;
    }

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



    public int getForAdditional() {
        return forAdditional;
    }

    public void setForAdditional(int forAdditional) {
        this.forAdditional = forAdditional;
    }


}

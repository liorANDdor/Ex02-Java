package SDMModel;

import SDMGenerated.SDMDiscount;

public class Sale {


    protected String name;
    protected IfBuy ifBuy;
    protected NeedToGet needToGet;
    protected Store storeOfferSale;


    public Store getStoreOfferSale() {
        return storeOfferSale;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IfBuy getIfBuy() {
        return ifBuy;
    }

    public void setIfBuy(IfBuy ifBuy) {
        this.ifBuy = ifBuy;
    }


    public NeedToGet getNeedToGet() {
        return needToGet;
    }

    public void setNeedToGet(NeedToGet needToGet) {
        this.needToGet = needToGet;
    }

    public static Sale createInstanceBySDM(SDMDiscount discount) {
        Sale newSale = new Sale();
        newSale.setName(discount.getName());
        newSale.setNeedToGet(NeedToGet.createInstanceBySDM(discount.getThenYouGet()));
        newSale.setIfBuy(IfBuy.createInstanceBySDM(discount.getIfYouBuy()));
        return newSale;
    }

    public void setStore(Store newStore) {
        storeOfferSale = newStore;
    }
}

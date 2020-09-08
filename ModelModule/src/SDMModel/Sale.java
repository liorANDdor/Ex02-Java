package SDMModel;

import SDMGenerated.SDMDiscount;

public class Sale {

    protected String name;
    protected NeedToBuy needToBuy;
    protected NeedToGet needToGet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NeedToBuy getNeedToBuy() {
        return needToBuy;
    }

    public void setNeedToBuy(NeedToBuy needToBuy) {
        this.needToBuy = needToBuy;
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
        newSale.setNeedToBuy(NeedToBuy.createInstanceBySDM(discount.getIfYouBuy()));
        return newSale;
    }
}

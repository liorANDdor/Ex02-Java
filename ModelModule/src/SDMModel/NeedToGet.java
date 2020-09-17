package SDMModel;

import SDMGenerated.SDMOffer;
import SDMGenerated.ThenYouGet;

import java.util.ArrayList;
import java.util.List;

public class NeedToGet {
    public NeedToGet(String operator){
        this.operator = operator;
        this.sdmOffer = new ArrayList<Offer>();
    }
    public NeedToGet(){

    }

    public List<Offer> getOffers() {
        return sdmOffer;
    }

    public void setSdmOffer(List<Offer> sdmOffer) {
        this.sdmOffer = sdmOffer;
    }

    public List<Offer> getSdmOffer() {
        return sdmOffer;
    }

    private List<Offer> sdmOffer = new ArrayList<>();

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    private String operator; //maybe change to Enum
    public static NeedToGet createInstanceBySDM(ThenYouGet thenYouGet) {
        NeedToGet needToGet = new NeedToGet();
        needToGet.setOperator(thenYouGet.getOperator());
        List<SDMOffer>sdmOffers=thenYouGet.getSDMOffer();
        for(SDMOffer sdmOffer : sdmOffers){
            Offer offer = Offer.createInstanceBySDM(sdmOffer);
            needToGet.getOffers().add(offer);
        }
        return needToGet;
    }
}

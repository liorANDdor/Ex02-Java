package SDMModel;

import SDMGenerated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class XmlUtilities {

    public String getWhatWrongMessage() {
        return whatWrongMessage;
    }
    private String whatWrongMessage ="";
    private boolean isXmlOk = true;

    public boolean getIsXmlOk() {
        return isXmlOk;
    }

    public SuperDuperMarketDescriptor loadFile(String fullPath) {
        SuperDuperMarketDescriptor instance = null;
        if (isXmlOk) {

            try {
                File file = new File(fullPath);
                JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
                Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
                instance = (SuperDuperMarketDescriptor) jaxbUnMarshaller.unmarshal(file);
            } catch (JAXBException e) {
                isXmlOk= false;
                whatWrongMessage = "Unknown file";
            } catch (Exception e) {
                isXmlOk = false;
                whatWrongMessage = "Unknown file";
            }
        }
        return instance;
    }

    public void checkIfTheXmlThatJustLoadedOk(SuperDuperMarketDescriptor superMarketSDM) {
        AtomicBoolean isContentAsNeeded = new AtomicBoolean(true);
        if (isXmlOk) {
            List<SDMStore> stores = superMarketSDM.getSDMStores().getSDMStore();
            List<SDMItem> items = superMarketSDM.getSDMItems().getSDMItem();
            List<SDMCustomer> customers = superMarketSDM.getSDMCustomers().getSDMCustomer();

            for (int i = 0; i < stores.size(); i++)
                for (int j = i + 1; j < stores.size(); j++)
                    if (stores.get(i).getId() == stores.get(j).getId()) {
                        isContentAsNeeded.set(false);
                        whatWrongMessage += String.format("There is two stores with the same ID : %d \n", stores.get(i).getId());
                    }

            for (int i = 0; i < items.size(); i++) {
                for (int j = i + 1; j < items.size(); j++) {
                    if (items.get(i).getId() == items.get(j).getId()) {
                        isContentAsNeeded.set(false);
                        whatWrongMessage += String.format("There is two items with the same ID : %d \n", items.get(i).getId());
                    }
                }
            }

            for (int i = 0; i < customers.size(); i++) {
                for (int j = i + 1; j < customers.size(); j++) {
                    if (customers.get(i).getId() == customers.get(j).getId()) {
                        isContentAsNeeded.set(false);
                        whatWrongMessage += String.format("There is two customers with the same ID : %d \n", customers.get(i).getId());
                    }
                }
            }

            for (int i = 1; i <= 50; i++) {
                for (int j = 1; j <= 50; j++) {
                    Point pointToCheck = new Point(i, j);
                    int count = 0;
                    count += stores
                            .stream()
                            .filter(store -> store.getLocation().getX() == pointToCheck.x && store.getLocation().getY() == pointToCheck.getY())
                            .count();
                    count += customers
                            .stream()
                            .filter(customer -> customer.getLocation().getX() == pointToCheck.x && customer.getLocation().getY() == pointToCheck.getY())
                            .count();
                    if (count > 1) {
                        isContentAsNeeded.set(false);
                        whatWrongMessage += String.format("There is two object on same location: %s", pointToCheck.toString());
                    }
                }
            }

            for (SDMStore store : stores) {
                for (SDMSell itemOfStore : store.getSDMPrices().getSDMSell()) {
                    boolean theItemExists = items.stream()
                            .filter(item -> item.getId() == itemOfStore.getItemId())
                            .count() >= 1;
                    if (!theItemExists) {
                        isContentAsNeeded.set(false);
                        whatWrongMessage += String.format(
                                "The item of id %d that the store %s is want to sell doesnt exits\n",
                                itemOfStore.getItemId(), store.getName());
                    }
                }
            }

            for (SDMItem item : items) {
                boolean itemIsAvailable = stores.stream().filter(sdmStore ->
                        sdmStore.getSDMPrices().getSDMSell().stream().anyMatch(price -> price.getItemId() == item.getId())
                ).count() >= 1;
                if (!itemIsAvailable) {
                    isContentAsNeeded.set(false);
                    whatWrongMessage += String.format(
                            "There is no store that sell the item of id %d\n",
                            item.getId());
                }
            }

            for (SDMStore store : stores) {
                List<SDMSell> sells = store.getSDMPrices().getSDMSell();
                for (SDMSell sell : sells) {
                    boolean moreThanOnce = sells.stream()
                            .filter(i -> i.getItemId() == sell.getItemId())
                            .count() > 1;
                    if (moreThanOnce) {
                        isContentAsNeeded.set(false);
                        whatWrongMessage += String.format(
                                "The store %s sells this item (id %d) more than once\n",
                                store.getName(), sell.getItemId());
                    }
                }
            }

            for (SDMStore store : stores) {
                Location location = store.getLocation();
                if (location.getY() > 50 || location.getX() > 50 || location.getX() < 1 || location.getY() < 1) {
                    isContentAsNeeded.set(false);
                    whatWrongMessage += String.format("The location of store %s is incorrect should be 1-50\n",
                            store.getName());
                }
            }

            for(SDMStore sdmStore : stores) {
                if (sdmStore.getSDMDiscounts() != null) {
                    List<SDMDiscount> sdmDiscounts = sdmStore.getSDMDiscounts().getSDMDiscount();
                    for (SDMDiscount sdmDiscount : sdmDiscounts) {
                        boolean isStoreSellsTheIfBuy = sdmStore.getSDMPrices().getSDMSell()
                                .stream()
                                .anyMatch(sdmSell -> sdmSell.getItemId() == sdmDiscount.getIfYouBuy().getItemId());
                        if (!isStoreSellsTheIfBuy) {
                            isContentAsNeeded.set(false);
                            whatWrongMessage += String.format("The store %s doesnt sell the item(ID) %s as mentioned in discounts\n"
                                    , sdmStore.getName(), sdmDiscount.getIfYouBuy().getItemId());
                        }
                        sdmDiscount.getThenYouGet().getSDMOffer().stream().forEach(offer -> {
                            boolean isStoreSellTheOffer = sdmStore.getSDMPrices().getSDMSell()
                                    .stream()
                                    .anyMatch(sdmsell -> offer.getItemId() == sdmsell.getItemId());
                            if (!isStoreSellTheOffer) {
                                isContentAsNeeded.set(false);
                                whatWrongMessage += String.format("The store %s doesnt sell the item(ID) %s as mentioned in discounts\n"
                                        , sdmStore.getName(), offer.getItemId());
                            }
                        });
                    }
                }
            }

            isXmlOk = isContentAsNeeded.get();
        }


    }

    public void isNameOfFileCorrect(String fullPath) {

        if(!(fullPath.split("\\.")[fullPath.split("\\.").length - 1].equals("xml"))){
            whatWrongMessage = "Your file should end with .xml";
            isXmlOk = false;
        }
    }

}

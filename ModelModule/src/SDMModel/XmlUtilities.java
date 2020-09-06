package SDMModel;

import SDMGenerated.*;
import com.sun.jmx.remote.internal.Unmarshal;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.List;

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


    public void WriteDataToFile(String fullPath, HashMap<Integer, Order> orders) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(fullPath))) {
            out.writeObject(orders);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HashMap<Integer, Order> ReadDataFromFile(String fullPath)
    {
        HashMap<Integer, Order> orders = new HashMap<Integer, Order>();
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(fullPath))) {
            // we know that we read array list of Persons
            orders =(HashMap<Integer, Order>) in.readObject();

        } catch (IOException e) {
            System.out.println("Cannot find file");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot find file");
        }

        return orders;

    }



    public void checkIfTheXmlThatJustLoadedOk(SuperDuperMarketDescriptor superMarketSDM) {
        boolean isContentAsNeeded = true;
        if (isXmlOk) {
            List<SDMStore> stores = superMarketSDM.getSDMStores().getSDMStore();
            List<SDMItem> items = superMarketSDM.getSDMItems().getSDMItem();

            for (int i = 0; i < stores.size(); i++)
                for (int j = i + 1; j < stores.size(); j++)
                    if (stores.get(i).getId() == stores.get(j).getId()) {
                        isContentAsNeeded = false;
                        whatWrongMessage += String.format("There is two stores with the same ID : %d \n", stores.get(i).getId());
                    }

            for (int i = 0; i < items.size(); i++) {
                for (int j = i + 1; j < items.size(); j++) {
                    if (items.get(i).getId() == items.get(j).getId()) {
                        isContentAsNeeded = false;
                        whatWrongMessage += String.format("There is two items with the same ID : %d \n", items.get(i).getId());
                    }
                }
            }

            for (SDMStore store : stores) {
                for (SDMSell itemOfStore : store.getSDMPrices().getSDMSell()) {
                    boolean theItemExists = items.stream()
                            .filter(item -> item.getId() == itemOfStore.getItemId())
                            .count() >= 1;
                    if (!theItemExists) {
                        isContentAsNeeded = false;
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
                    isContentAsNeeded = false;
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
                        isContentAsNeeded = false;
                        whatWrongMessage += String.format(
                                "The store %s sells this item (id %d) more than once\n",
                                store.getName(), sell.getItemId());
                    }
                }
            }

            for (SDMStore store : stores) {
                Location location = store.getLocation();
                if (location.getY() > 50 || location.getX() > 50 || location.getX() < 1 || location.getY() < 1) {
                    isContentAsNeeded = false;
                    whatWrongMessage += String.format("The location of store %s is incorrect should be 1-50",
                            store.getName());
                }
            }
            isXmlOk = isContentAsNeeded;
        }


    }

    public void isNameOfFileCorrect(String fullPath) {

        if(!(fullPath.split("\\.")[fullPath.split("\\.").length - 1].equals("xml"))){
            whatWrongMessage = "Your file should end with .xml";
            isXmlOk = false;
        }
    }

}

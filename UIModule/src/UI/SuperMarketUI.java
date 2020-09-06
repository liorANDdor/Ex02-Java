package UI;

import SDMModel.*;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SuperMarketUI {

    private SystemManager systemManager = new SystemManager();
    private Scanner scanner = new Scanner(System.in);

    public enum eMainMenu {
        LoadFile,
        ShowStores,
        ShowAllItems,
        CreateAnOrder,
        ShowHistory,
        SaveOrders,
        LoadOrders,
        Exit
    }

    private  eMainMenu handleSelection(int length) {

        int userSelectionAsInt = 0;
        boolean validOption = false;
        do {
            userSelectionAsInt = IO.getInt();
            if (userSelectionAsInt <= length && userSelectionAsInt >= 1)
                validOption = true;
            else
                System.out.println("Input not in range");
        }
        while (!validOption);

        return eMainMenu.values()[userSelectionAsInt - 1];
    }

    public  void run() {
        eMainMenu userSelection;
        do {
            int i = 0;
            for (eMainMenu e : eMainMenu.values()) {
                i++;
                System.out.println(i + ". " + String.join(" ", e.toString().split("(?=[A-Z])")));
            }
            userSelection = handleSelection(eMainMenu.values().length);

            switch (userSelection) {
                case LoadFile:
                    loadXMLFile();
                    break;
                case ShowStores:
                    System.out.println("Stores:");
                    showStores();
                    break;
                case ShowAllItems:
                    System.out.println("Items");
                    showItems();
                    break;
                case CreateAnOrder:
                    createOrder();
                    break;
                case ShowHistory:
                    showHistory();
                    break;
                case SaveOrders:
                    saveOrders();
                    break;
                case LoadOrders:
                    loadOrders();
                    break;
            }

        } while (!userSelection.equals(eMainMenu.Exit)) ;

    }

    private void saveOrders() {
        if(systemManager.isXmlLoaded()) {
            System.out.println("Please enter full path to which we will save the file:");
            String fullPathToSave = scanner.nextLine();
            systemManager.saveOrders(fullPathToSave);
        }
        else {
            System.out.println("Need to load XML first");
        }
    }

    private void loadOrders(){
        if(systemManager.isXmlLoaded()) {
            System.out.println("Please enter full path from which we will load the file:");
            String fullPathToLoad = scanner.nextLine();
            systemManager.loadOrders(fullPathToLoad);
        }
        else {
            System.out.println("Need to load XML first");
        }
    }

    private void showStores() {
        if(systemManager.isXmlLoaded()){
            HashMap<Integer, Store> stores = systemManager.getSuperMarket().getStores();
            for(Store store:stores.values()) {
                System.out.println("#################################################");
                printStoreAndSells(store);
                System.out.println("\n");
            }
        }
        else
            System.out.println("You should load an xml file");
    }

    private void showItems() {
        if(systemManager.isXmlLoaded()){
            HashMap<Integer, Item> items = systemManager.getSuperMarket().getItems();
            for(Item item:items.values()) {
                System.out.println("#################################################");
                printItem(item);
                System.out.println("\n");
            }
        }
        else
            System.out.println("You should load an xml file");
    }

    private void printStoreAndSells(Store store) {
        printStore(store);
        System.out.println("Store Items: \n");
        for(Sell sell:store.getItemsToSell()) {
            printSellOffer(sell);
        }
        System.out.println("Store Orders: \n");
        for(Order order:store.getOrders().values()) {
            List<Order.InfoOptions> list = new ArrayList<>();
            list.add(Order.InfoOptions.Date);
            list.add(Order.InfoOptions.AmountOfAllItems);
            list.add(Order.InfoOptions.ItemsPrice);
            list.add(Order.InfoOptions.ShipmentPrice);
            list.add(Order.InfoOptions.TotalPrice);
            System.out.println(systemManager.getinfoOrder(order, list));


        }

    }

    private void printStore(Store store) {
        List<Store.InfoOptions>list=new LinkedList<>();
        list.add(Store.InfoOptions.Id);
        list.add(Store.InfoOptions.Name);
        list.add(Store.InfoOptions.DeliveryPpk);
        list.add(Store.InfoOptions.TotalEarning);
        System.out.println(systemManager.getStoreInfo(store,list));
    }

    private void printSellOffer(Sell sell) {
        Item itemToSell = systemManager.getSuperMarket().getItemByID(sell.getItemId());
        List <Item.InfoOptions> itemAttributes = new ArrayList<Item.InfoOptions>();
        List <Sell.InfoOptions> sellAttributes = new ArrayList<>();
        itemAttributes.add(Item.InfoOptions.ItemId);
        itemAttributes.add(Item.InfoOptions.Name);
        itemAttributes.add(Item.InfoOptions.Category);
        StringBuilder SellInfo = systemManager.getinfoItem(itemToSell,itemAttributes);
        sellAttributes.add(Sell.InfoOptions.Price);
        sellAttributes.add(Sell.InfoOptions.TimesWasSold);
        System.out.println(SellInfo.append(systemManager.getInfoSell(sell, sellAttributes)));
    }

    private void printItem(Item item) {
        System.out.println("________");
        List <Item.InfoOptions>list = new ArrayList<Item.InfoOptions>();
        printItemIDNamePPK(item);
        list.add(Item.InfoOptions.NumberOfStoresSellTheItem);
        list.add(Item.InfoOptions.ItemAveragePrice);
        list.add(Item.InfoOptions.NumberOfTimesItemWasSold);
        System.out.println(systemManager.getinfoItem(item,list));

    }

    private void printItemIDNamePPK(Item item) {
        List<Item.InfoOptions> list = new ArrayList<Item.InfoOptions>();
        list.add(Item.InfoOptions.ItemId);
        list.add(Item.InfoOptions.Name);
        list.add(Item.InfoOptions.Category);
        System.out.print(systemManager.getinfoItem(item, list));
    }

    private void loadXMLFile() {
        System.out.println("Please enter full path of your XML file.");
        String fullPath = scanner.nextLine();
        systemManager.LoadXMLFileAndCheckIt(fullPath);
        if(systemManager.getXmlUtilities().getIsXmlOk())
            System.out.println("Loadeded successfully");
        else{
            System.out.println("Not loadeded successfully \n");
            System.out.println(systemManager.getXmlUtilities().getWhatWrongMessage());
        }
    }

    private void createOrder() {
        if(systemManager.isXmlLoaded()) {
            String isStatic;
            System.out.println("Would you like us to shop the items dynamically for you? (Yes/No)");
            do {
                isStatic = scanner.nextLine();
                if (isStatic.equals("Yes"))
                    createDynamicOrder();
                else if (isStatic.equals("No"))
                    createStaticOrder();
                else
                    System.out.println("Please choose 'Yes' or 'No' only");
            }while (!isStatic.equals("Yes") && !isStatic.equals("No"));
        }
        else
            System.out.println("You should load an xml file");
    }

    private void createDynamicOrder() {

        Order emptyOrder = systemManager.getEmptyOrder();
        System.out.println("Please enter delivery date (dd/mm-hh:mm format)");
        verifyAndSetDate(emptyOrder);
        System.out.println("Please enter a location");
        verifyAndSetLocation(emptyOrder);


        systemManager.getSuperMarket().getItems().forEach((id, item) -> {
            printItemIDNamePPK(item);
            Store storeLowestItemPrice = systemManager.getItemLowestPrice(item.getId());
            System.out.println("Item price: " + storeLowestItemPrice.getItemPrice(item.getId()) + "\n");
        });
        boolean isContinue = true;
        while (isContinue) {
            System.out.println("Please choose item by putting its ID");
            int itemId = IO.getInt();
            while (!systemManager.isValidItemId(itemId)) {
                System.out.println("Unknown item ID, try again (by ID)");
                itemId = IO.getInt();
            }
            Item.PurchaseCategory category = systemManager.getPurchaseCategory(itemId);
            double quantity;

            System.out.println("Please Enter QUANTITY (" + category + ")");
            if (category.equals(Item.PurchaseCategory.QUANTITY))
                quantity = IO.getInt();
            else
                quantity = IO.getDouble();
            Store storeLowestItemPrice = systemManager.getItemLowestPrice(itemId);
            systemManager.addAnItemToOrder(emptyOrder, storeLowestItemPrice, itemId, quantity);
            System.out.println("Item was added to order");
            System.out.println("Press Q if you dont want another item, or any key to continue");
            String userWantToContinue = scanner.nextLine();
            if (userWantToContinue.equals("q") || userWantToContinue.equals("Q"))
                isContinue = false;

        }
        if(emptyOrder.getItemsQuantity().size() != 0) {
            getOrderInfo(emptyOrder);
            System.out.println("Press Y if you to commit the order");
            String userWantToCommit = scanner.nextLine();
            if (userWantToCommit.equals("y") || userWantToCommit.equals("Y")) {
                systemManager.commitOrder(emptyOrder);
            }
        }
    }

    private void createStaticOrder() {
        Order emptyOrder = systemManager.getEmptyOrder();
        systemManager.getSuperMarket().getStores().forEach((id, store) -> printStore(store));
        System.out.println("Please Choose a store (by ID)");
        int storeID = IO.getInt();
        while (!systemManager.isValidStoreId(storeID)) {
            System.out.println("Unknown ID, try again (by ID)");
            storeID = IO.getInt();
        }
        systemManager.setStoreOfOrderByID(storeID, emptyOrder);
        System.out.println("Please enter delivery date (dd/mm-hh:mm format)");
        verifyAndSetDate(emptyOrder);
        System.out.println("Please enter a location");
        verifyAndSetLocation(emptyOrder);

        int finalStoreID = storeID;
        systemManager.getSuperMarket().getItems().forEach((id, item) -> {
            printItemIDNamePPK(item);
            String itemPrice = systemManager.getPriceOfItemByStoreId(item, finalStoreID);
            System.out.println("Item price: " + itemPrice + "\n");
        });
        boolean isContinue = true;
        while (isContinue) {
            System.out.println("Please choose item by putting its ID");
            int itemId = IO.getInt();
            while (!systemManager.isValidItemId(itemId)) {
                System.out.println("Unknown item ID, try again (by ID)");
                itemId = IO.getInt();
            }
            Item.PurchaseCategory category = systemManager.getPurchaseCategory(itemId);
            double quantity;
            if (systemManager.checkIfStoreSellAnItem(storeID, itemId)) {
                System.out.println("Please Enter QUANTITY (" + category + ")");
                if (category.equals(Item.PurchaseCategory.QUANTITY))
                    quantity = IO.getInt();
                else
                    quantity = IO.getDouble();
                Store storeToBuyFrom = systemManager.getSuperMarket().getStores().get(storeID);
                systemManager.addAnItemToOrder(emptyOrder, storeToBuyFrom, itemId, quantity);
                System.out.println("Item was added to order");
            } else
                System.out.println("this item is not an option");

            System.out.println("Press Q if you dont want another item, or any key to continue");
            String userWantToContinue = scanner.nextLine();
            if (userWantToContinue.equals("q") || userWantToContinue.equals("Q"))
                isContinue = false;

        }
        if (emptyOrder.getItemsQuantity().size() != 0) {
            getOrderInfo(emptyOrder);
            System.out.println("Press Y if you to commit the order");
            String userWantToCommit = scanner.nextLine();
            if (userWantToCommit.equals("y") || userWantToCommit.equals("Y"))
                systemManager.commitOrder(emptyOrder);
        }
    }

    private void verifyAndSetLocation(Order order) {

        boolean isfixedLocation = false;
        do {
            System.out.print("X: ");
            Integer xCordinate = IO.getInt();
            System.out.print("Y: ");
            Integer yCordinate = IO.getInt();
            try {
                systemManager.isfixedLocationAndSetToOrder(new Point(xCordinate, yCordinate), order);
                isfixedLocation = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } while (!isfixedLocation);

    }

    private void verifyAndSetDate(Order order) {
        boolean fixdate = false;
        while (fixdate == false) {
            String dateFromUser = scanner.nextLine();
            if(dateFromUser.length()==11) {
                try {
                    Date date = new SimpleDateFormat("dd/MM-hh:mm").parse(dateFromUser);
                    date.setYear(120);
                    fixdate = true;
                    order.setDateOfOrder(date);
                } catch (Exception e) {
                    System.out.println("The date that selected not fix, please select fix date");
                    fixdate = false;
                }
            }
            else
                System.out.println("The date that selected not fix, please select fix date");

        }
    }

    public void getOrderInfo(Order order){
        List <Item.InfoOptions> itemAttributes = new ArrayList<>();
        itemAttributes.add(Item.InfoOptions.ItemId);
        itemAttributes.add(Item.InfoOptions.Name);
        itemAttributes.add(Item.InfoOptions.Category);
        Integer itemID;
        double itemQuantity;
        double itemPrice;
        for (Item item: order.getItemsQuantity().keySet()){
            itemID = item.getId();
            System.out.print(systemManager.getinfoItem(item, itemAttributes));
            itemPrice = order.getItemPrice(itemID);
            itemQuantity = order.getItemsQuantity().get(item);
            System.out.println("Item price: " + itemPrice);
            System.out.println("Item quantity: " + itemQuantity);
            System.out.println("Item total price: " + ((itemPrice * itemQuantity) * 1000d) / 1000d +"\n");
        }
        for(Store store: order.getStoresToOrderFrom().keySet())
            printDistanceAndPPK(store, order);
    }

    private void printDistanceAndPPK(Store store, Order order) {
        Point clientLocation = order.getLocationOfClient();
        Point storeLocation = store.getLocation();
        order.setDeliveryDistance(Math.sqrt((clientLocation.x - storeLocation.x) *(clientLocation.x - storeLocation.x)
                + (clientLocation.y - storeLocation.y) *(clientLocation.y - storeLocation.y)));
        order.setShipmentPrice(order.getDeliveryDistance() * store.getDeliveryPpk());
        System.out.println("\n\nDistance from " + store.getName() + " store: " + order.getDeliveryDistance());
        System.out.println("PPK of store: " + store.getDeliveryPpk());
        System.out.println("Shipment total price: " + (double)Math.round(  order.getShipmentPrice() * 1000d) / 1000d);

    }

    private void showHistory() {
        if(systemManager.isXmlLoaded()){
            HashMap<Integer,Order> allOrders =  systemManager.getSuperMarket().getOrders();
            System.out.println("History orders \n");
            for(Order order: allOrders.values()) {
                System.out.println("________");
                List<Order.InfoOptions> list = new ArrayList<>();
                list.add(Order.InfoOptions.OrderId);
                list.add(Order.InfoOptions.Date);
                list.add(Order.InfoOptions.StoresThatSupplyTheOrder);
                list.add(Order.InfoOptions.AmountOfKindsOfItems);
                list.add(Order.InfoOptions.AmountOfAllItems);
                list.add(Order.InfoOptions.ItemsPrice);
                list.add(Order.InfoOptions.ShipmentPrice);
                list.add(Order.InfoOptions.TotalPrice);
                System.out.println(systemManager.getinfoOrder(order, list));

            }


        }
        else
            System.out.println("You should load an xml file");

    }

}



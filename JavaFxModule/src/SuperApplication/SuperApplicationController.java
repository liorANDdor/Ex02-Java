package SuperApplication;

import OrderWindow.OrderController;
import SDMModel.Item;
import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tile.tileController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SuperApplicationController {

    SystemManager systemManager = new SystemManager();
    private Map<Integer, Node> itemsTilesController = new HashMap<>();
    private Map<Integer, Node> storesTilesController = new HashMap<>();
    @FXML
    private FlowPane myPane;

    @FXML
    void addSaleHandler(ActionEvent event) {

    }

    @FXML
    void deleteItemFromStoreHandler(ActionEvent event) throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../OrderWindow/Order.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        OrderController o = fxmlLoader.getController();
        stg.initModality(Modality.WINDOW_MODAL);
        // newWindow.initOwner(primaryStage);
        Scene scene = new Scene(root, 200, 200);

        stg.setTitle("asd");
        stg.setScene(scene);
        stg.show();
    }

    @FXML
    void loadXMLHandler(ActionEvent event)   {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        System.out.println(selectedFile.getAbsolutePath());
        systemManager.LoadXMLFileAndCheckIt(selectedFile.getAbsolutePath());
        systemManager.getSuperMarket().getStores().values().stream().forEach(store -> {
            printStore(store);
        });

    }

    private void printStore(Store store) {
        List<Store.InfoOptions> list=new LinkedList<>();
        list.add(Store.InfoOptions.Id);
        list.add(Store.InfoOptions.Name);
        list.add(Store.InfoOptions.DeliveryPpk);
        list.add(Store.InfoOptions.TotalEarning);
        System.out.println(systemManager.getStoreInfo(store,list));
    }

    @FXML
    void showCustomersHandler(ActionEvent event) {

    }

    @FXML
    void showItemsHandler(ActionEvent event) {
        myPane.getChildren().clear();
        HashMap<Integer, Item> items = systemManager.getSuperMarket().getItems();
        for(Item item: items.values()){
            createItemTile(item);
        }
    }

    private void createItemTile(Item item) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("../tile/tile.fxml");
            fxmlLoader.setLocation(url);
            Node singleWordTile = fxmlLoader.load();
            List<Item.InfoOptions> list = new ArrayList<Item.InfoOptions>();
            //printItemIDNamePPK(item);

            list.add(Item.InfoOptions.ItemId);
            list.add(Item.InfoOptions.Name);
            list.add(Item.InfoOptions.Category);
            list.add(Item.InfoOptions.NumberOfStoresSellTheItem);
            list.add(Item.InfoOptions.ItemAveragePrice);
            list.add(Item.InfoOptions.NumberOfTimesItemWasSold);
            tileController tileController = fxmlLoader.getController();

            tileController.initialize(systemManager.getinfoItem(item,list));
            Platform.runLater(
                    () -> tileController.setName(item.getName())
            );

            myPane.getChildren().add(singleWordTile);
            itemsTilesController.put(item.getId(), singleWordTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createStoreTile(Store store) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("../tile/tile.fxml");
            fxmlLoader.setLocation(url);
            Node singleWordTile = fxmlLoader.load();
            List<Store.InfoOptions> list = new ArrayList<Store.InfoOptions>();
            //printItemIDNamePPK(item);

            list.add(Store.InfoOptions.Id);
            list.add(Store.InfoOptions.Name);
            list.add(Store.InfoOptions.DeliveryPpk);
            tileController tileController = fxmlLoader.getController();
            tileController.initialize(systemManager.getStoreInfo(store,list));
            Platform.runLater(
                    () -> tileController.setName(store.getName())
            );

            myPane.getChildren().add(singleWordTile);
            storesTilesController.put(store.getId(), singleWordTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void showMapHandler(ActionEvent event) {

    }

    @FXML
    void showOrdersHandler(ActionEvent event) {

    }

    @FXML
    void showStoresHandler(ActionEvent event) {
        myPane.getChildren().clear();
        HashMap<Integer, Store> items = systemManager.getSuperMarket().getStores();
        for(Store store: items.values()){
            createStoreTile(store);
        }
    }

}

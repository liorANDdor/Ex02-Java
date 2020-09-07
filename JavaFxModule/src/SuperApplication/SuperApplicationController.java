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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SuperApplicationController {

    SystemManager systemManager = new SystemManager();
    private Map<Integer, Node> itemsTilesController;

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
        HashMap<Integer, Item> items = systemManager.getSuperMarket().getItems();
        for(Item item: items.values()){
            createItemTile(item);
        }
    }

    private void createItemTile(Item item) {

        try {
            Stage stg = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("../tile/tile.fxml");
            fxmlLoader.setLocation(url);
            Node singleWordTile = fxmlLoader.load();
            tileController tileController = fxmlLoader.getController();
            tileController.initialize(item,systemManager);
            Platform.runLater(
                    () -> tileController.setName(item.getName())
            );


            myPane.getChildren().add(singleWordTile);
            //itemsTilesController.put(item.getId(), singleWordTile);



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

    }

}

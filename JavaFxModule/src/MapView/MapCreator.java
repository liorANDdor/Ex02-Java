package MapView;

import SDMModel.Customer;
import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tile.tileController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapCreator {
    SystemManager systemManager = SystemManager.getInstance();

    public Stage getMap() throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../MapView/MapView.fxml");
        fxmlLoader.setLocation(url);
        GridPane root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        root.setPadding(new Insets(17,17,17,5));
        int rows = systemManager.getMaxRows() + 2;
        int cols = systemManager.getMaxCols() + 2;
        HashMap<Integer, Customer> costumers = systemManager.getSuperMarket().getCostumers();
        HashMap<Integer, Store> stores = systemManager.getSuperMarket().getStores();
        for (int i = 1; i <= rows; i++) {
            RowConstraints r = new RowConstraints();
            r.setMinHeight(10);
            r.setPrefHeight(10);
            r.setVgrow(Priority.SOMETIMES);
            root.getRowConstraints().add(r);
        }
        for (int i = 1; i <= cols; i++) {
            ColumnConstraints r = new ColumnConstraints();
            r.setMinWidth(10);
            r.setPrefWidth(10);
            r.setHgrow(Priority.SOMETIMES);
            root.getColumnConstraints().add(r);
        }

        stores.values().stream().forEach(store -> {
            Button btn = new Button();
            BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("../MapView/store.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            Background background = new Background(backgroundImage);

            btn.setBackground(background);
            Point p = store.getLocation();
            Stage stage = null;
            try {
                stage = getStageOfStore(store);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage finalStage = stage;
            btn.setOnAction((e)-> finalStage.show());
            root.add(btn, (int) p.getY(), (int) p.getX());
        });

        costumers.values().stream().forEach(customer -> {
            Button btn = new Button();
            BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("../MapView/user.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            Background background = new Background(backgroundImage);
            btn.setBackground(background);
            Point p = customer.getLocation();
            Stage stage = null;
            try {
                stage = getStageOfCustomer(customer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage finalStage = stage;
            btn.setOnAction((e)-> finalStage.show());
            root.add(btn, (int) p.getY(), (int) p.getX());
        });

        Scene scene = new Scene(root);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setTitle("Map");
        stg.setScene(scene);
        return stg;
    }

    private Stage getStageOfCustomer(Customer customer) throws IOException {
        List<Customer.InfoOptions>lst = new ArrayList<>();
        lst.add(Customer.InfoOptions.CustomerId);
        lst.add(Customer.InfoOptions.Name);
        lst.add(Customer.InfoOptions.NumberOfOrders);
        Stage stg = new Stage();
        AnchorPane pane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../tile/tile.fxml");
        fxmlLoader.setLocation(url);
        Node singleWordTile = fxmlLoader.load();
        tileController tileController = fxmlLoader.getController();
        pane.getChildren().clear();
        tileController.initialize(systemManager.getCustomerInfo(customer,lst));
        pane.getChildren().add(singleWordTile);
        Scene scene = new Scene(pane);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setTitle("Customer");
        stg.setScene(scene);
        return stg;

    }

    private Stage getStageOfStore(Store store) throws IOException {

        List<Store.InfoOptions>lst = new ArrayList<>();
        lst.add(Store.InfoOptions.Id);
        lst.add(Store.InfoOptions.Name);
        lst.add(Store.InfoOptions.DeliveryPpk);
        lst.add(Store.InfoOptions.TotalEarning);
        Stage stg = new Stage();
        AnchorPane pane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../tile/tile.fxml");
        fxmlLoader.setLocation(url);
        Node singleWordTile = fxmlLoader.load();
        tileController tileController = fxmlLoader.getController();
        pane.getChildren().clear();
        tileController.initialize(systemManager.getStoreInfo(store,lst));
        pane.getChildren().add(singleWordTile);
        Scene scene = new Scene(pane);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setTitle("Store");
        stg.setScene(scene);
        return stg;
    }

}

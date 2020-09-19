package MapView;

import SDMModel.Customer;
import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class MapCreator {
    SystemManager systemManager = SystemManager.getInstance();

    public Stage getMap() throws IOException {
        Stage stg = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("../MapView/MapView.fxml");
        fxmlLoader.setLocation(url);
        GridPane root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        int rows = systemManager.getMaxRows();
        int cols = systemManager.getMaxCols();
        HashMap<Integer, Customer> costumers = systemManager.getSuperMarket().getCostumers();
        HashMap<Integer, Store> stores = systemManager.getSuperMarket().getStores();
        for (int i = 0; i <= rows; i++) {
            RowConstraints r = new RowConstraints();
            r.setMinHeight(10);
            r.setPrefHeight(10);
            r.setVgrow(Priority.SOMETIMES);
            root.getRowConstraints().add(r);
        }
        for (int i = 0; i <= cols; i++) {
            ColumnConstraints r = new ColumnConstraints();
            r.setMinWidth(10);
            r.setPrefWidth(10);
            r.setHgrow(Priority.SOMETIMES);
            root.getColumnConstraints().add(r);
        }

        stores.values().stream().forEach(store -> {
            Button btn = new Button();
            Point p = store.getLocation();
            btn.setText(store.getName());
            root.add(btn, (int) p.getY(), (int) p.getX());
        });

        costumers.values().stream().forEach(customer -> {
            Button btn = new Button();
            Point p = customer.getLocation();
            btn.setText(customer.getName());
            root.add(btn, (int) p.getY(), (int) p.getX());
        });
        Scene scene = new Scene(new ScrollPane( root));
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setTitle("Map");
        stg.setScene(scene);
        return stg;
    }

}

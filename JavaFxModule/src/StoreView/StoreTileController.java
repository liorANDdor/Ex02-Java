package StoreView;

import SDMModel.Item;
import SDMModel.Sell;
import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import tile.tileController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreTileController {

    @FXML
    private Label LocationLable;
    @FXML private Label PPKLabel;
    @FXML private Label STELabel;
    @FXML private ChoiceBox<String> storeView = new ChoiceBox<>();;
    @FXML private Label ID;
    @FXML private Button itemsBtn;
    @FXML private Button ordersBtn;
    @FXML private FlowPane itemsFlowPan;

    private SimpleStringProperty location = new SimpleStringProperty("");
    private SimpleStringProperty ppk = new SimpleStringProperty("");
    private SimpleStringProperty ste = new SimpleStringProperty("");
    private SimpleStringProperty id = new SimpleStringProperty("");



    @FXML public void initialize(HashMap<Integer, Store> stores, SystemManager sys) {
        HashMap<Integer, Store> storesInChoiceBox = new HashMap<>();
        Integer index = 0;
        for(Store store:stores.values()){
            storeView.getItems().add(store.getName());
            storesInChoiceBox.put(index, store);
            index++;
        }

        LocationLable.textProperty().bind(Bindings.format("Location: %s", location));
        PPKLabel.textProperty().bind(Bindings.format("PPK: %s", ppk));
        STELabel.textProperty().bind(Bindings.format("Shipment Earning: %s", ste));
        ID.textProperty().bind(Bindings.format("Store Id: %s", id));
        storeView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Store storeToShow = storesInChoiceBox.get(newValue);
                ste.set(Double.toString(storeToShow.getTotalShipmentEarning()));
                id.set(Integer.toString(storeToShow.getId()));
                ppk.set(Double.toString(storeToShow.getDeliveryPpk()));
                location.set("X:" +Integer.toString(storeToShow.getLocation().x) +" Y:" +Integer.toString(storeToShow.getLocation().y) );

                if(!itemsFlowPan.getChildren().isEmpty()) {
                    itemsFlowPan.getChildren().clear();
                }
                List<Sell> sells = storeToShow.getItemsToSell();
                for(Sell sell: sells){
                    createSellTile(sell,sys);
                    }
                }
            }
        );
      }

    private void createSellTile(Sell sell, SystemManager systemManager) {
        Item itemToSell = systemManager.getSuperMarket().getItemByID(sell.getItemId());
        List <Item.InfoOptions> itemAttributes = new ArrayList<Item.InfoOptions>();
        List <Sell.InfoOptions> sellAttributes = new ArrayList<>();
        itemAttributes.add(Item.InfoOptions.ItemId);
        itemAttributes.add(Item.InfoOptions.Name);
        itemAttributes.add(Item.InfoOptions.Category);
        StringBuilder SellInfo = systemManager.getinfoItem(itemToSell,itemAttributes);
        sellAttributes.add(Sell.InfoOptions.Price);
        sellAttributes.add(Sell.InfoOptions.TimesWasSold);


        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("../tile/tile.fxml");
            fxmlLoader.setLocation(url);
            Node singleWordTile = fxmlLoader.load();
            tileController tileController = fxmlLoader.getController();


            Platform.runLater(
                    () -> tileController.initialize( SellInfo.append(systemManager.getInfoSell(sell, sellAttributes)))
            );


            itemsFlowPan.getChildren().add(singleWordTile);
            //itemsTilesController.put(item.getId(), singleWordTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void setLocation(String Location){
        location.set(Location);
    }
    public void setSte(String STE){
        ste.set(STE);
    }
    public void setId(String ID){
        id.set(ID);
    }
    public void setPPK(String PPK){
        ppk.set(PPK);
    }
}

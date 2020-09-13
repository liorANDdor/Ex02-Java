package StoreView;

import OrdersView.OrdersSummaryController;
import SDMModel.Item;
import SDMModel.Sell;
import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import tile.tileController;

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
    HashMap<Integer, Store> storesInChoiceBox = new HashMap<>();
    private SystemManager systemManager = SystemManager.getInstance();
    private SimpleStringProperty location = new SimpleStringProperty("");
    private SimpleStringProperty ppk = new SimpleStringProperty("");
    private SimpleStringProperty ste = new SimpleStringProperty("");
    private SimpleStringProperty id = new SimpleStringProperty("");



    @FXML public void initialize(HashMap<Integer, Store> stores, SystemManager sys) {

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
                ste.set(Double.toString(storeToShow.getTotalShipmentEarning()));
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

      @FXML void showOrders() throws IOException {
          itemsFlowPan.getChildren().clear();
          FXMLLoader fxmlLoader = new FXMLLoader();
          URL url = getClass().getResource("../OrdersView/OrdersSummary.fxml");
          fxmlLoader.setLocation(url);
          Node orderSummary = fxmlLoader.load();
          OrdersSummaryController ordersSummaryController = fxmlLoader.getController();
          Store storeToShow = storesInChoiceBox.get(storeView.getSelectionModel().getSelectedIndex());
          ordersSummaryController.initialize(storeToShow.getOrders(), systemManager);
          Button btn = new Button("AggregateOrder");
          btn.setDisable(true);
          itemsFlowPan.getChildren().add(btn);
          itemsFlowPan.getChildren().add(orderSummary);

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

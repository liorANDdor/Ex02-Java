package SaleView;

import SDMModel.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SalesController {

    @FXML
    private FlowPane salesVB;
    @FXML
    private Label saleNameLabel;

    @FXML
    private Button continueBtn;
    Order order;
    private TableView itemsTable = new TableView();
    private TableColumn NameCol = new TableColumn("Name");
    private TableColumn IdCol = new TableColumn("ID");
    private TableColumn TypeCol = new TableColumn("Type");
    private TableColumn Store = new TableColumn("Store");
    private TableColumn totalQuantity = new TableColumn("Quantity");
    private TableColumn totalPrice = new TableColumn("Price");
    private TableView<ItemSetterGetter> itemsTableView;
    private HashMap<ToggleGroup, Sale> salesChosen = new HashMap<>();
    private SimpleBooleanProperty isContinuePressed = new SimpleBooleanProperty();

    public HashMap<ToggleGroup, Sale> getSalesChosen() {
        return salesChosen;
    }


    private SystemManager systemManager = SystemManager.getInstance();

    public void initData(Order order) {

        this.order = order;
        continueBtn.setStyle("-fx-background-color: slategray");
        continueBtn.setOnAction(x -> isContinuePressed.set(true));
    }

    public void showSales() {
        for (Store store : order.getStoresToOrderFrom().keySet()) {
            Order suborder = store.getOrders().get(order.getOrderNumber());
            for (Sale sale : store.getSales()) {
                IfBuy conditonForSale = sale.getIfBuy();
                Item itemSale = systemManager.getSuperMarket().getItemByID(conditonForSale.getItemId());
                if (suborder.getItemsQuantity().containsKey(itemSale))
                    for (double i = 0; i < Math.floor(suborder.getItemsQuantity().get(itemSale) / conditonForSale.getQuantity()); i++) {
                        createSaleTile(sale);
                    }

            }
        }
    }

    private void createSaleTile(Sale sale) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/SaleView/SingleSale.fxml");
            fxmlLoader.setLocation(url);
            Node singleSaleTile = fxmlLoader.load();
            SingleSaleController saleInfoController = fxmlLoader.getController();


            saleInfoController.init(sale, order, this);

            salesVB.getChildren().add(singleSaleTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void commitSales() throws IOException {
        for (Map.Entry<ToggleGroup, Sale> entry : salesChosen.entrySet()) {
            RadioButton selectedRadioButton = (RadioButton) entry.getKey().getSelectedToggle();
            Sale sale = entry.getValue();
            Integer storeId = sale.getStoreOfferSale().getId();
            if (selectedRadioButton.getText().equals("None"))
                continue;
            else {
                if (sale.getNeedToGet().getOperator().equals("ALL-OR-NOTHING")) {
                    if (!order.getSalesByStoreId().containsKey(storeId))
                        order.getSalesByStoreId().put(storeId, sale.getNeedToGet().getOffers());
                    else {
                        order.getSalesByStoreId().get(storeId).addAll(sale.getNeedToGet().getOffers());
                    }
                } else {
                    Integer radioIndex = entry.getKey().getToggles().indexOf(entry.getKey().getSelectedToggle());
                    Offer offer = sale.getNeedToGet().getOffers().get(radioIndex);
                    if (!order.getSalesByStoreId().containsKey(storeId)) {
                        ArrayList<Offer> offers = new ArrayList<>();
                        offers.add(offer);
                        order.getSalesByStoreId().put(storeId, offers);
                    } else {
                        order.getSalesByStoreId().get(storeId).add(offer);
                    }
                    order.setItemsPrice(order.getItemsPrice() + offer.getForAdditional());
                }
            }
        }

        showFinalOrder();
    }

    public void showFinalOrder() throws IOException {

        salesVB.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/SaleView/OrderSummary.fxml");
        fxmlLoader.setLocation(url);
        Node orderSummary = fxmlLoader.load();
        OrderSummaryController orderSummaryController = fxmlLoader.getController();
        orderSummaryController.initialize(order, systemManager);
//        Stage stage = (Stage) salesVB.getScene().getWindow();
//        stage.setTitle("Order Summary");
        salesVB.getChildren().add(orderSummary);

    }

}





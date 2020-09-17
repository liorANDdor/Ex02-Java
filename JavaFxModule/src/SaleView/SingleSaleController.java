package SaleView;

import SDMModel.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SingleSaleController {

    @FXML
    private Label saleName;

    @FXML
    private AnchorPane saleInfoPane;
    @FXML
    private VBox saleVB;
    @FXML
    private Label SaleTypeLabel;
    private SystemManager systemManager = SystemManager.getInstance();
    private ToggleGroup tg = new ToggleGroup();
    private Sale sale;
    private SalesController parantContoller;

    public void init(Sale sale, Order order, SalesController parantController) {
        NeedToGet needToGet = sale.getNeedToGet();
        this.sale = sale;
        saleName.setText(sale.getName());
        if (needToGet.getOperator().equals("ONE-OF")) {
            SaleTypeLabel.setText("Choose one:");
            for (Offer offer : needToGet.getOffers()) {
                String itemName = systemManager.getSuperMarket().getItemByID(offer.getItemId()).getName();
                RadioButton btn = new RadioButton(String.valueOf(offer.getQuantity()) + " " + itemName + " for " + String.valueOf(offer.getForAdditional()) + " ₪ ");
                btn.setPadding(new Insets(10));
                saleVB.getChildren().add(btn);
                btn.setToggleGroup(tg);
            }
            RadioButton btn = new RadioButton("None");
            btn.setSelected(true);
            btn.setPadding(new Insets(10));

            saleVB.getChildren().add(btn);
            btn.setToggleGroup(tg);
            parantController.getSalesChosen().put(tg, sale);
        }
        else {
            SaleTypeLabel.setText("Take all or nothing:");
            String allItemNames = "";
            Double itemTotalPrice = 0.0;
            for (Offer offer : needToGet.getOffers()) {
                String itemName = systemManager.getSuperMarket().getItemByID(offer.getItemId()).getName();
                allItemNames = allItemNames + " " + offer.getQuantity() + " "  + itemName + " " + offer.getForAdditional() + " ₪ " ;
                itemTotalPrice = itemTotalPrice +offer.getForAdditional();
                allItemNames = allItemNames + "\n";
            }
            allItemNames = allItemNames + "Total of " + itemTotalPrice + " ₪";
            RadioButton btn = new RadioButton(allItemNames);
            btn.setToggleGroup(tg);
            btn.setPadding(new Insets(10));
            saleVB.getChildren().add(btn);
            btn = new RadioButton("None");
            btn.setSelected(true);
            btn.setPadding(new Insets(10));
            btn.setPadding(new Insets(10));
            saleVB.getChildren().add(btn);
            btn.setToggleGroup(tg);
            parantController.getSalesChosen().put(tg, sale);
        }

    }

    public void showSale(Sale sale) {
        NeedToGet needToGet = sale.getNeedToGet();
        this.sale = sale;
        saleName.setText(sale.getName());
        Item item = systemManager.getSuperMarket().getItemByID(sale.getIfBuy().getItemId());
        if (needToGet.getOperator().equals("ONE-OF")) {
            SaleTypeLabel.setText("If you buy " + sale.getIfBuy().getQuantity() + " " + item.getName() + "\n\n" +
                    "Then you Choose one:");
            for (Offer offer : needToGet.getOffers()) {
                String itemName = systemManager.getSuperMarket().getItemByID(offer.getItemId()).getName();
                Label label = new Label(String.valueOf(offer.getQuantity()) + " " + itemName + " for " + String.valueOf(offer.getForAdditional()) + " ₪ ");
                label.setPadding(new Insets(10));
                saleVB.getChildren().add(label);

            }
        }
        else {
            SaleTypeLabel.setText("Take all or nothing:");
            String allItemNames = "";
            Double itemTotalPrice = 0.0;
            for (Offer offer : needToGet.getOffers()) {
                String itemName = systemManager.getSuperMarket().getItemByID(offer.getItemId()).getName();
                allItemNames = allItemNames + " " + offer.getQuantity() + " "  + itemName + " " + offer.getForAdditional() + " ₪ " ;
                itemTotalPrice = itemTotalPrice +offer.getForAdditional();
                allItemNames = allItemNames + "\n";
            }
            allItemNames = allItemNames + "Total of " + itemTotalPrice + " ₪";
            Label label = new Label(allItemNames);

            label.setPadding(new Insets(10));
            saleVB.getChildren().add(label);

        }

    }
}


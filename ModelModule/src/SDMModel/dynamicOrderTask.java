package SDMModel;

import SDMGenerated.SuperDuperMarketDescriptor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class dynamicOrderTask extends Task<Boolean> {

    private Consumer<ItemSetterGetter> orderDelegates;
    private Consumer<Boolean> dynamicBtnConsumer;
    private SystemManager sys ;
    private Order order;
    private HashMap<Integer, Order> subOrders = new HashMap<>();



    public dynamicOrderTask(Order order, HashMap<Integer, Order> subOrders , Consumer<ItemSetterGetter> orderDelegates,
                            Consumer<Boolean> dynamicBtnConsumer)  {
        this.order = order;
        this.sys = SystemManager.getInstance();
        this.subOrders = subOrders;
        this.orderDelegates = orderDelegates;
        this.dynamicBtnConsumer = dynamicBtnConsumer;

    }


    @Override
    protected Boolean call() throws Exception {
        updateProgress(0, 1);
        updateMessage("Fetching data");

        Thread.sleep(150);
        updateProgress(0.15, 1);
        updateMessage("Finding Items with lowest price");
        Thread.sleep(150);
        updateProgress(0.25, 1);

        List data = new ArrayList<>();
        double progress = 0.4;
        for (Item item : sys.getSuperMarket().getItems().values()) {
            Store storeLowestItemPrice = sys.getItemLowestPrice(item.getId());
            updateMessage("Finding best price for " + item.getName());
            Thread.sleep(150);
            updateProgress(progress, 1);
            progress = progress+0.04;
            data.add(
                    new ItemSetterGetter(Double.toString(sys.getItemLowestPrice(item.getId()).getItemPrice(item.getId())), //itemLowestPrice
                            item.getName(), Integer.toString(item.getId()), item.getPurchaseCategory().toString(), order, subOrders, storeLowestItemPrice, sys));
            orderDelegates.accept(new ItemSetterGetter(Double.toString(sys.getItemLowestPrice(item.getId()).getItemPrice(item.getId())), //itemLowestPrice
                    item.getName(), Integer.toString(item.getId()), item.getPurchaseCategory().toString(), order, subOrders, storeLowestItemPrice, sys));
        }



            updateMessage("Items are almost ready...");
            Thread.sleep(150);

            updateProgress(0.7, 1);
            updateProgress(0.8, 1);
            Thread.sleep(500);
            updateProgress(0.9, 1);
            Thread.sleep(500);
            updateProgress(1, 1);

            //orderDelegates.accept(FXCollections.observableList(data));
            Thread.sleep(500);
            updateMessage("Done");
             dynamicBtnConsumer.accept(true);
            return true;
    }
}

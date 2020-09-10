package SDMModel;

import SDMGenerated.SuperDuperMarketDescriptor;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import sun.java2d.ScreenUpdateManager;

import java.util.function.Consumer;

public class XmlLoaderTask  extends Task<Boolean> {
    private final String path;
    private Consumer<SuperMarket> superMarketDelegates;
    private Consumer<Boolean> isxmlLoaded;

    public XmlLoaderTask(String path, Consumer<SuperMarket> superMarket, Consumer<Boolean> isxmlLoaded) {
        this.path = path;
        this.superMarketDelegates = superMarket;
        this.isxmlLoaded = isxmlLoaded;
    }

    private XmlUtilities xmlUtilities;

    @Override
    protected Boolean call() throws Exception {
        updateProgress(0, 1);
        updateMessage("Starts to load xml");
        xmlUtilities = new XmlUtilities();
        xmlUtilities.isNameOfFileCorrect(path);
        Thread.sleep(1000);
        updateProgress(0.3, 1);
        updateMessage("Name is fine");
        SuperDuperMarketDescriptor superMarketSDM = xmlUtilities.loadFile(path);
        xmlUtilities.checkIfTheXmlThatJustLoadedOk(superMarketSDM);
        if (xmlUtilities.getIsXmlOk()) {
            Thread.sleep(1000);
            SuperMarket superMarket = SuperMarket.creatInstance(superMarketSDM);
            updateProgress(0.7, 1);
            superMarketDelegates.accept(superMarket);
            isxmlLoaded.accept(true);
        } else {
            updateMessage("Not loaded successfuly \n " + xmlUtilities.getWhatWrongMessage());
            return false;
        }
        updateProgress(0.8, 1);
        Thread.sleep(1000);
        updateProgress(0.9, 1);
        Thread.sleep(1000);
        updateProgress(1, 1);
        Thread.sleep(1000);
        updateMessage("Loaded Successfuly");
        return true;
    }
}

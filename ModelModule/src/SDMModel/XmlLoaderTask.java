package SDMModel;

import SDMGenerated.SuperDuperMarketDescriptor;
import javafx.concurrent.Task;

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
        Thread.sleep(150);
        updateProgress(0.15, 1);
        updateMessage("File name is OK");
        Thread.sleep(150);
        updateProgress(0.3, 1);
        SuperDuperMarketDescriptor superMarketSDM = xmlUtilities.loadFile(path);
        xmlUtilities.checkIfTheXmlThatJustLoadedOk(superMarketSDM);
        if (xmlUtilities.getIsXmlOk()) {
            updateMessage("Super market instace is at creation...");
            Thread.sleep(350);
            SuperMarket superMarket = SuperMarket.creatInstance(superMarketSDM);
            updateProgress(0.7, 1);
            superMarketDelegates.accept(superMarket);
            updateProgress(0.8, 1);
            Thread.sleep(500);
            updateProgress(0.9, 1);
            Thread.sleep(500);
            updateProgress(1, 1);
            Thread.sleep(500);
            updateMessage("Loaded Successfuly");

            isxmlLoaded.accept(true);
            return true;
        } else {
            Thread.sleep(150);
            updateProgress(0.85, 1);
            updateMessage("Not loaded successfuly \n " + xmlUtilities.getWhatWrongMessage());
            return false;
        }

    }
}
package XmlLoderView;

import SDMModel.SystemManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import java.io.File;

public class XmlLoaderController {

    SystemManager insatnce = SystemManager.getInstance();

    @FXML
    private Button LoadContentBtn;

    @FXML
    private Label ProgressPrecent;

    @FXML
    private ProgressBar ProgressBar;

    @FXML
    private Label MessageLabel;

    SimpleBooleanProperty isDisabledBtn = new SimpleBooleanProperty(true);

    @FXML private void initialize() {
        LoadContentBtn.disableProperty().bind(isDisabledBtn);
    }
    @FXML
    void LoadContentHandler(ActionEvent event) {
            insatnce.LoadXMLFileAndCheckIt(MessageLabel.getText(),this);
    }

    @FXML
    void OpenFileHandler(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile!=null){
            MessageLabel.setText(selectedFile.getAbsolutePath());
            isDisabledBtn.set(false);
        }
    }

    public void bindUIToTask(Task<Boolean> task) {
        MessageLabel.textProperty().bindBidirectional((Property<String>) task.messageProperty());
        ProgressBar.progressProperty().bind(task.progressProperty());
        ProgressPrecent.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        task.progressProperty(),
                                        100)),
                        " %"));
    }
}

package XmlLoderView;

import SDMModel.SystemManager;
import SDMModel.XmlLoaderTask;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import java.io.File;

public class XmlLoaderController {

    SystemManager insatnce = SystemManager.getInstance();
    @FXML
    private Label ProgressPrecent;

    @FXML
    private ProgressBar ProgressBar;

    @FXML
    private Label MessageLabel;

    @FXML
    void LoadContentHandler(ActionEvent event) {
            insatnce.LoadXMLFileAndCheckIt(MessageLabel.getText(),this);
    }

    @FXML
    void OpenFileHandler(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        MessageLabel.setText(selectedFile.getAbsolutePath());


    }

    public void bindUIToTask(Task<Boolean> task) {
        // task message
        MessageLabel.textProperty().bind(task.messageProperty());

        // task progress bar
        ProgressBar.progressProperty().bind(task.progressProperty());

        // task percent label
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

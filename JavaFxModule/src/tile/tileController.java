package tile;

import SDMModel.Item;
import SDMModel.SystemManager;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class tileController {

    @FXML
    private TitledPane tile;

    @FXML
    private Label name;

    protected SimpleStringProperty nameProperty;


    private SimpleStringProperty nameOfItem;


    @FXML
    public void initialize(StringBuilder string) {

        this.nameOfItem = new SimpleStringProperty(string.toString());
        name.textProperty().bind(Bindings.format(nameOfItem.getValue()));
    }

    @FXML
    public void setName(String name) {

        nameOfItem.setValue(name);

    }
}




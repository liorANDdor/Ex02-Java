package tile;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

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
        name.setPadding(new Insets(9));
        name.setStyle("-fx-font: 12 arial;");
    }

    @FXML
    public void setName(String name) {

        nameOfItem.setValue(name);

    }
}




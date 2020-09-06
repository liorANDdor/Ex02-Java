package OrderWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;



public class OrderController {

    @FXML
    private TextField sometext;
public String getText(){
    return sometext.getText();
}
    @FXML
    void commit(ActionEvent event) {

    }

}

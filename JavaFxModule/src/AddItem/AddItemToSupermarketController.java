package AddItem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddItemToSupermarketController {

    @FXML
    private Button addToStoreBtn;

    @FXML
    private Button finishBtn;

    @FXML
    private TextField priceTxt;
    @FXML private Button nextBtn;
    @FXML
    private TextField nameTxt;

    @FXML
    private TextField IdTxt;

    @FXML
    void addItem(ActionEvent event) {

    }

    @FXML void initialize(){
        priceTxt.setOnMouseClicked(x->priceTxt.setText(""));
        nameTxt.setOnMouseClicked(x->priceTxt.setText(""));
        IdTxt.setOnMouseClicked(x->priceTxt.setText(""));


        IdTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[1-50]")) {
                    IdTxt.setText(newValue.replaceAll("[^\\d]", ""));
                }
                addToStoreBtn.setDisable(false);
                finishBtn.setDisable(false);
                if(newValue.equals("")){
                    addToStoreBtn.setDisable(true);
                    finishBtn.setDisable(true);
                }
            }
        });

        nameTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                addToStoreBtn.setDisable(false);
                finishBtn.setDisable(false);
                if(newValue.equals("")){
                    addToStoreBtn.setDisable(true);
                    finishBtn.setDisable(true);
                }
            }
        });

        priceTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    priceTxt.setText(oldValue);
                }
                addToStoreBtn.setDisable(false);
                finishBtn.setDisable(false);
                if(newValue.equals("")){
                    addToStoreBtn.setDisable(true);
                    finishBtn.setDisable(true);
                }
            }
        });

    }

    @FXML
    void commitChangesHandler(ActionEvent event) {

    }

}

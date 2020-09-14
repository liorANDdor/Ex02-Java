package AddStore;

import SDMModel.Store;
import SDMModel.SystemManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tile.tileController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class AddStoreController {

    @FXML private TextField storeIDTxt;
    @FXML private TextField storeNameTxt;
    @FXML private Label showLocationLabel;
    @FXML private TextField xTxt;
    @FXML private TextField yTxt;
    @FXML private TextField storePpkTxt;
    @FXML private Button nextItemBtn;
    private SystemManager systemManager = SystemManager.getInstance();
    @FXML void initialize(){
        xTxt.setOnMouseClicked(x->xTxt.setText(""));
        yTxt.setOnMouseClicked(x->yTxt.setText(""));
        storePpkTxt.setOnMouseClicked(x->storePpkTxt.setText(""));
        storeIDTxt.setOnMouseClicked(x->storeIDTxt.setText(""));
        storeNameTxt.setOnMouseClicked(x->storeNameTxt.setText(""));

        xTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("[1-50]")) {
                    xTxt.setText(newValue.replaceAll("[^\\d]", ""));
                }

            }
        });
        yTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("[1-50]")) {
                    yTxt.setText(newValue.replaceAll("[^\\d]", ""));
                }

            }
        });
        storeIDTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("(1-9)")) {
                    storeIDTxt.setText(newValue.replaceAll("[^\\d]", ""));
                }

            }
        });
        storePpkTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("[1-500],.")) {
                    storePpkTxt.setText(newValue.replaceAll("[^\\d]", ""));
                }

            }
        });
        nextItemBtn.setOnAction(x->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("New Store");
            if(xTxt.getText().equals("X") || yTxt.getText().equals("Y") || storePpkTxt.getText().equals("PPK") ||
                    xTxt.getText().equals("") || yTxt.getText().equals("") || storePpkTxt.getText().equals("") ||
            nextItemBtn.getText().equals("") || storeIDTxt.getText().equals("") ){
              return;
            }
            else if(Integer.parseInt( xTxt.getText())<1 || Integer.parseInt(yTxt.getText())<1  | Integer.parseInt(xTxt.getText())>50  | Integer.parseInt(xTxt.getText())>50 ) {
                alert.setContentText("Store location must be between 1 to 50");
            alert.showAndWait();
            }
           else {

                Store store = new Store(storeNameTxt.getText(), Integer.parseInt(storeIDTxt.getText()), Integer.parseInt(storePpkTxt.getText()), new Point(Integer.parseInt(xTxt.getText()),
                        Integer.parseInt(yTxt.getText())));
                String errorMessage = "";
                boolean isStoreOk = systemManager.checkIfStoreOk(store, errorMessage);
                if (!isStoreOk) {
                    alert.setContentText(errorMessage);
                    alert.showAndWait();
                } else {
                    AddItemToStoreController addItemToStoreController = null;
                    Stage stg = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    URL url = getClass().getResource("../AddStore/AddItemToStore.fxml");
                    fxmlLoader.setLocation(url);
                    Parent root = null;
                    try {
                        root = fxmlLoader.load(fxmlLoader.getLocation().openStream());
                        addItemToStoreController = fxmlLoader.getController();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    addItemToStoreController.setStore(store);
                    Scene scene = new Scene(root, 250, 300);
                    stg.initModality(Modality.APPLICATION_MODAL);
                    stg.setTitle("Add item");
                    stg.setScene(scene);
                    stg.show();




                }
            }


            //alert.setHeaderText("Cannot commit order");

        });
    }

}

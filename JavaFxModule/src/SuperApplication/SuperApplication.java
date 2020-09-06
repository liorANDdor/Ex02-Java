package SuperApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SuperApplication extends Application {
    private static final String PATH = "./SuperApplication.fxml";

    public static void main(String[]args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = getFXML();
        Parent root = getROOT(fxmlLoader);

        Scene scene = new Scene(root, 1000, 600);

        primaryStage.setTitle("Super Market");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private FXMLLoader getFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(PATH);
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }



    private Parent getROOT(FXMLLoader fxmlLoader) throws Exception {
        return fxmlLoader.load(fxmlLoader.getLocation().openStream());
    }
}

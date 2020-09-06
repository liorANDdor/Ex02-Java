package SuperApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SuperApplication extends Application {
    public static void main(String[]args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = getPlayersFXMLLoader();
        Parent playersRoot = getPlayersRoot(fxmlLoader);

        Scene scene = new Scene(playersRoot, 500, 400);

        primaryStage.setTitle("Players Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private FXMLLoader getPlayersFXMLLoader() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(PLAYERS_SCENE_FXML_PATH);
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }



    private Parent getPlayersRoot(FXMLLoader fxmlLoader) throws Exception {
        return (Parent) fxmlLoader.load(fxmlLoader.getLocation().openStream());
    }
}

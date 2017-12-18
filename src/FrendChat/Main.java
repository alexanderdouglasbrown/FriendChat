package FrendChat;

import FrendChat.Models.FrendServer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    private static Stage primaryStage;
    private static Image icon = new Image("FrendChat/Icons/icon32.png");

    public static Stage getPrimaryStage(){
        return primaryStage;
    }

    public static void setPrimaryStage(Stage stage){
        primaryStage = stage;
    }

    public static Image getIcon(){
        return icon;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(icon);
        Parent root = FXMLLoader.load(getClass().getResource("/FrendChat/Views/Connect.fxml"));
        primaryStage.setTitle("Frend Chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(260);
        primaryStage.setMinHeight(165);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
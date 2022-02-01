package FrendChat.Presenters;

import FrendChat.Main;
import FrendChat.Models.FrendServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Connect {
    @FXML
    private TextField txtIP;
    @FXML
    private TextField txtPort;
    @FXML
    private Button btnConnect;
    @FXML
    private ProgressIndicator uiProgress;

    private FrendServer frendServer = FrendServer.getInstance();

    public void btnConnect(ActionEvent actionEvent) {
        int port;

        try {
            port = Integer.parseInt(txtPort.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Invalid Port Number");
            alert.setContentText("Port must contain a number.");
            alert.showAndWait();
            return;
        }

        if (port > 65535 || port < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Invalid Port Range");
            alert.setContentText("Port value must be between 0 and 65535.");
            alert.showAndWait();
            return;
        }

        btnConnect.setVisible(false);
        uiProgress.setVisible(true);
        frendServer.connect(txtIP.getText(), port, this);
    }

    public void mdlConnectSuccessful() {
        Platform.runLater(() -> {
            btnConnect.setVisible(true);
            uiProgress.setVisible(false);

            Main.getPrimaryStage().close();
            Main.setPrimaryStage(new Stage());
            Stage stage = Main.getPrimaryStage();
            stage.getIcons().add(Main.getIcon());
            stage.setTitle("Friend Chat");
            stage.setMinWidth(320);
            stage.setMinHeight(300);
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/FrendChat/Views/Login.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.exit(5);
            }
        });
    }

    public void mdlConnectUnsuccessful() {
        Platform.runLater(() -> {
            btnConnect.setVisible(true);
            uiProgress.setVisible(false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Unable to Connect");
            alert.setContentText("Unable to connect. Check if your IP address and port were entered correctly.");
            alert.showAndWait();
        });
    }

    public void mdlWrongVersion() {
        Platform.runLater(() -> {
            btnConnect.setVisible(true);
            uiProgress.setVisible(false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Friend Chat Client Not up to Date");
            alert.setContentText("Unable to connect. Please download the newest version of Friend Chat.");
            alert.showAndWait();
        });
    }
}

package FrendChat.Presenters;

import FrendChat.Models.FrendServer;
import com.sun.javaws.exceptions.ExitException;
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

    FrendServer frendServer = FrendServer.getInstance();

    public void btnConnect(ActionEvent actionEvent) {
        int port;

        try {
            port = Integer.parseInt(txtPort.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Invalid Port Number");
            alert.setContentText("Port must contain a number.");
            alert.showAndWait();
            return;
        }

        if (port > 65535 || port < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
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

            Stage stage = FrendChat.Main.getPrimaryStage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/FrendChat/Views/Login.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.exit(ExitException.LAUNCH_ERROR);
            }
        });
    }

    public void mdlConnectUnsuccessful() {
        Platform.runLater(() -> {
            btnConnect.setVisible(true);
            uiProgress.setVisible(false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Unable to Connect");
            alert.setContentText("Unable to connect. Check if your IP address and port were entered correctly.");
            alert.showAndWait();
        });
    }
}

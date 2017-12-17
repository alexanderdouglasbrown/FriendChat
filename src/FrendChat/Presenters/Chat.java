package FrendChat.Presenters;

import FrendChat.Main;
import FrendChat.Models.FrendServer;
import com.sun.javaws.exceptions.ExitException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Chat {
    FrendServer frendServer = FrendServer.getInstance();

    @FXML
    private WebView webChat;
    @FXML
    private ListView lstUsers;
    @FXML
    private TextField txtInput;

    public void initialize() {
        webChat.setContextMenuEnabled(false);

        String initialHTML = "<style>" +
                "body {" +
                "font: 14px \"Segoe UI\", Arial;" +
                "}" +
                "</style>";

        webChat.getEngine().loadContent(initialHTML);

        Platform.runLater(() -> {
            txtInput.requestFocus();
            frendServer.chatListen(this);
            frendServer.requestUserList(this);
        });
        Stage primaryStage = Main.getPrimaryStage();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                FrendServer.getInstance().closeConnection();
            }
        });
    }

    public void btnSend() {
        if (txtInput.getLength() == 0)
            return;

        String message = txtInput.getText();
        message = message.replace("&", "&amp;");
        message = message.replace("<", "&lt;");
        message = message.replace(">", "&gt;");
        message = message.replace("\"", "&quot;");
        message = message.replace("'", "&#39;");

        frendServer.broadcastMessage(message, this);
    }

    public void mdlClearInput() {
        Platform.runLater(() -> {
            txtInput.setText("");
            txtInput.requestFocus();
        });
    }

    public void mdlChatMessage(String message) {
        Platform.runLater(() -> {
            webChat.getEngine().executeScript("document.body.innerHTML += \"" + message + "\";");
            webChat.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight);");
        });
    }

    public void mdlConnectionError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Connection Error");
            alert.setContentText("A communication error has occurred with the server.");
            alert.showAndWait();

            frendServer.closeConnection();

            Main.getPrimaryStage().close();
            Main.setPrimaryStage(new Stage());
            Stage stage = Main.getPrimaryStage();
            stage.getIcons().add(Main.getIcon());
            stage.setTitle("Frend Chat");
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/FrendChat/Views/Connect.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.exit(ExitException.LAUNCH_ERROR);
            }
        });
    }

    public void mdlJoinUser(String username, String color) {
        Platform.runLater(() -> {
            Text user = new Text(username);
            user.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            user.setFill(Color.web(color));
            lstUsers.getItems().add(user);
        });
    }

    public void mdlLeaveUser(String username) {
        Platform.runLater(() -> {
            int pos = -1;
            String testString = "Text[text=\"" + username + "\"";
            for(int i = 0; i < lstUsers.getItems().size(); i++) {
                if (lstUsers.getItems().get(i).toString().contains(testString)) {
                    pos = i;
                    break;
                }
            }
            lstUsers.getItems().remove(pos);
        });
    }
}

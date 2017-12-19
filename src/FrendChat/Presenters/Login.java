package FrendChat.Presenters;

import FrendChat.Main;
import FrendChat.Models.FrendServer;
import com.sun.javaws.exceptions.ExitException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Login {
    final private int maxUsernameLength = 25;

    private FrendServer frendServer = FrendServer.getInstance();

    @FXML
    private TextField txtLoginUsername;
    @FXML
    private PasswordField pswLoginPassword;
    @FXML
    private TextField txtRegisterUsername;
    @FXML
    private PasswordField pswRegisterPassword;
    @FXML
    private PasswordField pswConfirmPassword;
    @FXML
    private ColorPicker clrRegisterColor;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnRegister;

    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                        if (newTab.getText().equals("Register")) {
                            btnLogin.setDefaultButton(false);
                            btnRegister.setDefaultButton(true);
                        }
                        if (newTab.getText().equals("Login")) {
                            btnRegister.setDefaultButton(false);
                            btnLogin.setDefaultButton(true);
                        }
                    }
                });
    }

    public void btnLogin(ActionEvent actionEvent) {
        if (txtLoginUsername.getText().contains(" ")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Username May Not Contain a Space");
            alert.setContentText("Your username may not contain a space. Please choose something different.");
            alert.showAndWait();

            return;
        }

        if (txtLoginUsername.getText().length() > maxUsernameLength) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Username May Not Contain Be Greater Than " + maxUsernameLength + " Characters");
            alert.setContentText("Your username may not be greater than " + maxUsernameLength + " characters. Please choose something different.");
            alert.showAndWait();

            return;
        }

        if (txtLoginUsername.getLength() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Username Field Is Empty");
            alert.setContentText("You have not typed in a username.");
            alert.showAndWait();

            return;
        }

        if (pswLoginPassword.getLength() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Password Field Is Empty");
            alert.setContentText("You have not typed in a password.");
            alert.showAndWait();

            return;
        }

        frendServer.login(txtLoginUsername.getText(), pswLoginPassword.getText(), this);
    }

    public void btnRegister(ActionEvent actionEvent) {
        if (txtRegisterUsername.getText().contains(" ")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Username May Not Contain a Space");
            alert.setContentText("Your username may not contain a space. Please choose something different.");
            alert.showAndWait();

            return;
        }

        if (txtRegisterUsername.getText().length() > maxUsernameLength) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Username May Not Contain Be Greater Than " + maxUsernameLength + " Characters");
            alert.setContentText("Your username may not be greater than " + maxUsernameLength + " characters. Please choose something different.");
            alert.showAndWait();

            return;
        }

        if (txtRegisterUsername.getLength() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Username Field Is Empty");
            alert.setContentText("You have not typed in a username.");
            alert.showAndWait();

            return;
        }

        if (pswRegisterPassword.getLength() < 8) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Password Is Too Short");
            alert.setContentText("Your password must be at least 8 characters in length. Please try again.");
            alert.showAndWait();

            return;
        }

        if (!pswRegisterPassword.getText().equals(pswConfirmPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Passwords Do Not Match");
            alert.setContentText("Your passwords do not match. Please try again.");
            alert.showAndWait();

            return;
        }

        byte red = (byte) (clrRegisterColor.getValue().getRed() * 255);
        byte green = (byte) (clrRegisterColor.getValue().getGreen() * 255);
        byte blue = (byte) (clrRegisterColor.getValue().getBlue() * 255);
        String colorHex = String.format("#%02X%02X%02X", red, green, blue);

        frendServer.register(txtRegisterUsername.getText(), pswRegisterPassword.getText(), colorHex, this);

    }

    public void mdlCredentialsAccepted(String username) {
        gotoChatScreen(username);
    }

    public void mdlUsernameInUse() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Username in Use");
            alert.setContentText("The username you have chosen is already in use. Please choose another.");
            alert.showAndWait();
        });
    }

    public void mdlCredentialsRejected() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Frend Chat");
            alert.setHeaderText("Invalid Username or Password");
            alert.setContentText("Could not log in with the provided username and password. Please try again.");
            alert.showAndWait();
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
            stage.setMinWidth(260);
            stage.setMinHeight(165);
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/FrendChat/Views/Connect.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.exit(ExitException.LAUNCH_ERROR);
            }
        });
    }

    private void gotoChatScreen(String username) {
        Platform.runLater(() -> {
            Main.getPrimaryStage().close();
            Main.setPrimaryStage(new Stage());
            Stage stage = Main.getPrimaryStage();
            stage.getIcons().add(Main.getIcon());
            stage.setTitle("Frend Chat");
            stage.setMinWidth(400);
            stage.setMinHeight(300);
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/FrendChat/Views/Chat.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.exit(ExitException.LAUNCH_ERROR);
            }
        });
    }
}

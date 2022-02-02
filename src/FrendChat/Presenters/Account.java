package FrendChat.Presenters;

import FrendChat.Models.FrendServer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.PasswordField;

public class Account {
    @FXML
    private PasswordField pswCurrent;
    @FXML
    private PasswordField pswNew;
    @FXML
    private PasswordField pswConfirm;
    @FXML
    private ColorPicker clrUserColor;

    FrendServer frendServer = FrendServer.getInstance();
    String newColor = "";

    public void initialize() {
        frendServer.accountCallback = this;
    }

    public void btnUpdatePassword() {
        if (pswNew.getLength() < 8) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Password Is Too Short");
            alert.setContentText("Your password must be at least 8 characters in length. Please try again.");
            alert.showAndWait();

            return;
        }

        if (!pswNew.getText().equals(pswConfirm.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Passwords Do Not Match");
            alert.setContentText("Your passwords do not match. Please try again.");
            alert.showAndWait();

            return;
        }

        frendServer.updatePassword(pswCurrent.getText(), pswNew.getText());
    }

    public void btnUpdateColor() {
        byte red = (byte) (clrUserColor.getValue().getRed() * 255);
        byte green = (byte) (clrUserColor.getValue().getGreen() * 255);
        byte blue = (byte) (clrUserColor.getValue().getBlue() * 255);
        String colorHex = String.format("#%02X%02X%02X", red, green, blue);
        newColor = colorHex;

        frendServer.updateColor(colorHex);
    }

    public void mdlPasswordInvalid() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Invalid Password");
            alert.setContentText("Your original password is incorrect. Please try again.");
            alert.showAndWait();
        });
    }

    public void mdlPasswordUpdated() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Password Successfully Changed");
            alert.setContentText("Your password has been updated.");
            alert.showAndWait();

            pswCurrent.setText("");
            pswNew.setText("");
            pswConfirm.setText("");
        });
    }

    public void mdlColorUpdated() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Friend Chat");
            alert.setHeaderText("Color Changed");
            alert.setContentText("Your username color has been updated.");
            alert.showAndWait();
        });
    }
}

package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public final class AlertsAndDialogs {
    
    private AlertsAndDialogs() {}
    
    public static void notNumberAlert(String validFormat) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Number");
        alert.setHeaderText("Please enter valid numbers.");
        alert.setContentText("Accepted format: " + validFormat);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
    }

}

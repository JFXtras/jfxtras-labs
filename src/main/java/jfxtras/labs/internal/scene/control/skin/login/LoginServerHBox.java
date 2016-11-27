package jfxtras.labs.internal.scene.control.skin.login;

import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import jfxtras.scene.layout.HBox;

public class LoginServerHBox extends HBox
{
	@FXML HBox serverHBox;
	@FXML ChoiceBox<String> serverChoiceBox;
	
    public LoginServerHBox(
            ResourceBundle resources,
            List<String> serverNames)
    {
        LoginBorderPane.loadFxml(LoginServerHBox.class.getResource("LoginServer.fxml"), this, resources);
        serverChoiceBox.getItems().addAll(serverNames);
    }
}

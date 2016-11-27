package jfxtras.labs.internal.scene.control.skin.login;

import java.util.List;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class LoginServerBorderPane extends LoginBorderPane
{	
    public LoginServerBorderPane(
            Callback<String[], Void> loginCallback,
            ResourceBundle resources,
            String initialUsername,
            List<String> serverNames)
    {
        super(loginCallback, resources, initialUsername);
        LoginServerHBox serverHBox = new LoginServerHBox(resources, serverNames);
        getControlVBox().getChildren().add(2, serverHBox);
        
        // Change LoginBorderPane's mouse click even handler to handle the server selection
        signInButton.setOnMouseClicked((EventHandler<? super MouseEvent>) (event) -> 
        {
            String[] strings = { 
                    usernameTextField.getText(),
                    passwordField.getText(),
                    serverHBox.serverChoiceBox.getSelectionModel().getSelectedItem()
                    };
            loginCallback.call(strings);
        });
    }
}

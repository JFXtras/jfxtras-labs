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
        System.out.println("serverHBox:" + serverHBox);
        
        serverHBox.serverChoiceBox.getItems().addAll(serverNames);
        
        signInButton.setOnMouseClicked((EventHandler<? super MouseEvent>) (event) -> 
        {
            String[] strings = { 
                    usernameTextField.getText(),
                    passwordField.getText(),
                    serverHBox.serverChoiceBox.getSelectionModel().getSelectedItem()
                    };
            loginCallback.call(strings);
        });
        
        getControlVBox().getChildren().add(2, serverHBox);
    }
}

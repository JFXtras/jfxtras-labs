package jfxtras.labs.internal.scene.control.skin.login;

import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.util.Callback;

public class LoginServerBorderPane extends LoginBorderPane
{
    private LoginServerHBox serverHBox;
    
    public LoginServerBorderPane(
            Callback<String[], Void> loginCallback,
            ResourceBundle resources,
            String initialUsername,
            List<String> serverNames)
    {
        super(loginCallback, resources, initialUsername);
        serverHBox = new LoginServerHBox(resources, serverNames);
        getControlVBox().getChildren().add(2, serverHBox);
        
        // Change LoginBorderPane's mouse click even handler to handle the server selection
//        signInButton.setOnMouseClicked((EventHandler<? super MouseEvent>) (event) -> 
//        {
//            String[] strings = { 
//                    usernameTextField.getText(),
//                    passwordField.getText(),
//                    serverHBox.serverChoiceBox.getSelectionModel().getSelectedItem()
//                    };
//            loginCallback.call(strings);
//        });
    }
    
    @Override
    @FXML void handleSignin()
    {
        String[] strings = { 
                usernameTextField.getText(),
                passwordField.getText(),
                serverHBox.serverChoiceBox.getSelectionModel().getSelectedItem()
                };
        loginCallback.call(strings);
    }
}

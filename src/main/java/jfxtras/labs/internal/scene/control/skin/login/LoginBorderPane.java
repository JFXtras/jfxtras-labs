package jfxtras.labs.internal.scene.control.skin.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class LoginBorderPane extends BorderPane
{
	@FXML Label organizationNameLabel;
    @FXML VBox controlVBox;
	@FXML TextField usernameTextField;
	@FXML PasswordField passwordField;
	@FXML Button signInButton;
	@FXML Button cancelButton;
	
    public LoginBorderPane(Callback<String[], Void> loginCallback, ResourceBundle resources, String initialUsername)
    {
        super();
        loadFxml(LoginBorderPane.class.getResource("Login.fxml"), this, resources);
        usernameTextField.setText(initialUsername);
        
        // Mouse click event handler calls client-provided callback
        signInButton.setOnMouseClicked((EventHandler<? super MouseEvent>) (event) -> 
        {
            String[] strings = { 
                    usernameTextField.getText(),
                    passwordField.getText()
                    };
            loginCallback.call(strings);
        });
        
        cancelButton.setOnMouseClicked((event) -> 
        {
        	getParent().getScene().getWindow().hide();
        });
    }
    
    Pane getControlVBox()
    {
        return controlVBox;
    }
    
    protected static void loadFxml(URL fxmlFile, Object rootController, ResourceBundle resources)
    {
        FXMLLoader loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        loader.setResources(resources);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}

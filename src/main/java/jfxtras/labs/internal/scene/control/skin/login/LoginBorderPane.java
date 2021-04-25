/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.internal.scene.control.skin.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
	
	Callback<String[], Void> loginCallback;
	
    public LoginBorderPane(Callback<String[], Void> loginCallback, ResourceBundle resources, String initialUsername)
    {
        super();
        this.loginCallback = loginCallback;
        loadFxml(LoginBorderPane.class.getResource("Login.fxml"), this, resources);
        usernameTextField.setText(initialUsername);
        
        // Mouse click event handler calls client-provided callback
//        signInButton.setOnMouseClicked((EventHandler<? super MouseEvent>) (event) -> 
//        {
//            String[] strings = { 
//                    usernameTextField.getText(),
//                    passwordField.getText()
//                    };
//            loginCallback.call(strings);
//        });
        
//        cancelButton.setOnMouseClicked((event) -> 
//        {
//        	getParent().getScene().getWindow().hide();
//        });
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
    
    @FXML private void handleCancel()
    {
        getParent().getScene().getWindow().hide();
    }
    
    @FXML void handleSignin()
    {
        String[] strings = { 
                usernameTextField.getText(),
                passwordField.getText()
                };
        System.out.println("strings:" + strings);
        loginCallback.call(strings);
    }
    
    @FXML private void handleKeyPress()
    {
        handleSignin();
    }
}

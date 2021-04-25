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

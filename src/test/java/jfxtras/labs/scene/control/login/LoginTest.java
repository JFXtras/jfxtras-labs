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
package jfxtras.labs.scene.control.login;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import jfxtras.test.AssertNode;
import jfxtras.test.JFXtrasGuiTest;

public class LoginTest extends JFXtrasGuiTest
{

	Object result;
	String username;
	
	@Override
    public Parent getRootNode()
    {
		Callback<String[], Void> loginCallback = (credentials) ->
		{
            username = credentials[0];
            String password = credentials[1];
			if (username.equals("David") && password.equals("password"))
			{
				result = "login ok";
			} else
			{
				result  = "login failed";
			}
			System.out.println(username + ", " + password + ":" + result);
			return null;
		};

        Locale myLocale = Locale.getDefault();
		ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.scene.control.login.LoginDefault", myLocale);
		Login root = new Login(loginCallback, resources, "David");
		root.setPrefSize(300, 400);
    	return root;
    }
    
    @Test
    public void canDisplayLogin()
    {
    	Node n = (Node)find("#Login-Default");
//    	TestUtil.sleep(3000);
//		AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
//		new AssertNode(n).assertXYWH(0.0, 0.0, 320.0, 400.0, 0.01);
    }
    
    @Test
    public void canCheckCredentials()
    {
    	TextField usernameTextField = find("#usernameTextField");
    	PasswordField passwordField = find("#passwordField");
    	usernameTextField.setText("David");
    	passwordField.setText("password");
    	click("#signInButton");
    	assertEquals("login ok", result);
    }
}

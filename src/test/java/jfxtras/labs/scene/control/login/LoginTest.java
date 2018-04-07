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

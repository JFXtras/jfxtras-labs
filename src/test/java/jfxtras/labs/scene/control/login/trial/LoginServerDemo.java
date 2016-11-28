package jfxtras.labs.scene.control.login.trial;


import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.internal.scene.control.skin.login.LoginServerSkin;
import jfxtras.labs.scene.control.login.Login;

/**
 * Login customized with new image, css and resource bundle.
 * 
 * @author David Bal
 *
 */
public class LoginServerDemo extends Application
{
	private Object result;
	private String username = "David";
	
    public static void main(String[] args) {
        launch(args);       
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception
	{	
		Callback<String[], Void> loginCallback = (credentials) ->
		{
			username = credentials[0];
			String password = credentials[1];
			String server = credentials[2];
			if (username.equals("David") && password.equals("password") && server.equals("LocalHost"))
			{
				result = "login ok";
				primaryStage.hide();
			} else
			{
				result  = "login failed";
			}
			System.out.println(username + ", " + password + ":" + result);
			return null;
		};

        Locale myLocale = Locale.getDefault();
		ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.scene.control.login.LoginServerDefault", myLocale);
		Login root = new Login();
		LoginServerSkin skin = new LoginServerSkin(
		        root,
		        loginCallback,
	            resources,
	            "David",
	            Arrays.asList("LocalHost", "Apple", "Banana", "173.29.20.182")
		        );
		root.setSkin(skin);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}

package jfxtras.labs.scene.control.login.trial;


import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.scene.control.login.Login;

/**
 * Login customized with new image, css and resource bundle.
 * 
 * @author David Bal
 *
 */
public class LoginDemo2 extends Application
{
	private Object result;
	private String username = "David";
	
    public static void main(String[] args) {
        launch(args);       
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception
	{	
		Callback<Pair<String, String>, Void> loginCallback = (credentials) ->
		{
			username = credentials.getKey();
			String password = credentials.getValue();
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
		ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.scene.control.login.trial.NewLogin", myLocale);
		Login root = new Login(resources, username, loginCallback);
		String style = LoginDemo2.class.getResource("NewStylesheet.css").toExternalForm();
		root.getStylesheets().add(style);
        Scene scene = new Scene(root, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}

package jfxtras.labs.scene.control.login.trial;


import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.scene.control.login.Login;

/**
 * Login customized with new image, css and resource bundle.
 * 
 * @author David Bal
 *
 */
public class LoginDemo extends Application
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
			if (username.equals("David") && password.equals("password"))
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
		ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.scene.control.login.trial.NewLogin", myLocale);
		Login root = new Login(loginCallback, resources, username);
		String style = LoginDemo.class.getResource("NewStylesheet.css").toExternalForm();
		root.getStylesheets().add(style);
        Scene scene = new Scene(root, 400, 270);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}

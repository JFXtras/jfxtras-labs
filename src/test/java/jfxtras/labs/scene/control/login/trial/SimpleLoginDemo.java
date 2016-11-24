package jfxtras.labs.scene.control.login.trial;


import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.login.Login;

/**
 * Login customized with new image, css and resource bundle.
 * 
 * @author David Bal
 *
 */
public class SimpleLoginDemo extends Application
{
    public static void main(String[] args) {
        launch(args);       
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Locale myLocale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.scene.control.login.LoginDefault", myLocale);
        Login root = new Login(resources, null, (p) -> null);
        Scene scene = new Scene(root, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

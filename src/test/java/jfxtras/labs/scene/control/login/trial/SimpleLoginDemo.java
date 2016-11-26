package jfxtras.labs.scene.control.login.trial;


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
        Login root = new Login();
        Scene scene = new Scene(root, 400, 270);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

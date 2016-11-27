package jfxtras.labs.scene.control.login;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.labs.internal.scene.control.skin.login.LoginSkin;

public class Login extends Control
{
    private static Locale myLocale = Locale.ENGLISH;

    /* Resource Bundle */
    private ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.scene.control.login.LoginDefault", myLocale);
    /** Gets resource bundle used to populate control elements */
    public ResourceBundle getResources()
    {
        return resources;
    }
    /** Sets resource bundle used to populate control elements */
    public void setResources(ResourceBundle resources)
    {
        this.resources = resources;
    }

    /* Username */
    private String initialUsername = null;
    /** Gets initial username that is inserted into the username text field on opening the control. */
    public String getInitialUsername()
    {
        return initialUsername;
    }
    /** Sets initial username that is inserted into the username text field on opening the control*/
    public void setInitialUsername(String initialUsername)
    {
        this.initialUsername = initialUsername;
    }

    /* Log in callback */
    private Callback<String[], Void> loginCallback = (credentials) -> null;
    /** Gets callback that is executed on clicking "sign in" */
    public Callback<String[], Void> getLoginCallback()
    {
        return loginCallback;
    }
    /** Sets callback that is executed on clicking "sign in" */
    public void setSigninCallback(Callback<String[], Void> loginCallback)
    {
        this.loginCallback = loginCallback;
    }

    /* Array of additional arguments for skins */
    private Object[] args;
    
    /**
     * No-arg constructor uses default English bundle, empty initial username, and no-op callback
     */
    public Login()
    {
        String style = Login.class.getResource("DefaultStylesheet.css").toExternalForm();
        getStylesheets().add(style);
        setId("Login-" + resources.getString("organization"));
    }
    
    public Login(Callback<String[], Void> loginCallback, ResourceBundle resources, String initialUsername)
    {
        this();
        this.resources = resources;
        this.initialUsername = initialUsername;
        this.loginCallback = loginCallback;
    }
    
    @Override
    public Skin<?> createDefaultSkin() {
        return new LoginSkin(
                this,
                resources,
                initialUsername,
                loginCallback);
    }
}

package jfxtras.labs.internal.scene.control.skin.login;

import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.SkinBase;
import javafx.util.Callback;
import jfxtras.labs.scene.control.login.Login;

public class LoginServerSkin extends SkinBase<Login>
{
    public LoginServerSkin(
            Login control,
    		Callback<String[], Void> loginCallback,
    		ResourceBundle resources,
    		String initialUsername,
    		List<String> serverNames)
    {
        super(control);
        LoginServerBorderPane borderPane = new LoginServerBorderPane(loginCallback, resources, initialUsername, serverNames);
		borderPane.prefWidthProperty().bind(getSkinnable().widthProperty()); // the border pane is the same size as the whole skin
		borderPane.prefHeightProperty().bind(getSkinnable().heightProperty());
		getChildren().add(borderPane);
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
        getSkinnable().getStyleClass().add(LoginSkin.class.getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
    }
}

package jfxtras.labs.scene.control.login;

import java.util.ResourceBundle;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.internal.scene.control.skin.login.LoginSkin;

public class Login extends Control
{
	private ResourceBundle resources;
	private String initialUsername;
	private Callback<Pair<String, String>, Void> loginCallback;
	
	public Login(ResourceBundle resources, String initialUsername, Callback<Pair<String, String>, Void> loginCallback)
	{
		this.resources = resources;
		this.initialUsername = initialUsername;
		this.loginCallback = loginCallback;
		String style = Login.class.getResource("DefaultStylesheet.css").toExternalForm();
		getStylesheets().add(style);
		setId("Login-" + resources.getString("organization"));
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

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

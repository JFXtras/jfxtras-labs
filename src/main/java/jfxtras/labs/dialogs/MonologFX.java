/**
 * MonologFX.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.dialogs;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @MkHeck)
 */
public class MonologFX extends Stage {
    /**
     * Type of dialog box is one of the following, each with a distinct icon:
     * <p>
     * ACCEPT = check mark icon
     * <p>
     * ERROR = red 'X' icon
     * <p>
     * INFO = blue 'i' (information) icon
     * <p>
     * QUESTION = blue question mark icon
     * <p>
     * If no type is specified in the constructor, the default is INFO.
     */
    public enum Type { ACCEPT, ERROR, INFO, QUESTION };
    public enum ButtonAlignment { LEFT, RIGHT, CENTER };

    private Type type;
    private Scene scene;
    private BorderPane pane = new BorderPane();
    private ImageView icon = new ImageView();
    private Label message = new Label();
    private HBox buttonBox = new HBox(10);
    private List<MonologFXButton> buttons = new ArrayList<>();
    private int buttonCancel = -1;
    private int buttonSelected = -1;
    private ButtonAlignment buttonAlignment = ButtonAlignment.CENTER;
    private List<String> stylesheets = new ArrayList<>();

    /**
     * Default constructor for a MonologFX dialog box. Creates an INFO box.
     * 
     * @see Type
     */
    public MonologFX() {
        initDialog(Type.INFO);
    }
    
    /**
     * Constructor for a MonologFX dialog box that accepts one of the enumerated
     * types listed above.
     * 
     * @param t The type of MonologFX dialog box to create.
     * @see Type
     */
    public MonologFX(Type t) {
        initDialog(t);
    }
    
    private void addOKButton() {
        MonologFXButton okBtn = new MonologFXButton();
        okBtn.setType(MonologFXButton.Type.OK);
        okBtn.setLabel("_OK");
        okBtn.setCancelButton(true);
        okBtn.setDefaultButton(true);
        
        addButton(okBtn);
    }
    
    private void addYesNoButtons() {
        /*
         * No default or cancel buttons designated, by design.
         * Some cases would require the Yes button to be default & No to cancel,
         * while others would require the opposite. You as the developer can 
         * assign default/cancel Yes/No buttons using the full addButtons()
         * method if required. You have the power!
         */
        MonologFXButton yesBtn = new MonologFXButton();
        yesBtn.setType(MonologFXButton.Type.YES);
        yesBtn.setLabel("_Yes");
        yesBtn.setCancelButton(false);
        yesBtn.setDefaultButton(false);
        
        addButton(yesBtn);
        
        MonologFXButton noBtn = new MonologFXButton();
        noBtn.setType(MonologFXButton.Type.NO);
        noBtn.setLabel("_No");
        noBtn.setCancelButton(false);
        noBtn.setDefaultButton(false);
        
        addButton(noBtn);
    }
    
    /**
     * Public method used to add a button to a MonologFX dialog.
     * 
     * @param btnToAdd A MonologFXButton object.
     * 
     * @see MonologFXButton
     */
    public void addButton(MonologFXButton btnToAdd) {
        buttons.add(btnToAdd);
        
        final Button btn = new Button();
        
        btn.setMnemonicParsing(true);
        btn.setText(btnToAdd.getLabel());
        
        if ( btnToAdd.getIcon() != null ) {
            btn.setGraphic(btnToAdd.getIcon());
        }
        
        btn.setDefaultButton(btnToAdd.isDefaultButton());
        if ( btnToAdd.isCancelButton() ) {
            btn.setCancelButton(true);
            buttonCancel = buttons.size() - 1;
        }

        if ( btn.isDefaultButton() ) {
            Platform.runLater( new Runnable() {
                @Override
                public void run() {
                    btn.requestFocus();
                }
            } );
        }

        btn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
            public void handle(ActionEvent evt) {
                // Iterate through to find correct button.
                for (int i=0; i < buttons.size(); i++) {
                    if ( buttons.get(i).getLabel().equalsIgnoreCase( ((Button) evt.getSource()).getText() ) ) {
                        buttonSelected = i;
                        break;
                    }
                }

                // Close the dialog
                ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
            }
        });
        
        buttonBox.getChildren().add(btn);
    }

    /**
     * Allows developer to add stylesheet for MonologFX dialog, supplementing or 
     * overriding existing styling.
     * 
     * @param stylesheet String variable containing the path/name of the 
     * stylesheet to apply to the dialog's scene and contained controls.
     */
    public void addStylesheet(String stylesheet) {
        try {
            String newStyle  = this.getClass().getResource(stylesheet).toExternalForm();
            stylesheets.add(newStyle);
        } catch (Exception ex) {
            System.err.println("Unable to find specified stylesheet: " + stylesheet);
            System.err.println("Error message: " + ex.getMessage());
        }
    }
    
    private void initDialog(Type t) {
        this.initStyle(StageStyle.UTILITY);
        
        setType(t);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);        
    }
    
    private void loadIconFromResource(String fileName) {
        Image imgIcon = new Image(getClass().getResourceAsStream(fileName));
        icon.setPreserveRatio(true);
        icon.setFitHeight(48);
        icon.setImage(imgIcon);
    }

    /**
     * Sets the button alignment for the MonologFX dialog box. Default is CENTER.
     * 
     * @param buttonAlignment Valid values are LEFT, RIGHT, and CENTER.
     * 
     * @see ButtonAlignment
     */
    public void setButtonAlignment(ButtonAlignment buttonAlignment) {
        this.buttonAlignment = buttonAlignment;
    }

    /**
     * Sets the text displayed within the MonologFX dialog box. Word wrap ensures
     * that all text is displayed.
     * 
     * @param msg String variable containing the text to display.
     */
    public void setMessage(String msg) {
        message.setText(msg);
        message.setWrapText(true);
    }
   
    /**
     * Sets the modality of the MonologFX dialog box.
     * 
     * @param isModal Boolean. A true value = APPLICATION_MODAL, false = NONE.
     */
    public void setModal(boolean isModal) {
        this.initModality((isModal ? Modality.APPLICATION_MODAL : Modality.NONE));
    }
    
    /**
     * Sets the text displayed in the title bar of the MonologFX dialog box.
     * 
     * @param title String containing the text to place in the title bar.
     */
    public void setTitleText(String title) {
        this.setTitle(title);
    }
    
    /**
     * Sets the Type of MonologFX dialog box to display.
     * 
     * @param typeToSet One of the supported types of dialogs.
     * @see Type
     */
    public void setType(Type typeToSet) {
        type = typeToSet;
    }
    
    public void setPos(double x, double y) {
        this.setX(x);
        this.setY(y);
    }
    
    private void populateStage() {
        String iconFile;
        
        switch ( type ) {
            case ACCEPT:
                iconFile = "Dialog-accept.jpg";
                if (buttons.size() == 0) {
                    addOKButton();
                }
                break;
            case ERROR:
                iconFile = "Dialog-error.jpg";
                if (buttons.size() == 0) {
                    addOKButton();
                }
                break;
            case INFO:
                iconFile = "Dialog-info.jpg";
                if (buttons.size() == 0) {
                    addOKButton();
                }
                break;
            case QUESTION:
                iconFile = "Dialog-question.jpg";
                break;
            default:
                iconFile = "Dialog-info.jpg";
                break;
        }
        
        try {
            loadIconFromResource(iconFile);
        } catch (Exception ex) {
            System.err.println("Exception trying to load icon file: " + ex.getMessage());
        }
        
        BorderPane.setAlignment(icon, Pos.CENTER_LEFT);
        BorderPane.setMargin(icon, new Insets(5));
        pane.setLeft(icon);
        
        BorderPane.setAlignment(message, Pos.CENTER);
        BorderPane.setMargin(message, new Insets(5));
        pane.setCenter(message);
        
        switch (buttonAlignment) {
            case LEFT:
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                break;
            case CENTER:
                buttonBox.setAlignment(Pos.CENTER);
                break;
            case RIGHT:
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                break;
        }

        BorderPane.setMargin(buttonBox, new Insets(5));
        pane.setBottom(buttonBox);
        
        scene = new Scene(pane);
        for (int i=0;i<stylesheets.size();i++) {
            try {
                scene.getStylesheets().add(stylesheets.get(i));
            } catch (Exception ex) {
                System.err.println("Unable to load specified stylesheet: " + stylesheets.get(i));
                System.err.println(ex.getMessage());
            }
        }
        this.setScene(scene);
    }
    
    /**
     * Displays the MonologFX dialog box and waits for user input.
     * 
     * @return The type of the button pressed.
     * 
     * @see MonologFXButton.Type
     */
    public MonologFXButton.Type showDialog() {
        populateStage();
        if ( type == Type.QUESTION ) {
            if ( buttons.size() == 0 ) {
                addYesNoButtons();
            }
        }
        
        this.setResizable(false);
        this.sizeToScene();        
        
        if (!(this.getX() > -1 && this.getY() > -1)) {
            this.centerOnScreen();
        }
        
        this.showAndWait();
        if ( buttonSelected == -1 ) {
            /* If a different type of button is designated the "cancel button",
             * e.g. a MonologFXButton.Type.NO button, return that one;
             * otherwise, return a CANCEL button type.
             */
            return ( buttonCancel == -1 ? MonologFXButton.Type.CANCEL : buttons.get(buttonCancel).getType() );
        } else {
            return buttons.get(buttonSelected).getType();
        }
    }
}

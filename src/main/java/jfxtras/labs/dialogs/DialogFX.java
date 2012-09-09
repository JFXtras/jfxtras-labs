/**
 * Copyright (c) 2012, JFXtras
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @HecklerMark)
 */
public final class DialogFX extends Stage {
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
    
    private static final String DIALOG_ICON_PATH = "/jfxtras/labs/dialogs/";
    
    private Type type;
    private Stage stage;
    private Scene scene;
    private BorderPane pane = new BorderPane();
    private ImageView icon = new ImageView();
    private Label message = new Label();
    private HBox buttonBox = new HBox(10);
    private List<String> buttonLabels;
    private int buttonCount = 0;
    private int buttonSelected = 0;
    
    /**
     * Default constructor for a DialogFX dialog box. Creates an INFO box.
     * 
     * @see Type
     */
    public DialogFX() {
        initDialog(Type.INFO);
    }
    
    /**
     * Constructor for a DialogFX dialog box that accepts one of the enumerated
     * types listed above.
     * 
     * @param t The type of DialogFX dialog box to create.
     * @see Type
     */
    public DialogFX(Type t) {
        initDialog(t);
    }
    
    /**
     * Public method used to add custom buttons to a DialogFX dialog.
     * 
     * @param labels A list of String variables. While technically unlimited,
     * usability makes the practical limit around three.
     */
    public void addButtons(List<String> labels) {
        addButtons(labels, -1, -1);
    }
    
    /**
     * Public method used to add custom buttons to a DialogFX dialog.
     * Additionally, default and cancel buttons are identified so user can
     * trigger them with the ENTER key (default) and ESCAPE (cancel).
     * 
     * @param labels A list of String variables. While technically unlimited,
     * usability makes the practical limit around three.
     * @param defaultBtn Position within the list of labels of the button to 
     * designate as the default button.
     * @param cancelBtn Position within the list of labels of the button to 
     * designate as the cancel button.
     */
    public void addButtons(List<String> labels, int defaultBtn, int cancelBtn) {
        buttonLabels = labels;
        
        for (int i=0; i<labels.size(); i++) {
            Button btn = new Button(labels.get(i));
            
            btn.setDefaultButton(i==defaultBtn);
            btn.setCancelButton(i==cancelBtn);
            
            btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent evt) {
                    buttonSelected = buttonLabels.indexOf(((Button) evt.getSource()).getText());
                    stage.close();
                }
            });
            buttonBox.getChildren().add(btn);
        }
        
        buttonBox.setAlignment(Pos.CENTER);
        
        BorderPane.setAlignment(buttonBox, Pos.CENTER);
        BorderPane.setMargin(buttonBox, new Insets(2,2,2,2));
        pane.setBottom(buttonBox);
        buttonCount = labels.size();  
    }
    
    private void addOKButton() {
        List<String> labels = new ArrayList<String>(1);
        labels.add("OK");
        
        addButtons(labels, 0, 0);
    }
    
    private void addYesNoButtons() {
        /*
         * No default or cancel buttons designated, by design.
         * Some cases would require the Yes button to be default & No to cancel,
         * while others would require the opposite. You as the developer can 
         * assign default/cancel Yes/No buttons using the full addButtons()
         * method if required. You have the power!
         */
        List<String> labels = new ArrayList<String>(2);
        labels.add("Yes");
        labels.add("No");
        
        addButtons(labels);
    }
    
    private void initDialog(Type t) {
        stage = new Stage();
        
        setType(t);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
    }
    
    private void loadIconFromResource(String fileName) {
        Image imgIcon = new Image(getClass().getResourceAsStream(DIALOG_ICON_PATH + fileName));
        icon.setPreserveRatio(true);
        icon.setFitHeight(48);
        icon.setImage(imgIcon);
    }
   
    /**
     * Sets the text displayed within the DialogFX dialog box. Word wrap ensures
     * that all text is displayed.
     * 
     * @param msg String variable containing the text to display.
     */
    public void setMessage(String msg) {
        message.setText(msg);
        message.setWrapText(true);
    }
   
    /**
     * Sets the modality of the DialogFX dialog box.
     * 
     * @param isModal Boolean. A true value = APPLICATION_MODAL, false = NONE.
     */
    public void setModal(boolean isModal) {
        stage.initModality((isModal ? Modality.APPLICATION_MODAL : Modality.NONE));
    }
    
    /**
     * Sets the text diplayed in the title bar of the DialogFX dialog box.
     * 
     * @param title String containing the text to place in the title bar.
     */
    public void setTitleText(String title) {
        stage.setTitle(title);
    }
    
    /**
     * Sets the Type of DialogFX dialog box to display.
     * 
     * @param typeToSet One of the supported types of dialogs.
     * @see Type
     */
    public void setType(Type typeToSet) {
        type = typeToSet;
    }
    
    private void populateStage() {
        String iconFile;
        
        switch ( type ) {
            case ACCEPT:
                iconFile = "Dialog-accept.jpg";
                addOKButton();
                break;
            case ERROR:
                iconFile = "Dialog-error.jpg";
                addOKButton();
                break;
            case INFO:
                iconFile = "Dialog-info.jpg";
                addOKButton();
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
        
        BorderPane.setAlignment(icon, Pos.CENTER);
        BorderPane.setMargin(icon, new Insets(2,2,2,2));
        pane.setLeft(icon);
        
        BorderPane.setAlignment(message, Pos.CENTER);
        BorderPane.setMargin(message, new Insets(2,2,2,2));
        pane.setCenter(message);
        
        scene = new Scene(pane);
        stage.setScene(scene);
    }
    
    /**
     * Displays the DialogFX dialog box and waits for user input.
     * 
     * @return The index of the button pressed.
     */
    public int showDialog() {
        populateStage();
        if ( type == Type.QUESTION ) {
            if ( buttonCount == 0 ) {
                addYesNoButtons();
            }
        }
        
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.showAndWait();
        return buttonSelected;
    }
}

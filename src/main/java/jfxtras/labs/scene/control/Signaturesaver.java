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
package jfxtras.labs.scene.control;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

class Signaturesaver extends Canvas{

    //Signature Saver is node that can be put on gui for users to pen their signatures and save them to a file.
    //could be useful in a number of different applications
    
    
    private double inx;
    private double iny;
    private String savebuttontext;
    private Button saveButton = new Button(savebuttontext);  
    private GraphicsContext gc;
    private WritableImage signature;
    private final FileChooser fc = new FileChooser();
    private Stage stage;
    private static int filecounter = 0;
    
    public Signaturesaver(){
        //if they dont provide a primary stage, the image will be saved to a default filename in the current dir;
        this(null);
    }
    
    public Signaturesaver(Stage currentstage){
        
        //the user can either call getsaveButton() and add that to this one to their container along with the Signaturesave canvas
        //or they can provide their own.
        //A Signaturesave will not work without a save button though.
        
        super(800,500);
        filecounter++;
        gc = this.getGraphicsContext2D();
        signature = new WritableImage((int)this.getWidth(), (int)this.getHeight());
        stage = currentstage;
        
        
        addEventHandler(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>() {
        
            public void handle(MouseEvent e) {
                inx = e.getSceneX();
                iny = e.getSceneY();
                e.consume();
                
            }
     });
        
        addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {  
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                e.consume();
            }
        });
        
        saveButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                File imageFile;
                
                snapshot(null,signature);
                if(stage == null){
                    imageFile = new File("SavedSignature"+filecounter+".png");
                }else
                    imageFile = fc.showSaveDialog(stage);
                
                try{
                    ImageIO.write(SwingFXUtils.fromFXImage(signature,null),"png", imageFile);
                }catch(IOException ioe){
                    System.out.println("something went wrong writing the image to disk");
                }
                
            }    
        });
        
        
    }
    
    public Button getSaveButton(){
            return saveButton;
    }
    
    public void setSaveButtonText(String text){
        savebuttontext = text;
    }
    
    public void setButton(Button savebutton){
        this.saveButton = savebutton;
    }
   
}


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
package jfxtras.labs.animation;

// max bisesi
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

 class AllSphere extends Sphere{

    private Material material;
    private SequentialTransition st = new SequentialTransition();
    private TranslateTransition moveAway = new TranslateTransition(Duration.millis(300),this);
    private static Group root;
  
    //AllSphereType type;
    public enum AllSphereType{
        RECORDADDER, EYECANDY, UPDATER;
    };
    
    public AllSphere(double radius){
        super(radius);
        setMaterial(new PhongMaterial( Color.CYAN));
        setCursor(Cursor.HAND);
        floatAnimation();
        setClickBehavior(AllSphereType.RECORDADDER);
        setLayoutX(100);
        setLayoutY(100);

        moveAway.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){ 
                moveAway.jumpTo(Duration.ZERO);
            }
        });
           
    }
    public AllSphere(double radius, PhongMaterial mat){
        super(radius);
        setMaterial(mat);
        setCursor(Cursor.HAND);
        floatAnimation();
    }
 
    public void floatAnimation(){
        
        //add sphere animations
        KeyValue kv = new KeyValue(translateYProperty(),20);
        Timeline animation = new Timeline();
        animation.getKeyFrames().add(new KeyFrame(Duration.millis(3000),kv));
 
        KeyValue kv4 = new KeyValue(translateYProperty(),5); 
        Timeline animation2 = new Timeline();
        animation2.getKeyFrames().add(new KeyFrame(Duration.millis(1500),kv4));
        
        KeyValue kv5 = new KeyValue(translateYProperty(),10);
        Timeline animation3 = new Timeline();
        animation3.getKeyFrames().add(new KeyFrame(Duration.millis(2000),kv5));
       
        st.getChildren().addAll(animation,animation2,animation3);
        st.setCycleCount(Timeline.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
    }
    
    public void moveAway(){ 
        st.jumpTo(Duration.ZERO);
        moveAway.setByX(800);
        moveAway.setCycleCount(1);
        moveAway.play();  
    }
    
    public void setFinishOperation(EventHandler<ActionEvent> ea){
        moveAway.setOnFinished(ea);
    }
    
    private void setClickBehavior(AllSphereType x){
        
        if(x.equals(AllSphereType.RECORDADDER)){
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e){
                    moveAway();
                }
            });
        }
        
        if(x.equals(AllSphereType.EYECANDY)){
            
        }
        
        if(x.equals(AllSphereType.UPDATER)){
            
        }
        
    }
}

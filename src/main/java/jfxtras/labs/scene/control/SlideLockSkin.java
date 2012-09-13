/**
 * Copyright (c) 2011, JFXtras
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
package jfxtras.labs.scene.control;


import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.SVGPathBuilder;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

/**
 *
 * @author cdea
 */
public class SlideLockSkin extends Region implements Skin<SlideLock>{
    final private SlideLock slideLock;

    
    public SlideLockSkin(final SlideLock slideLock) {
        this.slideLock = slideLock;

        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
        // 0 background
        Node slideBackground = createSlideBackground();
        getChildren().add(slideBackground);
        
        // 1 slide area
        getChildren().add(createSlideArea());
        
        // 2 glare frame
        Node glareFrame = createGlareFrame();
        getChildren().add(glareFrame);        
        
        // 3 text
        final Node text = createText();
        getChildren().add(text);
        
        // 4 
        final Node slideButton = createSlideButton();
        slideButton.setTranslateX(SlideLock.START_XCOORD);
        slideButton.translateXProperty().bind(slideLock.endXProperty());
        slideButton.setTranslateY(SlideLock.BUTTON_YCOORD);
        getChildren().add(slideButton);
        
        text.setTranslateX(SlideLock.START_XCOORD + slideButton.getBoundsInParent().getWidth() + 20 ); //182(move to the right of the button (left x plus width of button)
        text.setTranslateY(108);
        text.opacityProperty().bind(slideLock.textOpacityProperty()); // change it.
        

        
        // 5 top slide glare
        Node topSlideGlareRect = createTopSlideGlareRect();
        getChildren().add(topSlideGlareRect);
        
        // begin mouse press
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                buttonPressAction(slideButton, event.getX(), event.getY());
            }
        });
        
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                moveButtonAction(slideButton, event.getX(), event.getY());               
           }
        });
        
        setOnTouchPressed(new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                buttonPressAction(slideButton, event.getTouchPoint().getX(), event.getTouchPoint().getY());
            }
        });
        setOnTouchMoved(new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                moveButtonAction(slideButton, event.getTouchPoint().getX(), event.getTouchPoint().getY());
            }
        });
        
        
        

       
        // set the current location
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event){
                buttonSnapBack(slideLock.getSnapButtonBackAnim());
                if (!slideLock.lockedProperty().get()) {
                    System.out.println("Unlocked");
                } else {
                    System.out.println("Locked");
                }
            }
        });
        
        setOnTouchReleased(new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                buttonSnapBack(slideLock.getSnapButtonBackAnim());
                if (!slideLock.lockedProperty().get()) {
                    System.out.println("Unlocked");
                } else {
                    System.out.println("Locked");
                }
            }
        });

    } 
   
//    @Override
//    protected void layoutChildren() {
//        double width = getWidth();
//        double height = getHeight();
//        //               Node        x,          y,    width,   height, baselOff,       Hpos,        Vpos
//        layoutInArea(children.get(0), 0d,        0d,    111d,    111d,   50d,           HPos.LEFT, VPos.CENTER);
//    }

    @Override
    public SlideLock getSkinnable() {
        return slideLock;
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void buttonPressAction(final Node slideButton, double x, double y) {
        if (slideButton.getBoundsInParent().contains(x, y)) {
            slideLock.startXProperty().set(x - slideLock.endXProperty().get());
            System.out.println("press translate X: " + slideButton.getTranslateX() + 
                    " start X: " + slideLock.startXProperty().get());
        }
    }
    private void moveButtonAction(final Node slideButton, double x, double y) {
        
        String buttonPressed = Boolean.toString(slideButton.getBoundsInParent().contains(x, y));
        // check when user pressed the button
        if (slideButton.getBoundsInParent().contains(x, y)) {
            slideLock.endXProperty().set(x- slideLock.startXProperty().get());
            if (slideButton.getTranslateX() < SlideLock.START_XCOORD) {
                slideLock.lockedProperty().set(true);
                slideLock.endXProperty().set(SlideLock.START_XCOORD);
            } else if (slideButton.getTranslateX() > SlideLock.END_XCOORD) {
                slideLock.lockedProperty().set(false);
                slideLock.endXProperty().set(SlideLock.END_XCOORD);
            } else {
                slideLock.lockedProperty().set(true);

            }
            double opacity = 1d - (double)(slideButton.getTranslateX()) / 200;
            if (opacity < 0) {
                opacity = 0;
            }
            slideLock.textOpacityProperty().set(opacity);

            System.out.println("drag translate X: " + slideButton.getTranslateX() + 
                    " opacity " + (slideLock.textOpacityProperty()) + 
                    " start X: " + slideLock.startXProperty() + 
                    " is Locked: " + slideLock.lockedProperty());
        }
    }
    
    private void buttonSnapBack(Timeline snapBack) {
        if (slideLock.lockedProperty().getValue()){
            slideLock.getSnapButtonBackAnim().play();
        }
    }
    
    private Rectangle createSlideBackground() {
        Rectangle backgroundRect = RectangleBuilder.create()
                                        .id("slide-background")
                                        .width(523.28571)
                                        .height(188)
                                        
                                        .build();
        return backgroundRect;
    }
    private Rectangle createSlideArea() {
        Rectangle slideArea = RectangleBuilder.create()
                                        .id("slide-area")
                                        .x(32.05)
                                        .y(46.31)
                                        .width(462.01947)
                                        .height(100.01947)
                                        .arcWidth(15)
                                        .arcHeight(15)
                                        .build();
        return slideArea;
    }
    
    private Text createText() {
        Text text = TextBuilder.create()
            .id("slide-text")
            .text(slideLock.textProperty().get())
            .build();
        return text;
    }
    
    private SVGPath createGlareFrame() {
        SVGPath glareRect = SVGPathBuilder.create()
                                        .fill(LinearGradientBuilder.create()
                                            .proportional(true)
                                            .startX(0)
                                            .startY(0)
                                            .endX(0)
                                            .endY(1)
                                            .stops(new Stop(0, Color.web("f0f0f0", 1)),
                                                   new Stop(1, Color.web("f0f0f0", 0))
                                            ).build()
                                        )
                                        .opacity(.274)
                                        .content("m 0,0 0,94 32,0 0,-27.218747 C 30.998808,55.222973 37.761737,45.9354 46.156457,45.93665 l 431.687503,0.06427 c 8.39472,0.0013 15.15487,9.290837 15.15315,20.814756 l -0.004,27.218754 30.28125,0 0,-94.0000031 L 0,0 z")
                                        .id("glare-frame")
                                        .build();
        return glareRect;
    }

    private Node createSlideButton() {
        Group button = new Group();
        
        // build gradientRect
        Rectangle gradientRect = RectangleBuilder.create()
                .x(18.783)
                .y(12.211)
                .width(81.87566)
                .height(67.9892)
                .arcWidth(17.466)
                .arcWidth(17.466)
//                .fill(Color.WHITE)
                .fill(LinearGradientBuilder.create()
                    .proportional(true)
                    .startX(0)
                    .startY(1)
                    .endX(0)
                    .endY(0)
                    .stops(new Stop(0, Color.web("747474")),
                           new Stop(1, Color.web("e8e8e8")))
                .build()
                 )
                .id("button-gradient-rect")
                .build();
        button.getChildren().add(gradientRect);
        
        
        // build arrowBlurShadow
        SVGPath arrowBlurShadow = SVGPathBuilder.create()
                .fill(Color.BLACK)
                .effect(new GaussianBlur(5))
                .content("m 17.40912,2.47162 c -8.27303,0 -14.9375,7.04253 -14.9375,15.78125 l 0,59.9375 c 0,8.73872 6.66447,15.75 14.9375,15.75 l 84.625,0 c 8.27303,0 14.9375,-7.01128 14.9375,-15.75 l 0,-59.9375 c 0,-8.73872 -6.66447,-15.78125 -14.9375,-15.78125 l -84.625,0 z m 45.0625,18.15625 27.5625,27.59375 -27.5625,27.5625 0,-15.5625 -33.0625,0 0,-24 33.0625,0 0,-15.59375 z")
                .id("#button-arrow-blur-shadow")
                .build();
        
        
        button.getChildren().add(arrowBlurShadow);

        
        
        
        // build arrowStencilCrisp
        SVGPath arrowStencilCrisp = SVGPathBuilder.create()
                .content("m 17.40912,0.47162 c -8.27303,0 -14.9375,7.04253 -14.9375,15.78125 l 0,59.9375 c 0,8.73872 6.66447,15.75 14.9375,15.75 l 84.625,0 c 8.27303,0 14.9375,-7.01128 14.9375,-15.75 l 0,-59.9375 c 0,-8.73872 -6.66447,-15.78125 -14.9375,-15.78125 l -84.625,0 z m 45.0625,18.15625 27.5625,27.59375 -27.5625,27.5625 0,-15.5625 -33.0625,0 0,-24 33.0625,0 0,-15.59375 z")
                .fill(LinearGradientBuilder.create()
                    .proportional(true)
                    .startX(0)
                    .startY(1)
                    .endX(0)
                    .endY(0)
                    .stops(new Stop(0, Color.web("c5c5c5")),
                           new Stop(1, Color.web("f0f0f0"))).build()
//                    .stops(new Stop(0, Color.web("ff0000")),
//                    new Stop(1, Color.web("f0f0f0"))).build() // red button
                 )
                .id("#button-arrow-stencil-crisp")
                .build();
        button.getChildren().add(arrowStencilCrisp);
        
        // build glareRect
        SVGPath glareRect = SVGPathBuilder.create()
                .content("m 17.83252,1.67757 c -8.27303,0 -14.9375,7.21042 -14.9375,16.15746 l 0,28.31557 114.5,0 0,-28.31557 c 0,-8.94704 -6.66447,-16.15746 -14.9375,-16.15746 l -84.625,0 z")
                .fill(LinearGradientBuilder.create()
                    .proportional(true)
                    .startX(0)
                    .startY(1)
                    .endX(0)
                    .endY(0)
                    .stops(new Stop(0, Color.web("f4f4f4", 0.60)),
                           new Stop(1, Color.web("ffffff", 0.2063063)))
                    .build()
                 )
                .id("#button-arrow-glare-rect")
                .build();
//        glareRect.setVisible(false); // red button
        button.getChildren().add(glareRect);
        
        return button;
    }
    
    private Rectangle createTopSlideGlareRect() {
        Rectangle glareRect = RectangleBuilder.create()
                                        .id("slide-top-glare")
                                        .fill(Color.WHITE)
                                        .width(523.28571)
                                        .height(94)
                                        .opacity(0.0627451)
                                        .build();
        return glareRect;
    }
    
}

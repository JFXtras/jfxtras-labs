/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.internal.scene.control.skin;

import javafx.scene.control.SkinBase;
import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;
import jfxtras.labs.internal.scene.control.behavior.MatrixPanelBehavior;
import jfxtras.labs.scene.control.gauge.Content;
import jfxtras.labs.scene.control.gauge.Content.MatrixColor;
import jfxtras.labs.scene.control.gauge.MatrixPanel;
import jfxtras.labs.scene.control.gauge.UtilHex;
import jfxtras.labs.util.ConicalGradient;
import jfxtras.labs.util.Util;


/**
 * Created by
 * User: hansolo
 * Date: 09.01.12
 * Time: 18:04
 * Modified by Jose Pereda Llamas <jperedadnr>
 * On : 23-jun-2012, 11:47:23
 */
public class MatrixPanelSkin extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<MatrixPanel, MatrixPanelBehavior> {
    private static final Rectangle PREF_SIZE = new Rectangle(170, 350);

    
    private MatrixPanel         control;
    private Rectangle           gaugeBounds;
    private Point2D             framelessOffset;
    private Group               frame;
    private Group               background;
    private Group               matrix;
    private Group               foreground;
    private boolean             isDirty;
    private boolean             initialized;
    private IntegerProperty     ledWidth;
    private IntegerProperty     ledHeight;
    private Group               dots;
    private ObservableList<Content> contents;
    private Map<Integer, Shape> dotMap;
    private BooleanProperty[]   visibleContent=null;
    private final int           toneScale=85;
    private final Color         COLOR_OFF = Color.rgb(39, 39, 39,0.25);
                    
    // ******************** Constructors **************************************
    public MatrixPanelSkin(final MatrixPanel CONTROL) {
        super(CONTROL, new MatrixPanelBehavior(CONTROL));
        control = CONTROL;
        gaugeBounds = new Rectangle(800, 600);
        framelessOffset = new Point2D(0, 0);
        frame = new Group();
        background = new Group();
        matrix = new Group();
        foreground = new Group();
        ledWidth = new SimpleIntegerProperty(0);
        ledHeight = new SimpleIntegerProperty(0);
        dots = new Group();
        
        isDirty = false;
        initialized = false;
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (control.getPrefWidth() < 0 || control.getPrefHeight() < 0) {
            control.setPrefSize(PREF_SIZE.getWidth(), PREF_SIZE.getHeight());
        }
        ledWidth.bind(control.ledWidthProperty());
        ledHeight.bind(control.ledHeightProperty());

        contents = control.getContents();

        addBindings();
        addListeners();

        initialized = true;
        paint();
    }

    private void addBindings() {
        if (frame.visibleProperty().isBound()) {
            frame.visibleProperty().unbind();
        }
        frame.visibleProperty().bind(control.frameVisibleProperty());       

    }

    private void addListeners() {
        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                isDirty = true;
            }
        });

        ledHeight.addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                isDirty = true;
            }
        });
        ledWidth.addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                isDirty = true;
            }
        });
              
        contents.addListener(new ListChangeListener(){
            @Override public void onChanged(ListChangeListener.Change c) {
//                System.out.println("Change");
                updatePanel();                
            }            
        });

    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("FRAME_DESIGN".equals(PROPERTY)) {
            drawFrame();

        } else if ("SIMPLE_GRADIENT_BASE".equals(PROPERTY)) {
            isDirty = true;
        }
    }


    // ******************** Methods *******************************************
    public void paint() {
        if (!initialized) {
            init();
        }
        calcGaugeBounds();
        getSkinnable().setTranslateX(framelessOffset.getX());
        getSkinnable().setTranslateY(framelessOffset.getY());
        getChildren().clear();
        drawFrame();
        drawBackground();
        drawPanel();
        drawForeground();
        
        getChildren().addAll(frame,
            background,
            matrix,
            foreground);
        
        updatePanel();
        
        isDirty = false;
    }

//    @Override public void layoutChildren() {
//        if (isDirty) {
//            paint();
//        }
//        super.layoutChildren();
//    }

    public MatrixPanel getControl() {
        return control;
    }

    @Override public void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double WIDTH) {
        double prefWidth = PREF_SIZE.getWidth();
        if (WIDTH != -1) {
            prefWidth = Math.max(0, WIDTH - getSkinnable().getInsets().getLeft() - getSkinnable().getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double HEIGHT) {
        double prefHeight = PREF_SIZE.getHeight();
        if (HEIGHT != -1) {
            prefHeight = Math.max(0, HEIGHT - getSkinnable().getInsets().getTop() - getSkinnable().getInsets().getBottom());
        }
        return super.computePrefHeight(prefHeight);
    }

    private void calcGaugeBounds() {
        
        /*
         * CONTROL ASPECTRATIO == MATRIX ASPECTRATIO
         */
        if(control.getPrefWidth()==0){
            control.setPrefWidth(800);
        }
        if(control.getPrefHeight()==0){
            control.setPrefHeight(600);
        }
        
        if(control.getLedHeight()>0 && control.getLedWidth()>0){
            double scale=Math.min(control.getPrefWidth()/((double)control.getLedWidth()),
                            control.getPrefHeight()/((double)control.getLedHeight()));
            control.setPrefWidth(scale*control.getLedWidth());
            control.setPrefHeight(scale*control.getLedHeight());
        }

            
//        if (control.isFrameVisible()) {
            
            gaugeBounds.setWidth(control.getPrefWidth());
            gaugeBounds.setHeight(control.getPrefHeight());
            framelessOffset = new Point2D(0, 0);
//        } else {
//            final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
//            gaugeBounds.setWidth(control.getPrefWidth() + SIZE * 0.168224299 + 2);
//            gaugeBounds.setHeight(control.getPrefHeight() + SIZE * 0.168224299 + 2);
//            framelessOffset = new Point2D(-SIZE * 0.0841121495 - 1, -SIZE * 0.0841121495 - 1);
//        }
    }


    // ******************** Drawing *******************************************
    public void drawFrame() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        frame.getChildren().clear();

        final Rectangle SUBTRACT = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
            WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
        SUBTRACT.setArcWidth(0.05 * SIZE);
        SUBTRACT.setArcHeight(0.05 * SIZE);

        final Rectangle OUTER_FRAME = new Rectangle(0.0, 0.0,
            WIDTH, HEIGHT);
        OUTER_FRAME.setArcWidth(0.09333333333333334 * SIZE);
        OUTER_FRAME.setArcHeight(0.09333333333333334 * SIZE);
        OUTER_FRAME.setFill(Color.color(0.5176470588, 0.5176470588, 0.5176470588, 1));
        OUTER_FRAME.setStroke(null);
        frame.getChildren().add(OUTER_FRAME);

        final Rectangle MAIN_FRAME = new Rectangle(1, 1, WIDTH - 2, HEIGHT - 2);
        MAIN_FRAME.setArcWidth(0.08 * SIZE);
        MAIN_FRAME.setArcHeight(0.08 * SIZE);
        MAIN_FRAME.setStroke(null);

        final Rectangle INNER_FRAME = new Rectangle(0.0841121495 * SIZE, 0.0841121495 * SIZE,
            WIDTH - (2 * 0.0841121495 * SIZE), HEIGHT - (2 * 0.0841121495 * SIZE));
        INNER_FRAME.setArcWidth(0.05 * SIZE);
        INNER_FRAME.setArcHeight(0.05 * SIZE);
        INNER_FRAME.setFill(Color.color(0.6, 0.6, 0.6, 0.8));
        INNER_FRAME.setStroke(null);

        final ImageView IMAGE_VIEW;
        switch (control.getFrameDesign()) {
            case BLACK_METAL:
                ConicalGradient bmGradient = new ConicalGradient(new Point2D(MAIN_FRAME.getLayoutBounds().getWidth() / 2,
                                                                             MAIN_FRAME.getLayoutBounds().getHeight() / 2),
                                                                 new Stop(0.0000, Color.rgb(254, 254, 254)),
                                                                 new Stop(0.1250, Color.rgb(0, 0, 0)),
                                                                 new Stop(0.3472, Color.rgb(153, 153, 153)),
                                                                 new Stop(0.5000, Color.rgb(0, 0, 0)),
                                                                 new Stop(0.6805, Color.rgb(153, 153, 153)),
                                                                 new Stop(0.8750, Color.rgb(0, 0, 0)),
                                                                 new Stop(1.0000, Color.rgb(254, 254, 254)));
                MAIN_FRAME.setFill(bmGradient.apply(MAIN_FRAME));
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
            case SHINY_METAL:
                ConicalGradient smGradient = new ConicalGradient(new Point2D(MAIN_FRAME.getLayoutBounds().getWidth() / 2,
                                                                             MAIN_FRAME.getLayoutBounds().getHeight() / 2),
                                                                 new Stop(0.0000, Color.rgb(254, 254, 254)),
                                                                 new Stop(0.1250, Util.darker(control.getFrameBaseColor(), 0.15)),
                                                                 new Stop(0.2500, control.getFrameBaseColor().darker()),
                                                                 new Stop(0.3472, control.getFrameBaseColor().brighter()),
                                                                 new Stop(0.5000, control.getFrameBaseColor().darker().darker()),
                                                                 new Stop(0.6527, control.getFrameBaseColor().brighter()),
                                                                 new Stop(0.7500, control.getFrameBaseColor().darker()),
                                                                 new Stop(0.8750, Util.darker(control.getFrameBaseColor(), 0.15)),
                                                                 new Stop(1.0000, Color.rgb(254, 254, 254)));
                MAIN_FRAME.setFill(smGradient.apply(MAIN_FRAME));
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
            case CHROME:
                ConicalGradient cmGradient = new ConicalGradient(new Point2D(MAIN_FRAME.getLayoutBounds().getWidth() / 2,
                                                                             MAIN_FRAME.getLayoutBounds().getHeight() / 2),
                                                                 new Stop(0.00, Color.WHITE),
                                                                 new Stop(0.09, Color.WHITE),
                                                                 new Stop(0.12, Color.rgb(136, 136, 138)),
                                                                 new Stop(0.16, Color.rgb(164, 185, 190)),
                                                                 new Stop(0.25, Color.rgb(158, 179, 182)),
                                                                 new Stop(0.29, Color.rgb(112, 112, 112)),
                                                                 new Stop(0.33, Color.rgb(221, 227, 227)),
                                                                 new Stop(0.38, Color.rgb(155, 176, 179)),
                                                                 new Stop(0.48, Color.rgb(156, 176, 177)),
                                                                 new Stop(0.52, Color.rgb(254, 255, 255)),
                                                                 new Stop(0.63, Color.WHITE),
                                                                 new Stop(0.68, Color.rgb(156, 180, 180)),
                                                                 new Stop(0.80, Color.rgb(198, 209, 211)),
                                                                 new Stop(0.83, Color.rgb(246, 248, 247)),
                                                                 new Stop(0.87, Color.rgb(204, 216, 216)),
                                                                 new Stop(0.97, Color.rgb(164, 188, 190)),
                                                                 new Stop(1.00, Color.WHITE));
                MAIN_FRAME.setFill(cmGradient.apply(MAIN_FRAME));
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
            case GLOSSY_METAL:
                MAIN_FRAME.setFill(new LinearGradient(0.4714285714285714 * WIDTH, 0.014285714285714285 * HEIGHT,
                    0.47142857142857153 * WIDTH, 0.9785714285714285 * HEIGHT,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 1)),
                    new Stop(0.1, Color.color(0.7843137255, 0.7647058824, 0.7490196078, 1)),
                    new Stop(0.26, Color.WHITE),
                    new Stop(0.73, Color.color(0.1137254902, 0.1137254902, 0.1137254902, 1)),
                    new Stop(1.0, Color.color(0.8196078431, 0.8196078431, 0.8196078431, 1))));
                final Rectangle GLOSSY2 = new Rectangle(0.08571428571428572 * WIDTH, 0.08571428571428572 * HEIGHT,
                    0.8285714285714286 * WIDTH, 0.8285714285714286 * HEIGHT);
                GLOSSY2.setArcWidth(0.05714285714285714 * SIZE);
                GLOSSY2.setArcHeight(0.05714285714285714 * SIZE);
                GLOSSY2.setFill(new LinearGradient(0, GLOSSY2.getLayoutBounds().getMinY(),
                    0, GLOSSY2.getLayoutBounds().getMaxY(),
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 1.0)),
                    new Stop(0.23, Color.color(0.7843137255, 0.7647058824, 0.7490196078, 1.0)),
                    new Stop(0.36, Color.color(1.0, 1.0, 1.0, 1.0)),
                    new Stop(0.59, Color.color(0.1137254902, 0.1137254902, 0.1137254902, 1.0)),
                    new Stop(0.76, Color.color(0.7843137255, 0.7607843137, 0.7529411765, 1.0)),
                    new Stop(1.0, Color.color(0.8196078431, 0.8196078431, 0.8196078431, 1.0))));
                final Rectangle GLOSSY3 = new Rectangle(INNER_FRAME.getX() - 2, INNER_FRAME.getY() - 2, INNER_FRAME.getWidth() + 4, INNER_FRAME.getHeight() + 4);
                GLOSSY3.setArcWidth(INNER_FRAME.getArcWidth() + 1);
                GLOSSY3.setArcHeight(INNER_FRAME.getArcHeight() + 1);
                GLOSSY3.setFill(Color.web("#F6F6F6"));
                final Rectangle GLOSSY4 = new Rectangle(GLOSSY3.getX() + 2, GLOSSY3.getY() + 2, GLOSSY3.getWidth() - 4, GLOSSY3.getHeight() - 4);
                GLOSSY4.setArcWidth(GLOSSY3.getArcWidth() - 1);
                GLOSSY4.setArcHeight(GLOSSY3.getArcHeight() - 1);
                GLOSSY4.setFill(Color.web("#333333"));
                frame.getChildren().addAll(MAIN_FRAME, GLOSSY2, GLOSSY3, GLOSSY4);
                break;
            case DARK_GLOSSY:
                MAIN_FRAME.setFill(new LinearGradient(0.8551401869158879 * WIDTH, 0.14953271028037382 * HEIGHT,
                    0.15794611761513314 * WIDTH, 0.8467267795811287 * HEIGHT,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)),
                    new Stop(0.08, Color.color(0.9960784314, 0.9960784314, 1, 1)),
                    new Stop(0.52, Color.color(0, 0, 0, 1)),
                    new Stop(0.55, Color.color(0.0196078431, 0.0235294118, 0.0196078431, 1)),
                    new Stop(0.84, Color.color(0.9725490196, 0.9803921569, 0.9764705882, 1)),
                    new Stop(0.99, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)),
                    new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1))));
                final Rectangle DARK_GLOSSY2 = new Rectangle(0.08571428571428572 * WIDTH, 0.08571428571428572 * HEIGHT,
                    0.8285714285714286 * WIDTH, 0.8285714285714286 * HEIGHT);
                DARK_GLOSSY2.setArcWidth(0.05714285714285714 * SIZE);
                DARK_GLOSSY2.setArcHeight(0.05714285714285714 * SIZE);
                DARK_GLOSSY2.setFill(new LinearGradient(0.5 * WIDTH, 0.014018691588785047 * HEIGHT,
                    0.5 * WIDTH, 0.985981308411215 * HEIGHT,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.color(0.2588235294, 0.2588235294, 0.2588235294, 1)),
                    new Stop(0.42, Color.color(0.2588235294, 0.2588235294, 0.2588235294, 1)),
                    new Stop(1.0, Color.color(0.0509803922, 0.0509803922, 0.0509803922, 1))));
                DARK_GLOSSY2.setStroke(null);

                final Rectangle DARK_GLOSSY3 = new Rectangle(MAIN_FRAME.getX(), MAIN_FRAME.getY(),
                    MAIN_FRAME.getWidth(), MAIN_FRAME.getHeight() * 0.5);
                DARK_GLOSSY3.setArcWidth(MAIN_FRAME.getArcWidth());
                DARK_GLOSSY3.setArcHeight(MAIN_FRAME.getArcHeight());
                DARK_GLOSSY3.setFill(new LinearGradient(0.5 * WIDTH, 0.014018691588785047 * HEIGHT,
                    0.5 * WIDTH, 0.5280373831775701 * HEIGHT,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.color(1, 1, 1, 1)),
                    new Stop(0.26, Color.color(1, 1, 1, 1)),
                    new Stop(0.26009998, Color.color(1, 1, 1, 1)),
                    new Stop(1.0, Color.color(1, 1, 1, 0))));
                DARK_GLOSSY3.setStroke(null);

                final Rectangle DARK_GLOSSY4 = new Rectangle(INNER_FRAME.getX() - 2, INNER_FRAME.getY() - 2,
                    INNER_FRAME.getWidth() + 4, INNER_FRAME.getHeight() + 4);
                DARK_GLOSSY4.setArcWidth(INNER_FRAME.getArcWidth() + 1);
                DARK_GLOSSY4.setArcHeight(INNER_FRAME.getArcHeight() + 1);
                DARK_GLOSSY4.setFill(new LinearGradient(0.8037383177570093 * WIDTH, 0.1822429906542056 * HEIGHT,
                    0.18584594354259637 * WIDTH, 0.8001353648686187 * HEIGHT,
                    false, CycleMethod.NO_CYCLE,
                    new Stop(0.0, Color.color(0.6745098039, 0.6745098039, 0.6784313725, 1)),
                    new Stop(0.08, Color.color(0.9960784314, 0.9960784314, 1, 1)),
                    new Stop(0.52, Color.color(0, 0, 0, 1)),
                    new Stop(0.55, Color.color(0.0196078431, 0.0235294118, 0.0196078431, 1)),
                    new Stop(0.91, Color.color(0.9725490196, 0.9803921569, 0.9764705882, 1)),
                    new Stop(0.99, Color.color(0.6980392157, 0.6980392157, 0.6980392157, 1)),
                    new Stop(1.0, Color.color(0.6980392157, 0.6980392157, 0.6980392157, 1))));
                DARK_GLOSSY4.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, DARK_GLOSSY2, DARK_GLOSSY3, DARK_GLOSSY4);
                break;
            default:
                IMAGE_VIEW = new ImageView();
                IMAGE_VIEW.setVisible(false);
                MAIN_FRAME.getStyleClass().add(control.getFrameDesign().CSS);
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
        }
        frame.setCache(true);
    }

    public void drawBackground() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        background.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        background.getChildren().add(IBOUNDS);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.2 * SIZE);
        INNER_SHADOW.setHeight(0.2 * SIZE);
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 1.0));
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final LinearGradient HL_GRADIENT = new LinearGradient(0, 0, WIDTH, 0, false, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.color(0.0, 0.0, 0.0, 0.6)),
            new Stop(0.4, Color.color(0.0, 0.0, 0.0, 0.0)),
            new Stop(0.6, Color.color(0.0, 0.0, 0.0, 0.0)),
            new Stop(1.0, Color.color(0.0, 0.0, 0.0, 0.6)));

        final Rectangle BACKGROUND = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
            WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
        BACKGROUND.setArcWidth(0.05 * SIZE);
        BACKGROUND.setArcHeight(0.05 * SIZE);
        BACKGROUND.setStroke(null);

        final Rectangle CLIP_SHAPE = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
            WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
        CLIP_SHAPE.setArcWidth(0.05 * SIZE);
        CLIP_SHAPE.setArcHeight(0.05 * SIZE);

        
//        BACKGROUND.setStyle(control.getSimpleGradientBaseColorString());
//        BACKGROUND.getStyleClass().add(control.getBackgroundDesign().CSS_BACKGROUND);
        BACKGROUND.setEffect(INNER_SHADOW);
        background.getChildren().addAll(BACKGROUND);


        
        background.setCache(true);
    }

    public void drawForeground() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        foreground.getChildren().clear();

        final Insets INSETS = new Insets(0.0841121495 * SIZE + 2, WIDTH - 0.0841121495 * SIZE - 2, HEIGHT - 0.0841121495 * SIZE - 2, 0.0841121495 * SIZE + 2);
        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        foreground.getChildren().addAll(IBOUNDS);

        final Path FOREGROUND = new Path();
        final Point2D START;
        final Point2D STOP;
        // Highlight
        if (WIDTH >= HEIGHT) {
            // Horizontal glass effect
            FOREGROUND.setFillRule(FillRule.EVEN_ODD);
            FOREGROUND.getElements().add(new MoveTo(INSETS.getLeft(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new LineTo(INSETS.getRight(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getRight(), INSETS.getBottom(),
                INSETS.getRight() - 13, 0.7 * HEIGHT,
                INSETS.getRight() - 13, 0.5 * HEIGHT));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getRight() - 13, 0.3 * HEIGHT,
                INSETS.getRight(), INSETS.getTop(),
                INSETS.getRight(), INSETS.getTop()));
            FOREGROUND.getElements().add(new LineTo(INSETS.getLeft(), INSETS.getTop()));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getLeft(), INSETS.getTop(),
                INSETS.getLeft() + 13, 0.3 * HEIGHT,
                INSETS.getLeft() + 13, 0.5 * HEIGHT));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getLeft() + 13, 0.7 * HEIGHT,
                INSETS.getLeft(), INSETS.getBottom(),
                INSETS.getLeft(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new ClosePath());
            START = new Point2D(0, FOREGROUND.getLayoutBounds().getMaxY());
            STOP = new Point2D(0, FOREGROUND.getLayoutBounds().getMinY());
        } else {
            // Vertical glass effect
            FOREGROUND.setFillRule(FillRule.EVEN_ODD);
            FOREGROUND.getElements().add(new MoveTo(INSETS.getLeft(), INSETS.getTop()));
            FOREGROUND.getElements().add(new LineTo(INSETS.getLeft(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getLeft(), INSETS.getBottom(),
                0.3 * WIDTH, INSETS.getBottom() - 13,
                0.5 * WIDTH, INSETS.getBottom() - 13));
            FOREGROUND.getElements().add(new CubicCurveTo(0.7 * WIDTH, INSETS.getBottom() - 13,
                INSETS.getRight(), INSETS.getBottom(),
                INSETS.getRight(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new LineTo(INSETS.getRight(), INSETS.getTop()));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getRight(), INSETS.getTop(),
                0.7 * WIDTH, INSETS.getTop() + 13,
                0.5 * WIDTH, INSETS.getTop() + 13));
            FOREGROUND.getElements().add(new CubicCurveTo(0.3 * WIDTH, INSETS.getTop() + 13,
                INSETS.getLeft(), INSETS.getTop(),
                INSETS.getLeft(), INSETS.getTop()));
            FOREGROUND.getElements().add(new ClosePath());
            START = new Point2D(FOREGROUND.getLayoutBounds().getMinX(), 0);
            STOP = new Point2D(FOREGROUND.getLayoutBounds().getMaxX(), 0);
        }

        final LinearGradient GRADIENT = new LinearGradient(START.getX(), START.getY(),
            STOP.getX(), STOP.getY(),
            false, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.color(1, 1, 1, 0)),
            new Stop(0.06, Color.color(1, 1, 1, 0)),
            new Stop(0.07, Color.color(1, 1, 1, 0)),
            new Stop(0.12, Color.color(1, 1, 1, 0.05)),
            new Stop(0.17, Color.color(1, 1, 1, 0.0)),
            new Stop(0.18, Color.color(1, 1, 1, 0)),
            new Stop(0.23, Color.color(1, 1, 1, 0.02)),
            new Stop(0.30, Color.color(1, 1, 1, 0.0)),
            new Stop(0.8, Color.color(1, 1, 1, 0)),
            new Stop(0.84, Color.color(1, 1, 1, 0.08)),
            new Stop(0.93, Color.color(1, 1, 1, 0.18)),
            new Stop(0.94, Color.color(1, 1, 1, 0.20)),
            new Stop(0.96, Color.color(1, 1, 1, 0.10)),
            new Stop(0.97, Color.color(1, 1, 1, 0)),
            new Stop(1.0, Color.color(1, 1, 1, 0)));
        FOREGROUND.setFill(GRADIENT);
        FOREGROUND.setStroke(null);

        foreground.getChildren().addAll(FOREGROUND);
        foreground.setCache(true);
    }

    private double radio=0d;
    
    public void drawPanel() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth() - 2 * (0.0841121495 * SIZE + 5);
        final double HEIGHT = gaugeBounds.getHeight() - 2 * (0.0841121495 * SIZE + 5);

        matrix.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0.0841121495 * SIZE + 5, 0.0841121495 * SIZE + 5, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        matrix.getChildren().add(IBOUNDS);

        radio = WIDTH / (3d * ledWidth.doubleValue() + 1);
        double gapW = radio;
        double gapH = (HEIGHT - 2d * radio * ledHeight.doubleValue()) / (ledHeight.doubleValue() + 1);
        dots.getChildren().clear();

        dotMap = new HashMap<Integer, Shape>(ledWidth.intValue() * ledHeight.intValue());

        for (int i = 0; i < ledHeight.intValue(); i++) {
            for (int j = 0; j < ledWidth.intValue(); j++) {
                Circle circ = new Circle(0.0841121495 * SIZE + 5 + gapW + radio + j * (gapW + 2d * radio), 
                                         0.0841121495 * SIZE + 5 + gapH + radio + i * (gapH + 2d * radio), 
                                         radio, Color.DARKGREY);
                circ.setFill(COLOR_OFF);
                dotMap.put(new Integer(j + i * ledWidth.intValue()), circ);

                dots.getChildren().add(circ);
            }
        }
        dots.setCache(true);
        matrix.getChildren().add(dots);
    }
    
    private final int LED_COLUMN    = 0;
    private final int LED_ROW       = 1;
    private final int LED_INTENSITY = 2;
    /*
     * full area required for each content, even not visible
     */
    private ArrayList<int[][]> fullAreas = null;
    /*
     * visible AREAS in the panel, one per content
     */
    private Rectangle[] visibleArea = null;
    /* 
     * PAIRS of contents in the same area
     */
    private ArrayList<ContentPair> pairs=null;
    /*
     * ANIMATION of each content
     */
    private ArrayList<Animation> Anim=null;
    
       
    public void updatePanel() {
//        System.out.println("updatePanel "+contents.size());
        if (contents == null) {
            return;
        }
        // stop previous animations, if any
        stop();
        
        // run as thread
        Platform.runLater(new Runnable(){

            @Override
            public void run() {
                /*
                 * status of the full matrix of visible leds
                 * 0: off
                 * 1: tone low  ( 85)
                 * 2: tone mid  (170)
                 * 3: tone high (255)
                 */

                /*
                 * FIRST: GET IMAGE RAW DATA AND FILL THE LEDS IN ITS AREA
                 */
                int contAreas = 0;
                fullAreas = new ArrayList<int[][]>();
                pairs=new ArrayList<ContentPair>();
                visibleArea = new Rectangle[contents.size()];
                for (final Content content : contents) {
                    int x0 = (int) content.getOrigin().getX() + (int) content.getArea().getX();
                    int y0 = (int) content.getOrigin().getY() + (int) content.getArea().getY();
                    int maxX = Math.min((int) content.getArea().getWidth(), ledWidth.intValue());
                    int maxY = Math.min((int) content.getArea().getHeight(), ledHeight.intValue());
                    visibleArea[contAreas] = new Rectangle(Math.max(x0, 0), Math.max(y0, 0), maxX, maxY);

                    if (content.getType().equals(Content.Type.IMAGE)) {
                        UtilHex img = new UtilHex();
                        img.convertsBmp(content.getBmpName(), 65, 190, true,true,true);

                        String sBytes = img.getRawData();
                        if (sBytes != null) {
                            String[] v = sBytes.split("\\s");
                            final int levels = 3;
                            //final int bmpWidth = UtilHex.word2Int(v[6], v[7]);
                            final int bmpHeight = UtilHex.word2Int(v[8], v[9]);
                            final int tamLineaBMT = (int)(UtilHex.dword2Long(v[20],v[21], v[22], v[23]) / bmpHeight / levels / 3); // en bytes
                            int pos = 32;
                            final int[][] area = new int[bmpHeight][tamLineaBMT * 8];
                            final int[] colors={(content.getColor().equals(MatrixColor.RED) || content.getColor().equals(MatrixColor.YELLOW) || content.getColor().equals(MatrixColor.RGB))?1:0,
                                                (content.getColor().equals(MatrixColor.GREEN) || content.getColor().equals(MatrixColor.YELLOW) || content.getColor().equals(MatrixColor.RGB))?1:0,
                                                (content.getColor().equals(MatrixColor.BLUE) || content.getColor().equals(MatrixColor.RGB))?1:0};
                            for (int j = 0; j < levels; j++) { // leds: [RED k=0]0-1-2-3, [GREEN k=1]0-10-20-30, [BLUE k=2] 0-100-200-300
                                for(int k=0; k<3; k++){ // 3 colors
                                    for (int fila = 0; fila < bmpHeight; fila++) {
                                        for (int i = 0; i < tamLineaBMT; i++) { // recorrido por cada byte de cada fila
                                            String bits = UtilHex.hex2bin(v[pos++]); // contiene la fila de 8 bits
                                            for (int m = 0; m < 8; m++) {
                                                area[fila][i * 8 + m] += (bits.substring(m, m + 1).equalsIgnoreCase("1") ? 1 : 0)*Math.pow(10,k)*colors[k];
                                            }
                                        }
                                    }
                                }                        
                            }
                            fullAreas.add(contAreas,area);
                        }
                        else{
                            fullAreas.add(contAreas,null);
                        }
                    } else if (content.getType().equals(Content.Type.TEXT)) {
                        MatrixPanel.DotFont dotF = new MatrixPanel.DotFont(content.getTxtContent(), content.getMatrixFont(), content.getFontGap().getGapWidth());
                        boolean[][] bDots = dotF.getDotString();
                        if (bDots != null) {
                            final int color=(content.getColor().equals(MatrixColor.RED)?3:
                                            (content.getColor().equals(MatrixColor.GREEN)?30:
                                            (content.getColor().equals(MatrixColor.BLUE)?300:
                                            (content.getColor().equals(MatrixColor.YELLOW)?33:333))));
                            final int[][] area = new int[bDots.length][bDots[0].length];
                            for (int fila = 0; fila < bDots.length; fila++) {
                                for (int j = 0; j < bDots[fila].length; j++) {
                                    area[fila][j] = ((bDots[fila][j]) ? color : 0);
                                }
                            }
                            fullAreas.add(contAreas,area);
                        }
                        else{
                            fullAreas.add(contAreas,null);
                        }
                    }
                    contAreas += 1;

                }
                /*
                 * SECOND: CHECK FOR CONTENT PAIRS
                 */
                for (final Content content1 : contents) {            
                    if(content1.getOrder().equals(Content.RotationOrder.FIRST)){
                        final int iContent1=contents.indexOf(content1);
                        for (final Content content2 : contents) {            
                            final int iContent2=contents.indexOf(content2);
                            if(content2.getOrder().equals(Content.RotationOrder.SECOND) &&
                                    content1.getArea().getBoundsInLocal().equals(content2.getArea().getBoundsInLocal())){
                                ContentPair pair=new ContentPair(iContent1,iContent2);
                                pairs.add(pair);
                                break;
                            }
                        }
                    }
                }

        //        // Create the dark inner shadow on the bottom
        //        InnerShadow innerShadow = InnerShadowBuilder.create()
        //                                                    .offsetY(1)
        //                                                    .radius(1)
        //                                                    .color(Color.color(0, 0, 0, 0.65))
        //                                                    .blurType(BlurType.GAUSSIAN)
        //                                                    .build();
        //
        //        // Create the bright inner glow on the top
        //        InnerShadow innerGlow = InnerShadowBuilder.create()
        //                                                .offsetY(1)
        //                                                .radius(1)
        //                                                .color(Color.color(1, 1, 1, 0.65))
        //                                                .blurType(BlurType.GAUSSIAN)
        //                                                .input(innerShadow)
        //                                                .build();
        //
        //        // Create the drop shadow on the outside
        //        final DropShadow dropShadow = DropShadowBuilder.create()
        //                                                .radius(1)
        //                                                .color(Color.color(0, 0, 0, 0.65))
        //                                                .blurType(BlurType.GAUSSIAN)
        //                                                .input(innerGlow)
        //                                                .build();


                /*
                 * THIRD: DISPLAY WITH/OUT ANIMATION
                 */

                // bind content display to paired content
                visibleContent=new SimpleBooleanProperty[contents.size()];
                // clear screen
                for(Shape entry:dotMap.values()){
                    ((Circle)entry).setFill(COLOR_OFF);
                }

                Anim=new ArrayList<Animation>();

                for (final Content content : contents) {

                    final int iContent=contents.indexOf(content);
                    /*
                     * Create ANIMATION for each content, if content is not null
                     */
                    if(fullAreas.get(iContent)!=null){
                        Animation iAnim=new Animation(iContent, content);
                        iAnim.initAnimation();
                        Anim.add(iAnim);
                    }
                }        

                /*
                 * START ANIMATIONS AT THE SAME TIME
                 */
                for (final Animation a : Anim) {
                    a.start();
                }
            }            
        });
    }
    
    public void stop(){
        if(Anim!=null){
            for (final Animation a : Anim) {
                a.stop();
            }
            Anim.clear();
            Anim=null;
        }
    }

    private class Animation extends AnimationTimer{
        private long lastUpdate=0l;
        private boolean bBlink=false; // heartbit
        private int contBlink=0;
        private int iter=0;        
        private int iContent;
        private Content content=null;
        private int oriX, oriY, endX, endY;
        private int areaWidth, areaHeight;
        private int contentWidth, contentHeight;
        private IntegerProperty posX, posY, posXIni, posYIni;
        private int[][] contentArea=null;
        private int realLapse, advance, limX, limitBlink, iterLeds;
        private boolean isBlinkEffect;
        
        private LinkedHashMap<Integer,int[]> brightLeds=null;
        private ArrayList<int[]> arrBrightLeds=null;
        private IntegerProperty incrPos=null;
        
        public Animation(int iContent, Content theContent){
            
            this.iContent=iContent;
            this.content=theContent;
            
            // bind posX/posY increment (1) to allow for pause time (0) for each content
            incrPos=new SimpleIntegerProperty(1);
            
            visibleContent[iContent]=new SimpleBooleanProperty(true); // SINGLE && FIRST
            if(content.getOrder().equals(Content.RotationOrder.SECOND)){
                visibleContent[iContent].setValue(false);
            }
            
        }
        
        public void initAnimation(){
            this.contentArea = fullAreas.get(iContent);            

            oriX = (int) visibleArea[iContent].getX();
            oriY = (int) visibleArea[iContent].getY();
            endX = (int) visibleArea[iContent].getWidth();
            endY = (int) visibleArea[iContent].getHeight();
            areaWidth = endX-oriX;
            areaHeight = endY-oriY;

            /*
            * Total dimensions of area of the content
            */
            contentWidth = contentArea[0].length;
            contentHeight = contentArea.length;
            
            /*
            * START LOCATION OF CONTENT
            */
            posXIni= new SimpleIntegerProperty(0);
            posYIni = new SimpleIntegerProperty(0);

            // content at its final position
            posYIni.set(0);
            if(content.getTxtAlign().equals(Content.Align.LEFT)){
                posXIni.set(0);
                // SCROLL_RIGHT: +cW-cW, SCROLL_LEFT: -aW+aW=0, MIRROR: -cW/2+cW/2
                limX=0; 
            } else if(content.getTxtAlign().equals(Content.Align.CENTER)){
                posXIni.set(contentWidth/2-areaWidth/2);
                //SCROLL_RIGHT: +cW-(aW/2+cW/2) SCROLL_LEFT: -aW+(aW/2+fW/2), MIRROR: -aW/2+cW/2
                limX=-areaWidth/2+contentWidth/2; 
            } else if(content.getTxtAlign().equals(Content.Align.RIGHT)){
                posXIni.set(contentWidth-areaWidth);
                //SCROLL_RIGHT: +cW-aW, SCROLL_LEFT: -aW+cW=0, MIRROR: cW/2-aW + cW/2
                limX=contentWidth-areaWidth; 
            }
            
            // moved first if neccessary to start the scrolling effect
            if (content.getEffect().equals(Content.Effect.SCROLL_RIGHT)){
                // content to the left of the visible area
                posXIni.set(contentWidth);
            } else if (content.getEffect().equals(Content.Effect.SCROLL_LEFT)){
                // content to the right of the visible area
                posXIni.set(-areaWidth);
            } else if (content.getEffect().equals(Content.Effect.SCROLL_UP)){
                // content to the bottom of the visible area
                posYIni.set(-areaHeight);
            } else if (content.getEffect().equals(Content.Effect.SCROLL_DOWN)){
                // content to the top of the visible area
                posYIni.set(contentHeight);
            } else if (content.getEffect().equals(Content.Effect.MIRROR)){
                // content to the center of the visible area
                if(content.getTxtAlign().equals(Content.Align.LEFT)){
                    posXIni.set(-contentWidth/2);
                } else if(content.getTxtAlign().equals(Content.Align.CENTER)){
                    posXIni.set(0-areaWidth/2);
                } else if(content.getTxtAlign().equals(Content.Align.RIGHT)){
                    posXIni.set(contentWidth/2-areaWidth);
                }
            } 
            
            // +1,-1 to make the translation ot the content, 0 to pause it
            posX = new SimpleIntegerProperty(posXIni.get());
            posY = new SimpleIntegerProperty(posYIni.get());
            
            // speed = gap of ms to refresh the matrixPanel
            realLapse = (content.getLapse() >= 250)?content.getLapse():250;
            
            if(content.getLapse()>0){
                advance=realLapse/content.getLapse(); // horizontal advance per step (int). 
                realLapse=advance*content.getLapse();
            }
            else{
                advance=10;
            }
            
            isBlinkEffect=(content.getEffect().equals(Content.Effect.BLINK) || 
                            content.getEffect().equals(Content.Effect.BLINK_4) ||
                            content.getEffect().equals(Content.Effect.BLINK_10));
            limitBlink=(content.getEffect().equals(Content.Effect.BLINK)?10000: 
                        (content.getEffect().equals(Content.Effect.BLINK_4)?7:
                         (content.getEffect().equals(Content.Effect.BLINK_10)?19:0)));

            /*
             * Effect.SPRAY
             */
            if(content.getEffect().equals(Content.Effect.SPRAY)){
                brightLeds = new LinkedHashMap<Integer,int[]>();
                arrBrightLeds=new ArrayList<int[]>();
                
                // list of brighting LEDs: column j, row i, intensity val
                for (int i = oriY; i < endY; i++) {
                    for (int j = oriX; j < endX; j++) {
                        Integer dot = new Integer(j + i * ledWidth.intValue());
                        if (dotMap.get(dot) != null) {
                            int val = 0;
                            if (j + posX.intValue() >= oriX && j + posX.intValue() < contentWidth + oriX &&
                                i + posY.intValue() >= oriY && i + posY.intValue() < contentHeight + oriY) {
                                val = contentArea[i + posY.intValue() - oriY][j + posX.intValue() - oriX];
                                if(val>0){
                                    int[] led={j,i,val};
                                    arrBrightLeds.add(led);
                                }
                            } 
                        }
                    }
                }
                
                // RANDOMIZE ArrayList 
                Collections.shuffle(arrBrightLeds);
                
                // Create map with shuffled list
                final Iterator<int[]> vIter = arrBrightLeds.iterator();
                for (int k=0; k<arrBrightLeds.size(); k++){
                    brightLeds.put(k, vIter.next());
                }
                arrBrightLeds.clear();

                /*
                 * SPRAY Effect. Number of new leds showed in each step
                 */
                iterLeds=brightLeds.size()/advance;
            }
        }
                
        @Override
        public void handle(long now) {
            /*
            *  only make one frame step animation IF enough fps, 
            *  the content is visible and it isn't in pause
            */
            if (now > lastUpdate + realLapse*1000000 && 
                visibleContent[iContent].getValue() && 
                incrPos.intValue()==1) {  

                /*
                *  check only the visible area
                */
                if(content.getEffect().equals(Content.Effect.SPRAY)){
                    // show bunch of leds, starting from the end of the shrinking map
                    for(int buc=0;buc<iterLeds;buc++){
                        int[] led=(int[])brightLeds.get(brightLeds.size()-iter-1);
                        final int toneB=(int)(led[LED_INTENSITY]/100);
                        final int toneG=(int)((led[LED_INTENSITY]-toneB*100)/10);
                        final int toneR=(int)(led[LED_INTENSITY]-toneB*100-toneG*10);
                        Integer dot = new Integer(led[LED_COLUMN] + led[LED_ROW] * ledWidth.intValue());
                        ((Circle)dotMap.get(dot)).setFill(Color.rgb(toneScale*toneR, toneScale*toneG, toneScale*toneB));
                        iter=(iter<brightLeds.size()-1)?iter+1:iter;
                    }                            
                } else {
                    for (int j = oriX; j < endX; j++) {
                        for (int i = oriY; i < endY; i++) {
                            Integer dot = new Integer(j + i * ledWidth.intValue());
                            if (dotMap.get(dot) != null) {
                                int pos=posX.intValue();
                                if(content.getEffect().equals(Content.Effect.MIRROR)){
                                    if(content.getTxtAlign().equals(Content.Align.LEFT) && j-oriX>contentWidth/2){
                                        pos=-pos;
                                    } else if(content.getTxtAlign().equals(Content.Align.CENTER) && j-oriX>areaWidth/2d){
                                        pos=-pos-areaWidth+contentWidth;                                                
                                    } else if(content.getTxtAlign().equals(Content.Align.RIGHT) && j-oriX>-contentWidth/2+areaWidth){
                                        pos=-pos+2*(contentWidth-areaWidth);
                                    }                                            
                                }

                                int val = 0;
                                if (j + pos >= oriX && j + pos < contentWidth + oriX &&
                                    i + posY.intValue() >= oriY && i + posY.intValue() < contentHeight + oriY) {
                                    val = contentArea[i + posY.intValue() - oriY][j + pos - oriX];
                                } 
                                if ((val > 0 && !isBlinkEffect) || (val>0 && isBlinkEffect && bBlink)) {
                                    final int toneB=val/100;
                                    final int toneG=(val-toneB*100)/10;
                                    final int toneR=(val-toneB*100-toneG*10);
                                    ((Circle)dotMap.get(dot)).setFill(Color.rgb(toneScale*toneR, toneScale*toneG, toneScale*toneB));
                                } else { 
                                    ((Circle)dotMap.get(dot)).setFill(COLOR_OFF);
                                }
                            }
                        }
                    }
                }
                /*
                 * INCREMENT TRASLATION OF CONTENT 
                 * CHECK END OF MOVEMENT
                 */
                boolean endRotation=false;

                if (content.getEffect().equals(Content.Effect.NONE)) {
                    endRotation=true;
                } else if (content.getEffect().equals(Content.Effect.SCROLL_RIGHT)) {
                    endRotation=(posX.intValue() <= limX); 
                    if(posX.intValue() - advance*incrPos.getValue() <= limX){ 
                        posX.set(limX);
                    }
                    else{
                        posX.set(posX.intValue() - advance*incrPos.getValue());
                    } 
                } else if (content.getEffect().equals(Content.Effect.SCROLL_LEFT) || 
                           content.getEffect().equals(Content.Effect.MIRROR)) {
                    endRotation=(posX.intValue() >= limX); 
                    if(posX.intValue() + advance*incrPos.getValue() >= limX){ 
                        posX.set(limX);
                    }
                    else{
                        posX.set(posX.intValue() + advance*incrPos.getValue());
                    }                    
                } else if (content.getEffect().equals(Content.Effect.SCROLL_DOWN)) {
                    posY.set(posY.intValue() - incrPos.getValue());
                    endRotation = (posY.intValue() < 0); // fullHeight-fullHeight
                } else if (content.getEffect().equals(Content.Effect.SCROLL_UP)) {
                    posY.set(posY.intValue() + incrPos.getValue());
                    endRotation = (posY.intValue() > 0); // -areaHeight+areaHeight
                } else if (isBlinkEffect){
                    if(contBlink==limitBlink){
                        endRotation=true;
                        contBlink=-1;
                    } else if(incrPos.getValue()==1){ // not in pause time
                        endRotation=false;
                        contBlink+=1;
                        bBlink=!bBlink;                                
                    }
                } else if (content.getEffect().equals(Content.Effect.SPRAY)) {             
                    if(iter>=brightLeds.size()-1){
                        endRotation=true;
                        iter=0;
                    }
                    else{
                        endRotation=false;
                    }
                }

                /*
                * POST EFFECT
                */
                if(endRotation) {

                    if(content.getPostEffect().equals(Content.PostEffect.STOP)) { 
//                        System.out.println("stop content "+iContent);
                        this.stop();
                    } else if(content.getPostEffect().equals(Content.PostEffect.REPEAT) || 
                              content.getPostEffect().equals(Content.PostEffect.PAUSE)){
                        posX.set(posXIni.get());
                        posY.set(posYIni.get());                               

                        incrPos.setValue(0);                               

                        /*
                        * PAUSE BETWEEN ROTATIONS
                        */
                        PauseTransition t=new PauseTransition();
                        if(content.getPostEffect().equals(Content.PostEffect.REPEAT)){
                            t.setDuration(Duration.millis(10));
//                            System.out.println("repeat content "+iContent);  
                        } else{
                            t.setDuration(Duration.millis(content.getPause()));
//                            System.out.println("Start pause content "+iContent);
                        }
                        t.setOnFinished(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
//                                System.out.println("End pause content "+iContent);
                                incrPos.setValue(1);

                                // clear screen
                                if(content.getClear() || content.getEffect().equals(Content.Effect.SPRAY)){
                                    for (int i = oriY; i < endY; i++) {
                                        for (int j = oriX; j < endX; j++) {
                                            Integer dot = new Integer(j + i * ledWidth.intValue());
                                            ((Circle)dotMap.get(dot)).setFill(COLOR_OFF);
                                        }
                                    }
                                }

                                if(!content.getOrder().equals(Content.RotationOrder.SINGLE)){
                                    // at the end of the content display, allow paired content to be displayed
                                    for(ContentPair pair: pairs){
                                        if(pair.isInPair(iContent)){                                                    
                                            visibleContent[pair.getFirstIndex()].setValue(!pair.isVisibleFirst());
                                            visibleContent[pair.getSecondIndex()].setValue(!pair.isVisibleSecond());                                            
                                            pairs.get(pairs.indexOf(pair)).changeIndex();
                                            break;
                                        }
                                    }                                
                                }
                            }
                        });                                        
                        t.playFromStart();                                
                    }


                }
                //System.out.println((now-lastUpdate)/1000000);
                lastUpdate = now;
            }
        }
        
        
    }
    
    private static class ContentPair {

        private int indexFirst;
        private int indexSecond;
        private boolean bVisibleFirst;
        
        public ContentPair(int index1, int index2) {
            indexFirst=index1;
            bVisibleFirst=true;
            indexSecond=index2;            
        }
        
        public void setFirstIndex(int index){
            indexFirst=index;
            bVisibleFirst=true;
        }
        public void setSecondIndex(int index){
            indexSecond=index;
        }
        
        public void changeIndex(){
            bVisibleFirst=!bVisibleFirst;
        }
        public int getFirstIndex() {
            return indexFirst;
        }
        public int getSecondIndex() {
            return indexSecond;
        }

        public boolean isVisibleFirst(){
            return bVisibleFirst;
        }
        
        public boolean isVisibleSecond(){
            return !bVisibleFirst;
        }
        private boolean isInPair(int iContent) {
            return (indexFirst==iContent || indexSecond==iContent);
        }
        
    }
}

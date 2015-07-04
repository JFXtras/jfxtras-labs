/**
 * MatrixPanelSkin.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.labs.internal.scene.control.skin;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import jfxtras.labs.scene.control.gauge.Content;
import jfxtras.labs.scene.control.gauge.Content.MatrixColor;
import jfxtras.labs.scene.control.gauge.Content.RotationOrder;
import jfxtras.labs.scene.control.gauge.MatrixPanel;
import jfxtras.labs.scene.control.gauge.UtilHex;
import jfxtras.labs.util.ConicalGradient;
import jfxtras.labs.util.Util;


/**
 * Created by
 * User: hansolo
 * Date: 09.01.12
 * Time: 18:04
 * Modified by Jose Pereda Llamas &lt;jperedadnr&gt;
 * On : 23-jun-2012, 11:47:23
 */
public class MatrixPanelSkin extends SkinBase<MatrixPanel> implements Skin<MatrixPanel> {
    private static final double      PREFERRED_WIDTH  = 170;
    private static final double      PREFERRED_HEIGHT = 350;
    private static final double      MINIMUM_WIDTH    = 17;
    private static final double      MINIMUM_HEIGHT   = 35;
    private static final double      MAXIMUM_WIDTH    = 1700;
    private static final double      MAXIMUM_HEIGHT   = 3500;
    private static double            aspectRatio      = PREFERRED_HEIGHT / PREFERRED_WIDTH;
    private double                   width;
    private double                   height;
    private Pane                     pane;
    private Region                   main;
    private Region                   mainFrameOut;
    private Region                   mainFrame;
    private Region                   mainFrameIn;
    private Region                   mainForeground;
    private Pane                     dots;
    private Map<Integer, Shape>      dotMap;
    private int                      iDots;
    private double                   radio=0d;
    private final int                toneScale=85;
    private BooleanProperty[]        visibleContent=null;
    private final Color              COLOR_OFF = Color.rgb(39, 39, 39,0.25);
    private String                   jpgFrame;
    private Background               fillFrame;
    // ******************** Constructors **************************************
    public MatrixPanelSkin(final MatrixPanel CONTROL) {
        super(CONTROL);
        
        init();
        initGraphics();
        registerListeners();
    }

    // ******************** Initialization ************************************
    private void init() {
        if (Double.compare(getSkinnable().getPrefWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getPrefHeight(), 0.0) <= 0 ||
            Double.compare(getSkinnable().getWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getHeight(), 0.0) <= 0) {
            setSize();
        }

        if (Double.compare(getSkinnable().getMinWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getMinHeight(), 0.0) <= 0) {
            getSkinnable().setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(getSkinnable().getMaxWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getMaxHeight(), 0.0) <= 0) {
            getSkinnable().setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }

        if (getSkinnable().getPrefWidth() != PREFERRED_WIDTH || getSkinnable().getPrefHeight() != PREFERRED_HEIGHT) {
            aspectRatio = getSkinnable().getPrefHeight() / getSkinnable().getPrefWidth();
        }
    }

    private void initGraphics() {
        main = new Region();
        main.getStyleClass().setAll("main");
        main.setOpacity(1);
        
        mainFrameOut = new Region();
        
        mainFrame = new Region();
        mainFrameIn = new Region();
        setStyle();
        
        setDots();
        
        mainForeground = new Region();
        mainForeground.getStyleClass().setAll("frontFrame");
        mainForeground.setOpacity(1);
        
        pane = new Pane();
        pane.getChildren().setAll(main,mainFrameOut,mainFrame,mainFrameIn,dots,mainForeground);
        pane.setCache(true);

        pane.getChildren().stream().filter((n) -> n instanceof Pane).
                forEach((n) -> iDots=pane.getChildren().indexOf(n) );
        getChildren().setAll(pane);

        gradient();
        createContents();
        
    }
    
    private void setSize(){
        if (getSkinnable().getPrefWidth() > 0 && getSkinnable().getPrefHeight() > 0) {
            if(getSkinnable().getLedWidth()>0 && getSkinnable().getLedHeight()>0){
                double scale=Math.min(getSkinnable().getPrefWidth()/getSkinnable().getLedWidth(),
                                      getSkinnable().getPrefHeight()/getSkinnable().getLedHeight());
                /*
                ASPECT RATIO: DEPENDS ON LEDS ARRAY ASPECT RATIO
                */
                getSkinnable().setPrefSize(getSkinnable().getLedWidth()*scale,getSkinnable().getLedHeight()*scale);
            }
            else{
                getSkinnable().setPrefSize(getSkinnable().getPrefWidth(), getSkinnable().getPrefHeight());
            }
        } else {
            getSkinnable().setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        }
    }
    
    private void setStyle(){
        if(!getSkinnable().isFrameVisible()){
            mainFrameOut.getStyleClass().removeAll("mainFrameOut");
            mainFrame.getStyleClass().removeAll("glossy-metal","dark-glossy","gradient");
            mainFrameIn.getStyleClass().setAll("gradient-in");
            return;
        }
        switch (getSkinnable().getFrameDesign()) {
            case GLOSSY_METAL:
                mainFrame.getStyleClass().setAll("glossy-metal");
                mainFrameOut.getStyleClass().setAll("mainFrameOut");
                mainFrameIn.getStyleClass().remove("gradient-in");
                break;
            case DARK_GLOSSY:
                mainFrame.getStyleClass().setAll("dark-glossy");
                mainFrameOut.getStyleClass().setAll("mainFrameOut");
                mainFrameIn.getStyleClass().remove("gradient-in");
                break;
            case CHROME:
                mainFrame.getStyleClass().setAll("gradient");
                mainFrameIn.getStyleClass().setAll("gradient-in");
                break;
            case BLACK_METAL:
                mainFrame.getStyleClass().setAll("gradient");
                mainFrameIn.getStyleClass().setAll("gradient-in");
                break;
            case SHINY_METAL:
                mainFrame.getStyleClass().setAll("gradient");
                mainFrameIn.getStyleClass().setAll("gradient-in");
                break;
            case CUSTOM_DESIGN:
                mainFrameIn.getStyleClass().setAll("gradient-in");
                break;
        }
    }
    
    private void setDots(){
        dots = new Pane();
        dotMap = new HashMap<>(getSkinnable().ledWidthProperty().intValue() * getSkinnable().ledHeightProperty().intValue());
        for (int i = 0; i < getSkinnable().ledHeightProperty().intValue(); i++) {
            for (int j = 0; j < getSkinnable().ledWidthProperty().intValue(); j++) {
                Circle circ = new Circle(radio,COLOR_OFF);
                dotMap.put(new Integer(j + i * getSkinnable().ledWidthProperty().intValue()), circ);
                dots.getChildren().add(circ);
            }
        }      
        dots.setCache(true);
        if(pane!=null && pane.getChildren().size()>0){
            pane.getChildren().remove(iDots);
            pane.getChildren().add(iDots, dots);
        }
    }

    private void registerListeners() {
        getSkinnable().widthProperty().addListener(o -> handleControlPropertyChanged("RESIZE") );
        getSkinnable().heightProperty().addListener(o -> handleControlPropertyChanged("RESIZE") );
        getSkinnable().prefWidthProperty().addListener(o -> handleControlPropertyChanged("PREF_SIZE") );
        getSkinnable().prefHeightProperty().addListener(o -> handleControlPropertyChanged("PREF_SIZE") );
        getSkinnable().ledWidthProperty().addListener(o -> handleControlPropertyChanged("UPDATE") );
        getSkinnable().ledHeightProperty().addListener(o -> handleControlPropertyChanged("UPDATE") );
        getSkinnable().frameVisibleProperty().addListener(o -> handleControlPropertyChanged("STYLE") );
        getSkinnable().frameDesignProperty().addListener(o -> handleControlPropertyChanged("STYLE") );
        getSkinnable().frameCustomPathProperty().addListener(o -> handleControlPropertyChanged("STYLE") );
        getSkinnable().frameBaseColorProperty().addListener(o -> handleControlPropertyChanged("STYLE") );
        
        getSkinnable().getStyleClass().addListener((ListChangeListener.Change<? extends String> change) -> {
            resize();
            createContents();
        });
        getSkinnable().getContents().addListener((ListChangeListener.Change<? extends Content> change) -> {
            while(change.next()){
                if(change.wasAdded() || change.wasRemoved()){
                    createContents();
                }
            }
        });
        getSkinnable().getContents().stream().forEach((Content t)->{
            final int iContent=getSkinnable().getContents().indexOf(t);
            t.colorProperty().addListener((o)-> handleContentPropertyChanged("CREATE",iContent)); 
            t.originProperty().addListener((o)-> handleContentPropertyChanged("CREATE",iContent)); 
            t.areaProperty().addListener((o)-> handleContentPropertyChanged("CREATE",iContent)); 
            t.bmpNameProperty().addListener((o)-> {
                if(byteAreas!=null && byteAreas.size()>iContent && byteAreas.get(iContent)!=null){
                    byteAreas.set(iContent,null);
                }
                handleContentPropertyChanged("CREATE",iContent);
            }); 
            t.txtContentProperty().addListener((o)-> handleContentPropertyChanged("CREATE",iContent)); 
            t.matrixFontProperty().addListener((o)-> handleContentPropertyChanged("CREATE",iContent)); 
            t.fontGapProperty().addListener((o)-> handleContentPropertyChanged("CREATE",iContent)); 
            t.txtAlignProperty().addListener((o)-> handleContentPropertyChanged("CREATE",iContent)); 
            t.effectProperty().addListener((o)->handleContentPropertyChanged("ANIMATE",iContent)); 
            t.pauseProperty().addListener((o)->handleContentPropertyChanged("ANIMATE",iContent)); 
            t.postEffectProperty().addListener((o)->handleContentPropertyChanged("ANIMATE",iContent)); 
            t.lapseProperty().addListener((o)->handleContentPropertyChanged("ANIMATE",iContent)); 
            t.clearProperty().addListener((o)->handleContentPropertyChanged("ANIMATE",iContent)); 
            t.orderProperty().addListener((o)->handleContentPropertyChanged("PAIRS",iContent)); 
        });
    }

    protected void handleControlPropertyChanged(final String PROPERTY) {    
        switch (PROPERTY) {
            case "UPDATE":
                setDots();
                setSize();
                resize();
                createContents();  
                break;
            case "STYLE":
                setStyle();
                gradient();
                break;
            case "RESIZE":
                resize();
                break;
            case "PREF_SIZE":
                aspectRatio = getSkinnable().getPrefHeight() / getSkinnable().getPrefWidth();
                break;
        }
    }
    
    protected void handleContentPropertyChanged(final String PROPERTY, final int iContent) {    
        switch (PROPERTY) {
            case "CREATE":
                Platform.runLater(() -> {
                    initializeContent(iContent);
                    animContent(iContent);
                    Anim.get(iContent).start();
                });
                break;
            case "ANIMATE":
                Platform.runLater(() -> {
                    animContent(iContent);
                    Anim.get(iContent).start();
                });
                break;
            case "PAIRS":
                checkPairs();
                Anim.stream().forEach((a) -> a.stop() );
                Platform.runLater(() -> {
                    getSkinnable().getContents().stream().forEach(c->{
                        animContent(getSkinnable().getContents().indexOf(c));
                    });     
                    Anim.stream().forEach((a) -> a.start() );
                });
        }
    }
    
    @Override protected double computeMinWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMinWidth(Math.max(MINIMUM_HEIGHT, HEIGHT - TOP_INSET - BOTTOM_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computeMinHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMinHeight(Math.max(MINIMUM_WIDTH, WIDTH - LEFT_INSET - RIGHT_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }

    @Override protected double computeMaxWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMaxWidth(Math.min(MAXIMUM_HEIGHT, HEIGHT - TOP_INSET - BOTTOM_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computeMaxHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMaxHeight(Math.min(MAXIMUM_WIDTH, WIDTH - LEFT_INSET - RIGHT_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }

    @Override protected double computePrefWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        double prefHeight = PREFERRED_HEIGHT;
        if (HEIGHT != -1) {
            prefHeight = Math.max(0, HEIGHT - TOP_INSET - BOTTOM_INSET);
        }
        return super.computePrefWidth(prefHeight, TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computePrefHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        double prefWidth = PREFERRED_WIDTH;
        if (WIDTH != -1) {
            prefWidth = Math.max(0, WIDTH - LEFT_INSET - RIGHT_INSET);
        }
        return super.computePrefHeight(prefWidth, TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    
    private void gradient(){
        width  = getSkinnable().getWidth();
        height = getSkinnable().getHeight();
        if(getSkinnable().isFrameVisible()){
            Platform.runLater(()->{
                Image image = null;   
                switch (getSkinnable().getFrameDesign()) {
                    case BLACK_METAL:
                        ConicalGradient bmGradient = new ConicalGradient(new Point2D(width/2d,height/2d),
                                                                     new Stop(0.0000, Color.rgb(254, 254, 254)),
                                                                     new Stop(0.1250, Color.rgb(0, 0, 0)),
                                                                     new Stop(0.3472, Color.rgb(153, 153, 153)),
                                                                     new Stop(0.5000, Color.rgb(0, 0, 0)),
                                                                     new Stop(0.6805, Color.rgb(153, 153, 153)),
                                                                     new Stop(0.8750, Color.rgb(0, 0, 0)),
                                                                     new Stop(1.0000, Color.rgb(254, 254, 254)));
                        image = bmGradient.apply(new Rectangle(width,height)).getImage();
                        break;
                    case CHROME:
                        ConicalGradient cmGradient = new ConicalGradient(new Point2D(width/2d,height/2d),
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
                        image = cmGradient.apply(new Rectangle(width,height)).getImage();
                        break;
                    case SHINY_METAL:
                        Color c=getSkinnable().getFrameBaseColor();
                        ConicalGradient smGradient = new ConicalGradient(new Point2D(width/2d,height/2d),
                                                                 new Stop(0.0000, Color.rgb(254, 254, 254)),
                                                                 new Stop(0.1250, Util.darker(c, 0.15)),
                                                                 new Stop(0.2500, c.darker()),
                                                                 new Stop(0.3472, c.brighter()),
                                                                 new Stop(0.5000, c.darker().darker()),
                                                                 new Stop(0.6527, c.brighter()),
                                                                 new Stop(0.7500, c.darker()),
                                                                 new Stop(0.8750, Util.darker(c, 0.15)),
                                                                 new Stop(1.0000, Color.rgb(254, 254, 254)));
                        image = smGradient.apply(new Rectangle(width,height)).getImage();
                        break;
                    case CUSTOM_DESIGN:
                        // load image
                        String pathJpg=getSkinnable().getFrameCustomPath();
                        // 1. from jar or url
                        if(pathJpg!=null && (pathJpg.contains("http") || pathJpg.startsWith("/"))){
                            image=new Image(pathJpg);
                        }
                        // 2. from local file
                        else if(pathJpg!=null){
                            File file = new File(pathJpg);
                            if(file.exists()){
                                image=new Image(file.toURI().toString());
                            } else {
                                //3. from resource
                                image=new Image(MatrixPanel.class.getResource(pathJpg).toExternalForm());
                            }
                        }
                        break;
                }
                if(image!=null){
                    fillFrame=new Background(new BackgroundImage(image, 
                                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
                                            BackgroundPosition.CENTER, new BackgroundSize(width,height,false,false,false,true)));
                }
                resize();
            });
        } else {
            resize();
        }
    }
    private void resize() {
        width  = getSkinnable().getWidth();
        height = getSkinnable().getHeight();
        if (aspectRatio * width > height) {
            width = 1 / (aspectRatio / height);
        } else if (1 / (aspectRatio / height) > width) {
            height = aspectRatio * width;
        }

        if (width > 0 && height > 0) {
            double size=Math.min(width,height);
            main.setPrefSize(width, height);            
            mainFrameOut.setPrefSize(width, height);
            mainFrame.setPrefSize(width, height);
            mainFrameIn.setPrefSize(width, height);
            if(getSkinnable().isFrameVisible()){
                mainFrameOut.setStyle("-fx-background-radius: "+(0.09333333333*size/2d)+";");
                switch (getSkinnable().getFrameDesign()) {
                    case GLOSSY_METAL:
                        mainFrame.setStyle("-fx-background-radius: "+(0.08*size/2d)+", "+(0.05*size/2d+1d)+", "+(0.05*size/2d)+";"+
                                           "-fx-background-insets: 1, "+(0.0841121495 *size-2d)+", "+(0.0841121495 *size)+";");
                        break;
                    case DARK_GLOSSY:
                        mainFrame.setStyle("-fx-background-radius: "+(0.08*size/2d)+", "+(0.08*size/2d)+", "+(0.05*size/2d+1d)+", "+(0.05*size/2d)+";"+
                                           "-fx-background-insets: 1, 1, "+(0.0841121495 *size-2d)+", "+(0.0841121495 *size)+";");
                        break;
                    case BLACK_METAL:
                    case CHROME:
                    case SHINY_METAL:
                    case CUSTOM_DESIGN:
                        Rectangle rectFrame=new Rectangle(width,height);
                        rectFrame.setArcHeight(0.08*size); rectFrame.setArcWidth(0.08*size);
                        if(fillFrame!=null){
                            Platform.runLater(()-> mainFrame.setBackground(fillFrame) );
                        }
                        mainFrame.setShape(rectFrame);
                        mainFrameIn.setStyle("-fx-background-radius: "+(0.06*size/2d+1d)+", "+(0.05*size/2d)+";"+
                                           "-fx-background-insets: "+(0.0841121495 *size-2d)+", "+(0.0841121495 *size)+";");
                        break;
                    default:
                        Rectangle rect=new Rectangle(width,height);
                        rect.setArcHeight(0.08*size); rect.setArcWidth(0.08*size);
                        mainFrame.setShape(rect);
                        mainFrame.setStyle("-fx-background-image: url(\""+jpgFrame+"\");\n" +
                                           "-fx-background-position: center center;\n" +
                                           "-fx-background-repeat: no-repeat;\n" +
                                           "-fx-background-size: cover");
                        mainFrameIn.setStyle("-fx-background-radius: "+(0.06*size/2d+1d)+", "+(0.05*size/2d)+";"+
                                           "-fx-background-insets: "+(0.0841121495 *size-2d)+", "+(0.0841121495 *size)+";");
                        break;
                }
            } else {
                mainFrame.setStyle("-fx-background-image: null; -fx-background-color: null;");
                mainFrameIn.setStyle("-fx-background-radius: "+(0.06*size/2d+1d)+", "+(0.05*size/2d)+";"+
                                     "-fx-background-insets: "+(0.0841121495 *size-2d)+", "+(0.0841121495 *size)+";");
            }
            radio=(width-2d*(0.0841121495*size+5d))/(3d*getSkinnable().ledWidthProperty().doubleValue()+1);
            double gapH = (height-2d*(0.0841121495*size+5d)-2d*radio*getSkinnable().ledHeightProperty().doubleValue())/(getSkinnable().ledHeightProperty().doubleValue()+1);
            for (int i = 0; i < getSkinnable().ledHeightProperty().intValue(); i++) {
                for (int j = 0; j < getSkinnable().ledWidthProperty().intValue(); j++) {
                    Circle c=(Circle)dots.getChildren().get(i*getSkinnable().ledWidthProperty().intValue()+j);
                    c.setTranslateX(0.0841121495 * size + 5d + 2d*radio + j * 3d * radio);
                    c.setTranslateY(0.0841121495 * size + 5d + gapH + radio + i * (gapH + 2d * radio));
                    c.setRadius(radio);
                }
            }
            
            mainForeground.setPrefSize(width-2d*(0.0841121495 * size + 2d), height-2d*(0.0841121495 * size + 2d));
            mainForeground.setTranslateX(0.0841121495 * size + 2d);
            mainForeground.setTranslateY(0.0841121495 * size + 2d);
            
        }
    }

    /***************
     *** CONTENT ***
     ***************/
    private final int LED_COLUMN    = 0;
    private final int LED_ROW       = 1;
    private final int LED_INTENSITY = 2;
    /*
     * full area required for each content, even not visible
     */
    private ArrayList<int[][]> fullAreas = null;
    /*
     * Contains collection of images bytes, to avoid reprocessing BMPs
     */
    private ArrayList<byte[]> byteAreas = null;
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
    
    public void createContents() {
        // stop previous animations, if any
        stop();
        // reset leds
        dotMap.values().stream().forEach((c) -> c.setFill(COLOR_OFF) );
        
        List<Content> contents=getSkinnable().getContents();
        
        if (0 == contents.size()) {
            return;
        }
        
        fullAreas = new ArrayList<>();
        byteAreas = new ArrayList<>();
        visibleArea = new Rectangle[contents.size()];
        Anim=new ArrayList<>();
        visibleContent=new SimpleBooleanProperty[contents.size()];
            
        checkPairs();
        
        /* Initialize and animate every Content */
        Platform.runLater(() -> {
            contents.stream().forEach(content -> {
                final int iContent=contents.indexOf(content);
                initializeContent(iContent);
                animContent(iContent);
            });
            Anim.stream().forEach((a) -> a.start());            
        });
    }
    
    private void initializeContent(int iContent){
        
        if(iContent<Anim.size()){
            Anim.get(iContent).stop();
        } 
        if(iContent<fullAreas.size()){
            clearArea(iContent);
            fullAreas.remove(iContent);
        }
        
        final Content content=getSkinnable().getContents().get(iContent);
        
        int x0 = (int) content.getOrigin().getX() + (int) content.getArea().getX();
        int y0 = (int) content.getOrigin().getY() + (int) content.getArea().getY();
        int maxX = Math.min((int) content.getArea().getWidth(), getSkinnable().ledWidthProperty().intValue());
        int maxY = Math.min((int) content.getArea().getHeight(), getSkinnable().ledHeightProperty().intValue());
        visibleArea[iContent] = new Rectangle(Math.max(x0, 0), Math.max(y0, 0), maxX, maxY);

        if (content.getType().equals(Content.Type.IMAGE)) {
            byte[] sBytes=null;
            if(iContent<fullAreas.size() && byteAreas!=null && byteAreas.get(iContent)!=null){
                sBytes=byteAreas.get(iContent);
            } else {
                UtilHex img = new UtilHex();
                img.convertsBmp(content.getBmpName(), 65, 190, true,true,true);
                sBytes = img.getRawData();
                byteAreas.add(iContent,sBytes);
                img.resetRawData();
            }
            if (sBytes != null) {
                final int levels = 3;
                final int bmpHeight = UtilHex.bytes2int(new byte[]{sBytes[8],sBytes[9]});
                final int tamLineaBMT = UtilHex.bytes2int(new byte[]{sBytes[20],sBytes[21],sBytes[22],sBytes[23]}) / bmpHeight / levels / 3; // en bytes
                int pos = 32;
                final int[][] area = new int[bmpHeight][tamLineaBMT * 8];
                final int[] colors={(content.getColor().equals(MatrixColor.RED) || content.getColor().equals(MatrixColor.YELLOW) || content.getColor().equals(MatrixColor.RGB))?1:0,
                    (content.getColor().equals(MatrixColor.GREEN) || content.getColor().equals(MatrixColor.YELLOW) || content.getColor().equals(MatrixColor.RGB))?1:0,
                    (content.getColor().equals(MatrixColor.BLUE) || content.getColor().equals(MatrixColor.RGB))?1:0};
                for (int j = 0; j < levels; j++) { // leds: [RED k=0]0-1-2-3, [GREEN k=1]0-10-20-30, [BLUE k=2] 0-100-200-300
                    for(int k=0; k<3; k++){ // 3 colors
                        for (int fila = 0; fila < bmpHeight; fila++) {
                            for (int i = 0; i < tamLineaBMT; i++) { // recorrido por cada byte de cada fila
                                BitSet bs=BitSet.valueOf(new byte[]{sBytes[pos++]}); 
                                for (int m = 0; m < 8; m++) {
                                    area[fila][i * 8 + m] += (bs.get(7-m)?1:0)*Math.pow(10,k)*colors[k];
                                }
                            }
                        }                        
                    }
                }
                fullAreas.add(iContent,area);
            }
            else{
                System.out.println("Error, image null: "+content.getBmpName());
                fullAreas.add(iContent,null);
                byteAreas.add(iContent,null);
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
                fullAreas.add(iContent,area);
            }
            else{
                fullAreas.add(iContent,null);
            }
            byteAreas.add(iContent,null);
        }
    }
    
    private void clearArea(int iContent){
        for (int i = (int) visibleArea[iContent].getY(); i < (int) visibleArea[iContent].getHeight(); i++) {
            for (int j = (int)visibleArea[iContent].getX(); j < (int) visibleArea[iContent].getWidth(); j++) {
                Integer dot = new Integer(j + i * getSkinnable().ledWidthProperty().intValue());
                if (dotMap.get(dot) != null) {
                    ((Circle)dotMap.get(dot)).setFill(COLOR_OFF);
                }
            }
        }
    }
    
    private void animContent(int iContent){
        if(iContent<Anim.size()){
            Anim.get(iContent).stop();
            Anim.remove(iContent);
        } 
        Animation iAnim=new Animation(iContent);
        iAnim.initAnimation();
        Anim.add(iContent,iAnim);
    }
    
    private void checkPairs(){
        /* Check for Content Pairs */
        pairs=new ArrayList<>();
        getSkinnable().getContents().stream().filter(c -> c.getOrder()==RotationOrder.FIRST)
        .forEach((Content content1) -> {
            getSkinnable().getContents().stream().filter(c -> c.getOrder()==RotationOrder.SECOND)
            .forEach((Content content2) -> {
                if(content1.getArea().getBoundsInLocal().equals(content2.getArea().getBoundsInLocal())){
                    pairs.add(new ContentPair(getSkinnable().getContents().indexOf(content1),
                                              getSkinnable().getContents().indexOf(content2)));
                }
            });
        });
    }
    private void stop(){
        if(Anim!=null){
            Anim.stream().forEach((a) -> a.stop() );
            Anim.clear();
            Anim=null;
        }
        if(fullAreas!=null){
            fullAreas.clear();
            fullAreas=null;
        }
    }
    
    private class Animation extends AnimationTimer{
        private long lastUpdate=0l;
        private boolean bBlink=false; // heartbit
        private int contBlink=0;
        private int iter=0;        
        private final int iContent;
        private Content content=null;
        private int oriX, oriY, endX, endY;
        private int areaWidth, areaHeight;
        private int contentWidth, contentHeight;
        private IntegerProperty posX, posY, posXIni, posYIni;
//        private int[][] contentArea=null;
        private int realLapse, advance, limX, limitBlink, iterLeds;
        private boolean isBlinkEffect;
        
        private LinkedHashMap<Integer,int[]> brightLeds=null;
        private ArrayList<int[]> arrBrightLeds=null;
        private IntegerProperty incrPos=null;
        
        private PauseTransition pTrans=null;
        
        public Animation(int iContent){
            
            this.iContent=iContent;
            this.content=getSkinnable().getContents().get(iContent);
            
            // bind posX/posY increment (1) to allow for pause time (0) for each content
            incrPos=new SimpleIntegerProperty(1);
            
            visibleContent[iContent]=new SimpleBooleanProperty(true); // SINGLE && FIRST
            if(content!=null && content.getOrder().equals(Content.RotationOrder.SECOND)){
                visibleContent[iContent].setValue(false);
            }
            
        }
        
        public void initAnimation(){
//            this.contentArea = fullAreas.get(iContent);            

            oriX = (int) visibleArea[iContent].getX();
            oriY = (int) visibleArea[iContent].getY();
            endX = (int) visibleArea[iContent].getWidth();
            endY = (int) visibleArea[iContent].getHeight();
            areaWidth = endX-oriX;
            areaHeight = endY-oriY;

            /*
            * Total dimensions of area of the content
            */
            if(fullAreas!=null && fullAreas.get(iContent)!=null){
                contentWidth =fullAreas.get(iContent)[0].length;
                contentHeight = fullAreas.get(iContent).length;
            }
            /*
            * START LOCATION OF CONTENT
            */
            posXIni= new SimpleIntegerProperty(0);
            posYIni = new SimpleIntegerProperty(0);
            if(content!=null && !content.getEffect().equals(Content.Effect.NONE)){
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
            }
            // +1,-1 to make the translation ot the content, 0 to pause it
            posX = new SimpleIntegerProperty(posXIni.get());
            posY = new SimpleIntegerProperty(posYIni.get());
            
            // speed = gap of ms to refresh the matrixPanel
            realLapse = (content!=null && content.getLapse() >= 250)?content.getLapse():250;
            
            if(content!=null && content.getLapse()>0){
                // leds advance per step (int), lower lapse, bigger advance:
                advance=realLapse/content.getLapse(); 
                realLapse=advance*content.getLapse();
            }
            else{
                advance=10;
            }
            if(content!=null && !content.getEffect().equals(Content.Effect.NONE)){
            
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
                    brightLeds = new LinkedHashMap<>();
                    arrBrightLeds=new ArrayList<>();
                    int[][] contentArea= fullAreas.get(iContent); 
                    // list of brighting LEDs: column j, row i, intensity val
                    for (int i = oriY; i < endY; i++) {
                        for (int j = oriX; j < endX; j++) {
                            Integer dot = new Integer(j + i * getSkinnable().ledWidthProperty().intValue());
                            if (dotMap.get(dot) != null) {
                                int val;
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
                    if(brightLeds.size()>0){
                        if(content!=null && content.getLapse()>0){
                            iterLeds=brightLeds.size()/content.getLapse();
                        } else {
                            iterLeds=brightLeds.size()/10;
                        }
                    } else {
                        iterLeds=0;
                    }
                }
            }
        }
                
        @Override
        public void handle(long now) {
            /*
            *  only make one frame step animation IF enough fps, 
            *  the content is visible and it isn't in pause
            */
            if (now > lastUpdate + realLapse*1000000 && 
                content!=null && iContent<visibleContent.length && 
                visibleContent[iContent].getValue() && incrPos.intValue()==1) {  

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
                        Integer dot = new Integer(led[LED_COLUMN] + led[LED_ROW] * getSkinnable().ledWidthProperty().intValue());
                        ((Circle)dotMap.get(dot)).setFill(Color.rgb(toneScale*toneR, toneScale*toneG, toneScale*toneB));
                        iter=(iter<brightLeds.size()-1)?iter+1:iter;
                    }                            
                } else {
                    int[][] contentArea= fullAreas.get(iContent); 
                    for (int j = oriX; j < endX; j++) {
                        for (int i = oriY; i < endY; i++) {
                            Integer dot = new Integer(j + i * getSkinnable().ledWidthProperty().intValue());
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
                    contentArea=null;
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
                    endRotation = (posY.intValue() <= 0); // fullHeight-fullHeight
                    if(posY.intValue() - advance*incrPos.getValue() <= 0){ 
                        posY.set(0);
                    }
                    else{
                        posY.set(posY.intValue() - advance*incrPos.getValue());
                    }
                } else if (content.getEffect().equals(Content.Effect.SCROLL_UP)) {
                    endRotation = (posY.intValue() >= 0); // -areaHeight+areaHeight
                    if(posY.intValue() + advance*incrPos.getValue() >= 0){ 
                        posY.set(0);
                    }
                    else{
                        posY.set(posY.intValue() + advance*incrPos.getValue());
                    } 
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
                        pTrans=new PauseTransition();
                        if(content.getPostEffect().equals(Content.PostEffect.REPEAT)){
                            pTrans.setDuration(Duration.millis(10));
                        } else{
                            pTrans.setDuration(Duration.millis(content.getPause()));
                        }
                        pTrans.setOnFinished((ActionEvent event) -> {
                            incrPos.setValue(1);
                            
                            // clear screen
                            if(content!=null && (content.getClear() || content.getEffect().equals(Content.Effect.SPRAY))){
                                for (int i = oriY; i < endY; i++) {
                                    for (int j = oriX; j < endX; j++) {
                                        Integer dot = new Integer(j + i * getSkinnable().ledWidthProperty().intValue());
                                        ((Circle)dotMap.get(dot)).setFill(COLOR_OFF);
                                    }
                                }
                            }
                            
                            if(content!=null && !content.getOrder().equals(Content.RotationOrder.SINGLE)){
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
                        });                                        
                        pTrans.playFromStart();                                
                    }

                }
                //System.out.println((now-lastUpdate)/1000000);
                lastUpdate = now;
            }
        }
        
        @Override
        public void stop(){
            if(brightLeds!=null){
                brightLeds.clear();
                brightLeds=null;
            }
            if(arrBrightLeds!=null){
                arrBrightLeds.clear();
                arrBrightLeds=null;
            }
            if(pTrans!=null){
                pTrans.stop();
                pTrans=null;
            }
            content=null;
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

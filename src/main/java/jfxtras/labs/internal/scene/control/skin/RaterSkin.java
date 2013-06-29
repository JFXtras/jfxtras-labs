/*
 * Copyright (c) 2012, JFXtras
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of the <organization> nor the
 *         names of its contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.internal.scene.control.skin;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.RaterBehavior;
import jfxtras.labs.scene.control.gauge.Rater;
import jfxtras.labs.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 07.03.12
 * Time: 10:17
 */
public class RaterSkin extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<Rater, RaterBehavior> {
    private static final double      PREFERRED_WIDTH  = 256;
    private static final double      PREFERRED_HEIGHT = 256;
    private static final double      MINIMUM_WIDTH    = 8;
    private static final double      MINIMUM_HEIGHT   = 8;
    private static final double      MAXIMUM_WIDTH    = 32;
    private static final double      MAXIMUM_HEIGHT   = 32;
    private Rater       control;
    private boolean     isDirty;
    private boolean     initialized;
    private int         noOfStars;
    private int         rating;
    private HBox        starContainer;
    private List<Group> stars;
    private int         currentIndex;
    private int         currentMouseOverIndex;


    // ******************** Constructors **************************************
    public RaterSkin(final Rater CONTROL) {
        super(CONTROL, new RaterBehavior(CONTROL));
        control               = CONTROL;
        initialized           = false;
        isDirty               = false;
        noOfStars             = control.getNoOfStars();
        rating                = control.getRating();
        starContainer         = new HBox();
        stars                 = new ArrayList<Group>(noOfStars);
        currentIndex          = 0;
        currentMouseOverIndex = 0;

        init();
    }

    private void init() {
        if (Double.compare(getSkinnable().getPrefWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getPrefHeight(), 0.0) <= 0 ||
            Double.compare(getSkinnable().getWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getHeight(), 0.0) <= 0) {
            if (getSkinnable().getPrefWidth() > 0 && getSkinnable().getPrefHeight() > 0) {
                getSkinnable().setPrefSize(getSkinnable().getPrefWidth(), getSkinnable().getPrefHeight());
            } else {
                getSkinnable().setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        if (Double.compare(getSkinnable().getMinWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getMinHeight(), 0.0) <= 0) {
            getSkinnable().setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(getSkinnable().getMaxWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getMaxHeight(), 0.0) <= 0) {
            getSkinnable().setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.darkColorProperty(), "DARK_COLOR");
        registerChangeListener(control.brightColorProperty(), "BRIGHT_COLOR");
        registerChangeListener(control.noOfStarsProperty(), "NO_OF_STARS");
        registerChangeListener(control.ratingProperty(), "RATING");

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("NO_OF_STARS".equals(PROPERTY)) {
            noOfStars = control.getNoOfStars();
            drawStars();
            repaint();
        } else if ("RATING".equals(PROPERTY)) {
            rating = control.getRating();
            updateStars();
        } else if ("BRIGHT_COLOR".equals(PROPERTY)) {
            updateStars();
        } else if ("DARK_COLOR".equals(PROPERTY)) {
            updateStars();
        } else if ("PREF_WIDTH".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            repaint();
        }
    }

    public final void repaint() {
        isDirty = true;
        getSkinnable().requestLayout();
    }

    @Override public void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        if (!isDirty) {
            return;
        }
        if (!initialized) {
            init();
        }
        if (control.getScene() != null) {
            drawStars();
            getChildren().setAll(starContainer);
        }
        isDirty = false;
    }

    public final Rater getControl() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
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

    public int getCurrentIndex() {
        return currentIndex;
    }


    // ******************** Mouse event handling ******************************
    private void addMouseEventListener(final Group STAR, final int INDEX) {
        STAR.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                currentMouseOverIndex = INDEX;
                highlightStars();
            }
        });

        STAR.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent EVENT) {
                currentIndex = INDEX;
            }
        });
    }


    // ******************** Drawing related ***********************************
    private void drawStars() {
        starContainer.getChildren().clear();
        stars.clear();
        for (int i = 0 ; i < rating ; i++) {
            stars.add(createStar(true));
        }
        for (int i = 0 ; i < (noOfStars - rating) ;  i++) {
            stars.add(createStar(false));
        }
        int index = 0;
        for (Group star : stars) {
            starContainer.getChildren().add(star);
            addMouseEventListener(star, index + 1);
            index++;
        }
    }

    private void updateStars() {
        StringBuilder styleBuilder = new StringBuilder(128);
        for (int i = 0 ; i < stars.size() ; i++) {
            styleBuilder.setLength(0);
            if (i < rating) {
                styleBuilder.append("-fx-rater-bright-color: " + Util.colorToCssColor(control.getBrightColor()));
                styleBuilder.append("-fx-rater-dark-color: " + Util.colorToCssColor(control.getDarkColor()));
                if (i <= currentMouseOverIndex - 1) {
                    styleBuilder.append("-fx-rater-stroke: " + Util.colorToCssColor(control.getBrightColor()));
                } else {
                    styleBuilder.append("-fx-rater-stroke: transparent;");
                }
            } else {
                styleBuilder.append("-fx-rater-bright-color: white;");
                styleBuilder.append("-fx-rater-dark-color: rgb(204, 204, 204);");
                if (i <= currentMouseOverIndex - 1) {
                    styleBuilder.append("-fx-rater-stroke: " + Util.colorToCssColor(control.getBrightColor()));
                } else {
                    styleBuilder.append("-fx-rater-stroke: transparent;");
                }
            }
            stars.get(i).setStyle(styleBuilder.toString());
        }
    }

    public void highlightStars() {
        StringBuilder styleBuilder = new StringBuilder(128);
        for (int i = 0 ; i < stars.size() ; i++) {
            styleBuilder.setLength(0);
            if (i < rating) {
                styleBuilder.append("-fx-rater-bright-color: " + Util.colorToCssColor(control.getBrightColor()));
                styleBuilder.append("-fx-rater-dark-color: " + Util.colorToCssColor(control.getDarkColor()));
            } else {
                styleBuilder.append("-fx-rater-bright-color: white;");
                styleBuilder.append("-fx-rater-dark-color: rgb(204, 204, 204);");
            }
            if (i < currentMouseOverIndex) {
                styleBuilder.append("-fx-rater-stroke: " + Util.colorToCssColor(control.getBrightColor()));
            } else {
                styleBuilder.append("-fx-rater-stroke: transparent;");
            }
            stars.get(i).setStyle(styleBuilder.toString());
        }
    }

    public void deHighlightStars() {
        StringBuilder styleBuilder = new StringBuilder(128);
        currentMouseOverIndex = 0;
        for (int i = 0 ; i < stars.size() ; i++) {
            styleBuilder.setLength(0);
            if (i < rating) {
                styleBuilder.append("-fx-rater-bright-color: " + Util.colorToCssColor(control.getBrightColor()));
                styleBuilder.append("-fx-rater-dark-color: " + Util.colorToCssColor(control.getDarkColor()));
            } else {
                styleBuilder.append("-fx-rater-bright-color: white;");
                styleBuilder.append("-fx-rater-dark-color: rgb(204, 204, 204);");
            }
            styleBuilder.append("-fx-rater-stroke: transparent;");
            stars.get(i).setStyle(styleBuilder.toString());
        }
    }

    private final Group createStar(final boolean SELECTED) {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        final Group STAR = new Group();

        if (SELECTED) {
            STAR.setStyle("-fx-rater-bright-color: " + Util.colorToCssColor(control.getBrightColor()) +
                          "-fx-rater-dark-color: " + Util.colorToCssColor(control.getDarkColor()) +
                          "-fx-rater-stroke: transparent");
        } else {
            STAR.setStyle("-fx-rater-bright-color: white;" +
                          "-fx-rater-dark-color: rgb(204, 204, 204);" +
                          "-fx-rater-stroke: transparent");
        }

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        STAR.getChildren().add(IBOUNDS);

        final Path STAR_SHAPE = new Path();
        STAR_SHAPE.setFillRule(FillRule.EVEN_ODD);
        STAR_SHAPE.getElements().add(new MoveTo(0.5 * WIDTH, 0.04 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.505 * WIDTH, 0.04 * HEIGHT,
                                                      0.64 * WIDTH, 0.35 * HEIGHT,
                                                      0.64 * WIDTH, 0.35 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.64 * WIDTH, 0.35 * HEIGHT,
                                                      0.975 * WIDTH, 0.385 * HEIGHT,
                                                      0.975 * WIDTH, 0.385 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.975 * WIDTH, 0.39 * HEIGHT,
                                                      0.725 * WIDTH, 0.615 * HEIGHT,
                                                      0.725 * WIDTH, 0.615 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.725 * WIDTH, 0.615 * HEIGHT,
                                                      0.795 * WIDTH, 0.94 * HEIGHT,
                                                      0.795 * WIDTH, 0.945 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.79 * WIDTH, 0.945 * HEIGHT,
                                                      0.5 * WIDTH, 0.78 * HEIGHT,
                                                      0.5 * WIDTH, 0.78 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.78 * HEIGHT,
                                                      0.21 * WIDTH, 0.945 * HEIGHT,
                                                      0.205 * WIDTH, 0.945 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.205 * WIDTH, 0.94 * HEIGHT,
                                                      0.275 * WIDTH, 0.615 * HEIGHT,
                                                      0.275 * WIDTH, 0.615 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.275 * WIDTH, 0.615 * HEIGHT,
                                                      0.025 * WIDTH, 0.39 * HEIGHT,
                                                      0.025 * WIDTH, 0.385 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.025 * WIDTH, 0.385 * HEIGHT,
                                                      0.36 * WIDTH, 0.35 * HEIGHT,
                                                      0.36 * WIDTH, 0.35 * HEIGHT));
        STAR_SHAPE.getElements().add(new CubicCurveTo(0.36 * WIDTH, 0.35 * HEIGHT,
                                                      0.495 * WIDTH, 0.04 * HEIGHT,
                                                      0.5 * WIDTH, 0.04 * HEIGHT));
        STAR_SHAPE.getElements().add(new ClosePath());
        STAR_SHAPE.setSmooth(true);
        STAR_SHAPE.getStyleClass().add("star-fill");

        final InnerShadow INNER_GLOW = new InnerShadow();
        INNER_GLOW.setWidth(0.2 * STAR_SHAPE.getLayoutBounds().getWidth());
        INNER_GLOW.setHeight(0.2 * STAR_SHAPE.getLayoutBounds().getHeight());
        INNER_GLOW.setOffsetX(0.0);
        INNER_GLOW.setOffsetY(0.0);
        INNER_GLOW.setRadius(0.2 * STAR_SHAPE.getLayoutBounds().getWidth());
        INNER_GLOW.setColor(Color.color(1, 1, 1, 0.65));
        INNER_GLOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.1 * STAR_SHAPE.getLayoutBounds().getWidth());
        INNER_SHADOW.setHeight(0.1 * STAR_SHAPE.getLayoutBounds().getHeight());
        INNER_SHADOW.setOffsetX(0.0);
        INNER_SHADOW.setOffsetY(0.0);
        INNER_SHADOW.setRadius(0.1 * STAR_SHAPE.getLayoutBounds().getWidth());
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.65));
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_SHADOW.inputProperty().set(INNER_GLOW);

        final DropShadow DROP_SHADOW = new DropShadow();
        DROP_SHADOW.setWidth(0.1 * STAR_SHAPE.getLayoutBounds().getWidth());
        DROP_SHADOW.setHeight(0.1 * STAR_SHAPE.getLayoutBounds().getHeight());
        DROP_SHADOW.setOffsetX(0.0);
        DROP_SHADOW.setOffsetY(0.0);
        DROP_SHADOW.setRadius(0.1 * STAR_SHAPE.getLayoutBounds().getWidth());
        DROP_SHADOW.setColor(Color.color(0, 0, 0, 0.65));
        DROP_SHADOW.setBlurType(BlurType.GAUSSIAN);
        DROP_SHADOW.inputProperty().set(INNER_SHADOW);
        STAR_SHAPE.setEffect(DROP_SHADOW);

        final Path INNER_HIGHLIGHT = new Path();
        INNER_HIGHLIGHT.setFillRule(FillRule.EVEN_ODD);
        INNER_HIGHLIGHT.getElements().add(new MoveTo(0.5 * WIDTH, 0.09 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.49 * WIDTH, 0.09 * HEIGHT,
                                                           0.365 * WIDTH, 0.355 * HEIGHT,
                                                           0.365 * WIDTH, 0.355 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.365 * WIDTH, 0.355 * HEIGHT,
                                                           0.05 * WIDTH, 0.39 * HEIGHT,
                                                           0.045 * WIDTH, 0.395 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.045 * WIDTH, 0.395 * HEIGHT,
                                                           0.055 * WIDTH, 0.4 * HEIGHT,
                                                           0.065 * WIDTH, 0.41 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.135 * WIDTH, 0.4 * HEIGHT,
                                                           0.375 * WIDTH, 0.375 * HEIGHT,
                                                           0.375 * WIDTH, 0.375 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.375 * WIDTH, 0.375 * HEIGHT,
                                                           0.495 * WIDTH, 0.155 * HEIGHT,
                                                           0.5 * WIDTH, 0.155 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.51 * WIDTH, 0.155 * HEIGHT,
                                                           0.625 * WIDTH, 0.375 * HEIGHT,
                                                           0.625 * WIDTH, 0.375 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.625 * WIDTH, 0.375 * HEIGHT,
                                                           0.865 * WIDTH, 0.4 * HEIGHT,
                                                           0.935 * WIDTH, 0.41 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.945 * WIDTH, 0.4 * HEIGHT,
                                                           0.955 * WIDTH, 0.395 * HEIGHT,
                                                           0.955 * WIDTH, 0.395 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.95 * WIDTH, 0.39 * HEIGHT,
                                                           0.635 * WIDTH, 0.355 * HEIGHT,
                                                           0.635 * WIDTH, 0.355 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.635 * WIDTH, 0.355 * HEIGHT,
                                                           0.51 * WIDTH, 0.09 * HEIGHT,
                                                           0.5 * WIDTH, 0.09 * HEIGHT));
        INNER_HIGHLIGHT.getElements().add(new ClosePath());
        INNER_HIGHLIGHT.setSmooth(true);
        INNER_HIGHLIGHT.getStyleClass().add("highlights-inner-fill");
        INNER_HIGHLIGHT.setStroke(null);

        final Path TOP_HIGHLIGHT = new Path();
        TOP_HIGHLIGHT.setFillRule(FillRule.EVEN_ODD);
        TOP_HIGHLIGHT.getElements().add(new MoveTo(0.5 * WIDTH, 0.065 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new CubicCurveTo(0.495 * WIDTH, 0.065 * HEIGHT,
                                                         0.365 * WIDTH, 0.355 * HEIGHT,
                                                         0.365 * WIDTH, 0.355 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new CubicCurveTo(0.365 * WIDTH, 0.355 * HEIGHT,
                                                         0.05 * WIDTH, 0.39 * HEIGHT,
                                                         0.045 * WIDTH, 0.395 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new CubicCurveTo(0.045 * WIDTH, 0.395 * HEIGHT,
                                                         0.21 * WIDTH, 0.54 * HEIGHT,
                                                         0.265 * WIDTH, 0.595 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new CubicCurveTo(0.415 * WIDTH, 0.485 * HEIGHT,
                                                         0.655 * WIDTH, 0.415 * HEIGHT,
                                                         0.93 * WIDTH, 0.415 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new CubicCurveTo(0.93 * WIDTH, 0.415 * HEIGHT,
                                                         0.93 * WIDTH, 0.415 * HEIGHT,
                                                         0.93 * WIDTH, 0.415 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new CubicCurveTo(0.945 * WIDTH, 0.4 * HEIGHT,
                                                         0.955 * WIDTH, 0.395 * HEIGHT,
                                                         0.955 * WIDTH, 0.395 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new CubicCurveTo(0.95 * WIDTH, 0.39 * HEIGHT,
                                                         0.635 * WIDTH, 0.355 * HEIGHT,
                                                         0.635 * WIDTH, 0.355 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new CubicCurveTo(0.635 * WIDTH, 0.355 * HEIGHT,
                                                         0.505 * WIDTH, 0.065 * HEIGHT,
                                                         0.5 * WIDTH, 0.065 * HEIGHT));
        TOP_HIGHLIGHT.getElements().add(new ClosePath());
        TOP_HIGHLIGHT.setSmooth(true);
        TOP_HIGHLIGHT.getStyleClass().add("highlights-top-fill");
        TOP_HIGHLIGHT.setStroke(null);

        STAR.getChildren().addAll(STAR_SHAPE, INNER_HIGHLIGHT, TOP_HIGHLIGHT);
        STAR.setCache(true);

        return STAR;
    }
}

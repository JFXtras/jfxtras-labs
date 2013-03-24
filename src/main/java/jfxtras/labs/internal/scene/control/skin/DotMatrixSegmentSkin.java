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

import javafx.scene.control.SkinBase;
import javafx.scene.Group;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.DotMatrixSegmentBehavior;
import jfxtras.labs.scene.control.gauge.DotMatrixSegment;
import jfxtras.labs.util.Util;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by
 * User: hansolo
 * Date: 15.03.12
 * Time: 12:15
 */
public class DotMatrixSegmentSkin extends SkinBase<DotMatrixSegment, DotMatrixSegmentBehavior> {
    private DotMatrixSegment control;
    private boolean                          isDirty;
    private boolean                          initialized;
    private Group                            dots;
    private Map<DotMatrixSegment.Dot, Shape> dotMap;


    // ******************** Constructors **************************************
    public DotMatrixSegmentSkin(final DotMatrixSegment CONTROL) {
        super(CONTROL, new DotMatrixSegmentBehavior(CONTROL));
        control     = CONTROL;
        initialized = false;
        isDirty     = false;
        dots        = new Group();
        dotMap      = new HashMap<DotMatrixSegment.Dot, Shape>(17);

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(40, 56);
        }

        if (control.getMinWidth() < 0 | control.getMinHeight() < 0) {
            control.setMinSize(10, 14);
        }

        if (control.getMaxWidth() < 0 | control.getMaxHeight() < 0) {
            control.setMaxSize(400, 560);
        }

        createDots();
        updateCharacter();

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.characterProperty(), "CHARACTER");
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.plainColorProperty(), "PLAIN_COLOR");
        registerChangeListener(control.customDotMappingProperty(), "CUSTOM_MAPPING");
        registerChangeListener(control.dotOnProperty(), "DOT_ON");

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if ("CHARACTER".equals(PROPERTY)) {
            updateCharacter();
        } else if ("COLOR".equals(PROPERTY)) {
            updateCharacter();
        } else if ("PLAIN_COLOR".equals(PROPERTY)) {
            updateCharacter();
        } else if ("CUSTOM_MAPPING".equals(PROPERTY)) {
            updateCharacter();
        } else if ("DOT_ON".equals(PROPERTY)) {
            updateCharacter();
        } else if ("PREF_WIDTH".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            repaint();
        }
    }

    public final void repaint() {
        isDirty = true;
        requestLayout();
    }

    @Override public void layoutChildren() {
        if (!isDirty) {
            return;
        }
        if (!initialized) {
            init();
        }
        if (control.getScene() != null) {
            updateCharacter();
            getChildren().setAll(dots);
        }
        isDirty = false;

        super.layoutChildren();
    }

    @Override public final DotMatrixSegment getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 40;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 56;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    public void updateCharacter() {
        dots.setStyle("-fx-segment-color-on: " + Util.colorToCssColor(control.getColor()) +
                      "-fx-segment-color-off: " + Util.colorToCssColor(Color.color(control.getColor().getRed(), control.getColor().getGreen(), control.getColor().getBlue(), 0.075)));
        final int ASCII = control.getCharacter().isEmpty() ? 20 : control.getCharacter().toUpperCase().charAt(0);
        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setRadius(0.05 * control.getPrefWidth());
        INNER_SHADOW.setColor(Color.hsb(control.getColor().getHue(), control.getColor().getSaturation(), 0.2));

        final String ON_STYLE = control.isPlainColor() ? "dot-matrix-segment-plain-on" : "dot-matrix-segment-on";

        if (control.getCustomDotMapping().isEmpty()) {
            for (DotMatrixSegment.Dot dot : dotMap.keySet()) {
                if (control.getDotMapping().containsKey(ASCII)) {
                    if (control.getDotMapping().get(ASCII).contains(dot)) {
                        dotMap.get(dot).getStyleClass().clear();
                        dotMap.get(dot).getStyleClass().add(ON_STYLE);
                        dotMap.get(dot).setEffect(INNER_SHADOW);
                    } else {
                        dotMap.get(dot).getStyleClass().clear();
                        dotMap.get(dot).getStyleClass().add("dot-matrix-segment-off");
                        dotMap.get(dot).setEffect(null);
                    }
                } else {
                    dotMap.get(dot).getStyleClass().clear();
                    dotMap.get(dot).getStyleClass().add("dot-matrix-segment-off");
                    dotMap.get(dot).setEffect(null);
                }
            }
        } else {
            for (DotMatrixSegment.Dot dot : dotMap.keySet()) {
                if (control.getCustomDotMapping().containsKey(ASCII)) {
                    if (control.getCustomDotMapping().get(ASCII).contains(dot)) {
                        dotMap.get(dot).getStyleClass().clear();
                        dotMap.get(dot).getStyleClass().add(ON_STYLE);
                        dotMap.get(dot).setEffect(INNER_SHADOW);
                    } else {
                        dotMap.get(dot).getStyleClass().clear();
                        dotMap.get(dot).getStyleClass().add("dot-matrix-segment-off");
                        dotMap.get(dot).setEffect(null);
                    }
                } else {
                    dotMap.get(dot).getStyleClass().clear();
                    dotMap.get(dot).getStyleClass().add("dot-matrix-segment-off");
                    dotMap.get(dot).setEffect(null);
                }
            }
        }
    }

    public final void createDots() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        dots.setStyle("-fx-segment-color-on: " + Util.colorToCssColor(control.getColor()) +
                      "-fx-segment-color-off: " + Util.colorToCssColor(Color.color(control.getColor().getRed(), control.getColor().getGreen(), control.getColor().getBlue(), 0.075)));

        dots.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        dots.getChildren().add(IBOUNDS);

        final Circle D57 = new Circle(0.8902439024390244 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        D57.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D57, D57);

        final Circle D47 = new Circle(0.6951219512195121 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        D47.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D47, D47);

        final Circle D37 = new Circle(0.5 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        D37.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D37, D37);

        final Circle D27 = new Circle(0.3048780487804878 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        D27.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D27, D27);

        final Circle D17 = new Circle(0.10975609756097561 * WIDTH, 0.9210526315789473 * HEIGHT, 0.08536585365853659 * WIDTH);
        D17.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D17, D17);

        final Circle D56 = new Circle(0.8902439024390244 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        D56.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D56, D56);

        final Circle D46 = new Circle(0.6951219512195121 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        D46.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D46, D46);

        final Circle D36 = new Circle(0.5 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        D36.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D36, D36);

        final Circle D26 = new Circle(0.3048780487804878 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        D26.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D26, D26);

        final Circle D16 = new Circle(0.10975609756097561 * WIDTH, 0.7807017543859649 * HEIGHT, 0.08536585365853659 * WIDTH);
        D16.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D16, D16);

        final Circle D55 = new Circle(0.8902439024390244 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        D55.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D55, D55);

        final Circle D45 = new Circle(0.6951219512195121 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        D45.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D45, D45);

        final Circle D35 = new Circle(0.5 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        D35.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D35, D35);

        final Circle D25 = new Circle(0.3048780487804878 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        D25.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D25, D25);

        final Circle D15 = new Circle(0.10975609756097561 * WIDTH, 0.6403508771929824 * HEIGHT, 0.08536585365853659 * WIDTH);
        D15.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D15, D15);

        final Circle D54 = new Circle(0.8902439024390244 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        D54.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D54, D54);

        final Circle D44 = new Circle(0.6951219512195121 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        D44.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D44, D44);

        final Circle D34 = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        D34.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D34, D34);

        final Circle D24 = new Circle(0.3048780487804878 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        D24.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D24, D24);

        final Circle D14 = new Circle(0.10975609756097561 * WIDTH, 0.5 * HEIGHT, 0.08536585365853659 * WIDTH);
        D14.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D14, D14);

        final Circle D53 = new Circle(0.8902439024390244 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        D53.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D53, D53);

        final Circle D43 = new Circle(0.6951219512195121 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        D43.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D43, D43);

        final Circle D33 = new Circle(0.5 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        D33.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D33, D33);

        final Circle D23 = new Circle(0.3048780487804878 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        D23.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D23, D23);

        final Circle D13 = new Circle(0.10975609756097561 * WIDTH, 0.35964912280701755 * HEIGHT, 0.08536585365853659 * WIDTH);
        D13.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D13, D13);

        final Circle D52 = new Circle(0.8902439024390244 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        D52.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D52, D52);

        final Circle D42 = new Circle(0.6951219512195121 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        D42.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D42, D42);

        final Circle D32 = new Circle(0.5 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        D32.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D32, D32);

        final Circle D22 = new Circle(0.3048780487804878 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        D22.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D22, D22);

        final Circle D12 = new Circle(0.10975609756097561 * WIDTH, 0.21929824561403508 * HEIGHT, 0.08536585365853659 * WIDTH);
        D12.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D12, D12);

        final Circle D51 = new Circle(0.8902439024390244 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        D51.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D51, D51);

        final Circle D41 = new Circle(0.6951219512195121 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        D41.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D41, D41);

        final Circle D31 = new Circle(0.5 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        D31.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D31, D31);

        final Circle D21 = new Circle(0.3048780487804878 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        D21.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D21, D21);

        final Circle D11 = new Circle(0.10975609756097561 * WIDTH, 0.07894736842105263 * HEIGHT, 0.08536585365853659 * WIDTH);
        D11.getStyleClass().add("dot-matrix-segment-off");
        dotMap.put(DotMatrixSegment.Dot.D11, D11);

        dots.getChildren().addAll(D57,
                                 D47,
                                 D37,
                                 D27,
                                 D17,
                                 D56,
                                 D46,
                                 D36,
                                 D26,
                                 D16,
                                 D55,
                                 D45,
                                 D35,
                                 D25,
                                 D15,
                                 D54,
                                 D44,
                                 D34,
                                 D24,
                                 D14,
                                 D53,
                                 D43,
                                 D33,
                                 D23,
                                 D13,
                                 D52,
                                 D42,
                                 D32,
                                 D22,
                                 D12,
                                 D51,
                                 D41,
                                 D31,
                                 D21,
                                 D11);
        dots.setCache(true);
    }
}

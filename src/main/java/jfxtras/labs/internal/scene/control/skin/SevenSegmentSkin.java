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

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.SevenSegmentBehavior;
import jfxtras.labs.internal.scene.control.behavior.SixteenSegmentBehavior;
import jfxtras.labs.scene.control.gauge.SevenSegment;
import jfxtras.labs.scene.control.gauge.SixteenSegment;
import jfxtras.labs.scene.control.gauge.Util;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by
 * User: hansolo
 * Date: 23.04.12
 * Time: 08:12
 */
public class SevenSegmentSkin extends SkinBase<SevenSegment, SevenSegmentBehavior> {
    private SevenSegment                       control;
    private boolean                            isDirty;
    private boolean                            initialized;
    private Group segments;
    private Map<SevenSegment.Segment, Shape> segmentMap;


    // ******************** Constructors **************************************
    public SevenSegmentSkin(final SevenSegment CONTROL) {
        super(CONTROL, new SevenSegmentBehavior(CONTROL));
        control     = CONTROL;
        initialized = false;
        isDirty     = false;
        segments    = new Group();
        segmentMap  = new HashMap<SevenSegment.Segment, Shape>(17);

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(100, 150);
        }

        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });
        createSegments();
        updateCharacter();

        // Register listeners
        registerChangeListener(control.characterProperty(), "CHARACTER");
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.plainColorProperty(), "PLAIN_COLOR");
        registerChangeListener(control.customSegmentMappingProperty(), "CUSTOM_MAPPING");
        registerChangeListener(control.dotOnProperty(), "DOT_ON");

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        updateCharacter();
        getChildren().clear();
        getChildren().add(segments);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if (PROPERTY == "CHARACTER") {
            updateCharacter();
        } else if (PROPERTY == "COLOR") {
            updateCharacter();
        } else if (PROPERTY == "PLAIN_COLOR") {
            updateCharacter();
        } else if (PROPERTY == "CUSTOM_MAPPING") {
            updateCharacter();
        } else if (PROPERTY == "DOT_ON") {
            updateCharacter();
        }
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public final SevenSegment getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 100;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 150;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    public void updateCharacter() {
        segments.setStyle("-fx-segment-color-on: " + Util.INSTANCE.createCssColor(control.getColor()) +
                          "-fx-segment-color-off: " + Util.INSTANCE.createCssColor(Color.color(control.getColor().getRed(), control.getColor().getGreen(), control.getColor().getBlue(), 0.075)));
        final int ASCII = control.getCharacter().isEmpty() ? 20 : control.getCharacter().toUpperCase().charAt(0);
        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setRadius(0.05 * control.getPrefWidth());
        INNER_SHADOW.setColor(Color.hsb(control.getColor().getHue(), control.getColor().getSaturation(), 0.2));

        final String ON_STYLE = control.isPlainColor() ? "sixteen-segment-plain-on" : "sixteen-segment-on";

        if (control.getCustomSegmentMapping().isEmpty()) {
            for (SevenSegment.Segment segment : segmentMap.keySet()) {
                if (control.getSegmentMapping().containsKey(ASCII)) {
                    if (control.getSegmentMapping().get(ASCII).contains(segment)) {
                        segmentMap.get(segment).setId(ON_STYLE);
                        segmentMap.get(segment).setEffect(INNER_SHADOW);
                    } else {
                        segmentMap.get(segment).setId("sixteen-segment-off");
                        segmentMap.get(segment).setEffect(null);
                    }
                } else {
                    segmentMap.get(segment).setId("sixteen-segment-off");
                    segmentMap.get(segment).setEffect(null);
                }
            }
        } else {
            for (SevenSegment.Segment segment : segmentMap.keySet()) {
                if (control.getCustomSegmentMapping().containsKey(ASCII)) {
                    if (control.getCustomSegmentMapping().get(ASCII).contains(segment)) {
                        segmentMap.get(segment).setId(ON_STYLE);
                        segmentMap.get(segment).setEffect(INNER_SHADOW);
                    } else {
                        segmentMap.get(segment).setId("sixteen-segment-off");
                        segmentMap.get(segment).setEffect(null);
                    }
                } else {
                    segmentMap.get(segment).setId("sixteen-segment-off");
                    segmentMap.get(segment).setEffect(null);
                }
            }
        }
        if (control.isDotOn()) {
            segmentMap.get(SevenSegment.Segment.DOT).setId(ON_STYLE);
            segmentMap.get(SevenSegment.Segment.DOT).setEffect(INNER_SHADOW);
        }
    }

    public void createSegments() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        segments.setStyle("-fx-segment-color-on: " + Util.INSTANCE.createCssColor(control.getColor()) +
                          "-fx-segment-color-off: " + Util.INSTANCE.createCssColor(Color.color(control.getColor().getRed(), control.getColor().getGreen(), control.getColor().getBlue(), 0.075)));

        segments.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        segments.getChildren().add(IBOUNDS);

        final Path A = new Path();
        A.setFillRule(FillRule.EVEN_ODD);
        A.getElements().add(new MoveTo(0.11790393013100436 * WIDTH, 0.014925373134328358 * HEIGHT));
        A.getElements().add(new LineTo(0.11790393013100436 * WIDTH, 0.01791044776119403 * HEIGHT));
        A.getElements().add(new LineTo(0.1965065502183406 * WIDTH, 0.07164179104477612 * HEIGHT));
        A.getElements().add(new LineTo(0.8122270742358079 * WIDTH, 0.07164179104477612 * HEIGHT));
        A.getElements().add(new LineTo(0.8864628820960698 * WIDTH, 0.020895522388059702 * HEIGHT));
        A.getElements().add(new LineTo(0.8864628820960698 * WIDTH, 0.01791044776119403 * HEIGHT));
        A.getElements().add(new LineTo(0.8602620087336245 * WIDTH, 0.0));
        A.getElements().add(new LineTo(0.13973799126637554 * WIDTH, 0.0));
        A.getElements().add(new LineTo(0.11790393013100436 * WIDTH, 0.014925373134328358 * HEIGHT));
        A.getElements().add(new ClosePath());
        A.setId("seven-segment-off");
        segmentMap.put(SevenSegment.Segment.A, A);

        final Path B = new Path();
        B.setFillRule(FillRule.EVEN_ODD);
        B.getElements().add(new MoveTo(0.8951965065502183 * WIDTH, 0.023880597014925373 * HEIGHT));
        B.getElements().add(new LineTo(0.9213973799126638 * WIDTH, 0.04477611940298507 * HEIGHT));
        B.getElements().add(new LineTo(0.9213973799126638 * WIDTH, 0.08358208955223881 * HEIGHT));
        B.getElements().add(new LineTo(0.8820960698689956 * WIDTH, 0.4626865671641791 * HEIGHT));
        B.getElements().add(new LineTo(0.8296943231441049 * WIDTH, 0.49850746268656715 * HEIGHT));
        B.getElements().add(new LineTo(0.777292576419214 * WIDTH, 0.4626865671641791 * HEIGHT));
        B.getElements().add(new LineTo(0.8209606986899564 * WIDTH, 0.07462686567164178 * HEIGHT));
        B.getElements().add(new LineTo(0.8951965065502183 * WIDTH, 0.023880597014925373 * HEIGHT));
        B.getElements().add(new ClosePath());
        B.setId("seven-segment-off");
        segmentMap.put(SevenSegment.Segment.B, B);

        final Path C = new Path();
        C.setFillRule(FillRule.EVEN_ODD);
        C.getElements().add(new MoveTo(0.8296943231441049 * WIDTH, 0.5014925373134328 * HEIGHT));
        C.getElements().add(new LineTo(0.8777292576419214 * WIDTH, 0.5343283582089552 * HEIGHT));
        C.getElements().add(new LineTo(0.8296943231441049 * WIDTH, 0.9671641791044776 * HEIGHT));
        C.getElements().add(new LineTo(0.8078602620087336 * WIDTH, 0.982089552238806 * HEIGHT));
        C.getElements().add(new LineTo(0.7292576419213974 * WIDTH, 0.9253731343283582 * HEIGHT));
        C.getElements().add(new LineTo(0.7685589519650655 * WIDTH, 0.5432835820895522 * HEIGHT));
        C.getElements().add(new LineTo(0.8296943231441049 * WIDTH, 0.5014925373134328 * HEIGHT));
        C.getElements().add(new ClosePath());
        C.setId("seven-segment-off");
        segmentMap.put(SevenSegment.Segment.C, C);

        final Path D = new Path();
        D.setFillRule(FillRule.EVEN_ODD);
        D.getElements().add(new MoveTo(0.7205240174672489 * WIDTH, 0.9283582089552239 * HEIGHT));
        D.getElements().add(new LineTo(0.1091703056768559 * WIDTH, 0.9283582089552239 * HEIGHT));
        D.getElements().add(new LineTo(0.039301310043668124 * WIDTH, 0.9761194029850746 * HEIGHT));
        D.getElements().add(new LineTo(0.039301310043668124 * WIDTH, 0.982089552238806 * HEIGHT));
        D.getElements().add(new LineTo(0.06550218340611354 * WIDTH, HEIGHT));
        D.getElements().add(new LineTo(0.7816593886462883 * WIDTH, HEIGHT));
        D.getElements().add(new LineTo(0.7991266375545851 * WIDTH, 0.9880597014925373 * HEIGHT));
        D.getElements().add(new LineTo(0.7991266375545851 * WIDTH, 0.982089552238806 * HEIGHT));
        D.getElements().add(new LineTo(0.7205240174672489 * WIDTH, 0.9283582089552239 * HEIGHT));
        D.getElements().add(new ClosePath());
        D.setId("seven-segment-off");
        segmentMap.put(SevenSegment.Segment.D, D);

        final Path E = new Path();
        E.setFillRule(FillRule.EVEN_ODD);
        E.getElements().add(new MoveTo(0.03056768558951965 * WIDTH, 0.9761194029850746 * HEIGHT));
        E.getElements().add(new LineTo(0.0, 0.9552238805970149 * HEIGHT));
        E.getElements().add(new LineTo(0.0, 0.9164179104477612 * HEIGHT));
        E.getElements().add(new LineTo(0.043668122270742356 * WIDTH, 0.5373134328358209 * HEIGHT));
        E.getElements().add(new LineTo(0.09606986899563319 * WIDTH, 0.5014925373134328 * HEIGHT));
        E.getElements().add(new LineTo(0.14410480349344978 * WIDTH, 0.5373134328358209 * HEIGHT));
        E.getElements().add(new LineTo(0.10043668122270742 * WIDTH, 0.9253731343283582 * HEIGHT));
        E.getElements().add(new LineTo(0.03056768558951965 * WIDTH, 0.9761194029850746 * HEIGHT));
        E.getElements().add(new ClosePath());
        E.setId("seven-segment-off");
        segmentMap.put(SevenSegment.Segment.E, E);

        final Path F = new Path();
        F.setFillRule(FillRule.EVEN_ODD);
        F.getElements().add(new MoveTo(0.1091703056768559 * WIDTH, 0.01791044776119403 * HEIGHT));
        F.getElements().add(new LineTo(0.18777292576419213 * WIDTH, 0.07462686567164178 * HEIGHT));
        F.getElements().add(new LineTo(0.15283842794759825 * WIDTH, 0.45671641791044776 * HEIGHT));
        F.getElements().add(new LineTo(0.09170305676855896 * WIDTH, 0.49850746268656715 * HEIGHT));
        F.getElements().add(new LineTo(0.043668122270742356 * WIDTH, 0.4626865671641791 * HEIGHT));
        F.getElements().add(new LineTo(0.08733624454148471 * WIDTH, 0.03283582089552239 * HEIGHT));
        F.getElements().add(new LineTo(0.1091703056768559 * WIDTH, 0.01791044776119403 * HEIGHT));
        F.getElements().add(new ClosePath());
        F.setId("seven-segment-off");
        segmentMap.put(SevenSegment.Segment.F, F);

        final Path G = new Path();
        G.setFillRule(FillRule.EVEN_ODD);
        G.getElements().add(new MoveTo(0.7729257641921398 * WIDTH, 0.5373134328358209 * HEIGHT));
        G.getElements().add(new LineTo(0.8253275109170306 * WIDTH, 0.49850746268656715 * HEIGHT));
        G.getElements().add(new LineTo(0.7685589519650655 * WIDTH, 0.4626865671641791 * HEIGHT));
        G.getElements().add(new LineTo(0.1572052401746725 * WIDTH, 0.4626865671641791 * HEIGHT));
        G.getElements().add(new LineTo(0.10043668122270742 * WIDTH, 0.49850746268656715 * HEIGHT));
        G.getElements().add(new LineTo(0.1572052401746725 * WIDTH, 0.5373134328358209 * HEIGHT));
        G.getElements().add(new LineTo(0.7729257641921398 * WIDTH, 0.5373134328358209 * HEIGHT));
        G.getElements().add(new ClosePath());
        G.setId("seven-segment-off");
        segmentMap.put(SevenSegment.Segment.G, G);

        final Circle DOT = new Circle(0.9301310043668122 * WIDTH, 0.9522388059701492 * HEIGHT, 0.06986899563318777 * WIDTH);
        DOT.setId("seven-segment-off");
        segmentMap.put(SevenSegment.Segment.DOT, DOT);

        segments.getChildren().addAll(A,
                                      B,
                                      C,
                                      D,
                                      E,
                                      F,
                                      G,
                                      DOT);
        segments.setCache(true);
    }
}

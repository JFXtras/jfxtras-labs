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
import jfxtras.labs.internal.scene.control.behavior.SixteenSegmentBehavior;
import jfxtras.labs.scene.control.gauge.SixteenSegment;
import jfxtras.labs.util.Util;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by
 * User: hansolo
 * Date: 13.03.12
 * Time: 11:27
 */
public class SixteenSegmentSkin extends SkinBase<SixteenSegment, SixteenSegmentBehavior> {
    private SixteenSegment                     control;
    private boolean                            isDirty;
    private boolean                            initialized;
    private Group                              segments;
    private Map<SixteenSegment.Segment, Shape> segmentMap;


    // ******************** Constructors **************************************
    public SixteenSegmentSkin(final SixteenSegment CONTROL) {
        super(CONTROL, new SixteenSegmentBehavior(CONTROL));
        control     = CONTROL;
        initialized = false;
        isDirty     = false;
        segments    = new Group();
        segmentMap  = new HashMap<SixteenSegment.Segment, Shape>(17);

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(40, 56);
        }

        createSegments();
        updateCharacter();

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.characterProperty(), "CHARACTER");
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.plainColorProperty(), "PLAIN_COLOR");
        registerChangeListener(control.customSegmentMappingProperty(), "CUSTOM_MAPPING");
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
            getChildren().clear();
            getChildren().add(segments);
        }
        isDirty = false;

        super.layoutChildren();
    }

    @Override public final SixteenSegment getSkinnable() {
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
        segments.setStyle("-fx-segment-color-on: " + Util.createCssColor(control.getColor()) +
                          "-fx-segment-color-off: " + Util.createCssColor(Color.color(control.getColor().getRed(), control.getColor().getGreen(), control.getColor().getBlue(), 0.075)));
        final int ASCII = control.getCharacter().isEmpty() ? 20 : control.getCharacter().toUpperCase().charAt(0);
        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setRadius(0.05 * control.getPrefWidth());
        INNER_SHADOW.setColor(Color.hsb(control.getColor().getHue(), control.getColor().getSaturation(), 0.2));

        final String ON_STYLE = control.isPlainColor() ? "sixteen-segment-plain-on" : "sixteen-segment-on";

        if (control.getCustomSegmentMapping().isEmpty()) {
            for (SixteenSegment.Segment segment : segmentMap.keySet()) {
                if (control.getSegmentMapping().containsKey(ASCII)) {
                    if (control.getSegmentMapping().get(ASCII).contains(segment)) {
                        segmentMap.get(segment).getStyleClass().clear();
                        segmentMap.get(segment).getStyleClass().add(ON_STYLE);
                        segmentMap.get(segment).setEffect(INNER_SHADOW);
                    } else {
                        segmentMap.get(segment).getStyleClass().clear();
                        segmentMap.get(segment).getStyleClass().add("sixteen-segment-off");
                        segmentMap.get(segment).setEffect(null);
                    }
                } else {
                    segmentMap.get(segment).getStyleClass().clear();
                    segmentMap.get(segment).getStyleClass().add("sixteen-segment-off");
                    segmentMap.get(segment).setEffect(null);
                }
            }
        } else {
            for (SixteenSegment.Segment segment : segmentMap.keySet()) {
                if (control.getCustomSegmentMapping().containsKey(ASCII)) {
                    if (control.getCustomSegmentMapping().get(ASCII).contains(segment)) {
                        segmentMap.get(segment).getStyleClass().clear();
                        segmentMap.get(segment).getStyleClass().add(ON_STYLE);
                        segmentMap.get(segment).setEffect(INNER_SHADOW);
                    } else {
                        segmentMap.get(segment).getStyleClass().clear();
                        segmentMap.get(segment).getStyleClass().add("sixteen-segment-off");
                        segmentMap.get(segment).setEffect(null);
                    }
                } else {
                    segmentMap.get(segment).getStyleClass().clear();
                    segmentMap.get(segment).getStyleClass().add("sixteen-segment-off");
                    segmentMap.get(segment).setEffect(null);
                }
            }
        }
        if (control.isDotOn()) {
            segmentMap.get(SixteenSegment.Segment.DOT).getStyleClass().clear();
            segmentMap.get(SixteenSegment.Segment.DOT).getStyleClass().add(ON_STYLE);
            segmentMap.get(SixteenSegment.Segment.DOT).setEffect(INNER_SHADOW);
        }
    }

    public void createSegments() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        segments.setStyle("-fx-segment-color-on: " + Util.createCssColor(control.getColor()) +
                          "-fx-segment-color-off: " + Util.createCssColor(Color.color(control.getColor().getRed(), control.getColor().getGreen(), control.getColor().getBlue(), 0.075)));

        segments.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        segments.getChildren().add(IBOUNDS);

        final Path A1 = new Path();
        A1.setFillRule(FillRule.EVEN_ODD);
        A1.getElements().add(new MoveTo(0.13389121338912133 * WIDTH, 0.028985507246376812 * HEIGHT));
        A1.getElements().add(new LineTo(0.15481171548117154 * WIDTH, 0.014492753623188406 * HEIGHT));
        A1.getElements().add(new LineTo(0.45188284518828453 * WIDTH, 0.014492753623188406 * HEIGHT));
        A1.getElements().add(new LineTo(0.497907949790795 * WIDTH, 0.043478260869565216 * HEIGHT));
        A1.getElements().add(new LineTo(0.497907949790795 * WIDTH, 0.04927536231884058 * HEIGHT));
        A1.getElements().add(new LineTo(0.4435146443514644 * WIDTH, 0.08405797101449275 * HEIGHT));
        A1.getElements().add(new LineTo(0.20920502092050208 * WIDTH, 0.08405797101449275 * HEIGHT));
        A1.getElements().add(new LineTo(0.13389121338912133 * WIDTH, 0.03188405797101449 * HEIGHT));
        A1.getElements().add(new LineTo(0.13389121338912133 * WIDTH, 0.028985507246376812 * HEIGHT));
        A1.getElements().add(new ClosePath());
        A1.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.A1, A1);

        final Path A2 = new Path();
        A2.setFillRule(FillRule.EVEN_ODD);
        A2.getElements().add(new MoveTo(0.8702928870292888 * WIDTH, 0.03188405797101449 * HEIGHT));
        A2.getElements().add(new LineTo(0.8451882845188284 * WIDTH, 0.014492753623188406 * HEIGHT));
        A2.getElements().add(new LineTo(0.5523012552301255 * WIDTH, 0.014492753623188406 * HEIGHT));
        A2.getElements().add(new LineTo(0.502092050209205 * WIDTH, 0.043478260869565216 * HEIGHT));
        A2.getElements().add(new LineTo(0.502092050209205 * WIDTH, 0.0463768115942029 * HEIGHT));
        A2.getElements().add(new LineTo(0.5564853556485355 * WIDTH, 0.08405797101449275 * HEIGHT));
        A2.getElements().add(new LineTo(0.799163179916318 * WIDTH, 0.08405797101449275 * HEIGHT));
        A2.getElements().add(new LineTo(0.8702928870292888 * WIDTH, 0.034782608695652174 * HEIGHT));
        A2.getElements().add(new LineTo(0.8702928870292888 * WIDTH, 0.03188405797101449 * HEIGHT));
        A2.getElements().add(new ClosePath());
        A2.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.A2, A2);

        final Path B = new Path();
        B.setFillRule(FillRule.EVEN_ODD);
        B.getElements().add(new MoveTo(0.8786610878661087 * WIDTH, 0.03768115942028986 * HEIGHT));
        B.getElements().add(new LineTo(0.9037656903765691 * WIDTH, 0.057971014492753624 * HEIGHT));
        B.getElements().add(new LineTo(0.9037656903765691 * WIDTH, 0.09565217391304348 * HEIGHT));
        B.getElements().add(new LineTo(0.8661087866108786 * WIDTH, 0.463768115942029 * HEIGHT));
        B.getElements().add(new LineTo(0.8158995815899581 * WIDTH, 0.4985507246376812 * HEIGHT));
        B.getElements().add(new LineTo(0.7656903765690377 * WIDTH, 0.463768115942029 * HEIGHT));
        B.getElements().add(new LineTo(0.8075313807531381 * WIDTH, 0.08695652173913043 * HEIGHT));
        B.getElements().add(new LineTo(0.8786610878661087 * WIDTH, 0.03768115942028986 * HEIGHT));
        B.getElements().add(new ClosePath());
        B.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.B, B);

        final Path C = new Path();
        C.setFillRule(FillRule.EVEN_ODD);
        C.getElements().add(new MoveTo(0.8158995815899581 * WIDTH, 0.5014492753623189 * HEIGHT));
        C.getElements().add(new LineTo(0.8619246861924686 * WIDTH, 0.5333333333333333 * HEIGHT));
        C.getElements().add(new LineTo(0.8158995815899581 * WIDTH, 0.9536231884057971 * HEIGHT));
        C.getElements().add(new LineTo(0.7949790794979079 * WIDTH, 0.9681159420289855 * HEIGHT));
        C.getElements().add(new LineTo(0.7196652719665272 * WIDTH, 0.9130434782608695 * HEIGHT));
        C.getElements().add(new LineTo(0.7573221757322176 * WIDTH, 0.5420289855072464 * HEIGHT));
        C.getElements().add(new LineTo(0.8158995815899581 * WIDTH, 0.5014492753623189 * HEIGHT));
        C.getElements().add(new ClosePath());
        C.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.C, C);

        final Path D2 = new Path();
        D2.setFillRule(FillRule.EVEN_ODD);
        D2.getElements().add(new MoveTo(0.7112970711297071 * WIDTH, 0.9159420289855073 * HEIGHT));
        D2.getElements().add(new LineTo(0.7866108786610879 * WIDTH, 0.9681159420289855 * HEIGHT));
        D2.getElements().add(new LineTo(0.7866108786610879 * WIDTH, 0.9739130434782609 * HEIGHT));
        D2.getElements().add(new LineTo(0.7698744769874477 * WIDTH, 0.9855072463768116 * HEIGHT));
        D2.getElements().add(new LineTo(0.47280334728033474 * WIDTH, 0.9855072463768116 * HEIGHT));
        D2.getElements().add(new LineTo(0.4309623430962343 * WIDTH, 0.9565217391304348 * HEIGHT));
        D2.getElements().add(new LineTo(0.4309623430962343 * WIDTH, 0.9507246376811594 * HEIGHT));
        D2.getElements().add(new LineTo(0.4811715481171548 * WIDTH, 0.9159420289855073 * HEIGHT));
        D2.getElements().add(new LineTo(0.7112970711297071 * WIDTH, 0.9159420289855073 * HEIGHT));
        D2.getElements().add(new ClosePath());
        D2.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.D2, D2);

        final Path D1 = new Path();
        D1.setFillRule(FillRule.EVEN_ODD);
        D1.getElements().add(new MoveTo(0.3682008368200837 * WIDTH, 0.9159420289855073 * HEIGHT));
        D1.getElements().add(new LineTo(0.41841004184100417 * WIDTH, 0.9507246376811594 * HEIGHT));
        D1.getElements().add(new LineTo(0.41841004184100417 * WIDTH, 0.9565217391304348 * HEIGHT));
        D1.getElements().add(new LineTo(0.3723849372384937 * WIDTH, 0.9855072463768116 * HEIGHT));
        D1.getElements().add(new LineTo(0.08368200836820083 * WIDTH, 0.9855072463768116 * HEIGHT));
        D1.getElements().add(new LineTo(0.058577405857740586 * WIDTH, 0.9681159420289855 * HEIGHT));
        D1.getElements().add(new LineTo(0.058577405857740586 * WIDTH, 0.9623188405797102 * HEIGHT));
        D1.getElements().add(new LineTo(0.12552301255230125 * WIDTH, 0.9159420289855073 * HEIGHT));
        D1.getElements().add(new LineTo(0.3682008368200837 * WIDTH, 0.9159420289855073 * HEIGHT));
        D1.getElements().add(new ClosePath());
        D1.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.D1, D1);

        final Path E = new Path();
        E.setFillRule(FillRule.EVEN_ODD);
        E.getElements().add(new MoveTo(0.0502092050209205 * WIDTH, 0.9623188405797102 * HEIGHT));
        E.getElements().add(new LineTo(0.02092050209205021 * WIDTH, 0.9420289855072463 * HEIGHT));
        E.getElements().add(new LineTo(0.02092050209205021 * WIDTH, 0.9043478260869565 * HEIGHT));
        E.getElements().add(new LineTo(0.06276150627615062 * WIDTH, 0.5362318840579711 * HEIGHT));
        E.getElements().add(new LineTo(0.11297071129707113 * WIDTH, 0.5014492753623189 * HEIGHT));
        E.getElements().add(new LineTo(0.1589958158995816 * WIDTH, 0.5362318840579711 * HEIGHT));
        E.getElements().add(new LineTo(0.11715481171548117 * WIDTH, 0.9130434782608695 * HEIGHT));
        E.getElements().add(new LineTo(0.0502092050209205 * WIDTH, 0.9623188405797102 * HEIGHT));
        E.getElements().add(new ClosePath());
        E.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.E, E);

        final Path F = new Path();
        F.setFillRule(FillRule.EVEN_ODD);
        F.getElements().add(new MoveTo(0.12552301255230125 * WIDTH, 0.03188405797101449 * HEIGHT));
        F.getElements().add(new LineTo(0.200836820083682 * WIDTH, 0.08695652173913043 * HEIGHT));
        F.getElements().add(new LineTo(0.16736401673640167 * WIDTH, 0.4579710144927536 * HEIGHT));
        F.getElements().add(new LineTo(0.1087866108786611 * WIDTH, 0.4985507246376812 * HEIGHT));
        F.getElements().add(new LineTo(0.06276150627615062 * WIDTH, 0.463768115942029 * HEIGHT));
        F.getElements().add(new LineTo(0.10460251046025104 * WIDTH, 0.0463768115942029 * HEIGHT));
        F.getElements().add(new LineTo(0.12552301255230125 * WIDTH, 0.03188405797101449 * HEIGHT));
        F.getElements().add(new ClosePath());
        F.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.F, F);

        final Path G = new Path();
        G.setFillRule(FillRule.EVEN_ODD);
        G.getElements().add(new MoveTo(0.20920502092050208 * WIDTH, 0.08695652173913043 * HEIGHT));
        G.getElements().add(new LineTo(0.2510460251046025 * WIDTH, 0.1246376811594203 * HEIGHT));
        G.getElements().add(new LineTo(0.41422594142259417 * WIDTH, 0.3536231884057971 * HEIGHT));
        G.getElements().add(new LineTo(0.45188284518828453 * WIDTH, 0.48695652173913045 * HEIGHT));
        G.getElements().add(new LineTo(0.3472803347280335 * WIDTH, 0.41739130434782606 * HEIGHT));
        G.getElements().add(new LineTo(0.19665271966527198 * WIDTH, 0.20579710144927535 * HEIGHT));
        G.getElements().add(new LineTo(0.20920502092050208 * WIDTH, 0.08695652173913043 * HEIGHT));
        G.getElements().add(new ClosePath());
        G.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.G, G);

        final Path H = new Path();
        H.setFillRule(FillRule.EVEN_ODD);
        H.getElements().add(new MoveTo(0.502092050209205 * WIDTH, 0.05507246376811594 * HEIGHT));
        H.getElements().add(new LineTo(0.5481171548117155 * WIDTH, 0.08695652173913043 * HEIGHT));
        H.getElements().add(new LineTo(0.5397489539748954 * WIDTH, 0.1565217391304348 * HEIGHT));
        H.getElements().add(new LineTo(0.5188284518828452 * WIDTH, 0.34782608695652173 * HEIGHT));
        H.getElements().add(new LineTo(0.4602510460251046 * WIDTH, 0.4956521739130435 * HEIGHT));
        H.getElements().add(new LineTo(0.41841004184100417 * WIDTH, 0.3536231884057971 * HEIGHT));
        H.getElements().add(new LineTo(0.4476987447698745 * WIDTH, 0.08695652173913043 * HEIGHT));
        H.getElements().add(new LineTo(0.502092050209205 * WIDTH, 0.05507246376811594 * HEIGHT));
        H.getElements().add(new ClosePath());
        H.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.H, H);

        final Path J = new Path();
        J.setFillRule(FillRule.EVEN_ODD);
        J.getElements().add(new MoveTo(0.799163179916318 * WIDTH, 0.08695652173913043 * HEIGHT));
        J.getElements().add(new LineTo(0.7907949790794979 * WIDTH, 0.19710144927536233 * HEIGHT));
        J.getElements().add(new LineTo(0.5941422594142259 * WIDTH, 0.4028985507246377 * HEIGHT));
        J.getElements().add(new LineTo(0.47280334728033474 * WIDTH, 0.48695652173913045 * HEIGHT));
        J.getElements().add(new LineTo(0.5313807531380753 * WIDTH, 0.3391304347826087 * HEIGHT));
        J.getElements().add(new LineTo(0.7280334728033473 * WIDTH, 0.13333333333333333 * HEIGHT));
        J.getElements().add(new LineTo(0.799163179916318 * WIDTH, 0.08695652173913043 * HEIGHT));
        J.getElements().add(new ClosePath());
        J.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.J, J);

        final Path K = new Path();
        K.setFillRule(FillRule.EVEN_ODD);
        K.getElements().add(new MoveTo(0.46443514644351463 * WIDTH, 0.4985507246376812 * HEIGHT));
        K.getElements().add(new LineTo(0.5146443514644351 * WIDTH, 0.463768115942029 * HEIGHT));
        K.getElements().add(new LineTo(0.7573221757322176 * WIDTH, 0.463768115942029 * HEIGHT));
        K.getElements().add(new LineTo(0.8117154811715481 * WIDTH, 0.4985507246376812 * HEIGHT));
        K.getElements().add(new LineTo(0.7615062761506276 * WIDTH, 0.5362318840579711 * HEIGHT));
        K.getElements().add(new LineTo(0.5188284518828452 * WIDTH, 0.5362318840579711 * HEIGHT));
        K.getElements().add(new LineTo(0.46443514644351463 * WIDTH, 0.4985507246376812 * HEIGHT));
        K.getElements().add(new ClosePath());
        K.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.K, K);

        final Path L = new Path();
        L.setFillRule(FillRule.EVEN_ODD);
        L.getElements().add(new MoveTo(0.7154811715481172 * WIDTH, 0.9130434782608695 * HEIGHT));
        L.getElements().add(new LineTo(0.7154811715481172 * WIDTH, 0.8985507246376812 * HEIGHT));
        L.getElements().add(new LineTo(0.7238493723849372 * WIDTH, 0.8144927536231884 * HEIGHT));
        L.getElements().add(new LineTo(0.7238493723849372 * WIDTH, 0.7913043478260869 * HEIGHT));
        L.getElements().add(new LineTo(0.5732217573221757 * WIDTH, 0.5826086956521739 * HEIGHT));
        L.getElements().add(new LineTo(0.47280334728033474 * WIDTH, 0.5130434782608696 * HEIGHT));
        L.getElements().add(new LineTo(0.5062761506276151 * WIDTH, 0.6405797101449275 * HEIGHT));
        L.getElements().add(new LineTo(0.6778242677824268 * WIDTH, 0.8753623188405797 * HEIGHT));
        L.getElements().add(new LineTo(0.7154811715481172 * WIDTH, 0.9130434782608695 * HEIGHT));
        L.getElements().add(new ClosePath());
        L.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.L, L);

        final Path M = new Path();
        M.setFillRule(FillRule.EVEN_ODD);
        M.getElements().add(new MoveTo(0.4225941422594142 * WIDTH, 0.9478260869565217 * HEIGHT));
        M.getElements().add(new LineTo(0.37656903765690375 * WIDTH, 0.9159420289855073 * HEIGHT));
        M.getElements().add(new LineTo(0.37656903765690375 * WIDTH, 0.881159420289855 * HEIGHT));
        M.getElements().add(new LineTo(0.401673640167364 * WIDTH, 0.6521739130434783 * HEIGHT));
        M.getElements().add(new LineTo(0.46443514644351463 * WIDTH, 0.5043478260869565 * HEIGHT));
        M.getElements().add(new LineTo(0.5062761506276151 * WIDTH, 0.6434782608695652 * HEIGHT));
        M.getElements().add(new LineTo(0.4769874476987448 * WIDTH, 0.9130434782608695 * HEIGHT));
        M.getElements().add(new LineTo(0.4225941422594142 * WIDTH, 0.9478260869565217 * HEIGHT));
        M.getElements().add(new ClosePath());
        M.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.M, M);

        final Path N = new Path();
        N.setFillRule(FillRule.EVEN_ODD);
        N.getElements().add(new MoveTo(0.12552301255230125 * WIDTH, 0.9130434782608695 * HEIGHT));
        N.getElements().add(new LineTo(0.13389121338912133 * WIDTH, 0.8 * HEIGHT));
        N.getElements().add(new LineTo(0.24267782426778242 * WIDTH, 0.6898550724637681 * HEIGHT));
        N.getElements().add(new LineTo(0.3263598326359833 * WIDTH, 0.5971014492753624 * HEIGHT));
        N.getElements().add(new LineTo(0.45188284518828453 * WIDTH, 0.5130434782608696 * HEIGHT));
        N.getElements().add(new LineTo(0.39748953974895396 * WIDTH, 0.6463768115942029 * HEIGHT));
        N.getElements().add(new LineTo(0.38493723849372385 * WIDTH, 0.6666666666666666 * HEIGHT));
        N.getElements().add(new LineTo(0.19665271966527198 * WIDTH, 0.8666666666666667 * HEIGHT));
        N.getElements().add(new LineTo(0.12552301255230125 * WIDTH, 0.9130434782608695 * HEIGHT));
        N.getElements().add(new ClosePath());
        N.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.N, N);

        final Path P = new Path();
        P.setFillRule(FillRule.EVEN_ODD);
        P.getElements().add(new MoveTo(0.11715481171548117 * WIDTH, 0.4985507246376812 * HEIGHT));
        P.getElements().add(new LineTo(0.17154811715481172 * WIDTH, 0.463768115942029 * HEIGHT));
        P.getElements().add(new LineTo(0.4100418410041841 * WIDTH, 0.463768115942029 * HEIGHT));
        P.getElements().add(new LineTo(0.4602510460251046 * WIDTH, 0.4985507246376812 * HEIGHT));
        P.getElements().add(new LineTo(0.40585774058577406 * WIDTH, 0.5362318840579711 * HEIGHT));
        P.getElements().add(new LineTo(0.17154811715481172 * WIDTH, 0.5362318840579711 * HEIGHT));
        P.getElements().add(new LineTo(0.11715481171548117 * WIDTH, 0.4985507246376812 * HEIGHT));
        P.getElements().add(new ClosePath());
        P.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.P, P);

        final Circle DOT = new Circle(0.9121338912133892 * WIDTH, 0.9391304347826087 * HEIGHT, 0.06694560669456066 * WIDTH);
        DOT.getStyleClass().add("sixteen-segment-off");
        segmentMap.put(SixteenSegment.Segment.DOT, DOT);

        segments.getChildren().addAll(A1,
                                      A2,
                                      B,
                                      C,
                                      D2,
                                      D1,
                                      E,
                                      F,
                                      G,
                                      H,
                                      J,
                                      K,
                                      L,
                                      M,
                                      N,
                                      P,
                                      DOT);
        segments.setCache(true);
    }
}

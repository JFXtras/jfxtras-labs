/**
 * SevenSegment.java
 *
 * Copyright (c) 2011-2013, JFXtras
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

package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by
 * User: hansolo
 * Date: 23.04.12
 * Time: 08:09
 */
public class SevenSegment extends Control {
    private static final String                         DEFAULT_STYLE_CLASS = "seven-segment";
    private ObjectProperty<Color>                       color;
    private BooleanProperty                             plainColor;
    private StringProperty                              character;
    private BooleanProperty                             dotOn;
    private Map<Integer, List<Segment>>                 mapping;
    private ObjectProperty<Map<Integer, List<Segment>>> customSegmentMapping;
    public static enum                                  Segment {
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        DOT
    };


    // ******************** Constructors **************************************
    public SevenSegment() {
        this(" ", Color.rgb(255, 126, 18));
    }

    public SevenSegment(final String CHARACTER) {
        this(CHARACTER, Color.rgb(255, 126, 18));
    }

    public SevenSegment(final String CHARACTER, final Color COLOR) {
        color                = new SimpleObjectProperty<Color>(COLOR);
        plainColor           = new SimpleBooleanProperty(false);
        character            = new SimpleStringProperty(CHARACTER);
        dotOn                = new SimpleBooleanProperty(false);
        mapping              = new HashMap<Integer, List<Segment>>(42);

        initMapping();
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


    // ******************** Initialization ************************************
    private void initMapping() {
        // Space
        mapping.put(20, Arrays.asList(new Segment[] {}));
        // .
        mapping.put(46, Arrays.asList(new Segment[]{Segment.DOT}));
        // 0 - 9
        mapping.put(48, Arrays.asList(new Segment[]{Segment.A, Segment.B, Segment.C, Segment.D, Segment.E, Segment.F}));
        mapping.put(49, Arrays.asList(new Segment[]{Segment.B, Segment.C}));
        mapping.put(50, Arrays.asList(new Segment[]{Segment.A, Segment.B, Segment.D, Segment.E, Segment.G}));
        mapping.put(51, Arrays.asList(new Segment[]{Segment.A, Segment.B, Segment.C, Segment.D, Segment.G}));
        mapping.put(52, Arrays.asList(new Segment[]{Segment.B, Segment.C, Segment.F, Segment.G}));
        mapping.put(53, Arrays.asList(new Segment[]{Segment.A, Segment.C, Segment.D, Segment.F, Segment.G}));
        mapping.put(54, Arrays.asList(new Segment[]{Segment.A, Segment.C, Segment.D, Segment.E, Segment.F, Segment.G}));
        mapping.put(55, Arrays.asList(new Segment[]{Segment.A, Segment.B, Segment.C}));
        mapping.put(56, Arrays.asList(new Segment[]{Segment.A, Segment.B, Segment.C, Segment.D, Segment.E, Segment.F, Segment.G}));
        mapping.put(57, Arrays.asList(new Segment[]{Segment.A, Segment.B, Segment.C, Segment.D, Segment.F, Segment.G}));
    }


    // ******************** Methods *******************************************
    public final Color getColor() {
        return color.get();
    }

    public final void setColor(final Color COLOR) {
        color.set(COLOR);
    }

    public final ObjectProperty<Color> colorProperty() {
        return color;
    }

    public final boolean isPlainColor() {
        return plainColor.get();
    }

    public final void setPlainColor(final boolean PLAIN_COLOR) {
        plainColor.set(PLAIN_COLOR);
    }

    public final BooleanProperty plainColorProperty() {
        return plainColor;
    }

    public final String getCharacter() {
        return character.get();
    }

    public final void setCharacter(final String CHARACTER) {
        character.set(CHARACTER);
    }

    public final void setCharacter(final Character CHARACTER) {
        character.set(String.valueOf(CHARACTER));
    }

    public final void setCharacter(final int CHARACTER) {
        character.set(Integer.toString(CHARACTER));
    }

    public final StringProperty characterProperty() {
        return character;
    }

    public final boolean isDotOn() {
        return dotOn.get();
    }

    public final void setDotOn(final boolean DOT_ON) {
        dotOn.set(DOT_ON);
    }

    public final BooleanProperty dotOnProperty() {
        return dotOn;
    }

    public final Map<Integer, List<Segment>> getCustomSegmentMapping() {
        if (customSegmentMapping == null) {
            customSegmentMapping = new SimpleObjectProperty<Map<Integer, List<Segment>>>(new HashMap<Integer, List<Segment>>());
        }
        return customSegmentMapping.get();
    }

    public final void setCustomSegmentMapping(final Map<Integer, List<Segment>> CUSTOM_SEGMENT_MAPPING) {
        if (customSegmentMapping == null) {
            customSegmentMapping = new SimpleObjectProperty<Map<Integer, List<Segment>>>(new HashMap<Integer, List<Segment>>());
        }
        customSegmentMapping.get().clear();
        for (int key : CUSTOM_SEGMENT_MAPPING.keySet()) {
            customSegmentMapping.get().put(key, CUSTOM_SEGMENT_MAPPING.get(key));
        }
    }

    public final ObjectProperty<Map<Integer, List<Segment>>> customSegmentMappingProperty() {
        if (customSegmentMapping == null) {
            customSegmentMapping = new SimpleObjectProperty<Map<Integer, List<Segment>>>(new HashMap<Integer, List<Segment>>());
        }
        return customSegmentMapping;
    }

    /**
     * Returns a Map that contains the default mapping from ascii integers to lcd segments.
     * The segments are defined as follows:
     *
     *         A1A1A1 A2A2A2
     *        F G    H    I B
     *        F  G   H   I  B
     *        F   G  H  I   B
     *        F    G H I    B
     *         PPPPPP KKKKKK
     *        E    N M L    C
     *        E   N  M  L   C
     *        E  N   M   L  C
     *        E N    M    L C
     *         D1D1D1 D2D2D2
     *
     * If you would like to add a $ sign (ASCII: 36) for example you should add the following code to
     * your custom segment map.
     *
     * customSegmentMapping.put(36, Arrays.asList(new SixteenSegment.Dot[] {
     *     SixteenSegment.Dot.A1,
     *     SixteenSegment.Dot.A2,
     *     SixteenSegment.Dot.F,
     *     SixteenSegment.Dot.P,
     *     SixteenSegment.Dot.K,
     *     SixteenSegment.Dot.C,
     *     SixteenSegment.Dot.D2,
     *     SixteenSegment.Dot.D1,
     *     SixteenSegment.Dot.H,
     *     SixteenSegment.Dot.M
     * }));
     *
     * @return a Map that contains the default mapping from ascii integers to segments
     */
    public final Map<Integer, List<Segment>> getSegmentMapping() {
        HashMap<Integer, List<Segment>> segmentMapping = new HashMap<Integer, List<Segment>>(42);
        for (int key : mapping.keySet()) {
            segmentMapping.put(key, mapping.get(key));
        }
        return segmentMapping;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        //final double SIZE = WIDTH < (HEIGHT) ? (WIDTH) : HEIGHT;
        double width = WIDTH <= HEIGHT ? WIDTH : HEIGHT / 1.5;
        double height = WIDTH <= HEIGHT ? WIDTH * 1.5 : HEIGHT;
        super.setPrefSize(width, height);
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}

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
 * Date: 15.03.12
 * Time: 12:14
 */
public class DotMatrixSegment extends Control {
    private static final String                         DEFAULT_STYLE_CLASS = "dot-matrix-segment";
    private ObjectProperty<Color>                       color;
    private BooleanProperty                             plainColor;
    private StringProperty                              character;
    private BooleanProperty                             dotOn;
    private Map<Integer, List<Dot>>                     mapping;
    private ObjectProperty<Map<Integer, List<Dot>>>     customDotMapping;
    public static enum                                  Dot {
        D11, D21, D31, D41, D51,
        D12, D22, D32, D42, D52,
        D13, D23, D33, D43, D53,
        D14, D24, D34, D44, D54,
        D15, D25, D35, D45, D55,
        D16, D26, D36, D46, D56,
        D17, D27, D37, D47, D57
    };


    // ******************** Constructors **************************************
    public DotMatrixSegment() {
        this(" ", Color.rgb(255, 126, 18));
    }

    public DotMatrixSegment(final String CHARACTER) {
        this(CHARACTER, Color.rgb(255, 126, 18));
    }

    public DotMatrixSegment(final String CHARACTER, final Color COLOR) {
        color                = new SimpleObjectProperty<Color>(COLOR);
        plainColor           = new SimpleBooleanProperty(false);
        character            = new SimpleStringProperty(CHARACTER);
        dotOn                = new SimpleBooleanProperty(false);
        mapping              = new HashMap<Integer, List<Dot>>(72);
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        initMapping();
        // the -fx-skin attribute in the CSS sets which Skin class is used
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    private void initMapping() {
        // Space
        mapping.put(20, Arrays.asList(new Dot[] {}));
        // * + , - . / : ; = \ _ < > #
        mapping.put(42, Arrays.asList(new Dot[]{Dot.D32, Dot.D13, Dot.D33, Dot.D53, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D35, Dot.D55, Dot.D36}));
        mapping.put(43, Arrays.asList(new Dot[]{Dot.D32, Dot.D33, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D54, Dot.D35, Dot.D36}));
        mapping.put(44, Arrays.asList(new Dot[]{Dot.D25, Dot.D35, Dot.D36, Dot.D27}));
        mapping.put(45, Arrays.asList(new Dot[]{Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D54}));
        mapping.put(46, Arrays.asList(new Dot[]{Dot.D35, Dot.D36, Dot.D45, Dot.D46}));
        mapping.put(47, Arrays.asList(new Dot[]{Dot.D52, Dot.D43, Dot.D34, Dot.D25, Dot.D16}));
        mapping.put(58, Arrays.asList(new Dot[]{Dot.D22, Dot.D32, Dot.D23, Dot.D33, Dot.D25, Dot.D35, Dot.D26, Dot.D36}));
        mapping.put(59, Arrays.asList(new Dot[]{Dot.D22, Dot.D32, Dot.D23, Dot.D33, Dot.D25, Dot.D35, Dot.D36, Dot.D27}));
        mapping.put(61, Arrays.asList(new Dot[]{Dot.D13, Dot.D23, Dot.D33, Dot.D43, Dot.D53, Dot.D15, Dot.D25, Dot.D35, Dot.D45, Dot.D55}));
        mapping.put(92, Arrays.asList(new Dot[]{Dot.D12, Dot.D23, Dot.D34, Dot.D45, Dot.D56}));
        mapping.put(95, Arrays.asList(new Dot[]{Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(60, Arrays.asList(new Dot[]{Dot.D41, Dot.D32, Dot.D23, Dot.D14, Dot.D25, Dot.D36, Dot.D47}));
        mapping.put(62, Arrays.asList(new Dot[]{Dot.D21, Dot.D32, Dot.D43, Dot.D54, Dot.D45, Dot.D36, Dot.D27}));
        mapping.put(35, Arrays.asList(new Dot[]{Dot.D21, Dot.D41, Dot.D22, Dot.D42, Dot.D13, Dot.D23, Dot.D33, Dot.D43, Dot.D53, Dot.D24, Dot.D44, Dot.D15, Dot.D25, Dot.D35, Dot.D45, Dot.D55, Dot.D26, Dot.D46, Dot.D27, Dot.D47}));
        mapping.put(34, Arrays.asList(new Dot[]{Dot.D21, Dot.D41, Dot.D22, Dot.D42, Dot.D23, Dot.D43}));
        // 0 - 9
        mapping.put(48, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D33, Dot.D53, Dot.D14, Dot.D34, Dot.D54, Dot.D15, Dot.D35, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(49, Arrays.asList(new Dot[]{Dot.D31, Dot.D22, Dot.D32, Dot.D33, Dot.D34, Dot.D35, Dot.D36, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(50, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D53, Dot.D44, Dot.D35, Dot.D26, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(51, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D53, Dot.D34, Dot.D44, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(52, Arrays.asList(new Dot[]{Dot.D14, Dot.D32, Dot.D42, Dot.D23, Dot.D41, Dot.D43, Dot.D44, Dot.D15, Dot.D25, Dot.D35, Dot.D45, Dot.D55, Dot.D46, Dot.D47}));
        mapping.put(53, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D23, Dot.D33, Dot.D43, Dot.D54, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(54, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(55, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D52, Dot.D43, Dot.D34, Dot.D35, Dot.D36, Dot.D37}));
        mapping.put(56, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(57, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D24, Dot.D34, Dot.D44, Dot.D54, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        // ? ! % $ [ ] ( ) { }
        mapping.put(63, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D53, Dot.D34, Dot.D44, Dot.D35, Dot.D37}));
        mapping.put(33, Arrays.asList(new Dot[]{Dot.D31, Dot.D32, Dot.D33, Dot.D34, Dot.D35, Dot.D37}));
        mapping.put(37, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D12, Dot.D22, Dot.D52, Dot.D43, Dot.D34, Dot.D25, Dot.D16, Dot.D46, Dot.D56, Dot.D47, Dot.D57}));
        mapping.put(36, Arrays.asList(new Dot[]{Dot.D31, Dot.D22, Dot.D32, Dot.D42, Dot.D52, Dot.D13, Dot.D33, Dot.D24, Dot.D34, Dot.D44, Dot.D35, Dot.D55, Dot.D16, Dot.D26, Dot.D36, Dot.D46, Dot.D37}));
        mapping.put(91, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D42, Dot.D43, Dot.D44, Dot.D45, Dot.D46, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(93, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D22, Dot.D23, Dot.D24, Dot.D25, Dot.D26, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(40, Arrays.asList(new Dot[]{Dot.D41, Dot.D32, Dot.D23, Dot.D24, Dot.D25, Dot.D36, Dot.D47}));
        mapping.put(41, Arrays.asList(new Dot[]{Dot.D21, Dot.D32, Dot.D43, Dot.D44, Dot.D45, Dot.D36, Dot.D27}));
        mapping.put(123, Arrays.asList(new Dot[]{Dot.D31, Dot.D41, Dot.D22, Dot.D23, Dot.D14, Dot.D25, Dot.D26, Dot.D37, Dot.D47}));
        mapping.put(125, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D42, Dot.D43, Dot.D54, Dot.D45, Dot.D46, Dot.D27, Dot.D37}));
        // A - Z
        mapping.put(65, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(66, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(67, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D14, Dot.D15, Dot.D16, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(68, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(69, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D16, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(70, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D16, Dot.D17}));
        mapping.put(71, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D14, Dot.D34, Dot.D44, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(72, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(73, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D32, Dot.D33, Dot.D34, Dot.D35, Dot.D36, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(74, Arrays.asList(new Dot[]{Dot.D41, Dot.D42, Dot.D43, Dot.D44, Dot.D15, Dot.D45, Dot.D16, Dot.D46, Dot.D27, Dot.D37}));
        mapping.put(75, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D42, Dot.D13, Dot.D33, Dot.D14, Dot.D24, Dot.D15, Dot.D35, Dot.D16, Dot.D46, Dot.D17, Dot.D57}));
        mapping.put(76, Arrays.asList(new Dot[]{Dot.D11, Dot.D12, Dot.D13, Dot.D14, Dot.D15, Dot.D16, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(77, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D22, Dot.D42, Dot.D52, Dot.D13, Dot.D33, Dot.D53, Dot.D14, Dot.D34, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(78, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D23, Dot.D53, Dot.D14, Dot.D34, Dot.D54, Dot.D15, Dot.D45, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(79, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(80, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D16, Dot.D17}));
        mapping.put(81, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D35, Dot.D55, Dot.D16, Dot.D46, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(82, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D35, Dot.D16, Dot.D46, Dot.D17, Dot.D57}));
        mapping.put(83, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D24, Dot.D34, Dot.D44, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(84, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D32, Dot.D33, Dot.D34, Dot.D35, Dot.D36, Dot.D37}));
        mapping.put(85, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(86, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D55, Dot.D26, Dot.D46, Dot.D37}));
        mapping.put(87, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D34, Dot.D54, Dot.D15, Dot.D35, Dot.D55, Dot.D16, Dot.D26, Dot.D46, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(88, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D23, Dot.D43, Dot.D34, Dot.D25, Dot.D45, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(89, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D23, Dot.D43, Dot.D34, Dot.D35, Dot.D36, Dot.D37}));
        mapping.put(90, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D52, Dot.D43, Dot.D34, Dot.D25, Dot.D16, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
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

    public final Map<Integer, List<Dot>> getCustomDotMapping() {
        if (customDotMapping == null) {
            customDotMapping = new SimpleObjectProperty<Map<Integer, List<Dot>>>(new HashMap<Integer, List<Dot>>());
        }
        return customDotMapping.get();
    }

    public final void setCustomDotMapping(final Map<Integer, List<Dot>> CUSTOM_DOT_MAPPING) {
        if (customDotMapping == null) {
            customDotMapping = new SimpleObjectProperty<Map<Integer, List<Dot>>>(new HashMap<Integer, List<Dot>>());
        }
        customDotMapping.get().clear();
        for (int key : CUSTOM_DOT_MAPPING.keySet()) {
            customDotMapping.get().put(key, CUSTOM_DOT_MAPPING.get(key));
        }
    }

    public final ObjectProperty<Map<Integer, List<Dot>>> customDotMappingProperty() {
        if (customDotMapping == null) {
            customDotMapping = new SimpleObjectProperty<Map<Integer, List<Dot>>>(new HashMap<Integer, List<Dot>>());
        }
        return customDotMapping;
    }

    /**
     * Returns a Map that contains the default mapping from ascii integers to lcd segments.
     * The segments are defined as follows:
     *
     *        D11 D21 D31 D41 D51
     *        D12 D22 D32 D42 D52
     *        D13 D23 D33 D43 D53
     *        D14 D24 D34 D44 D54
     *        D15 D25 D35 D45 D55
     *        D16 D26 D36 D46 D56
     *        D17 D27 D37 D47 D57
     *
     * If you would like to add a $ sign (ASCII: 36) for example you should add the following code to
     * your custom dot map.
     *
     * customDotMapping.put(36, Arrays.asList(new DotMatrixSegment.Dot[] {
     *     DotMatrixSegment.Dot.D11,
     *     DotMatrixSegment.Dot.A2,
     *     DotMatrixSegment.Dot.F,
     *     DotMatrixSegment.Dot.P,
     *     DotMatrixSegment.Dot.K,
     *     DotMatrixSegment.Dot.C,
     *     DotMatrixSegment.Dot.D2,
     *     DotMatrixSegment.Dot.D1,
     *     DotMatrixSegment.Dot.H,
     *     DotMatrixSegment.Dot.M
     * }));
     *
     * @return a Map that contains the default mapping from ascii integers to segments
     */
    public final Map<Integer, List<Dot>> getDotMapping() {
        HashMap<Integer, List<Dot>> dotMapping = new HashMap<Integer, List<Dot>>(42);
        for (int key : mapping.keySet()) {
            dotMapping.put(key, mapping.get(key));
        }
        return dotMapping;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double prefHeight = WIDTH < (HEIGHT * 0.7192982456140351) ? (WIDTH * 1.3902439024390243) : HEIGHT;
        double prefWidth = prefHeight * 0.7192982456140351;
        super.setPrefSize(prefWidth, prefHeight);
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}

package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

import javax.xml.bind.ValidationEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by
 * User: hansolo
 * Date: 23.02.12
 * Time: 09:11
 */
public class Flipchar extends Control {
    public enum Type {
        NUMERIC(48, 57),
        ALPHANUMERIC(45, 94),
        TIME(48, 53);

        public final int LOWER_BOUND;
        public final int UPPER_BOUND;

        private Type(final int LOWER_BOUND, final int UPPER_BOUND) {
            this.LOWER_BOUND = LOWER_BOUND;
            this.UPPER_BOUND = UPPER_BOUND;
        }
    }
    private static final String DEFAULT_STYLE_CLASS = "flipchar";
    private ObjectProperty<Color>     color;
    private ObjectProperty<Color>     characterColor;
    private ObjectProperty<Type>      type;
    private IntegerProperty           character;
    private LongProperty              flipTime;
    private IntegerProperty           flipSteps;
    private boolean                   keepAspect;


    // ******************** Constructors **************************************
    public Flipchar() {
        color          = new SimpleObjectProperty<>(Color.rgb(80, 80, 80));
        characterColor = new SimpleObjectProperty<>(Color.WHITE);
        type           = new SimpleObjectProperty<>(Type.NUMERIC);
        character      = new SimpleIntegerProperty(48);
        flipTime       = new SimpleLongProperty(500000l);
        flipSteps      = new SimpleIntegerProperty(18);
        keepAspect     = true;

        init();
    }

    private void init() {
        // the -fx-skin attribute in the CSS sets which Skin class is used
        getStyleClass().add(DEFAULT_STYLE_CLASS);
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

    public final Color getCharacterColor() {
        return characterColor.get();
    }

    public final void setCharacterColor(final Color COLOR) {
        characterColor.set(COLOR);
    }

    public final ObjectProperty<Color> characterColorProperty() {
        return characterColor;
    }

    public final Type getType() {
        return type.get();
    }

    public final void setType(final Type TYPE) {
        type.set(TYPE);
    }

    public final ObjectProperty<Type> typeProperty() {
        return type;
    }

    public final char getCharacter() {
        return (char) character.get();
    }

    public final void setCharacter(final String CHARACTER) {
        if (CHARACTER.charAt(0) == 32 || (CHARACTER.charAt(0) >= type.get().LOWER_BOUND && CHARACTER.charAt(0) <= type.get().UPPER_BOUND)) {
            character.set(CHARACTER.charAt(0));
        } else {
            character.set(32);
        }
    }

    public final IntegerProperty characterProperty() {
        return character;
    }

    public final long getFlipTime() {
        return flipTime.get();
    }

    public final void setFlipTime(final long FLIP_TIME) {
        flipTime.set(FLIP_TIME < 10000l ? 10000l : (FLIP_TIME > 3000000000l ? 3000000000l : FLIP_TIME));
    }

    public final LongProperty flipTimeProperty() {
        return flipTime;
    }

    public final int getFlipSteps() {
        return flipSteps.get();
    }

    public final void setFlipSteps(final int FLIP_STEPS) {
        flipSteps.set(FLIP_STEPS < 3 ? 3 : (FLIP_STEPS > 144 ? 144 : FLIP_STEPS));
    }

    public final IntegerProperty flipStepsProperty() {
        return flipSteps;
    }

    public final boolean isKeepAspect() {
        return keepAspect;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double prefHeight = WIDTH < (HEIGHT * 0.5925925925925926) ? (WIDTH * 1.6875) : HEIGHT;
        double prefWidth = prefHeight * 0.5925925925925926;

        if (keepAspect) {
            super.setPrefSize(prefWidth, prefHeight);
        } else {
            super.setPrefSize(WIDTH, HEIGHT);
        }
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}

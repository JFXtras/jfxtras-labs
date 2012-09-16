package jfxtras.labs.scene.control;

import javafx.beans.property.*;
import javafx.scene.control.ControlBuilder;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

import java.util.HashMap;

/**
 * Represents the Slide lock control's builder (pattern) for convenient object creation.
 *
 * @see jfxtras.labs.scene.control.SlideLock
 *
 * @author cdea
 * @author hansolo
 */
public class SlideLockBuilder<B extends SlideLockBuilder<B>> extends ControlBuilder<B> implements Builder<SlideLock> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected SlideLockBuilder() {
    }


    // ******************** Methods *******************************************
    public final static SlideLockBuilder create() {
        return new SlideLockBuilder();
    }

    public final SlideLockBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
        properties.put("BACKGROUND_VISIBLE", new SimpleBooleanProperty(BACKGROUND_VISIBLE));
        return this;
    }

    public final SlideLockBuilder locked(final boolean LOCKED) {
        properties.put("LOCKED", new SimpleBooleanProperty(LOCKED));
        return this;
    }

    public final SlideLockBuilder text(final String TEXT) {
        properties.put("TEXT", new SimpleStringProperty(TEXT));
        return this;
    }

    public final SlideLockBuilder textOpacity(final double TEXT_OPACITY) {
        properties.put("TEXT_OPACITY", new SimpleDoubleProperty(TEXT_OPACITY));
        return this;
    }

    public final SlideLockBuilder buttonArrowBackgroundColor(final Paint COLOR) {
        properties.put("BUTTON_ARROW_BG_COLOR", new SimpleObjectProperty(COLOR));
        return this;
    }

    public final SlideLockBuilder buttonColor(final Paint COLOR) {
        properties.put("BUTTON_COLOR", new SimpleObjectProperty(COLOR));
        return this;
    }

    public final SlideLockBuilder buttonGlareVisible(final boolean VISIBLE) {
        properties.put("BUTTON_GLARE", new SimpleBooleanProperty(VISIBLE));
        return this;
    }

    @Override public final B prefWidth(final double PREF_WIDTH) {
        properties.put("PREF_WIDTH", new SimpleDoubleProperty(PREF_WIDTH));
        return (B) this;
    }

    @Override public final B prefHeight(final double PREF_HEIGHT) {
        properties.put("PREF_HEIGHT", new SimpleDoubleProperty(PREF_HEIGHT));
        return (B) this;
    }

    @Override public final B layoutX(final double LAYOUT_X) {
        properties.put("LAYOUT_X", new SimpleDoubleProperty(LAYOUT_X));
        return (B) this;
    }

    @Override public final B layoutY(final double LAYOUT_Y) {
        properties.put("LAYOUT_Y", new SimpleDoubleProperty(LAYOUT_Y));
        return (B) this;
    }

    @Override public final SlideLock build() {
        final SlideLock CONTROL = new SlideLock();
        for (String key : properties.keySet()) {
            if ("LOCKED".equals(key)) {
                CONTROL.setLocked(((BooleanProperty) properties.get(key)).get());
            } else if ("BACKGROUND_VISIBLE".equals(key)) {
                CONTROL.setBackgroundVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("TEXT".equals(key)) {
                CONTROL.setText(((StringProperty) properties.get(key)).get());
            } else if ("TEXT_OPACITY".equals(key)) {
                CONTROL.setTextOpacity(((DoubleProperty) properties.get(key)).get());
            } else if ("PREF_WIDTH".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("PREF_HEIGHT".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            } else if ("LAYOUT_X".equals(key)) {
                CONTROL.setLayoutX(((DoubleProperty) properties.get(key)).get());
            } else if ("LAYOUT_Y".equals(key)) {
                CONTROL.setLayoutY(((DoubleProperty) properties.get(key)).get());
            } else if ("BUTTON_ARROW_BG_COLOR".equals(key)) {
                CONTROL.setButtonArrowBackgroundColor(((ObjectProperty<Paint>) properties.get(key)).get());
            } else if ("BUTTON_COLOR".equals(key)) {
                CONTROL.setButtonColor(((ObjectProperty<Paint>) properties.get(key)).get());
            } else if ("BUTTON_GLARE".equals(key)) {
                CONTROL.setButtonGlareVisible(((BooleanProperty) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
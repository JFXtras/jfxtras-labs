package jfxtras.labs.internal.scene.control.skin;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import jfxtras.labs.internal.scene.control.behavior.FlipcharBehavior;
import jfxtras.labs.scene.control.gauge.Flipchar;


/**
 * Created by
 * User: hansolo
 * Date: 23.02.12
 * Time: 09:12
 */
public class FlipcharSkin extends SkinBase<Flipchar, FlipcharBehavior> {
    private Flipchar       control;
    private boolean        isDirty;
    private boolean        initialized;
    private Group          fixture;
    private Group          flip;
    private Color          bright;
    private Color          dark;
    private Color          textColor;
    private Path           upper;
    private Text           upperText;
    private Path           upperNext;
    private Text           upperNextText;
    private Path           lower;
    private Text           lowerText;
    private Text           lowerNextText;
    private char           currentChar;
    private char           nextChar;
    private Rotate         rotate;
    private Rotate         lowerFlipVert;
    private double         flipSteps;
    private double         flipStepInterval;
    private double         angleStep;
    private double         currentAngle;
    private boolean        flipping;
    private AnimationTimer timer;
    private long           lastTimerCall;


    // ******************** Constructors **************************************
    public FlipcharSkin(final Flipchar CONTROL) {
        super(CONTROL, new FlipcharBehavior(CONTROL));
        control          = CONTROL;
        initialized      = false;
        isDirty          = false;
        fixture          = new Group();
        flip             = new Group();
        dark             = control.getColor().darker().darker();
        bright           = control.getColor().brighter();
        textColor        = control.getCharacterColor();
        upperText        = new Text(Character.toString(control.getCharacter()));
        lowerText        = new Text(Character.toString(control.getCharacter()));
        upperNextText    = new Text(Character.toString((char) (control.getCharacter() + 1)));
        lowerNextText    = new Text(Character.toString((char) (control.getCharacter() + 1)));
        currentChar      = control.getCharacter();
        nextChar         = (char) (control.getCharacter() + 1);
        rotate           = new Rotate();
        flipSteps        = control.getFlipSteps();
        flipStepInterval = control.getFlipTime() / control.getFlipSteps();
        angleStep        = 180.0 / flipSteps;
        currentAngle     = 0;
        flipping         = false;
        timer            = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastTimerCall + flipStepInterval) {
                    if (initialized) {
                        doTheFlip(angleStep);
                    }
                    lastTimerCall = currentNanoTime;
                }
            }
        };
        lastTimerCall    = 0l;

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(112, 189);
        }

        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        rotate.setAxis(Rotate.X_AXIS);
        rotate.setPivotY(control.getPrefHeight() / 2);

        lowerFlipVert = new Rotate();

        // Register listeners
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.characterColorProperty(), "CHARACTER_COLOR");
        registerChangeListener(control.characterProperty(), "CHARACTER");
        registerChangeListener(control.flipTimeProperty(), "FLIP_TIME");
        registerChangeListener(control.flipStepsProperty(), "FLIP_STEPS");

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawFixture();
        drawFlip();

        getChildren().addAll(fixture,
                             flip);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "COLOR") {
            dark = control.getColor().darker().darker();
            bright = control.getColor().brighter();
            paint();
        } else if (PROPERTY == "CHARACTER_COLOR") {
            textColor = control.getCharacterColor();
            paint();
        } else if (PROPERTY == "CHARACTER") {
            if (control.getCharacter() != currentChar) {
                timer.stop();
                flipping = true;
                timer.start();
            }
        } else if (PROPERTY == "FLIP_TIME") {
            flipStepInterval = control.getFlipTime() / control.getFlipSteps();
        } else if (PROPERTY == "FLIP_STEPS") {
            flipSteps = control.getFlipSteps();
            flipStepInterval = control.getFlipTime() / control.getFlipSteps();
        }
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public final Flipchar getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 112;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 189;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    private void doTheFlip(final double ANGLE) {
        currentAngle += ANGLE;
        if (Double.compare(currentAngle, 180) >= 0) {
            currentAngle = 0;
            upper.getTransforms().clear();
            upperText.getTransforms().clear();
            lowerNextText.getTransforms().clear();
            lowerNextText.setVisible(false);
            lowerFlipVert.setAxis(Rotate.X_AXIS);
            lowerFlipVert.setPivotY(control.getPrefHeight() * 0.07 + lowerNextText.getLayoutBounds().getHeight() / 2);
            lowerFlipVert.setAngle(180);
            lowerNextText.getTransforms().add(lowerFlipVert);
            upperText.setVisible(true);

            currentChar++;
            if (currentChar > control.getType().UPPER_BOUND) {
                currentChar = (char) control.getType().LOWER_BOUND;
            }
            nextChar = (char) (currentChar + 1);
            if (nextChar > control.getType().UPPER_BOUND) {
                nextChar = (char) control.getType().LOWER_BOUND;
            }
            if (currentChar == control.getCharacter()) {
                timer.stop();
                flipping = false;
            }
            upperText.setText(Character.toString(currentChar));
            lowerText.setText(Character.toString(currentChar));
            upperNextText.setText(Character.toString(nextChar));
            lowerNextText.setText(Character.toString(nextChar));
        }
        if (currentAngle > 90) {
            upperText.setVisible(false);
            lowerNextText.setVisible(true);
        }
        if (flipping) {
            rotate.setAngle(ANGLE);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
        }
    }

    // ******************** Drawing related ***********************************
    public final void drawFixture() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        fixture.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        fixture.getChildren().add(IBOUNDS);

        final Rectangle RIGHTFRAME = new Rectangle(0.9196428571428571 * WIDTH, 0.41798941798941797 * HEIGHT,
                                                   0.08035714285714286 * WIDTH, 0.164021164021164 * HEIGHT);
        final Paint RIGHTFRAME_FILL = new LinearGradient(0.9642857142857143 * WIDTH, 0.41798941798941797 * HEIGHT,
                                                         0.9642857142857143 * WIDTH, 0.582010582010582 * HEIGHT,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                         new Stop(0.18, Color.color(0.6117647059, 0.6117647059, 0.6117647059, 1)),
                                                         new Stop(0.65, Color.color(0.1843137255, 0.1843137255, 0.1843137255, 1)),
                                                         new Stop(0.89, Color.color(0.3294117647, 0.3372549020, 0.3333333333, 1)),
                                                         new Stop(1.0, Color.color(0.2156862745, 0.2156862745, 0.2156862745, 1)));
        RIGHTFRAME.setFill(RIGHTFRAME_FILL);
        RIGHTFRAME.setStroke(null);

        final Rectangle RIGHTMAIN = new Rectangle(0.9285714285714286 * WIDTH, 0.42328042328042326 * HEIGHT,
                                                  0.0625 * WIDTH, 0.15343915343915343 * HEIGHT);
        final Paint RIGHTMAIN_FILL = new LinearGradient(0.9642857142857143 * WIDTH, 0.42328042328042326 * HEIGHT,
                                                        0.9642857142857143 * WIDTH, 0.5767195767195767 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.4549019608, 0.4549019608, 0.4549019608, 1)),
                                                        new Stop(0.13, Color.color(0.8352941176, 0.8352941176, 0.8352941176, 1)),
                                                        new Stop(0.66, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                        new Stop(0.73, Color.color(0.2509803922, 0.2509803922, 0.2509803922, 1)),
                                                        new Stop(0.9, Color.color(0.4274509804, 0.4274509804, 0.4274509804, 1)),
                                                        new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)));
        RIGHTMAIN.setFill(RIGHTMAIN_FILL);
        RIGHTMAIN.setStroke(null);

        final Rectangle LEFTFRAME = new Rectangle(0.0, 0.41798941798941797 * HEIGHT,
                                                  0.08035714285714286 * WIDTH, 0.164021164021164 * HEIGHT);
        final Paint LEFTFRAME_FILL = new LinearGradient(0.044642857142857144 * WIDTH, 0.41798941798941797 * HEIGHT,
                                                        0.04464285714285716 * WIDTH, 0.582010582010582 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                        new Stop(0.18, Color.color(0.6117647059, 0.6117647059, 0.6117647059, 1)),
                                                        new Stop(0.65, Color.color(0.1843137255, 0.1843137255, 0.1843137255, 1)),
                                                        new Stop(0.89, Color.color(0.3294117647, 0.3372549020, 0.3333333333, 1)),
                                                        new Stop(1.0, Color.color(0.2156862745, 0.2156862745, 0.2156862745, 1)));
        LEFTFRAME.setFill(LEFTFRAME_FILL);
        LEFTFRAME.setStroke(null);

        final Rectangle LEFTMAIN = new Rectangle(0.008928571428571428 * WIDTH, 0.42328042328042326 * HEIGHT,
                                                 0.0625 * WIDTH, 0.15343915343915343 * HEIGHT);
        final Paint LEFTMAIN_FILL = new LinearGradient(0.044642857142857144 * WIDTH, 0.42328042328042326 * HEIGHT,
                                                       0.04464285714285716 * WIDTH, 0.5767195767195767 * HEIGHT,
                                                       false, CycleMethod.NO_CYCLE,
                                                       new Stop(0.0, Color.color(0.4549019608, 0.4549019608, 0.4549019608, 1)),
                                                       new Stop(0.13, Color.color(0.8352941176, 0.8352941176, 0.8352941176, 1)),
                                                       new Stop(0.66, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                       new Stop(0.73, Color.color(0.2509803922, 0.2509803922, 0.2509803922, 1)),
                                                       new Stop(0.9, Color.color(0.4274509804, 0.4274509804, 0.4274509804, 1)),
                                                       new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)));
        LEFTMAIN.setFill(LEFTMAIN_FILL);
        LEFTMAIN.setStroke(null);

        fixture.getChildren().addAll(RIGHTFRAME,
                                     RIGHTMAIN,
                                     LEFTFRAME,
                                     LEFTMAIN);
    }

    public final void drawFlip() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        flip.getChildren().clear();

        lower = new Path();
        lower.setFillRule(FillRule.EVEN_ODD);
        lower.getElements().add(new MoveTo(0.9196428571428571 * WIDTH, HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.9642857142857143 * WIDTH, HEIGHT,
                                                 WIDTH, 0.9735449735449735 * HEIGHT,
                                                 WIDTH, 0.9523809523809523 * HEIGHT));
        lower.getElements().add(new CubicCurveTo(WIDTH, 0.9523809523809523 * HEIGHT,
                                                 WIDTH, 0.5925925925925926 * HEIGHT,
                                                 WIDTH, 0.5925925925925926 * HEIGHT));
        lower.getElements().add(new LineTo(0.9017857142857143 * WIDTH, 0.5925925925925926 * HEIGHT));
        lower.getElements().add(new LineTo(0.9017857142857143 * WIDTH, 0.5079365079365079 * HEIGHT));
        lower.getElements().add(new LineTo(0.09821428571428571 * WIDTH, 0.5079365079365079 * HEIGHT));
        lower.getElements().add(new LineTo(0.09821428571428571 * WIDTH, 0.5925925925925926 * HEIGHT));
        lower.getElements().add(new LineTo(0.0, 0.5925925925925926 * HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.0, 0.5925925925925926 * HEIGHT,
                                                 0.0, 0.9523809523809523 * HEIGHT,
                                                 0.0, 0.9523809523809523 * HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.0, 0.9735449735449735 * HEIGHT,
                                                 0.03571428571428571 * WIDTH, HEIGHT,
                                                 0.08035714285714286 * WIDTH, HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.08035714285714286 * WIDTH, HEIGHT,
                                                 0.9196428571428571 * WIDTH, HEIGHT,
                                                 0.9196428571428571 * WIDTH, HEIGHT));
        lower.getElements().add(new ClosePath());
        final Paint LOWER_FILL = new LinearGradient(0.5342465753424658 * WIDTH, 0.5079365079365079 * HEIGHT,
                                                    0.5342465753424658 * WIDTH, 0.9947089947089947 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, bright),
                                                    new Stop(1.0, dark));
        lower.setFill(LOWER_FILL);
        lower.setStroke(null);

        final InnerShadow LOWER_INNER_SHADOW0 = new InnerShadow();
        LOWER_INNER_SHADOW0.setWidth(0.075 * lower.getLayoutBounds().getWidth());
        LOWER_INNER_SHADOW0.setHeight(0.075 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW0.setOffsetX(0.0);
        LOWER_INNER_SHADOW0.setOffsetY(0.0);
        LOWER_INNER_SHADOW0.setRadius(0.075 * lower.getLayoutBounds().getWidth());
        LOWER_INNER_SHADOW0.setColor(Color.BLACK);
        LOWER_INNER_SHADOW0.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow LOWER_INNER_SHADOW1 = new InnerShadow();
        LOWER_INNER_SHADOW1.setWidth(0.05 * lower.getLayoutBounds().getWidth());
        LOWER_INNER_SHADOW1.setHeight(0.05 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW1.setOffsetX(0);
        LOWER_INNER_SHADOW1.setOffsetY(0.018 * SIZE);
        LOWER_INNER_SHADOW1.setRadius(0.05 * lower.getLayoutBounds().getWidth());
        LOWER_INNER_SHADOW1.setColor(Color.WHITE);
        LOWER_INNER_SHADOW1.setBlurType(BlurType.GAUSSIAN);
        LOWER_INNER_SHADOW1.inputProperty().set(LOWER_INNER_SHADOW0);
        lower.setEffect(LOWER_INNER_SHADOW1);

        upper = new Path();
        upper.setFillRule(FillRule.EVEN_ODD);
        upper.getElements().add(new MoveTo(0.9196428571428571 * WIDTH, 0.0));
        upper.getElements().add(new CubicCurveTo(0.9642857142857143 * WIDTH, 0.0,
                                                 WIDTH, 0.026455026455026454 * HEIGHT,
                                                 WIDTH, 0.047619047619047616 * HEIGHT));
        upper.getElements().add(new CubicCurveTo(WIDTH, 0.047619047619047616 * HEIGHT,
                                                 WIDTH, 0.4074074074074074 * HEIGHT,
                                                 WIDTH, 0.4074074074074074 * HEIGHT));
        upper.getElements().add(new LineTo(0.9017857142857143 * WIDTH, 0.4074074074074074 * HEIGHT));
        upper.getElements().add(new LineTo(0.9017857142857143 * WIDTH, 0.49206349206349204 * HEIGHT));
        upper.getElements().add(new LineTo(0.09821428571428571 * WIDTH, 0.49206349206349204 * HEIGHT));
        upper.getElements().add(new LineTo(0.09821428571428571 * WIDTH, 0.4074074074074074 * HEIGHT));
        upper.getElements().add(new LineTo(0.0, 0.4074074074074074 * HEIGHT));
        upper.getElements().add(new CubicCurveTo(0.0, 0.4074074074074074 * HEIGHT,
                                                 0.0, 0.047619047619047616 * HEIGHT,
                                                 0.0, 0.047619047619047616 * HEIGHT));
        upper.getElements().add(new CubicCurveTo(0.0, 0.026455026455026454 * HEIGHT,
                                                 0.03571428571428571 * WIDTH, 0.0,
                                                 0.08035714285714286 * WIDTH, 0.0));
        upper.getElements().add(new CubicCurveTo(0.08035714285714286 * WIDTH, 0.0,
                                                 0.9196428571428571 * WIDTH, 0.0,
                                                 0.9196428571428571 * WIDTH, 0.0));
        upper.getElements().add(new ClosePath());
        final Paint UPPER_FILL = new LinearGradient(0.5205479452054794 * WIDTH, 0.0,
                                                    0.5205479452054794 * WIDTH, 0.49206349206349204 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, dark),
                                                    new Stop(1.0, bright));
        upper.setFill(UPPER_FILL);
        upper.setStroke(null);

        final InnerShadow UPPER_INNER_SHADOW0 = new InnerShadow();
        UPPER_INNER_SHADOW0.setWidth(0.075 * upper.getLayoutBounds().getWidth());
        UPPER_INNER_SHADOW0.setHeight(0.075 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW0.setOffsetX(0.0);
        UPPER_INNER_SHADOW0.setOffsetY(0.0);
        UPPER_INNER_SHADOW0.setRadius(0.075 * upper.getLayoutBounds().getWidth());
        UPPER_INNER_SHADOW0.setColor(Color.BLACK);
        UPPER_INNER_SHADOW0.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow UPPER_INNER_SHADOW1 = new InnerShadow();
        UPPER_INNER_SHADOW1.setWidth(0.05 * upper.getLayoutBounds().getWidth());
        UPPER_INNER_SHADOW1.setHeight(0.05 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW1.setOffsetX(0);
        UPPER_INNER_SHADOW1.setOffsetY(0.018 * SIZE);
        UPPER_INNER_SHADOW1.setRadius(0.05 * upper.getLayoutBounds().getWidth());
        UPPER_INNER_SHADOW1.setColor(Color.WHITE);
        UPPER_INNER_SHADOW1.setBlurType(BlurType.GAUSSIAN);
        UPPER_INNER_SHADOW1.inputProperty().set(UPPER_INNER_SHADOW0);
        upper.setEffect(UPPER_INNER_SHADOW1);

        Font font = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/droidsansmono.ttf"), (0.74 * HEIGHT));

        Rectangle upperClip = new Rectangle(0, 0, WIDTH, upper.getLayoutBounds().getHeight());
        upperText.setTextOrigin(VPos.BOTTOM);
        upperText.setFont(font);
        upperText.setFontSmoothingType(FontSmoothingType.LCD);
        upperText.setText(Character.toString(control.getCharacter()));
        upperText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        upperText.setY(HEIGHT * 0.07 + upperText.getLayoutBounds().getHeight());
        upperText.setClip(upperClip);
        LinearGradient upperTextFill = new LinearGradient(0.0, upperText.getLayoutBounds().getMinY(),
                                                          0.0, upperText.getLayoutBounds().getMaxY(),
                                                          false, CycleMethod.NO_CYCLE,
                                                          new Stop(0.0, textColor.darker()),
                                                          new Stop(0.5, textColor));
        upperText.setFill(upperTextFill);
        upperText.setStroke(null);

        Rectangle lowerClip = new Rectangle(0, lower.getLayoutBounds().getMinY(), WIDTH, HEIGHT / 2);
        lowerText.setTextOrigin(VPos.BOTTOM);
        lowerText.setFont(font);
        lowerText.setFontSmoothingType(FontSmoothingType.LCD);
        lowerText.setText(Character.toString(control.getCharacter()));
        lowerText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        lowerText.setY(HEIGHT * 0.07 + upperText.getLayoutBounds().getHeight());
        lowerText.setClip(lowerClip);
        LinearGradient lowerTextFill = new LinearGradient(0.0, lowerText.getLayoutBounds().getMinY(),
                                                          0.0, lowerText.getLayoutBounds().getMaxY(),
                                                          false, CycleMethod.NO_CYCLE,
                                                          new Stop(0.5, textColor),
                                                          new Stop(1.0, textColor.darker()));
        lowerText.setFill(lowerTextFill);
        lowerText.setStroke(null);

        upperNext = new Path();
        upperNext.setFillRule(FillRule.EVEN_ODD);
        upperNext.getElements().add(new MoveTo(0.9196428571428571 * WIDTH, 0.0));
        upperNext.getElements().add(new CubicCurveTo(0.9642857142857143 * WIDTH, 0.0,
                                                     WIDTH, 0.026455026455026454 * HEIGHT,
                                                     WIDTH, 0.047619047619047616 * HEIGHT));
        upperNext.getElements().add(new CubicCurveTo(WIDTH, 0.047619047619047616 * HEIGHT,
                                                     WIDTH, 0.4074074074074074 * HEIGHT,
                                                     WIDTH, 0.4074074074074074 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.9017857142857143 * WIDTH, 0.4074074074074074 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.9017857142857143 * WIDTH, 0.49206349206349204 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.09821428571428571 * WIDTH, 0.49206349206349204 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.09821428571428571 * WIDTH, 0.4074074074074074 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.0, 0.4074074074074074 * HEIGHT));
        upperNext.getElements().add(new CubicCurveTo(0.0, 0.4074074074074074 * HEIGHT,
                                                     0.0, 0.047619047619047616 * HEIGHT,
                                                     0.0, 0.047619047619047616 * HEIGHT));
        upperNext.getElements().add(new CubicCurveTo(0.0, 0.026455026455026454 * HEIGHT,
                                                     0.03571428571428571 * WIDTH, 0.0,
                                                     0.08035714285714286 * WIDTH, 0.0));
        upperNext.getElements().add(new CubicCurveTo(0.08035714285714286 * WIDTH, 0.0,
                                                     0.9196428571428571 * WIDTH, 0.0,
                                                     0.9196428571428571 * WIDTH, 0.0));
        upperNext.getElements().add(new ClosePath());
        final Paint UPPER_NEXT_FILL = new LinearGradient(0.5205479452054794 * WIDTH, 0.0,
                                                         0.5205479452054794 * WIDTH, 0.49206349206349204 * HEIGHT,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, dark),
                                                         new Stop(1.0, bright));
        upperNext.setFill(UPPER_NEXT_FILL);
        upperNext.setStroke(null);
        upperNext.setEffect(UPPER_INNER_SHADOW1);

        Rectangle upperNextClip = new Rectangle(0, 0, WIDTH, upper.getLayoutBounds().getHeight());
        upperNextText.setTextOrigin(VPos.BOTTOM);
        upperNextText.setFont(font);
        upperNextText.setFontSmoothingType(FontSmoothingType.LCD);
        upperNextText.setText(Character.toString((char)(control.getCharacter() + 1)));
        upperNextText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        upperNextText.setY(HEIGHT * 0.07 + upperText.getLayoutBounds().getHeight());
        upperNextText.setClip(upperNextClip);
        LinearGradient upperNextTextFill = new LinearGradient(0.0, upperNextText.getLayoutBounds().getMinY(),
                                                              0.0, upperNextText.getLayoutBounds().getMaxY(),
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, textColor.darker()),
                                                              new Stop(0.5, textColor));
        upperNextText.setFill(upperNextTextFill);
        upperNextText.setStroke(null);

        Rectangle lowerNextClip = new Rectangle(0, lower.getLayoutBounds().getMinY(), WIDTH, HEIGHT / 2);
        lowerNextText.setTextOrigin(VPos.BOTTOM);
        lowerNextText.setFont(font);
        lowerNextText.setFontSmoothingType(FontSmoothingType.LCD);
        lowerNextText.setText(Character.toString((char)(control.getCharacter() + 1)));
        lowerNextText.setX(((WIDTH - lowerNextText.getLayoutBounds().getWidth()) / 2.0));
        lowerNextText.setY(HEIGHT * 0.07 + lowerNextText.getLayoutBounds().getHeight());
        lowerNextText.setClip(lowerNextClip);
        LinearGradient lowerNextTextFill = new LinearGradient(0.0, lowerNextText.getLayoutBounds().getMinY(),
                                                              0.0, lowerNextText.getLayoutBounds().getMaxY(),
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, textColor.darker()),
                                                              new Stop(0.5, textColor));
        lowerNextText.setFill(lowerNextTextFill);
        lowerNextText.setStroke(null);
        lowerNextText.setVisible(false);
        lowerFlipVert = new Rotate();
        lowerFlipVert.setAxis(Rotate.X_AXIS);
        lowerFlipVert.setPivotY(HEIGHT * 0.07 + lowerNextText.getLayoutBounds().getHeight() / 2);
        lowerFlipVert.setAngle(180);
        lowerNextText.getTransforms().add(lowerFlipVert);

        flip.getChildren().addAll(lower,
                                  lowerText,
                                  upperNext,
                                  upperNextText,
                                  upper,
                                  upperText,
                                  lowerNextText
                                  );
    }
}

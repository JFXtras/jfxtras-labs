/**
 * Odometer.java
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

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;


/**
 * @author hansolo
 * @author Unai Vivi
 * @version 2.2-r6
 * @since 2012-03-16
 */
public class Odometer extends Control {
    private static final String   DEFAULT_STYLE_CLASS = "odometer";
    private ObjectProperty<Color> color;
    private ObjectProperty<Color> decimalColor;
    private ObjectProperty<Color> numberColor;
    private ObjectProperty<Color> numberDecimalColor;
    private LongProperty          interval;
    private IntegerProperty       rotations;
    private IntegerProperty       rotationPreset;
    private BooleanProperty       countdownMode;
    private IntegerProperty       noOfDigits;
    private IntegerProperty       noOfDecimals;
    private long                  lastTimerCall;
    private AnimationTimer        timer;


    // ******************** Constructors **************************************
    public Odometer() {
        this(6);
    }

    public Odometer(final int NO_OF_DIGITS) {
        color              = new SimpleObjectProperty<>(Color.rgb(240, 240, 240));
        decimalColor       = new SimpleObjectProperty<>(Color.rgb(220, 0, 0));
        numberColor        = new SimpleObjectProperty<>(Color.BLACK);
        numberDecimalColor = new SimpleObjectProperty<>(Color.WHITE);
        noOfDigits         = new SimpleIntegerProperty(NO_OF_DIGITS < 0 ? 1 : NO_OF_DIGITS);
        noOfDecimals       = new SimpleIntegerProperty(0);
        interval           = new SimpleLongProperty(1000);
        rotations          = new SimpleIntegerProperty(0);
        rotationPreset     = new SimpleIntegerProperty(0);
        countdownMode      = new SimpleBooleanProperty(false);
        lastTimerCall      = System.nanoTime();
        timer              = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > (lastTimerCall + interval.get() * 1000000)) {
                    increment();
                    lastTimerCall = currentNanoTime;
                }
            }
        };

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

    public final Color getDecimalColor() {
        return decimalColor.get();
    }

    public final void setDecimalColor(final Color DECIMAL_COLOR) {
        decimalColor.set(DECIMAL_COLOR);
    }

    public final ObjectProperty<Color> decimalColorProperty() {
        return decimalColor;
    }

    public final Color getNumberColor() {
        return numberColor.get();
    }

    public final void setNumberColor(final Color COLOR) {
        numberColor.set(COLOR);
    }

    public final ObjectProperty<Color> numberColorProperty() {
        return numberColor;
    }

    public final Color getNumberDecimalColor() {
        return numberDecimalColor.get();
    }

    public final void setNumberDecimalColor(final Color NUMBER_DECIMAL_COLOR) {
        numberDecimalColor.set(NUMBER_DECIMAL_COLOR);
    }

    public final ObjectProperty<Color> numberDecimalColorProperty() {
        return numberDecimalColor;
    }

    public final long getInterval() {
        return interval.get();
    }

    public final void setInterval(final long INTERVAL) {
        interval.set(INTERVAL < 100 ? 100 : (INTERVAL > 5000 ? 5000 : INTERVAL));
    }

    public final LongProperty intervalProperty() {
        return interval;
    }

    public final int getRotations() {
        return rotations.get();
    }

    /**
     * Invoke to set the {@link Odometer}'s digits to the numerical value specified in <code>ROTATIONS</code>.
     * If the <i>n</i> rightmost digits of the constructed are decimals, then the <i>n</i>
     * rightmost digits in <code>ROTATIONS</code> will be considered as such.
     * <hr>
     * <b>Usage example:</b>
     * <pre>
     * final Odometer o = OdometerBuilder.create()
     *              .noOfDecimals(1)
     *              .noOfDigits(7)
     *              .color(Color.DARKSLATEGREY)
     *              .numberColor(Color.WHITE)
     *              .interval(100).build();
     * for (int i = 0; i < 200; i++)
     * {
     *     final int sWait = i*2;
     *     Task<Void> t = new Task<Void>()
     *     {
     *         protected Void call()
     *         {
     *             try{Thread.sleep(sWait);}
     *             catch(InterruptedException ie){}
     *             Platform.runLater(new Runnable()
     *                 {
     *                     public void run()
     *                     {
     *                         o.setRotations((int) (Math.random() * 100_000_000));
     *                     }
     *                 }
     *         }
     *     };
     *     Thread th = new Thread(t, "number instance");
     *     th.setDaemon(true);
     *     th.start();
     * }
     * </pre>
     * <hr>
     * @param ROTATIONS New numerical value for the {@link Odometer}
     */
    public final void setRotations(final int ROTATIONS) {
        rotations.set(ROTATIONS);
    }

    public final ReadOnlyIntegerProperty rotationsProperty() {
        return rotations;
    }

    public final int getRotationPreset() {
        return rotationPreset.get();
    }

    public final void setRotationPreset(final int ROTATION_PRESET) {
        rotationPreset.set(ROTATION_PRESET);
    }

    public final IntegerProperty rotationPresetProperty() {
        return rotationPreset;
    }

    public final void start() {
        timer.start();
    }

    public final void stop() {
        timer.stop();
    }

    public final void reset() {
        timer.stop();
        rotations.set(0);
    }

    public final boolean isCountdownMode() {
        return countdownMode.get();
    }

    public final void setCountdownMode(final boolean COUNTDOWN_MODE) {
        countdownMode.set(COUNTDOWN_MODE);
    }

    public final BooleanProperty countdownModeProperty() {
        return countdownMode;
    }

    public final int getNoOfDigits() {
        return noOfDigits.get();
    }

    public final void setNoOfDigits(final int NO_OF_DIGITS) {
        noOfDigits.set(NO_OF_DIGITS);
    }

    public final IntegerProperty noOfDigitsProperty() {
        return noOfDigits;
    }

    public final int getNoOfDecimals() {
        return noOfDecimals.get();
    }

    public final void setNoOfDecimals(final int NO_OF_DECIMALS) {
        noOfDecimals.set(NO_OF_DECIMALS);
    }

    public final IntegerProperty noOfDecimalsProperty() {
        return noOfDecimals;
    }

    public final void increment() {
        rotations.set(rotations.get() + 1);
        if (rotations.get() >= Math.pow(10, (noOfDigits.get()) + noOfDecimals.get())) {
            rotations.set(0);
        }
    }

    /*public final int getDialPosition(int dial) {
        double pow = Math.pow(10, dial);
        return (int) Math.floor((rotations.get() % pow) / (pow / 10));
    }*/
    /**
     * @param dial 1-based dial index
     * @return digit value
     */
    public final int getDialPosition(int dial)
    {//[Micro-]Optimization: refactored with integer arithmetic instead of FP. Approx. x20 performance increase.
        int pow = 1;
        --dial;//now zero-based
        for (int i = dial; i > 0; --i)
            pow *= 10;
        return  rotations.get() / pow % 10;
    }

    @Override public void setPrefSize(final double WIDTH, final double HEIGHT) {
        double prefHeight = (WIDTH / (getNoOfDigits() + getNoOfDecimals())) * 1.6875 < (HEIGHT * 0.5925925925925926) ? (WIDTH / (getNoOfDigits() + getNoOfDecimals())) * 1.6875 : HEIGHT;
        double prefWidth = prefHeight * 0.5925925925925926 * (getNoOfDigits() + getNoOfDecimals());
        super.setPrefSize(prefWidth, prefHeight);
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("extras.css").toExternalForm();
    }
}

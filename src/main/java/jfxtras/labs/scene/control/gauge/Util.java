/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control.gauge;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotParametersBuilder;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;

import java.util.Random;


/**
 * Created by
 * User: hansolo
 * Date: 27.01.12
 * Time: 15:35
 */
public enum Util {
    INSTANCE;

    private static final SnapshotParameters SNAPSHOT_PARAMETER = SnapshotParametersBuilder.create().fill(Color.TRANSPARENT).build();

    public final String createCssColor(final Color COLOR) {
        final StringBuilder CSS_COLOR = new StringBuilder(19);
        CSS_COLOR.append("rgba(");
        CSS_COLOR.append((int) (COLOR.getRed() * 255));
        CSS_COLOR.append(", ");
        CSS_COLOR.append((int) (COLOR.getGreen() * 255));
        CSS_COLOR.append(", ");
        CSS_COLOR.append((int) (COLOR.getBlue() * 255));
        CSS_COLOR.append(", ");
        CSS_COLOR.append(COLOR.getOpacity());
        CSS_COLOR.append(");");
        return CSS_COLOR.toString();
    }

    public final Canvas createConicalGradient(final Shape SHAPE, final Stop[] STOPS, final double ROTATION_OFFSET) {
            final Canvas CANVAS = new Canvas(SHAPE.getLayoutBounds().getWidth(), SHAPE.getLayoutBounds().getHeight());
            createConicalGradient(CANVAS, SHAPE, STOPS, ROTATION_OFFSET);
            return CANVAS;
        }

    public final void createConicalGradient(final Canvas CANVAS, final Shape SHAPE, final Stop[] STOPS, final double ROTATION_OFFSET) {
        // adjust size of canvas to size of shape if needed
        if (CANVAS.getLayoutBounds().getWidth() < SHAPE.getLayoutBounds().getWidth()) {
            CANVAS.setWidth(SHAPE.getLayoutBounds().getWidth());
        }
        if (CANVAS.getLayoutBounds().getHeight() < SHAPE.getLayoutBounds().getHeight()) {
            CANVAS.setHeight(SHAPE.getLayoutBounds().getHeight());
        }
        // create clip shape
        final Shape CLIP = SHAPE;
        CLIP.setTranslateX(-SHAPE.getLayoutBounds().getMinX());
        CLIP.setTranslateY(-SHAPE.getLayoutBounds().getMinY());
        // adjust position of canvas in relation to shape
        CANVAS.setLayoutX(SHAPE.getLayoutBounds().getMinX());
        CANVAS.setLayoutY(SHAPE.getLayoutBounds().getMinY());
        CANVAS.setClip(CLIP);
        // create the gradient with the given stops
        final GraphicsContext CTX          = CANVAS.getGraphicsContext2D();
        final Bounds          BOUNDS       = SHAPE.getLayoutBounds();
        final Point2D         CENTER       = new Point2D(BOUNDS.getWidth() / 2, BOUNDS.getHeight() / 2);
        final double          RADIUS       = Math.sqrt(BOUNDS.getWidth() * BOUNDS.getWidth() + BOUNDS.getHeight() * BOUNDS.getHeight()) / 2;
        final double          ANGLE_STEP   = 0.1;
        final GradientLookup  COLOR_LOOKUP = new GradientLookup(STOPS);
        CTX.translate(CENTER.getX(), CENTER.getY());
        CTX.rotate(-90 + ROTATION_OFFSET);
        CTX.translate(-CENTER.getX(), -CENTER.getY());
        for (int i = 0, size = STOPS.length - 1; i < size; i++) {
            for (double angle = STOPS[i].getOffset() * 360; Double.compare(angle,STOPS[i + 1].getOffset() * 360) <= 0; angle += 0.1) {
                CTX.beginPath();
                CTX.moveTo(CENTER.getX() - RADIUS, CENTER.getY() - RADIUS);
                CTX.setFill(COLOR_LOOKUP.getColorAt(angle / 360));
                if (RADIUS > 0) {
                    CTX.fillArc(CENTER.getX() - RADIUS, CENTER.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS, angle, ANGLE_STEP, ArcType.ROUND);
                } else {
                    CTX.moveTo(CENTER.getX() - RADIUS, CENTER.getY() - RADIUS);
                }
                CTX.fill();
            }
        }
    }

    public final ImagePattern createCarbonPattern() {
        final double WIDTH        = 12;
        final double HEIGHT       = 12;
        final Canvas CANVAS       = new Canvas(WIDTH, HEIGHT);
        final GraphicsContext CTX = CANVAS.getGraphicsContext2D();

        double offsetY = 0;

        // RULB
        CTX.beginPath();
        CTX.rect(0, 0, WIDTH * 0.5, HEIGHT * 0.5);
        CTX.closePath();

        CTX.setFill(new LinearGradient(0, offsetY * HEIGHT,
                                       0, 0.5 * HEIGHT + offsetY * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(35, 35, 35)),
                                       new Stop(1, Color.rgb(23, 23, 23))));
        CTX.fill();

        // RULF
        CTX.beginPath();
        CTX.rect(WIDTH * 0.083333, 0, WIDTH * 0.333333, HEIGHT * 0.416666);
        CTX.closePath();
        offsetY = 0;
        CTX.setFill(new LinearGradient(0, offsetY * HEIGHT,
                                       0, 0.416666 * HEIGHT + offsetY * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(38, 38, 38)),
                                       new Stop(1, Color.rgb(30, 30, 30))));
        CTX.fill();

        // RLRB
        CTX.beginPath();
        CTX.rect(WIDTH * 0.5, HEIGHT * 0.5, WIDTH * 0.5, HEIGHT * 0.5);
        CTX.closePath();
        offsetY = 0.5;
        CTX.setFill(new LinearGradient(0, offsetY * HEIGHT,
                                       0, 0.5 * HEIGHT + offsetY * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(35, 35, 35)),
                                       new Stop(1, Color.rgb(23, 23, 23))));
        CTX.fill();

        // RLRF
        CTX.beginPath();
        CTX.rect(WIDTH * 0.583333, HEIGHT * 0.5, WIDTH * 0.333333, HEIGHT * 0.416666);
        CTX.closePath();
        offsetY = 0.5;
        CTX.setFill(new LinearGradient(0, offsetY * HEIGHT,
                                       0, 0.416666 * HEIGHT + offsetY * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(38, 38, 38)),
                                       new Stop(1, Color.rgb(30, 30, 30))));
        CTX.fill();

        // RURB
        CTX.beginPath();
        CTX.rect(WIDTH * 0.5, 0, WIDTH * 0.5, HEIGHT * 0.5);
        CTX.closePath();
        offsetY = 0;
        CTX.setFill(new LinearGradient(0, offsetY * HEIGHT,
                                       0, 0.5 * HEIGHT + offsetY * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(48, 48, 48)),
                                       new Stop(1, Color.rgb(40, 40, 40))));
        CTX.fill();

        // RURF
        CTX.beginPath();
        CTX.rect(WIDTH * 0.583333, HEIGHT * 0.083333, WIDTH * 0.333333, HEIGHT * 0.416666);
        CTX.closePath();
        offsetY = 0.083333;
        CTX.setFill(new LinearGradient(0, offsetY * HEIGHT,
                                       0, 0.416666 * HEIGHT + offsetY * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(53, 53, 53)),
                                       new Stop(1, Color.rgb(45, 45, 45))));
        CTX.fill();

        // RLLB
        CTX.beginPath();
        CTX.rect(0, HEIGHT * 0.5, WIDTH * 0.5, HEIGHT * 0.5);
        CTX.closePath();
        offsetY = 0.5;
        CTX.setFill(new LinearGradient(0, offsetY * HEIGHT,
                                       0, 0.5 * HEIGHT + offsetY * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(48, 48, 48)),
                                       new Stop(1, Color.rgb(40, 40, 40))));
        CTX.fill();

        // RLLF
        CTX.beginPath();
        CTX.rect(WIDTH * 0.083333, HEIGHT * 0.583333, WIDTH * 0.333333, HEIGHT * 0.416666);
        CTX.closePath();
        offsetY = 0.583333;
        CTX.setFill(new LinearGradient(0, offsetY * HEIGHT,
                                       0, 0.416666 * HEIGHT + offsetY * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(53, 53, 53)),
                                       new Stop(1, Color.rgb(45, 45, 45))));
        CTX.fill();

        final Image PATTERN_IMAGE = CANVAS.snapshot(SNAPSHOT_PARAMETER, null);
        final ImagePattern PATTERN = new ImagePattern(PATTERN_IMAGE, 0, 0, WIDTH, HEIGHT, false);

        return PATTERN;
    }

    public final ImagePattern createPunchedSheetPattern(final Color TEXTURE_COLOR) {
        final double WIDTH = 15;
        final double HEIGHT = 15;
        final Canvas CANVAS = new Canvas(WIDTH, HEIGHT);
        final GraphicsContext CTX = CANVAS.getGraphicsContext2D();

        // BACK
        CTX.beginPath();
        CTX.rect(0, 0, WIDTH, HEIGHT);
        CTX.closePath();
        //CTX.setFill(Color.rgb(29, 33, 35));
        CTX.setFill(TEXTURE_COLOR);
        CTX.fill();

        // ULB
        CTX.beginPath();
        CTX.moveTo(0, HEIGHT * 0.266666);
        CTX.bezierCurveTo(0, HEIGHT * 0.4, WIDTH * 0.066666, HEIGHT * 0.466666, WIDTH * 0.2, HEIGHT * 0.466666);
        CTX.bezierCurveTo(WIDTH * 0.333333, HEIGHT * 0.466666, WIDTH * 0.4, HEIGHT * 0.4, WIDTH * 0.4, HEIGHT * 0.266666);
        CTX.bezierCurveTo(WIDTH * 0.4, HEIGHT * 0.133333, WIDTH * 0.333333, HEIGHT * 0.066666, WIDTH * 0.2, HEIGHT * 0.066666);
        CTX.bezierCurveTo(WIDTH * 0.066666, HEIGHT * 0.066666, 0, HEIGHT * 0.133333, 0, HEIGHT * 0.266666);
        CTX.closePath();
        CTX.setFill(new LinearGradient(0, 0.066666 * HEIGHT,
                                       0, 0.466666 * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(0, 0, 0)),
                                       new Stop(1, Color.rgb(68, 68, 68))));
        CTX.fill();

        // ULF
        CTX.beginPath();
        CTX.moveTo(0, HEIGHT * 0.2);
        CTX.bezierCurveTo(0, HEIGHT * 0.333333, WIDTH * 0.066666, HEIGHT * 0.4, WIDTH * 0.2, HEIGHT * 0.4);
        CTX.bezierCurveTo(WIDTH * 0.333333, HEIGHT * 0.4, WIDTH * 0.4, HEIGHT * 0.333333, WIDTH * 0.4, HEIGHT * 0.2);
        CTX.bezierCurveTo(WIDTH * 0.4, HEIGHT * 0.066666, WIDTH * 0.333333, 0, WIDTH * 0.2, 0);
        CTX.bezierCurveTo(WIDTH * 0.066666, 0, 0, HEIGHT * 0.066666, 0, HEIGHT * 0.2);
        CTX.closePath();
        CTX.setFill(TEXTURE_COLOR.darker().darker());
        CTX.fill();

        // LRB
        CTX.beginPath();
        CTX.moveTo(WIDTH * 0.466666, HEIGHT * 0.733333);
        CTX.bezierCurveTo(WIDTH * 0.466666, HEIGHT * 0.866666, WIDTH * 0.533333, HEIGHT * 0.933333, WIDTH * 0.666666, HEIGHT * 0.933333);
        CTX.bezierCurveTo(WIDTH * 0.8, HEIGHT * 0.933333, WIDTH * 0.866666, HEIGHT * 0.866666, WIDTH * 0.866666, HEIGHT * 0.733333);
        CTX.bezierCurveTo(WIDTH * 0.866666, HEIGHT * 0.6, WIDTH * 0.8, HEIGHT * 0.533333, WIDTH * 0.666666, HEIGHT * 0.533333);
        CTX.bezierCurveTo(WIDTH * 0.533333, HEIGHT * 0.533333, WIDTH * 0.466666, HEIGHT * 0.6, WIDTH * 0.466666, HEIGHT * 0.733333);
        CTX.closePath();
        CTX.setFill(new LinearGradient(0, 0.533333 * HEIGHT,
                                       0, 0.933333 * HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, Color.rgb(0, 0, 0)),
                                       new Stop(1, Color.rgb(68, 68, 68))));
        CTX.fill();

        // LRF
        CTX.beginPath();
        CTX.moveTo(WIDTH * 0.466666, HEIGHT * 0.666666);
        CTX.bezierCurveTo(WIDTH * 0.466666, HEIGHT * 0.8, WIDTH * 0.533333, HEIGHT * 0.866666, WIDTH * 0.666666, HEIGHT * 0.866666);
        CTX.bezierCurveTo(WIDTH * 0.8, HEIGHT * 0.866666, WIDTH * 0.866666, HEIGHT * 0.8, WIDTH * 0.866666, HEIGHT * 0.666666);
        CTX.bezierCurveTo(WIDTH * 0.866666, HEIGHT * 0.533333, WIDTH * 0.8, HEIGHT * 0.466666, WIDTH * 0.666666, HEIGHT * 0.466666);
        CTX.bezierCurveTo(WIDTH * 0.533333, HEIGHT * 0.466666, WIDTH * 0.466666, HEIGHT * 0.533333, WIDTH * 0.466666, HEIGHT * 0.666666);
        CTX.closePath();
        CTX.setFill(TEXTURE_COLOR.darker().darker());
        CTX.fill();

        final Image PATTERN_IMAGE = CANVAS.snapshot(SNAPSHOT_PARAMETER, null);
        final ImagePattern PATTERN = new ImagePattern(PATTERN_IMAGE, 0, 0, WIDTH, HEIGHT, false);

        return PATTERN;
    }

    public final ImagePattern createNoisePattern(final double WIDTH, final double HEIGHT, final Color TEXTURE_COLOR) {
        final Canvas CANVAS = new Canvas(WIDTH, HEIGHT);
        final GraphicsContext CTX = CANVAS.getGraphicsContext2D();

        CTX.setFill(new LinearGradient(0, 0,
                                       WIDTH, HEIGHT,
                                       false, CycleMethod.NO_CYCLE,
                                       new Stop(0, brighter(TEXTURE_COLOR, 0.15)),
                                       new Stop(1, darker(TEXTURE_COLOR, 0.15))));
        CTX.fillRect(0, 0, WIDTH, HEIGHT);

        final Color  DARK_NOISE   = TEXTURE_COLOR.darker();
        final Color  BRIGHT_NOISE = TEXTURE_COLOR.brighter();
        final Random BW_RND       = new Random();
        final Random ALPHA_RND    = new Random();
        Color  noiseColor;
        double noiseAlpha;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (BW_RND.nextBoolean()) {
                    noiseColor = BRIGHT_NOISE;
                } else {
                    noiseColor = DARK_NOISE;
                }
                noiseAlpha = 0.0392156863 + (ALPHA_RND.nextInt(10) / 255) - 0.0196078431;
                CTX.beginPath();
                CTX.moveTo(x, y);
                CTX.lineTo(x, y);
                CTX.setStroke(Color.color(noiseColor.getRed(), noiseColor.getGreen(), noiseColor.getBlue(), noiseAlpha));
                CTX.stroke();
            }
        }
        final Image PATTERN_IMAGE = CANVAS.snapshot(SNAPSHOT_PARAMETER, null);
        final ImagePattern PATTERN = new ImagePattern(PATTERN_IMAGE, 0, 0, WIDTH, HEIGHT, false);

        return PATTERN;
    }

    public Color darker(final Color COLOR, final double FRACTION) {
        double red   = Math.round(COLOR.getRed() * (1.0 - FRACTION));
        double green = Math.round(COLOR.getGreen() * (1.0 - FRACTION));
        double blue  = Math.round(COLOR.getBlue() * (1.0 - FRACTION));

        red   = red < 0 ? 0 : (red > 1 ? 1 : red);
        green = green < 0 ? 0 : (green > 1 ? 1 : green);
        blue  = blue < 0 ? 0 : (blue > 1 ? 1 : blue);

        return Color.color(red, green, blue, COLOR.getOpacity());
    }

    public Color brighter(final Color COLOR, final double FRACTION) {
        double red   = Math.round(COLOR.getRed() * (1.0 + FRACTION));
        double green = Math.round(COLOR.getGreen() * (1.0 + FRACTION));
        double blue  = Math.round(COLOR.getBlue() * (1.0 + FRACTION));

        red = red < 0 ? 0 : (red > 1 ? 1 : red);
        green = green < 0 ? 0 : (green > 1 ? 1 : green);
        blue = blue < 0 ? 0 : (blue > 1 ? 1 : blue);

        return Color.color(red, green, blue, COLOR.getOpacity());
    }
}

/**
 * BiLinearGradient.java
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

package jfxtras.labs.util;

import javafx.animation.Interpolator;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;


/**
 * Created by
 * User: hansolo
 * Date: 17.07.12
 * Time: 08:13
 */
public class BiLinearGradient {
    private final Color COLOR_00;
    private final Color COLOR_10;
    private final Color COLOR_01;
    private final Color COLOR_11;

    // ******************** Constructors **************************************
    public BiLinearGradient(final Color COLOR_00, final Color COLOR_10, final Color COLOR_01, final Color COLOR_11) {
        // Set the values
        this.COLOR_00 = COLOR_00;
        this.COLOR_10 = COLOR_10;
        this.COLOR_01 = COLOR_01;
        this.COLOR_11 = COLOR_11;
    }


    // ******************** Methods *******************************************
    private Color biLinearInterpolateColor(final Color COL_00, final Color COL_10, final Color COL_01, final Color COL_11, final double FRAC_X, final double FRAC_Y) {
        final Color INTERPOLATED_COLOR_X1 = (Color) Interpolator.LINEAR.interpolate(COL_00, COL_10, FRAC_X);
        final Color INTERPOLATED_COLOR_X2 = (Color) Interpolator.LINEAR.interpolate(COL_01, COL_11, FRAC_X);
        return (Color) Interpolator.LINEAR.interpolate(INTERPOLATED_COLOR_X1, INTERPOLATED_COLOR_X2, FRAC_Y);
    }

    public Image getImage(final double WIDTH, final double HEIGHT) {
        int   width  = (int) WIDTH  <= 0 ? 100 : (int) WIDTH;
        int   height = (int) HEIGHT <= 0 ? 100 : (int) HEIGHT;
        final WritableImage RASTER       = new WritableImage(width, height);
        final PixelWriter   PIXEL_WRITER = RASTER.getPixelWriter();
        final double FRACTION_STEP_X     = 1.0 / (WIDTH - 1);
        final double FRACTION_STEP_Y     = 1.0 / (HEIGHT - 1);
        double fractionX = 0;
        double fractionY = 0;

        for (int y = 0; y < HEIGHT ; y++) {
            for (int x = 0 ; x < WIDTH ; x++) {
                PIXEL_WRITER.setColor(x, y, biLinearInterpolateColor(COLOR_00, COLOR_10, COLOR_01, COLOR_11, fractionX, fractionY));
                fractionX += FRACTION_STEP_X;
                fractionX = fractionX > 1 ? 1 : fractionX;
            }
            fractionY += FRACTION_STEP_Y;
            fractionY = fractionY > 1 ? 1 : fractionY;
            fractionX = 0;
        }
        return RASTER;
    }

    public ImagePattern apply(final Shape SHAPE) {
        double x      = SHAPE.getLayoutBounds().getMinX();
        double y      = SHAPE.getLayoutBounds().getMinY();
        double width  = SHAPE.getLayoutBounds().getWidth();
        double height = SHAPE.getLayoutBounds().getHeight();
        return new ImagePattern(getImage(width, height), x, y, width, height, false);
    }
}

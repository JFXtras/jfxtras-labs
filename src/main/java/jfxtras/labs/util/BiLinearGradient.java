package jfxtras.labs.util;

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
    private Color interpolateColor(final Color COLOR1, final Color COLOR2, final double FRACTION) {
        double red   = COLOR1.getRed() + (COLOR2.getRed() - COLOR1.getRed()) * FRACTION;
        double green = COLOR1.getGreen() + (COLOR2.getGreen() - COLOR1.getGreen()) * FRACTION;
        double blue  = COLOR1.getBlue() + (COLOR2.getBlue() - COLOR1.getBlue()) * FRACTION;
        double alpha = COLOR1.getOpacity() + (COLOR2.getOpacity() - COLOR1.getOpacity()) * FRACTION;
        red   = red < 0 ? 0 : (red > 1 ? 1 : red);
        green = green < 0 ? 0 : (green > 1 ? 1 : green);
        blue  = blue < 0 ? 0 : (blue > 1 ? 1 : blue);
        alpha = alpha < 0 ? 0 : (alpha > 1 ? 1 : alpha);
        return new Color(red, green, blue, alpha);
    }

    private Color biLinearInterpolateColor(final Color COL_00, final Color COL_10, final Color COL_01, final Color COL_11, final double FRAC_X, final double FRAC_Y) {
        final Color INTERPOLATED_COLOR_X1 = interpolateColor(COL_00, COL_10, FRAC_X);
        final Color INTERPOLATED_COLOR_X2 = interpolateColor(COL_01, COL_11, FRAC_X);
        return interpolateColor(INTERPOLATED_COLOR_X1, INTERPOLATED_COLOR_X2, FRAC_Y);
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

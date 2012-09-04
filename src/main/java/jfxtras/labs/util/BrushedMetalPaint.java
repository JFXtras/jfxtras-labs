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

package jfxtras.labs.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 04.09.12
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class BrushedMetalPaint {
    private int     radius;
    private double  amount;
    private int     color;
    private double  shine;
    private boolean monochrome;
    private Random  randomNumbers;


    // ******************** Constructors **************************************
    public BrushedMetalPaint() {
        this(Color.rgb(136, 136, 136), 5, 0.1, true, 0.3);
    }

    public BrushedMetalPaint(final Color COLOR) {
        this(COLOR, 5, 0.1, true, 0.3);
    }

    public BrushedMetalPaint(final Color COLOR, final int RADIUS, final double AMOUNT, final boolean MONOCHROME, final double SHINE) {
        color      = getIntFromColor(COLOR);
        radius     = RADIUS;
        amount     = AMOUNT;
        monochrome = MONOCHROME;
        shine      = SHINE;
    }


    // ******************** Methods *******************************************
    public Image filter(final double W, final double H) {
        final int WIDTH  = (int) W;
        final int HEIGHT = (int) H;

        WritableImage DESTINATION = new WritableImage(WIDTH, HEIGHT);

        final int[] IN_PIXELS  = new int[WIDTH];
        final int[] OUT_PIXELS = new int[WIDTH];

        randomNumbers   = new Random(0);
        final int ALPHA = color & 0xff000000;
        final int RED   = (color >> 16) & 0xff;
        final int GREEN = (color >> 8) & 0xff;
        final int BLUE  = color & 0xff;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int tr = RED;
                int tg = GREEN;
                int tb = BLUE;
                if (shine != 0) {
                    int f = (int) (255 * shine * Math.sin((double) x / WIDTH * Math.PI));
                    tr += f;
                    tg += f;
                    tb += f;
                }
                if (monochrome) {
                    int n = (int) (255 * (2 * randomNumbers.nextFloat() - 1) * amount);
                    IN_PIXELS[x] = ALPHA | (clamp(tr + n) << 16) | (clamp(tg + n) << 8) | clamp(tb + n);
                } else {
                    IN_PIXELS[x] = ALPHA | (random(tr) << 16) | (random(tg) << 8) | random(tb);
                }
            }

            if (radius != 0) {
                blur(IN_PIXELS, OUT_PIXELS, WIDTH, radius);
                setRGB(DESTINATION, 0, y, OUT_PIXELS);
            } else {
                setRGB(DESTINATION, 0, y, IN_PIXELS);
            }
        }
        return DESTINATION;
    }

    public ImageView filter(final double W, final double H, final Shape CLIP) {
        final Image IMAGE = filter(W, H);
        final ImageView IMAGE_VIEW = new ImageView(IMAGE);
        IMAGE_VIEW.setClip(CLIP);
        return IMAGE_VIEW;
    }

    public void blur(final int[] IN, final int[] OUT, final int WIDTH, final int RADIUS) {
        final int WIDTH_MINUS_1 = WIDTH - 1;
        final int R2 = 2 * RADIUS + 1;
        int tr = 0, tg = 0, tb = 0;

        for (int i = -RADIUS; i <= RADIUS; i++) {
            int rgb = IN[mod(i, WIDTH)];
            tr += (rgb >> 16) & 0xff;
            tg += (rgb >> 8) & 0xff;
            tb += rgb & 0xff;
        }

        for (int x = 0; x < WIDTH; x++) {
            OUT[x] = 0xff000000 | ((tr / R2) << 16) | ((tg / R2) << 8) | (tb / R2);

            int i1 = x + RADIUS + 1;
            if (i1 > WIDTH_MINUS_1) {
                i1 = mod(i1, WIDTH);
            }
            int i2 = x - RADIUS;
            if (i2 < 0) {
                i2 = mod(i2, WIDTH);
            }
            int rgb1 = IN[i1];
            int rgb2 = IN[i2];

            tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
            tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
            tb += (rgb1 & 0xff) - (rgb2 & 0xff);
        }
    }

    public void setRadius(final int RADIUS) {
        radius = RADIUS;
    }

    public int getRadius() {
        return radius;
    }

    public void setAmount(final double AMOUNT) {
        amount = AMOUNT;
    }

    public double getAmount() {
        return amount;
    }

    public void setColor(final int COLOR) {
        color = COLOR;
    }

    public int getColor() {
        return color;
    }

    public void setMonochrome(final boolean MONOCHROME) {
        monochrome = MONOCHROME;
    }

    public boolean isMonochrome() {
        return monochrome;
    }

    public void setShine(final double SHINE) {
        shine = SHINE;
    }

    public double getShine() {
        return shine;
    }

    private int random(int x) {
        x += (int) (255 * (2 * randomNumbers.nextFloat() - 1) * amount);
        if (x < 0) {
            x = 0;
        } else if (x > 0xff) {
            x = 0xff;
        }
        return x;
    }

    private int clamp(final int C) {
        int ret = C;
        if (C < 0) {
            ret = 0;
        }
        if (C > 255) {
            ret = 255;
        }
        return ret;
    }

    private int mod(int a, final int B) {
        final int N = a / B;
        a -= N * B;
        if (a < 0) {
            return a + B;
        }
        return a;
    }

    private void setRGB(final WritableImage IMAGE, final int X, final int Y, final int[] PIXELS) {
        final PixelWriter RASTER = IMAGE.getPixelWriter();
        for (int x = 0 ; x < PIXELS.length ; x++) {
            RASTER.setColor(X + x, Y, Color.rgb((PIXELS[x] >> 16) & 0xFF, (PIXELS[x] >> 8) & 0xFF, (PIXELS[x] &0xFF)));
        }
    }

    private int getIntFromColor(final Color COLOR) {
        String hex = COLOR.toString();
        StringBuilder intValue = new StringBuilder(10);
        intValue.append(hex.substring(8, 10));
        intValue.append(hex.substring(2, 8));
        //System.out.println((int) (Long.parseLong(intValue.toString(), 16) - 0x100000000l));
        return (int) Long.parseLong(intValue.toString(), 16);
    }
}

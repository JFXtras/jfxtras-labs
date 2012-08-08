/*
 * Copyright (c) 2012, JFXtras
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of the <organization> nor the
 *         names of its contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.FlowPaneBuilder;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.TilePaneBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * TODO: describe the class
 *
 * @author Andreas Billmann
 */
public class MiniIconButtonDemo extends Application {

    final WritableImage bigIcon = new WritableImage(32, 32);
    final PixelWriter bigIconWriter = bigIcon.getPixelWriter();

    final WritableImage mediumIcon = new WritableImage(24, 24);
    final PixelWriter mediumIconWriter = mediumIcon.getPixelWriter();


    final WritableImage smallIcon = new WritableImage(16, 16);
    final PixelWriter smallIconWriter = smallIcon.getPixelWriter();

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        fillImages();

        final MiniIconButton b1 = new MiniIconButton(new ImageView(bigIcon), new ImageView(smallIcon));
        b1.setAnimationType(MiniIconButton.AnimationType.BLINK);

        final MiniIconButton b2 = new MiniIconButton("2. Button", new ImageView(bigIcon), new ImageView(smallIcon));
        b2.setAnimationType(MiniIconButton.AnimationType.JUMP);

        final MiniIconButton b3 = new MiniIconButton("3. Button", new ImageView(bigIcon), new ImageView(smallIcon));

        final MiniIconButton b4 = new MiniIconButton("4. Button", new ImageView(bigIcon), new ImageView(smallIcon));
        b4.setAnimationType(MiniIconButton.AnimationType.BLINK);
        b4.setMiniIconRatio(1.0);
        b4.setAnimationDuration(1000);

        final MiniIconButton b5 = new MiniIconButton("5. Button", new ImageView(bigIcon), new ImageView(smallIcon));
        b5.setAnimationType(MiniIconButton.AnimationType.JUMP);
        b5.setAnimationDuration(100);

        final MiniIconButton b6 = new MiniIconButton("6. Button", new ImageView(bigIcon), new ImageView(smallIcon));
        b6.setMiniIconRatio(2.0);
        b6.setMiniIconPosition(Pos.BOTTOM_LEFT);

        final MiniIconButton b7 = new MiniIconButton("7. Button", new ImageView(bigIcon), new ImageView(mediumIcon));
        b7.setMiniIconPosition(Pos.CENTER);

        final MiniIconButton b8 = new MiniIconButton(new ImageView(smallIcon), new ImageView(mediumIcon));
        b8.setMiniIconRatio(1.0);

        final MiniIconButton b9 = new MiniIconButton(new ImageView(smallIcon), new ImageView(mediumIcon));
        b9.setMiniIconRatio(0.75);

        final MiniIconButton b10 = new MiniIconButton(new ImageView(smallIcon), new ImageView(mediumIcon));
        b10.setMiniIconRatio(0.5);

        final MiniIconButton b11 = new MiniIconButton(new ImageView(smallIcon), new ImageView(mediumIcon));
        b11.setMiniIconRatio(0.25);

        final MiniIconButton b12 = new MiniIconButton(new ImageView(smallIcon), new ImageView(mediumIcon));
        b12.setMiniIconRatio(0.1);

        final MiniIconButton b13 = new MiniIconButton(new ImageView(smallIcon), new ImageView(mediumIcon));
        b13.setMiniIconRatio(0.0);

        final FlowPane rootPane = FlowPaneBuilder
                .create()
                .children(b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13)
                .hgap(10)
                .build();

        final Scene scene = SceneBuilder
                .create()
                .width(800)
                .height(600)
                .root(rootPane)
                .build();

        stage.setScene(scene);
        stage.show();
    }

    public void fillImages() {
        for (int i=0; i<32; i++) {
            for (int j=0; j<32; j++) {
                bigIconWriter.setColor(i, j, Color.gray(0.8));
            }
        }

        for (int i=0; i<24; i++) {
            for (int j=0; j<24; j++) {
                mediumIconWriter.setColor(i, j, Color.rgb(0, 200, 0, 1.0));
            }
        }

        for (int i=0; i<16; i++) {
            for (int j=0; j<16; j++) {
                smallIconWriter.setColor(i, j, Color.rgb(200, 0, 0, 0.8));
            }
        }
    }

}

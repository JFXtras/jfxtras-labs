/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.control.radialmenu;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.labs.scene.control.radialmenu.RadialMenu.CenterVisibility;

/**
 *
 * @author MrLoNee
 */
public class RadialMenuDemo extends Application {

    protected RadialMenu radialMenu;

    protected Label actionPerformedLabel = new Label();

    protected boolean show;

    protected double lastOffsetValue;

    protected double lastInitialAngleValue;

    private FadeTransition textFadeTransition;

    public RadialMenuDemo() {
        radialMenu = this.createRadialMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(asContextMenuDemo(), 800, 600);

        primaryStage.setTitle("RadialMenu Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane demo1() {
        AnchorPane root = new AnchorPane();

        AnchorPane.setBottomAnchor(actionPerformedLabel, 20d);
        AnchorPane.setLeftAnchor(actionPerformedLabel, 20d);
        root.getChildren().add(actionPerformedLabel);

        root.getChildren().addAll(radialMenu);
        return root;
    }

    private Pane asContextMenuDemo() {
        AnchorPane root = new AnchorPane();
        root.getChildren().add(new Label("Use the secondary mouse button to show the menu"));

        AnchorPane.setBottomAnchor(actionPerformedLabel, 20d);
        AnchorPane.setLeftAnchor(actionPerformedLabel, 20d);
        root.getChildren().add(actionPerformedLabel);

        radialMenu.setVisible(false);
        radialMenu.setOpacity(0.66);
        radialMenu.hideRadialMenu();
        radialMenu.centerVisibilityProperty().set(CenterVisibility.WITH_MENU);
        root.getChildren().addAll(radialMenu);
        root.setOnContextMenuRequested(e -> {
            radialMenu.setTranslateX(e.getX());
            radialMenu.setTranslateY(e.getY());
            radialMenu.setVisible(true);
            radialMenu.showRadialMenu();
        });

        return root;
    }

    public RadialMenu createRadialMenu() {

        LinearGradient background = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.LIGHTGREY), new Stop(0.6, Color.LIGHTGREY.darker()));

        LinearGradient backgroundMouseOn = new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.NO_CYCLE, new Stop(0.0, Color.LIGHTGREY),
                new Stop(0.8, Color.LIGHTGREY.darker()));

        this.radialMenu = new RadialMenu(-37, 30, 100, 5, background, backgroundMouseOn,
                Color.DARKGREY.darker().darker(), Color.DARKGREY.darker(), false,
                RadialMenu.CenterVisibility.ALWAYS, null);

        this.radialMenu.setTranslateX(200);
        this.radialMenu.setTranslateY(200);

        final ImageView play = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSamplePlayIcon.png")));

        final ImageView stop = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSampleStopIcon.png")));

        final ImageView pause = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSamplePauseIcon.png")));

        final ImageView forward = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSampleForwardIcon.png")));

        final ImageView backward = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSampleBackwardIcon.png")));

        final EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

            @Override
            public synchronized void handle(final ActionEvent paramT) {
                final RadialMenuItem item = (RadialMenuItem) paramT.getSource();
                if (RadialMenuDemo.this.textFadeTransition != null
                        && RadialMenuDemo.this.textFadeTransition
                                .getStatus() != Animation.Status.STOPPED) {
                    RadialMenuDemo.this.textFadeTransition.stop();
                    RadialMenuDemo.this.actionPerformedLabel.setOpacity(1.0);
                }
                RadialMenuDemo.this.actionPerformedLabel.setText(item.getText());
                RadialMenuDemo.this.actionPerformedLabel.setVisible(true);
                FadeTransition fade = new FadeTransition(Duration.millis(400),
                        RadialMenuDemo.this.actionPerformedLabel);
                fade.setDelay(Duration.seconds(1));
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setOnFinished(e -> {
                    RadialMenuDemo.this.actionPerformedLabel.setVisible(false);
                    RadialMenuDemo.this.actionPerformedLabel.setOpacity(1.0);
                });

                RadialMenuDemo.this.textFadeTransition = fade;
                RadialMenuDemo.this.textFadeTransition.play();
            }

        };

        final ImageView fiveSec = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSample5SecIcon.png")));

        final ImageView tenSec = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSample10SecIcon.png")));

        final ImageView twentySec = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSample20SecIcon.png")));

        final RadialContainerMenuItem forwardItem = new RadialContainerMenuItem(50, "forward",
                forward);
        forwardItem.addMenuItem(new RadialMenuItem(30, "forward 5'", fiveSec, handler));
        forwardItem.addMenuItem(new RadialMenuItem(30, "forward 10'", tenSec, handler));
        forwardItem.addMenuItem(new RadialMenuItem(30, "forward 20'", twentySec, handler));

        this.radialMenu.addMenuItem(forwardItem);

        this.radialMenu.addMenuItem(new RadialMenuItem(50, "pause", pause, handler));

        this.radialMenu.addMenuItem(new RadialMenuItem(50, "play", play, handler));

        this.radialMenu.addMenuItem(new RadialMenuItem(50, "stop", stop, handler));

        final RadialContainerMenuItem backwardItem = new RadialContainerMenuItem(50, "backward",
                backward);
        final ImageView fiveSecBack = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSample5SecIcon.png")));

        final ImageView tenSecBack = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSample10SecIcon.png")));

        final ImageView twentySecBack = new ImageView(
                new Image(this.getClass().getResourceAsStream("RadialMenuSample20SecIcon.png")));

        backwardItem.addMenuItem(new RadialMenuItem(30, "backward 5'", fiveSecBack, handler));
        backwardItem.addMenuItem(new RadialMenuItem(30, "bacward 10'", tenSecBack, handler));
        backwardItem.addMenuItem(new RadialMenuItem(30, "bacward 20'", twentySecBack, handler));

        this.radialMenu.addMenuItem(backwardItem);

        return this.radialMenu;
    }

}

/**
 * GridFxTrial2.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.labs.scene.control;

import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import jfxtras.labs.scene.control.grid.GridCell;
import jfxtras.labs.scene.control.grid.GridView;
import jfxtras.labs.scene.control.grid.cell.ColorGridCell;

public class GridFxTrial2 extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private class ColoredAndAnimatedGridCell extends ColorGridCell {
		
		private RotateTransition rotateInTransition;
		
		private ScaleTransition scaleInTransition;
		
		private RotateTransition rotateOutTransition;
		
		private ScaleTransition scaleOutTransition;
		
		
		public ColoredAndAnimatedGridCell() {
			addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					toFront();
					if(rotateOutTransition != null) {
						rotateOutTransition.stop();
					}
					if(scaleOutTransition != null) {
						scaleOutTransition.stop();
					}
					rotateInTransition = RotateTransitionBuilder.create().fromAngle(ColoredAndAnimatedGridCell.this.getRotate()).toAngle(16).interpolator(Interpolator.EASE_IN).duration(Duration.millis(120)).node(ColoredAndAnimatedGridCell.this).build();
					scaleInTransition = ScaleTransitionBuilder.create().fromX(ColoredAndAnimatedGridCell.this.getScaleX()).toX(1.3).fromY(ColoredAndAnimatedGridCell.this.getScaleY()).toY(1.3).interpolator(Interpolator.EASE_IN).duration(Duration.millis(120)).node(ColoredAndAnimatedGridCell.this).build();
					rotateInTransition.play();
					scaleInTransition.play();
				}
			});
			
			addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					if(rotateInTransition != null) {
						rotateInTransition.stop();
					}
					if(scaleInTransition != null) {
						scaleInTransition.stop();
					}
					rotateOutTransition = RotateTransitionBuilder.create().fromAngle(ColoredAndAnimatedGridCell.this.getRotate()).toAngle(0).interpolator(Interpolator.EASE_OUT).duration(Duration.millis(340)).node(ColoredAndAnimatedGridCell.this).build();
					scaleOutTransition = ScaleTransitionBuilder.create().fromX(ColoredAndAnimatedGridCell.this.getScaleX()).toX(1.0).fromY(ColoredAndAnimatedGridCell.this.getScaleY()).toY(1.0).interpolator(Interpolator.EASE_OUT).duration(Duration.millis(340)).node(ColoredAndAnimatedGridCell.this).build();
					rotateOutTransition.play();
					scaleOutTransition.play();
				}
			});
		}

	}
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("GridFxTrial2");

		final ObservableList<Color> list = FXCollections
				.<Color> observableArrayList();
		GridView<Color> myGrid = new GridView<>(list);

		myGrid.setCellFactory(new Callback<GridView<Color>, GridCell<Color>>() {

			@Override
			public GridCell<Color> call(GridView<Color> arg0) {
				return new ColoredAndAnimatedGridCell();
			}
		});
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < 100; i++) {
			list.add(new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(),
					1.0));
		}

		BorderPane root = new BorderPane();
		root.setCenter(myGrid);

		Scene scene = new Scene(root, 540, 210);

		stage.setScene(scene);
		stage.show();
	}
}
/**
 * AgendaTrial1.java
 *
 * Copyright (c) 2011-2014, JFXtras
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

package jfxtras.labs.scene.control.gauge.linear.trial;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.gauge.linear.CompleteSegment;
import jfxtras.labs.scene.control.gauge.linear.Indicator;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;
import jfxtras.labs.scene.control.gauge.linear.PercentMarker;
import jfxtras.labs.scene.control.gauge.linear.PercentSegment;
import jfxtras.labs.scene.control.gauge.linear.Segment;
import jfxtras.labs.test.TestUtil;

/**
 * @author Tom Eugelink
 */
abstract public class LinearGaugeTrial1 extends Application {
	
	public abstract LinearGauge<?> createLinearGauge();
	
	@Override
	public void start(Stage stage) {

		List<LinearGauge<?>> gauges = new ArrayList<>();
		
		FlowPane lFlowPane = new FlowPane(10, 10);
		
        // naked
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
		}
        
        // without segments, static value
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.withValue(50.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
		}
        
        // without segments
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // broken
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.withValue(-10.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);			
		}

		// 10 segments
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 10.0, (i+1) * 10.0);
				lLinearGauge.segments().add(lSegment);
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
		// 10 segments, color schema
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-green-to-red-10");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 10.0, (i+1) * 10.0);
				lLinearGauge.segments().add(lSegment);
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // not animated
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000; -fxx-animated:NO;");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // shrunk
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setValue(50.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.setPrefSize(150.0, 150.0);
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // larger
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setValue(50.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.setPrefSize(300.0, 300.0);
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // large range with format
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setMinValue(-10.0);
			lLinearGauge.setMaxValue(1000.0);
			lLinearGauge.setValue(100.0);
			lLinearGauge.setStyle("-fx-border-color:#000000; -fxx-value-format:' ##0.0W';");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // negative large range
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setMinValue(-1000000.0);
			lLinearGauge.setMaxValue(100.0);
			lLinearGauge.setValue(-1000.0);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // 10 segments, with remove
		{
			HBox lHBox = new HBox();
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-green-to-red-10");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 10.0, (i+1) * 10.0);
				lLinearGauge.segments().add(lSegment);
			}
			lHBox.getChildren().add(lLinearGauge);
			
			Button lButton = new Button("X");
			lButton.setOnAction( (event) -> {
				if (lLinearGauge.segments().size() > 0) {
					lLinearGauge.segments().remove(lLinearGauge.segments().size() - 1);
					System.out.println("removed segment, remaining " + lLinearGauge.segments().size());
				}
			});
			lHBox.getChildren().add(lButton);

			lFlowPane.getChildren().add(lHBox);
			
			gauges.add(lLinearGauge);
		}
        
        
        // 20 segments
		{
			HBox lHBox = new HBox();
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-green-to-red-10");
			for (int i = 0; i < 20; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 5.0, (i+1) * 5.0);
				lLinearGauge.segments().add(lSegment);
			}
			lHBox.getChildren().add(lLinearGauge);

			lFlowPane.getChildren().add(lHBox);
			
			gauges.add(lLinearGauge);
		}
        
		// manually show indicators
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.indicators().add(new Indicator(0, "warning"));
			lLinearGauge.indicators().add(new Indicator(1, "error"));
			lLinearGauge.setStyle("-fx-border-color: #000000; -fxx-warning-indicator-visibility: visible; -fxx-error-indicator-visibility: visible; ");
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
		// 10 segments, transparent, with segment related indicators
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.indicators().add(new Indicator(0, "warning"));
			lLinearGauge.indicators().add(new Indicator(1, "error"));
			lLinearGauge.setId("segmentRelatedIndicators");
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			lLinearGauge.getStyleClass().add("colorscheme-first-grey-rest-transparent-10");
			lLinearGauge.segments().add(new CompleteSegment(lLinearGauge));
			lLinearGauge.segments().add(new PercentSegment(lLinearGauge, 50.0, 100.0, "warningSegment"));
			lLinearGauge.segments().add(new PercentSegment(lLinearGauge, 75.0, 100.0, "errorSegment"));
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}

        // markers
		{
			final LinearGauge<?> lLinearGauge = createLinearGauge();
			lLinearGauge.setStyle("-fx-border-color: #000000;");
			for (int i = 0; i <= 20; i++) {
				lLinearGauge.markers().add(new PercentMarker(lLinearGauge, i * 5.0));
			}
			lFlowPane.getChildren().add(lLinearGauge);
			gauges.add(lLinearGauge);
		}
        
        // create scene
        Scene scene = new Scene(lFlowPane, 1500, 900);
        scene.getStylesheets().add(this.getClass().getResource(LinearGaugeTrial1.class.getSimpleName()+ ".css").toExternalForm());

        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
        
        // start periodically changing the value of the gauges
		Thread t = new Thread( () -> {
			Random lRandom = new Random();
			while (true) {
				TestUtil.sleep(2000);
				Platform.runLater( () -> {
					double d = lRandom.nextDouble();
					for (LinearGauge<?> g : gauges) {
				 		double minValue = g.getMinValue();
				 		double maxValue = g.getMaxValue();
						g.setValue(minValue + (d * (maxValue - minValue)));
					}
				});
				
			}
		});
		t.setDaemon(true);
		t.start();
    }
}


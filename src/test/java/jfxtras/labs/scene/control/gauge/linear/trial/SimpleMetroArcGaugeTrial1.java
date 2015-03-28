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
import jfxtras.labs.scene.control.gauge.linear.PercentMarker;
import jfxtras.labs.scene.control.gauge.linear.PercentSegment;
import jfxtras.labs.scene.control.gauge.linear.Segment;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.labs.test.TestUtil;

/**
 * @author Tom Eugelink
 */
public class SimpleMetroArcGaugeTrial1 extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		List<SimpleMetroArcGauge> gauges = new ArrayList<SimpleMetroArcGauge>();
		
		FlowPane lFlowPane = new FlowPane(10, 10);
		
        // naked
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
		}
        
        // without segments, static value
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge().withValue(50.0);
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
		}
        
        // without segments
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
        // broken
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge().withValue(-10.0);
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);			
		}

		// 10 segments
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lSimpleMetroArcGauge, i * 10.0, (i+1) * 10.0);
				lSimpleMetroArcGauge.segments().add(lSegment);
			}
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
		// 10 segments, color schema
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lSimpleMetroArcGauge.getStyleClass().add("colorscheme-green-to-red-10");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lSimpleMetroArcGauge, i * 10.0, (i+1) * 10.0);
				lSimpleMetroArcGauge.segments().add(lSegment);
			}
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
        // not animated
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000; -fxx-animated:NO;");
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
        // shrunk
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setValue(50.0);
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lSimpleMetroArcGauge.setPrefSize(100.0, 100.0);
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
        // larger
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setValue(50.0);
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lSimpleMetroArcGauge.setPrefSize(300.0, 300.0);
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
        // large range with format
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setMinValue(-10.0);
			lSimpleMetroArcGauge.setMaxValue(1000.0);
			lSimpleMetroArcGauge.setValue(100.0);
			lSimpleMetroArcGauge.setStyle("-fx-border-color:#000000; -fxx-value-format:' ##0.0W';");
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
        // negative large range
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setMinValue(-1000000.0);
			lSimpleMetroArcGauge.setMaxValue(100.0);
			lSimpleMetroArcGauge.setValue(-1000.0);
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
        // 10 segments, with remove
		{
			HBox lHBox = new HBox();
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lSimpleMetroArcGauge.getStyleClass().add("colorscheme-green-to-red-10");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lSimpleMetroArcGauge, i * 10.0, (i+1) * 10.0);
				lSimpleMetroArcGauge.segments().add(lSegment);
			}
			lHBox.getChildren().add(lSimpleMetroArcGauge);
			
			Button lButton = new Button("X");
			lButton.setOnAction( (event) -> {
				if (lSimpleMetroArcGauge.segments().size() > 0) {
					lSimpleMetroArcGauge.segments().remove(lSimpleMetroArcGauge.segments().size() - 1);
					System.out.println("removed segment, remaining " + lSimpleMetroArcGauge.segments().size());
				}
			});
			lHBox.getChildren().add(lButton);

			lFlowPane.getChildren().add(lHBox);
			
			gauges.add(lSimpleMetroArcGauge);
		}
        
        
        // 20 segments
		{
			HBox lHBox = new HBox();
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lSimpleMetroArcGauge.getStyleClass().add("colorscheme-green-to-red-10");
			for (int i = 0; i < 20; i++) {
				Segment lSegment = new PercentSegment(lSimpleMetroArcGauge, i * 5.0, (i+1) * 5.0);
				lSimpleMetroArcGauge.segments().add(lSegment);
			}
			lHBox.getChildren().add(lSimpleMetroArcGauge);

			lFlowPane.getChildren().add(lHBox);
			
			gauges.add(lSimpleMetroArcGauge);
		}
        
		// manually show indicators
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000; -fxx-warning-indicator-visibility: visible; -fxx-error-indicator-visibility: visible; ");
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
		// 10 segments, transparent, with segment related indicators
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setId("segmentRelatedIndicators");
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			lSimpleMetroArcGauge.getStyleClass().add("colorscheme-first-grey-rest-transparent-10");
			lSimpleMetroArcGauge.segments().add(new CompleteSegment(lSimpleMetroArcGauge));
			lSimpleMetroArcGauge.segments().add(new PercentSegment(lSimpleMetroArcGauge, 50.0, 100.0, "warningSegment"));
			lSimpleMetroArcGauge.segments().add(new PercentSegment(lSimpleMetroArcGauge, 75.0, 100.0, "errorSegment"));
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}

        // markers
		{
			final SimpleMetroArcGauge lSimpleMetroArcGauge = new SimpleMetroArcGauge();
			lSimpleMetroArcGauge.setStyle("-fx-border-color: #000000;");
			for (int i = 0; i <= 20; i++) {
				lSimpleMetroArcGauge.markers().add(new PercentMarker(lSimpleMetroArcGauge, i * 5.0));
			}
			lFlowPane.getChildren().add(lSimpleMetroArcGauge);
			gauges.add(lSimpleMetroArcGauge);
		}
        
        // create scene
        Scene scene = new Scene(lFlowPane, 1500, 900);
        scene.getStylesheets().add(this.getClass().getResource(this.getClass().getSimpleName()+ ".css").toExternalForm());

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
					for (SimpleMetroArcGauge g : gauges) {
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


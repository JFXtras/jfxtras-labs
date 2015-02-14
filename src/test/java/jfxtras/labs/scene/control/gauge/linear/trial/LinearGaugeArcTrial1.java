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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;
import jfxtras.labs.scene.control.gauge.linear.PercentSegment;
import jfxtras.labs.scene.control.gauge.linear.Segment;

/**
 * @author Tom Eugelink
 */
public class LinearGaugeArcTrial1 extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {

		FlowPane lFlowPane = new FlowPane(10, 10);
		
        // without segments
		{
			final LinearGauge lLinearGauge = new LinearGauge();
			lFlowPane.getChildren().add(lLinearGauge);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
		}
        
        // 10 segments
		{
			final LinearGauge lLinearGauge = new LinearGauge();
			lLinearGauge.getStyleClass().add("colorscheme-green-to-red-10");
			for (int i = 0; i < 10; i++) {
				Segment lSegment = new PercentSegment(lLinearGauge, i * 10.0, (i+1) * 10.0);
				lLinearGauge.segments().add(lSegment);
			}
			lFlowPane.getChildren().add(lLinearGauge);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
		}
        
        // shrunk
		{
			final LinearGauge lLinearGauge = new LinearGauge();
			lLinearGauge.setPrefSize(100.0, 100.0);
			lFlowPane.getChildren().add(lLinearGauge);
			lLinearGauge.setStyle("-fx-border-color: #000000;");
		}
        
        // create scene
        //lBorderPane.setLeft(new Label("AAAAAAA"));
        Scene scene = new Scene(lFlowPane, 900, 900);

        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();	
    }
}


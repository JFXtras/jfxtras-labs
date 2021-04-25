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
package jfxtras.labs.scene.control.scheduler.skin;

import javafx.scene.CacheHint;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.scheduler.Scheduler;
import jfxtras.util.NodeUtil;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class ResourceHeaderPane extends Pane {
    final Scheduler.Resource resource;
    private final LayoutHelp layoutHelp;

    public ResourceHeaderPane(Scheduler.Resource resource, LayoutHelp layoutHelp) {
        this.resource = resource;
        this.layoutHelp = layoutHelp;

        setMouseTransparent(true);

        setCacheHint(CacheHint.QUALITY);
        setCache(true);
        setCacheShape(true);

        construct();
    }

    private void construct() {
        // for debugging setStyle("-fx-border-color:BLUE;-fx-border-width:4px;");
//        setStyle("-fx-border-color:BLUE;-fx-border-width:4px;");
        {
            setId("ResourceHeaderPane" + resource.getId()); // for testing

            Text t = new Text(resource.getName());
            t.xProperty().bind(layoutHelp.timeWidthProperty.subtract(t.getBoundsInParent().getWidth()).subtract(layoutHelp.timeColumnWhitespaceProperty.get() / 2));
//        t.yProperty().bind(layoutHelp.resourceHeightProperty.multiply(lHour));
            t.setTranslateY(t.getBoundsInParent().getHeight()); // move it under the line
            t.getStyleClass().add("HourLabel");
            t.setFontSmoothingType(FontSmoothingType.LCD);
            getChildren().add(t);
        }

        {
            Line l = new Line(0, 10, 100, 10);
            l.setId("bottomLine" + resource.getId());
            l.getStyleClass().add("HalfHourLine");
            l.startXProperty().set(0.0);
            l.endXProperty().bind(NodeUtil.snapXY(layoutHelp.resourceWidthProperty).add(layoutHelp.timeWidthProperty));
            l.startYProperty().bind(NodeUtil.snapXY(layoutHelp.resourceHeightProperty.multiply(1)));
            l.endYProperty().bind(NodeUtil.snapXY(l.startYProperty()));
            getChildren().add(l);
        }
    }


}

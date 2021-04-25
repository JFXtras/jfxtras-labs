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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jfxtras.util.NodeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class DayBodyPane extends Pane {

    /**
     *
     */
    public DayBodyPane(LocalDate localDate, AllEvents allEvents, LayoutHelp layoutHints) {
        this.localDateObjectProperty.set(localDate);
        this.allEvents = allEvents;
        this.layoutHelp = layoutHints;
        construct();
    }

    final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
    final AllEvents allEvents;
    final LayoutHelp layoutHelp;

    /**
     *
     */
    private void construct() {

        // for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");
        getStyleClass().add("Day");
        setId("DayBodyPane" + localDateObjectProperty.get()); // for testing

        // react to changes in the appointments
/*        allEvents.addOnChangeListener(() -> {
            setupAppointments();
        });
        setupAppointments();

        // change the layout related to the size
        widthProperty().addListener((observable) -> {
            relayout();
        });
        heightProperty().addListener((observable) -> {
            relayout();
        });*/

//        setupMouseDrag();

        // for testing
        localDateObjectProperty.addListener((observable) -> {
            setId("DayBody" + localDateObjectProperty.get());
        });
        setId("DayBody" + localDateObjectProperty.get());
    }

    /**
     * @param x scene coordinate
     * @param y scene coordinate
     * @return a localDateTime where nano seconds == 1
     */
    LocalDateTime convertClickInSceneToDateTime(double x, double y) {
        Rectangle r = new Rectangle(NodeUtil.sceneX(this), NodeUtil.sceneY(this), this.getWidth(), this.getHeight());
        if (r.contains(x, y)) {
            LocalDate localDate = localDateObjectProperty.get();
            double lHeightOffset = (y - r.getY());
            int ms = (int) (lHeightOffset * layoutHelp.durationInMSPerPixelProperty.get());
            LocalDateTime localDateTime = localDate.atStartOfDay().plusSeconds(ms / 1000);
            localDateTime = localDateTime.withNano(EventAbstractPane.DRAG_DAY); // we abuse the nano second to deviate body panes from header panes
            return localDateTime;
        }
        return null;
    }

}

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
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class DayHeaderPane extends Pane {


    public DayHeaderPane(LocalDate localDate, AllEvents allEvents, LayoutHelp layoutHelp) {
        this.localDateObjectProperty.set(localDate);
        this.allEvents = allEvents;
        this.layoutHelp = layoutHelp;
        construct();
    }

    final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
    final AllEvents allEvents;
    final LayoutHelp layoutHelp;

    private void construct() {

        // setStyle("-fx-border-color:PINK;-fx-border-width:4px;"); // for debugging
        getStyleClass().add("DayHeader");

        // set day label
        dayText = new Text("?");
        dayText.getStyleClass().add("DayLabel");
        dayText.setX(layoutHelp.paddingProperty.get()); // align left
        dayText.setY(dayText.prefHeight(0));
        getChildren().add(dayText);

        // clip the visible part
        Rectangle lClip = new Rectangle(0, 0, 0, 0);
        lClip.widthProperty().bind(widthProperty().subtract(layoutHelp.paddingProperty.get()));
        lClip.heightProperty().bind(heightProperty());
        dayText.setClip(lClip);

        // react to changes in the calendar by updating the label
        localDateObjectProperty.addListener((observable) -> {
            setLabel();
        });
        setLabel();
    }

    private void setLabel() {
        String lLabel =
//                localDateObjectProperty.get().format(layoutHelp.dayOfWeekDateTimeFormatter)
                 " "
                 + localDateObjectProperty.get().getDayOfMonth();
        dayText.setText(lLabel);

        // for testing
        setId("DayHeader" + localDateObjectProperty.get());
    }

    private Text dayText = new Text("?");

    /**
     *
     */

    final private List<Scheduler.Event> events = new ArrayList<>();

    /**
     * So the out view knows how much room (height) we need
     *
     * @return
     */
    public int getNumberOfWholeDayAppointments() {
        return events.size();
    }

}

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

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GroupedHeaderPane extends Pane {

    private final LayoutHelp layoutHelp;
    private List<LocalDate> displayedLocalDates;

    public GroupedHeaderPane(List<LocalDate> displayedLocalDates, LayoutHelp layoutHelp) {
        this.displayedLocalDates = displayedLocalDates;
        this.layoutHelp = layoutHelp;

        construct();
    }

    private void construct() {

        int prevMonth = 0;
        int prevYear = 0;
        int prevDays = 0;
        for (int j = 0; j < displayedLocalDates.size(); j++) {

            prefWidthProperty().bind(layoutHelp.timeWidthProperty.add(layoutHelp.resourceWidthProperty));
            LocalDate curDate = displayedLocalDates.get(j);

            if (curDate.getMonthValue() != prevMonth || curDate.getYear() != prevYear) {
                // Collect all days in the current month for getting days count
                List<LocalDate> localDates = displayedLocalDates.stream()
                        .filter(c -> c.getMonthValue() == curDate.getMonthValue() && c.getYear() == curDate.getYear())
                        .collect(Collectors.toList());

                prevMonth = curDate.getMonthValue();
                prevYear = curDate.getYear();


                Text dayText = new Text(curDate.getMonth() + ", " + curDate.getYear());
                dayText.getStyleClass().add("DayLabel");
                TilePane tilePane = new TilePane(dayText);
                tilePane.getStyleClass().add("GroupedHeader");
                tilePane.setAlignment(Pos.CENTER);
                tilePane.prefWidthProperty().bind(layoutHelp.dayWidthProperty.multiply(localDates.size()));
                tilePane.prefHeightProperty().bind(heightProperty());
                tilePane.layoutXProperty().bind(layoutHelp.timeWidthProperty.add(layoutHelp.dayWidthProperty.multiply(prevDays)));
                /*hBox.layoutXProperty().bind(layoutHelp.dayWidthProperty.multiply(localDates.size())
                        .divide(2)
                        .add(layoutHelp.timeWidthProperty)
                        .add(layoutHelp.dayWidthProperty.multiply(prevDays)));*/
                getChildren().add(tilePane);


                prevDays += localDates.size();
            }
        }
    }
}

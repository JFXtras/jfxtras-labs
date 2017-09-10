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

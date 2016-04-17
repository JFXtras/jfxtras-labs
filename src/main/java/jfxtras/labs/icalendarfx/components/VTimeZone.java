package jfxtras.labs.icalendarfx.components;

import javafx.collections.ObservableList;

public interface VTimeZone extends VComponentRepeatable
{
    ObservableList<StandardOrSavings> getStandardOrDaylight();
    void setStandardOrDaylight(ObservableList<StandardOrSavings> properties);
}

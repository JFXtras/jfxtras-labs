package jfxtras.labs.icalendar.components;

import javafx.collections.ObservableList;

public interface VTimeZone extends VComponentRepeatable
{
    ObservableList<StandardOrSavings> getStandardOrDaylight();
    void setStandardOrDaylight(ObservableList<StandardOrSavings> properties);
}

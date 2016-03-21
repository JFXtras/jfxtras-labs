package jfxtras.labs.icalendar.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class PropertyBase implements Property
{
    // Uses lazy initialization because its rarely used
    @Override
    public ObservableList<Object> getOtherParameters()
    {
        if (otherParameters == null)
        {
            otherParameters = FXCollections.observableArrayList();
        }
        return otherParameters;
    }
    ObservableList<Object> otherParameters;
    @Override
    public void setOtherParameters(ObservableList<Object> other)
    {
        this.otherParameters = other;
    }

}

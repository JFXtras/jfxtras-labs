package jfxtras.labs.icalendar.parameters;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class ParameterTextBase implements Parameter
{
    @Override
    public String getValue() { return value.get(); }
    public StringProperty valueProperty() { return value; }
    private StringProperty value = new SimpleStringProperty(this, name);
    public void setValue(String value) { this.value.set(value); }
}

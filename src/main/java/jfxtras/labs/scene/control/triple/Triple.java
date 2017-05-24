package jfxtras.labs.scene.control.triple;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Triple
{
    private final StringProperty name = new SimpleStringProperty(this, "name");
    public StringProperty labelProperty() { return name; }
    public void setName(String label) { this.name.set(label); }
    public String getName() { return name.getValue(); }
    public Triple withName(String value) { setName(value); return this; }

    private final StringProperty value;
    public StringProperty valueProperty() { return value; }
    public String getValue() { return value.getValue(); }
    public void setValue(String value) {
                    this.value.set(value);
    }
    public boolean isEmpty() { return value.getValue() == null; }
    public Triple withValue(String value) { setValue(value); return this; }

    private final BooleanProperty primary = new SimpleBooleanProperty(this, "primary", false);
    public BooleanProperty primaryProperty() { return primary; }
    public void setPrimary(Boolean primary) { this.primary.set(primary); }
    public Boolean isPrimary() { return primary.getValue(); }
    public Triple withPrimary(boolean value) { setPrimary(value); return this; }
    
    // Constructor
    public Triple(String valueName)
    {
        value = new SimpleStringProperty(this, valueName);
    }
    
	@Override
	public String toString() {
		return "Triple [label=" + getName() + ", value=" + getValue() + ", primary=" + isPrimary() + "]";
	}
}

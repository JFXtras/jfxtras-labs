package jfxtras.labs.scene.control.triple;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Triple
{
    private final StringProperty label = new SimpleStringProperty(this, "label");
    public StringProperty labelProperty() { return label; }
    public void setLabel(String label) { this.label.set(label); }
    public String getName() { return label.getValue(); }
    public Triple withLabel(String value) { setLabel(value); return this; }

    private final StringProperty value;
    public StringProperty valueProperty() { return value; }
    public String getValue() { return value.getValue(); }
    public void setValue(String value) {
//        if (value != null) {
//            boolean ok = validateValue.test(value);
//            System.out.println("value ok:" + ok + " " + value);
//            if (ok)
//            {
//                if (this.value == null)
//                    this.value.set(value);
//                else
                    this.value.set(value);
//            }
//            return ok;
//        }
//        return false;
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
}

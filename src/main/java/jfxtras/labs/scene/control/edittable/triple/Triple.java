package jfxtras.labs.scene.control.edittable.triple;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Triple<A,B,C>
{
    private final ObjectProperty<A> name = new SimpleObjectProperty<A>(this, "name");
    public ObjectProperty<A> nameProperty() { return name; }
    public void setName(A label) { this.name.set(label); }
    public A getName() { return name.getValue(); }
    public Triple<A,B,C> withName(A value) { setName(value); return this; }

    private final ObjectProperty<B> value = new SimpleObjectProperty<B>(this, "value");
    public ObjectProperty<B> valueProperty() { return value; }
    public B getValue() { return value.getValue(); }
    public void setValue(B value) {
                    this.value.set(value);
    }
    public boolean isEmpty() { return value.getValue() == null; }
    public Triple<A,B,C> withValue(B value) { setValue(value); return this; }

    private final ObjectProperty<C> primary = new SimpleObjectProperty<C>(this, "primary");
    public ObjectProperty<C> primaryProperty() { return primary; }
    public void setPrimary(C primary) { this.primary.set(primary); }
    public C isPrimary() { return primary.getValue(); }
    public Triple<A,B,C> withPrimary(C value) { setPrimary(value); return this; }
        
	@Override
	public String toString() {
		return "Triple [label=" + getName() + ", value=" + getValue() + ", primary=" + isPrimary() + "]";
	}
}

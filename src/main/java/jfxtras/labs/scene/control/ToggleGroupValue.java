package jfxtras.labs.scene.control;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * An extended ToggleGroup that adds a value property.
 * Toggles should be added to this group using the add method, which takes a toggle and a value. 
 * Whenever the selected toggle changes, the corresponding value is set in the value property.
 * Vice versa, when the value property is set, the corresponding toggle is selected.
 * Note: of course values have to be unique and null is used for when no toggle is selected.
 * 
 * @author Tom Eugelink
 *
 * @param <T>
 */
public class ToggleGroupValue<T> extends ToggleGroup
{
	/**
	 * 
	 */
	public ToggleGroupValue()
	{
		construct();
	}
	
	/**
	 * 
	 */
	private void construct()
	{
		selectedToggleProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable toggleProperty)
			{
				// get selected toggle
				Toggle lToggle = selectedToggleProperty().get();
				if (lToggle == null) 
				{
					valueObjectProperty.set(null);
				}
				else
				{
					T lValue = toggleToValue.get(lToggle);
					valueObjectProperty.set( lValue );
				}
			}
		});
	}
	private final Map<Toggle, T> toggleToValue = new HashMap<>();
	private final Map<T, Toggle> valueToToggle = new HashMap<>();
	
	/**
	 * 
	 * @param toggle
	 * @param value
	 */
	public void add(Toggle toggle, T value)
	{
		toggle.setToggleGroup(this);
		toggleToValue.put(toggle, value);
		valueToToggle.put(value, toggle);
	}
	
	/**
	 * 
	 * @param toggle
	 */
	public void remove(Toggle toggle)
	{
		toggle.setToggleGroup(null);
		T lValue = toggleToValue.remove(toggle);
		valueToToggle.remove(lValue);
	}
	
	/** Value: */
	public ObjectProperty<T> valueProperty() { return this.valueObjectProperty; }
	final private ObjectProperty<T> valueObjectProperty = new SimpleObjectProperty<T>(this, "value", null)
	{
		public void set(T value)
		{
			// do it
			super.set(value);
			
			// set toggle if required
			Toggle lToggle = valueToToggle.get(value);
			if (getSelectedToggle() != lToggle)
			{
				selectToggle(lToggle);
			}
		}
	};
	// java bean API
	public T getValue() { return this.valueObjectProperty.getValue(); }
	public void setValue(T value) { this.valueObjectProperty.setValue(value); }
	public ToggleGroupValue<T> withValue(T value) { setValue(value); return this; }
}

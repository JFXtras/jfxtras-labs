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
					T lValue = (T)lToggle.getUserData();
					valueObjectProperty.set( lValue );
				}
			}
		});
// totally dummy listener just which prevents the other to get unregistered		
valueProperty().addListener(new InvalidationListener()
{
	@Override
	public void invalidated(Observable arg0)
	{
		System.out.println("value invalidated " + valueObjectProperty);
	}
});
	}
	
	/**
	 * Convenience method for toggle's setToggleGroup and setUserData.
	 * @param toggle
	 * @param value
	 */
	public void add(Toggle toggle, T value)
	{
		toggle.setToggleGroup(this);
		toggle.setUserData(value);
	}
	
	/** Value: */
	public ObjectProperty<T> valueProperty() { return this.valueObjectProperty; }
	final private ObjectProperty<T> valueObjectProperty = new SimpleObjectProperty<T>(this, "value", null)
	{
		public void set(T value)
		{
//System.out.println("set " + value);			
			// do it
			super.set(value);

			// if null
			if (value == null)
			{
				// deselect
				selectToggle(null);
				return;
			}

			// scan all toggles
			for (Toggle lToggle : getToggles())
			{
				// if user data is equal 
				if (lToggle.getUserData() != null && lToggle.getUserData().equals(value))
				{
					// set toggle if required
					if (getSelectedToggle() != lToggle)
					{
						selectToggle(lToggle);
						return;
					}
				}
			}
		}
	};
	// java bean API
	public T getValue() { return this.valueObjectProperty.getValue(); }
	public void setValue(T value) { this.valueObjectProperty.setValue(value); }
	public ToggleGroupValue<T> withValue(T value) { setValue(value); return this; }
}

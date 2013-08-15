package jfxtras.labs.scene.control;

public class Control<T> extends javafx.scene.control.Control
{
	// ==================================================================================================================
	// CONSTRUCTOR

	public Control()
	{
		super();
	}

	
	// ==================================================================================================================
	// PROPERTIES
	
	/** style */
	public T withStyle(String value) { setStyle(value); return (T)this; }
}

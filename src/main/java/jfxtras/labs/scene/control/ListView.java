package jfxtras.labs.scene.control;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

public class ListView<T> extends javafx.scene.control.ListView<T>
{
	// =====================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public ListView()
	{
		super();
		construct();
	}

	/**
	 * 
	 * @param arg0
	 */
	public ListView(ObservableList<T> arg0)
	{
		super(arg0);
		construct();
	}

	/**
	 * 
	 */
	private void construct()
	{
		constructSelectedItem();
	}
	
	
	// =====================================================================================================
	// BINDABLE SELECTED ITEM PROPERTY
	
	/** 
	 * A direct accessable and tywo way bindable selected item property (unlike the one in selection model). 
	 * If in multi select mode, this will be the last selected item.
	 * If you bind bidirectional to this property in multi selected mode, it will keep selecting just one item. 
	 */
	public ObjectProperty<T> selectedItemProperty() { return this.selectedItemObjectProperty; }
	final private ObjectProperty<T> selectedItemObjectProperty = new SimpleObjectProperty<T>(this, "selectedItem", null);
	// java bean API
	public T getSelectedItem() { return this.selectedItemObjectProperty.getValue(); }
	public void setSelectedItem(T value) { this.selectedItemObjectProperty.setValue(value); }
	public ListView<T> withSelectedItem(T value) { setSelectedItem(value); return this; }
	// construct
	private void constructSelectedItem()
	{
		// when the selectedItem in the selectionModel changes, update our selectedItem
		getSelectionModel().selectedItemProperty().addListener(new InvalidationListener()
		{			
			@Override
			public void invalidated(Observable arg0)
			{
				if (getSelectionModel().selectedItemProperty().get() != selectedItemObjectProperty.get())
				{
					selectedItemObjectProperty.set( getSelectionModel().selectedItemProperty().get() );
				}
			}
		});
		// and vice versa
		selectedItemObjectProperty.addListener(new InvalidationListener()
		{			
			@Override
			public void invalidated(Observable arg0)
			{
				if (getSelectionModel().selectedItemProperty().get() != selectedItemObjectProperty.get())
				{
					getSelectionModel().select( selectedItemObjectProperty.get() );
				}
			}
		});
	}
	
}

package jfxtras.labs.internal.scene.control.skin;

import com.sun.javafx.scene.control.skin.ListViewSkin;

/**
 * We need to use a custom skin, so we can access the protected handleControlPropertyChanged method.
 * @author Tom Eugelink
 *
 */
public class ListViewSkinJFXtras<T> extends ListViewSkin<T>
{
	public ListViewSkinJFXtras(javafx.scene.control.ListView<T> skinnable)
	{
		super(skinnable);
	}

	public void refresh()
	{
		// make it recreated all the cells
		super.handleControlPropertyChanged("CELL_FACTORY");
	}
}
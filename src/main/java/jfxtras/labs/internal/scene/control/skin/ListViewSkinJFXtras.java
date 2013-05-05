package jfxtras.labs.internal.scene.control.skin;

import com.sun.javafx.scene.control.skin.ListViewSkin;

/**
 * We need to use a custom skin, so we can access the protected flow variable.
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
		// make the skin recreate all the cells
		flow.recreateCells();
	}
}
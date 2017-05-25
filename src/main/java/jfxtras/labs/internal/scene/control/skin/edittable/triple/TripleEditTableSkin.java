package jfxtras.labs.internal.scene.control.skin.edittable.triple;

import javafx.collections.ObservableList;
import jfxtras.labs.scene.control.edittable.triple.Triple;

public interface TripleEditTableSkin<A,B,C>
{
	public ObservableList<Triple<A,B,C>> getTableList();
}

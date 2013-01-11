/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxtras.labs.util;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.scene.control.window.Clipboard;
import jfxtras.labs.scene.control.window.SelectableNode;

/**
 * Window utility class.
 *
 * This class contains several methods for managing window nodes (selection
 * etc.).
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class WindowUtil {

    private static Clipboard clipboard;

    /**
     * Returns the default clipboard (used for window selection etc.).
     *
     * @return the default clipboard
     *
     * @see
     * jfxtras.labs.util.event.MouseControlUtil#addSelectionRectangleGesture(javafx.scene.Parent,
     * javafx.scene.shape.Rectangle)
     * @see jfxtras.labs.scene.control.window.SelectableNode
     */
    public static Clipboard getDefaultClipboard() {

        if (clipboard == null) {
            clipboard = new ClipboardImpl();
        }

        return clipboard;
    }
}

/**
 * Default clipboard implementation.
 * 
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class ClipboardImpl implements Clipboard {

    private final ObservableList<SelectableNode> items =
            FXCollections.observableArrayList();

    @Override
    public boolean select(SelectableNode n, boolean selected) {
        if (n.requestSelection(selected)) {
            if (selected) {
                items.add(n);
            } else {
                items.remove(n);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public ObservableList<SelectableNode> getSelectedItems() {
        return items;
    }

    @Override
    public void unselectAll() {

        List<SelectableNode> unselectList = new ArrayList<>();
        unselectList.addAll(items);

        for (SelectableNode sN : unselectList) {
            select(sN, false);
        }
    }
}
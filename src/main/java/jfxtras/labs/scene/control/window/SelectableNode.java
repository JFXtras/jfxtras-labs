/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxtras.labs.scene.control.window;

/**
 * A node must implement this interface to be selectable. Usually, nodes/windows
 * are selected via selection rectangle gesture.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 *
 * @see
 * jfxtras.labs.util.event.MouseControlUtil#addSelectionRectangleGesture(javafx.scene.Parent,
 * javafx.scene.shape.Rectangle)
 */
public interface SelectableNode {

    /**
     * Requests selection/deselection.
     *
     * @param select defines whether to select or deselect the node
     * @return <code>true</code> if request is accepted;<code>false</code>
     * otherwise
     */
    public boolean requestSelection(boolean select);
}

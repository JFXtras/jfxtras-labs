package jfxtras.labs.util;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class NodeUtil {

    /**
     *
     * @param node
     * @return The X screen coordinate of the node.
     */
    static public double screenX(Node node) {
        return node.localToScene(node.getBoundsInLocal()).getMinX() + node.getScene().getX() + node.getScene().getWindow().getX();

    }

    /**
     *
     * @param node
     * @return The Y screen coordinate of the node.
     */
    static public double screenY(Node node) {
        return node.localToScene(node.getBoundsInLocal()).getMinY() + node.getScene().getY() + node.getScene().getWindow().getY();
    }

    /**
     * Removes the specified node from its parent.
     * @param n the node to remove
     */
    public static void removeFromParent(Node n) {
        if (n.getParent() instanceof Group) {
            ((Group) n.getParent()).getChildren().remove(n);
        } else if (n.getParent() instanceof Region) {
            ((Pane) n.getParent()).getChildren().remove(n);
        }
    }
}

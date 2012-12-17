/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxtras.labs.util.event;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * This is a utility class that provides methods for mouse gesture control.
 * Currently, it can be used to make nodes draggable.
 * 
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class MouseControlUtil {
    
    // no instanciation allowed
    private MouseControlUtil() {
        throw new AssertionError(); // not in this class either!
    }

    /**
     * Makes a node draggable via mouse gesture.
     *
     * <p> <b>Note:</b> Existing handlers will be replaced!</p>
     *
     * @param n the node that shall be made draggable
     */
    public static void makeDraggable(final Node n) {

        makeDraggable(n, null, null);
    }
    
    
    /**
     * Makes a node draggable via mouse gesture.
     *
     * <p> <b>Note:</b> Existing handlers will be replaced!</p>
     *
     * @param n the node that shall be made draggable
     * @param dragHandler additional drag handler
     * @param pressHandler additional press handler
     */
    public static void makeDraggable(final Node n,
            EventHandler<MouseEvent> dragHandler,
            EventHandler<MouseEvent> pressHandler) {
        
        EventHandlerGroup<MouseEvent> dragHandlerGroup = new EventHandlerGroup<>();
        EventHandlerGroup<MouseEvent> pressHandlerGroup = new EventHandlerGroup<>();
        
        if (dragHandler!=null) {
            dragHandlerGroup.addHandler(dragHandler);
        }
        
        if (pressHandler!=null) {
            pressHandlerGroup.addHandler(pressHandler);
        }
        
        n.setOnMouseDragged(dragHandlerGroup);
        n.setOnMousePressed(pressHandlerGroup);
        
        n.layoutXProperty().unbind();
        n.layoutYProperty().unbind();
        
        _makeDraggable(n, dragHandlerGroup, pressHandlerGroup);
    }

//    public static void makeResizable(Node n) {
//        
//    }

    private static void _makeDraggable(
            final Node n,
            EventHandlerGroup<MouseEvent> dragHandler,
            EventHandlerGroup<MouseEvent> pressHandler) {


        DraggingControllerImpl draggingController =
                new DraggingControllerImpl();
        draggingController.apply(n, dragHandler, pressHandler);
    }
}

class DraggingControllerImpl {

    private double nodeX;
    private double nodeY;
    private double mouseX;
    private double mouseY;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mousePressedEventHandler;

    public DraggingControllerImpl() {
        //
    }

    public void apply(Node n,
            EventHandlerGroup<MouseEvent> draggedEvtHandler,
            EventHandlerGroup<MouseEvent> pressedEvtHandler) {
        init(n);
        draggedEvtHandler.addHandler(mouseDraggedEventHandler);
        pressedEvtHandler.addHandler(mousePressedEventHandler);
    }

    private void init(final Node n) {
        mouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                performDrag(n, event);

                event.consume();
            }
        };

        mousePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                performDragBegin(n, event);
                event.consume();
            }
        };
    }

    public void performDrag(
            Node n, MouseEvent event) {
        final double parentScaleX = n.getParent().
                localToSceneTransformProperty().getValue().getMxx();
        final double parentScaleY = n.getParent().
                localToSceneTransformProperty().getValue().getMyy();

        // Get the exact moved X and Y

        double offsetX = event.getSceneX() - mouseX;
        double offsetY = event.getSceneY() - mouseY;

        nodeX += offsetX;
        nodeY += offsetY;

        double scaledX = nodeX * 1 / parentScaleX;
        double scaledY = nodeY * 1 / parentScaleY;

        n.setLayoutX(scaledX);
        n.setLayoutY(scaledY);

        // again set current Mouse x AND y position
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    public void performDragBegin(
            Node n, MouseEvent event) {

        final double parentScaleX = n.getParent().
                localToSceneTransformProperty().getValue().getMxx();
        final double parentScaleY = n.getParent().
                localToSceneTransformProperty().getValue().getMyy();

        // record the current mouse X and Y position on Node
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();

        nodeX = n.getLayoutX() * parentScaleX;
        nodeY = n.getLayoutY() * parentScaleY;

        n.toFront();
    }
}

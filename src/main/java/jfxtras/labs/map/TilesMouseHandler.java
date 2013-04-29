package jfxtras.labs.map;

import java.awt.Point;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * This class adds mouse handling for a widget that displays tiles.
 *
 * @author Mario Schröder
 */
public class TilesMouseHandler {

    private MapControlable controlable;

    private boolean adjusted;

    public TilesMouseHandler(MapControlable controlable) {
        this.controlable = controlable;
    }

    /**
     * Sets that object, which publishers mouse events.
     *
     * @param tilesGroup
     */
    public void setEventPublisher(final Group tilesGroup) {

        tilesGroup.setOnScroll(new ScrollEventAdapter());

        tilesGroup.setOnMouseEntered(new MouseEnterAdapter(tilesGroup));
        tilesGroup.setOnMouseExited(new MouseExitAdapter(tilesGroup));
        tilesGroup.setOnMouseMoved(new MouseMovedAdapter());
        tilesGroup.setOnMouseClicked(new MouseClickedAdapter());
        tilesGroup.setOnMousePressed(new MousePressedAdapter());
        tilesGroup.setOnMouseReleased(new MouseReleasedAdapter());
        tilesGroup.setOnMouseDragged(new MouseDraggedAdapter(tilesGroup));
        tilesGroup.setOnMouseDragReleased(new MouseDragReleasedAdapter(tilesGroup));
    }

    private void updateCursorLocationText(ScrollEvent me) {

        updateCursorLocationText(me.getX(), me.getY());
    }

    private void updateCursorLocationText(MouseEvent me) {

        updateCursorLocationText(me.getX(), me.getY());
    }

    private void updateCursorLocationText(double x, double y) {
        
        controlable.setCursorLocationText(x, y);

        if (!adjusted) {
            controlable.adjustCursorLocationText();
            adjusted = true;
        }
    }

    private void moveMap(MouseEvent me) {
        moveMap(new Point((int) me.getX(), (int) me.getY()));
    }

    private void moveMap(Point p) {
        Point lastDragPoint = controlable.getLastDragPoint();
        if (lastDragPoint != null) {
            int diffx = lastDragPoint.x - p.x;
            int diffy = lastDragPoint.y - p.y;
            controlable.moveMap(diffx, diffy);
        }
    }

    private class ScrollEventAdapter implements EventHandler<ScrollEvent> {

        @Override
        public void handle(ScrollEvent se) {
            if (se.getDeltaY() > 0) {
                if (controlable.getZoom() < ZoomBounds.MAX.getValue()) {
                    controlable.zoomIn(new Point((int) se.getX(), (int) se.getY()));
                    updateCursorLocationText(se);
                }
            } else if (controlable.getZoom() > ZoomBounds.Min.getValue()) {
                controlable.zoomOut(new Point((int) se.getX(), (int) se.getY()));
                updateCursorLocationText(se);
            }

        }
    }

    private class MouseClickedAdapter implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent me) {
            if (me.isSecondaryButtonDown()) {
                controlable.zoomIn(new Point((int) me.getX(), (int) me.getY()));
            }
        }
    }

    private class MousePressedAdapter implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent me) {
            if (me.isPrimaryButtonDown()) {
                controlable.setLastDragPoint(null);
                controlable.setMoving(true);
            }
        }
    }

    private class MouseEnterAdapter implements EventHandler<MouseEvent> {

        private Group group;

        MouseEnterAdapter(Group group) {
            this.group = group;
        }

        @Override
        public void handle(MouseEvent e) {
            group.setCursor(Cursor.CROSSHAIR);
            updateCursorLocationText(e);
        }
    }

    private static class MouseExitAdapter implements EventHandler<MouseEvent> {

        private Group group;

        MouseExitAdapter(Group group) {
            this.group = group;
        }

        @Override
        public void handle(MouseEvent e) {
            group.setCursor(Cursor.DEFAULT);
        }
    }

    private class MouseMovedAdapter implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent me) {
            if (me.isPrimaryButtonDown()) {

                Point p = new Point((int) me.getX(), (int) me.getY());
                moveMap(p);
                controlable.setLastDragPoint(p);
            } else {
                updateCursorLocationText(me);
            }

        }
    }

    private class MouseReleasedAdapter implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent me) {
            if (controlable.isMoving()) {
                moveMap(me);
                controlable.setLastDragPoint(null);
                controlable.setMoving(false);
            }
        }
    }

    private class MouseDragReleasedAdapter implements EventHandler<MouseEvent> {
        
        private Group tilesGroup;

        MouseDragReleasedAdapter(Group tilesGroup) {
            this.tilesGroup = tilesGroup;
        }
        
        @Override
        public void handle(MouseEvent me) {

            tilesGroup.setCursor(Cursor.CROSSHAIR);
            if (controlable.isMovementEnabled() && controlable.isMoving()) {
                moveMap(me);
            }
        }
    }

    private class MouseDraggedAdapter implements EventHandler<MouseEvent> {

        private Group tilesGroup;

        MouseDraggedAdapter(Group tilesGroup) {
            this.tilesGroup = tilesGroup;
        }

        @Override
        public void handle(MouseEvent me) {
            if (controlable.isMovementEnabled() && controlable.isMoving() && me.isPrimaryButtonDown()) {
                tilesGroup.setCursor(Cursor.MOVE);
                Point point = controlable.getLastDragPoint();
                if (point == null) {
                    Point p = new Point((int) me.getX(), (int) me.getY());
                    controlable.setLastDragPoint(p);
                    controlable.setMoving(true);
                }
            }
        }
    }
}

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
 * @author Mario Schroeder
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
        tilesGroup.setOnMousePressed(new MousePressedAdapter());
        tilesGroup.setOnMouseReleased(new MouseReleasedAdapter(tilesGroup));
        tilesGroup.setOnMouseDragged(new MouseDraggedAdapter(tilesGroup));
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

    private void setLastDragPoint(MouseEvent me) {
        Point p = new Point((int) me.getX(), (int) me.getY());
        controlable.setLastDragPoint(p);
    }

    private class ScrollEventAdapter implements EventHandler<ScrollEvent> {

        @Override
        public void handle(ScrollEvent se) {
            if (se.getDeltaY() > 0) {
                if (controlable.getZoom() < controlable.getTileSource().getMaxZoom()) {
                    controlable.zoomIn(new Point((int) se.getX(), (int) se.getY()));
                    updateCursorLocationText(se);
                }
            } else if (controlable.getZoom() > controlable.getTileSource().getMinZoom()) {
                controlable.zoomOut(new Point((int) se.getX(), (int) se.getY()));
                updateCursorLocationText(se);
            }

        }
    }

    private class MousePressedAdapter implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent me) {
            if (me.isPrimaryButtonDown()) {
                controlable.setLastDragPoint(new Point((int) me.getX(), (int) me.getY()));
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
                setLastDragPoint(me);
            } else {
                updateCursorLocationText(me);
            }

        }
    }

    private class MouseReleasedAdapter implements EventHandler<MouseEvent> {

        private Group tilesGroup;

        MouseReleasedAdapter(Group tilesGroup) {
            this.tilesGroup = tilesGroup;
        }

        @Override
        public void handle(MouseEvent me) {
            tilesGroup.setCursor(Cursor.CROSSHAIR);
            moveMap(me);
            controlable.setLastDragPoint(null);
        }
    }

    private class MouseDraggedAdapter implements EventHandler<MouseEvent> {

        private Group tilesGroup;

        MouseDraggedAdapter(Group tilesGroup) {
            this.tilesGroup = tilesGroup;
        }

        @Override
        public void handle(MouseEvent me) {
            if (me.isPrimaryButtonDown()) {
                tilesGroup.setCursor(Cursor.MOVE);
                Point point = controlable.getLastDragPoint();
                if (point == null) {
                    setLastDragPoint(me);
                }
            }
        }
    }
}

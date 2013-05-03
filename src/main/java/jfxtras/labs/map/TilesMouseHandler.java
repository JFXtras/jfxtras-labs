package jfxtras.labs.map;

import java.awt.Point;

import javafx.event.EventHandler;
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

        tilesGroup.setOnMousePressed(new MousePressedAdapter());
        tilesGroup.setOnMouseDragged(new MouseDraggedAdapter(tilesGroup));
    }

    private void updateCursorLocationText(ScrollEvent me) {

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
                controlable.setLastDragPoint(new Point((int)me.getX(), (int)me.getY()));
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
            if (me.isPrimaryButtonDown()) {
                moveMap(me);
                
                Point p = new Point((int) me.getX(), (int) me.getY());
                controlable.setLastDragPoint(p);
            }
        }
    }
}

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

	private MapTileable map;

	private Point dragStartPoint;

	private boolean adjusted;	

	/**
	 * Sets that object, which publishers mouse events.
	 * 
	 * @param tilesGroup
	 */
	public void setEventPublisher(MapTileable map) {
		this.map = map;
		addListeners(map.getTilesGroup());
	}

	protected void addListeners(Group tilesGroup) {
		tilesGroup.setOnScroll(new ScrollEventAdapter());
		tilesGroup.setOnMouseEntered(new MouseEnterAdapter(tilesGroup));
		tilesGroup.setOnMouseExited(new MouseExitAdapter(tilesGroup));
		tilesGroup.setOnMouseMoved(new MouseMovedAdapter());
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

		map.setCursorLocationText(x, y);

		if (!adjusted) {
			map.adjustCursorLocationText();
			adjusted = true;
		}
	}

	private void moveMap(MouseEvent me) {
		moveMap(new Point((int) me.getX(), (int) me.getY()));
	}

	private void moveMap(Point p) {
		if (dragStartPoint != null) {
			int diffx = dragStartPoint.x - p.x;
			int diffy = dragStartPoint.y - p.y;
			map.moveMap(diffx, diffy);
		}
	}

	private void setDragStartPoint(MouseEvent me) {
		dragStartPoint = new Point((int) me.getX(), (int) me.getY());
	}

	private boolean isMoveable() {
		return map.isMapMoveable();
	}

	private class ScrollEventAdapter implements EventHandler<ScrollEvent> {

		@Override
		public void handle(ScrollEvent se) {

			Point p = new Point((int) se.getX(), (int) se.getY());
			if (se.getDeltaY() > 0) {
				map.zoomIn(p);
			} else {
				map.zoomOut(p);
			}
			updateCursorLocationText(se);
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
				setDragStartPoint(me);
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
			if (isMoveable()) {
				moveMap(me);
				dragStartPoint = null;
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
			if (me.isPrimaryButtonDown() && isMoveable()) {
				tilesGroup.setCursor(Cursor.MOVE);
				if (dragStartPoint == null) {
					setDragStartPoint(me);
				}
			}
		}
	}
}

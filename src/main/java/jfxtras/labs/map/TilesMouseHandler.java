/**
 * TilesMouseHandler.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

	private MapTilesourceable map;

	private Point dragStartPoint;

	private boolean adjusted;	

	/**
	 * Sets that object, which publishers mouse events.
	 * 
	 * @param tilesGroup
	 */
	public void setEventPublisher(MapTilesourceable map) {
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
			if(isMoveable()){
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

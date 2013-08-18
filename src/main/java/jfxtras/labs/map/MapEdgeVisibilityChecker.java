package jfxtras.labs.map;

import java.awt.Dimension;
import java.awt.Point;

import jfxtras.labs.map.render.TileRenderable;

/**
 * This class provides methods to check if any of the edges of the map
 * is within the a size.
 * @author Mario Schroeder
 *
 */
class MapEdgeVisibilityChecker {
	
	private TileRenderable tileRenderer;
	
	MapEdgeVisibilityChecker(TileRenderable tileRenderer) {
		this.tileRenderer = tileRenderer;
	}
	
	/**
	 * This method returns true if all of the edges is visible.
	 * @param dim Dimension of the outside window
	 * @return true if visible
	 */
	boolean isAllVisible(Dimension dim){
		return isTopEdgeVisible(dim) && isBottomEdgeVisible(dim) && isLeftEdgeVisible(dim) && isRightEdgeVisible(dim);
	}
	
	/**
	 * This method returns true if any of the edges is visible.
	 * @param dim Dimension of the outside window
	 * @return true if visible
	 */
	boolean isAnyVisible(Dimension dim){
		return isTopEdgeVisible(dim) || isBottomEdgeVisible(dim) || isLeftEdgeVisible(dim) || isRightEdgeVisible(dim);
	}
	
	/**
	 * This method returns true if the top edge is visible.
	 * @param dim Dimension of the outside window
	 * @return true if visible
	 */
	boolean isTopEdgeVisible(Dimension dim){

		return isEdgeVisible(new TopEdgeVisible(), dim);
	}
	
	/**
	 * This method returns true if the bottom edge is visible.
	 * @param dim Dimension of the outside window
	 * @return true if visible
	 */
	boolean isBottomEdgeVisible(Dimension dim){

		return isEdgeVisible(new BottomEdgeVisible(), dim);
	}
	
	/**
	 * This method returns true if the left edge is visible.
	 * @param dim Dimension of the outside window
	 * @return true if visible
	 */
	boolean isLeftEdgeVisible(Dimension dim){

		return isEdgeVisible(new LeftEdgeVisible(), dim);
	}
	
	/**
	 * This method returns true if the right edge is visible.
	 * @param dim Dimension of the outside window
	 * @return true if visible
	 */
	boolean isRightEdgeVisible(Dimension dim){
		
		return isEdgeVisible(new RightEdgeVisible(), dim);
	}
	
	private boolean isEdgeVisible(MapEdgeVisibility visibility, Dimension dim){
		boolean visible = false;
		Point[] bounds = tileRenderer.getBounds();
		if(bounds != null && bounds.length == 2){
			visible = visibility.isVisible(bounds, dim);
		}
		return visible;
	}
	
	private static class LeftEdgeVisible implements MapEdgeVisibility{
		@Override
		public boolean isVisible(Point[] bounds, Dimension dimension) {
			return bounds[0].getX() > 0;
		}
	}

	private static class RightEdgeVisible implements MapEdgeVisibility{
		@Override
		public boolean isVisible(Point[] bounds, Dimension dimension) {
			return bounds[1].getX() < dimension.getWidth();
		}
	}
	
	private static class TopEdgeVisible implements MapEdgeVisibility{
		@Override
		public boolean isVisible(Point[] bounds, Dimension dimension) {
			return bounds[0].getY() > 0;
		}
	}
	
	private static class BottomEdgeVisible implements MapEdgeVisibility{
		@Override
		public boolean isVisible(Point[] bounds, Dimension dimension) {
			return bounds[1].getY() < dimension.getHeight();
		}
	}
	
	/**
	 * This is an interface to check the condition if any of the map edge is
	 * within the application window.
	 *
	 */
	interface MapEdgeVisibility {

		/**
		 * This method should return true if the edge is visible otherwise false.
		 * @param bounds upper left and lower right points of the actual map
		 * @param dimension size of the window
		 * @return <code>true</code> if edge is visble
		 */
		boolean isVisible(Point [] bounds, Dimension dimension);
	}
}

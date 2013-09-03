/**
 * MapEdgeChecker.java
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

import java.awt.Dimension;
import java.awt.Point;

import jfxtras.labs.map.render.TileRenderable;

/**
 * This class provides methods to check if any of the edges of the map
 * is within the a size.
 * @author Mario Schroeder
 *
 */
class MapEdgeChecker {
	
	/**minimum with of the border required still to be visible*/
	private static final int MIN = 20;
	
	private TileRenderable tileRenderer;
	
	MapEdgeChecker(TileRenderable tileRenderer) {
		this.tileRenderer = tileRenderer;
	}
	
	boolean isOnEdge(Dimension dim){
		return isOnLeftEdge(dim) || isOnRightEdge(dim) 
				|| isOnTopEdge(dim) || isOnBottomEdge(dim);
	}
	
	boolean isOnLeftEdge(Dimension dim){
		return isEdgeVisible(new OnLeftEdge(), dim);
	}
	
	boolean isOnRightEdge(Dimension dim){
		return isEdgeVisible(new OnRightEdge(), dim);
	}
	
	boolean isOnTopEdge(Dimension dim){
		return isEdgeVisible(new OnTopEdge(), dim);
	}
	
	boolean isOnBottomEdge(Dimension dim){
		return isEdgeVisible(new OnBottomEdge(), dim);
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
	
	private static class OnLeftEdge implements MapEdgeVisibility {

		@Override
		public boolean isVisible(Point[] bounds, Dimension dimension) {
			return bounds[1].getX() < MIN;
		}
	}
	
	private static class OnRightEdge implements MapEdgeVisibility {

		@Override
		public boolean isVisible(Point[] bounds, Dimension dimension) {
			return bounds[0].getX() > dimension.getWidth() - MIN;
		}
	}
	
	private static class OnTopEdge implements MapEdgeVisibility {

		@Override
		public boolean isVisible(Point[] bounds, Dimension dimension) {
			return bounds[1].getY() < MIN;
		}
	}
	
	private static class OnBottomEdge implements MapEdgeVisibility {

		@Override
		public boolean isVisible(Point[] bounds, Dimension dimension) {
			return bounds[0].getY() > dimension.getHeight() - MIN;
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

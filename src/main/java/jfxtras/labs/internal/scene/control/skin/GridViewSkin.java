/**
 * GridViewSkin.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.labs.internal.scene.control.skin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.internal.scene.control.behavior.GridViewBehavior;
import jfxtras.labs.scene.control.grid.GridCell;
import jfxtras.labs.scene.control.grid.GridView;
import jfxtras.labs.scene.control.grid.cell.DefaultGridCell;

/**
 * 
 * @author Hendrik Ebbers
 * 
 */
public class GridViewSkin<T> extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<GridView<T>, GridViewBehavior<T>> {

	private ListChangeListener<T> itemsListener;

	private ChangeListener<Number> layoutListener;

	private ChangeListener<ObservableList<T>> itemListChangedListener;

	public GridViewSkin(GridView<T> control) {
		super(control, new GridViewBehavior<>(control));

		layoutListener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				getSkinnable().requestLayout();
			}

		};

		itemsListener = new ListChangeListener<T>() {
			@Override
			public void onChanged(Change<? extends T> change) {
				while (change.next()) {
					int start = change.getFrom();
					int end = change.getTo();

					if (change.wasReplaced() || change.wasAdded()
							|| change.wasPermutated() || change.wasUpdated()) {
						for (int i = start; i < end; i++) {
							if (change.wasReplaced()) {
								replaceCell(i);
							} else if (change.wasAdded()) {
								addCell(i);
							} else if (change.wasPermutated()) {
								// TODO: what to do know??
								updateAllCells();
							} else if (change.wasRemoved()) {
								removeCell(i);
							} else if (change.wasUpdated()) {
								updateCell(i);
							}
						}
					} else if (change.wasRemoved()) {
						for (int i = start; i <= end; i++) {
							removeCell(i);
						}
					}
				}
			}
		};

		itemListChangedListener = new ChangeListener<ObservableList<T>>() {

			@Override
			public void changed(
					ObservableValue<? extends ObservableList<T>> arg0,
					ObservableList<T> oldList, ObservableList<T> newList) {
				if (oldList != null) {
					oldList.removeListener(itemsListener);
				}
				if (newList != null) {
					newList.addListener(itemsListener);
				}
				updateAllCells();
			}
		};

		getSkinnable().itemsProperty().addListener(itemListChangedListener);
		ObservableList<T> currentList = getSkinnable().itemsProperty().get();
		if (currentList != null) {
			currentList.addListener(itemsListener);
		}

		getSkinnable().cellHeightProperty().addListener(layoutListener);
		getSkinnable().cellWidthProperty().addListener(layoutListener);
		getSkinnable().verticalCellSpacingProperty()
				.addListener(layoutListener);
		getSkinnable().horizontalCellSpacingProperty().addListener(
				layoutListener);

		updateAllCells();
	}

	public void updateAllCells() {
		getChildren().clear();
		ObservableList<T> items = getSkinnable().getItems();
		if (items != null) {

			for (int index = 0; index < items.size(); index++) {
				T item = items.get(index);
				GridCell<T> cell = createCell();
				cell.setItem(item);
				cell.updateIndex(index);
				getChildren().add(cell);
			}
		}
		getSkinnable().requestLayout();
	}

	private void removeCell(int index) {
		getChildren().remove(index);
		// TODO: Update Index for all following cells
		getSkinnable().requestLayout();
	}

	private void replaceCell(int index) {
		getChildren().remove(index);
		addCell(index);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateCell(int index) {
		T item = getSkinnable().getItems().get(index);
		((GridCell) getChildren().get(index)).setItem(item);
	}

	private void addCell(int index) {
		T item = getSkinnable().getItems().get(index);
		GridCell<T> cell = createCell();
		cell.setItem(item);
		cell.updateIndex(index);
		getChildren().add(index, cell);
		// TODO: Update Index for all following cells
		getSkinnable().requestLayout();
	}

	private GridCell<T> createCell() {
		GridCell<T> cell;
		if (getSkinnable().getCellFactory() != null) {
			cell = getSkinnable().getCellFactory().call(getSkinnable());
		} else {
			cell = createDefaultCellImpl();
		}
		return cell;
	}

	protected GridCell<T> createDefaultCellImpl() {
		return new DefaultGridCell<T>();
	}

// remove compilation problem
//	@Override
//	protected void layoutChildren() {
//		super.layoutChildren();
//		double currentWidth = getWidth();
//		double cellWidth = getSkinnable().getCellWidth();
//		double cellHeight = getSkinnable().getCellHeight();
//		double horizontalCellSpacing = getSkinnable()
//				.getHorizontalCellSpacing();
//		double verticalCellSpacing = getSkinnable().getVerticalCellSpacing();
//
//		double xPos = 0;
//		double yPos = 0;
//
//		HPos currentHorizontalAlignment = getSkinnable()
//				.getHorizontalAlignment();
//		if (currentHorizontalAlignment != null) {
//			if (currentHorizontalAlignment.equals(HPos.CENTER)) {
//				xPos = (currentWidth % computeCellWidth()) / 2;
//			} else if (currentHorizontalAlignment.equals(HPos.RIGHT)) {
//				xPos = currentWidth % computeCellWidth();
//			}
//		}
//
//		for (Node child : getChildren()) {
//			if (xPos + horizontalCellSpacing + cellWidth
//					+ horizontalCellSpacing > currentWidth) {
//				// new line
//				xPos = 0;
//				if (currentHorizontalAlignment != null) {
//					if (currentHorizontalAlignment.equals(HPos.CENTER)) {
//						xPos = (currentWidth % computeCellWidth()) / 2;
//					} else if (currentHorizontalAlignment.equals(HPos.RIGHT)) {
//						xPos = currentWidth % computeCellWidth();
//					}
//				}
//
//				yPos = yPos + verticalCellSpacing + cellHeight
//						+ verticalCellSpacing;
//			}
//			child.relocate(xPos + horizontalCellSpacing, yPos
//					+ verticalCellSpacing);
//			child.resize(cellWidth, cellHeight);
//			xPos = xPos + horizontalCellSpacing + cellWidth
//					+ horizontalCellSpacing;
//			;
//		}
//	}

// remove compilation problem
//	@Override
//	public Orientation getContentBias() {
//		return Orientation.HORIZONTAL;
//	}

	protected double computeCellWidth() {
		return getSkinnable().cellWidthProperty().doubleValue()
				+ getSkinnable().horizontalCellSpacingProperty().doubleValue()
				+ getSkinnable().horizontalCellSpacingProperty().doubleValue();
	}

	protected double computeCellHeight() {
		return getSkinnable().cellHeightProperty().doubleValue()
				+ getSkinnable().verticalCellSpacingProperty().doubleValue()
				+ getSkinnable().verticalCellSpacingProperty().doubleValue();
	}

	@Override
	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return computeCellHeight();
	}

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return computeCellWidth();
	}

	@Override
	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		return Double.MAX_VALUE;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		return Double.MAX_VALUE;
	}

	@Override
	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		int maxCellsInRow = computeMaxCellsInRow(width);
		int rowCount = (int) Math.floor((double) getSkinnable().getItems()
				.size() / (double) maxCellsInRow);
		return rowCount * computeCellHeight();
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		int maxCellsInColumn = computeMaxCellsInColumn(height);
		int columnCount = (int) Math.floor((double) getSkinnable().getItems()
				.size() / (double) maxCellsInColumn);
		return columnCount * computeCellWidth();
	}

	public int computeRowIndexForItem(int itemIndex) {
		int maxCellsInRow = computeMaxCellsInRow();
		return itemIndex / maxCellsInRow;
	}

	public int computeColumnIndexForItem(int itemIndex) {
		int maxCellsInRow = computeMaxCellsInRow();
		return itemIndex % maxCellsInRow;
	}

	public int computeMaxCellsInRow() {
		return computeMaxCellsInRow(getSkinnable().getWidth());
	}

	public int computeCurrentRowCount() {
		return (int) Math.ceil((double) getSkinnable().getItems().size()
				/ (double) computeMaxCellsInRow());
	}

	public int computeMaxCellsInRow(double width) {
		return Math.max((int) Math.floor(width / computeCellWidth()), 1);
	}

	public int computeMaxCellsInColumn(double height) {
		return Math.max((int) Math.floor(height / computeCellHeight()), 1);
	}
}

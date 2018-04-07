/**
 * GridPaginationHelper.java
 *
 * Copyright (c) 2011-2015, JFXtras
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

package jfxtras.labs.util.grid;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.util.Callback;
import jfxtras.labs.scene.control.grid.GridCell;
import jfxtras.labs.scene.control.grid.GridView;

/**
 * 
 * @author Hendrik Ebbers
 * 
 */
public class GridPaginationHelper<T> {

	private Pagination pagination;

	private ObservableList<T> items;

	private Callback<GridView<T>, GridCell<T>> gridCellFactory;

	private Callback<Integer, Node> pageFactory;

	private DoubleProperty cellWidth;

	private DoubleProperty cellHeight;

	private DoubleProperty horizontalCellSpacing;

	private DoubleProperty verticalCellSpacing;

	private ChangeListener<Number> defaultUpdateListener;

	public GridPaginationHelper(Pagination pagination,
			final ObservableList<T> items,
			Callback<GridView<T>, GridCell<T>> gridCellFactory) {
		this.pagination = pagination;
		this.items = items;
		this.gridCellFactory = gridCellFactory;

		defaultUpdateListener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				update();
			}
		};

		pageFactory = new Callback<Integer, Node>() {

			@Override
			public Node call(Integer arg0) {
				GridView<T> gridView = null;
				gridView = new GridView<>();
				int startIndex = getCellStartIndexForPage(arg0);
				ObservableList<T> currentItems = FXCollections.observableArrayList(
						items.subList(
								startIndex,
								Math.min(startIndex
										+ calcMaxVisibleCellsPerPage(),
										items.size())));
				gridView.setItems(currentItems);
				gridView.setCellFactory(GridPaginationHelper.this.gridCellFactory);

				gridView.cellHeightProperty().bind(
						GridPaginationHelper.this.cellHeightProperty());
				gridView.cellWidthProperty().bind(
						GridPaginationHelper.this.cellWidthProperty());
				gridView.horizontalCellSpacingProperty().bind(
						GridPaginationHelper.this
								.horizontalCellSpacingProperty());
				gridView.verticalCellSpacingProperty()
						.bind(GridPaginationHelper.this
								.verticalCellSpacingProperty());

				gridView.cellHeightProperty()
						.addListener(defaultUpdateListener);
				gridView.cellWidthProperty().addListener(defaultUpdateListener);
				gridView.horizontalCellSpacingProperty().addListener(
						defaultUpdateListener);
				gridView.verticalCellSpacingProperty().addListener(
						defaultUpdateListener);

				return gridView;
			}
		};

		// TODO: this is a hack...
		GridView<T> dummyGridView = new GridView<>();
		cellHeightProperty().setValue(dummyGridView.getCellHeight());
		cellWidthProperty().setValue(dummyGridView.getCellWidth());
		verticalCellSpacingProperty().setValue(
				dummyGridView.getVerticalCellSpacing());
		horizontalCellSpacingProperty().setValue(
				dummyGridView.getHorizontalCellSpacing());

		items.addListener(new ListChangeListener<T>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends T> arg0) {
				update();
			}
		});

		// TODO: http://javafx-jira.kenai.com/browse/RT-21118
		pagination.widthProperty().addListener(defaultUpdateListener);

		// TODO: http://javafx-jira.kenai.com/browse/RT-21118
		pagination.heightProperty().addListener(defaultUpdateListener);

		pagination.setPageFactory(pageFactory);
	}

	private void update() {
		int firstCellOnPage = calcMaxVisibleCellsPerPage()
				* pagination.getCurrentPageIndex();
		pagination.setPageCount(calcPageCount());
		pagination.setCurrentPageIndex((int) Math.floor(firstCellOnPage
				/ calcMaxVisibleCellsPerPage()));
	}

	private int calcMaxVisibleCellsPerPage() {
		return Math.max(1, computeMaxCellsInOneRow() * computeMaxRowsPerPage());
	}

	private int computeMaxRowsPerPage() {
		double cellHeight = getCellHeight() + getVerticalCellSpacing()
				+ getVerticalCellSpacing();
		return (int) Math.floor((pagination.getHeight() - 64) / cellHeight);
	}

	private int computeMaxCellsInOneRow() {
		double cellWidth = getHorizontalCellSpacing() + getCellWidth()
				+ getHorizontalCellSpacing();
		return (int) Math.floor(pagination.getWidth() / cellWidth);
	}

	private int getCellStartIndexForPage(int pageIndex) {
		return calcMaxVisibleCellsPerPage() * pageIndex;
	}

	private int calcPageCount() {
		return (int) Math.floor(items.size() / calcMaxVisibleCellsPerPage());
	}

	public void setHorizontalCellSpacing(double value) {
		horizontalCellSpacingProperty().set(value);
	}

	public double getHorizontalCellSpacing() {
		return horizontalCellSpacing == null ? null : horizontalCellSpacing
				.get();
	}

	public final DoubleProperty horizontalCellSpacingProperty() {
		if (horizontalCellSpacing == null) {
			horizontalCellSpacing = new SimpleDoubleProperty(this,
					"horizontalCellSpacing");
		}
		return horizontalCellSpacing;
	}

	public void setVerticalCellSpacing(double value) {
		verticalCellSpacingProperty().set(value);
	}

	public double getVerticalCellSpacing() {
		return verticalCellSpacing == null ? null : verticalCellSpacing.get();
	}

	public final DoubleProperty verticalCellSpacingProperty() {
		if (verticalCellSpacing == null) {
			verticalCellSpacing = new SimpleDoubleProperty(this,
					"verticalCellSpacing");
		}
		return verticalCellSpacing;
	}

	public final DoubleProperty cellWidthProperty() {
		if (cellWidth == null) {
			cellWidth = new SimpleDoubleProperty(this, "cellWidth");
		}
		return cellWidth;
	}

	public void setCellWidth(double value) {
		cellWidthProperty().set(value);
	}

	public double getCellWidth() {
		return cellWidth == null ? null : cellWidth.get();
	}

	public final DoubleProperty cellHeightProperty() {
		if (cellHeight == null) {
			cellHeight = new SimpleDoubleProperty(this, "cellHeight");
		}
		return cellHeight;
	}

	public void setCellHeight(double value) {
		cellHeightProperty().set(value);
	}

	public double getCellHeight() {
		return cellHeight == null ? null : cellHeight.get();
	}
}

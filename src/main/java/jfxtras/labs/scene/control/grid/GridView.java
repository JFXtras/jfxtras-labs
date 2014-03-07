/**
 * GridView.java
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

package jfxtras.labs.scene.control.grid;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.Control;
import javafx.util.Callback;

/**
 * 
 * @author Hendrik Ebbers
 * 
 */
public class GridView<T> extends Control {

	private ObjectProperty<ObservableList<T>> items;

	private ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactory;

	private DoubleProperty cellWidth;

	private DoubleProperty cellHeight;

	private DoubleProperty horizontalCellSpacing;

	private DoubleProperty verticalCellSpacing;

	private ObjectProperty<HPos> horizontalAlignment;

	public GridView() {
		this(FXCollections.<T> observableArrayList());
	}

	public GridView(ObservableList<T> items) {
		getStyleClass().add("grid-view");
		setItems(items);
	}

	public void setHorizontalCellSpacing(double value) {
		horizontalCellSpacingProperty().set(value);
	}

	public double getHorizontalCellSpacing() {
		return horizontalCellSpacing == null ? 12.0 : horizontalCellSpacing
				.get();
	}

	public final DoubleProperty horizontalCellSpacingProperty() {
//		if (horizontalCellSpacing == null) {
//			horizontalCellSpacing = new SimpleStyleableDoubleProperty(this,
//					"horizontalCellSpacing",
//					StyleableProperties.HORIZONTAL_CELL_SPACING);
//		}
		return horizontalCellSpacing;
	}

	public void setVerticalCellSpacing(double value) {
		verticalCellSpacingProperty().set(value);
	}

	public double getVerticalCellSpacing() {
		return verticalCellSpacing == null ? 12.0 : verticalCellSpacing.get();
	}

	public final DoubleProperty verticalCellSpacingProperty() {
//		if (verticalCellSpacing == null) {
//			verticalCellSpacing = new SimpleStyleableDoubleProperty(this,
//					"verticalCellSpacing",
//					StyleableProperties.VERTICAL_CELL_SPACING);
//		}
		return verticalCellSpacing;
	}

	public final DoubleProperty cellWidthProperty() {
//		if (cellWidth == null) {
//			cellWidth = new SimpleStyleableDoubleProperty(this, "cellWidth",
//					StyleableProperties.CELL_WIDTH);
//		}
		return cellWidth;
	}

	public void setCellWidth(double value) {
		cellWidthProperty().set(value);
	}

	public double getCellWidth() {
		return cellWidth == null ? 64.0 : cellWidth.get();
	}

	public final DoubleProperty cellHeightProperty() {
//		if (cellHeight == null) {
//			cellHeight = new SimpleStyleableDoubleProperty(this, "cellHeight",
//					StyleableProperties.CELL_HEIGHT);
//		}
		return cellHeight;
	}

	public void setCellHeight(double value) {
		cellHeightProperty().set(value);
	}

	public double getCellHeight() {
		return cellHeight == null ? 64.0 : cellHeight.get();
	}

	public final ObjectProperty<HPos> horizontalAlignmentProperty() {
//		if (horizontalAlignment == null) {
//			horizontalAlignment = new StyleableObjectProperty<HPos>(HPos.CENTER) {
//
//				@SuppressWarnings("rawtypes")
//				@Override
//				public StyleableProperty getStyleableProperty() {
//					return StyleableProperties.HORIZONTAL_ALIGNMENT;
//				}
//
//				@Override
//				public Object getBean() {
//					return GridView.this;
//				}
//
//				@Override
//				public String getName() {
//					return "horizontalAlignment";
//				}
//			};
//		}
		return horizontalAlignment;
	}

	public final void setHorizontalAlignment(HPos value) {
		horizontalAlignmentProperty().set(value);
	}

	public final HPos getHorizontalAlignment() {
		return horizontalAlignment == null ? HPos.CENTER : horizontalAlignment.get();
	}

	public final ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactoryProperty() {
		if (cellFactory == null) {
			cellFactory = new SimpleObjectProperty<Callback<GridView<T>, GridCell<T>>>(
					this, "cellFactory");
		}
		return cellFactory;
	}

	public final void setCellFactory(Callback<GridView<T>, GridCell<T>> value) {
		cellFactoryProperty().set(value);
	}

	public final Callback<GridView<T>, GridCell<T>> getCellFactory() {
		return cellFactory == null ? null : cellFactory.get();
	}

	public final void setItems(ObservableList<T> value) {
		itemsProperty().set(value);
	}

	public final ObservableList<T> getItems() {
		return items == null ? null : items.get();
	}

	public final ObjectProperty<ObservableList<T>> itemsProperty() {
		if (items == null) {
			items = new SimpleObjectProperty<ObservableList<T>>(this, "items");
		}
		return items;
	}

	@Override
	protected String getUserAgentStylesheet() {
		return GridView.class.getResource("gridview.css").toExternalForm();
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private static class StyleableProperties {

//		private static final StyleableProperty<GridView, HPos> HORIZONTAL_ALIGNMENT = new StyleableProperty<GridView, HPos>(
//				"-fx-horizontal-alignment", new EnumConverter<HPos>(HPos.class),
//				HPos.CENTER) {
//
//			@Override
//			public boolean isSettable(GridView n) {
//				return n.horizontalAlignment == null || !n.horizontalAlignment.isBound();
//			}
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public WritableValue<HPos> getWritableValue(GridView n) {
//				return n.horizontalAlignmentProperty();
//			}
//
//			@Override
//			public HPos getInitialValue(GridView n) {
//				return HPos.CENTER;
//			}
//		};
//
//		private static final StyleableProperty<GridView, Number> CELL_WIDTH = new StyleableProperty<GridView, Number>(
//				"-fx-cell-width", SizeConverter.getInstance(), 64.0) {
//
//			@Override
//			public boolean isSettable(GridView n) {
//				return n.cellWidth == null || !n.cellWidth.isBound();
//			}
//
//			@Override
//			public WritableValue<Number> getWritableValue(GridView n) {
//				return n.cellWidthProperty();
//			}
//		};
//
//		private static final StyleableProperty<GridView, Number> CELL_HEIGHT = new StyleableProperty<GridView, Number>(
//				"-fx-cell-height", SizeConverter.getInstance(), 64.0) {
//
//			@Override
//			public boolean isSettable(GridView n) {
//				return n.cellHeight == null || !n.cellHeight.isBound();
//			}
//
//			@Override
//			public WritableValue<Number> getWritableValue(GridView n) {
//				return n.cellHeightProperty();
//			}
//		};
//
//		private static final StyleableProperty<GridView, Number> HORIZONTAL_CELL_SPACING = new StyleableProperty<GridView, Number>(
//				"-fx-horizontal-cell-spacing", SizeConverter.getInstance(),
//				12.0) {
//
//			@Override
//			public boolean isSettable(GridView n) {
//				return n.horizontalCellSpacing == null
//						|| !n.horizontalCellSpacing.isBound();
//			}
//
//			@Override
//			public WritableValue<Number> getWritableValue(GridView n) {
//				return n.horizontalCellSpacingProperty();
//			}
//		};
//
//		private static final StyleableProperty<GridView, Number> VERTICAL_CELL_SPACING = new StyleableProperty<GridView, Number>(
//				"-fx-vertical-cell-spacing", SizeConverter.getInstance(), 12.0) {
//
//			@Override
//			public boolean isSettable(GridView n) {
//				return n.verticalCellSpacing == null
//						|| !n.verticalCellSpacing.isBound();
//			}
//
//			@Override
//			public WritableValue<Number> getWritableValue(GridView n) {
//				return n.verticalCellSpacingProperty();
//			}
//		};
//
//		private static final List<StyleableProperty> STYLEABLES;
//		static {
//			final List<StyleableProperty> styleables = new ArrayList<StyleableProperty>(
//					Control.impl_CSS_STYLEABLES());
//			Collections.addAll(styleables, HORIZONTAL_ALIGNMENT, CELL_HEIGHT, CELL_WIDTH,
//					HORIZONTAL_CELL_SPACING, VERTICAL_CELL_SPACING);
//			STYLEABLES = Collections.unmodifiableList(styleables);
//		}
	}

//	@SuppressWarnings("rawtypes")
//	public static List<StyleableProperty> impl_CSS_STYLEABLES() {
//		return GridView.StyleableProperties.STYLEABLES;
//	}

//	@SuppressWarnings("rawtypes")
//	public List<StyleableProperty> impl_getStyleableProperties() {
//		return impl_CSS_STYLEABLES();
//	}
}

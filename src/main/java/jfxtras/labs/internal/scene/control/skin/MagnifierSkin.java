/**
 * 
 */
package jfxtras.labs.internal.scene.control.skin;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.PerspectiveCameraBuilder;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotResult;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.transform.Scale;
import javafx.stage.Popup;
import javafx.util.Callback;
import jfxtras.labs.internal.scene.control.behavior.MagnifierBehavior;
import jfxtras.labs.scene.control.Magnifier;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * Skin implementation for the {@link Magnifier} control.
 * 
 * @author SaiPradeepDandem
 */
public class MagnifierSkin extends SkinBase<Magnifier, MagnifierBehavior> {

	private Scene scene;
	private WritableImage writeImg;
	private final double shift = 1.0D;
	private final double MIN_RADIUS = 50.0D;
	private final double RADIUS_DELTA = 5.0D;
	private final double ZOOM_DELTA = 0.3D;
	private final double ZOOM_MAX = 10.0D;
	private final double ZOOM_MIN = 1.0D;
	private DoubleProperty localRadius = new SimpleDoubleProperty();
	private DoubleProperty localScaleFactor = new SimpleDoubleProperty();
	private DoubleProperty prevX = new SimpleDoubleProperty();
	private DoubleProperty prevY = new SimpleDoubleProperty();

	private Callback<SnapshotResult, java.lang.Void> callBack;
	private SnapshotParameters param;
	private ImageView snapView;
	private Viewer viewer;

	/**
	 * Instantiates the skin implementation for {@link Magnifier}.
	 * 
	 * @param magnifier
	 *            Instance of {@link Magnifier} control.
	 */
	public MagnifierSkin(Magnifier magnifier) {
		super(magnifier, new MagnifierBehavior(magnifier));
		initialize();
	}

	private void initialize() {
		final DoubleProperty frameWidthProperty = getSkinnable().frameWidthProperty();
		final DoubleProperty scopeLineWidthProperty = getSkinnable().scopeLineWidthProperty();
		final BooleanProperty scopeLinesVisibleProperty = getSkinnable().scopeLinesVisibleProperty();

		// Adding listener to control "radiusProperty" to add the value to "localRadius".
		getSkinnable().radiusProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				localRadius.set(getSkinnable().getRadius());
			}
		});
		localRadius.set(getSkinnable().getRadius());

		// Adding listener to control "scaleFactorProperty" to add the value to "localScaleFactor".
		getSkinnable().scaleFactorProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				localScaleFactor.set(getSkinnable().getScaleFactor());
			}
		});
		localScaleFactor.set(getSkinnable().getScaleFactor());

		snapView = new ImageView();
		callBack = new Callback<SnapshotResult, Void>() {
			@Override
			public Void call(SnapshotResult result) {
				return null;
			}
		};
		final Scale scale = new Scale();
		scale.xProperty().bind(localScaleFactor);
		scale.yProperty().bind(localScaleFactor);
		param = new SnapshotParameters();
		param.setCamera(PerspectiveCameraBuilder.create().fieldOfView(45).build());
		param.setDepthBuffer(true);
		param.setTransform(scale);

		final StackPane mainContent = StackPaneBuilder.create().build();
		final Circle frame = CircleBuilder.create().styleClass("magnifier-frame").build();
		frame.radiusProperty().bind(localRadius.add(frameWidthProperty));
		
		final Circle cClip = CircleBuilder.create().build();
		cClip.radiusProperty().bind(localRadius);
		cClip.translateXProperty().bind(localRadius);
		cClip.translateYProperty().bind(localRadius);

		viewer = new Viewer(localRadius, localRadius);
		viewer.setClip(cClip);

		final Line vL = LineBuilder.create().startX(0).startY(0).endX(0).styleClass("magnifier-vLine").build();
		vL.strokeWidthProperty().bind(scopeLineWidthProperty);
		vL.visibleProperty().bind(scopeLinesVisibleProperty);
		vL.endYProperty().bind(localRadius.multiply(2));

		final Line hL = LineBuilder.create().startX(0).startY(0).endY(0).styleClass("magnifier-hLine").build();
		hL.strokeWidthProperty().bind(scopeLineWidthProperty);
		hL.visibleProperty().bind(scopeLinesVisibleProperty);
		hL.endXProperty().bind(localRadius.multiply(2));

		// Adding all parts in a container.
		mainContent.getChildren().addAll(frame, viewer, vL, hL);

		final Popup popUp = new Popup();
		popUp.getContent().add(mainContent);

		// Adding mask implementation. The below code is responsible to not access the contents when magnifier is shown.
		final StackPane mask = new StackPane();
		getChildren().add(mask);
		final SimpleBooleanProperty maskFlag = new SimpleBooleanProperty(true);
		getChildren().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				if (getSkinnable().isActive() && maskFlag.get()) {
					maskFlag.set(false);
					getChildren().remove(mask);
					getChildren().add(mask);
					maskFlag.set(true);
				}
			}
		});

		final EventHandler<MouseEvent> enteredEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				popUp.show(getSkinnable(), e.getScreenX() - shift, e.getScreenY() - shift);
				takeSnap(e.getX(), e.getY());
			}
		};
		final EventHandler<MouseEvent> exitedEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				popUp.hide();
			}
		};
		final EventHandler<MouseEvent> movedEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				final double r = localRadius.get();
				final double s = localScaleFactor.get();
				if (e.getSceneX() > (scene.getWidth() - (2 * r))) {
					popUp.setX(e.getScreenX() - (2 * r) - shift);
				} else {
					popUp.setX(e.getScreenX() - shift);
				}

				if (e.getSceneY() > (scene.getHeight() - (2 * r))) {
					popUp.setY(e.getScreenY() - (2 * r) - shift);
				} else {
					popUp.setY(e.getScreenY() - shift);
				}
				prevX.set(e.getX());
				prevY.set(e.getY());
				shiftViewerContent(prevX.get(), prevY.get(), r, s);
			}
		};

		// Adding listener to activateProperty.
		getSkinnable().activeProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				if (getSkinnable().isActive()) {
					addEventFilter(MouseEvent.MOUSE_ENTERED, enteredEvent);
					addEventFilter(MouseEvent.MOUSE_EXITED, exitedEvent);
					addEventFilter(MouseEvent.MOUSE_MOVED, movedEvent);
					if (!getChildren().contains(mask)) {
						getChildren().add(mask);
					}
				} else {
					removeEventFilter(MouseEvent.MOUSE_ENTERED, enteredEvent);
					removeEventFilter(MouseEvent.MOUSE_EXITED, exitedEvent);
					removeEventFilter(MouseEvent.MOUSE_MOVED, movedEvent);
					if (getChildren().contains(mask)) {
						getChildren().remove(mask);
					}
				}
			}
		});
		if (getSkinnable().isActive()) {
			addEventFilter(MouseEvent.MOUSE_ENTERED, enteredEvent);
			addEventFilter(MouseEvent.MOUSE_EXITED, exitedEvent);
			addEventFilter(MouseEvent.MOUSE_MOVED, movedEvent);
			if (!getChildren().contains(mask)) {
				getChildren().add(mask);
			}
		}

		// Adding listener to contentProperty.
		getSkinnable().contentProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				getChildren().clear();
				getChildren().add(getSkinnable().getContent());
			}
		});
		if (getSkinnable().getContent() != null) {
			getChildren().add(getSkinnable().getContent());
		}

		// Handling scroll behavior.
		mainContent.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent e) {
				// If scrolled with CTRL key press
				if (e.isControlDown() && getSkinnable().isScalableOnScroll()) {
					double delta = e.getDeltaY();
					double newValue = localScaleFactor.get();
			        if (delta > 0) { // Increasing the zoom.
			        	newValue = newValue + ZOOM_DELTA;
						if (newValue > ZOOM_MAX) {
				            newValue = ZOOM_MAX;
				        }
					} else if (delta < 0) { // Decreasing the zoom.
						newValue = newValue - ZOOM_DELTA;
						if (newValue < ZOOM_MIN) {
				            newValue = ZOOM_MIN;
				        }
					}
			        localScaleFactor.set(newValue);
					takeSnap(prevX.get(), prevY.get());
				}
				// If scrolled with ALT key press
				else if (e.isAltDown() && getSkinnable().isResizableOnScroll()) {
					final double delta = e.getDeltaY();
					if (delta > 0) { // Increasing the size.
						localRadius.set(localRadius.get() + RADIUS_DELTA);
						shiftViewerContent(prevX.get(), prevY.get(), localRadius.get(), localScaleFactor.get());
					} else if (delta < 0) { // Decreasing the size.
						if (localRadius.get() > MIN_RADIUS) {
							localRadius.set(localRadius.get() - RADIUS_DELTA);
							shiftViewerContent(prevX.get(), prevY.get(), localRadius.get(), localScaleFactor.get());
						}
					}
				}
			}
		});

	}

	private void takeSnap(double x, double y) {
		int w = (int) (getSkinnable().getWidth() * localScaleFactor.get());
		int h = (int) (getSkinnable().getHeight() * localScaleFactor.get());
		writeImg = new WritableImage(w, h);

		// Get snapshot image
		getSkinnable().snapshot(callBack, param, writeImg);
		snapView.setImage(writeImg);
		viewer.setContent(snapView);
		shiftViewerContent(x, y, localRadius.get(), localScaleFactor.get());
	}

	private void shiftViewerContent(double x, double y, double r, double s) {
		viewer.transXProperty().set((x * s) - r);
		viewer.transYProperty().set((y * s) - r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		if (this.scene == null) {
			this.scene = getScene();
		}
	}

	/**
	 * Region(Viewer) that holds the clipped area of the scaled image.
	 */
	private class Viewer extends Region {
		private Node content;
		private final DoubleProperty width = new SimpleDoubleProperty();
		private final DoubleProperty height = new SimpleDoubleProperty();
		private final Rectangle clip;
		private final SimpleDoubleProperty transX = new SimpleDoubleProperty();
		private final SimpleDoubleProperty transY = new SimpleDoubleProperty();

		public Viewer(DoubleProperty w, DoubleProperty h) {
			this.width.bind(w.multiply(2));
			this.height.bind(h.multiply(2));
			this.clip = RectangleBuilder.create().build();
			this.clip.widthProperty().bind(this.width);
			this.clip.heightProperty().bind(this.height);
			this.clip.translateXProperty().bind(transX);
			this.clip.translateYProperty().bind(transY);
		}

		public void setContent(Node content) {
			if (this.content != null) {
				this.content.setClip(null);
				this.content.translateXProperty().unbind();
				this.content.translateYProperty().unbind();
				getChildren().clear();
			}
			this.content = content;
			this.content.setClip(this.clip);
			this.content.translateXProperty().bind(transX.multiply(-1));
			this.content.translateYProperty().bind(transY.multiply(-1));
			getChildren().setAll(content);
		}

		@Override
		protected double computeMinWidth(double d) {
			return width.get();
		}

		@Override
		protected double computeMinHeight(double d) {
			return height.get();
		}

		@Override
		protected double computePrefWidth(double d) {
			return width.get();
		}

		@Override
		protected double computePrefHeight(double d) {
			return height.get();
		}

		@Override
		protected double computeMaxWidth(double d) {
			return width.get();
		}

		@Override
		protected double computeMaxHeight(double d) {
			return height.get();
		}

		public SimpleDoubleProperty transXProperty() {
			return transX;
		}

		public SimpleDoubleProperty transYProperty() {
			return transY;
		}
	}
}

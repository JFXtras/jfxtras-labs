/**
 * MagnifierDemo.java
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

package jfxtras.labs.scene.control;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Demo class for the {@link jfxtras.labs.scene.control.Magnifier} control.
 * 
 * @author SaiPradeepDandem
 * 
 */
public class MagnifierDemo extends Application {
	Stage stage;
	BorderPane root;
	StackPane body;
	String title = "JavaFX Magnifier";

	@Override
	public void start(Stage paramStage) throws Exception {
		stage = paramStage;
		configure();
		layoutDemo();
	}

	/* ******************************************* Demo Layout ******************************************* */
	private DoubleProperty radius = new SimpleDoubleProperty();
	private DoubleProperty frameWidth = new SimpleDoubleProperty();
	private DoubleProperty scaleFactor = new SimpleDoubleProperty();
	private DoubleProperty scopeLineWidth = new SimpleDoubleProperty();
	private BooleanProperty scopeLinesVisible = new SimpleBooleanProperty();
	private BooleanProperty active = new SimpleBooleanProperty();
	private BooleanProperty scalableOnScroll = new SimpleBooleanProperty();
	private BooleanProperty resizableOnScroll = new SimpleBooleanProperty();

	final DecimalFormat df = new DecimalFormat("##.#");
	final Map<Integer, Node> samples = new HashMap<>();
	final StackPane container = new StackPane();
	private final Label tooltipText = new Label();
	private Popup tooltipPopup;

	private void layoutDemo() {
		tooltipText.setWrapText(true);
		StackPane separator = new StackPane();
		separator.setMaxWidth(1);
		separator.setPrefWidth(2.5);
		separator.getStyleClass().add("divider");
		HBox.setMargin(separator, new Insets(20, 10, 20, 0));

		HBox pane = new HBox();
		pane.setSpacing(10);
		pane.setAlignment(Pos.TOP_RIGHT);
		pane.getChildren().addAll(configureSamples(), separator, configurePropertyPane());
		body.getChildren().add(pane);
	}

	private VBox configureSamples() {
		final ImageView sample1 = new ImageView(new Image(
				MagnifierDemo.class.getResourceAsStream("/jfxtras/labs/scene/control/magnifier-sample1.jpg")));
		sample1.setFitHeight(480);
		sample1.setFitWidth(420);
		samples.put(1, configureSample(sample1));

		final ImageView sample2 = new ImageView(new Image(
				MagnifierDemo.class.getResourceAsStream("/jfxtras/labs/scene/control/magnifier-sample2.jpg")));
		sample2.setFitHeight(420);
		sample2.setFitWidth(500);
		samples.put(2, configureSample(sample2));

		GridPane gp = new GridPane();
		gp.setVgap(8);
		gp.setHgap(10);
		gp.addRow(0, new Label("First Name"), new Label(":"), new TextField());
		gp.addRow(1, new Label("Last Name"), new Label(":"), new TextField());
		gp.addRow(2, new Label("Gender"), new Label(":"), new RadioButton("Male"));
		gp.addRow(3, new Label(""), new Label(""), new RadioButton("Female"));
		gp.addRow(4, new Label("Subjects"), new Label(":"), new CheckBox("Maths"), new CheckBox("Social"));
		gp.addRow(5, new Label(""), new Label(""), new CheckBox("Science"), new CheckBox("Biology"));

		HBox formButtonBox = new HBox();
		formButtonBox.setSpacing(10);
		formButtonBox.setPadding(new Insets(15, 0, 0, 0));
		formButtonBox.setAlignment(Pos.CENTER_RIGHT);
		formButtonBox.getChildren().addAll(new Button("Cancel"), new Button("Submit"));

		VBox vb = new VBox();
		vb.setAlignment(Pos.TOP_LEFT);
		vb.setSpacing(10);
		vb.setPadding(new Insets(10));
		vb.getStyleClass().addAll("formBox");
		vb.setMaxHeight(250);
		vb.setMaxWidth(400);
		vb.getChildren().addAll(getLabel("Student Form", "formTitle"), gp, formButtonBox);

		VBox layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(10);
		layout.getChildren().addAll(getLabel("* Deselect the \"active\" property to access the form.", "note"), vb);
		samples.put(3, configureSample(layout));

		HBox buttonBox = new HBox();
		buttonBox.setSpacing(15);
		buttonBox.getChildren().addAll(getButton("Sample 1", 1), getButton("Sample 2", 2), getButton("Sample 3", 3));

		VBox v = new VBox();
		v.setSpacing(20);
		v.getChildren().addAll(buttonBox, container);
		VBox.setVgrow(container, Priority.ALWAYS);
		HBox.setHgrow(v, Priority.ALWAYS);
		return v;
	}

	private Label getLabel(String txt, String... styleClass) {
		Label lbl = new Label(txt);
		lbl.getStyleClass().addAll(styleClass);
		return lbl;
	}

	private Magnifier configureSample(Node sample) {
		Magnifier p = new Magnifier();
		p.radiusProperty().bindBidirectional(radius);
		p.frameWidthProperty().bind(frameWidth);
		p.scaleFactorProperty().bind(scaleFactor);
		p.scopeLineWidthProperty().bind(scopeLineWidth);
		p.scopeLinesVisibleProperty().bind(scopeLinesVisible);
		p.activeProperty().bind(active);
		p.scalableOnScrollProperty().bind(scalableOnScroll);
		p.resizableOnScrollProperty().bind(resizableOnScroll);
		p.setContent(sample);

		return p;
	}

	private List<StackPane> buttonsList = new ArrayList<>();
	private int currentBtn = -1;

	protected Node getButton(String text, final int id) {
		final StackPane sp = new StackPane();
		sp.getStyleClass().add("sample-button");
		sp.getChildren().add(new Label(text));
		buttonsList.add(sp);
		sp.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (id != currentBtn) {
					showSample(sp, id);
				}
			}
		});
		if (id == 1) {
			showSample(sp, id);
		}
		return new Group(sp);
	}

	private void showSample(StackPane sp, int id) {
		for (StackPane btn : buttonsList) {
			btn.getStyleClass().removeAll("sample-button-selected", "sample-button");
			btn.getStyleClass().add("sample-button");
		}
		sp.getStyleClass().remove("sample-button");
		sp.getStyleClass().add("sample-button-selected");
		currentBtn = id;
		showContent(id);
	}

	protected void showContent(int num) {
		container.getChildren().clear();
		container.getChildren().add(samples.get(num));
	}

	private GridPane configurePropertyPane() {
		GridPane gp = new GridPane();
		gp.setVgap(18);
		gp.setHgap(10);
		gp.setPadding(new Insets(0, 15, 0, 15));
		final CheckBox activateCB = new CheckBox();
		activateCB.setSelected(true);
		active.bind(activateCB.selectedProperty());

		final Slider rSlider = new Slider(50, 150, 86);
		rSlider.disableProperty().bind(activateCB.selectedProperty().not());
		radius.bind(rSlider.valueProperty());
		Label rL = new Label();
		rL.textProperty().bind(new StringBinding() {
			{
				bind(rSlider.valueProperty());
			}

			@Override
			protected String computeValue() {
				return df.format(rSlider.getValue()) + "px";
			}
		});

		final Slider fmSlider = new Slider(3, 10, 5.5);
		fmSlider.disableProperty().bind(activateCB.selectedProperty().not());
		frameWidth.bind(fmSlider.valueProperty());
		Label fmL = new Label();
		fmL.textProperty().bind(new StringBinding() {
			{
				bind(fmSlider.valueProperty());
			}

			@Override
			protected String computeValue() {
				return df.format(fmSlider.getValue()) + "px";
			}
		});

		final Slider sfSlider = new Slider(1, 8, 3);
		sfSlider.disableProperty().bind(activateCB.selectedProperty().not());
		scaleFactor.bind(sfSlider.valueProperty());
		Label sfL = new Label();
		sfL.textProperty().bind(new StringBinding() {
			{
				bind(sfSlider.valueProperty());
			}

			@Override
			protected String computeValue() {
				return df.format(sfSlider.getValue()) + "";
			}
		});

		final CheckBox slVisibleCB = new CheckBox();
		slVisibleCB.disableProperty().bind(activateCB.selectedProperty().not());
		scopeLinesVisible.bind(slVisibleCB.selectedProperty());

		final Slider sllider = new Slider(1, 4, 1.5);
		sllider.disableProperty().bind(new BooleanBinding() {
			{
				bind(activateCB.selectedProperty(), slVisibleCB.selectedProperty());
			}

			@Override
			protected boolean computeValue() {
				if (!activateCB.isSelected() || !slVisibleCB.isSelected()) {
					return true;
				}
				return false;
			}
		});
		scopeLineWidth.bind(sllider.valueProperty());
		Label slL = new Label();
		slL.textProperty().bind(new StringBinding() {
			{
				bind(sllider.valueProperty());
			}

			@Override
			protected String computeValue() {
				return df.format(sllider.getValue()) + "px";
			}
		});

		CheckBox scaleOnScrollCB = new CheckBox();
		scaleOnScrollCB.disableProperty().bind(activateCB.selectedProperty().not());
		scalableOnScroll.bind(scaleOnScrollCB.selectedProperty());

		CheckBox resizeOnScrollCB = new CheckBox();
		resizeOnScrollCB.disableProperty().bind(activateCB.selectedProperty().not());
		resizableOnScroll.bind(resizeOnScrollCB.selectedProperty());

		final Label secHeading = new Label("Configurable Properties :");
		secHeading.getStyleClass().add("sectionHeading");
		secHeading.setPrefHeight(40);
		secHeading.setAlignment(Pos.TOP_CENTER);

		gp.add(secHeading, 0, 0, 4, 1);
		gp.addRow(1, getSep("Active", MagnifierDoc.active), new Label(":"), activateCB, getSpacer());
		gp.addRow(2, getSep("Radius", MagnifierDoc.radius), new Label(":"), rSlider, rL);
		gp.addRow(3, getSep("Frame Width", MagnifierDoc.frameWidth), new Label(":"), fmSlider, fmL);
		gp.addRow(4, getSep("Scale Factor", MagnifierDoc.scaleFactor), new Label(":"), sfSlider, sfL);
		gp.addRow(5, getSep("Scope Lines Visible", MagnifierDoc.scopeLinesVisible), new Label(":"), slVisibleCB, getSpacer());
		gp.addRow(6, getSep("Scope Line Width", MagnifierDoc.scopeLineWidth), new Label(":"), sllider, slL);
		gp.addRow(7, getSep("Scalable On Scroll", MagnifierDoc.scalableOnScroll), new Label(":"), scaleOnScrollCB, getSpacer());
		gp.addRow(8, getSep("Resizable On Scroll", MagnifierDoc.resizableOnScroll), new Label(":"), resizeOnScrollCB, getSpacer());
		return gp;
	}

	private Node getSpacer() {
		StackPane spacer = new StackPane();
		spacer.setMinWidth(50);
		spacer.getChildren().add(new Label(""));
		return spacer;
	}

	private Label getSep(String txt, String str) {
		Label label = new Label(txt);
		label.setGraphic(getHelpIcon(str));
		label.setContentDisplay(ContentDisplay.RIGHT);
		label.setGraphicTextGap(10);
		return label;
	}

	private StackPane getHelpIcon(final String str) {
		final StackPane help = new StackPane();
		help.setMinHeight(12);
		help.setMinWidth(10);
		help.setMaxHeight(12);
		help.setMaxWidth(10);
		help.setPrefHeight(12);
		help.setPrefWidth(10);
		help.getStyleClass().add("helpIcon");
		help.getChildren().add(new Label("?"));
		help.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				tooltipText.setText(str);
				showToolTip(help);
			}
		});
		help.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				getPopup().hide();
			}
		});
		return help;
	}

	private Popup getPopup() {
		if (tooltipPopup == null) {
			final StackPane cont = new StackPane();
			cont.setMaxWidth(275);
			cont.getStyleClass().add("doc-tool-tip");
			cont.getChildren().add(tooltipText);

			final Popup popup = new Popup();
			popup.setAutoHide(true);
			popup.setAutoFix(true);
			popup.setHideOnEscape(true);
			popup.getContent().add(cont);
			tooltipPopup = popup;
		}
		return tooltipPopup;
	}

	/**
	 * Shows the tooltip of the alert.
	 * 
	 * @param node
	 *            Node to which the tooltip need to be shown.
	 */
	private void showToolTip(StackPane node) {
		final Parent parent = node.getParent();
		final Bounds childBounds = node.getBoundsInParent();
		final Bounds parentBounds = parent.localToScene(parent.getBoundsInLocal());
		double layoutX = childBounds.getMinX() + parentBounds.getMinX() + parent.getScene().getX() + parent.getScene().getWindow().getX();
		double layoutY = childBounds.getMaxY() + parentBounds.getMinY() + parent.getScene().getY() + parent.getScene().getWindow().getY();
		getPopup().show(node, layoutX, layoutY + 5);
	}

	/* ******************************************* Configuration ******************************************* */
	public static void main(String[] args) {
		Application.launch(args);
	}

	private void configure() {

		final Label titleLabel = new Label(title);
		titleLabel.getStyleClass().add("titleLabel");

		final StackPane sep = new StackPane();
		sep.setMinWidth(1);
		sep.setMaxWidth(1);
		sep.setMaxHeight(40);
		sep.getStyleClass().add("headerSeparator");

		final Label subTitleLabel = new Label("A JFXtras-Labs Demo");
		subTitleLabel.getStyleClass().add("subTitleLabel");

		final HBox row = new HBox();
		row.setSpacing(15);
		row.setPadding(new Insets(0, 0, 0, 30));
		row.setAlignment(Pos.CENTER_LEFT);
		row.setPrefHeight(100);
		row.getChildren().addAll(titleLabel, sep, subTitleLabel);

		final StackPane header = new StackPane();
		header.setMaxHeight(100);
		header.setPrefHeight(100);
		header.getStyleClass().add("headerBg");
		header.getChildren().add(row);

		body = new StackPane();
		body.getStyleClass().add("bodyBg");
		body.setPadding(new Insets(15));

		StackPane footer = new StackPane();
		footer.setMaxHeight(5);
		footer.setPrefHeight(5);
		footer.getStyleClass().add("footerBg");

		stage.setTitle(title);
		stage.setWidth(1200);
		stage.setHeight(700);

		root = new BorderPane();
		root.setTop(header);
		root.setCenter(body);
		root.setBottom(footer);

		Scene scene = new Scene(root, Color.CORNSILK);
		scene.getStylesheets().add("jfxtras/labs/scene/control/MagnifierDemo.css");
		stage.setScene(scene);
		stage.show();
	}

	private interface MagnifierDoc {
		final String active = "Controls the magnifier whether to activate or not.\n\n   - true : Shows the magnified viewer on mouse over and does not allow to access the content inside the control.\n   - false : Does not show the magnified viewer on mouse over and can access the content inside the control.\n\nDefault value is true";
		final String radius = "Property for setting the radius of the circular viewer. The default value is 86.0D.";
		final String frameWidth = "Property for setting the frame width of the circular viewer. The default value is 5.5D.";
		final String scaleFactor = "Property for setting the scale factor to which the content need to be magnified. The default value is 3.0D.";
		final String scopeLineWidth = "Property for setting the width of the scope lines that are visible in the circular viewer. The default value is 1.5px.";
		final String scopeLinesVisible = "Controls whether lines are displayed to show in the magnifier viewer. Default is false.";
		final String scalableOnScroll = "Controls the magnifier whether to scale the content on mouse scroll or not. Content is scaled only when the mouse is scrolled in combination with CTRL key press.\n\n   - true : Allows the content to scale when mouse is scrolled in combination with CTRL key press.\n   - false : Does not allow the content to scale when mouse is scrolled.\n\nDefault value is false.";
		final String resizableOnScroll = "Controls the magnifier whether to resize the viewer on mouse scroll or not. The viewer is resized only when the mouse is scrolled in combination with ALT key press.\n\n   - true : Allows the viewer to resize when mouse is scrolled in combination with ALT key press.\n   - false : Does not allow the viewer to resize when mouse is scrolled.\n\nDefault value is false.";
	}

}

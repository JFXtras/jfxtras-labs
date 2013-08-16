package jfxtras.labs.scene.control;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxBuilder;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.RadioButtonBuilder;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleButtonBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.PopupBuilder;
import javafx.stage.Stage;

/**
 * Demo class for the {@link Magnifier} control.
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
	final ToggleGroup group = new ToggleGroup();
	final StackPane container = new StackPane();
	private final Label tooltipText = LabelBuilder.create().wrapText(true).build();
	private Popup tooltipPopup;

	private void layoutDemo() {
		StackPane separator = StackPaneBuilder.create().maxWidth(1).prefWidth(2.5).styleClass("divider").build();
		HBox.setMargin(separator, new Insets(20, 10, 20, 0));
		HBox pane = HBoxBuilder.create().spacing(10).children(configureSamples(), separator, configurePropertyPane())
				.alignment(Pos.TOP_RIGHT).build();
		body.getChildren().add(pane);
	}

	private VBox configureSamples() {
		final ImageView sample1 = new ImageView(new Image(MagnifierDemo.class.getResourceAsStream("/jfxtras/labs/scene/control/magnifier-sample1.jpg")));
		sample1.setFitHeight(480);
		sample1.setFitWidth(420);
		samples.put(1, configureSample(sample1));

		final ImageView sample2 = new ImageView(new Image(MagnifierDemo.class.getResourceAsStream("/jfxtras/labs/scene/control/magnifier-sample2.jpg")));
		sample2.setFitHeight(420);
		sample2.setFitWidth(500);
		samples.put(2, configureSample(sample2));

		GridPane gp = GridPaneBuilder.create().vgap(8).hgap(10).build();
		gp.addRow(0, new Label("First Name"), new Label(":"), new TextField());
		gp.addRow(1, new Label("Last Name"), new Label(":"), new TextField());
		gp.addRow(2, new Label("Gender"), new Label(":"), RadioButtonBuilder.create().text("Male").build());
		gp.addRow(3, new Label(""), new Label(""), RadioButtonBuilder.create().text("Female").build());
		gp.addRow(4, new Label("Subjects"), new Label(":"), CheckBoxBuilder.create().text("Maths").build(),
				CheckBoxBuilder.create().text("Social").build());
		gp.addRow(5, new Label(""), new Label(""), CheckBoxBuilder.create().text("Science").build(),
				CheckBoxBuilder.create().text("Biology").build());
		VBox vb = VBoxBuilder
				.create()
				.maxHeight(250)
				.maxWidth(400)
				.styleClass("formBox")
				.alignment(Pos.TOP_LEFT)
				.padding(new Insets(10))
				.spacing(10)
				.children(
						LabelBuilder.create().text("Student Form").styleClass("formTitle").build(),
						gp,
						HBoxBuilder.create().spacing(10).padding(new Insets(15, 0, 0, 0)).alignment(Pos.CENTER_RIGHT)
								.children(new Button("Cancel"), new Button("Submit")).build()).build();
		samples.put(
				3,
				configureSample(VBoxBuilder
						.create()
						.alignment(Pos.CENTER)
						.children(
								LabelBuilder.create().styleClass("note").text("* Deselect the \"active\" property to access the form.")
										.build(), vb).spacing(10).build()));

		HBox buttonBox = HBoxBuilder.create().spacing(15)
				.children(getButton("Sample 1", 1), getButton("Sample 2", 2), getButton("Sample 3", 3)).build();
		VBox v = VBoxBuilder.create().children(buttonBox, container).spacing(20).build();
		VBox.setVgrow(container, Priority.ALWAYS);
		HBox.setHgrow(v, Priority.ALWAYS);
		return v;
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

	protected ToggleButton getButton(String txt, final int num) {
		final ToggleButton btn = ToggleButtonBuilder.create().text(txt).styleClass("toggle-button-with-bg").toggleGroup(group)
				.translateY(7).maxHeight(20).build();
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent paramT) {
				if (!btn.isSelected()) {
					btn.setSelected(true);
				} else {
					showContent(num);
				}
			}
		});
		if (num == 1) {
			btn.fire();
		}
		return btn;
	}

	protected void showContent(int num) {
		container.getChildren().clear();
		container.getChildren().add(samples.get(num));
	}

	private GridPane configurePropertyPane() {
		GridPane gp = GridPaneBuilder.create().vgap(18).hgap(10).padding(new Insets(0, 15, 0, 15)).build();
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

		gp.add(LabelBuilder.create().text("Configurable Properties :").styleClass("sectionHeading").prefHeight(40)
				.alignment(Pos.TOP_CENTER).build(), 0, 0, 4, 1);
		gp.addRow(1, getSep("Active", MagnifierDoc.active), new Label(":"), activateCB,
				StackPaneBuilder.create().minWidth(50).children(new Label("")).build());
		gp.addRow(2, getSep("Radius", MagnifierDoc.radius), new Label(":"), rSlider, rL);
		gp.addRow(3, getSep("Frame Width", MagnifierDoc.frameWidth), new Label(":"), fmSlider, fmL);
		gp.addRow(4, getSep("Scale Factor", MagnifierDoc.scaleFactor), new Label(":"), sfSlider, sfL);
		gp.addRow(5, getSep("Scope Lines Visible", MagnifierDoc.scopeLinesVisible), new Label(":"), slVisibleCB, StackPaneBuilder.create()
				.minWidth(50).children(new Label("")).build());
		gp.addRow(6, getSep("Scope Line Width", MagnifierDoc.scopeLineWidth), new Label(":"), sllider, slL);
		gp.addRow(7, getSep("Scalable On Scroll", MagnifierDoc.scalableOnScroll), new Label(":"), scaleOnScrollCB, StackPaneBuilder
				.create().minWidth(50).children(new Label("")).build());
		gp.addRow(8, getSep("Resizable On Scroll", MagnifierDoc.resizableOnScroll), new Label(":"), resizeOnScrollCB, StackPaneBuilder
				.create().minWidth(50).children(new Label("")).build());
		return gp;
	}

	private Label getSep(String txt, String str) {
		return LabelBuilder.create().graphic(getHelpIcon(str)).contentDisplay(ContentDisplay.RIGHT).graphicTextGap(10).text(txt).build();
	}

	private StackPane getHelpIcon(final String str) {
		final StackPane help = StackPaneBuilder.create().minHeight(12).minWidth(10).maxHeight(12).maxWidth(10).prefHeight(12).prefWidth(10)
				.styleClass("helpIcon").children(new Label("?")).build();
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
			tooltipPopup = PopupBuilder.create().autoHide(true).autoFix(true).hideOnEscape(true)
					.content(StackPaneBuilder.create().maxWidth(275).styleClass("doc-tool-tip").children(tooltipText).build()).build();
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
		StackPane header = StackPaneBuilder
				.create()
				.children(
						HBoxBuilder
								.create()
								.spacing(15)
								.padding(new Insets(0, 0, 0, 30))
								.children(
										LabelBuilder.create().styleClass("titleLabel").text(title).build(),
										StackPaneBuilder.create().styleClass("headerSeparator").minWidth(1).maxWidth(1).maxHeight(40)
												.build(),
										LabelBuilder.create().styleClass("subTitleLabel").text("A JFXtras-Labs Demo").build())
								.alignment(Pos.CENTER_LEFT).build()).maxHeight(100).prefHeight(100).styleClass("headerBg").build();
		body = StackPaneBuilder.create().styleClass("bodyBg").padding(new Insets(15)).build();
		StackPane footer = StackPaneBuilder.create().maxHeight(5).prefHeight(5).styleClass("footerBg").build();

		stage.setTitle(title);
		stage.setWidth(1200);
		stage.setHeight(700);
		root = BorderPaneBuilder.create().top(header).center(body).bottom(footer).build();
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

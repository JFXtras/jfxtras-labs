package jfxtras.labs.scene.layout;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.layout.ResponsivePane.Ref;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

public class ResponsivePaneDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(ResponsivePaneDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// layout
		ResponsivePane lResponsivePane = new ResponsivePane();
		lResponsivePane.addRef("CalendarPicker", new CalendarPicker());
		lResponsivePane.addRef("TreeView", new TreeView());
		lResponsivePane.addRef("TableView", new TableView());
		lResponsivePane.addRef("save", new Button("save"));
		lResponsivePane.addRef("saveAndTomorrow", new Button("saveAndTomorrow"));
		lResponsivePane.addRef("-", new Button("-"));
		lResponsivePane.addRef("+", new Button("+"));
		lResponsivePane.addRef("Logo", new Button("Logo"));
		lResponsivePane.addRef("version", new Label("v1.0"));
		//
		lResponsivePane.addLayout(400.0, createPhoneLayout());
		lResponsivePane.addLayout(1024.0, createDesktopLayout());		
		// css
		lResponsivePane.addSceneStylesheet(500.0, getClass().getResource("phone.css"));
		lResponsivePane.addSceneStylesheet(800.0, getClass().getResource("tablet.css"));
		lResponsivePane.addSceneStylesheet(1200.0, getClass().getResource("desktop.css"));

		// show
		primaryStage.setScene(new Scene(lResponsivePane, 2000, 1000));
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	private Node createDesktopLayout() {
    	MigPane lMigPane = new MigPane();
    	lMigPane.setLayoutConstraints( new LC().hideMode(2).fill().gridGap("10px", "10px") );
    	lMigPane.add(new Ref("Logo"), new CC());
    	lMigPane.add(new Ref("version"), new CC().spanX(2).alignX("right").alignY("top").wrap()); // menu
    	lMigPane.add(new Ref("CalendarPicker"), new CC().alignY("top"));
    	lMigPane.add(new Button("cardTabPane"), new CC().grow().pushX());
    	lMigPane.add(new Ref("TreeView"), new CC().grow().pushX().spanY(3).wrap());
    	ScrollPane scrollPane = new ScrollPane(new Ref("TableView"));
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		lMigPane.add(new BorderPane(scrollPane, null, new VBox(new Ref("-"), new Ref("+")).withSpacing(5.0), null, null), new CC().spanX(2).grow().pushY().wrap());
		lMigPane.add(new HBox(new Ref("save"), new Ref("saveAndTomorrow")).withSpacing(5.0), new CC().spanX(2));
		return lMigPane;
	}
	
    private Node createPhoneLayout() {
        TabPane mainTabPane;
        Tab mainTabCalendar;
        Tab mainTabHours;
        Tab mainTabProject;
        Tab mainTabCards;

    	mainTabPane = new TabPane();
    	mainTabPane.setSide(Side.LEFT);
    	mainTabPane.setPadding(new Insets(10.0)); // to CSS
    	{
    		mainTabCalendar = new Tab();
    		mainTabCalendar.setText("Date");
    		mainTabCalendar.setTooltip(new Tooltip("Kalender"));
    		mainTabCalendar.setClosable(false);
    		mainTabPane.getTabs().add(mainTabCalendar);
    	}
    	{
    		mainTabHours = new Tab();
    		mainTabHours.setText("Time");
    		mainTabHours.setTooltip(new Tooltip("Uren"));
    		mainTabHours.setClosable(false);
    		mainTabPane.getTabs().add(mainTabHours);
    	}
    	{
    		mainTabProject = new Tab();
    		mainTabProject.setText("Tree");
    		mainTabProject.setTooltip(new Tooltip("Projectboom"));
    		mainTabProject.setClosable(false);
    		mainTabPane.getTabs().add(mainTabProject);
    	}
    	{
    		mainTabCards = new Tab();
    		mainTabCards.setText("Calc");
    		mainTabCards.setTooltip(new Tooltip("Totalen"));
    		mainTabCards.setClosable(false);
    		mainTabPane.getTabs().add(mainTabCards);
    	}
    	
   		mainTabCalendar.setContent(new Ref("CalendarPicker"));
   		mainTabProject.setContent(new Ref("TreeView"));
   		mainTabCards.setContent(new Button("cardTabPane"));

		BorderPane lBorderPane = new BorderPane(new Ref("TableView"));
		lBorderPane.setRight(new VBox(new Ref("-"), new Ref("+")));
		lBorderPane.setBottom(new HBox(new Ref("save"), new Ref("saveAndTomorrow")).withSpacing(5.0));
   		mainTabHours.setContent(lBorderPane);
   		return mainTabPane;
    }

}

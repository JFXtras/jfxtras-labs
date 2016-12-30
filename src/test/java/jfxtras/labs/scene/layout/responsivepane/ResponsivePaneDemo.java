package jfxtras.labs.scene.layout.responsivepane;

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
import jfxtras.labs.scene.layout.responsivepane.Ref;
import jfxtras.labs.scene.layout.responsivepane.ResponsivePane;
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
		
		ResponsivePane responsivePane = new ResponsivePane();
		responsivePane.setDebug(true);
		// nodes
		responsivePane.addReusableNode("CalendarPicker", new CalendarPicker());
		responsivePane.addReusableNode("save", new Button("save"));
		responsivePane.addReusableNode("saveAndTomorrow", new Button("saveAndTomorrow"));
		// layout
		responsivePane.addLayout(Device.PHONE, createPhoneLayout());
		responsivePane.addLayout(Diagonal.inches(12.0), createDesktopLayout());		
		// css
		responsivePane.addSceneStylesheet(Diagonal.inches(4.0), ResponsivePaneTest.class.getResource("phone.css").toExternalForm());
		responsivePane.addSceneStylesheet(Diagonal.inches(6.0), ResponsivePaneTest.class.getResource("tablet.css").toExternalForm());
		responsivePane.addSceneStylesheet(Diagonal.inches(12.0), ResponsivePaneTest.class.getResource("desktop.css").toExternalForm());

		// show
		primaryStage.setScene(new Scene(responsivePane, 1700, 1000));
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
		
		lMigPane.add(new HBox(new Ref("save", "thisRefHasALongId"), new Ref("saveAndTomorrow")).withSpacing(5.0), new CC().spanX(2));
		return lMigPane;
	}
	
    private Node createPhoneLayout() {
        TabPane mainTabPane = new TabPane();
   		mainTabPane.getTabs().add(createTab("Date", "Calendar", new Ref("CalendarPicker")));
   		mainTabPane.getTabs().add(createTab("Tree", "Projectboom", new Ref("TreeView")));
   		mainTabPane.getTabs().add(createTab("Calc", "Totalen", new Ref("TreeView")));
   		
		BorderPane lBorderPane = new BorderPane(new Ref("TableView"));
		lBorderPane.setRight(new VBox(new Ref("-"), new Ref("+")));
		lBorderPane.setBottom(new HBox(new Ref("save", "thisRefHasALongId"), new Ref("saveAndTomorrow")).withSpacing(5.0));
   		mainTabPane.getTabs().add(createTab("Time", "Hours", lBorderPane));
   		
   		return mainTabPane;
    }

    private Tab createTab(String label, String tooltip, Node content) {
		Tab tab = new Tab();
		tab.setText(label);
		tab.setTooltip(new Tooltip(tooltip));
		tab.setClosable(false);
   		tab.setContent(content);
		return tab;
    }
}

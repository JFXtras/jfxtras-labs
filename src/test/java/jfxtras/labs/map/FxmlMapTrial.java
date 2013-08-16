package jfxtras.labs.map;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

/**
 *
 * @author Mario Schroeder
 */
public class FxmlMapTrial extends Application{
	


    public static void main(String[] args) {
        Application.launch(FxmlMapTrial.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Map.fxml"), null, 
                new MapPaneBuilderFactory());
        
        Scene scene = new Scene(root);
        //TODO find a way that the map automatic resizes without the need to add listeners
        final ToolBar toolBar = (ToolBar)root.lookup("#toolBar");
        final MapPane map = (MapPane) root.lookup("#map");
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                Number newValue) {
                map.setMinWidth(newValue.doubleValue());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                Number newValue) {
                map.setMinHeight(newValue.doubleValue() - toolBar.getHeight());
            }
        });
        
        stage.setTitle("FXML Map");
        
        stage.setScene(scene);
        stage.show();
    }

}

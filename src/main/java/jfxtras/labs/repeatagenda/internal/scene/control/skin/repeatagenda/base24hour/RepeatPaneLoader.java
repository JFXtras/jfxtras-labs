package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import javafx.fxml.FXMLLoader;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.RepeatableControllerOld;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;

public class RepeatPaneLoader {

    private RepeatPaneLoader() { }
    
    private RepeatableControllerOld repeatPaneController;
    public RepeatableControllerOld getController() { return repeatPaneController; }
    
    public static FXMLLoader newInstance()
    {
//      // LOAD FXML
      FXMLLoader repeatPaneLoader = new FXMLLoader();
      repeatPaneLoader.setLocation(RepeatMenu.class.getResource("view/Repeatable.fxml"));
      repeatPaneLoader.setResources(Settings.resources);
//      try {
//          repeatPane = repeatPaneLoader.load();
//      } catch (IOException e) {
//          e.printStackTrace();
//      }
      return repeatPaneLoader;
    }
}

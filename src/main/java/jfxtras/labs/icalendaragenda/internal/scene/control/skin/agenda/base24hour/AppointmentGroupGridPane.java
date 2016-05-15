package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** makes a group of colored squares used to select appointment group */
public class AppointmentGroupGridPane extends GridPane
{
private Pane[] icons;
final private ImageView checkIcon = new ImageView();
//SVGPath myIcon;

/** Index of selected AppointmentGroup */
public IntegerProperty appointmentGroupSelectedProperty() { return appointmentGroupSelected; }
private IntegerProperty appointmentGroupSelected = new SimpleIntegerProperty(-1);
public void setAppointmentGroupSelected(Integer i) { appointmentGroupSelected.set(i); }
public Integer getAppointmentGroupSelected() { return appointmentGroupSelected.getValue(); }   
 
public AppointmentGroupGridPane()
{
    checkIcon.getStyleClass().add("check-icon");
}

public AppointmentGroupGridPane(VComponentDisplayable<?> vComponent, List<AppointmentGroup> appointmentGroups)
{
    this();
    setupData(vComponent, appointmentGroups);
}
 
 public void setupData(VComponentDisplayable<?> vComponent, List<AppointmentGroup> appointmentGroups)
 {
//      myIcon = new SVGPath();
//     myIcon.setFill(Color.rgb(0, 255, 0, .9));
//     myIcon.setStroke(Color.WHITE);//
//     myIcon.setContent("M2.379,14.729 5.208,11.899 12.958,19.648 25.877,6.733 28.707,9.56112.958,25.308z");
     setHgap(3);
     setVgap(3);
     icons = new Pane[appointmentGroups.size()];
     
     int lCnt = 0;
     for (AppointmentGroup lAppointmentGroup : appointmentGroups)
     {
         Pane icon = new Pane();
         icon.setPrefSize(24, 24);
         Rectangle rectangle = new Rectangle(24, 24);
         rectangle.setArcWidth(6);
         rectangle.setArcHeight(6);
         rectangle.getStyleClass().add(lAppointmentGroup.getStyleClass());
         icon.getChildren().add(rectangle);
         icons[lCnt] = icon;
         this.add(icons[lCnt], lCnt % 12, lCnt / 12 );

         // tooltip
         updateToolTip(lCnt, appointmentGroups);

         // mouse 
         setupMouseOverAsBusy(icons[lCnt]);
         icons[lCnt].setOnMouseClicked( (mouseEvent) ->
         {
             mouseEvent.consume(); // consume before anything else, in case there is a problem in the handling
             appointmentGroupSelected.set(appointmentGroups.indexOf(lAppointmentGroup));

             // assign appointment group, store description in CATEGORIES field
             AppointmentGroup g = appointmentGroups.get(appointmentGroupSelected.getValue());
             vComponent.setCategories(FXCollections.observableArrayList(Categories.parse(g.getDescription())));
         });
         lCnt++;
     }

     // Select current group
     AppointmentGroup myAppointmentGroup = appointmentGroups
             .stream()
             .filter(a -> a.getDescription().equals(vComponent.getCategories()))
             .findFirst()
             .orElse(appointmentGroups.get(0));
     int index = appointmentGroups.indexOf(myAppointmentGroup);
     setAppointmentGroupSelected(index);
     setLPane(index);
     
     // change listener - fires when new icon is selected
     appointmentGroupSelectedProperty().addListener((observable, oldSelection, newSelection) ->  {
       int oldS = (int) oldSelection;
       int newS = (int) newSelection;
       setLPane(newS);
       unsetLPane(oldS);
     });
 }

 // blue border in selection
 private void unsetLPane(int i)
 {
     icons[i].getChildren().remove(checkIcon);
 }
 private void setLPane(int i) {
     icons[i].getChildren().add(checkIcon);
 }
 
 public void updateToolTip(int i, List<AppointmentGroup> appointmentGroups) {
     AppointmentGroup a = appointmentGroups.get(i);
     if (a.getDescription() != "" && a.getDescription() != null) {
         Tooltip.install(icons[i], new Tooltip(a.getDescription()));
     } 
 }

 void setupMouseOverAsBusy(final Node node)
 {
     // play with the mouse pointer to show something can be done here
     node.setOnMouseEntered( (mouseEvent) -> {
         if (!mouseEvent.isPrimaryButtonDown()) {                        
             node.setCursor(Cursor.HAND);
             mouseEvent.consume();
         }
     });
     node.setOnMouseExited( (mouseEvent) -> {
         if (!mouseEvent.isPrimaryButtonDown()) {
             node.setCursor(Cursor.DEFAULT);
             mouseEvent.consume();
         }
     });
 }

}

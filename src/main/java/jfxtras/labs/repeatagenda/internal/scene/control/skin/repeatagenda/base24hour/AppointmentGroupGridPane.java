package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/** makes a group of colored squares used to select appointment group */
public class AppointmentGroupGridPane extends GridPane
{
private Pane[] lPane;

/** Index of selected AppointmentGroup */
public IntegerProperty appointmentGroupSelectedProperty() { return appointmentGroupSelected; }
private IntegerProperty appointmentGroupSelected = new SimpleIntegerProperty(-1);
public void setAppointmentGroupSelected(Integer i) { appointmentGroupSelected.set(i); }
public Integer getAppointmentGroupSelected() { return appointmentGroupSelected.getValue(); }   
 
 public AppointmentGroupGridPane() {
 }

 public AppointmentGroupGridPane(VComponent vComponent, List<AppointmentGroup> appointmentGroups) {
     setupData(vComponent, appointmentGroups);
 }
 
 public void setupData(VComponent vComponent, List<AppointmentGroup> appointmentGroups)
 {
     this.getStyleClass().add("AppointmentGroups");
     this.setHgap(2);
     this.setVgap(2);
     lPane = new Pane[appointmentGroups.size()];
     
     int lCnt = 0;
     for (AppointmentGroup lAppointmentGroup : appointmentGroups)
     {
         lPane[lCnt] = ((ICalendarAgenda.AppointmentGroupImpl) lAppointmentGroup).getIcon();
         this.add(lPane[lCnt], lCnt % 12, lCnt / 12 );

         // tooltip
         updateToolTip(lCnt, appointmentGroups);

         // mouse 
         setupMouseOverAsBusy(lPane[lCnt]);
         lPane[lCnt].setOnMouseClicked( (mouseEvent) ->
         {
             mouseEvent.consume(); // consume before anything else, in case there is a problem in the handling
             appointmentGroupSelected.set(appointmentGroups.indexOf(lAppointmentGroup));

             // assign appointment group, store description in CATEGORIES field
             AppointmentGroup g = appointmentGroups.get(appointmentGroupSelected.getValue());
             vComponent.setCategories(g.getDescription());
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
 private void unsetLPane(int i) {
     lPane[i].setStyle("-fx-border-color: WHITE");
 }
 private void setLPane(int i) {
      final String cssDefault = "-fx-border-color: blue;\n"
             + "-fx-border-insets: -1;\n"
             + "-fx-border-width: 2;\n";
     lPane[i].setStyle(cssDefault);
 }
 
 public void updateToolTip(int i, List<AppointmentGroup> appointmentGroups) {
     AppointmentGroup a = appointmentGroups.get(i);
     if (a.getDescription() != "" && a.getDescription() != null) {
         Tooltip.install(lPane[i], new Tooltip(a.getDescription()));
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

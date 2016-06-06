package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

/** makes a group of colored squares used to select appointment group */
public class CategorySelectionGridPane extends GridPane
{
private Pane[] icons;
final private ImageView checkIcon = new ImageView();
//SVGPath myIcon;

/** Index of selected String */
public IntegerProperty categorySelectedProperty() { return categorySelected; }
private IntegerProperty categorySelected = new SimpleIntegerProperty(-1);
public void setCategorySelected(Integer i) { categorySelected.set(i); }
public Integer getCategorySelected() { return categorySelected.getValue(); }   
 
public CategorySelectionGridPane()
{
    checkIcon.getStyleClass().add("check-icon");
}

public CategorySelectionGridPane(VComponentDisplayable<?> vComponent, List<SimpleStringProperty> categoryList)
{
    this();
    setupData(vComponent, categoryList);
}
 
 public void setupData(VComponentDisplayable<?> vComponent, List<SimpleStringProperty> categoryList)
 {
     setHgap(3);
     setVgap(3);
     icons = new Pane[categoryList.size()];
     
     int lCnt = 0;
     for (SimpleStringProperty category : categoryList)
     {
         Pane icon = new Pane();
         icon.setPrefSize(24, 24);
         Rectangle rectangle = new Rectangle(24, 24);
         rectangle.setArcWidth(6);
         rectangle.setArcHeight(6);
         rectangle.getStyleClass().add("group" + lCnt);
         icon.getChildren().add(rectangle);
         icons[lCnt] = icon;
         this.add(icons[lCnt], lCnt % 12, lCnt / 12 );

         // tooltip
         updateToolTip(lCnt, categoryList.get(lCnt));

         // mouse 
         setupMouseOverAsBusy(icons[lCnt]);
         icons[lCnt].setOnMouseClicked( (mouseEvent) ->
         {
             mouseEvent.consume(); // consume before anything else, in case there is a problem in the handling
             categorySelected.set(categoryList.indexOf(category));

             // assign appointment group, store description in CATEGORIES field
             String g = categoryList.get(categorySelected.getValue()).get();
             vComponent.setCategories(FXCollections.observableArrayList(Categories.parse(g)));
         });
         lCnt++;
     }

     final String myCategory;
     if ((vComponent.getCategories() != null) && (vComponent.getCategories().get(0).getValue() != null))
     {
         myCategory = categoryList
                 .stream()
                 .map(p -> p.get())
                 .filter(a -> a.equals(vComponent.getCategories().get(0).getValue().get(0)))
                 .findFirst()
                 .orElse(categoryList.get(0).get());
     } else
     {
         myCategory = categoryList.get(0).get();
     }
     int index = categoryList.indexOf(myCategory);
     setCategorySelected(index);
     setLPane(index);
     
     // change listener - fires when new icon is selected
     categorySelectedProperty().addListener((observable, oldSelection, newSelection) ->  {
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
 
 public void updateToolTip(int i, String category) {
     if (category != null)
     {
         Tooltip.install(icons[i], new Tooltip(category));
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

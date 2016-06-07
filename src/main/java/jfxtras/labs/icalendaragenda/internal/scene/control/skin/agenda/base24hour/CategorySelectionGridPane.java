package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/** makes a group of colored squares used to select appointment group */
public class CategorySelectionGridPane extends GridPane
{
private Pane[] icons;
private List<String> originalCategories;
//private List<Integer> categoriesIndices;
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
//
//public CategorySelectionGridPane(VComponentDisplayable<?> vComponent, List<String> categories)
//{
//    this();
//    setupData(vComponent, categories);
//}
// 
 public void setupData(String initialCategory, List<String> categories)
// public void setupData()
{
//     List<String> categoriesIndices = IntStream.range(0, categories.size()).mapToObj(i -> Integer.toString(i)).collect(Collectors.toList());
//     List<Integer> categoriesIndices = Collections.unmodifiableList(IntStream.range(0, categories.size()).mapToObj(i -> new Integer(i)).collect(Collectors.toList()));
     List<Integer> categoriesIndices = IntStream.range(0, categories.size()).mapToObj(i -> new Integer(i)).collect(Collectors.toList());
//     originalCategories = categories.stream().map(c -> c + c.hashCode())
     setHgap(3);
     setVgap(3);
     icons = new Pane[categories.size()];
     
//     int lCnt = 0;
//     for (String category : categoriesIndices)
     for (Integer lCnt : categoriesIndices)
//     Iterator<Integer> iter = categoriesIndices.iterator();
//     while (iter.hasNext())
//     for (Integer lCnt : categoriesIndices)
//     for (int lCnt=0; lCnt < categories.size(); lCnt++)
     {
//         Integer lCnt = iter.next();
//         String category = categories.get(lCnt);
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
         updateToolTip(lCnt, categories.get(lCnt));
//         Tooltip tooltip = new Tooltip();
//         tooltip.textProperty().bind(category);
//         Tooltip.install(icons[lCnt], tooltip);

         // mouse 
         setupMouseOverAsBusy(icons[lCnt]);
         icons[lCnt].setOnMouseClicked( (mouseEvent) ->
         {
             mouseEvent.consume(); // consume before anything else, in case there is a problem in the handling
             System.out.println("selected cat:" + lCnt + " " + categories.indexOf(lCnt) + " "+ categories.size() + " ");
//             categories.stream().limit(5).forEach(System.out::println);
//             categorySelected.set(categories.indexOf(category));
             categorySelected.set(lCnt);

//             // assign category
//             String g = categories.get(categorySelected.getValue());
//             vComponent.setCategories(FXCollections.observableArrayList(Categories.parse(g)));
         });
//         lCnt++;
     }

//     final String myCategory;
//     if ((vComponent.getCategories() != null) && (vComponent.getCategories().get(0).getValue() != null))
//     {
//         myCategory = categories
//                 .stream()
////                 .map(p -> p.get())
//                 .filter(a -> a.equals(vComponent.getCategories().get(0).getValue().get(0)))
//                 .findFirst()
//                 .orElse(categories.get(0));
//     } else
//     {
//         myCategory = categories.get(0);
//     }
     int index = categories.indexOf(initialCategory);
     if (index >= 0)
     {
         setCategorySelected(index);
         setLPane(index);
     }
     
     // change listener - fires when new icon is selected
     categorySelectedProperty().addListener((observable, oldSelection, newSelection) ->  {
       int oldS = (int) oldSelection;
       int newS = (int) newSelection;
       System.out.println("icon:" + oldS + " " + newS);
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

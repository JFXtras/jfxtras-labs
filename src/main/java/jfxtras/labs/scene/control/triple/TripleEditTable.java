package jfxtras.labs.scene.control.triple;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.skin.triple.DefaultTripleEditTableSkin;
import jfxtras.labs.internal.scene.control.skin.triple.TripleEditTableSkin;

// TODO - make control instead of HBox?
public abstract class TripleEditTable<T> extends Control
{
	// initialize tableList with extractor so change listeners fire when properties change.
		
	static private String language = "en";
	static private Locale myLocale = new Locale(language);
	static private ResourceBundle defaultResources  = ResourceBundle.getBundle("jfxtras.labs.scene.control.triple.Bundle", myLocale);
	final private ResourceBundle resources;
	
	private List<T> beanList;
	public List<T> getBeanList() {
		return beanList;
	}
	private List<Triple> tripleList;
	
	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
		tripleList = beanList.stream()
			.map(e -> converter.fromBeanElement(e))
			.collect(Collectors.toList());
		TripleEditTableSkin tripleEditTableSkin = (TripleEditTableSkin) getSkin();
		if (tripleEditTableSkin != null)
		{
			ObservableList<Triple> tableList = tripleEditTableSkin.getTableList();
			tableList.clear();
			tableList.addAll(tripleList);
		}
	}
	
//	private String emptyString = "empty"; // TODO - USE ResourceBundle
//	private Callback<CellDataFeatures<Triple, String>, ObservableValue<String>> tripleValueCellValueFactory = (cellData) ->
//	{
//        if (cellData.getValue().isEmpty())
//        {
//            return new SimpleStringProperty(emptyString);
//        } else
//        {
//            return cellData.getValue().valueProperty();
//        }
//	};
	
	private final Predicate<String> validateValue;
//	private  String valueName;
	private final String[] alertTexts;
	private final String[] nameOptions;
	private  TripleConverter<T> converter;
	
	private ListChangeListener<Triple> synchBeanItemTripleChangeLister = (ListChangeListener.Change<? extends Triple> change) ->
    {
    	System.out.println("change:" + change);
        while (change.next())
        {
            if (change.wasUpdated())
            {
            	int to = change.getTo();
            	int from = change.getFrom();
            	for (int i=from; i<to; i++)
            	{
            		Triple t = change.getList().get(i);
            		T e = converter.toBeanElement(t);
            		System.out.println("i:" + i + " " + beanList.size());
            		if (i <= beanList.size()-1)
            		{
            			System.out.println("changed existing element");
            			beanList.set(i, e);
            		} else
            		{
            			beanList.add(e);
            			System.out.println("new element added");
            		}
            	}
            }
        }
    };
	
	// CONSTRUCTOR
	public TripleEditTable(
			Predicate<String> validateValue,
			String valueName,
			TripleConverter<T> converter,
//			Callback<Triple, T> createBeanItemCallback,
			String[] alertTexts,
			String[] nameOptions,
			ResourceBundle resources
			)
	{
		super();
		this.validateValue = validateValue;
//		this.valueName = valueName;
		this.converter = converter;
		this.alertTexts = alertTexts;
		this.nameOptions = nameOptions;
		this.resources = (resources == null) ? defaultResources : resources;
		System.out.println("Construct");
	}
	
	@Override
	public Skin<?> createDefaultSkin()
	{
		return new DefaultTripleEditTableSkin<T>(
				validateValue,
				tripleList,
				converter,
				synchBeanItemTripleChangeLister,
				alertTexts,
				nameOptions,
				resources,
				this
				);
	}

//	// TODO - NEEDS TO BE EXPLICATELY RUN - NO FXML HERE
//	public void initialize()
//	{
//		System.out.println("skin at initialize:" + getSkin());
//        table.setItems(tableList);
//        table.setOnKeyPressed(k -> 
//        {
//    	    if (k.getCode().equals(KeyCode.DELETE)) {
//    	    	Triple selectedItem = table.getSelectionModel().getSelectedItem();
//    	        if (selectedItem.valueProperty() != null)
//    	            deleteItem(selectedItem);
//    	    }
//        });
//        deleteButton.setOnAction(event ->
//        {
//			Triple selectedItem = table.getSelectionModel().getSelectedItem();
//		    if (! selectedItem.isEmpty()) deleteItem(selectedItem);
//        });
//
//        table.setEditable(true);
//        
//	    ObservableList<String> nameList = FXCollections.observableArrayList(nameOptions);
//	    nameColumn.setCellFactory(column -> new ComboBoxTableCell<Triple, String>(nameList));
//	    nameColumn.setCellValueFactory(cellData -> cellData.getValue().labelProperty());
//	    dataColumn.setCellValueFactory(tripleValueCellValueFactory);
//	      
//	    dataColumn.setCellFactory(column -> new EditCell3<Triple, String>(emptyString));
//	    primaryColumn.setCellValueFactory(cellData -> cellData.getValue().primaryProperty());
//	    primaryColumn.setCellFactory(CheckBoxTableCell.forTableColumn(primaryColumn));
//	    
//        deleteButton.setOnAction(e -> {
//        	Triple selectedItem = table.getSelectionModel().getSelectedItem();
//            if (! selectedItem.isEmpty()) deleteItem(selectedItem);
//        });
//
//        table.setOnKeyPressed(new EventHandler<KeyEvent>(){
//            @Override public void handle( KeyEvent k){
//                if (k.getCode().equals(KeyCode.DELETE)) {
//                    Triple selectedItem = table.getSelectionModel().getSelectedItem();
//                    if (selectedItem.valueProperty() != null)
//                        deleteItem(selectedItem);
//                }
//            }
//          });
//        dataColumn.setOnEditCommit(new TripleValueEventHandler(tableList));
//	}
//	
//	public void setup()
//	{
//		// COLUMN WIDTH - need to add up to 1
//	    nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.425));
//	    dataColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.43));
//	    primaryColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.14));
//	    nameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 42.5);
//	    dataColumn.setMaxWidth(1f * Integer.MAX_VALUE * 43);
//	    primaryColumn.setMaxWidth(1f * Integer.MAX_VALUE * 14);	
//	    
//	    System.out.println("columns:" + nameColumn.getWidth() + " " + dataColumn.getWidth() + " " + primaryColumn.getWidth());
//	    System.out.println("pref columns:" + nameColumn.getPrefWidth() + " " + dataColumn.getPrefWidth() + " " + primaryColumn.getPrefWidth());
//
//	}
//		
//    private void deleteItem(Triple selectedItem)
//    {
//        Alert alert = new Alert(AlertType.CONFIRMATION);
//        alert.setTitle("Delete Phone Number?");
//        alert.setContentText("Delete phone number " + 
//                table.getSelectionModel().getSelectedItem().getValue() + "?");
//        ButtonType buttonTypeOne = new ButtonType("Delete");
//        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
//        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == buttonTypeOne) {
//            tableList.remove(selectedItem);
////            writeNeeded.set(! writeNeeded.getValue()); // toggle writeNeeded property
//        }
//    }
//
//	private class TripleValueEventHandler implements EventHandler<CellEditEvent<Triple, String>>
//    {
////        private final Class<Triple> myClass;
////        private final BooleanProperty triggerProperty;
////        private final ObservableList<Triple> tableList;
////        private final String[] alertTexts;
//        private boolean alertOn = false;
//            
//        public TripleValueEventHandler(
//                  ObservableList<Triple> tableList
////                , Class<Triple> myClass
////                , BooleanProperty triggerProperty
////                , String[] alertTexts
//                  )
//        {
////            this.myClass = myClass;
////            this.triggerProperty = triggerProperty;
////            this.tableList = tableList;
////            this.alertTexts = alertTexts;
//        }
//
//        @Override
//        public void handle(CellEditEvent<Triple, String> t)
//        {
//        	Triple n = new Triple(valueName);  // new instance to get validate predicate and possibly to add empty row
////        	T n = createNewElementCallback.call(null);
////        	T n = getNewInstance(myClass);  // new instance to get validate predicate and possibly to add empty row
////        	Predicate<String> vaildate = n.getVaildate();
//			boolean isValueOK = validateValue.test(t.getNewValue());
//          if (isValueOK)
//          {
//              t.getTableView().getItems().get(t.getTablePosition().getRow()).setValue(t.getNewValue());
////              triggerProperty.set(! triggerProperty.getValue());  // flip trigger to fire change listener to write new data
//              System.out.println("tableList:" + tableList);
//              if (! tableList.get(tableList.size()-1).isEmpty())
//              {
//                  tableList.add(n); // create empty row
//              }
//          } else {
//              if (! alertOn) {  // prevent 2nd alert from duplicate triggers
//                  alertOn = true;
//                  Alert alert = new Alert(AlertType.WARNING);
//                  alert.setTitle(alertTexts[0]);
//                  alert.setHeaderText(alertTexts[1]);
//                  alert.setContentText(alertTexts[2]);
//                  ButtonType buttonTypeOk = new ButtonType(alertTexts[3], ButtonData.CANCEL_CLOSE);
//                  alert.getButtonTypes().setAll(buttonTypeOk);
//                  alert.showAndWait();
//                  t.getTableView().getItems().get(t.getTablePosition().getRow()).setValue(t.getOldValue());
//                  // workaround for refreshing rendered values
//                  t.getTableView().getColumns().get(0).setVisible(false);
//                  t.getTableView().getColumns().get(0).setVisible(true);
//                  alertOn = false;
//              }
//          }
//        }
//    }
	
	
	
	
	
//    private static void loadFxml(URL fxmlFile, Object rootController, ResourceBundle resources)
//    {
//        FXMLLoader loader = new FXMLLoader(fxmlFile);
//        loader.setController(rootController);
//        loader.setRoot(rootController);
//        loader.setResources(resources);
//        try {
//            loader.load();
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
    
//    public class Triple
//    {
//        private final StringProperty label = new SimpleStringProperty(this, "label");
//        public StringProperty labelProperty() { return label; }
//        public void setLabel(String label) { this.label.set(label); }
//        public String getName() { return label.getValue(); }
//        public Triple withLabel(String value) { setLabel(value); return this; }
//
//        private final StringProperty value;
//        public StringProperty valueProperty() { return value; }
//        public String getValue() { return value.getValue(); }
//        public boolean setValue(String value) {
//            if (value != null) {
//                boolean ok = validateValue.test(value);
//                System.out.println("value ok:" + ok + " " + value);
//                if (ok)
//                {
//                    if (this.value == null)
//                        this.value.set(value);
//                    else
//                        this.value.set(value);
//                }
//                return ok;
//            }
//            return false;
//        }
//        public boolean isEmpty() { return value.getValue() == null; }
//        public Triple withValue(String value) { setValue(value); return this; }
//
//        private final BooleanProperty primary = new SimpleBooleanProperty(this, "primary", false);
//        public BooleanProperty primaryProperty() { return primary; }
//        public void setPrimary(Boolean primary) { this.primary.set(primary); }
//        public Boolean isPrimary() { return primary.getValue(); }
//        public Triple withPrimary(boolean value) { setPrimary(value); return this; }
//        
//        // Constructor
//        public Triple(String valueName)
//        {
//            value = new SimpleStringProperty(this, valueName);
//        }
//    }
    
//    public interface tripleConverter
//    {
//    	abstract Triple fromBeanElement(T beanElement);
//    	abstract T toBeanElement(Triple triple);
//    }
    
//    private class EditCell3<T, E> extends TableCell<T, String>
//    {
//
//    	private TextField textField;
//    	private String emptyCell;
//
//    	public EditCell3() {
//    	    super();
//    	    this.emptyCell = null;
//    	}
//    	public EditCell3(String emptyCell) {
//    	    super();
//    	    this.emptyCell = emptyCell;
//    	}
//
//    	@Override
//    	public void startEdit() {
//    	    if (!isEmpty()) {
//    	        super.startEdit();
//    	        createTextField();
//    	        setText(null);
//    	        setGraphic(textField);
//    	        textField.requestFocus();   // select all text
//    	        // if not empty unselect all text and move caret to front (can't figure out how to move it to end)
//    	        if (! getString().equals(emptyCell)) textField.selectEnd();
//    	    }
//    	}
//
//    	@Override
//    	public void cancelEdit() {
//    	    super.cancelEdit();
//    	    
//    	    setText((String) getItem());
//    	    setGraphic(null);
//    	}
//
//    	@Override
//    	public void updateItem(String item, boolean empty) {
//    	super.updateItem(item, empty);
//
//    	if (empty) {
//    	    setText(null);
//    	    setGraphic(null);
//    	    } else {
//    	        if (isEditing()) {
//    	            if (textField != null) {
//    	            textField.setText(getString());
//    	            }
//    	            setText(null);
//    	            setGraphic(textField);
//    	        } else {
//    	            setText(getString());
//    	            setGraphic(null);
//    	        }
//    	    }
//    	}
//
//    	private void createTextField() {
//    	    textField = new TextField(getString());
//    	    textField.setOnAction(evt -> {  // enable ENTER commit
//    	        commitEdit(textField.getText());
//    	    });
//
//    	    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
//    	    
//    	    ChangeListener<? super Boolean> changeListener = (observable, oldSelection, newSelection) ->
//    	    {
//    	        if (! newSelection)
//    	            commitEdit(textField.getText());
//    	    };
//    	    textField.focusedProperty().addListener(changeListener);
//    	    
//    	    textField.setOnKeyPressed((ke) -> {
//    	        if (ke.getCode().equals(KeyCode.ESCAPE)) {
//    	            textField.focusedProperty().removeListener(changeListener);
//    	            cancelEdit();
//    	        }
//    	    });
//    	}
//
//    	private String getString() {
//    	    return getItem() == null ? "" : getItem().toString();
//    	}
//
//
//    	@Override
//    	public void commitEdit(String item) {
//
//    	if (isEditing()) {
//    	    super.commitEdit(item);
//    	} else {
//    	    final TableView table = getTableView();
//    	    if (table != null) {
//    	        TablePosition position = new TablePosition(getTableView(), getTableRow().getIndex(), getTableColumn());
//    	        CellEditEvent editEvent = new CellEditEvent(table, position, TableColumn.editCommitEvent(), item);
//    	        Event.fireEvent(getTableColumn(), editEvent);
//    	    }
//    	        updateItem(item, false);
//    	        if (table != null) {
//    	            table.edit(-1, null);
//    	        }
//
//    	    }
//    	}
//    }
}
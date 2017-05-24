package jfxtras.labs.internal.scene.control.skin.triple;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import jfxtras.labs.scene.control.triple.Triple;
import jfxtras.labs.scene.control.triple.TripleConverter;
import jfxtras.labs.scene.control.triple.TripleEditTable;

public class DefaultTripleEditTableSkin<T> extends SkinBase<TripleEditTable<T>> implements TripleEditTableSkin
{
	@FXML public TableView<Triple> table;
	@FXML private TableColumn<Triple, String> dataColumn;
	@FXML private TableColumn<Triple, String> nameColumn;
	@FXML private TableColumn<Triple, Boolean> primaryColumn;
	@FXML private Button deleteButton;
	@FXML final protected ResourceBundle resources;

	private final ObservableList<Triple> tableList = FXCollections.observableArrayList(e -> new Observable[]
			{
				e.labelProperty(),
				e.valueProperty(),
				e.primaryProperty()
			});
	private ListChangeListener<Triple> synchBeanItemTripleChangeLister;
	public ObservableList<Triple> getTableList() {
		return tableList;
	}
	
	private String emptyString = "empty"; // TODO - USE ResourceBundle
	private Callback<CellDataFeatures<Triple, String>, ObservableValue<String>> tripleValueCellValueFactory = (cellData) ->
	{
        if (cellData.getValue().isEmpty())
        {
            return new SimpleStringProperty(emptyString);
        } else
        {
            return cellData.getValue().valueProperty();
        }
	};

	private final Predicate<String> validateValue;
	private  String valueName;
	private final String[] alertTexts;
	private final String[] nameOptions;
	// CONSTRUCTOR
	public DefaultTripleEditTableSkin(
			Predicate<String> validateValue,
			List<Triple> initialTripleList,
			TripleConverter<T> converter,
			ListChangeListener<Triple> synchBeanItemTripleChangeLister,
			String[] alertTexts,
			String[] nameOptions,
			ResourceBundle resources,
			TripleEditTable<T> control
			)
	{
		super(control);
		this.resources = resources;
		this.alertTexts = alertTexts;
		this.validateValue = validateValue;
		this.nameOptions = nameOptions;
		this.synchBeanItemTripleChangeLister = synchBeanItemTripleChangeLister;

		// setup component
		createNodes();
		setupListeners(initialTripleList);
	}
	
	private void createNodes()
	{
		getChildren().clear();
		HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TripleTable.fxml"));
        loader.setController(this);
        loader.setRoot(hbox);
        loader.setResources(resources);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
		getChildren().add(hbox);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		tableList.removeListener(synchBeanItemTripleChangeLister);
		tableList.removeListener(maintainEmptyRowTripleChangeLister);
	}
	
	private ListChangeListener<Triple> maintainEmptyRowTripleChangeLister = (ListChangeListener.Change<? extends Triple> change) ->
    {
        boolean isEmptyPresent = change.getList().stream()
        		.peek(e -> System.out.println("table values:" + e.getValue()))
        	.anyMatch(e -> (e.getValue() == null) || (e.getValue().equals(emptyString)));
        System.out.println("isEmptyPresent:" + isEmptyPresent);
        if (! isEmptyPresent)
        {
			tableList.add(new Triple(valueName));
	        System.out.println("tableList size:" + tableList.size());
        }
    };
    
    // toggles Delete button
    private final ChangeListener<Triple> toggleDeleteButtonChangeListener = (observable, oldSelection, newSelection) ->
    {
        if (newSelection != null && newSelection.isEmpty())
        {
            deleteButton.setDisable(true);
        } else
        {
            deleteButton.setDisable(false);
        }
    };

	private void setupListeners(List<Triple> initialTripleList)
	{
		table.setItems(tableList);
		
	    tableList.addListener(maintainEmptyRowTripleChangeLister);
		tableList.addListener(synchBeanItemTripleChangeLister);
		table.getSelectionModel().selectedItemProperty().addListener(toggleDeleteButtonChangeListener);
		if (initialTripleList != null)
		{
			tableList.addAll(initialTripleList);
		}

        table.setOnKeyPressed(k -> 
        {
    	    if (k.getCode().equals(KeyCode.DELETE)) {
    	    	Triple selectedItem = table.getSelectionModel().getSelectedItem();
    	        if (selectedItem.valueProperty() != null)
    	            deleteItem(selectedItem);
    	    }
        });
        deleteButton.setOnAction(event ->
        {
			Triple selectedItem = table.getSelectionModel().getSelectedItem();
		    if (! selectedItem.isEmpty()) deleteItem(selectedItem);
        });

        table.setEditable(true);
        
	    ObservableList<String> nameList = FXCollections.observableArrayList(nameOptions);
	    nameColumn.setCellFactory(column -> new ComboBoxTableCell<Triple, String>(nameList));
	    nameColumn.setCellValueFactory(cellData -> cellData.getValue().labelProperty());
	    dataColumn.setCellValueFactory(tripleValueCellValueFactory);
	      
	    dataColumn.setCellFactory(column -> new EditCell3<Triple, String>(emptyString));
	    primaryColumn.setCellValueFactory(cellData -> cellData.getValue().primaryProperty());
	    primaryColumn.setCellFactory(CheckBoxTableCell.forTableColumn(primaryColumn));
	    
        deleteButton.setOnAction(e -> {
        	Triple selectedItem = table.getSelectionModel().getSelectedItem();
            if (! selectedItem.isEmpty()) deleteItem(selectedItem);
        });

        table.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override public void handle( KeyEvent k){
                if (k.getCode().equals(KeyCode.DELETE)) {
                    Triple selectedItem = table.getSelectionModel().getSelectedItem();
                    if (selectedItem.valueProperty() != null)
                        deleteItem(selectedItem);
                }
            }
          });
        dataColumn.setOnEditCommit(new TripleValueEventHandler());

		// COLUMN WIDTH - need to add up to 1
	    nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.425));
	    dataColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.43));
	    primaryColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.14));
	    nameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 42.5);
	    dataColumn.setMaxWidth(1f * Integer.MAX_VALUE * 43);
	    primaryColumn.setMaxWidth(1f * Integer.MAX_VALUE * 14);	
	    
	    System.out.println("columns:" + nameColumn.getWidth() + " " + dataColumn.getWidth() + " " + primaryColumn.getWidth());
	    System.out.println("pref columns:" + nameColumn.getPrefWidth() + " " + dataColumn.getPrefWidth() + " " + primaryColumn.getPrefWidth());

	}
		
    private void deleteItem(Triple selectedItem)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Phone Number?");
        alert.setContentText("Delete phone number " + 
                table.getSelectionModel().getSelectedItem().getValue() + "?");
        ButtonType buttonTypeOne = new ButtonType("Delete");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            tableList.remove(selectedItem);
//            writeNeeded.set(! writeNeeded.getValue()); // toggle writeNeeded property
        }
    }

	private class TripleValueEventHandler implements EventHandler<CellEditEvent<Triple, String>>
    {
        private boolean alertOn = false;

        @Override
        public void handle(CellEditEvent<Triple, String> t)
        {
//        	Triple n = new Triple(valueName);  // new instance to get validate predicate and possibly to add empty row
			boolean isValueOK = validateValue.test(t.getNewValue());
			System.out.println("is valid??? " + isValueOK);
          if (isValueOK)
          {
              t.getTableView().getItems().get(t.getTablePosition().getRow()).setValue(t.getNewValue());
          } else {
              if (! alertOn) {  // prevent 2nd alert from duplicate triggers
                  alertOn = true;
                  String oldValue = (t.getOldValue().equals(emptyString)) ? null : t.getOldValue();
				System.out.println("tableList1:" + tableList.size() + " " + "old:" + oldValue);
                  t.getTableView().getItems().get(t.getTablePosition().getRow()).setValue(oldValue);
//                  boolean removed = tableList.remove(t.getRowValue());
//                  System.out.println("tableList2:" + tableList.size() + removed);
                  Alert alert = new Alert(AlertType.WARNING);
                  alert.setTitle(alertTexts[0]);
                  alert.setHeaderText(alertTexts[1]);
                  alert.setContentText(alertTexts[2]);
                  // TODO - not detecting invalid changes - pressing enter should cancel - doesn't
                  ButtonType buttonTypeOk = new ButtonType(alertTexts[3], ButtonData.CANCEL_CLOSE);
                  alert.getButtonTypes().setAll(buttonTypeOk);
                  alert.showAndWait();
//                  t.getTableView().getItems().get(t.getTablePosition().getRow()).setValue(t.getOldValue());
                  // workaround for refreshing rendered values
                  t.getTableView().getColumns().get(0).setVisible(false);
                  t.getTableView().getColumns().get(0).setVisible(true);
                  alertOn = false;
              	}
            }
          System.out.println("done event handler");
        }
    }
	
    private class EditCell3<T, E> extends TableCell<T, String>
    {

    	private TextField textField;
    	private String emptyCell;

    	public EditCell3() {
    	    super();
    	    this.emptyCell = null;
    	}
    	public EditCell3(String emptyCell) {
    	    super();
    	    this.emptyCell = emptyCell;
    	}

    	@Override
    	public void startEdit() {
    	    if (!isEmpty()) {
    	        super.startEdit();
    	        createTextField();
    	        setText(null);
    	        setGraphic(textField);
    	        textField.requestFocus();   // select all text
    	        // if not empty unselect all text and move caret to front (can't figure out how to move it to end)
    	        if (! getString().equals(emptyCell)) textField.selectEnd();
    	    }
    	}

    	@Override
    	public void cancelEdit() {
    	    super.cancelEdit();
    	    
    	    setText((String) getItem());
    	    setGraphic(null);
    	}

    	@Override
    	public void updateItem(String item, boolean empty) {
    	super.updateItem(item, empty);

    	if (empty) {
    	    setText(null);
    	    setGraphic(null);
    	    } else {
    	        if (isEditing()) {
    	            if (textField != null) {
    	            textField.setText(getString());
    	            }
    	            setText(null);
    	            setGraphic(textField);
    	        } else {
    	            setText(getString());
    	            setGraphic(null);
    	        }
    	    }
    	}

    	private void createTextField() {
    	    textField = new TextField(getString());
    	    textField.setOnAction(evt -> {  // enable ENTER commit
    	        commitEdit(textField.getText());
    	    });

    	    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
    	    
    	    ChangeListener<? super Boolean> changeListener = (observable, oldSelection, newSelection) ->
    	    {
    	        if (! newSelection)
    	            commitEdit(textField.getText());
    	    };
    	    textField.focusedProperty().addListener(changeListener);
    	    
    	    textField.setOnKeyPressed((ke) -> {
    	        if (ke.getCode().equals(KeyCode.ESCAPE)) {
    	            textField.focusedProperty().removeListener(changeListener);
    	            cancelEdit();
    	        }
    	    });
    	}

    	private String getString() {
    	    return getItem() == null ? "" : getItem().toString();
    	}


    	@Override
    	public void commitEdit(String item) {

    	if (isEditing()) {
    	    super.commitEdit(item);
    	} else {
    	    final TableView table = getTableView();
    	    if (table != null) {
    	        TablePosition position = new TablePosition(getTableView(), getTableRow().getIndex(), getTableColumn());
    	        CellEditEvent editEvent = new CellEditEvent(table, position, TableColumn.editCommitEvent(), item);
    	        Event.fireEvent(getTableColumn(), editEvent);
    	    }
    	        updateItem(item, false);
    	        if (table != null) {
    	            table.edit(-1, null);
    	        }

    	    }
    	}
    }
}

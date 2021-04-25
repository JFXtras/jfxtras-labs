/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.internal.scene.control.skin.edittable.triple;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import jfxtras.labs.scene.control.edittable.triple.Triple;
import jfxtras.labs.scene.control.edittable.triple.TripleConverter;
import jfxtras.labs.scene.control.edittable.triple.TripleEditTable;

// A is name, B is value, C is primary
public abstract class TripleEditTableSkinBase<T,A,B,C> extends SkinBase<TripleEditTable<T,A,B,C>> implements TripleEditTableSkin<A,B,C>
{
	final private ResourceBundle resources;

	private ListChangeListener<Triple<A,B,C>> synchBeanItemTripleChangeLister;
	@Override
	public ObservableList<Triple<A,B,C>> getTableList() {
		return hbox.getTableList();
	}
	
	protected String emptyString;
	private Callback<CellDataFeatures<Triple<A,B,C>, B>, ObservableValue<B>> tripleValueCellValueFactory = (cellData) ->
	{
        if (cellData.getValue().isEmpty())
        {
            return new SimpleObjectProperty<B>((B) emptyString);
        } else
        {
            return cellData.getValue().valueProperty();
        }
	};

	private final Predicate<B> validateValue;
	private final String[] alertTexts;
	private final A[] nameOptions;
	
	// CONSTRUCTOR
	public TripleEditTableSkinBase(
			Predicate<B> validateValue,
			List<Triple<A,B,C>> initialTripleList,
			TripleConverter<T,A,B,C> converter,
			ListChangeListener<Triple<A,B,C>> synchBeanItemTripleChangeLister,
			String[] alertTexts,
			A[] nameOptions,
			ResourceBundle resources,
			TripleEditTable<T,A,B,C> control
			)
	{
		super(control);
		this.resources = resources;
		emptyString = resources.getString("empty");
		this.alertTexts = alertTexts;
		this.validateValue = validateValue;
		this.nameOptions = nameOptions;
		this.synchBeanItemTripleChangeLister = synchBeanItemTripleChangeLister;

		// setup component
		createNodes();
		System.out.println(initialTripleList);
		setupListeners(initialTripleList);
	}
	
	protected TripleHBox<A,B,C> hbox;
	private void createNodes()
	{
		getChildren().clear();
		hbox = new TripleHBox<A,B,C>(resources);
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
		getChildren().add(hbox);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		getTableList().removeListener(synchBeanItemTripleChangeLister);
		getTableList().removeListener(maintainEmptyRowTripleChangeLister);
	}
	
	private ListChangeListener<Triple<A,B,C>> maintainEmptyRowTripleChangeLister = (ListChangeListener.Change<? extends Triple<A,B,C>> change) ->
    {
        boolean isEmptyPresent = change.getList().stream()
//        		.peek(e -> System.out.println("table values:" + e.getValue()))
        	.anyMatch(e -> (e.getValue() == null) || (e.getValue().equals(emptyString)));
        System.out.println("isEmptyPresent:" + isEmptyPresent);
        if (! isEmptyPresent)
        {
			getTableList().add(new Triple<A,B,C>());
	        System.out.println("added empty, getTableList() size:" + getTableList().size());
        }
    };
    
    // toggles Delete button
    private final ChangeListener<Triple<A,B,C>> toggleDeleteButtonChangeListener = (observable, oldSelection, newSelection) ->
    {
        if (newSelection != null && newSelection.isEmpty())
        {
        	hbox.deleteButton.setDisable(true);
        } else
        {
        	hbox.deleteButton.setDisable(false);
        }
    };

    protected void setupListeners(List<Triple<A,B,C>> initialTripleList)
	{
	    getTableList().addListener(maintainEmptyRowTripleChangeLister);
		getTableList().addListener(synchBeanItemTripleChangeLister);
		if (initialTripleList != null)
		{
			getTableList().addAll(initialTripleList);
			if (initialTripleList.isEmpty())
			{
				getTableList().add(new Triple<A,B,C>());
			}
		}

		hbox.table.getSelectionModel().selectedItemProperty().addListener(toggleDeleteButtonChangeListener);

        hbox.table.setOnKeyPressed(k -> 
        {
    	    if (k.getCode().equals(KeyCode.DELETE)) {
    	    	Triple<A,B,C> selectedItem = hbox.table.getSelectionModel().getSelectedItem();
    	        if (selectedItem.valueProperty() != null)
    	            deleteItem(selectedItem);
    	    }
        });
        hbox.deleteButton.setOnAction(event ->
        {
			Triple<A,B,C> selectedItem = hbox.table.getSelectionModel().getSelectedItem();
		    if (! selectedItem.isEmpty()) deleteItem(selectedItem);
        });

        hbox.table.setEditable(true);
        
	    ObservableList<A> nameList = FXCollections.observableArrayList(nameOptions);
	    hbox.nameColumn.setCellFactory(column -> new ComboBoxTableCell<Triple<A,B,C>, A>(nameList));
	    hbox.nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
	    hbox.dataColumn.setCellValueFactory(tripleValueCellValueFactory);
	    
//	    hbox.dataColumn.setCellFactory(column -> new EditCell3<Triple<A,B,C>, B>(emptyString));
	    hbox.primaryColumn.setCellValueFactory(cellData -> cellData.getValue().primaryProperty());
//	    hbox.primaryColumn.setCellFactory(CheckBoxTableCell.forTableColumn(hbox.primaryColumn));
	    
        hbox.deleteButton.setOnAction(e -> {
        	Triple<A,B,C> selectedItem = hbox.table.getSelectionModel().getSelectedItem();
            if (! selectedItem.isEmpty()) deleteItem(selectedItem);
        });

        hbox.table.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override public void handle( KeyEvent k){
                if (k.getCode().equals(KeyCode.DELETE)) {
                    Triple<A,B,C> selectedItem = hbox.table.getSelectionModel().getSelectedItem();
                    if (selectedItem.valueProperty() != null)
                        deleteItem(selectedItem);
                }
            }
          });
        hbox.dataColumn.setOnEditCommit(new TripleValueEventHandler());
        hbox.nameColumn.setOnEditCommit(t ->
    	{
            t.getTableView()
            	.getItems()
            	.get(t.getTablePosition().getRow())
            	.setName(t.getNewValue());
    	});

		// COLUMN WIDTH - need to add up to 1
//	    hbox.nameColumn.prefWidthProperty().bind(hbox.table.widthProperty().multiply(0.425));
//	    hbox.dataColumn.prefWidthProperty().bind(hbox.table.widthProperty().multiply(0.43));
//	    hbox.primaryColumn.prefWidthProperty().bind(hbox.table.widthProperty().multiply(0.14));
	    hbox.nameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 42.5);
	    hbox.dataColumn.setMaxWidth(1f * Integer.MAX_VALUE * 43);
	    hbox.primaryColumn.setMaxWidth(1f * Integer.MAX_VALUE * 14);	
	}
		
    private void deleteItem(Triple<A,B,C> selectedItem)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Phone Number?");
        alert.setContentText("Delete phone number " + 
                hbox.table.getSelectionModel().getSelectedItem().getValue() + "?");
        ButtonType buttonTypeOne = new ButtonType("Delete");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne)
        {
            getTableList().remove(selectedItem);
        }
    }

	private class TripleValueEventHandler implements EventHandler<CellEditEvent<Triple<A,B,C>, B>>
    {
        private boolean alertOn = false;

        @Override
        public void handle(CellEditEvent<Triple<A,B,C>, B> t)
        {
			boolean isValueOK = validateValue.test(t.getNewValue());
          if (isValueOK)
          {
              t.getTableView()
              		.getItems()
              		.get(t.getTablePosition().getRow())
              		.setValue(t.getNewValue());
          } else {
              if (! alertOn) {  // prevent 2nd alert from duplicate triggers
                  alertOn = true;
                  B oldValue = (t.getOldValue().equals(emptyString)) ? null : t.getOldValue();
                  t.getTableView()
                  		.getItems()
                  		.get(t.getTablePosition().getRow())
                  		.setValue(oldValue);
                  Alert alert = new Alert(AlertType.WARNING);
                  alert.setTitle(alertTexts[0]);
                  alert.setHeaderText(alertTexts[1]);
                  alert.setContentText(alertTexts[2]);
                  ButtonType buttonTypeOk = new ButtonType(alertTexts[3], ButtonData.CANCEL_CLOSE);
                  alert.getButtonTypes().setAll(buttonTypeOk);
                  alert.showAndWait();

                  // workaround for refreshing rendered values
                  t.getTableView().getColumns().get(0).setVisible(false);
                  t.getTableView().getColumns().get(0).setVisible(true);
                  alertOn = false;
              	}
            }
        }
    }
}

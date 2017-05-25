package jfxtras.labs.internal.scene.control.skin.edittable.triple;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import jfxtras.labs.scene.control.edittable.triple.Triple;

public class TripleHBox<A,B,C> extends HBox
{
	@FXML protected TableView<Triple<A,B,C>> table;
	@FXML protected TableColumn<Triple<A,B,C>, A> nameColumn;
	@FXML protected TableColumn<Triple<A,B,C>, B> dataColumn;
	@FXML protected TableColumn<Triple<A,B,C>, C> primaryColumn;
	@FXML protected Button deleteButton;
	@FXML protected ResourceBundle resources;
	
	final ObservableList<Triple<A,B,C>> tableList = FXCollections.observableArrayList(e -> new Observable[]
			{
				e.nameProperty(),
				e.valueProperty(),
				e.primaryProperty()
			});
	public ObservableList<Triple<A,B,C>> getTableList() {
		return tableList;
	}
	
	private static String language = "en";
	private static Locale myLocale = new Locale(language);
	private static ResourceBundle defaultResources  = ResourceBundle.getBundle("jfxtras.labs.scene.control.triple.Bundle", myLocale);
	
	public TripleHBox()
	{
		this(defaultResources);
	}
	
	public TripleHBox(ResourceBundle resources)
	{
		loadFxml(getClass().getResource("TripleHBox.fxml"), this, resources);
		System.out.println("set items:" + tableList);
		table.setItems(tableList);
	}
	
    private static void loadFxml(URL fxmlFile, Object rootController, ResourceBundle resources)
    {
        FXMLLoader loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        loader.setResources(resources);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}

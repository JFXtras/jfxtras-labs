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

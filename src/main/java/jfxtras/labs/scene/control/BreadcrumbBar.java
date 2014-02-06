/**
 * BreadcrumbBar.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control;

import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import jfxtras.labs.util.BreadcrumbBarEventHandler;

/**
 * The container that will host BreadcrumbItem.
 *
 * @author Thierry Wasylczenko
 */
public class BreadcrumbBar extends Control {

    private static final String DEFAULT_STYLE_CLASS = "breadcrumbbar";
    
    private final ReadOnlyListProperty<BreadcrumbItem> items = new SimpleListProperty<BreadcrumbItem>(FXCollections.observableArrayList(new ArrayList<BreadcrumbItem>()));
    private final ObjectProperty<BreadcrumbItem> homeItem = new SimpleObjectProperty<BreadcrumbItem>();
    private final ObjectProperty<BreadcrumbBarEventHandler> onItemAction = new SimpleObjectProperty<BreadcrumbBarEventHandler>();
    
    /**
     * Create a new BreadcrumbBar.
     */
    public BreadcrumbBar() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setPrefHeight(30);
        setPrefWidth(200);
        
        this.onItemAction.addListener(new ChangeListener<BreadcrumbBarEventHandler>() {
            @Override
            public void changed(ObservableValue<? extends BreadcrumbBarEventHandler> observable, BreadcrumbBarEventHandler oldValue, BreadcrumbBarEventHandler newValue) {
                if (oldValue != null) {
                    for (BreadcrumbItem i : itemsProperty()) {
                        i.removeEventHandler(MouseEvent.MOUSE_CLICKED, oldValue);
                    }
                }

                if (newValue != null) {
                    for (BreadcrumbItem i : itemsProperty()) {
                        i.addEventHandler(MouseEvent.MOUSE_CLICKED, newValue);
                    }
                }
            }
        });

        this.items.get().addListener(new ListChangeListener<BreadcrumbItem>() {
            @Override
            public void onChanged(Change<? extends BreadcrumbItem> c) {
                // Adding the onActionItem
                while (c.next()) {
                    if (c.wasAdded() && onItemAction.get() != null) {
                        for (BreadcrumbItem i : c.getAddedSubList()) {
                            i.addEventHandler(MouseEvent.MOUSE_CLICKED, getOnItemAction());
                        }
                    }
                }
                c.reset();

                // Updating the firstProperty() of each item
                synchronized (BreadcrumbBar.this.itemsProperty()) {
                    for (BreadcrumbItem item : BreadcrumbBar.this.itemsProperty()) {
                        ((SimpleBooleanProperty) item.firstProperty()).set(items.indexOf(item) == 0);
                    }

                }
            }
        });
    }

    /**
     * Contains all items added to the bar. The property won't never be null.
     *
     * @return the list of items in the bar.
     */
    public final ReadOnlyListProperty<BreadcrumbItem> itemsProperty() {
        return this.items;
    }

    /**
     * The item considered ad home for the bar. The value of this property will
     * be <b>null</b> if the item has not been initialized. The home item is not
     * mandatory.
     *
     * @return the home item if any.
     */
    public final ObjectProperty<BreadcrumbItem> homeItemProperty() {
        return this.homeItem;
    }

    /**
     * Get the BreadcrumbItem considered as home for this bar.
     *
     * @return The item if any, null otherwise.
     */
    public final BreadcrumbItem getHomeItem() {
        return this.homeItemProperty().get();
    }

    /**
     * Set the home item for the bar.
     *
     * @param homeItem
     */
    public final void setHomeItem(BreadcrumbItem homeItem) {
        this.homeItemProperty().set(homeItem);
    }

    /**
     * Get the event handler that is applied to each item in the bar.
     *
     * @return the event handler if any, <b>null</b> otherwise.
     */
    public final ObjectProperty<BreadcrumbBarEventHandler> onItemActionProperty() {
        return this.onItemAction;
    }

    /**
     * Get the event handler that is applied to each item in the bar.
     *
     * @return the event handler if any, <b>null</b> otherwise.
     */
    public final BreadcrumbBarEventHandler getOnItemAction() {
        return this.onItemActionProperty().get();
    }

    /**
     * Define the event handler that will be applied to each item in the bar. If
     * a previous handler has been defined, it will be removed for every item
     * present in the bar. If the new handler is null, only the older one is
     * removed for every item.
     *
     * @param handler the new handler that will be executed for a click on each
     * item in the bar.
     */
    public final void setOnItemAction(BreadcrumbBarEventHandler handler) {
        this.onItemActionProperty().set(handler);
    }
    
    /**
     * Add a new item at the end of the bar. If the item null, nothing is
     * performed.
     *
     * @param item the item to add in the bar.
     * @return The added item.
     */
    public final BreadcrumbItem addItem(final BreadcrumbItem item) {
        if (item != null) {
            itemsProperty().add(item);
        }
        return item;
    }

    /**
     * Define the home item for this bar. The home item will always be the first
     * item of the bar. If a previous home item has been set it will be
     * replaced.
     *
     * @param content the content of the home item.
     * @return The home item.
     */
    public final BreadcrumbItem addHome(Node content) {
        Image icon = new Image(getClass().getResource("/jfxtras/labs/internal/scene/control/home.png").toExternalForm());
        BreadcrumbItem hItem = new BreadcrumbItem(this, "", icon, content);
        if (getHomeItem() == null) {
            ((SimpleListProperty) this.itemsProperty()).add(0, hItem);
        } else {
            ((SimpleListProperty) this.itemsProperty()).set(0, hItem);
        }
        setHomeItem(hItem);
        return hItem;
    }

    /**
     * Create a new item in the bar.
     *
     * @param text The text of the item, could be null
     * @param icon The icon of the item, could be null
     * @param content The content of the item, could be null
     * @return The added item.
     */
    public final BreadcrumbItem addItem(String text, Image icon, Node content) {
        final BreadcrumbItem item = new BreadcrumbItem(this, text, icon, content);
        return addItem(item);
    }

    /**
     * Create a new item in the bar.
     *
     * @param text The text of the item, could be null
     * @param svgIcon The SVG icon of the item, could be null
     * @param content The content of the item, could be null
     * @return The added item.
     */
    public final BreadcrumbItem addItem(String text, SVGPath svgIcon, Node content) {
        final BreadcrumbItem item = new BreadcrumbItem(this, text, svgIcon, content);
        return addItem(item);
    }
    
    /**
     * Create a new item in the bar without an icon.
     *
     * @param text The text of the item, could be null
     * @param content The content of the item, could be null
     * @return The added item.
     * @return
     */
    public final BreadcrumbItem addItem(String text, Node content) {
        final BreadcrumbItem item = new BreadcrumbItem(this, text, (Image) null, content);
        return addItem(item);
    }

    /**
     * Create a new item in the bar with no text.
     *
     * @param icon The icon of the item, could be null
     * @param content The content of the item, could be null
     * @return The added item.
     */
    public final BreadcrumbItem addItem(Image icon, Node content) {
        final BreadcrumbItem item = new BreadcrumbItem(this, null, icon, content);
        return addItem(item);
    }

    /**
     * Remove an item and every following items in the bar.
     *
     * @param item The item to remove.
     * @return true if something has been deleted, false otherwise.
     */
    public final boolean removeItem(BreadcrumbItem item) {
        boolean deleted = false;

        if (item != null) {
            int index = this.items.indexOf(item);

            if (index == this.items.size() - 1) {
                deleted = ((SimpleListProperty) this.itemsProperty()).remove(item);
            } else if (index < this.items.size()) {
                int previousSize = this.items.size();
                ((SimpleListProperty) this.itemsProperty()).remove(index, this.items.size());
                deleted = previousSize > this.items.size();
            }
        }

        return deleted;
    }

    /**
     * Remove the item at the given index and every following item.
     *
     * @param index the index of the item to remove.
     * @return true if something has been deleted, false otherwise.
     */
    public final boolean removeItem(int index) {
        boolean deleted = false;

        if (index >= 0 && index < this.itemsProperty().getSize()) {
            deleted = removeItem(this.itemsProperty().get(index));
        }

        return deleted;
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/jfxtras/labs/internal/scene/control/" + getClass().getSimpleName() + ".css").toExternalForm();
    }
}

/**
 * BreadcrumbItem.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;

/**
 * An item that can be added to a BreadcrumbBar.
 *
 * @author Thierry Wasylczenko
 */
public class BreadcrumbItem extends Control {

    private static final String DEFAULT_STYLE_CLASS = "breadcrumbitem";
    private final ReadOnlyObjectProperty<BreadcrumbBar> breadcrumbBar = new SimpleObjectProperty<BreadcrumbBar>();
    private final ObjectProperty<Node> content = new SimpleObjectProperty<Node>();
    private final StringProperty text = new SimpleStringProperty();
    private final ObjectProperty<Image> icon = new SimpleObjectProperty<Image>();
    private final ObjectProperty<SVGPath> svgIcon = new SimpleObjectProperty<SVGPath>();
    private final ReadOnlyBooleanProperty first = new SimpleBooleanProperty(false);

    private BreadcrumbItem() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (BreadcrumbItem.this.breadcrumbBarProperty().get() != null) {
                    int index = BreadcrumbItem.this.breadcrumbBar.get().itemsProperty().get().indexOf(BreadcrumbItem.this);
                    try {
                        BreadcrumbItem item = BreadcrumbItem.this.breadcrumbBar.get().itemsProperty().get().get(index + 1);
                        BreadcrumbItem.this.breadcrumbBar.get().removeItem(item);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
    
    protected BreadcrumbItem(BreadcrumbBar breadcrumbBar, String text, Image icon, Node content) {
        this();
        ((SimpleObjectProperty) this.breadcrumbBar).set(breadcrumbBar);
        this.content.set(content);
        this.icon.set(icon);
        this.text.set(text);
    }

    protected BreadcrumbItem(BreadcrumbBar breadcrumbBar, String text, SVGPath icon, Node content) {
        this();
        ((SimpleObjectProperty) this.breadcrumbBar).set(breadcrumbBar);
        this.content.set(content);
        this.svgIconProperty().set(icon);
        this.text.set(text);
    }
    
    /**
     * The property that references the breadcrumb bar the item belongs to.
     *
     * @return The breadcrumb bar the item belongs to.
     */
    public final ReadOnlyObjectProperty<BreadcrumbBar> breadcrumbBarProperty() {
        return this.breadcrumbBar;
    }

    /**
     * Get the breadcrumb bar the item belongs to.
     *
     * @return the breadcrumb bar the item belongs to.
     */
    public final BreadcrumbBar getBreadcrumbBar() {
        return this.breadcrumbBarProperty().get();
    }

    /**
     * The property referencing the content of the item.
     *
     * @return the content the item contain.
     */
    public final ObjectProperty<Node> contentProperty() {
        return this.content;
    }

    /**
     * Get the content of this item.
     *
     * @return the content of this item.
     */
    public final Node getContent() {
        return this.contentProperty().get();
    }

    /**
     * Set the content of this item.
     *
     * @param content the new content of this item.
     */
    public final void setContent(Node content) {
        this.contentProperty().set(content);
    }

    /**
     * The property referencing the text of this item.
     *
     * @return the text of this item.
     */
    public final StringProperty textProperty() {
        return this.text;
    }

    /**
     * Get the text of this item.
     *
     * @return the text of this item, <b>null</b> if no text is defined.
     */
    public final String getText() {
        return this.textProperty().get();
    }

    /**
     * Define the text of this item.
     *
     * @param text the new text of this item.
     */
    public final void setText(String text) {
        this.textProperty().set(text);
    }

    /**
     * The property referencing the icon of this item.
     *
     * @return the icon of this item.
     */
    public final ObjectProperty<Image> iconProperty() {
        return this.icon;
    }

    /**
     * Get the icon of this item.
     *
     * @return the icon of this item, <b>null</b> if no icon has been defined.
     */
    public final Image getIcon() {
        return this.iconProperty().get();
    }

    /**
     * Define the icon of this item. The SVG icon will be erased.
     *
     * @param icon the new icon of this item.
     */
    public final void setIcon(Image icon) {
        this.iconProperty().set(icon);
        this.svgIconProperty().set(null);
    }

    /**
     * The property referencing the SVG icon of this item.
     * @return the SVG icon of this item.
     */
    public final ObjectProperty<SVGPath> svgIconProperty() {
        return this.svgIcon;
    }
    
    /**
     * Get the SVG icon of this item.
     * @return the SVG icon of this item.
     */
    public final SVGPath getSvgIcon() {
        return this.svgIconProperty().get();
    }
    
    /**
     * Define the SVG icon for this item. The image icon will be erased.
     * @param svgIcon the new icon.
     */
    public final void setSvgIcom(SVGPath svgIcon) {
        this.svgIconProperty().set(svgIcon);
        this.iconProperty().set(null);
    }
    
    /**
     * This property indicating if this item is the first item in the bar it
     * belongs to.
     *
     * @return true if this item is the first item in the bar it belongs to,
     * false otherwise.
     */
    public final ReadOnlyBooleanProperty firstProperty() {
        return this.first;
    }

    /**
     * Indicates if this item is the first in the bar it belongs to.
     *
     * @return true if this item is the first in the bar it belongs to, false
     * otherwise.
     */
    public final boolean isFirst() {
        return this.firstProperty().get();
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/jfxtras/labs/internal/scene/control/" + getClass().getSimpleName() + ".css").toExternalForm();
    }
}

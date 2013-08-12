/*
 * Copyright (c) 2012, JFXtras
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of the <organization> nor the
 *         names of its contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.internal.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.layout.HBox;
import jfxtras.labs.scene.control.BreadcrumbBar;
import jfxtras.labs.scene.control.BreadcrumbItem;

/**
 *
 * @author Thierry Wasylczenko
 */
public class BreadcrumbBarSkin extends com.sun.javafx.scene.control.skin.BehaviorSkinBase<BreadcrumbBar, BehaviorBase<BreadcrumbBar>> {

    private HBox itemsBox = new HBox();

    public BreadcrumbBarSkin(BreadcrumbBar c) {
        super(c, new BehaviorBase<BreadcrumbBar>(c,new ArrayList<KeyBinding>()));

        c.itemsProperty().get().addListener(new ListChangeListener<BreadcrumbItem>() {
            @Override
            public void onChanged(Change<? extends BreadcrumbItem> c) {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        itemsBox.getChildren().removeAll(c.getRemoved());
                    } else if (c.wasAdded()) {
                        itemsBox.getChildren().addAll(c.getAddedSubList());
                    }
                }
                c.reset();
            }
        });

        itemsBox.getStyleClass().add("breadcrumbbar-ui");
        itemsBox.setStyle(c.getStyle());
        itemsBox.setSpacing(-10);
        itemsBox.setPrefHeight(c.getPrefHeight());
        itemsBox.setPrefWidth(c.getPrefWidth());

        for (BreadcrumbItem item : c.itemsProperty()) {
            itemsBox.getChildren().add(item);
        }

        getChildren().add(itemsBox);
    }
}

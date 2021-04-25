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
package jfxtras.labs.scene.control.scheduler.skin;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.CacheHint;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.scheduler.Scheduler;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class EventRegularBodyPane extends EventAbstractTrackedPane {
    public EventRegularBodyPane(Scheduler.Event event, LayoutHelp layoutHelp) {
        super(event, layoutHelp);

        // strings
        this.startAsString = layoutHelp.timeDateTimeFormatter.format(this.startDateTime);
        this.endAsString = layoutHelp.timeDateTimeFormatter.format(this.endDateTime);

        // add the duration as text
        Text lTimeText = new Text((firstPaneOfEvent ? startAsString : "") + "-" + (lastPaneOfEvent ? endAsString : ""));
        {
            lTimeText.getStyleClass().add("EventTimeLabel");
            lTimeText.setX(layoutHelp.paddingProperty.get() );
            lTimeText.setY(lTimeText.prefHeight(0));
            layoutHelp.clip(this, lTimeText, widthProperty().subtract( layoutHelp.paddingProperty ), heightProperty().add(0.0), true, 0.0);
            getChildren().add(lTimeText);
        }

        // add summary
        Text lSummaryText = new Text(event.getText());
        {
            lSummaryText.getStyleClass().add("EventLabel");
            lSummaryText.setX( layoutHelp.paddingProperty.get() );
            lSummaryText.setY( lTimeText.getY() + layoutHelp.textHeightProperty.get());
            lSummaryText.wrappingWidthProperty().bind(widthProperty().subtract( layoutHelp.paddingProperty.get() ));
            layoutHelp.clip(this, lSummaryText, widthProperty().add(0.0), heightProperty().subtract( layoutHelp.paddingProperty ), false, 0.0);
            getChildren().add(lSummaryText);
        }

        // add the menu header
        getChildren().add(eventMenu);

        // add the duration dragger
        layoutHelp.skinnable.allowResizeProperty().addListener(new WeakInvalidationListener(allowResizeInvalidationListener));
        setupDurationDragger();

        setCacheHint(CacheHint.SPEED);
        setCache(true);
        setCacheShape(true);
    }
    private String startAsString;
    private String endAsString;
    final private InvalidationListener allowResizeInvalidationListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable arg0) {
            setupDurationDragger();
        }
    };

    /**
     *
     */
    private void setupDurationDragger() {
        if (lastPaneOfEvent && layoutHelp.skinnable.getAllowResize()) {
            if (durationDragger == null) {
                durationDragger = new EventDurationDragger(this, event, layoutHelp);
            }
            getChildren().add(durationDragger);
        }
        else {
            getChildren().remove(durationDragger);
        }
    }
    private EventDurationDragger durationDragger = null;
}

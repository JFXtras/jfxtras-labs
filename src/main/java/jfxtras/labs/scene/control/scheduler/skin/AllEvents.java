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

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import jfxtras.labs.scene.control.scheduler.Scheduler.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class AllEvents {
    /**
     *
     */
    public AllEvents(ObservableList<Event> events) {
        this.events = events;

        events.addListener(new WeakListChangeListener<>(listChangeListener));

    }

    final private ObservableList<Event> events;
    final private ListChangeListener<Event> listChangeListener = new ListChangeListener<Event>() {
        @Override
        public void onChanged(javafx.collections.ListChangeListener.Change<? extends Event> changes) {
            fireOnChangeListener();
        }
    };

    /**
     * fires when something changes in the events
     */
    public void addOnChangeListener(Runnable runnable) {
        this.runnables.add(runnable);
    }

    public void removeOnChangeListener(Runnable runnable) {
        this.runnables.remove(runnable);
    }

    private List<Runnable> runnables = new ArrayList<>();

    private void fireOnChangeListener() {
        for (Runnable runnable : runnables) {
//            runnable.run();
            Platform.runLater(runnable);
        }
    }

    /**
     * @param resourceId
     * @return
     */
    public List<Event> collectRegularForResourceAndDates(long resourceId, LocalDate minDate, LocalDate maxDate) {
        List<Event> collectedEvents =  events.parallelStream().filter(c ->
                c.getResourceId().equals(resourceId)
    &&(  ( (c.getStartTime().toLocalDate().isAfter(minDate) || c.getStartTime().toLocalDate().isEqual(minDate))
                        && (c.getStartTime().toLocalDate().isBefore(maxDate) || c.getStartTime().toLocalDate().isEqual(maxDate))  )

    || ((c.getEndTime().toLocalDate().isBefore(maxDate) || c.getEndTime().toLocalDate().isEqual(maxDate))
                        && (c.getEndTime().toLocalDate().isAfter(minDate) || c.getEndTime().toLocalDate().isEqual(minDate))))   )
                .collect(Collectors.toList());
/*
        // scan all events and filter the ones for this resource
        for (Event lEvent : events) {
            if (lEvent.getResourceId().equals(resourceId)) {
                collectedEvents.add(lEvent);
            }

        }*/
        return collectedEvents;
    }
}

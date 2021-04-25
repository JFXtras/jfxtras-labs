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
package jfxtras.labs.scene.control.scheduler;

import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.labs.scene.control.scheduler.skin.SchedulerMonthSkin;
import jfxtras.labs.scene.control.scheduler.skin.SchedulerSkin;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class Scheduler extends Control {

    public Scheduler() {
        conctruct();
    }

    private void conctruct() {
        // Pref size
        setPrefSize(1000, 800);

        // Setup CSS
        this.getStyleClass().add(Scheduler.class.getSimpleName());

        constructEvents();
    }

    /**
     * Return the path to the CSS file so things are setup right
     */
    @Override
    public String getUserAgentStylesheet() {
        return Scheduler.class.getResource("/scheduler/" + Scheduler.class.getSimpleName() + ".css").toExternalForm();
    }

    /**
     *
     */
    @Override
    public Skin<?> createDefaultSkin() {
        return new SchedulerMonthSkin(this);
    }

    private void constructEvents() {
        // when Events are removed, they can't be selected anymore
        events.addListener(new ListChangeListener<Event>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Event> changes) {
                while (changes.next()) {
                    for (Event lAppointment : changes.getRemoved()) {
                        selectedevents.remove(lAppointment);
                    }
                }
            }
        });
    }

    /* Events */
    final private ObservableList<Event> events = javafx.collections.FXCollections.observableArrayList();

    public ObservableList<Event> events() {
        return events;
    }

    /* Resources */
    final private ObservableList<Resource> resources = javafx.collections.FXCollections.observableArrayList();

    public ObservableList<Resource> resources() {
        return resources;
    }


    /**
     * Locale: the locale is used to determine first-day-of-week, weekday labels, etc
     */
    public ObjectProperty<Locale> localeProperty() {
        return localeObjectProperty;
    }

    final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());

    public Locale getLocale() {
        return localeObjectProperty.getValue();
    }

    public void setLocale(Locale value) {
        localeObjectProperty.setValue(value);
    }

    public Scheduler withLocale(Locale value) {
        setLocale(value);
        return this;
    }

    /**
     * AllowDragging: allow events being dragged by the mouse
     */
    public SimpleBooleanProperty allowDraggingProperty() {
        return allowDraggingObjectProperty;
    }

    final private SimpleBooleanProperty allowDraggingObjectProperty = new SimpleBooleanProperty(this, "allowDragging", true);

    public boolean getAllowDragging() {
        return allowDraggingObjectProperty.getValue();
    }

    public void setAllowDragging(boolean value) {
        allowDraggingObjectProperty.setValue(value);
    }

    public Scheduler withAllowDragging(boolean value) {
        setAllowDragging(value);
        return this;
    }

    /**
     * AllowResize: allow events to be resized using the mouse
     */
    public SimpleBooleanProperty allowResizeProperty() {
        return allowResizeObjectProperty;
    }

    final private SimpleBooleanProperty allowResizeObjectProperty = new SimpleBooleanProperty(this, "allowResize", true);

    public boolean getAllowResize() {
        return allowResizeObjectProperty.getValue();
    }

    public void setAllowResize(boolean value) {
        allowResizeObjectProperty.setValue(value);
    }

    public Scheduler withAllowResize(boolean value) {
        setAllowResize(value);
        return this;
    }


    /**
     * DisplayedCalendar: this calendar denotes the timeframe being displayed.
     * If the agenda is in week skin, it will display the week containing this date. (Things like FirstDayOfWeek are taken into account.)
     * In month skin, the month containing this date.
     */
    private final ObjectProperty<Calendar> displayedCalendarObjectProperty = new SimpleObjectProperty<Calendar>(this, "displayedCalendar", Calendar.getInstance());

    /**
     * The skin will use this date and time to determine what to display.
     * Each skin determines what interval suites best; for example the week skin will find the week where this LocalDateTime falls in using the Locale to decide on what day a week starts, the day skin will render the date.
     * Possibly in the future there may be skins that render part of a day, that simply is not known, hence this is a LocalDateTime instead of a LocalDate.
     */
    public ObjectProperty<LocalDateTime> displayedLocalDateTime() {
        return displayedLocalDateTimeObjectProperty;
    }

    private final ObjectProperty<LocalDateTime> displayedLocalDateTimeObjectProperty = new SimpleObjectProperty<LocalDateTime>(this, "displayedLocalDateTime", LocalDateTime.now());

    public LocalDateTime getDisplayedLocalDateTime() {
        return displayedLocalDateTimeObjectProperty.getValue();
    }

    public void setDisplayedLocalDateTime(LocalDateTime value) {
        displayedLocalDateTimeObjectProperty.setValue(value);
    }

    public Scheduler withDisplayedLocalDateTime(LocalDateTime value) {
        setDisplayedLocalDateTime(value);
        return this;
    }

    /**
     * selectedevents: a list of selected events
     */
    public ObservableList<Event> selectedEvents() {
        return selectedevents;
    }

    final private ObservableList<Event> selectedevents = javafx.collections.FXCollections.observableArrayList();

    /**
     * localDateTimeRangeCallback:
     * Appointments should match:
     * - start date &gt;= range start
     * - end date &lt;= range end
     */
    public ObjectProperty<Callback<LocalDateTimeRange, Void>> localDateTimeRangeCallbackProperty() {
        return localDateTimeRangeCallbackObjectProperty;
    }

    final private ObjectProperty<Callback<LocalDateTimeRange, Void>> localDateTimeRangeCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDateTimeRange, Void>>(this, "localDateTimeRangeCallback", null);

    public Callback<LocalDateTimeRange, Void> getLocalDateTimeRangeCallback() {
        return this.localDateTimeRangeCallbackObjectProperty.getValue();
    }

    public void setLocalDateTimeRangeCallback(Callback<LocalDateTimeRange, Void> value) {
        this.localDateTimeRangeCallbackObjectProperty.setValue(value);
    }

    public Scheduler withLocalDateTimeRangeCallback(Callback<LocalDateTimeRange, Void> value) {
        setLocalDateTimeRangeCallback(value);
        return this;
    }

    /**
     * addEventCallback:
     * Since the Scheduler is not the owner of the appointments but only dictates an interface, it does not know how to create a new one.
     * So you need to implement this callback and create an event.
     * The calendars in the provided range specify the start and end times, they can be used to create the new event (they do not need to be cloned).
     * Null may be returned to indicate that no event was created.
     */
    public ObjectProperty<Callback<LocalDateTimeRange, Event>> newEventCallbackProperty() {
        return newEventCallbackObjectProperty;
    }

    final private ObjectProperty<Callback<LocalDateTimeRange, Event>> newEventCallbackObjectProperty = new SimpleObjectProperty<Callback<LocalDateTimeRange, Event>>(this, "newEventCallback", null);

    public Callback<LocalDateTimeRange, Event> getNewAppointmentCallback() {
        return this.newEventCallbackObjectProperty.getValue();
    }

    public void setNewEventCallback(Callback<LocalDateTimeRange, Event> value) {
        this.newEventCallbackObjectProperty.setValue(value);
    }

    public Scheduler withNewAppointmentCallback(Callback<LocalDateTimeRange, Event> value) {
        setNewEventCallback(value);
        return this;
    }

    /**
     * editEventCallback:
     * Scheduler has a default popup, but maybe you want to do something yourself.
     * If so, you need to set this callback method and open your own window.
     * Because Scheduler does not dictate a event/callback in the implementation of event, it has no way of being informed of changes on the event.
     * So when the custom edit is done, make sure that control gets updated, if this does not happen automatically through any of the existing listeners, then call refresh().
     */
    public ObjectProperty<Callback<Event, Void>> editAppointmentCallbackProperty() {
        return editEventCallbackObjectProperty;
    }

    final private ObjectProperty<Callback<Event, Void>> editEventCallbackObjectProperty = new SimpleObjectProperty<Callback<Event, Void>>(this, "editEventCallback", null);

    public Callback<Event, Void> getEditAppointmentCallback() {
        return this.editEventCallbackObjectProperty.getValue();
    }

    public void setEditEventCallback(Callback<Event, Void> value) {
        this.editEventCallbackObjectProperty.setValue(value);
    }

    public Scheduler withEditEventCallback(Callback<Event, Void> value) {
        setEditEventCallback(value);
        return this;
    }

    /**
     * eventChangedCallback:
     * When an event is changed by Scheduler (e.g. drag-n-drop to new time) change listeners will not fire.
     * To enable the client to process those changes this callback can be used.  Additionally, for a repeatable
     * event, this can be used to prompt the user if they want the change to occur to one, this-and-future
     * or all events in series.
     */
    public ObjectProperty<Callback<Event, Void>> appointmentChangedCallbackProperty() {
        return eventChangedCallbackObjectProperty;
    }

    final private ObjectProperty<Callback<Event, Void>> eventChangedCallbackObjectProperty = new SimpleObjectProperty<Callback<Event, Void>>(this, "eventChangedCallback", null);

    public Callback<Event, Void> getAppointmentChangedCallback() {
        return this.eventChangedCallbackObjectProperty.getValue();
    }

    public void setEventChangedCallback(Callback<Event, Void> value) {
        this.eventChangedCallbackObjectProperty.setValue(value);
    }

    public Scheduler withAppointmentChangedCallback(Callback<Event, Void> value) {
        setEventChangedCallback(value);
        return this;
    }

    /**
     * actionCallback:
     * This triggered when the action is called on an event, usually this is a double click
     */
    public ObjectProperty<Callback<Event, Void>> actionCallbackProperty() {
        return actionCallbackObjectProperty;
    }

    final private ObjectProperty<Callback<Event, Void>> actionCallbackObjectProperty = new SimpleObjectProperty<Callback<Event, Void>>(this, "actionCallback", null);

    public Callback<Event, Void> getActionCallback() {
        return this.actionCallbackObjectProperty.getValue();
    }

    public void setActionCallback(Callback<Event, Void> value) {
        this.actionCallbackObjectProperty.setValue(value);
    }

    public Scheduler withActionCallback(Callback<Event, Void> value) {
        setActionCallback(value);
        return this;
    }


    /**
     * A Datetime range, for callbacks
     */
    static public class LocalDateTimeRange {
        public LocalDateTimeRange(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }

        public LocalDateTime getStartLocalDateTime() {
            return start;
        }

        final LocalDateTime start;

        public LocalDateTime getEndLocalDateTime() {
            return end;
        }

        final LocalDateTime end;

        public String toString() {
            return super.toString() + " " + start + " to " + end;
        }
    }

    /**
     * Force the agenda to completely refresh itself
     */
    public void refresh() {
        ((SchedulerSkin) getSkin()).refresh();
    }


    public interface Event {

        Long getId();

        Long getResourceId();

        void setResourceId(long resourceId);

        Data getData();

        void setData(Data data);

        LocalDateTime getStartTime();

        void setStartTime(LocalDateTime t);

        LocalDateTime getEndTime();

        void setEndTime(LocalDateTime t);

        String getText();

        void setText(String s);
    }

    public static class Data {

    }

    public static class EventImpl implements Event {

        final private LongProperty idLongProperty = new SimpleLongProperty(this, "id");
        final private LongProperty resourceIdLongProperty = new SimpleLongProperty(this, "resourceId");
        private ObjectProperty<Data> dataObjectProperty = new SimpleObjectProperty<>(this, "data");
        final private ObjectProperty<LocalDateTime> startTimeObjectProperty = new SimpleObjectProperty<>(this, "startTime");
        final private ObjectProperty<LocalDateTime> endTimeObjectProperty = new SimpleObjectProperty<>(this, "endTime");
        final private StringProperty textStringProperty = new SimpleStringProperty(this, "text");

        /**
         * id accessors
         *
         * @return
         */
        public EventImpl withId(Long id) {
            this.idLongProperty.setValue(id);
            return this;
        }

        public LongProperty idLongProprerty() {
            return this.idLongProperty;
        }

        @Override
        public Long getId() {
            return this.idLongProperty.getValue();
        }

        /**
         * resource id accessors
         *
         * @return
         */
        public EventImpl withResourceId(Long id) {
            this.resourceIdLongProperty.setValue(id);
            return this;
        }

        public LongProperty resourceIdLongProprerty() {
            return this.resourceIdLongProperty;
        }

        @Override
        public Long getResourceId() {
            return this.resourceIdLongProperty.getValue();
        }

        @Override
        public void setResourceId(long resourceId) {
            this.resourceIdLongProperty.setValue(resourceId);
        }

        /**
         * data accsessors
         *
         * @return
         */
        ObjectProperty<Data> dataProperty() {
            return this.dataObjectProperty;
        }

        @Override
        public Data getData() {
            return this.dataProperty().getValue();
        }

        @Override
        public void setData(Data data) {
            this.dataObjectProperty.setValue(data);
        }

        /**
         * start time accessors
         *
         * @return
         */

        public ObjectProperty<LocalDateTime> startTimeProperty() {
            return this.startTimeObjectProperty;
        }

        @Override
        public LocalDateTime getStartTime() {
            return this.startTimeProperty().getValue();
        }

        @Override
        public void setStartTime(LocalDateTime t) {
            this.startTimeObjectProperty.setValue(t);
        }

        public EventImpl withStartTime(LocalDateTime t) {
            this.setStartTime(t);
            return this;
        }

        /**
         * end time accessors
         *
         * @return
         */
        public ObjectProperty<LocalDateTime> endTimeProperty() {
            return this.endTimeObjectProperty;
        }

        @Override
        public LocalDateTime getEndTime() {
            return this.endTimeProperty().getValue();
        }

        @Override
        public void setEndTime(LocalDateTime t) {
            this.endTimeObjectProperty.setValue(t);
        }

        public EventImpl withEndTime(LocalDateTime t) {
            this.setEndTime(t);
            return this;
        }

        /**
         * Text accessors
         *
         * @return
         */
        public StringProperty textStringPtoperty() {
            return this.textStringProperty;
        }

        @Override
        public String getText() {
            return this.textStringPtoperty().getValue();
        }

        @Override
        public void setText(String s) {
            this.textStringPtoperty().setValue(s);
        }

        public EventImpl withText(String text) {
            this.setText(text);
            return this;
        }
    }

    public interface Resource {
        Long getId();

        void setId(Long id);

        String getName();

        void setName(String name);

        String getStatus();

        void setStatus(String status);
    }

    public static class ResoureImpl implements Resource {

        private LongProperty idLongProperty = new SimpleLongProperty(this, "id");
        private StringProperty nameStringProperty = new SimpleStringProperty(this, "name");
        private StringProperty statusStringProperty = new SimpleStringProperty(this, "status");

        /**
         * Id accessors
         *
         * @param id
         * @return
         */
        public ResoureImpl withId(Long id) {
            this.idLongProperty().setValue(id);
            return this;
        }

        public LongProperty idLongProperty() {
            return this.idLongProperty;
        }

        @Override
        public Long getId() {
            return this.idLongProperty().getValue();
        }

        @Override
        public void setId(Long id) {
            this.idLongProperty().setValue(id);
        }

        /**
         * Name accessors
         *
         * @return
         */
        public ResoureImpl withName(String name) {
            this.nameStringPropety().setValue(name);
            return this;
        }

        public StringProperty nameStringPropety() {
            return this.nameStringProperty;
        }

        @Override
        public String getName() {
            return this.nameStringPropety().getValue();
        }

        @Override
        public void setName(String name) {
            this.nameStringPropety().setValue(name);
        }

        /**
         * Status accessors
         *
         * @return
         */
        public StringProperty statusStringProperty() {
            return this.statusStringProperty;
        }

        @Override
        public String getStatus() {
            return this.statusStringProperty().getValue();
        }

        @Override
        public void setStatus(String status) {
            this.statusStringProperty().setValue(status);
        }
    }


}

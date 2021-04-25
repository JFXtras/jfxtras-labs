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
package jfxtras.labs.scene.control;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by khachmakhov on 04.07.17.
 */
public class SchedulerDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Button buttonAddresources = new Button("Add resources");
        Button buttonAddEvents = new Button("Add events");
        ToolBar toolBar = new ToolBar(buttonAddresources, buttonAddEvents);

        Scheduler scheduler = new Scheduler().withLocale(Locale.forLanguageTag("ru"));

        VBox vBox = new VBox( toolBar, scheduler);

        StackPane root = new StackPane();
        root.getChildren().addAll(vBox);

        Scene scene = new Scene(root, 1000, 800);

        primaryStage.setTitle("JFXtras Scheduler");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();

//        ScenicView.show(scene);

        LocalDate now = LocalDate.now();

        buttonAddresources.setOnAction(e -> {
            scheduler.resources().clear();
            List<Scheduler.Resource> resources = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                Scheduler.ResoureImpl resource = new Scheduler.ResoureImpl().withId((long) i).withName("Conf room " + i);
                resources.add(resource);
            }
            scheduler.resources().addAll(resources);
        });

        buttonAddEvents.setOnAction( e -> {
            scheduler.events().clear();
            List<Scheduler.Event> events = new ArrayList<>();
            for (int i = 0; i < 200; i++) {

                Scheduler.EventImpl event1 = new Scheduler.EventImpl().withId(1L + i).withResourceId((long) i).withStartTime(now.with(DayOfWeek.MONDAY).atStartOfDay()).withEndTime(now.with(DayOfWeek.MONDAY).atStartOfDay().plusHours(24));
                Scheduler.EventImpl event2 = new Scheduler.EventImpl().withId(2L + i).withResourceId((long) i).withStartTime(now.with(DayOfWeek.TUESDAY).atStartOfDay()).withEndTime(now.with(DayOfWeek.TUESDAY).atStartOfDay().plusHours(24));
                Scheduler.EventImpl event3 = new Scheduler.EventImpl().withId(3L + i).withResourceId((long) i).withStartTime(now.with(DayOfWeek.WEDNESDAY).atStartOfDay()).withEndTime(now.with(DayOfWeek.WEDNESDAY).atStartOfDay().plusHours(24));
                Scheduler.EventImpl event4 = new Scheduler.EventImpl().withId(4L + i).withResourceId((long) i).withStartTime(now.with(DayOfWeek.THURSDAY).atStartOfDay()).withEndTime(now.with(DayOfWeek.THURSDAY).atStartOfDay().plusHours(24));
                Scheduler.EventImpl event5 = new Scheduler.EventImpl().withId(5L + i).withResourceId((long) i).withStartTime(now.with(DayOfWeek.FRIDAY).atStartOfDay()).withEndTime(now.with(DayOfWeek.FRIDAY).atStartOfDay().plusHours(24));
                Scheduler.EventImpl event6 = new Scheduler.EventImpl().withId(6L + i).withResourceId((long) i).withStartTime(now.with(DayOfWeek.SATURDAY).atStartOfDay()).withEndTime(now.with(DayOfWeek.SATURDAY).atStartOfDay().plusHours(24));
                Scheduler.EventImpl event7 = new Scheduler.EventImpl().withId(7L + i).withResourceId((long) i).withStartTime(now.with(DayOfWeek.SUNDAY).atStartOfDay().plusDays(1)).withEndTime(now.with(DayOfWeek.SUNDAY).atStartOfDay().plusDays(7));


                events.addAll(Arrays.asList(event1, event2, event3, event4, event5, event6, event7));
            }
            scheduler.events().addAll(events);
        });
    }
}

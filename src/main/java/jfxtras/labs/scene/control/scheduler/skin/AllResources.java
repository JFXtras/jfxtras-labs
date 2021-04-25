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
import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class AllResources {

    final private ObservableList<Scheduler.Resource> resources;
    final private ListChangeListener<Scheduler.Resource> listChangeListener= new ListChangeListener<Scheduler.Resource>() {

        @Override
        public void onChanged(Change<? extends Scheduler.Resource> c) {
            fireOnChangeListener();
        }
    };

    public AllResources(ObservableList<Scheduler.Resource> resources) {
        this.resources = resources;

        this.resources.addListener(new WeakListChangeListener<>(listChangeListener));
    }


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

    List<Scheduler.Resource> collectRegular() {
        return resources;
    }


}

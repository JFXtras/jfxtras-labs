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

import javafx.print.PrinterJob;
import javafx.scene.Node;
import jfxtras.labs.scene.control.scheduler.Scheduler;

import java.time.LocalDateTime;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public interface SchedulerSkin {
    /**
     * Complete refresh
     */
    void refresh();

    /**
     * Recreate the events
     */
    void setupEvents();

    /**
     * Recreate events only on participant ResourceBodyPanes
     */
    void setupParticularEvents(long oldResourceId, long newResourceId);

    /**
     *
     * @param x scene coordinate
     * @param y scene coordinate
     * @return a localDateTime equivalent of the click location, where a drop in the day section has nano seconds == 1, and a drop in a header (wholeday) section has nano seconds == 0
     */
    LocalDateTime convertClickInSceneToDateTime(double x, double y);

    long convertClickInSceneToResourceId(double x, double y);

    /**
     * Finds rendered node for event.  The node can be used as the owner for a popup.
     * or finding its x, y coordinates.
     *
     * @param event
     * @return rendered node that represents event
     */
    Node getNodeForPopup(Scheduler.Event event);

    public void print(PrinterJob job);
}

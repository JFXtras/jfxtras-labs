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

package jfxtras.labs.scene.control.gauge;

import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 12.04.12
 * Time: 14:48
 */
public class SplitFlapBuilder {
    private SplitFlap splitFlap;

    public final SplitFlapBuilder create() {
        splitFlap = new SplitFlap();
        return this;
    }

    public final SplitFlapBuilder textColor(final Color TEXT_COLOR) {
        splitFlap.setCharacterColor(TEXT_COLOR);
        return this;
    }

    public final SplitFlapBuilder color(final Color COLOR) {
        splitFlap.setColor(COLOR);
        return this;
    }

    public final SplitFlapBuilder character(final String CHARACTER) {
        splitFlap.setCharacter(CHARACTER);
        return this;
    }

    public final SplitFlapBuilder type(final SplitFlap.Type TYPE) {
        splitFlap.setType(TYPE);
        return this;
    }

    public final SplitFlapBuilder soundOn(final boolean SOUND_ON) {
        splitFlap.setSoundOn(SOUND_ON);
        return this;
    }

    public final SplitFlapBuilder sound(final SplitFlap.Sound SOUND) {
        splitFlap.setSound(SOUND);
        return this;
    }

    public final SplitFlapBuilder frameVisible(final boolean FRAME_VISIBLE) {
        splitFlap.setFrameVisible(FRAME_VISIBLE);
        return this;
    }

    public final SplitFlapBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
        splitFlap.setBackgroundVisible(BACKGROUND_VISIBLE);
        return this;
    }

    public final SplitFlapBuilder interactive(final boolean INTERACTIVE) {
        splitFlap.setInteractive(INTERACTIVE);
        return this;
    }

    public final SplitFlapBuilder flipTimeInMs(final long FLIP_TIME_IN_MS) {
        splitFlap.setFlipTimeInMs(FLIP_TIME_IN_MS);
        return this;
    }

    public final SplitFlap build() {
        return splitFlap != null ? splitFlap : new SplitFlap();
    }
}

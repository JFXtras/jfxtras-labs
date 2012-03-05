/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control.gauge;

import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 27.12.11
 * Time: 11:11
 */
public enum LedColor {
    RED("-fx-red;", Color.rgb(213, 0, 0)),
    GREEN("-fx-green;", Color.rgb(0, 148, 0)),
    BLUE("-fx-blue;", Color.rgb(0, 120, 220)),
    ORANGE("-fx-orange;", Color.rgb(248, 142, 0)),
    YELLOW("-fx-yellow;", Color.rgb(210, 204, 0)),
    CYAN("-fx-cyan;", Color.rgb(0, 159, 215)),
    MAGENTA("-fx-magenta;", Color.rgb(223, 42, 125)),
    LILA("-fx-lila;", Color.rgb( 71, 0,255)),
    WHITE("-fx-white;", Color.rgb(245, 245, 245)),
    GRAY("-fx-gray;", Color.rgb(102, 102, 102)),
    BLACK("-fx-black;", Color.rgb(15, 15, 15)),
    RAITH("-fx-raith;", Color.rgb(65, 143, 193)),
    GREEN_LCD("-fx-green-lcd;", Color.rgb(24, 220, 183)),
    JUG_GREEN("-fx-jug-green;", Color.rgb(90, 183, 0)),
    CUSTOM("-fx-custom;", Color.rgb(0, 195, 97));

    public final String CSS;
    public final Color GLOW_COLOR;

    LedColor(final String CSS_COLOR, final Color GLOW_COLOR) {
        this.CSS = "-fx-led: " + CSS_COLOR;
        this.GLOW_COLOR = GLOW_COLOR;
    }
}

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


/**
 * Color definitions for different LCD designs.
 * Some of the colors are taken from images of
 * real lcd's which really leads to a more realistic
 * look of the lcd display.
 * @author hansolo
 */
public enum LcdDesign {
    BEIGE("lcd-beige"),
    BLUE("lcd-blue"),
    ORANGE("lcd-orange"),
    RED("lcd-red"),
    YELLOW("lcd-yellow"),
    WHITE("lcd-white"),
    GRAY("lcd-gray"),
    BLACK("lcd-black"),
    GREEN("lcd-green"),
    GREEN_DARKGREEN("lcd-green-darkgreen"),
    BLUE2("lcd-blue2"),
    BLUE_BLACK("lcd-blue-black"),
    BLUE_DARKBLUE("lcd-blue-darkblue"),
    BLUE_LIGHTBLUE("lcd-blue-lightblue"),
    BLUE_GRAY("lcd-blue-gray"),
    STANDARD("lcd-standard"),
    STANDARD_GREEN("lcd-standard-green"),
    BLUE_BLUE("lcd-blue-blue"),
    RED_DARKRED("lcd-red-darkred"),
    DARKBLUE("lcd-darkblue"),
    LILA("lcd-lila"),
    BLACK_RED("lcd-black-red"),
    DARKGREEN("lcd-darkgreen"),
    AMBER("lcd-amber"),
    LIGHTBLUE("lcd-lightblue"),
    GREEN_BLACK("lcd-green-black"),
    YELLOW_BLACK("lcd-yellow-black"),
    BLACK_YELLOW("lcd-black-yellow"),
    LIGHTGREEN_BLACK("lcd-lightgreen-black"),
    DARKLILA("lcd-darklila"),
    DARKAMBER("lcd-darkamber"),
    BLUE_LIGHTBLUE2("lcd-blue-lightblue2"),
    GRAY_LILA("lcd-gray-lila"),
    SECTIONS("lcd-sections");

    public final String CSS;

    LcdDesign(final String CSS) {
        this.CSS = CSS;
    }
}

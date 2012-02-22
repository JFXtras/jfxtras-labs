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
    BEIGE("-fx-start: -fx-beige-start; -fx-frac1: -fx-beige-frac1; -fx-frac2: -fx-beige-frac2; -fx-frac3: -fx-beige-frac3; -fx-stop: -fx-beige-stop; -fx-lcd-text: -fx-beige-text; -fx-lcd-text-bg: -fx-beige-text-bg;"),
    BLUE("-fx-start: -fx-blue-start; -fx-frac1: -fx-blue-frac1; -fx-frac2: -fx-blue-frac2; -fx-frac3: -fx-blue-frac3; -fx-stop: -fx-blue-stop; -fx-lcd-text: -fx-blue-text; -fx-lcd-text-bg: -fx-blue-text-bg;"),
    ORANGE("-fx-start: -fx-orange-start; -fx-frac1: -fx-orange-frac1; -fx-frac2: -fx-orange-frac2; -fx-frac3: -fx-orange-frac3; -fx-stop: -fx-orange-stop; -fx-lcd-text: -fx-orange-text; -fx-lcd-text-bg: -fx-orange-text-bg;"),
    RED("-fx-start: -fx-red-start; -fx-frac1: -fx-red-frac1; -fx-frac2: -fx-red-frac2; -fx-frac3: -fx-red-frac3; -fx-stop: -fx-red-stop; -fx-lcd-text: -fx-red-text; -fx-lcd-text-bg: -fx-red-text-bg;"),
    YELLOW("-fx-start: -fx-yellow-start; -fx-frac1: -fx-yellow-frac1; -fx-frac2: -fx-yellow-frac2; -fx-frac3: -fx-yellow-frac3; -fx-stop: -fx-yellow-stop; -fx-lcd-text: -fx-yellow-text; -fx-lcd-text-bg: -fx-yellow-text-bg;"),
    WHITE("-fx-start: -fx-white-start; -fx-frac1: -fx-white-frac1; -fx-frac2: -fx-white-frac2; -fx-frac3: -fx-white-frac3; -fx-stop: -fx-white-stop; -fx-lcd-text: -fx-white-text; -fx-lcd-text-bg: -fx-white-text-bg;"),
    GRAY("-fx-start: -fx-gray-start; -fx-frac1: -fx-gray-frac1; -fx-frac2: -fx-gray-frac2; -fx-frac3: -fx-gray-frac3; -fx-stop: -fx-gray-stop; -fx-lcd-text: -fx-gray-text; -fx-lcd-text-bg: -fx-gray-text-bg;"),
    BLACK("-fx-start: -fx-black-start; -fx-frac1: -fx-black-frac1; -fx-frac2: -fx-black-frac2; -fx-frac3: -fx-black-frac3; -fx-stop: -fx-black-stop; -fx-lcd-text: -fx-black-text; -fx-lcd-text-bg: -fx-black-text-bg;"),
    GREEN("-fx-start: -fx-green-start; -fx-frac1: -fx-green-frac1; -fx-frac2: -fx-green-frac2; -fx-frac3: -fx-green-frac3; -fx-stop: -fx-green-stop; -fx-lcd-text: -fx-green-text; -fx-lcd-text-bg: -fx-green-text-bg;"),
    GREEN_DARKGREEN("-fx-start: -fx-greendarkgreen-start; -fx-frac1: -fx-greendarkgreen-frac1; -fx-frac2: -fx-greendarkgreen-frac2; -fx-frac3: -fx-greendarkgreen-frac3; -fx-stop: -fx-greendarkgreen-stop; -fx-lcd-text: -fx-greendarkgreen-text; -fx-lcd-text-bg: -fx-greendarkgreen-text-bg;"),
    BLUE2("-fx-start: -fx-blue2-start; -fx-frac1: -fx-blue2-frac1; -fx-frac2: -fx-blue2-frac2; -fx-frac3: -fx-blue2-frac3; -fx-stop: -fx-blue2-stop; -fx-lcd-text: -fx-blue2-text; -fx-lcd-text-bg: -fx-blue2-text-bg;"),
    BLUE_BLACK("-fx-start: -fx-blueblack-start; -fx-frac1: -fx-blueblack-frac1; -fx-frac2: -fx-blueblack-frac2; -fx-frac3: -fx-blueblack-frac3; -fx-stop: -fx-blueblack-stop; -fx-lcd-text: -fx-blueblack-text; -fx-lcd-text-bg: -fx-blueblack-text-bg;"),
    BLUE_DARKBLUE("-fx-start: -fx-bluedarkblue-start; -fx-frac1: -fx-bluedarkblue-frac1; -fx-frac2: -fx-bluedarkblue-frac2; -fx-frac3: -fx-bluedarkblue-frac3; -fx-stop: -fx-bluedarkblue-stop; -fx-lcd-text: -fx-bluedarkblue-text; -fx-lcd-text-bg: -fx-bluedarkblue-text-bg;"),
    BLUE_LIGHTBLUE("-fx-start: -fx-bluelightblue-start; -fx-frac1: -fx-bluelightblue-frac1; -fx-frac2: -fx-bluelightblue-frac2; -fx-frac3: -fx-bluelightblue-frac3; -fx-stop: -fx-bluelightblue-stop; -fx-lcd-text: -fx-bluelightblue-text; -fx-lcd-text-bg: -fx-bluelightblue-text-bg;"),
    BLUE_GRAY("-fx-start: -fx-bluegray-start; -fx-frac1: -fx-bluegray-frac1; -fx-frac2: -fx-bluegray-frac2; -fx-frac3: -fx-bluegray-frac3; -fx-stop: -fx-bluegray-stop; -fx-lcd-text: -fx-bluegray-text; -fx-lcd-text-bg: -fx-bluegray-text-bg;"),
    STANDARD("-fx-start: -fx-standard-start; -fx-frac1: -fx-standard-frac1; -fx-frac2: -fx-standard-frac2; -fx-frac3: -fx-standard-frac3; -fx-stop: -fx-standard-stop; -fx-lcd-text: -fx-standard-text; -fx-lcd-text-bg: -fx-standard-text-bg;"),
    STANDARD_GREEN("-fx-start: -fx-standardgreen-start; -fx-frac1: -fx-standardgreen-frac1; -fx-frac2: -fx-standardgreen-frac2; -fx-frac3: -fx-standardgreen-frac3; -fx-stop: -fx-standardgreen-stop; -fx-lcd-text: -fx-standardgreen-text; -fx-lcd-text-bg: -fx-standardgreen-text-bg;"),
    BLUE_BLUE("-fx-start: -fx-blueblue-start; -fx-frac1: -fx-blueblue-frac1; -fx-frac2: -fx-blueblue-frac2; -fx-frac3: -fx-blueblue-frac3; -fx-stop: -fx-blueblue-stop; -fx-lcd-text: -fx-blueblue-text; -fx-lcd-text-bg: -fx-blueblue-text-bg;"),
    RED_DARKRED("-fx-start: -fx-reddarkred-start; -fx-frac1: -fx-reddarkred-frac1; -fx-frac2: -fx-reddarkred-frac2; -fx-frac3: -fx-reddarkred-frac3; -fx-stop: -fx-reddarkred-stop; -fx-lcd-text: -fx-reddarkred-text; -fx-lcd-text-bg: -fx-reddarkred-text-bg;"),
    DARKBLUE("-fx-start: -fx-darkblue-start; -fx-frac1: -fx-darkblue-frac1; -fx-frac2: -fx-darkblue-frac2; -fx-frac3: -fx-darkblue-frac3; -fx-stop: -fx-darkblue-stop; -fx-lcd-text: -fx-darkblue-text; -fx-lcd-text-bg: -fx-darkblue-text-bg;"),
    LILA("-fx-start: -fx-lila-start; -fx-frac1: -fx-lila-frac1; -fx-frac2: -fx-lila-frac2; -fx-frac3: -fx-lila-frac3; -fx-stop: -fx-lila-stop; -fx-lcd-text: -fx-lila-text; -fx-lcd-text-bg: -fx-lila-text-bg;"),
    BLACK_RED("-fx-start: -fx-blackred-start; -fx-frac1: -fx-blackred-frac1; -fx-frac2: -fx-blackred-frac2; -fx-frac3: -fx-blackred-frac3; -fx-stop: -fx-blackred-stop; -fx-lcd-text: -fx-blackred-text; -fx-lcd-text-bg: -fx-blackred-text-bg;"),
    DARKGREEN("-fx-start: -fx-darkgreen-start; -fx-frac1: -fx-darkgreen-frac1; -fx-frac2: -fx-darkgreen-frac2; -fx-frac3: -fx-darkgreen-frac3; -fx-stop: -fx-darkgreen-stop; -fx-lcd-text: -fx-darkgreen-text; -fx-lcd-text-bg: -fx-darkgreen-text-bg;"),
    AMBER("-fx-start: -fx-amber-start; -fx-frac1: -fx-amber-frac1; -fx-frac2: -fx-amber-frac2; -fx-frac3: -fx-amber-frac3; -fx-stop: -fx-amber-stop; -fx-lcd-text: -fx-amber-text; -fx-lcd-text-bg: -fx-amber-text-bg;"),
    LIGHTBLUE("-fx-start: -fx-lightblue-start; -fx-frac1: -fx-lightblue-frac1; -fx-frac2: -fx-lightblue-frac2; -fx-frac3: -fx-lightblue-frac3; -fx-stop: -fx-lightblue-stop; -fx-lcd-text: -fx-lightblue-text; -fx-lcd-text-bg: -fx-lightblue-text-bg;"),
    GREEN_BLACK("-fx-start: -fx-greenblack-start; -fx-frac1: -fx-greenblack-frac1; -fx-frac2: -fx-greenblack-frac2; -fx-frac3: -fx-greenblack-frac3; -fx-stop: -fx-greenblack-stop; -fx-lcd-text: -fx-greenblack-text; -fx-lcd-text-bg: -fx-greenblack-text-bg;"),
    YELLOW_BLACK("-fx-start: -fx-yellowblack-start; -fx-frac1: -fx-yellowblack-frac1; -fx-frac2: -fx-yellowblack-frac2; -fx-frac3: -fx-yellowblack-frac3; -fx-stop: -fx-yellowblack-stop; -fx-lcd-text: -fx-yellowblack-text; -fx-lcd-text-bg: -fx-yellowblack-text-bg;"),
    BLACK_YELLOW("-fx-start: -fx-blackyellow-start; -fx-frac1: -fx-blackyellow-frac1; -fx-frac2: -fx-blackyellow-frac2; -fx-frac3: -fx-blackyellow-frac3; -fx-stop: -fx-blackyellow-stop; -fx-lcd-text: -fx-blackyellow-text; -fx-lcd-text-bg: -fx-blackyellow-text-bg;"),
    LIGHTGREEN_BLACK("-fx-start: -fx-lightgreenblack-start; -fx-frac1: -fx-lightgreenblack-frac1; -fx-frac2: -fx-lightgreenblack-frac2; -fx-frac3: -fx-lightgreenblack-frac3; -fx-stop: -fx-lightgreenblack-stop; -fx-lcd-text: -fx-lightgreenblack-text; -fx-lcd-text-bg: -fx-lightgreenblack-text-bg;"),
    DARKLILA("-fx-start: -fx-darklila-start; -fx-frac1: -fx-darklila-frac1; -fx-frac2: -fx-darklila-frac2; -fx-frac3: -fx-darklila-frac3; -fx-stop: -fx-darklila-stop; -fx-lcd-text: -fx-darklila-text; -fx-lcd-text-bg: -fx-darklila-text-bg;"),
    DARKAMBER("-fx-start: -fx-darkamber-start; -fx-frac1: -fx-darkamber-frac1; -fx-frac2: -fx-darkamber-frac2; -fx-frac3: -fx-darkamber-frac3; -fx-stop: -fx-darkamber-stop; -fx-lcd-text: -fx-darkamber-text; -fx-lcd-text-bg: -fx-darkamber-text-bg;"),
    BLUE_LIGHTBLUE2("-fx-start: -fx-bluelightblue2-start; -fx-frac1: -fx-bluelightblue2-frac1; -fx-frac2: -fx-bluelightblue2-frac2; -fx-frac3: -fx-bluelightblue2-frac3; -fx-stop: -fx-bluelightblue2-stop; -fx-lcd-text: -fx-bluelightblue2-text; -fx-lcd-text-bg: -fx-bluelightblue2-text-bg;"),
    GRAY_LILA("-fx-start: -fx-graylila-start; -fx-frac1: -fx-graylila-frac1; -fx-frac2: -fx-graylila-frac2; -fx-frac3: -fx-graylila-frac3; -fx-stop: -fx-graylila-stop; -fx-lcd-text: -fx-graylila-text; -fx-lcd-text-bg: -fx-graylila-text-bg;"),
    SECTIONS("-fx-start: -fx-sections-start; -fx-frac1: -fx-sections-frac1; -fx-frac2: -fx-sections-frac2; -fx-frac3: -fx-sections-frac3; -fx-stop: -fx-sections-stop; -fx-lcd-text: -fx-sections-text; -fx-lcd-text-bg: -fx-sections-text-bg;");

    public final String CSS;

    LcdDesign(final String CSS) {
        this.CSS = CSS;
    }
}

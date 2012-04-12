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

/**
 * Created by
 * User: hansolo
 * Date: 09.03.12
 * Time: 16:12
 */
public class TrafficLightBuilder {
    private TrafficLight trafficLight;

    public final TrafficLightBuilder create() {
        trafficLight = new TrafficLight();
        return this;
    }

    public final TrafficLightBuilder redOn(final boolean RED_ON) {
        trafficLight.setRedOn(RED_ON);
        return this;
    }

    public final TrafficLightBuilder redBlinking(final boolean RED_BLINKING) {
        trafficLight.setRedBlinking(RED_BLINKING);
        return this;
    }

    public final TrafficLightBuilder yellowOn(final boolean YELLOW_ON) {
        trafficLight.setYellowOn(YELLOW_ON);
        return this;
    }

    public final TrafficLightBuilder yellowBlinking(final boolean YELLOW_BLINKING) {
        trafficLight.setYellowBlinking(YELLOW_BLINKING);
        return this;
    }

    public final TrafficLightBuilder greenOn(final boolean GREEN_ON) {
        trafficLight.setGreenOn(GREEN_ON);
        return this;
    }

    public final TrafficLightBuilder greenBlinking(final boolean GREEN_BLINKING) {
        trafficLight.setGreenBlinking(GREEN_BLINKING);
        return this;
    }

    public final TrafficLightBuilder darkBackground(final boolean DARK_BACKGROUND) {
        trafficLight.setDarkBackground(DARK_BACKGROUND);
        return this;
    }

    public final TrafficLight build() {
        return trafficLight != null ? trafficLight : new TrafficLight();
    }
}

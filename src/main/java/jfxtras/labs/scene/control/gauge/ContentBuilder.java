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

import java.util.HashMap;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.util.Builder;

/**
 *
 * @author Jose Pereda Llamas <jperedadnr>
 * Created on : 09-jul-2012, 23:04:22
 */
public class ContentBuilder implements Builder<Content> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    /**
     * To define a matrixPanel content, please indicate:
     * <ul><li><i>type</i>: IMAGE, <i>bmpName</i>: file name (and path)</li>
     * <li><i>type</i>: TEXT, <i>txtContent</i>: Text string, <i>font</i>, font <i>fontGap</i></li></ul>
     * <p>and:
     * <i>area</i>, <i>origin</i>, <i>color</i>, <i>effect</i> (and <i>lapse</i>), <i>postEffect</i> (and <i>pause</i>)
     * <p>To rotate contents in the same area use <i>order</i>
     * 
     * @return 
     */
    public static final ContentBuilder create(){
        return new ContentBuilder();
    }
    
    /**
     * 
     * @param MATRIX_COLOR Choose the matrix LED's color: 
     * <ul><li><i>MatrixColor.RED</i>: there are three tones of RED: 85,170,255; BLUE and GREEN are filtered to 0.</li>
     * <li><i>MatrixColor.GREEN</i>: there are three tones of GREEN: 85,170,255; RED and BLUE are filtered to 0.</li>
     * <li><i>MatrixColor.BLUE</i>: there are three tones of BLUE: 85,170,255; RED and GREEN are filtered to 0.</li>
     * <li><i>MatrixColor.RGB</i>: there are three tones of RED, BLUE AND GREEN: 85,170,255 for each one of them.</li></ul>
     * @return 
     */
    public final ContentBuilder color(final Content.MatrixColor MATRIX_COLOR) {
        properties.put("color", new SimpleObjectProperty<Content.MatrixColor>(MATRIX_COLOR));
        return this;
    }
    
    /**
     * 
     * @param TYPE Choose the type of the content:
     * <ul><li><i>Type.IMAGE</i>: The content is a BMP Image</li>
     * <li><i>Type.TEXT</i>: The content is a line of Text</li></ul>
     * @return 
     */
    public final ContentBuilder type(final Content.Type TYPE) {
        properties.put("type", new SimpleObjectProperty<Content.Type>(TYPE));
        return this;
    }
    
    /**
     * @see area(int, int, int, int)
     * @see origin(int, int)
     * @param ORIGIN Insert a Point2D with the (int) X and Y coordinates, relative to the <i>area</i> 
     * in which the content is displayed, measured from the left-top of it, to the right-bottom. 
     * @return 
     */
    public final ContentBuilder origin(final Point2D ORIGIN) {
        properties.put("origin", new SimpleObjectProperty<Point2D>(ORIGIN));
        return this;
    }
    
    /**
     * @see area(int, int, int, int)
     * @see origin(Point2D)
     * @param ORIGIN_X Insert the X coordinate, relative to the <i>area</i> 
     * in which the content is displayed, measured from the left of it, to the right. 
     * @param ORIGIN_Y Insert the Y coordinate, relative to the <i>area</i> 
     * in which the content is displayed, measured from the top of it, to the bottom. 
     * @return 
     */
    public final ContentBuilder origin(final int ORIGIN_X, final int ORIGIN_Y) {
        properties.put("origin", new SimpleObjectProperty<Point2D>(new Point2D(ORIGIN_X,ORIGIN_Y)));
        return this;
    }
    
    /**
     * @see area(int, int, int, int)
     * @param AREA Insert a Rectangle with X,Y as the left-top coordinates, and W,H as the width and height
     * of the window in which the content will be displayed. 
     * <p>This area should be inside the bounds of the matrixPanel and should not overlap with other areas.
     * <p>The very same area could be used to display two different contents, check <i>order(RotationOrder)</i>.
     * @return 
     */
    public final ContentBuilder area(final Rectangle AREA) {
        final Rectangle RECT=new Rectangle(AREA.getX(),AREA.getY(),AREA.getX()+AREA.getWidth(),AREA.getY()+AREA.getHeight());
        properties.put("area", new SimpleObjectProperty<Rectangle>(RECT));
        return this;
    }
    
    /**
     * @see area(Rectangle)
     * @param ORIGIN_X Insert the X left coordinate of the window in which the content is displayed. 
     * @param ORIGIN_Y Insert the Y top coordinate of the window in which the content is displayed. 
     * @param END_X Insert the X rigth coordinate of the window in which the content is displayed. 
     * @param END_Y Insert the Y bottom coordinate of the window in which the content is displayed. 
     * <p>This window should be inside the bounds of the matrixPanel and should not overlap with other areas.
     * <p>The very same area could be used to display two different contents, check <i>order(RotationOrder)</i>.
     * @return 
     */
    public final ContentBuilder area(final int ORIGIN_X, final int ORIGIN_Y, final int END_X, final int END_Y) {
        properties.put("area", new SimpleObjectProperty<Rectangle>(new Rectangle(ORIGIN_X,ORIGIN_Y,END_X,END_Y)));
        return this;
    }
    
    /**
     * @see type(Content.Type Content.Type.IMAGE)
     * @param BMP_NAME Two options for a valid name of a BMP image, with or without ".bmp" extension: 
     * <ul><li>It should be already in the source (relative to matrixPanel package)</li>
     * <li>or a full valid path should be added to the name in case it has be loaded from an external resource</li></ul>
     * @return 
     */
    public final ContentBuilder bmpName(final String BMP_NAME) {
        properties.put("bmpName", new SimpleStringProperty(BMP_NAME));
        return this;
    }
    
    /**
     * @see type(Content.Type Content.Type.TEXT)
     * @param TXT_CONTENT Insert the string of text to be displayed, it will be showed in one line. In case
     * it is too long, a SCROLL effect is recommended. 
     * @return 
     */
    public final ContentBuilder txtContent(final String TXT_CONTENT) {
        properties.put("txtContent", new SimpleStringProperty(TXT_CONTENT));
        return this;
    }
    /**
     * @see type(Content.Type Content.Type.TEXT)
     * @param FONT Select the font for the text to be displayed. Several proportional dotted fonts are available, 
     * all of them named with the <i>Width</i> and <i>Height</i> used for each character. 
     * Check MatrixPanel to insert missing characters to the list.
     * @return 
     */
    public final ContentBuilder font(final Content.MatrixFont FONT) {
        properties.put("matrixFont", new SimpleObjectProperty<Content.MatrixFont>(FONT));
        return this;
    }
    
    /**
     * @see type(Content.Type Content.Type.TEXT)
     * @param FONT_GAP Select the gap between the characters: 
     * <ul><li><i>Gap.NULL</i>: No space will be used between consecutive characters</li>
     * <li><i>Gap.SIMPLE</i>: One LED will be used as gap between consecutive characters</li>
     * <li><i>Gap.DOUBLE</i>: Two LEDs will be used as gap between consecutive characters</li>
     * @return 
     */public final ContentBuilder fontGap(final Content.Gap FONT_GAP) {
        properties.put("fontGap", new SimpleObjectProperty<Content.Gap>(FONT_GAP));
        return this;
    }
    
     /**
      * @see type(Content.Type Content.Type.TEXT)
      * @see effect(Content.EFFECT)
      * @param TXT_ALIGN Select how to align the string of text
      * <ul><li>Align.LEFT: Align the text to the left of the area</li>
      * <li>Align.CENTER: Align the text to the center of the area</li>
      * <li>Align.LEFT: Align the text to the right of the area</li></ul>
      * <p>In case of long string of text, to display the whole string select a Scroll effect 
      * opposite to the selected align. 
      * @return 
      */
    public final ContentBuilder align(final Content.Align TXT_ALIGN) {
        properties.put("align", new SimpleObjectProperty<Content.Align>(TXT_ALIGN));
        return this;
    }
    
    /**
     * @see align(Content.Align)
     * @see lapse(Integer)
     * @see postEffect(Content.PostEffect)
     * @param EFFECT Select the Effect to display animated content
     * <ul><li><i>Effect.NONE</i>: The content will be displayed in its area without animation effect</li>
     * <li><i>Effect.SCROLL_RIGHT</i>, <i>Effect.SCROLL_LEFT</i>, <i>Effect.SCROLL_UP</i>, <i>Effect.SCROLL_DOWN</i>: 
     * The content will be scrolled from the outside to its final position in its area, from the right to the left, 
     * the left to the right, the bottom to the top, or the top to the bottom, respectively. They must be coordinated
     * with the propor align of the content</li>
     * <li><i>Effect.BLINK</i>, <i>Effect.BLINK_10</i>, <i>Effect.BLINK_4</i>: The content is displayed in its area
     * and start blinking, indefinetly, ten times and stop, four times and stop, respectively</li></ul>
     * <p>The animation effect is repeted every <i>lapse</i> milliseconds.
     * <p>To repeat the effect, choose a <i>postEffect</i> action.
     * @return 
     */
    public final ContentBuilder effect(final Content.Effect EFFECT) {
        properties.put("effect", new SimpleObjectProperty<Content.Effect>(EFFECT));
        return this;
    }
    
    /**
     * @see effect(Content.Effect)
     * @see pause(Integer)
     * @param POST_EFFECT Select the action after the animation effect has finished: 
     * <ul><li><i>PostEffect.STOP</i>: the content will remain in its position.</li>
     * <li><i>PostEffect.REPEAT</i>: the content will be animated again.</li>
     * <li><i>PostEffect.PAUSE</i>: the content will remain in its position for a specificied time (see <i>pause</i>,
     * then will be animated again.</li></ul>
     * @return 
     */
    public final ContentBuilder postEffect(final Content.PostEffect POST_EFFECT) {
        properties.put("postEffect", new SimpleObjectProperty<Content.PostEffect>(POST_EFFECT));
        return this;
    }
    
    /** 
     * @see postEffect(Content.Effect Content.Effect.PAUSE)
     * @see order(Content.RotationOrder)
     * @param PAUSE Insert the time in milliseconds that the content will be showed in its final position, before
     * the selected effect starts again, or the content is replaced by other in the same area (see <i>order</i>).
     * @return 
     */
    public final ContentBuilder pause(final Integer PAUSE) {
        properties.put("pause", new SimpleIntegerProperty(PAUSE));
        return this;
    }
    
    /**
     * @see effect(Content.Effect)
     * @param TIME_LAPSE in terms of milliseconds, is the time lapse to perform the animation effect, 
     * movement or blink of the whole content.
     * @return 
     */
    public final ContentBuilder lapse(final Integer TIME_LAPSE) {
        properties.put("lapse", new SimpleIntegerProperty(TIME_LAPSE));
        return this;
    }
    
    /**
     * @see clear(Boolean)
     * @param ORDER Select <i>RotationOrder.Single</i> for a unique content in its area. 
     * <p>In case two different contents should be displayed alternately in the very same area, select:
     * <ul><li><i>RotationOrder.FIRST</i>: for the first content to be displayed, with its own effect and postEffect 
     * (other than PostEffect.STOP)</li>
     * <li><i>RotationOrder.SECOND</i>: for the second content to be displayed, with its own effect and postEffect 
     * (other than PostEffect.STOP)</li>
     * <p>To erase the area before displaying the next content, select clear<i>(true)</i> to prevent mixing contents.
     * @return 
     */
    public final ContentBuilder order(final Content.RotationOrder ORDER) {
        properties.put("order", new SimpleObjectProperty<Content.RotationOrder>(ORDER));
        return this;
    }
    
    /**
     * @see order(Content.RotationOrder)
     * @param CLEAR In case two different contents should be displayed alternately in the very same area, select
     * if the area should be erased before displaying the next content.
     * @return 
     */
    public final ContentBuilder clear(final Boolean CLEAR) {
        properties.put("clear", new SimpleBooleanProperty(CLEAR));
        return this;
    }
    
    @Override
    public Content build() {
        final Content CONTROL = new Content();
        for (String key : properties.keySet()) {
            if ("color".equals(key)) {
                CONTROL.setColor(((ObjectProperty<Content.MatrixColor>) properties.get(key)).get());
            } else if ("type".equals(key)) {
                CONTROL.setType(((ObjectProperty<Content.Type>) properties.get(key)).get());
            } else if ("origin".equals(key)) {
                CONTROL.setOrigin(((ObjectProperty<Point2D>) properties.get(key)).get());
            } else if ("area".equals(key)) {
                CONTROL.setArea(((ObjectProperty<Rectangle>) properties.get(key)).get());
            } else if ("bmpName".equals(key)) {
                CONTROL.setBmpName(((StringProperty) properties.get(key)).get());
            } else if ("txtContent".equals(key)) {
                CONTROL.setTxtContent(((StringProperty) properties.get(key)).get());
            } else if ("matrixFont".equals(key)) {
                CONTROL.setMatrixFont(((ObjectProperty<Content.MatrixFont>) properties.get(key)).get());
            } else if ("fontGap".equals(key)) {
                CONTROL.setFontGap(((ObjectProperty<Content.Gap>) properties.get(key)).get());
            } else if ("align".equals(key)) {
                CONTROL.setTxtAlign(((ObjectProperty<Content.Align>) properties.get(key)).get());
            } else if ("effect".equals(key)) {
                CONTROL.setEffect(((ObjectProperty<Content.Effect>) properties.get(key)).get());
            } else if ("postEffect".equals(key)) {
                CONTROL.setPostEffect(((ObjectProperty<Content.PostEffect>) properties.get(key)).get());
            } else if ("pause".equals(key)) {
                CONTROL.setPause(((IntegerProperty) properties.get(key)).get());
            } else if ("lapse".equals(key)) {
                CONTROL.setLapse(((IntegerProperty) properties.get(key)).get());
            } else if ("order".equals(key)) {
                CONTROL.setOrder(((ObjectProperty<Content.RotationOrder>) properties.get(key)).get());
            } else if ("clear".equals(key)) {
                CONTROL.setClear(((BooleanProperty) properties.get(key)).get());
            } 
        }
        return CONTROL;
    }

}

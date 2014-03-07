/**
 * Content.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control.gauge;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Jose Pereda Llamas &lt;jperedadnr&gt;
 * Created on : 23-jun-2012, 12:59:27
 */
public class Content {
    
    public static enum MatrixColor {
        RED,
        GREEN,
        BLUE,
        YELLOW,
        RGB
    }
    
    public static enum Type {
        IMAGE,
        TEXT;
    }
    public static enum Effect {
        NONE,
        SCROLL_LEFT,
        SCROLL_RIGHT,
        SCROLL_UP,
        SCROLL_DOWN,
        MIRROR,
        BLINK,
        BLINK_4,
        BLINK_10,
        SPRAY        
    }
    public static enum PostEffect {
        STOP,
        PAUSE,
        REPEAT
    }
    public static enum MatrixFont {
        NONE,
        FF_5x7,
        FF_7x7,
        FF_7x9,
        FF_8x14,
        FF_10x14,
        FF_8x16,
        FF_10x16,
        FF_15x32
    }
    public static enum Gap{
        NULL(0),
        SIMPLE(1),
        DOUBLE(2);

        private final int gapWidth;

        Gap(final int gapWidth){
            this.gapWidth=gapWidth;
        }
        public int getGapWidth() { return this.gapWidth; }
    }
    public static enum Align{
        LEFT,
        CENTER,
        RIGHT
    }
    
    public static enum RotationOrder{
        SINGLE,
        FIRST,
        SECOND
    }

    private ObjectProperty<MatrixColor>     color;
    private ObjectProperty<Type>            type;
    private ObjectProperty<Point2D>         origin;
    private ObjectProperty<Rectangle>       area;
    private ObjectProperty<Effect>          effect;
    private ObjectProperty<PostEffect>      postEffect;
    private IntegerProperty                 pause;
    private IntegerProperty                 lapse;
    private ObjectProperty<RotationOrder>   order;
    private BooleanProperty                 clear;
    
    // IMAGE
    private StringProperty                  bmpName;

    // TEXT
    private StringProperty                  txtContent;
    private ObjectProperty<MatrixFont>      matrixFont;
    private ObjectProperty<Gap>             fontGap;
    private ObjectProperty<Align>           txtAlign;
    
    public Content(){
        this(MatrixColor.RED, Type.TEXT, new Point2D(0,0), new Rectangle(0,0,20000,20000), "", MatrixFont.FF_5x7,
                Gap.SIMPLE, Align.LEFT, Effect.NONE, PostEffect.STOP, 0, 10, RotationOrder.SINGLE, false);
    }

    
    public Content(final MatrixColor COLOR, final Type TYPE, final Point2D ORIGIN, final Rectangle RECT, 
                   final String BMP_TXT_CONTENT, final MatrixFont VOID_MATRIX_FONT_NAME, final Gap VOID_GAP, final Align TXT_ALIGN,
                   final Effect EFFECT, final PostEffect POSTEFFECT, final int PAUSE, final int SPEED, 
                   final RotationOrder ORDER, final boolean CLEAR){
        
        color    = new SimpleObjectProperty<>(COLOR);
        type    = new SimpleObjectProperty<>(TYPE);
        origin  = new SimpleObjectProperty<>(ORIGIN);
        area    = new SimpleObjectProperty<>(RECT);
        if(type.get().equals(Type.IMAGE)) {
            bmpName = new SimpleStringProperty(BMP_TXT_CONTENT);
            txtContent = new SimpleStringProperty("");

        }
        else if(type.get().equals(Type.TEXT)) {
            bmpName = new SimpleStringProperty("");
            txtContent = new SimpleStringProperty(BMP_TXT_CONTENT);
        }
        matrixFont = new SimpleObjectProperty<>(VOID_MATRIX_FONT_NAME);
        fontGap    = new SimpleObjectProperty<>(VOID_GAP);
        txtAlign   = new SimpleObjectProperty<>(TXT_ALIGN);
        effect     = new SimpleObjectProperty<>(EFFECT);
        postEffect = new SimpleObjectProperty<>(POSTEFFECT);
        pause      = new SimpleIntegerProperty(PAUSE);
        lapse      = new SimpleIntegerProperty(SPEED);
        order      = new SimpleObjectProperty<>(ORDER);
        clear      = new SimpleBooleanProperty(CLEAR);
        
    }

    public final MatrixColor getColor(){
        return color.get();
    }

    public void setColor(MatrixColor COLOR){
        color.set(COLOR);
    }

    public final ObjectProperty<MatrixColor> colorProperty(){
        return color;
    }

    public final Type getType(){
        return type.get();
    }

    public void setType(Type TYPE){
        type.set(TYPE);
    }

    public final ObjectProperty<Type> typeProperty(){
        return type;
    }

    public final Point2D getOrigin(){
        return origin.get();
    }

    public void setOrigin(Point2D ORIGIN){
        origin.set(ORIGIN);
    }

    public final ObjectProperty<Point2D> originProperty(){
        return origin;
    }

    public final Rectangle getArea(){
        return area.get();
    }

    public void setArea(Rectangle AREA){
        area.set(AREA);
    }

    public final ObjectProperty<Rectangle> areaProperty(){
        return area;
    }

    public final String getBmpName(){
        return bmpName.get();
    }

    public void setBmpName(String BMP_NAME){
        bmpName.set(BMP_NAME);
    }

    public final StringProperty bmpNameProperty(){
        return bmpName;
    }

    public final String getTxtContent(){
        return txtContent.get();
    }

    public void setTxtContent(String TXT_CONTENT){
        txtContent.set(TXT_CONTENT);
    }

    public final StringProperty txtContentProperty(){
        return txtContent;
    }

    public final MatrixFont getMatrixFont(){
        return matrixFont.get();
    }

    public void setMatrixFont(MatrixFont MATRIX_FONT){
        matrixFont.set(MATRIX_FONT);
    }

    public final ObjectProperty<MatrixFont> matrixFontProperty(){
        return matrixFont;
    }

    public final Gap getFontGap(){
        return fontGap.get();
    }

    public void setFontGap(Gap FONT_GAP){
        fontGap.set(FONT_GAP);
    }

    public final ObjectProperty<Gap> fontGapProperty(){
        return fontGap;
    }

    public final Align getTxtAlign(){
        return txtAlign.get();
    }

    public void setTxtAlign(Align TXT_ALIGN){
        txtAlign.set(TXT_ALIGN);
    }

    public final ObjectProperty<Align> txtAlignProperty(){
        return txtAlign;
    }

    public final Effect getEffect(){
        return effect.get();
    }

    public void setEffect(Effect EFFECT){
        effect.set(EFFECT);
    }

    public final ObjectProperty<Effect> effectProperty(){
        return effect;
    }

    public final PostEffect getPostEffect(){
        return postEffect.get();
    }

    public void setPostEffect(PostEffect POSTEFFECT){
        postEffect.set(POSTEFFECT);
    }

    public final ObjectProperty<PostEffect> postEffectProperty(){
        return postEffect;
    }

    public final int getLapse(){
        return lapse.get();
    }

    public void setLapse(int LAPSE){
        lapse.set(LAPSE);
    }

    public final IntegerProperty lapseProperty(){
        return lapse;
    }

    public final int getPause(){
        return pause.get();
    }

    public void setPause(int PAUSE){
        pause.set(PAUSE);
    }

    public final IntegerProperty pauseProperty(){
        return pause;
    }

    public final RotationOrder getOrder(){
        return order.get();
    }

    public void setOrder(RotationOrder ORDER){
        order.set(ORDER);
    }

    public final ObjectProperty<RotationOrder> orderProperty(){
        return order;
    }
    
    public final boolean getClear(){
        return clear.get();
    }

    public void setClear(boolean CLEAR){
        clear.set(CLEAR);
    }

    public final BooleanProperty clearProperty(){
        return clear;
    }

    public boolean equals(final Content CONTENT) {
        return (CONTENT.getType().equals(getType()) &&
                CONTENT.getColor().equals(getColor()) &&
                CONTENT.getMatrixFont().equals(getMatrixFont()) && 
                CONTENT.getOrigin().equals(getOrigin()) &&
                CONTENT.getArea().getBoundsInLocal().equals(getArea().getBoundsInLocal()) &&
                CONTENT.getTxtContent().equals(getTxtContent()) &&
                CONTENT.getBmpName().equals(getBmpName()) &&
                CONTENT.getEffect().equals(getEffect()) &&
                CONTENT.getPostEffect().equals(getPostEffect()) &&
                CONTENT.getPause()==getPause() &&
                CONTENT.getLapse()==getLapse() && 
                CONTENT.getOrder().equals(getOrder()) &&
                CONTENT.getClear()==getClear());
    }
}

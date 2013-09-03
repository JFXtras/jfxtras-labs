/**
 * LicenseRenderer.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
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

package jfxtras.labs.map.render;

import java.awt.Desktop;
import java.awt.Point;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import jfxtras.labs.map.Coordinate;
import jfxtras.labs.map.MapTilesourceable;
import jfxtras.labs.map.tile.TileSource;

import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;

import static jfxtras.labs.map.CoordinatesConverter.*;

/**
 * This class displays copy right informations on the map
 *
 * @author Mario Schroeder
 *
 */
public class LicenseRenderer implements Renderable {

    private static final String STYLE_RIGHTS = "copyRight";

    public static final String STYLE_TERMS = "termsOfUse";

    @Override
    public void render(MapTilesourceable viewer) {

        TileSource tileSource = viewer.getTileSource();
        Group tilesGroup = viewer.getTilesGroup();

        int x = viewer.getMapX();
        int y = viewer.getMapY();
        int width = viewer.getMapWidth();
        int height = viewer.getMapHeight();
        int yText = height - 8;
        
        
        Coordinate topLeft = toCoordinate(new Point(x,y), viewer);
        Coordinate bottomRight = toCoordinate(new Point(width, height), viewer);
        String attrTxt = tileSource.getAttributionText(viewer.zoomProperty().get(), topLeft, bottomRight);

        // Draw attribution text
        if (attrTxt != null) {
            Text attrText = createLicenseText(attrTxt);
            attrText.setId(STYLE_RIGHTS);

            double strwidth = attrText.getBoundsInParent().getWidth();
            attrText.setLayoutX(width - strwidth);
            attrText.setLayoutY(yText);
            tilesGroup.getChildren().add(attrText);
        }

        Image attrImage = tileSource.getAttributionImage();
        // Draw attribution logo
        if (attrImage != null) {
            ImageView attImgView = new ImageView(attrImage);
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.BLACK);
            attImgView.setEffect(ds);
            attImgView.setLayoutX(8);
            attImgView.setLayoutY(yText - (attrImage.getHeight() + 15));
            tilesGroup.getChildren().add(attImgView);
        }

        String termsOfUse = tileSource.getTermsOfUseURL();
        if (termsOfUse != null) {
            Text termsText = createLicenseText("Terms Of Use");
            termsText.setUnderline(true);
            termsText.setId(STYLE_TERMS);

            termsText.setLayoutX(8);
            termsText.setLayoutY(yText);

            termsText.setOnMouseEntered(new MouseEnteredAdapter(termsText));
            termsText.setOnMouseClicked(new MouseClickedAdapter(termsOfUse));

            tilesGroup.getChildren().add(termsText);
        }

    }

    private Text createLicenseText(String attrTxt) {
        Text txt = new Text(attrTxt);
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.BLACK);
        txt.setEffect(ds);
        txt.setFontSmoothingType(FontSmoothingType.LCD);
        txt.setFill(Color.WHITE);
        return txt;
    }

    private class MouseClickedAdapter implements EventHandler<MouseEvent> {

        private String url;

        MouseClickedAdapter(String termsOfUse) {
            this.url = termsOfUse;
        }

        @Override
        public void handle(MouseEvent me) {
            try {
                URI u = new URI(url);
                Desktop.getDesktop().browse(u);
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    private class MouseEnteredAdapter implements EventHandler<MouseEvent> {

        private Text text;

        MouseEnteredAdapter(Text text) {
            this.text = text;
        }

        @Override
        public void handle(MouseEvent t) {
            text.setCursor(Cursor.HAND);
        }
    }
}

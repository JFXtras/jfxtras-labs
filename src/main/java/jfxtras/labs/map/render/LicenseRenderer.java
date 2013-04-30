package jfxtras.labs.map.render;

import java.awt.Desktop;
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
import jfxtras.labs.map.MapControlable;
import jfxtras.labs.map.tile.TileSource;

import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;

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
    public void render(MapControlable mapController) {

        TileSource tileSource = mapController.getTileSource();
        Group tilesGroup = mapController.getTilesGroup();

        int x = mapController.getMapX();
        int y = mapController.getMapY();
        int width = mapController.getMapWidth();
        int height = mapController.getMapHeight();
        int yText = height - 8;
        
        Coordinate topLeft = mapController.getCoordinate(x, y);
        Coordinate bottomRight = mapController.getCoordinate(width, height);
        String attrTxt = tileSource.getAttributionText(mapController.getZoom(), topLeft, bottomRight);

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

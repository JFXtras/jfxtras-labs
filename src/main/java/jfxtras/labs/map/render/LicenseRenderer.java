package jfxtras.labs.map.render;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import jfxtras.labs.map.Coordinate;
import jfxtras.labs.map.MapControlable;
import jfxtras.labs.map.tile.TileSource;

import javafx.scene.paint.Color;
import javafx.scene.text.FontSmoothingType;

/**
 * This class displays copy right informations on the map
 * @author Mario Schroeder
 *
 */
public class LicenseRenderer implements Renderable{

	@Override
	public void render(MapControlable mapController) {
		
		TileSource tileSource = mapController.getTileSource();
		Group tilesGroup = mapController.getTilesGroup();
		
        int x = mapController.getMapX();
        int y = mapController.getMapY();
        int width = mapController.getMapWidth();
        int height = mapController.getMapHeight();
        
		Coordinate topLeft = mapController.getCoordinate(x, y);
        Coordinate bottomRight = mapController.getCoordinate(width, height);
        String attrTxt = tileSource.getAttributionText(mapController.getZoom(), topLeft, bottomRight);
        // Draw attribution text
        if (attrTxt != null) {
            Text attrText = createLicenseText(attrTxt);
            tilesGroup.getChildren().add(attrText);

            double strwidth = attrText.getBoundsInParent().getWidth();
            attrText.setLayoutX(width - strwidth);
            attrText.setLayoutY(height - 8);
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
            attImgView.setLayoutY(height - (1.8 * attrImage.getHeight()));
            tilesGroup.getChildren().add(attImgView);
        }
        
        String termsOfUse = tileSource.getTermsOfUseURL();
        if(termsOfUse != null){
        	Text termsText = createLicenseText(termsOfUse);
        	tilesGroup.getChildren().add(termsText);
        	
        	termsText.setLayoutX(8);
        	termsText.setLayoutY(height - 8);
        }
		
	}

	private Text createLicenseText(String attrTxt) {
		Text txt = new Text(attrTxt);
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.BLACK);
		txt.setEffect(ds);
		txt.setFontSmoothingType(FontSmoothingType.LCD);
		txt.setFill(Color.BLUE);
		return txt;
	}
	
}

package jfxtras.labs.map.render;

import java.awt.Point;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jfxtras.labs.map.MapControlable;
import jfxtras.labs.map.tile.Tile;
import jfxtras.labs.map.tile.TileCacheable;
import jfxtras.labs.map.tile.TileSource;

/**
 * Rendered for map tiles.
 * @author Mario Schroeder
 */
public class TileRenderer implements Renderable {
    
    private static final Point[] movePoints = {new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1)};
    
    private static final int START = 0;
    
    private final TileCacheable tileCache;
    
    private boolean monoChrome;
    
    private boolean tileGridVisible;
    
    public TileRenderer(TileCacheable tileCache){
        this.tileCache = tileCache;
    }
    

    @Override
    public void render(MapControlable mapController) {

        TileSource tileSource = mapController.getTileSource();

        int iMove;
        int tilesize = tileSource.getTileSize();
        Point center = mapController.getCenter();

        int diff_left = (center.x % tilesize);
        int diff_right = tilesize - diff_left;
        int diff_top = (center.y % tilesize);
        int diff_bottom = tilesize - diff_top;

        boolean start_left = diff_left < diff_right;
        boolean start_top = diff_top < diff_bottom;

        if (start_top) {
            if (start_left) {
                iMove = 2;
            } else {
                iMove = 3;
            }
        } else {
            if (start_left) {
                iMove = 1;
            } else {
                iMove = START;
            }
        }

        renderTiles(mapController, tilesize, diff_left, diff_top, iMove);

    }

    private void renderTiles(MapControlable mapController, int tilesize, int off_x, int off_y, int iMove) {

        int zoom = mapController.getZoom();
        
        Group tilesGroup = mapController.getTilesGroup();
        tilesGroup.getChildren().clear();
        
        int x_max = (int) mapController.getMapWidth();
        int y_max = (int) mapController.getMapHeight();

        int x_min = -tilesize, y_min = -tilesize;

        int posx = (x_max / 2) - off_x;
        int posy = (y_max / 2) - off_y;

        Point center = mapController.getCenter();
        int tilex = (center.x / tilesize);
        int tiley = (center.y / tilesize);

        // paint the tiles in a spiral, starting from center of the map
        boolean painted = true;
        int x = START;
        while (painted) {
            painted = false;
            for (int i = START; i < 4; i++) {
                if (i % 2 == START) {
                    x++;
                }
                for (int j = START; j < x; j++) {
                    if (x_min <= posx && posx <= x_max && y_min <= posy && posy <= y_max) {
                        // tile is visible
                        Tile tile = tileCache.getTile(tilex, tiley, zoom);
                        if (tile != null) {
                            painted = true;

                            tile.getImageView().translateXProperty().set(posx);
                            tile.getImageView().translateYProperty().set(posy);

                            if (monoChrome) {
                                setMonochromeEffect(tile);
                            }

                            if (tileGridVisible) {
                                tilesGroup.getChildren().add(createGrid(posx, posy, tilesize));
                            }

                            tilesGroup.getChildren().add(tile.getImageView());
                        }
                    }
                    Point p = movePoints[iMove];
                    posx += p.x * tilesize;
                    posy += p.y * tilesize;
                    tilex += p.x;
                    tiley += p.y;
                }
                iMove = (iMove + 1) % movePoints.length;
            }
        }
    }

    private void setMonochromeEffect(Tile tile) {

        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1);
        monochrome.setContrast(-0.3);
        monochrome.setBrightness(-0.3);
        tile.getImageView().setEffect(monochrome);
    }

    protected Path createGrid(int posx, int posy, int tilesize) {

        Path path = new Path();
        path.getElements().add(new MoveTo(posx, posy));
        path.getElements().add(new LineTo(posx + tilesize, posy));
        path.getElements().add(new LineTo(posx + tilesize, posy + tilesize));
        path.getElements().add(new LineTo(posx, posy + tilesize));
        path.getElements().add(new LineTo(posx, posy));
        path.setStrokeWidth(1);
        path.setStroke(Color.BLACK);

        return path;
    }
    
    public void setTileSource(TileSource tileSource){
        tileCache.setTileSource(tileSource);
    }
    
    public TileSource getTileSource(){
        return tileCache.getTileSource();
    }

    public void setMonoChrome(boolean monoChrome) {
        this.monoChrome = monoChrome;
    }

    public void setTileGridVisible(boolean tileGridVisible) {
        this.tileGridVisible = tileGridVisible;
    }
    
}

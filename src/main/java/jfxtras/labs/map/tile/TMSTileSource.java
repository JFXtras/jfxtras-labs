package jfxtras.labs.map.tile;


public class TMSTileSource extends AbstractTileSource {
    private int maxZoom;

    public TMSTileSource(String name, String url, int maxZoom) {
        super(name, url);
        this.maxZoom = maxZoom;
    }

    @Override
    public int getMaxZoom() {
        return (maxZoom == 0) ? super.getMaxZoom() : maxZoom;
    }

}

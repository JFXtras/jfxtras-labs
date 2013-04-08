package jfxtras.labs.map.tile;


public class TemplatedTMSTileSource extends AbstractTileSource {
    private int maxZoom;
    
    public TemplatedTMSTileSource(String name, String url, int maxZoom) {
        super(name, url);
        this.maxZoom = maxZoom;
    }

    @Override
    public String getTileUrl(int zoom, int tilex, int tiley) {
        return this.baseUrl
        .replaceAll("\\{zoom\\}", Integer.toString(zoom))
        .replaceAll("\\{x\\}", Integer.toString(tilex))
        .replaceAll("\\{y\\}", Integer.toString(tiley));
        
    }

    @Override
    public int getMaxZoom() {
        return (maxZoom == 0) ? super.getMaxZoom() : maxZoom;
    }

}
package jfxtras.labs.map.tile;

/**
 * This class builds the urls to an osm tiles server.
 * 
 * @author Mario Schroeder
 */
public class AlternateOsmTileUrlBuilder implements TileUrlBuildable{
    
    private final static String[] SERVERS = {"a", "b", "c"};
    
    private int serverNumber = 0;
    
    /**
     * This method builds the url.
     * The url is alternated each time the method is called.<br/>
     * Sample:<br/>
     * <ol>
     * <li>http://a.tile.opencyclemap.org/cycle</li>
     * <li>http://b.tile.opencyclemap.org/cycle</li>
     * </ol>
     *
     * The parameter must contain a '%s', because it will be replaced.
     * 
     * @param baseUrl the url which will alternated. Example: http://%s.tile.opencyclemap.org/cycle
     * @return the url as string
     */
    @Override
    public String build(String baseUrl){
        String url = String.format(baseUrl, new Object[]{SERVERS[serverNumber]});
        serverNumber = (serverNumber + 1) % SERVERS.length;
        return url;
    }
    
}

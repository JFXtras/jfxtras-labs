package jfxtras.labs.map;

/**
 * A enum for api keys, only for test purposes.
 * @author msc
 */
public enum ApiKeys {
    
    Bing("Arzdiw4nlOJzRwOz__qailc8NiR31Tt51dN2D7cm57NrnceZnCpgOkmJhNpGoppU");
    
    private String value;

    private ApiKeys(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
}

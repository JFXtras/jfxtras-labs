package jfxtras.labs.scene.layout.responsivepane;

enum Unit {
	INCH("in");
	
	private Unit(String suffix){
        this.suffix = suffix;
    }
    final String suffix;
    
	public double toInches(double value) {
		return value;
	}	    
}
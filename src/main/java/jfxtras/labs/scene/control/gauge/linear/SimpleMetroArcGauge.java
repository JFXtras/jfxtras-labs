package jfxtras.labs.scene.control.gauge.linear;

import javafx.collections.ObservableList;
import javafx.scene.control.Skin;
import jfxtras.labs.internal.scene.control.skin.gauge.linear.SimpleMetroArcGaugeSkin;
import jfxtras.scene.control.ListSpinner;

/**
 * = SimpleMetroArcGauge
 * This gauge is a simple flat possibly colorful (Microsoft Metro) arc shaped gauge.
 * The needle ranges from about 7 o'clock (min) via 12 o'clock to 5 o'clock (max).
 * 
 * It supports segments:
 * - Segment colors can be set using CSS classes matching "segment1", the numeric part is the index of the segment in the segments list.
 * - Another option is to specify an segment ID, which can be used to style the segment in CSS.
 * - The SimpleMetroArcGauge.css per default supports segment classes segment0 - segment9.
 * - The CSS also contains a number of color schemes, like "colorscheme-green-to-red-10" (for 10 segments) which can be activated by assigning the color scheme class to the gauge.
 * - If no segments are specified a single segment will be drawn.
 * - If segments are specified, the user is responsible for not leaving any gaps.
 * 
 * TBEERNOT: styleable properties
 * 
 * This is a blatant but approved copy of Gerrit Grunwald's Enzo SimpleGauge (https://bitbucket.org/hansolo/enzo/src)
 */
public class SimpleMetroArcGauge extends AbstractLinearGauge<SimpleMetroArcGauge> {
	
	// ==================================================================================================================
	// AbstractLinearGauge

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet() {
		return ListSpinner.class.getResource("/jfxtras/labs/internal/scene/control/gauge/linear/" + SimpleMetroArcGauge.class.getSimpleName() + ".css").toExternalForm();
	}

	@Override public Skin<?> createDefaultSkin() {
		return new SimpleMetroArcGaugeSkin(this); 
	}

	// ==================================================================================================================
	// PROPERTIES

	/** segments:
	 * If you do not provide any segments, a default segment will be drawn.
	 * But if you provide at least one segment, you are responsible for making sure the segments cover the range of the gauge. 
	 */
	public ObservableList<Segment> segments() { return segments; }
	final private ObservableList<Segment> segments =  javafx.collections.FXCollections.observableArrayList();
}

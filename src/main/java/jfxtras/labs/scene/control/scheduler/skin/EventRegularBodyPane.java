package jfxtras.labs.scene.control.scheduler.skin;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.CacheHint;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.scheduler.Scheduler;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class EventRegularBodyPane extends EventAbstractTrackedPane{
    public EventRegularBodyPane(Scheduler.Event event, LayoutHelp layoutHelp) {
        super(event, layoutHelp);

        // strings
        this.startAsString = layoutHelp.timeDateTimeFormatter.format(this.startDateTime);
        this.endAsString = layoutHelp.timeDateTimeFormatter.format(this.endDateTime);

        // add the duration as text
        Text lTimeText = new Text((firstPaneOfEvent ? startAsString : "") + "-" + (lastPaneOfEvent ? endAsString : ""));
        {
            lTimeText.getStyleClass().add("EventTimeLabel");
            lTimeText.setX(layoutHelp.paddingProperty.get() );
            lTimeText.setY(lTimeText.prefHeight(0));
            layoutHelp.clip(this, lTimeText, widthProperty().subtract( layoutHelp.paddingProperty ), heightProperty().add(0.0), true, 0.0);
            getChildren().add(lTimeText);
        }

        // add summary
        Text lSummaryText = new Text(event.getText());
        {
            lSummaryText.getStyleClass().add("EventLabel");
            lSummaryText.setX( layoutHelp.paddingProperty.get() );
            lSummaryText.setY( lTimeText.getY() + layoutHelp.textHeightProperty.get());
            lSummaryText.wrappingWidthProperty().bind(widthProperty().subtract( layoutHelp.paddingProperty.get() ));
            layoutHelp.clip(this, lSummaryText, widthProperty().add(0.0), heightProperty().subtract( layoutHelp.paddingProperty ), false, 0.0);
            getChildren().add(lSummaryText);
        }

        // add the menu header
        getChildren().add(eventMenu);

        // add the duration dragger
        layoutHelp.skinnable.allowResizeProperty().addListener(new WeakInvalidationListener(allowResizeInvalidationListener));
        setupDurationDragger();

        setCacheHint(CacheHint.SPEED);
        setCache(true);
        setCacheShape(true);
    }
    private String startAsString;
    private String endAsString;
    final private InvalidationListener allowResizeInvalidationListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable arg0) {
            setupDurationDragger();
        }
    };

    /**
     *
     */
    private void setupDurationDragger() {
        if (lastPaneOfEvent && layoutHelp.skinnable.getAllowResize()) {
            if (durationDragger == null) {
                durationDragger = new EventDurationDragger(this, event, layoutHelp);
            }
            getChildren().add(durationDragger);
        }
        else {
            getChildren().remove(durationDragger);
        }
    }
    private EventDurationDragger durationDragger = null;
}

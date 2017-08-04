package jfxtras.labs.scene.control.scheduler.skin;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Callback;
import jfxtras.labs.scene.control.scheduler.Scheduler;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.util.NodeUtil;

import java.time.LocalDateTime;

/**
 * @author Tom Eugelink
 * @author Islam Khachmakhov
 */
public class EventMenu extends Rectangle {

    /**
     * @param pane
     * @param event
     * @param layoutHelp
     */
    EventMenu(Pane pane, Scheduler.Event event, LayoutHelp layoutHelp) {
        this.pane = pane;
        this.event = event;
        this.layoutHelp = layoutHelp;

        // layout
        setX(NodeUtil.snapXY(layoutHelp.paddingProperty.get()));
        setY(NodeUtil.snapXY(layoutHelp.paddingProperty.get()));
        setWidth(6);
        setHeight(3);

        // style
        getStyleClass().add("MenuIcon");

        // mouse
        layoutHelp.setupMouseOverAsBusy(this);
        setupMouseClick();
    }

    final Pane pane;
    final Scheduler.Event event;
    final LayoutHelp layoutHelp;

    /**
     *
     */
    private void setupMouseClick() {
        setOnMousePressed((mouseEvent) -> {
            mouseEvent.consume();
        });
        setOnMouseReleased((mouseEvent) -> {
            mouseEvent.consume();
        });
        setOnMouseClicked((mouseEvent) -> {
            mouseEvent.consume();
            showMenu(mouseEvent);
        });
    }

    /**
     * @param mouseEvent
     */
    void showMenu(MouseEvent mouseEvent) {
        // has the client done his own popup?
        Callback<Scheduler.Event, Void> lEditCallback = layoutHelp.skinnable.getEditAppointmentCallback();
        if (lEditCallback != null) {
            lEditCallback.call(event);
            return;
        }

        // only if not already showing
        if (popup != null && popup.isShowing()) {
            return;
        }

        // create popup
        popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.setOnHidden((windowEvent) -> {
            layoutHelp.skin.setupParticularEvents(event.getResourceId(), event.getResourceId());
        });

        // popup contents
        BorderPane lBorderPane = new BorderPane() {
            // As of 1.8.0_40 CSS files are added in the scope of a control, the popup does not fall under the control, so the stylesheet must be reapplied
            // When JFxtras is based on 1.8.0_40+: @Override
            public String getUserAgentStylesheet() {
                return layoutHelp.skinnable.getUserAgentStylesheet();
            }
        };
        lBorderPane.getStyleClass().add(layoutHelp.skinnable.getClass().getSimpleName() + "Popup");
        popup.getContent().add(lBorderPane);

        // close icon
        lBorderPane.setRight(createCloseIcon());

        // initial layout
        VBox lVBox = new VBox(layoutHelp.paddingProperty.get());
        lBorderPane.setCenter(lVBox);

        // start and end
        lVBox.getChildren().add(new Text("Time:"));
        lVBox.getChildren().add(createStartTextField());
        lVBox.getChildren().add(createEndTextField());

        // summary
        lVBox.getChildren().add(new Text("Summary:"));
        lVBox.getChildren().add(createSummaryTextField());

        // actions
        lVBox.getChildren().add(new Text("Actions:"));
        lVBox.getChildren().add(createActions());

        // show it just below the menu icon
        popup.show(pane, NodeUtil.screenX(pane), NodeUtil.screenY(pane));
    }

    private Popup popup;

    /**
     * @return
     */
    private ImageViewButton createCloseIcon() {
        closeIconImageView = new ImageViewButton();
        closeIconImageView.getStyleClass().add("close-icon");
        closeIconImageView.setPickOnBounds(true);
        closeIconImageView.setOnMouseClicked((mouseEvent2) -> {
            popup.hide();
        });
        return closeIconImageView;
    }

    private ImageViewButton closeIconImageView = null;

    /**
     * @return
     */
    private LocalDateTimeTextField createStartTextField() {
        startTextField = new LocalDateTimeTextField();
        startTextField.setLocale(layoutHelp.skinnable.getLocale());
        startTextField.setLocalDateTime(event.getStartTime());

        // event handling
        startTextField.localDateTimeProperty().addListener((observable, oldValue, newValue) -> {

            // remember
            LocalDateTime lOldStart = event.getStartTime();

            // set
            event.setStartTime(newValue);

            // update end date
/*            if (event.getEndTime() != null) {

                // enddate = start + duration
                long lDurationInNano = event.getEndTime().getNano() - lOldStart.getNano();
                LocalDateTime lEndLocalDateTime = event.getStartTime().plusNanos(lDurationInNano);
                event.setEndTime(lEndLocalDateTime);

                // update field
                endTextField.setLocalDateTime(event.getEndTime());
            }*/

            // refresh is done upon popup close
            layoutHelp.callEventChangedCallback(event);
        });

        return startTextField;
    }

    private LocalDateTimeTextField startTextField = null;

    /**
     * @return
     */
    private LocalDateTimeTextField createEndTextField() {
        endTextField = new LocalDateTimeTextField(event.getEndTime());
        endTextField.setLocale(layoutHelp.skinnable.getLocale());
        endTextField.setLocalDateTime(event.getEndTime());

        endTextField.localDateTimeProperty().addListener((observable, oldValue, newValue) -> {
            event.setEndTime(newValue);
            // refresh is done upon popup close
            layoutHelp.callEventChangedCallback(event);
        });

        return endTextField;
    }

    private LocalDateTimeTextField endTextField = null;

    /**
     * @return
     */
    private TextField createSummaryTextField() {
        summaryTextField = new TextField();
        summaryTextField.setText(event.getText());
        summaryTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            event.setText(newValue);
            // refresh is done upon popup close
            layoutHelp.callEventChangedCallback(event);
        });
        return summaryTextField;
    }

    private TextField summaryTextField = null;

    /**
     * @return
     */
    private HBox createActions() {
        HBox lHBox = new HBox();

        // delete
        {
            deleteImageViewButton = createActionButton("delete-icon", "Delete");
            deleteImageViewButton.setOnMouseClicked((mouseEvent) -> {
                popup.hide();
                layoutHelp.skinnable.events().remove(event);
                // refresh is done via the collection events
            });
            lHBox.getChildren().add(deleteImageViewButton);
        }

        // action
        if (layoutHelp.skinnable.getActionCallback() != null) {
            actionImageViewButton = createActionButton("action-icon", "Action");
            actionImageViewButton.setOnMouseClicked((mouseEvent) -> {
                popup.hide();
                layoutHelp.skinnable.getActionCallback().call(event);
                // any refresh is done via the collection events
            });
            lHBox.getChildren().add(actionImageViewButton);
        }

        return lHBox;
    }

    private ImageViewButton deleteImageViewButton = null;
    private ImageViewButton actionImageViewButton = null;

    private ImageViewButton createActionButton(String styleClass, String tooltipText) {
        ImageViewButton lImageViewButton = new ImageViewButton();
        lImageViewButton.getStyleClass().add(styleClass);
        lImageViewButton.setPickOnBounds(true);
        Tooltip.install(lImageViewButton, new Tooltip(tooltipText));
        return lImageViewButton;
    }
}

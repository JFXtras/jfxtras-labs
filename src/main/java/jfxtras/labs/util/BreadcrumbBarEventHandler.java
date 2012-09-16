package jfxtras.labs.util;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import jfxtras.labs.scene.control.BreadcrumbItem;

/**
 *
 * @author Thierry Wasylczenko
 */
public interface BreadcrumbBarEventHandler<T extends BreadcrumbItem> extends EventHandler<MouseEvent> {
}

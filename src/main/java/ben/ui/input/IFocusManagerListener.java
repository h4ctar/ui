package ben.ui.input;

import ben.ui.widget.IWidget;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for a Focus Manager Listener.
 * <p>
 * Focus Listeners get notified when a widget gets focus.
 */
public interface IFocusManagerListener {

    /**
     * A widget has received focus.
     * @param focusedWidget the widget that has received focus, null if no widget is focused.
     */
    void focusedWidget(@Nullable IWidget focusedWidget);
}

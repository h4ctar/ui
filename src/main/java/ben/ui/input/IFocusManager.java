package ben.ui.input;

import ben.ui.widget.IWidget;

import javax.annotation.Nonnull;

/**
 * Interface for a Focus Manager.
 */
public interface IFocusManager {

    /**
     * Add a listener that will be notified of focus events.
     *
     * @param focusListener the new listener
     */
    void addFocusListener(@Nonnull IFocusManagerListener focusListener);

    /**
     * Remove a focus listener.
     *
     * @param focusListener the listener to remove
     */
    void removeFocusListener(@Nonnull IFocusManagerListener focusListener);

    /**
     * Set the currently focused widget.
     *
     * @param widget the widget to focus
     */
    void setFocusedWidget(@Nonnull IWidget widget);
}

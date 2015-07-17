package ben.ui.input;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for a Focus Manager.
 */
public interface IFocusManager {

    /**
     * Add a listener that will be notified of focus events.
     * @param focusListener the new listener
     */
    void addFocusListener(@NotNull IFocusManagerListener focusListener);

    /**
     * Remove a focus listener.
     * @param focusListener the listener to remove
     */
    void removeFocusListener(@NotNull IFocusManagerListener focusListener);
}

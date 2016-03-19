package ben.ui.widget;

import ben.ui.widget.window.IWindow;

import javax.annotation.Nonnull;

/**
 * Desktop Interface.
 */
public interface IDesktop {

    /**
     * Add a new window to the desktop pane.
     * @param window the new widget to add
     */
    void addWindow(@Nonnull IWindow window);

    /**
     * Remove a window from the desktop pane.
     * @param window the widget to remove
     */
    void removeWindow(@Nonnull IWindow window);

    /**
     * Push a dialog onto the desktop.
     *
     * Dialogs are stacked and removed when they loose focus.
     *
     * @param dialog the dialog to push
     */
    void pushDialog(@Nonnull IWidget dialog);

    /**
     * Pop all the dialogs off the desktop.
     */
    void popAllDialogs();
}

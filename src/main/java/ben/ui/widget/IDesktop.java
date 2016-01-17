package ben.ui.widget;

import javax.annotation.Nonnull;

/**
 * Desktop Interface.
 */
public interface IDesktop {

    /**
     * Push a dialog onto the desktop.
     *
     * Dialogs are stacked and removed when they loose focus.
     *
     * @param dialog the dialog to push
     */
    void pushDialog(@Nonnull IWidget dialog);
}

package ben.ui.widget.window;

import ben.ui.math.Rect;
import ben.ui.widget.IWidget;

import javax.annotation.Nonnull;

/**
 * Window Interface.
 */
public interface IWindow extends IWidget {

    /**
     * Set the desktop rectangle.
     *
     * Can be used to clip the windows position.
     * @param rect the desktop rectangle
     */
    void setDesktopRect(@Nonnull Rect rect);
}

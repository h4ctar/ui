package ben.ui.input.mouse;

import ben.ui.math.Vec2i;
import javax.annotation.Nonnull;

/**
 * Interface for a mouse listener.
 */
public interface IMouseListener {

    /**
     * A mouse button has been clicked.
     *
     * @param button the mouse button that was clicked
     * @param widgetPos the absolute position of the widget that was clicked
     */
    void mouseClicked(@Nonnull MouseButton button, @Nonnull Vec2i widgetPos);

    /**
     * The mouse has entered the area.
     */
    void mouseEntered();

    /**
     * The mouse has exited the area.
     */
    void mouseExited();

    /**
     * A mouse button has been pressed.
     *
     * @param button the mouse button that was pressed
     * @param pos the position of the mouse within the widget
     */
    void mousePressed(@Nonnull MouseButton button, @Nonnull Vec2i pos);

    /**
     * A mouse button has been released.
     *
     * @param button the mouse button that was pressed
     * @param mousePos the position of the mouse within the widget
     */
    void mouseReleased(@Nonnull MouseButton button, @Nonnull Vec2i mousePos);

    /**
     * The mouse has moved.
     *
     * @param mousePos the position of the mouse within the widget
     */
    void mouseMoved(@Nonnull Vec2i mousePos);

    /**
     * The mouse has been dragged.
     *
     * @param mousePos the position of the mouse within the widget
     */
    void mouseDragged(@Nonnull Vec2i mousePos);

    /**
     * The mouse wheel has moved.
     *
     * @param wheel how far the wheel was moved
     */
    void mouseWheelMoved(float wheel);
}

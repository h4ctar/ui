package ben.ui.input.mouse;

import ben.ui.math.Vec2i;
import javax.annotation.Nonnull;

/**
 * Interface for a mouse listener.
 */
public interface IMouseListener {

    /**
     * A mouse button has been clicked.
     * @param button the mouse button that was clicked
     */
    void mouseClicked(@Nonnull MouseButton button);

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
     * @param button the mouse button that was pressed
     * @param pos the position of the mouse
     */
    void mousePressed(@Nonnull MouseButton button, @Nonnull Vec2i pos);

    /**
     * A mouse button has been released.
     * @param button the mouse button that was pressed
     * @param pos the position of the mouse
     */
    void mouseReleased(@Nonnull MouseButton button, @Nonnull Vec2i pos);

    /**
     * The mouse has moved.
     * @param pos the position of the mouse
     */
    void mouseMoved(@Nonnull Vec2i pos);

    /**
     * The mouse has been dragged.
     * @param pos the position of the mouse
     */
    void mouseDragged(@Nonnull Vec2i pos);

    /**
     * The mouse wheel has moved.
     * @param wheel how far the wheel was moved
     */
    void mouseWheelMoved(float wheel);
}

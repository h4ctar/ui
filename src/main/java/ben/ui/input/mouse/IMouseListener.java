package ben.ui.input.mouse;

import ben.math.Vec2i;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for a mouse listener.
 */
public interface IMouseListener {

    /**
     * A mouse button has been clicked.
     * @param button the mouse button that was clicked
     */
    void mouseClicked(@NotNull MouseButton button);

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
    void mousePressed(@NotNull MouseButton button, @NotNull Vec2i pos);

    /**
     * A mouse button has been released.
     * @param button the mouse button that was pressed
     * @param pos the position of the mouse
     */
    void mouseReleased(@NotNull MouseButton button, @NotNull Vec2i pos);

    /**
     * The mouse has moved.
     * @param pos the position of the mouse
     */
    void mouseMoved(@NotNull Vec2i pos);

    /**
     * The mouse has been dragged.
     * @param pos the position of the mouse
     */
    void mouseDragged(@NotNull Vec2i pos);

    /**
     * The mouse wheel has moved.
     * @param wheel how far the wheel was moved
     */
    void mouseWheelMoved(float wheel);
}

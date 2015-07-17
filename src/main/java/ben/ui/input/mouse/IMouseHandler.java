package ben.ui.input.mouse;

import ben.ui.math.Vec2i;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for a mouse handler.
 */
public interface IMouseHandler {

    /**
     * Add a listener that will be notified when a mouse event occurs.
     * @param mouseListener the listener to add
     */
    void addMouseListener(@NotNull IMouseListener mouseListener);

    /**
     * Remove a mouse listener.
     * @param mouseListener the listener to remove
     */
    void removeMouseListener(@NotNull IMouseListener mouseListener);

    /**
     * A mouse button has been clicked.
     * @param button the mouse button that was clicked
     * @param pos the position of the mouse
     * @return true if the click is consumed
     */
    boolean mouseClicked(@NotNull MouseButton button, @NotNull Vec2i pos);

    /**
     * The mouse has entered the area.
     * @return true if the event is consumed
     */
    boolean mouseEntered();

    /**
     * The mouse has exited the area.
     * @return true if the event is consumed
     */
    boolean mouseExited();

    /**
     * A mouse button has been pressed.
     * @param button the mouse button that was pressed
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mousePressed(@NotNull MouseButton button, @NotNull Vec2i pos);

    /**
     * A mouse button has been released.
     * @param button the mouse button that was pressed
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mouseReleased(@NotNull MouseButton button, @NotNull Vec2i pos);

    /**
     * The mouse has moved.
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mouseMoved(@NotNull Vec2i pos);

    /**
     * The mouse has been dragged.
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mouseDragged(@NotNull Vec2i pos);

    /**
     * The mouse wheel has moved.
     * @param wheel how far the wheel was moved
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mouseWheelMoved(float wheel, @NotNull Vec2i pos);
}

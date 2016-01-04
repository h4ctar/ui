package ben.ui.input.mouse;

import ben.ui.math.Vec2i;
import javax.annotation.Nonnull;

/**
 * Interface for a mouse handler.
 */
public interface IMouseHandler {

    /**
     * Add a listener that will be notified when a mouse event occurs.
     * @param mouseListener the listener to add
     */
    void addMouseListener(@Nonnull IMouseListener mouseListener);

    /**
     * Remove a mouse listener.
     * @param mouseListener the listener to remove
     */
    void removeMouseListener(@Nonnull IMouseListener mouseListener);

    /**
     * A mouse button has been clicked.
     * @param button the mouse button that was clicked
     * @param pos the position of the mouse
     * @return true if the click is consumed
     */
    boolean mouseClicked(@Nonnull MouseButton button, @Nonnull Vec2i pos);

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
    boolean mousePressed(@Nonnull MouseButton button, @Nonnull Vec2i pos);

    /**
     * A mouse button has been released.
     * @param button the mouse button that was pressed
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mouseReleased(@Nonnull MouseButton button, @Nonnull Vec2i pos);

    /**
     * The mouse has moved.
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mouseMoved(@Nonnull Vec2i pos);

    /**
     * The mouse has been dragged.
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mouseDragged(@Nonnull Vec2i pos);

    /**
     * The mouse wheel has moved.
     * @param wheel how far the wheel was moved
     * @param pos the position of the mouse
     * @return true if the event is consumed
     */
    boolean mouseWheelMoved(float wheel, @Nonnull Vec2i pos);
}

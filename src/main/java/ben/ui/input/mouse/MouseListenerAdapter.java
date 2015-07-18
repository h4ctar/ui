package ben.ui.input.mouse;

import ben.math.Vec2i;
import org.jetbrains.annotations.NotNull;

/**
 * Adapter for the mouse listener.
 */
public class MouseListenerAdapter implements IMouseListener {

    @Override
    public void mouseClicked(@NotNull MouseButton button) { }

    @Override
    public void mouseEntered() { }

    @Override
    public void mouseExited() { }

    @Override
    public void mousePressed(@NotNull MouseButton button, @NotNull Vec2i pos) { }

    @Override
    public void mouseReleased(@NotNull MouseButton button, @NotNull Vec2i pos) { }

    @Override
    public void mouseMoved(@NotNull Vec2i pos) { }

    @Override
    public void mouseDragged(@NotNull Vec2i pos) { }

    @Override
    public void mouseWheelMoved(float wheel) { }
}

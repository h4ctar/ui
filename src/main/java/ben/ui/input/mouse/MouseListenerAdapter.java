package ben.ui.input.mouse;

import ben.ui.math.Vec2i;
import javax.annotation.Nonnull;

/**
 * Adapter for the mouse listener.
 */
public class MouseListenerAdapter implements IMouseListener {

    @Override
    public void mouseClicked(@Nonnull MouseButton button) { }

    @Override
    public void mouseEntered() { }

    @Override
    public void mouseExited() { }

    @Override
    public void mousePressed(@Nonnull MouseButton button, @Nonnull Vec2i pos) { }

    @Override
    public void mouseReleased(@Nonnull MouseButton button, @Nonnull Vec2i pos) { }

    @Override
    public void mouseMoved(@Nonnull Vec2i pos) { }

    @Override
    public void mouseDragged(@Nonnull Vec2i pos) { }

    @Override
    public void mouseWheelMoved(float wheel) { }
}

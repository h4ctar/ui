package ben.ui.input.mouse;

import ben.ui.math.Vec2i;
import javax.annotation.Nonnull;

import java.util.HashSet;
import java.util.Set;

/**
 * Basic Mouse Handler.
 */
public final class BasicMouseHandler implements IMouseHandler {

    /**
     * The key listeners.
     */
    private final Set<IMouseListener> mouseListeners = new HashSet<>();

    @Override
    public void addMouseListener(@Nonnull IMouseListener mouseListener) {
        assert !mouseListeners.contains(mouseListener) : "Mouse listener already added";
        mouseListeners.add(mouseListener);
    }

    @Override
    public void removeMouseListener(@Nonnull IMouseListener mouseListener) {
        assert mouseListeners.contains(mouseListener) : "Mouse listener was never added or is already removed";
        mouseListeners.remove(mouseListener);
    }

    @Override
    public boolean mouseClicked(@Nonnull MouseButton button, @Nonnull Vec2i mousePos, @Nonnull Vec2i widgetPos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseClicked(button, widgetPos);
        }
        return true;
    }

    @Override
    public boolean mouseEntered() {
        for (IMouseListener listener : mouseListeners) {
            listener.mouseEntered();
        }
        return true;
    }


    @Override
    public boolean mouseExited() {
        for (IMouseListener listener : mouseListeners) {
            listener.mouseExited();
        }
        return true;
    }

    @Override
    public boolean mousePressed(@Nonnull MouseButton button, @Nonnull Vec2i mousePos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mousePressed(button, mousePos);
        }
        return true;
    }

    @Override
    public boolean mouseReleased(@Nonnull MouseButton button, @Nonnull Vec2i mousePos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseReleased(button, mousePos);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(@Nonnull Vec2i mousePos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseMoved(mousePos);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(@Nonnull Vec2i mousePos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseDragged(mousePos);
        }
        return true;
    }

    @Override
    public boolean mouseWheelMoved(float wheel, @Nonnull Vec2i mousePos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseWheelMoved(wheel);
        }
        return true;
    }
}

package ben.ui.input.mouse;

import ben.ui.math.Vec2i;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Basic Mouse Handler.
 */
public class BasicMouseHandler implements IMouseHandler {

    /**
     * The key listeners.
     */
    private final Set<IMouseListener> mouseListeners = new HashSet<>();

    @Override
    public final void addMouseListener(@NotNull IMouseListener mouseListener) {
        assert !mouseListeners.contains(mouseListener) : "Mouse listener already added";
        mouseListeners.add(mouseListener);
    }

    @Override
    public final void removeMouseListener(@NotNull IMouseListener mouseListener) {
        assert mouseListeners.contains(mouseListener) : "Mouse listener was never added or is already removed";
        mouseListeners.remove(mouseListener);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButton button, @NotNull Vec2i pos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseClicked(button);
        }
        return true;
    }

    @Override
    public boolean mouseEntered() {
        mouseListeners.forEach(IMouseListener::mouseEntered);
        return true;
    }


    @Override
    public boolean mouseExited() {
        mouseListeners.forEach(IMouseListener::mouseExited);
        return true;
    }

    @Override
    public boolean mousePressed(@NotNull MouseButton button, @NotNull Vec2i pos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mousePressed(button, pos);
        }
        return true;
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButton button, @NotNull Vec2i pos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseReleased(button, pos);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(@NotNull Vec2i pos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseMoved(pos);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(@NotNull Vec2i pos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseDragged(pos);
        }
        return true;
    }

    @Override
    public boolean mouseWheelMoved(float wheel, @NotNull Vec2i pos) {
        for (IMouseListener mouseListener : mouseListeners) {
            mouseListener.mouseWheelMoved(wheel);
        }
        return true;
    }
}

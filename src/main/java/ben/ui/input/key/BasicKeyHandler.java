package ben.ui.input.key;

import javax.annotation.Nonnull;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * A basic key handler.
 * <p>
 * Only notifies listeners.
 */
public final class BasicKeyHandler implements IKeyHandler {

    /**
     * The key listeners.
     */
    private final Set<IKeyListener> keyListeners = new HashSet<>();

    /**
     * True of the key handler will consume events.
     */
    private boolean consumeEvents;

    /**
     * If disabled, the handler will not notify listeners.
     */
    private boolean enabled = true;

    @Override
    public void addKeyListener(@Nonnull IKeyListener keyListener) {
        assert !keyListeners.contains(keyListener);
        keyListeners.add(keyListener);
    }

    @Override
    public void removeKeyListener(@Nonnull IKeyListener keyListener) {
        assert keyListeners.contains(keyListener);
        keyListeners.remove(keyListener);
    }

    @Override
    public boolean keyPressed(@Nonnull KeyEvent e) {
        if (enabled) {
            for (IKeyListener keyListener : keyListeners) {
                keyListener.keyPressed(e);
            }
        }
        return consumeEvents;
    }

    @Override
    public boolean keyReleased(@Nonnull KeyEvent e) {
        if (enabled) {
            for (IKeyListener keyListener : keyListeners) {
                keyListener.keyReleased(e);
            }
        }
        return consumeEvents;
    }

    /**
     * Set if the key handler should consume events.
     * @param consumeEvents true if it should consume events
     */
    public void setConsumeEvents(boolean consumeEvents) {
        this.consumeEvents = consumeEvents;
    }

    /**
     * Set if the key handler should notify listeners of events.
     * @param enabled true if enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

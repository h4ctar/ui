package ben.ui.input.key;

import ben.ui.input.IFocusManager;
import ben.ui.input.IFocusManagerListener;
import ben.ui.widget.IWidget;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * The container key handler.
 * <p>
 * Passes key events onto the focused widget.
 */
public final class ContainerKeyHandler implements IKeyHandler {

    /**
     * The focus manager.
     */
    @Nonnull
    private final IFocusManager focusManager;

    /**
     * The focused widget.
     */
    @Nullable
    private IWidget focusedWidget;

    /**
     * The key listeners.
     */
    private final Set<IKeyListener> keyListeners = new HashSet<>();

    /**
     * The focus listener for the key handler.
     */
    private final FocusListener focusListener = new FocusListener();

    /**
     * Should the handler consume events.
     */
    private boolean consumeEvents;

    /**
     * Constructor.
     * @param focusManager the focus manager that will notify this key handler when a widget gets focus
     */
    public ContainerKeyHandler(@Nonnull IFocusManager focusManager) {
        this.focusManager = focusManager;
        this.focusManager.addFocusListener(focusListener);
    }

    /**
     * Remove teh container key handler.
     */
    public void remove() {
        focusManager.removeFocusListener(focusListener);
    }

    /**
     * Set if the handler should consume events.
     * @param consumeEvents true if it should consume events
     */
    public void setConsumeEvents(boolean consumeEvents) {
        this.consumeEvents = consumeEvents;
    }

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
        boolean consumed = consumeEvents;
        if (focusedWidget != null) {
            consumed |= focusedWidget.getKeyHandler().keyPressed(e);
        }
        for (IKeyListener keyListener : keyListeners) {
            keyListener.keyPressed(e);
        }
        return consumed;
    }

    @Override
    public boolean keyReleased(@Nonnull KeyEvent e) {
        boolean consumed = consumeEvents;
        if (focusedWidget != null) {
            consumed |= focusedWidget.getKeyHandler().keyReleased(e);
        }
        for (IKeyListener keyListener : keyListeners) {
            keyListener.keyReleased(e);
        }
        return consumed;
    }

    /**
     * The Focus Listener.
     */
    private class FocusListener implements IFocusManagerListener {

        @Override
        public void focusedWidget(@Nullable IWidget focusedWidget) {
            ContainerKeyHandler.this.focusedWidget = focusedWidget;
        }
    }
}

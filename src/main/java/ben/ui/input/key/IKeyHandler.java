package ben.ui.input.key;

import javax.annotation.Nonnull;
import java.awt.event.KeyEvent;

/**
 * Interface for the key handler.
 */
public interface IKeyHandler {

    /**
     * Add a key listener.
     * @param keyListener the key listener to add
     */
    void addKeyListener(@Nonnull IKeyListener keyListener);

    /**
     * Remove a key listener.
     * @param keyListener the key listener to remove
     */
    void removeKeyListener(@Nonnull IKeyListener keyListener);

    /**
     * A key has been pressed.
     * @param e the key event
     * @return true if the handler consumed the event
     */
    boolean keyPressed(@Nonnull KeyEvent e);

    /**
     * A key has been released.
     * @param e the key event
     * @return true if the handler consumed the event
     */
    boolean keyReleased(@Nonnull KeyEvent e);
}

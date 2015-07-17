package ben.ui.input.key;

import com.jogamp.newt.event.KeyEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for the key handler.
 */
public interface IKeyHandler {

    /**
     * Add a key listener.
     * @param keyListener the key listener to add
     */
    void addKeyListener(@NotNull IKeyListener keyListener);

    /**
     * Remove a key listener.
     * @param keyListener the key listener to remove
     */
    void removeKeyListener(@NotNull IKeyListener keyListener);

    /**
     * A key has been pressed.
     * @param e the key event
     * @return true if the handler consumed the event
     */
    boolean keyPressed(@NotNull KeyEvent e);

    /**
     * A key has been released.
     * @param e the key event
     * @return true if the handler consumed the event
     */
    boolean keyReleased(@NotNull KeyEvent e);
}

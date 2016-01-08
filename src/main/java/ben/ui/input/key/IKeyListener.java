package ben.ui.input.key;

import javax.annotation.Nonnull;
import java.awt.event.KeyEvent;

/**
 * Interface for a key listener.
 */
public interface IKeyListener {

    /**
     * A key has been pressed.
     * @param e the key event
     */
    void keyPressed(@Nonnull KeyEvent e);

    /**
     * A key has been released.
     * @param e the key event
     */
    void keyReleased(@Nonnull KeyEvent e);
}

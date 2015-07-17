package ben.ui.input.key;

import com.jogamp.newt.event.KeyEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for a key listener.
 */
public interface IKeyListener {

    /**
     * A key has been pressed.
     * @param e the key event
     */
    void keyPressed(@NotNull KeyEvent e);

    /**
     * A key has been released.
     * @param e the key event
     */
    void keyReleased(@NotNull KeyEvent e);
}

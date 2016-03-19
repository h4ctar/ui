package ben.ui.action;

/**
 * # Action Listener Interface
 *
 * Action listeners are notified when the executable state of an action changes.
 */
public interface IActionListener {

    /**
     * The action state has changed.
     */
    void actionChanged();
}

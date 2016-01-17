package ben.ui.action;

import ben.ui.math.Vec2i;

import javax.annotation.Nonnull;

/**
 * Interface for an action.
 *
 * An action is added to widgets and will be executed when the widget wants, i.e. when a button is clicked.
 */
public interface IAction {

    /**
     * Execute the action.
     * @param widgetPos the absolute position of the widget when the action is executed
     */
    void execute(@Nonnull Vec2i widgetPos);

    /**
     * Is the action executable.
     * @return true if it's executable
     */
    boolean isExecutable();

    /**
     * Add a listener that will be notified when the state of the action changes.
     * @param actionListener the listener to add
     */
    void addListener(@Nonnull IActionListener actionListener);

    /**
     * Remove a listener.
     * @param actionListener the listener to remove
     */
    void removeListener(@Nonnull IActionListener actionListener);
}

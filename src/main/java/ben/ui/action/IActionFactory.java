package ben.ui.action;

import org.jetbrains.annotations.NotNull;

/**
 * Action Factory Interface.
 * @param <T> the type of action that the Factory creates.
 */
public interface IActionFactory<T extends IAction> {

    /**
     * Get an Action.
     * @return the action
     */
    @NotNull T getAction();
}

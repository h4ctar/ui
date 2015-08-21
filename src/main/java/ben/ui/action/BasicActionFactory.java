package ben.ui.action;

import org.jetbrains.annotations.NotNull;

/**
 * Basic Action Factory.
 */
public class BasicActionFactory<T extends IAction> implements IActionFactory<T> {

    private final T action;

    public BasicActionFactory(T action) {
        this.action = action;
    }


    /**
     * Get an Action.
     * @return the action
     */
    @NotNull
    @Override
    public T getAction() {
        return action;
    }
}

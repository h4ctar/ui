package ben.ui.action;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Action Manager.
 * <p>
 *     Manages that Action Factories, allowing a controller to get a new instance of an action.
 * </p>
 */
public class ActionManager {

    /**
     * The Action Factories, keyed my action type.
     */
    private final Map<Class<? extends IAction>, IActionFactory<? extends IAction>> actionFactories = new HashMap<>();

    /**
     * Add an action factory.
     * @param actionClass the class that the factory creates
     * @param actionFactory the action factory
     * @param <T> the type of the action
     */
    public final <T extends IAction> void addActionFactory(@NotNull Class<T> actionClass, @NotNull IActionFactory<T> actionFactory) {
        assert !actionFactories.containsKey(actionClass) : actionClass.getSimpleName() + " is already registered";
        actionFactories.put(actionClass, actionFactory);
    }

    /**
     * Add an action factory.
     * @param actionClass the type of the action
     * @param action the action
     * @param <T> the type of the action
     */
    public final <T extends IAction> void addActionFactory(@NotNull Class<T> actionClass, @NotNull T action) {
        assert !actionFactories.containsKey(actionClass) : actionClass.getSimpleName() + " is already registered";
        IActionFactory<T> actionFactory = new BasicActionFactory<>(action);
        actionFactories.put(actionClass, actionFactory);
    }

    /**
     * Get an action.
     * @param <T> the type of the action
     * @param actionClass the type of the action to get
     * @return the action
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public final <T extends IAction> T getAction(@NotNull Class<T> actionClass) {
        assert actionFactories.containsKey(actionClass) : actionClass.getSimpleName() + " is not registered";
        IActionFactory<T> actionFactory = (IActionFactory<T>) actionFactories.get(actionClass);
        return actionFactory.getAction();
    }
}

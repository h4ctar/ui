package ben.ui.action;

import ben.ui.rule.IRule;
import ben.ui.rule.IRuleListener;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * An abstract implementation of IAction.
 */
public abstract class AbstractAction implements IAction {

    /**
     * The Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(AbstractAction.class.getSimpleName());

    /**
     * The action listeners.
     */
    private final Set<IActionListener> listeners = new HashSet<>();

    /**
     * The Rules associated to this Action.
     * <p>
     *     All the rules must pass for this action to be executed.
     * </p>
     */
    private final Set<IRule> rules = new HashSet<>();

    /**
     * The Rule Listener.
     * <p>
     *     Listens to the state of each rule.
     * </p>
     */
    private final RuleListener ruleListener = new RuleListener();

    /**
     * True if the action is executable based on the rules.
     */
    private boolean isExecutable = true;

    @Override
    public final void execute() {
        if (isExecutable()) {
            LOGGER.info("Executing " + this);
            doAction();
        }
        else {
            LOGGER.info("Did not execute " + this + " because it was not executable");
        }
    }

    /**
     * Do the actual action.
     */
    protected abstract void doAction();

    @Override
    public final boolean isExecutable() {
        return isExecutable;
    }

    /**
     * Add a Rule to this Action.
     * @param rule the Rule to add
     */
    protected final void addRule(@NotNull IRule rule) {
        assert !rules.contains(rule) : "Rule already added";
        rules.add(rule);
        rule.addListener(ruleListener);
        evaluateRules();
    }

    /**
     * Remove a Rule from this Action.
     * @param rule the Rule to remove
     */
    protected final void removeRule(@NotNull IRule rule) {
        assert rules.contains(rule) : "Rule not added";
        rules.remove(rule);
        rule.removeListener(ruleListener);
        evaluateRules();
    }

    /**
     * Evaluate all the Rules, update the isExecutable flag and notify listeners.
     */
    private void evaluateRules() {
        boolean isExecutableTmp = true;
        for (IRule rule : rules) {
            if (!rule.isValid()) {
                isExecutableTmp = false;
                break;
            }
        }
        if (isExecutable != isExecutableTmp) {
            isExecutable = isExecutableTmp;
            notifyListeners();
        }
    }

    /**
     * Notify the listeners that the action state has changed.
     */
    private void notifyListeners() {
        for (IActionListener listener : listeners) {
            listener.actionChanged();
        }
    }

    @Override
    public final void addListener(@NotNull IActionListener actionListener) {
        assert !listeners.contains(actionListener);
        listeners.add(actionListener);
        actionListener.actionChanged();
    }

    @Override
    public final void removeListener(@NotNull IActionListener actionListener) {
        assert listeners.contains(actionListener);
        listeners.remove(actionListener);
    }

    /**
     * The Rule Listener.
     * <p>
     *     Listens to the state of each rule.
     * </p>
     */
    private class RuleListener implements IRuleListener {

        @Override
        public void ruleChanged() {
            evaluateRules();
        }
    }
}

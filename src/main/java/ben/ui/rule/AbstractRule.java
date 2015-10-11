package ben.ui.rule;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract Rule.
 * <p>
 *     Provides listener and notification functionality.
 *     Implementing sub classes only need to call setValid when their state changes.
 * </p>
 */
public abstract class AbstractRule implements IRule {

    /**
     * The rule listeners.
     */
    private final Set<IRuleListener> listeners = new HashSet<>();

    /**
     * The current state of the rule.
     */
    private boolean isValid = false;

    @Override
    public final boolean isValid() {
        return isValid;
    }

    /**
     * Set if the rule is valid.
     * @param isValid true if valid
     */
    protected final void setValid(boolean isValid) {
        if (this.isValid != isValid) {
            this.isValid = isValid;
            notifyListeners();
        }
    }

    /**
     * Notify the listeners that the rule state has changed.
     */
    private void notifyListeners() {
        for (IRuleListener listener : listeners) {
            listener.ruleChanged();
        }
    }

    @Override
    public void addListener(@NotNull IRuleListener listener) {
        assert !listeners.contains(listener) : "Listener already added";
        listeners.add(listener);
    }

    @Override
    public void removeListener(@NotNull IRuleListener listener) {
        assert listeners.contains(listener) : "Listener already removed/was never added";
        listeners.remove(listener);
    }
}

package ben.ui.rule;

import javax.annotation.Nonnull;

/**
 * Rule Interface.
 * <p>
 *     Rules are added to actions to enable/disable them.
 *     Implementations are responsible for notifying listeners when their state changes.
 * </p>
 */
public interface IRule {

    /**
     * Is the rule valid?
     * @return true if it's valid
     */
    boolean isValid();

    /**
     * Add a listener that will be notified when the rule changes state.
     * @param listener the listener to add
     */
    void addListener(@Nonnull IRuleListener listener);

    /**
     * Remove a listener.
     * @param listener the listener to remove
     */
    void removeListener(@Nonnull IRuleListener listener);
}

package ben.ui.rule;

import org.jetbrains.annotations.NotNull;

/**
 * Rule Factory.
 */
public interface IRuleFactory<T extends IRule> {

    @NotNull
    T getRule();
}

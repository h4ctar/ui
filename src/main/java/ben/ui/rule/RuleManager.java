package ben.ui.rule;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Rule Manager.
 */
public class RuleManager {

    private final Map<Class<? extends IRule>, IRuleFactory<? extends IRule>> ruleFactories = new HashMap<>();

    /**
     * Add a rule factory.
     * @param <T> the type of the rule
     * @param ruleClass the class that the factory creates
     * @param ruleFactory the rule factory
     */
    public final <T extends IRule> void addRuleFactory(@NotNull Class<T> ruleClass, @NotNull IRuleFactory<T> ruleFactory) {
        assert !ruleFactories.containsKey(ruleClass) : ruleClass.getSimpleName() + " is already registered";
        ruleFactories.put(ruleClass, ruleFactory);
    }

    /**
     * Get a rule.
     * @param <T> the type of the rule
     * @param ruleClass the type of the rule to get
     * @return the rule
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public final <T extends IRule> T getRule(@NotNull Class<T> ruleClass) {
        assert ruleFactories.containsKey(ruleClass) : ruleClass.getSimpleName() + " is not registered";
        IRuleFactory<T> ruleFactory = (IRuleFactory<T>) ruleFactories.get(ruleClass);
        return ruleFactory.getRule();
    }
}

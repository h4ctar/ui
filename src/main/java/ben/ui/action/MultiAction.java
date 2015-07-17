package ben.ui.action;

import ben.ui.rule.AbstractRule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Multi Action.
 * <p>
 *     Executes many actions.
 *     Only valid if all sub actions are valid.
 * </p>
 */
public class MultiAction extends AbstractAction {

    /**
     * The sub Actions.
     */
    @NotNull
    private final List<IAction> actions = new ArrayList<>();

    /**
     * Constructor.
     * @param actions the sub actions
     */
    public MultiAction(IAction ... actions) {
        LocalRule localRule = new LocalRule();
        for (IAction action : actions) {
            this.actions.add(action);
            action.addListener(localRule);
        }
        addRule(localRule);
    }

    @Override
    public String toString() {
        return MultiAction.class.getSimpleName();
    }

    @Override
    protected void doAction() {
        actions.forEach(ben.ui.action.IAction::execute);
    }

    /**
     * Local Rule.
     * <p>
     *     Added to all sub actions.
     *     Makes sure this action is only executable if all sub actions are executable.
     * </p>
     */
    private class LocalRule extends AbstractRule implements IActionListener {

        @Override
        public void actionChanged() {
            boolean valid = true;
            for (IAction action : actions) {
                if (!action.isExecutable()) {
                    valid = false;
                    break;
                }
            }
            setValid(valid);
        }
    }
}

package ben.ui.widget;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Pane Interface.
 * <p>
 *     A pane is a widget that contains other widgets.
 * </p>
 */
public interface IPane extends IWidget {

    /**
     * Get all the child widgets.
     * @return the child widgets
     */
    @NotNull
    Set<IWidget> getWidgets();
}

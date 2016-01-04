package ben.ui.widget;

import javax.annotation.Nonnull;

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
    @Nonnull
    Set<IWidget> getWidgets();
}

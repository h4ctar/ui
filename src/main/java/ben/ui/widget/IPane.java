package ben.ui.widget;

import ben.ui.input.IFocusManager;
import javax.annotation.Nonnull;
import java.util.List;

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
    List<IWidget> getWidgets();

    /**
     * Get the focus manager of the pane.
     * @return the focus manager
     */
    @Nonnull
    IFocusManager getFocusManager();
}

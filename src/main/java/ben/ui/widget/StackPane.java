package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL3;
import org.jetbrains.annotations.Nullable;

/**
 * Stack Pane.
 * <p>
 *     Widgets are resized to the entire pane and are drawn on top of each other.
 * </p>
 */
public class StackPane extends AbstractPane {

    /**
     * Constructor.
     * @param name name of the pane
     */
    public StackPane(@Nullable String name) {
        super(name);
    }

    @Override
    protected void initDraw(@NotNull GL3 gl, @NotNull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@NotNull GL3 gl) { }

    @Override
    protected void doDraw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix) { }

    /**
     * Add a widget to the pane.
     * @param widget the widget to add
     */
    public final void add(@NotNull IWidget widget) {
        addWidget(widget);
        updateLayout();
    }

    @Override
    protected void updateLayout() {
        for (IWidget widget : getWidgets()) {
            widget.setSize(getSize());
        }
    }

    @Override
    public void updateSize() { }
}

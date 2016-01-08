package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Desktop Pane.
 *
 * The desktop pane does no layout of widgets.
 */
public final class DesktopPane extends AbstractPane {

    /**
     * Constructor.
     * @param name the name of the pane
     */
    public DesktopPane(@Nullable String name) {
        super(name, false);
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    @Override
    protected void updateLayout() { }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        return getSize();
    }

    /**
     * Add a new widget to the desktop pane.
     * @param widget the new widget to add
     */
    public void add(@Nonnull IWidget widget) {
        addWidget(widget);
    }

    /**
     * Remove a widget from the desktop pane.
     * @param widget the widget to remove
     */
    public void remove(@Nonnull IWidget widget) {
        removeWidget(widget);
    }

//    public void pushDialog(@Nonnull IWidget dialog) {
//        addWidget(dialog);
//    }
//
//    public void popDialog(@Nonnull IWidget dialog) {
//        removeWidget(dialog);
//    }
}

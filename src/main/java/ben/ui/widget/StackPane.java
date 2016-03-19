package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.IGlResourceManager;
import javax.annotation.Nonnull;

import com.jogamp.opengl.GL2;
import javax.annotation.Nullable;

/**
 * # Stack Pane
 *
 * Widgets are resized to the entire pane and are drawn on top of each other.
 *
 * The pane has a preferred size so that its width is the same as its widest child and height is the same as its tallest
 * child.
 */
public final class StackPane extends AbstractPane {

    /**
     * Constructor.
     * @param name name of the pane
     */
    public StackPane(@Nullable String name) {
        super(name, true, false);
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull IGlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    /**
     * Add a widget to the pane.
     *
     * @param widget the widget to add
     */
    public void add(@Nonnull IWidget widget) {
        addWidget(widget);
        updateLayout();
    }

    @Override
    protected void updateLayout() {
        for (IWidget widget : getWidgets()) {
            widget.setSize(getSize());
        }
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        int width = 0;
        int height = 0;
        for (IWidget widget : getWidgets()) {
            Vec2i widgetSize = widget.getPreferredSize();
            if (widgetSize.getX() > width) {
                width = widgetSize.getX();
            }
            if (widgetSize.getY() > height) {
                height = widgetSize.getY();
            }
        }
        return new Vec2i(width, height);
    }
}

package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Center Pane.
 *
 * <pre>
 * +--------------------+
 * |                    |
 * |     +--------+     |
 * |     | Center |     |
 * |     +--------+     |
 * |                    |
 * +--------------------+
 * </pre>
 *
 * The center widget will keep its preferred size and be placed right in the middle of the pane.
 */
public final class CenterPane extends AbstractPane {

    /**
     * The center widget.
     */
    @Nullable
    private IWidget center;

    /**
     * Constructor.
     * @param name the name of the pane
     * @param center the center pane
     */
    public CenterPane(@Nullable String name, @Nullable IWidget center) {
        super(name, true, true);
        setCenter(center);
    }

    @Override
    public String toString() {
        return CenterPane.class.getSimpleName() + "[name: \"" + getName() + "\"]";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    /**
     * Set the center widget.
     * @param center the top widget.
     */
    public void setCenter(@Nullable IWidget center) {
        assert this.center != center : "Setting the same widget";
        if (this.center != null) {
            removeWidget(this.center);
        }
        this.center = center;
        if (this.center != null) {
            addWidget(this.center);
        }
        updateLayout();
    }

    @Override
    protected void updateLayout() {
        if (center != null) {
            int x = (getSize().getX() - center.getSize().getX()) / 2;
            int y = (getSize().getY() - center.getSize().getY()) / 2;
            center.setPosition(new Vec2i(x, y));
        }
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        return center == null ? new Vec2i(0, 0) : center.getPreferredSize();
    }
}

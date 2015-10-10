package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import com.jogamp.opengl.GL3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Center Pane
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
     */
    public CenterPane(@Nullable String name) {
        super(name);
    }

    @Override
    protected void initDraw(@NotNull GL3 gl, @NotNull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@NotNull GL3 gl) { }

    @Override
    protected void doDraw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix) { }

    /**
     * Set the center widget.
     * @param center the top widget.
     */
    public final void setCenter(@Nullable IWidget center) {
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

    @Override
    public void updateSize() { }
}

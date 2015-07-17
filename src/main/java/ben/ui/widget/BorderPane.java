package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import ben.ui.math.Vec2i;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.jogamp.opengl.GL3;

/**
 * Border Pane.
 *
 * Children widgets are layed out in top, bottom, left and right positions.
 */
@ThreadSafe
public class BorderPane extends AbstractPane {

    /**
     * The top widget.
     */
    @Nullable
    private IWidget top;

    /**
     * The bottom widget.
     */
    @Nullable
    private IWidget bottom;

    /**
     * The left widget.
     */
    @Nullable
    private IWidget left;

    /**
     * The right widget.
     */
    @Nullable
    private IWidget right;

    /**
     * The center widget.
     */
    @Nullable
    private IWidget center;

    /**
     * Constructor.
     * @param name name of the pane
     */
    public BorderPane(@Nullable String name) {
        super(name);
    }

    @Override
    protected void initDraw(@NotNull GL3 gl, @NotNull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@NotNull GL3 gl) { }

    @Override
    protected void doDraw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix) { }

    /**
     * Set the top widget.
     * @param top the top widget.
     */
    public final void setTop(@Nullable IWidget top) {
        assert this.top != top : "Setting the same widget";
        if (this.top != null) {
            removeWidget(this.top);
        }
        this.top = top;
        if (this.top != null) {
            addWidget(this.top);
        }
        updateLayout();
    }

    /**
     * Set the bottom widget.
     * @param bottom the top widget.
     */
    public final void setBottom(@Nullable IWidget bottom) {
        assert this.bottom != bottom : "Setting the same widget";
        if (this.bottom != null) {
            removeWidget(this.bottom);
        }
        this.bottom = bottom;
        if (this.bottom != null) {
            addWidget(this.bottom);
        }
        updateLayout();
    }

    /**
     * Set the left widget.
     * @param left the top widget.
     */
    public final void setLeft(@Nullable IWidget left) {
        assert this.left != left : "Setting the same widget";
        if (this.left != null) {
            removeWidget(this.left);
        }
        this.left = left;
        if (this.left != null) {
            addWidget(this.left);
        }
        updateLayout();
    }

    /**
     * Set the right widget.
     * @param right the top widget.
     */
    public final void setRight(@Nullable IWidget right) {
        assert this.right != right : "Setting the same widget";
        if (this.right != null) {
            removeWidget(this.right);
        }
        this.right = right;
        if (this.right != null) {
            addWidget(this.right);
        }
        updateLayout();
    }

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
    protected final void updateLayout() {
        int centerX = 0;
        int centerY = 0;
        int centerWidth = getSize().getX();
        int centerHeight = getSize().getY();

        if (top != null) {
            top.updateSize();
            top.setPosition(new Vec2i(0, 0));
            top.setSize(new Vec2i(getSize().getX(), top.getSize().getY()));
            centerY = top.getSize().getY();
            centerHeight -= top.getSize().getY();
        }

        if (bottom != null) {
            bottom.updateSize();
            bottom.setPosition(new Vec2i(0, getSize().getY() - bottom.getSize().getY()));
            bottom.setSize(new Vec2i(getSize().getX(), bottom.getSize().getY()));
            centerHeight -= bottom.getSize().getY();
        }

        if (left != null) {
            left.updateSize();
            left.setPosition(new Vec2i(0, centerY));
            left.setSize(new Vec2i(left.getSize().getX(), centerHeight));
            centerX = left.getSize().getX();
            centerWidth -= left.getSize().getX();
        }

        if (right != null) {
            right.updateSize();
            right.setPosition(new Vec2i(getSize().getX() - right.getSize().getX(), centerY));
            right.setSize(new Vec2i(right.getSize().getX(), centerHeight));
            centerWidth -= right.getSize().getX();
        }

        if (center != null) {
            center.setPosition(new Vec2i(centerX, centerY));
            center.setSize(new Vec2i(centerWidth, centerHeight));
        }
    }

    @Override
    public void updateSize() { }
}

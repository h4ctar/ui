package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import ben.ui.math.Vec2i;
import net.jcip.annotations.ThreadSafe;

import com.jogamp.opengl.GL2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Border Pane.
 *
 * Children widgets are layed out in top, bottom, left and right and center positions.
 *
 * <pre>
 * +------------------------+
 * | Top                    |
 * +-------+--------+-------+
 * |       |        |       |
 * | Left  | Center | Right |
 * |       |        |       |
 * +-------+--------+-------+
 * | Bottom                 |
 * +------------------------+
 * </pre>
 *
 * The top and bottom widgets will have their preferred heights, but will get the width of the pane.
 *
 * The left and right widgets will have their preferred widths, but will have the height of the pane minus the heights
 * of the top and bottom widgets.
 *
 * The center widget will be sized with the remaining space, its preferred size will be ignored.
 */
@ThreadSafe
public final class BorderPane extends AbstractPane {

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
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    /**
     * Set the top widget.
     * @param top the top widget.
     */
    public void setTop(@Nullable IWidget top) {
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
    public void setBottom(@Nullable IWidget bottom) {
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
    public void setLeft(@Nullable IWidget left) {
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
    public void setRight(@Nullable IWidget right) {
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
        int centerX = 0;
        int centerY = 0;
        int centerWidth = getSize().getX();
        int centerHeight = getSize().getY();

        if (top != null) {
            Vec2i preferredSize = top.getPreferredSize();
            Vec2i actualSize = new Vec2i(getSize().getX(), preferredSize.getY());
            top.setSize(actualSize);
            top.setPosition(new Vec2i(0, 0));
            centerY = top.getSize().getY();
            centerHeight -= top.getSize().getY();
        }

        if (bottom != null) {
            Vec2i preferredSize = bottom.getPreferredSize();
            Vec2i actualSize = new Vec2i(getSize().getX(), preferredSize.getY());
            bottom.setSize(actualSize);
            bottom.setPosition(new Vec2i(0, getSize().getY() - bottom.getSize().getY()));
            centerHeight -= bottom.getSize().getY();
        }

        if (left != null) {
            Vec2i preferredSize = left.getPreferredSize();
            Vec2i actualSize = new Vec2i(preferredSize.getX(), centerHeight);
            left.setSize(actualSize);
            left.setPosition(new Vec2i(0, centerY));
            centerX = left.getSize().getX();
            centerWidth -= left.getSize().getX();
        }

        if (right != null) {
            Vec2i preferredSize = right.getPreferredSize();
            Vec2i actualSize = new Vec2i(preferredSize.getX(), centerHeight);
            right.setSize(actualSize);
            right.setPosition(new Vec2i(getSize().getX() - right.getSize().getX(), centerY));
            centerWidth -= right.getSize().getX();
        }

        if (center != null) {
            center.setPosition(new Vec2i(centerX, centerY));
            center.setSize(new Vec2i(centerWidth, centerHeight));
        }
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        return getSize();
    }
}

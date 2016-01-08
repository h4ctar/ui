package ben.ui.widget.tab;

import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.math.Vec2i;
import ben.ui.renderer.FlatRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.widget.AbstractPane;
import ben.ui.widget.IWidget;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Tab Bar.
 */
public final class TabBar extends AbstractPane {

    /**
     * The padding between the widgets.
     */
    private static final int PADDING = -1;

    /**
     * The size of the bar at the bottom of the tab bar.
     */
    private static final int BAR = 5;

    /**
     * The background colour of the pane.
     */
    @Nonnull
    private static final Color BACKGROUND_COLOR = new Color(0.235f, 0.247f, 0.254f);

    /**
     * The colour of the bar at the bottom of the tab bar.
     */
    @Nonnull
    private static final Color BAR_COLOR = new Color(0.31f, 0.34f, 0.35f);

    /**
     * The background renderer.
     */
    @Nullable
    private FlatRenderer backgroundRenderer;

    /**
     * The bar renderer.
     */
    @Nullable
    private FlatRenderer barRenderer;

    /**
     * Constructor.
     */
    public TabBar() {
        super(null, true);
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) {
        backgroundRenderer = new FlatRenderer(gl, glResourceManager, getRect(), BACKGROUND_COLOR);
        barRenderer = new FlatRenderer(gl, glResourceManager, getBarRect(), BAR_COLOR);
    }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) {
        assert backgroundRenderer != null : "Update draw should not be called before init draw";
        assert barRenderer != null : "Update draw should not be called before init draw";

        backgroundRenderer.setRect(gl, getRect());
        barRenderer.setRect(gl, getBarRect());
    }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        assert backgroundRenderer != null : "Draw should not be called before init draw";
        assert barRenderer != null : "Update draw should not be called before init draw";

        backgroundRenderer.draw(gl, pmvMatrix);
        barRenderer.draw(gl, pmvMatrix);
    }

    /**
     * Add a widget to the horizontal pane.
     * @param widget the widget to add
     */
    public void add(@Nonnull IWidget widget) {
        addWidget(widget);
        updateLayout();
    }

    @Override
    protected void updateLayout() {
        int x = 0;
        int y = BAR;
        int height = getSize().getY() - 2 * BAR;

        for (IWidget widget : getWidgets()) {
            Vec2i preferredSize = widget.getPreferredSize();
            Vec2i actualSize = new Vec2i(preferredSize.getX(), height);
            widget.setSize(actualSize);
            widget.setPosition(new Vec2i(x, y));
            x += widget.getSize().getX() + PADDING;
        }
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        int height = 0;
        int width = 0;

        if (!getWidgets().isEmpty()) {
            for (IWidget widget : getWidgets()) {
                Vec2i preferredSize = widget.getPreferredSize();
                widget.setSize(preferredSize);
                width += preferredSize.getX() + PADDING;

                if (preferredSize.getY() > height) {
                    height = preferredSize.getY();
                }
            }

            // Remove the last bit of spacing.
            width -= PADDING;
        }

        height += 2 * BAR;

        return new Vec2i(width, height);
    }

    @Override
    public void remove(@Nonnull GL2 gl) {
        if (backgroundRenderer != null) {
            backgroundRenderer.remove(gl);
        }
        if (barRenderer != null) {
            barRenderer.remove(gl);
        }
        super.remove(gl);
    }

    /**
     * Get the bottom bar rectangle.
     * @return the bar rectangle
     */
    private Rect getBarRect() {
        return new Rect(0, getRect().getHeight() - BAR, getSize().getX(), getSize().getY());
    }
}

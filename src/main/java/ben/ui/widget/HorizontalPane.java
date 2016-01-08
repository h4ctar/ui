package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.renderer.FlatRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.math.Vec2i;
import javax.annotation.Nonnull;

import com.jogamp.opengl.GL2;
import javax.annotation.Nullable;

/**
 * Horizontal Pane.
 *
 * Lays widgets out in a horizontal row, with spacing between them and a border around them.
 *
 * <pre>
 * +----------+----------+----------+
 * | Widget 1 | Widget 2 | Widget 3 |
 * +----------+----------+          |
 * |                     |          |
 * +---------------------+----------+
 * </pre>
 *
 * The widgets will all keep their preferred size.
 *
 * The pane will have a preferred width equal to the sum of the widths of the child widgets, plus border and spacing.
 *
 * The pane will have a preferred height equal to the height of the largest child widget plus border.
 */
public final class HorizontalPane extends AbstractPane {

    /**
     * The padding between the widgets.
     */
    private static final int PADDING = 5;

    /**
     * The width of the border around the widgets.
     */
    private static final int BORDER = 5;

    /**
     * The default background colour of the pane.
     */
    @Nonnull
    private static final Color BACKGROUND_COLOR = new Color(0.235f, 0.247f, 0.254f);

    /**
     * Is there border around the frame?
     */
    private final boolean border;

    /**
     * The background renderer.
     */
    @Nullable
    private FlatRenderer backgroundRenderer;

    /**
     * Constructor.
     * @param name the name of the pane
     * @param border is there a border around the frame?
     */
    public HorizontalPane(@Nullable String name, boolean border) {
        super(name, true);
        this.border = border;
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) {
        backgroundRenderer = new FlatRenderer(gl, glResourceManager, getRect(), BACKGROUND_COLOR);
    }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) {
        assert backgroundRenderer != null : "Update draw should not be called before init draw";
        backgroundRenderer.setRect(gl, getRect());
    }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        assert backgroundRenderer != null : "Draw should not be called before init draw";
        backgroundRenderer.draw(gl, pmvMatrix);
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
        int x = border ? BORDER : 0;
        int y = border ? BORDER : 0;
        int height = getSize().getY() - (border ? BORDER * 2 : 0);

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

        if (getWidgets().isEmpty()) {
            width = 0;
            height = 0;
        }
        else {
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

            if (border) {
                width += 2 * BORDER;
                height += 2 * BORDER;
            }
        }

        return new Vec2i(width, height);
    }

    @Override
    public void remove(@Nonnull GL2 gl) {
        if (backgroundRenderer != null) {
            backgroundRenderer.remove(gl);
        }
        super.remove(gl);
    }
}

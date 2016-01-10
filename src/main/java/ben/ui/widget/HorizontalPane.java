package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import ben.ui.math.Vec2i;

import com.jogamp.opengl.GL2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Horizontal Pane.
 *
 * Lays widgets out in a horizontal row, with spacing between them and a padding around them.
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
 * The pane will have a preferred width equal to the sum of the widths of the child widgets, plus padding and spacing.
 *
 * The pane will have a preferred height equal to the height of the largest child widget plus padding.
 */
public final class HorizontalPane extends AbstractPane {

    /**
     * The spacing between the widgets.
     */
    private static final int SPACING = 5;

    /**
     * The width of the padding around the widgets.
     */
    private static final int PADDING = 5;

    /**
     * Is there padding around the frame?
     */
    private final boolean padding;

    /**
     * Constructor.
     * @param name the name of the pane
     * @param padding is there padding around the frame?
     */
    public HorizontalPane(@Nullable String name, boolean padding) {
        super(name, true, true);
        this.padding = padding;
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

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
        int x = padding ? PADDING : 0;
        int y = padding ? PADDING : 0;
        int height = getSize().getY() - (padding ? PADDING * 2 : 0);

        for (IWidget widget : getWidgets()) {
            Vec2i preferredSize = widget.getPreferredSize();
            Vec2i actualSize = new Vec2i(preferredSize.getX(), height);
            widget.setSize(actualSize);
            widget.setPosition(new Vec2i(x, y));
            x += widget.getSize().getX() + SPACING;
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
                width += preferredSize.getX() + SPACING;

                if (preferredSize.getY() > height) {
                    height = preferredSize.getY();
                }
            }

            // Remove the last bit of spacing.
            width -= SPACING;

            if (padding) {
                width += 2 * PADDING;
                height += 2 * PADDING;
            }
        }

        return new Vec2i(width, height);
    }
}

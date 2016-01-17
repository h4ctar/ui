package ben.ui.widget;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import ben.ui.math.Vec2i;
import javax.annotation.Nonnull;

import com.jogamp.opengl.GL2;
import javax.annotation.Nullable;

/**
 * Vertical Pane.
 *
 * Lays widgets out in a vertical column, with spacing between them and a padding around them.
 *
 * <pre>
 * +----------+---+
 * | Widget 1 |   |
 * +----------+   |
 * | Widget 2 |   |
 * +----------+---+
 * | Widget 3     |
 * +--------------+
 * </pre>
 *
 * The widgets will all keep their preferred size.
 *
 * The pane will have a preferred width equal to the width of the largest child widget plus padding.
 *
 * The pane will have a preferred height equal to the sum of the heights of the child widgets, plus spacing and padding.
 */
public final class VerticalPane extends AbstractPane {

    /**
     * The spacing between the widgets.
     */
    private static final int SPACING = 5;

    /**
     * The width of the padding around the frame.
     */
    private static final int PADDING = 5;

    /**
     * Is there padding around the frame?
     */
    private final boolean padding;

    /**
     * Constructor.
     * @param name the name of the pane
     * @param padding is there padding around the frame
     */
    public VerticalPane(@Nullable String name, boolean padding) {
        super(name, true, true);
        this.padding = padding;
    }

    @Override
    public String toString() {
        return VerticalPane.class.getSimpleName() + "[name: \"" + getName() + "\"]";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    /**
     * Add a widget to this pane.
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
        int width = getSize().getX() - (padding ? PADDING * 2 : 0);

        for (IWidget widget : getWidgets()) {
            Vec2i preferredSize = widget.getPreferredSize();
            Vec2i actualSize = new Vec2i(width, preferredSize.getY());
            widget.setSize(actualSize);
            widget.setPosition(new Vec2i(x, y));
            y += widget.getSize().getY() + SPACING;
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
                height += preferredSize.getY() + SPACING;

                if (preferredSize.getX() > width) {
                    width = preferredSize.getX();
                }
            }

            // Remove the last bit of padding.
            height -= SPACING;

            if (padding) {
                width += 2 * PADDING;
                height += 2 * PADDING;
            }
        }

        return new Vec2i(width, height);
    }
}

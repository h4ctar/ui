package ben.ui.widget;

import ben.math.PmvMatrix;
import ben.math.Rect;
import ben.ui.renderer.FlatRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.math.Vec2i;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL3;
import org.jetbrains.annotations.Nullable;

/**
 * Vertical Pane.
 * <p>
 *     Lays widgets out in a vertical column, with padding around them.
 *     Draws a background.
 * </p>
 */
public class VerticalPane extends AbstractPane {

    /**
     * The padding around the widgets.
     */
    private static final int PADDING = 5;

    /**
     * The background colour of the pane.
     */
    @NotNull
    private static final Color BACKGROUND_COLOR = new Color(0.235f, 0.247f, 0.254f);

    /**
     * The background renderer.
     */
    private FlatRenderer backgroundRenderer;

    /**
     * Constructor.
     * @param name the name of the pane
     */
    public VerticalPane(@Nullable String name) {
        super(name);
    }

    @Override
    protected void initDraw(@NotNull GL3 gl, @NotNull GlResourceManager glResourceManager) {
        backgroundRenderer = new FlatRenderer(gl, glResourceManager, getRect(), BACKGROUND_COLOR);
    }

    @Override
    protected void updateDraw(@NotNull GL3 gl) {
        backgroundRenderer.setRect(gl, getRect());
    }

    /**
     * Get the position and size of the pane.
     */
    private Rect getRect() {
        return new Rect(new Vec2i(0, 0), getSize());
    }

    @Override
    protected void doDraw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix) {
        backgroundRenderer.draw(gl, pmvMatrix);
    }

    /**
     * Add a widget to this pane.
     * @param widget the widget to add
     */
    public final void add(@NotNull IWidget widget) {
        addWidget(widget);
        updateSize();
        updateLayout();
    }

    @Override
    protected void updateLayout() {
        int y = PADDING;
        for (IWidget widget : getWidgets()) {
            widget.updateSize();
            widget.setSize(new Vec2i(getSize().getX() - 2 * PADDING, widget.getSize().getY()));
            widget.setPosition(new Vec2i(PADDING, y));
            y += widget.getSize().getY() + PADDING;
        }
    }

    @Override
    public void updateSize() {
        int height = PADDING;
        int width = PADDING;
        for (IWidget widget : getWidgets()) {
            widget.updateSize();
            height += widget.getSize().getY() + PADDING;
            if (widget.getSize().getX() + 2 * PADDING > width) {
                width = widget.getSize().getX() + 2 * PADDING;
            }
        }
        setSize(new Vec2i(width, height));
    }

    @Override
    public void remove(@NotNull GL3 gl) {
        super.remove(gl);
        backgroundRenderer.remove(gl);
    }
}

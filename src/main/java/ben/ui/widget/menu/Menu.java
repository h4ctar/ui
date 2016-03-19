package ben.ui.widget.menu;

import ben.ui.action.IAction;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.renderer.LineRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.widget.AbstractPane;
import ben.ui.widget.IDesktop;
import ben.ui.widget.IWidget;
import com.jogamp.opengl.GL2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Menu.
 */
public final class Menu extends AbstractPane {

    /**
     * The width of the window frame.
     */
    private static final int FRAME = 1;

    /**
     * The colour of the window frame.
     */
    @Nonnull
    private static final Color FRAME_COLOR = new Color(0.37f, 0.38f, 0.38f);

    /**
     * The spacing between the menu items.
     */
    private static final int SPACING = 5;

    /**
     * The width of the padding around the frame.
     */
    private static final int PADDING = 5;

    /**
     * The desktop that this menu is added to.
     */
    @Nonnull
    private final IDesktop desktop;

    /**
     * The frame renderer.
     */
    @Nullable
    private LineRenderer frameRenderer;

    /**
     * Constructor.
     *
     * @param name the name of the pane
     * @param desktop the desktop that this menu is added to
     */
    public Menu(@Nullable String name, @Nonnull IDesktop desktop) {
        super(name, true, true);
        this.desktop = desktop;
    }

    @Override
    public String toString() {
        return Menu.class.getSimpleName() + "[name: \"" + getName() + "\"]";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) {
        float[] positions = getFrameLines();
        frameRenderer = new LineRenderer(gl, glResourceManager, positions, 2, GL2.GL_LINES, FRAME_COLOR);
    }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) {
        assert frameRenderer != null;

        float[] positions = getFrameLines();
        frameRenderer.setPositions(gl, positions);
    }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        assert frameRenderer != null;

        frameRenderer.draw(gl, pmvMatrix);
    }

    @Override
    protected void updateLayout() {
        int x = PADDING;
        int y = PADDING;
        int width = getSize().getX() - PADDING * 2;

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

            width += 2 * PADDING;
            height += 2 * PADDING;
        }

        return new Vec2i(width, height);
    }

    /**
     * Add a menu item with an action.
     *
     * @param name the name of the menu item
     * @param text the text of the menu item
     * @param action the action that will be executed when the menu item is clicked
     */
    public void addMenuItem(@Nullable String name, @Nonnull String text, @Nullable IAction action) {
        MenuItem menuItem = new MenuItem(name, text, action, desktop);
        addWidget(menuItem);
        pack();
    }

    /**
     * Add a menu item for a sub menu.
     *
     * @param name the name of the menu item
     * @param text the text of the menu item
     * @param menu the menu to add
     */
    public void addMenuItem(@Nullable String name, @Nonnull String text, @Nonnull Menu menu) {
        MenuItem menuItem = new MenuItem(name, text, menu, desktop);
        addWidget(menuItem);
        pack();
    }

    /**
     * Pack the menu so that its size is its preferred size.
     */
    private void pack() {
        setSize(getPreferredSize());
    }

    /**
     * Get the verticies for the frame lines.
     *
     * @return the verticies
     */
    private float[] getFrameLines() {
        int numVerts = 10;

        float[] frameLines = new float[numVerts * 2];
        int i = 0;

        // Top.
        frameLines[i++] = 0;
        frameLines[i++] = FRAME;
        frameLines[i++] = getSize().getX();
        frameLines[i++] = FRAME;

        // Right.
        frameLines[i++] = getSize().getX();
        frameLines[i++] = FRAME;
        frameLines[i++] = getSize().getX();
        frameLines[i++] = getSize().getY();

        // Bottom.
        frameLines[i++] = FRAME;
        frameLines[i++] = getSize().getY();
        frameLines[i++] = getSize().getX();
        frameLines[i++] = getSize().getY();

        // Left.
        frameLines[i++] = FRAME;
        frameLines[i++] = getSize().getY();
        frameLines[i++] = FRAME;
        frameLines[i] = FRAME;

        return frameLines;
    }
}

package ben.ui.widget.window;

import ben.ui.input.mouse.MouseButton;
import ben.ui.input.mouse.MouseListenerAdapter;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.math.Vec2i;
import ben.ui.renderer.LineRenderer;
import ben.ui.resource.IGlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.widget.AbstractPane;
import ben.ui.widget.IWidget;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * # Window
 *
 *     +--------------------------+
 *     | Title Bar                |
 *     +--------------------------+
 *     | Content                  |
 *     |                          |
 *     +--------------------------+
 *
 * The preferred size of the pane will be the preferred size of the content plus a little bit of height to fit the
 * title bar.
 *
 * If the window is resized the content will be sized to fit.
 */
public final class Window extends AbstractPane implements IWindow {

    /**
     * The width of the window frame.
     */
    private static final int FRAME = 1;

    /**
     * The colour of the window frame.
     */
    @Nonnull
    private static final Color FRAME_COLOR = Color.BLACK;

    /**
     * The title of the window.
     */
    @Nonnull
    private final String title;

    /**
     * The title bar.
     */
    @Nonnull
    private final TitleBar titleBar;

    /**
     * The content widget.
     */
    @Nonnull
    private final IWidget contentWidget;

    /**
     * The frame renderer.
     */
    @Nullable
    private LineRenderer frameRenderer;

    /**
     * The desktop rectangle.
     *
     * Used to clip the windows position.
     */
    @Nullable
    private Rect desktopRect;

    /**
     * Constructor.
     *
     * @param name the name of the widget
     * @param title the title of the window
     * @param contentWidget the content widget
     */
    public Window(@Nullable String name, @Nonnull String title, @Nonnull IWidget contentWidget) {
        super(name, true, true);
        this.title = title;
        this.contentWidget = contentWidget;
        titleBar = new TitleBar(null, title);
        titleBar.getMouseHandler().addMouseListener(new MoveWindowMouseListener());
        addWidget(titleBar);
        addWidget(contentWidget);
        pack();
    }

    @Override
    public String toString() {
        return Window.class.getSimpleName() + "[name: \"" + getName() + "\", title: \"" + title + "\"]";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull IGlResourceManager glResourceManager) {
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
        int titleHeight = titleBar.getPreferredSize().getY();

        titleBar.setPosition(new Vec2i(FRAME, FRAME));
        titleBar.setSize(new Vec2i(getSize().getX() - 2 * FRAME, titleHeight));

        contentWidget.setPosition(new Vec2i(FRAME, titleHeight + 2 * FRAME));
        contentWidget.setSize(new Vec2i(getSize().getX() - 2 * FRAME, getSize().getY() - titleHeight - 3 * FRAME));
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        Vec2i titlePrefSize = titleBar.getPreferredSize();
        Vec2i contentSize = contentWidget.getPreferredSize();

        int windowWidth = Math.max(titlePrefSize.getX(), contentSize.getX()) + FRAME * 2;
        int windowHeight = contentSize.getY() + titlePrefSize.getY() + FRAME * 3;

        return new Vec2i(windowWidth, windowHeight);
    }

    @Override
    public void setDesktopRect(@Nullable Rect rect) {
        this.desktopRect = rect;
        Vec2i newPos = getClippedPosition(getPosition());
        setPosition(newPos);
    }

    /**
     * Pack the window so that its size is its preferred size.
     */
    public void pack() {
        setSize(getPreferredSize());
    }

    /**
     * Get the window position clipped against the desktop pane.
     *
     * @param pos the un clipped position
     * @return the clipped position
     */
    private Vec2i getClippedPosition(@Nonnull Vec2i pos) {
        int x = pos.getX();
        int y = pos.getY();
        if (desktopRect != null) {
            x = Math.max(x, desktopRect.getX());
            x = Math.min(x, desktopRect.getX() + desktopRect.getWidth() - getSize().getX());
            y = Math.max(y, desktopRect.getY());
            y = Math.min(y, desktopRect.getY() + desktopRect.getHeight() - getSize().getY());
        }
        return new Vec2i(x, y);
    }

    /**
     * The Move Window Mouse Listener.
     */
    private class MoveWindowMouseListener extends MouseListenerAdapter {

        /**
         * The mouse position when the mouse was pressed.
         */
        private Vec2i startMouse;

        @Override
        public void mousePressed(@Nonnull MouseButton button, @Nonnull Vec2i pos) {
            startMouse = pos;
        }

        @Override
        public void mouseDragged(@Nonnull Vec2i mousePos) {
            Vec2i newPos = getClippedPosition(getPosition().add(mousePos).sub(startMouse));
            setPosition(newPos);
        }
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

        // Title.
        frameLines[i++] = FRAME;
        frameLines[i++] = titleBar.getSize().getY() + 2 * FRAME;
        frameLines[i++] = getSize().getX();
        frameLines[i++] = titleBar.getSize().getY() + 2 * FRAME;

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

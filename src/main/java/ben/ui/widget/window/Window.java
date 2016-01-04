package ben.ui.widget.window;

import ben.ui.input.mouse.MouseButton;
import ben.ui.input.mouse.MouseListenerAdapter;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import ben.ui.widget.AbstractPane;
import ben.ui.widget.IWidget;
import com.jogamp.opengl.GL2;
import net.jcip.annotations.ThreadSafe;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Window.
 *
 * <pre>
 * +--------------------------+
 * | Title Bar                |
 * +--------------------------+
 * | Content                  |
 * |                          |
 * +--------------------------+
 * </pre>
 *
 * The preferred size of the pane will be the preferred size of the content plus a little bit of height to fit the
 * title bar.
 *
 * If the window is resized the content will be sized to fit.
 */
@ThreadSafe
public final class Window extends AbstractPane {

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
     * Constructor.
     * @param name the name of the widget
     * @param title the title of the window
     * @param contentWidget the content widget
     */
    public Window(@Nullable String name, @Nonnull String title, @Nonnull IWidget contentWidget) {
        super(name);
        titleBar = new TitleBar(null, title);
        this.contentWidget = contentWidget;
        titleBar.getMouseHandler().addMouseListener(new MoveWindowMouseListener());
        addWidget(titleBar);
        addWidget(contentWidget);
        setSize(getPreferredSize());
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    @Override
    protected void updateLayout() {
        int titleHeight = titleBar.getPreferredSize().getY();

        titleBar.setPosition(new Vec2i(0, 0));
        titleBar.setSize(new Vec2i(getSize().getX(), titleHeight));

        contentWidget.setPosition(new Vec2i(0, titleHeight));
        contentWidget.setSize(new Vec2i(getSize().getX(), getSize().getY() - titleHeight));
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        Vec2i titlePrefSize = titleBar.getPreferredSize();
        Vec2i contentSize = contentWidget.getPreferredSize();

        int windowWidth = Math.max(titlePrefSize.getX(), contentSize.getX());
        int windowHeight = contentSize.getY() + titlePrefSize.getY();

        return new Vec2i(windowWidth, windowHeight);
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
        public void mouseDragged(@Nonnull Vec2i pos) {
            Vec2i newPos = getPosition().add(pos).sub(startMouse);
            setPosition(newPos);
        }
    }
}

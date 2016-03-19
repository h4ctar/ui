package ben.ui.widget.scroll;

import ben.ui.input.mouse.MouseListenerAdapter;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.IGlResourceManager;
import ben.ui.widget.AbstractPane;
import ben.ui.widget.IWidget;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * # Scroll Pane
 *
 *     +-------------------------+-+
 *     | Content                 | |
 *     |                         +-+
 *     |                         |#|
 *     |                         +-+
 *     |                         | |
 *     |                         | |
 *     +-------------------------+-+
 *
 * The preferred size of the scroll pane will be the preferred size of the content plus a little width for the scroll
 * bar (This will result with the pane not requiring scrolling).
 *
 * If the preferred size of the pane is ignored then the content widget will be resized to the width of the pane (minus
 * width of scroll bar) and the height will take the height of the pane.
 */
public final class ScrollPane extends AbstractPane {

    /**
     * The vertical scroll bar widget.
     */
    @Nonnull
    private final VerticalScrollBar verticalScrollBar = new VerticalScrollBar(null);

    /**
     * The main content widget that is scrolled.
     */
    @Nonnull
    private final IWidget contentWidget;

    /**
     * Constructor.
     *
     * @param name the name of the pane
     * @param contentWidget the widget that will be scrolled
     */
    public ScrollPane(@Nullable String name, @Nonnull IWidget contentWidget) {
        super(name, true, true);
        this.contentWidget = contentWidget;

        addWidget(verticalScrollBar);
        addWidget(contentWidget);

        verticalScrollBar.addValueListener(value -> updateLayout());

        getMouseHandler().addMouseListener(new ScrollMouseListener());
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull IGlResourceManager glResourceManager) { }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) { }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) { }

    @Override
    protected void updateLayout() {
        int scrollBarWidth = verticalScrollBar.getPreferredSize().getX();
        int contentWidth = getSize().getX() - scrollBarWidth;
        int contentHeight = contentWidget.getPreferredSize().getY();
        int height = getSize().getY();

        contentWidget.setPosition(new Vec2i(0, (int) -verticalScrollBar.getValue()));
        contentWidget.setSize(new Vec2i(contentWidth, contentHeight));

        verticalScrollBar.setMin(0);
        verticalScrollBar.setMax(contentHeight - height);
        verticalScrollBar.setPosition(new Vec2i(contentWidth, 0));
        verticalScrollBar.setSize(new Vec2i(scrollBarWidth, height));
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        int scrollBarWidth = verticalScrollBar.getPreferredSize().getX();
        Vec2i contentSize = contentWidget.getPreferredSize();

        return new Vec2i(contentSize.getX() + scrollBarWidth, contentSize.getY());
    }

    /**
     * # Scroll Mouse Listener
     *
     * The listener is on the entire panel to hook the mouse wheel up to scroll the content.
     */
    private class ScrollMouseListener extends MouseListenerAdapter {

        /**
         * The speed in pixels per wheel click.
         */
        private static final int SPEED = 5;

        @Override
        public void mouseWheelMoved(float wheel) {
            verticalScrollBar.scroll(SPEED * wheel);
        }
    }
}

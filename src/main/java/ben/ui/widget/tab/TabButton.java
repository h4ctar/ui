package ben.ui.widget.tab;

import ben.ui.action.IAction;
import ben.ui.input.mouse.MouseButton;
import ben.ui.input.mouse.MouseListenerAdapter;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.math.Vec2i;
import ben.ui.renderer.FlatRenderer;
import ben.ui.renderer.TextRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.widget.AbstractWidget;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Tab Button.
 */
public final class TabButton extends AbstractWidget {

    /**
     * The padding around the button.
     */
    private static final int PADDING = 5;

    /**
     * The height of the tab.
     */
    private static final int HEIGHT = TextRenderer.CHARACTER_SIZE + 2 * PADDING;

    /**
     * The background colour of the tab.
     */
    @Nonnull
    private static final Color BACKGROUND_COLOR = new Color(0.235f, 0.247f, 0.254f);

    /**
     * The background colour of the button.
     */
    @Nonnull
    private static final Color SELECTED_BACKGROUND_COLOR = new Color(0.31f, 0.34f, 0.35f);

    /**
     * The text colour.
     */
    @Nonnull
    private static final Color TEXT_COLOR = new Color(0.73f, 0.73f, 0.73f);

    /**
     * The text.
     */
    @Nonnull
    private final String text;

    /**
     * The background renderer.
     */
    @Nullable
    private FlatRenderer backgroundRenderer;

    /**
     * The text renderer.
     */
    @Nullable
    private TextRenderer textRenderer;

    /**
     * True if this tab is the selected tab.
     */
    private boolean selected;

    /**
     * Constructor.
     * @param name the name of the button
     * @param text the text
     */
    public TabButton(@Nullable String name, @Nonnull String text) {
        super(name);
        this.text = text;
        getMouseHandler().addMouseListener(new MouseListener());
        setSize(getPreferredSize());
    }

    @Override
    public String toString() {
        return TabButton.class.getSimpleName() + "[text: '" + text + "']";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) {
        backgroundRenderer = new FlatRenderer(gl, glResourceManager, getRect(), BACKGROUND_COLOR);
        textRenderer = new TextRenderer(gl, glResourceManager, text, new Vec2i(PADDING, PADDING), TEXT_COLOR);
    }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) {
        assert backgroundRenderer != null : "Update draw should not be called before init draw";
        assert textRenderer != null : "Update draw should not be called before init draw";

        backgroundRenderer.setRect(gl, getRect());
        textRenderer.setText(gl, text);
    }

    /**
     * Get the position and size of the button.
     * @return the rectangle
     */
    private Rect getRect() {
        Vec2i borderPos = new Vec2i(0, 0);
        Vec2i borderSize = getSize();
        return new Rect(borderPos, borderSize);
    }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        assert backgroundRenderer != null : "Draw should not be called before init draw";
        assert textRenderer != null : "Draw should not be called before init draw";

        backgroundRenderer.setColor(selected ? SELECTED_BACKGROUND_COLOR : BACKGROUND_COLOR);
        backgroundRenderer.draw(gl, pmvMatrix);
        textRenderer.draw(gl, pmvMatrix);
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        int width = text.length() * TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        return new Vec2i(width, HEIGHT);
    }

    @Override
    protected void preRemove(@Nonnull GL2 gl) {
        if (backgroundRenderer != null) {
            backgroundRenderer.remove(gl);
        }
        if (textRenderer != null) {
            textRenderer.remove(gl);
        }
    }

    /**
     * Set if this tab is selected.
     * @param selected true to select
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * The Mouse Listener.
     *
     * Executes the action when the button is clicked.
     */
    private class MouseListener extends MouseListenerAdapter {

        @Override
        public void mouseClicked(@Nonnull MouseButton button) {
            IAction action = getAction();
            if (action != null) {
                action.execute();
            }
        }
    }
}

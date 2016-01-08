package ben.ui.widget;

import ben.ui.action.IAction;
import ben.ui.input.mouse.MouseButton;
import ben.ui.input.mouse.MouseListenerAdapter;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.renderer.TextRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Menu Item.
 */
public final class MenuItem extends AbstractWidget {

    /**
     * The padding around the menu item.
     */
    private static final int PADDING = 5;

    /**
     * The height of the menu item.
     */
    private static final int HEIGHT = TextRenderer.CHARACTER_SIZE + 2 * PADDING;

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
     * The text renderer.
     */
    @Nullable
    private TextRenderer textRenderer;

    /**
     * Constructor.
     * @param name the name of the button
     * @param text the text
     */
    public MenuItem(@Nullable String name, @Nonnull String text) {
        super(name);
        this.text = text;
        getMouseHandler().addMouseListener(new MouseListener());
        setSize(getPreferredSize());
    }

    @Override
    public String toString() {
        return "Button[text: '" + text + "']";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) {
        textRenderer = new TextRenderer(gl, glResourceManager, text, new Vec2i(PADDING, PADDING), TEXT_COLOR);
    }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) {
        assert textRenderer != null : "Update draw should not be called before init draw";
        textRenderer.setText(gl, text);
    }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        assert textRenderer != null : "Draw should not be called before init draw";
        textRenderer.draw(gl, pmvMatrix);
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        int width = text.length() * TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        return new Vec2i(width, HEIGHT);
    }

    @Override
    public void remove(@Nonnull GL2 gl) {
        if (textRenderer != null) {
            textRenderer.remove(gl);
        }
        super.remove(gl);
    }

    /**
     * The Mouse Listener.
     * <p>
     *     Executes the action when the button is clicked.
     * </p>
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

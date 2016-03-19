package ben.ui.widget.window;

import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.math.Vec2i;
import ben.ui.renderer.FlatRenderer;
import ben.ui.renderer.TextRenderer;
import ben.ui.resource.IGlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.widget.AbstractWidget;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * # Title Bar
 */
public final class TitleBar extends AbstractWidget {

    /**
     * The padding around the button.
     */
    private static final int PADDING = 5;

    /**
     * The height of the button.
     */
    private static final int HEIGHT = TextRenderer.CHARACTER_SIZE + 2 * PADDING;

    /**
     * The background colour of the button.
     */
    @Nonnull
    private static final Color BACKGROUND_COLOR = new Color(0.31f, 0.34f, 0.35f);

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
     * Constructor.
     *
     * @param name the name of the button
     * @param text the text
     */
    public TitleBar(@Nullable String name, @Nonnull String text) {
        super(name);
        this.text = text;
    }

    @Override
    public String toString() {
        return "Button[text: '" + text + "']";
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull IGlResourceManager glResourceManager) {
        backgroundRenderer = new FlatRenderer(gl, glResourceManager, getBgRect(), BACKGROUND_COLOR);
        textRenderer = new TextRenderer(gl, glResourceManager, text, new Vec2i(PADDING, PADDING), TEXT_COLOR);
    }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) {
        assert backgroundRenderer != null : "Update draw should not be called before init draw";
        assert textRenderer != null : "Update draw should not be called before init draw";
        backgroundRenderer.setRect(gl, getBgRect());
        textRenderer.setText(gl, text);
    }

    /**
     * Get the background position and size.
     *
     * @return the rectangle
     */
    private Rect getBgRect() {
        Vec2i bgPos = new Vec2i(0, 0);
        Vec2i bgSize = getSize();
        return new Rect(bgPos, bgSize);
    }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        assert backgroundRenderer != null : "Draw should not be called before init draw";
        assert textRenderer != null : "Draw should not be called before init draw";

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
}

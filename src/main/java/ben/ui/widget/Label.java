package ben.ui.widget;

import com.jogamp.opengl.GL2;

import ben.ui.math.PmvMatrix;
import ben.ui.renderer.TextRenderer;
import ben.ui.resource.IGlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.math.Vec2i;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * # Label
 *
 * Renders text with no background.
 */
public final class Label extends AbstractWidget {

    /**
     * The padding around the text in pixels.
     */
    private static final int PADDING = 5;

    /**
     * The colour of the text.
     */
    @Nonnull
    private static final Color TEXT_COLOR = new Color(0.73f, 0.73f, 0.73f);

    /**
     * The text.
     */
    @Nonnull
    private String text;

    /**
     * The text renderer.
     */
    @Nullable
    private TextRenderer textRenderer;

    /**
     * Constructor.
     *
     * @param name the name of the label
     * @param text the text and the name of the label
     */
    public Label(@Nullable String name, @Nonnull String text) {
        super(name);
        this.text = text;
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull IGlResourceManager glResourceManager) {
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

    /**
     * Set the text of the label.
     *
     * @param textTmp the new text
     */
    public void setText(@Nonnull String textTmp) {
        text = textTmp;
        setDirty();
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        int width = text.length() * TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        int height = TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        return new Vec2i(width, height);
    }

    @Override
    protected void preRemove(@Nonnull GL2 gl) {
        if (textRenderer != null) {
            textRenderer.remove(gl);
        }
    }
}

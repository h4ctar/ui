package ben.ui.widget;

import com.jogamp.opengl.GL3;

import ben.math.PmvMatrix;
import ben.ui.renderer.TextRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.math.Vec2i;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Label widget.
 * <p>
 *     Renders text with no background.
 * </p>
 */
@ThreadSafe
public final class Label extends AbstractWidget {

    /**
     * The padding around the text in pixels.
     */
    private static final int PADDING = 5;

    /**
     * The colour of the text.
     */
    @NotNull
    private static final Color TEXT_COLOR = new Color(0.73f, 0.73f, 0.73f);

    /**
     * The text.
     */
    @NotNull
    private String text;

    /**
     * The text renderer.
     */
    @Nullable
    private TextRenderer textRenderer;

    /**
     * Constructor.
     * @param name the name of the label
     * @param text the text and the name of the label
     */
    public Label(@Nullable String name, @NotNull String text) {
        super(name);
        this.text = text;
        updateSize();
    }

    @Override
    protected final void initDraw(@NotNull GL3 gl, @NotNull GlResourceManager glResourceManager) {
        textRenderer = new TextRenderer(gl, glResourceManager, text, new Vec2i(PADDING, PADDING), TEXT_COLOR);
    }

    @Override
    protected final void updateDraw(@NotNull GL3 gl) {
        assert textRenderer != null : "Update draw should not be called before init draw";
        textRenderer.setText(gl, text);
    }

    @Override
    protected final void doDraw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix) {
        assert textRenderer != null : "Draw should not be called before init draw";
        textRenderer.draw(gl, pmvMatrix);
    }

    /**
     * Set the text of the label.
     * @param textTmp the new text
     */
    public final void setText(@NotNull String textTmp) {
        text = textTmp;
        updateSize();
        setDirty();
    }

    @Override
    public void updateSize() {
        int width = text.length() * TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        int height = TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        setSize(new Vec2i(width, height));
    }

    @Override
    public void remove(@NotNull GL3 gl) {
        if (textRenderer != null) {
            textRenderer.remove(gl);
        }
        super.remove(gl);
    }
}

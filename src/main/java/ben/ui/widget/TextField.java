package ben.ui.widget;

import com.jogamp.opengl.GL2;

import ben.ui.converter.IValueConverter;
import ben.ui.input.key.IKeyListener;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.renderer.FlatRenderer;
import ben.ui.renderer.TextRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;

import ben.ui.math.Vec2i;
import com.jogamp.newt.event.KeyEvent;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * The Text Field Widget.
 * @param <V> the type of the value
 */
@ThreadSafe
public final class TextField<V> extends AbstractWidget {

    /**
     * The padding around the text in pixels.
     */
    private static final int PADDING = 5;

    /**
     * The border size in pixels.
     */
    private static final int BORDER = 1;

    /**
     * The default length of the field.
     */
    private static final int DEFAULT_LENGTH = 20;

    /**
     * The background colour of the text field when it has an invalid value.
     */
    @NotNull
    private static final Color INVALID_BACKGROUND_COLOR = new Color(0.4f, 0.3f, 0.3f);

    /**
     * The background colour of the text field when it has a valid value.
     */
    @NotNull
    private static final Color VALID_BACKGROUND_COLOR = new Color(0.3f, 0.4f, 0.3f);

    /**
     * The text colour of the text field.
     */
    @NotNull
    private static final Color TEXT_COLOR = new Color(0.73f, 0.73f, 0.73f);

    /**
     * The border colour of the text field.
     */
    @NotNull
    private static final Color UNFOCUS_BORDER_COLOR = new Color(0.5f, 0.5f, 0.5f);

    /**
     * The border colour of the text field.
     */
    @NotNull
    private static final Color FOCUS_BORDER_COLOR = new Color(0.42f, 0.65f, 0.87f);

    /**
     * The value converter.
     */
    @NotNull
    private final IValueConverter<V> valueConverter;

    /**
     * The value listeners that will be notified when the value changes.
     */
    @NotNull
    private final Set<IValueListener<V>> valueListeners = new HashSet<>();

    /**
     * The text.
     */
    @NotNull
    private String text = "";

    /**
     * The value.
     */
    @Nullable
    private V value;

    /**
     * The background renderer.
     */
    private FlatRenderer backgroundRenderer;

    /**
     * The border renderer.
     */
    private FlatRenderer borderRenderer;

    /**
     * The cursor renderer.
     */
    private FlatRenderer cursorRenderer;

    /**
     * The text renderer.
     */
    private TextRenderer textRenderer;

    /**
     * The position of the cursor, number of characters from the start of the text.
     */
    private int cursor = 0;

    /**
     * The offset where the text will start being drawn from in characters.
     * <p>
     *     Used if the text length is too big for the field.
     * </p>
     */
    private int textOffset = 0;

    /**
     * The length of the field.
     */
    private int length = DEFAULT_LENGTH;

    /**
     * Constructor.
     * @param name the name of the button
     * @param valueConverter the value converter
     */
    public TextField(@NotNull String name, @NotNull IValueConverter<V> valueConverter) {
        super(name);
        this.valueConverter = valueConverter;
        updateSize();
        getKeyHandler().addKeyListener(new TextFieldKeyListener());
    }

    @Override
    protected void initDraw(@NotNull GL2 gl, @NotNull GlResourceManager glResourceManager) {
        backgroundRenderer = new FlatRenderer(gl, glResourceManager, getBgRect(), INVALID_BACKGROUND_COLOR);
        borderRenderer = new FlatRenderer(gl, glResourceManager, getBorderRect(), UNFOCUS_BORDER_COLOR);
        cursorRenderer = new FlatRenderer(gl, glResourceManager, getCursorRect(), TEXT_COLOR);
        textRenderer = new TextRenderer(gl, glResourceManager, text, new Vec2i(PADDING, PADDING), TEXT_COLOR);
    }

    @Override
    protected void updateDraw(@NotNull GL2 gl) {
        backgroundRenderer.setRect(gl, getBgRect());
        backgroundRenderer.setColor(value == null ? INVALID_BACKGROUND_COLOR : VALID_BACKGROUND_COLOR);

        borderRenderer.setRect(gl, getBorderRect());
        borderRenderer.setColor(isFocused() ? FOCUS_BORDER_COLOR : UNFOCUS_BORDER_COLOR);

        cursorRenderer.setRect(gl, getCursorRect());

        String displayedText = text.substring(textOffset, text.length());
        if (displayedText.length() > length) {
            displayedText = displayedText.substring(0, length);
        }
        textRenderer.setText(gl, displayedText);
    }

    /**
     * Get the background position and size.
     * @return the rectangle
     */
    private Rect getBgRect() {
        Vec2i bgPos = new Vec2i(BORDER, BORDER);
        Vec2i bgSize = getSize().sub(new Vec2i(2 * BORDER, 2 * BORDER));
        return new Rect(bgPos, bgSize);
    }

    /**
     * Get the border position and size.
     * @return the rectangle
     */
    private Rect getBorderRect() {
        Vec2i borderPos = new Vec2i(0, 0);
        Vec2i borderSize = getSize();
        return new Rect(borderPos, borderSize);
    }

    /**
     * Get the cursor position and size.
     * @return the rectangle
     */
    private Rect getCursorRect() {
        Vec2i cursorPos = new Vec2i(PADDING + (cursor - textOffset) * TextRenderer.CHARACTER_SIZE - 2, PADDING - 1);
        Vec2i cursorSize = new Vec2i(1, TextRenderer.CHARACTER_SIZE + 1);
        return new Rect(cursorPos, cursorSize);
    }

    @Override
    protected void doDraw(@NotNull GL2 gl, @NotNull PmvMatrix pmvMatrix) {
        borderRenderer.draw(gl, pmvMatrix);
        backgroundRenderer.draw(gl, pmvMatrix);
        textRenderer.draw(gl, pmvMatrix);
        if (isFocused()) {
            cursorRenderer.draw(gl, pmvMatrix);
        }
    }

    @Override
    public void updateSize() {
        int width = length * TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        int height = TextRenderer.CHARACTER_SIZE + 2 * PADDING;
        setSize(new Vec2i(width, height));
    }

    /**
     * Add a value listener.
     * @param valueListener the value listener to add
     */
    public void addValueListener(@NotNull IValueListener<V> valueListener) {
        assert !valueListeners.contains(valueListener);
        valueListeners.add(valueListener);
        valueListener.valueChanged(value);
    }

    /**
     * Remove a value listener.
     * @param valueListener the value listener to remove
     */
    public void removeValueListener(@NotNull IValueListener<V> valueListener) {
        assert valueListeners.contains(valueListener);
        valueListeners.remove(valueListener);
    }

    /**
     * Set the text and update the value.
     * @param text the text to set
     */
    public void setText(@NotNull String text) {
        if (!this.text.equals(text)) {
            this.text = text;
            V value = valueConverter.convertTo(text);
            if ((value == null && this.value != null) || (value != null && !value.equals(this.value))) {
                this.value = value;
                notifyValueChanged();
            }
            setDirty();
        }
    }

    /**
     * Set the length of the field.
     * @param length the length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Set the value.
     * @param value the new value
     */
    public void setValue(@NotNull V value) {
        if (!value.equals(this.value)) {
            this.value = value;
            this.text = valueConverter.convertFrom(value);
            notifyValueChanged();
            setDirty();
        }
    }

    /**
     * Get the value of the text field.
     * @return the value
     */
    @Nullable
    public V getValue() {
        return value;
    }

    /**
     * Notify listeners that the value has changed.
     */
    private void notifyValueChanged() {
        for (IValueListener<V> valueListener : valueListeners) {
            valueListener.valueChanged(value);
        }
    }

    @Override
    public void remove(@NotNull GL2 gl) {
        super.remove(gl);
        backgroundRenderer.remove(gl);
        borderRenderer.remove(gl);
        cursorRenderer.remove(gl);
        textRenderer.remove(gl);
    }

    /**
     * The key listener.
     * <p>
     *     Modifies the text in the field according to the key presses.
     * </p>
     */
    private class TextFieldKeyListener implements IKeyListener {

        @Override
        public void keyPressed(@NotNull KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    // Move cursor to the left if it's not at the start of the text.
                    if (cursor > 0) {
                        cursor--;
                    }
                    // Move the text if the cursor is at the start of the displayed text.
                    if (cursor < textOffset) {
                        textOffset--;
                    }
                    // Need to manually set dirty as set text is not called.
                    setDirty();
                    break;

                case KeyEvent.VK_RIGHT:
                    // Move the cursor to the right if it's not at the end of the text.
                    if (cursor < text.length()) {
                        cursor++;
                    }
                    // Move the text if the cursor is at the end of the displayed text.
                    if (cursor - textOffset > length) {
                        textOffset++;
                    }
                    // Need to manually set dirty as set text is not called.
                    setDirty();
                    break;

                case KeyEvent.VK_HOME:
                    // Move cursor to the start of the text.
                    cursor = 0;
                    // Move the displayed text so that its start is at the start.
                    textOffset = 0;
                    // Need to manually set dirty as set text is not called.
                    setDirty();
                    break;

                case KeyEvent.VK_END:
                    // Move cursor to the end of the text.
                    cursor = text.length();
                    textOffset = Math.max(text.length() - length, 0);
                    // Need to manually set dirty as set text is not called.
                    setDirty();
                    break;

                case KeyEvent.VK_BACK_SPACE:
                    if (text.length() > 0 && cursor > 0) {
                        // Remove the character before the cursor.
                        setText(text.substring(0, cursor - 1) + text.substring(cursor, text.length()));
                        // Move the cursor left.
                        cursor--;
                        // Move the displayed text if it's not already at the start.
                        if (textOffset > 0) {
                            textOffset--;
                        }
                    }
                    break;

                case KeyEvent.VK_DELETE:
                    if (cursor < text.length()) {
                        // Remove the character after the cursor.
                        setText(text.substring(0, cursor) + text.substring(cursor + 1, text.length()));
                    }
                    break;

                default:
                    if (e.isPrintableKey() && e.getKeyCode() != KeyEvent.VK_ENTER) {
                        // Add the new character at the cursor position.
                        setText(text.substring(0, cursor) + e.getKeyChar() + text.substring(cursor, text.length()));
                        cursor++;
                        if (cursor + textOffset > length) {
                            textOffset++;
                        }
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(@NotNull KeyEvent e) { }
    }
}

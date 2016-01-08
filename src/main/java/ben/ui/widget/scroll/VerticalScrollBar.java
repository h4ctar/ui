package ben.ui.widget.scroll;

import ben.ui.input.mouse.MouseButton;
import ben.ui.input.mouse.MouseListenerAdapter;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.renderer.FlatRenderer;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.widget.AbstractPane;
import ben.ui.widget.Button;
import ben.ui.widget.IValueListener;
import com.jogamp.opengl.GL2;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Vertical Scroll Bar.
 *
 * <pre>
 * +-+
 * | |
 * +-+
 * |#|
 * |#|
 * +-+
 * | |
 * | |
 * +-+
 * </pre>
 */
public final class VerticalScrollBar extends AbstractPane {

    /**
     * The background colour of the pane.
     */
    @Nonnull
    private static final Color BACKGROUND_COLOR = new Color(0.235f, 0.247f, 0.254f);

    /**
     * The bar on the track.
     */
    @Nonnull
    private final Button bar = new Button(null, "");

    /**
     * The value listeners that will be notified when the value changes.
     */
    @Nonnull
    private final Set<IValueListener<Float>> valueListeners = new HashSet<>();

    /**
     * The background renderer.
     */
    @Nullable
    private FlatRenderer backgroundRenderer;

    /**
     * The minimum value.
     */
    private float min = 0;

    /**
     * The maximum value.
     */
    private float max = 100;

    /**
     * The current value.
     */
    private float value = 0;

    /**
     * The size of the scroll bar (not pixels, but whatever unit the other values are in).
     */
    private float size = 40;

    /**
     * Constructor.
     * @param name the name of the pane
     */
    public VerticalScrollBar(@Nullable String name) {
        super(name, true);
        bar.getMouseHandler().addMouseListener(new BarMouseListener());
        addWidget(bar);
    }

    @Override
    protected void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager) {
        backgroundRenderer = new FlatRenderer(gl, glResourceManager, getRect(), BACKGROUND_COLOR);
    }

    @Override
    protected void updateDraw(@Nonnull GL2 gl) {
        assert backgroundRenderer != null : "Update draw should not be called before init draw";
        backgroundRenderer.setRect(gl, getRect());
    }

    @Override
    protected void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        assert backgroundRenderer != null : "Draw should not be called before init draw";
        backgroundRenderer.draw(gl, pmvMatrix);
    }

    @Override
    protected void updateLayout() {
        int trackHeight = getSize().getY();
        int barTop = Math.round(value * trackHeight / (max - min + size));
        int barHeight = Math.round(size * trackHeight / (max - min + size));

        bar.setPosition(new Vec2i(0, barTop));
        bar.setSize(new Vec2i(getSize().getX(), barHeight));
    }

    @Nonnull
    @Override
    public Vec2i getPreferredSize() {
        return bar.getPreferredSize();
    }

    @Override
    public void remove(@Nonnull GL2 gl) {
        if (backgroundRenderer != null) {
            backgroundRenderer.remove(gl);
        }
        super.remove(gl);
    }

    /**
     * Set the minumum value.
     * @param min the minimum value
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Set the maximum value.
     * @param max the maximum value
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Get the current value.
     * @return the current value
     */
    public float getValue() {
        return value;
    }

    /**
     * Add a value listener.
     * @param valueListener the value listener to add
     */
    public void addValueListener(@Nonnull IValueListener<Float> valueListener) {
        assert !valueListeners.contains(valueListener);
        valueListeners.add(valueListener);
        valueListener.valueChanged(value);
    }

    /**
     * Remove a value listener.
     * @param valueListener the value listener to remove
     */
    public void removeValueListener(@Nonnull IValueListener<Float> valueListener) {
        assert valueListeners.contains(valueListener);
        valueListeners.remove(valueListener);
    }

    /**
     * Notify listeners that the value has changed.
     */
    private void notifyValueChanged() {
        for (IValueListener<Float> valueListener : valueListeners) {
            valueListener.valueChanged(value);
        }
    }

    /**
     * Scroll the scroll bar my a screen distance.
     * @param distance the screen distance
     */
    public void scroll(float distance) {
        int trackHeight = getSize().getY();

        value += distance * (max - min + size) / trackHeight;
        value = Math.max(min, Math.min(max, value));

        updateLayout();

        notifyValueChanged();
    }

    /**
     * Bar Mouse Listener.
     *
     * Listener that is added to the bar that allows the bar to be dragged.
     */
    private class BarMouseListener extends MouseListenerAdapter {

        /**
         * The mouse position when the mouse was pressed.
         */
        private int startMouse;

        @Override
        public void mousePressed(@Nonnull MouseButton button, @Nonnull Vec2i pos) {
            startMouse = pos.getY();
        }

        @Override
        public void mouseDragged(@Nonnull Vec2i mousePos) {
            int barTop = bar.getPosition().getY() + mousePos.getY() - startMouse;
            int trackHeight = getSize().getY();

            value = barTop * (max - min + size) / trackHeight;
            value = Math.max(min, Math.min(max, value));

            updateLayout();

            notifyValueChanged();
        }
    }
}

package ben.ui.widget;

import ben.ui.input.key.IKeyHandler;
import ben.ui.input.mouse.IMouseHandler;
import ben.math.PmvMatrix;
import ben.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL3;
import org.jetbrains.annotations.Nullable;

/**
 * Widget Interface.
 */
@ThreadSafe
public interface IWidget {

    /**
     * Get the name of the widget.
     * @return the name of the widget
     */
    @Nullable
    String getName();

    /**
     * Draw the widget.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix
     * @param glResourceManager the OpenGL resource manager
     */
    void draw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix, @NotNull GlResourceManager glResourceManager);

    /**
     * Set the position of the widget
     * @param position the new position
     */
    void setPosition(@NotNull Vec2i position);

    /**
     * Get the position of the widget.
     * @return the position
     */
    @NotNull
    Vec2i getPosition();

    /**
     * Set the size of the widget
     * @param size the size of the widget
     */
    void setSize(@NotNull Vec2i size);

    /**
     * Get the size of the widget
     * @return the size of the widget
     */
    @NotNull
    Vec2i getSize();

    /**
     * Update the size of the widget.
     * The widget can update its size to its preferred size.
     * This is usually called before the widget is layed out.
     */
    void updateSize();

    /**
     * Set if the widget is focused or not.
     * @param focused true if its focused
     */
    void setFocused(boolean focused);

    /**
     * Is the widget visible.
     * @return true if it's visible
     */
    boolean isVisible();

    /**
     * Get the mouse handler of the widget.
     * @return the mouse handler
     */
    @NotNull
    IMouseHandler getMouseHandler();

    /**
     * Get the key handler of the widget.
     * @return the key handler
     */
    @NotNull
    IKeyHandler getKeyHandler();

    /**
     * Does the widget contain the point.
     * @param pos the point to check
     * @return true if the point is inside the widget
     */
    boolean contains(@NotNull Vec2i pos);

    /**
     * Remove the widget.
     * @param gl the OpenGL interface
     */
    void remove(@NotNull GL3 gl);
}

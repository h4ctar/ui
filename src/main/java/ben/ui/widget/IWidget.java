package ben.ui.widget;

import ben.ui.input.key.IKeyHandler;
import ben.ui.input.mouse.IMouseHandler;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import javax.annotation.Nonnull;

import com.jogamp.opengl.GL2;
import javax.annotation.Nullable;

/**
 * Widget Interface.
 */
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
    void draw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix, @Nonnull GlResourceManager glResourceManager);

    /**
     * Set the position of the widget.
     * @param position the new position
     */
    void setPosition(@Nonnull Vec2i position);

    /**
     * Get the position of the widget.
     * @return the position
     */
    @Nonnull
    Vec2i getPosition();

    /**
     * Set the size of the widget.
     * @param size the size of the widget
     */
    void setSize(@Nonnull Vec2i size);

    /**
     * Get the size of the widget.
     * @return the size of the widget
     */
    @Nonnull
    Vec2i getSize();

    /**
     * Get the preferred size of the widget.
     * @return the preferred size
     */
    @Nonnull
    Vec2i getPreferredSize();

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
    @Nonnull
    IMouseHandler getMouseHandler();

    /**
     * Get the key handler of the widget.
     * @return the key handler
     */
    @Nonnull
    IKeyHandler getKeyHandler();

    /**
     * Does the widget contain the point.
     * @param pos the point to check
     * @return true if the point is inside the widget
     */
    boolean contains(@Nonnull Vec2i pos);

    /**
     * Remove the widget.
     * @param gl the OpenGL interface
     */
    void remove(@Nonnull GL2 gl);
}

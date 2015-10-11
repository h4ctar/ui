package ben.ui.math;

import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;import java.lang.Override;import java.lang.String;

/**
 * Rectangle.
 */
@Immutable
public class Rect {

    /**
     * The position of the rectangle (top left).
     */
    @NotNull
    private final Vec2i pos;

    /**
     * The size of the rectangle.
     */
    @NotNull
    private final Vec2i size;

    /**
     * Constructor.
     * @param pos the position of the rectangle (top left)
     * @param size the size of the rectangle
     */
    public Rect(@NotNull Vec2i pos, @NotNull Vec2i size) {
        this.pos = pos;
        this.size = size;
    }

    /**
     * Constructor.
     * @param x the x position of the rectangle (left)
     * @param y the y position of the rectangle (top)
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public Rect(int x, int y, int width, int height) {
        pos = new Vec2i(x, y);
        size = new Vec2i(width, height);
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + pos + ", " + size + ")";
    }

    /**
     * Get the x position of the rectangle (left).
     * @return the x position of the rectangle
     */
    public int getX() {
        return pos.getX();
    }

    /**
     * Get the y position of the rectangle (top).
     * @return the y position of the rectangle
     */
    public int getY() {
        return pos.getY();
    }

    /**
     * Get the width of the rectangle.
     * @return the width of the rectangle
     */
    public int getWidth() {
        return size.getX();
    }

    /**
     * Get the height of the rectangle.
     * @return the height of the rectangle
     */
    public int getHeight() {
        return size.getY();
    }
}

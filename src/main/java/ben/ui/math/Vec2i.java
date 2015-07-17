package ben.ui.math;

import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

/**
 * Vector 2D.
 */
@Immutable
public final class Vec2i {

    /**
     * The X coordinate.
     */
    private final int x;

    /**
     * The Y coordinate.
     */
    private final int y;

    /**
     * Constructor
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Get the X coordinate.
     * @return the X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the Y coordinate.
     * @return the Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Add a vector to this one.
     * @param v the vector to add
     * @return the result
     */
    public Vec2i add(@NotNull Vec2i v) {
        return new Vec2i(x + v.x, y + v.y);
    }

    /**
     * Subtract a vector from this one.
     * @param v the vector to subtract
     * @return the result
     */
    public Vec2i sub(@NotNull Vec2i v) {
        return new Vec2i(x - v.x, y - v.y);
    }
}

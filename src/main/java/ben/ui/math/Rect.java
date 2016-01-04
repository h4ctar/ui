package ben.ui.math;

import net.jcip.annotations.Immutable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Rectangle.
 */
@Immutable
public final class Rect {

    /**
     * The position of the rectangle (top left).
     */
    @Nonnull
    private final Vec2i pos;

    /**
     * The size of the rectangle.
     */
    @Nonnull
    private final Vec2i size;

    /**
     * Constructor.
     * @param pos the position of the rectangle (top left)
     * @param size the size of the rectangle
     */
    public Rect(@Nonnull Vec2i pos, @Nonnull Vec2i size) {
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

    @Nonnull
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

    /**
     * Does the rectangle contain the point.
     * @param pos the point to check
     * @return true if the point is inside the rectangle
     */
    public boolean contains(@Nonnull Vec2i pos) {
        return pos.getX() >= getX() && pos.getY() >= getY() && pos.getX() <= getX() + getWidth() && pos.getY() <= getY() + getHeight();
    }

    /**
     * Intersect the rectangle with another rectangle.
     * @param other the other rectangle
     * @return the intersection of this rectangle with the other one
     */
    @Nullable
    public Rect intersect(Rect other) {
        Rect intersection;

        int left1 = getX();
        int right1 = getX() + getWidth();
        int top1 = getY();
        int bottom1 = getY() + getHeight();

        int left2 = other.getX();
        int right2 = other.getX() + other.getWidth();
        int top2 = other.getY();
        int bottom2 = other.getY() + other.getHeight();

        if (right1 < left2 || right2 < left1 || bottom1 < top2 || bottom2 < top1) {
            intersection = null;
        }
        else {
            int x = Math.max(left1, left2);
            int width = Math.min(right1, right2) - x;
            int y = Math.max(top1, top2);
            int height = Math.min(bottom1, bottom2) - y;
            intersection = new Rect(x, y, width, height);
        }
        return intersection;
    }
}

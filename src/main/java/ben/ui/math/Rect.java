package ben.ui.math;

import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;import java.lang.Override;import java.lang.String;

/**
 * Rectangle.
 */
@Immutable
public class Rect {

    @NotNull
    private final Vec2i pos;

    @NotNull
    private final Vec2i size;

    public Rect(@NotNull Vec2i pos, @NotNull Vec2i size) {
        this.pos = pos;
        this.size = size;
    }

    public Rect(int x, int y, int width, int height) {
        pos = new Vec2i(x, y);
        size = new Vec2i(width, height);
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + pos + ", " + size + ")";
    }

    public int getX() {
        return pos.getX();
    }

    public int getY() {
        return pos.getY();
    }

    public int getWidth() {
        return size.getX();
    }

    public int getHeight() {
        return size.getY();
    }
}

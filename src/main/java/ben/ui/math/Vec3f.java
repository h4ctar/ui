package ben.ui.math;

import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

/**
 * Vector 3D.
 */
@Immutable
public final class Vec3f {

    /**
     * The X coordinate.
     */
    private final float x;

    /**
     * The Y coordinate.
     */
    private final float y;

    /**
     * The Z coordinate.
     */
    private final float z;

    /**
     * Constructor.
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copy Constructor.
     * @param v the vector to clone
     */
    public Vec3f(Vec4f v) {
        x = v.getX();
        y = v.getY();
        z = v.getZ();
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * Get the X coordinate.
     * @return the coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Get the Y coordinate.
     * @return the coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Get the Z coordinate.
     * @return the coordinate
     */
    public float getZ() {
        return z;
    }

    /**
     * Length.
     * @return the length
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalize.
     * @return the result
     */
    @NotNull
    public Vec3f normalize() {
        return div(length());
    }

    /**
     * Dot product.
     * @param v the other vector
     * @return the result
     */
    public float dot(@NotNull Vec3f v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Multiply.
     * @param s the scalar
     * @return the result
     */
    @NotNull
    public Vec3f mul(float s) {
        return new Vec3f(s * x, s * y, s * z);
    }

    /**
     * Divide.
     * @param s the scalar
     * @return the result
     */
    @NotNull
    public Vec3f div(float s) {
        return new Vec3f(x / s, y / s, z / s);
    }

    /**
     * Add.
     * @param v the other vector
     * @return the result
     */
    @NotNull
    public Vec3f add(@NotNull Vec3f v) {
        return new Vec3f(x + v.x, y + v.y, z + v.z);
    }
}

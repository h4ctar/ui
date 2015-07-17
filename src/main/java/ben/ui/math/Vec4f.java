package ben.ui.math;

import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

/**
 * Vector 3D with W.
 */
@Immutable
public final class Vec4f {

    private final float x;

    private final float y;

    private final float z;

    private final float w;

    public Vec4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4f(@NotNull Vec3f vec, float w) {
        x = vec.getX();
        y = vec.getY();
        z = vec.getZ();
        this.w = w;
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }

    /**
     * Length.
     * @return the result
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    /**
     * Normalize.
     * @return the result
     */
    @NotNull
    public Vec4f normalize() {
        return div(length());
    }

    /**
     * Dot Product.
     * @param v the vector
     * @return the result
     */
    public float dot(@NotNull Vec4f v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    /**
     * Multiply.
     * @param s the scalar
     * @return the result
     */
    @NotNull
    public Vec4f mul(float s) {
        return new Vec4f(s * x, s * y, s * z, s * w);
    }

    /**
     * Divide.
     * @param s the scalar
     * @return the result
     */
    @NotNull
    public Vec4f div(float s) {
        return new Vec4f(x / s, y / s, z / s, w / s);
    }

    /**
     * Add.
     * @param v the vector
     * @return the result
     */
    @NotNull
    public Vec4f add(Vec4f v) {
        return new Vec4f(x + v.x, y + v.y, z + v.z, w + v.w);
    }

    /**
     * Subtract.
     * @param v the vector
     * @return the result
     */
    @NotNull
    public Vec4f sub(Vec4f v) {
        return new Vec4f(x - v.x, y - v.y, z - v.z, w - v.w);
    }
}

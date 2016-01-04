package ben.ui.math;

import net.jcip.annotations.Immutable;
import javax.annotation.Nonnull;

/**
 * Vector 3D with W.
 */
@Immutable
public final class Vec4f {

    /**
     * The X element of the vector.
     */
    private final float x;

    /**
     * The Y element of the vector.
     */
    private final float y;

    /**
     * The Z element of the vector.
     */
    private final float z;

    /**
     * The W element of the vector.
     */
    private final float w;

    /**
     * Constructor.
     * @param x the X element
     * @param y the Y element
     * @param z the Z element
     * @param w the W element
     */
    public Vec4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Constructor from 3D vector and W.
     * @param vec the 3D vector
     * @param w the W element
     */
    public Vec4f(@Nonnull Vec3f vec, float w) {
        x = vec.getX();
        y = vec.getY();
        z = vec.getZ();
        this.w = w;
    }

    @Nonnull
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    /**
     * Get the X element.
     * @return the X element
     */
    public float getX() {
        return x;
    }

    /**
     * Get the Y element.
     * @return the Y element
     */
    public float getY() {
        return y;
    }

    /**
     * Get the Z element.
     * @return the Z element
     */
    public float getZ() {
        return z;
    }

    /**
     * Get the W element.
     * @return the W element
     */
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
    @Nonnull
    public Vec4f normalize() {
        return div(length());
    }

    /**
     * Dot Product.
     * @param v the vector
     * @return the result
     */
    public float dot(@Nonnull Vec4f v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    /**
     * Multiply.
     * @param s the scalar
     * @return the result
     */
    @Nonnull
    public Vec4f mul(float s) {
        return new Vec4f(s * x, s * y, s * z, s * w);
    }

    /**
     * Divide.
     * @param s the scalar
     * @return the result
     */
    @Nonnull
    public Vec4f div(float s) {
        return new Vec4f(x / s, y / s, z / s, w / s);
    }

    /**
     * Add.
     * @param v the vector
     * @return the result
     */
    @Nonnull
    public Vec4f add(Vec4f v) {
        return new Vec4f(x + v.x, y + v.y, z + v.z, w + v.w);
    }

    /**
     * Subtract.
     * @param v the vector
     * @return the result
     */
    @Nonnull
    public Vec4f sub(Vec4f v) {
        return new Vec4f(x - v.x, y - v.y, z - v.z, w - v.w);
    }
}

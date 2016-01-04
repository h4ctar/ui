package ben.ui.math;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.nio.FloatBuffer;

/**
 * 4x4 Matrix.
 */
public final class Matrix {

    /** m00. **/
    private float m00;
    /** m01. **/
    private float m01;
    /** m02. **/
    private float m02;
    /** m03. **/
    private float m03;
    /** m10. **/
    private float m10;
    /** m11. **/
    private float m11;
    /** m12. **/
    private float m12;
    /** m13. **/
    private float m13;
    /** m20. **/
    private float m20;
    /** m21. **/
    private float m21;
    /** m22. **/
    private float m22;
    /** m23. **/
    private float m23;
    /** m30. **/
    private float m30;
    /** m31. **/
    private float m31;
    /** m32. **/
    private float m32;
    /** m33. **/
    private float m33;

    /**
     * Constructor.
     */
    public Matrix() {
        identity();
    }

    /**
     * Copy constructor.
     * @param m the matrix to copy
     */
    public Matrix(@Nonnull Matrix m) {
        m00 = m.m00;
        m01 = m.m01;
        m02 = m.m02;
        m03 = m.m03;
        m10 = m.m10;
        m11 = m.m11;
        m12 = m.m12;
        m13 = m.m13;
        m20 = m.m20;
        m21 = m.m21;
        m22 = m.m22;
        m23 = m.m23;
        m30 = m.m30;
        m31 = m.m31;
        m32 = m.m32;
        m33 = m.m33;
    }

    @Nonnull
    @Override
    public String toString() {
        return "Matrix { " + m00 + ", " + m10 + ", " + m20 + ", " + m30 + ",\n"
                + "         " + m01 + ", " + m11 + ", " + m21 + ", " + m31 + ",\n"
                + "         " + m02 + ", " + m12 + ", " + m22 + ", " + m32 + ",\n"
                + "         " + m03 + ", " + m13 + ", " + m23 + ", " + m33 + " }\n";
    }

    /**
     * Set the matrix to another matrix.
     * @param m the matrix to copy
     */
    private void set(Matrix m) {
        this.m00 = m.m00;
        this.m01 = m.m01;
        this.m02 = m.m02;
        this.m03 = m.m03;
        this.m10 = m.m10;
        this.m11 = m.m11;
        this.m12 = m.m12;
        this.m13 = m.m13;
        this.m20 = m.m20;
        this.m21 = m.m21;
        this.m22 = m.m22;
        this.m23 = m.m23;
        this.m30 = m.m30;
        this.m31 = m.m31;
        this.m32 = m.m32;
        this.m33 = m.m33;
    }

    /**
     * Set all elements to zero.
     */
    public void zero() {
        m00 = 0.0f;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 0.0f;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 0.0f;
        m23 = 0.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 0.0f;
    }

    /**
     * Load the identity matrix.
     */
    public void identity() {
        m00 = 1.0f;
        m01 = 0.0f;
        m02 = 0.0f;
        m03 = 0.0f;
        m10 = 0.0f;
        m11 = 1.0f;
        m12 = 0.0f;
        m13 = 0.0f;
        m20 = 0.0f;
        m21 = 0.0f;
        m22 = 1.0f;
        m23 = 0.0f;
        m30 = 0.0f;
        m31 = 0.0f;
        m32 = 0.0f;
        m33 = 1.0f;
    }

    /**
     * Calculate the determinant.
     * @return the determinant
     */
    public float determinant() {
        return (m00 * m11 - m01 * m10) * (m22 * m33 - m23 * m32) - (m00 * m12 - m02 * m10) * (m21 * m33 - m23 * m31)
                + (m00 * m13 - m03 * m10) * (m21 * m32 - m22 * m31) + (m01 * m12 - m02 * m11) * (m20 * m33 - m23 * m30)
                - (m01 * m13 - m03 * m11) * (m20 * m32 - m22 * m30) + (m02 * m13 - m03 * m12) * (m20 * m31 - m21 * m30);
    }

    /**
     * Multiply two matricies.
     * @param m0 the first matrix
     * @param m1 the second matrix
     * @return the product matrix
     */
    @Nonnull
    public static Matrix mul(@Nonnull Matrix m0, @Nonnull Matrix m1) {
        Matrix result = new Matrix();
        result.m00 = m0.m00 * m1.m00 + m0.m10 * m1.m01 + m0.m20 * m1.m02 + m0.m30 * m1.m03;
        result.m01 = m0.m01 * m1.m00 + m0.m11 * m1.m01 + m0.m21 * m1.m02 + m0.m31 * m1.m03;
        result.m02 = m0.m02 * m1.m00 + m0.m12 * m1.m01 + m0.m22 * m1.m02 + m0.m32 * m1.m03;
        result.m03 = m0.m03 * m1.m00 + m0.m13 * m1.m01 + m0.m23 * m1.m02 + m0.m33 * m1.m03;
        result.m10 = m0.m00 * m1.m10 + m0.m10 * m1.m11 + m0.m20 * m1.m12 + m0.m30 * m1.m13;
        result.m11 = m0.m01 * m1.m10 + m0.m11 * m1.m11 + m0.m21 * m1.m12 + m0.m31 * m1.m13;
        result.m12 = m0.m02 * m1.m10 + m0.m12 * m1.m11 + m0.m22 * m1.m12 + m0.m32 * m1.m13;
        result.m13 = m0.m03 * m1.m10 + m0.m13 * m1.m11 + m0.m23 * m1.m12 + m0.m33 * m1.m13;
        result.m20 = m0.m00 * m1.m20 + m0.m10 * m1.m21 + m0.m20 * m1.m22 + m0.m30 * m1.m23;
        result.m21 = m0.m01 * m1.m20 + m0.m11 * m1.m21 + m0.m21 * m1.m22 + m0.m31 * m1.m23;
        result.m22 = m0.m02 * m1.m20 + m0.m12 * m1.m21 + m0.m22 * m1.m22 + m0.m32 * m1.m23;
        result.m23 = m0.m03 * m1.m20 + m0.m13 * m1.m21 + m0.m23 * m1.m22 + m0.m33 * m1.m23;
        result.m30 = m0.m00 * m1.m30 + m0.m10 * m1.m31 + m0.m20 * m1.m32 + m0.m30 * m1.m33;
        result.m31 = m0.m01 * m1.m30 + m0.m11 * m1.m31 + m0.m21 * m1.m32 + m0.m31 * m1.m33;
        result.m32 = m0.m02 * m1.m30 + m0.m12 * m1.m31 + m0.m22 * m1.m32 + m0.m32 * m1.m33;
        result.m33 = m0.m03 * m1.m30 + m0.m13 * m1.m31 + m0.m23 * m1.m32 + m0.m33 * m1.m33;
        return result;
    }

    /**
     * Multiply a matrix by a vector.
     * @param m the matrix
     * @param v the vector
     * @return the product vector
     */
    @Nonnull
    public static Vec4f mul(@Nonnull Matrix m, @Nonnull Vec4f v) {
        float x = m.m00 * v.getX() + m.m10 * v.getY() + m.m20 * v.getZ() + m.m30 * v.getW();
        float y = m.m01 * v.getX() + m.m11 * v.getY() + m.m21 * v.getZ() + m.m31 * v.getW();
        float z = m.m02 * v.getX() + m.m12 * v.getY() + m.m22 * v.getZ() + m.m32 * v.getW();
        float w = m.m03 * v.getX() + m.m13 * v.getY() + m.m23 * v.getZ() + m.m33 * v.getW();
        return new Vec4f(x, y, z, w);
    }

    /**
     * Inverse the matrix.
     * @return the inverse matrix
     */
    @Nullable
    public Matrix inverse() {
        float s = determinant();
        if (s == 0.0f) {
            return null;
        }
        s = 1.0f / s;

        Matrix result = new Matrix();

        result.m00 = (m11 * (m22 * m33 - m23 * m32) + m12 * (m23 * m31 - m21 * m33) + m13 * (m21 * m32 - m22 * m31)) * s;
        result.m01 = (m21 * (m02 * m33 - m03 * m32) + m22 * (m03 * m31 - m01 * m33) + m23 * (m01 * m32 - m02 * m31)) * s;
        result.m02 = (m31 * (m02 * m13 - m03 * m12) + m32 * (m03 * m11 - m01 * m13) + m33 * (m01 * m12 - m02 * m11)) * s;
        result.m03 = (m01 * (m13 * m22 - m12 * m23) + m02 * (m11 * m23 - m13 * m21) + m03 * (m12 * m21 - m11 * m22)) * s;
        result.m10 = (m12 * (m20 * m33 - m23 * m30) + m13 * (m22 * m30 - m20 * m32) + m10 * (m23 * m32 - m22 * m33)) * s;
        result.m11 = (m22 * (m00 * m33 - m03 * m30) + m23 * (m02 * m30 - m00 * m32) + m20 * (m03 * m32 - m02 * m33)) * s;
        result.m12 = (m32 * (m00 * m13 - m03 * m10) + m33 * (m02 * m10 - m00 * m12) + m30 * (m03 * m12 - m02 * m13)) * s;
        result.m13 = (m02 * (m13 * m20 - m10 * m23) + m03 * (m10 * m22 - m12 * m20) + m00 * (m12 * m23 - m13 * m22)) * s;
        result.m20 = (m13 * (m20 * m31 - m21 * m30) + m10 * (m21 * m33 - m23 * m31) + m11 * (m23 * m30 - m20 * m33)) * s;
        result.m21 = (m23 * (m00 * m31 - m01 * m30) + m20 * (m01 * m33 - m03 * m31) + m21 * (m03 * m30 - m00 * m33)) * s;
        result.m22 = (m33 * (m00 * m11 - m01 * m10) + m30 * (m01 * m13 - m03 * m11) + m31 * (m03 * m10 - m00 * m13)) * s;
        result.m23 = (m03 * (m11 * m20 - m10 * m21) + m00 * (m13 * m21 - m11 * m23) + m01 * (m10 * m23 - m13 * m20)) * s;
        result.m30 = (m10 * (m22 * m31 - m21 * m32) + m11 * (m20 * m32 - m22 * m30) + m12 * (m21 * m30 - m20 * m31)) * s;
        result.m31 = (m20 * (m02 * m31 - m01 * m32) + m21 * (m00 * m32 - m02 * m30) + m22 * (m01 * m30 - m00 * m31)) * s;
        result.m32 = (m30 * (m02 * m11 - m01 * m12) + m31 * (m00 * m12 - m02 * m10) + m32 * (m01 * m10 - m00 * m11)) * s;
        result.m33 = (m00 * (m11 * m22 - m12 * m21) + m01 * (m12 * m20 - m10 * m22) + m02 * (m10 * m21 - m11 * m20)) * s;

        return result;
    }

    /**
     * Load the perspective matrix.
     * @param fovy the field of view
     * @param aspect the aspect ratio
     * @param zNear the near plane
     * @param zFar the far plane
     * @param yScale the y scale
     * @param xScale the x scale
     * @param frustrumLength the frustrum length
     */
    public void perspective(float fovy, float aspect, float zNear, float zFar, float yScale, float xScale, float frustrumLength) {
        zero();
        m00 = xScale;
        m11 = yScale;
        m22 = -((zFar + zNear) / frustrumLength);
        m23 = -1.0f;
        m32 = -((2.0f * zNear * zFar) / frustrumLength);
    }

    /**
     * Load the orthographic matrix.
     * @param left the left plane
     * @param right the right plane
     * @param bottom the bottom plane
     * @param top the top plane
     * @param zNear the near plane
     * @param zFar the far plane
     */
    public void orthographic(float left, float right, float bottom, float top, float zNear, float zFar) {
        identity();
        m00 = 2.0f / (right - left);
        m11 = 2.0f / (top - bottom);
        m22 = -2.0f / (zFar - zNear);
        m30 = -(right + left) / (right - left);
        m31 = -(top + bottom) / (top - bottom);
        m32 = -(zFar + zNear) / (zFar - zNear);
    }

    /**
     * Translate the model view matrix.
     * @param translation the translation
     */
    public void translate(Vec3f translation) {
        Matrix translateMatrix = new Matrix();

        translateMatrix.m30 = translation.getX();
        translateMatrix.m31 = translation.getY();
        translateMatrix.m32 = translation.getZ();

        set(mul(this, translateMatrix));
    }

    /**
     * Rotate the model view matrix around the x axis.
     * @param phi rotation angle around x axis
     */
    public void rotateX(float phi) {
        // http://www.cprogramming.com/tutorial/3d/rotationMatrices.html
        Matrix rotationMatrix = new Matrix();

        rotationMatrix.m11 = (float) Math.cos(phi);
        rotationMatrix.m21 = (float) -Math.sin(phi);
        rotationMatrix.m12 = (float) Math.sin(phi);
        rotationMatrix.m22 = (float) Math.cos(phi);

        set(mul(this, rotationMatrix));
    }

    /**
     * Rotate the model view matrix around the y axis.
     * @param theta rotation angle around y axis
     */
    public void rotateY(float theta) {
        // http://www.cprogramming.com/tutorial/3d/rotationMatrices.html
        Matrix rotationMatrix = new Matrix();

        rotationMatrix.m00 = (float) Math.cos(theta);
        rotationMatrix.m20 = (float) Math.sin(theta);
        rotationMatrix.m02 = (float) -Math.sin(theta);
        rotationMatrix.m22 = (float) Math.cos(theta);

        set(mul(this, rotationMatrix));
    }

    /**
     * Get the matrix in a float buffer.
     * @return the float buffer
     */
    @Nonnull
    public FloatBuffer getBuffer() {
        FloatBuffer buffer = FloatBuffer.allocate(16);
        buffer.put(m00);
        buffer.put(m01);
        buffer.put(m02);
        buffer.put(m03);
        buffer.put(m10);
        buffer.put(m11);
        buffer.put(m12);
        buffer.put(m13);
        buffer.put(m20);
        buffer.put(m21);
        buffer.put(m22);
        buffer.put(m23);
        buffer.put(m30);
        buffer.put(m31);
        buffer.put(m32);
        buffer.put(m33);
        buffer.rewind();
        return buffer;
    }
}

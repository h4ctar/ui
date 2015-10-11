package ben.ui.math;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Math;import java.util.Stack;

/**
 * Perspective Model View Matrix.
 */
public final class PmvMatrix {

    /**
     * The projection matrix.
     */
    private final Matrix pMatrix = new Matrix();

    /**
     * The model view matrix.
     */
    private Matrix mvMatrix = new Matrix();

    /**
     * The matrix stack.
     * For matrix push and pop.
     */
    private final Stack<Matrix> mvMatrixStack = new Stack<>();

    /**
     * The viewport.
     * Used so that widgets can reset the glViewport after they have changed it.
     */
    private Rect viewport;

    /**
     * Set both the projection and model view matrix to identity.
     */
    public void identity() {
        pMatrix.identity();
        mvMatrix.identity();
    }

    /**
     * Set the projection matrix to perspective.
     * @param viewport a rectangle describing the viewport
     * @param zNear the near plane
     * @param zFar the far plane
     */
    public void perspective(@NotNull Rect viewport, float zNear, float zFar) {
        this.viewport = viewport;
        float fovy = 45.0f;
        float aspect = (float) viewport.getWidth() / viewport.getHeight();
        float yScale = (float) (1.0f / Math.tan(fovy * Math.PI / 360.0f));
        float xScale = yScale / aspect;
        float frustrumLength = zFar - zNear;
        pMatrix.perspective(fovy, aspect, zNear, zFar, yScale, xScale, frustrumLength);
    }

    /**
     * Set the projection matrix to orthographic.
     * @param viewport a rectangle describing the viewport
     */
    public void orthographic(@NotNull Rect viewport) {
        this.viewport = viewport;
        float left = 0.0f;
        float right = viewport.getWidth();
        float bottom = viewport.getHeight();
        float top = 0.0f;
        float zNear = -1.0f;
        float zFar = 1.0f;
        pMatrix.orthographic(left, right, bottom, top, zNear, zFar);
    }

    /**
     * Get the perspective matrix.
     * @return the perspective matrix
     */
    @NotNull
    public Matrix getPMatrix() {
        return pMatrix;
    }

    /**
     * Get the model view matrix.
     * @return the model view matrix
     */
    @NotNull
    public Matrix getMvMatrix() {
        return mvMatrix;
    }

    /**
     * Get the Projection Model View Matrix.
     * @return the PMV Matrix
     */
    @NotNull
    public Matrix getPmvMatrix() {
        return Matrix.mul(pMatrix, mvMatrix);
    }

    /**
     * Get the viewport rectangle.
     * @return the viewport
     */
    @NotNull
    public Rect getViewport() {
        return viewport;
    }

    /**
     * Push the current model view matrix onto the stack.
     */
    public void push() {
        mvMatrixStack.push(mvMatrix);
        mvMatrix = new Matrix(mvMatrix);
    }

    /**
     * Pop the top model view matrix off the stack.
     */
    public void pop() {
        mvMatrix = mvMatrixStack.pop();
    }

    /**
     * Translate the model view matrix.
     * @param translation the translation
     */
    public void translate(Vec3f translation) {
        mvMatrix.translate(translation);
    }

    /**
     * Rotate the model view matrix around the x axis.
     * @param phi rotation angle around x axis
     */
    public void rotateX(float phi) {
        mvMatrix.rotateX(phi);
    }

    /**
     * Rotate the model view matrix around the y axis.
     * @param theta rotation angle around y axis
     */
    public void rotateY(float theta) {
        mvMatrix.rotateY(theta);
    }

    /**
     * Project a world point through the matrix to get its screen position.
     * @param world the point to project
     * @return its screen position
     */
    @Nullable
    public Vec2i project(@NotNull Vec3f world) {
        Vec4f vec = Matrix.mul(getPmvMatrix(), new Vec4f(world, 1.0f));

        Vec2i screen = null;
        if (vec.getW() != 0.0f) {
            double z = vec.getZ() / vec.getW();
            if (z < 1) {
                int x = (int) ((vec.getX() / vec.getW() * 0.5 + 0.5) * viewport.getWidth());
                int y = (int) (viewport.getHeight() - (vec.getY() / vec.getW() * 0.5 + 0.5) * viewport.getHeight());
                screen = new Vec2i(x, y);
            }
        }

        return screen;
    }
}

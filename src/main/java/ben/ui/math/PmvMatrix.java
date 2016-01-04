package ben.ui.math;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Stack;

/**
 * Perspective Model View Matrix.
 */
public final class PmvMatrix {

    /**
     * The perspective matrix stack.
     * For push and pop.
     */
    @Nonnull
    private final Stack<Matrix> pMatrixStack = new Stack<>();

    /**
     * The model view matrix stack.
     * For push and pop.
     */
    @Nonnull
    private final Stack<Matrix> mvMatrixStack = new Stack<>();

    /**
     * The scissor box stack.
     * For push and pop.
     */
    @Nonnull
    private final Stack<Rect> scissorBoxStack = new Stack<>();

    /**
     * The projection matrix.
     */
    @Nonnull
    private Matrix pMatrix = new Matrix();

    /**
     * The model view matrix.
     */
    @Nonnull
    private Matrix mvMatrix = new Matrix();

    /**
     * The screen size.
     * Used so that panes can reset the glViewport after they have changed it.
     */
    @Nonnull
    private Vec2i screenSize = new Vec2i(0, 0);

    /**
     * The scissor box.
     * Used so that the panes can intersect their new scissor box with the old one.
     */
    @Nullable
    private Rect scissorBox;

    /**
     * Set both the projection and model view matrix to identity.
     */
    public void identity() {
        pMatrix.identity();
        mvMatrix.identity();
    }

    /**
     * Set the projection matrix to perspective.
     * @param screenSize the size of the screen
     * @param zNear the near plane
     * @param zFar the far plane
     */
    public void perspective(@Nonnull Vec2i screenSize, float zNear, float zFar) {
        this.screenSize = screenSize;
        float fovy = 45.0f;
        float aspect = (float) screenSize.getX() / screenSize.getY();
        float yScale = (float) (1.0f / Math.tan(fovy * Math.PI / 360.0f));
        float xScale = yScale / aspect;
        float frustrumLength = zFar - zNear;
        pMatrix.perspective(fovy, aspect, zNear, zFar, yScale, xScale, frustrumLength);
    }

    /**
     * Set the projection matrix to orthographic.
     * @param viewport a rectangle describing the viewport
     */
    public void orthographic(@Nonnull Rect viewport) {
        float left = viewport.getX();
        float right = viewport.getWidth() + viewport.getX();
        float bottom = viewport.getHeight() + viewport.getY();
        float top = viewport.getY();
        float zNear = -1.0f;
        float zFar = 1.0f;
        pMatrix.orthographic(left, right, bottom, top, zNear, zFar);
    }

    /**
     * Get the perspective matrix.
     * @return the perspective matrix
     */
    @Nonnull
    public Matrix getPMatrix() {
        return pMatrix;
    }

    /**
     * Get the model view matrix.
     * @return the model view matrix
     */
    @Nonnull
    public Matrix getMvMatrix() {
        return mvMatrix;
    }

    /**
     * Get the Projection Model View Matrix.
     * @return the PMV Matrix
     */
    @Nonnull
    public Matrix getPmvMatrix() {
        return Matrix.mul(pMatrix, mvMatrix);
    }

    /**
     * Set the screen size.
     * @param screenSize the screen size
     */
    public void setScreenSize(@Nonnull Vec2i screenSize) {
        this.screenSize = screenSize;
    }

    /**
     * Get the screen size.
     * @return the screen size
     */
    @Nonnull
    public Vec2i getScreenSize() {
        return screenSize;
    }

    /**
     * Push the current model view matrix onto the stack.
     */
    public void push() {
        pMatrixStack.push(pMatrix);
        pMatrix = new Matrix(pMatrix);

        mvMatrixStack.push(mvMatrix);
        mvMatrix = new Matrix(mvMatrix);

        scissorBoxStack.push(scissorBox);
    }

    /**
     * Pop the top model view matrix off the stack.
     */
    public void pop() {
        pMatrix = pMatrixStack.pop();
        mvMatrix = mvMatrixStack.pop();
        scissorBox = scissorBoxStack.pop();
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
    public Vec2i project(@Nonnull Vec3f world) {
        Vec4f vec = Matrix.mul(getPmvMatrix(), new Vec4f(world, 1.0f));

        Vec2i screen = null;
        if (vec.getW() != 0.0f) {
            double z = vec.getZ() / vec.getW();
            if (z < 1) {
                int x = (int) ((vec.getX() / vec.getW() * 0.5 + 0.5) * screenSize.getX());
                int y = (int) (screenSize.getY() - (vec.getY() / vec.getW() * 0.5 + 0.5) * screenSize.getY());
                screen = new Vec2i(x, y);
            }
        }

        return screen;
    }

    /**
     * Set the current scissor box.
     * @param scissorBox the scissor box
     */
    public void setScissorBox(@Nullable Rect scissorBox) {
        this.scissorBox = scissorBox;
    }

    /**
     * Get the current scissor box.
     * @return the scissor box
     */
    @Nullable
    public Rect getScissorBox() {
        return scissorBox;
    }

    /**
     * Scissor the scissor box.
     * Intersects the current scissor box with another scissor box.
     * @param scissorBox the scissor box to scissor the current scissor box with
     * @return the new scissor box
     */
    @Nullable
    public Rect scissor(@Nonnull Rect scissorBox) {
        if (this.scissorBox == null) {
            this.scissorBox = scissorBox;
        }
        else {
            this.scissorBox = this.scissorBox.intersect(scissorBox);
        }

        return this.scissorBox;
    }
}

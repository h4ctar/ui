package ben.ui.renderer;

import ben.math.PmvMatrix;
import ben.math.Rect;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.resource.shader.FlatProgram;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL3;

/**
 * Flat Rectangle Renderer.
 * <p>
 *     Renders a 2D rectangle with a solid colour.
 * </p>
 */
public final class FlatRenderer {

    /**
     * The number of vertices in a rectangle.
     */
    private static final int NUMBER_OF_VERTICES = 4;

    /**
     * The shader program.
     */
    private final FlatProgram program;

    /**
     * The VAO.
     */
    @NotNull
    private final VertexArrayObject vertexArrayObject;

    /**
     * The colour.
     */
    private Color color;

    /**
     * The position buffer of the vertices.
     */
    private final int positionsBuffer;

    /**
     * Constructor.
     * @param gl the OpenGL interface
     * @param glResourceManager the resource manager
     * @param rect the position and size of the rectangle
     * @param color the colour of the rectangle
     */
    public FlatRenderer(@NotNull GL3 gl, @NotNull GlResourceManager glResourceManager, @NotNull Rect rect, @NotNull Color color) {
        program = glResourceManager.getShaderManager().getProgram(FlatProgram.class);
        vertexArrayObject = new VertexArrayObject(gl);
        this.color = color;
        float[] positions = createPositions(rect);
        positionsBuffer = vertexArrayObject.addBuffer(gl, FlatProgram.POSITION_LOCATION, positions, 2);
    }

    /**
     * Draw the rectangle.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix
     */
    public void draw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix) {
        program.use(gl);
        program.setPmvMatrix(gl, pmvMatrix);
        program.setColor(gl, color);
        vertexArrayObject.draw(gl, GL3.GL_TRIANGLE_FAN, NUMBER_OF_VERTICES);
    }

    /**
     * Set the colour of the rectangle.
     * @param color the new colour
     */
    public void setColor(@NotNull Color color) {
        this.color = color;
    }

    /**
     * Set the position and size of the rectangle.
     * @param gl the OpenGL interface
     * @param rect the rectangle
     */
    public void setRect(@NotNull GL3 gl, @NotNull Rect rect) {
        float[] positions = createPositions(rect);
        vertexArrayObject.updateBuffer(gl, positionsBuffer, positions);
    }

    /**
     * Create the positions array of the verticies for the rectangle.
     * @param rect the position and size of the rectangle
     * @return the positions
     */
    @NotNull
    private float[] createPositions(@NotNull Rect rect) {
        return new float[] { rect.getX(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY(),
                rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), rect.getX(), rect.getY() + rect.getHeight() };
    }

    /**
     * Remove the renderers VAO.
     * @param gl the OpenGL interface
     */
    public void remove(@NotNull GL3 gl) {
        vertexArrayObject.remove(gl);
    }
}

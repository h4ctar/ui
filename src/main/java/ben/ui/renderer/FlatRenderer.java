package ben.ui.renderer;

import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.resource.IGlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.resource.shader.FlatProgram;
import javax.annotation.Nonnull;

import com.jogamp.opengl.GL2;

/**
 * Flat Rectangle Renderer.
 *
 * Renders a 2D rectangle with a solid colour.
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
    @Nonnull
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
     *
     * @param gl the OpenGL interface
     * @param glResourceManager the resource manager
     * @param rect the position and size of the rectangle
     * @param color the colour of the rectangle
     */
    public FlatRenderer(@Nonnull GL2 gl, @Nonnull IGlResourceManager glResourceManager, @Nonnull Rect rect, @Nonnull Color color) {
        program = glResourceManager.getShaderManager().getProgram(FlatProgram.class);
        vertexArrayObject = new VertexArrayObject(gl);
        this.color = color;
        float[] positions = createPositions(rect);
        positionsBuffer = vertexArrayObject.addBuffer(gl, FlatProgram.POSITION_LOCATION, positions, 2);
    }

    /**
     * Draw the rectangle.
     *
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix
     */
    public void draw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        program.use(gl);
        program.setPmvMatrix(gl, pmvMatrix);
        program.setColor(gl, color);
        vertexArrayObject.draw(gl, GL2.GL_TRIANGLE_FAN, NUMBER_OF_VERTICES);
    }

    /**
     * Set the colour of the rectangle.
     *
     * @param color the new colour
     */
    public void setColor(@Nonnull Color color) {
        this.color = color;
    }

    /**
     * Set the position and size of the rectangle.
     *
     * @param gl the OpenGL interface
     * @param rect the rectangle
     */
    public void setRect(@Nonnull GL2 gl, @Nonnull Rect rect) {
        float[] positions = createPositions(rect);
        vertexArrayObject.updateBuffer(gl, positionsBuffer, positions);
    }

    /**
     * Create the positions array of the verticies for the rectangle.
     *
     * @param rect the position and size of the rectangle
     * @return the positions
     */
    @Nonnull
    private float[] createPositions(@Nonnull Rect rect) {
        return new float[] {rect.getX(), rect.getY(),
                rect.getX() + rect.getWidth(), rect.getY(),
                rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(),
                rect.getX(), rect.getY() + rect.getHeight()};
    }

    /**
     * Remove the renderers VAO.
     *
     * @param gl the OpenGL interface
     */
    public void remove(@Nonnull GL2 gl) {
        vertexArrayObject.remove(gl);
    }
}

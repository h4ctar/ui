package ben.ui.renderer;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.resource.shader.TextProgram;
import ben.ui.resource.shader.TextureProgram;
import ben.ui.math.Vec2i;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import javax.annotation.Nonnull;

/**
 * The Symbol Renderer.
 * <p>
 *     Renders a Textured 2D Rectangle with a colour.
 * </p>
 */
public final class SymbolRenderer {

    /**
     * The number of vertices in a rectangle.
     */
    private static final int NUMBER_OF_VERTICES = 4;

    /**
     * The shader program.
     */
    @Nonnull
    private final TextProgram program;

    /**
     * The VAO.
     */
    @Nonnull
    private final VertexArrayObject vertexArrayObject;

    /**
     * The texture.
     */
    @Nonnull
    private final Texture texture;

    /**
     * The colour of the symbol.
     */
    @Nonnull
    private Color color;

    /**
     * The position buffer of the vertices.
     */
    private final int positionsBuffer;

    /**
     * Constructor.
     * @param gl the OpenGL interface
     * @param glResourceManager the resource manager
     * @param size the size of the rectangle
     * @param texture the texture of the rectangle
     * @param color the colour of the symbol
     */
    public SymbolRenderer(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager, @Nonnull Vec2i size, @Nonnull Enum<?> texture, @Nonnull Color color) {
        this.color = color;

        program = glResourceManager.getShaderManager().getProgram(TextProgram.class);
        vertexArrayObject = new VertexArrayObject(gl);
        this.texture = glResourceManager.getTextureManager().getTexture(texture);
        float[] positions = createPositions(size);
        positionsBuffer = vertexArrayObject.addBuffer(gl, TextureProgram.POSITION_LOCATION, positions, 2);

        float[] textureCoordinates = createTextureCoordinates();
        vertexArrayObject.addBuffer(gl, TextureProgram.TEXTURE_COORDINATE_LOCATION, textureCoordinates, 2);
    }

    /**
     * Set the colour of the symbol.
     * @param color the colour of the symbol
     */
    public void setColor(@Nonnull Color color) {
        this.color = color;
    }

    /**
     * Draw the rectangle.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix
     */
    public void draw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        program.use(gl);
        program.setPmvMatrix(gl, pmvMatrix);
        program.setTexture(gl, texture);
        program.setColor(gl, color);
        vertexArrayObject.draw(gl, GL2.GL_TRIANGLE_FAN, NUMBER_OF_VERTICES);
    }

    /**
     * Set the size of the rectangle.
     * @param gl the OpenGL interface
     * @param size the size of the rectangle
     */
    public void setSize(@Nonnull GL2 gl, @Nonnull Vec2i size) {
        float[] positions = createPositions(size);
        vertexArrayObject.updateBuffer(gl, positionsBuffer, positions);
    }

    /**
     * Create the positions array of the verticies for the rectangle.
     * @param size the size of the rectangle
     * @return the positions
     */
    @Nonnull
    private float[] createPositions(@Nonnull Vec2i size) {
        return new float[] {0, 0, size.getX(), 0, size.getX(), size.getY(), 0, size.getY()};
    }

    /**
     * Create the texture coordinates array.
     * @return the texture coordinates array
     */
    @Nonnull
    private float[] createTextureCoordinates() {
        return new float[] {0, 1, 1, 1, 1, 0, 0, 0};
    }
}

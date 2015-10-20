package ben.ui.renderer;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.Color;
import ben.ui.resource.shader.TextProgram;
import ben.ui.resource.texture.UiTextures;
import ben.ui.math.Vec2i;
import com.jogamp.opengl.util.texture.Texture;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL2;

/**
 * The text renderer.
 */
public final class TextRenderer {

    /**
     * The size in pixels of a character.
     */
    public static final int CHARACTER_SIZE = 8;

    /**
     * The number of characters in a row of the font sheet.
     */
    private static final int CHARACTERS_PER_ROW = 16;

    /**
     * The number of vertices per character.
     */
    private static final int VERTICES_PER_CHARACTER = 4;

    /**
     * The VAO.
     */
    @NotNull
    private final VertexArrayObject vertexArrayObject;

    /**
     * The positions buffer ID.
     */
    private final int positionsBuffer;

    /**
     * The texture coordinates buffer ID.
     */
    private final int textureCoordinatesBuffer;

    /**
     * The font sheet texture.
     */
    @NotNull
    private final Texture texture;

    /**
     * The shader program.
     */
    @NotNull
    private final TextProgram program;

    /**
     * The top left position of the text.
     */
    @NotNull
    private final Vec2i pos;

    /**
     * The colour of the text.
     */
    @NotNull
    private Color color;

    /**
     * The text to be rendered.
     */
    @NotNull
    private String text;

    /**
     * Constructor.
     * @param gl the OpenGL interface
     * @param glResourceManager the resource manager
     * @param text the text to be rendered
     * @param pos the top left position of the text
     * @param color the colour of the text
     */
    public TextRenderer(@NotNull GL2 gl, @NotNull GlResourceManager glResourceManager, @NotNull String text, @NotNull Vec2i pos, @NotNull Color color) {
        this.text = text;
        this.pos = pos;
        this.color = color;

        vertexArrayObject = new VertexArrayObject(gl);
        texture = glResourceManager.getTextureManager().getTexture(UiTextures.FONT);
        program = glResourceManager.getShaderManager().getProgram(TextProgram.class);

        float[] positions = createPositions();
        float[] textureCoordinates = createTextureCoordinates();

        positionsBuffer = vertexArrayObject.addBuffer(gl, TextProgram.POSITION_LOCATION, positions, 2);
        textureCoordinatesBuffer = vertexArrayObject.addBuffer(gl, TextProgram.TEXTURE_COORDINATE_LOCATION, textureCoordinates, 2);
    }

    /**
     * Set the text to be rendered.
     * @param gl the OpenGL interface
     * @param text the text to be rendered
     */
    public void setText(@NotNull GL2 gl, @NotNull String text) {
        this.text = text;
        float[] positions = createPositions();
        float[] textureCoordinates = createTextureCoordinates();

        vertexArrayObject.updateBuffer(gl, positionsBuffer, positions);
        vertexArrayObject.updateBuffer(gl, textureCoordinatesBuffer, textureCoordinates);
    }

    /**
     * Set the colour of the text.
     * @param color the colour of the text
     */
    public void setColor(@NotNull Color color) {
        this.color = color;
    }

    /**
     * Draw the text.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix
     */
    public void draw(@NotNull GL2 gl, @NotNull PmvMatrix pmvMatrix) {
        program.use(gl);
        program.setPmvMatrix(gl, pmvMatrix);
        program.setTexture(gl, texture);
        program.setColor(gl, color);
        vertexArrayObject.draw(gl, GL2.GL_TRIANGLE_STRIP, text.length() * VERTICES_PER_CHARACTER);
    }

    /**
     * Create the positions array.
     * <p>
     *     There are two triangles per character.
     * </p>
     * @return the positions array
     */
    @NotNull
    private float[] createPositions() {
        int numberOfVerticies = text.length() * VERTICES_PER_CHARACTER;
        float[] positions = new float[numberOfVerticies * 2];

        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            positions[j++] = pos.getX() + i * CHARACTER_SIZE;
            positions[j++] = pos.getY();

            positions[j++] = pos.getX() + i * CHARACTER_SIZE;
            positions[j++] = pos.getY() + CHARACTER_SIZE;

            positions[j++] = pos.getX() + (i + 1) * CHARACTER_SIZE;
            positions[j++] = pos.getY();

            positions[j++] = pos.getX() + (i + 1) * CHARACTER_SIZE;
            positions[j++] = pos.getY() + CHARACTER_SIZE;
        }

        return positions;
    }

    /**
     * Create the texture coordinates array.
     * @return the texture coordinates array
     */
    @NotNull
    private float[] createTextureCoordinates() {
        int numberOfVerticies = text.length() * VERTICES_PER_CHARACTER;
        float[] textureCoordinates = new float[numberOfVerticies * 2];

        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            char code = text.charAt(i);
            int x = code % CHARACTERS_PER_ROW;
            int y = code / CHARACTERS_PER_ROW;

            textureCoordinates[j++] = x / (float) CHARACTERS_PER_ROW;
            textureCoordinates[j++] = 1 - y / (float) CHARACTERS_PER_ROW;

            textureCoordinates[j++] = x / (float) CHARACTERS_PER_ROW;
            textureCoordinates[j++] = 1 - (y + 1) / (float) CHARACTERS_PER_ROW;

            textureCoordinates[j++] = (x + 1) / (float) CHARACTERS_PER_ROW;
            textureCoordinates[j++] = 1 - y / (float) CHARACTERS_PER_ROW;

            textureCoordinates[j++] = (x + 1) / (float) CHARACTERS_PER_ROW;
            textureCoordinates[j++] = 1 - (y + 1) / (float) CHARACTERS_PER_ROW;
        }
        return textureCoordinates;
    }

    /**
     * Remove the renderers VAO.
     * @param gl the OpenGL interface
     */
    public void remove(GL2 gl) {
        vertexArrayObject.remove(gl);
    }
}

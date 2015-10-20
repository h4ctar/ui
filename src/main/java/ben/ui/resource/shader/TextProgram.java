package ben.ui.resource.shader;

import java.nio.FloatBuffer;
import java.util.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import ben.ui.math.PmvMatrix;

import ben.ui.resource.color.Color;
import com.jogamp.opengl.util.texture.Texture;
import org.jetbrains.annotations.NotNull;

/**
 * The Text Program.
 * <p>
 * Program to render text; uses the alpha from the font sheet with the RGB of the colour uniform.
 */
public class TextProgram extends Program {

    /**
     * The location of the position attribute.
     */
    public static final int POSITION_LOCATION = 0;

    /**
     * The location of the texture coordinate attribute.
     */
    public static final int TEXTURE_COORDINATE_LOCATION = 1;

    /**
     * The location of the PMV matrix uniform.
     */
    private final int pmvLocation;

    /**
     * The location of the texture uniform.
     */
    private final int textureLocation;

    /**
     * The location of the colour uniform.
     */
    private final int colorLocation;

    /**
     * Constructor.
     * @param gl the OpenGL interface
     */
    public TextProgram(@NotNull GL2 gl) {
        super(gl);
        pmvLocation = gl.glGetUniformLocation(getId(), "pmv");
        textureLocation = gl.glGetUniformLocation(getId(), "tex");
        colorLocation = gl.glGetUniformLocation(getId(), "color");
    }

    @Override
    protected final void getSourceFiles(@NotNull Map<Integer, String> sourceFiles) {
        sourceFiles.put(GL2.GL_VERTEX_SHADER, "/shaders/text.vert");
        sourceFiles.put(GL2.GL_FRAGMENT_SHADER, "/shaders/text.frag");
    }

    /**
     * Set the PMV matrix uniform.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix to set
     */
    public final void setPmvMatrix(@NotNull GL2 gl, @NotNull PmvMatrix pmvMatrix) {
        FloatBuffer buffer = pmvMatrix.getPmvMatrix().getBuffer();
        gl.glUniformMatrix4fv(pmvLocation, 1, false, buffer);
    }

    /**
     * Set the texture uniform.
     * @param gl the OpenGL interface
     * @param texture the texture to set
     */
    public final void setTexture(@NotNull GL2 gl, @NotNull Texture texture) {
        gl.glActiveTexture(GL.GL_TEXTURE0);
        texture.enable(gl);
        texture.bind(gl);
        gl.glUniform1i(textureLocation, 0);
    }

    /**
     * Set the colour of the text.
     * <p>
     * The alpha component is ignored.
     * @param gl the OpenGL context
     * @param color the colour
     */
    public final void setColor(@NotNull GL2 gl, @NotNull Color color) {
        gl.glUniform4f(colorLocation, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}

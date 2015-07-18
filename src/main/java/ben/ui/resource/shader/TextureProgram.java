package ben.ui.resource.shader;

import java.nio.FloatBuffer;
import java.util.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import ben.math.PmvMatrix;

import com.jogamp.opengl.util.texture.Texture;
import org.jetbrains.annotations.NotNull;

/**
 * The Texture Shader Program.
 */
public class TextureProgram extends Program {

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
    private final int texureLocation;

    /**
     * Constructor.
     * @param gl the OpenGL interface
     */
    public TextureProgram(@NotNull GL3 gl) {
        super(gl);
        pmvLocation = gl.glGetUniformLocation(getId(), "pmv");
        texureLocation = gl.glGetUniformLocation(getId(), "tex");
    }

    @Override
    protected final void getSourceFiles(@NotNull Map<Integer, String> sourceFiles) {
        sourceFiles.put(GL3.GL_VERTEX_SHADER, "/shaders/texture.vert");
        sourceFiles.put(GL3.GL_FRAGMENT_SHADER, "/shaders/texture.frag");
    }

    /**
     * Set the PMV matrix uniform.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix to set
     */
    public final void setPmvMatrix(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix) {
        FloatBuffer buffer = pmvMatrix.getPmvMatrix().getBuffer();
        gl.glUniformMatrix4fv(pmvLocation, 1, false, buffer);
    }

    /**
     * Set the texture uniform.
     * @param gl the OpenGL interface
     * @param texture the texture to set
     */
    public final void setTexture(@NotNull GL3 gl, @NotNull Texture texture) {
        gl.glActiveTexture(GL.GL_TEXTURE0);
        texture.enable(gl);
        texture.bind(gl);
        gl.glUniform1i(texureLocation, 0);
    }
}

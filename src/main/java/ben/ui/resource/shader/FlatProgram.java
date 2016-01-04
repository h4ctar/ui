package ben.ui.resource.shader;

import java.nio.FloatBuffer;
import java.util.Map;

import com.jogamp.opengl.GL2;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.color.Color;
import javax.annotation.Nonnull;

/**
 * Flat AbstractProgram.
 * <p>
 * A GLSL shader program that renders a 2D or 3D shape with a solid colour.
 */
public class FlatProgram extends AbstractProgram {

    /**
     * The positions of the verticies should be set to this attribute location.
     */
    public static final int POSITION_LOCATION = 0;

    /**
     * The location of the colour uniform.
     */
    private final int colorLocation;

    /**
     * The location of the PMV matrix uniform.
     */
    private final int pmvLocation;

    /**
     * Constructor.
     * @param gl the OpenGL interface
     */
    public FlatProgram(@Nonnull GL2 gl) {
        super(gl);
        colorLocation = gl.glGetUniformLocation(getId(), "color");
        pmvLocation = gl.glGetUniformLocation(getId(), "pmv");
    }

    @Override
    protected final void getSourceFiles(@Nonnull Map<Integer, String> sourceFiles) {
        sourceFiles.put(GL2.GL_VERTEX_SHADER, "/shaders/flat.vert");
        sourceFiles.put(GL2.GL_FRAGMENT_SHADER, "/shaders/flat.frag");
    }

    /**
     * Set the PMV matrix.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix to set
     */
    public final void setPmvMatrix(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix) {
        FloatBuffer buffer = pmvMatrix.getPmvMatrix().getBuffer();
        gl.glUniformMatrix4fv(pmvLocation, 1, false, buffer);
    }

    /**
     * Set the colour.
     * @param gl the OpenGL interface
     * @param color the colour to set
     */
    public final void setColor(@Nonnull GL2 gl, @Nonnull Color color) {
        gl.glUniform4f(colorLocation, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}

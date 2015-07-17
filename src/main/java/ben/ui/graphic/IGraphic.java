package ben.ui.graphic;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL3;

/**
 * Graphic Interface.
 */
public interface IGraphic {

    /**
     * Draw the graphic.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix
     * @param glResourceManager the OpenGL resource manager
     */
    void draw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix, @NotNull GlResourceManager glResourceManager);

    /**
     * Remove the graphic.
     * @param gl the OpenGL interface
     */
    void remove(@NotNull GL3 gl);
}

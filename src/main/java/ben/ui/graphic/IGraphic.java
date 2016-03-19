package ben.ui.graphic;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.IGlResourceManager;
import javax.annotation.Nonnull;

import com.jogamp.opengl.GL2;

/**
 * # Graphic Interface
 */
public interface IGraphic {

    /**
     * Draw the graphic.
     *
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix
     * @param glResourceManager the OpenGL resource manager
     */
    void draw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix, @Nonnull IGlResourceManager glResourceManager);

    /**
     * Remove the graphic.
     *
     * @param gl the OpenGL interface
     */
    void remove(@Nonnull GL2 gl);
}

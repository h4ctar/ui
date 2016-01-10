package ben.ui.graphic;

import ben.ui.math.PmvMatrix;
import ben.ui.resource.GlResourceManager;
import javax.annotation.Nonnull;

import com.jogamp.opengl.GL2;

/**
 * Abstract Graphic.
 */
public abstract class AbstractGraphic implements IGraphic {

    /**
     * Is the graphic initialised?
     * <p>
     *     If this is false, the initDraw method will be called on the next frame.
     * </p>
     */
    private boolean isInitialised = false;

    /**
     * Is the graphic dirty?
     * <p>
     *     If true, the updateDraw method will be called on the next frame.
     * </p>
     */
    private boolean isDirty = false;

    @Override
    public final void draw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix, @Nonnull GlResourceManager glResourceManager) {
        if (!isInitialised) {
            initDraw(gl, glResourceManager);
            isInitialised = true;
        }
        else if (isDirty) {
            updateDraw(gl);
            isDirty = false;
        }
        doDraw(gl, pmvMatrix);
    }

    /**
     * Initialise the draw.
     * @param gl the OpenGL interface
     * @param glResourceManager the OpenGL resource manager
     */
    protected abstract void initDraw(@Nonnull GL2 gl, @Nonnull GlResourceManager glResourceManager);

    /**
     * Update the draw.
     * @param gl the OpenGL interface
     */
    protected abstract void updateDraw(@Nonnull GL2 gl);

    /**
     * Do the draw.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV matrix
     */
    protected abstract void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix);

    /**
     * Flag the graphic as dirty.
     */
    protected final void setDirty() {
        isDirty = true;
    }

    @Override
    public final void remove(@Nonnull GL2 gl) {
        isInitialised = false;
        isDirty = false;
    }
}

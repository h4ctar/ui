package ben.ui.window;

import ben.ui.resource.GlResourceManager;
import com.jogamp.opengl.GL2;

import javax.annotation.Nonnull;

/**
 * Resource Loader.
 *
 * Can be used to load program OpenGL resources.
 */
public interface IResourceLoader {

    /**
     * Load resources.
     *
     * @param gl the OpenGL context
     * @param resourceManager the resource manager
     */
    void loadResources(GL2 gl, @Nonnull GlResourceManager resourceManager);
}

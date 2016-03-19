package ben.ui.resource;

import ben.ui.resource.color.ColorManager;
import ben.ui.resource.shader.IShaderManager;
import ben.ui.resource.texture.TextureManager;
import javax.annotation.Nonnull;

/**
 * # OpenGL Resource Manager Interface
 *
 * Manages all resources that are tied to an OpenGL context.
 */
public interface IGlResourceManager {

    /**
     * Get the texture manager.
     * @return the texture manager
     */
    @Nonnull
    TextureManager getTextureManager();

    /**
     * Get the shader manager.
     * @return the shader manager
     */
    @Nonnull
    IShaderManager getShaderManager();

    /**
     * Get the color manager.
     * @return the color manager
     */
    @Nonnull
    ColorManager getColorManager();
}

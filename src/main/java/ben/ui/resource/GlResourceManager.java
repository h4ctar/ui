package ben.ui.resource;

import ben.ui.resource.color.ColorManager;
import ben.ui.resource.shader.IShaderManager;
import ben.ui.resource.shader.ShaderManager;
import ben.ui.resource.texture.TextureManager;
import javax.annotation.Nonnull;

/**
 * # OpenGL Resource Manager
 *
 * Standard implementation.
 */
public class GlResourceManager implements IGlResourceManager {

    /**
     * The texture manager.
     */
    private final TextureManager textureManager = new TextureManager();

    /**
     * The shader manager.
     */
    private final ShaderManager shaderManager = new ShaderManager();

    /**
     * The color manager.
     */
    private final ColorManager colorManager = new ColorManager();

    @Override
    @Nonnull
    public final TextureManager getTextureManager() {
        return textureManager;
    }

    @Override
    @Nonnull
    public final IShaderManager getShaderManager() {
        return shaderManager;
    }

    @Override
    @Nonnull
    public final ColorManager getColorManager() {
        return colorManager;
    }
}

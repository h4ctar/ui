package ben.ui.resource;

import ben.ui.resource.color.ColorManager;
import ben.ui.resource.shader.ShaderManager;
import ben.ui.resource.texture.TextureManager;
import org.jetbrains.annotations.NotNull;

/**
 * The resource manager.
 * <p>
 * Manages all resources that are tied to an OpenGL context.
 */
public class GlResourceManager {

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

    /**
     * Get the texture manager.
     * @return the texture manager
     */
    @NotNull
    public final TextureManager getTextureManager() {
        return textureManager;
    }

    /**
     * Get the shader manager.
     * @return the shader manager
     */
    @NotNull
    public final ShaderManager getShaderManager() {
        return shaderManager;
    }

    /**
     * Get the color manager.
     * @return the color manager
     */
    @NotNull
    public final ColorManager getColorManager() {
        return colorManager;
    }
}

package ben.ui.resource.texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GLProfile;

/**
 * The Texture Manager.
 */
public class TextureManager {

    /**
     * The Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(TextureManager.class.getSimpleName());

    /**
     * The textures.
     */
    private final Map<Enum<?>, Texture> textures = new HashMap<>();

    /**
     * Load a texture from a local file.
     * @param key the key to register the texture against
     * @param textureResourceName the path to the texture
     */
    public final void loadTexture(@NotNull Enum<?> key, @NotNull String textureResourceName) {
        LOGGER.info("Loading texture: " + key + " -> " + textureResourceName);
        assert !textures.containsKey(key) : "Texture is already loaded";
        try {
            InputStream stream = getClass().getResourceAsStream(textureResourceName);
            TextureData data = TextureIO.newTextureData(GLProfile.get(GLProfile.GL3), stream, false, "png");
            Texture texture = TextureIO.newTexture(data);
            textures.put(key, texture);
        }
        catch (IOException e) {
            throw new RuntimeException("Couldn't load texture", e);
        }
    }

    /**
     * Get a texture.
     * @param key the key of the texture
     * @return the texture
     */
    public final Texture getTexture(@NotNull Enum<?> key) {
        assert textures.containsKey(key) : "Texture has not been loaded";
        return textures.get(key);
    }
}

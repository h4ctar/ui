package ben.ui.resource.shader;

import javax.annotation.Nonnull;

/**
 * # Shader Manager Interface
 */
public interface IShaderManager {

    /**
     * Add a new program to the shader manager.
     *
     * @param program the program to add
     */
    void addProgram(@Nonnull AbstractProgram program);

    /**
     * Get a shader program.
     *
     * @param programClass the class of the program to get
     * @param <T> the type of the shader program to get
     * @return the shader program
     */
    @Nonnull
    <T extends AbstractProgram> T getProgram(Class<T> programClass);
}

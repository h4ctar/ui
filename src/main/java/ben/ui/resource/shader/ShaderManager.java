package ben.ui.resource.shader;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

/**
 * The Shader Manager.
 */
public class ShaderManager {

    /**
     * The map of programs keyed by the program class.
     */
    private final Map<Class<? extends AbstractProgram>, AbstractProgram> programs = new HashMap<>();

    /**
     * Add a new program to the shader manager.
     * @param program the program to add
     */
    public final void addProgram(@Nonnull AbstractProgram program) {
        programs.put(program.getClass(), program);
    }

    /**
     * Get a shader program.
     * @param programClass the class of the program to get
     * @param <T> the type of the shader program to get
     * @return the shader program
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public final <T extends AbstractProgram> T getProgram(Class<T> programClass) {
        T program = (T) programs.get(programClass);
        assert program != null;
        return program;
    }
}

package ben.ui.resource.shader;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * The Shader Manager.
 */
public class ShaderManager {

    /**
     * The map of programs keyed by the program class.
     */
    private final Map<Class<? extends Program>, Program> programs = new HashMap<>();

    /**
     * Add a new program to the shader manager.
     * @param program the program to add
     */
    public final void addProgram(@NotNull Program program) {
        programs.put(program.getClass(), program);
    }

    /**
     * Get a shader program.
     * @param programClass the class of the program to get
     * @param <T> the type of the shader program to get
     * @return the shader program
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public final <T extends Program> T getProgram(Class<T> programClass) {
        T program = (T) programs.get(programClass);
        assert program != null;
        return program;
    }
}

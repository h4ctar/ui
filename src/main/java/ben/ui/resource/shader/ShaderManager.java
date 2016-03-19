package ben.ui.resource.shader;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

/**
 * # Shader Manager
 */
public class ShaderManager implements IShaderManager {

    /**
     * The map of programs keyed by the program class.
     */
    private final Map<Class<? extends AbstractProgram>, AbstractProgram> programs = new HashMap<>();

    @Override
    public final void addProgram(@Nonnull AbstractProgram program) {
        programs.put(program.getClass(), program);
    }

    @Override
    @Nonnull
    @SuppressWarnings("unchecked")
    public final <T extends AbstractProgram> T getProgram(Class<T> programClass) {
        T program = (T) programs.get(programClass);
        assert program != null;
        return program;
    }
}

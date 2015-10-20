package ben.ui.resource.shader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;

import com.jogamp.common.nio.Buffers;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract shader program.
 * <p>
 * Provides the base functionality for shader programs. <br>
 * The program only supports one shader per shader type, i.e. only one vertex shader. <br>
 * Concrete shader programs should provide methods to set the uniforms and provide the locations of the attributes. <br>
 * Because shader program classes are very tightly coupled to the shader source, the shader source should be a resource
 * in the same package as the program.
 */
public abstract class Program {

    /**
     * The Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(Program.class.getSimpleName());

    /**
     * The OpenGL ID of the shader program.
     */
    private final int id;

    /**
     * Constructor.
     * @param gl the OpenGL interface
     */
    public Program(@NotNull GL2 gl) {
        LOGGER.info("Creating " + getClass().getSimpleName());
        id = gl.glCreateProgram();
        Map<Integer, String> sourceFiles = new HashMap<>();
        getSourceFiles(sourceFiles);
        for (Entry<Integer, String> sourceFileEntry : sourceFiles.entrySet()) {
            addShader(gl, sourceFileEntry.getKey(), sourceFileEntry.getValue());
        }
        gl.glLinkProgram(id);
        checkProgram(gl);
    }

    /**
     * Get the source files to compile and link into the program.
     * @param sourceFiles a map of source files, keyed by the shader type, i.e. GL_VERTEX_SHADER, GL_FRAGMENT_SHADER
     * etc...
     */
    protected abstract void getSourceFiles(Map<Integer, String> sourceFiles);

    /**
     * Use the program.
     * @param gl the OpenGL interface
     */
    public final void use(@NotNull GL2 gl) {
        gl.glUseProgram(getId());
    }

    /**
     * Add a shader to the program.
     * <p>
     * Compiles the shader and links it to the program.
     * @param gl the OpenGL interface
     * @param shaderType the type of the shader
     * @param shaderResourceName the resource of the shader
     */
    private void addShader(@NotNull GL2 gl, int shaderType, String shaderResourceName) {
        LOGGER.info("Adding " + shaderResourceName + " to " + getClass().getSimpleName());
        int vertexShader = gl.glCreateShader(shaderType);
        String vertexShaderSource = loadFile(getClass().getResource(shaderResourceName));

        gl.glShaderSource(vertexShader, 1, new String[] { vertexShaderSource }, null);
        gl.glCompileShader(vertexShader);
        checkShader(gl, vertexShader);
        gl.glAttachShader(id, vertexShader);
    }

    /**
     * Get the OpenGL ID of the program.
     * @return the OpenGL ID of the program
     */
    protected final int getId() {
        return id;
    }

    /**
     * Load a file into a string.
     * @param url the URL of the file
     * @return the file contents in a string
     */
    @NotNull
    private static String loadFile(@NotNull URL url) {
        String source = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                source += line + "\n";
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    /**
     * Check a shader for errors.
     * <p>
     * Call this method on a compiled shader.
     * @param gl the OpenGL interface
     * @param shader the ID of the shader to check
     */
    private static void checkShader(@NotNull GL2 gl, int shader) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetShaderiv(shader, GL2.GL_COMPILE_STATUS, intBuffer);
        if (intBuffer.get(0) == GL.GL_FALSE) {
            gl.glGetShaderiv(shader, GL2.GL_INFO_LOG_LENGTH, intBuffer);
            int length = intBuffer.get(0);
            String out = null;
            if (length > 0) {
                ByteBuffer infoLog = Buffers.newDirectByteBuffer(length);
                gl.glGetShaderInfoLog(shader, infoLog.limit(), intBuffer, infoLog);
                byte[] infoBytes = new byte[length];
                infoLog.get(infoBytes);
                out = new String(infoBytes);
            }
            throw new GLException("Error during shader compilation:\n" + out);
        }
    }

    /**
     * Check the program for errors.
     * <p>
     * Call this after a shader has been linked into the program.
     * @param gl the OpenGL interface
     */
    private void checkProgram(@NotNull GL2 gl) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(id, GL2.GL_LINK_STATUS, intBuffer);
        if (intBuffer.get(0) == GL.GL_FALSE) {
            gl.glGetProgramiv(id, GL2.GL_INFO_LOG_LENGTH, intBuffer);
            int length = intBuffer.get(0);
            String out = null;
            if (length > 0) {
                ByteBuffer infoLog = Buffers.newDirectByteBuffer(length);
                gl.glGetProgramInfoLog(id, infoLog.limit(), intBuffer, infoLog);
                byte[] infoBytes = new byte[length];
                infoLog.get(infoBytes);
                out = new String(infoBytes);
            }
            throw new GLException("Error during program link:\n" + out);
        }
    }
}

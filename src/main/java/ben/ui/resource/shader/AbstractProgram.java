package ben.ui.resource.shader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import javax.annotation.Nonnull;

/**
 * Abstract shader program.
 *
 * Provides the base functionality for shader programs.
 * The program only supports one shader per shader type, i.e. only one vertex shader.
 * Concrete shader programs should provide methods to set the uniforms and provide the locations of the attributes.
 * Because shader program classes are very tightly coupled to the shader source, the shader source should be a resource
 * in the same package as the program.
 */
public abstract class AbstractProgram {

    /**
     * The Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(AbstractProgram.class.getSimpleName());

    /**
     * The OpenGL ID of the shader program.
     */
    private final int id;

    /**
     * Constructor.
     *
     * @param gl the OpenGL interface
     */
    public AbstractProgram(@Nonnull GL2 gl) {
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
     *
     * @param sourceFiles a map of source files, keyed by the shader type, i.e. GL_VERTEX_SHADER, GL_FRAGMENT_SHADER
     * etc...
     */
    protected abstract void getSourceFiles(Map<Integer, String> sourceFiles);

    /**
     * Use the program.
     *
     * @param gl the OpenGL interface
     */
    public final void use(@Nonnull GL2 gl) {
        gl.glUseProgram(getId());
    }

    /**
     * Add a shader to the program.
     *
     * Compiles the shader and links it to the program.
     *
     * @param gl the OpenGL interface
     * @param shaderType the type of the shader
     * @param shaderResourceName the resource of the shader
     */
    private void addShader(@Nonnull GL2 gl, int shaderType, String shaderResourceName) {
        LOGGER.info("Adding " + shaderResourceName + " to " + getClass().getSimpleName());
        int vertexShader = gl.glCreateShader(shaderType);
        String vertexShaderSource = loadFile(AbstractProgram.class.getResource(shaderResourceName));

        gl.glShaderSource(vertexShader, 1, new String[] {vertexShaderSource}, null);
        gl.glCompileShader(vertexShader);
        checkShader(gl, vertexShader);
        gl.glAttachShader(id, vertexShader);
    }

    /**
     * Get the OpenGL ID of the program.
     *
     * @return the OpenGL ID of the program
     */
    protected final int getId() {
        return id;
    }

    /**
     * Load a file into a string.
     *
     * @param url the URL of the file
     * @return the file contents in a string
     */
    @Nonnull
    private static String loadFile(@Nonnull URL url) {
        StringBuilder source = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line);
                source.append("\n");
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return source.toString();
    }

    /**
     * Check a shader for errors.
     *
     * Call this method on a compiled shader.
     *
     * @param gl the OpenGL interface
     * @param shader the ID of the shader to check
     */
    private static void checkShader(@Nonnull GL2 gl, int shader) {
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
                try {
                    out = new String(infoBytes, "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    throw new GLException("Error during shader compilation");
                }
            }
            throw new GLException("Error during shader compilation:\n" + out);
        }
    }

    /**
     * Check the program for errors.
     *
     * Call this after a shader has been linked into the program.
     *
     * @param gl the OpenGL interface
     */
    private void checkProgram(@Nonnull GL2 gl) {
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
                try {
                    out = new String(infoBytes, "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    throw new GLException("Error during program link");
                }
            }
            throw new GLException("Error during program link:\n" + out);
        }
    }
}

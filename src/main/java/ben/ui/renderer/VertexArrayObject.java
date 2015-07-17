package ben.ui.renderer;

import com.jogamp.common.nio.Buffers;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Vertex Array Object.
 */
public class VertexArrayObject {

    /**
     * The maximum number of buffers allowed for a VAO.
     */
    private static final int MAXIMUM_BUFFERS = 10;

    /**
     * The ID of the VAO.
     */
    private final int id;

    /**
     * The buffers added to this VAO.
     */
    private final IntBuffer buffers = IntBuffer.allocate(MAXIMUM_BUFFERS);

    /**
     * Constructor.
     * <p>
     * Creates the VAO.
     * @param gl the OpenGL interface
     */
    public VertexArrayObject(@NotNull GL3 gl) {
        id = genVertexArray(gl);
        assert id != -1;
    }

    /**
     * Add a buffer to this VAO.
     * @param gl the OpenGL interface
     * @param location the location of the attribute
     * @param data the data
     * @param size the number of elements per vertex
     * @return the buffer ID
     */
    public final int addBuffer(@NotNull GL3 gl, int location, @NotNull float[] data, int size) {
        Buffer dataBuffer = FloatBuffer.wrap(data);
        int buffer = genBuffer(gl);
        assert buffers.position() < MAXIMUM_BUFFERS;
        buffers.put(buffer);
        gl.glBindVertexArray(id);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, dataBuffer.limit() * Buffers.SIZEOF_FLOAT, dataBuffer, GL.GL_DYNAMIC_DRAW);
        gl.glEnableVertexAttribArray(location);
        gl.glVertexAttribPointer(location, size, GL.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
        return buffer;
    }

    /**
     * Update a buffer.
     * <p>
     * Overwrites the entire buffer with new data.
     * @param gl the OpenGL interface
     * @param buffer the buffer ID
     * @param data the data
     */
    public final void updateBuffer(@NotNull GL3 gl, int buffer, @NotNull float[] data) {
        Buffer dataBuffer = FloatBuffer.wrap(data);
        gl.glBindVertexArray(id);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, dataBuffer.limit() * Buffers.SIZEOF_FLOAT, dataBuffer, GL.GL_DYNAMIC_DRAW);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
    }

    /**
     * Binds and draws the entire VAO.
     * @param gl the OpenGL interface
     * @param mode the primitive mode, i.e. GL_POINTS, GL_LINE_STRIP, etc...
     * @param count the number of indicies to render
     */
    public final void draw(@NotNull GL3 gl, int mode, int count) {
        gl.glBindVertexArray(id);
        gl.glDrawArrays(mode, 0, count);
        gl.glBindVertexArray(0);
    }

    /**
     * Generate a vertex array.
     * @param gl the OpenGL interface
     * @return the vertex array ID
     */
    private static int genVertexArray(@NotNull GL3 gl) {
        IntBuffer vertexArray = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, vertexArray);
        return vertexArray.get();
    }

    /**
     * Generate a buffer.
     * @param gl the OpenGL interface
     * @return the buffer ID
     */
    private static int genBuffer(@NotNull GL3 gl) {
        IntBuffer buffer = IntBuffer.allocate(1);
        gl.glGenBuffers(1, buffer);
        return buffer.get();
    }

    /**
     * Remove the VAO and its buffers.
     * @param gl the OpenGL interface
     */
    public void remove(GL3 gl) {
        int n = buffers.position();
        buffers.rewind();
        gl.glDeleteBuffers(n, buffers);
        gl.glDeleteVertexArrays(1, new int[]{id}, 0);
    }
}

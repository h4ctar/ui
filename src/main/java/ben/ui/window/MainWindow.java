package ben.ui.window;

import ben.ui.input.mouse.MouseButton;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.UiColors;
import ben.ui.resource.shader.FlatProgram;
import ben.ui.resource.shader.TextProgram;
import ben.ui.resource.shader.TextureProgram;
import ben.ui.resource.texture.UiTextures;
import ben.ui.widget.IWidget;
import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import net.jcip.annotations.ThreadSafe;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.jogamp.opengl.*;

/**
 * Main Window.
 * <p>
 *     Has an OpenGL context.
 * </p>
 */
@ThreadSafe
public class MainWindow {

    /**
     * The Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(MainWindow.class);

    /**
     * The number of frames per second for the animator.
     */
    private static final int FRAMES_PER_SECOND = 60;

    /**
     * The animator.
     */
    @NotNull
    private final FPSAnimator animator;

    /**
     * The PMV Matrix.
     */
    private final PmvMatrix pmvMatrix = new PmvMatrix();

    /**
     * The OpenGL Resource Manager.
     */
    private final GlResourceManager glResourceManager = new GlResourceManager();

    /**
     * The GL window.
     */
    private final GLWindow glWindow;

    /**
     * The root widget.
     */
    @Nullable
    private IWidget rootWidget;

    /**
     * Constructor.
     * @param width the width of the window in pixels
     * @param height the height of the window in pixels
     */
    public MainWindow(int width, int height) {
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);

        glWindow = GLWindow.create(caps);
        glWindow.setSize(width, height);
        glWindow.setVisible(true);

        glWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                LOGGER.info("Window destroy event");
                stop();
            }
        });

        glWindow.addGLEventListener(new GlEventHandler());
        glWindow.addMouseListener(new WindowMouseListener());
        glWindow.addKeyListener(new WindowKeyListener());

        animator = new FPSAnimator(glWindow, FRAMES_PER_SECOND);
        animator.start();
    }

    /**
     * Set the root widget.
     * @param rootWidget the root widget
     */
    public final void setRootWidget(@Nullable IWidget rootWidget) {
        this.rootWidget = rootWidget;
        if (rootWidget != null) {
            Vec2i size = new Vec2i(glWindow.getWidth(), glWindow.getHeight());
            rootWidget.setSize(size);
        }
    }

    /**
     * Get the root widget.
     * @return the root widget.
     */
    public final IWidget getRootWidget() {
        return rootWidget;
    }

    /**
     * Request focus.
     */
    public final void requestFocus() {
        glWindow.requestFocus();
    }

    /**
     * Load GL Resources.
     * @param gl the OpenGL interface
     * @param glResourceManager the GL Resource Manager
     */
    protected void loadGlResources(@NotNull GL3 gl, @NotNull GlResourceManager glResourceManager) {
        glResourceManager.getTextureManager().loadTexture(UiTextures.FONT, "/textures/font.png");

        glResourceManager.getShaderManager().addProgram(new FlatProgram(gl));
        glResourceManager.getShaderManager().addProgram(new TextureProgram(gl));
        glResourceManager.getShaderManager().addProgram(new TextProgram(gl));

        glResourceManager.getColorManager().loadColors(UiColors.class, "/colors/colors.xml");
    }

    /**
     * Exit the application.
     */
    public void stop() {
        animator.stop();
        if (glWindow.isVisible()) {
            glWindow.destroy();
        }
    }

    /**
     * Invoke a runnable on the EDT thread.
     * @param runnable the runnable to invoke
     */
    protected final void invokeOnEdt(@NotNull Runnable runnable) {
        glWindow.runOnEDTIfAvail(false, runnable);
    }

    /**
     * Get the position of the window.
     * @return the position of the window
     */
    public final Vec2i getPosition() {
        return new Vec2i(glWindow.getX(), glWindow.getY());
    }

    /**
     * The OpenGL Event Handler.
     */
    private class GlEventHandler implements GLEventListener {

        @Override
        public void init(@NotNull GLAutoDrawable drawable) {
            LOGGER.info("Initialising the Window");
            GL3 gl = drawable.getGL().getGL3();
            drawable.setGL(GLPipelineFactory.create("com.jogamp.opengl.Debug", GL3.class, gl, null));

            MainWindow.this.loadGlResources(gl, glResourceManager);
        }

        @Override
        public void dispose(@NotNull GLAutoDrawable drawable) {
            LOGGER.info("Disposing the Window");
        }

        @Override
        public void display(@NotNull GLAutoDrawable drawable) {
            GL3 gl = drawable.getGL().getGL3();
            gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
            gl.glEnable(GL3.GL_BLEND);
            gl.glDisable(GL3.GL_DEPTH_TEST);
            gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
            if (rootWidget != null) {
                rootWidget.draw(gl, pmvMatrix, glResourceManager);
            }
        }

        @Override
        public void reshape(@NotNull GLAutoDrawable drawable, int x, int y, int width, int height) {
            LOGGER.info("Reshaping the Window - " + width + "x" + height);
            GL3 gl = drawable.getGL().getGL3();
            gl.glViewport(0, 0, width, height);
            pmvMatrix.identity();
            pmvMatrix.orthographic(new Rect(0, 0, width, height));
            if (rootWidget != null) {
                rootWidget.setSize(new Vec2i(width, height));
            }
        }
    }

    /**
     * The Window Mouse Listener.
     * <p>
     *     Forwards events to the Root Widget.
     * </p>
     */
    private class WindowMouseListener implements MouseListener {

        @Override
        public void mouseClicked(@NotNull MouseEvent e) {
            MouseButton button = newtToNotNewt(e.getButton());
            if (rootWidget != null && button != null) {
                rootWidget.getMouseHandler().mouseClicked(button, new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseEntered(@NotNull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseEntered();
            }
        }

        @Override
        public void mouseExited(@NotNull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseExited();
            }
        }

        @Override
        public void mousePressed(@NotNull MouseEvent e) {
            MouseButton button = newtToNotNewt(e.getButton());
            if (rootWidget != null && button != null) {
                rootWidget.getMouseHandler().mousePressed(button, new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseReleased(@NotNull MouseEvent e) {
            MouseButton button = newtToNotNewt(e.getButton());
            if (rootWidget != null && button != null) {
                rootWidget.getMouseHandler().mouseReleased(button, new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseMoved(@NotNull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseMoved(new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseDragged(@NotNull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseDragged(new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseWheelMoved(@NotNull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseWheelMoved(e.getRotation()[1], new Vec2i(e.getX(), e.getY()));
            }
        }

        /**
         * Converts a NEWT button ID to an enum.
         * @param newtButton the NEWT button ID
         * @return the enum
         */
        @Nullable
        private MouseButton newtToNotNewt(int newtButton) {
            MouseButton button = null;
            switch (newtButton) {
                case MouseEvent.BUTTON1:
                    button = MouseButton.LEFT;
                    break;
                case MouseEvent.BUTTON2:
                    button = MouseButton.MIDDLE;
                    break;
                case MouseEvent.BUTTON3:
                    button = MouseButton.RIGHT;
                    break;
            }
            return button;
        }
    }

    /**
     * Window Key Listener.
     * <p>
     *     Forwards events to the Root Widget.
     * </p>
     */
    private class WindowKeyListener implements KeyListener {

        @Override
        public void keyPressed(@NotNull KeyEvent e) {
            if (rootWidget != null) {
                rootWidget.getKeyHandler().keyPressed(e);
            }
        }

        @Override
        public void keyReleased(@NotNull KeyEvent e) {
            if (rootWidget != null) {
                rootWidget.getKeyHandler().keyReleased(e);
            }
        }
    }
}

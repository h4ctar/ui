package ben.ui.window;

import ben.ui.action.ActionManager;
import ben.ui.input.mouse.MouseButton;
import ben.math.PmvMatrix;
import ben.math.Rect;
import ben.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import ben.ui.resource.color.UiColors;
import ben.ui.resource.shader.FlatProgram;
import ben.ui.resource.shader.TextProgram;
import ben.ui.resource.shader.TextureProgram;
import ben.ui.resource.texture.UiTextures;
import ben.ui.rule.RuleManager;
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
    private static final Logger LOGGER = LogManager.getLogger(MainWindow.class.getSimpleName());

    /**
     * The number of frames per second for the animator.
     */
    private static final int FRAMES_PER_SECOND = 60;

    /**
     * The initial width of the window.
     */
    private static final int WINDOW_WIDTH = 1024;

    /**
     * The initial height of the window.
     */
    private static final int WINDOW_HEIGHT = 1024;

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
     * The Action Manager.
     */
    private final ActionManager actionManager = new ActionManager();

    /**
     * The Rule Manager.
     */
    private final RuleManager ruleManager = new RuleManager();

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
     */
    public MainWindow() {
        GLProfile glp = GLProfile.get(GLProfile.GL3);
        GLCapabilities caps = new GLCapabilities(glp);

        glWindow = GLWindow.create(caps);
        glWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
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

    public final void setRootWidget(@Nullable IWidget rootWidget) {
        this.rootWidget = rootWidget;
        if (rootWidget != null) {
            Vec2i size = new Vec2i(glWindow.getWidth(), glWindow.getHeight());
            rootWidget.setSize(size);
        }
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
     * Get the Action Manager.
     * @return the Action Manager
     */
    @NotNull
    public final ActionManager getActionManager() {
        return actionManager;
    }

    /**
     * Get the Rule Manager.
     * @return the Rule Manager
     */
    @NotNull
    public final RuleManager getRuleManager() {
        return ruleManager;
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
            LOGGER.info("Reshaping the Window");
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
                rootWidget.getMouseHandler().mouseWheelMoved(e.getRotation()[0], new Vec2i(e.getX(), e.getY()));
            }
        }

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

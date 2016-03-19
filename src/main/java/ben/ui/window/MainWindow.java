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
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLPipelineFactory;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Main Window.
 *
 * Has an OpenGL context.
 */
public final class MainWindow {

    /**
     * The Logger.
     */
    @Nonnull
    private static final Logger LOGGER = LogManager.getLogger(MainWindow.class);

    /**
     * The number of frames per second for the animator.
     */
    private static final int FRAMES_PER_SECOND = 60;

    /**
     * Close listeners are notified when the window is closed.
     */
    private final Set<ICloseListener> closeListeners = new HashSet<>();

    /**
     * The animator.
     */
    @Nonnull
    private final FPSAnimator animator;

    /**
     * The PMV Matrix.
     */
    @Nonnull
    private final PmvMatrix pmvMatrix = new PmvMatrix();

    /**
     * The OpenGL Resource Manager.
     */
    @Nonnull
    private final GlResourceManager glResourceManager = new GlResourceManager();

    /**
     * The GL canvas.
     */
    @Nonnull
    private final GLCanvas canvas;

    /**
     * The window.
     */
    @Nonnull
    private final Frame frame;

    /**
     * The mouse listener.
     */
    @Nonnull
    private final WindowMouseListener mouseListener;

    /**
     * The key listener.
     */
    @Nonnull
    private final WindowKeyListener keyListener;

    /**
     * The resource loader.
     */
    @Nullable
    private final IResourceLoader resourceLoader;

    /**
     * The root widget.
     */
    @Nullable
    private IWidget rootWidget;

    /**
     * Constructor.
     *
     * @param width the width of the window in pixels
     * @param height the height of the window in pixels
     * @param resourceLoader the resource loader, will be called when the window is initialised and an OpenGL context is
     *                       current
     */
    public MainWindow(int width, int height, @Nullable IResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);

        canvas = new GLCanvas(caps);

        frame = new Frame();
        frame.setSize(width, height);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                LOGGER.info("Window closing event");
                stop();
            }
        });

        mouseListener = new WindowMouseListener();
        keyListener = new WindowKeyListener();

        canvas.addGLEventListener(new GlEventHandler());
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);
        canvas.addMouseWheelListener(mouseListener);
        canvas.addKeyListener(keyListener);

        animator = new FPSAnimator(canvas, FRAMES_PER_SECOND);
        animator.start();
    }

    /**
     * Set the root widget.
     *
     * @param rootWidget the root widget
     */
    public void setRootWidget(@Nullable IWidget rootWidget) {
        this.rootWidget = rootWidget;
        if (rootWidget != null) {
            Vec2i size = new Vec2i(canvas.getWidth(), canvas.getHeight());
            rootWidget.setSize(size);
        }
    }

    /**
     * Get the root widget.
     *
     * @return the root widget.
     */
    @Nullable
    public IWidget getRootWidget() {
        return rootWidget;
    }

    /**
     * Exit the application.
     */
    public void stop() {
        assert animator.isAnimating();

        animator.stop();
        if (canvas.isVisible()) {
            canvas.destroy();
        }
        frame.dispose();

        for (ICloseListener closeListener : closeListeners) {
            closeListener.windowClosed();
        }
    }

    /**
     * Add a close listener that will ben notified when the window closes.
     *
     * @param closeListener the close listener to add
     */
    public void addCloseListener(@Nonnull ICloseListener closeListener) {
        assert !closeListeners.contains(closeListener);

        closeListeners.add(closeListener);
    }

    /**
     * Get the position of the window.
     *
     * @return the position of the window
     */
    @Nonnull
    public Vec2i getPosition() {
        return new Vec2i(canvas.getX(), canvas.getY());
    }

    /**
     * Get the mouse listener.
     *
     * @return the mouse listener
     */
    @Nonnull
    public MouseListener getMouseListener() {
        return mouseListener;
    }

    /**
     * Get the key listener.
     *
     * @return the key listener
     */
    @Nonnull
    public KeyListener getKeyListener() {
        return keyListener;
    }

    /**
     * The OpenGL Event Handler.
     */
    private class GlEventHandler implements GLEventListener {

        @Override
        public void init(@Nonnull GLAutoDrawable drawable) {
            LOGGER.info("Initialising the Window");
            GL2 gl = drawable.getGL().getGL2();
            drawable.setGL(GLPipelineFactory.create("com.jogamp.opengl.Debug", GL2.class, gl, null));

            glResourceManager.getTextureManager().loadTexture(UiTextures.FONT, "/textures/font.png");

            glResourceManager.getShaderManager().addProgram(new FlatProgram(gl));
            glResourceManager.getShaderManager().addProgram(new TextureProgram(gl));
            glResourceManager.getShaderManager().addProgram(new TextProgram(gl));

            glResourceManager.getColorManager().loadColors(UiColors.class, "/colors/colors.xml");

            if (resourceLoader != null) {
                resourceLoader.loadResources(gl, glResourceManager);
            }
        }

        @Override
        public void dispose(@Nonnull GLAutoDrawable drawable) {
            LOGGER.info("Disposing the Window");
        }

        @Override
        public void display(@Nonnull GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();

            gl.glDisable(GL.GL_SCISSOR_TEST);
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
            gl.glEnable(GL.GL_SCISSOR_TEST);

            gl.glEnable(GL2.GL_BLEND);
            gl.glDisable(GL2.GL_DEPTH_TEST);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

            if (rootWidget != null) {
                rootWidget.draw(gl, pmvMatrix, glResourceManager);
            }
        }

        @Override
        public void reshape(@Nonnull GLAutoDrawable drawable, int x, int y, int width, int height) {
            LOGGER.info("Reshaping the Window - " + width + "x" + height);
            GL2 gl = drawable.getGL().getGL2();
            gl.glViewport(0, 0, width, height);
            pmvMatrix.identity();
            pmvMatrix.orthographic(new Rect(0, 0, width, height));
            pmvMatrix.setScreenSize(new Vec2i(width, height));
            pmvMatrix.setScissorBox(new Rect(0, 0, width, height));
            if (rootWidget != null) {
                rootWidget.setSize(new Vec2i(width, height));
            }
        }
    }

    /**
     * The Window Mouse Listener.
     *
     * Forwards events to the Root Widget.
     */
    private class WindowMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

        @Override
        public void mouseClicked(@Nonnull MouseEvent e) {
            MouseButton button = newtToNotNewt(e.getButton());
            if (rootWidget != null && button != null) {
                Vec2i mousePos = new Vec2i(e.getX(), e.getY());
                Vec2i widgetPos = new Vec2i(0, 0);
                rootWidget.getMouseHandler().mouseClicked(button, mousePos, widgetPos);
            }
        }

        @Override
        public void mouseEntered(@Nonnull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseEntered();
            }
        }

        @Override
        public void mouseExited(@Nonnull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseExited();
            }
        }

        @Override
        public void mousePressed(@Nonnull MouseEvent e) {
            MouseButton button = newtToNotNewt(e.getButton());
            if (rootWidget != null && button != null) {
                rootWidget.getMouseHandler().mousePressed(button, new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseReleased(@Nonnull MouseEvent e) {
            MouseButton button = newtToNotNewt(e.getButton());
            if (rootWidget != null && button != null) {
                rootWidget.getMouseHandler().mouseReleased(button, new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseMoved(@Nonnull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseMoved(new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseDragged(@Nonnull MouseEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseDragged(new Vec2i(e.getX(), e.getY()));
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (rootWidget != null) {
                rootWidget.getMouseHandler().mouseWheelMoved(e.getWheelRotation(), new Vec2i(e.getX(), e.getY()));
            }
        }

        /**
         * Converts a NEWT button ID to an enum.
         *
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
                default:
            }
            return button;
        }
    }

    /**
     * Window Key Listener.
     *
     * Forwards events to the Root Widget.
     */
    private class WindowKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(@Nonnull KeyEvent e) {
            if (rootWidget != null) {
                rootWidget.getKeyHandler().keyPressed(e);
            }
        }

        @Override
        public void keyReleased(@Nonnull KeyEvent e) {
            if (rootWidget != null) {
                rootWidget.getKeyHandler().keyReleased(e);
            }
        }
    }
}

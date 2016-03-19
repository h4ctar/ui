package ben.ui.widget;

import ben.ui.input.IFocusManager;
import ben.ui.input.key.ContainerKeyHandler;
import ben.ui.input.key.IKeyHandler;
import ben.ui.input.mouse.ContainerMouseHandler;
import ben.ui.input.mouse.IMouseHandler;
import ben.ui.math.Matrix;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.math.Vec2i;
import ben.ui.math.Vec3f;
import ben.ui.math.Vec4f;
import ben.ui.renderer.FlatRenderer;
import ben.ui.resource.IGlResourceManager;
import ben.ui.resource.color.Color;
import javax.annotation.Nonnull;

import com.jogamp.opengl.GL2;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * # Abstract Pane
 */
public abstract class AbstractPane implements IPane {

    /**
     * The default background colour of the pane.
     */
    @Nonnull
    private static final Color BACKGROUND_COLOR = new Color(0.235f, 0.247f, 0.254f);

    /**
     * The child widgets in the pane.
     */
    @Nonnull
    private final List<IWidget> widgets = new ArrayList<>();

    /**
     * The child widgets that have been removed from the pane and need to be cleaned up.
     */
    @Nonnull
    private final List<IWidget> removedWidgets = new ArrayList<>();

    /**
     * The mouse handler.
     * Container so that all events are forwarded to children.
     */
    @Nonnull
    private final ContainerMouseHandler mouseHandler;

    /**
     * The key handler.
     * Container so that all events are forwarded to children.
     */
    @Nonnull
    private final ContainerKeyHandler keyHandler;

    /**
     * The name of the pane.
     */
    @Nullable
    private final String name;

    /**
     * Should the background be drawn?
     */
    private final boolean drawBackground;

    /**
     * The position of the pane relative to its parent.
     */
    @Nonnull
    private Vec2i position = new Vec2i(0, 0);

    /**
     * The size of the pane in pixels.
     */
    @Nonnull
    private Vec2i size = new Vec2i(0, 0);

    /**
     * Has draw of the pane been initialised.
     */
    private boolean isInitialised = false;

    /**
     * Does updateDraw need to be called before the background of the pane is drawn again.
     */
    private boolean isDirty = false;

    /**
     * Is the pane focused?
     */
    private boolean focused = false;

    /**
     * The background renderer.
     */
    @Nullable
    private FlatRenderer backgroundRenderer;

    /**
     * Constructor.
     *
     * @param name the name of the pane
     * @param alwaysConsume should the pane always consume events?
     * @param drawBackground should the background be drawn?
     */
    public AbstractPane(@Nullable String name, boolean alwaysConsume, boolean drawBackground) {
        this.name = name;
        this.drawBackground = drawBackground;
        mouseHandler = new ContainerMouseHandler();
        mouseHandler.setAlwaysConsume(alwaysConsume);
        keyHandler = new ContainerKeyHandler(mouseHandler);
    }

    @Nullable
    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final void draw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix, @Nonnull IGlResourceManager glResourceManager) {
        if (!isInitialised) {
            if (drawBackground) {
                backgroundRenderer = new FlatRenderer(gl, glResourceManager, getRect(), BACKGROUND_COLOR);
            }
            initDraw(gl, glResourceManager);
            isInitialised = true;
        }
        else if (isDirty) {
            if (drawBackground) {
                assert backgroundRenderer != null;
                backgroundRenderer.setRect(gl, getRect());
            }
            updateDraw(gl);
            isDirty = false;
        }

        for (IWidget widget : removedWidgets) {
            widget.remove(gl);
        }
        removedWidgets.clear();

        pmvMatrix.push();

        pmvMatrix.translate(new Vec3f(position.getX(), position.getY(), 0));

        // Scissor the viewport so that nothing is drawn outside the pane.
        Vec4f panePosition = Matrix.mul(pmvMatrix.getMvMatrix(), new Vec4f(0, 0, 0, 1));
        Vec2i screenSize = pmvMatrix.getScreenSize();
        Rect scissorBox = new Rect((int) panePosition.getX(), screenSize.getY() - (int) panePosition.getY() - size.getY(), size.getX(), size.getY());
        scissorBox = pmvMatrix.scissor(scissorBox);

        if (scissorBox != null) {
            gl.glScissor(scissorBox.getX(), scissorBox.getY(), scissorBox.getWidth(), scissorBox.getHeight());

            if (drawBackground) {
                assert backgroundRenderer != null;
                backgroundRenderer.draw(gl, pmvMatrix);
            }
            doDraw(gl, pmvMatrix);

            for (IWidget widget : widgets) {
                widget.draw(gl, pmvMatrix, glResourceManager);
            }
        }

        pmvMatrix.pop();
        scissorBox = pmvMatrix.getScissorBox();
        if (scissorBox == null) {
            gl.glScissor(0, 0, screenSize.getX(), screenSize.getY());
        }
        else {
            gl.glScissor(scissorBox.getX(), scissorBox.getY(), scissorBox.getWidth(), scissorBox.getHeight());
        }
    }

    /**
     * Initialise the draw.
     *
     * This method is called before the first draw.
     * @param gl the OpenGL interface
     * @param glResourceManager the OpenGL resource manager
     */
    protected abstract void initDraw(@Nonnull GL2 gl, @Nonnull IGlResourceManager glResourceManager);

    /**
     * Update the draw.
     *
     * This method is called if the widget has been flagged as dirty
     * @param gl the OpenGL interface
     */
    protected abstract void updateDraw(@Nonnull GL2 gl);

    /**
     * Draw the background of the pane.
     *
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV Matrix
     */
    protected abstract void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix);

    @Override
    public final void setPosition(@Nonnull Vec2i position) {
        this.position = position;
    }

    @Nonnull
    @Override
    public final Vec2i getPosition() {
        return position;
    }

    @Override
    public final void setSize(@Nonnull Vec2i size) {
        this.size = size;
        isDirty = true;
        updateLayout();
    }

    @Nonnull
    @Override
    public final Vec2i getSize() {
        return size;
    }

    /**
     * Update the layout.
     */
    protected abstract void updateLayout();

    @Override
    public final boolean isVisible() {
        return true;
    }

    /**
     * Get the position and size of the pane.
     *
     * @return the rectangle
     */
    public final Rect getRect() {
        return new Rect(new Vec2i(0, 0), getSize());
    }

    /**
     * Add a widget.
     *
     * @param widget the widget to add
     */
    protected final void addWidget(@Nonnull IWidget widget) {
        assert !widgets.contains(widget) : "Trying to add widget that is already added";

        widgets.add(widget);
        mouseHandler.addWidget(widget);
        removedWidgets.remove(widget);
    }

    /**
     * Remove a widget.
     *
     * @param widget the widget to remove
     */
    protected final void removeWidget(@Nonnull IWidget widget) {
        assert widgets.contains(widget) : "Trying to remove widget that is not added";
        assert !removedWidgets.contains(widget) : "The widget should not already be in the removed widgets";

        mouseHandler.removeWidget(widget);
        widgets.remove(widget);
        removedWidgets.add(widget);
    }

    @Nonnull
    @Override
    public final List<IWidget> getWidgets() {
        return widgets;
    }

    @Nonnull
    @Override
    public final IMouseHandler getMouseHandler() {
        return mouseHandler;
    }

    @Nonnull
    @Override
    public final IKeyHandler getKeyHandler() {
        return keyHandler;
    }

    @Nonnull
    @Override
    public final IFocusManager getFocusManager() {
        return mouseHandler;
    }

    @Override
    public final boolean contains(@Nonnull Vec2i pos) {
        return new Rect(position, size).contains(pos);
    }

    @Override
    public final void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            if (!focused) {
                mouseHandler.clearFocus();
            }
        }
    }

    @Override
    public final void remove(@Nonnull GL2 gl) {
        preRemove(gl);

        if (backgroundRenderer != null) {
            backgroundRenderer.remove(gl);
        }
        isInitialised = false;
        isDirty = false;
        for (IWidget widget : widgets) {
            widget.remove(gl);
        }
    }

    /**
     * The pane is about to be removed.
     *
     * @param gl the OpenGL interface
     */
    protected void preRemove(@Nonnull GL2 gl) { }
}

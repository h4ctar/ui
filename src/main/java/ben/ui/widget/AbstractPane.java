package ben.ui.widget;

import ben.ui.input.key.ContainerKeyHandler;
import ben.ui.input.key.IKeyHandler;
import ben.ui.input.mouse.ContainerMouseHandler;
import ben.ui.input.mouse.IMouseHandler;
import ben.math.PmvMatrix;
import ben.math.Vec2i;
import ben.ui.resource.GlResourceManager;
import ben.math.Vec3f;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import com.jogamp.opengl.GL3;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Abstract Pane.
 */
@ThreadSafe
public abstract class AbstractPane implements IPane {

    /**
     * The child widgets in the pane.
     */
    @NotNull
    private final Set<IWidget> widgets = new CopyOnWriteArraySet<>();

    /**
     * The child widgets that have been removed from the pane and need to be cleaned up.
     */
    @GuardedBy("removedWidgets")
    @NotNull
    private final Set<IWidget> removedWidgets = new CopyOnWriteArraySet<>();

    /**
     * The mouse handler.
     * Container so that all events are forwarded to children.
     */
    @NotNull
    private final ContainerMouseHandler mouseHandler;

    /**
     * The key handler.
     * Container so that all events are forwarded to children.
     */
    @NotNull
    private final ContainerKeyHandler keyHandler;

    /**
     * The position of the pane relative to its parent.
     */
    @NotNull
    private Vec2i position = new Vec2i(0, 0);

    /**
     * The size of the pane in pixels.
     */
    @NotNull
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
     * The name of the pane.
     */
    @Nullable
    private String name;

    /**
     * Constructor.
     * @param name the name of the pane
     */
    public AbstractPane(@Nullable String name) {
        this.name = name;
        mouseHandler = new ContainerMouseHandler();
        mouseHandler.setAlwaysConsume(false);
        keyHandler = new ContainerKeyHandler(mouseHandler);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public final void draw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix, @NotNull GlResourceManager glResourceManager) {
        if (!isInitialised) {
            initDraw(gl, glResourceManager);
            isInitialised = true;
        }
        else if (isDirty) {
            updateDraw(gl);
            isDirty = false;
        }
        synchronized (removedWidgets) {
            for (IWidget widget : removedWidgets) {
                widget.remove(gl);
            }
            removedWidgets.clear();
        }
        pmvMatrix.push();
        pmvMatrix.translate(new Vec3f(position.getX(), position.getY(), 0));
        doDraw(gl, pmvMatrix);
        for (IWidget widget : widgets) {
            widget.draw(gl, pmvMatrix, glResourceManager);
        }
        pmvMatrix.pop();
    }

    /**
     * Initialise the draw.
     * This method is called before the first draw.
     * @param gl the OpenGL interface
     * @param glResourceManager the OpenGL resource manager
     */
    protected abstract void initDraw(@NotNull GL3 gl, @NotNull GlResourceManager glResourceManager);

    /**
     * Update the draw.
     * This method is called if the widget has been flagged as dirty
     * @param gl the OpenGL interface
     */
    protected abstract void updateDraw(@NotNull GL3 gl);

    /**
     * Draw the background of the pane.
     * @param gl the OpenGL interface
     * @param pmvMatrix the PMV Matrix
     */
    protected abstract void doDraw(@NotNull GL3 gl, @NotNull PmvMatrix pmvMatrix);

    @Override
    public final void setPosition(@NotNull Vec2i position) {
        this.position = position;
    }

    @NotNull
    @Override
    public final Vec2i getPosition() {
        return position;
    }

    @Override
    public final void setSize(@NotNull Vec2i size) {
        this.size = size;
        updateLayout();
    }

    @NotNull
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
     * Add a widget.
     * @param widget the widget to add
     */
    protected final void addWidget(@NotNull IWidget widget) {
        assert !widgets.contains(widget) : "Trying to add widget that is already added";
        widgets.add(widget);
        mouseHandler.addWidget(widget);
        synchronized (removedWidgets) {
            removedWidgets.remove(widget);
        }
    }

    /**
     * Remove a widget.
     * @param widget the widget to remove
     */
    protected final void removeWidget(@NotNull IWidget widget) {
        assert widgets.contains(widget) : "Trying to remove widget that is not added";
        mouseHandler.removeWidget(widget);
        widgets.remove(widget);
        synchronized (removedWidgets) {
            assert !removedWidgets.contains(widget) : "The widget should not already be in the removed widgets";
            removedWidgets.add(widget);
        }
    }

    @NotNull
    @Override
    public final Set<IWidget> getWidgets() {
        return widgets;
    }

    @NotNull
    @Override
    public final IMouseHandler getMouseHandler() {
        return mouseHandler;
    }

    @NotNull
    @Override
    public final IKeyHandler getKeyHandler() {
        return keyHandler;
    }

    @Override
    public final boolean contains(@NotNull Vec2i pos) {
        return (pos.getX() >= position.getX()) && (pos.getY() >= position.getY()) && (pos.getX() <= position.getX() + size.getX()) && (pos.getY() <= position.getY() + size.getY());
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
    public void remove(@NotNull GL3 gl) {
        isInitialised = false;
        isDirty = false;
        for (IWidget widget : widgets) {
            widget.remove(gl);
        }
    }
}

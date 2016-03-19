package ben.ui.widget;

import ben.ui.action.IAction;
import ben.ui.action.IActionListener;
import ben.ui.input.key.BasicKeyHandler;
import ben.ui.input.key.IKeyHandler;
import ben.ui.input.mouse.BasicMouseHandler;
import ben.ui.input.mouse.IMouseHandler;
import ben.ui.math.PmvMatrix;
import ben.ui.math.Rect;
import ben.ui.math.Vec2i;
import ben.ui.resource.IGlResourceManager;
import ben.ui.math.Vec3f;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jogamp.opengl.GL2;

/**
 * # Abstract Widget
 */
public abstract class AbstractWidget implements IWidget {

    /**
     * The mouse handler for the widget.
     */
    @Nonnull
    private final BasicMouseHandler mouseHandler = new BasicMouseHandler();

    /**
     * The Key Handler for the widget.
     */
    @Nonnull
    private final BasicKeyHandler keyHandler = new BasicKeyHandler();

    /**
     * The action listener for the widget.
     *
     * Updates the enabled state of the widget when the action changes.
     */
    @Nonnull
    private final ActionListener actionListener = new ActionListener();

    /**
     * The name of the widget.
     */
    @Nullable
    private final String name;

    /**
     * The position of the widget with respect to its parent.
     */
    @Nonnull
    private Vec2i position = new Vec2i(0, 0);

    /**
     * The size of the widget.
     */
    @Nonnull
    private Vec2i size = new Vec2i(0, 0);

    /**
     * Is the drawing of the widget initialised?
     *
     * Used to build VAOs and stuff.
     */
    private boolean isInitialised = false;

    /**
     * Is the widget dirty?
     *
     * If true, the updateDraw method will be called before the next doDraw.
     */
    private boolean isDirty = false;

    /**
     * Is the widget visible?
     *
     * initDraw, doDraw and updateDraw will not be called if it's not visible.
     */
    private boolean isVisible = true;

    /**
     * The action associated to this widget.
     *
     * It's up to the implementing sub class to execute it!
     */
    @Nullable
    private IAction action;

    /**
     * True if the widget is enabled.
     *
     * This is tied to the action.
     */
    private boolean enabled = true;

    /**
     * True if the widget is focused.
     */
    private boolean focused;

    /**
     * Constructor.
     *
     * @param name the name of the widget
     */
    protected AbstractWidget(@Nullable String name) {
        this.name = name;
    }

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
//        assert size.getX() >= 0 : "AbstractCanvas size must not be negative";
//        assert size.getY() >= 0 : "AbstractCanvas size must not be negative";
        this.size = size;
        setDirty();
    }

    @Nonnull
    @Override
    public final Vec2i getSize() {
        return size;
    }

    @Nullable
    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final void draw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix, @Nonnull IGlResourceManager glResourceManager) {
        preDraw();
        if (isVisible) {
            if (!isInitialised) {
                initDraw(gl, glResourceManager);
                isInitialised = true;
            }
            else if (isDirty) {
                updateDraw(gl);
                isDirty = false;
            }
            pmvMatrix.push();
            pmvMatrix.translate(new Vec3f(position.getX(), position.getY(), 0));
            doDraw(gl, pmvMatrix);
            pmvMatrix.pop();
        }
    }

    /**
     * Pre Draw.
     *
     * Calculations that need to be done every frame should be put in here.
     * Called even if the widget is not drawn.
     */
    protected void preDraw() { }

    /**
     * Initialise Draw.
     *
     * Called once, before the first time the widget is drawn.
     *
     * @param gl the OpenGL interface
     * @param glResourceManager the OpenGL Resource Manager
     */
    protected abstract void initDraw(@Nonnull GL2 gl, @Nonnull IGlResourceManager glResourceManager);

    /**
     * Update the draw.
     *
     * Called before doDraw if the widget is flagged as dirty.
     *
     * @param gl the OpenGL interface
     */
    protected abstract void updateDraw(@Nonnull GL2 gl);

    /**
     * Do the draw.
     *
     * @param gl the OpenGL interface
     * @param pmvMatrix the Projection Model View Matrix
     */
    protected abstract void doDraw(@Nonnull GL2 gl, @Nonnull PmvMatrix pmvMatrix);

    /**
     * Flag the widget as dirty so that it is updated before the next time it's drawn.
     */
    protected final void setDirty() {
        isDirty = true;
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

    @Override
    public final boolean contains(@Nonnull Vec2i pos) {
        return new Rect(position, size).contains(pos);
    }

    @Override
    public final void setFocused(boolean focused) {
        if (this.focused != focused && enabled) {
            this.focused = focused;
            setDirty();
        }
    }

    /**
     * Is the widget focused.
     *
     * @return true if the widget is focused
     */
    public final boolean isFocused() {
        return focused;
    }

    /**
     * Set the visible state of the widget.
     *
     * @param isVisible true if it's visible
     */
    public final void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public final boolean isVisible() {
        return isVisible;
    }

    /**
     * Set the action of the widget, null if it has no action.
     *
     * @param action the action
     */
    public final void setAction(@Nullable IAction action) {
        if (action != this.action) {
            if (this.action != null) {
                this.action.removeListener(actionListener);
            }
            this.action = action;
            if (this.action != null) {
                this.action.addListener(actionListener);
                setEnabled(this.action.isExecutable());
            }
            else {
                setEnabled(true);
            }
        }
    }

    /**
     * Get the action that is installed on this widget.
     *
     * @return the action
     */
    @Nullable
    protected final IAction getAction() {
        return action;
    }

    /**
     * Set if the widget is enabled.
     *
     * Warning: don't use if the widget has an action
     *
     * @param enabled true if it's enabled
     */
    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            keyHandler.setEnabled(enabled);
            if (!enabled) {
                setFocused(false);
            }
        }
    }

    /**
     * Is the widget enabled.
     *
     * @return true if it's enabled
     */
    protected final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void remove(@Nonnull GL2 gl) {
        preRemove(gl);
        isInitialised = false;
        isDirty = false;
    }

    /**
     * The pane is about to be removed.
     *
     * @param gl the OpenGL interface
     */
    protected void preRemove(@Nonnull GL2 gl) { }

    /**
     * # Action Listener
     *
     * The action listener that is installed on the current action.
     */
    private class ActionListener implements IActionListener {

        @Override
        public void actionChanged() {
            assert action != null : "Listener can't be fired if action is null";
            setEnabled(action.isExecutable());
        }
    }
}
